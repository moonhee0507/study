# Create employee name list (Zootopia characters)
employees = [
    "Judy Hopps",
    "Nick Wilde",
    "Chief Bogo",
    "Bellwether",
    "Flash",
    "Clawhauser",
    "Gazelle",
    "Finnick",
    "Mr. Big",
    "Fru Fru"
]

# Split the list into two sub-lists
subList1 = employees[:5]
subList2 = employees[5:]

# Add a new employee
subList2.append("Kriti Brown")

# Remove the second employee from subList1
del subList1[1]

# Merge the two lists
mergedList = subList1 + subList2

# Create salary list
salaryList = [50000, 52000, 55000, 48000, 47000, 53000, 60000, 45000, 58000, 62000, 51000]

# Give a 4 percent salary increase
for i in range(len(salaryList)):
    salaryList[i] = salaryList[i] * 1.04

# Sort the salary list in descending order
salaryList.sort(reverse=True)

# Show top 3 salaries
top_three_salaries = salaryList[:3]

print("Final employee list:")
print(mergedList)
print()

print("Top 3 salaries:")
print(top_three_salaries)