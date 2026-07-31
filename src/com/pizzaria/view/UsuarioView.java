/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.view;

import com.pizzaria.dao.UsuarioDAO;
import com.pizzaria.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioView extends JFrame {

    private JTextField txtId, txtNome, txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbTipo;
    private JTable tabela;
    private UsuarioDAO dao = new UsuarioDAO();

    public UsuarioView() {
        setTitle("Cadastro de Usuários");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        JPanel campos = new JPanel(new GridLayout(5, 2, 5, 5));
        campos.setBorder(BorderFactory.createTitledBorder("Dados do usuário"));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNome = new JTextField();
        txtLogin = new JTextField();
        txtSenha = new JPasswordField();
        cbTipo = new JComboBox<>(new String[]{"ADMIN", "ATENDENTE", "MOTOBOY"});

        campos.add(new JLabel("ID:"));
        campos.add(txtId);
        campos.add(new JLabel("Nome:"));
        campos.add(txtNome);
        campos.add(new JLabel("Login:"));
        campos.add(txtLogin);
        campos.add(new JLabel("Senha:"));
        campos.add(txtSenha);
        campos.add(new JLabel("Tipo:"));
        campos.add(cbTipo);

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
                new Object[]{"ID", "Nome", "Login", "Tipo"}, 0
        ));
        JScrollPane scroll = new JScrollPane(tabela);

        setLayout(new BorderLayout());
        add(campos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        btnNovo.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarTabela());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                int row = tabela.getSelectedRow();
                txtId.setText(tabela.getValueAt(row, 0).toString());
                txtNome.setText(tabela.getValueAt(row, 1).toString());
                txtLogin.setText(tabela.getValueAt(row, 2).toString());
                cbTipo.setSelectedItem(tabela.getValueAt(row, 3).toString());
                txtSenha.setText(""); // não mostramos senha
            }
        });
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        cbTipo.setSelectedIndex(0);
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String tipo = cbTipo.getSelectedItem().toString();

        if (nome.isEmpty() || login.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e login são obrigatórios.");
            return;
        }

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setLogin(login);
        u.setSenha(senha);
        u.setTipo(tipo);

        if (txtId.getText().isEmpty()) {
            try {
                dao.inserir(u);
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso.");
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(this, "Já existe um usuário com esse login.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (java.sql.SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            u.setId(Integer.parseInt(txtId.getText()));
            dao.atualizar(u);
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso.");
        }

        carregarTabela();
        limparCampos();
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            return;
        }

        int op = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este usuário?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            dao.excluir(id);
            carregarTabela();
            limparCampos();
        }
    }

    private void carregarTabela() {
        List<Usuario> lista = dao.listarTodos();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);

        for (Usuario u : lista) {
            model.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getLogin(), u.getTipo()
            });
        }
    }
}
