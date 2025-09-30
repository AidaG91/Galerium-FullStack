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


✨ Key Features (Características Principales)
En esta sección, puedes destacar las mejoras de la experiencia de usuario. Esto le dice a quien lo lee que no solo te preocupas de que funcione, sino de que funcione bien.

Ejemplo:

Interfaz Reactiva y Centrada en el Usuario: La aplicación proporciona feedback constante al usuario, mostrando estados de carga para evitar saltos visuales (flicker-free), mensajes de error claros cuando falla la comunicación con el servidor y notificaciones cuando no se encuentran resultados.

Sistema de Diseño Coherente: Se ha implementado una guía de estilos unificada con variables CSS, asegurando consistencia visual en todos los componentes (botones, modales, formularios, etc.).

Modales Interactivos: Los modales de confirmación (ej. para eliminar un cliente) incluyen mejoras de usabilidad como cierre al hacer clic fuera y animaciones suaves para una experiencia más fluida.

Previsualización de Imágenes en Formularios: Para mejorar la usabilidad, el formulario de cliente incluye una vista previa instantánea de la imagen al introducir una URL. El sistema es robusto, mostrando un aviso en caso de que el enlace esté roto o la imagen no esté disponible, evitando así iconos de imagen rota y mejorando la experiencia.

Frontend Architecture (Arquitectura Frontend)
Aquí puedes describir las decisiones técnicas que hemos tomado. Esto demuestra que piensas en la escalabilidad y el mantenimiento del código.

Ejemplo:

CSS Escalable con CSS Modules: Para evitar conflictos de estilos y asegurar que los componentes sean autocontenidos, el proyecto utiliza CSS Modules. Cada componente importa sus propios estilos, garantizando un encapsulamiento real.

Estilos Globales Reutilizables: Se ha establecido un sistema de diseño base en index.css que incluye:

Una paleta de colores centralizada mediante variables CSS (:root).

Clases de utilidad globales para elementos comunes como botones (.btn, .btn--primary, .btn--danger), promoviendo la reutilización de código (principio DRY).