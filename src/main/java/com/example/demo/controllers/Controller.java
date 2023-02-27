package com.example.demo.controllers;

import com.example.demo.entities.Invoice;
import com.example.demo.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class Controller {
    public boolean listHasOver50(Invoice invoice, Product product) {
        int count = 0;
        for (Product product1 : invoice.products) {
            if (product1.name.equals(product.name)) {
                count += product1.getCount();
            }
        }
        return count > 49;
    }
    public int difference50(Invoice invoice, Product product) {
        int count = 0;
        for (Product product1 : invoice.products) {
            if (product1.name.equals(product.name)) {
                count += product1.getCount();
            }
        }
        return count;
    }

    public int osize;
    public int fsize;
    public int productsCount;

    @PostMapping("generateInvoices")
    public List<Invoice> generateInvoices(@RequestBody List<Product> products) {
        List<Invoice> invoices = new ArrayList<>();
        Invoice latestInvoice = new Invoice();
        latestInvoice.setName("Invoice 1");
        invoices.add(latestInvoice);
        int invoicesCount = 1;
        int overfifty = 0;
        latestInvoice.products.stream().map(p -> p.getPrice() > 500 ? this.fsize++ : "");

        while (products.size() > 0) {
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                if (product.getCount() == 0) {
                    products.remove(product);
                    continue;
                }

                if (product.price < 500) {
                    if (!listHasOver50(latestInvoice, product)) {
                        if (product.getCount() <= 50) {
                            if ((latestInvoice.getTotal() + ((product.price * product.getCount()) + (((product.price * product.getCount()) / 100) * product.getVat()))) <= 500) {
                                if(product.getCount() <= (50 - difference50(latestInvoice,product))) {
                                    Product product1 = new Product(product);
                                    product1.setPrice(product.getPrice() + product.getDiscount());
                                    latestInvoice.products.add(product1);
                                    products.remove(product);
                                    latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * product.getCount()) + (((product.price * product.getCount()) / 100) * product.getVat())));
                                }
                            } else {
                                if ((latestInvoice.getTotal() + ((product.price) + (((product.price) / 100) * product.getVat()))) > 500)
                                    this.osize++;
                                if (this.osize == products.size() - (this.fsize)) {
                                    this.osize = 0;
                                    latestInvoice = new Invoice();
                                    latestInvoice.setName("Invoice " + ++invoicesCount);
                                    invoices.add(latestInvoice);
                                }
                                double diff = (500 - latestInvoice.getTotal()) / (((product.price) + (((product.price) / 100) * product.getVat())));
                                int rdiff = (int) diff;
                                if (rdiff > 0) {
                                    if (rdiff <= product.getCount() && (rdiff <= (50-difference50(latestInvoice,product)))) {
                                            int latestCount = product.getCount();
                                            Product product1 = new Product(product);
                                            product1.setPrice(product.getPrice() + product.getDiscount());
                                        product1.setCount(rdiff);
                                            latestInvoice.products.add(product1);
                                            products.remove(product);
                                            latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * product1.getCount()) + (((product.price * product1.getCount()) / 100) * product.getVat())));
                                            product.setCount(latestCount - rdiff);
                                    } else if(rdiff > product.getCount() && (rdiff <= (50-difference50(latestInvoice,product)))) {
                                        int latestCount = product.getCount();
                                        Product product1 = new Product(product);
                                        product1.setPrice(product.getPrice() + product.getDiscount());
                                        latestInvoice.products.add(product1);
                                        latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * product.getCount()) + (((product.price * product.getCount()) / 100) * product.getVat())));
                                        products.remove(product);
                                    } else if (rdiff <= product.getCount() && (rdiff > (50-difference50(latestInvoice,product)))) {
                                        int difference50 = 50 - difference50(latestInvoice,product);
                                        int latestCount = product.getCount();
                                        Product product1 = new Product(product);
                                        product1.setPrice(product.getPrice() + product.getDiscount());
                                        product1.setCount(difference50);
                                        latestInvoice.products.add(product1);
                                        latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * product1.getCount()) + (((product.price * product1.getCount()) / 100) * product.getVat())));
                                        product.setCount(latestCount - difference50);
                                    }else if(rdiff > product.getCount() && rdiff > (50-difference50(latestInvoice,product))){
                                        if(product.getCount() > (50 - difference50(latestInvoice,product))){
                                            int difference50 = 50 - difference50(latestInvoice,product);
                                            Product product1 = new Product(product);
                                            product1.setPrice(product.getPrice() + product.getDiscount());
                                            product1.setCount(difference50);
                                            latestInvoice.products.add(product1);
                                            products.remove(product);
                                            latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * product1.getCount()) + (((product.price * product1.getCount()) / 100) * product.getVat())));
                                            product.setCount(product.getCount() - difference50);
                                        }else{
                                            Product product1 = new Product(product);
                                            product1.setPrice(product.getPrice() + product.getDiscount());
                                            latestInvoice.products.add(product1);
                                            products.remove(product);
                                            latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * product.getCount()) + (((product.price * product.getCount()) / 100) * product.getVat())));
                                        }
                                    }
                                }
                            }
                        } else {
                            if ((latestInvoice.getTotal() + ((product.price) + (((product.price) / 100) * product.getVat()))) > 500)
                                this.osize++;
                            if (this.osize == products.size() - (this.fsize)) {
                                this.osize = 0;
                                latestInvoice = new Invoice();
                                latestInvoice.setName("Invoice " + ++invoicesCount);
                                invoices.add(latestInvoice);
                            }
                            double diff = (500 - latestInvoice.getTotal()) / (((product.price) + (((product.price) / 100) * product.getVat())));
                            int rdiff = (int) diff;
                            if (rdiff > 0) {
                                if (rdiff < 50) {
                                      if(rdiff > (50 - difference50(latestInvoice,product))){
                                          int howmuch = 50 - difference50(latestInvoice,product);
                                          int latestCount = product.getCount();
                                          Product product1 = new Product(product);
                                          product1.setPrice(product.getPrice() + product.getDiscount());
                                          product1.setCount(howmuch);
                                          latestInvoice.products.add(product1);
                                          latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * howmuch) + (((product.price * howmuch) / 100) * product.getVat())));
                                          product.setCount(latestCount - howmuch);
                                      }
                                      else{
                                          int latestCount = product.getCount();
                                          Product product1 = new Product(product);
                                          product1.setPrice(product.getPrice() + product.getDiscount());
                                          product1.setCount(rdiff);
                                          latestInvoice.products.add(product1);
                                          latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * rdiff) + (((product.price * rdiff) / 100) * product.getVat())));
                                          product.setCount(latestCount - rdiff);
                                      }
                                } else {
                                    int howmuch = 50 - difference50(latestInvoice,product);
                                    int latestCount = product.getCount();
                                    Product product1 = new Product(product);
                                    product1.setPrice(product.getPrice() + product.getDiscount());
                                    product1.setCount(howmuch);
                                    latestInvoice.products.add(product1);
                                    latestInvoice.setTotal(latestInvoice.getTotal() + ((product.price * howmuch) + (((product.price * howmuch) / 100) * product.getVat())));
                                    product.setCount(latestCount - howmuch);
                                }
                            }
                        }
                    } else {
                        this.osize++;
                        if (this.osize == products.size() - (this.fsize)) {
                            latestInvoice = new Invoice();
                            latestInvoice.setName("Invoice " + ++invoicesCount);
                            invoices.add(latestInvoice);
                            this.osize = 0;
                        }
                    }
                } else {
                    int count = product.getCount();
                    for (int j = 0; j < count; j++) {
                        Invoice latestInvoice2 = new Invoice();
                        Product product1 = new Product(product);
                        product1.setPrice(product.getPrice() + product.getDiscount());
                        product1.setCount(1);
                        product.setCount(product.getCount() - 1);
                        latestInvoice2.setName("Invoice " + ++invoicesCount);
                        latestInvoice2.products.add(product1);
                        latestInvoice2.setTotal(product.price + ((product.price / 100) * product.getVat()));
                        invoices.add(latestInvoice2);
                    }
                    product.setCount(0);
                    products.remove(product);
                }
            }
            this.osize = 0;
            this.productsCount = 0;
            overfifty = 0;
        }
        return invoices;
    }

}
