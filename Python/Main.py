import re
import os
import hashlib
import secrets

#Group: Team 12
#Jered W
#Julia K
#Luca S


#validates name and allow up to 50 characters of only letters that starts with a capital letter
def validateName():
    name = ""
    valid = False
    while not valid:
        name = input("(Enter a valid name that that only contains uppercase/lowercase letters and/or hyphens)")
        valid = re.match("^[a-zA-Z-]{1,50}+$", name)
        if not valid:
            print("Error: Name is invalid \n")
        else:
            print("Name is valid")
            valid = True
    return name


#prompts for 2 int inputs and adds them together if they are withing the correct range
def validateInt(num):
    valid = False
    while not valid:   
        try:
            if  num > 2147483647 or num < -2147483648:
                print("Error: One or more numbers are out of range \n")
                num = int(input("Please enter an int between the numbers -2147483648 and 2147483647: "))
            else:
                valid = True
        except ValueError:
            print("Error: One or more numbers are not integers\n")
    return num


def hash_password(password, salt):
    sha256 = hashlib.sha256()
    sha256.update(salt)
    sha256.update(password.encode('utf-8'))
    return sha256.digest()


def store_password_to_file(hashed_password):
    with open("password.txt", 'wb') as file:
        file.write(hashed_password)


def pull_password_from_file():
    with open("password.txt", 'rb') as file:
        hashed_password = file.read()
    return hashed_password


def validatePassword():
    salt = secrets.token_bytes(16)
    password_verified = False

    while not password_verified:
        first_password = input("Please enter a password: ")
        first_hashed_password = hash_password(first_password, salt)
        store_password_to_file(first_hashed_password)

        second_password = input("Please confirm your password: ")
        second_hashed_password = hash_password(second_password, salt)
        hashed_password_from_file = pull_password_from_file()

        if second_hashed_password == hashed_password_from_file:
            password_verified = True
        else:
            print("Passwords do not match. Please try again\n")     


def validate_file_name(file_name):
    valid = False
    while not valid:
        if not re.match("^[a-zA-Z0-9-_]{1,50}+$", file_name):
            print("Error: File name is invalid\n")
            file_name = input("Please enter a valid file name: ")
        else:
            valid = True
    return file_name


def write_output(first_name, last_name, num1, num2, input_file_name, output_file_name):
    with open(output_file_name, 'w') as out_file:
        out_file.write(f"First Name: {first_name}\n")
        out_file.write(f"Last Name: {last_name}\n")
        out_file.write(f"First Integer: {num1}\n")
        out_file.write(f"Second Integer: {num2}\n")
        out_file.write(f"Sum: {num1 + num2}\n")
        out_file.write(f"Product: {num1 * num2}\n")

        out_file.write(f"Input File Name: {input_file_name}\n")
        out_file.write("Input File Contents:\n")

        try:
            with open(input_file_name, 'r') as input_file:
                for line in input_file:
                    out_file.write(line)
        except FileNotFoundError:
            pass
    print(f"Information successfully written to {output_file_name}")  


def main():  
    print("Please enter your first name")
    first_name = validateName()
          
    print("\nPlease enter your last name")
    last_name = validateName()

    num1 = int(input("\nPlease enter an int between the numbers -2147483648 and 2147483647: "))
    num2 = int(input("\nPlease enter a second int between the numbers -2147483648 and 2147483647: "))
    num1, num2 = validateInt(num1), validateInt(num2)

    input_file_name = validate_file_name(input("\nPlease enter the name of the input file (Enter a file name including any combination of uppercase/lowercase letters, numbers, hyphens, and/or underscores but excluding the file extension.)\n"))
    output_file_name = validate_file_name(input("\nPlease enter the name of the output file (Enter a file name including any combination of uppercase/lowercase letters, numbers, hyphens, and/or underscores but excluding the file extension.)\n"))

    validatePassword()

    write_output(first_name, last_name, num1, num2, input_file_name, output_file_name)


if __name__ == "__main__":
    main()
