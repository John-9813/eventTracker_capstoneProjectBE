# 🎯 Event Tracker - Backend

## 📋 Descrizione del Progetto:
L'Event Tracker è un'applicazione RESTful che fornisce un servizio backend per monitorare eventi e notizie.
Utilizzando le API esterne di Ticketmaster per gli eventi e NewsAPI per le notizie, il backend gestisce la logica principale, l'autenticazione e il salvataggio dei dati.

### Link parte frontend: https://github.com/John-9813/event-tracker_capstoneproject_fe

## 🚀 Tecnologie Utilizzate:
Java 21
Spring Boot (Spring Web, Spring Data JPA, Spring Security)
Maven (Gestione delle dipendenze)
PostgreSQL (Database)
Hibernate (ORM)
JWT (Autenticazione e Autorizzazione)
Lombok (Per ridurre boilerplate code)
⚙️ Configurazione del Progetto

### 1. Requisiti Preliminari
Java 21 installato
PostgreSQL installato e configurato
Maven installato

### 2. Configurazione del Database
Crea un database PostgreSQL denominato event_tracker.
Configura il file application.properties per il collegamento al database.

## application.properties

#Server
server.port=${SERVER_PORT}

#DB config
spring.datasource.url=${POSTGRES_URL}
spring.datasource.username=${POSTGRES_USERNAME}
spring.datasource.password=${POSTGRES_PASSWORD}

#Hibernate settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

#Jwt secret
jwt.secret=${JWT_SECRET}

#Api keys
ticketmaster.api.key=${TICKETMASTER_API_KEY}
newsapi.base_url=https://newsapi.org/v2
newsapi.api.key=${NEWS_API_KEY}

## 3. Configurazione delle Chiavi API
Ottieni le chiavi API da:
Ticketmaster: https://developer.ticketmaster.com
NewsAPI: https://newsapi.org

Aggiungi le chiavi nel file application.properties:

### Ticketmaster API Key
ticketmaster.api.key=your-ticketmaster-api-key

### NewsAPI Key
newsapi.api.key=your-newsapi-key

## 🛠️ Avvio dell'Applicazione

### Clona il repository:
git clone https://github.com/your-repository/event-tracker-backend.git

### Vai nella cartella del progetto:
cd event-tracker-backend

### Compila e avvia l'applicazione:
mvn clean install
mvn spring-boot:run

L'applicazione sarà disponibile su:
http://localhost:8080

## 📄 Documentazione API
Puoi testare le API utilizzando Postman o Swagger:

## Endpoint Principali
Autenticazione
POST /auth/login: Effettua il login e restituisce il token JWT.
Eventi
GET /events/filter: Filtra eventi utilizzando città e keyword.
POST /events/saved: Salva un evento preferito.
DELETE /events/saved/{id}: Rimuove un evento preferito.
Notizie
GET /news/external: Recupera le notizie tramite NewsAPI.
POST /news/saved: Salva una notizia preferita.
DELETE /news/saved/{id}: Rimuove una notizia preferita.

## 🔒 Autenticazione
L'autenticazione è gestita tramite JWT (JSON Web Token):

Effettua il login usando l'endpoint POST /auth/login.
Copia il token JWT ricevuto e inseriscilo nel campo Authorization delle richieste.
Authorization: Bearer <token>

## 🧪 Test
Per eseguire i test unitari e di integrazione:
mvn test

## 👨‍💻 Struttura delle Cartelle:
src

│-- main

|   │-- java

|   |   └── com.example.eventtracker

|   |       ├── controllers   # Controller REST

|   |       ├── services      # Logica applicativa

|   |       ├── repositories  # Interfacce per DB

|   |       ├── entities      # Modelli dati (JPA)

|   |       ├── dto           # Data Transfer Objects

|   |       └── security      # Configurazione JWT

|   │-- resources

|       ├── application.properties  # Configurazione app

└── test

    └── java # Test unitari
    
    
## 📫 Contatti

Se hai bisogno di ulteriori informazioni o supporto:
John Oliveira
Email: john.oliver98.br@gmail.com
LinkedIn: https://www.linkedin.com/in/john-oliver-2205aa247
