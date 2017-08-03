/**
 * GetClientBank.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetClientBank  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String sessionID;

    private java.lang.String clientCode;

    private java.lang.String cur;

    public GetClientBank() {
    }

    public GetClientBank(
           java.lang.String id,
           java.lang.String sessionID,
           java.lang.String clientCode,
           java.lang.String cur) {
           this.id = id;
           this.sessionID = sessionID;
           this.clientCode = clientCode;
           this.cur = cur;
    }


    /**
     * Gets the id value for this GetClientBank.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this GetClientBank.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the sessionID value for this GetClientBank.
     * 
     * @return sessionID
     */
    public java.lang.String getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this GetClientBank.
     * 
     * @param sessionID
     */
    public void setSessionID(java.lang.String sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the clientCode value for this GetClientBank.
     * 
     * @return clientCode
     */
    public java.lang.String getClientCode() {
        return clientCode;
    }


    /**
     * Sets the clientCode value for this GetClientBank.
     * 
     * @param clientCode
     */
    public void setClientCode(java.lang.String clientCode) {
        this.clientCode = clientCode;
    }


    /**
     * Gets the cur value for this GetClientBank.
     * 
     * @return cur
     */
    public java.lang.String getCur() {
        return cur;
    }


    /**
     * Sets the cur value for this GetClientBank.
     * 
     * @param cur
     */
    public void setCur(java.lang.String cur) {
        this.cur = cur;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetClientBank)) return false;
        GetClientBank other = (GetClientBank) obj;
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
            ((this.sessionID==null && other.getSessionID()==null) || 
             (this.sessionID!=null &&
              this.sessionID.equals(other.getSessionID()))) &&
            ((this.clientCode==null && other.getClientCode()==null) || 
             (this.clientCode!=null &&
              this.clientCode.equals(other.getClientCode()))) &&
            ((this.cur==null && other.getCur()==null) || 
             (this.cur!=null &&
              this.cur.equals(other.getCur())));
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
        if (getSessionID() != null) {
            _hashCode += getSessionID().hashCode();
        }
        if (getClientCode() != null) {
            _hashCode += getClientCode().hashCode();
        }
        if (getCur() != null) {
            _hashCode += getCur().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetClientBank.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetClientBank"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "sessionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "clientCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "cur"));
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
