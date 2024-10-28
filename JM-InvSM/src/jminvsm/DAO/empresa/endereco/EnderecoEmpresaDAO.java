/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.empresa.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import jminvsm.model.empresa.endereco.EnderecoEmpresa;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoEmpresaDAO implements EnderecoEmpresaDAOImpl<EnderecoEmpresa> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public EnderecoEmpresaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    @Override
    public void addEntity(EnderecoEmpresa t) {
        try {
            sql = "insert into endereco_empresa (provincia,cidade,bairro,"
                    + "avenida,rua,codigo_postal,numero, tipo, fk_id_empresa) value (?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_emp());
            ps.setString(2, t.getCidade_emp());
            ps.setString(3, t.getBairro_emp());
            ps.setString(4, t.getAvenida_emp());
            ps.setString(5, t.getRua_emp());
            ps.setInt(6, t.getCodigoPostal_emp());
            ps.setInt(7, t.getNumero_emp());
            ps.setString(8, t.getTipo_emp());
//            ps.setInt(9, t.getFk_id_empresa());

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar Endereco da Empresa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void updateEntityByID(EnderecoEmpresa t, int id) {
        try {
            sql = "update endereco_empresa set provincia=?,cidade=?,bairro=?,"
                    + "avenida=?,rua=?,codigo_postal=?,numero=?, tipo=?, fk_id_empresa=? where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getProvincia_emp());
            ps.setString(2, t.getCidade_emp());
            ps.setString(3, t.getBairro_emp());
            ps.setString(4, t.getAvenida_emp());
            ps.setString(5, t.getRua_emp());
            ps.setInt(6, t.getCodigoPostal_emp());
            ps.setInt(7, t.getNumero_emp());
            ps.setString(8, t.getTipo_emp());
//            ps.setInt(9, t.getFk_id_empresa());
            ps.setInt(10, id);

            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao actulizar Endereco da Empresa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<EnderecoEmpresa> listAllEntities() {
        List<EnderecoEmpresa> lista = new ArrayList<>();
        try {
            sql = "select * from endereco_empresa";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                EnderecoEmpresa c = new EnderecoEmpresa();
                c.setId(result.getInt("id"));
                c.setProvincia_emp(result.getString("provincia"));
                c.setCidade_emp(result.getString("cidade"));
                c.setBairro_emp(result.getString("bairro"));
                c.setAvenida_emp(result.getString("avenida"));
                c.setRua_emp(result.getString("rua"));
                c.setCodigoPostal_emp(result.getInt("codigo_postal"));
                c.setNumero_emp(result.getInt("numero"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setTipo_emp(result.getString("tipo"));
//                c.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                c.setActive_or_not(result.getBoolean("active_or_not"));
                lista.add(c);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Endereco da Empresa: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    @Override
    public EnderecoEmpresa searchEntityByName(String contact) {
        EnderecoEmpresa c = null;
        try {
            sql = "select * from endereco_empresa where bairro like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + contact + "%");
            ResultSet result = ps.executeQuery();
//            if (result.next()) {
            while (result.next()) {
                c = new EnderecoEmpresa();
                c.setId(result.getInt("id"));
                c.setProvincia_emp(result.getString("provincia"));
                c.setCidade_emp(result.getString("cidade"));
                c.setBairro_emp(result.getString("bairro"));
                c.setAvenida_emp(result.getString("avenida"));
                c.setRua_emp(result.getString("rua"));
                c.setCodigoPostal_emp(result.getInt("codigo_postal"));
                c.setNumero_emp(result.getInt("numero"));
                c.setData_criacao(result.getString("data_criacao"));
                c.setTipo_emp(result.getString("tipo"));
//                c.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                c.setActive_or_not(result.getBoolean("active_or_not"));
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Endereco da Empresa: " + ex,
                    "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return c;

    }

    @Override
    public void deleteEntityByROOT(int id) {
        try {
            sql = "delete from endereco_empresa where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Endereco da Empresa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteEntityByUSER(int id) {
        try {
            sql = "update endereco_empresa set active_or_not=false where id=?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Endereco da Empresa: "
                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
