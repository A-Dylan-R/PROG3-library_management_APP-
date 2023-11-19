package models;

public class Visitor extends User {
    private String reference;

    public Visitor(int userId, String name, String reference) {
        super(userId, name);
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
