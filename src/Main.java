/**
 * Programmer: Mohamed Hussein
 * Course: COSC 311, F '24
 * Project: 5
 * Due date: 12-03-24
 */

import java.io.*;
import java.util.*;

public class Main {
    // Constants
    private static final int RECORD_SIZE = 4 + 8 + 40 + 40; // Size of a record in bytes
    private static final String DELETED = "DELETED"; // Marker for deleted records
    private static final Hashing<Integer, Integer> indexTable = new Hashing<>(); // Hash table for indexing records

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // scanner object
        String outputFilename = null; // Name of the random access file, initilized so it doesnt cause errors


        while (true) {
            displayMenu(); // Display the menu options
            int choice = getChoice(scanner); // Get user's choice

            try {
                // switch for getChoice
                switch (choice) {
                    case 1:
                        outputFilename = RAF(scanner); // Create a random access file
                        break;
                    case 2:
                        if (isValidFile(outputFilename)) {
                            displayRecords(scanner, outputFilename); // Display records from the file
                        }
                        break;
                    case 3:
                        if (isValidFile(outputFilename)) {
                            buildIndex(outputFilename); // Build the hash table index
                        }
                        break;
                    case 4:
                        if (isValidIndex()) {
                            displayIndex(); // Display the hash table index
                        }
                        break;
                    case 5:
                        if (isValidFile(outputFilename) && isValidIndex()) {
                            retrieveRecord(scanner, outputFilename); // Retrieve a record using the index
                        }
                        break;
                    case 6:
                        if (isValidFile(outputFilename) && isValidIndex()) {
                            modifyRecord(scanner, outputFilename); // Modify an existing record
                        }
                        break;
                    case 7:
                        if (isValidFile(outputFilename) && isValidIndex()) {
                            addNewRecord(scanner, outputFilename); // Add a new record to the file
                        }
                        break;
                    case 8:
                        if (isValidFile(outputFilename) && isValidIndex()) {
                            deleteRecord(scanner, outputFilename); // Delete a record
                        }
                        break;
                    case 9:
                        quitProgram(); // Quit the program
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (Exception e) {
                // Handle any unexpected errors
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the main menu to the user.
     */
    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Make a random-access file");
        System.out.println("2. Display the random-access file");
        System.out.println("3. Build the index");
        System.out.println("4. Display the index");
        System.out.println("5. Retrieve a record");
        System.out.println("6. Modify a record");
        System.out.println("7. Add a new record");
        System.out.println("8. Delete a record");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Reads and validates the user's menu choice.
     * param scanner Scanner object for user input
     * return Validated choice as an integer
     */
    private static int getChoice(Scanner scanner) {
        int choice = -1;
        while (true) {
            try {
                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input. Enter a number: ");
                    scanner.next();
                }
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 9) {
                    break;
                } else {
                    System.out.print("Invalid choice. Enter a number between 1 and 9: ");
                }
            } catch (Exception e) {
                System.out.println("Error reading input: " + e.getMessage());
            }
        }
        return choice;
    }

    /**
     * Checks if a random access file is valid (exists and is not null).
     * param filename Name of the file to validate
     * return True if valid, false otherwise
     */
    private static boolean isValidFile(String filename) {
        if (filename == null || !new File(filename).exists()) {
            System.out.println("Random Access File does not exist. Please create it first.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the hash table index is valid (not empty).
     * return True if valid, false otherwise
     */
    private static boolean isValidIndex() {
        for (int i = 0; i < indexTable.getSIZE(); i++) {
            if (indexTable.getTable()[i] != null && !indexTable.getTable()[i].empty()) {
                return true;
            }
        }
        System.out.println("Index does not exist or is empty. Please build the index first.");
        return false;
    }

    /**
     * Creates a random access file from a text file.
     * param scanner Scanner object for user input
     * return Name of the created random access file
     */
    private static String RAF(Scanner scanner) {
        System.out.print("Enter an input file name: ");
        String inputFilename = scanner.next();
        File inputFile = new File(inputFilename);
        if (!inputFile.exists() || inputFile.isDirectory()) {
            System.out.println("Error: Input file does not exist.");
            return null;
        }

        System.out.print("Enter an output file name: ");
        String outputFilename = scanner.next();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(outputFilename, "rw");
             Scanner fileScanner = new Scanner(inputFile)) {
            while (fileScanner.hasNextLine()) {
                Student student = new Student();
                student.readFromTextFile(fileScanner); // Read student data from the text file
                student.writeToFile(randomAccessFile); // Write student data to the random access file
            }
            System.out.println("Random access file is built successfully\n");
        } catch (IOException e) {
            System.out.println("\nError, file not created: " + e.getMessage());
            return null;
        }

        return outputFilename;
    }

    /**
     * Displays the records from the random access file.
     * param scanner Scanner object for user input
     * param filename Name of the random access file
     */
    private static void displayRecords(Scanner scanner, String filename) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
            long recordCount = randomAccessFile.length() / RECORD_SIZE; // Total number of records
            int currentRecord = 0; // Pointer to the current record being displayed
            int displayedCount = 0; // Counter for displayed records

            System.out.println("Displaying records: ");
            while (currentRecord < recordCount) {
                try {
                    randomAccessFile.seek(currentRecord * RECORD_SIZE); // Navigate to the current record
                    Student student = new Student();
                    student.readFromFile(randomAccessFile); // Read the record into a Student object
                    if (!student.getFirst().trim().equals(DELETED)) {
                        System.out.println(student); // Print the record if it's not marked as deleted
                        displayedCount++;
                    }
                    currentRecord++;

                    // Handle pagination after every 5 displayed records
                    if (displayedCount % 5 == 0 && currentRecord < recordCount) {
                        System.out.println("\nEnter N (for next 5 records), A (for all remaining records), M(for main menu):");
                        String choice = scanner.next().toUpperCase();
                        if (choice.equals("M")) {
                            break; // Return to the main menu
                        } else if (choice.equals("A")) {
                            // Display all remaining records
                            while (currentRecord < recordCount) {
                                randomAccessFile.seek(currentRecord * RECORD_SIZE);
                                student = new Student();
                                student.readFromFile(randomAccessFile);
                                if (!student.getFirst().trim().equals(DELETED)) {
                                    System.out.println(student);
                                    displayedCount++;
                                }
                                currentRecord++;
                            }
                            break;
                        } else if (!choice.equals("N")) {
                            System.out.println("Invalid choice. Returning to main menu.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading record: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error displaying file: " + e.getMessage());
        }
    }

    /**
     * Builds the index (hash table) from the RAF
     * Reads each record, extracts its key (student ID), and adds it to the index
     * param filename Name of the random access file
     */
    private static void buildIndex(String filename) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r")) {
            long recordCount = randomAccessFile.length() / RECORD_SIZE; // Total number of records

            System.out.println("\nBuilding index...");
            for (int i = 0; i < recordCount; i++) {
                try {
                    randomAccessFile.seek(i * RECORD_SIZE); // Navigate to the record
                    Student student = new Student();
                    student.readFromFile(randomAccessFile);

                    if (!student.getFirst().trim().equals(DELETED)) {
                        int key = student.getID(); // Extract student ID as the key
                        int address = i; // Record position serves as the value (address)
                        indexTable.put(key, address); // Insert key-value pair into the hash table
                    }
                } catch (IOException e) {
                    System.out.println("Error reading record for index: " + e.getMessage());
                }
            }
            System.out.println("Index built successfully.\n");
        } catch (IOException e) {
            System.out.println("Error building index: " + e.getMessage());
        }
    }

    /**
     * Displays the contents of the hash table (index).
     */
    private static void displayIndex() {
        System.out.println("\nDisplaying index:");
        for (int i = 0; i < indexTable.getSIZE(); i++) {
            BST<Pair<Integer, Integer>> table = indexTable.getTable()[i]; // Get the table at index i
            if (table != null) {
                System.out.print(i + ": ");
                try {
                    table.levelOrder(); // Display table contents in level-order
                } catch (Exception e) {
                    System.out.println("Error displaying index table: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Retrieves and displays a specific record based on the student ID.
     * param scanner Scanner object for user input
     * param filename Name of the random access file
     */
    private static void retrieveRecord(Scanner scanner, String filename) {
        System.out.print("Enter student ID: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a valid student ID: ");
            scanner.next();
        }
        int studentId = scanner.nextInt(); // Get the student ID from user
        int index = indexTable.hash(studentId); // Calculate the hash index
        BST<Pair<Integer, Integer>> table = indexTable.getTable()[index]; // Access the table

        if (table != null) {
            Pair<Integer, Integer> searchPair = new Pair<>(studentId, null); // Create a search key
            Pair<Integer, Integer> foundPair = table.find(searchPair); // Search the table

            if (foundPair != null) {
                int recordPosition = foundPair.getValue(); // Get the address of the record
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r")) {
                    randomAccessFile.seek(recordPosition * RECORD_SIZE); // Navigate to the record
                    Student student = new Student();
                    student.readFromFile(randomAccessFile); // Read the record
                    // Display the record
                    System.out.println("Student ID: " + student.getID());
                    System.out.println("Name: " + student.getFirst() + " " + student.getLast());
                    System.out.println("GPA: " + student.getGPA());
                } catch (IOException e) {
                    System.out.println("Error retrieving record: " + e.getMessage());
                }
            } else {
                System.out.println("No record found for Student ID: " + studentId);
            }
        } else {
            System.out.println("No record found for Student ID: " + studentId);
        }
    }

    /**
     * Modifies an existing record in the random access file.
     * Prompts the user for a student ID, displays the current record, and allows updates to specific fields.
     * param scanner Scanner object for user input
     * param filename Name of the random access file
     */
    private static void modifyRecord(Scanner scanner, String filename) {
        System.out.print("Enter student ID: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a valid student ID: ");
            scanner.next();
        }
        int studentId = scanner.nextInt(); // Get the student ID from user
        int index = indexTable.hash(studentId); // Calculate the hash index
        BST<Pair<Integer, Integer>> table = indexTable.getTable()[index]; // Access the table

        if (table != null) {
            Pair<Integer, Integer> searchPair = new Pair<>(studentId, null); // Create a search key
            Pair<Integer, Integer> foundPair = table.find(searchPair); // Search the table

            if (foundPair != null) {
                int recordPosition = foundPair.getValue(); // Get the address of the record
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
                    randomAccessFile.seek(recordPosition * RECORD_SIZE); // Navigate to the record
                    Student student = new Student();
                    student.readFromFile(randomAccessFile); // Read the record

                    // Display current record information
                    System.out.println("Current record: ");
                    System.out.println("Student ID: " + student.getID());
                    System.out.println("First Name: " + student.getFirst().trim());
                    System.out.println("Last Name: " + student.getLast().trim());
                    System.out.println("GPA: " + student.getGPA());

                    // Prompt user for modifications
                    System.out.print("\nEnter new first name (or press Enter to keep current): ");
                    scanner.nextLine(); // Consume the leftover newline
                    String newFirstName = scanner.nextLine();
                    if (!newFirstName.trim().isEmpty()) {
                        student.setFirst(newFirstName);
                    }

                    System.out.print("Enter new last name (or press Enter to keep current): ");
                    String newLastName = scanner.nextLine();
                    if (!newLastName.trim().isEmpty()) {
                        student.setLast(newLastName);
                    }

                    System.out.print("Enter new GPA (or press Enter to keep current): ");
                    String newGpaInput = scanner.nextLine();
                    if (!newGpaInput.trim().isEmpty()) {
                        try {
                            double newGpa = Double.parseDouble(newGpaInput);
                            student.setGPA(newGpa);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid GPA input. Keeping current GPA.");
                        }
                    }

                    // Write updated record back to the file
                    randomAccessFile.seek(recordPosition * RECORD_SIZE);
                    student.writeToFile(randomAccessFile);
                    System.out.println("Record updated successfully.");

                } catch (IOException e) {
                    System.out.println("Error modifying record: " + e.getMessage());
                }
            } else {
                System.out.println("No record found for Student ID: " + studentId);
            }
        } else {
            System.out.println("No record found for Student ID: " + studentId);
        }
    }

    /**
     * Adds a new record to the random access file.
     * Ensures that the student ID does not already exist and prompts the user for new record data.
     * param scanner Scanner object for user input
     * param filename Name of the random access file
     */
    private static void addNewRecord(Scanner scanner, String filename) {
        System.out.print("Enter student ID: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a valid student ID: ");
            scanner.next();
        }
        int studentId = scanner.nextInt(); // Get the student ID from user
        int index = indexTable.hash(studentId); // Calculate the hash index
        BST<Pair<Integer, Integer>> table = indexTable.getTable()[index]; // Access the table

        // Check if a record with this ID already exists
        if (table != null && table.find(new Pair<>(studentId, null)) != null) {
            System.out.println("A record with this student ID already exists.");
            return;
        }

        scanner.nextLine(); // Consume the leftover newline
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter GPA: ");
        double gpa = scanner.nextDouble();

        // Create a new student record
        Student student = new Student();
        student.setData(firstName, lastName, studentId, gpa);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
            long recordCount = randomAccessFile.length() / RECORD_SIZE; // Get the current record count
            randomAccessFile.seek(recordCount * RECORD_SIZE); // Navigate to the end of the file
            student.writeToFile(randomAccessFile); // Write the new record

            // Add the new record to the hash table
            indexTable.put(studentId, (int) recordCount);
            System.out.println("Record added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    /**
     * Deletes a record from the random access file by marking it as "DELETED."
     * Also removes the record from the hash table index.
     * param scanner Scanner object for user input
     * param filename Name of the random access file
     */
    private static void deleteRecord(Scanner scanner, String filename) {
        System.out.print("Enter student ID: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a valid student ID: ");
            scanner.next();
        }
        int studentId = scanner.nextInt(); // Get the student ID from user
        int index = indexTable.hash(studentId); // Calculate the hash index
        BST<Pair<Integer, Integer>> table = indexTable.getTable()[index]; // Access the table

        if (table != null) {
            Pair<Integer, Integer> searchPair = new Pair<>(studentId, null); // Create a search key
            Pair<Integer, Integer> foundPair = table.find(searchPair); // Search the table

            if (foundPair != null) {
                int recordPosition = foundPair.getValue(); // Get the address of the record
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
                    randomAccessFile.seek(recordPosition * RECORD_SIZE); // Navigate to the record
                    Student student = new Student();
                    student.readFromFile(randomAccessFile); // Read the record

                    // Mark the record as deleted
                    student.setFirst(DELETED);
                    randomAccessFile.seek(recordPosition * RECORD_SIZE);
                    student.writeToFile(randomAccessFile); // Write the updated record
                    System.out.println("Record deleted successfully.");

                    // Remove the record from the hash table
                    table.remove(searchPair);
                } catch (IOException e) {
                    System.out.println("Error deleting record: " + e.getMessage());
                }
            } else {
                System.out.println("No record found for Student ID: " + studentId);
            }
        } else {
            System.out.println("No record found for Student ID: " + studentId);
        }
    }


    /**
     * Quits the program after displaying the entire hash table for verification.
     * Exits the program with a message.
     */
    private static void quitProgram() {
        System.out.println("\nDisplaying entire hash table before quitting:");
        for (int i = 0; i < indexTable.getSIZE(); i++) {
            BST<Pair<Integer, Integer>> table = indexTable.getTable()[i];
            if (table != null && !table.empty()) {
                System.out.print(i + "- ");
                table.levelOrder(); // Display the table contents
                System.out.println();
            }
        }
        System.out.println("Exiting the program...");
        System.exit(0); // Terminate the program
    }
}
