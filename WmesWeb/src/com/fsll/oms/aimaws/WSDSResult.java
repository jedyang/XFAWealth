/**
 * WSDSResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class WSDSResult  extends com.fsll.oms.aimaws.WSResult  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSDSResultDataSet dataSet;

    public WSDSResult() {
    }

    public WSDSResult(
           int errorCode,
           java.lang.String errorMessage,
           com.fsll.oms.aimaws.WSDSResultDataSet dataSet) {
        super(
            errorCode,
            errorMessage);
        this.dataSet = dataSet;
    }


    /**
     * Gets the dataSet value for this WSDSResult.
     * 
     * @return dataSet
     */
    public com.fsll.oms.aimaws.WSDSResultDataSet getDataSet() {
        return dataSet;
    }


    /**
     * Sets the dataSet value for this WSDSResult.
     * 
     * @param dataSet
     */
    public void setDataSet(com.fsll.oms.aimaws.WSDSResultDataSet dataSet) {
        this.dataSet = dataSet;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WSDSResult)) return false;
        WSDSResult other = (WSDSResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.dataSet==null && other.getDataSet()==null) || 
             (this.dataSet!=null &&
              this.dataSet.equals(other.getDataSet())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getDataSet() != null) {
            _hashCode += getDataSet().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WSDSResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "WSDSResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "DataSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">WSDSResult>DataSet"));
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
