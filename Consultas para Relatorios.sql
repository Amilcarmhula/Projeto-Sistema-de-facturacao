use _sysface;
CALL atualizar_estado_precovenda();
select count(*) from itemfactura;
select * from factura;

                    
                    


-- Conta produtos em funcao da categoria
select count(*)
	from produto p
    LEFT JOIN categoria c ON c.id=p.fk_id_categoria
    where (NULL IS NULL OR c.id = NULL);

-- Conta stock em funcao da categoria ou armazem
select sum(s.saldo) as total_stock
	from stock s
    LEFT JOIN armazem a ON a.id=s.fk_id_armazem
    LEFT JOIN produto p ON p.id=s.fk_id_produto
    LEFT JOIN categoria c ON c.id=p.fk_id_categoria
    where (NULL IS NULL OR a.id=NULL) and (NULL IS NULL OR c.id=NULL);


    
    

  
     

                    
                    
                     

                     
                    
                    
                    
                    

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

