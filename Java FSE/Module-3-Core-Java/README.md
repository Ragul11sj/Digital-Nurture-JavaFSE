# Community Event Management System

## Description

Community Event Management System (CivicPulse) is a full-featured console-based Java application that models the complete lifecycle of community events — from user and event management through registrations, cancellations, file exports, and live statistics. Every source file is designed to demonstrate a distinct set of Core Java concepts as required by Cognizant Digital Nurture 5.0 Module-3.

---

## Project Structure

```
Community-Event-Management-System/
├── src/main/java/com/ragulsj/eventmanagement/
│   ├── Main.java
│   ├── model/
│   │   ├── BaseEntity.java          (abstract class, inheritance)
│   │   ├── Manageable.java          (interface, abstraction)
│   │   ├── User.java                (Serializable, Comparable)
│   │   ├── Event.java               (Serializable, enums, Comparable)
│   │   ├── Registration.java        (Serializable, enums)
│   │   └── EventRecord.java         (Java 17 Record)
│   ├── exception/
│   │   ├── EventManagementException.java
│   │   ├── UserNotFoundException.java
│   │   ├── EventNotFoundException.java
│   │   ├── RegistrationException.java
│   │   └── ValidationException.java
│   ├── collection/
│   │   └── CollectionManager.java   (ArrayList, LinkedList, HashSet, HashMap, Queue, Stack, TreeMap)
│   ├── service/
│   │   ├── UserService.java         (CRUD, overloading, recursion)
│   │   ├── EventService.java        (CRUD, arrays, type casting)
│   │   ├── RegistrationService.java (register, cancel, validation)
│   │   ├── SeedDataService.java     (demo data loader)
│   │   └── MenuHandler.java         (interactive menu, all demos)
│   ├── functional/
│   │   └── FunctionalService.java   (lambdas, streams, functional interfaces, Optional)
│   ├── filehandling/
│   │   └── FileHandlingService.java (FileReader/Writer, BufferedReader/Writer, Serialization)
│   ├── multithreading/
│   │   ├── NotificationThread.java  (Thread class, switch expression)
│   │   └── ThreadPoolManager.java   (ExecutorService, Runnable, synchronization, singleton)
│   ├── jdbc/
│   │   ├── DatabaseConfig.java      (DB constants)
│   │   └── JdbcService.java         (JDBC CRUD, PreparedStatement, transactions)
│   └── util/
│       ├── ConsoleUtil.java          (formatted I/O, ANSI colors)
│       ├── Validator.java            (validation helpers)
│       ├── IdGenerator.java          (AtomicInteger counters)
│       ├── AppLogger.java            (file + console logging)
│       └── NetworkService.java       (HttpURLConnection, URI)
└── database/
    └── event_management.sql
```

---

## Features

- Add, view, search, update, and delete users
- Add, view, filter, update status, and delete events
- Register users for events with seat management
- Cancel registrations with automatic seat restoration
- Thread-based email notification simulation on register/cancel
- Real-time statistics dashboard with functional streams
- Export users and events to text files
- Serialize and deserialize data with Java I/O
- JDBC integration with MySQL (CRUD + transactions)
- Interactive console menu with input validation

---

## Java Concepts Covered

