package com.abhi.TrainReservation.model;

public class Passenger implements Comparable<Passenger> {

	private String name;
	private int age;
	private char gender;
	private String aadhaarNumber;

	public Passenger(String name, int age, char gender, String aadhaarNumber) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}
	
	public String getAadharNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}


	@Override
	public int compareTo(Passenger passenger) {

		return this.getAge() - passenger.getAge();
	}

}