use _sysface;



DELIMITER //
CREATE PROCEDURE EnviarTransferencia(p_transferencia INT)
BEGIN

DECLARE id_produto INTEGER;
DECLARE id_armazem INTEGER;
DECLARE qtd INTEGER;
DECLARE saldo_stock INTEGER;

-- SAÍDA DO ARMAZÉM ORIGEM
INSERT INTO movimento(
    tipoOrigem,
    tipoMovimento,
    dataMovimento,
    quantidade,
    idArmazem,
    idProduto
)
SELECT
    'Transferencia',
    'Saida',
    CURDATE(),
    quantidade,
    t.idArmazemOrigem,
    i.idProduto
FROM item_transferencia i
JOIN transferencia_stock t ON t.id=i.idTransferencia
WHERE i.idTransferencia=p_transferencia;

-- cria stock em trânsito
INSERT INTO stock_transito(
    idTransferencia,
    idProduto,
    quantidade,
    estado
)
SELECT
    idTransferencia,
    idProduto,
    quantidade,
    'Em Transito'
FROM item_transferencia
WHERE idTransferencia=p_transferencia;

UPDATE transferencia_stock
SET estado='Em Transito'
WHERE id=p_transferencia;

	SELECT t.idArmazemOrigem
    INTO id_armazem
    FROM transferencia_stock t
    WHERE t.id = p_transferencia;
    
    SELECT it.quantidade
    INTO qtd
    FROM item_transferencia it
    WHERE it.idTransferencia = p_transferencia;
    
    SELECT it.idProduto
    INTO id_produto
    FROM item_transferencia it
    WHERE it.idTransferencia = p_transferencia;
    
    SELECT s.saldo into saldo_stock
    from stock s
    where s.idProduto=id_produto  and s.idArmazem=id_armazem;
    
UPDATE stock SET saldo = saldo_stock - qtd 
WHERE stock.idProduto = id_produto
AND stock.idArmazem = id_armazem;

END//
DELIMITER ;

delimiter //
CREATE PROCEDURE ReceberTransferencia(p_transferencia INT)
BEGIN

INSERT INTO movimento(
    tipoOrigem,
    tipoMovimento,
    dataMovimento,
    quantidade,
    idArmazem,
    idProduto
)
SELECT
    'Transferencia',
    'Entrada',
    CURDATE(),
    st.quantidade,
    t.idArmazemDestino,
    st.idProduto
FROM stock_transito st
JOIN transferencia_stock t ON t.id=st.idTransferencia
WHERE st.idTransferencia=p_transferencia;

UPDATE stock_transito
SET estado='Recebido'
WHERE idTransferencia=p_transferencia;

UPDATE transferencia_stock
SET estado='Recebido'
WHERE id=p_transferencia;

end//
delimiter ;



DELIMITER //
CREATE TRIGGER Tg_reservaStock
AFTER INSERT ON reserva_stock
FOR EACH ROW
BEGIN

UPDATE stock
SET reservado = reservado + NEW.quantidade
WHERE idProduto = NEW.idProduto
AND idArmazem = NEW.idArmazem;

END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_estadoRequisicao
AFTER INSERT ON itemRequisicao
FOR EACH ROW
BEGIN
    UPDATE requisicao
    SET estado = 'Em processamento'
    WHERE id = NEW.idRequisicao
      AND estado = 'Pendente';
END//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE AprovarRequisicao(p_id INT)
BEGIN

INSERT INTO reserva_stock(
    idProduto,
    idArmazem,
    idRequisicao,
    quantidade
)
SELECT
    idProduto,
    idArmazem,
    idRequisicao,
    quantidade
FROM itemRequisicao
WHERE idRequisicao = p_id;

UPDATE requisicao
SET estado='Aprovada'
WHERE id=p_id;

INSERT INTO picking(tipo,estado,idRequisicao)
VALUES('Requisicao','Aguardando',p_id);

END//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE ConfirmarPicking(p_requisicao INT)
BEGIN

DECLARE id_produto INTEGER;
DECLARE id_armazem INTEGER;
DECLARE qtd INTEGER;
DECLARE saldo_stock INTEGER;

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
WHERE idRequisicao=p_requisicao;

UPDATE reserva_stock
SET estado='Consumido'
WHERE idRequisicao=p_requisicao;

