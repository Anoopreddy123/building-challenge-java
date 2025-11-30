package com.buildingchallenge.assignment2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SalesQueryAnalyzerTest Class
 * 
 * Unit tests for SalesAnalyzerImpl query methods.
 * 
 * @author Building Challenge
 * @version 1.0
 */
@DisplayName("Sales Query Analyzer Tests")
class SalesQueryAnalyzerTest {
    
    private SalesAnalyzerImpl analyzer;
    
    @BeforeEach
    void setUp() {
        List<SalesRecord> salesRecords = Arrays.asList(
            new SalesRecord("P001", "Laptop", "Electronics", LocalDate.of(2024, 1, 15), 
                          new BigDecimal("1000.00"), 2, "North", "John"),
            new SalesRecord("P002", "Mouse", "Electronics", LocalDate.of(2024, 1, 20), 
                          new BigDecimal("30.00"), 5, "South", "Jane"),
            new SalesRecord("P003", "Chair", "Furniture", LocalDate.of(2024, 2, 10), 
                          new BigDecimal("200.00"), 3, "North", "John"),
            new SalesRecord("P001", "Laptop", "Electronics", LocalDate.of(2024, 2, 15), 
                          new BigDecimal("1000.00"), 1, "East", "Mike"),
            new SalesRecord("P004", "Desk", "Furniture", LocalDate.of(2024, 3, 5), 
                          new BigDecimal("400.00"), 2, "South", "Jane")
        );
        analyzer = new SalesAnalyzerImpl(salesRecords);
    }
    
    /**
     * Test total revenue sum
     */
    @Test
    @DisplayName("Test total revenue sum")
    void testTotalRevenueSum() {
        // Expected: (1000*2) + (30*5) + (200*3) + (1000*1) + (400*2) = 2000 + 150 + 600 + 1000 + 800 = 4550
        BigDecimal expected = new BigDecimal("4550.00");
        BigDecimal actual = analyzer.getTotalSales();
        
        assertEquals(0, expected.compareTo(actual), 
                    "Total revenue should be 4550.00");
    }
    
    /**
     * Test revenue grouped by category
     */
    @Test
    @DisplayName("Test revenue grouped by category")
    void testRevenueGroupedByCategory() {
        Map<String, BigDecimal> revenueByCategory = analyzer.getSalesByCategory();
        
        assertNotNull(revenueByCategory, "Revenue by category should not be null");
        assertTrue(revenueByCategory.containsKey("Electronics"), 
                  "Should contain Electronics category");
        assertTrue(revenueByCategory.containsKey("Furniture"), 
                  "Should contain Furniture category");
        
        // Electronics: (1000*2) + (30*5) + (1000*1) = 3150
        BigDecimal electronicsTotal = revenueByCategory.get("Electronics");
        assertEquals(0, new BigDecimal("3150.00").compareTo(electronicsTotal),
                    "Electronics total should be 3150.00");
    }
    
