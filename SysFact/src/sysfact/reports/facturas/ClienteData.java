/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.reports.facturas;

/**
 *
 * @author JM-Tecnologias
 */
public class ClienteData {
    private String apelido;
    private int nuit;
    private String contacto;
    private String email;
    private String endereco;

    public ClienteData() {
    }

    public ClienteData(String apelido, int nuit, String contacto, String email, String endereco) {
        this.apelido = apelido;
        this.nuit = nuit;
        this.contacto = contacto;
        this.email = email;
        this.endereco = endereco;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public int getNuit() {
        return nuit;
    }

    public void setNuit(int nuit) {
        this.nuit = nuit;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    
}
