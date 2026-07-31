/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.view;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    private JProgressBar progressBar;
    private JLabel lblStatus;

    public SplashScreen() {
        setUndecorated(true);
        setSize(500, 280);
        setLocationRelativeTo(null);
        initComponents();
        iniciarLoading();
    }

    private void initComponents() {
        JPanel painel = new JPanel();
        painel.setBackground(new Color(30, 30, 46));
        painel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        painel.setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Sistema Pizzaria", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));
        painel.add(lblTitulo, BorderLayout.NORTH);

        // Centro com ícone ou texto
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel lblSub = new JLabel("Carregando módulos do sistema...", SwingConstants.CENTER);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(220, 220, 220));

        centro.add(lblSub);
        painel.add(centro, BorderLayout.CENTER);

        // Rodapé com barra e status
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(255, 102, 0));

        lblStatus = new JLabel("Iniciando...", SwingConstants.RIGHT);
        lblStatus.setForeground(new Color(220, 220, 220));
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        rodape.add(progressBar, BorderLayout.CENTER);
        rodape.add(lblStatus, BorderLayout.SOUTH);

        painel.add(rodape, BorderLayout.SOUTH);

        setContentPane(painel);
    }

    private void iniciarLoading() {
        // Timer para simular carregamento
        Timer timer = new Timer(40, e -> {
            int value = progressBar.getValue() + 2;
            progressBar.setValue(value);

            if (value < 30) {
                lblStatus.setText("Carregando componentes...");
            } else if (value < 60) {
                lblStatus.setText("Conectando ao banco de dados...");
            } else if (value < 90) {
                lblStatus.setText("Iniciando interface...");
            } else {
                lblStatus.setText("Pronto!");
            }

            if (value >= 100) {
                ((Timer) e.getSource()).stop();
                abrirLogin();
            }
        });

        timer.start();
    }

    private void abrirLogin() {
        dispose();
        new LoginView().setVisible(true);
    }
}

