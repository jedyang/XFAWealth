package com.fsll.wmes.notice.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.entity.Notice;
import com.fsll.wmes.notice.vo.NoticeVO;

public interface NoticeService {

	/**
	 *  系统公告数据获取
	 * @author wwluo
	 * @date 2017-06-05
	 * @param jsonPaging
	 * @param noticeVO 查询数据过滤VO
	 * @return jsonPaging
	 */
	public JsonPaging getSysNotices(JsonPaging jsonPaging, NoticeVO noticeVO);

	/**
	 *  根据ID获取公告实体
	 * @author wwluo
	 * @date 2017-06-05
	 * @param id
	 * @return Notice
	 */
	public Notice findById(String id);

	/**
	 *  根据ID获取公告VO
	 * @author wwluo
	 * @date 2017-06-05
	 * @param id
	 * @param langCode
	 * @return NoticeVO
	 */
	public NoticeVO findNoticeVOById(String id, String langCode);

	/**
	 *  业务公告数据获取
	 * @author wwluo
	 * @date 2017-06-05
	 * @param jsonPaging
	 * @param noticeVO 查询数据过滤VO
	 * @param langCode 多语言标识
	 * @return jsonPaging
	 */
	public JsonPaging getConsoleNotices(JsonPaging jsonPaging, NoticeVO noticeVO, String langCode);

	/**
	 *  公告附件保存
	 * @author wwluo
	 * @date 2017-06-05
	 * @param admin 当前管理员
	 * @param id 公告ID
	 * @param filePath 附件路径
	 * @param orifilename 附件名称
	 */
	public AccessoryFile saveNoticeDoc(SysAdmin admin, String id, String filePath,
			String orifilename);

	/**
	 *  公告附件删除
	 * @author wwluo
	 * @date 2017-06-05
	 */
	public Boolean delNoticeDoc(String accessoryfileId);

	/**
	 *  保存公告
	 * @author wwluo
	 * @date 2017-06-05
	 */
	public Notice save(Notice notice);

}
