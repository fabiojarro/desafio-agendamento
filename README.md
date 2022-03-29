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

##### Operações
Para fins de testes as seguintes operações foram criadas previamente:

| ID | DESCRIPTION |
|---|---|
| 1 | COMPRA A VISTA |
| 2 | COMPRA PARCELADA |
| 3 | SAQUE |
| 4 | PAGAMENTO |


Desta forma apenas estas operações podem ser utilizdas durante os testes 
 

##### Testes básicos usando curl

- Criando uma conta

```
curl http://localhost:8080/accounts -H 'content-type: application/json' -d '{"document_number":123123123131231}' -v
```
resposta semelhante esperada: 

```json
{"account_id":5,"document_number":123123123131231}
```

- Consultando uma conta

```
curl http://localhost:8080/accounts/5 -H 'content-type: application/json' -v
```

resposta semelhante esperada: 

```json
{"account_id":5,"document_number":123123123131231}
```

- Criando uma transação

```
curl http://localhost:8080/transactions -H 'content-type: application/json' -d '{"account_id":5, "operation_type_id":4, "amount": 123.45}' -v
```
resposta semelhante esperada:

```json
{"id":6,"account":{"account_id":5,"document_number":123123123131231},"operationType":{"id":4,"description":"PAGAMENTO","debt":false},"amount":123.45,"eventDate":"2021-09-07T01:01:02.935+00:00"}
```

##### Documentação
Para acessar a documentação da API:

```
http://localhost:8080/docs.html

http://localhost:8080/api-docs/

```