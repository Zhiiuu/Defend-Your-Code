import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.math.BigInteger;
import java.security.*;

/**
 * Team 12
 * @author Jered W
 * @author Julia K
 * @author Luca S
 */

public class Main {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Please enter your first name (up to 50 characters, only letters and hyphens):");
        String firstName = validateName(userInput);

        System.out.println("Please enter your last name (up to 50 characters, only letters and hyphens):");
        String lastName = validateName(userInput);

        System.out.println("Please enter two integer values (range: -2147483648 to 2147483647):");
        System.out.println("First Digit:");
        int num1 = validateInt(userInput);
        System.out.println("Second Digit:");
        int num2 = validateInt(userInput);

        System.out.println("Please enter the name of an input file (excluding file extension):");
        String inputFileName = validateFileName(userInput);

        System.out.println("Please enter the name of an output file (excluding file extension):");
        String outputFileName = validateFileName(userInput);

        //Password checker
        validatePassword(userInput);

        writeOutput(firstName, lastName, num1, num2, inputFileName, outputFileName);




    }

    private static void writeOutput(final String firstName, final String lastName,
                                    final int num1, final int num2,
                                    final String inputFileName,
                                    final String outputFileName) {

        // Write the user information to the output file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName))) {
            bw.write("First Name: " + firstName);
            bw.newLine();
            bw.write("Last Name: " + lastName);
            bw.newLine();
            bw.write("First Integer: " + num1);
            bw.newLine();
            bw.write("Second Integer: " + num2);
            bw.newLine();
            BigInteger sum = BigInteger.valueOf(num1).add(BigInteger.valueOf(num2));
            BigInteger product = BigInteger.valueOf(num1).multiply(BigInteger.valueOf(num2));
            bw.write("Sum: " + sum);
            bw.newLine();
            bw.write("Product: " + product);
            bw.newLine();

            // Write the contents of the input file to the output file
            bw.write("Input File Name: " + inputFileName);
            bw.newLine();
            bw.write("Input File Contents:");
            bw.newLine();
            try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (FileNotFoundException e) {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Information successfully written to " + outputFileName);
    }

    private static void validatePassword(Scanner scanner) {

        // Generate salt for password hashing
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        boolean passwordVerified = false;

        while (!passwordVerified) {
            System.out.println("Please enter a password: ");
            String firstPassword = scanner.nextLine();

            //Hash password
            byte[] firstHashedPassword = hashPassword(firstPassword, salt);

            //Store password
            storePasswordToFile(firstHashedPassword);

            System.out.println("Please confirm your password: ");
            String secondPassword = scanner.nextLine();

            //Hash 2nd entry, pull hash from file, compare hashes
            //Hash password
            byte[] secondHashedPassword = hashPassword(secondPassword, salt);

            //Pull previously stored hash from file
            byte[] hashedPasswordFromFile = pullPasswordFromFile();

            //Compare hashes
            if (Arrays.equals(secondHashedPassword, hashedPasswordFromFile)) {
                passwordVerified = true;
            } else {
                System.out.println("Passwords do not match. Please try again.");
            }
        }

    }

    private static byte[] hashPassword(final String password, final byte[] salt) {

        byte[] hashedPassword;

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            hashedPassword = md.digest(password.getBytes());


        } catch (NoSuchAlgorithmException e) {
            try (FileWriter fw = new FileWriter("error_log.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                e.printStackTrace(new PrintWriter(bw));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        }

        return hashedPassword;

    }

    private static byte[] pullPasswordFromFile() {
        try(FileInputStream fis = new FileInputStream("password.txt")) {
            //System.out.println(fis.read());
            byte[] hashedPassword = fis.readAllBytes();
            fis.close();
            return hashedPassword;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void storePasswordToFile(final byte[] hashedPassword) {
        // Write the salt and hashed password to a file
        try (FileOutputStream fos = new FileOutputStream("password.txt")) {
            fos.write(hashedPassword);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int validateInt(Scanner scanner) {
        int num;
        while (true) {
            String line = scanner.nextLine();
            Scanner temp = new Scanner(line);
            if (temp.hasNextInt()) {
                num = temp.nextInt();
                if (num >= -2147483648 && num <= 2147483647) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter an integer value (range: -2147483648 to 2147483647):");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer value (range: -2147483648 to 2147483647):");
            }

        }
        return num;
    }

    private static String validateName(Scanner scanner) {
        String name;
        while (true) {
            name = scanner.nextLine();
            if (name.length() > 0 && name.length() <= 50 && name.matches("^[a-zA-Z-]+$")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a name with up to 50 characters, only letters and hyphens:");
            }
        }
        return name;
    }

    private static String validateFileName(Scanner scanner) {
        String fileName;
        while (true) {
            fileName = scanner.nextLine();
            if (fileName.length() > 0 && fileName.length() <= 50 && fileName.matches("^[a-zA-Z0-9-]+$")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a file name with up to 50 characters, only letters, numbers, and hyphens:");
            }
        }
        return fileName;
    }

    /**
     * Helper method used for debugging
     */
    private static void printBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i]);
        }
        System.out.println();
    }
}
