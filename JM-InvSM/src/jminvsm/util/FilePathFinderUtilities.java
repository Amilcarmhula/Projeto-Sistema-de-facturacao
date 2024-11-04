/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.util;

import java.io.File;

/**
 *
 * @author JM-Tecnologias
 */
public class FilePathFinderUtilities {

    // Método recursivo que busca um arquivo ou diretório a partir da pasta raiz
    public static String fxmlPathBuilder(File rootDirectory, String searchName) {
        // Verifica se o diretório raiz existe e se é realmente um diretório
        if (rootDirectory.exists() && rootDirectory.isDirectory()) {
            // Lista todos os arquivos e subdiretórios no diretório raiz
            File[] files = rootDirectory.listFiles();
            if (files != null) {
                for (File file : files) {

                    // Se o arquivo ou diretório atual corresponder ao nome de busca
                    if (file.getName().equalsIgnoreCase(searchName)) {
                        String foundPath;
                        if (!file.getAbsolutePath().contains("build")) {
                            foundPath = file.getAbsolutePath().replace(System.getProperty("user.dir")+"\\src\\", "/");
                            
                            return foundPath.replace("\\", "/"); // Retorna o caminho do arquivo encontrado
                        }
                    }
                    // Se for um diretório, chama o método recursivamente
                    if (file.isDirectory()) {
                        String foundInSubdirectory = fxmlPathBuilder(file, searchName);
                        if (foundInSubdirectory != null) {
                            return foundInSubdirectory; // Retorna o caminho se encontrado em subdiretórios
                        }
                    }
                }
            }
        } else {
            System.out.println("O diretório raiz informado não existe ou não é um diretório válido.");
        }
        return null; // Retorna null se o arquivo não for encontrado
    }
    
     // Método recursivo que busca um arquivo ou diretório a partir da pasta raiz
    public static String absolutePathBuilder(File rootDirectory, String searchName) {
        // Verifica se o diretório raiz existe e se é realmente um diretório
        if (rootDirectory.exists() && rootDirectory.isDirectory()) {
            // Lista todos os arquivos e subdiretórios no diretório raiz
            File[] files = rootDirectory.listFiles();
            if (files != null) {
                for (File file : files) {

                    // Se o arquivo ou diretório atual corresponder ao nome de busca
                    if (file.getName().equalsIgnoreCase(searchName)) {
                        String foundPath;
                        if (!file.getAbsolutePath().contains("build")) {
//                            foundPath = file.getAbsolutePath().replace(System.getProperty("user.dir")+"\\src\\sysfact\\reports\\", "../");
                            foundPath = file.getAbsolutePath();
                            System.out.println(foundPath);
//                            return foundPath.replace("\\", "/"); // Retorna o caminho do arquivo encontrado
                            return foundPath;
                        }
                    }
                    // Se for um diretório, chama o método recursivamente
                    if (file.isDirectory()) {
                        String foundInSubdirectory = absolutePathBuilder(file, searchName);
                        if (foundInSubdirectory != null) {
                            return foundInSubdirectory; // Retorna o caminho se encontrado em subdiretórios
                        }
                    }
                }
            }
        } else {
            System.out.println("O diretório raiz informado não existe ou não é um diretório válido.");
        }
        return null; // Retorna null se o arquivo não for encontrado
    }

    public static void main(String[] args) {
        System.out.println(FilePathFinderUtilities.fxmlPathBuilder(new File(System.getProperty("user.dir")), "MiniPagamentoView.fxml"));
    }
}
