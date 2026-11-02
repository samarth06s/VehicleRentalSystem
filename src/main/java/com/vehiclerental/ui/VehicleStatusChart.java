
package com.vehiclerental.ui;

import javax.swing.*;
import java.awt.*;

public class VehicleStatusChart extends JPanel {

    private int availableVehicles;
    private int rentedVehicles;

    private final Color availableColor = new Color(46, 160, 100);
    private final Color rentedColor = new Color(230, 145, 50);

    public VehicleStatusChart() {

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 280));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 235)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    }

    public void setCounts(int available, int rented) {

        this.availableVehicles = Math.max(0, available);
        this.rentedVehicles = Math.max(0, rented);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setFont(new Font("SansSerif", Font.BOLD, 17));
            g2.setColor(new Color(30, 50, 80));

            g2.drawString("Vehicle Availability", 20, 30);

            int total = availableVehicles + rentedVehicles;

            if (total == 0) {
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g2.setColor(Color.GRAY);
                g2.drawString("No vehicle data available", 25, 100);
                return;
            }

            int x = 30;
            int y = 55;
            int size = 150;

            int availableAngle =
                    (int) Math.round(360.0 * availableVehicles / total);

            int rentedAngle = 360 - availableAngle;

            g2.setColor(availableColor);
            g2.fillArc(x, y, size, size, 90, availableAngle);

            g2.setColor(rentedColor);
            g2.fillArc(
                    x, y, size, size,
                    90 + availableAngle, rentedAngle
            );

            int legendX = 220;
            int legendY = 90;

            g2.setColor(availableColor);
            g2.fillRect(legendX, legendY, 14, 14);

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2.drawString(
                    "Available: " + availableVehicles,
                    legendX + 22, legendY + 12
            );

            g2.setColor(rentedColor);
            g2.fillRect(legendX, legendY + 35, 14, 14);

            g2.setColor(Color.DARK_GRAY);
            g2.drawString(
                    "Rented: " + rentedVehicles,
                    legendX + 22, legendY + 47
            );

            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString(
                    "Total: " + total,
                    legendX, legendY + 85
            );

        } finally {
            g2.dispose();
        }
    }
}
