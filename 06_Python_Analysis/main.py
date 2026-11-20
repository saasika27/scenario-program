
import pandas as pd
import matplotlib.pyplot as plt
import os

# Read student results
df = pd.read_csv("06_Python_Analysis/student_results.csv")

# Data preprocessing
df = df.drop_duplicates()
df = df.dropna()

df["total_marks"] = pd.to_numeric(
    df["total_marks"], errors="coerce"
)
df["percentage"] = pd.to_numeric(
    df["percentage"], errors="coerce"
)

df = df.dropna(subset=["total_marks", "percentage"])

# Performance classification
def performance(percentage):
    if percentage >= 90:
        return "Excellent"
    elif percentage >= 75:
        return "Good"
    elif percentage >= 50:
        return "Average"
    else:
        return "Needs Improvement"

df["performance"] = df["percentage"].apply(performance)

# Save processed data
df.to_csv(
    "06_Python_Analysis/processed_results.csv",
    index=False
)

# Create visualization folder
os.makedirs("06_Python_Analysis/visualizations", exist_ok=True)

# 1. Student Marks Bar Chart
plt.figure(figsize=(10, 5))
labels = df["name"] + " - " + df["subject"]
plt.bar(labels, df["total_marks"])
plt.title("Student Marks Analysis")
plt.xlabel("Student - Subject")
plt.ylabel("Total Marks")
plt.xticks(rotation=45)
plt.tight_layout()
plt.savefig("06_Python_Analysis/visualizations/1_student_marks.png")

# 2. Performance Pie Chart
performance_count = df["performance"].value_counts()

plt.figure(figsize=(7, 7))
plt.pie(
    performance_count,
    labels=performance_count.index,
    autopct="%1.1f%%",
    startangle=90
)
plt.title("Student Performance Analysis")
plt.savefig("06_Python_Analysis/visualizations/2_performance.png")

# 3. Subject-wise Average Marks Bar Chart
subject_marks = df.groupby("subject")["total_marks"].mean()

plt.figure(figsize=(8, 5))
plt.bar(subject_marks.index, subject_marks.values)
plt.title("Subject-wise Average Marks")
plt.xlabel("Subject")
plt.ylabel("Average Marks")
plt.tight_layout()
plt.savefig("06_Python_Analysis/visualizations/3_subject_marks.png")

# 4. Grade Distribution Bar Chart
grade_count = df["grade"].value_counts()

plt.figure(figsize=(8, 5))
plt.bar(grade_count.index, grade_count.values)
plt.title("Grade Distribution")
plt.xlabel("Grade")
plt.ylabel("Number of Results")
plt.tight_layout()
plt.savefig("06_Python_Analysis/visualizations/4_grade_distribution.png")

# 5. Subject-wise Average Percentage Line Chart
subject_percentage = df.groupby("subject")["percentage"].mean()

plt.figure(figsize=(8, 5))
plt.plot(
    subject_percentage.index,
    subject_percentage.values,
    marker="o"
)
plt.title("Subject-wise Average Percentage")
plt.xlabel("Subject")
plt.ylabel("Average Percentage")
plt.grid(True)
plt.tight_layout()
plt.savefig("06_Python_Analysis/visualizations/5_subject_percentage.png")

# Display all five charts
plt.show()

print("Python Analysis Completed!")
print("All 5 charts generated successfully!")
