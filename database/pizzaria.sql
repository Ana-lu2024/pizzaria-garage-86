-- Script de criação do banco de dados do Sistema Pizzaria Garage 86
-- Execute este script no MySQL antes de rodar a aplicação.

CREATE DATABASE IF NOT EXISTS pizzaria;
USE pizzaria;

CREATE TABLE cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    endereco VARCHAR(150)
);

CREATE TABLE produto (
    idProduto INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(50),
    estoque INT NOT NULL DEFAULT 0
);

CREATE TABLE usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL, -- armazenada como hash SHA-256
    tipo VARCHAR(20) NOT NULL DEFAULT 'ATENDENTE' -- ADMIN, ATENDENTE, MOTOBOY
);

CREATE TABLE pedido (
    idPedido INT AUTO_INCREMENT PRIMARY KEY,
    dataHora DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AGUARDANDO', -- AGUARDANDO, ENTREGUE, CANCELADO
    idCliente INT NOT NULL,
    idUsuario INT NOT NULL,
    valorTotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES cliente(idCliente),
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
);

CREATE TABLE item_pedido (
    idItemPedido INT AUTO_INCREMENT PRIMARY KEY,
    idPedido INT NOT NULL,
    idProduto INT NOT NULL,
    quantidade INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (idPedido) REFERENCES pedido(idPedido) ON DELETE CASCADE,
    FOREIGN KEY (idProduto) REFERENCES produto(idProduto)
);

-- Dados de exemplo (opcional, útil para testar o sistema)

INSERT INTO usuario (nome, login, senha, tipo) VALUES
('Administrador', 'admin', SHA2('123', 256), 'ADMIN'),
('Atendente', 'atendente', SHA2('456', 256), 'ATENDENTE'),
('Motoboy 2', 'motoboy2', SHA2('789', 256), 'MOTOBOY');

INSERT INTO produto (nome, preco, categoria, estoque) VALUES
('Pizza Portuguesa', 45.00, 'Pizzas', 10),
('Pizza Calabresa', 42.00, 'Pizzas', 8),
('Coca-Cola 2L', 12.00, 'Bebidas', 25),
('Guaraná Antarctica 2L', 10.00, 'Bebidas', 18),
('Suco de Laranja 1L', 8.00, 'Bebidas', 20);

INSERT INTO cliente (nome, telefone, endereco) VALUES
('João da Silva', '(11) 99999-9999', 'Rua das Flores, 123'),
('Maria Oliveira', '(11) 98888-8888', 'Av. Brasil, 456'),
('Carlos Pereira', '(11) 97777-7777', 'Rua Direita, 789'),
('Ana Paula', '(11) 96666-6666', 'Rua do Sol, 321');