UPDATE requisicao
SET estado='Finalizada'
WHERE id=p_requisicao;

    
     UPDATE picking SET estado = 'Confirmado'
     WHERE picking.idRequisicao=p_requisicao;
END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_registaMovimentoEntrada
AFTER INSERT ON lote
FOR EACH ROW
BEGIN
	-- Registra movimento de entrada de lote
	INSERT INTO movimento (tipoMovimento, dataMovimento, quantidade,idArmazem,idProduto,idLote) 
		VALUES ('Entrada', NEW.dataEntrada, NEW.quantidadeAtual,NEW.idArmazem, NEW.idProduto, NEW.id);
END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Tg_actualizaStock_IO
AFTER INSERT ON movimento
FOR EACH ROW
BEGIN
    DECLARE totalMovimentado INT;
    DECLARE estoque_atual INT;

    SET totalMovimentado = NEW.quantidade;

    SELECT saldo INTO estoque_atual
    FROM stock
    WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem
    LIMIT 1;

    -- Apenas ENTRADAS
    IF NEW.tipoMovimento = 'Entrada' THEN
        UPDATE stock
        SET saldo = estoque_atual + totalMovimentado
        WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem;
    END IF;
END//
DELIMITER ;







DELIMITER //
CREATE TRIGGER Tg_FIFO
AFTER INSERT ON movimento
FOR EACH ROW
BEGIN
    DECLARE qtdRestante INT;
    DECLARE loteID INT;
    DECLARE qtdLote INT;
    DECLARE done INT DEFAULT 0;

    DECLARE curLotes CURSOR FOR
        SELECT id, quantidadeAtual
        FROM lote
        WHERE idProduto = NEW.idProduto
          AND idArmazem = NEW.idArmazem
          AND quantidadeAtual > 0
        ORDER BY dataEntrada ASC;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    IF NEW.tipoOrigem = 'Requisicao' AND NEW.tipoMovimento = 'Saida' THEN
        SET qtdRestante = NEW.quantidade;

        OPEN curLotes;

        read_loop: LOOP
            FETCH curLotes INTO loteID, qtdLote;
            IF done = 1 OR qtdRestante <= 0 THEN
                LEAVE read_loop;
            END IF;

            IF qtdLote >= qtdRestante THEN
                -- Retira o restante do lote atual
                UPDATE lote
                SET quantidadeAtual = quantidadeAtual - qtdRestante,
                    estado = CASE WHEN quantidadeAtual - qtdRestante = 0 THEN 'Esgotado' ELSE estado END
                WHERE id = loteID;

                -- Registra no log
                INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
                VALUES (NEW.id, loteID, qtdRestante);

                SET qtdRestante = 0;
            ELSE
                -- Lote esgotado completamente
                UPDATE lote
                SET quantidadeAtual = 0,
                    estado = 'Esgotado'
                WHERE id = loteID;

                INSERT INTO movimento_lote (idMovimento, idLote, quantidadeRetirada)
                VALUES (NEW.id, loteID, qtdLote);

                SET qtdRestante = qtdRestante - qtdLote;
            END IF;
        END LOOP;

        CLOSE curLotes;

        -- Atualiza saldo geral no stock
         UPDATE stock
          SET saldo = saldo - NEW.quantidade
          WHERE idProduto = NEW.idProduto AND idArmazem = NEW.idArmazem;

        IF qtdRestante > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Stock insuficiente para completar a saída (FIFO).';
        END IF;
    END IF;
END//
DELIMITER ;



-- INSERT INTO fornecedor(tipo, razaosocial, nuit) VALUES
-- ('Jurídica', 'AgroComercial Lda', 123456789),
-- ('Jurídica', 'Distribuidora Zambezia', 987654321);

-- INSERT INTO endereco_fornecedor(provincia, cidade, bairro, avenida, rua, numero, idFornecedor) VALUES
-- ('Zambézia', 'Quelimane', 'Centro', 'Av. 25 de Setembro', 'Rua A', 10, 1),
-- ('Zambézia', 'Nicoadala', 'Central', 'Av. Principal', 'Rua B', 20, 2);

-- INSERT INTO contacto_fornecedor(pessoa_de_contacto, contacto, email, website, idFornecedor) VALUES
-- ('João Paulo', '841111111', 'agro@lda.co.mz', 'www.agrolda.co.mz', 1),
-- ('Maria João', '842222222', 'dist@zambezia.co.mz', 'www.distz.co.mz', 2);
--                      

