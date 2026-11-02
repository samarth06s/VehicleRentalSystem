
package com.vehiclerental;

import com.formdev.flatlaf.FlatLightLaf;
import com.vehiclerental.ui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
