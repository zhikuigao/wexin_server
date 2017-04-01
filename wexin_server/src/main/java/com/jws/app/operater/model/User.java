package com.jws.app.operater.model;

public class User {
	 private String email;
	 private String password;
	 
	 public User(String accoun, String pass){
		 this.email=accoun;
		 this.password= pass;
	 }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
