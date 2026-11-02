
package com.vehiclerental.ui;

import com.vehiclerental.dao.DashboardDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    private final JLabel totalVehiclesValue = new JLabel("...");
    private final JLabel availableVehiclesValue = new JLabel("...");
    private final JLabel rentedVehiclesValue = new JLabel("...");
    private final JLabel totalCustomersValue = new JLabel("...");
    private final JLabel activeRentalsValue = new JLabel("...");
    private final JLabel completedRentalsValue = new JLabel("...");

    private final JButton refreshButton = new JButton("Refresh");

    private final VehicleStatusChart vehicleStatusChart =
            new VehicleStatusChart();

    public DashboardPanel() {

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
                "Overview of your vehicle rental business"
        );
        subtitle.setForeground(Color.GRAY);

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> loadDashboardData());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(heading, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Six dashboard statistics cards
        JPanel cardsPanel = new JPanel(
                new GridLayout(2, 3, 20, 20)
        );
        cardsPanel.setOpaque(false);

        cardsPanel.add(createStatCard(
                "Total Vehicles", totalVehiclesValue));

        cardsPanel.add(createStatCard(
                "Available Vehicles", availableVehiclesValue));

        cardsPanel.add(createStatCard(
                "Rented Vehicles", rentedVehiclesValue));

        cardsPanel.add(createStatCard(
                "Total Customers", totalCustomersValue));

        cardsPanel.add(createStatCard(
                "Active Rentals", activeRentalsValue));

        cardsPanel.add(createStatCard(
                "Completed Rentals", completedRentalsValue));

        JPanel dashboardContent = new JPanel(
                new BorderLayout(20, 20)
        );

        dashboardContent.setOpaque(false);

// Statistics at the top
        dashboardContent.add(cardsPanel, BorderLayout.NORTH);

// Pie chart below statistics
        dashboardContent.add(
                vehicleStatusChart,
                BorderLayout.CENTER
        );

        add(dashboardContent, BorderLayout.CENTER);
        loadDashboardData();
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {

        JPanel card = new JPanel(new BorderLayout(10, 15));

        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 235)),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );
        titleLabel.setForeground(Color.GRAY);

        valueLabel.setFont(
                new Font("SansSerif", Font.BOLD, 32)
        );
        valueLabel.setForeground(new Color(30, 50, 80));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void loadDashboardData() {

        refreshButton.setEnabled(false);

        new SwingWorker<int[], Void>() {

            @Override
            protected int[] doInBackground() throws SQLException {

                return new int[]{
                        dashboardDAO.getTotalVehicles(),
                        dashboardDAO.getAvailableVehicles(),
                        dashboardDAO.getRentedVehicles(),
                        dashboardDAO.getTotalCustomers(),
                        dashboardDAO.getActiveRentals(),
                        dashboardDAO.getCompletedRentals()
                };
            }

            @Override
            protected void done() {

                try {

                    int[] stats = get();

                    totalVehiclesValue.setText(
                            String.valueOf(stats[0]));

                    availableVehiclesValue.setText(
                            String.valueOf(stats[1]));

                    rentedVehiclesValue.setText(
                            String.valueOf(stats[2]));

                    totalCustomersValue.setText(
                            String.valueOf(stats[3]));

                    activeRentalsValue.setText(
                            String.valueOf(stats[4]));

                    completedRentalsValue.setText(
                            String.valueOf(stats[5]));

                    // Update vehicle availability chart
                    vehicleStatusChart.setCounts(
                            stats[1],  // Available vehicles
                            stats[2]   // Rented vehicles
                    );

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(
                            DashboardPanel.this,
                            "Unable to load dashboard data:\n"
                                    + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                } finally {
                    refreshButton.setEnabled(true);
                }
            }

        }.execute();
    }
}
