/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pizzaria.view;

import com.pizzaria.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private final Usuario usuario;
    private JLabel lblBemVindo;

    // Menus
    private JMenu menuCad;
    private JMenu menuEntregas;
    private JMenu menuRel;
    private JMenu menuLogout;

    // Itens de menu
    private JMenuItem miClientes;
    private JMenuItem miProdutos;
    private JMenuItem miUsuarios;   // só ADMIN

    private JMenuItem miNovoPedido;
    private JMenuItem miListaPedidos;

    private JMenuItem miRelDiario;
    private JMenuItem miRelMensal;
    private JMenuItem miLogout;

    public MenuPrincipalView(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Pizzaria - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        initComponents();
        configurarPermissoes();
    }

    private void initComponents() {
        JPanel painelCentro = new JPanel(new GridBagLayout());
        painelCentro.setBackground(new Color(240, 243, 248));

        lblBemVindo = new JLabel("Bem-vindo(a), " + usuario.getNome() +
                " - Perfil: " + usuario.getTipo(), SwingConstants.CENTER);
        lblBemVindo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel lblSubtitulo = new JLabel("Use o menu acima para acessar cadastros, entregas e relatórios.");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(90, 90, 90));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridy = 0;
        painelCentro.add(lblBemVindo, gbc);

        gbc.gridy = 1;
        painelCentro.add(lblSubtitulo, gbc);

        add(painelCentro, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        // ===== CADASTROS =====
        menuCad = new JMenu("Cadastros");
        miClientes = new JMenuItem("Clientes");
        miProdutos = new JMenuItem("Produtos");
        miUsuarios = new JMenuItem("Usuários (ADMIN)");

        miClientes.addActionListener(e -> new ClienteView(usuario).setVisible(true));
        miProdutos.addActionListener(e -> new ProdutoView().setVisible(true));
        miUsuarios.addActionListener(e -> new UsuarioView().setVisible(true));

        menuCad.add(miClientes);
        menuCad.add(miProdutos);
        menuCad.addSeparator();
        menuCad.add(miUsuarios);
        menuBar.add(menuCad);

        // ===== ENTREGAS / PEDIDOS =====
        menuEntregas = new JMenu("Entregas");
        miNovoPedido = new JMenuItem("Registrar pedido");
        miListaPedidos = new JMenuItem("Acompanhar entregas");

        miNovoPedido.addActionListener(e -> new PedidoView(usuario).setVisible(true));
        miListaPedidos.addActionListener(e -> new ListaPedidosView().setVisible(true));

        menuEntregas.add(miNovoPedido);
        menuEntregas.add(miListaPedidos);
        menuBar.add(menuEntregas);

        // ===== RELATÓRIOS =====
        menuRel = new JMenu("Relatórios");
        miRelDiario = new JMenuItem("Relatório diário");
        miRelMensal = new JMenuItem("Relatório mensal");

        miRelDiario.addActionListener(e -> new RelatorioDiarioView().setVisible(true));
        miRelMensal.addActionListener(e -> new RelatorioMensalView().setVisible(true));

        menuRel.add(miRelDiario);
        menuRel.add(miRelMensal);
        menuBar.add(menuRel);

        // ===== LOGOUT =====
        menuLogout = new JMenu("Logout");
        miLogout = new JMenuItem("Sair do sistema");
        miLogout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        menuLogout.add(miLogout);
        menuBar.add(menuLogout);

        setJMenuBar(menuBar);
    }

    private void configurarPermissoes() {
        String tipo = usuario.getTipo() == null ? "" : usuario.getTipo().toUpperCase();

        switch (tipo) {
            case "ADMIN":
                // pode tudo
                break;

            case "ATENDENTE":
                // Atendente:
                //  - pode: Clientes, Produtos, Registrar pedido, Acompanhar entregas
                //  - não pode: Usuários, Relatórios
                miUsuarios.setVisible(false);
                menuRel.setVisible(false);
                break;

            case "MOTOBOY":
                // Motoboy:
                //  - só pode acompanhar entregas + logout
                menuCad.setVisible(false);
                miNovoPedido.setVisible(false);
                menuRel.setVisible(false);
                break;

            default:
                // se vier tipo estranho, deixa tudo visível
                break;
        }
    }
}






