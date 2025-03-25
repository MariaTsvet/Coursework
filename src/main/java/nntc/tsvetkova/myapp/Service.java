package nntc.tsvetkova.myapp;

public class Service {
    private int id;
    private String name;
    private String email;


    public Service(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
