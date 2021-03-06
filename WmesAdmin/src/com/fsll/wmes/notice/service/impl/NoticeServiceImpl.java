package com.fsll.wmes.notice.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.Notice;
import com.fsll.wmes.notice.service.NoticeService;
import com.fsll.wmes.notice.vo.NoticeVO;

@Service("noticeService")
public class NoticeServiceImpl extends BaseService implements NoticeService {

	@Autowired
	private AccessoryFileService accessoryFileService;
	
	/**
	 *  系统公告数据获取
	 * @author wwluo
	 * @date 2017-06-05
	 * @param jsonPaging
	 * @param noticeVO 查询数据过滤VO
	 * @return jsonPaging
	 */
	@Override
	public JsonPaging getSysNotices(JsonPaging jsonPaging, NoticeVO noticeVO) {
		StringBuffer hql = new StringBuffer(""
				+ " FROM"
				+ " Notice n"
				+ " WHERE"
				+ " n.type=?"
				+ "");
		List<Object> params = new ArrayList<Object>();
		params.add("0");
		if (StringUtils.isNotBlank(noticeVO.getFilterTitle())) {
			hql.append(" AND n.subject LIKE ?");
			params.add("%" + noticeVO.getFilterTitle() + "%");
		}
		if (StringUtils.isNotBlank(noticeVO.getFilterContent())) {
			hql.append(" AND n.content LIKE ?");
			params.add("%" + noticeVO.getFilterContent() + "%");
		}
		if (StringUtils.isNotBlank(noticeVO.getFilterLevel())) {
			hql.append(" AND n.level=?");
			params.add(noticeVO.getFilterLevel());
		}
		if (StringUtils.isNotBlank(noticeVO.getFilterStartTime()) && StringUtils.isNotBlank(noticeVO.getFilterEndTime())) {
			hql.append(" AND n.releaseDate BETWEEN ? AND ?");
			params.add(DateUtil.StringToDate(noticeVO.getFilterStartTime(), CommonConstants.FORMAT_DATE_TIME));
			params.add(DateUtil.StringToDate(noticeVO.getFilterEndTime(), CommonConstants.FORMAT_DATE_TIME));
		}
		hql.append(" ORDER BY n.releaseDate DESC");
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			List<Notice> notices = jsonPaging.getList();
			List<NoticeVO> vos = new ArrayList<NoticeVO>();
			for (Notice notice : notices) {
				NoticeVO vo = new NoticeVO();
				BeanUtils.copyProperties(notice, vo);
				if(notice.getReleaseDate() != null){
					vo.setReleaseDateStr(DateUtil.dateToDateString(notice.getReleaseDate(), CommonConstants.FORMAT_DATE_TIME));
				}
				vos.add(vo);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}
		return jsonPaging;
	}

	/**
	 *  根据ID获取公告实体
	 * @author wwluo
	 * @date 2017-06-05
	 * @param id
	 * @return Notice
	 */
	@Override
	public Notice findById(String id) {
		return (Notice) this.baseDao.get(Notice.class, id);
	}

