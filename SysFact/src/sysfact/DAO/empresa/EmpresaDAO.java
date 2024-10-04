/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.dao.empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sysfact.model.empresa.Empresa;
import sysfact.model.empresa.conta.ContaBancaria;
import sysfact.model.empresa.contacto.ContactoEmpresa;
import sysfact.model.empresa.endereco.EnderecoEmpresa;
import sysfact.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class EmpresaDAO implements EmpresaDAOImpl<Empresa> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public EmpresaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public void addEntity(Empresa c) {
        try {
            sql = "insert into empresa (nome,apelido,nuit,) value (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getNome());
            ps.setString(2, c.getApelido());
            ps.setInt(3, c.getNuit());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao adicionar empresa: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateEntityByID(Empresa c, int id) {
        try {
            sql = "update empresa set nome=? ,apelido=? ,nuit=?"
                    + " where id=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getNome());
            ps.setString(2, c.getApelido());
            ps.setInt(3, c.getNuit());
            ps.setInt(4, id);

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao actualizar empresa: "
                    + e, "Informação", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Empresa getEmpresaData() {
        Empresa empresa = null;
        try {
            sql = "SELECT e.id, e.nome, e.apelido AS razao, e.nuit, "
                    + "en.provincia, en.cidade, en.bairro, en.avenida, en.rua, en.codigo_postal, en.numero, en.tipo,"
                    + "GROUP_CONCAT(DISTINCT c.contacto SEPARATOR ' ') AS contatos, "
                    + "c.email, c.website,"
                    + "a.nome_banco, a.numero as numeroConta, a.nib, a.nuib "
                    + "FROM empresa e "
                    + "LEFT JOIN endereco_empresa en ON e.id = en.fk_id_empresa "
                    + "LEFT JOIN contacto_empresa c ON e.id = c.fk_id_empresa "
                    + "LEFT JOIN contabancaria a ON e.id=a.fk_id_empresa";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                empresa = new Empresa();
                empresa.setId(result.getInt("id"));
                empresa.setApelido(result.getString("razao"));
                empresa.setNome(result.getString("nome"));
                empresa.setNuit(result.getInt("nuit"));
                EnderecoEmpresa e = new EnderecoEmpresa();
                e.setProvincia(result.getString("provincia"));
                e.setCidade(result.getString("cidade"));
                e.setBairro(result.getString("bairro"));
                e.setAvenida(result.getString("avenida"));
                e.setRua(result.getString("rua"));
                e.setCodigoPostal(result.getInt("codigo_postal"));
                e.setNumero(result.getInt("numero"));
                empresa.setEnderecoEmpresa(e);
                ContactoEmpresa ce = new ContactoEmpresa();
//                ce.setContacto(result.getString("contacto"));
                ce.setEmail(result.getString("email"));
                ce.setWebsite(result.getString("website"));
                empresa.setContactoEmpresa(ce);
                
                List<ContactoEmpresa> listaContactos = new ArrayList<>();
                String contactos = result.getString("contatos");
                if (contactos != null && !contactos.trim().isEmpty()) {
                    for (String split : contactos.split(" ")) {
                        ContactoEmpresa cc = new ContactoEmpresa();
                        cc.setContacto(split);
                        listaContactos.add(cc);
                    }
                }
                empresa.setContactosEmpresa(listaContactos);
                
                ContaBancaria conta = new ContaBancaria();
                conta.setNome_banco(result.getString("nome_banco"));
                conta.setNib(result.getString("nib"));
                conta.setNuib(result.getString("nuib"));
                conta.setNumero(result.getString("numeroConta"));
                empresa.setContaBancaria(conta);

            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha ao Bucar empresa: " + e, "Informação", JOptionPane.ERROR_MESSAGE);

        }
        return empresa;
    }

}
