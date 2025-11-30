package com.buildingchallenge.assignment2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * SalesAnalyzerUtil Interface
 * 
 * Purpose: Defines the contract for analyzing sales data.
 * Contains only method declarations (function names).
 * 
 * query1: Get the total sales.
 * query2: Get the sales records.
 * query3: Get the sales by category.
 * query4: Get the sales count by region.
 * query5: Get the top products by sales.
 * query6: Get the top sales reps by revenue.
 * query7: Get the product count by category.
 * query8: Get the sales by date range.
 * 
 */
public interface SalesAnalyzerUtil {
    
    List<SalesRecord> getSalesRecords();
    BigDecimal getTotalSales();
    Map<String, BigDecimal> getSalesByCategory();
    Map<String, Long> getSalesCountByRegion();
    Map<String, BigDecimal> getTopProductsBySales(int n);
    Map<String, BigDecimal> getTopSalesReps(int n);
    Map<String, Long> getProductCountByCategory();
    List<SalesRecord> getSalesByDateRange(LocalDate startDate, LocalDate endDate);
}
