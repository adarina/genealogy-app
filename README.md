# Genealogy App – Microservices System

A professional, comprehensive system for managing genealogical data, building family trees, and visualizing family history through geographical mapping. This project is built using a modern microservices architecture with a reactive backend and an Angular frontend.

## 🏗 System Architecture

The ecosystem consists of several specialized components working together:

* **[Genealogy App (Core)](https://github.com/adarina/genealogy-app):** The primary microservice managing individuals, families, and events, citations, sources, locations, geography using Java and Spring Boot and Neo4j.
* **[App Reactive](https://github.com/adarina/genealogy-app-reactive):** A reactive module designed for high-performance streaming and processing of genealogical data such as imports, exports.
* **[App User](https://github.com/adarina/genealogy-app-user):** Dedicated service for authentication, authorization, and user profile management (includes the Login/Sign-up logic) with PostgreSQL.
* **[Eureka Server](https://github.com/adarina/eureka-server):** The Service Discovery server that allows microservices to find and communicate with each other.
* **[Angular Frontend](https://github.com/adarina/genealogy-app-angular):** A responsive, feature-rich web interface built with Angular.


## 🚀 Key Features

Based on the application's capabilities, the system provides:

* **Entity Management Views:** Dedicated views for **Persons, Families, Events, Citations, Sources, and Locations**, where you can perform advanced searches for individual entities, filter through complex datasets, and seamlessly add new records.
  
![Alt text](/img/5.png?raw=true)

![Alt text](/img/6.png?raw=true)

![Alt text](/img/8.png?raw=true)

![Alt text](/img/7.png?raw=true)

![Alt text](/img/9.png?raw=true)

* **Person Management:** Full CRUD operations for persons, including specialized views for events, ancestors (with horizontal charts), family affiliations, and an interactive map showing the person's life events.

![Alt text](/img/10.png?raw=true)

![Alt text](/img/11.png?raw=true)

![Alt text](/img/12.png?raw=true)

* **Family Management:** Full CRUD operations for families, featuring detailed views for family-specific events and children.
* **Events Management:** Full CRUD operations for events, including the management of citations and participants.
* **Citations & Sources:** Full CRUD operations for citations (including file attachments) and sources, ensuring high-quality data documentation.
* **Files & Media:** Robust management of files associated with genealogical records.
* **Geography & Mapping:** Full CRUD operations for locations, integrated with **OpenStreetMap** to visualize geographical data.
* **Data Portability:** Advanced tools to import and export data via **CSV** and the industry-standard **GEDCOM** format.

![Alt text](/img/3.png?raw=true)

![Alt text](/img/4.png?raw=true)

* **User:** Log In and Sign Up

![Alt text](/img/1.png?raw=true)

![Alt text](/img/2.png?raw=true)

## 🛠 Tech Stack

### Backend
* **Java 21**
* **Spring Boot**
* **PostgreSQL**
* **Neo4j**

### Frontend & DevOps
* **Angular**
* **Docker & Docker Compose** (for containerization)
* **Maven** (project management)
