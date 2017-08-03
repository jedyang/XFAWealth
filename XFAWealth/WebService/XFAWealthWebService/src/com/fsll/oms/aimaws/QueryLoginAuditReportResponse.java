/**
 * QueryLoginAuditReportResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class QueryLoginAuditReportResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult queryLoginAuditReportResult;

    public QueryLoginAuditReportResponse() {
    }

    public QueryLoginAuditReportResponse(
           com.fsll.oms.aimaws.WSDSResult queryLoginAuditReportResult) {
           this.queryLoginAuditReportResult = queryLoginAuditReportResult;
    }


    /**
     * Gets the queryLoginAuditReportResult value for this QueryLoginAuditReportResponse.
     * 
     * @return queryLoginAuditReportResult
     */
    public com.fsll.oms.aimaws.WSDSResult getQueryLoginAuditReportResult() {
        return queryLoginAuditReportResult;
    }


    /**
     * Sets the queryLoginAuditReportResult value for this QueryLoginAuditReportResponse.
     * 
     * @param queryLoginAuditReportResult
     */
    public void setQueryLoginAuditReportResult(com.fsll.oms.aimaws.WSDSResult queryLoginAuditReportResult) {
        this.queryLoginAuditReportResult = queryLoginAuditReportResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryLoginAuditReportResponse)) return false;
        QueryLoginAuditReportResponse other = (QueryLoginAuditReportResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryLoginAuditReportResult==null && other.getQueryLoginAuditReportResult()==null) || 
             (this.queryLoginAuditReportResult!=null &&
              this.queryLoginAuditReportResult.equals(other.getQueryLoginAuditReportResult())));
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
        if (getQueryLoginAuditReportResult() != null) {
            _hashCode += getQueryLoginAuditReportResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryLoginAuditReportResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryLoginAuditReportResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryLoginAuditReportResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QueryLoginAuditReportResult"));
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
