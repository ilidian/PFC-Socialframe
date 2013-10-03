package org.pfc.socialframe.model;

public class User {
	private String uid;
	private String name;
	private String lastname;
	private String birthday;
	private String gender;
	private String city;
	private String picuser;
	public User(String uid, String name, String lastname, String birthday, String gender, String city, String picuser){
		this.setUid(uid);
		this.setName(name);
		this.setLastname(lastname);
		this.setBirthday(birthday);
		this.setCity(city);
		this.setGender(gender);
		this.setPicuser(picuser);
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUid() {
		return uid;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGender() {
		return gender;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return city;
	}
	public void setPicuser(String picuser) {
		this.picuser = picuser;
	}
	public String getPicuser() {
		return picuser;
	}
}
