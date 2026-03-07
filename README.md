# 🏨 Airbnb Clone - Backend Project

A Spring Boot-based backend application for an Airbnb-like platform that manages hotels, rooms, bookings, guests, and payments.

---

## 📋 **Project Overview**

This is a **Java Spring Boot** application that provides backend services for a hotel booking platform. The project uses:
- **Spring Boot 4.0.1** - Web framework
- **Spring Data JPA** - ORM for database operations
- **H2 Database** - In-memory database for development
- **Lombok** - Reduces boilerplate code
- **Jakarta Persistence** - JPA implementation
- **Maven** - Build tool
- **Java 21** - Programming language

---

## 🏗️ **Project Structure**

```
airbnb/
├── src/
│   ├── main/
│   │   ├── java/com/example/airbnb/
│   │   │   ├── AirbnbApplication.java          # Main Spring Boot application
│   │   │   ├── controller/                     # REST API controllers
│   │   │   ├── dto/                            # Data Transfer Objects
│   │   │   ├── entity/                         # JPA Entity classes
│   │   │   │   ├── user.java                   # User entity (base user class)
│   │   │   │   ├── Guest.java                  # Guest entity (extends user)
│   │   │   │   ├── Hotel.java                  # Hotel entity
│   │   │   │   ├── Room.java                   # Room entity
│   │   │   │   ├── booking.java                # Booking entity
│   │   │   │   ├── Payment.java                # Payment entity
│   │   │   │   ├── Inventory.java              # Inventory management
│   │   │   │   ├── hotelcontactinfo.java       # Hotel contact information
│   │   │   │   └── enumsrole/                  # Enum types
│   │   │   │       ├── roles.java              # User roles (GUEST, HOTEL_MANAGER)
│   │   │   │       ├── bookingStatus.java      # Booking status enum
│   │   │   │       ├── Gender.java             # Gender enum
│   │   │   │       └── Paymentstatus.java      # Payment status enum
│   │   │   ├── repository/                     # Spring Data JPA repositories
│   │   │   ├── service/                        # Business logic layer
│   │   │   └── util/                           # Utility classes
│   │   └── resources/
│   │       ├── application.properties          # Application configuration
│   │       ├── static/                         # Static files (CSS, JS, images)
│   │       └── templates/                      # HTML templates
│   └── test/
│       └── java/com/example/airbnb/
│           └── AirbnbApplicationTests.java     # Unit tests
├── pom.xml                                      # Maven configuration
├── mvnw / mvnw.cmd                             # Maven wrapper scripts
└── README.md                                    # This file
```

---

## 📊 **Database Schema (Current Progress)**

### **Entities Implemented:**

#### 1. **User** (`user.java`)
- Base user class with authentication details
- Stores username, password, and name
- Has role management (GUEST, HOTEL_MANAGER)
- **Table**: `app_user`
- **Fields**:
  - `id` (Long) - Primary key
  - `username` (String) - Unique username
  - `password` (String) - Encrypted password
  - `name` (String) - User's full name
  - `roles` (Set<Role>) - User roles (EAGER fetch)

#### 2. **Guest** (`Guest.java`)
- Extends user functionality for guest-specific data
- Has relationship with User (Many-to-One)
- Gender information
- **Relationships**:
  - `@ManyToOne` with User (foreign key: `user_id`)
  - `@ManyToMany` with Booking (mappedBy: "guests")
- **Fields**:
  - `id` (Long) - Primary key
  - `user` (user) - Reference to User
  - `gender` (Gender) - Guest's gender
  - `bookings` (Set<booking>) - Guest's bookings

#### 3. **Hotel** (`Hotel.java`)
- Represents a hotel/property listing
- Stores hotel details and metadata

#### 4. **Room** (`Room.java`)
- Represents individual rooms in a hotel
- Contains room details and pricing

#### 5. **Booking** (`booking.java`)
- Manages reservations/bookings
- Tracks booking status and dates
- Has Many-to-Many relationship with Guest

#### 6. **Payment** (`Payment.java`)
- Handles payment transactions
- Tracks payment status and amount

#### 7. **Inventory** (`Inventory.java`)
- Manages room availability and stock

#### 8. **HotelContactInfo** (`hotelcontactinfo.java`)
- Stores hotel contact details

---

## 📌 **Enums Implemented**

1. **Roles** (`roles.java`)
   - `GUEST` - Regular guest user
   - `HOTEL_MANAGER` - Hotel property manager

2. **BookingStatus** (`bookingStatus.java`)
   - `PENDING` - Booking pending confirmation
   - `CONFIRMED` - Booking confirmed
   - `CANCELLED` - Booking cancelled
   - `COMPLETED` - Booking completed

3. **Gender** (`Gender.java`)
   - `MALE`
   - `FEMALE`
   - `OTHER`

4. **PaymentStatus** (`Paymentstatus.java`)
   - `PENDING` - Payment awaiting
   - `COMPLETED` - Payment successful
   - `FAILED` - Payment failed
   - `REFUNDED` - Payment refunded

