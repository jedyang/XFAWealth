/**
 * GetHoldingResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetHoldingResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult getHoldingResult;

    public GetHoldingResponse() {
    }

    public GetHoldingResponse(
           com.fsll.oms.aimaws.WSDSResult getHoldingResult) {
           this.getHoldingResult = getHoldingResult;
    }


    /**
     * Gets the getHoldingResult value for this GetHoldingResponse.
     * 
     * @return getHoldingResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGetHoldingResult() {
        return getHoldingResult;
    }


    /**
     * Sets the getHoldingResult value for this GetHoldingResponse.
     * 
     * @param getHoldingResult
     */
    public void setGetHoldingResult(com.fsll.oms.aimaws.WSDSResult getHoldingResult) {
        this.getHoldingResult = getHoldingResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetHoldingResponse)) return false;
        GetHoldingResponse other = (GetHoldingResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getHoldingResult==null && other.getGetHoldingResult()==null) || 
             (this.getHoldingResult!=null &&
              this.getHoldingResult.equals(other.getGetHoldingResult())));
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
        if (getGetHoldingResult() != null) {
            _hashCode += getGetHoldingResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetHoldingResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetHoldingResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getHoldingResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetHoldingResult"));
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
