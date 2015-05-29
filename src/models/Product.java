/**
 * 
 */
package models;


/**
 * @author Jules Testard
 */
public class Product {

    private int id;

    private int cid;

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
    public Product(int id, int cid, String name, String sKU, int price) {
        this.id = id;
        this.cid = cid;
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

    /**
     * @return the cid
     */
    public int getCid() {
        return cid;
    }

}
