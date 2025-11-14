package br.com.financial.manager.app.infrastructure.config.pdf;

import com.itextpdf.text.Document;

import java.io.ByteArrayOutputStream;

public class DocumentWrapper {

    private final Document document;
    private final ByteArrayOutputStream outputStream;

    public DocumentWrapper(Document document, ByteArrayOutputStream outputStream) {
        this.document = document;
        this.outputStream = outputStream;
    }

    public Document getDocument() {
        return document;
    }

    public byte[] getBytes() {
        document.close();
        return outputStream.toByteArray();
    }
}
