/**
 * GetFund_TxnResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetFund_TxnResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult getFund_TxnResult;

    public GetFund_TxnResponse() {
    }

    public GetFund_TxnResponse(
           com.fsll.oms.aimaws.WSDSResult getFund_TxnResult) {
           this.getFund_TxnResult = getFund_TxnResult;
    }


    /**
     * Gets the getFund_TxnResult value for this GetFund_TxnResponse.
     * 
     * @return getFund_TxnResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGetFund_TxnResult() {
        return getFund_TxnResult;
    }


    /**
     * Sets the getFund_TxnResult value for this GetFund_TxnResponse.
     * 
     * @param getFund_TxnResult
     */
    public void setGetFund_TxnResult(com.fsll.oms.aimaws.WSDSResult getFund_TxnResult) {
        this.getFund_TxnResult = getFund_TxnResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetFund_TxnResponse)) return false;
        GetFund_TxnResponse other = (GetFund_TxnResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getFund_TxnResult==null && other.getGetFund_TxnResult()==null) || 
             (this.getFund_TxnResult!=null &&
              this.getFund_TxnResult.equals(other.getGetFund_TxnResult())));
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
        if (getGetFund_TxnResult() != null) {
            _hashCode += getGetFund_TxnResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetFund_TxnResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetFund_TxnResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getFund_TxnResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFund_TxnResult"));
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
