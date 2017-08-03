/**
 * RemoveTradingAllowableResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class RemoveTradingAllowableResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSResult removeTradingAllowableResult;

    public RemoveTradingAllowableResponse() {
    }

    public RemoveTradingAllowableResponse(
           com.fsll.oms.aimaws.WSResult removeTradingAllowableResult) {
           this.removeTradingAllowableResult = removeTradingAllowableResult;
    }


    /**
     * Gets the removeTradingAllowableResult value for this RemoveTradingAllowableResponse.
     * 
     * @return removeTradingAllowableResult
     */
    public com.fsll.oms.aimaws.WSResult getRemoveTradingAllowableResult() {
        return removeTradingAllowableResult;
    }


    /**
     * Sets the removeTradingAllowableResult value for this RemoveTradingAllowableResponse.
     * 
     * @param removeTradingAllowableResult
     */
    public void setRemoveTradingAllowableResult(com.fsll.oms.aimaws.WSResult removeTradingAllowableResult) {
        this.removeTradingAllowableResult = removeTradingAllowableResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RemoveTradingAllowableResponse)) return false;
        RemoveTradingAllowableResponse other = (RemoveTradingAllowableResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.removeTradingAllowableResult==null && other.getRemoveTradingAllowableResult()==null) || 
             (this.removeTradingAllowableResult!=null &&
              this.removeTradingAllowableResult.equals(other.getRemoveTradingAllowableResult())));
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
        if (getRemoveTradingAllowableResult() != null) {
            _hashCode += getRemoveTradingAllowableResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RemoveTradingAllowableResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">RemoveTradingAllowableResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("removeTradingAllowableResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "RemoveTradingAllowableResult"));
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
