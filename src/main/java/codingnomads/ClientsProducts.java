package codingnomads;

import java.util.ArrayList;

public class ClientsProducts {



    private ArrayList<String> products = new ArrayList<>();
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return "ClientsProducts{" +
                "products=" + products +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public ArrayList<String> getProducts() {
        return products;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
