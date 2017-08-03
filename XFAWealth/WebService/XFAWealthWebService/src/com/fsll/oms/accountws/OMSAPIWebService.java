/**
 * OMSAPIWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.accountws;

public interface OMSAPIWebService extends javax.xml.rpc.Service {
    public java.lang.String getOMSAPIWebServiceSoap12Address();

    public com.fsll.oms.accountws.OMSAPIWebServiceSoap getOMSAPIWebServiceSoap12() throws javax.xml.rpc.ServiceException;

    public com.fsll.oms.accountws.OMSAPIWebServiceSoap getOMSAPIWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getOMSAPIWebServiceSoapAddress();

    public com.fsll.oms.accountws.OMSAPIWebServiceSoap getOMSAPIWebServiceSoap() throws javax.xml.rpc.ServiceException;

    public com.fsll.oms.accountws.OMSAPIWebServiceSoap getOMSAPIWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
