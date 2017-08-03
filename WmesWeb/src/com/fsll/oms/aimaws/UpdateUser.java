/**
 * UpdateUser.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class UpdateUser  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String session;

    private com.fsll.oms.aimaws.UpdateUserTable table;

    public UpdateUser() {
    }

    public UpdateUser(
           java.lang.String id,
           java.lang.String session,
           com.fsll.oms.aimaws.UpdateUserTable table) {
           this.id = id;
           this.session = session;
           this.table = table;
    }


    /**
     * Gets the id value for this UpdateUser.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this UpdateUser.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the session value for this UpdateUser.
     * 
     * @return session
     */
    public java.lang.String getSession() {
        return session;
    }


    /**
     * Sets the session value for this UpdateUser.
     * 
     * @param session
     */
    public void setSession(java.lang.String session) {
        this.session = session;
    }


    /**
     * Gets the table value for this UpdateUser.
     * 
     * @return table
     */
    public com.fsll.oms.aimaws.UpdateUserTable getTable() {
        return table;
    }


    /**
     * Sets the table value for this UpdateUser.
     * 
     * @param table
     */
    public void setTable(com.fsll.oms.aimaws.UpdateUserTable table) {
        this.table = table;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateUser)) return false;
        UpdateUser other = (UpdateUser) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.session==null && other.getSession()==null) || 
             (this.session!=null &&
              this.session.equals(other.getSession()))) &&
            ((this.table==null && other.getTable()==null) || 
             (this.table!=null &&
              this.table.equals(other.getTable())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getSession() != null) {
            _hashCode += getSession().hashCode();
        }
        if (getTable() != null) {
            _hashCode += getTable().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdateUser.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">UpdateUser"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("session");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "session"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("table");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "table"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">>UpdateUser>table"));
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
