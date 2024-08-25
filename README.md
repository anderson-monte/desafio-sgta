# Sistema de Gerenciamento de Tarefas Abstrato
 
## Descrição
Este projeto foi desenvolvido para o desafio técnico da empresa SUPERA TECNOLOGIA. Ele consiste em um sistema básico de gerenciar Listas e seus respectivos Itens.

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3.3.3
- PostgreSQL
- Docker
- Docker Compose
- TestContainers

## Como rodar o projeto
Para rodar o projeto, é necessário ter o Docker e o Docker Compose instalados na máquina.
Com isso, basta rodar o comando abaixo na raiz do projeto:
```shell
docker-compose up --build
```

## Endpoints
### Listas
- **GET** /lists
- **GET** /lists/{id}
- **POST** /lists
- **DELETE** /lists/{id} 
- **DELETE** /lists/{id}/items/{itemId}

>⚠️ Para filtrar as listas por nome, basta passar o parâmetro `name`. Exemplo: `/lists?name=sample`

**Exemplo de requisição:**
```json
{
  "name": "Lista de Compras"
}
```

**Exemplo de resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "LISTA DE COMPRAS",
      "items": []
    }
  ],
  "page": {
    "size": 10,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Itens
- **GET** /Items
- **GET** /Items/{id}
- **POST** /Items
- **POST** /Items/{id}active
- **POST** /Items/{id}inactive
- **POST** /Items/{id}/highlight
- **POST** /Items/{id}/remove-highlight

>⚠️ Para filtrar as itens por nome, basta passar o parâmetro `name`. Exemplo: `/items?name=sample`

**Exemplo de requisição:**
```json
{
  "name": "Arroz",
  "list": "LISTA DE COMPRAS"
}
```

**Exemplo de resposta:**
```json
{
  "id": 1,
  "name": "LISTA DE COMPRAS",
  "items": [
    {
      "id": 1,
      "name": "ARROZ",
      "state": "PENDING",
      "highlighted": false
    }
  ]
}
```

## Testes
Para rodar os testes, deve-se ter instalado o maven na máquina. Com isso, basta rodar o comando abaixo na raiz do projeto:
```shell
  mvn test
```
Ou utilizar alguma IDE que suporte a execução de testes JUnit, principalmente para os testes de integração.