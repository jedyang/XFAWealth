/**
 * ApproveUserResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class ApproveUserResponse  implements java.io.Serializable {
    private com.fsll.oms.aimaws.WSResult approveUserResult;

    public ApproveUserResponse() {
    }

    public ApproveUserResponse(
           com.fsll.oms.aimaws.WSResult approveUserResult) {
           this.approveUserResult = approveUserResult;
    }


    /**
     * Gets the approveUserResult value for this ApproveUserResponse.
     * 
     * @return approveUserResult
     */
    public com.fsll.oms.aimaws.WSResult getApproveUserResult() {
        return approveUserResult;
    }


    /**
     * Sets the approveUserResult value for this ApproveUserResponse.
     * 
     * @param approveUserResult
     */
    public void setApproveUserResult(com.fsll.oms.aimaws.WSResult approveUserResult) {
        this.approveUserResult = approveUserResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ApproveUserResponse)) return false;
        ApproveUserResponse other = (ApproveUserResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.approveUserResult==null && other.getApproveUserResult()==null) || 
             (this.approveUserResult!=null &&
              this.approveUserResult.equals(other.getApproveUserResult())));
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
        if (getApproveUserResult() != null) {
            _hashCode += getApproveUserResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ApproveUserResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ApproveUserResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approveUserResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ApproveUserResult"));
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
