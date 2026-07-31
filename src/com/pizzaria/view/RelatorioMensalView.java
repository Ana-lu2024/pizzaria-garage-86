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
import java.util.List;

public class RelatorioMensalView extends JFrame {

    private JComboBox<String> cbMes;
    private JSpinner spAno;
    private JTable tabela;
    private JLabel lblTotal;
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public RelatorioMensalView() {
        setTitle("Relatório mensal de pedidos");
        setSize(750, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        carregarRelatorio();
    }

    private void initComponents() {
        JPanel painelTopo = new JPanel();

        cbMes = new JComboBox<>(new String[]{
                "01 - Janeiro", "02 - Fevereiro", "03 - Março", "04 - Abril",
                "05 - Maio", "06 - Junho", "07 - Julho", "08 - Agosto",
                "09 - Setembro", "10 - Outubro", "11 - Novembro", "12 - Dezembro"
        });

        int anoAtual = LocalDate.now().getYear();
        spAno = new JSpinner(new SpinnerNumberModel(anoAtual, 2000, 2100, 1));

        cbMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        JButton btnBuscar = new JButton("Buscar");

        painelTopo.add(new JLabel("Mês:"));
        painelTopo.add(cbMes);
        painelTopo.add(new JLabel("Ano:"));
        painelTopo.add(spAno);
        painelTopo.add(btnBuscar);

        tabela = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Data", "Cliente", "Status", "Total"}, 0
        ));
        JScrollPane scroll = new JScrollPane(tabela);

        lblTotal = new JLabel("Total do mês: R$ 0,00");
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setLayout(new BorderLayout());
        add(painelTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblTotal, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> carregarRelatorio());
    }

    private void carregarRelatorio() {
        int mesSelecionado = cbMes.getSelectedIndex() + 1;
        int ano = (Integer) spAno.getValue();

        try {
            List<Pedido> lista = pedidoDAO.listarPorMes(ano, mesSelecionado);

            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            model.setRowCount(0);

            double total = 0;
            for (Pedido p : lista) {
                model.addRow(new Object[]{
                        p.getId(),
                        p.getDataHora().toLocalDate().toString(),
                        p.getCliente().getNome(),
                        p.getStatus(),
                        String.format("R$ %.2f", p.getValorTotal())
                });
                total += p.getValorTotal();
            }

            lblTotal.setText(String.format("Total do mês: R$ %.2f", total));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar relatório: " + e.getMessage());
        }
    }
}




