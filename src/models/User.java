/**
 * 
 */
package models;

/**
 * @author julestestard
 */
public class User {

    private int id;

    private String userName;

    private String role;

    private int age;

    private int stateId;

    /**
     * @param userName
     * @param role
     * @param age
     * @param stateId
     */
    public User(int id, String userName, String role, int age, int stateId) {
        this.id = id;
        this.userName = userName;
        this.role = role;
        this.age = age;
        this.stateId = stateId;
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
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role
     *            the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the stateId
     */
    public int getStateId() {
        return stateId;
    }

    /**
     * @param stateId
     *            the stateId to set
     */
    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

}
