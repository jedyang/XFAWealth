/**
 * GetStock.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetStock  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String sessionID;

    private java.lang.String stockCode;

    private java.lang.String stockType;

    private java.lang.String tradable;

    public GetStock() {
    }

    public GetStock(
           java.lang.String id,
           java.lang.String sessionID,
           java.lang.String stockCode,
           java.lang.String stockType,
           java.lang.String tradable) {
           this.id = id;
           this.sessionID = sessionID;
           this.stockCode = stockCode;
           this.stockType = stockType;
           this.tradable = tradable;
    }


    /**
     * Gets the id value for this GetStock.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this GetStock.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the sessionID value for this GetStock.
     * 
     * @return sessionID
     */
    public java.lang.String getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this GetStock.
     * 
     * @param sessionID
     */
    public void setSessionID(java.lang.String sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the stockCode value for this GetStock.
     * 
     * @return stockCode
     */
    public java.lang.String getStockCode() {
        return stockCode;
    }


    /**
     * Sets the stockCode value for this GetStock.
     * 
     * @param stockCode
     */
    public void setStockCode(java.lang.String stockCode) {
        this.stockCode = stockCode;
    }


    /**
     * Gets the stockType value for this GetStock.
     * 
     * @return stockType
     */
    public java.lang.String getStockType() {
        return stockType;
    }


    /**
     * Sets the stockType value for this GetStock.
     * 
     * @param stockType
     */
    public void setStockType(java.lang.String stockType) {
        this.stockType = stockType;
    }


    /**
     * Gets the tradable value for this GetStock.
     * 
     * @return tradable
     */
    public java.lang.String getTradable() {
        return tradable;
    }


    /**
     * Sets the tradable value for this GetStock.
     * 
     * @param tradable
     */
    public void setTradable(java.lang.String tradable) {
        this.tradable = tradable;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetStock)) return false;
        GetStock other = (GetStock) obj;
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
            ((this.stockCode==null && other.getStockCode()==null) || 
             (this.stockCode!=null &&
              this.stockCode.equals(other.getStockCode()))) &&
            ((this.stockType==null && other.getStockType()==null) || 
             (this.stockType!=null &&
              this.stockType.equals(other.getStockType()))) &&
            ((this.tradable==null && other.getTradable()==null) || 
             (this.tradable!=null &&
              this.tradable.equals(other.getTradable())));
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
        if (getStockCode() != null) {
            _hashCode += getStockCode().hashCode();
        }
        if (getStockType() != null) {
            _hashCode += getStockType().hashCode();
        }
        if (getTradable() != null) {
            _hashCode += getTradable().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetStock.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetStock"));
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
        elemField.setFieldName("stockCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "stockCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stockType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "stockType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tradable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "tradable"));
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