---

## 🔗 **Entity Relationships**

```
User (1) ──────── Many (Guest)
  │
  └── roles: Set<Role> (ElementCollection)

Guest (Many) ──────── Many (Booking)
       ▲                    │
       │                    │
       └─── mappedBy: "guests"

Hotel (1) ──────── Many (Room)

Room (1) ──────── Many (Booking)

Booking ──────── Payment

Booking ──────── Inventory
```

---

## 🚀 **Getting Started**

### **Prerequisites**
- Java 21 or higher
- Maven 3.8+
- Git
- IDE (IntelliJ IDEA, VS Code, or Eclipse)

### **Installation**

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/airbnb.git
   cd airbnb
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   OR
   ```bash
   ./mvnw spring-boot:run  # On Linux/Mac
   mvnw.cmd spring-boot:run  # On Windows
   ```

4. **Access H2 Console**
   - Navigate to: `http://localhost:8080/h2-console`
   - Default JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

---

## 📦 **Dependencies**

```xml
<!-- Spring Boot Starters -->
- spring-boot-starter-data-jpa
- spring-boot-starter-webmvc
- spring-boot-h2console

<!-- Database -->
- h2 (in-memory database)

<!-- Lombok -->
- lombok

<!-- Testing -->
- spring-boot-starter-test
```

---

## ✅ **Current Progress**

### **Completed ✔️**
- [x] Project setup with Spring Boot 4.0.1
- [x] Entity classes created with JPA annotations
- [x] Database relationships configured
- [x] Enum types for roles, booking status, payment status, and gender
- [x] Lombok annotations for getters/setters
- [x] H2 database integration
- [x] Maven build configuration
- [x] Git repository initialized and pushed to GitHub

### **In Progress 🔄**
- [ ] REST API Controllers
- [ ] Service layer (business logic)
- [ ] Repository interfaces
- [ ] DTOs for API requests/responses

### **Planned 📋**
- [ ] Authentication & Authorization (JWT/Spring Security)
- [ ] API documentation (Swagger/OpenAPI)
- [ ] Unit and Integration tests
- [ ] Database migrations (Flyway/Liquibase)
- [ ] Error handling and exception management
- [ ] Validation rules
- [ ] API endpoints testing
- [ ] Frontend integration

---

## 🐛 **Known Issues & Fixes**

### **Issue: "The import is never used" warning**
**Status**: ✅ Resolved

**Problem**: Import warning in `Guest.java` for the `user` entity import.
- **Root Cause**: Class naming convention - class `user` should be `User` (PascalCase)
- **File naming mismatch**: File `user.java` contains class `user`
- **Solution**: 
  1. Rename class from `user` to `User`
  2. Update imports to `com.example.airbnb.entity.User`
  3. Update field declaration from `private user user;` to `private User user;`

---

## 📝 **Code Standards**

- **Class Naming**: PascalCase (e.g., `Guest`, `Hotel`, `User`)
- **File Naming**: Match class name exactly
- **Field Naming**: camelCase (e.g., `bookingStatus`, `hotelName`)
- **Constants**: UPPER_SNAKE_CASE
- **Annotations**: Use Jakarta Persistence (jakarta.persistence.*) not javax

---

## 🔑 **Key Features to Implement**

1. **User Management**
   - User registration and login
   - Role-based access control

2. **Hotel Management**
   - Add/Edit/Delete hotels
   - Manage hotel information and contact details

3. **Room Management**
   - Create rooms under hotels
   - Manage room pricing and availability

4. **Booking System**
   - Create bookings
   - Check room availability
   - View booking history

5. **Payment Processing**
   - Process payments
   - Track payment status
   - Handle refunds

---

## 📚 **Useful Concepts Reference**

### **@ManyToMany with mappedBy**
- Used in bidirectional relationships
- `mappedBy` indicates the owning side is managed by another entity
- Prevents duplicate join tables
- Example: Guest bookings are managed by the Booking entity

### **@ElementCollection**
- Used to store collections of basic/embeddable types
- Creates a separate table (e.g., `app_user_roles`)
- `@Enumerated(EnumType.ORDINAL)` stores enum position in DB

### **@JoinColumn**
- Specifies foreign key column name
- Used in `@ManyToOne` and `@OneToMany` relationships
- Example: `@JoinColumn(name = "user_id")`

---

## 📞 **Contact & Support**

- **Developer**: KIIT
- **Repository**: [GitHub - Airbnb Clone](https://github.com/YOUR_USERNAME/airbnb)
- **Created**: December 2025

---

## 📄 **License**

This project is open source and available under the MIT License.

---

## 🎯 **Next Steps**

1. Implement Repository interfaces for database operations
2. Create DTOs for API requests/responses
3. Build REST API Controllers with CRUD operations
4. Implement Service layer with business logic
5. Add validation and error handling
6. Implement authentication and authorization
7. Write comprehensive unit and integration tests

---

**Happy Coding! 🚀**
