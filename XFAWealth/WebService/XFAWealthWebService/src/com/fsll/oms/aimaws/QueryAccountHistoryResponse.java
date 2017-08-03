/**
 * QueryAccountHistoryResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class QueryAccountHistoryResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult queryAccountHistoryResult;

    public QueryAccountHistoryResponse() {
    }

    public QueryAccountHistoryResponse(
           com.fsll.oms.aimaws.WSDSResult queryAccountHistoryResult) {
           this.queryAccountHistoryResult = queryAccountHistoryResult;
    }


    /**
     * Gets the queryAccountHistoryResult value for this QueryAccountHistoryResponse.
     * 
     * @return queryAccountHistoryResult
     */
    public com.fsll.oms.aimaws.WSDSResult getQueryAccountHistoryResult() {
        return queryAccountHistoryResult;
    }


    /**
     * Sets the queryAccountHistoryResult value for this QueryAccountHistoryResponse.
     * 
     * @param queryAccountHistoryResult
     */
    public void setQueryAccountHistoryResult(com.fsll.oms.aimaws.WSDSResult queryAccountHistoryResult) {
        this.queryAccountHistoryResult = queryAccountHistoryResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryAccountHistoryResponse)) return false;
        QueryAccountHistoryResponse other = (QueryAccountHistoryResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryAccountHistoryResult==null && other.getQueryAccountHistoryResult()==null) || 
             (this.queryAccountHistoryResult!=null &&
              this.queryAccountHistoryResult.equals(other.getQueryAccountHistoryResult())));
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
        if (getQueryAccountHistoryResult() != null) {
            _hashCode += getQueryAccountHistoryResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryAccountHistoryResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryAccountHistoryResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryAccountHistoryResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QueryAccountHistoryResult"));
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
