# Galerium FullStack

<p align="center">
<img src="https://img.shields.io/badge/Java-17-blue?logo=java&logoColor=white" alt="Java 17">
<img src="https://img.shields.io/badge/Spring_Boot-3.x-green?logo=spring&logoColor=white" alt="Spring Boot 3.x">
<img src="https://img.shields.io/badge/React-18-blue?logo=react&logoColor=white" alt="React 18">
<img src="https://img.shields.io/badge/Vite-blue?logo=vite&logoColor=white" alt="Vite">
<img src="https://img.shields.io/badge/MySQL-8-orange?logo=mysql&logoColor=white" alt="MySQL 8">
<img src="https://img.shields.io/badge/Code_Style-Prettier-ff69b4?logo=prettier&logoColor=white" alt="Code Style: Prettier">
</p>

A full-stack CRUD application designed for photographers to manage clients, galleries, and sessions. This project was developed as the final project for Module 3 of the IronHack Web Development Bootcamp.

## 📋 Table of Contents

- [✨ Key Features](#-key-features)
- [📸 Screenshots](#-screenshots)
- [🛠️ Tech Stack](#️-tech-stack)
- [🚀 Getting Started Locally](#-getting-started-locally)
- [⚙️ Configuration](#️-configuration)
- [📚 API Endpoints](#-api-endpoints)
- [🧪 Testing](#-testing)
- [🔧 Development Tooling & Quality Assurance](#️-development-tooling--quality-assurance)
- [🏗️ Architectural Decisions](#️-architectural-decisions)
- [🏆 Challenges & Learnings](#-challenges--learnings)
- [🗂️ Project Structure](#️-project-structure)
- [📈 Project Management & Presentation](#-project-management--presentation)
- [👤 Author](#-author)

---

## ✨ Key Features

This application was built with a strong focus on a clean user experience and a robust, scalable architecture.

* **Full Client Management:** Complete CRUD functionality (Create, Read, Update, Delete) for client records.
* **Dynamic Data Handling:**
    * Server-side pagination and sorting for efficient data management.
    * Debounced text search across multiple client fields (name, email, phone).
    * Intuitive, multi-select tag filtering to easily segment clients.
* **Advanced User Experience (UX):**
    * **User-Centric Feedback:** The UI provides constant feedback with flicker-free loading states, clear error messages on API failures, and "no results" notifications.
    * **Advanced Tag System:** The client form features a tag input with autocomplete, suggesting existing tags to ensure data consistency while still allowing the creation of new ones on the fly.
    * **Interactive Modals:** Confirmation modals for destructive actions (like deleting) are user-friendly, featuring smooth animations and the ability to close by clicking outside.
    * **Image Previews:** The client form provides an instant preview for image URLs, with elegant error handling for broken links.
* **Consistent Design System:** A unified styling guide with CSS variables ensures a consistent and professional look and feel across all components.

<div align="right">
    <a href="#-table-of-contents">⬆️ Back to Top</a>
</div>

---

## 📸 Screenshots

<p align="center">
  <em>Client list with search, multi-tag filtering, and pagination.</em>
  <br>
  <img src="./docs/images/filter-demo.gif" alt="Client Filter Demo" width="600">
</p>

---

## 🛠️ Tech Stack

| Area       | Technologies                                              |
| :--------- | :-------------------------------------------------------- |
| **Frontend** | React, Vite, React Router,  `react-hot-toast`           |
| **Backend** | Java, Spring Boot, Spring Data JPA, Spring Security   |
| **Database** | MySQL (Production), H2 (Development)                      |
| **Testing** | JUnit 5, Mockito (Backend), Vitest, RTL (Frontend)        |
| **Tooling** | Maven, Node.js, ESLint, Prettier                          |

<div align="right">
    <a href="#-table-of-contents">⬆️ Back to Top</a>
</div>
---

## 🚀 Getting Started Locally

### Prerequisites

* Java 17+
* Node.js 18+
* Maven 3+
* Git

### 1. Backend Setup

```bash
# Clone the repository
git clone https://github.com/AidaG91/Galerium-FullStack.git
cd galerium-fullstack/backend

# Install dependencies and build the project
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run

# The backend will be available at http://localhost:8080
# You can check the health status at: http://localhost:8080/api/health
```

### 2. Frontend Setup
```bash
# In a new terminal, navigate to the frontend directory
cd ../frontend

# Install dependencies
npm install

# Create the local environment file
cp .env.example .env

# Run the development server
npm run dev

# The application will be available at http://localhost:5173
```

<div align="right">
    <a href="#-table-of-contents">⬆️ Back to Top</a>
</div>

---

## Configuration
The project uses configuration files for both the backend and frontend.

### Backend (backend/src/main/resources/application.properties)
For development, the application uses an in-memory H2 database. For production, you should configure the connection to a MySQL database and set the ddl-auto property to update.

```properties
# Development (H2)
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.url=jdbc:h2:mem:galeriumdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Production (MySQL Example)
# spring.jpa.hibernate.ddl-auto=update
# spring.datasource.url=jdbc:mysql://localhost:3306/galerium
# spring.datasource.username=root
# spring.datasource.password=yourpassword
```

### Frontend (frontend/.env)
The frontend needs to know the base URL of the backend API. Copy this file to .env for local development.

```dotenv 
VITE_API_URL=http://localhost:8080/api
```

<div align="right">
<a href="#-table-of-contents">⬆️ Back to Top</a>
</div>

---

## 📚 API Endpoints

The core API for the `Client` resource follows RESTful conventions.

| Method   | Endpoint                      | Description                                |
| :------- | :---------------------------- | :----------------------------------------- |
| `GET`    | `/api/health`                 | Checks the database connection status.     |
| `GET`    | `/api/tags`                   | Retrieves a list of all unique tags.       |
| `GET`    | `/api/clients/paged`          | Lists clients with pagination and sorting. |
| `GET`    | `/api/clients/search/paged`   | Searches clients by text query.            |
| `GET`    | `/api/clients/by-tag`         | Filters clients by one or more tags.       |
| `POST`   | `/api/clients`                | Creates a new client.                      |
| `GET`    | `/api/clients/{id}`           | Retrieves a single client by ID.           |
| `PUT`    | `/api/clients/{id}`           | Updates an existing client.                |
| `DELETE` | `/api/clients/{id}`           | Deletes a client.                          |

<div align="right">
<a href="#-table-of-contents">⬆️ Back to Top</a>
</div>
---

## 🧪 Testing

The project includes tests for both the backend and frontend to ensure code quality and reliability.

### Backend Tests

The backend includes both **Unit tests** for the controller layer (`@WebMvcTest`) and a full **Integration test** (`@SpringBootTest`) that validates the complete CRUD lifecycle against a test database.

```bash
# From the /backend directory
mvn test
```

### Frontend Tests
The frontend uses Vitest and React Testing Library to test critical components. The tests cover rendering the client list from a mocked API and verifying the "no results" state.

```bash
# From the /frontend directory
npm test
```

<div align="right">
<a href="#-table-of-contents">⬆️ Back to Top</a>
</div>
---

## 🛠️ Development Tooling & Quality Assurance

This project is configured with modern tools to ensure code quality, consistency, and a productive development experience. All dependencies are included in the respective `package.json` and `pom.xml` files.

* **ESLint:** Used for static code analysis to find and fix problems in the JavaScript/React code. The configuration is based on the modern "flat config" (`eslint.config.js`).
* **Prettier:** Integrated for automated code formatting, ensuring a consistent style across the entire codebase.
* **Vitest & React Testing Library:** A modern testing framework is set up for the frontend, allowing for fast and reliable component testing in a simulated DOM environment (`jsdom`).

---

## 🏗️ Architectural Decisions

### Frontend Architecture

* **Scalable CSS with CSS Modules:** Prevents style conflicts and ensures components are self-contained.
* **Reusable Global Styles:** A base design system is established in `index.css`, including a centralized color palette with CSS variables and global utility classes for common elements like buttons (`.btn`, `.btn--primary`), promoting a DRY (Don't Repeat Yourself) codebase.
* **Centralized API Logic:** All `fetch` calls to the backend are managed in a dedicated service file (`src/api/clientService.js`), separating API communication logic from the UI components for better maintainability.

### Backend Architecture

* **Robust Entity Relationships (`@ManyToMany`):** For the `tags` feature, a `@ManyToMany` relationship between the `Client` and `Tag` entities was implemented. This architecture ensures data integrity, prevents tag duplication, and provides a scalable foundation for advanced filtering.
* **Service Layer Intelligence:** The logic for managing `tags` (finding existing ones or creating new ones) is centralized in the service layer (`ClientService`), keeping the controllers slim and focused on handling HTTP requests.
* **Data Seeding on Startup:** A `DataLoader` component uses `CommandLineRunner` to populate the database with realistic sample data on application startup, facilitating easy testing and development.

<div align="right">
<a href="#-table-of-contents">⬆️ Back to Top</a>
</div>
---

## 🏆 Challenges & Learnings

This project was a deep dive into full-stack development and presented several real-world challenges that were crucial for my learning.

* **Performance Optimization (N+1 Query Problem):**
    * **Challenge:** The initial implementation of the client list was extremely inefficient. It performed one query to fetch the clients and then N additional queries to fetch the tags for each client.
    * **Solution:** I refactored the Spring Data JPA repository to use a custom `@Query` with `LEFT JOIN FETCH`. This allowed Hibernate to retrieve clients and their associated tags in a single, efficient database call, dramatically improving performance.

* **Debugging the "Silent" 500 Error:**
    * **Challenge:** The most difficult challenge was a persistent 500 Internal Server Error during client creation that produced **no error log** in the backend console. This led to a long "blind" debugging session where I explored issues in the database, security, and entity relationships.
    * **Solution:** The root cause was twofold: a simple validation rule (`@Size(min=5)` on an optional field) and a `GlobalExceptionHandler` that was catching the exception but failing to log it. The key takeaway was immense: **an unlogged error is a developer's worst enemy**. I learned the critical importance of robust logging in every exception handler, which was the final key to diagnosing and fixing the bug.

<div align="right">
<a href="#-table-of-contents">⬆️ Back to Top</a>
</div>
---

## 🗂️ Project Structure

<details>
<summary>Click to view the full project structure</summary>

```bash
.
|   PreguntaEntrevista.md
|   README.md
|
+---backend
|   |   .env.example
|   |   mvnw
|   |   mvnw.cmd
|   |   pom.xml
|   |
|   +---assets
|   |       class_diagram_Galerium.png
|   |
|   \---src
|       +---main
|       |   +---java
|       |   |   \---galerium
|       |   |       |   GaleriumApplication.java
|       |   |       |
|       |   |       +---config
|       |   |       |       GlobalExceptionHandler.java
|       |   |       |       SecurityConfig.java
|       |   |       |       WebConfig.java
|       |   |       |
|       |   |       +---controller
|       |   |       |       ClientController.java
|       |   |       |       HealthController.java
|       |   |       |       TagController.java
|       |   |       |       (and other controllers...)
|       |   |       |
|       |   |       +---dto
|       |   |       |   +---client
|       |   |       |   |       ClientRequestDTO.java
|       |   |       |   |       ClientResponseDTO.java
|       |   |       |   |       ClientUpdateDTO.java
|       |   |       |   |
|       |   |       |   \---(other DTOs...)
|       |   |       |
|       |   |       +---enums
|       |   |       |       UserRole.java
|       |   |       |
|       |   |       +---model
|       |   |       |       Client.java
|       |   |       |       Tag.java
|       |   |       |       User.java
|       |   |       |       (and other models...)
|       |   |       |
|       |   |       +---repository
|       |   |       |       ClientRepository.java
|       |   |       |       TagRepository.java
|       |   |       |       UserRepository.java
|       |   |       |       (and other repositories...)
|       |   |       |
|       |   |       +---service
|       |   |       |   +---impl
|       |   |       |   |       ClientServiceImpl.java
|       |   |       |   |       TagServiceImpl.java
|       |   |       |   |       (and other services...)
|       |   |       |   |
|       |   |       |   \---interfaces
|       |   |       |           ClientService.java
|       |   |       |           TagService.java
|       |   |       |           (and other interfaces...)
|       |   |       |
|       |   |       \---util
|       |   |               DataLoader.java
|       |   |               OpenApiConfig.java
|       |   |
|       |   \---resources
|       |           application.properties
|       |
|       \---test
|           \---java
|               \---galerium
|                       (test files...)
|
+---docs
|   \---images
|           filter-demo.gif
|
\---frontend
    |   .env
    |   .env.example
    |   eslint.config.js
    |   index.html
    |   package.json
    |   vite.config.js
    |
    +---public
    |       galerium-favicon.png
    |       galerium-logo-text.png
    |
    +---src
    |   |   App.jsx
    |   |   index.css
    |   |   main.jsx
    |   |   setupTests.js
    |   |
    |   +---api
    |   |       clientService.js
    |   |
    |   +---components
    |   |       ClientCRUD.jsx
    |   |       ClientForm.jsx
    |   |       DeleteModal.jsx
    |   |       Sidebar.jsx
    |   |
    |   +---layouts
    |   |       SidebarLayout.jsx
    |   |
    |   +---pages
    |   |       ClientDetailPage.jsx
    |   |       ClientFormPage.jsx
    |   |       ClientsPage.jsx
    |   |       Dashboard.jsx
    |   |       LandingPage.jsx
    |   |
    |   +---routes
    |   |       AppRouter.jsx
    |   |
    |   \---styles
    |           ClientCRUD.module.css
    |           (and other style files...)
    |
    \---tests
            ClientsPage.test.jsx
```

</details>

<div align="right">
<a href="#-table-of-contents">⬆️ Back to Top</a>
</div>

---

## 📈 Project Management & Presentation

Project planning and progress were tracked using the Atlassian suite.

* **Jira Board:** [https://aidagarcia.atlassian.net/jira/software/projects/GAL/summary](https://aidagarcia.atlassian.net/jira/software/projects/GAL/summary)
* **Confluence Documentation (WIP):** [https://aidagarcia.atlassian.net/wiki/spaces/GALERIUM/overview](https://aidagarcia.atlassian.net/wiki/spaces/GALERIUM/overview)
* **Final Presentation:** [View on Canva](https://www.canva.com/design/DAG08qM5OZo/Vtqlf_txpu14m3Sr0hVkvA/view)

<div align="right">
<a href="#-table-of-contents">⬆️ Back to Top</a>
</div>
---

## 👤 Author

**Aïda García**: Junior Web Developer, among other things. Check my gitHub profile 😊

