/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pizzaria.dao;

import com.pizzaria.model.*;
import com.pizzaria.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void salvar(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedido (dataHora, status, idCliente, idUsuario, valorTotal) " +
                           "VALUES (?, ?, ?, ?, ?)";

        String sqlItem = "INSERT INTO item_pedido (idPedido, idProduto, quantidade, subtotal) " +
                         "VALUES (?, ?, ?, ?)";

        String sqlEstoque = "UPDATE produto SET estoque = estoque - ? WHERE idProduto = ?";

        Connection conn = Conexao.getConnection();
        try {
            conn.setAutoCommit(false);

            // salva pedido
            try (PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setTimestamp(1, Timestamp.valueOf(pedido.getDataHora()));
                ps.setString(2, pedido.getStatus());
                ps.setInt(3, pedido.getCliente().getId());
                ps.setInt(4, pedido.getUsuario().getId());
                ps.setDouble(5, pedido.getValorTotal());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    pedido.setId(rs.getInt(1));
                }
            }

            // salva itens + baixa estoque
            try (PreparedStatement psItem = conn.prepareStatement(sqlItem);
                 PreparedStatement psEstoque = conn.prepareStatement(sqlEstoque)) {

                for (ItemPedido item : pedido.getItens()) {
                    // item_pedido
                    psItem.setInt(1, pedido.getId());
                    psItem.setInt(2, item.getProduto().getId());
                    psItem.setInt(3, item.getQuantidade());
                    psItem.setDouble(4, item.getSubtotal());
                    psItem.addBatch();

                    // estoque
                    psEstoque.setInt(1, item.getQuantidade());
                    psEstoque.setInt(2, item.getProduto().getId());
                    psEstoque.addBatch();
                }

                psItem.executeBatch();
                psEstoque.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public List<Pedido> listarPorData(LocalDate data) throws SQLException {
        List<Pedido> lista = new ArrayList<>();

        String sql = "SELECT p.idPedido, p.dataHora, p.status, p.valorTotal, " +
                     "c.idCliente, c.nome AS nomeCliente " +
                     "FROM pedido p " +
                     "JOIN cliente c ON p.idCliente = c.idCliente " +
                     "WHERE DATE(p.dataHora) = ? " +
                     "ORDER BY p.dataHora";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(data));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("idPedido"));
                p.setDataHora(rs.getTimestamp("dataHora").toLocalDateTime());
                p.setStatus(rs.getString("status"));
                p.setValorTotal(rs.getDouble("valorTotal"));

                Cliente c = new Cliente();
                c.setId(rs.getInt("idCliente"));
                c.setNome(rs.getString("nomeCliente"));
                p.setCliente(c);

                lista.add(p);
            }
        }
        return lista;
    }

    public List<Pedido> listarTodosPendentes() throws SQLException {
        List<Pedido> lista = new ArrayList<>();

        String sql = "SELECT p.idPedido, p.dataHora, p.status, p.valorTotal, " +
                     "c.nome AS nomeCliente " +
                     "FROM pedido p " +
                     "JOIN cliente c ON p.idCliente = c.idCliente " +
                     "WHERE p.status <> 'ENTREGUE' " +
                     "ORDER BY p.dataHora";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("idPedido"));
                p.setDataHora(rs.getTimestamp("dataHora").toLocalDateTime());
                p.setStatus(rs.getString("status"));
                p.setValorTotal(rs.getDouble("valorTotal"));

                Cliente c = new Cliente();
                c.setNome(rs.getString("nomeCliente"));
                p.setCliente(c);

                lista.add(p);
            }
        }
        return lista;
    }

    public void atualizarStatus(int idPedido, String novoStatus) throws SQLException {
        String sql = "UPDATE pedido SET status = ? WHERE idPedido = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, novoStatus);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }
    }

    // 🔹 NOVO: lista pedidos por mês/ano (usado no RelatorioMensalView)
    public List<Pedido> listarPorMes(int ano, int mesSelecionado) throws SQLException {
        List<Pedido> lista = new ArrayList<>();

        String sql = "SELECT p.idPedido, p.dataHora, p.status, p.valorTotal, " +
                     "c.idCliente, c.nome AS nomeCliente " +
                     "FROM pedido p " +
                     "JOIN cliente c ON p.idCliente = c.idCliente " +
                     "WHERE YEAR(p.dataHora) = ? AND MONTH(p.dataHora) = ? " +
                     "ORDER BY p.dataHora";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ano);
            ps.setInt(2, mesSelecionado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("idPedido"));
                    p.setDataHora(rs.getTimestamp("dataHora").toLocalDateTime());
                    p.setStatus(rs.getString("status"));
                    p.setValorTotal(rs.getDouble("valorTotal"));

                    Cliente c = new Cliente();
                    c.setId(rs.getInt("idCliente"));
                    c.setNome(rs.getString("nomeCliente"));
                    p.setCliente(c);

                    lista.add(p);
                }
            }
        }

        return lista;
    }
}


