package models;

import java.util.Date;

public class Transaction {
    private double amount;
    private Date date;

    public Transaction(Object o, Object object, double amount, Date date) {
        this.amount = amount;
        this.date = date;
    }

}