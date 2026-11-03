
package com.vehiclerental.ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final Map<String, JButton> navigationButtons =
            new LinkedHashMap<>();

    private static final Color SIDEBAR_COLOR =
            new Color(24, 38, 62);

    private static final Color SIDEBAR_HOVER =
            new Color(42, 60, 88);

    private static final Color ACTIVE_COLOR =
            new Color(48, 110, 210);

    private static final Color BACKGROUND_COLOR =
            new Color(245, 247, 251);

    private static final Color TEXT_COLOR =
            new Color(35, 45, 60);

    public MainFrame() {

        setTitle("Drive Rent | Vehicle Rental Management System");
        setSize(1250, 780);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Application screens
        contentPanel.add(new DashboardPanel(), "Dashboard");
        contentPanel.add(new VehiclePanel(), "Vehicles");
        contentPanel.add(new CustomerPanel(), "Customers");
        contentPanel.add(new RentalPanel(), "Rentals");

        // Main layout
        add(createSidebar(), BorderLayout.WEST);
        add(createHeader(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        showScreen("Dashboard");
    }

    // Sidebar
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(235, 0));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setLayout(new BoxLayout(
                sidebar, BoxLayout.Y_AXIS));

        // Logo / Application name
        JLabel logo = new JLabel("DRIVE RENT");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font(
                "SansSerif", Font.BOLD, 24));

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(
                30, 25, 8, 10));

        sidebar.add(logo);

        JLabel subtitle = new JLabel("VEHICLE MANAGEMENT");
        subtitle.setForeground(new Color(165, 180, 200));
        subtitle.setFont(new Font(
                "SansSerif", Font.PLAIN, 10));

        subtitle.setBorder(BorderFactory.createEmptyBorder(
                0, 27, 40, 10));

        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(subtitle);

        // Navigation heading
        JLabel menuLabel = new JLabel("MAIN MENU");
        menuLabel.setForeground(new Color(150, 165, 185));
        menuLabel.setFont(new Font(
                "SansSerif", Font.BOLD, 11));

        menuLabel.setBorder(BorderFactory.createEmptyBorder(
                0, 27, 15, 10));

        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(menuLabel);

        addNavigationButton(sidebar, "Dashboard", "Dashboard");
        addNavigationButton(sidebar, "Vehicles", "Vehicles");
        addNavigationButton(sidebar, "Customers", "Customers");
        addNavigationButton(sidebar, "Rentals", "Rentals");

        sidebar.add(Box.createVerticalGlue());

        // Footer
        JLabel footer = new JLabel(
                "Vehicle Rental System");

        footer.setForeground(new Color(165, 180, 200));
        footer.setFont(new Font(
                "SansSerif", Font.PLAIN, 12));

        footer.setBorder(BorderFactory.createEmptyBorder(
                20, 25, 25, 10));

        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(footer);

        return sidebar;
    }

    // Navigation buttons
    private void addNavigationButton(
            JPanel sidebar,
            String text,
            String cardName) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 46));

        button.setPreferredSize(new Dimension(210, 46));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setHorizontalAlignment(
                SwingConstants.LEFT);

        button.setFont(new Font(
                "SansSerif", Font.BOLD, 14));

        button.setForeground(Color.WHITE);
        button.setBackground(SIDEBAR_COLOR);

        button.setBorder(BorderFactory.createEmptyBorder(
                10, 25, 10, 10));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);

        button.setCursor(new Cursor(
                Cursor.HAND_CURSOR));

        button.addActionListener(e -> showScreen(cardName));

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {

                if (!button.getBackground().equals(ACTIVE_COLOR)) {
                    button.setBackground(SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {

                if (!button.getBackground().equals(ACTIVE_COLOR)) {
                    button.setBackground(SIDEBAR_COLOR);
                }
            }
        });

        navigationButtons.put(cardName, button);

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(8));
    }

    // Switch screens and highlight selected menu
    private void showScreen(String cardName) {

        cardLayout.show(contentPanel, cardName);

        for (Map.Entry<String, JButton> entry :
                navigationButtons.entrySet()) {

            JButton button = entry.getValue();

            if (entry.getKey().equals(cardName)) {
                button.setBackground(ACTIVE_COLOR);
                button.setFont(new Font(
                        "SansSerif", Font.BOLD, 14));
            } else {
                button.setBackground(SIDEBAR_COLOR);
                button.setFont(new Font(
                        "SansSerif", Font.PLAIN, 14));
            }
        }
    }

    // Header
    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0,
                        new Color(230, 233, 240)),
                BorderFactory.createEmptyBorder(
                        18, 28, 18, 28)
        ));

        JLabel title = new JLabel(
                "Vehicle Rental Management System");

        title.setFont(new Font(
                "SansSerif", Font.BOLD, 20));

        title.setForeground(TEXT_COLOR);

        JLabel admin = new JLabel("Administrator");

        admin.setFont(new Font(
                "SansSerif", Font.PLAIN, 13));

        admin.setForeground(new Color(100, 110, 125));

        header.add(title, BorderLayout.WEST);
        header.add(admin, BorderLayout.EAST);

        return header;
    }
}
