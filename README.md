# 📚 Guia de Estudos: CRUD com Spring Boot

Este documento resume os conceitos aplicados no projeto de gerenciamento de Clientes (`testeCrud`). O objetivo é fixar o entendimento sobre a arquitetura MVC, injeção de dependências e JPA.

---

## 🏗️ 1. Arquitetura do Projeto

O projeto segue o fluxo clássico em camadas do Spring Boot:

1.  **Controller (`clienteController`)**: Recebe a requisição HTTP (do Postman, navegador ou Front-end).
2.  **Service (`ClienteService`)**: Contém a regra de negócio (validações, cálculos).
3.  **Repository (`ClienteRepository`)**: Comunica-se diretamente com o Banco de Dados.
4.  **Model/Entity (`Cliente`)**: Representa a tabela do banco de dados em forma de classe Java.

---

## 🧠 2. Conceitos por Camada

### A. Model (`Cliente.java`)
Define a estrutura dos dados. Usamos **JPA** para mapear para o banco e **Lombok** para reduzir código.

* `@Entity`: Diz ao Spring que essa classe representa uma tabela no banco.
* `@Table(name = "clientes")`: Define o nome exato da tabela.
* `@Id` & `@GeneratedValue`: Define a Chave Primária (PK) e como ela é gerada (Auto-incremento).
* **Lombok**:
    * `@Data`: Cria automaticamente Getters, Setters, toString, equals e hashcode.
    * `@AllArgsConstructor` / `@NoArgsConstructor`: Cria os construtores com e sem argumentos.

### B. Repository (`ClienteRepository.java`)
Responsável pelo acesso aos dados (DAO).

* `extends JpaRepository<Cliente, Integer>`: A mágica do Spring Data. Ao estender essa interface, você ganha de graça métodos como `.save()`, `.findAll()`, `.delete()`, sem precisar escrever SQL.
* `@Repository`: Indica que é um componente de persistência.

### C. Service (`ClienteService.java`)
A camada intermediária de inteligência.

* `@Service`: Indica que é uma classe de serviço (Regra de Negócio).
* `@Autowired`: **Injeção de Dependência**. O Spring inicializa o `ClienteRepository` e o "injeta" aqui dentro, para que você possa usá-lo sem dar `new`.
* **Optional**: Usado no método `listarClienteById` para evitar `NullPointerException` caso o cliente não exista.

### D. Controller (`clienteController.java`)
A porta de entrada da API (REST).

* `@RestController`: Combina `@Controller` e `@ResponseBody`. Retorna dados (JSON) diretamente, não páginas HTML.
* `@RequestMapping("/clientes")`: Define a URL base.
* **Verbos HTTP**:
    * `@GetMapping`: Para buscar dados.
    * `@PostMapping`: Para criar novos dados.
* `ResponseEntity`: Usado para controlar a resposta HTTP (ex: retornar status 404 se não achar o ID, ou 200 OK se achar).

---

## ⚙️ 3. Configurações (`application.properties`)

Configurações do ambiente e banco de dados.

* **Banco H2**: `jdbc:h2:mem:crud` configura um banco em memória. Ele apaga os dados quando a aplicação para.
* `ddl-auto=update`: O Hibernate verifica suas classes `@Entity` e cria/atualiza as tabelas automaticamente no banco.

---

## 📝 4. Glossário de Anotações

| Anotação | Função | Camada Típica |
| :--- | :--- | :--- |
| `@SpringBootApplication` | Inicializa a aplicação Spring Boot. | Main |
| `@Autowired` | Injeta uma dependência (ex: Repository dentro do Service). | Todas |
| `@PathVariable` | Pega um valor direto da URL (ex: `/clientes/1`). | Controller |
| `@RequestBody` | Pega o JSON enviado no corpo da requisição e transforma em Objeto Java. | Controller |
| `@Column` | Configura detalhes da coluna no banco (nome, nullable). | Model |

---

## ⚠️ 5. Pontos de Atenção (Para refatorar)

Durante o estudo do código, notei dois detalhes importantes para corrigir no futuro:

1.  **Tipagem do ID**:
    * Na classe `Cliente`, o ID é `Long`.
    * No `ClienteRepository` e `Service`, está sendo usado `Integer`.
    * *Dica:* Padronize tudo para `Long` para evitar erros de conversão.

2.  **Lógica do Update**:
    * No método `atualizarCliente` em `ClienteService.java`, há um erro de "Copy/Paste":
        ```java
        clienteExiste.setNome(clienteAtualizado.getNome());
        clienteExiste.setNome(clienteAtualizado.getEndereco()); // Erro: Sobrescrevendo Nome com Endereço
        clienteExiste.setNome(String.valueOf(clienteAtualizado.getAtivo())); // Erro: Sobrescrevendo Nome com Ativo
        ```
    * *Correção:* Usar os setters corretos (`setEndereco`, `setAtivo`).
