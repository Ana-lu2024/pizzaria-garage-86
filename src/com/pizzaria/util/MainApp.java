/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.util;

import com.pizzaria.view.SplashScreen;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainApp {

    public static void main(String[] args) {

        // Define o tema Nimbus, se disponível
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Não foi possível carregar o tema Nimbus.");
        }

        // Abre o splash
        SwingUtilities.invokeLater(() -> {
            new SplashScreen().setVisible(true);
        });
    }
}





