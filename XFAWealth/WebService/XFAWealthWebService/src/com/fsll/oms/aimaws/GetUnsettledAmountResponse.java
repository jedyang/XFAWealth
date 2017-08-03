/**
 * GetUnsettledAmountResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetUnsettledAmountResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult getUnsettledAmountResult;

    public GetUnsettledAmountResponse() {
    }

    public GetUnsettledAmountResponse(
           com.fsll.oms.aimaws.WSDSResult getUnsettledAmountResult) {
           this.getUnsettledAmountResult = getUnsettledAmountResult;
    }


    /**
     * Gets the getUnsettledAmountResult value for this GetUnsettledAmountResponse.
     * 
     * @return getUnsettledAmountResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGetUnsettledAmountResult() {
        return getUnsettledAmountResult;
    }


    /**
     * Sets the getUnsettledAmountResult value for this GetUnsettledAmountResponse.
     * 
     * @param getUnsettledAmountResult
     */
    public void setGetUnsettledAmountResult(com.fsll.oms.aimaws.WSDSResult getUnsettledAmountResult) {
        this.getUnsettledAmountResult = getUnsettledAmountResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetUnsettledAmountResponse)) return false;
        GetUnsettledAmountResponse other = (GetUnsettledAmountResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getUnsettledAmountResult==null && other.getGetUnsettledAmountResult()==null) || 
             (this.getUnsettledAmountResult!=null &&
              this.getUnsettledAmountResult.equals(other.getGetUnsettledAmountResult())));
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
        if (getGetUnsettledAmountResult() != null) {
            _hashCode += getGetUnsettledAmountResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetUnsettledAmountResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetUnsettledAmountResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getUnsettledAmountResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetUnsettledAmountResult"));
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
