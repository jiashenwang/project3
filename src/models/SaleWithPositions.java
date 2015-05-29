/**
 * 
 */
package models;


/**
 * @author julestestard
 */
public class SaleWithPositions {

    private int id;

    private int uid;

    private int pid;

    private int quantity;

    private int price;

    private int x;

    private int y;

    /**
     * @param id
     * @param uid
     * @param pid
     * @param quantity
     * @param price
     */
    public SaleWithPositions(int id, int uid, int pid, int quantity, int price, int x, int y) {
        super();
        this.id = id;
        this.uid = uid;
        this.pid = pid;
        this.quantity = quantity;
        this.price = price;
        this.x = x;
        this.y = y;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * @param uid
     *            the uid to set
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * @param pid
     *            the pid to set
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     *            the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

}
