package com.vehiclerental.ui;

import com.vehiclerental.dao.VehicleDAO;
import com.vehiclerental.dao.CustomerDAO;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.service.RentalService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import com.vehiclerental.service.InvoiceService;
import com.vehiclerental.util.InvoicePDFExporter;
import javax.swing.JFileChooser;
import java.nio.file.Path;

public class RentalPanel extends JPanel {

    private void viewInvoice() {

        int selectedRow = rentalTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a rental first!"
            );
            return;
        }

        int modelRow = rentalTable.convertRowIndexToModel(selectedRow);

        int rentalId = (int) rentalTable.getModel()
                .getValueAt(modelRow, 0);

        try {
            String invoice = invoiceService.generateInvoice(rentalId);

            JTextArea invoiceArea = new JTextArea(invoice);
            invoiceArea.setEditable(false);
            invoiceArea.setFont(new java.awt.Font(
                    "Monospaced", java.awt.Font.PLAIN, 13
            ));

            JScrollPane scrollPane = new JScrollPane(invoiceArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 550));

            Object[] options = {
                    "Print Invoice",
                    "Save as PDF",
                    "Close"
            };

            int choice = JOptionPane.showOptionDialog(
                    this,
                    scrollPane,
                    "Rental Invoice",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );


            if (choice == 0) {

                // Print Invoice
                try {
                    invoiceArea.print();
                } catch (java.awt.print.PrinterException e) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Printing failed: " + e.getMessage(),
                            "Print Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } else if (choice == 1) {

                // Save Invoice as PDF
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setDialogTitle("Save Invoice as PDF");
                fileChooser.setSelectedFile(
                        new java.io.File("Invoice_" + rentalId + ".pdf")
                );

                int result = fileChooser.showSaveDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {

                    Path filePath = fileChooser.getSelectedFile().toPath();

                    if (!filePath.toString().toLowerCase().endsWith(".pdf")) {
                        filePath = Path.of(filePath + ".pdf");
                    }

                    try {
                        InvoicePDFExporter.export(invoice, filePath);

                        JOptionPane.showMessageDialog(
                                this,
                                "Invoice PDF saved successfully!\n" + filePath,
                                "PDF Export Successful",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } catch (java.io.IOException e) {
                        JOptionPane.showMessageDialog(
                                this,
                                "PDF export failed: " + e.getMessage(),
                                "PDF Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }


            if (choice == 0) {
                try {
                    invoiceArea.print();
                } catch (java.awt.print.PrinterException e) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Printing failed: " + e.getMessage(),
                            "Print Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to generate invoice: " + e.getMessage(),
                    "Invoice Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private final RentalService rentalService = new RentalService();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final InvoiceService invoiceService = new InvoiceService();

    private final JComboBox<Customer> customerBox = new JComboBox<>();
    private final JComboBox<Vehicle> vehicleBox = new JComboBox<>();

    private final JTextField rentalDateField =
            new JTextField(LocalDate.now().toString());

    private final JTextField dueDateField =
            new JTextField(LocalDate.now().plusDays(3).toString());

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{"Rental ID", "Customer ID", "Vehicle ID",
                            "Rental Date", "Due Date", "Return Date",
                            "Amount", "Status"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable rentalTable = new JTable(tableModel);

    private final JTextField searchField = new JTextField(20);

    private final TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(tableModel);

    public RentalPanel() {

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadCustomersAndVehicles();
        loadRentals();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setOpaque(false);

        JLabel title = new JLabel("Rental Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel subtitle = new JLabel(
                "Book vehicles and manage rental returns"
        );
        subtitle.setForeground(Color.GRAY);

        panel.add(title);
        panel.add(subtitle);

        return panel;
    }

    private JPanel createMainContent() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);

        panel.add(createBookingForm(), BorderLayout.WEST);

        rentalTable.setRowHeight(30);
        rentalTable.setAutoCreateRowSorter(true);
        rentalTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(rentalTable);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(
                BorderFactory.createTitledBorder("Rental Records")
        );

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchPanel.add(new JLabel("Search Rental:"));
        searchPanel.add(searchField);

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {

            private void filterRentals() {

                String text = searchField.getText().trim();

                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(
                            RowFilter.regexFilter(
                                    "(?i)" + java.util.regex.Pattern.quote(text)
                            )
                    );
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                filterRentals();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterRentals();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterRentals();
            }
        });

        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBookingForm() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        panel.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 3, 8, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addField(panel, gbc, 0, "Select Customer", customerBox);
        addField(panel, gbc, 2, "Select Vehicle", vehicleBox);
        addField(panel, gbc, 4, "Rental Date (YYYY-MM-DD)", rentalDateField);
        addField(panel, gbc, 6, "Due Date (YYYY-MM-DD)", dueDateField);

        JButton bookButton = new JButton("Book Vehicle");
        JButton returnButton = new JButton("Return Selected Rental");
        JButton refreshButton = new JButton("Refresh");
        JButton invoiceButton = new JButton("View Invoice");

        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(bookButton, gbc);

        gbc.gridy = 10;
        panel.add(returnButton, gbc);

        gbc.gridy = 11;
        panel.add(refreshButton, gbc);

        gbc.gridy = 12;
        panel.add(invoiceButton, gbc);

        bookButton.addActionListener(e -> bookVehicle());
        returnButton.addActionListener(e -> returnVehicle());
        refreshButton.addActionListener(e -> refreshData());
        invoiceButton.addActionListener(e -> viewInvoice());

        return panel;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent component) {

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridy = row + 1;
        panel.add(component, gbc);
    }

    private void loadCustomersAndVehicles() {

        try {
            customerBox.removeAllItems();
            vehicleBox.removeAllItems();

            List<Customer> customers = customerDAO.getAllCustomers();
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();

            for (Customer customer : customers) {
                customerBox.addItem(customer);
            }

            for (Vehicle vehicle : vehicles) {
                if ("AVAILABLE".equalsIgnoreCase(vehicle.getStatus())) {
                    vehicleBox.addItem(vehicle);
                }
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void bookVehicle() {

        try {

            Customer customer =
                    (Customer) customerBox.getSelectedItem();

            Vehicle vehicle =
                    (Vehicle) vehicleBox.getSelectedItem();

            if (customer == null || vehicle == null) {
                showError("Please add a customer and an available vehicle first.");
                return;
            }

            LocalDate rentalDate =
                    LocalDate.parse(rentalDateField.getText().trim());

            LocalDate dueDate =
                    LocalDate.parse(dueDateField.getText().trim());

            Rental rental = new Rental();

            rental.setCustomerId(customer.getCustomerId());
            rental.setVehicleId(vehicle.getVehicleId());
            rental.setRentalDate(rentalDate);
            rental.setDueDate(dueDate);

            rentalService.bookVehicle(rental);

            JOptionPane.showMessageDialog(
                    this, "Vehicle booked successfully!"
            );

            refreshData();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void returnVehicle() {

        int selectedRow = rentalTable.getSelectedRow();

        if (selectedRow == -1) {
            showError("Please select a rental record from the table.");
            return;
        }

        int modelRow = rentalTable.convertRowIndexToModel(selectedRow);

        int rentalId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        String status = tableModel.getValueAt(modelRow, 7).toString();

        if (!"ACTIVE".equalsIgnoreCase(status)) {
            showError("This rental has already been completed.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Confirm return of this vehicle today?",
                "Return Vehicle",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            try {

                rentalService.returnVehicle(
                        rentalId, LocalDate.now()
                );

                JOptionPane.showMessageDialog(
                        this, "Vehicle returned successfully!"
                );

                refreshData();

            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    private void loadRentals() {

        try {

            tableModel.setRowCount(0);

            for (Rental rental : rentalService.getAllRentals()) {

                tableModel.addRow(new Object[]{
                        rental.getRentalId(),
                        rental.getCustomerId(),
                        rental.getVehicleId(),
                        rental.getRentalDate(),
                        rental.getDueDate(),
                        rental.getActualReturnDate(),
                        rental.getTotalAmount(),
                        rental.getStatus()
                });
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void refreshData() {
        loadCustomersAndVehicles();
        loadRentals();
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Rental Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}