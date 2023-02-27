package com.example.demo.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.objenesis.SpringObjenesis;


@Getter
@Setter
public class Product {
    public Product(int count,String name,double vat,double price,int discount){
        this.count = count;
        this.name = name;
        this.vat = vat;
        this.price = price - discount;
        this.discount = discount;
    }
    public Product(Product object){
        this.count = object.getCount();
        this.name = object.getName();
        this.vat = object.getVat();
        this.price = object.price - object.discount;
        this.discount = object.discount;

    }
    public String name;
    public double vat;
    public int count;
    public double price;
    public double discount;
}
