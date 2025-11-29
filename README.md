1. **Assignment 1**: Producer-Consumer pattern with thread synchronization
   - Demonstrates concurrent programming concepts
   - Implements thread-safe blocking queue
   - Uses wait/notify mechanism for thread coordination
---

## Project Structure

```
BuildingChallenge/
├── pom.xml                                    # Maven build configuration
├── README.md                                  # This file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── buildingchallenge/
│   │   │           ├── assignment1/
│   │   │           │   ├── SharedQueue.java          # Thread-safe blocking queue
│   │   │           │   ├── Producer.java             # Producer thread implementation
│   │   │           │   ├── Consumer.java             # Consumer thread implementation
│   │   │           │   └── ProducerConsumerDemo.java # Main demo application
│   │   │           └── assignment2/
│   │   │               ├── SalesRecord.java          # Data model for sales records
│   │   │               ├── CSVDataReader.java        # CSV file reader and parser
│   │   │               ├── SalesAnalyzerUtil.java    # Interface for sales analysis
│   │   │               ├── SalesAnalyzerImpl.java    # Implementation with Stream API operations
│   │   │               └── SalesAnalysisDemo.java    # Main demo application
│   │   └── resources/
│   │       └── sales_data.csv                        # Sample CSV data file
│   └── test/
│       └── java/
│           └── com/
│               └── buildingchallenge/
│                   ├── assignment1/
│                   │   ├── SharedQueueTest.java      # Tests for SharedQueue
│                   │   └── ProducerConsumerTest.java # Unit tests for integrating both Producer and Consumer functionality
│                   └── assignment2/
│                       ├── SalesQueryAnalyzerTest.java # Tests for query operations
│                       └── CSVDataReaderTest.java      # Tests for CSV reader
```

---

## Prerequisites

- **Java Development Kit (JDK)**: Version 11 or higher
- **Apache Maven**: Version 3.6.0 or higher
- **IDE** (Optional): IntelliJ IDEA, Eclipse, or VS Code

## Setup Instructions

### 1. Clone or Download the Project

If using git:
```bash
git clone https://github.com/Anoopreddy123/building-challenge-java.git
cd building-challenge-java
```

### 2. Build the Project

Compile the project and download dependencies:

```bash
mvn clean compile
```

### 3. Run Tests

Execute all unit tests:

```bash
mvn test
```

### 4. Package the Application

Create a JAR file:

```bash
mvn package
```

The JAR file will be created in the `target/` directory.

---

## Assignment 1: Producer-Consumer Pattern

### Overview

This assignment implements a classic producer-consumer pattern demonstrating thread synchronization and communication. The program simulates concurrent data transfer between:
- **Producer Thread**: Reads from a source container and places items into a shared queue
- **Consumer Thread**: Reads from the queue and stores items in a destination container

### Key Features

- Thread-safe blocking queue implementation
- Bounded buffer with capacity control
- Wait/Notify mechanism for thread coordination
- Thread synchronization using synchronized blocks
- Graceful thread interruption handling

### Classes

1. **SharedQueue<T>**: Thread-safe blocking queue with bounded capacity
   - `put(T item)`: Adds item to queue (blocks if full)
   - `take()`: Removes item from queue (blocks if empty)
   - Uses wait/notify for thread coordination

2. **Producer**: Runnable implementation that produces items
   - Reads from source container
   - Places items in shared queue

3. **Consumer**: Runnable implementation that consumes items
   - Reads from shared queue
   - Stores items in destination container

4. **ProducerConsumerDemo**: Main application class
   - Coordinates producer and consumer threads
   - Validates data integrity

### Running Assignment 1

```bash

cd src/main/java
javac com/buildingchallenge/assignment1/*.java
java com.buildingchallenge.assignment1.ProducerConsumerDemo

```

### Expected Output

