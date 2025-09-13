package jminvsm.dao.imposto;

import java.sql.SQLException;
import jminvsm.dao.GenericDAO;
import jminvsm.model.imposto.Imposto;

/**
 *
 * @author JM-Tecnologias
 */
public class TesteImpostoDAO extends GenericDAO<Imposto> {

    public TesteImpostoDAO() throws SQLException {
        super(Imposto.class, "imposto", new String[]{"nome", "percentagem", 
            "descricao"}
        );
        
    }
    public static void main(String[] args) throws SQLException, Exception {
        TesteImpostoDAO dao = new TesteImpostoDAO();
        Imposto i = new Imposto();
        i.setNome("IVu");
        i.setDescricao("descricao de teste IVA");
        i.setPercentagem(0.54);
        
//        dao.save(i);
        
        for(Imposto  imp: dao.listAll()){
            System.out.println("--"+imp.getNome());
        }
    }

}
