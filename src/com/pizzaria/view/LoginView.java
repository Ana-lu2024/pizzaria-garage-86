/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.view;

import com.pizzaria.dao.UsuarioDAO;
import com.pizzaria.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnSair;

    public LoginView() {
        setTitle("Sistema Pizzaria - Login");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Painel de fundo
        JPanel fundo = new JPanel(new BorderLayout());
        fundo.setBackground(new Color(30, 30, 46));

        // Topo com título
        JLabel lblTitulo = new JLabel("Sistema Pizzaria", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        fundo.add(lblTitulo, BorderLayout.NORTH);

        // Painel central (card de login)
        JPanel card = new JPanel();
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(lblLogin, gbc);

        gbc.gridx = 1;
        txtLogin = new JTextField(15);
        card.add(txtLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(lblSenha, gbc);

        gbc.gridx = 1;
        txtSenha = new JPasswordField(15);
        card.add(txtSenha, gbc);

        // linha de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setOpaque(false);

        btnEntrar = new JButton("Entrar");
        btnSair = new JButton("Sair");

        // aparência dos botões
        btnEntrar.setBackground(new Color(255, 102, 0));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnSair.setBackground(new Color(200, 200, 200));
        btnSair.setForeground(Color.BLACK);
        btnSair.setFocusPainted(false);
        btnSair.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnSair);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        card.add(painelBotoes, gbc);

        // Link para criar uma nova conta
        JButton btnCriarConta = new JButton("Criar conta");
        btnCriarConta.setBorderPainted(false);
        btnCriarConta.setContentAreaFilled(false);
        btnCriarConta.setForeground(new Color(0, 102, 204));
        btnCriarConta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCriarConta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCriarConta.addActionListener(e -> new CadastroUsuarioView(this).setVisible(true));

        JPanel painelCriarConta = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        painelCriarConta.setOpaque(false);
        painelCriarConta.add(btnCriarConta);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(painelCriarConta, gbc);

        fundo.add(card, BorderLayout.CENTER);

        setContentPane(fundo);

        // Ações
        btnEntrar.addActionListener(e -> autenticar());
        btnSair.addActionListener(e -> System.exit(0));

        // Enter na senha chama autenticar
        txtSenha.addActionListener(e -> autenticar());

        // Deixa o botão Entrar como default (ENTER)
        getRootPane().setDefaultButton(btnEntrar);
    }

    private void autenticar() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe login e senha.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.autenticar(login, senha);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this,
                    "Bem-vindo(a), " + usuario.getNome(),
                    "Login realizado",
                    JOptionPane.INFORMATION_MESSAGE);

            new MenuPrincipalView(usuario).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Login ou senha inválidos.",
                    "Erro de autenticação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

