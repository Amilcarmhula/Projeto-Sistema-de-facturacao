USE _sysface_teste;

DROP TRIGGER IF EXISTS Tg_C_Saida;

DELIMITER //
CREATE TRIGGER Tg_C_Saida
AFTER INSERT ON itemfactura
FOR EACH ROW
BEGIN
    DECLARE qtd_restante INT;
    DECLARE qtd_total INT;
    DECLARE lote_id INT DEFAULT 0;
    DECLARE movimento_id INT;
    DECLARE qtd_lote INT;
    DECLARE qtd_saida INT;
    DECLARE saldo_atual INT DEFAULT 0;

    SET qtd_total = NEW.quantidade * NEW.qtd_por_unidade;
    SET qtd_restante = qtd_total;

    SELECT COALESCE((
        SELECT saldo
        FROM stock
        WHERE idProduto = NEW.fk_id_produto
          AND idArmazem = NEW.fk_id_armazem
        LIMIT 1
    ), 0)
    INTO saldo_atual;

    IF saldo_atual < qtd_total THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente para concluir a venda';
    END IF;

    WHILE qtd_restante > 0 DO
        SET lote_id = 0;
        SET qtd_lote = 0;

        SELECT
            COALESCE((
                SELECT id
                FROM lote
                WHERE idProduto = NEW.fk_id_produto
                  AND idArmazem = NEW.fk_id_armazem
                  AND estado = 'Ativo'
                  AND quantidadeAtual > 0
                ORDER BY dataEntrada, id
                LIMIT 1
            ), 0),
            COALESCE((
                SELECT quantidadeAtual
                FROM lote
                WHERE idProduto = NEW.fk_id_produto
                  AND idArmazem = NEW.fk_id_armazem
                  AND estado = 'Ativo'
                  AND quantidadeAtual > 0
                ORDER BY dataEntrada, id
                LIMIT 1
            ), 0)
        INTO lote_id, qtd_lote;

        IF lote_id = 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Lote insuficiente para concluir a venda';
        END IF;

        SET qtd_saida = LEAST(qtd_lote, qtd_restante);

        UPDATE lote
        SET quantidadeAtual = quantidadeAtual - qtd_saida,
            estado = IF(quantidadeAtual - qtd_saida = 0, 'Esgotado', 'Ativo')
        WHERE id = lote_id;

        INSERT INTO movimento (
            tipoOrigem,
            tipoMovimento,
            dataMovimento,
            quantidade,
            idProduto,
            idArmazem,
            idLote
        ) VALUES (
            'Venda',
            'Saida',
            DATE(NEW.data_adicao),
            qtd_saida,
            NEW.fk_id_produto,
            NEW.fk_id_armazem,
            lote_id
        );

        SET movimento_id = LAST_INSERT_ID();

        INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
        VALUES (movimento_id, lote_id, qtd_saida);

        SET qtd_restante = qtd_restante - qtd_saida;
    END WHILE;

    UPDATE stock
    SET saldo = saldo - qtd_total
    WHERE idProduto = NEW.fk_id_produto
      AND idArmazem = NEW.fk_id_armazem;
END//
DELIMITER ;
