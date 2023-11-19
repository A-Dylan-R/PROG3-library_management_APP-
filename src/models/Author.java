package models;

public class Author extends User {
    private String sex;

    public Author(int userId, String name, String sex) {
        super(userId, name);
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
