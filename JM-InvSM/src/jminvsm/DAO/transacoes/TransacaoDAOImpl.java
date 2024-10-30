/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.transacoes;

import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface TransacaoDAOImpl<T> {
    boolean addTansacaoAvista(T c);
    boolean addTansacaoParcelada(T c);
    ObservableList<T> getTransacoes(String numero);
}
