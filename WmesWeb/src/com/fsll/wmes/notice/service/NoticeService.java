package com.fsll.wmes.notice.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.Notice;
import com.fsll.wmes.notice.vo.NoticeFilterVO;
import com.fsll.wmes.notice.vo.NoticeVO;


public interface NoticeService {

	/**
	 * 公告列表数据获取
	 * @author wwluo
	 * @data 2017-03-14
	 * @param jsonPaging
	 * @param noticeFilter 列表过滤条件
	 * @param langCode 多语言参数
	 * @param dateFormat 时间显示个性化格式
	 * @return
	 */
	public JsonPaging getNoticeJson(JsonPaging jsonPaging, NoticeFilterVO noticeFilter,
			String langCode, String dateFormat);

	/**
	 * 获取一条公告信息
	 * @author wwluo
	 * @data 2017-03-15
	 * @param noticeId 公告Id
	 * @return
	 */
	public Notice getNoticeById(String noticeId);

	/**
	 * 保存公告信息
	 * @author wwluo
	 * @data 2017-03-15
	 * @param notice 公告实体
	 * @return
	 */
	public Notice saveNotice(Notice notice);

	/**
	 * 删除公告
	 * @author wwluo
	 * @data 2017-03-16
	 * @param 
	 * @return
	 */
	public void deleteNotice(String id);

	/**
	 * 获取公告信息 Announcement Fund
	 * @author wwluo
	 * @data 2017-03-16
	 * @param jsonPaging
	 * @param loginUser
	 * @return
	 */
	public JsonPaging getAnnouncementFund(JsonPaging jsonPaging, MemberBase loginUser, String langCode);
	
	/**
	 * 获取公告信息 Announcement System
	 * @author wwluo
	 * @data 2017-03-16
	 * @param jsonPaging
	 * @param loginUser 当前登录用户
	 * @return
	 */
	public JsonPaging getAnnouncementSys(JsonPaging jsonPaging, MemberBase loginUser, String langCode);

	/**
	 * 获取公告VO实体
	 * @author wwluo
	 * @data 2017-03-16
	 * @param jsonPaging
	 * @param loginUser 当前登录用户
	 * @return
	 */
	public NoticeVO getNoticeVOById(String id, MemberBase loginUser,
			String langCode);

	/**
	 * 公告数据获取(当前member有权限查看的)
	 * @author wwluo
	 * @data 2017-03-14
	 * @param loginUser
	 * @param langCode 多语言参数
	 * @param maxResult 限定数据数量
	 * @return
	 */
	public List<NoticeVO> getMyNotices(MemberBase loginUser, String langCode,
			Integer maxResult);

	/**
	 *  公告附件保存
	 * @author wwluo
	 * @date 2017-06-05
	 * @param admin 当前管理员
	 * @param id 公告ID
	 * @param filePath 附件路径
	 * @param orifilename 附件名称
	 */
	public AccessoryFile saveNoticeDoc(MemberBase loginUser, String id,
			String filePath, String orifilename);

	/**
	 *  公告附件删除
	 * @author wwluo
	 * @date 2017-06-05
	 */
	public Boolean delNoticeDoc(String accessoryfileId);

}
