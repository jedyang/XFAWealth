/**
 * GeneratePasswordResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GeneratePasswordResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult generatePasswordResult;

    public GeneratePasswordResponse() {
    }

    public GeneratePasswordResponse(
           com.fsll.oms.aimaws.WSDSResult generatePasswordResult) {
           this.generatePasswordResult = generatePasswordResult;
    }


    /**
     * Gets the generatePasswordResult value for this GeneratePasswordResponse.
     * 
     * @return generatePasswordResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGeneratePasswordResult() {
        return generatePasswordResult;
    }


    /**
     * Sets the generatePasswordResult value for this GeneratePasswordResponse.
     * 
     * @param generatePasswordResult
     */
    public void setGeneratePasswordResult(com.fsll.oms.aimaws.WSDSResult generatePasswordResult) {
        this.generatePasswordResult = generatePasswordResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GeneratePasswordResponse)) return false;
        GeneratePasswordResponse other = (GeneratePasswordResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.generatePasswordResult==null && other.getGeneratePasswordResult()==null) || 
             (this.generatePasswordResult!=null &&
              this.generatePasswordResult.equals(other.getGeneratePasswordResult())));
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
        if (getGeneratePasswordResult() != null) {
            _hashCode += getGeneratePasswordResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GeneratePasswordResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GeneratePasswordResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generatePasswordResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GeneratePasswordResult"));
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
