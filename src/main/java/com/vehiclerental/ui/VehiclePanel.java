package com.vehiclerental.ui;

import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.VehicleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

public class VehiclePanel extends JPanel {

    private final VehicleService vehicleService = new VehicleService();

    private final JTextField brandField = new JTextField();
    private final JTextField modelField = new JTextField();
    private final JTextField typeField = new JTextField();
    private final JTextField registrationField = new JTextField();
    private final JTextField rentField = new JTextField();

    private final JComboBox<String> statusBox =
            new JComboBox<>(new String[]{"AVAILABLE", "RENTED"});

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{"ID", "Brand", "Model", "Type",
                            "Registration", "Rent/Day", "Status"}, 0) {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable vehicleTable = new JTable(tableModel);

    private final JTextField searchField = new JTextField(20);

    private final TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(tableModel);

    private int selectedVehicleId = -1;

    public VehiclePanel() {

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadVehicles();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Vehicle Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel subtitle = new JLabel(
                "Add, update, view and manage vehicles"
        );
        subtitle.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(subtitle);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createMainContent() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);

        panel.add(createFormPanel(), BorderLayout.WEST);

        vehicleTable.setRowHeight(30);
        vehicleTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        vehicleTable.setAutoCreateRowSorter(true);
        vehicleTable.setRowSorter(sorter);

        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                "Registered Vehicles"
        ));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchPanel.add(new JLabel("Search Vehicle:"));
        searchPanel.add(searchField);

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {

            private void filterVehicles() {

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
                filterVehicles();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterVehicles();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterVehicles();
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
        panel.setPreferredSize(new Dimension(270, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 3, 7, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addField(panel, gbc, 0, "Brand", brandField);
        addField(panel, gbc, 1, "Model", modelField);
        addField(panel, gbc, 2, "Vehicle Type", typeField);
        addField(panel, gbc, 3, "Registration Number", registrationField);
        addField(panel, gbc, 4, "Rent Per Day (₹)", rentField);
        addField(panel, gbc, 5, "Status", statusBox);

        JButton addButton = new JButton("Add Vehicle");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 3, 10, 3);

        panel.add(addButton, gbc);

        gbc.gridy = 13;
        panel.add(updateButton, gbc);

        gbc.gridy = 14;
        panel.add(deleteButton, gbc);

        gbc.gridy = 15;
        panel.add(clearButton, gbc);

        addButton.addActionListener(e -> addVehicle());
        updateButton.addActionListener(e -> updateVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        clearButton.addActionListener(e -> clearForm());

        return panel;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent field) {

        gbc.gridy = row;
        gbc.gridx = 0;

        panel.add(new JLabel(label), gbc);

        gbc.gridy = row + 1;
        panel.add(field, gbc);

        gbc.gridy = row + 1;
    }

    private Vehicle getVehicleFromForm() {

        return new Vehicle(
                brandField.getText().trim(),
                modelField.getText().trim(),
                typeField.getText().trim(),
                registrationField.getText().trim(),
                new BigDecimal(rentField.getText().trim()),
                (String) statusBox.getSelectedItem()
        );
    }

    private void addVehicle() {

        try {
            Vehicle vehicle = getVehicleFromForm();

            vehicleService.addVehicle(vehicle);

            JOptionPane.showMessageDialog(
                    this, "Vehicle added successfully!"
            );

            clearForm();
            loadVehicles();

        } catch (NumberFormatException e) {
            showError("Rent must be a valid number.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void updateVehicle() {

        if (selectedVehicleId == -1) {
            showError("Please select a vehicle from the table.");
            return;
        }

        try {
            Vehicle vehicle = getVehicleFromForm();
            vehicle.setVehicleId(selectedVehicleId);

            vehicleService.updateVehicle(vehicle);

            JOptionPane.showMessageDialog(
                    this, "Vehicle updated successfully!"
            );

            clearForm();
            loadVehicles();

        } catch (NumberFormatException e) {
            showError("Rent must be a valid number.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void deleteVehicle() {

        if (selectedVehicleId == -1) {
            showError("Please select a vehicle to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this vehicle?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            try {
                vehicleService.deleteVehicle(selectedVehicleId);

                JOptionPane.showMessageDialog(
                        this, "Vehicle deleted successfully!"
                );

                clearForm();
                loadVehicles();

            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    private void loadVehicles() {

        try {
            tableModel.setRowCount(0);

            for (Vehicle vehicle : vehicleService.getAllVehicles()) {

                tableModel.addRow(new Object[]{
                        vehicle.getVehicleId(),
                        vehicle.getBrand(),
                        vehicle.getModel(),
                        vehicle.getVehicleType(),
                        vehicle.getRegistrationNumber(),
                        vehicle.getRentPerDay(),
                        vehicle.getStatus()
                });
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void fillFormFromSelectedRow() {

        int row = vehicleTable.getSelectedRow();

        if (row == -1) {
            return;
        }

        int modelRow = vehicleTable.convertRowIndexToModel(row);

        selectedVehicleId =
                Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());

        brandField.setText(tableModel.getValueAt(modelRow, 1).toString());
        modelField.setText(tableModel.getValueAt(modelRow, 2).toString());
        typeField.setText(tableModel.getValueAt(modelRow, 3).toString());
        registrationField.setText(tableModel.getValueAt(modelRow, 4).toString());
        rentField.setText(tableModel.getValueAt(modelRow, 5).toString());
        statusBox.setSelectedItem(tableModel.getValueAt(modelRow, 6).toString());
    }

    private void clearForm() {

        brandField.setText("");
        modelField.setText("");
        typeField.setText("");
        registrationField.setText("");
        rentField.setText("");

        statusBox.setSelectedItem("AVAILABLE");

        selectedVehicleId = -1;
        vehicleTable.clearSelection();
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