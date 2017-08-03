/**
 * UpdateClientResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.accountws;

public class UpdateClientResponse  implements java.io.Serializable {
    private com.fsll.oms.accountws.WSDSResult updateClientResult;

    public UpdateClientResponse() {
    }

    public UpdateClientResponse(
           com.fsll.oms.accountws.WSDSResult updateClientResult) {
           this.updateClientResult = updateClientResult;
    }


    /**
     * Gets the updateClientResult value for this UpdateClientResponse.
     * 
     * @return updateClientResult
     */
    public com.fsll.oms.accountws.WSDSResult getUpdateClientResult() {
        return updateClientResult;
    }


    /**
     * Sets the updateClientResult value for this UpdateClientResponse.
     * 
     * @param updateClientResult
     */
    public void setUpdateClientResult(com.fsll.oms.accountws.WSDSResult updateClientResult) {
        this.updateClientResult = updateClientResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateClientResponse)) return false;
        UpdateClientResponse other = (UpdateClientResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.updateClientResult==null && other.getUpdateClientResult()==null) || 
             (this.updateClientResult!=null &&
              this.updateClientResult.equals(other.getUpdateClientResult())));
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
        if (getUpdateClientResult() != null) {
            _hashCode += getUpdateClientResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdateClientResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">UpdateClientResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updateClientResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "UpdateClientResult"));
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
