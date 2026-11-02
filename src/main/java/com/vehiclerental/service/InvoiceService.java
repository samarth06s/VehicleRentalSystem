
package com.vehiclerental.service;

import com.vehiclerental.dao.InvoiceDAO;
import com.vehiclerental.model.Invoice;

import java.sql.SQLException;

public class InvoiceService {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    public String generateInvoice(int rentalId) throws SQLException {

        Invoice invoice = invoiceDAO.getInvoiceData(rentalId);

        if (!"COMPLETED".equalsIgnoreCase(invoice.status())
                || invoice.actualReturnDate() == null) {

            throw new IllegalStateException(
                    "Invoice is available only after the vehicle is returned!"
            );
        }

        StringBuilder bill = new StringBuilder();

        bill.append("\n");
        bill.append("============================================\n");
        bill.append("          VEHICLE RENTAL SYSTEM             \n");
        bill.append("                 INVOICE                    \n");
        bill.append("============================================\n\n");

        bill.append("INVOICE DETAILS\n");
        bill.append("--------------------------------------------\n");
        bill.append("Invoice ID       : ").append(invoice.rentalId()).append("\n");
        bill.append("Status           : ").append(invoice.status()).append("\n\n");

        bill.append("CUSTOMER DETAILS\n");
        bill.append("--------------------------------------------\n");
        bill.append("Name             : ").append(invoice.customerName()).append("\n");
        bill.append("Phone            : ").append(invoice.phone()).append("\n");
        bill.append("Email            : ")
                .append(invoice.email() == null ? "N/A" : invoice.email())
                .append("\n");
        bill.append("License Number   : ").append(invoice.licenseNumber()).append("\n\n");

        bill.append("VEHICLE DETAILS\n");
        bill.append("--------------------------------------------\n");
        bill.append("Vehicle          : ")
                .append(invoice.brand()).append(" ")
                .append(invoice.model()).append("\n");
        bill.append("Vehicle Type     : ").append(invoice.vehicleType()).append("\n");
        bill.append("Registration No. : ").append(invoice.registrationNumber()).append("\n");
        bill.append("Rent Per Day     : Rs. ").append(invoice.rentPerDay()).append("\n\n");

        bill.append("RENTAL DETAILS\n");
        bill.append("--------------------------------------------\n");
        bill.append("Rental Date      : ").append(invoice.rentalDate()).append("\n");
        bill.append("Due Date         : ").append(invoice.dueDate()).append("\n");
        bill.append("Return Date      : ")
                .append(invoice.actualReturnDate() == null
                        ? "Not Returned"
                        : invoice.actualReturnDate())
                .append("\n\n");

        bill.append("============================================\n");
        bill.append("TOTAL AMOUNT     : Rs. ")
                .append(invoice.totalAmount()).append("\n");
        bill.append("============================================\n");
        bill.append("           THANK YOU FOR CHOOSING US        \n");
        bill.append("============================================\n");

        return bill.toString();
    }
}
