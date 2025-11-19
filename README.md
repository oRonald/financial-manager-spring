# 💰 Gerenciador de Finanças Pessoais

<p>Sistema completo de gerenciamento de finanças pessoais com autenticação JWT, geração de relatórios em PDF e notificações por email.</p>

## 📋 Sobre o Projeto

API RESTful desenvolvida com Spring Boot que permite aos usuários gerenciar suas finanças através de contas bancárias, transações de receitas e despesas, com geração automática de relatórios financeiros.

## Funcionalidades Principais
- ✅ Autenticação e Autorização com JWT e Spring Security
- ✅ Gerenciamento de Contas (Conta Corrente, Conta Salário, etc.)
- ✅ Transações Financeiras: INCOME (receita) e EXPENSE (Despesa)
- ✅ Relatórios em PDF com histórico completo de transações
- ✅ Recuperação de Senha via token por email
- ✅ Envio de Emails para relatórios de transações

## 🏗️ Arquitetura e Design Patterns

### Arquitetura em Camadas:
- Controller → Service → Repository

### Design Patterns Implementados:

- DTO (Data Transfer Object) - Transferência segura de dados entre camadas

## 🛠️ Tecnologias Utilizadas

### Backend:

- Java 17
- Spring Boot
  - Spring Security (JWT)
  - Spring Data JPA
  - Spring Data MongoDB
  - Spring Web
  - Spring Mail
- Bean Validation

### Bancos de Dados:

- PostgreSQL (usuarios/transações)
- MongoDB (cache de tokens de recuperação de senhas e auditoria de transações)

### Testes:

- JUnit 5
- Mockito

### DevOps:

- Docker
- Docker Compose

# 📚 Documentação da API

<strong>*Nessa aplicação foi implementado o Swagger UI (Documentação Interativa)</strong>

## 🔗 Endpoints
### Usuários
- POST /users - Cadastra um novo usuário
- POST /users/login - Realiza o login com email e senha
- 🔐 POST /users/account - Cria uma nova conta para o usuário
- POST /users/recovery-token - Gera um token que é enviado para o email do usuário
- POST /users/change-password - A partir do token enviado pelo email o usuário consegue trocar a senha de sua conta

### Contas
- 🔐 POST /accounts - O usuário consegue criar uma nova transação a partir do nome da conta
- 🔐 GET /accounts - Retorna todas as transações da conta
- 🔐 POST /accounts/statement-report - É gerado e enviado por email um PDF com todas as transações do usuário

## 🔐 Segurança
*Todos os endpoints com o 🔐 necessitam de autenticação que é realizada pelo endpoint: POST /users/login
- Autenticação via JWT (JSON Web Token)
- Senhas criptografadas com BCrypt
- Validação de entrada em todos os endpoints
- Tokens de recuperação de senha com expiração

# 🔄 Fluxo de Uso

- Cadastro: Usuário cria conta no sistema
- Login: Recebe token JWT para autenticação
- Criar Conta Bancária: Define contas (Corrente, Salário, etc.)
- Registrar Transações: Adiciona receitas (INCOME) e despesas (EXPENSE)
- Consultar Histórico: Visualiza todas as transações
- Gerar Relatório: Recebe PDF por email com resumo de todas as transações

## 📫 Contato
Ronald de Oliveira Farias<br>
📧 ronaldfarias.oliveira@gmail.com<br>
💼 <a href="https://www.linkedin.com/in/ronald-de-oliveira-farias-274b411a3/">Linkedin</a>
