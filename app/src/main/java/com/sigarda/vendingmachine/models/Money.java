package com.sigarda.vendingmachine.models;

import java.io.Serializable;

public class Money implements Serializable {
    String name;
    int price;
    int stock;
    boolean selected;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
