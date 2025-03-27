package nntc.tsvetkova.myapp;

public class Appointment {
    private int id;
    private String time;
    private String date;
    private String worker;
    private String client;
    private String service;
    //private String storage;


    public Appointment(int id, String time, String date, String worker, String client, String service) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.worker = worker;
        this.client = client;
        this.service = service;
       // this.storage = storage;
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

    public String getClient() {
        return client;
    }

    public String getWorker() {
        return worker;
    }

    public String getService() {
        return service;
    }

    //public String getStorage() {
       // return storage;
   // }
}
