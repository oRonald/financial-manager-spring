package br.com.financial.manager.app.infrastructure.config.mail.pdfGenerator;

import br.com.financial.manager.app.domain.entity.Account;
import br.com.financial.manager.app.domain.entity.Transaction;
import br.com.financial.manager.app.exception.exceptions.PdfGenerateException;
import br.com.financial.manager.app.infrastructure.config.pdf.DocumentWrapper;
import br.com.financial.manager.app.infrastructure.config.pdf.PdfFactoryConfig;
import br.com.financial.manager.app.service.EmailService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Component
public class GenerateStatementReport {

    private final EmailService emailService;
    private final PdfFactoryConfig pdfFactoryConfig;

    public GenerateStatementReport(EmailService emailService, PdfFactoryConfig pdfFactoryConfig) {
        this.emailService = emailService;
        this.pdfFactoryConfig = pdfFactoryConfig;
    }

    public void generateStatement(Account account){
        try {
            DocumentWrapper wrapper = pdfFactoryConfig.createDocument();
            Document doc = wrapper.getDocument();

            Paragraph title = new Paragraph("Financial Statement Report");
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10f);
            doc.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{6f, 5f, 5f, 6f, 6f, 6f});

            Stream.of("Description", "Transaction Value", "Date", "Type", "Category", "Account")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell(new Paragraph(header));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                    });

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    .withZone(ZoneId.of("America/Sao_Paulo"));

            for(Transaction t : account.getTransactions()){
                String formatedDate = formatter.format(t.getDate());
                table.addCell(new PdfPCell(new Paragraph(t.getDescription().isEmpty() ? "UNKNOWN" : t.getDescription())));
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(t.getTransactionValue()))));
                table.addCell(new PdfPCell(new Paragraph(formatedDate)));
                table.addCell(new PdfPCell(new Paragraph(t.getType().name())));
                table.addCell(new PdfPCell(new Paragraph(t.getCategory() != null ? t.getCategory().getName() : "UNKNOWN")));
                table.addCell(new PdfPCell(new Paragraph(t.getAccount().getName())));
            }

            doc.add(table);

            Paragraph info = new Paragraph("Account owner: " + account.getOwner().getEmail());
            info.setAlignment(Element.ALIGN_LEFT);

            doc.add(info);
            byte[] pdfBytes = wrapper.getBytes();

            emailService.sendEmailStatement(pdfBytes, account.getOwner().getEmail());
        } catch (DocumentException e) {
            throw new PdfGenerateException("Error generating report PDF");
        }
    }
}
