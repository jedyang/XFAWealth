/**
 * GetTradeResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetTradeResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult getTradeResult;

    public GetTradeResponse() {
    }

    public GetTradeResponse(
           com.fsll.oms.aimaws.WSDSResult getTradeResult) {
           this.getTradeResult = getTradeResult;
    }


    /**
     * Gets the getTradeResult value for this GetTradeResponse.
     * 
     * @return getTradeResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGetTradeResult() {
        return getTradeResult;
    }


    /**
     * Sets the getTradeResult value for this GetTradeResponse.
     * 
     * @param getTradeResult
     */
    public void setGetTradeResult(com.fsll.oms.aimaws.WSDSResult getTradeResult) {
        this.getTradeResult = getTradeResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetTradeResponse)) return false;
        GetTradeResponse other = (GetTradeResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getTradeResult==null && other.getGetTradeResult()==null) || 
             (this.getTradeResult!=null &&
              this.getTradeResult.equals(other.getGetTradeResult())));
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
        if (getGetTradeResult() != null) {
            _hashCode += getGetTradeResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetTradeResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetTradeResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getTradeResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetTradeResult"));
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
