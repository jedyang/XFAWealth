package com.fsll.wmes.chat.pojo.result;

import java.util.List;

import com.fsll.wmes.chat.pojo.BigGroup;
import com.fsll.wmes.chat.pojo.FriendGroup;
import com.fsll.wmes.chat.pojo.StatusUser;
import com.fsll.wmes.chat.vo.ChatHistoryVO;
import com.fsll.wmes.chat.vo.RecentContactsVO;


/**
 * Created by mjhuang
 * 这个类主要是对应于layim初始化的数据接口,里面包含mine\friend\group数据
 */
public class BaseDataResult {
    public StatusUser getMine() {
        return mine;
    }
    public void setMine(StatusUser mine) {
        this.mine = mine;
    }
    public List<FriendGroup> getFriend() {
        return friend;
    }
    public void setFriend(List<FriendGroup> friend) {
        this.friend = friend;
    }
    public List<BigGroup> getGroup() {
        return group;
    }
    public void setGroup(List<BigGroup> group) {
        this.group = group;
    }
    
    public List<RecentContactsVO> getHistory() {
		return history;
	}
	public void setHistory(List<RecentContactsVO> history) {
		this.history = history;
	}

	private StatusUser mine;
    private List<FriendGroup> friend;
    private List<BigGroup> group;
    private List<RecentContactsVO> history;
}
