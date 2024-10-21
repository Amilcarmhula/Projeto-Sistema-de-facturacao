use _sysface;
CALL atualizar_estado_precovenda();

select u.nome, u.email, u.usuario,u.senha,u.estado,p.tipo,n.descricao from usuario u
LEFT JOIN nivel_acesso n ON u.fk_nivel_acesso=n.id
LEFT JOIN permissao p ON u.fk_permissao=p.id
where usuario='root' and senha='senha123' and estado=1;
                    

select p.nome as Produto, a.nome as Armazem, s.saldo
	from stock s 
	LEFT JOIN produto p ON p.id=s.fk_id_produto
	LEFT JOIN armazem a ON a.id=s.fk_id_armazem
    where s.saldo<=p.nivelstock;



    
    

  
     

                    
                    
                     

                     
                    
                    
                    
                    

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
        
     
     

