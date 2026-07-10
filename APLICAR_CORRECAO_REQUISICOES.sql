USE _sysface_teste;

DROP PROCEDURE IF EXISTS ConfirmarPicking;
DROP PROCEDURE IF EXISTS AprovarRequisicao;
DROP TRIGGER IF EXISTS Tg_reservaStock_estado;
DROP TRIGGER IF EXISTS Tg_reservaStock;
DROP TRIGGER IF EXISTS Tg_reservaStock_validar;
DROP TRIGGER IF EXISTS Tg_estadoRequisicao;
DROP TRIGGER IF EXISTS Tg_registaMovimentoEntrada;

DELIMITER //
CREATE TRIGGER Tg_reservaStock_validar
BEFORE INSERT ON reserva_stock
FOR EACH ROW
BEGIN
    DECLARE disponivel_atual INT DEFAULT 0;

    SELECT COALESCE((
        SELECT saldo - reservado
        FROM stock
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem
        LIMIT 1
    ), 0)
    INTO disponivel_atual;

    IF NEW.quantidade <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Quantidade reservada deve ser maior que zero';
    END IF;

    IF disponivel_atual < NEW.quantidade THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock disponivel insuficiente para reservar requisicao';
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Tg_reservaStock
AFTER INSERT ON reserva_stock
FOR EACH ROW
BEGIN
    IF NEW.estado = 'Reservado' THEN
        UPDATE stock
        SET reservado = reservado + NEW.quantidade
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Tg_reservaStock_estado
AFTER UPDATE ON reserva_stock
FOR EACH ROW
BEGIN
    IF OLD.estado = 'Reservado' AND NEW.estado IN ('Consumido', 'Liberado', 'Cancelado') THEN
        UPDATE stock
        SET reservado = GREATEST(reservado - OLD.quantidade, 0)
        WHERE idProduto = OLD.idProduto
          AND idArmazem = OLD.idArmazem;
    ELSEIF OLD.estado IN ('Consumido', 'Liberado', 'Cancelado') AND NEW.estado = 'Reservado' THEN
        UPDATE stock
        SET reservado = reservado + NEW.quantidade
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Tg_estadoRequisicao
AFTER INSERT ON itemRequisicao
FOR EACH ROW
BEGIN
    UPDATE requisicao
    SET estado = 'Submetida'
    WHERE id = NEW.idRequisicao
      AND estado = 'Pendente';
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE AprovarRequisicao(p_id INT)
BEGIN
    DECLARE v_estado VARCHAR(30);
    DECLARE v_total_itens INT DEFAULT 0;
    DECLARE v_reservas_ativas INT DEFAULT 0;
    DECLARE v_indisponiveis INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado
    INTO v_estado
    FROM requisicao
    WHERE id = p_id
    LIMIT 1;

    IF v_estado IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao inexistente';
    END IF;

    IF v_estado NOT IN ('Submetida', 'AguardandoAprovacao', 'Pendente') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao esta em estado aprovavel';
    END IF;

    SELECT COUNT(*)
    INTO v_total_itens
    FROM itemRequisicao
    WHERE idRequisicao = p_id;

    IF v_total_itens = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao possui itens';
    END IF;

    SELECT COUNT(*)
    INTO v_reservas_ativas
    FROM reserva_stock
    WHERE idRequisicao = p_id
      AND estado = 'Reservado';

    IF v_reservas_ativas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao ja possui reserva ativa';
    END IF;

    SELECT COUNT(*)
    INTO v_indisponiveis
    FROM (
        SELECT
            ir.idProduto,
            ir.idArmazem,
            SUM(ir.quantidade) AS quantidade_solicitada,
            COALESCE(MAX(s.saldo - s.reservado), 0) AS quantidade_disponivel
        FROM itemRequisicao ir
        LEFT JOIN stock s
          ON s.idProduto = ir.idProduto
         AND s.idArmazem = ir.idArmazem
        WHERE ir.idRequisicao = p_id
        GROUP BY ir.idProduto, ir.idArmazem
        HAVING quantidade_disponivel < quantidade_solicitada
    ) indisponiveis;

    IF v_indisponiveis > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock disponivel insuficiente para aprovar requisicao';
    END IF;

    INSERT INTO reserva_stock(
        idProduto,
        idArmazem,
        idRequisicao,
        quantidade,
        estado
    )
    SELECT
        idProduto,
        idArmazem,
        idRequisicao,
        SUM(quantidade),
        'Reservado'
    FROM itemRequisicao
    WHERE idRequisicao = p_id
    GROUP BY idProduto, idArmazem, idRequisicao;

    UPDATE requisicao
    SET estado = 'Aprovada'
    WHERE id = p_id;

    INSERT INTO picking(tipo, estado, idRequisicao, dataInicio)
    SELECT 'Requisicao', 'Aguardando', p_id, NOW()
    WHERE NOT EXISTS (
        SELECT 1 FROM picking WHERE idRequisicao = p_id
    );

    COMMIT;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE ConfirmarPicking(p_requisicao INT)
BEGIN
    DECLARE v_estado VARCHAR(30);
    DECLARE v_reservas_ativas INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT estado
    INTO v_estado
    FROM requisicao
    WHERE id = p_requisicao
    LIMIT 1;

    IF v_estado <> 'Aprovada' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas requisicoes aprovadas podem confirmar picking';
    END IF;

    SELECT COUNT(*)
    INTO v_reservas_ativas
    FROM reserva_stock
    WHERE idRequisicao = p_requisicao
      AND estado = 'Reservado';

    IF v_reservas_ativas = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Requisicao nao possui reserva ativa para consumir';
    END IF;

    INSERT INTO movimento (
        tipoOrigem,
        tipoMovimento,
        dataMovimento,
        quantidade,
        idArmazem,
        idProduto,
        idItemRequisicao
    )
    SELECT
        'Requisicao',
        'Saida',
        CURDATE(),
        quantidade,
        idArmazem,
        idProduto,
        id
    FROM itemRequisicao
    WHERE idRequisicao = p_requisicao;

    UPDATE reserva_stock
    SET estado = 'Consumido'
    WHERE idRequisicao = p_requisicao
      AND estado = 'Reservado';

    UPDATE requisicao
    SET estado = 'Finalizada'
    WHERE id = p_requisicao;

    UPDATE picking
    SET estado = 'Confirmado',
        dataFim = NOW()
    WHERE idRequisicao = p_requisicao;

    COMMIT;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Tg_registaMovimentoEntrada
AFTER INSERT ON lote
FOR EACH ROW
BEGIN
    INSERT INTO movimento (tipoOrigem, tipoMovimento, dataMovimento, quantidade, idArmazem, idProduto, idLote)
    VALUES ('Compra', 'Entrada', NEW.dataEntrada, NEW.quantidadeAtual, NEW.idArmazem, NEW.idProduto, NEW.id);
END//
DELIMITER ;