```========================================
Producer-Consumer Pattern Demo
========================================

Source container prepared with 10 items
Starting threads...

Consumer [Consumer-1] started
Producer [Producer-1] started
Queue is empty. Consumer waiting...
Produced: Item-1 | Queue size: 1
Produced: Item-2 | Queue size: 2
Produced: Item-3 | Queue size: 3
Produced: Item-4 | Queue size: 4
Produced: Item-5 | Queue size: 5
Queue is full. Producer waiting...
Consumed: Item-1 | Queue size: 4
Consumed: Item-2 | Queue size: 3
Consumed: Item-3 | Queue size: 2
Consumed: Item-4 | Queue size: 1
Consumed: Item-5 | Queue size: 0
Produced: Item-6 | Queue size: 1
Produced: Item-7 | Queue size: 2
Produced: Item-8 | Queue size: 3
Produced: Item-9 | Queue size: 4
Produced: Item-10 | Queue size: 5
Consumed: Item-6 | Queue size: 4
Consumed: Item-7 | Queue size: 3
Consumed: Item-8 | Queue size: 2
Consumed: Item-9 | Queue size: 1
Consumed: Item-10 | Queue size: 0
Producer [Producer-1] finished producing 10 items
Consumer [Consumer-1] finished consuming 10 items

========================================
Threads completed
========================================

Validation Results:
Source items count: 10
Destination items count: 10
All items were successfully consumed
All items are present in destination

Destination container contents:
  - Item-1
  - Item-2
  - Item-3
  - Item-4
  - Item-5
  - Item-6
  - Item-7
  - Item-8
  - Item-9
  - Item-10
```

---

## Assignment 2: CSV Data Analysis with Functional Programming

### Overview

This assignment demonstrates proficiency with Java Streams API and functional programming paradigms by performing various data analysis operations on sales data provided in CSV format. The program reads data from a CSV file and executes multiple analytical queries using functional programming concepts including lambda expressions, method references, and stream operations.

### Key Features

- CSV file parsing and data loading
- Multiple aggregation operations using Streams API
- Grouping operations by various fields (category, region)
- Functional programming paradigms (lambda expressions, method references)
- Data aggregations (sum, count, grouping)
- Complex data transformations
- Ranking operations (top N products, top N sales reps)
- Date range filtering

### CSV File Format

The CSV file should have the following structure:

```csv
ProductID,ProductName,Category,SaleDate,Amount,Quantity,Region,SalesRep
P001,Laptop,Electronics,2024-01-15,1000.00,2,North,John
P002,Mouse,Electronics,2024-01-20,30.00,5,South,Jane
...
```

### Classes

1. **SalesRecord**: Data model representing a sales transaction
   - Immutable value object
   - Contains all sales attributes (product, category, date, amount, quantity, region, sales rep)
   - Calculated properties (total value = amount × quantity)

2. **CSVDataReader**: Utility class for reading and parsing CSV files
   - Loads sales data from CSV file
   - Parses CSV rows into SalesRecord objects
   - Handles invalid rows gracefully

3. **SalesAnalyzerUtil**: Interface defining sales analysis contract
   - Defines method signatures for all query operations
   - Provides loose coupling through interface

4. **SalesAnalyzerImpl**: Implementation of sales analysis using Stream API
   - Implements all query methods using functional programming
   - Uses Stream API operations (map, filter, reduce, collect, groupingBy)
   - Demonstrates lambda expressions and method references

5. **SalesAnalysisDemo**: Main application class
   - Demonstrates all analytical queries
   - Displays results to console

### Available Analysis Methods

- `getTotalSales()`: Total revenue across all sales records
- `getSalesByCategory()`: Revenue grouped by product category
- `getSalesCountByRegion()`: Number of sales grouped by region
- `getTopProductsBySales(n)`: Top N products by total revenue
- `getTopSalesReps(n)`: Top N sales representatives by revenue
- `getProductCountByCategory()`: Product count grouped by category
- `getSalesByDateRange(start, end)`: Sales filtered by date range

### Running Assignment 2

```bash
# Using Maven (with default CSV file)
mvn exec:java -Dexec.mainClass="com.buildingchallenge.assignment2.SalesAnalysisDemo"

# Or compile and run directly
cd src/main/java
javac -cp "$(mvn dependency:build-classpath -q -DincludeScope=compile):." com/buildingchallenge/assignment2/*.java
java -cp "$(mvn dependency:build-classpath -q -DincludeScope=compile):." com.buildingchallenge.assignment2.SalesAnalysisDemo
```

### Expected Output

