/**
 * GetStockVoucherResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetStockVoucherResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult getStockVoucherResult;

    public GetStockVoucherResponse() {
    }

    public GetStockVoucherResponse(
           com.fsll.oms.aimaws.WSDSResult getStockVoucherResult) {
           this.getStockVoucherResult = getStockVoucherResult;
    }


    /**
     * Gets the getStockVoucherResult value for this GetStockVoucherResponse.
     * 
     * @return getStockVoucherResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGetStockVoucherResult() {
        return getStockVoucherResult;
    }


    /**
     * Sets the getStockVoucherResult value for this GetStockVoucherResponse.
     * 
     * @param getStockVoucherResult
     */
    public void setGetStockVoucherResult(com.fsll.oms.aimaws.WSDSResult getStockVoucherResult) {
        this.getStockVoucherResult = getStockVoucherResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetStockVoucherResponse)) return false;
        GetStockVoucherResponse other = (GetStockVoucherResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getStockVoucherResult==null && other.getGetStockVoucherResult()==null) || 
             (this.getStockVoucherResult!=null &&
              this.getStockVoucherResult.equals(other.getGetStockVoucherResult())));
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
        if (getGetStockVoucherResult() != null) {
            _hashCode += getGetStockVoucherResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetStockVoucherResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetStockVoucherResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getStockVoucherResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetStockVoucherResult"));
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
