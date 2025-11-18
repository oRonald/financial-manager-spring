<h1>
  <span>Gerenciador de Finanças Pessoais</span>
  <img width="50" height="50" alt="logo" src="https://github.com/user-attachments/assets/28b6f7ee-883f-445f-9d40-550650734044" />
</h1>

<p>Sistema desenvolvido em Spring Boot com Arquitetura de Camadas.</p>
Permite que o usuário administre suas finanças com EXPENSE (despesa) e INCOME (renda), com geração de um relatório pessoal em PDF enviador por email.
<p>Também desenvolvi um sistema de trocar senha com base em um token enviado via email para validação</p>

## Fluxo
1. O usuário realiza o cadastro.
2. O usuário pode criar sua conta. (Ex: Conta Corrente, Conta Salário)
3. O usuário pode criar uma nova transação do tipo INCOME ou EXPENSE
4. O usuário pode solicitar um histórico de transações
5. É possível gerar um relatório em PDF de todas as transações realizadas

## Design Patterns usados:
- Strategy Pattern
- Data Transfer Object

## Tecnologias
- Java 17
- Spring Boot (Security, JPA, Data MongoBD, Web)
- Spring Mail
- Validations
- Token JWT
- PostgreSQL
- MongoDB
- Docker / Docker Compose

## Como utilizar?

<p>A aplicação possui o docker-compose de todos os componentes, sendo necessário apenas baixar este repositório.</p>
<p>Após baixar os arquivos do repositório, abrir em sua IDE favorita, use este comando no terminal:</p> <strong>docker-compose up --build</strong> <br> <br>
e acessar por essa URL: <br> <br> <strong>http://localhost:8080/swagger-ui/index.html#/</strong>
