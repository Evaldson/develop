# develop
# swagger

Projeto desenvolvido como teste técnico do 

##Tecnologias
Para o desenvolvimento da aplicação, foram utilizadas às tecnologias e libs abaixo:

| Nome                     | Versão         |
|:-------------------------|:---------------|
| Java                     | JDK 11         |
| Spring Boot              | 2.5.1          |
| Banco                    | h2 ou Postgres | 
| Maven                    | 3.1.0          |

##Pré requisitos de tecnologia
- Instalar o _**Java 11*_
- Instalar o _**maven 3.1**_


##Execução
- Executar a aplicação basta clonar o repositório da aplicação e depois executar em sua IDE de preferência: IntelJ IDEA ou Spring Tools;
- Pontos a serem observados no processo de execução são:
    - Execução na porta _**8080**_;
    - Para realizar o login será necessário pegar o token de acesso em http://localhost:8080/login 
    passando o seguinte Json:
 ```
    {
 	    "email": "EmailCadastradoNoBanco",
	    "password": "Senha"
    }
 ```
    - Acesso através do swagger-ui = (http://localhost:8080/swagger-ui/index.html#/)
    - Observações a respeito do Swagger: O swagger está documentando a api corretamente, porém não consegui fazer as requisições por ele mesmo passando o token. Então recomendo que use o Postman ou Insonia para fazer as requisições
  

# APIs

O projeto disponibiliza a API /Usuarios, onde o padrão de comunicação é Rest, produzindo e consumindo arquivos no formato JSON.

 - /usuarios (Post)
  - Espera um usuario no body da requisição para cadastro no banco, exemplo:
      ```
    {
 	    "username": "tiago",
	    "email": "tiago@hotmail.com",
	    "password": "123321"
    }
         ```

- /usuarios/task/unfinished (Get)
    - Busca a lista de tarefas não finalizadas do usuário

- /usuarios/task (Put)
 - Adiciona uma nova task para o usuário logado
    ```
    {
 	    "priority": "Baixa",
	    "description": "DESCRICAO"
    }
    ```

- /usuarios/task/{id}/completed (Patch)
 - Muda a task para completed dado o ID na task



- /usuarios/task/{id} (Patch)
 - Altera a task selecionada de acordo com o body da requisição

    ```
    {
 	    "priority": "Alta",
	    "description": "DESCRICAO ALTERADA"
    }
    ```

- /usuarios/task/{id} (Delete)
 - Deleta a task selecionada de acordo com o ID

 # Observação

    Como a API foi desenvolvida utilizando Spring boot, não precisei criar scripts de sql já que o hibernate faz o mapeamento das entidades.

    Assim que a aplicação é executata, eu criei dois usuários no banco para fazer o teste:

   - INSERT INTO usuario (id, username, email, password) VALUES (100, 'john', 'emailfic@hotmail.com', '$2a$10$Z7HSYnyfoqERpl3kMReV1ePr7gZMUJo.hONVMPFOFZtU99rD/6Psu');
   - INSERT INTO usuario (id, username, email, password) VALUES (101, 'jane', 'emailfic2@hotmail.com', '$2a$10$Z7HSYnyfoqERpl3kMReV1ePr7gZMUJo.hONVMPFOFZtU99rD/6Psu');




 
