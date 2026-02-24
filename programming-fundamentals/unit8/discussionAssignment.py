filename = "unit8/data.txt"

try:
    f = open(filename, "r")
    content = f.read()
    print("File '%s' opened successfully." % filename)
    f.close()
except FileNotFoundError:
    print("Error: The file '%s' was not found." % filename)