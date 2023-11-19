package models;

public class Admin extends User {
    private String role;

    public Admin(int userId, String name) {
        super(userId, name);
        this.role = "admin";
    }

    // Getter and setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
