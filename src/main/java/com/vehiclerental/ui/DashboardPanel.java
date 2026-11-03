
package com.vehiclerental.ui;

import com.vehiclerental.dao.DashboardDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {

    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color DARK = new Color(30, 41, 59);
    private static final Color MUTED = new Color(110, 120, 135);

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

        setLayout(new BorderLayout(20, 25));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(28, 30, 28, 30));

        // Heading
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "Overview of your vehicle rental business"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);

        heading.add(title);
        heading.add(Box.createVerticalStrut(6));
        heading.add(subtitle);

        // Refresh button styling
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(37, 99, 235));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(
                new EmptyBorder(11, 22, 11, 22)
        );
        refreshButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        refreshButton.addActionListener(e -> loadDashboardData());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(heading, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Statistics cards
        JPanel cardsPanel = new JPanel(
                new GridLayout(2, 3, 18, 18)
        );
        cardsPanel.setOpaque(false);

        cardsPanel.add(createStatCard(
                "Total Vehicles",
                totalVehiclesValue,
                new Color(59, 130, 246)
        ));

        cardsPanel.add(createStatCard(
                "Available Vehicles",
                availableVehiclesValue,
                new Color(22, 163, 74)
        ));

        cardsPanel.add(createStatCard(
                "Rented Vehicles",
                rentedVehiclesValue,
                new Color(234, 138, 30)
        ));

        cardsPanel.add(createStatCard(
                "Total Customers",
                totalCustomersValue,
                new Color(14, 165, 180)
        ));

        cardsPanel.add(createStatCard(
                "Active Rentals",
                activeRentalsValue,
                new Color(139, 92, 246)
        ));

        cardsPanel.add(createStatCard(
                "Completed Rentals",
                completedRentalsValue,
                new Color(16, 145, 120)
        ));

        // Dashboard content
        JPanel dashboardContent = new JPanel(
                new BorderLayout(20, 22)
        );
        dashboardContent.setOpaque(false);

        dashboardContent.add(cardsPanel, BorderLayout.NORTH);
        dashboardContent.add(
                vehicleStatusChart,
                BorderLayout.CENTER
        );

        add(dashboardContent, BorderLayout.CENTER);

        loadDashboardData();
    }

    private JPanel createStatCard(
            String title,
            JLabel valueLabel,
            Color accentColor
    ) {

        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 234, 240)
                ),
                new EmptyBorder(18, 18, 18, 18)
        ));

        // Colored accent strip
        JPanel accent = new JPanel();
        accent.setBackground(accentColor);
        accent.setPreferredSize(new Dimension(5, 10));

        // Card text
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );
        titleLabel.setForeground(MUTED);

        valueLabel.setFont(
                new Font("SansSerif", Font.BOLD, 32)
        );
        valueLabel.setForeground(DARK);

        JPanel textPanel = new JPanel(
                new BorderLayout(0, 12)
        );
        textPanel.setOpaque(false);

        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.CENTER);

        card.add(accent, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private void loadDashboardData() {

        refreshButton.setEnabled(false);
        refreshButton.setText("Loading...");

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
                            String.valueOf(stats[0])
                    );

                    availableVehiclesValue.setText(
                            String.valueOf(stats[1])
                    );

                    rentedVehiclesValue.setText(
                            String.valueOf(stats[2])
                    );

                    totalCustomersValue.setText(
                            String.valueOf(stats[3])
                    );

                    activeRentalsValue.setText(
                            String.valueOf(stats[4])
                    );

                    completedRentalsValue.setText(
                            String.valueOf(stats[5])
                    );

                    vehicleStatusChart.setCounts(
                            stats[1],
                            stats[2]
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
                    refreshButton.setText("Refresh");
                }
            }

        }.execute();
    }
}