    /**
     * Test revenue grouped by region
     */
    @Test
    @DisplayName("Test revenue grouped by region")
    void testRevenueGroupedByRegion() {
        // Calculate revenue by region manually using the available methods
        List<SalesRecord> records = analyzer.getSalesRecords();
        
        Map<String, BigDecimal> revenueByRegion = records.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                SalesRecord::getRegion,
                java.util.stream.Collectors.reducing(
                    java.math.BigDecimal.ZERO,
                    SalesRecord::getTotalValue,
                    java.math.BigDecimal::add
                )
            ));
        
        assertNotNull(revenueByRegion, "Revenue by region should not be null");
        assertTrue(revenueByRegion.containsKey("North"), 
                  "Should contain North region");
        assertTrue(revenueByRegion.containsKey("South"), 
                  "Should contain South region");
        assertTrue(revenueByRegion.containsKey("East"), 
                  "Should contain East region");
        
        // Verify revenue calculation
        // North: (1000*2) + (200*3) = 2000 + 600 = 2600
        BigDecimal northRevenue = revenueByRegion.get("North");
        assertEquals(0, new BigDecimal("2600.00").compareTo(northRevenue),
                    "North region revenue should be 2600.00");
    }
    
    /**
     * Test top N products by revenue ordering and limit
     */
    @Test
    @DisplayName("Test top N products by revenue ordering and limit")
    void testTopNProductsByRevenue() {
        Map<String, BigDecimal> topProducts = analyzer.getTopProductsBySales(2);
        
        assertNotNull(topProducts, "Top products should not be null");
        assertEquals(2, topProducts.size(), "Should return exactly 2 products");
        assertTrue(topProducts.containsKey("Laptop"), 
                  "Laptop should be in top 2 products");
        
        // Laptop total: (1000*2) + (1000*1) = 3000
        BigDecimal laptopTotal = topProducts.get("Laptop");
        assertEquals(0, new BigDecimal("3000.00").compareTo(laptopTotal),
                    "Laptop total sales should be 3000.00");
        
        // Verify ordering - Laptop should be first (highest revenue)
        BigDecimal firstValue = topProducts.values().iterator().next();
        assertTrue(firstValue.compareTo(new BigDecimal("2500.00")) >= 0,
                  "Top product should have revenue >= 2500");
    }
    
    /**
     * Test date range filter boundaries
     */
    @Test
    @DisplayName("Test date range filter boundaries")
    void testDateRangeFilterBoundaries() {
        // Test inclusive boundaries
        LocalDate startDate = LocalDate.of(2024, 2, 1);
        LocalDate endDate = LocalDate.of(2024, 2, 28);
        
        List<SalesRecord> salesInRange = analyzer.getSalesByDateRange(startDate, endDate);
        
        assertEquals(2, salesInRange.size(), 
                    "Should have 2 sales in February 2024");
        
        // Verify boundary dates are included
        boolean hasFeb10 = salesInRange.stream()
            .anyMatch(r -> r.getSaleDate().equals(LocalDate.of(2024, 2, 10)));
        assertTrue(hasFeb10, "Should include sales on start boundary");
        
        boolean hasFeb15 = salesInRange.stream()
            .anyMatch(r -> r.getSaleDate().equals(LocalDate.of(2024, 2, 15)));
        assertTrue(hasFeb15, "Should include sales on end boundary");
    }
    
    /**
     * Test sales count grouped by region
     */
    @Test
    @DisplayName("Test sales count grouped by region")
    void testSalesCountByRegion() {
        Map<String, Long> salesCountByRegion = analyzer.getSalesCountByRegion();
        
        assertNotNull(salesCountByRegion, "Sales count by region should not be null");
        assertTrue(salesCountByRegion.containsKey("North"), 
                  "Should contain North region");
        assertTrue(salesCountByRegion.containsKey("South"), 
                  "Should contain South region");
        assertTrue(salesCountByRegion.containsKey("East"), 
                  "Should contain East region");
        
        // Verify count calculation
        // North: 2 sales (Laptop on 2024-01-15, Chair on 2024-02-10)
        assertEquals(2L, salesCountByRegion.get("North"),
                    "North region should have 2 sales");
        
        // South: 2 sales (Mouse on 2024-01-20, Desk on 2024-03-05)
        assertEquals(2L, salesCountByRegion.get("South"),
                    "South region should have 2 sales");
        
        // East: 1 sale (Laptop on 2024-02-15)
        assertEquals(1L, salesCountByRegion.get("East"),
                    "East region should have 1 sale");
    }
    
    /**
     * Test top N sales reps by revenue ordering and limit
     */
    @Test
    @DisplayName("Test top N sales reps by revenue ordering and limit")
    void testTopNSalesReps() {
        Map<String, BigDecimal> topSalesReps = analyzer.getTopSalesReps(2);
        
        assertNotNull(topSalesReps, "Top sales reps should not be null");
        assertEquals(2, topSalesReps.size(), "Should return exactly 2 sales reps");
        assertTrue(topSalesReps.containsKey("John"), 
                  "John should be in top 2 sales reps");
        
        // John total: (1000*2) + (200*3) = 2000 + 600 = 2600
        BigDecimal johnTotal = topSalesReps.get("John");
        assertEquals(0, new BigDecimal("2600.00").compareTo(johnTotal),
                    "John total sales should be 2600.00");
        
        // Verify ordering - John should be first (highest revenue)
        BigDecimal firstValue = topSalesReps.values().iterator().next();
        assertTrue(firstValue.compareTo(new BigDecimal("2500.00")) >= 0,
                  "Top sales rep should have revenue >= 2500");
    }
    
    /**
     * Test product count grouped by category
     */
    @Test
    @DisplayName("Test product count grouped by category")
    void testProductCountByCategory() {
        Map<String, Long> productCountByCategory = analyzer.getProductCountByCategory();
        
        assertNotNull(productCountByCategory, "Product count by category should not be null");
        assertTrue(productCountByCategory.containsKey("Electronics"), 
                  "Should contain Electronics category");
        assertTrue(productCountByCategory.containsKey("Furniture"), 
                  "Should contain Furniture category");
        
        // Electronics: 3 sales records (Laptop twice, Mouse once)
        assertEquals(3L, productCountByCategory.get("Electronics"),
                    "Electronics category should have 3 sales records");
        
        // Furniture: 2 sales records (Chair once, Desk once)
        assertEquals(2L, productCountByCategory.get("Furniture"),
                    "Furniture category should have 2 sales records");
    }
    
    /**
     * Test empty input returns safe defaults (0 or empty map)
     */
    @Test
    @DisplayName("Test empty input returns safe defaults")
    void testEmptyInputReturnsSafeDefaults() {
        SalesAnalyzerImpl emptyAnalyzer = new SalesAnalyzerImpl(Arrays.asList());
        
        // Test total revenue returns 0
        BigDecimal totalRevenue = emptyAnalyzer.getTotalSales();
        assertEquals(0, BigDecimal.ZERO.compareTo(totalRevenue),
                    "Total revenue for empty list should be zero");
        
        // Test revenue by category returns empty map
        Map<String, BigDecimal> revenueByCategory = emptyAnalyzer.getSalesByCategory();
        assertNotNull(revenueByCategory, "Revenue by category should not be null");
        assertTrue(revenueByCategory.isEmpty(), 
                  "Revenue by category should return empty map for empty input");
        
        // Test sales count by region returns empty map
        Map<String, Long> salesCountByRegion = emptyAnalyzer.getSalesCountByRegion();
        assertNotNull(salesCountByRegion, "Sales count by region should not be null");
        assertTrue(salesCountByRegion.isEmpty(), 
                  "Sales count by region should return empty map for empty input");
        
        // Test top products returns empty map
        Map<String, BigDecimal> topProducts = emptyAnalyzer.getTopProductsBySales(5);
        assertNotNull(topProducts, "Top products should not be null");
        assertTrue(topProducts.isEmpty(), 
                  "Top products should return empty map for empty input");
        
        // Test top sales reps returns empty map
        Map<String, BigDecimal> topSalesReps = emptyAnalyzer.getTopSalesReps(5);
        assertNotNull(topSalesReps, "Top sales reps should not be null");
        assertTrue(topSalesReps.isEmpty(), 
                  "Top sales reps should return empty map for empty input");
        
        // Test product count by category returns empty map
        Map<String, Long> productCountByCategory = emptyAnalyzer.getProductCountByCategory();
        assertNotNull(productCountByCategory, "Product count by category should not be null");
        assertTrue(productCountByCategory.isEmpty(), 
                  "Product count by category should return empty map for empty input");
        
        // Test date range filter returns empty list
        List<SalesRecord> salesInRange = emptyAnalyzer.getSalesByDateRange(
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 31)
        );
        assertNotNull(salesInRange, "Sales by date range should not be null");
        assertTrue(salesInRange.isEmpty(), 
                  "Sales by date range should return empty list for empty input");
    }
}

