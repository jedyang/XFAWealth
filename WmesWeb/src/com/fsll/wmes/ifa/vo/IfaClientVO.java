package com.fsll.wmes.ifa.vo;

public class IfaClientVO {

	private String id;
	private String firstName;
	private String lastName;
	private String fullName;
	private String iconUrl;
	private int isImportant;
	private String clientType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public int getIsImportant() {
		return isImportant;
	}
	public void setIsImportant(int isImportant) {
		this.isImportant = isImportant;
	}
	
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	@Override  
    public boolean equals(Object o) {  
        if (o instanceof IfaClientVO) {  
        	IfaClientVO ifaClientVO = (IfaClientVO) o;  
            return this.id.equals(ifaClientVO.id);  
        }  
        return super.equals(o);  
    } 
}
