# Microserviço de Cadastro de Eventos

Este é um projeto de exemplo de um microserviço de cadastro de eventos desenvolvido com Java 17, Spring Boot, PostgreSQL e Docker Compose. O objetivo deste microserviço é fornecer funcionalidades para cadastrar eventos, adicionar cupons de desconto e associar um endereço a cada evento.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- PostgreSQL
- Docker Compose

## Funcionalidades Principais

1. Cadastro de Eventos: Os usuários podem cadastrar eventos fornecendo informações como nome, descrição, data, etc.

2. Adição de Cupons de Desconto: Os usuários podem adicionar cupons de desconto aos eventos, especificando o código do cupom e a porcentagem de desconto.

3. Associação de Endereço: Cada evento pode estar associado a um endereço, contendo informações como rua, número, cidade, etc.

## Estrutura do Projeto

O projeto segue uma arquitetura baseada em Domain-Driven Design (DDD) e segue os princípios SOLID. As principais camadas do projeto incluem:

- **Controller**: Responsável por receber e responder às solicitações HTTP.
- **Service**: Contém a lógica de negócios da aplicação, incluindo a lógica de cadastro de eventos, adição de cupons, etc.
- **Repository**: Responsável pela interação com o banco de dados PostgreSQL.
- **DTO**: Contém objetos de transferência de dados para comunicação entre camadas.

## Configuração do Ambiente

1. Certifique-se de ter o Java 17 instalado em sua máquina.
2. Instale o Docker e o Docker Compose para a execução do PostgreSQL e outros contêineres necessários.
3. Clone o repositório do projeto.
4. Execute `docker-compose up` na raiz do projeto para iniciar os contêineres Docker.

## Como Executar

1. Importe o projeto em sua IDE preferida.
2. Execute a classe principal `Application.java` para iniciar o microserviço.
3. O serviço estará acessível em `http://localhost:8080`.

## Testes (Em Desenvolvimento)

Os testes automatizados ainda estão em desenvolvimento para garantir a qualidade do código. Eles serão adicionados em futuras atualizações do projeto.

## Integração com Microserviço de Usuários

A ideia desse microserviço é consumir os usuários do outro microserviço através do Kong, projeto : https://github.com/matheusparro/ManagementUsersJwtSpring

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para enviar pull requests ou relatar problemas.

## Licença

Este projeto é licenciado sob a [MIT License](LICENSE).
