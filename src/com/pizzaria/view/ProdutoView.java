/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.view;

import com.pizzaria.dao.ProdutoDAO;
import com.pizzaria.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoView extends JFrame {

    private JTextField txtId, txtNome, txtPreco, txtCategoria, txtEstoque;
    private JTable tabela;
    private ProdutoDAO dao = new ProdutoDAO();

    public ProdutoView() {
        setTitle("Cadastro de Produtos");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        JPanel campos = new JPanel(new GridLayout(5, 2, 5, 5));
        campos.setBorder(BorderFactory.createTitledBorder("Dados do produto"));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNome = new JTextField();
        txtPreco = new JTextField();
        txtCategoria = new JTextField();
        txtEstoque = new JTextField();

        campos.add(new JLabel("ID:"));
        campos.add(txtId);
        campos.add(new JLabel("Nome:"));
        campos.add(txtNome);
        campos.add(new JLabel("Preço:"));
        campos.add(txtPreco);
        campos.add(new JLabel("Categoria:"));
        campos.add(txtCategoria);
        campos.add(new JLabel("Estoque:"));
        campos.add(txtEstoque);

        JPanel botoes = new JPanel();
        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar tabela");

        botoes.add(btnNovo);
        botoes.add(btnSalvar);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);

        tabela = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Nome", "Preço", "Categoria", "Estoque"}, 0
        ));

        // 👉 renderer para destacar estoque baixo
        tabela.setDefaultRenderer(Object.class, new EstoqueRenderer());

        JScrollPane scroll = new JScrollPane(tabela);

        setLayout(new BorderLayout());
        add(campos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        // Ações
        btnNovo.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarTabela());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                int row = tabela.getSelectedRow();
                txtId.setText(tabela.getValueAt(row, 0).toString());
                txtNome.setText(tabela.getValueAt(row, 1).toString());
                txtPreco.setText(tabela.getValueAt(row, 2).toString());
                txtCategoria.setText(tabela.getValueAt(row, 3).toString());
                txtEstoque.setText(tabela.getValueAt(row, 4).toString());
            }
        });
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtCategoria.setText("");
        txtEstoque.setText("");
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String precoStr = txtPreco.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String estoqueStr = txtEstoque.getText().trim();

        if (nome.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e preço são obrigatórios.");
            return;
        }

        double preco;
        int estoque = 0;
        try {
            preco = Double.parseDouble(precoStr.replace(",", "."));
            if (!estoqueStr.isEmpty()) {
                estoque = Integer.parseInt(estoqueStr);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço/estoque inválidos.");
            return;
        }

        Produto p = new Produto();
        p.setNome(nome);
        p.setPreco(preco);
        p.setCategoria(categoria);
        p.setEstoque(estoque);

        if (txtId.getText().isEmpty()) {
            dao.inserir(p);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso.");
        } else {
            p.setId(Integer.parseInt(txtId.getText()));
            dao.atualizar(p);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso.");
        }

        carregarTabela();
        limparCampos();
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        int op = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir?", "Confirmação",
                JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            dao.excluir(id);
            carregarTabela();
            limparCampos();
        }
    }

    private void carregarTabela() {
        List<Produto> lista = dao.listarTodos();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);
        for (Produto p : lista) {
            model.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getPreco(), p.getCategoria(), p.getEstoque()
            });
        }
    }

    // 🔴 Renderer interna para colorir estoque baixo
    private static class EstoqueRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int estoqueColIndex = 4; // coluna "Estoque"
            Object valEstoque = table.getValueAt(row, estoqueColIndex);
            int estoque = 0;
            try {
                estoque = Integer.parseInt(valEstoque.toString());
            } catch (Exception ignored) {}

            if (!isSelected) {
                if (estoque <= 0) {
                    c.setBackground(new Color(255, 204, 204)); // vermelho bem claro
                } else if (estoque < 5) {
                    c.setBackground(new Color(255, 230, 204)); // laranja claro
                } else {
                    c.setBackground(Color.WHITE);
                }
            }

            return c;
        }
    }
}

