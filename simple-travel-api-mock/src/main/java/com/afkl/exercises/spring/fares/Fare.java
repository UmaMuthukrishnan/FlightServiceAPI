package com.afkl.exercises.spring.fares;

import lombok.Value;

//@Value
public class Fare {

    double amount;
    Currency currency;
    String origin, destination;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Fare(double amount, Currency currency, String origin, String destination) {
		super();
		this.amount = amount;
		this.currency = currency;
		this.origin = origin;
		this.destination = destination;
	}
    
    

}
