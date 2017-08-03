/**
 * OMSAPIWebServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.accountws;

public interface OMSAPIWebServiceSoap extends java.rmi.Remote {
    public com.fsll.oms.accountws.WSLogonResult logon(java.lang.String id, java.lang.String password) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSResult logoff(java.lang.String id, java.lang.String sessionID) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSResult resetPassword(java.lang.String id, java.lang.String session, java.lang.String userType, java.lang.String targetID) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSDSResult getExchangeRate(java.lang.String id, java.lang.String sessionID, java.lang.String cur, java.util.Calendar tradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSDSResult createClient(java.lang.String id, java.lang.String sessionID, com.fsll.oms.accountws.CreateClientTheTable theTable) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSDSResult updateClient(java.lang.String id, java.lang.String sessionID, com.fsll.oms.accountws.UpdateClientTheTable theTable) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSDSResult getClient(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSDSResult deleteClient(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSDSResult createMoneyVoucher(java.lang.String id, java.lang.String sessionID, com.fsll.oms.accountws.CreateMoneyVoucherTheTable theTable) throws java.rmi.RemoteException;
    public com.fsll.oms.accountws.WSDSResult getMoneyVoucher(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.util.Calendar fromTradeDate, java.util.Calendar toTradeDate) throws java.rmi.RemoteException;
}