	/**
	 *  根据ID获取公告VO
	 * @author wwluo
	 * @date 2017-06-05
	 * @param id
	 * @param langCode
	 * @return NoticeVO
	 */
	@Override
	public NoticeVO findNoticeVOById(String id, String langCode) {
		NoticeVO noticeVO = null;
		if (StringUtils.isNotBlank(id)) {
			Notice notice = (Notice) this.baseDao.get(Notice.class, id);
			if(notice != null){
				noticeVO = new NoticeVO();
				BeanUtils.copyProperties(notice, noticeVO);
				if(StringUtils.isNotBlank(notice.getReleaseBy())){
					String releaseBy = notice.getReleaseBy();
					if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_SYS.equals(notice.getSourceType())){
						SysAdmin admin = (SysAdmin) baseDao.get(SysAdmin.class, releaseBy);
						if (StringUtils.isNotBlank(admin.getNickName())) {
							noticeVO.setReleaseName(admin.getNickName());
						}else{
							noticeVO.setReleaseName(admin.getLoginCode());
						}
					}else{
						MemberBase base = (MemberBase) baseDao.get(MemberBase.class, releaseBy);
						if (StringUtils.isNotBlank(base.getNickName())) {
							noticeVO.setReleaseName(base.getNickName());
						}else{
							noticeVO.setReleaseName(base.getLoginCode());
						}
					}
				}
				if(notice.getReleaseDate() != null){
					noticeVO.setReleaseDateStr(DateUtil.dateToDateString(notice.getReleaseDate(), CommonConstants.FORMAT_DATE_TIME));
				}
				/*if (StringUtils.isNotBlank(notice.getContent())) {
					noticeVO.setContent(UriEncoder.encode(new String(notice.getContent())));
				}*/
				if (StringUtils.isNotBlank(notice.getSource())) {
					String sourceName = null;
					if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())){
						if(CommonConstants.LANG_CODE_SC.equals(langCode)){
							MemberIfafirmSc ifafirmSc = (MemberIfafirmSc) baseDao.get(MemberIfafirmSc.class, notice.getSource());
							sourceName = ifafirmSc.getCompanyName();
						}else if(CommonConstants.LANG_CODE_TC.equals(langCode)){
							MemberIfafirmTc ifafirmTc = (MemberIfafirmTc) baseDao.get(MemberIfafirmTc.class, notice.getSource());
							sourceName = ifafirmTc.getCompanyName();
						}else{
							MemberIfafirmEn ifafirmEn = (MemberIfafirmEn) baseDao.get(MemberIfafirmEn.class, notice.getSource());
							sourceName = ifafirmEn.getCompanyName();
						}
					}else if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())){
						MemberDistributor distributor = (MemberDistributor) baseDao.get(MemberDistributor.class, notice.getSource());
						sourceName = distributor.getCompanyName();
					}
					noticeVO.setSourceName(sourceName);
				}
			}
		}
		return noticeVO;
	}

	/**
	 *  业务公告数据获取
	 * @author wwluo
	 * @date 2017-06-05
	 * @param jsonPaging
	 * @param noticeVO 查询数据过滤VO
	 * @param langCode 多语言标识
	 * @return jsonPaging
	 */
	@Override
	public JsonPaging getConsoleNotices(JsonPaging jsonPaging, NoticeVO noticeVO, String langCode) {
		StringBuffer hql = new StringBuffer(""
				+ " FROM"
				+ " Notice n"
				+ " WHERE"
				+ " n.type=?"
				+ "");
		List<Object> params = new ArrayList<Object>();
		params.add("1");
		if (StringUtils.isNotBlank(noticeVO.getFilterTitle())) {
			hql.append(" AND n.subject LIKE ?");
			params.add("%" + noticeVO.getFilterTitle() + "%");
		}
		if (StringUtils.isNotBlank(noticeVO.getFilterContent())) {
			hql.append(" AND n.content LIKE ?");
			params.add("%" + noticeVO.getFilterContent() + "%");
		}
		if (StringUtils.isNotBlank(noticeVO.getFilterLevel())) {
			hql.append(" AND n.level=?");
			params.add(noticeVO.getFilterLevel());
		}
		if (StringUtils.isNotBlank(noticeVO.getFilterStartTime()) && StringUtils.isNotBlank(noticeVO.getFilterEndTime())) {
			hql.append(" AND n.releaseDate BETWEEN ? AND ?");
			params.add(DateUtil.StringToDate(noticeVO.getFilterStartTime(), CommonConstants.FORMAT_DATE_TIME));
			params.add(DateUtil.StringToDate(noticeVO.getFilterEndTime(), CommonConstants.FORMAT_DATE_TIME));
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			List<Notice> notices = jsonPaging.getList();
			List<NoticeVO> vos = new ArrayList<NoticeVO>();
			for (Notice notice : notices) {
				NoticeVO vo = new NoticeVO();
				BeanUtils.copyProperties(notice, vo);
				if(notice.getReleaseDate() != null){
					vo.setReleaseDateStr(DateUtil.dateToDateString(notice.getReleaseDate(), CommonConstants.FORMAT_DATE_TIME));
				}
				/*if (StringUtils.isNotBlank(notice.getContent())) {
					vo.setContent(UriEncoder.encode(notice.getContent()));
				}*/
				String releaseName = null;
				if (CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
						&& StringUtils.isNotBlank(notice.getReleaseBy())) {
					if(CommonConstants.LANG_CODE_SC.equals(langCode)){
						MemberIfafirmSc ifafirmSc = (MemberIfafirmSc) baseDao.get(MemberIfafirmSc.class, notice.getSource());
						releaseName = ifafirmSc != null ? ifafirmSc.getCompanyName() : null;
					}else if(CommonConstants.LANG_CODE_TC.equals(langCode)){
						MemberIfafirmTc ifafirmTc = (MemberIfafirmTc) baseDao.get(MemberIfafirmTc.class, notice.getSource());
						releaseName = ifafirmTc != null ? ifafirmTc.getCompanyName() : null;
					}else if(CommonConstants.LANG_CODE_EN.equals(langCode)){
						MemberIfafirmEn ifafirmEn = (MemberIfafirmEn) baseDao.get(MemberIfafirmEn.class, notice.getSource());
						releaseName = ifafirmEn != null ? ifafirmEn.getCompanyName() : null;
					}
				}else if (CommonConstantsWeb.NOTICE_SOURCE_TYPE_DISTRIBUTOR.equals(notice.getSourceType())
						&& StringUtils.isNotBlank(notice.getReleaseBy())) {
					MemberDistributor distributor = (MemberDistributor) baseDao.get(MemberDistributor.class, notice.getSource());
					releaseName = distributor != null ? distributor.getCompanyName() : null;
				}
				vo.setReleaseName(releaseName);
				vos.add(vo);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}
		return jsonPaging;
	}

	/**
	 *  公告附件保存
	 * @author wwluo
	 * @date 2017-06-05
	 * @param admin 当前管理员
	 * @param id 公告ID
	 * @param filePath 附件路径
	 * @param orifilename 附件名称
	 */
	@Override
	public AccessoryFile saveNoticeDoc(SysAdmin admin, String id, String filePath,
			String orifilename) {
		AccessoryFile accessoryFile = null;
		if (StringUtils.isNotBlank(filePath)) {
			if(accessoryFile == null){
				accessoryFile = new AccessoryFile();
			}
			String fileType = null;
			if(StringUtils.isNotBlank(orifilename) && orifilename.indexOf(".") > -1){
				Object[] objs = orifilename.split(".");
				if(objs.length > 0){
					fileType = (String) objs[1];
					fileType = fileType.toUpperCase();
				}
			}
			accessoryFile.setRelateId(id);
			accessoryFile.setFilePath(filePath);
			accessoryFile.setFileName(orifilename);
			accessoryFile.setFileType(fileType);
			accessoryFile.setModuleType(CommonConstantsWeb.ACCESSORY_FILE_MODULE_TYPE_NOTICE);
			accessoryFile.setCreateBy(admin.getId());
			accessoryFile.setCreateTime(new Date());
			accessoryFile = accessoryFileService.saveOrUpdate(accessoryFile);
		}
		return accessoryFile;
	}

	/**
	 *  公告附件删除
	 * @author wwluo
	 * @date 2017-06-05
	 */
	@Override
	public Boolean delNoticeDoc(String accessoryfileId) {
		Boolean flag = false;
		if (StringUtils.isNotBlank(accessoryfileId)) {
			AccessoryFile accessoryFile = accessoryFileService.findAccessoryFileById(accessoryfileId);
			if(accessoryFile != null){
				if(StringUtils.isNotBlank(accessoryFile.getFilePath())){
					File file = new File(accessoryFile.getFilePath());
					if(file.exists()){
						file.delete();
					}
				}
				this.baseDao.delete(accessoryFile);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 *  保存公告
	 * @author wwluo
	 * @date 2017-06-05
	 */
	@Override
	public Notice save(Notice notice) {
		return (Notice) this.baseDao.saveOrUpdate(notice);
	}

}
