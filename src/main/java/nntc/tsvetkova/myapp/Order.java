package nntc.tsvetkova.myapp;

public class Order {
    private int id;
    private String date;
    private String time;
    private String worker;
    private String user;
    private String service;
    private String products;
    public int userId;
    public int workerId;
    public int serviceId;
    public int productsId;


    public Order(int id, String date, String customer, String time, String worker, String service, String products) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.time = time;
        this.worker = worker;
        this.service = service;
        this.products = products;
    }

    public void setUser(int userId) {
        this.userId = userId;
    }
    public void setWorker(int workerId) {
        this.workerId = workerId;
    }
    public void setService(int serviceId) {
        this.serviceId = serviceId;
    }
    public void setProducts(int productsId) {
        this.productsId = productsId;
    }


    public int getId() {
        return id;
    }

    public int getUserId(){
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getWorkerId() {
        return workerId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getProductsId() {
        return productsId;
    }

}
