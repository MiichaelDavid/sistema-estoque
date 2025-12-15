# Sistema de Controle de Estoque

Projeto Maven Multi-módulo desenvolvido em Java 8.

## Estrutura

- `estoque-core`: Lógica de negócio, Entidades JPA e acesso a dados.
- `estoque-desktop`: Aplicação Swing para cadastro e movimentação.
- `estoque-web`: Aplicação JSF para visualização e dashboard.

## Pré-requisitos

- Java JDK 1.8
- Maven 3.x
- PostgreSQL (Base de dados: `estoque_db`, Usuário: `postgres`, Senha: `postgres`)

## Configuração do Banco de Dados

Crie o banco de dados antes de rodar a aplicação:

```sql
CREATE DATABASE estoque_db OWNER postgres;
```

O Hibernate está configurado para `hbm2ddl.auto=update`, então as tabelas serão criadas automaticamente ao iniciar a aplicação.

## Compilação

Na raiz do projeto (`sistema-estoque`), execute:

```bash
mvn clean install
```

## Executando as Aplicações

### Backend / Desktop (Swing)

Para rodar a aplicação Desktop, você pode usar sua IDE ou executar o jar gerado (lembrando de configurar o classpath com as dependências).

Via IDE (IntelliJ/Eclipse):
- Execute a classe `com.estoque.desktop.MainFrame` no módulo `estoque-desktop`.

Via Linha de Comando (Jar Executável):
1. Execute `mvn clean install` na raiz.
2. O arquivo executável será gerado em: `estoque-desktop/target/estoque-desktop-1.0-SNAPSHOT.jar`.
3. Rode com: `java -jar estoque-desktop/target/estoque-desktop-1.0-SNAPSHOT.jar`.

### Frontend / Web (JSF)

1. Pegue o arquivo `.war` gerado em `estoque-web/target/estoque-web-1.0-SNAPSHOT.war`.
2. Faça o deploy em um servidor Tomcat 8 ou 9.
3. Acesse: `http://localhost:8080/estoque-web/`

**Alternativa (Jetty via Maven):**
Você pode rodar diretamente sem instalar o Tomcat, usando o plugin do Jetty configurado.
1. Entre na pasta `estoque-web`.
2. Execute: `mvn jetty:run`
3. Acesse: `http://localhost:8080/estoque-web/`

## Funcionalidades

- **Cadastro de Produtos (Desktop)**: Adicione novos produtos com quantidade mínima e valor.
- **Movimentação de Estoque (Desktop)**: Registre ENTRADA ou SAÍDA de produtos.
- **Dashboard (Web)**: Visualize produtos com estoque atual menor que o mínimo.
- **Busca (Web)**: Pesquise produtos e veja o saldo atual calculado em tempo real.
