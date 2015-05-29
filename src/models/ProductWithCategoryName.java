/**
 * 
 */
package models;

import helpers.HelperUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Jules Testard
 */
public class ProductWithCategoryName {

    private int id;

    private String categoryName;

    private String name;

    private String SKU;

    private int price;

    /**
     * @param id
     * @param cid
     * @param name
     * @param sKU
     * @param price
     */
    public ProductWithCategoryName(int id, String categoryName, String name, String sKU, int price) {
        this.id = id;
        this.categoryName = categoryName;
        this.name = name;
        SKU = sKU;
        this.price = price;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the cid
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the sKU
     */
    public String getSKU() {
        return SKU;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    public static ArrayList<ProductWithCategoryName> getProductsWithCategoryNamesWithFilter(String filter)
            throws SQLException {
        ArrayList<ProductWithCategoryName> productWithCategoryNames = new ArrayList<ProductWithCategoryName>();
        Connection conn = null;
        try {
            conn = HelperUtils.connect();
        } catch (Exception e) {
            System.err.println("Internal Server Error. This shouldn't happen.");
            return new ArrayList<ProductWithCategoryName>();
        }
        Statement stmt = conn.createStatement();
        String query = "WITH selected AS (SELECT * FROM products" + filter
                + ") SELECT s.id, c.name, s.name, s.SKU, s.price FROM selected s JOIN categories c ON s.cid = c.id";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Integer id = rs.getInt(1);
            String cname = rs.getString(2);
            String name = rs.getString(3);
            String SKU = rs.getString(4);
            Integer price = rs.getInt(5);
            productWithCategoryNames.add(new ProductWithCategoryName(id, cname, name, SKU, price));
        }
        return productWithCategoryNames;
    }
}
