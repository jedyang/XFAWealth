/**
 * ResetPIN.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class ResetPIN  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String session;

    private java.lang.String userType;

    private java.lang.String targetID;

    public ResetPIN() {
    }

    public ResetPIN(
           java.lang.String id,
           java.lang.String session,
           java.lang.String userType,
           java.lang.String targetID) {
           this.id = id;
           this.session = session;
           this.userType = userType;
           this.targetID = targetID;
    }


    /**
     * Gets the id value for this ResetPIN.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this ResetPIN.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the session value for this ResetPIN.
     * 
     * @return session
     */
    public java.lang.String getSession() {
        return session;
    }


    /**
     * Sets the session value for this ResetPIN.
     * 
     * @param session
     */
    public void setSession(java.lang.String session) {
        this.session = session;
    }


    /**
     * Gets the userType value for this ResetPIN.
     * 
     * @return userType
     */
    public java.lang.String getUserType() {
        return userType;
    }


    /**
     * Sets the userType value for this ResetPIN.
     * 
     * @param userType
     */
    public void setUserType(java.lang.String userType) {
        this.userType = userType;
    }


    /**
     * Gets the targetID value for this ResetPIN.
     * 
     * @return targetID
     */
    public java.lang.String getTargetID() {
        return targetID;
    }


    /**
     * Sets the targetID value for this ResetPIN.
     * 
     * @param targetID
     */
    public void setTargetID(java.lang.String targetID) {
        this.targetID = targetID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResetPIN)) return false;
        ResetPIN other = (ResetPIN) obj;
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
            ((this.userType==null && other.getUserType()==null) || 
             (this.userType!=null &&
              this.userType.equals(other.getUserType()))) &&
            ((this.targetID==null && other.getTargetID()==null) || 
             (this.targetID!=null &&
              this.targetID.equals(other.getTargetID())));
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
        if (getUserType() != null) {
            _hashCode += getUserType().hashCode();
        }
        if (getTargetID() != null) {
            _hashCode += getTargetID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResetPIN.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ResetPIN"));
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
        elemField.setFieldName("userType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "userType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "targetID"));
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
