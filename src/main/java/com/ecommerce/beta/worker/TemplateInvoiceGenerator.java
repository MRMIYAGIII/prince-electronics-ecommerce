package com.ecommerce.beta.worker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.OrderHistory;
import com.ecommerce.beta.entity.OrderItems;
import com.ecommerce.beta.enums.OrderType;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

@Service
public class TemplateInvoiceGenerator {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public void generateInvoice(OrderHistory orderHistory) {
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads/invoices/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String outputFilePath = uploadDir + orderHistory.getUuid().toString() + ".pdf";

        // Delete the file if it already exists
        File file = new File(outputFilePath);
        if (file.exists()) {
            file.delete();
        }

        try (InputStream inputStream = new ClassPathResource("templates/invoice_template.html").getInputStream();
             OutputStream outputStream = new FileOutputStream(outputFilePath)) {

            // Load the HTML template
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // Replace placeholders with actual data
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("{{orderId}}", orderHistory.getUuid().toString());
            dataMap.put("{{orderDate}}", DATE_FORMAT.format(orderHistory.getCreatedAt()));
            dataMap.put("{{flat}}", orderHistory.getUserAddress().getFlat() != null ? orderHistory.getUserAddress().getFlat() : "");
            dataMap.put("{{area}}", orderHistory.getUserAddress().getArea() != null ? orderHistory.getUserAddress().getArea() : "");
            dataMap.put("{{town}}", orderHistory.getUserAddress().getTown() != null ? orderHistory.getUserAddress().getTown() : "");
            dataMap.put("{{city}}", orderHistory.getUserAddress().getCity() != null ? orderHistory.getUserAddress().getCity() : "");
            dataMap.put("{{state}}", orderHistory.getUserAddress().getState() != null ? orderHistory.getUserAddress().getState() : "");
            dataMap.put("{{pin}}", orderHistory.getUserAddress().getPin() != null ? orderHistory.getUserAddress().getPin() : "");
            dataMap.put("{{phone}}", orderHistory.getUserInfo().getPhone() != null ? orderHistory.getUserInfo().getPhone() : "");
            dataMap.put("{{email}}", orderHistory.getUserInfo().getEmail() != null ? orderHistory.getUserInfo().getEmail() : "");
            dataMap.put("{{name}}", orderHistory.getUserInfo().getUsername() != null ? orderHistory.getUserInfo().getUsername() : "");
            dataMap.put("{{generatedDate}}", DATE_FORMAT.format(new Date()));

            dataMap.put("{{subTotal}}", String.valueOf(orderHistory.getTotal()));
            dataMap.put("{{tax}}", String.valueOf(orderHistory.getTax()));
            dataMap.put("{{discount}}", String.valueOf(orderHistory.getOff()));
            dataMap.put("{{gross}}", String.valueOf(orderHistory.getGrossLong()));
            dataMap.put("{{type}}", orderHistory.getOrderType().name());
            dataMap.put("{{onlineRef}}", ""); // No online reference since Razorpay is removed

            StringBuilder orderItemsRows = new StringBuilder();

            for (OrderItems orderItem : orderHistory.getItems()) {
                Map<String, String> orderItemData = new HashMap<>();
                orderItemData.put("{{description}}", orderItem.getItemName() != null ? orderItem.getItemName() : "N/A");
                orderItemData.put("{{quantity}}", String.valueOf(orderItem.getQuantity()));
                orderItemData.put("{{unitPrice}}", String.valueOf(orderItem.getOrderPrice()));
                orderItemData.put("{{subtotal}}", String.valueOf(orderItem.getTotal()));

                // Replace placeholders with order item data
                String orderItemRow = "<tr>" +
                        "<td valign='top' style='text-align: left;'>" + orderItemData.get("{{description}}") + "</td>" +
                        "<td valign='top' style='text-align: center;'>" + orderItemData.get("{{quantity}}") + "</td>" +
                        "<td valign='top' style='text-align: center;'>" + orderItemData.get("{{unitPrice}}") + "</td>" +
                        "<td valign='top' style='text-align: center;'>" + orderItemData.get("{{subtotal}}") + "</td>" +
                        "</tr>";

                orderItemsRows.append(orderItemRow);
            }

            // Replace the placeholder in the HTML template with the generated order items rows
            htmlTemplate = htmlTemplate.replace("{{orderItemsRows}}", orderItemsRows.toString());

            // Replace the remaining placeholders in the HTML template with the actual data values
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                htmlTemplate = htmlTemplate.replace(entry.getKey(), entry.getValue());
            }

            // Generate the PDF from the modified HTML template
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(htmlTemplate, pdfDocument, converterProperties);

            System.out.println("Invoice generated successfully!");
        } catch (IOException e) {
            System.err.println("Error occurred while generating the invoice: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void deleteInvoices() throws IOException {
        System.out.println("     __    __    __  _                                   __    \n" +
                " ___/ /__ / /__ / /_(_)__  ___ _  _______ ___  ___  ____/ /____\n" +
                "/ _  / -_) / -_) __/ / _ \\/ _ `/ / __/ -_) _ \\/ _ \\/ __/ __(_-<\n" +
                "\\_,_/\\__/_/\\__/\\__/_/_//_/\\_, / /_/  \\__/ .__/\\___/_/  \\__/___/\n" +
                "                         /___/         /_/                     \n" +
                "\n");

        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads/reports/";
        File directory = new File(uploadDir);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        handleDelete(fileName);
                    }
                }
            }
        }
    }

    private void handleDelete(String fileName) throws IOException {
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads/reports/";
        String filePath = uploadDir + "/" + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
            System.out.println(fileName + " deleted");
        } else {
            System.out.println("File not found!");
        }
    }
}