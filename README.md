# 🍕 Sistema Pizzaria Garage 86

Sistema desktop para gestão operacional de uma pizzaria, desenvolvido em **Java** com arquitetura **MVC** e padrão **DAO**, interface gráfica em **Swing** e persistência de dados em **MySQL** via **JDBC**.

## 📋 Sobre o projeto

O sistema organiza o cadastro de clientes, produtos e usuários, permitindo que o funcionário registre pedidos, consulte informações, altere dados cadastrados e acompanhe vendas por meio de relatórios.

## ✨ Funcionalidades

- **Login** — autenticação de usuários por login e senha (senha armazenada como hash SHA-256)
- **Clientes** — cadastro, consulta, alteração e exclusão
- **Produtos** — cadastro, consulta, alteração e exclusão, com controle de estoque
- **Usuários** — cadastro, consulta, alteração e exclusão, com perfis de acesso (ADMIN, ATENDENTE, MOTOBOY)
- **Pedidos** — registro vinculado a cliente e usuário, com cálculo automático de subtotal e total
- **Estoque** — atualização automática após o registro de um pedido
- **Relatórios** — relatório diário e mensal de vendas

## 🏗️ Arquitetura

O projeto segue o padrão **MVC**, organizado em camadas:

- **Model** — `Cliente`, `Produto`, `Usuario`, `Pedido`, `ItemPedido`
- **DAO** — operações SQL (`ClienteDAO`, `ProdutoDAO`, `UsuarioDAO`, `PedidoDAO`), com uso de `PreparedStatement` e transações (commit/rollback) no registro de pedidos
- **View** — telas Swing responsáveis pela interação com o usuário
- **util.Conexao** — centraliza a conexão JDBC com o banco MySQL

## 🛠️ Tecnologias

- Java
- Java Swing
- MySQL
- JDBC

## ▶️ Como executar

1. **Crie o banco de dados**: execute o script `database/pizzaria.sql` em um servidor MySQL. Ele cria as tabelas e já insere alguns dados de exemplo (produtos, clientes e 3 usuários de teste).

2. **Configure a conexão**: por segurança, o usuário e senha do banco não ficam mais fixos no código. Defina as variáveis de ambiente antes de rodar o projeto:

   ```bash
   export DB_URL="jdbc:mysql://localhost:3306/pizzaria?useSSL=false&serverTimezone=UTC"
   export DB_USER="root"
   export DB_PASS="sua_senha_aqui"
   ```

   Se as variáveis não forem definidas, o sistema tenta `root` sem senha como padrão.

3. **Compile e execute**: abra o projeto no NetBeans (usa `build.xml` e `nbproject/`) e rode a classe `com.pizzaria.util.MainApp`, ou compile manualmente:

   ```bash
   javac -cp lib/mysql-connector-j-9.5.0.jar -d build $(find src -name "*.java")
   java -cp "build:lib/mysql-connector-j-9.5.0.jar" com.pizzaria.util.MainApp
   ```

4. **Login de teste** (após rodar o script SQL):
   - Login: `admin` / Senha: `123`
   - Login: `atendente` / Senha: `456`

## 📸 Demonstração

O projeto conta com telas de login, menu principal, cadastro de pedidos, listagens de clientes/produtos/usuários e relatórios diário e mensal.

## 👩‍💻 Autoria

Desenvolvido por **Ana Luiza Pontes Franco** e **Hannah Sumiya**, como trabalho prático da disciplina de Análise e Desenvolvimento de Software — Universidade de Mogi das Cruzes.
