
package com.vehiclerental.ui;

import com.vehiclerental.dao.VehicleDAO;
import com.vehiclerental.dao.CustomerDAO;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.service.RentalService;
import com.vehiclerental.service.InvoiceService;
import com.vehiclerental.util.InvoicePDFExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.nio.file.Path;

public class RentalPanel extends JPanel {

    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color DARK = new Color(30, 41, 59);
    private static final Color MUTED = new Color(110, 120, 135);

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
                    new String[]{
                            "Rental ID", "Customer ID", "Vehicle ID",
                            "Rental Date", "Due Date", "Return Date",
                            "Amount", "Status"
                    },
                    0
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

        setLayout(new BorderLayout(20, 22));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(28, 30, 28, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadCustomersAndVehicles();
        loadRentals();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Rental Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "Book vehicles, manage returns and generate rental invoices"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(subtitle);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createMainContent() {

        JPanel panel = new JPanel(new BorderLayout(18, 0));
        panel.setOpaque(false);

        panel.add(createBookingForm(), BorderLayout.WEST);

        // Rental table styling
        rentalTable.setRowHeight(38);
        rentalTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rentalTable.setForeground(DARK);
        rentalTable.setBackground(Color.WHITE);
        rentalTable.setSelectionBackground(new Color(219, 234, 254));
        rentalTable.setSelectionForeground(DARK);
        rentalTable.setShowGrid(false);
        rentalTable.setIntercellSpacing(new Dimension(0, 1));
        rentalTable.setFillsViewportHeight(true);
        rentalTable.setAutoCreateRowSorter(false);
        rentalTable.setRowSorter(sorter);
        rentalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        rentalTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        rentalTable.getTableHeader().setBackground(
                new Color(241, 245, 249)
        );

        rentalTable.getTableHeader().setForeground(DARK);
        rentalTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );
        rentalTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(rentalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Rental records container
        JPanel tablePanel = new JPanel(new BorderLayout(12, 15));
        tablePanel.setBackground(Color.WHITE);

        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 234, 240)
                ),
                new EmptyBorder(18, 18, 18, 18)
        ));

        // Search area
        JLabel searchLabel = new JLabel("Search Rental Records");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchLabel.setForeground(DARK);

        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(230, 36));

        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(215, 222, 232)
                ),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JPanel searchPanel = new JPanel(new BorderLayout(12, 0));
        searchPanel.setOpaque(false);

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Search functionality
        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

                    private void filterRentals() {

                        String text = searchField.getText().trim();

                        if (text.isEmpty()) {
                            sorter.setRowFilter(null);
                        } else {
                            sorter.setRowFilter(
                                    RowFilter.regexFilter(
                                            "(?i)" +
                                                    java.util.regex.Pattern.quote(text)
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
                }
        );

        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBookingForm() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 234, 240)
                ),
                new EmptyBorder(18, 16, 18, 16)
        ));

        panel.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 3, 5, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel formTitle = new JLabel("Book a Vehicle");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        formTitle.setForeground(DARK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 3, 14, 3);

        panel.add(formTitle, gbc);

        styleInput(customerBox);
        styleInput(vehicleBox);
        styleInput(rentalDateField);
        styleInput(dueDateField);

        gbc.insets = new Insets(5, 3, 5, 3);

        addField(panel, gbc, 1, "Select Customer", customerBox);
        addField(panel, gbc, 2, "Select Vehicle", vehicleBox);
        addField(panel, gbc, 3, "Rental Date (YYYY-MM-DD)", rentalDateField);
        addField(panel, gbc, 4, "Due Date (YYYY-MM-DD)", dueDateField);

        JButton bookButton = createButton(
                "Book Vehicle", new Color(37, 99, 235)
        );

        JButton returnButton = createButton(
                "Return Selected Rental", new Color(14, 165, 180)
        );

        JButton refreshButton = createButton(
                "Refresh Records", new Color(100, 116, 139)
        );

        JButton invoiceButton = createButton(
                "View Invoice", new Color(124, 88, 190)
        );

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 10;
        gbc.insets = new Insets(8, 3, 4, 3);

        panel.add(bookButton, gbc);

        gbc.gridy = 11;
        panel.add(returnButton, gbc);

        gbc.gridy = 12;
        panel.add(refreshButton, gbc);

        gbc.gridy = 13;
        gbc.insets = new Insets(4, 3, 0, 3);
        panel.add(invoiceButton, gbc);

        bookButton.addActionListener(e -> bookVehicle());
        returnButton.addActionListener(e -> returnVehicle());
        refreshButton.addActionListener(e -> refreshData());
        invoiceButton.addActionListener(e -> viewInvoice());

        return panel;
    }

    private void styleInput(JComponent component) {

        component.setFont(new Font("SansSerif", Font.PLAIN, 13));
        component.setPreferredSize(new Dimension(240, 36));

        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(215, 222, 232)
                ),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 8, 10, 8));

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        button.setPreferredSize(new Dimension(240, 38));

        return button;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent component) {

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        labelComponent.setForeground(DARK);

        gbc.gridx = 0;
        gbc.gridy = row * 2 - 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;

        panel.add(labelComponent, gbc);

        gbc.gridy = row * 2;

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
                showError(
                        "Please add a customer and an available vehicle first."
                );
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
            invoiceArea.setFont(
                    new Font("Monospaced", Font.PLAIN, 13)
            );

            JScrollPane scrollPane = new JScrollPane(invoiceArea);
            scrollPane.setPreferredSize(new Dimension(500, 550));

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

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to generate invoice: " + e.getMessage(),
                    "Invoice Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
