/**
 * GetBrokerTrade.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetBrokerTrade  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String sessionID;

    private java.lang.String brokerCode;

    private java.util.Calendar fromTradeDate;

    private java.util.Calendar toTradeDate;

    public GetBrokerTrade() {
    }

    public GetBrokerTrade(
           java.lang.String id,
           java.lang.String sessionID,
           java.lang.String brokerCode,
           java.util.Calendar fromTradeDate,
           java.util.Calendar toTradeDate) {
           this.id = id;
           this.sessionID = sessionID;
           this.brokerCode = brokerCode;
           this.fromTradeDate = fromTradeDate;
           this.toTradeDate = toTradeDate;
    }


    /**
     * Gets the id value for this GetBrokerTrade.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this GetBrokerTrade.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the sessionID value for this GetBrokerTrade.
     * 
     * @return sessionID
     */
    public java.lang.String getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this GetBrokerTrade.
     * 
     * @param sessionID
     */
    public void setSessionID(java.lang.String sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the brokerCode value for this GetBrokerTrade.
     * 
     * @return brokerCode
     */
    public java.lang.String getBrokerCode() {
        return brokerCode;
    }


    /**
     * Sets the brokerCode value for this GetBrokerTrade.
     * 
     * @param brokerCode
     */
    public void setBrokerCode(java.lang.String brokerCode) {
        this.brokerCode = brokerCode;
    }


    /**
     * Gets the fromTradeDate value for this GetBrokerTrade.
     * 
     * @return fromTradeDate
     */
    public java.util.Calendar getFromTradeDate() {
        return fromTradeDate;
    }


    /**
     * Sets the fromTradeDate value for this GetBrokerTrade.
     * 
     * @param fromTradeDate
     */
    public void setFromTradeDate(java.util.Calendar fromTradeDate) {
        this.fromTradeDate = fromTradeDate;
    }


    /**
     * Gets the toTradeDate value for this GetBrokerTrade.
     * 
     * @return toTradeDate
     */
    public java.util.Calendar getToTradeDate() {
        return toTradeDate;
    }


    /**
     * Sets the toTradeDate value for this GetBrokerTrade.
     * 
     * @param toTradeDate
     */
    public void setToTradeDate(java.util.Calendar toTradeDate) {
        this.toTradeDate = toTradeDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetBrokerTrade)) return false;
        GetBrokerTrade other = (GetBrokerTrade) obj;
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
            ((this.brokerCode==null && other.getBrokerCode()==null) || 
             (this.brokerCode!=null &&
              this.brokerCode.equals(other.getBrokerCode()))) &&
            ((this.fromTradeDate==null && other.getFromTradeDate()==null) || 
             (this.fromTradeDate!=null &&
              this.fromTradeDate.equals(other.getFromTradeDate()))) &&
            ((this.toTradeDate==null && other.getToTradeDate()==null) || 
             (this.toTradeDate!=null &&
              this.toTradeDate.equals(other.getToTradeDate())));
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
        if (getBrokerCode() != null) {
            _hashCode += getBrokerCode().hashCode();
        }
        if (getFromTradeDate() != null) {
            _hashCode += getFromTradeDate().hashCode();
        }
        if (getToTradeDate() != null) {
            _hashCode += getToTradeDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetBrokerTrade.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetBrokerTrade"));
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
        elemField.setFieldName("brokerCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "brokerCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fromTradeDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "fromTradeDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("toTradeDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "toTradeDate"));
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
