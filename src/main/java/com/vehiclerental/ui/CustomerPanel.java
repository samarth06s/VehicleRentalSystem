
package com.vehiclerental.ui;

import com.vehiclerental.model.Customer;
import com.vehiclerental.service.CustomerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class CustomerPanel extends JPanel {

    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color DARK = new Color(30, 41, 59);
    private static final Color MUTED = new Color(110, 120, 135);

    private final CustomerService customerService = new CustomerService();

    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField licenseField = new JTextField();

    private int selectedCustomerId = -1;

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{"ID", "Name", "Phone", "Email", "License Number"},
                    0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable customerTable = new JTable(tableModel);

    private final JTextField searchField = new JTextField(20);

    private final TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(tableModel);

    public CustomerPanel() {

        setLayout(new BorderLayout(20, 22));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(28, 30, 28, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadCustomers();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Customer Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "Register, update and manage your rental customers"
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

        panel.add(createFormPanel(), BorderLayout.WEST);

        // Customer table styling
        customerTable.setRowHeight(38);
        customerTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        customerTable.setForeground(DARK);
        customerTable.setBackground(Color.WHITE);
        customerTable.setSelectionBackground(new Color(219, 234, 254));
        customerTable.setSelectionForeground(DARK);
        customerTable.setShowGrid(false);
        customerTable.setIntercellSpacing(new Dimension(0, 1));
        customerTable.setFillsViewportHeight(true);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setAutoCreateRowSorter(false);
        customerTable.setRowSorter(sorter);

        customerTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        customerTable.getTableHeader().setBackground(
                new Color(241, 245, 249)
        );

        customerTable.getTableHeader().setForeground(DARK);
        customerTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );
        customerTable.getTableHeader().setReorderingAllowed(false);

        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Table container
        JPanel tablePanel = new JPanel(new BorderLayout(12, 15));
        tablePanel.setBackground(Color.WHITE);

        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 234, 240)
                ),
                new EmptyBorder(18, 18, 18, 18)
        ));

        // Search area
        JLabel searchLabel = new JLabel("Search Customers");
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

                    private void filterCustomers() {

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
                        filterCustomers();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        filterCustomers();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        filterCustomers();
                    }
                }
        );

        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {

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

        JLabel formTitle = new JLabel("Customer Details");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        formTitle.setForeground(DARK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 3, 14, 3);

        panel.add(formTitle, gbc);

        styleInput(nameField);
        styleInput(phoneField);
        styleInput(emailField);
        styleInput(licenseField);

        gbc.insets = new Insets(5, 3, 5, 3);

        addField(panel, gbc, 1, "Customer Name", nameField);
        addField(panel, gbc, 2, "Phone Number", phoneField);
        addField(panel, gbc, 3, "Email Address", emailField);
        addField(panel, gbc, 4, "Driving License Number", licenseField);

        JButton addButton = createButton(
                "Add Customer", new Color(37, 99, 235)
        );

        JButton updateButton = createButton(
                "Update Customer", new Color(14, 165, 180)
        );

        JButton deleteButton = createButton(
                "Delete Customer", new Color(220, 65, 65)
        );

        JButton clearButton = createButton(
                "Clear Form", new Color(100, 116, 139)
        );

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 10;
        gbc.insets = new Insets(8, 3, 4, 3);

        panel.add(addButton, gbc);

        gbc.gridy = 11;
        panel.add(updateButton, gbc);

        gbc.gridy = 12;
        panel.add(deleteButton, gbc);

        gbc.gridy = 13;
        gbc.insets = new Insets(4, 3, 0, 3);
        panel.add(clearButton, gbc);

        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        clearButton.addActionListener(e -> clearForm());

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

        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 12, 10, 12));

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
            JComponent field) {

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

        panel.add(field, gbc);
    }

    private Customer getCustomerFromForm() {

        return new Customer(
                nameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                licenseField.getText().trim()
        );
    }

    private void addCustomer() {

        try {
            Customer customer = getCustomerFromForm();

            customerService.addCustomer(customer);

            JOptionPane.showMessageDialog(
                    this, "Customer added successfully!"
            );

            clearForm();
            loadCustomers();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void updateCustomer() {

        if (selectedCustomerId == -1) {
            showError("Please select a customer from the table.");
            return;
        }

        try {
            Customer customer = getCustomerFromForm();
            customer.setCustomerId(selectedCustomerId);

            customerService.updateCustomer(customer);

            JOptionPane.showMessageDialog(
                    this, "Customer updated successfully!"
            );

            clearForm();
            loadCustomers();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void deleteCustomer() {

        if (selectedCustomerId == -1) {
            showError("Please select a customer to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this customer?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            try {
                customerService.deleteCustomer(selectedCustomerId);

                JOptionPane.showMessageDialog(
                        this, "Customer deleted successfully!"
                );

                clearForm();
                loadCustomers();

            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    private void loadCustomers() {

        try {
            tableModel.setRowCount(0);

            for (Customer customer : customerService.getAllCustomers()) {

                tableModel.addRow(new Object[]{
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getPhone(),
                        customer.getEmail(),
                        customer.getLicenseNumber()
                });
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void fillFormFromSelectedRow() {

        int row = customerTable.getSelectedRow();

        if (row == -1) {
            return;
        }

        int modelRow = customerTable.convertRowIndexToModel(row);

        selectedCustomerId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        nameField.setText(
                tableModel.getValueAt(modelRow, 1).toString()
        );

        phoneField.setText(
                tableModel.getValueAt(modelRow, 2).toString()
        );

        Object email = tableModel.getValueAt(modelRow, 3);
        emailField.setText(email == null ? "" : email.toString());

        licenseField.setText(
                tableModel.getValueAt(modelRow, 4).toString()
        );
    }

    private void clearForm() {

        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        licenseField.setText("");

        selectedCustomerId = -1;
        customerTable.clearSelection();
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
