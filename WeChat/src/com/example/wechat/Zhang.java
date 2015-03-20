package com.example.wechat;

public class Zhang {
	       
    private String name;  
    private int age;  
    private String email;  
    private String address;  
    public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

   
    public Zhang(String name, int age, String email, String address) {  
        super();  
        this.name = name;  
        this.age = age;  
        this.email = email;  
        this.address = address;  
    }  
   
    @Override  
    public String toString() {  
        return "Person [name=" + name + ", age=" + age + ", email=" + email  
                + ", address=" + address + "]";  
    }  
   
}  
