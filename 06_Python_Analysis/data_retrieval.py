
import mysql.connector
import pandas as pd

# Connect to ExamInsight database
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="password",
    database="ExamInsightDB"
)

# Retrieve student examination results
query = """
SELECT
    s.student_id,
    s.name,
    e.subject,
    r.total_marks,
    r.percentage,
    r.grade,
    r.result_status
FROM Student s
JOIN Registration reg
    ON s.student_id = reg.student_id
JOIN Examination e
    ON reg.exam_id = e.exam_id
JOIN Result r
    ON reg.registration_id = r.registration_id
"""

# Convert database records into DataFrame
df = pd.read_sql_query(query, conn)

print("===== EXAMINSIGHT STUDENT RESULTS =====")
print(df)

# Save retrieved data
df.to_csv("student_results.csv", index=False)

conn.close()

print("\nData retrieved successfully!")
print("Student results saved to student_results.csv")
