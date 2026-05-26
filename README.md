# Java Student Record Indexing System

## Overview

This project is a Java-based student record management system that uses random-access files for persistent storage and custom data structures for indexed record lookup.

The program reads student records from a text file, writes them into a fixed-length random-access file, builds a hash table index over the records, and supports record retrieval, modification, insertion, deletion, and display through a menu-driven command-line interface.

## Features

- Converts text-based student records into a random-access file
- Uses fixed-length records for direct file positioning
- Builds an index for faster student ID lookup
- Supports displaying, retrieving, modifying, adding, and deleting records
- Uses a custom hash table implementation with binary search tree chaining
- Includes custom data structures such as `BST`, `Queue`, and `Pair`
- Provides a sample input file and sample run output

## Project Structure

| Path | Purpose |
|---|---|
| `src/Main.java` | Menu-driven application logic and record operations |
| `src/Student.java` | Student record model and file serialization methods |
| `src/Hashing.java` | Custom hash table used for indexing records |
| `src/BST.java` | Binary search tree used for hash-table chaining |
| `src/Queue.java` | Queue implementation used by tree traversal logic |
| `src/Pair.java` | Key-value pair structure for indexed records |
| `data/students.txt` | Sample student input data |
| `docs/sample-run.txt` | Example command-line run of the program |
| `Makefile` | Build and run commands |

## How It Works

1. The user provides a text input file containing student records.
2. The program writes each student into a random-access file using a fixed record size.
3. The index is built by mapping student IDs to record positions.
4. Record operations use the index to seek directly to the correct file location.
5. Deleted records are marked with a `DELETED` marker rather than immediately removed from the file.

## Requirements

- Java JDK 8 or newer
- Make, optional but recommended
- Linux, macOS, WSL, or another Unix-like terminal environment

## Build

Using Make:

```bash
make
```

Manual build:

```bash
mkdir -p build
javac -d build src/*.java
```

## Run

Using Make:

```bash
make run
```

Manual run:

```bash
java -cp build Main
```

When the program asks for an input file, use:

```text
data/students.txt
```

For the random-access output file, use a generated file name such as:

```text
students.db
```

## Example Workflow

From the menu:

```text
1. Make a random-access file
2. Display the random-access file
3. Build the index
4. Display the index
5. Retrieve a record
6. Modify a record
7. Add a new record
8. Delete a record
9. Exit
```

A typical test flow is:

```text
1 -> create random-access file from data/students.txt
2 -> display records
3 -> build the index
4 -> display the index
5 -> retrieve a record by student ID
6 -> modify a record
7 -> add a new record
8 -> delete a record
9 -> exit
```

## Skills Demonstrated

- Java programming
- File I/O and random-access files
- Fixed-length record design
- Hash table indexing
- Binary search tree chaining
- Queue-based traversal
- CRUD operations
- Command-line application design
- Data structures implementation

## Future Improvements

- Add automated tests for record operations
- Add input validation for malformed student records
- Add a cleaner command-line interface
- Save and reload the index automatically
- Replace deleted record slots when adding new records
- Add CSV import/export support
