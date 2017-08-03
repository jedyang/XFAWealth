package com.fsll.wmes.chat.dao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fsll.wmes.chat.pojo.BigGroup;
import com.fsll.wmes.chat.pojo.FriendGroup;
import com.fsll.wmes.chat.pojo.StatusUser;
import com.fsll.wmes.chat.pojo.User;



/**
 * Created by mjhuang
 */
public class SQLHelper {
	
    private String connectionString;
    private String dbUserName;
    private String dbUserPwd;
    private String driverName="com.mysql.jdbc.Driver";

    //无参构造函数
    public SQLHelper(){
        connectionString = "jdbc:mysql://192.168.138.71:3306/wmes_web_db?characterEncoding=UTF-8";
        dbUserName = "root";
        dbUserPwd = "p#ssw0rd";
    }

    /*
     *获取连接
     * */
    private Connection getConnection(){
        Connection connection = null;
        try{
        	Class.forName(driverName);
            connection = DriverManager.getConnection(connectionString,dbUserName,dbUserPwd);
            return connection;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 执行sql语句 UPDATE  INSERT  DELETE
     * */
    public boolean executeNonquery(String sql,Map<Integer,Object> params){
        //获取连接
        Connection connection = getConnection();
        if(connection == null) return false;
        PreparedStatement statement = prepare(connection,sql,params);
        int result = 0;
        try {
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeAll(connection,statement);
        }
        return result > 0;
    }
    
    public boolean executeNonquery(String sql){
        return executeNonquery(sql,null);
    }

    /*
    * 查询ResultSet  结果集
    */
    public Object queryResult(String sql, Map<Integer,Object> params,IResultSetOperate operate){
        Connection connection = getConnection();
        PreparedStatement statement = prepare(connection,sql,params);
        try {
            ResultSet resultSet = statement.executeQuery();
            //想要获取相应的结果，需要实现IResultOperate接口
            Object object =  operate.getObject(resultSet);
            closeAll(connection,statement);
            return object;
        }catch (SQLException ex) {
            ex.printStackTrace();
            closeAll(connection, statement);
            return null;
        }
    }

    public Object queryManyResult(String sql, Map<Integer,Object> params, IResultSetOperate operate){
        Connection connection = getConnection();
        PreparedStatement statement = prepare(connection,sql,params);
        try {
            //想要获取相应的结果，需要实现IResultOperate接口
            Object object =  operate.getObject(statement);
            closeAll(connection,statement);
            return object;
        }catch (Exception ex){
            ex.printStackTrace();
            closeAll(connection,statement);
            return null;
        }
    }

    private PreparedStatement prepare(Connection connection,String sql,Map<Integer,Object> params){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            if(params != null){
                for(int i = 0;i<params.size();i++){
                    statement.setObject(i+1, params.get(i+1));
                }
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 关闭
     */
    private void closeAll(Connection connection,PreparedStatement statement){
        try {
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * 查询组的owner
     */
     public User queryOwner(String gId){
         Connection connection = getConnection();
         String sql = "select u.id as uid,u.im_nick_name as nickname,u.highlight as sign,u.im_icon_url as avatar from member_base u,layim_group g where u.id=g.uid and g.gid=?";
         Map params = new HashMap();
         params.put(1, gId);
         PreparedStatement statement = prepare(connection,sql,params);
         try {
             ResultSet resultSet = statement.executeQuery();
             User owner = new User();
             while (resultSet.next()){
            	 owner = new User();
            	 owner.setId(resultSet.getString(1));
            	 owner.setUsername(resultSet.getString(2));
            	 owner.setSign(resultSet.getString(3));
            	 owner.setAvatar(resultSet.getString(4));
             }
             closeAll(connection,statement);
             return owner;
         }catch (SQLException ex) {
             ex.printStackTrace();
             closeAll(connection, statement);
             return null;
         }
     }
    
     /*
      * 查询组成员
      */
      public List<User> queryMember(String gId){
          Connection connection = getConnection();
          String sql = "select u.id as uid,u.im_nick_name as nickname,u.highlight as sign,u.im_icon_url as avatar from member_base u,layim_group_detail g where u.id=g.uid and g.gid=?";

          Map params = new HashMap();
          params.put(1,gId);
          PreparedStatement statement = prepare(connection,sql,params);
          try {
              ResultSet resultSet = statement.executeQuery();
              List<User> users = new ArrayList<User>();
              while (resultSet.next()){
            	 User user = new User();
            	 user.setId(resultSet.getString(1));
            	 user.setUsername(resultSet.getString(2));
            	 user.setSign(resultSet.getString(3));
            	 user.setAvatar(resultSet.getString(4));
            	 users.add(user);
              }
              closeAll(connection,statement);
              return users;
          }catch (SQLException ex) {
              ex.printStackTrace();
              closeAll(connection, statement);
              return null;
          }
      }
     
    /*
     * 查询Mine结果集
     */
     public StatusUser queryMine(String userId){
         Connection connection = getConnection();
         String sql = "select u.id as uid,u.nick_name as nickname,u.highlight as sign,u.icon_url as avatar,u.im_status as status from member_base u where u.id=?";
         Map params = new HashMap();
         params.put(1, userId);
         PreparedStatement statement = prepare(connection,sql,params);
         try {
             ResultSet resultSet = statement.executeQuery();
             StatusUser user = null;
             while (resultSet.next()){
                 user = new StatusUser();
                 user.setId(resultSet.getString(1));
                 user.setUsername(resultSet.getString(2));
                 user.setSign(resultSet.getString(3));
                 user.setAvatar(resultSet.getString(4));
                 user.setStatus(resultSet.getString(5));
             }
             closeAll(connection,statement);
             return user;
         }catch (SQLException ex) {
             ex.printStackTrace();
             closeAll(connection, statement);
             return null;
         }
     }
     
     /*
      * 查询朋友分组集
      */
      public List<FriendGroup> queryFriendGroup(String userId){
          Connection connection = getConnection();
          String sql = "select gid,groupname,avatar from layim_group where grouptype='f' and uid= ? ";
          Map params = new HashMap();
          params.put(1, userId);
          PreparedStatement statement = prepare(connection,sql,params);
          try {
              ResultSet rsGroup = statement.executeQuery();
              List<FriendGroup> friendGroups = new ArrayList<FriendGroup>();
              while (rsGroup.next()) {
                  FriendGroup friendGroup = new FriendGroup();
                  String gid = rsGroup.getString(1);
                  friendGroup.setId(gid);
                  friendGroup.setGroupname(rsGroup.getString(2));
                  friendGroups.add(friendGroup);
              }
              closeAll(connection,statement);
              return friendGroups;
          }catch (SQLException ex) {
              ex.printStackTrace();
              closeAll(connection, statement);
              return null;
          }
      }
     
     /*
      * 查询朋友分组集
      */
      public List<StatusUser> queryFriends(String userId){
          Connection connection = getConnection();
          String sql = "select u.id as uid,d.gid,u.im_nick_name as nickname,u.highlight as sign,u.im_icon_url as avatar,u.im_status as status from layim_group g,layim_group_detail d,member_base u where g.gid=d.gid and d.uid=u.id and g.uid = ? ";
          Map params = new HashMap();
          params.put(1, userId);
          PreparedStatement statement = prepare(connection,sql,params);
          try {
              ResultSet rsDetail = statement.executeQuery();
              List<StatusUser> friends = new ArrayList<StatusUser>();
              while (rsDetail.next()) {
            	  StatusUser u = new StatusUser();
                  u.setId(rsDetail.getString(1));
                  u.setFgid(rsDetail.getString(2));
                  u.setUsername(rsDetail.getString(3));
                  u.setSign(rsDetail.getString(4));
                  u.setAvatar(rsDetail.getString(5));
                  u.setStatus(rsDetail.getString(6));
                  friends.add(u);
              }
              closeAll(connection,statement);
              return friends;
          }catch (SQLException ex) {
              ex.printStackTrace();
              closeAll(connection, statement);
              return null;
          }
      }
      
      /*
       * 查询大分组集
       */
       public List<BigGroup> queryGroup(String userId){
           Connection connection = getConnection();
           String sql = "select gid,groupname,avatar from layim_group where grouptype='g' and uid= ? ";
           Map params = new HashMap();
           params.put(1, userId);
           PreparedStatement statement = prepare(connection,sql,params);
           try {
               ResultSet rsGroup = statement.executeQuery();
               List<BigGroup> bigGroups = new ArrayList<BigGroup>();
               while (rsGroup.next()) {
                   BigGroup group = new BigGroup();
                   group.setId(rsGroup.getString(1));
                   group.setGroupname(rsGroup.getString(2));
                   group.setAvatar(rsGroup.getString(3));
                   bigGroups.add(group);
               }
               closeAll(connection,statement);
               return bigGroups;
           }catch (SQLException ex) {
               ex.printStackTrace();
               closeAll(connection, statement);
               return null;
           }
       }
       
}

