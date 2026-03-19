# Sistema Distribuído de Relógio Mundial (TCP/UDP)

## 📌 Resumo do Projeto

Este projeto tem como objetivo demonstrar a comunicação entre processos distribuídos utilizando **Sockets em Java**. A aplicação consiste num sistema de "Relógio Mundial" onde um cliente informa o ID de uma região geográfica (ex: `America/Sao_Paulo`, `Europe/London`, `Asia/Tokyo`) e o servidor devolve a data e hora exatas daquele local.

A atividade explora as diferenças fundamentais entre protocolos de rede e a evolução da arquitetura do servidor, sendo dividida em três versões:
1. **UDP (Não orientado à conexão):** Foco em economia de recursos e tratamento de perdas de pacotes com a implementação de um *Timeout* no cliente.
2. **TCP Single-Thread (Orientado à conexão):** Foco na garantia de entrega e fluxo de dados contínuo (Streams), porém com processamento sequencial.
3. **TCP Multithread (Concorrente):** Foco em escalabilidade, permitindo que o servidor atenda múltiplos clientes simultaneamente através da criação de Threads dedicadas.

---

## 📂 Estrutura de Pastas

* `/udp-clock`: Código-fonte do Cliente e Servidor utilizando o protocolo UDP.
* `/tcp-clock-simple`: Código-fonte do Cliente e Servidor utilizando TCP sequencial.
* `/tcp-clock-multithread`: Código-fonte do Servidor TCP com suporte a múltiplas conexões (Multithread).

---

## 🚀 Instruções de Execução

Para executar o projeto, você precisará ter o [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) instalado na sua máquina. Abra dois terminais (um para o servidor e outro para o cliente).

### Versão 1: UDP
**No Terminal 1 (Servidor):**
` ` `bash
cd udp-clock
javac ServerUDP.java
java ServerUDP
` ` `

**No Terminal 2 (Cliente):**
` ` `bash
cd udp-clock
javac ClientUDP.java
java ClientUDP
` ` `

### Versão 2: TCP Single-Thread
**No Terminal 1 (Servidor):**
` ` `bash
cd tcp-clock-simple
javac ServerTCPSimple.java
java ServerTCPSimple
` ` `

**No Terminal 2 (Cliente):**
` ` `bash
cd tcp-clock-simple
javac ClientTCP.java
java ClientTCP
` ` `

### Versão 3: TCP Multithread
**No Terminal 1 (Servidor):**
` ` `bash
cd tcp-clock-multithread
javac Worker.java ServerTCPMultithread.java
java ServerTCPMultithread
` ` `

**No Terminal 2 (Cliente):**
*(Nota: Para testar esta versão, você pode utilizar o `ClientTCP` da versão 2, alterando a variável `porta` no código do cliente para `9878` e recompilando, ou usar comandos como o `telnet` ou `nc`)*.
` ` `bash
nc localhost 9878
# Após conectar, digite a região, ex: America/Sao_Paulo
` ` `

---

## 📊 Análise Técnica: Single-Thread vs Multithread

Ao submeter a aplicação a um cenário de múltiplas ligações simultâneas, a diferença de performance entre as arquiteturas TCP (Versão 2 e Versão 3) torna-se bastante evidente:

* **Versão 2 (Single-Thread):** O servidor atende um único cliente de cada vez. O processo de aceitar a conexão, ler a requisição, buscar a hora na biblioteca do Java e enviar a resposta bloqueia o servidor. Se 100 clientes tentarem se conectar ao mesmo instante, 99 ficarão bloqueados na fila de espera do Sistema Operacional até que o cliente anterior seja totalmente servido e o seu *socket* fechado. O tempo de resposta para os últimos clientes da fila degrada-se significativamente.
* **Versão 3 (Multithread):** Resolve o gargalo de processamento. Ao receber a ligação através do `accept()`, o fluxo principal (a *Main Thread*) não executa as operações de I/O. Em vez disso, instancia imediatamente um objeto `Worker` e inicia uma nova *Thread* (`threadProcessamento.start()`), delegando o trabalho e voltando instantaneamente para o loop do `accept()`. Isso permite um processamento em paralelo, onde múltiplos clientes recebem a resposta ao mesmo tempo (limitado apenas pelo número de núcleos e *threads* suportados pelo hardware do servidor), garantindo alta escalabilidade e tempos de resposta consistentemente baixos.
