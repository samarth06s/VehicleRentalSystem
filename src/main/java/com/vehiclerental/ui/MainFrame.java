
package com.vehiclerental.ui;

import javax.swing.*;
import java.awt.*;
import com.vehiclerental.ui.DashboardPanel;
import com.vehiclerental.ui.CustomerPanel;
import com.vehiclerental.ui.RentalPanel;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private static final Color SIDEBAR_COLOR =
            new Color(24, 38, 62);

    private static final Color BACKGROUND_COLOR =
            new Color(245, 247, 251);

    public MainFrame() {

        setTitle("Vehicle Rental Management System");
        setSize(1200, 750);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Initialize CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Add application sections
        contentPanel.add(new DashboardPanel(), "Dashboard");

        contentPanel.add(new VehiclePanel(), "Vehicles");

        contentPanel.add(new CustomerPanel(), "Customers");

        contentPanel.add(new RentalPanel(), "Rentals");

        // Add components
        add(createSidebar(), BorderLayout.WEST);
        add(createHeader(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Open dashboard initially
        cardLayout.show(contentPanel, "Dashboard");
    }

    // Sidebar navigation
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setLayout(new BoxLayout(
                sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("  DRIVE RENT");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 23));
        logo.setBorder(BorderFactory.createEmptyBorder(
                30, 15, 40, 10));

        sidebar.add(logo);

        addNavigationButton(sidebar, "Dashboard", "Dashboard");
        addNavigationButton(sidebar, "Vehicles", "Vehicles");
        addNavigationButton(sidebar, "Customers", "Customers");
        addNavigationButton(sidebar, "Rentals", "Rentals");

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("  Vehicle Rental System");
        footer.setForeground(Color.LIGHT_GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(
                15, 10, 25, 10));

        sidebar.add(footer);

        return sidebar;
    }

    // Create navigation buttons
    private void addNavigationButton(
            JPanel sidebar, String text, String cardName) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 48));

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setForeground(Color.WHITE);
        button.setBackground(SIDEBAR_COLOR);
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));

        button.setBorder(BorderFactory.createEmptyBorder(
                10, 25, 10, 10));

        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e ->
                cardLayout.show(contentPanel, cardName));

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(8));
    }

    // Application header
    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(
                18, 25, 18, 25));

        JLabel title = new JLabel(
                "Vehicle Rental Management System");

        title.setFont(new Font(
                "SansSerif", Font.BOLD, 20));

        title.setForeground(SIDEBAR_COLOR);

        JLabel admin = new JLabel("Administrator");

        admin.setFont(new Font(
                "SansSerif", Font.PLAIN, 14));

        header.add(title, BorderLayout.WEST);
        header.add(admin, BorderLayout.EAST);

        return header;
    }

    // Temporary placeholder screens
    private JPanel createPlaceholder(String title) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(title);

        label.setFont(new Font(
                "SansSerif", Font.BOLD, 28));

        label.setForeground(SIDEBAR_COLOR);

        panel.add(label);

        return panel;
    }
}
