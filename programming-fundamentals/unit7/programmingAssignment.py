def invert_dictionary(student_courses):
    inverted = {}

    for student, courses in student_courses.items():
        for course in courses:
            if course not in inverted:
                inverted[course] = []
            inverted[course].append(student)

    return inverted


# Original dictionary
students = {
    'Stud1': ['CS1101', 'CS2402', 'CS2001'],
    'Stud2': ['CS2402', 'CS2001', 'CS1102']
}

# Invert the dictionary
inverted_students = invert_dictionary(students)

# Print results
print("Original Dictionary:")
print(students)

print("\nInverted Dictionary:")
print(inverted_students)