/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.precovenda;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import sysfact.dao.preco.PrecoVendaDAO;
import sysfact.model.preco.PrecoVenda;
import sysfact.model.produto.Produto;
import sysfact.model.usuario.Usuario;
import static sysfact.util.AlertUtilities.showDialog;
import static sysfact.util.AlertUtilities.showErroNotification;
import static sysfact.util.AlertUtilities.showSuccessNotification;

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
            showErroNotification("Erro", "Produto não definido!");
            return;
        }
        
        if (user == null) {
            showErroNotification("Erro", "Usuário não definido!");
            return;
        }
        PrecoVenda pv = new PrecoVenda();
        pv.setPreco(preco);
        pv.setData_inicio(data_inicio);
        pv.setData_fim(data_fim);
        pv.setUsuario(user);
        pv.setProduto(pro);

        try {
            precoVendaDao.addEntity(pv);
            showSuccessNotification("Sucesso", "Preço registado com sucesso!");
        } catch (Exception e) {
            showDialog(FontAwesomeIcon.TIMES, "Erro", "Erro ao registrar preço de venda: " + e.getMessage());
            System.out.println("Erro: " + e);
        }
    }
    
    public void actualizaPrecoVenda(int id, double preco, String data_inicio, 
            String data_fim, Usuario user) {
        
        preco = validarPreco(preco);
        data_inicio = validarDataInicio(data_inicio);
        data_fim = validarDataFim(data_fim);

        
        if (user == null) {
            showErroNotification("Erro", "Usuário não definido!");
            return;
        }
        PrecoVenda pv = new PrecoVenda();
        pv.setPreco(preco);
        pv.setData_inicio(data_inicio);
        pv.setData_fim(data_fim);
        pv.setUsuario(user);

        try {
            precoVendaDao.updateEntityByID(pv, id);
            showSuccessNotification("Sucesso", "Preço actualizado com sucesso!");
        } catch (Exception e) {
            showDialog(FontAwesomeIcon.TIMES, "Erro", "Erro ao actualizar preço de venda: " + e.getMessage());
            System.out.println("Erro: " + e);
        }
    }

    private double validarPreco(Double preco) {
        if (preco == null || preco <= 0) {
            showErroNotification("Erro", "Preço nao pode ser nulo, Será definido um preço padrão.");
            return PRECO_PADRAO;
        }
        return preco;
    }

    private String validarDataInicio(String data_inicio) {
        if (data_inicio == null || data_inicio.isEmpty()) {
            showErroNotification("Erro", "Data inicial do preco não pode ser nula. Será usada a data atual!");
            return LocalDate.now().toString();
        }
        try {
            LocalDate.parse(data_inicio);
        } catch (DateTimeParseException e) {
            showErroNotification("Erro", "Data inicial do preco não pode ser nula. Será usada a data atual!");
            return LocalDate.now().toString();
        }
        return data_inicio;
    }

    private String validarDataFim(String data_fim) {
        if (data_fim == null || data_fim.isEmpty()) {
            showErroNotification("Erro", "Data final do preco não pode ser nula. Será usada uma data padrão!");
            return DATA_FIM_PADRAO;
        }
        try {
            LocalDate.parse(data_fim);
        } catch (DateTimeParseException e) {
            showErroNotification("Erro", "Data final do preco não pode ser nula. Será usada uma data padrão!");
            return DATA_FIM_PADRAO;
        }
        return data_fim;
    }
}