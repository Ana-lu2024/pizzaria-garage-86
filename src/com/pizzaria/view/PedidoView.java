/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.view;

import com.pizzaria.dao.ClienteDAO;
import com.pizzaria.dao.PedidoDAO;
import com.pizzaria.dao.ProdutoDAO;
import com.pizzaria.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoView extends JFrame {

    private Usuario usuarioLogado;
    private JComboBox<Cliente> cbCliente;
    private JComboBox<Produto> cbProduto;
    private JSpinner spQuantidade;
    private JTable tabelaItens;
    private JLabel lblTotal;

    private Pedido pedidoAtual = new Pedido();

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public PedidoView(Usuario usuario) {
        this.usuarioLogado = usuario;
        setTitle("Registrar Pedido");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        carregarCombos();
        atualizarTotal();
    }

    private void initComponents() {
        JPanel painelTopo = new JPanel(new GridLayout(2, 2, 5, 5));
        painelTopo.setBorder(BorderFactory.createTitledBorder("Dados do pedido"));

        cbCliente = new JComboBox<>();
        cbProduto = new JComboBox<>();
        spQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        // 👉 combos com largura melhor
        cbCliente.setPreferredSize(new Dimension(300, 25));
        cbProduto.setPreferredSize(new Dimension(300, 25));

        painelTopo.add(new JLabel("Cliente:"));
        painelTopo.add(cbCliente);
        painelTopo.add(new JLabel("Produto:"));
        painelTopo.add(cbProduto);

        JPanel painelMeio = new JPanel(new BorderLayout());
        JPanel painelAdd = new JPanel();
        JButton btnAdd = new JButton("Adicionar item");
        JButton btnRemover = new JButton("Remover item");

        painelAdd.add(new JLabel("Quantidade:"));
        painelAdd.add(spQuantidade);
        painelAdd.add(btnAdd);
        painelAdd.add(btnRemover);

        tabelaItens = new JTable(new DefaultTableModel(
                new Object[]{"Produto", "Qtd", "Preço", "Subtotal"}, 0
        ));
        JScrollPane scroll = new JScrollPane(tabelaItens);

        painelMeio.add(painelAdd, BorderLayout.NORTH);
        painelMeio.add(scroll, BorderLayout.CENTER);

        JPanel painelBaixo = new JPanel(new BorderLayout());
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton btnSalvar = new JButton("Confirmar Pedido");

        painelBaixo.add(lblTotal, BorderLayout.CENTER);
        painelBaixo.add(btnSalvar, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(painelTopo, BorderLayout.NORTH);
        add(painelMeio, BorderLayout.CENTER);
        add(painelBaixo, BorderLayout.SOUTH);

        // Ações
        btnAdd.addActionListener(e -> adicionarItem());
        btnRemover.addActionListener(e -> removerItem());
        btnSalvar.addActionListener(e -> salvarPedido());
    }

    private void carregarCombos() {
        // limpa combos
        cbCliente.removeAllItems();
        cbProduto.removeAllItems();

        // carrega e já vem ordenado (DAO usa ORDER BY nome)
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente c : clientes) {
            cbCliente.addItem(c);
        }

        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            cbProduto.addItem(p);
        }

        // 👉 renderers com tooltip
        cbCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente c) {
                    setText(c.getNome());
                    setToolTipText(c.getNome() + " - " + c.getTelefone() + " - " + c.getEndereco());
                }
                return comp;
            }
        });

        cbProduto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto p) {
                    setText(p.getNome());
                    setToolTipText(
                            "<html><b>" + p.getNome() + "</b><br/>" +
                            "Categoria: " + p.getCategoria() + "<br/>" +
                            "Preço: R$ " + String.format("%.2f", p.getPreco()) + "<br/>" +
                            "Estoque: " + p.getEstoque() +
                            "</html>");
                }
                return comp;
            }
        });

        // tooltip também quando o combo está fechado (item selecionado)
        cbCliente.addActionListener(e -> {
            Cliente c = (Cliente) cbCliente.getSelectedItem();
            if (c != null) {
                cbCliente.setToolTipText(c.getNome() + " - " + c.getTelefone() + " - " + c.getEndereco());
            }
        });

        cbProduto.addActionListener(e -> {
            Produto p = (Produto) cbProduto.getSelectedItem();
            if (p != null) {
                cbProduto.setToolTipText(
                        p.getNome() + " | " + p.getCategoria() +
                        " | R$ " + String.format("%.2f", p.getPreco()) +
                        " | Estoque: " + p.getEstoque());
            }
        });
    }

    private void adicionarItem() {
        Cliente cliente = (Cliente) cbCliente.getSelectedItem();
        Produto produto = (Produto) cbProduto.getSelectedItem();
        int qtd = (int) spQuantidade.getValue();

        if (cliente == null || produto == null) {
            JOptionPane.showMessageDialog(this, "Selecione cliente e produto.");
            return;
        }
        if (qtd <= 0) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            return;
        }
        if (produto.getEstoque() < qtd) {
            JOptionPane.showMessageDialog(this, "Estoque insuficiente para este produto.");
            return;
        }

        double subtotal = produto.getPreco() * qtd;

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(qtd);
        item.setSubtotal(subtotal);

        pedidoAtual.adicionarItem(item);
        pedidoAtual.setCliente(cliente);
        pedidoAtual.setUsuario(usuarioLogado);

        DefaultTableModel model = (DefaultTableModel) tabelaItens.getModel();
        model.addRow(new Object[]{
                produto.getNome(),
                qtd,
                String.format("R$ %.2f", produto.getPreco()),
                String.format("R$ %.2f", subtotal)
        });

        atualizarTotal();
    }

    private void removerItem() {
        int row = tabelaItens.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela.");
            return;
        }
        ((DefaultTableModel) tabelaItens.getModel()).removeRow(row);
        pedidoAtual.removerItem(row);
        atualizarTotal();
    }

    private void atualizarTotal() {
        pedidoAtual.recalcularTotal();
        lblTotal.setText(String.format("Total: R$ %.2f", pedidoAtual.getValorTotal()));
    }

    private void salvarPedido() {
        if (pedidoAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item.");
            return;
        }
        if (pedidoAtual.getCliente() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.");
            return;
        }

        pedidoAtual.setDataHora(LocalDateTime.now());
        pedidoAtual.setStatus("EM_PREPARO");

        try {
            pedidoDAO.salvar(pedidoAtual);
            JOptionPane.showMessageDialog(this, "Pedido registrado com sucesso!");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar pedido: " + e.getMessage());
        }
    }
}

