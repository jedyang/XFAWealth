/**
 * GetStockResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetStockResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult getStockResult;

    public GetStockResponse() {
    }

    public GetStockResponse(
           com.fsll.oms.aimaws.WSDSResult getStockResult) {
           this.getStockResult = getStockResult;
    }


    /**
     * Gets the getStockResult value for this GetStockResponse.
     * 
     * @return getStockResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGetStockResult() {
        return getStockResult;
    }


    /**
     * Sets the getStockResult value for this GetStockResponse.
     * 
     * @param getStockResult
     */
    public void setGetStockResult(com.fsll.oms.aimaws.WSDSResult getStockResult) {
        this.getStockResult = getStockResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetStockResponse)) return false;
        GetStockResponse other = (GetStockResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getStockResult==null && other.getGetStockResult()==null) || 
             (this.getStockResult!=null &&
              this.getStockResult.equals(other.getGetStockResult())));
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
        if (getGetStockResult() != null) {
            _hashCode += getGetStockResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetStockResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetStockResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getStockResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetStockResult"));
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
