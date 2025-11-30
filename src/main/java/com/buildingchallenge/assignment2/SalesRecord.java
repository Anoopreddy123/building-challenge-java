package com.buildingchallenge.assignment2;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * SalesRecord Class
 * 
 * Purpose: Represents a single sales record from the CSV file.
 * This is a data model class that encapsulates all attributes of a sales transaction.
 *
 */

public class SalesRecord {
    
    private final String productId;
    private final String productName;
    private final String category;
    private final LocalDate saleDate;
    private final BigDecimal amount;
    private final int quantity;
    private final String region;
    private final String salesRep;
    
    /**
     * Constructor - Creates a new SalesRecord with all required fields
     * 
     * @param productId Unique identifier for the product
     * @param productName Name of the product
     * @param category Category to which the product belongs
     * @param saleDate Date when the sale occurred
     * @param amount Total sale amount
     * @param quantity Number of units sold
     * @param region Geographic region where sale occurred
     * @param salesRep Name of the sales representative
     */
    public SalesRecord(String productId, String productName, String category,
                      LocalDate saleDate, BigDecimal amount, int quantity,
                      String region, String salesRep) {
        // Validate required fields

        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (saleDate == null) {
            throw new IllegalArgumentException("Sale date cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-null and non-negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        
        this.productId = productId.trim();
        this.productName = productName.trim();
        this.category = category != null ? category.trim() : "Uncategorized";
        this.saleDate = saleDate;
        this.amount = amount;
        this.quantity = quantity;
        this.region = region != null ? region.trim() : "Unknown";
        this.salesRep = salesRep != null ? salesRep.trim() : "Unknown";
    }
    
    // Getters for all fields
  
    public String getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public LocalDate getSaleDate() {
        return saleDate;
    }
 
    public BigDecimal getAmount() {
        return amount;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public String getRegion() {
        return region;
    }

    public String getSalesRep() {
        return salesRep;
    }

    public BigDecimal getTotalValue() {
        return amount.multiply(BigDecimal.valueOf(quantity));
    }

    public int getYear() {
        return saleDate.getYear();
    }

    public int getMonth() {
        return saleDate.getMonthValue();
    }
 
    @Override
    public String toString() {
        return String.format("SalesRecord{productId='%s', productName='%s', " +
                           "category='%s', date=%s, amount=%.2f, quantity=%d, " +
                           "region='%s', salesRep='%s'}", 
                           productId, productName, category, saleDate, 
                           amount, quantity, region, salesRep);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        SalesRecord that = (SalesRecord) obj;
        
        return productId.equals(that.productId) &&
               saleDate.equals(that.saleDate) &&
               amount.compareTo(that.amount) == 0;
    }
    
    @Override
    public int hashCode() {
        int result = productId.hashCode();
        result = 31 * result + saleDate.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }
}
