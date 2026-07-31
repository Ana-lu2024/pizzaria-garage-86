package com.pizzaria.view;

import com.pizzaria.dao.UsuarioDAO;
import com.pizzaria.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Tela de criação de conta, acessada a partir da tela de Login.
 * Permite que um novo usuário se cadastre no sistema antes de entrar.
 */
public class CadastroUsuarioView extends JDialog {

    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;
    private JComboBox<String> cbTipo;

    public CadastroUsuarioView(JFrame parent) {
        super(parent, "Criar novo usuário", true);
        setSize(420, 340);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(16);
        painel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        txtLogin = new JTextField(16);
        painel.add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtSenha = new JPasswordField(16);
        painel.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Confirmar senha:"), gbc);
        gbc.gridx = 1;
        txtConfirmarSenha = new JPasswordField(16);
        painel.add(txtConfirmarSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Perfil:"), gbc);
        gbc.gridx = 1;
        cbTipo = new JComboBox<>(new String[]{"ATENDENTE", "MOTOBOY", "ADMIN"});
        painel.add(cbTipo, gbc);

        JButton btnCriar = new JButton("Criar conta");
        btnCriar.setBackground(new Color(255, 102, 0));
        btnCriar.setForeground(Color.WHITE);
        btnCriar.setFocusPainted(false);
        btnCriar.addActionListener(e -> criarConta());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.add(btnCriar);
        painelBotoes.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        painel.add(painelBotoes, gbc);

        setContentPane(painel);
        getRootPane().setDefaultButton(btnCriar);
    }

    private void criarConta() {
        String nome = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());
        String tipo = (String) cbTipo.getSelectedItem();

        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha nome, login e senha.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (senha.length() < 3) {
            JOptionPane.showMessageDialog(this, "A senha deve ter pelo menos 3 caracteres.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setLogin(login);
        u.setSenha(senha);
        u.setTipo(tipo);

        UsuarioDAO dao = new UsuarioDAO();
        try {
            dao.inserir(u);
            JOptionPane.showMessageDialog(this,
                    "Conta criada com sucesso! Agora você já pode entrar.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this,
                    "Já existe um usuário com esse login. Escolha outro.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao criar conta: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
