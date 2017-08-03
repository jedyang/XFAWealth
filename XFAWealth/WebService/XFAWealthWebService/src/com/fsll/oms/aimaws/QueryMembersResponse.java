/**
 * QueryMembersResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class QueryMembersResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult queryMembersResult;

    public QueryMembersResponse() {
    }

    public QueryMembersResponse(
           com.fsll.oms.aimaws.WSDSResult queryMembersResult) {
           this.queryMembersResult = queryMembersResult;
    }


    /**
     * Gets the queryMembersResult value for this QueryMembersResponse.
     * 
     * @return queryMembersResult
     */
    public com.fsll.oms.aimaws.WSDSResult getQueryMembersResult() {
        return queryMembersResult;
    }


    /**
     * Sets the queryMembersResult value for this QueryMembersResponse.
     * 
     * @param queryMembersResult
     */
    public void setQueryMembersResult(com.fsll.oms.aimaws.WSDSResult queryMembersResult) {
        this.queryMembersResult = queryMembersResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryMembersResponse)) return false;
        QueryMembersResponse other = (QueryMembersResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryMembersResult==null && other.getQueryMembersResult()==null) || 
             (this.queryMembersResult!=null &&
              this.queryMembersResult.equals(other.getQueryMembersResult())));
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
        if (getQueryMembersResult() != null) {
            _hashCode += getQueryMembersResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryMembersResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryMembersResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryMembersResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QueryMembersResult"));
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
