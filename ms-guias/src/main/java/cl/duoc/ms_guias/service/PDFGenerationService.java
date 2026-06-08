package cl.duoc.ms_guias.service;

import cl.duoc.ms_guias.dto.PedidoDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class PDFGenerationService {

    public byte[] generateGuiaPdf(PedidoDTO pedido) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("GUÍA DE DESPACHO"));
        document.add(new Paragraph("Número de pedido: " + pedido.getNumeroPedido()));
        document.add(new Paragraph("Transportista: " + pedido.getTransportista()));
        document.add(new Paragraph("Cliente: " + pedido.getCliente()));
        document.add(new Paragraph("Origen: " + pedido.getOrigen()));
        document.add(new Paragraph("Destino: " + pedido.getDestino()));
        document.add(new Paragraph("Peso: " + pedido.getPeso() + " kg"));
        document.add(new Paragraph("Monto: $" + pedido.getMonto()));
        document.add(new Paragraph("Fecha: " + java.time.LocalDate.now()));

        document.close();
        return out.toByteArray();
    }
}