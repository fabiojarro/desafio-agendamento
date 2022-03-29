### Desafio Agendamento

#### Objetivo
A API criada possui endpoints para atender as seguintes entidades:

- Cliente

	- cadastrar um novo cliente.
	- listar clientes
	- alterar um cliente
	- remover cliente
	
- Serviço

	- cadastrar um novo serviço.
	- listar serviço
	- alterar serviço
	- remover serviço

- Agendamento

	- cadastrar um novo agendamento.
	- listar agendamentos
	- listar agendamentos agrupados por data e valor
	- reagendar agendamentos existentes
	- sumarizar agendamentos por cliente e data
		
#### Tecnologias

A api foi construída utilizando Spring Boot como framework principal e JPA com Spring Data para acesso ao banco relacional.

O banco H2 foi utilizado como banco relacional para esta aplicação, desta forma é possível testar as requisições para API. 

Foi Utilizado Maven para gerenciar as depências e git para o versionamento.

#### Requisitos
- Maven 3.6.1 ou superior
- Openjdk 8 ou Java 8
- Porta 8080 livre para poder ser utilizada.


##### Executando a aplicação
Para executar a aplicação é necessário primeiro realizar o build usando maven através do seguinte comando:

```
mvn clean install
```
A aplicação pode ser executada diretamente na máquina utilizando o seguinte comando:

```
java -jar target/desafio.jar
```

Ou criando um imagem do Docker e executando a imagem criada. Exexmplo:

```
docker build -t desafio:v1 .

docker run -p 8080:8080 desafio:v1

```

##### Documentação
Para acessar a documentação da API:

```
http://localhost:8080/docs.html

http://localhost:8080/api-docs/

```