```
========================================
Sales Data Analysis Demo
========================================

Loading sales data from: src/main/resources/sales_data.csv
Loaded 30 sales records

========================================
ANALYSIS RESULTS
========================================

1. TOTAL REVENUE
----------------
Total Revenue: $41181.65

2. REVENUE BY CATEGORY
----------------------
  Electronics         : $  34218.96
  Furniture           : $   5608.74
  Office Supplies     : $   1353.95

3. SALES COUNT BY REGION
------------------------
  North               : 9 sales
  East                : 8 sales
  South               : 7 sales
  West                : 6 sales

4. TOP 5 PRODUCTS BY REVENUE
----------------------------
  1. Smartphone                    : $   9599.88
  2. Laptop Computer               : $   8999.91
  3. Tablet                        : $   5849.87
  4. Monitor                       : $   3599.88
  5. Office Chair                  : $   2999.85

5. TOP 5 SALES REPRESENTATIVES
------------------------------
  1. John Smith                    : $  15054.21
  2. Mike Johnson                  : $  14509.28
  3. Jane Doe                      : $   6668.79
  4. Sarah Wilson                  : $   4949.37

6. PRODUCT COUNT BY CATEGORY
----------------------------
  Electronics         : 17 products
  Furniture           : 8 products
  Office Supplies     : 5 products

7. SALES IN DATE RANGE (2024-01-01 to 2024-03-31)
--------------------------------------------------
Number of Sales: 10
Total Revenue: $15473.46

========================================
Analysis Complete!
========================================
```

---

## Running Tests

### Run All Tests

```bash
mvn test
```

### Run Tests for Specific Assignment

```bash
# Assignment 1 tests only
mvn test -Dtest=com.buildingchallenge.assignment1.*

# Assignment 2 tests only
mvn test -Dtest=com.buildingchallenge.assignment2.*
```

### Run Individual Test Classes

```bash
# Run SharedQueueTest
mvn test -Dtest=SharedQueueTest

# Run SalesQueryAnalyzerTest
mvn test -Dtest=SalesQueryAnalyzerTest

# Run CSVDataReaderTest
mvn test -Dtest=CSVDataReaderTest
```

### Generate Test Coverage Report

```bash
mvn test jacoco:report
```

---

## SDLC Documentation

### Software Development Life Cycle (SDLC) Phases

This project follows standard SDLC practices across all phases:

#### 1. **Planning Phase**
- Requirements analysis from challenge instructions
- Technology stack selection (Java 11, Maven, JUnit 5)
- Project structure planning

#### 2. **Analysis Phase**
- Understanding requirements:
  - Assignment 1: Thread synchronization and concurrent programming
  - Assignment 2: Functional programming and Stream operations
- Identifying key design patterns and best practices

#### 3. **Design Phase**
- **Assignment 1 Design**:
  - Producer-Consumer pattern architecture
  - Thread-safe data structure design
  - Synchronization mechanism design (wait/notify)
  
- **Assignment 2 Design**:
  - Data model design (SalesRecord)
  - Interface-based design (SalesAnalyzerUtil)
  - Implementation with Stream API operations (SalesAnalyzerImpl)
  - CSV reading service design (CSVDataReader)

#### 4. **Implementation Phase**
- **Code Quality Practices**:
  - Comprehensive code comments explaining purpose, functionality, and SDLC phase
  - Clear method documentation with JavaDoc
  - Proper exception handling
  - Input validation
  - Meaningful variable and method names

- **Assignment 1 Implementation**:
  - `SharedQueue`: Thread-safe blocking queue with bounded capacity
  - `Producer`: Thread implementation for producing items
  - `Consumer`: Thread implementation for consuming items
  - `ProducerConsumerDemo`: Main application coordinating threads

- **Assignment 2 Implementation**:
  - `SalesRecord`: Immutable data model class
  - `CSVDataReader`: CSV file reader and parser
  - `SalesAnalyzerUtil`: Interface for sales analysis operations
  - `SalesAnalyzerImpl`: Implementation with Stream API operations
  - `SalesAnalysisDemo`: Main application demonstrating analyses

#### 5. **Testing Phase**
- **Unit Testing**:
  - Comprehensive unit tests for all classes
  - Test coverage for edge cases
  - Thread safety testing
  - Integration testing

- **Test Classes**:
  - `SharedQueueTest`: Tests thread safety and blocking behavior
  - `ProducerConsumerTest`: Integration tests for producer-consumer pattern
  - `SalesQueryAnalyzerTest`: Tests all query operations (aggregation, grouping, ranking, filtering)
  - `CSVDataReaderTest`: Tests CSV file reading and parsing

#### 6. **Deployment Phase**
- Build configuration (Maven `pom.xml`)
- Dependency management
- Documentation (README)
- Sample data files

#### 7. **Maintenance Phase**
- Code is maintainable with clear structure
- Comprehensive comments for future developers
- Well-documented public APIs

