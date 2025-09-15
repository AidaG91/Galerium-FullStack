# Galerium 📸  
![Java](https://img.shields.io/badge/Java-17-blue?logo=java)  
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green?logo=spring)  
![MySQL](https://img.shields.io/badge/MySQL-Database-orange?logo=mysql)  
![License](https://img.shields.io/badge/License-MIT-lightgrey)

> **Streamlining the creative workflow for photographers, one gallery at a time.**

---

## 1. Project Description  
Galerium is a backend photo gallery management system built with Java and Spring Boot. It allows clients to browse 
galleries, photographers to manage their work and clients, and admins to oversee the platform. The app follows REST API 
best practices and includes robust error handling, validation, and testing.


## 2. UML Class Diagram  
![Class Diagram](./assets/class_diagram_Galerium.png)

> This diagram was generated using DBeaver's ER visualization tool and reflects the relationships between entities 
> such as User, Photographer, Gallery, Photo, and Client.


## 3. Setup  

```bash
# Clone the repository
git clone https://github.com/AidaG91/Galerium.git

# Navigate into the project
cd galerium

# Create a MySQL database named 'galerium'

# Update application.properties with your DB credentials

# Run the app
./mvnw spring-boot:run
```
## 4. Technologies Used  
- Java 17  
- Spring Boot  
- Spring Data JPA
- MySQL
- JUnit & Mockito
- Git & GitHub
- Postman
- Swagger (OpenAPI)
- Spring Security & JWT—To Be Implemented

## 5. Controller & Route Structure  

| Entity       | Method | Route                                 | Description                          |
|--------------|--------|----------------------------------------|--------------------------------------|
| Client       | GET    | `/api/clients`                         | Get all clients                      |
| Client       | POST   | `/api/clients`                         | Create a new client                  |
| Client       | PUT    | `/api/clients/{id}`                    | Update client info                   |
| Client       | DELETE | `/api/clients/{id}`                    | Delete client                        |
| Client       | GET    | `/api/clients/{id}`                    | Get client by ID                     |
| Client       | GET    | `/api/clients/email/{email}`          | Get client by email                  |
| Client       | GET    | `/api/clients/name/{name}`            | Get clients by name                  |
| Client       | GET    | `/api/clients/phone/{phoneNumber}`    | Get clients by phone number          |
| Client       | GET    | `/api/clients/gallery/{title}`        | Get clients by gallery title         |
| Photographer | GET    | `/api/photographers`                  | Get all photographers                |
| Photographer | POST   | `/api/photographers`                  | Create a new photographer            |
| Photographer | PUT    | `/api/photographers/{id}`             | Update photographer info             |
| Photographer | DELETE | `/api/photographers/{id}`             | Delete photographer                  |
| Gallery      | GET    | `/api/galleries`                      | List all galleries                   |
| Gallery      | POST   | `/api/galleries`                      | Create a new gallery                 |
| Gallery      | PUT    | `/api/galleries/{id}`                 | Update gallery info                  |
| Gallery      | DELETE | `/api/galleries/{id}`                 | Delete gallery                       |
| Photo        | GET    | `/api/photos`                         | List all photos                      |
| Photo        | POST   | `/api/photos`                         | Upload a photo                       |
| Photo        | PUT    | `/api/photos/{id}`                    | Update photo info                    |
| Photo        | DELETE | `/api/photos/{id}`                    | Delete photo                         |

## 6. Extra Links  
- [Kanban with GitHub](https://github.com/users/AidaG91/projects/3/)  
- [Presentation Slides](https://www.canva.com/design/DAGzCZWOk40/yHYaeIKBJNhqztTYHw08JQ/view?utm_content=DAGzCZWOk40&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h064741c1dd)  

## 7. Future Improvements  
- Implement full authentication and role-based access using Spring Security
- Integrate image upload with cloud storage (e.g. Cloudinary)
- Enable gallery sharing via public link
- Add permission-based photo downloads
- Notify photographers when galleries are viewed or updated
- Add pagination and filtering to gallery and photo endpoints
- Build a frontend to consume the API 

## 8. About the author
- Aïda (Backend Developer & Project Owner)

## 9. License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).  
You are free to use, modify, and distribute this code with proper attribution.


