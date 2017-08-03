package com.fsll.wmes.chat.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fsll.wmes.chat.dao.db.SQLHelper;
import com.fsll.wmes.chat.dao.operate.LayIMGetMemberIdsOperate;
import com.fsll.wmes.chat.pojo.BigGroup;
import com.fsll.wmes.chat.pojo.FriendGroup;
import com.fsll.wmes.chat.pojo.JsonResultType;
import com.fsll.wmes.chat.pojo.StatusUser;
import com.fsll.wmes.chat.pojo.User;
import com.fsll.wmes.chat.pojo.message.ToDBMessage;
import com.fsll.wmes.chat.pojo.result.BaseDataResult;
import com.fsll.wmes.chat.pojo.result.GroupMemberResult;
import com.fsll.wmes.chat.pojo.result.JsonResult;
import com.fsll.wmes.chat.pojo.result.JsonResultHelper;


/**
 * mjhuang
 */
public class LayIMDao {
    SQLHelper sqlHelper = new SQLHelper();
    //获取基本信息列表
    public JsonResult getBaseList(String userId) {
        if (userId == null){
            return JsonResultHelper.createFailedResult("invalid userid");
        }
        JsonResult jsonResult = new JsonResult();
        BaseDataResult result = new BaseDataResult();
    	//查询mine
        StatusUser mine = sqlHelper.queryMine(userId);
        if (mine == null) {
            return JsonResultHelper.createFailedResult("用户不存在");
        }
        result.setMine(mine);
        List<FriendGroup> friendGroups = sqlHelper.queryFriendGroup(userId);//friend
        List<StatusUser> users  = sqlHelper.queryFriends(userId);//查询用户
        List<BigGroup> groups = sqlHelper.queryGroup(userId); //group
        
        friendGroups = getFriendGroupList(friendGroups, users);
        result.setFriend(friendGroups);
        result.setGroup(groups);
        jsonResult.setCode(JsonResultType.TYPESUCCESS);
        jsonResult.setMsg("");
        jsonResult.setData(result);
        
        return jsonResult;
    }
        
    //处理分组和好友间的关系
    private List<FriendGroup> getFriendGroupList(List<FriendGroup> friendGroup, List<StatusUser> friends) {
        List<FriendGroup> list = new ArrayList<FriendGroup>();
        for (FriendGroup fg : friendGroup){
            List<StatusUser> users = new ArrayList<StatusUser>();
            int online = 0;
            for (StatusUser u : friends){
                if (fg.getId().equals(u.getFgid())) {
                	if("online".equals(u.getStatus())){
                		online ++ ;
                	}
                    users.add(u);
                }
            }
            fg.setOnline(online);
            fg.setList(users);
            list.add(fg);
        }
        return list;
    }

    //根据groupid获取群员列表
    public JsonResult getMemberList(String groupId){
        if (groupId == null){
            return JsonResultHelper.createFailedResult("invalid groupId");
        }
        GroupMemberResult groupMemberResult = new GroupMemberResult();
        List<User> users = sqlHelper.queryMember(groupId);
        User owner = sqlHelper.queryOwner(groupId);
        groupMemberResult.setOwner(owner);
        groupMemberResult.setMembers(users.size());
        groupMemberResult.setList(users);
        return JsonResultHelper.createSuccessResult("",groupMemberResult);
    }

    //根据群id获取所有群员的id
    public List<String> getMemberListOnlyIds(String groupId){
        Map params = new HashMap();
        params.put(1,groupId);
        String sql = "select member_id as userid from layim_group_detail where gid =?";
        Object object = sqlHelper.queryResult(sql,params,new LayIMGetMemberIdsOperate());
        return (List<String>) object;
    }

    //添加聊天记录
    public boolean addMsgRecord(ToDBMessage message){
    	String baseId = UUID.randomUUID().toString().replace("-", "");
        String sql = "insert into layim_msg_history (from_user,to_user,group_id,msg,chat_type,create_time,msg_type,timestamp,id)values (?,?,?,?,?,now(),?,?,?)";
        Map<Integer,Object> param = new HashMap<Integer,Object>();
        param.put(1,message.getSendUserId());
        param.put(2,message.getToUserId());
        param.put(3,message.getGroupId());
        param.put(4,message.getMsg());
        param.put(5,message.getChatType());
        param.put(6,message.getMsgType());
        param.put(7,message.getAddtime());
        param.put(8,baseId);
        return sqlHelper.executeNonquery(sql,param);
    }
    
}
