/**
 * CreateTradingAllowableResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class CreateTradingAllowableResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSResult createTradingAllowableResult;

    public CreateTradingAllowableResponse() {
    }

    public CreateTradingAllowableResponse(
           com.fsll.oms.aimaws.WSResult createTradingAllowableResult) {
           this.createTradingAllowableResult = createTradingAllowableResult;
    }


    /**
     * Gets the createTradingAllowableResult value for this CreateTradingAllowableResponse.
     * 
     * @return createTradingAllowableResult
     */
    public com.fsll.oms.aimaws.WSResult getCreateTradingAllowableResult() {
        return createTradingAllowableResult;
    }


    /**
     * Sets the createTradingAllowableResult value for this CreateTradingAllowableResponse.
     * 
     * @param createTradingAllowableResult
     */
    public void setCreateTradingAllowableResult(com.fsll.oms.aimaws.WSResult createTradingAllowableResult) {
        this.createTradingAllowableResult = createTradingAllowableResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateTradingAllowableResponse)) return false;
        CreateTradingAllowableResponse other = (CreateTradingAllowableResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createTradingAllowableResult==null && other.getCreateTradingAllowableResult()==null) || 
             (this.createTradingAllowableResult!=null &&
              this.createTradingAllowableResult.equals(other.getCreateTradingAllowableResult())));
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
        if (getCreateTradingAllowableResult() != null) {
            _hashCode += getCreateTradingAllowableResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateTradingAllowableResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">CreateTradingAllowableResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createTradingAllowableResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "CreateTradingAllowableResult"));
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
