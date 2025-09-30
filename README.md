# Galerium FullStack

Aplicación CRUD para fotógrafos, desarrollada como proyecto final del Módulo 3 en IronHack. Permite gestionar clientes, galerías y sesiones, con frontend en React y backend en Spring Boot.

---

## 🧪 Stack tecnológico

- **Frontend**: React + Vite + React Router
- **Backend**: Java Spring Boot + JPA  / MySQL (prod)
- **Base de datos**: H2 local (desarrollo), PostgreSQL (producción)
- **Gestión de tareas**: Jira + Confluence
- **Control de versiones**: Git + GitHub

---

## 🚀 Puesta en marcha local

### 🔧 Requisitos previos

- Java 17+
- Node.js 18+
- Maven
- Git

### 📦 Backend

```bash
cd backend
# Instalar dependencias y compilar
mvn clean install
# Ejecutar en local
mvn spring-boot:run
Accede a: http://localhost:8080/api/health
```

### 💻 Frontend

```bash
cd frontend
npm install
npm run dev
Accede a: http://localhost:5173
```

---

## 🔐 Variables de entorno

### Backend (`backend/src/main/resources/application.properties`)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

### Frontend (`frontend/.env`)

```properties
VITE_API_URL=http://localhost:8080/api
```

---

## 📚 Rutas principales

### Backend

| Método | Ruta                                 | Descripción                       |
| ------ | ------------------------------------ | --------------------------------- |
| GET    | `/api/health`                        | Estado de conexión                |
| GET    | `/api/items?search=&page=&pageSize=` | Listado con búsqueda y paginación |
| POST   | `/api/items`                         | Crear nuevo item                  |
| GET    | `/api/items/:id`                     | Ver detalle                       |
| PUT    | `/api/items/:id`                     | Actualizar item                   |
| DELETE | `/api/items/:id`                     | Eliminar item                     |

---

## 🧪 Pruebas

### Backend

- Test de integración CRUD (`src/test`)
- Alternativa: colección Postman con scripts

### Frontend

- Test de lista y estado “sin resultados” (opcional)

---

## 📸 Capturas

Se añadirán capturas o GIFs de las vistas principales (listado, formulario, búsqueda).

---

## 📦 Scripts útiles

```bash
# Backend
mvn spring-boot:run
mvn test

# Frontend
npm run dev
npm run test
```

---
### Funcionalidades del módulo Client

- CRUD completo: creación, edición, visualización y eliminación de clientes
- Formulario con validación visual y feedback de éxito
- Paginación dinámica con `GET /clients?page=&size=`
- Búsqueda con debounce por nombre, email y teléfono
- Listado real consumiendo API REST
- Vista de detalle con imagen, datos y acciones
- Eliminación con confirmación visual (modal personalizado)

### Experiencia de usuario

- Mensajes de error personalizados en formularios
- Mensaje de éxito tras guardar cliente
- Modal de confirmación reutilizable para eliminar
- Placeholder visual si el cliente no tiene imagen
- Navegación fluida entre listado, detalle y edición

### Componentes reutilizables

- `ClientForm`: formulario adaptable para creación y edición
- `DeleteModal`: modal de confirmación visual para acciones destructivas

### Endpoints utilizados

- `GET /api/clients?page=&size=&query=` → listado paginado y filtrado
- `GET /api/clients/:id` → detalle de cliente
- `POST /api/clients` → creación
- `PUT /api/clients/:id` → edición
- `DELETE /api/clients/:id` → eliminación
