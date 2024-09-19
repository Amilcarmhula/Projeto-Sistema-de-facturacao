/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.precovenda;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sysfact.dao.preco.PrecoVendaDAO;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.usuario.Usuario;

/**
 *
 * @author JM-Tecnologias
 */
public class ServicePrecoVenda {

    private static final double PRECO_PADRAO = 1.0;
    private static final String DATA_FIM_PADRAO = "2099-12-31";

    private PrecoVendaDAO precoVendaDao;

    public ServicePrecoVenda(PrecoVendaDAO precoVendaDao) {
        this.precoVendaDao = precoVendaDao;
    }

    public void registraPrecoVenda(double preco, String data_inicio, 
            String data_fim, Usuario user, Produto pro) {
        
        preco = validarPreco(preco);
        data_inicio = validarDataInicio(data_inicio);
        data_fim = validarDataFim(data_fim);
        
        if (pro == null) {
            showErrorDialog("Erro de validação", "Produto não definido.");
            return;
        }
        
        if (user == null) {
            showErrorDialog("Erro de validação", "Usuário não definido.");
            return;
        }
        System.out.println("P:"+preco);
        PrecoVenda pv = new PrecoVenda();
        pv.setPreco(preco);
        pv.setData_inicio(data_inicio);
        pv.setData_fim(data_fim);
        pv.setUsuario(user);
        pv.setProduto(pro);

        try {
            precoVendaDao.addEntity(pv);
//            showInfoDialog("Sucesso", "Preço de venda registrado com sucesso.");
        } catch (Exception e) {
            showErrorDialog("Erro", "Erro ao registrar preço de venda: " + e.getMessage());
            System.out.println("Erro: " + e);
        }
    }
    
    public void actualizaPrecoVenda(int id, double preco, String data_inicio, 
            String data_fim, Usuario user) {
        
        preco = validarPreco(preco);
        data_inicio = validarDataInicio(data_inicio);
        data_fim = validarDataFim(data_fim);

        
        if (user == null) {
            showErrorDialog("Erro de validação", "Usuário não definido.");
            return;
        }
        System.out.println("P:"+preco);
        PrecoVenda pv = new PrecoVenda();
        pv.setPreco(preco);
        pv.setData_inicio(data_inicio);
        pv.setData_fim(data_fim);
        pv.setUsuario(user);

        try {
            precoVendaDao.updateEntityByID(pv, id);
            showInfoDialog("Sucesso", "Preço de venda registrado com sucesso.");
        } catch (Exception e) {
            showErrorDialog("Erro", "Erro ao registrar preço de venda: " + e.getMessage());
            System.out.println("Erro: " + e);
        }
    }

    private double validarPreco(Double preco) {
        if (preco == null || preco <= 0) {
            showErrorDialog("Erro de validação", "Preço inválido. Será definido um preço padrão.");
            return PRECO_PADRAO;
        }
        return preco;
    }

    private String validarDataInicio(String data_inicio) {
        if (data_inicio == null || data_inicio.isEmpty()) {
            showErrorDialog("Erro de validação", "Data inicial não definida. Será usada a data atual.");
            return LocalDate.now().toString();
        }
        try {
            LocalDate.parse(data_inicio);
        } catch (DateTimeParseException e) {
            showErrorDialog("Erro de validação", "Data inicial inválida. Será usada a data atual.");
            return LocalDate.now().toString();
        }
        return data_inicio;
    }

    private String validarDataFim(String data_fim) {
        if (data_fim == null || data_fim.isEmpty()) {
            showErrorDialog("Erro de validação", "Data final não definida. Será usada a data padrão.");
            return DATA_FIM_PADRAO;
        }
        try {
            LocalDate.parse(data_fim);
        } catch (DateTimeParseException e) {
            showErrorDialog("Erro de validação", "Data final inválida. Será usada a data padrão.");
            return DATA_FIM_PADRAO;
        }
        return data_fim;
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}