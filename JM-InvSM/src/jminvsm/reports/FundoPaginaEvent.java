/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.reports;



/**
 *
 * @author JM-Tecnologias
 */
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import java.net.MalformedURLException;


class FundoPaginaEvent implements IEventHandler {

    private final ImageData imageData;
    
      public FundoPaginaEvent(String caminhoImagem) throws MalformedURLException {
        this.imageData = ImageDataFactory.create(caminhoImagem);
    }


    @Override
    public void handleEvent(Event event) {

        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
//        PageSize pageSize = (PageSize) page.getPageSize();
        Rectangle pageSize = page.getPageSize();


        PdfCanvas canvas = new PdfCanvas(
                page.newContentStreamBefore(),
                page.getResources(),
                pdfDoc
        );

        canvas.addImage(
                imageData,
                pageSize,
                false
        );
        
        
    }
}
