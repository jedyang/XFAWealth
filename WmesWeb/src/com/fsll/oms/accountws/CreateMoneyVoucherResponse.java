/**
 * CreateMoneyVoucherResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.accountws;

public class CreateMoneyVoucherResponse  implements java.io.Serializable {
    private com.fsll.oms.accountws.WSDSResult createMoneyVoucherResult;

    public CreateMoneyVoucherResponse() {
    }

    public CreateMoneyVoucherResponse(
           com.fsll.oms.accountws.WSDSResult createMoneyVoucherResult) {
           this.createMoneyVoucherResult = createMoneyVoucherResult;
    }


    /**
     * Gets the createMoneyVoucherResult value for this CreateMoneyVoucherResponse.
     * 
     * @return createMoneyVoucherResult
     */
    public com.fsll.oms.accountws.WSDSResult getCreateMoneyVoucherResult() {
        return createMoneyVoucherResult;
    }


    /**
     * Sets the createMoneyVoucherResult value for this CreateMoneyVoucherResponse.
     * 
     * @param createMoneyVoucherResult
     */
    public void setCreateMoneyVoucherResult(com.fsll.oms.accountws.WSDSResult createMoneyVoucherResult) {
        this.createMoneyVoucherResult = createMoneyVoucherResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateMoneyVoucherResponse)) return false;
        CreateMoneyVoucherResponse other = (CreateMoneyVoucherResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createMoneyVoucherResult==null && other.getCreateMoneyVoucherResult()==null) || 
             (this.createMoneyVoucherResult!=null &&
              this.createMoneyVoucherResult.equals(other.getCreateMoneyVoucherResult())));
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
        if (getCreateMoneyVoucherResult() != null) {
            _hashCode += getCreateMoneyVoucherResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateMoneyVoucherResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">CreateMoneyVoucherResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createMoneyVoucherResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "CreateMoneyVoucherResult"));
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
