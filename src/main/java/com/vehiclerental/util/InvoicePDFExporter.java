
package com.vehiclerental.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;

public class InvoicePDFExporter {

    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float LEFT_MARGIN = 55;
    private static final float RIGHT_MARGIN = 55;
    private static final float BOTTOM_MARGIN = 55;

    private static final Color NAVY = new Color(24, 38, 62);
    private static final Color BLUE = new Color(48, 110, 210);
    private static final Color DARK = new Color(45, 52, 65);
    private static final Color MUTED = new Color(115, 125, 140);
    private static final Color LIGHT = new Color(230, 234, 240);

    public static void export(String invoiceText, Path filePath)
            throws IOException {

        if (invoiceText == null || invoiceText.trim().isEmpty()) {
            throw new IOException("Invoice content is empty.");
        }

        try (PDDocument document = new PDDocument()) {

            PDType1Font regular = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA);

            PDType1Font bold = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD);

            PDType1Font titleFont = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream content =
                    new PDPageContentStream(document, page);

            float y = drawHeader(content, titleFont);

            String[] lines = invoiceText.split("\\R", -1);

            for (String originalLine : lines) {

                String line = cleanText(originalLine).trim();

                // Empty line: add spacing
                if (line.isEmpty()) {
                    y -= 9;
                    continue;
                }

                // Draw divider for separator lines
                if (line.matches("[-=_]{3,}")) {

                    if (y < BOTTOM_MARGIN + 15) {
                        content.close();

                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);

                        content = new PDPageContentStream(
                                document, page);

                        y = drawContinuationHeader(
                                content, titleFont);
                    }

                    content.setStrokingColor(LIGHT);
                    content.setLineWidth(0.8f);

                    content.moveTo(LEFT_MARGIN, y);
                    content.lineTo(
                            PAGE_WIDTH - RIGHT_MARGIN, y);

                    content.stroke();

                    y -= 15;
                    continue;
                }

                // Highlight section headings
                boolean isHeading =
                        line.endsWith(":") && line.length() < 45;

                PDType1Font currentFont =
                        isHeading ? bold : regular;

                float fontSize = isHeading ? 11 : 10;

                float lineHeight = isHeading ? 19 : 15;

                // Wrap long text automatically
                String[] wrappedLines = wrapText(
                        line,
                        currentFont,
                        fontSize,
                        PAGE_WIDTH - LEFT_MARGIN - RIGHT_MARGIN
                );

                for (String wrappedLine : wrappedLines) {

                    if (y < BOTTOM_MARGIN + lineHeight) {

                        content.close();

                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);

                        content = new PDPageContentStream(
                                document, page);

                        y = drawContinuationHeader(
                                content, titleFont);
                    }

                    content.beginText();

                    content.setFont(currentFont, fontSize);

                    if (isHeading) {
                        content.setNonStrokingColor(BLUE);
                    } else {
                        content.setNonStrokingColor(DARK);
                    }

                    content.newLineAtOffset(LEFT_MARGIN, y);
                    content.showText(wrappedLine);
                    content.endText();

                    y -= lineHeight;
                }
            }

            // Footer
            drawFooter(content);

            content.close();

            document.save(filePath.toFile());
        }
    }

    // Main invoice header
    private static float drawHeader(
            PDPageContentStream content,
            PDType1Font font) throws IOException {

        content.setNonStrokingColor(NAVY);

        content.addRect(
                0,
                720,
                PAGE_WIDTH,
                122
        );

        content.fill();

        content.beginText();

        content.setFont(font, 23);
        content.setNonStrokingColor(Color.WHITE);

        content.newLineAtOffset(LEFT_MARGIN, 790);
        content.showText("DRIVE RENT");

        content.endText();

        content.beginText();

        content.setFont(font, 12);
        content.setNonStrokingColor(Color.WHITE);

        content.newLineAtOffset(LEFT_MARGIN, 765);
        content.showText("VEHICLE RENTAL INVOICE");

        content.endText();

        content.setStrokingColor(
                new Color(95, 130, 180));

        content.setLineWidth(1);

        content.moveTo(LEFT_MARGIN, 748);
        content.lineTo(PAGE_WIDTH - RIGHT_MARGIN, 748);
        content.stroke();

        content.beginText();

        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA),
                9
        );

        content.setNonStrokingColor(
                new Color(215, 225, 240));

        content.newLineAtOffset(LEFT_MARGIN, 733);
        content.showText("Vehicle Rental Management System");

        content.endText();

        return 695;
    }

    // Header for additional pages
    private static float drawContinuationHeader(
            PDPageContentStream content,
            PDType1Font font) throws IOException {

        content.setNonStrokingColor(NAVY);

        content.addRect(0, 775, PAGE_WIDTH, 67);
        content.fill();

        content.beginText();

        content.setFont(font, 17);
        content.setNonStrokingColor(Color.WHITE);

        content.newLineAtOffset(LEFT_MARGIN, 802);
        content.showText("DRIVE RENT - INVOICE");

        content.endText();

        return 750;
    }

    // Footer
    private static void drawFooter(
            PDPageContentStream content) throws IOException {

        content.setStrokingColor(LIGHT);
        content.setLineWidth(0.7f);

        content.moveTo(LEFT_MARGIN, 40);
        content.lineTo(PAGE_WIDTH - RIGHT_MARGIN, 40);
        content.stroke();

        content.beginText();

        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA),
                8
        );

        content.setNonStrokingColor(MUTED);

        content.newLineAtOffset(LEFT_MARGIN, 27);
        content.showText(
                "Thank you for choosing Drive Rent!");

        content.endText();
    }

    // Automatically wrap long lines
    private static String[] wrapText(
            String text,
            PDType1Font font,
            float fontSize,
            float maxWidth) throws IOException {

        java.util.List<String> result =
                new java.util.ArrayList<>();

        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split("\\s+")) {

            String testLine = currentLine.length() == 0
                    ? word
                    : currentLine + " " + word;

            float width =
                    font.getStringWidth(testLine) / 1000 * fontSize;

            if (width > maxWidth && currentLine.length() > 0) {

                result.add(currentLine.toString());
                currentLine = new StringBuilder(word);

            } else {
                currentLine = new StringBuilder(testLine);
            }
        }

        if (currentLine.length() > 0) {
            result.add(currentLine.toString());
        }

        return result.toArray(new String[0]);
    }

    // Convert unsupported Unicode characters for standard PDF fonts
    private static String cleanText(String text) {

        return text
                .replace("₹", "Rs. ")
                .replace("—", "-")
                .replace("–", "-")
                .replace("•", "-")
                .replace("“", "\"")
                .replace("”", "\"")
                .replace("’", "'")
                .replace("‘", "'")
                .replaceAll("[^\\x20-\\x7E]", "?");
    }
}
