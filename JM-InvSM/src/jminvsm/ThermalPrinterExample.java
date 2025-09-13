/*
 * The MIT License
 *
 * Copyright 2025 JM-Tecnologias.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jminvsm;

import javax.print.*;
//import javax.print.attribute.*;
//import javax.print.attribute.standard.*;

/**
 *
 * @author JM-Tecnologias
 */


public class ThermalPrinterExample {
    public static void main(String[] args) {
        try {
            // Nome da impressora térmica (tem que estar instalada no sistema)
//            String printerName = "MINHA_IMPRESSORA_TERMICA";
//            String printerName = "POS-58"; // via USB     
            String printerName = "POS58BT"; // via Bluetooth



            // Localiza a impressora
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService thermalPrinter = null;
            for (PrintService service : services) {
                if (service.getName().equalsIgnoreCase(printerName)) {
                    thermalPrinter = service;
                    break;
                }
            }

            if (thermalPrinter == null) {
                System.out.println("Impressora não encontrada.");
                return;
            }

            // Monta o texto ESC/POS
            String data =
                "\u001B@\n" +             // Inicializa impressora
                "\u001B!\u0010" + "JM-TECNOLOGIAS\n" + // Texto duplo
                "\u001B!\u0000" + "Venda Nº 12345\n" + // Texto normal
                "-----------------------------\n" +
                "Produto A     2x   10,00\n" +
                "Produto B     1x   25,00\n" +
                "-----------------------------\n" +
                "TOTAL:             45,00\n" +
                "\nObrigado pela preferência!\n\n\n" +
                "\u001DV\u0000"; // Corte de papel (se suportado)

            // Converte para bytes
            byte[] bytes = data.getBytes("CP437");

            // Prepara e imprime
            DocPrintJob job = thermalPrinter.createPrintJob();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(bytes, flavor, null);
            job.print(doc, null);

            System.out.println("Impressão enviada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

