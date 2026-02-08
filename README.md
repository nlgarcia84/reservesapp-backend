# ReservesApp Backend

## Descripció

Aquest repositori conté el **backend** de **ReservesApp**, una aplicació per a gestionar reserves de sales. El backend està desenvolupat amb **Spring Boot** i ofereix una API REST que permet al frontend consultar disponibilitat, crear, editar i cancel·lar reserves de manera eficient.

---

## Funcionalitats principals

- Gestionar sales i horaris disponibles.  
- Crear noves reserves i consultar-les.  
- Editar i cancel·lar reserves existents.  
- Exposició d’**endpoints REST** per a ser consumits pel frontend.  
- Integració amb base de dades (H2, PostgreSQL o MySQL).  
- Configuració per a entorn de desenvolupament i producció.

---

## Estructura del projecte

reservesapp-backend/
├─ src/
│ ├─ main/
│ │ ├─ java/com/reservesapp/
│ │ │ ├─ controller/ # Endpoints REST
│ │ │ ├─ service/ # Lògica de negoci
│ │ │ ├─ repository/ # Accés a dades
│ │ │ └─ ReservesAppBackendApplication.java
│ │ └─ resources/
│ │ ├─ application.properties # Configuració del projecte
│ │ └─ data.sql # Dades inicials opcional
│ └─ test/
├─ pom.xml
└─ .gitignore


---

## Tecnologies

- **Spring Boot**: Framework principal per al backend.  
- **Java**: Llenguatge principal.  
- **Maven**: Gestió de dependències i compilació.  
- **JPA / Hibernate**: Accés a la base de dades.  
- **GitHub**: Control de versions i col·laboració.  

---

## Instal·lació i execució

1. Clonar el repositori:

```bash
git clone https://github.com/nlgarcia84/reservesapp-backend.git


Entrar a la carpeta del projecte:

cd reservesapp-backend
Compilar i executar l’aplicació:

./mvnw spring-boot:run
L’aplicació s’executarà per defecte a: http://localhost:8080

Contribució
Crea una branca nova per a cada funcionalitat:

git checkout -b feature/nova-funcionalitat
Fes commit dels teus canvis i puja la branca:

git add .
git commit -m "Afegida nova funcionalitat"
git push origin feature/nova-funcionalitat
Obre un Pull Request per revisar i fusionar amb main.