-- INSERT INTO armazem(nome, tipo, descricao) VALUES
-- ('Armazém Central', 'Central', 'Principal'),
-- ('Loja Quelimane', 'Loja', 'Venda ao público');

-- INSERT INTO categoria(grupo, subgrupo, familia, descricao) VALUES
-- ('Alimentos', 'Grãos', 'Arroz', 'Produtos alimentares'),
-- ('Bebidas', 'Refrigerantes', 'Soda', 'Bebidas gaseificadas');

-- INSERT INTO unidadeMedida(nome, sigla, descricao) VALUES
-- ('Kilograma', 'kg', 'Peso'),
-- ('Litro', 'L', 'Volume');


-- INSERT INTO imposto(nome, percentagem, descricao) VALUES
-- ('IVA', 17.000, 'Imposto padrão'),
-- ('Isento', 0.000, 'Sem imposto');

-- INSERT INTO produto(
-- nome, codigo_barra, tipoProduto, unidadePorTipo,
-- descricao, controle_stock, idCategoria, idUnidade, idImposto
-- ) VALUES
-- ('Arroz 50kg', 'P001', 'Simples', 1, 'Arroz branco', 1, 1, 1, 1),
-- ('Refrigerante Cola', 'P002', 'Simples', 1, 'Soda cola 2L', 1, 2, 2, 1);

-- -- Depois do insert de produtos, o trigger cria:
-- -- ✔ stock para cada armazém
-- -- ✔ saldo = 0
-- -- ---------------------------------------------------------
-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L001', 1, 1, 1, CURDATE(), 100, 25.00);

-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L002', 1, 1, 1, CURDATE(), 50, 26.00);

-- INSERT INTO lote(
-- codigo, idProduto, idArmazem, idFornecedor,
-- dataEntrada, quantidadeAtual, custoUnitario
-- ) VALUES
-- ('L003', 2, 1, 2, CURDATE(), 200, 15.00);

-- -- ---------------------------------------------------------

-- INSERT INTO requisicao(dataNecessidade, motivo)
-- VALUES (DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'Reposição loja');
-- select * from requisicao;

-- INSERT INTO itemRequisicao(quantidade, idRequisicao, idProduto, idArmazem)
-- VALUES
-- (60, 1, 1, 1),
-- (30, 1, 2, 1);
-- select * from itemRequisicao;
-- CALL AprovarRequisicao(1);



-- -- esperado:
-- -- reserva_stock criada
-- select * from reserva_stock;
-- -- stock.reservado atualizado
-- select * from stock;
-- -- picking criado
-- select * from picking;

-- CALL ConfirmarPicking(1);
-- -- esperado:
-- -- movimento SAIDA criado
-- -- FIFO executa:
-- -- L001 reduz primeiro
-- -- depois L002
-- -- stock atualizado corretamente
-- -- ---------------------------------------------------------



-- INSERT INTO transferencia_stock(idArmazemOrigem, idArmazemDestino, estado)
-- VALUES (1, 2, 'Pendente');
-- select * from item_transferencia;
-- INSERT INTO item_transferencia(idTransferencia, idProduto, quantidade)
-- VALUES (1, 1, 1);

-- CALL EnviarTransferencia(1);
-- --    
-- -- esperado:
-- -- movimento saída
-- select * from movimento;
-- -- stock_transito criado
-- select * from stock_transito;
-- -- stock reduzido via FIFO
-- select * from stock;

-- CALL ReceberTransferencia(1);
-- -- esperado:
-- -- movimento entrada
-- select * from movimento;
-- -- stock aumenta no destino
-- select * from stock;
-- -- estado atualizado
-- select * from transferencia_stock;
-- select * from stock_transito;
-- -- ---------------------------------------------------------
-- SELECT SUM(quantidadeAtual) FROM lote WHERE idProduto = 1;
-- SELECT saldo FROM stock WHERE idProduto = 1;
-- -- ✔ devem ser IGUAIS

-- SELECT * FROM movimento_lote;
-- -- ✔ deve mostrar distribuição entre lotes

-- SELECT * FROM stock;
-- -- ✔ reservado nunca pode ultrapassar saldo

