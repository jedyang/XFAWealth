/**
 * CreateClientResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.accountws;

public class CreateClientResponse  implements java.io.Serializable {
    private com.fsll.oms.accountws.WSDSResult createClientResult;

    public CreateClientResponse() {
    }

    public CreateClientResponse(
           com.fsll.oms.accountws.WSDSResult createClientResult) {
           this.createClientResult = createClientResult;
    }


    /**
     * Gets the createClientResult value for this CreateClientResponse.
     * 
     * @return createClientResult
     */
    public com.fsll.oms.accountws.WSDSResult getCreateClientResult() {
        return createClientResult;
    }


    /**
     * Sets the createClientResult value for this CreateClientResponse.
     * 
     * @param createClientResult
     */
    public void setCreateClientResult(com.fsll.oms.accountws.WSDSResult createClientResult) {
        this.createClientResult = createClientResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateClientResponse)) return false;
        CreateClientResponse other = (CreateClientResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createClientResult==null && other.getCreateClientResult()==null) || 
             (this.createClientResult!=null &&
              this.createClientResult.equals(other.getCreateClientResult())));
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
        if (getCreateClientResult() != null) {
            _hashCode += getCreateClientResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateClientResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">CreateClientResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createClientResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "CreateClientResult"));
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
