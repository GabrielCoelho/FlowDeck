# FlowDeck

**FlowDeck** é um sistema de gerenciamento de workflow baseado em quadros kanban, desenvolvido com Java Spring Boot e React. A aplicação permite organizar tarefas em quadros (boards), colunas (columns) e cartões (cards), facilitando a visualização do fluxo de trabalho e aumentando a produtividade de equipes.

![FlowDeck Banner](https://via.placeholder.com/800x200?text=FlowDeck+-+Workflow+Management)

## 🎯 Visão Geral

O FlowDeck foi projetado para oferecer uma experiência intuitiva de gerenciamento de tarefas, onde os usuários podem:

- Criar múltiplos quadros para diferentes projetos ou workflows
- Personalizar colunas dentro de cada quadro
- Adicionar cartões representando tarefas ou itens de trabalho
- Mover cartões entre colunas à medida que progridem no workflow
- Bloquear cartões quando há impedimentos, registrando o motivo
- Visualizar métricas e relatórios sobre o progresso do trabalho

## 🚀 Funcionalidades

### Boards (Quadros)

- Criação, edição e exclusão de quadros
- Visualização resumida com estatísticas (número de colunas, cards)
- Cada quadro vem com colunas padrão pré-configuradas

### Columns (Colunas)

- Colunas especiais: Inicial, Final e Cancelado
- Colunas personalizáveis: adicionar, editar, reordenar e excluir
- Visualização do número de cartões por coluna

### Cards (Cartões)

- Criação de cartões com título e descrição
- Movimentação entre colunas de acordo com o progresso
- Visualização detalhada com histórico de bloqueios
- Cancelamento de cartões (move para coluna Cancelado)

### Blocks (Bloqueios)

- Bloqueio de cartões com registro de motivo
- Desbloqueio de cartões com registro de resolução
- Visualização de histórico de bloqueios por cartão
- Relatórios de bloqueios por período

## 🛠️ Tecnologias

### Backend

- **Java 17+**
- **Spring Boot 3.2.5**
- **Spring Data JPA**
- **Hibernate**
- **MySQL** (banco de dados principal)
- **H2** (banco de dados para testes)
- **Liquibase** (versionamento de banco de dados)
- **JUnit 5** e **Mockito** (testes)
- **Lombok** (redução de boilerplate)
- **Maven** (gerenciamento de dependências)

### Frontend (Planejado)

- **React 18+**
- **TypeScript**
- **Tailwind CSS**
- **React Router**
- **React Query**
- **Axios**
- **React Beautiful DnD** (drag-and-drop)

## 📐 Arquitetura

O projeto segue uma arquitetura em camadas com clara separação de responsabilidades:

### Camadas do Backend

1. **Model**
   - Entidades JPA (`Board`, `BoardColumn`, `Card`, `Block`)
   - Enums (`BoardColumnKind`)

2. **Repository**
   - Interfaces JPA Repository (`BoardRepository`, `BoardColumnRepository`, etc.)
   - DAOs personalizados para consultas complexas

3. **Service**
   - Lógica de negócio encapsulada em serviços
   - Transações e validações

4. **Controller**
   - Endpoints REST para CRUD de entidades
   - Retorno de DTOs ao invés de entidades diretas

5. **DTO (Data Transfer Objects)**
   - DTOs básicos para todas as entidades (`BoardDTO`, `CardDTO`, etc.)
   - DTOs resumidos para listagens (`BoardSummaryDTO`, `CardSummaryDTO`)
   - DTOs específicos para requisições (`CreateBoardRequest`, `BlockCardRequest`, etc.)
   - Mappers para conversão entre entidades e DTOs

6. **Exception**
   - Exceções personalizadas para diferentes cenários de negócio
   - Manipulador global de exceções

### Camadas do Frontend (Planejado)

1. **Components**
   - Componentes reutilizáveis de UI
   - Componentes específicos de domínio (Board, Card, etc.)

2. **Pages**
   - Páginas principais da aplicação (Dashboard, BoardView, etc.)

3. **Services**
   - Integração com a API REST
   - Transformação de dados

4. **Hooks**
   - Custom hooks para lógica reutilizável
   - Hooks de integração com API

5. **Contexts**
   - Gerenciamento de estado global
   - Temas e configurações

## 📚 Padrões de Projeto Implementados

- **Repository Pattern**: Acesso a dados através de interfaces específicas
- **DAO Pattern**: Consultas personalizadas e complexas
- **Service Layer**: Encapsulamento da lógica de negócio
- **DTO Pattern**: Transferência segura de dados sem ciclos de serialização
- **Builder Pattern**: Construção flexível de objetos complexos (via Lombok)
- **Factory Method**: Criação de objetos em serviços
- **Exception Handling Strategy**: Tratamento centralizado de exceções

## 📦 Modelos de Dados

### Board

- Representa um quadro de trabalho
- Possui nome e uma lista de colunas
- Oferece métodos para acessar colunas especiais (inicial, final, cancelado)

### BoardColumn

- Representa uma coluna dentro de um quadro
- Possui nome, ordem e tipo (INITIAL, FINAL, CANCEL, PENDING)
- Contém uma lista de cartões

### Card

- Representa uma tarefa ou item de trabalho
- Possui título, descrição, datas de criação/atualização
- Pertence a uma coluna e pode ter bloqueios

### Block

- Representa um bloqueio aplicado a um cartão
- Contém motivo do bloqueio, data de bloqueio, motivo e data de desbloqueio

## 🔄 API REST

### Endpoints

#### Boards

```
GET    /api/boards                # Lista todos os boards
GET    /api/boards/{id}           # Obtém um board específico
POST   /api/boards                # Cria um novo board
PUT    /api/boards/{id}           # Atualiza um board
DELETE /api/boards/{id}           # Exclui um board
```

#### Columns

```
GET    /api/boards/{boardId}/columns                # Lista colunas de um board
GET    /api/boards/{boardId}/columns/{id}           # Obtém uma coluna específica
POST   /api/boards/{boardId}/columns                # Cria uma nova coluna
PUT    /api/boards/{boardId}/columns/{id}           # Atualiza uma coluna
DELETE /api/boards/{boardId}/columns/{id}           # Exclui uma coluna
POST   /api/boards/{boardId}/columns/reorder        # Reordena colunas
```

#### Cards

```
GET    /api/cards/board/{boardId}         # Lista cards de um board
GET    /api/cards/column/{columnId}       # Lista cards de uma coluna
GET    /api/cards/{id}                    # Obtém um card específico
POST   /api/cards/board/{boardId}         # Cria um novo card
PUT    /api/cards/{id}                    # Atualiza um card
DELETE /api/cards/{id}                    # Exclui um card
POST   /api/cards/{id}/move/{columnId}    # Move um card para outra coluna
POST   /api/cards/{id}/cancel             # Cancela um card
```

#### Blocks

```
GET    /api/cards/{cardId}/blocks         # Lista bloqueios de um card
GET    /api/cards/{cardId}/blocks/status  # Verifica se um card está bloqueado
POST   /api/cards/{cardId}/blocks         # Bloqueia um card
POST   /api/cards/{cardId}/blocks/unblock # Desbloqueia um card
```

## 🚀 Próximos Passos

- Implementação da interface de usuário com React
- Funcionalidade de arrastar e soltar (drag-and-drop) para cards
- Filtros e pesquisa de cards
- Autenticação e autorização
- Notificações para eventos de cards e bloqueios
- Métricas e dashboards avançados

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- MySQL 8.0 ou superior
- Node.js 16+ e npm (para o frontend)

## 🔧 Configuração e Execução

### Backend

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/flowdeck.git
cd flowdeck
```

2. Configure o banco de dados em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost/flowdeck
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

3. Compile e execute o projeto:

```bash
mvn clean install
mvn spring-boot:run
```

4. Acesse a API em <http://localhost:8080/api/>

## 👥 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.
