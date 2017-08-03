/**
 * UpdateMembersResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class UpdateMembersResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSResult updateMembersResult;

    public UpdateMembersResponse() {
    }

    public UpdateMembersResponse(
           com.fsll.oms.aimaws.WSResult updateMembersResult) {
           this.updateMembersResult = updateMembersResult;
    }


    /**
     * Gets the updateMembersResult value for this UpdateMembersResponse.
     * 
     * @return updateMembersResult
     */
    public com.fsll.oms.aimaws.WSResult getUpdateMembersResult() {
        return updateMembersResult;
    }


    /**
     * Sets the updateMembersResult value for this UpdateMembersResponse.
     * 
     * @param updateMembersResult
     */
    public void setUpdateMembersResult(com.fsll.oms.aimaws.WSResult updateMembersResult) {
        this.updateMembersResult = updateMembersResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateMembersResponse)) return false;
        UpdateMembersResponse other = (UpdateMembersResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.updateMembersResult==null && other.getUpdateMembersResult()==null) || 
             (this.updateMembersResult!=null &&
              this.updateMembersResult.equals(other.getUpdateMembersResult())));
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
        if (getUpdateMembersResult() != null) {
            _hashCode += getUpdateMembersResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdateMembersResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">UpdateMembersResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updateMembersResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "UpdateMembersResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "WSResult"));
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
