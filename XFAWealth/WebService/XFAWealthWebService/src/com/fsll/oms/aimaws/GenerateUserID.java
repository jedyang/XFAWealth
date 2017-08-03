/**
 * GenerateUserID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GenerateUserID  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String session;

    private java.lang.String branchCode;

    private java.lang.String roleType;

    public GenerateUserID() {
    }

    public GenerateUserID(
           java.lang.String id,
           java.lang.String session,
           java.lang.String branchCode,
           java.lang.String roleType) {
           this.id = id;
           this.session = session;
           this.branchCode = branchCode;
           this.roleType = roleType;
    }


    /**
     * Gets the id value for this GenerateUserID.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this GenerateUserID.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the session value for this GenerateUserID.
     * 
     * @return session
     */
    public java.lang.String getSession() {
        return session;
    }


    /**
     * Sets the session value for this GenerateUserID.
     * 
     * @param session
     */
    public void setSession(java.lang.String session) {
        this.session = session;
    }


    /**
     * Gets the branchCode value for this GenerateUserID.
     * 
     * @return branchCode
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }


    /**
     * Sets the branchCode value for this GenerateUserID.
     * 
     * @param branchCode
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }


    /**
     * Gets the roleType value for this GenerateUserID.
     * 
     * @return roleType
     */
    public java.lang.String getRoleType() {
        return roleType;
    }


    /**
     * Sets the roleType value for this GenerateUserID.
     * 
     * @param roleType
     */
    public void setRoleType(java.lang.String roleType) {
        this.roleType = roleType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GenerateUserID)) return false;
        GenerateUserID other = (GenerateUserID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.session==null && other.getSession()==null) || 
             (this.session!=null &&
              this.session.equals(other.getSession()))) &&
            ((this.branchCode==null && other.getBranchCode()==null) || 
             (this.branchCode!=null &&
              this.branchCode.equals(other.getBranchCode()))) &&
            ((this.roleType==null && other.getRoleType()==null) || 
             (this.roleType!=null &&
              this.roleType.equals(other.getRoleType())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getSession() != null) {
            _hashCode += getSession().hashCode();
        }
        if (getBranchCode() != null) {
            _hashCode += getBranchCode().hashCode();
        }
        if (getRoleType() != null) {
            _hashCode += getRoleType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GenerateUserID.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GenerateUserID"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("session");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "session"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("branchCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "branchCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "roleType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
