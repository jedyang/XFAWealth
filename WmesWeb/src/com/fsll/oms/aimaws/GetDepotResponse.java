/**
 * GetDepotResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class GetDepotResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult getDepotResult;

    public GetDepotResponse() {
    }

    public GetDepotResponse(
           com.fsll.oms.aimaws.WSDSResult getDepotResult) {
           this.getDepotResult = getDepotResult;
    }


    /**
     * Gets the getDepotResult value for this GetDepotResponse.
     * 
     * @return getDepotResult
     */
    public com.fsll.oms.aimaws.WSDSResult getGetDepotResult() {
        return getDepotResult;
    }


    /**
     * Sets the getDepotResult value for this GetDepotResponse.
     * 
     * @param getDepotResult
     */
    public void setGetDepotResult(com.fsll.oms.aimaws.WSDSResult getDepotResult) {
        this.getDepotResult = getDepotResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetDepotResponse)) return false;
        GetDepotResponse other = (GetDepotResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getDepotResult==null && other.getGetDepotResult()==null) || 
             (this.getDepotResult!=null &&
              this.getDepotResult.equals(other.getGetDepotResult())));
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
        if (getGetDepotResult() != null) {
            _hashCode += getGetDepotResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetDepotResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetDepotResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getDepotResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetDepotResult"));
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
