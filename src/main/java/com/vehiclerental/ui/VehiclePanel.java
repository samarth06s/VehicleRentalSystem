
package com.vehiclerental.ui;

import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.VehicleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.math.BigDecimal;

public class VehiclePanel extends JPanel {

    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color DARK = new Color(30, 41, 59);
    private static final Color MUTED = new Color(110, 120, 135);
    private static final Color BLUE = new Color(37, 99, 235);

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

        setLayout(new BorderLayout(20, 22));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(28, 30, 28, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadVehicles();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Vehicle Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "Add, update, view and manage your vehicles"
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

        // Table styling
        vehicleTable.setRowHeight(38);
        vehicleTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        vehicleTable.setForeground(DARK);
        vehicleTable.setBackground(Color.WHITE);
        vehicleTable.setSelectionBackground(new Color(219, 234, 254));
        vehicleTable.setSelectionForeground(DARK);
        vehicleTable.setShowGrid(false);
        vehicleTable.setIntercellSpacing(new Dimension(0, 1));
        vehicleTable.setFillsViewportHeight(true);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.setAutoCreateRowSorter(false);
        vehicleTable.setRowSorter(sorter);

        vehicleTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );
        vehicleTable.getTableHeader().setBackground(
                new Color(241, 245, 249)
        );
        vehicleTable.getTableHeader().setForeground(DARK);
        vehicleTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );
        vehicleTable.getTableHeader().setReorderingAllowed(false);

        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tablePanel = new JPanel(new BorderLayout(12, 15));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 234, 240)
                ),
                new EmptyBorder(18, 18, 18, 18)
        ));

        // Search area
        JLabel searchLabel = new JLabel("Search Vehicles");
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

                    private void filterVehicles() {

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
        panel.setPreferredSize(new Dimension(275, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 3, 5, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel formTitle = new JLabel("Vehicle Details");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        formTitle.setForeground(DARK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 3, 14, 3);
        panel.add(formTitle, gbc);

        styleInput(brandField);
        styleInput(modelField);
        styleInput(typeField);
        styleInput(registrationField);
        styleInput(rentField);
        styleInput(statusBox);

        gbc.insets = new Insets(5, 3, 5, 3);

        addField(panel, gbc, 1, "Brand", brandField);
        addField(panel, gbc, 2, "Model", modelField);
        addField(panel, gbc, 3, "Vehicle Type", typeField);
        addField(panel, gbc, 4, "Registration Number", registrationField);
        addField(panel, gbc, 5, "Rent Per Day (₹)", rentField);
        addField(panel, gbc, 6, "Status", statusBox);

        JButton addButton = createButton(
                "Add Vehicle", new Color(37, 99, 235)
        );

        JButton updateButton = createButton(
                "Update Vehicle", new Color(14, 165, 180)
        );

        JButton deleteButton = createButton(
                "Delete Vehicle", new Color(220, 65, 65)
        );

        JButton clearButton = createButton(
                "Clear Form", new Color(100, 116, 139)
        );

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 3, 4, 3);

        gbc.gridy = 13;
        panel.add(addButton, gbc);

        gbc.gridy = 14;
        panel.add(updateButton, gbc);

        gbc.gridy = 15;
        panel.add(deleteButton, gbc);

        gbc.gridy = 16;
        gbc.insets = new Insets(4, 3, 0, 3);
        panel.add(clearButton, gbc);

        addButton.addActionListener(e -> addVehicle());
        updateButton.addActionListener(e -> updateVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        clearButton.addActionListener(e -> clearForm());

        return panel;
    }

    private void styleInput(JComponent component) {

        component.setFont(new Font("SansSerif", Font.PLAIN, 13));
        component.setPreferredSize(new Dimension(220, 34));

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
        button.setPreferredSize(new Dimension(220, 38));

        return button;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent field) {

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = row * 2 - 1;
        gbc.weightx = 1;

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        labelComponent.setForeground(DARK);

        panel.add(labelComponent, gbc);

        gbc.gridy = row * 2;

        panel.add(field, gbc);
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
                Integer.parseInt(
                        tableModel.getValueAt(modelRow, 0).toString()
                );

        brandField.setText(tableModel.getValueAt(modelRow, 1).toString());
        modelField.setText(tableModel.getValueAt(modelRow, 2).toString());
        typeField.setText(tableModel.getValueAt(modelRow, 3).toString());
        registrationField.setText(
                tableModel.getValueAt(modelRow, 4).toString()
        );
        rentField.setText(tableModel.getValueAt(modelRow, 5).toString());
        statusBox.setSelectedItem(
                tableModel.getValueAt(modelRow, 6).toString()
        );
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
