package com.fsll.wmes.web.vo;
import java.io.Serializable;  
import java.util.Map;

/** 
 *Title:学生类 
 *Description:实现序列化接口的学生类 
 *Copyright: copyright(c) 2012 
 *Filename: Student.java 
 *@author Wang Luqing 
 *@version 1.0 
 */  
public class IfafirmTeamVO implements Serializable  
{  
    
 private String id;  
 private String parentid;  
 private String ifafirmid;  
 private String name;  
 private String code;  
 private int orderby;  
 private int classlayer;
 private Map childTeam;
  
 public IfafirmTeamVO()  
 {  
  
 }  

 public void setId(String id)  
 {  
  this.id = id;  
 }  
 
 public void setParentid(String parentid)  
 {  
  this.parentid = parentid;  
 }  
 
 public void setIfafirmid(String ifafirmid)  
 {  
  this.ifafirmid = ifafirmid;  
 }  
 
 public void setName(String name)  
 {  
  this.name = name;  
 }  
  
 public void setCode(String code)  
 {  
  this.code = code;  
 }  
  
 public void setOrderby(int orderby)  
 {  
  this.orderby = orderby;  
 }  
  
 public void setClasslayer(int classlayer)  
 {  
  this.classlayer = classlayer;  
 }  
  
 public String getId()  
 {  
  return this.id;  
 }  
 
 public String getParentid()  
 {  
  return this.parentid;  
 } 
 
 public String getIfafirmid()  
 {  
  return this.ifafirmid;  
 } 
 
 public String getName()  
 {  
  return this.name;  
 }  
   
 public String getcode()  
 {  
  return this.code;  
 }  
  
 public int getOrderby()  
 {  
  return this.orderby;  
 }  
  
 public int getClasslayer()  
 {  
  return this.classlayer;  
 }

public Map getChildTeam() {
	return childTeam;
}

public void setChildTeam(Map childTeam) {
	this.childTeam = childTeam;
}



}  