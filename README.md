# 🎓 Student Management System

A console-based Student Management System built with **Java 21**, **Spring Boot**, and **PostgreSQL** — using raw JDBC for direct database interaction.

---

## 🚀 Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core programming language |
| Spring Boot 3.5.6 | Application framework |
| PostgreSQL | Relational database |
| JDBC | Direct database connection & queries |
| Lombok | Boilerplate reduction |
| Gradle | Build tool |

---

## 📌 Features

- ✅ **Add Student** — Insert new student with validation
- ✅ **View All Students** — List all students from database
- ✅ **Update Student** — Update student details by ID
- ✅ **Delete Student** — Delete student by ID
- ✅ **Search by Grade** — Filter students by grade (A-F)
- ✅ **Top-Performing Students** — List students with score ≥ 90
- ✅ **Average Score** — Calculate average score of all students
- ✅ **Bulk Insert** — Insert multiple students using JDBC batch processing
- ✅ **Input Validation** — Validates age, email format, grade (A-F), score (0-100)

---

## 🗄️ Data Model

### Student
| Field | Type | Description |
|---|---|---|
| `id` | Integer | Auto-generated primary key |
| `name` | String | Student full name |
| `age` | int | Student age (must be positive) |
| `email` | String | Valid email format |
| `grade` | String | Grade A, B, C, D, or F |
| `score` | int | Score between 0 and 100 |

---

## 📋 Menu Options

```
=========================
   Student Management System
=========================
1. Add Student
2. View All Students
3. Update Student
4. Delete Student
5. Search Students by Grade
6. Top-Performing Students
7. Average Score
8. Bulk Insert Students
9. Exit
```

---

## ⚙️ Configuration

```yaml
spring:
  application:
    name: spring-student
  datasource:
    url: jdbc:postgresql://localhost:5432/api_student001
    username: postgres
    password: 123
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

---

## 🛠️ Getting Started

### Prerequisites
- Java 21+
- PostgreSQL
- Gradle

### Setup Database

```sql
CREATE DATABASE api_student001;

CREATE TABLE Student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(100) NOT NULL,
    grade VARCHAR(1) NOT NULL,
    score INT NOT NULL
);
```

### Run the Application

```bash
# Clone the repository
git clone https://github.com/ekvitou/<repo-name>.git

# Navigate to project
cd <repo-name>

# Run with Gradle
./gradlew bootRun
```

---

## 📁 Project Structure

```
src/
├── config/
│   └── DatabaseConnectionConfig.java   # JDBC connection setup
├── model/
│   ├── Student.java                     # Student entity
│   ├── dto/
│   │   └── StudentUpdateDto.java        # Update DTO
│   └── repository/
│       └── StudentRepository.java       # JDBC queries (CRUD + search + bulk)
└── Main.java                            # Console menu & application entry point
```

---

## ✅ Validation Rules

| Field | Rule |
|---|---|
| Age | Must be positive (> 0) |
| Email | Must match valid email format |
| Grade | Must be A, B, C, D, or F only |
| Score | Must be between 0 and 100 |

---

## 👨‍💻 Author

**Ekvitou Kong**
- GitHub: [@ekvitou](https://github.com/ekvitou)
- Role: Java Spring Boot Backend Developer

---

## 📜 License

This project is built for portfolio and learning purposes.