| Concept | File |
|---|---|
| Classes, Objects, Packages | All source files |
| Inheritance | `BaseEntity` → `User`, `Event`, `Registration` |
| Interface & Abstraction | `Manageable.java` |
| Encapsulation | All model classes (private fields + getters/setters) |
| Polymorphism | `Manageable` implemented by entities |
| Method Overloading | `UserService.addUser()` (2 signatures) |
| Recursion | `UserService.factorial()`, `UserService.fibonacci()` |
| Primitive & Reference Types | All service classes |
| Type Casting | `EventService.demonstrateTypeCasting()` |
| Arrays (1D and 2D) | `EventService.demonstrateArrays()` |
| String, StringBuilder, StringBuffer | `EventService.demonstrateArrays()` |
| Control Flow (if/else, switch, for, while, do-while) | `MenuHandler`, all services |
| try/catch/finally/throw/throws | All services and JDBC |
| Custom Exceptions | `exception/` package (5 classes) |
| ArrayList, LinkedList | `CollectionManager` |
| HashSet, HashMap, Queue, Stack, TreeMap | `CollectionManager` |
| Lambda Expressions | `FunctionalService` |
| Functional Interfaces | `EventFilter`, `UserProcessor`, `Predicate`, `Function`, `Consumer`, `Supplier`, `UnaryOperator`, `BinaryOperator` |
| Streams API | `FunctionalService` — filter, map, sorted, collect, reduce, groupingBy, joining |
| Optional | `CollectionManager.findUserById()`, `FunctionalService.getMostPopularEvent()` |
| FileReader/Writer, BufferedReader/Writer | `FileHandlingService` |
| Serialization / Deserialization | `FileHandlingService` |
| Thread class | `NotificationThread` |
| Runnable interface | `ThreadPoolManager.submitTask()` |
| Synchronization | `ThreadPoolManager.demonstrateSynchronization()` |
| ExecutorService, ScheduledExecutorService | `ThreadPoolManager` |
| JDBC MySQL Connection | `JdbcService.getConnection()` |
| PreparedStatement | All JDBC methods |
| Transactions (commit/rollback) | `JdbcService.registerUserWithTransaction()` |
| Java Records | `EventRecord.java` |
| Text Blocks | `MenuHandler.demoTextBlocks()` |
| Pattern Matching (switch) | `MenuHandler.demoPatternMatching()`, `NotificationThread` |
| AtomicInteger | `IdGenerator` |
| HttpURLConnection / URI | `NetworkService` |
| ANSI Console Formatting | `ConsoleUtil` |
| Enums | `Event.Category`, `Event.Status`, `Registration.PaymentStatus`, `Registration.AttendanceStatus` |

---

## Technologies Used

| Technology | Version |
|---|---|
| Java | 17 or 21 (LTS recommended) |
| MySQL | 8.0+ |
| MySQL Connector/J | 8.0.x |
| Maven | 3.8+ (optional, for build) |

---

## Database Setup

1. Start MySQL server.
2. Open a MySQL client and run:

```sql
SOURCE /path/to/Community-Event-Management-System/database/event_management.sql;
```

Or paste the contents of `event_management.sql` directly into MySQL Workbench.

3. Update credentials in `DatabaseConfig.java` if needed:

```java
public static final String DB_URL      = "jdbc:mysql://localhost:3306/community_event_management?...";
public static final String DB_USER     = "root";
public static final String DB_PASSWORD = "password";
```

---

## How to Run

### Prerequisites

- JDK 17 or 21 installed
- `javac` and `java` on your PATH
- MySQL (optional — app runs in standalone mode without it)

### Option A: Compile and Run Manually

```bash
cd Community-Event-Management-System

javac -d out -sourcepath src/main/java \
  $(find src/main/java -name "*.java")

java -cp out com.ragulsj.eventmanagement.Main
```

### Option B: Using Maven

Create a `pom.xml` at the project root with `maven-compiler-plugin` set to Java 17+, add `mysql-connector-java` as a dependency, then:

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.ragulsj.eventmanagement.Main"
```

### Option C: IntelliJ IDEA / Eclipse

1. Open as a Java project (or Maven project if pom.xml is added).
2. Set `src/main/java` as the sources root.
3. Add `mysql-connector-java-8.x.x.jar` to the classpath.
4. Run `Main.java`.

---

## Notes

- The application runs fully in-memory without a database. JDBC features are available when MySQL is configured and the connector JAR is on the classpath.
- Log output is written to `event_management.log` in the working directory.
- Exported files are written to `data/` in the working directory.
- Notification threads are daemon threads and complete asynchronously.

---

## Author

**Name:** Ragul SJ
**Email:** sjragul555@gmail.com

---

## Copyright

&copy; 2026 Community Event Management System — Ragul SJ. All rights reserved.
