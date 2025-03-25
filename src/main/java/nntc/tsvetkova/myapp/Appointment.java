package nntc.tsvetkova.myapp;

public class Appointment {
    private int id;
    private String time;
    private String date;
    private int worker;
    private int client;
    private int service;
    private int storage;


    public Appointment(int id, String time, String date, int worker, int client, int service, int storage) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.worker = worker;
        this.client = client;
        this.service = service;
        this.storage = storage;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getClient() {
        return client;
    }

    public int getWorker() {
        return worker;
    }

    public int getService() {
        return service;
    }

    public int getStorage() {
        return storage;
    }
}
