# Course Manager Application

A complete Spring Boot MVC web application using JSP to manage Students and Courses with a Many-to-Many relationship.

---

## 🧩 Tech Stack

*   **Java 17+**
*   **Spring Boot 3.2.5**
*   **Spring MVC**
*   **Spring Data JPA (Hibernate)**
*   **MySQL** (configured with `root` user and `1234` password, creates DB if not exists)
*   **JSP + JSTL** for views
*   **Maven**
*   **Lombok** for boilerplate code reduction
*   **JUnit 5** and **Mockito** for testing

---

## 📊 Entities & Relationships

### 1. Student

*   `id` (Primary Key, auto-generated)
*   `name`
*   `email` (Unique)

### 2. Course

*   `id` (Primary Key, auto-generated)
*   `title`
*   `description`

### Relationship: Many-to-Many

*   A student can enroll in multiple courses, and a course can have multiple students.
*   Implemented using `@ManyToMany` with a `student_course` join table.

---

## 🗃️ Database Setup

*   Uses JPA annotations to auto-create tables (`ddl-auto=update`).
*   Configured to connect to **MySQL** database named `coursemanagerdb`.
*   The application will attempt to create the `coursemanagerdb` if it does not exist (requires appropriate MySQL user permissions).
*   Populates the database with **10 sample records in each table** (Students and Courses) on startup using `CommandLineRunner`, along with initial enrollments.

---

## 🔧 Features Implemented

### 1. CREATE Operation

*   JSP forms (`add-student.jsp`, `add-course.jsp`) to add new students and courses.
*   Controller methods handle form submissions and save entities via the service layer.

### 2. READ Operation

*   JSP pages (`list-students.jsp`, `list-courses.jsp`) to display all students and courses.
*   Controller fetches data via the service layer.
*   Uses JSTL/EL to render data, including enrolled courses for students and enrolled students for courses.
*   Includes a custom repository query (`findAllStudentsWithCourses`) to fetch students with their enrolled courses efficiently.

### 3. UPDATE Operation

*   JSP forms (`update-student.jsp`, `update-course.jsp`) to update student and course details.
*   Forms are pre-filled with existing data.
*   Controller updates entities via the service layer.

### 4. DELETE Operation

*   Links on list pages to delete students and courses.
*   Confirmation prompt before deletion.
*   Service layer handles removal of relationships when deleting a course.

---

## 🏗️ Architecture

*   **Entity Layer**: JPA entities (`Student`, `Course`) with annotations.
*   **Repository Layer**: Interfaces extending `JpaRepository` (`StudentRepository`, `CourseRepository`) for data access.
*   **Service Layer**: Business logic (`StudentService`, `CourseService`) for CRUD operations and relationship management.
*   **Controller Layer**: Spring MVC controllers (`StudentController`, `CourseController`) handling web requests and view rendering.
*   **View Layer**: JSP pages (`.jsp`) for the user interface.

---

## 📁 Project Structure

```
coursemanager/
├── src/
│   ├── main/
│   │   ├── java/com/adam/coursemanager/
│   │   │   ├── controller/
│   │   │   │   ├── CourseController.java
│   │   │   │   └── StudentController.java
│   │   │   ├── entity/
│   │   │   │   ├── Course.java
│   │   │   │   └── Student.java
│   │   │   ├── repository/
│   │   │   │   ├── CourseRepository.java
│   │   │   │   └── StudentRepository.java
│   │   │   ├── service/
│   │   │   │   ├── CourseService.java
│   │   │   │   └── StudentService.java
│   │   │   └── CoursemanagerApplication.java
│   │   └── resources/
│   │       ├── META-INF/resources/WEB-INF/jsp/  <-- JSP files are here
│   │       │   ├── add-course.jsp
│   │       │   ├── add-student.jsp
│   │       │   ├── list-courses.jsp
│   │       │   ├── list-students.jsp
│   │       │   ├── update-course.jsp
│   │       │   └── update-student.jsp
│   │       └── application.properties
│   └── test/
│       └── java/com/adam/coursemanager/
│           ├── repository/
│           │   ├── CourseRepositoryTest.java
│           │   └── StudentRepositoryTest.java
│           └── service/
│               ├── CourseServiceTest.java
│               └── StudentServiceTest.java
├── pom.xml
└── README.md
```

---

## 🚀 How to Run the Application

### Prerequisites

1.  **Java Development Kit (JDK) 17 or higher** installed.
2.  **Apache Maven** installed.
3.  **MySQL Server** installed and running.
4.  **MySQL User**: A MySQL user with username `root` and password `1234`. This user must have privileges to create databases.

### Steps

1.  **Clone the repository** (if applicable) or navigate to the project root directory.
    ```bash
    cd D:/Spring Boot/coursemanager
    ```

2.  **Build the project** using Maven. This will download dependencies and compile the code.
    ```bash
    mvn clean install
    ```

3.  **Run the Spring Boot application.**
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the `CoursemanagerApplication.java` file directly from your IDE.

4.  **Access the application in your web browser.**
    Upon successful startup, the console will print the access links:
    *   **Student List:** `http://localhost:8080/students`
    *   **Course List:** `http://localhost:8080/courses`

    The application will automatically create the `coursemanagerdb` database if it doesn't exist and populate it with sample data.

---

## 🧪 How to Test the Application

Unit tests are provided for the repository and service layers using JUnit 5 and Mockito.

### Steps

1.  **Navigate to the project root directory** in your terminal or command prompt.
    ```bash
    cd D:/Spring Boot/coursemanager
    ```

2.  **Execute the Maven test command.**
    ```bash
    mvn test
    ```
    This command will run all tests located in `src/test/java/`. You will see a summary of the test results in your console.

---
