/**
 * GetExchangeRateResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.accountws;

public class GetExchangeRateResponse  implements java.io.Serializable {
    private com.fsll.oms.accountws.WSDSResult getExchangeRateResult;

    public GetExchangeRateResponse() {
    }

    public GetExchangeRateResponse(
           com.fsll.oms.accountws.WSDSResult getExchangeRateResult) {
           this.getExchangeRateResult = getExchangeRateResult;
    }


    /**
     * Gets the getExchangeRateResult value for this GetExchangeRateResponse.
     * 
     * @return getExchangeRateResult
     */
    public com.fsll.oms.accountws.WSDSResult getGetExchangeRateResult() {
        return getExchangeRateResult;
    }


    /**
     * Sets the getExchangeRateResult value for this GetExchangeRateResponse.
     * 
     * @param getExchangeRateResult
     */
    public void setGetExchangeRateResult(com.fsll.oms.accountws.WSDSResult getExchangeRateResult) {
        this.getExchangeRateResult = getExchangeRateResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExchangeRateResponse)) return false;
        GetExchangeRateResponse other = (GetExchangeRateResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getExchangeRateResult==null && other.getGetExchangeRateResult()==null) || 
             (this.getExchangeRateResult!=null &&
              this.getExchangeRateResult.equals(other.getGetExchangeRateResult())));
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
        if (getGetExchangeRateResult() != null) {
            _hashCode += getGetExchangeRateResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExchangeRateResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetExchangeRateResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getExchangeRateResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetExchangeRateResult"));
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
