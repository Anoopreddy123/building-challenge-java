package com.buildingchallenge.assignment2;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * SalesAnalysisDemo Class
 * 
 * Main application class that demonstrates CSV data analysis using
 * Java Streams API and functional programming paradigms. Uses SalesAnalyzerImpl
 * to load data and perform queries, then prints all results to console.
 * @author Building Challenge
 * @version 1.0
 */
public class SalesAnalysisDemo {
    
    /**
     * Main method - Entry point of the application
     * 
     * This method:
     * 1. Uses CSVDataReader to load sales data from CSV file
     * 2. Uses SalesAnalyzerImpl methods to perform analytical queries
     * 3. Displays all results to console
     * 
     * @param args Command-line arguments (optional: path to CSV file)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Sales Data Analysis Demo");
        System.out.println("========================================\n");
        
        try {
            Path csvFilePath;
            if (args.length > 0) {
                csvFilePath = Paths.get(args[0]);
            } else {
                // Default to sample data file in resources
                csvFilePath = Paths.get("src/main/resources/sales_data.csv");
            }
            
            System.out.println("Loading sales data from: " + csvFilePath);
            
            // Step 1: Create analyzer which loads data from CSV
            SalesAnalyzerImpl analyzer = new SalesAnalyzerImpl(csvFilePath);
            
            System.out.println("Loaded " + analyzer.getSalesRecords().size() + " sales records\n");
            
            // Step 2: Perform various analyses using SalesAnalyzerImpl methods
            performAnalyses(analyzer);
            
        } catch (IOException | CsvException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Performs analytical queries and displays results
     * 
     * Demonstrates various Stream API operations:
     * - Aggregation (total revenue)
     * - Grouping (by category, by region)
     * - Ranking (top products, top sales reps)
     * - Counting (product count per category)
     * - Filtering (by date range)
     * 
     * @param analyzer Sales analyzer instance with loaded data
     */
    private static void performAnalyses(SalesAnalyzerImpl analyzer) {
        System.out.println("========================================");
        System.out.println("ANALYSIS RESULTS");
        System.out.println("========================================\n");
        
        // 1. Total Revenue
        System.out.println("1. TOTAL REVENUE");
        System.out.println("----------------");
        BigDecimal totalRevenue = analyzer.getTotalSales();
        System.out.println("Total Revenue: $" + totalRevenue);
        System.out.println();
        
        // 2. Revenue by Category
        System.out.println("2. REVENUE BY CATEGORY");
        System.out.println("----------------------");
        Map<String, BigDecimal> revenueByCategory = analyzer.getSalesByCategory();
        // 20 characters wide and 10.2f precision
        revenueByCategory.entrySet().stream()
            .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.printf("  %-20s: $%10.2f%n", entry.getKey(), entry.getValue())
            );
        System.out.println();
        
        // 3. Sales Count by Region
        System.out.println("3. SALES COUNT BY REGION");
        System.out.println("------------------------");
        Map<String, Long> salesCountByRegion = analyzer.getSalesCountByRegion();
        salesCountByRegion.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.printf("  %-20s: %d sales%n", entry.getKey(), entry.getValue())
            );
        System.out.println();
        
        // 4. Top 5 Products by Revenue
        System.out.println("4. TOP 5 PRODUCTS BY REVENUE");
        System.out.println("----------------------------");
        Map<String, BigDecimal> topProducts = analyzer.getTopProductsBySales(5);
        // 30 characters wide and 10.2f precision
        int rank = 1;
        for (Map.Entry<String, BigDecimal> entry : topProducts.entrySet()) {
            System.out.printf("  %d. %-30s: $%10.2f%n", rank++, entry.getKey(), entry.getValue());
        }
        System.out.println();
        
        // 5. Top 5 Sales Representatives
        System.out.println("5. TOP 5 SALES REPRESENTATIVES");
        System.out.println("------------------------------");
        Map<String, BigDecimal> topSalesReps = analyzer.getTopSalesReps(5);
        // 30 characters wide and 10.2f precision
        rank = 1;
        for (Map.Entry<String, BigDecimal> entry : topSalesReps.entrySet()) {
            System.out.printf("  %d. %-30s: $%10.2f%n", rank++, entry.getKey(), entry.getValue());
        }
        System.out.println();
        
        // 6. Product Count by Category
        System.out.println("6. PRODUCT COUNT BY CATEGORY");
        System.out.println("----------------------------");
        Map<String, Long> productCountByCategory = analyzer.getProductCountByCategory();
        // 20 characters wide and 10.2f precision
        productCountByCategory.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.printf("  %-20s: %d products%n", entry.getKey(), entry.getValue())
            );
        System.out.println();
        
        // 7. Sales by Date Range (Q1 2024)
        System.out.println("7. SALES IN DATE RANGE (2024-01-01 to 2024-03-31)");
        System.out.println("--------------------------------------------------");
    
        // Analyzer has a method to get sales by date range so we can use it to get the sales in Q1 2024.
        List<SalesRecord> salesInQ1 = analyzer.getSalesByDateRange(
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 3, 31)
        );
        BigDecimal q1Revenue = salesInQ1.stream()
            .map(SalesRecord::getTotalValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Number of Sales: " + salesInQ1.size());
        System.out.println("Total Revenue: $" + q1Revenue.setScale(2, RoundingMode.HALF_UP));
        System.out.println();
        
        System.out.println("========================================");
        System.out.println("Analysis Complete!");
        System.out.println("========================================");
    }
}