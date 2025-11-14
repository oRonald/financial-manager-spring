package br.com.financial.manager.app.infrastructure.config.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfFactoryConfig {

    public DocumentWrapper createDocument(){
        try{
            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, outputStream);
            document.open();

            return new DocumentWrapper(document, outputStream);
        } catch (DocumentException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
