# 👁️ Ativo e Operante!

Sistema web de denúncias cidadãs desenvolvido como projeto acadêmico. Permite que moradores registrem problemas urbanos com foto, urgência e localização por órgão competente, enquanto administradores gerenciam, respondem e acompanham as ocorrências.

---

## Funcionalidades

**Cidadão**
- Cadastro e login com autenticação JWT
- Registro de denúncias com título, descrição, data, urgência, órgão, tipo e foto
- Visualização das próprias denúncias e feedbacks recebidos

**Administrador**
- Painel com estatísticas (total, urgentes, respondidas)
- Listagem e exclusão de todas as denúncias
- Envio e remoção de feedbacks por denúncia
- Gerenciamento de órgãos e tipos de problema

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 25 + Spring Boot 4 |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL |
| Autenticação | JWT |
| Documentação | Springdoc OpenAPI (Swagger UI) |
| Frontend | HTML, CSS e JavaScript puro |

---

## 🏗️ Arquitetura

```
src/
├── entities/          # Mapeamento JPA (Denuncia, Feedback, Usuario, Orgao, Tipo)
├── repositories/      # Interfaces JpaRepository com queries customizadas
├── services/          # Regras de negócio
├── controllers/       # REST Controllers (acesso, cidadão, admin)
└── security/
    ├── JWTTokenProvider.java    # Geração e validação de tokens
    ├── AccessFilter.java        # Filtro de autenticação por rota
    └── FilterConfiguration.java # Registro dos filtros no Tomcat
```

---

## ⚙️ Como executar

### Pré-requisitos
- Java 25+
- Maven
- PostgreSQL rodando localmente

### Configuração do banco

- Crie um novo banco de dados chamado ativo_operante
- Execute o script SQL disponível na pasta /sql/schema.sql para criar as tabelas necessárias.
- No arquivo src/main/resources/application.properties, ajuste as credenciais do seu banco de dados

### application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost/ativooperante
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update

springdoc.swagger-ui.path=/ativooperante-doc.html
```

### Rodando o projeto

A API estará disponível em `http://localhost:8080`.  
A documentação Swagger em `http://localhost:8080/ativooperante-doc.html`.

---

## 🔐 Autenticação

O sistema usa JWT. Para acessar rotas protegidas:

1. Faça `POST /apis/acesso/autenticar` com `login` e `senha`
2. Copie o token retornado
3. Envie o token no header `Authorization` de todas as requisições seguintes

Rotas protegidas pelo `AccessFilter`:
- `/apis/cidadao/*` — requer nível 2 (cidadão)
- `/apis/adm/*` — requer nível 1 (administrador)

---

## 📁 Estrutura de rotas

### Acesso público
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/apis/acesso/autenticar` | Login |
| POST | `/apis/acesso/cadastrar-cidadao` | Cadastro de cidadão |

### Cidadão
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/apis/cidadao/denuncias` | Registrar denúncia (com foto) |
| GET | `/apis/cidadao/denuncias/usuario/minhas` | Listar minhas denúncias |
| GET | `/apis/cidadao/orgao-all` | Listar órgãos |
| GET | `/apis/cidadao/tipos-all` | Listar tipos |

### Administrador
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/apis/adm/denuncias-all` | Listar todas as denúncias |
| DELETE | `/apis/adm/denuncia/deletar/{id}` | Excluir denúncia |
| POST | `/apis/adm/denuncias/{id}/feedback` | Dar feedback |
| DELETE | `/apis/adm/denuncias/deletar/feedback/{id}` | Remover feedback |
| POST/GET/DELETE | `/apis/adm/orgao` | CRUD de órgãos |
| POST/GET/DELETE | `/apis/adm/tipos` | CRUD de tipos |

---

## 📸 Upload de fotos

As fotos enviadas nas denúncias são salvas em `src/main/resources/static/uploads/` e servidas pelo Spring Boot como recursos estáticos em `/uploads/{nome-do-arquivo}`.

---

## 🤝 Colaboradores

Este projeto foi desenvolvido em parceria com:
* **[Caroliny](https://github.com/carolinymartins)**
