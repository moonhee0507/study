def read_dictionary(filename):
    dictionary = {}
    try:
        with open(filename, 'r') as f:
            for line in f:
                key, values = line.strip().split(':')
                dictionary[key] = values.split(',')
    except FileNotFoundError:
        print("Input file not found.")
    return dictionary


def invert_dictionary(original_dict):
    inverted = {}
    for key, values in original_dict.items():
        for value in values:
            if value not in inverted:
                inverted[value] = []
            inverted[value].append(key)
    return inverted


def write_dictionary(filename, dictionary):
    try:
        with open(filename, 'w') as f:
            for key, values in dictionary.items():
                f.write(f"{key}:{', '.join(values)}\n")
    except IOError:
        print("An error occurred while writing to the output file.")


# Program execution
input_file = "unit8/input_dict.txt"
output_file = "unit8/output_dict.txt"

original_dict = read_dictionary(input_file)
inverted_dict = invert_dictionary(original_dict)

print("Original dictionary:", original_dict)
print("Inverted dictionary:", inverted_dict)

write_dictionary(output_file, inverted_dict)