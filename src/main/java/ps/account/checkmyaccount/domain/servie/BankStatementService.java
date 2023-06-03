package ps.account.checkmyaccount.domain.servie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import ps.account.checkmyaccount.domain.dto.TransactionDto;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

@Service
public class BankStatementService {

    private TemplateEngine templateEngine;

    @Autowired
    public BankStatementService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String generateBankStatementPDF(String bankName, String accountHolderName, String branch, List<TransactionDto> transactions, String outputFilePath) throws Exception {
        // Create Thymeleaf context and set variables
        Context context = new Context();
        context.setVariable("bankName", bankName);
        context.setVariable("accountHolderName", accountHolderName);
        context.setVariable("branch", branch);
        context.setVariable("transactions", transactions);



        // Process Thymeleaf template
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Bank Transaction Table</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\">\n" +
                "    <style>\n" +
                "        .status-pending {\n" +
                "            background-color: orange;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>")
                        .append("<div class=\"container\">\n" +
                                "    <div class=\"header\">")
                                .append("<h1>").append(bankName).append("</h1>")
                        .append("<p>Account Holder Name: <span >").append(accountHolderName).append("</span></p>")
                        .append("<p>Branch: <span>").append(branch).append("</span></p>")
                        .append("</div>\n" +
                                "</div>");

        sb.append("<table class=\"table table-bordered table-striped\">\n" +
                "        <thead class=\"thead-light\">\n" +
                "        <tr>\n" +
                "            <th>Date</th>\n" +
                "            <th>Debits</th>\n" +
                "            <th>Credits</th>\n" +
                "            <th>Balance</th>\n" +
                "            <th>Interest</th>\n" +
                "            <th>Status</th>\n" +
                "            <th>Description</th>\n" +
                "        </tr>\n" +
                "        </thead>\n" +
                "        <tbody>");

        transactions.forEach(tr->{
            sb.append("<tr").append(tr.isInterest()?"th:class=\"status-pending\"":"").append(">")
                    .append("<td>").append(tr.getOrderedDate().toString()).append("</td>")
                    .append("<td>").append(tr.getDebit().toString()).append("</td>")
                    .append("<td>").append(tr.getCredit().toString()).append("</td>")
                    .append("<td>").append(tr.getBalance().toString()).append("</td>")
                    .append("<td>").append(tr.isInterest()?tr.getInterest().toString():"").append("</td>")
                    .append("<td>").append(tr.isDateStatus()).append("</td>")
                    .append("<td>").append(
                            Objects.isNull(tr.getDescription())?"":tr.getDescription().values().stream().toArray().toString()
                    ).append("</td>")
                    .append("</tr>");

        });
        context.setVariable("table",sb.toString());

        sb.append("</tbody>\n" +
                "    </table>");

        sb.append("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js\"></script>\n" +
                "</body>\n" +
                "</html>");

//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setPrefix("/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode(TemplateMode.HTML);

//        templateEngine.setTemplateResolver(templateResolver);
//        String htmlContent = templateEngine.process("/input/temp.html", context);


        return sb.toString();



//         //Generate PDF from HTML using Flying Saucer
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(htmlContent);
//        renderer.layout();
//
//        try (OutputStream outputStream = new FileOutputStream("/outputs")) {
//            renderer.createPDF(outputStream);
//        }
    }
}
