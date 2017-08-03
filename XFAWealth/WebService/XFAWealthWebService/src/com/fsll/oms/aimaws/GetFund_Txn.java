/**
 * GetFund_Txn.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetFund_Txn  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String sessionID;

    private java.lang.String clientCode;

    private java.util.Calendar fromOrderDate;

    private java.util.Calendar toOrderDate;

    public GetFund_Txn() {
    }

    public GetFund_Txn(
           java.lang.String id,
           java.lang.String sessionID,
           java.lang.String clientCode,
           java.util.Calendar fromOrderDate,
           java.util.Calendar toOrderDate) {
           this.id = id;
           this.sessionID = sessionID;
           this.clientCode = clientCode;
           this.fromOrderDate = fromOrderDate;
           this.toOrderDate = toOrderDate;
    }


    /**
     * Gets the id value for this GetFund_Txn.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this GetFund_Txn.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the sessionID value for this GetFund_Txn.
     * 
     * @return sessionID
     */
    public java.lang.String getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this GetFund_Txn.
     * 
     * @param sessionID
     */
    public void setSessionID(java.lang.String sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the clientCode value for this GetFund_Txn.
     * 
     * @return clientCode
     */
    public java.lang.String getClientCode() {
        return clientCode;
    }


    /**
     * Sets the clientCode value for this GetFund_Txn.
     * 
     * @param clientCode
     */
    public void setClientCode(java.lang.String clientCode) {
        this.clientCode = clientCode;
    }


    /**
     * Gets the fromOrderDate value for this GetFund_Txn.
     * 
     * @return fromOrderDate
     */
    public java.util.Calendar getFromOrderDate() {
        return fromOrderDate;
    }


    /**
     * Sets the fromOrderDate value for this GetFund_Txn.
     * 
     * @param fromOrderDate
     */
    public void setFromOrderDate(java.util.Calendar fromOrderDate) {
        this.fromOrderDate = fromOrderDate;
    }


    /**
     * Gets the toOrderDate value for this GetFund_Txn.
     * 
     * @return toOrderDate
     */
    public java.util.Calendar getToOrderDate() {
        return toOrderDate;
    }


    /**
     * Sets the toOrderDate value for this GetFund_Txn.
     * 
     * @param toOrderDate
     */
    public void setToOrderDate(java.util.Calendar toOrderDate) {
        this.toOrderDate = toOrderDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetFund_Txn)) return false;
        GetFund_Txn other = (GetFund_Txn) obj;
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
            ((this.fromOrderDate==null && other.getFromOrderDate()==null) || 
             (this.fromOrderDate!=null &&
              this.fromOrderDate.equals(other.getFromOrderDate()))) &&
            ((this.toOrderDate==null && other.getToOrderDate()==null) || 
             (this.toOrderDate!=null &&
              this.toOrderDate.equals(other.getToOrderDate())));
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
        if (getFromOrderDate() != null) {
            _hashCode += getFromOrderDate().hashCode();
        }
        if (getToOrderDate() != null) {
            _hashCode += getToOrderDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetFund_Txn.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetFund_Txn"));
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
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ClientCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fromOrderDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "FromOrderDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("toOrderDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ToOrderDate"));
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
