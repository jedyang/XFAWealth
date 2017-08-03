/**
 * GetExchangeRate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetExchangeRate  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String sessionID;

    private java.lang.String cur;

    private java.util.Calendar tradeDate;

    public GetExchangeRate() {
    }

    public GetExchangeRate(
           java.lang.String id,
           java.lang.String sessionID,
           java.lang.String cur,
           java.util.Calendar tradeDate) {
           this.id = id;
           this.sessionID = sessionID;
           this.cur = cur;
           this.tradeDate = tradeDate;
    }


    /**
     * Gets the id value for this GetExchangeRate.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this GetExchangeRate.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the sessionID value for this GetExchangeRate.
     * 
     * @return sessionID
     */
    public java.lang.String getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this GetExchangeRate.
     * 
     * @param sessionID
     */
    public void setSessionID(java.lang.String sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the cur value for this GetExchangeRate.
     * 
     * @return cur
     */
    public java.lang.String getCur() {
        return cur;
    }


    /**
     * Sets the cur value for this GetExchangeRate.
     * 
     * @param cur
     */
    public void setCur(java.lang.String cur) {
        this.cur = cur;
    }


    /**
     * Gets the tradeDate value for this GetExchangeRate.
     * 
     * @return tradeDate
     */
    public java.util.Calendar getTradeDate() {
        return tradeDate;
    }


    /**
     * Sets the tradeDate value for this GetExchangeRate.
     * 
     * @param tradeDate
     */
    public void setTradeDate(java.util.Calendar tradeDate) {
        this.tradeDate = tradeDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExchangeRate)) return false;
        GetExchangeRate other = (GetExchangeRate) obj;
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
            ((this.cur==null && other.getCur()==null) || 
             (this.cur!=null &&
              this.cur.equals(other.getCur()))) &&
            ((this.tradeDate==null && other.getTradeDate()==null) || 
             (this.tradeDate!=null &&
              this.tradeDate.equals(other.getTradeDate())));
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
        if (getCur() != null) {
            _hashCode += getCur().hashCode();
        }
        if (getTradeDate() != null) {
            _hashCode += getTradeDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExchangeRate.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetExchangeRate"));
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
        elemField.setFieldName("cur");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "cur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tradeDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "tradeDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
