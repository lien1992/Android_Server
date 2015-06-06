package com.crystal.arc.entity;

public class User {
	private long id;
	private String name;
	private String password;
	private long register_date;
	
	public User(long id, String name, String password, long register_date) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.register_date = register_date;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public long getRegister_date() {
		return register_date;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRegister_date(long register_date) {
		this.register_date = register_date;
	}
	

}
