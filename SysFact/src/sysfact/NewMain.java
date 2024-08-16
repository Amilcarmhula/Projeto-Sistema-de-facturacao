/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sysfact;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.FileNotFoundException;

/**
 *
 * @author JM-Tecnologias
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        String path = "teste.pdf";
        String texto = "Paragrafo de testo";
        Paragraph p = new Paragraph(texto); 
         PdfWriter pdfw = new PdfWriter(path);

        PdfDocument pdfdoc = new PdfDocument(pdfw);
        pdfdoc.addNewPage();

        Document doc = new Document(pdfdoc);

        doc.add(p);
        doc.close();

        // TODO code application logic here
    }

}
