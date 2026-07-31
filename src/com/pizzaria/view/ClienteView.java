package com.pizzaria.view;

import com.pizzaria.dao.ClienteDAO;
import com.pizzaria.model.Cliente;
import com.pizzaria.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClienteView extends JFrame {

    private Usuario usuarioLogado;

    private JTextField txtId, txtNome, txtTelefone, txtEndereco;
    private JTable tabela;
    private JButton btnExcluir;

    private ClienteDAO dao = new ClienteDAO();

    // construtor principal (passa o usuário logado)
    public ClienteView(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Cadastro de Clientes");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        aplicarPermissoes();
        carregarTabela();
    }

    // construtor sem usuário (se precisar em algum lugar antigo)
    public ClienteView() {
        this(null);
    }

    private void initComponents() {
        JPanel campos = new JPanel(new GridLayout(4, 2, 5, 5));
        campos.setBorder(BorderFactory.createTitledBorder("Dados do cliente"));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNome = new JTextField();
        txtTelefone = new JTextField();
        txtEndereco = new JTextField();

        campos.add(new JLabel("ID:"));
        campos.add(txtId);
        campos.add(new JLabel("Nome:"));
        campos.add(txtNome);
        campos.add(new JLabel("Telefone:"));
        campos.add(txtTelefone);
        campos.add(new JLabel("Endereço:"));
        campos.add(txtEndereco);

        JPanel botoes = new JPanel();
        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar tabela");

        botoes.add(btnNovo);
        botoes.add(btnSalvar);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);

        tabela = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Nome", "Telefone", "Endereço"}, 0
        ));
        JScrollPane scroll = new JScrollPane(tabela);

        setLayout(new BorderLayout());
        add(campos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        // ações
        btnNovo.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarTabela());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                int row = tabela.getSelectedRow();
                txtId.setText(tabela.getValueAt(row, 0).toString());
                txtNome.setText(tabela.getValueAt(row, 1).toString());
                txtTelefone.setText(tabela.getValueAt(row, 2).toString());
                txtEndereco.setText(tabela.getValueAt(row, 3).toString());
            }
        });
    }

    private void aplicarPermissoes() {
        // se não for ADMIN, não pode excluir cliente
        if (usuarioLogado == null || !"ADMIN".equalsIgnoreCase(usuarioLogado.getTipo())) {
            btnExcluir.setEnabled(false);
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String endereco = txtEndereco.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
            return;
        }

        Cliente c = new Cliente();
        c.setNome(nome);
        c.setTelefone(telefone);
        c.setEndereco(endereco);

        if (txtId.getText().isEmpty()) {
            dao.inserir(c);
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso.");
        } else {
            c.setId(Integer.parseInt(txtId.getText()));
            dao.atualizar(c);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso.");
        }

        carregarTabela();
        limparCampos();
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.");
            return;
        }

        int op = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este cliente?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            dao.excluir(id);
            carregarTabela();
            limparCampos();
        }
    }

    private void carregarTabela() {
        List<Cliente> lista = dao.listarTodos();
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);

        for (Cliente c : lista) {
            model.addRow(new Object[]{
                    c.getId(), c.getNome(), c.getTelefone(), c.getEndereco()
            });
        }
    }
}

