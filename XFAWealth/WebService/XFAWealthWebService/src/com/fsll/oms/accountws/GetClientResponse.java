/**
 * GetClientResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.accountws;

public class GetClientResponse  implements java.io.Serializable {
    private com.fsll.oms.accountws.WSDSResult getClientResult;

    public GetClientResponse() {
    }

    public GetClientResponse(
           com.fsll.oms.accountws.WSDSResult getClientResult) {
           this.getClientResult = getClientResult;
    }


    /**
     * Gets the getClientResult value for this GetClientResponse.
     * 
     * @return getClientResult
     */
    public com.fsll.oms.accountws.WSDSResult getGetClientResult() {
        return getClientResult;
    }


    /**
     * Sets the getClientResult value for this GetClientResponse.
     * 
     * @param getClientResult
     */
    public void setGetClientResult(com.fsll.oms.accountws.WSDSResult getClientResult) {
        this.getClientResult = getClientResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetClientResponse)) return false;
        GetClientResponse other = (GetClientResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getClientResult==null && other.getGetClientResult()==null) || 
             (this.getClientResult!=null &&
              this.getClientResult.equals(other.getGetClientResult())));
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
        if (getGetClientResult() != null) {
            _hashCode += getGetClientResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetClientResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetClientResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getClientResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetClientResult"));
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
