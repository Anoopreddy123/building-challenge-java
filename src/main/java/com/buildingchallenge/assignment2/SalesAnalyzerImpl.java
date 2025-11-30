package com.buildingchallenge.assignment2;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SalesAnalyzerImpl Class
 * 
 * Implementation of SalesAnalyzerUtil interface.
 * Contains all declarations and implementation details including
 * all query logic using Java Streams API.
 *
 */
public class SalesAnalyzerImpl implements SalesAnalyzerUtil {
    
    private final List<SalesRecord> salesRecords;
    
    /**
     * Constructor - Creates analyzer and loads data from CSV file
     * 
     * @param csvFilePath Path to CSV file
     * @throws IOException if file cannot be read
     * @throws CsvException if CSV parsing fails
     */
    public SalesAnalyzerImpl(Path csvFilePath) throws IOException, CsvException {
        CSVDataReader dataReader = new CSVDataReader();
        this.salesRecords = dataReader.readSalesData(csvFilePath);
    }
    
    /**
     * Constructor - Creates analyzer and loads data from InputStream (e.g., from classpath)
     * 
     * This constructor allows loading CSV data from resources packaged in the application,
     * making it work regardless of the current working directory or when packaged as a JAR.
     * 
     * @param inputStream InputStream containing CSV data
     * @throws IOException if stream cannot be read
     * @throws CsvException if CSV parsing fails
     */
    public SalesAnalyzerImpl(InputStream inputStream) throws IOException, CsvException {
        CSVDataReader dataReader = new CSVDataReader();
        this.salesRecords = dataReader.readSalesData(inputStream);
    }
    
    /**
     * Package-private constructor for testing - Creates analyzer with provided sales records
     * 
     * @param salesRecords List of sales records to use
     */
    SalesAnalyzerImpl(List<SalesRecord> salesRecords) {
        this.salesRecords = salesRecords;
    }
    
    // Query1: Get the sales records.
    @Override
    public List<SalesRecord> getSalesRecords() {
        return salesRecords;
    }
    
    // Query2: Get the total sales.
    @Override
    public BigDecimal getTotalSales() {
        return salesRecords.stream()
            .map(SalesRecord::getTotalValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Query3: Get the sales by category.
    @Override
    public Map<String, BigDecimal> getSalesByCategory() {
        return salesRecords.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getCategory,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    SalesRecord::getTotalValue,
                    BigDecimal::add
                )
            ));
    }
    
    // Query4: Get the sales count by region.
    @Override
    public Map<String, Long> getSalesCountByRegion() {
        return salesRecords.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getRegion,
                Collectors.counting()
            ));
    }
    
    // Query5: Get the top products by sales.
    @Override
    public Map<String, BigDecimal> getTopProductsBySales(int n) {
        Map<String, BigDecimal> totals = salesRecords.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getProductName,
                Collectors.mapping(SalesRecord::getTotalValue,
                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
            ));

        return totals.entrySet().stream()
            .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
            .limit(n)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a,
                LinkedHashMap::new
            ));
    }
    
    // Query6: Get the top sales reps by revenue.
    @Override
    public Map<String, BigDecimal> getTopSalesReps(int n) {
        return salesRecords.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getSalesRep,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    SalesRecord::getTotalValue,
                    BigDecimal::add
                )
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
            .limit(n)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }
    
    // Query7: Get the product count by category.
    @Override
    public Map<String, Long> getProductCountByCategory() {
        return salesRecords.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getCategory,
                Collectors.counting()
            ));
    }
    
    // Query8: Get the sales by date range.
    @Override
    public List<SalesRecord> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesRecords.stream()
            .filter(record -> !record.getSaleDate().isBefore(startDate) &&
                            !record.getSaleDate().isAfter(endDate))
            .collect(Collectors.toList());
    }
}
