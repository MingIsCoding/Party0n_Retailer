package edu.sjsu.pm.partyonretailer;

/**
 * Created by Ming on 12/7/16.
 */

public class Merchandise {
    private String name;
    private float price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Merchandise(String name,float price) {
        this.name = name;
        this.price = price;
    }
}
