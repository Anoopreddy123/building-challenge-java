package com.buildingchallenge.assignment2;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CSVDataReader Class
 * Purpose: Reads and parses sales data from a CSV file, returning a list of
 * SalesRecord objects. This class handles all CSV file reading and parsing operations.
 * 
 */
public class CSVDataReader {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    
    /**
     * Reads sales data from CSV file and returns list of SalesRecord objects
     * 
     * @param csvFilePath Path to the CSV file
     * @return List of parsed SalesRecord objects
     * @throws IOException if file cannot be read
     * @throws CsvException if CSV parsing fails
     */
    public List<SalesRecord> readSalesData(Path csvFilePath) throws IOException, CsvException {

        // List to store the rows of the CSV file as SalesRecord objects.
        List<SalesRecord> records = new ArrayList<>();
        
        // Read the CSV file and parse the sales records.
        try (Reader reader = Files.newBufferedReader(csvFilePath);
        
        // Builder pattern to build CSVReader.
             CSVReader csvReader = new CSVReaderBuilder(reader)
                 .withSkipLines(1) // Skip header row
                 .build()) {
            
            List<String[]> rows = csvReader.readAll();
            
            for (int i = 0; i < rows.size(); i++) {
                String[] row = rows.get(i);
                try {
                    SalesRecord record = parseRow(row);
                    records.add(record);
                } catch (Exception e) {
                    System.err.println("Warning: Skipping invalid row " + (i + 2) + ": " + e.getMessage());
                }
            }
        }
        
        // Retunring unmodifiable list to prevent modification of the data present in the list
        return Collections.unmodifiableList(records);
    }
    
    /**
     * Reads sales data from InputStream (e.g., from classpath) and returns list of SalesRecord objects
     * 
     * This method allows reading CSV data from resources packaged in the application,
     * making it work regardless of the current working directory or when packaged as a JAR.
     * 
     * @param inputStream InputStream containing CSV data
     * @return List of parsed SalesRecord objects
     * @throws IOException if stream cannot be read
     * @throws CsvException if CSV parsing fails
     */
    public List<SalesRecord> readSalesData(InputStream inputStream) throws IOException, CsvException {
        List<SalesRecord> records = new ArrayList<>();
        
        try (Reader reader = new InputStreamReader(inputStream);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                 .withSkipLines(1) // Skip header row
                 .build()) {
            
            List<String[]> rows = csvReader.readAll();
            
            for (int i = 0; i < rows.size(); i++) {
                String[] row = rows.get(i);
                try {
                    SalesRecord record = parseRow(row);
                    records.add(record);
                } catch (Exception e) {
                    System.err.println("Warning: Skipping invalid row " + (i + 2) + ": " + e.getMessage());
                }
            }
        }
        
        return Collections.unmodifiableList(records);
    }
    
    /**
     * Parses a CSV row into a SalesRecord object
     * 
     * Format: ProductID,ProductName,Category,SaleDate,Amount,Quantity,Region,SalesRep
     * 
     * @param row CSV row as string array
     * @return Parsed SalesRecord
     */
    private SalesRecord parseRow(String[] row) {
        if (row.length < 8) {
            throw new IllegalArgumentException("Row must have at least 8 columns");
        }
        
        String productId = row[0];
        String productName = row[1];
        String category = row[2];
        
        LocalDate saleDate = LocalDate.parse(row[3], DATE_FORMATTER);
        BigDecimal amount = new BigDecimal(row[4]).setScale(2, RoundingMode.HALF_UP);
        int quantity = Integer.parseInt(row[5]);
        String region = row[6];
        String salesRep = row[7];
        
        return new SalesRecord(productId, productName, category, saleDate, 
                              amount, quantity, region, salesRep);
    }
}
