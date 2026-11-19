import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import os

# Read preprocessed data
df = pd.read_csv("processed_results.csv")

# Create folder for charts
os.makedirs("visualizations", exist_ok=True)

sns.set_theme(style="whitegrid")

# 1. Student Marks Comparison
plt.figure(figsize=(9, 5))
sns.barplot(data=df, x="name", y="total_marks", hue="subject")
plt.title("Student Marks Comparison")
plt.xlabel("Student Name")
plt.ylabel("Total Marks")
plt.tight_layout()
plt.savefig("visualizations/student_marks.png")
plt.show()

# 2. Subject-wise Average Percentage
subject_avg = df.groupby("subject")["percentage"].mean()

plt.figure(figsize=(8, 5))
subject_avg.plot(kind="bar")
plt.title("Subject-wise Average Percentage")
plt.xlabel("Subject")
plt.ylabel("Average Percentage")
plt.ylim(0, 110)
plt.tight_layout()
plt.savefig("visualizations/subject_performance.png")
plt.show()

# 3. Pass and Fail Analysis
result_counts = df["result_status"].value_counts()

plt.figure(figsize=(6, 6))
plt.pie(
    result_counts,
    labels=result_counts.index,
    autopct="%1.1f%%",
    startangle=90
)
plt.title("Pass and Fail Analysis")
plt.tight_layout()
plt.savefig("visualizations/pass_fail_analysis.png")
plt.show()

# 4. Performance Category Analysis
performance_counts = df["performance"].value_counts()

plt.figure(figsize=(8, 5))
sns.barplot(
    x=performance_counts.index,
    y=performance_counts.values
)
plt.title("Student Performance Analysis")
plt.xlabel("Performance Category")
plt.ylabel("Number of Results")
plt.tight_layout()
plt.savefig("visualizations/performance_analysis.png")
plt.show()

# NumPy statistical analysis
percentages = df["percentage"].to_numpy()

print("\n===== NUMPY STATISTICS =====")
print("Average Percentage:", np.mean(percentages))
print("Highest Percentage:", np.max(percentages))
print("Lowest Percentage:", np.min(percentages))

print("\nAll visualizations generated successfully!")
print("Charts are saved inside the visualizations folder.")