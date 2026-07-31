/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.view;

import com.pizzaria.dao.PedidoDAO;
import com.pizzaria.model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListaPedidosView extends JFrame {

    private JTable tabela;
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private JComboBox<String> cbStatus;

    public ListaPedidosView() {
        setTitle("Pedidos em aberto");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        tabela = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Data/Hora", "Cliente", "Status", "Total"}, 0
        ));
        JScrollPane scroll = new JScrollPane(tabela);

        cbStatus = new JComboBox<>(new String[]{"EM_PREPARO", "SAIU_PARA_ENTREGA", "ENTREGUE", "CANCELADO"});
        JButton btnAtualizar = new JButton("Mudar status");

        JPanel painelSul = new JPanel();
        painelSul.add(new JLabel("Novo status:"));
        painelSul.add(cbStatus);
        painelSul.add(btnAtualizar);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> atualizarStatus());
    }

    private void carregarTabela() {
        try {
            List<Pedido> lista = pedidoDAO.listarTodosPendentes();
            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            model.setRowCount(0);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");

            for (Pedido p : lista) {
                model.addRow(new Object[]{
                        p.getId(),
                        p.getDataHora().format(fmt),
                        p.getCliente().getNome(),
                        p.getStatus(),
                        String.format("R$ %.2f", p.getValorTotal())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void atualizarStatus() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido na tabela.");
            return;
        }
        int idPedido = (Integer) tabela.getValueAt(row, 0);
        String novoStatus = (String) cbStatus.getSelectedItem();

        try {
            pedidoDAO.atualizarStatus(idPedido, novoStatus);
            JOptionPane.showMessageDialog(this, "Status atualizado.");
            carregarTabela();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao atualizar status: " + e.getMessage());
        }
    }
}
