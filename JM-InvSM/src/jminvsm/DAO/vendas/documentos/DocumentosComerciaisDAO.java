/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.vendas.documentos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import jminvsm.model.vendas.documetos.DocumentosComerciais;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class DocumentosComerciaisDAO implements DocumentosComerciaisDAOImpl<DocumentosComerciais> {
    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public DocumentosComerciaisDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }
    
    
    @Override
    public ObservableList<DocumentosComerciais> listAllEntities() {
        ObservableList<DocumentosComerciais> lista = FXCollections.observableArrayList();
        try {
            sql = "select * from documentocomercial";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                DocumentosComerciais d = new DocumentosComerciais();
                d.setId(result.getInt("id"));
                d.setSigla_doc(result.getString("sigla"));
                d.setNome_doc(result.getString("nome"));
                d.setDiasUteis(result.getInt("diasuteis"));
                d.setDescricao(result.getString("descricao"));
                lista.add(d);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Buscar Documentos: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }
}
