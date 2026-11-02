
package com.vehiclerental.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.nio.file.Path;

public class InvoicePDFExporter {

    public static void export(String invoiceText, Path filePath)
            throws IOException {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                content.beginText();

                content.setFont(
                        new PDType1Font(Standard14Fonts.FontName.COURIER),
                        10
                );

                content.setLeading(16);

                content.newLineAtOffset(55, 780);

                String[] lines = invoiceText.split("\\R");

                for (String line : lines) {

                    if (line.length() > 85) {
                        line = line.substring(0, 85);
                    }

                    content.showText(line);
                    content.newLine();
                }

                content.endText();
            }

            document.save(filePath.toFile());
        }
    }
}
