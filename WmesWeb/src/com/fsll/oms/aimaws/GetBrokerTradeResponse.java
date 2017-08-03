/**
 * GetBrokerTradeResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetBrokerTradeResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult getBrokerTradeResult;

    public GetBrokerTradeResponse() {
    }

    public GetBrokerTradeResponse(
           com.fsll.oms.aimaws.WSDSResult getBrokerTradeResult) {
           this.getBrokerTradeResult = getBrokerTradeResult;
    }


    /**
     * Gets the getBrokerTradeResult value for this GetBrokerTradeResponse.
     * 
     * @return getBrokerTradeResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGetBrokerTradeResult() {
        return getBrokerTradeResult;
    }


    /**
     * Sets the getBrokerTradeResult value for this GetBrokerTradeResponse.
     * 
     * @param getBrokerTradeResult
     */
    public void setGetBrokerTradeResult(com.fsll.oms.aimaws.WSDSResult getBrokerTradeResult) {
        this.getBrokerTradeResult = getBrokerTradeResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetBrokerTradeResponse)) return false;
        GetBrokerTradeResponse other = (GetBrokerTradeResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getBrokerTradeResult==null && other.getGetBrokerTradeResult()==null) || 
             (this.getBrokerTradeResult!=null &&
              this.getBrokerTradeResult.equals(other.getGetBrokerTradeResult())));
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
        if (getGetBrokerTradeResult() != null) {
            _hashCode += getGetBrokerTradeResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetBrokerTradeResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetBrokerTradeResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getBrokerTradeResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetBrokerTradeResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "WSDSResult"));
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
