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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioDiarioView extends JFrame {

    private JFormattedTextField txtData;
    private JTable tabela;
    private JLabel lblTotal;
    private PedidoDAO pedidoDAO = new PedidoDAO();

    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RelatorioDiarioView() {
        setTitle("Relatório diário de pedidos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        txtData.setText(LocalDate.now().format(fmt));
        carregarRelatorio();
    }

    private void initComponents() {
        JPanel painelTopo = new JPanel();
        try {
            txtData = new JFormattedTextField(new javax.swing.text.MaskFormatter("##/##/####"));
        } catch (Exception e) {
            txtData = new JFormattedTextField();
        }
        JButton btnBuscar = new JButton("Buscar");

        painelTopo.add(new JLabel("Data:"));
        painelTopo.add(txtData);
        painelTopo.add(btnBuscar);

        tabela = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Hora", "Cliente", "Status", "Total"}, 0
        ));
        JScrollPane scroll = new JScrollPane(tabela);

        lblTotal = new JLabel("Total do dia: R$ 0,00");
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setLayout(new BorderLayout());
        add(painelTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblTotal, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> carregarRelatorio());
    }

    private void carregarRelatorio() {
        try {
            LocalDate data = LocalDate.parse(txtData.getText(), fmt);
            List<Pedido> lista = pedidoDAO.listarPorData(data);

            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            model.setRowCount(0);

            double total = 0;
            for (Pedido p : lista) {
                model.addRow(new Object[]{
                        p.getId(),
                        p.getDataHora().toLocalTime().toString().substring(0,5),
                        p.getCliente().getNome(),
                        p.getStatus(),
                        String.format("R$ %.2f", p.getValorTotal())
                });
                total += p.getValorTotal();
            }

            lblTotal.setText(String.format("Total do dia: R$ %.2f", total));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data inválida.");
        }
    }
}

