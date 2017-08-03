/**
 * ResetPasswordResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class ResetPasswordResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSResult resetPasswordResult;

    public ResetPasswordResponse() {
    }

    public ResetPasswordResponse(
           com.fsll.oms.aimaws.WSResult resetPasswordResult) {
           this.resetPasswordResult = resetPasswordResult;
    }


    /**
     * Gets the resetPasswordResult value for this ResetPasswordResponse.
     * 
     * @return resetPasswordResult
     */
    public com.fsll.oms.aimaws.WSResult getResetPasswordResult() {
        return resetPasswordResult;
    }


    /**
     * Sets the resetPasswordResult value for this ResetPasswordResponse.
     * 
     * @param resetPasswordResult
     */
    public void setResetPasswordResult(com.fsll.oms.aimaws.WSResult resetPasswordResult) {
        this.resetPasswordResult = resetPasswordResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResetPasswordResponse)) return false;
        ResetPasswordResponse other = (ResetPasswordResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.resetPasswordResult==null && other.getResetPasswordResult()==null) || 
             (this.resetPasswordResult!=null &&
              this.resetPasswordResult.equals(other.getResetPasswordResult())));
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
        if (getResetPasswordResult() != null) {
            _hashCode += getResetPasswordResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResetPasswordResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ResetPasswordResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resetPasswordResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ResetPasswordResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "WSResult"));
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
