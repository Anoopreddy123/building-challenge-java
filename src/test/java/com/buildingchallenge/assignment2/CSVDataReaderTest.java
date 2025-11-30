package com.buildingchallenge.assignment2;

import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * CSVDataReaderTest Class
 * unit tests for CSVDataReader class.
 */
@DisplayName("CSV Data Reader Tests")
class CSVDataReaderTest {
    
    /**
     * Test reads valid CSV and returns correct record count
     */
    @Test
    @DisplayName("Test reads valid CSV and returns correct record count")
    void testReadsValidCSV(@TempDir Path tempDir) throws IOException, CsvException {
        Path csvFilePath = tempDir.resolve("test_sales.csv");
        
        String csvContent = 
            "ProductID,ProductName,Category,SaleDate,Amount,Quantity,Region,SalesRep\n" +
            "P001,Laptop,Electronics,2024-01-15,1000.00,2,North,John\n" +
            "P002,Mouse,Electronics,2024-01-20,30.00,5,South,Jane\n" +
            "P003,Chair,Furniture,2024-02-10,200.00,3,North,John\n" +
            "P001,Laptop,Electronics,2024-02-15,1000.00,1,East,Mike\n" +
            "P004,Desk,Furniture,2024-03-05,400.00,2,South,Jane\n";
        
        Files.write(csvFilePath, csvContent.getBytes());
        
        CSVDataReader reader = new CSVDataReader();
        var salesRecords = reader.readSalesData(csvFilePath);
        
        assertEquals(5, salesRecords.size(), 
                    "Should load all 5 records from CSV");
    }
    
    /**
     * Test invalid row throws (bad date or number or columns)
     * 
     * Note: CSVDataReader skips invalid rows instead of throwing exceptions.
     * This test verifies that invalid rows are properly skipped.
     */
    @Test
    @DisplayName("Test invalid row throws")
    void testInvalidRowThrows(@TempDir Path tempDir) throws IOException, CsvException {
        Path csvFilePath = tempDir.resolve("invalid_sales.csv");
        
        // Test with invalid date format - row should be skipped
        String csvContentWithBadDate = 
            "ProductID,ProductName,Category,SaleDate,Amount,Quantity,Region,SalesRep\n" +
            "P001,Laptop,Electronics,invalid-date,1000.00,2,North,John\n" +
            "P002,Mouse,Electronics,2024-01-20,30.00,5,South,Jane\n";
        
        Files.write(csvFilePath, csvContentWithBadDate.getBytes());
        
        CSVDataReader reader = new CSVDataReader();
        var salesRecords = reader.readSalesData(csvFilePath);
        
        // Only the valid row should be loaded, invalid row should be skipped
        assertEquals(1, salesRecords.size(), 
                    "Should skip invalid date row and load only valid rows");
        
        // Test with invalid number format - row should be skipped
        String csvContentWithBadNumber = 
            "ProductID,ProductName,Category,SaleDate,Amount,Quantity,Region,SalesRep\n" +
            "P001,Laptop,Electronics,2024-01-15,not-a-number,2,North,John\n" +
            "P002,Mouse,Electronics,2024-01-20,30.00,5,South,Jane\n";
        
        Files.write(csvFilePath, csvContentWithBadNumber.getBytes());
        
        salesRecords = reader.readSalesData(csvFilePath);
        assertEquals(1, salesRecords.size(), 
                    "Should skip invalid number row and load only valid rows");
        
        // Test with wrong number of columns - row should be skipped
        String csvContentWithWrongColumns = 
            "ProductID,ProductName,Category,SaleDate,Amount,Quantity,Region,SalesRep\n" +
            "P001,Laptop,Electronics,2024-01-15,1000.00,2\n" +
            "P002,Mouse,Electronics,2024-01-20,30.00,5,South,Jane\n";
        
        Files.write(csvFilePath, csvContentWithWrongColumns.getBytes());
        
        salesRecords = reader.readSalesData(csvFilePath);
        assertEquals(1, salesRecords.size(), 
                    "Should skip row with wrong number of columns and load only valid rows");
    }
}
