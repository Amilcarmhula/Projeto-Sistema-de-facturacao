/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.empresa.contacto.ContactoEmpresa;
import jminvsm.model.empresa.endereco.EnderecoEmpresa;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class EmpresaDAO implements EmpresaDAOImpl<Empresa> {

    @Override
    public void addEntity(Empresa c) {
        String sql = "insert into empresa (nome,razaosocial,nuit) value (?,?,?)";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome_emp());
            ps.setString(2, c.getRazao_emp());
            ps.setInt(3, c.getNuit_emp());
            ps.executeUpdate();
        } catch (SQLException e) {
            DaoErrorHandler.log("Erro Empresa", "Falha ao adicionar empresa: " + e);
        }
    }

    @Override
    public void updateEntityByID(Empresa c, int id) {
        String sql = "update empresa set nome=? ,razaosocial=? ,nuit=?"
                + " where id=?";
        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNome_emp());
            ps.setString(2, c.getRazao_emp());
            ps.setInt(3, c.getNuit_emp());
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            DaoErrorHandler.log("Erro Empresa", "Falha ao adicionar empresa: " + e);
        }
    }

    @Override
    public Empresa getEmpresaData() {
        Empresa empresa = null;
        String sql = "SELECT e.id, e.nome, e.razaosocial AS razao, e.nuit, "
                + "en.provincia, en.cidade, en.bairro, en.avenida, en.rua, en.codigo_postal, en.numero, en.tipo,"
                + "GROUP_CONCAT(DISTINCT c.contacto SEPARATOR ' ') AS contatos, "
                + "c.email, c.website,"
                + "GROUP_CONCAT(DISTINCT a.id,'-', a.nome_banco,'-', a.numero,'-', a.nib,'-', a.nuib  SEPARATOR ';') AS contas_bancarias "
                + "FROM empresa e "
                + "LEFT JOIN endereco_empresa en ON e.id = en.fk_id_empresa "
                + "LEFT JOIN contacto_empresa c ON e.id = c.fk_id_empresa "
                + "LEFT JOIN contabancaria a ON e.id=a.fk_id_empresa";

        try (
                 Connection con = Conexao.getConexao();  PreparedStatement ps = con.prepareStatement(sql)) {
            try ( ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    empresa = new Empresa();
                    empresa.setId(result.getInt("id"));
                    empresa.setRazao_emp(result.getString("razao"));
                    empresa.setNome_emp(result.getString("nome"));
                    empresa.setNuit_emp(result.getInt("nuit"));
                    EnderecoEmpresa e = new EnderecoEmpresa();
                    e.setProvincia_emp(result.getString("provincia"));
                    e.setCidade_emp(result.getString("cidade"));
                    e.setBairro_emp(result.getString("bairro"));
                    e.setAvenida_emp(result.getString("avenida"));
                    e.setRua_emp(result.getString("rua"));
                    e.setCodigoPostal_emp(result.getInt("codigo_postal"));
                    e.setNumero_emp(result.getInt("numero"));
                    empresa.setEnderecoEmpresa(e);
                    ContactoEmpresa ce = new ContactoEmpresa();
//                ce.setContacto(result.getString("contacto"));
                    ce.setEmail_emp(result.getString("email"));
                    ce.setWebsite_emp(result.getString("website"));
                    empresa.setContactoEmpresa(ce);

                    List<ContactoEmpresa> listaContactos = new ArrayList<>();
                    String contactos = result.getString("contatos");
                    if (contactos != null && !contactos.trim().isEmpty()) {
                        for (String split : contactos.split(" ")) {
                            ContactoEmpresa cc = new ContactoEmpresa();
                            cc.setContacto_emp(split);
                            listaContactos.add(cc);
                        }
                    }
                    empresa.setContactosEmpresa(listaContactos);
                    ContaBancaria conta;
                    List<ContaBancaria> listaContas = new ArrayList<>();
                    String contas = result.getString("contas_bancarias");
                    if (contas != null && !contas.trim().isEmpty()) {
                        String[] split_one = contas.split(";");
                            for (String split_two : split_one) {
                                conta = new ContaBancaria();
                                String[] split_three = split_two.split("-");
                                conta.setId(Integer.parseInt(split_three[0]));
                                conta.setNome_banco(split_three[1]);
                                conta.setNumero_conta(split_three[2]);
                                conta.setNib(split_three[3]);
                                conta.setNuib(split_three[4]);
                                listaContas.add(conta);
                            }
                    }
                    empresa.setContasBancarias(listaContas);

                }
            }
        } catch (SQLException e) {
            DaoErrorHandler.log("Erro Empresa", "Falha ao adicionar empresa: " + e);

        }
        return empresa;
    }

}
