package models;

import java.util.ArrayList;
import java.util.HashMap;

public class Analytics {

    private int totalUserCount;

    private int totalProductCount;

    private int totalStateCount;

    private long time_taken;

    private HashMap<Integer, Integer> productPrices = null;

    private HashMap<Integer, Integer> userPrices = null;

    private HashMap<Integer, Integer> statePrices = null;

    private HashMap<Integer, User> users = null;

    private HashMap<Integer, State> states = null;

    private HashMap<Integer, Product> products;

    private ArrayList<Integer> productPositions;

    private ArrayList<Integer> userPositions = null;

    private ArrayList<Integer> statePositions = null;

    private int prices[][];

    public Analytics() {

        this.userPrices = new HashMap<Integer, Integer>();
        this.statePrices = new HashMap<Integer, Integer>();
        this.productPrices = new HashMap<Integer, Integer>();
        this.products = new HashMap<Integer, Product>();
        this.states = new HashMap<Integer, State>();
        this.users = new HashMap<Integer, User>();
        this.userPositions = new ArrayList<Integer>();
        this.statePositions = new ArrayList<Integer>();
        this.productPositions = new ArrayList<Integer>();
        this.prices = new int[20][10];
    }

    /**
     * @param totalUserCount
     * @param totalProductCount
     * @param totalStateCount
     * @param productPrices
     * @param userPrices
     * @param statePrices
     * @param users
     * @param states
     * @param products
     * @param prices
     */
    public Analytics(int totalUserCount, int totalProductCount, int totalStateCount,
            HashMap<Integer, Integer> productPrices, HashMap<Integer, Integer> userPrices,
            HashMap<Integer, Integer> statePrices, HashMap<Integer, Product> products, HashMap<Integer, User> users,
            HashMap<Integer, State> states, ArrayList<Integer> productPositions, ArrayList<Integer> userPositions,
            ArrayList<Integer> statePositions, int[][] prices, long time_taken) {
        super();
        this.totalUserCount = totalUserCount;
        this.totalProductCount = totalProductCount;
        this.totalStateCount = totalStateCount;
        this.productPrices = productPrices;
        this.userPrices = userPrices;
        this.statePrices = statePrices;
        this.users = users;
        this.states = states;
        this.products = products;
        this.prices = prices;
        this.time_taken = time_taken;
        this.userPositions = userPositions;
        this.statePositions = statePositions;
        this.productPositions = productPositions;
    }

    /**
     * @return the totalUserCount
     */
    public int getTotalUserCount() {
        return totalUserCount;
    }

    /**
     * @return the totalProductCount
     */
    public int getTotalProductCount() {
        return totalProductCount;
    }

    /**
     * @return the totalStateCount
     */
    public int getTotalStateCount() {
        return totalStateCount;
    }

    /**
     * @return the productPrices
     */
    public HashMap<Integer, Integer> getProductPrices() {
        return productPrices;
    }

    /**
     * @return the userPrices
     */
    public HashMap<Integer, Integer> getUserPrices() {
        return userPrices;
    }

    /**
     * @return the statePrices
     */
    public HashMap<Integer, Integer> getStatePrices() {
        return statePrices;
    }

    /**
     * @return the users
     */
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    /**
     * @return the states
     */
    public HashMap<Integer, State> getStates() {
        return states;
    }

    /**
     * @return the products
     */
    public HashMap<Integer, Product> getProducts() {
        return products;
    }

    /**
     * @return the prices
     */
    public int[][] getPrices() {
        return prices;
    }

    public long getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(long time_taken) {
        this.time_taken = time_taken;
    }

    /**
     * @return the productPositions
     */
    public ArrayList<Integer> getProductPositions() {
        return productPositions;
    }

    /**
     * @return the userPositions
     */
    public ArrayList<Integer> getUserPositions() {
        return userPositions;
    }

    /**
     * @return the statePositions
     */
    public ArrayList<Integer> getStatePositions() {
        return statePositions;
    }

}
