# todo-app

---

## ✅ **README.md — To-Do App (Maven Project)**

```md
# 📝 To-Do Application (Maven)

A simple and efficient **To-Do Application** built using **Java** and **Maven**.  
This project demonstrates clean project structure, modular code, and the use of Maven for dependency management and build automation.

---

## 🚀 Features

- ➕ Add new tasks  
- 📋 View all tasks  
- ✔️ Mark tasks as completed  
- 🗑️ Delete tasks  
- 💾 File-based or in-memory storage (based on implementation)  
- 📦 Maven-powered build and dependency management  

---

## 🧱 Project Structure

```

todo-app/

│── src/

│   ├── main/

│   │   ├── java/

│   │   │   └── com.todo/

│   │   │        ├── App.java

│   │   │        ├── Task.java

│   │   │        └── TaskManager.java

│   │   └── resources/

│   └── test/

│       └── java/

│── pom.xml

│── README.md


````

---

## 📦 Maven Dependencies

This project uses only core Java.  
If you add extra libraries (like Gson, Lombok, JUnit), include them in `pom.xml`.

Example:

```xml
<dependencies>
    <!-- For testing -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
````

---

## ▶️ How to Run

### 1️⃣ Clone the repository

```bash
git clone https://github.com/your-username/todo-app.git
cd todo-app
```

### 2️⃣ Build the project

```bash
mvn clean install
```

### 3️⃣ Run the application

```bash
mvn exec:java -Dexec.mainClass="com.todo.App"
```

---

## 🖥️ Usage

Once the application starts, you will see an interactive menu such as:

```
1. Add Task
2. View Tasks
3. Mark Task as Completed
4. Delete Task
5. Exit
```

Enter the option number to perform actions.

---

## Example Code Snippet (Task Model)

```java
public class Task {
    private String name;
    private boolean isCompleted;

    public Task(String name) {
        this.name = name;
        this.isCompleted = false;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    public String toString() {
        return (isCompleted ? "[✓] " : "[ ] ") + name;
    }
}
```

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 🛠️ Built Using

* **Java 8+**
* **Maven**
* **JUnit**

---


```
