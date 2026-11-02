package com.vehiclerental.ui;

import com.vehiclerental.model.Customer;
import com.vehiclerental.service.CustomerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

public class CustomerPanel extends JPanel {

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

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadCustomers();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setOpaque(false);

        JLabel title = new JLabel("Customer Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel subtitle = new JLabel(
                "Register and manage rental customers"
        );
        subtitle.setForeground(Color.GRAY);

        panel.add(title);
        panel.add(subtitle);

        return panel;
    }

    private JPanel createMainContent() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);

        panel.add(createFormPanel(), BorderLayout.WEST);

        customerTable.setRowHeight(30);
        customerTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        customerTable.setAutoCreateRowSorter(true);
        customerTable.setRowSorter(sorter);

        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(customerTable);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(
                BorderFactory.createTitledBorder("Registered Customers")
        );

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchPanel.add(new JLabel("Search Customer:"));
        searchPanel.add(searchField);

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {

            private void filterCustomers() {

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
        });

        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        panel.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 3, 7, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addField(panel, gbc, 0, "Customer Name", nameField);
        addField(panel, gbc, 2, "Phone Number", phoneField);
        addField(panel, gbc, 4, "Email Address", emailField);
        addField(panel, gbc, 6, "Driving License Number", licenseField);

        JButton addButton = new JButton("Add Customer");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 9;

        panel.add(addButton, gbc);

        gbc.gridy = 10;
        panel.add(updateButton, gbc);

        gbc.gridy = 11;
        panel.add(deleteButton, gbc);

        gbc.gridy = 12;
        panel.add(clearButton, gbc);

        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        clearButton.addActionListener(e -> clearForm());

        return panel;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent field) {

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridy = row + 1;
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

        if (row == -1) return;

        int modelRow = customerTable.convertRowIndexToModel(row);

        selectedCustomerId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString()
        );

        nameField.setText(tableModel.getValueAt(modelRow, 1).toString());
        phoneField.setText(tableModel.getValueAt(modelRow, 2).toString());

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