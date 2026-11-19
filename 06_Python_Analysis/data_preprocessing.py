
import pandas as pd

# Read student results
df = pd.read_csv("student_results.csv")

print("===== ORIGINAL DATA =====")
print(df)

# Remove duplicate records
df = df.drop_duplicates()

# Handle missing values
df = df.dropna()

# Convert marks into numeric values
df["total_marks"] = pd.to_numeric(
    df["total_marks"], errors="coerce"
)

df["percentage"] = pd.to_numeric(
    df["percentage"], errors="coerce"
)

# Remove invalid numeric records
df = df.dropna(
    subset=["total_marks", "percentage"]
)

# Categorize student performance
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

# Display processed data
print("\n===== PROCESSED STUDENT DATA =====")
print(df)

# Save processed data
df.to_csv("processed_results.csv", index=False)

print("\nData preprocessing completed!")
print("Processed data saved successfully!")
