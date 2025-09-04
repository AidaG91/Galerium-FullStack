# FocusPoint 📸  
![Java](https://img.shields.io/badge/Java-17-blue?logo=java)  
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green?logo=spring)  
![MySQL](https://img.shields.io/badge/MySQL-Database-orange?logo=mysql)  
![License](https://img.shields.io/badge/License-MIT-lightgrey)

> **Streamlining the creative workflow for photographers, one gallery at a time.**

---

## 1. Project Description  
FocusPoint is a backend application designed for professional photographers to manage their photoshoots, clients, and image galleries.  
Clients can access their galleries, mark favorite photos, and—if granted permission—download or share images.


## 2. UML Class Diagram  
*(Insert diagram here — image or textual description)*


## 3. Setup  

```bash
# 1. Clone the repository
git clone https://github.com/your-username/focuspoint.git

# 2. Configure MySQL credentials
# Edit src/main/resources/application.properties

# 3. Run the application
./mvnw spring-boot:run

# 4. Test endpoints using Postman
```
## 4. Technologies Used  
- Java 17  
- Spring Boot  
- Spring Security + JWT  
- MySQL  
- JPA/Hibernate  
- JUnit + Mockito  
- Trello  
- GitHub  

## 5. Controller & Route Structure  

| Resource     | Method | Route                          | Description                     |
|--------------|--------|--------------------------------|---------------------------------|
| Photographer | POST   | `/api/photographers`           | Create a photographer           |
| Client       | POST   | `/api/clients`                 | Create a client                 |
| Session      | POST   | `/api/sessions`                | Create a photoshoot session     |
| Photo        | POST   | `/api/photos`                  | Upload a photo                  |
| Photo        | PATCH  | `/api/photos/{id}/favorite`    | Mark photo as favorite          |
| Photo        | DELETE | `/api/photos/{id}`             | Delete a photo                  |

## 6. Extra Links  
- [Trello Board](URL)  
- [Presentation Slides](URL)  

## 7. Future Improvements  
- Real image upload (Cloudinary, S3…)  
- Share via link  
- Permission-based downloads  
- Photographer notifications  

## 8. Resources  
- Official Spring Boot documentation  
- JWT guide with Spring Security  
- UML Class Diagram Generator  

## 9. About the author
- Aïda (Backend Developer & Project Owner)

