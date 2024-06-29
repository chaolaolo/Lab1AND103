package com.example.lab;


import java.util.Date;

public class CountryModel {
   private String id,image,name,size;
   private int quantity,price;
    private Date timestamp;

    public CountryModel() {
    }

    public CountryModel(String id, String image, String name, String size, int quantity, int price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    public CountryModel(String image, String name, String size, int quantity, int price, Date timestamp) {
        this.image = image;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    public CountryModel(String image, String name, String size, int quantity, int price) {
        this.image = image;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
