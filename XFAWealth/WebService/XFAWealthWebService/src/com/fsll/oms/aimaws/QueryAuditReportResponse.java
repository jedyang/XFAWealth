/**
 * QueryAuditReportResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class QueryAuditReportResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResult queryAuditReportResult;

    public QueryAuditReportResponse() {
    }

    public QueryAuditReportResponse(
           com.fsll.oms.aimaws.WSDSResult queryAuditReportResult) {
           this.queryAuditReportResult = queryAuditReportResult;
    }


    /**
     * Gets the queryAuditReportResult value for this QueryAuditReportResponse.
     * 
     * @return queryAuditReportResult
     */
    public com.fsll.oms.aimaws.WSDSResult getQueryAuditReportResult() {
        return queryAuditReportResult;
    }


    /**
     * Sets the queryAuditReportResult value for this QueryAuditReportResponse.
     * 
     * @param queryAuditReportResult
     */
    public void setQueryAuditReportResult(com.fsll.oms.aimaws.WSDSResult queryAuditReportResult) {
        this.queryAuditReportResult = queryAuditReportResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryAuditReportResponse)) return false;
        QueryAuditReportResponse other = (QueryAuditReportResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryAuditReportResult==null && other.getQueryAuditReportResult()==null) || 
             (this.queryAuditReportResult!=null &&
              this.queryAuditReportResult.equals(other.getQueryAuditReportResult())));
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
        if (getQueryAuditReportResult() != null) {
            _hashCode += getQueryAuditReportResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryAuditReportResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">QueryAuditReportResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryAuditReportResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QueryAuditReportResult"));
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
