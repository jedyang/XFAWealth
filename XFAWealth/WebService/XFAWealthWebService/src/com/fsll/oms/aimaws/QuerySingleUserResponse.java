/**
 * QuerySingleUserResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class QuerySingleUserResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult querySingleUserResult;

    public QuerySingleUserResponse() {
    }

    public QuerySingleUserResponse(
           com.fsll.oms.aimaws.WSDSResult querySingleUserResult) {
           this.querySingleUserResult = querySingleUserResult;
    }


    /**
     * Gets the querySingleUserResult value for this QuerySingleUserResponse.
     * 
     * @return querySingleUserResult
     */
    public com.fsll.oms.aimaws.WSDSResult getQuerySingleUserResult() {
        return querySingleUserResult;
    }


    /**
     * Sets the querySingleUserResult value for this QuerySingleUserResponse.
     * 
     * @param querySingleUserResult
     */
    public void setQuerySingleUserResult(com.fsll.oms.aimaws.WSDSResult querySingleUserResult) {
        this.querySingleUserResult = querySingleUserResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QuerySingleUserResponse)) return false;
        QuerySingleUserResponse other = (QuerySingleUserResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.querySingleUserResult==null && other.getQuerySingleUserResult()==null) || 
             (this.querySingleUserResult!=null &&
              this.querySingleUserResult.equals(other.getQuerySingleUserResult())));
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
        if (getQuerySingleUserResult() != null) {
            _hashCode += getQuerySingleUserResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QuerySingleUserResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QuerySingleUserResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("querySingleUserResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QuerySingleUserResult"));
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
