/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pizzaria.dao;

import com.pizzaria.model.Usuario;
import com.pizzaria.util.Conexao;
import com.pizzaria.util.Criptografia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // === LOGIN ===
    public Usuario autenticar(String login, String senhaPura) {

        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String hash = Criptografia.hashSenha(senhaPura);

            ps.setString(1, login);
            ps.setString(2, hash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("idUsuario"));
                    u.setNome(rs.getString("nome"));
                    u.setLogin(rs.getString("login"));
                    u.setSenha(rs.getString("senha"));
                    u.setTipo(rs.getString("tipo"));
                    return u;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // === LISTAR TODOS (pra UsuarioView) ===
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nome";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("idUsuario"));
                u.setNome(rs.getString("nome"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setTipo(rs.getString("tipo"));
                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // === INSERIR USUÁRIO ===
    // Lança SQLException para a tela poder tratar casos como "login já existe"
    public void inserir(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nome, login, senha, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());
            ps.setString(3, Criptografia.hashSenha(u.getSenha())); // hash da senha
            ps.setString(4, u.getTipo());

            ps.executeUpdate();
        }
    }

    // === ATUALIZAR USUÁRIO ===
    public void atualizar(Usuario u) {
        String sql;

        // se senha veio vazia, não atualiza senha
        boolean senhaVazia = (u.getSenha() == null || u.getSenha().isEmpty());

        if (senhaVazia) {
            sql = "UPDATE usuario SET nome = ?, login = ?, tipo = ? WHERE idUsuario = ?";
        } else {
            sql = "UPDATE usuario SET nome = ?, login = ?, senha = ?, tipo = ? WHERE idUsuario = ?";
        }

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());

            if (senhaVazia) {
                ps.setString(3, u.getTipo());
                ps.setInt(4, u.getId());
            } else {
                ps.setString(3, Criptografia.hashSenha(u.getSenha()));
                ps.setString(4, u.getTipo());
                ps.setInt(5, u.getId());
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // === EXCLUIR USUÁRIO ===
    public void excluir(int id) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
