/**
 * OMSAPIWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public class OMSAPIWebServiceLocator extends org.apache.axis.client.Service implements com.fsll.oms.aimaws.OMSAPIWebService {

    public OMSAPIWebServiceLocator() {
    }


    public OMSAPIWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OMSAPIWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OMSAPIWebServiceSoap12
    private java.lang.String OMSAPIWebServiceSoap12_address = "http://210.177.6.141/OMSAPIWS/OMSAPIWS.asmx";

    public java.lang.String getOMSAPIWebServiceSoap12Address() {
        return OMSAPIWebServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OMSAPIWebServiceSoap12WSDDServiceName = "OMSAPIWebServiceSoap12";

    public java.lang.String getOMSAPIWebServiceSoap12WSDDServiceName() {
        return OMSAPIWebServiceSoap12WSDDServiceName;
    }

    public void setOMSAPIWebServiceSoap12WSDDServiceName(java.lang.String name) {
        OMSAPIWebServiceSoap12WSDDServiceName = name;
    }

    public com.fsll.oms.aimaws.OMSAPIWebServiceSoap getOMSAPIWebServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OMSAPIWebServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOMSAPIWebServiceSoap12(endpoint);
    }

    public com.fsll.oms.aimaws.OMSAPIWebServiceSoap getOMSAPIWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.fsll.oms.aimaws.OMSAPIWebServiceSoap12Stub _stub = new com.fsll.oms.aimaws.OMSAPIWebServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getOMSAPIWebServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOMSAPIWebServiceSoap12EndpointAddress(java.lang.String address) {
        OMSAPIWebServiceSoap12_address = address;
    }


    // Use to get a proxy class for OMSAPIWebServiceSoap
    private java.lang.String OMSAPIWebServiceSoap_address = "http://210.177.6.141/OMSAPIWS/OMSAPIWS.asmx";

    public java.lang.String getOMSAPIWebServiceSoapAddress() {
        return OMSAPIWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OMSAPIWebServiceSoapWSDDServiceName = "OMSAPIWebServiceSoap";

    public java.lang.String getOMSAPIWebServiceSoapWSDDServiceName() {
        return OMSAPIWebServiceSoapWSDDServiceName;
    }

    public void setOMSAPIWebServiceSoapWSDDServiceName(java.lang.String name) {
        OMSAPIWebServiceSoapWSDDServiceName = name;
    }

    public com.fsll.oms.aimaws.OMSAPIWebServiceSoap getOMSAPIWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OMSAPIWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOMSAPIWebServiceSoap(endpoint);
    }

    public com.fsll.oms.aimaws.OMSAPIWebServiceSoap getOMSAPIWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub _stub = new com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub(portAddress, this);
            _stub.setPortName(getOMSAPIWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOMSAPIWebServiceSoapEndpointAddress(java.lang.String address) {
        OMSAPIWebServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.fsll.oms.aimaws.OMSAPIWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.fsll.oms.aimaws.OMSAPIWebServiceSoap12Stub _stub = new com.fsll.oms.aimaws.OMSAPIWebServiceSoap12Stub(new java.net.URL(OMSAPIWebServiceSoap12_address), this);
                _stub.setPortName(getOMSAPIWebServiceSoap12WSDDServiceName());
                return _stub;
            }
            if (com.fsll.oms.aimaws.OMSAPIWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub _stub = new com.fsll.oms.aimaws.OMSAPIWebServiceSoapStub(new java.net.URL(OMSAPIWebServiceSoap_address), this);
                _stub.setPortName(getOMSAPIWebServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("OMSAPIWebServiceSoap12".equals(inputPortName)) {
            return getOMSAPIWebServiceSoap12();
        }
        else if ("OMSAPIWebServiceSoap".equals(inputPortName)) {
            return getOMSAPIWebServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "OMSAPIWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "OMSAPIWebServiceSoap12"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "OMSAPIWebServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OMSAPIWebServiceSoap12".equals(portName)) {
            setOMSAPIWebServiceSoap12EndpointAddress(address);
        }
        else 
if ("OMSAPIWebServiceSoap".equals(portName)) {
            setOMSAPIWebServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
