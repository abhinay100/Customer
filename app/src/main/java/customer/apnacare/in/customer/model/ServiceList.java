package customer.apnacare.in.customer.model;

/**
 * Created by root on 18/1/17.
 */

public class ServiceList {

    private int id;
    private String serviceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceList(int id, String name){
        this.id = id;
        this.serviceName = name;
    }

    //to display object as a string in spinner
    public String toString() {
        return serviceName;
    }


}
