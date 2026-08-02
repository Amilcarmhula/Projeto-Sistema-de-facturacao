/*
 * The MIT License
 *
 * Copyright 2026 JM-Tecnologias.
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
package jminvsm.reports;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import jminvsm.dao.empresa.EmpresaDAO;
import jminvsm.dao.vendas.factura.FacturaDAO;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.vendas.factura.itens.ServiceItem;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.FilePathFinderUtilities;

/**
 *
 * @author JM-Tecnologias
 */
public class ReportModel {

    private Path path = Paths.get(System.getProperty("user.home"), "Documents", "JM-InvSM/Report/");
    private PdfWriter pdfwriter;
    private PdfDocument pdfDocs;
    private Document document;
    private PdfFont bold = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
    private PdfFont normal = PdfFontFactory.createFont(StandardFonts.COURIER);

    public ReportModel(Empresa emp, Factura f) throws MalformedURLException, IOException, SQLException {
        if (!Files.exists(this.path)) {
            try {
                Files.createDirectories(this.path);
            } catch (IOException ex) {
                showDialog("Erro ", "Error:" + ex);
            }
        }

        try {
            String invName = f.getNumero_fac().replaceAll("/", "-");
            this.pdfwriter = new PdfWriter(this.path.toString() + "\\JM-INVSM-" + invName + ".pdf");
            this.pdfDocs = new PdfDocument(this.pdfwriter);
            // Adiciona o evento de fundo
            this.pdfDocs.addEventHandler(
                    PdfDocumentEvent.START_PAGE,
                    new FundoPaginaEvent(FilePathFinderUtilities.absolutePathBuilder(new File(System.getProperty("user.dir")), "bg1.jpg"))
            );
            this.pdfDocs.addNewPage();
            this.document = new Document(pdfDocs);
            // Adiciona logo no header
            ImageData data = ImageDataFactory.create(FilePathFinderUtilities.absolutePathBuilder(new File(System.getProperty("user.dir")), "LOGOTIPO.png"));
            Image img = new Image(data);
            img.scaleToFit(280, 280);           // largura x altura máximas
            img.setFixedPosition(-50, 650);      // posição absoluta (x, y)
            img.setHorizontalAlignment(HorizontalAlignment.CENTER); // centro
            this.document.add(img);
            this.document.add(new Paragraph(emp.getRazao_emp()).setFont(bold).setTextAlignment(TextAlignment.RIGHT).setFontSize(14));
            this.document.add(new Paragraph("NUIT: " + emp.getNuit_emp()).setFont(normal).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
            this.document.add(new Paragraph(emp.printEnderecos()).setFont(normal).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
            this.document.add(new Paragraph("Telefone: " + emp.getContactoEmpresa().getContacto_emp()).setFont(normal).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
            this.document.add(new Paragraph("E-mail: " + emp.getContactoEmpresa().getEmail_emp()).setFont(normal).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
            this.document.add(new Paragraph("Site: " + emp.getContactoEmpresa().getWebsite_emp()).setFont(normal).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
            // metodos que executam outras funcoes aqui
            this.document.add(new Paragraph(" ").setFontSize(8));
            this.document.add(summaryHeader(f));
            this.document.add(new Paragraph(" ").setFontSize(8));
            this.document.add(itemSummary(f.getNumeroFactura()));
            this.document.add(custo(emp, f.getNumeroFactura()));

//            this.document.add(new Paragraph("© 2026 JM Tecnologias — Todos os direitos reservados.").setTextAlignment(TextAlignment.CENTER).setFontColor(WebColors.getRGBColor("#e5e5e5")).setFontSize(8));
            closeDocumente();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro: " + ex);
            showDialog("Erro ", "Error:" + ex);
        }
    }

    private Table summaryHeader(Factura f) {
        float[] columnWidths = {35f, 19f, 23f, 23f};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));
        Color myColor = WebColors.getRGBColor("#e5e5e5");

        table.addCell(new Cell().add(new Paragraph(f.getDocComerciais().getNome_doc())
                .setFont(bold).setFontSize(10).setBorder(Border.NO_BORDER)));

        table.addCell(new Cell().add(new Paragraph("Nº." + f.getNumeroFactura())
                .setFont(bold).setFontSize(10).setBorder(Border.NO_BORDER)));

        table.addCell(new Cell().add(new Paragraph("De: " + f.getData_emissao())
                .setFont(bold).setFontSize(10).setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.RIGHT));

        table.addCell(new Cell().add(new Paragraph("Ate: " + f.getData_vencimento())
                .setFont(bold).setFontSize(10).setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.RIGHT));

        table.addCell(new Cell(1, 4).add(new Paragraph("Exmo.(s) Sr.(s): " + f.getCliente().getRazao_cli() + ": " + f.getCliente().getNuit_cli()).setFont(bold)).setFontSize(12));
        table.addCell(new Cell(1, 4).add(new Paragraph("Endereco: " + f.getCliente().printEnderecos())).setFont(normal).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table.addCell(new Cell(1, 4).add(new Paragraph("E-mail: " + f.getCliente().getContactoCliente().getEmail_cli() + " / Contacto: " + f.getCliente().getContactoCliente().getContacto_cli())).setFont(normal).setTextAlignment(TextAlignment.LEFT).setFontSize(9));

        Border solidBorder = new SolidBorder(myColor, 2);
        table.setBorder(solidBorder);
        table.getChildren().forEach(cell -> {
            ((Cell) cell).setBorder(Border.NO_BORDER);
        });
        return table;
    }

    private Table itemSummary(String fac) throws SQLException {
        float[] colWidths = {22f, 37f, 7f, 10f, 7f, 7f, 10f};
        Table table = new Table(UnitValue.createPercentArray(colWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addCell(new Cell().add(new Paragraph("Produto/Servico").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
        table.addCell(new Cell().add(new Paragraph("Descricao").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
        table.addCell(new Cell().add(new Paragraph("Qtd").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
        table.addCell(new Cell().add(new Paragraph("Preco Un").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
        table.addCell(new Cell().add(new Paragraph("Tax").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
        table.addCell(new Cell().add(new Paragraph("Desc.").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
        table.addCell(new Cell().add(new Paragraph("Total").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.CENTER).setFontSize(9));

        ServiceItem si = new ServiceItem();
        List<Item> itemList = si.consultaItensFactura(fac);
        for (Item i : itemList) {
            table.addCell(new Cell().add(new Paragraph(i.getProduto().getNome()).setFont(normal)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
            table.addCell(new Cell().add(new Paragraph(i.getProduto().getDescricao()).setFont(normal)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(i.getQuantidade())).setFont(normal)).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f",i.getPrecoProdutoArmazem().getPrecoVenda()) + "MT").setFont(normal)).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
            table.addCell(new Cell().add(new Paragraph(i.getProduto().getImposto().getPercentagem() + "%").setFont(normal)).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
            table.addCell(new Cell().add(new Paragraph(i.getProduto().getDescontoProduto().getDesconto().getPercentagem() + "%").setFont(normal)).setTextAlignment(TextAlignment.CENTER).setFontSize(9));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f",i.getSubtotal()) + "MT").setFont(normal)).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
        }
        table.getChildren().forEach(cell -> {
            ((Cell) cell).setBorder(Border.NO_BORDER);
        });
        return table;
    }

    private Table custo(Empresa emp, String fac) throws SQLException {
        ServiceItem si = new ServiceItem();
        List<Item> itemList = si.consultaItensFactura(fac);
        PdfPage page = this.pdfDocs.getPage(1);
        Rectangle pageSize = page.getPageSize();

        float marginLeft = 37;
        float tableWidth = pageSize.getWidth() - 74;
        float positionY = pageSize.getTop() - 830;
        
        double iva = 0;
        double desc = 0;
        double totalLiquido = 0;
        double total = 0;
        for (Item i : itemList) {
            totalLiquido += i.getPrecoProdutoArmazem().getPrecoBase() * i.getQuantidade();
            iva += i.getPrecoProdutoArmazem().getPrecoBase() * (i.getProduto().getImposto().getPercentagem() / 100) * i.getQuantidade();
            desc += i.getPrecoProdutoArmazem().getPrecoVenda() * (i.getProduto().getDescontoProduto().getDesconto().getPercentagem() / 100) * i.getQuantidade();
            total += i.getPrecoProdutoArmazem().getPrecoFinal() * i.getQuantidade();
        }
        
        float[] cols = {60f, 40f};
        Table table1 = new Table(UnitValue.createPercentArray(cols));
        table1.setWidth(UnitValue.createPercentValue(100));

        Table table2 = new Table(UnitValue.createPercentArray(cols));
        table2.setWidth(UnitValue.createPercentValue(100));
        table2.addCell(new Cell().add(new Paragraph("Total Liquido:").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
        table2.addCell(new Cell().add(new Paragraph(String.format("%.2f", totalLiquido) + " MT").setFont(normal)).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));

        table2.addCell(new Cell().add(new Paragraph("IVA:").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
        table2.addCell(new Cell().add(new Paragraph(String.format("%.2f", iva) + " MT").setFont(normal)).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));

        table2.addCell(new Cell().add(new Paragraph("Desconto:").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
        table2.addCell(new Cell().add(new Paragraph(String.format("%.2f", desc) + " MT").setFont(normal)).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));

        table2.addCell(new Cell().add(new Paragraph("Total:").setFont(bold)).setBackgroundColor(WebColors.getRGBColor("#DCDCDC")).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));
        table2.addCell(new Cell().add(new Paragraph(String.format("%.2f", total) + " MT").setFont(normal)).setTextAlignment(TextAlignment.RIGHT).setFontSize(9));

        String dados = "" ;
        for (ContaBancaria conta : emp.getContasBancarias()) {
            dados += "\n "+conta.getNome_banco()+"\n Conta: "+conta.getNumero_conta()+"\n NIB: "+conta.getNib();
        }
        table1.addCell(new Cell().add(new Paragraph("Dados Bancários "+dados).setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table1.addCell(new Cell().add(table2).setVerticalAlignment(VerticalAlignment.BOTTOM));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph("Responsável").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph("_________________________________________________").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph("Processado Por Computador").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell(1,2).add(new Paragraph(" ").setFont(bold)).setTextAlignment(TextAlignment.LEFT).setFontSize(9));
        table1.addCell(new Cell().add(new Paragraph("JM-InvSM. v.1.0.0 under license: ").setFont(normal)).setTextAlignment(TextAlignment.LEFT).setFontSize(8));
        table1.addCell(new Cell().add(new Paragraph("Date: AAAA-MM-DD / #User: Root").setFont(normal)).setTextAlignment(TextAlignment.RIGHT).setFontSize(8));
        table2.getChildren().forEach(cell -> {
            ((Cell) cell).setBorder(Border.NO_BORDER);
        });

        table1.setFixedPosition(
                1,
                marginLeft,
                positionY,
                tableWidth
        );
        table1.getChildren().forEach(cell -> {
            ((Cell) cell).setBorder(Border.NO_BORDER);
        });
        return table1;
    }

    public boolean closeDocumente() {
        this.document.close();
        return true;
    }

    public static void main(String[] args) throws IOException, SQLException {
        EmpresaDAO empresaData = new EmpresaDAO();
        FacturaDAO factDao = new FacturaDAO();

        ReportModel rm = new ReportModel(empresaData.getEmpresaData(), factDao.getFacturaData("2026-893/1"));

    }

}
