
package src;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== EXAMINSIGHT =====");
        System.out.println("1. Student Login");
        System.out.println("2. Faculty Login");
        System.out.println("3. Student Registration");
        System.out.println("4. Faculty Registration");
        System.out.println("5. Exit");

        System.out.print("Enter your choice: ");
        int loginChoice = sc.nextInt();

        if (loginChoice == 2) {
         facultyLogin(sc);
         sc.close();
        return;
}
else if (loginChoice == 3) {
    studentRegistration(sc);
    sc.close();
    return;
}

    else if (loginChoice == 4) {
    facultyRegistration(sc);
    sc.close();
    return;

}
else if (loginChoice == 5) {
    System.out.println("Thank you for using ExamInsight!");
    sc.close();
    return;
}
else if (loginChoice != 1) {
    System.out.println("Invalid choice!");
    sc.close();
    return;
}

        // STUDENT LOGIN

        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return;

            String query = "SELECT * FROM Student WHERE student_id = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\nWelcome, " + rs.getString("name"));

                int choice;

                do {

                    System.out.println("\n===== STUDENT DASHBOARD =====");
                    System.out.println("1. View Available Exams");
                    System.out.println("2. View Exam Results");
                    System.out.println("3. View Performance");
                    System.out.println("4. Start Exam");
                    System.out.println("5. Logout");

                    System.out.print("Enter your choice: ");

                    if (sc.hasNextInt()) {
                        choice = sc.nextInt();
                    } else {
                        System.out.println("Invalid input! Enter a number.");
                        sc.next();
                        choice = 0;
                    }

                    switch (choice) {

                        case 1:

                            System.out.println("\n--- AVAILABLE EXAMS ---");

                            Statement st = con.createStatement();

                            ResultSet exams = st.executeQuery(
                                "SELECT exam_id, subject, max_marks, duration FROM Examination"
                            );

                            while (exams.next()) {

                                System.out.println(
                                    "Exam ID: " + exams.getInt("exam_id") +
                                    " | Subject: " + exams.getString("subject") +
                                    " | Marks: " + exams.getInt("max_marks") +
                                    " | Duration: " + exams.getInt("duration") + " minutes"
                                );
                            }

                            break;

                        case 2:

                            System.out.println("\n--- MY EXAM RESULTS ---");

                            String resultQuery =
                                "SELECT e.subject, r.total_marks, r.percentage, r.grade, r.result_status " +
                                "FROM Registration reg " +
                                "JOIN Examination e ON reg.exam_id = e.exam_id " +
                                "JOIN Result r ON reg.registration_id = r.registration_id " +
                                "WHERE reg.student_id = ?";

                            PreparedStatement resultPs =
                                con.prepareStatement(resultQuery);

                            resultPs.setInt(1, id);

                            ResultSet resultRs = resultPs.executeQuery();

                            boolean resultFound = false;

                            while (resultRs.next()) {

                                resultFound = true;

                                System.out.println(
                                    "Subject: " + resultRs.getString("subject") +
                                    " | Marks: " + resultRs.getDouble("total_marks") +
                                    " | Percentage: " + resultRs.getDouble("percentage") + "%" +
                                    " | Grade: " + resultRs.getString("grade") +
                                    " | Status: " + resultRs.getString("result_status")
                                );
                            }

                            if (!resultFound) {
                                System.out.println("No exam results available.");
                            }

                            break;

                        case 3:

                            System.out.println("\n--- PERFORMANCE ANALYSIS ---");

                            String performanceQuery =
                                "SELECT e.subject, r.percentage, r.grade, " +
                                "CASE " +
                                "WHEN r.percentage < 50 THEN 'Needs Improvement' " +
                                "WHEN r.percentage < 70 THEN 'Average' " +
                                "WHEN r.percentage < 90 THEN 'Good Performance' " +
                                "ELSE 'Excellent Performance' END AS performance " +
                                "FROM Registration reg " +
                                "JOIN Examination e ON reg.exam_id = e.exam_id " +
                                "JOIN Result r ON reg.registration_id = r.registration_id " +
                                "WHERE reg.student_id = ?";

                            PreparedStatement performancePs =
                                con.prepareStatement(performanceQuery);

                            performancePs.setInt(1, id);

                            ResultSet performanceRs =
                                performancePs.executeQuery();

                            boolean performanceFound = false;

                            while (performanceRs.next()) {

                                performanceFound = true;

                                System.out.println("Subject: " +
                                    performanceRs.getString("subject"));

                                System.out.println("Percentage: " +
                                    performanceRs.getDouble("percentage") + "%");

                                System.out.println("Grade: " +
                                    performanceRs.getString("grade"));

                                System.out.println("Performance: " +
                                    performanceRs.getString("performance"));

                                System.out.println("----------------------");
                            }

                            if (!performanceFound) {
                                System.out.println("No performance data available.");
                            }

                            break;

                        case 4:

                            startExam(con, sc, id);
                            break;

                        case 5:

                            System.out.println("Logged Out Successfully!");
                            break;

                        default:

                            System.out.println("Invalid Choice!");
                    }

                } while (choice != 5);

            } else {

                System.out.println("Invalid Student ID!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }


    // FACULTY LOGIN METHOD

    public static void facultyLogin(Scanner sc) {

        System.out.println("\n===== FACULTY LOGIN =====");

        System.out.print("Enter Faculty ID: ");
        int facultyId = sc.nextInt();

        System.out.print("Enter Password: ");
        String password = sc.next();

        try (Connection con = DBConnection.getConnection()) {

            String query =
                "SELECT * FROM Faculty WHERE faculty_id = ? AND password = ?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, facultyId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\nWelcome, " +
                    rs.getString("faculty_name"));

                System.out.println("Faculty Login Successful!");

                // OPEN FACULTY DASHBOARD
                facultyDashboard(con, sc);

            } else {

                System.out.println("Invalid Faculty ID or Password!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // FACULTY DASHBOARD

    public static void facultyDashboard(Connection con, Scanner sc) {

        int choice;

        do {

            System.out.println("\n===== FACULTY DASHBOARD =====");
            System.out.println("1. View All Exams");
            System.out.println("2. Add New Questions");
            System.out.println("3. View Student Results");
            System.out.println("4. View Student Performance");
            System.out.println("5. Python Data Analysis");
            System.out.println("6. Logout");  
            System.out.print("Enter your choice: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                System.out.println("Invalid input! Enter a number.");
                sc.next();
                choice = 0;
            }

            switch (choice) {

                case 1:
                    viewAllExams(con);
                    break;

                case 2:
                    addQuestion(con, sc);
                    break;

                case 3:
                    viewStudentResults(con);
                    break;

                case 4:
                    viewStudentPerformance(con);
                    break;
                
                 case 5:
                    runPythonAnalysis();
                     break;

                  case 6:
                    System.out.println("Faculty Logged Out Successfully!");
                    break;       
  
                 default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 6);
    }


    // VIEW ALL EXAMS

    public static void viewAllExams(Connection con) {

        try {

            System.out.println("\n--- ALL EXAMINATIONS ---");

            String query =
                "SELECT exam_id, subject, exam_date, duration, max_marks " +
                "FROM Examination";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                    "Exam ID: " + rs.getInt("exam_id") +
                    " | Subject: " + rs.getString("subject") +
                    " | Date: " + rs.getDate("exam_date") +
                    " | Duration: " + rs.getInt("duration") + " minutes" +
                    " | Marks: " + rs.getInt("max_marks")
                );
            }

            if (!found) {
                System.out.println("No examinations available.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ADD NEW QUESTIONS

    public static void addQuestion(Connection con, Scanner sc) {

        try {

            System.out.println("\n--- ADD NEW QUESTION ---");

            viewAllExams(con);

            System.out.print("\nEnter Exam ID: ");
            int examId = sc.nextInt();
            sc.nextLine();

            PreparedStatement check = con.prepareStatement(
                "SELECT exam_id FROM Examination WHERE exam_id = ?"
            );

            check.setInt(1, examId);

            ResultSet examRs = check.executeQuery();

            if (!examRs.next()) {
                System.out.println("Invalid Exam ID!");
                return;
            }

            System.out.print("Enter Question: ");
            String question = sc.nextLine();

            System.out.print("Enter Option A: ");
            String a = sc.nextLine();

            System.out.print("Enter Option B: ");
            String b = sc.nextLine();

            System.out.print("Enter Option C: ");
            String c = sc.nextLine();

            System.out.print("Enter Option D: ");
            String d = sc.nextLine();

            System.out.print("Enter Correct Answer (full option text): ");
            String answer = sc.nextLine();

            if (!answer.equals(a) && !answer.equals(b) &&
                !answer.equals(c) && !answer.equals(d)) {

                System.out.println(
                    "Correct answer must match one of the options!"
                );

                return;
            }

            System.out.print("Enter Marks: ");
            int marks = sc.nextInt();

            if (marks <= 0) {
                System.out.println("Marks must be greater than zero!");
                return;
            }

            Statement st = con.createStatement();

            ResultSet idRs = st.executeQuery(
                "SELECT COALESCE(MAX(question_id), 0) + 1 AS next_id FROM Question"
            );

            idRs.next();
            int questionId = idRs.getInt("next_id");

            String insert =
                "INSERT INTO Question " +
                "(question_id, exam_id, question_text, question_type, " +
                "option_a, option_b, option_c, option_d, correct_answer, marks) " +
                "VALUES (?, ?, ?, 'MCQ', ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(insert);

            ps.setInt(1, questionId);
            ps.setInt(2, examId);
            ps.setString(3, question);
            ps.setString(4, a);
            ps.setString(5, b);
            ps.setString(6, c);
            ps.setString(7, d);
            ps.setString(8, answer);
            ps.setInt(9, marks);

            ps.executeUpdate();

            System.out.println("Question Added Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // VIEW STUDENT RESULTS

    public static void viewStudentResults(Connection con) {

        try {

            System.out.println("\n--- STUDENT EXAM RESULTS ---");

            String query =
                "SELECT s.student_id, s.name, e.subject, " +
                "r.total_marks, r.percentage, r.grade, r.result_status " +
                "FROM Result r " +
                "JOIN Registration reg ON r.registration_id = reg.registration_id " +
                "JOIN Student s ON reg.student_id = s.student_id " +
                "JOIN Examination e ON reg.exam_id = e.exam_id " +
                "ORDER BY s.student_id";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                    "Student ID: " + rs.getInt("student_id") +
                    " | Name: " + rs.getString("name") +
                    " | Subject: " + rs.getString("subject") +
                    " | Marks: " + rs.getDouble("total_marks") +
                    " | Percentage: " + rs.getDouble("percentage") + "%" +
                    " | Grade: " + rs.getString("grade") +
                    " | Result: " + rs.getString("result_status")
                );
            }

            if (!found) {
                System.out.println("No student results available.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // VIEW STUDENT PERFORMANCE

    public static void viewStudentPerformance(Connection con) {

        try {

            System.out.println("\n--- STUDENT PERFORMANCE ANALYSIS ---");

            String query =
                "SELECT s.student_id, s.name, " +
                "COUNT(r.result_id) AS exams_attended, " +
                "ROUND(AVG(r.percentage), 2) AS average_percentage " +
                "FROM Student s " +
                "JOIN Registration reg ON s.student_id = reg.student_id " +
                "JOIN Result r ON reg.registration_id = r.registration_id " +
                "GROUP BY s.student_id, s.name " +
                "ORDER BY average_percentage DESC";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                    "Student ID: " + rs.getInt("student_id") +
                    " | Name: " + rs.getString("name") +
                    " | Exams Attended: " + rs.getInt("exams_attended") +
                    " | Average: " + rs.getDouble("average_percentage") + "%"
                );
            }

            if (!found) {
                System.out.println("No performance data available.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // START EXAM METHOD

    public static void startExam(Connection con, Scanner sc, int studentId) {

        try {

            System.out.println("\n--- YOUR REGISTERED EXAMS ---");

            String registeredQuery =
                "SELECT reg.exam_id, e.subject " +
                "FROM Registration reg " +
                "JOIN Examination e ON reg.exam_id = e.exam_id " +
                "WHERE reg.student_id = ? AND reg.status = 'Registered'";

            PreparedStatement registeredPs =
                con.prepareStatement(registeredQuery);

            registeredPs.setInt(1, studentId);

            ResultSet registeredRs = registeredPs.executeQuery();

            boolean found = false;

            while (registeredRs.next()) {

                found = true;

                System.out.println(
                    "Exam ID: " + registeredRs.getInt("exam_id") +
                    " | Subject: " + registeredRs.getString("subject")
                );
            }

            if (!found) {
                System.out.println("No registered exams available.");
                return;
            }

            System.out.print("\nEnter Exam ID to start: ");
            int examId = sc.nextInt();

            String checkQuery =
                "SELECT registration_id FROM Registration " +
                "WHERE student_id = ? AND exam_id = ? AND status = 'Registered'";

            PreparedStatement checkPs =
                con.prepareStatement(checkQuery);

            checkPs.setInt(1, studentId);
            checkPs.setInt(2, examId);

            ResultSet checkRs = checkPs.executeQuery();

            if (!checkRs.next()) {
                System.out.println("You are not registered for this exam!");
                return;
            }

            int registrationId = checkRs.getInt("registration_id");

            String examQuery =
                "SELECT subject, max_marks FROM Examination WHERE exam_id = ?";

            PreparedStatement examPs =
                con.prepareStatement(examQuery);

            examPs.setInt(1, examId);

            ResultSet examRs = examPs.executeQuery();

            if (!examRs.next()) {
                System.out.println("Exam not found!");
                return;
            }

            String subject = examRs.getString("subject");

            System.out.println("\n===== " + subject + " EXAM =====");

            con.setAutoCommit(false);

            // DELETE PREVIOUS STUDENT RESPONSES
            String deleteResponses =
                "DELETE FROM Student_Response WHERE registration_id = ?";

            PreparedStatement deleteResponsePs =
                con.prepareStatement(deleteResponses);

            deleteResponsePs.setInt(1, registrationId);
            deleteResponsePs.executeUpdate();

            // DELETE PREVIOUS EXAM RESULT
            String deleteResult =
                "DELETE FROM Result WHERE registration_id = ?";

            PreparedStatement deleteResultPs =
                con.prepareStatement(deleteResult);

            deleteResultPs.setInt(1, registrationId);
            deleteResultPs.executeUpdate();

            // LOAD EXAM QUESTIONS
            String questionQuery =
                "SELECT * FROM Question WHERE exam_id = ?";

            PreparedStatement questionPs =
                con.prepareStatement(questionQuery);

            questionPs.setInt(1, examId);

            ResultSet questionRs = questionPs.executeQuery();

            int totalMarks = 0;
            int totalQuestionMarks = 0;
            int questionCount = 0;

            Statement idStatement = con.createStatement();

            ResultSet responseIdRs = idStatement.executeQuery(
                "SELECT COALESCE(MAX(response_id), 0) + 1 AS next_id FROM Student_Response"
            );

            responseIdRs.next();

            int nextResponseId = responseIdRs.getInt("next_id");

            while (questionRs.next()) {

                questionCount++;

                int questionId = questionRs.getInt("question_id");
                int marks = questionRs.getInt("marks");

                totalQuestionMarks += marks;

                String optionA = questionRs.getString("option_a");
                String optionB = questionRs.getString("option_b");
                String optionC = questionRs.getString("option_c");
                String optionD = questionRs.getString("option_d");

                String correctAnswer = questionRs.getString("correct_answer");

                System.out.println("\nQuestion " + questionCount + ": " +
                    questionRs.getString("question_text"));

                System.out.println("A. " + optionA);
                System.out.println("B. " + optionB);
                System.out.println("C. " + optionC);
                System.out.println("D. " + optionD);

                System.out.print("Enter your answer (A/B/C/D): ");

                String answer;

                while (true) {

                    String choice = sc.next().toUpperCase();

                    switch (choice) {

                        case "A":
                            answer = optionA;
                            break;

                        case "B":
                            answer = optionB;
                            break;

                        case "C":
                            answer = optionC;
                            break;

                        case "D":
                            answer = optionD;
                            break;

                        default:
                            System.out.print("Invalid option! Enter A/B/C/D: ");
                            continue;
                    }

                    break;
                }

                int awardedMarks =
                    answer.equalsIgnoreCase(correctAnswer) ? marks : 0;

                totalMarks += awardedMarks;

                System.out.println(
                    awardedMarks > 0 ? "Correct Answer!" : "Wrong Answer!"
                );

                String insertResponse =
                    "INSERT INTO Student_Response " +
                    "(response_id, registration_id, question_id, answer, marks_awarded) " +
                    "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement responsePs =
                    con.prepareStatement(insertResponse);

                responsePs.setInt(1, nextResponseId++);
                responsePs.setInt(2, registrationId);
                responsePs.setInt(3, questionId);
                responsePs.setString(4, answer);
                responsePs.setInt(5, awardedMarks);

                responsePs.executeUpdate();
            }

            if (questionCount == 0 || totalQuestionMarks == 0) {

                con.rollback();
                con.setAutoCommit(true);

                System.out.println("No valid questions available for this exam.");
                return;
            }

            double percentage =
                (totalMarks * 100.0) / totalQuestionMarks;

            String grade;

            if (percentage >= 90) {
                grade = "O";
            } else if (percentage >= 80) {
                grade = "A";
            } else if (percentage >= 70) {
                grade = "B";
            } else if (percentage >= 50) {
                grade = "C";
            } else {
                grade = "F";
            }

            String status = percentage >= 50 ? "PASS" : "FAIL";

            Statement resultIdStatement = con.createStatement();

            ResultSet resultIdRs = resultIdStatement.executeQuery(
                "SELECT COALESCE(MAX(result_id), 0) + 1 AS next_id FROM Result"
            );

            resultIdRs.next();

            int resultId = resultIdRs.getInt("next_id");

            String insertResult =
                "INSERT INTO Result " +
                "(result_id, registration_id, total_marks, percentage, grade, result_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement resultInsertPs =
                con.prepareStatement(insertResult);

            resultInsertPs.setInt(1, resultId);
            resultInsertPs.setInt(2, registrationId);
            resultInsertPs.setInt(3, totalMarks);
            resultInsertPs.setDouble(4, percentage);
            resultInsertPs.setString(5, grade);
            resultInsertPs.setString(6, status);

            resultInsertPs.executeUpdate();

            String updateRegistration =
                "UPDATE Registration SET status = 'Completed' " +
                "WHERE registration_id = ?";

            PreparedStatement updatePs =
                con.prepareStatement(updateRegistration);

            updatePs.setInt(1, registrationId);
            updatePs.executeUpdate();

            con.commit();

            System.out.println("\n===== EXAM SUBMITTED =====");
            System.out.println("Subject: " + subject);
            System.out.println("Total Marks: " + totalMarks);
            System.out.println("Percentage: " + percentage + "%");
            System.out.println("Grade: " + grade);
            System.out.println("Result: " + status);

            con.setAutoCommit(true);

        } catch (Exception e) {

            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }

    // RUN PYTHON DATA ANALYSIS
    public static void runPythonAnalysis() {

        System.out.println("\n===== PYTHON DATA ANALYSIS =====");

        String[] scripts = {
            "data_retrieval.py",
            "data_preprocessing.py",
            "data_visualization.py"
        };

        try {
            java.io.File pythonFolder =
                new java.io.File("..", "06_Python_Analysis");

            if (!pythonFolder.exists() || !pythonFolder.isDirectory()) {
                System.out.println("Python folder not found: "
                    + pythonFolder.getAbsolutePath());
                return;
            }

            for (String script : scripts) {
                java.io.File scriptFile =
                    new java.io.File(pythonFolder, script);

                if (!scriptFile.exists()) {
                    System.out.println("Script not found: "
                        + scriptFile.getAbsolutePath());
                    return;
                }

                System.out.println("\nRunning: " + script);

                ProcessBuilder pb =
                    new ProcessBuilder("python", script);

                pb.directory(pythonFolder);
                pb.inheritIO();

                Process process = pb.start();
                int exitCode = process.waitFor();

                if (exitCode != 0) {
                    System.out.println(
                        "Python script failed: " + script
                        + " (exit code: " + exitCode + ")"
                    );
                    return;
                }
            }

            System.out.println(
                "\nPython Analysis Completed Successfully!"
            );

        } catch (Exception e) {
            System.out.println("Python Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // STUDENT REGISTRATION
public static void studentRegistration(Scanner sc) {

    try (Connection con = DBConnection.getConnection()) {

        if (con == null) return;

        sc.nextLine();

        System.out.println("\n===== STUDENT REGISTRATION =====");

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Department: ");
        String department = sc.nextLine();

        System.out.print("Enter Year: ");
        int year = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        String idQuery =
            "SELECT COALESCE(MAX(student_id), 0) + 1 AS next_id FROM Student";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(idQuery);

        rs.next();
        int studentId = rs.getInt("next_id");

        String insertQuery =
            "INSERT INTO Student " +
            "(student_id, name, department, year, email, phone, password) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(insertQuery);

        ps.setInt(1, studentId);
        ps.setString(2, name);
        ps.setString(3, department);
        ps.setInt(4, year);
        ps.setString(5, email);
        ps.setString(6, phone);
        ps.setString(7, password);

        ps.executeUpdate();

        System.out.println("\nRegistration Successful!");
        System.out.println("Your Student ID: " + studentId);
        System.out.println("Please save your Student ID.");

    } catch (Exception e) {
        System.out.println("Registration Failed: " + e.getMessage());
    }
}
// FACULTY REGISTRATION
public static void facultyRegistration(Scanner sc) {

    try (Connection con = DBConnection.getConnection()) {

        if (con == null) return;

        sc.nextLine();

        System.out.println("\n===== FACULTY REGISTRATION =====");

        System.out.print("Enter Faculty Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Department: ");
        String department = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        String idQuery =
            "SELECT COALESCE(MAX(faculty_id), 0) + 1 AS next_id FROM Faculty";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(idQuery);

        rs.next();
        int facultyId = rs.getInt("next_id");

        String insertQuery =
            "INSERT INTO Faculty (faculty_id, faculty_name, department, email, password) " +
            "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(insertQuery);

        ps.setInt(1, facultyId);
        ps.setString(2, name);
        ps.setString(3, department);
        ps.setString(4, email);
        ps.setString(5, password);

        ps.executeUpdate();

        System.out.println("\nFaculty Registration Successful!");
        System.out.println("Your Faculty ID: " + facultyId);
        System.out.println("Please save your Faculty ID.");

    } catch (Exception e) {
        System.out.println("Registration Failed: " + e.getMessage());
    }
}
}
