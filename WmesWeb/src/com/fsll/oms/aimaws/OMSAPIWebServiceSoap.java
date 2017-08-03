/**
 * OMSAPIWebServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fsll.oms.aimaws;

public interface OMSAPIWebServiceSoap extends java.rmi.Remote {
    public com.fsll.oms.aimaws.WSLogonResult logon(java.lang.String id, java.lang.String password) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult logoff(java.lang.String id, java.lang.String sessionID) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryUser(java.lang.String id, java.lang.String sessionID, java.lang.String user, boolean approved) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult querySingleUser(java.lang.String id, java.lang.String sessionID, java.lang.String user) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult createUser(java.lang.String id, java.lang.String sessionID, com.fsll.oms.aimaws.CreateUserTable table) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult updateUser(java.lang.String id, java.lang.String session, com.fsll.oms.aimaws.UpdateUserTable table) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult approveUser(java.lang.String id, java.lang.String session, java.lang.String user, boolean approve) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult generateUserID(java.lang.String id, java.lang.String session, java.lang.String branchCode, java.lang.String roleType) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult generatePassword(java.lang.String id, java.lang.String session) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult resetPIN(java.lang.String id, java.lang.String session, java.lang.String userType, java.lang.String targetID) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult resetPassword(java.lang.String id, java.lang.String session, java.lang.String userType, java.lang.String targetID) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryMembers(java.lang.String id, java.lang.String sessionID, java.lang.String user) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult activateAccount(java.lang.String id, java.lang.String sessionID, java.lang.String targetID, java.lang.String userType, int state) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult suspendTrading(java.lang.String id, java.lang.String sessionID, java.lang.String targetID, java.lang.String userType, boolean suspend) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryClient(java.lang.String id, java.lang.String sessionID, java.lang.String user) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryAccountHistory(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.util.Calendar fromDate, java.util.Calendar toDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryTradeHistory(java.lang.String id, java.lang.String sessionID, java.util.Calendar fromDate, java.util.Calendar toDate, java.lang.String clientCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryFee(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.lang.String classCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryLoginAuditReport(java.lang.String id, java.lang.String sessionID, java.lang.String reportUser, java.util.Calendar fromDate, java.util.Calendar toDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryAccountAuditReport(java.lang.String id, java.lang.String sessionID, java.util.Calendar fromDate, java.util.Calendar toDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryAuditReport(java.lang.String id, java.lang.String sessionID, java.lang.String reportUser, java.util.Calendar fromDate, java.util.Calendar toDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryOperationLog(java.lang.String id, java.lang.String sessionID, java.util.Calendar fromDate, java.util.Calendar toDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult updateMembers(java.lang.String id, java.lang.String sessionID, com.fsll.oms.aimaws.UpdateMembersTable table) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryAllowableAssets(java.lang.String id, java.lang.String sessionID) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryAllowableMarkets(java.lang.String id, java.lang.String sessionID) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult queryTradingAllowable(java.lang.String id, java.lang.String sessionID, java.lang.String approved) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult createTradingAllowable(java.lang.String id, java.lang.String sessionID, com.fsll.oms.aimaws.CreateTradingAllowableTable table) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSResult removeTradingAllowable(java.lang.String id, java.lang.String sessionID, com.fsll.oms.aimaws.RemoveTradingAllowableTable table) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getBalance(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getBranch(java.lang.String id, java.lang.String sessionID, java.lang.String branchCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getBroker(java.lang.String id, java.lang.String sessionID, java.lang.String brokerCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getBrokerTrade(java.lang.String id, java.lang.String sessionID, java.lang.String brokerCode, java.util.Calendar fromTradeDate, java.util.Calendar toTradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getDepot(java.lang.String id, java.lang.String sessionID, java.lang.String depotCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getDividend(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.lang.String runnerCode, java.util.Calendar fromTradeDate, java.util.Calendar toTradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getDistribute(java.lang.String id, java.lang.String sessionID, java.lang.String productCode, java.util.Calendar fromTradeDate, java.util.Calendar toTradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getExchangeRate(java.lang.String id, java.lang.String sessionID, java.lang.String cur, java.util.Calendar tradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getClientBank(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.lang.String cur) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getHolding(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getInterest(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.util.Calendar fromTradeDate, java.util.Calendar toTradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getLedger(java.lang.String id, java.lang.String sessionID, java.lang.String ledgerCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getLocation(java.lang.String id, java.lang.String sessionID, java.lang.String location) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getMoneyVoucher(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.util.Calendar fromTradeDate, java.util.Calendar toTradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getStockVoucher(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.util.Calendar fromTradeDate, java.util.Calendar toTradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getRunner(java.lang.String id, java.lang.String sessionID, java.lang.String runnerCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getStock(java.lang.String id, java.lang.String sessionID, java.lang.String stockCode, java.lang.String stockType, java.lang.String tradable) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getTrade(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.util.Calendar fromTradeDate, java.util.Calendar toTradeDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getFund_Txn(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode, java.util.Calendar fromOrderDate, java.util.Calendar toOrderDate) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getUnsettledAmount(java.lang.String id, java.lang.String sessionID, java.lang.String clientCode) throws java.rmi.RemoteException;
    public com.fsll.oms.aimaws.WSDSResult getFundSuspend(java.lang.String id, java.lang.String sessionID, java.lang.String actionCode, java.lang.String fundCode) throws java.rmi.RemoteException;
}
