# ExamInsight – Online Examination Management System

## Project Overview
ExamInsight is a Java-based Online Examination Management System developed using Java, JDBC and MySQL.

The system provides separate login facilities for students and faculty. Students can attend examinations, view their results and check their performance. Faculty members can manage questions and monitor student examination results.

## Features

### Student Module
- Student Login
- View Available Examinations
- Attend Multiple Choice Questions (MCQ)
- Automatic Answer Evaluation
- View Examination Results
- View Performance Analysis
- Logout

### Faculty Module
- Faculty Login
- View All Examinations
- Add New Questions
- View Student Results
- View Student Performance
- Logout

## Technologies Used
- Programming Language: Java
- Database: MySQL
- Database Connectivity: JDBC
- IDE: Visual Studio Code / Java-supported IDE
- Database Tool: MySQL Workbench

## Database Details

Database Name: `ExamInsightDB`

The project uses the following seven tables:

1. Student
2. Faculty
3. Examination
4. Question
5. Registration
6. Student_Response
7. Result

## Project Folder Structure

```text
ExamInsight_GitHub/
├── 01_Documentation/
├── 02_Design_Diagrams/
├── 03_Testing/
├── 04_Java_Source_Code/
│   ├── Main.java
│   └── DBConnection.java
├── 05_Database/
│   └── Dump20261116.sql
├── 06_Python_Analysis/
└── README.md
```

## Requirements
- JDK installed
- MySQL Server
- MySQL Connector/J
- MySQL Workbench (recommended)

## Installation and Execution

### Step 1: Create the Database
1. Open MySQL Workbench.
2. Import and execute the SQL file from `05_Database`.
3. Ensure the database is named `ExamInsightDB`.

### Step 2: Configure Database Connection
Open `DBConnection.java`.

Update the database username and password according to your local MySQL configuration.

Do not upload real database passwords or credentials to a public GitHub repository.

### Step 3: Compile the Java Files

Open Command Prompt in the project directory and execute:

```cmd
javac -cp "Java\lib\mysql-connector-j-26.10.0.jar" -d Java Java\src\DBConnection.java Java\src\Main.java
```

### Step 4: Run the Application

```cmd
java -cp "Java;Java\lib\mysql-connector-j-26.10.0.jar" src.Main
```

## Examination Process
1. Student logs into the system.
2. Student views available examinations.
3. Student selects a registered examination.
4. Student answers the MCQ questions.
5. System evaluates the answers automatically.
6. Examination results and performance are stored in MySQL.
7. Faculty can view student results and performance.

## Expected Outcome
ExamInsight simplifies examination management by providing automated evaluation, result generation and student performance monitoring through a Java and MySQL-based application.