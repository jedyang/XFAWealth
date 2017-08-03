/**
 * QueryClientResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class QueryClientResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult queryClientResult;

    public QueryClientResponse() {
    }

    public QueryClientResponse(
           com.fsll.oms.aimaws.WSDSResult queryClientResult) {
           this.queryClientResult = queryClientResult;
    }


    /**
     * Gets the queryClientResult value for this QueryClientResponse.
     * 
     * @return queryClientResult
     */
    public com.fsll.oms.aimaws.WSDSResult getQueryClientResult() {
        return queryClientResult;
    }


    /**
     * Sets the queryClientResult value for this QueryClientResponse.
     * 
     * @param queryClientResult
     */
    public void setQueryClientResult(com.fsll.oms.aimaws.WSDSResult queryClientResult) {
        this.queryClientResult = queryClientResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryClientResponse)) return false;
        QueryClientResponse other = (QueryClientResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryClientResult==null && other.getQueryClientResult()==null) || 
             (this.queryClientResult!=null &&
              this.queryClientResult.equals(other.getQueryClientResult())));
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
        if (getQueryClientResult() != null) {
            _hashCode += getQueryClientResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryClientResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryClientResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryClientResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QueryClientResult"));
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
