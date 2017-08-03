/**
 * GenerateUserIDResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GenerateUserIDResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult generateUserIDResult;

    public GenerateUserIDResponse() {
    }

    public GenerateUserIDResponse(
           com.fsll.oms.aimaws.WSDSResult generateUserIDResult) {
           this.generateUserIDResult = generateUserIDResult;
    }


    /**
     * Gets the generateUserIDResult value for this GenerateUserIDResponse.
     * 
     * @return generateUserIDResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGenerateUserIDResult() {
        return generateUserIDResult;
    }


    /**
     * Sets the generateUserIDResult value for this GenerateUserIDResponse.
     * 
     * @param generateUserIDResult
     */
    public void setGenerateUserIDResult(com.fsll.oms.aimaws.WSDSResult generateUserIDResult) {
        this.generateUserIDResult = generateUserIDResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GenerateUserIDResponse)) return false;
        GenerateUserIDResponse other = (GenerateUserIDResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.generateUserIDResult==null && other.getGenerateUserIDResult()==null) || 
             (this.generateUserIDResult!=null &&
              this.generateUserIDResult.equals(other.getGenerateUserIDResult())));
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
        if (getGenerateUserIDResult() != null) {
            _hashCode += getGenerateUserIDResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GenerateUserIDResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GenerateUserIDResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generateUserIDResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GenerateUserIDResult"));
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
