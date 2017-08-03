package com.fsll.wmes.notice.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.COMM_FAILURE;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.Notice;
import com.fsll.wmes.fund.vo.FundAnnoVO;
import com.fsll.wmes.notice.service.NoticeService;
import com.fsll.wmes.notice.vo.NoticeFilterVO;
import com.fsll.wmes.notice.vo.NoticeVO;

@Service("noticeService")
public class NoticeServiceImpl extends BaseService implements NoticeService {

	@Autowired
	private AccessoryFileService accessoryFileService;
	
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
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getNoticeJson(JsonPaging jsonPaging,
		NoticeFilterVO noticeFilter, String langCode, String dateFormat) {
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " n"
				+ " FROM"
				+ " Notice n"
				+ " LEFT JOIN"
				+ " MemberIfa i"
				+ " ON"
				+ " i.member.id=n.releaseBy"
				+ " LEFT JOIN"
				+ " MemberBase m"
				+ " ON"
				+ " m.id=n.releaseBy"
				+ " WHERE"
				+ " n.isValid=1"
				+ " AND"
				+ " n.sourceType=?"
				+ " AND"
				+ " n.source=?"
				+ "");
		List<Object> params = new ArrayList<Object>();
		params.add(noticeFilter.getSourceType());
		params.add(noticeFilter.getSource());
		if (StringUtils.isNotBlank(noticeFilter.getSubject())) {
			hql.append(" AND n.subject LIKE ?");
			params.add("%" + noticeFilter.getSubject() + "%");
		}	
		if (StringUtils.isNotBlank(noticeFilter.getLevel())) {
			hql.append(" AND n.level=?");
			params.add(noticeFilter.getLevel());
		}	
		if (StringUtils.isNotBlank(noticeFilter.getTarget())) {
			hql.append(" AND n.target=?");
			params.add(noticeFilter.getTarget());
		}	
		if (StringUtils.isNotBlank(noticeFilter.getReleaseName())) {
			hql.append(" AND(");
			hql.append(" m.nickName LIKE ? OR");
			hql.append(" m.loginCode LIKE ? )");
			params.add("%" + noticeFilter.getReleaseName() + "%");
			params.add("%" + noticeFilter.getReleaseName() + "%");
		}	
		if (StringUtils.isNotBlank(noticeFilter.getKeyWord())) {
			hql.append(" AND(");
			hql.append(" m.nickName LIKE ? OR");
			hql.append(" m.loginCode LIKE ? OR");
			hql.append(" n.subject LIKE ?");
			hql.append(" )");
			params.add("%" + noticeFilter.getKeyWord() + "%");
			params.add("%" + noticeFilter.getKeyWord() + "%");
			params.add("%" + noticeFilter.getKeyWord() + "%");
		}
		if (StringUtils.isNotBlank(noticeFilter.getStartTime()) && StringUtils.isNotBlank(noticeFilter.getEndTime())) {
			hql.append(" AND n.releaseDate BETWEEN ? AND ?");
			params.add(DateUtil.StringToDate(noticeFilter.getStartTime(), CommonConstants.FORMAT_DATE_TIME));
			params.add(DateUtil.StringToDate(noticeFilter.getEndTime(), CommonConstants.FORMAT_DATE_TIME));
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<Notice> notices = jsonPaging.getList();
		if(notices != null && !notices.isEmpty()){
			List<NoticeVO> vos = new ArrayList<NoticeVO>();
			for (Notice notice : notices) {
				NoticeVO noticeVO = new NoticeVO();
				BeanUtils.copyProperties(notice, noticeVO);
				if(StringUtils.isNotBlank(notice.getReleaseBy())){
					String releaseBy = notice.getReleaseBy();
					MemberBase base = (MemberBase) baseDao.get(MemberBase.class, releaseBy);
					if (StringUtils.isNotBlank(base.getNickName())) {
						noticeVO.setReleaseByName(base.getNickName());
					}else{
						noticeVO.setReleaseByName(base.getLangCode());
					}
				}
				if(notice.getReleaseDate() != null){
					noticeVO.setReleaseDateStr(DateUtil.dateToDateString(notice.getReleaseDate(), dateFormat));
				}
				/*if (StringUtils.isNotBlank(notice.getContent())) {
					noticeVO.setContent(UriEncoder.encode(new String(notice.getContent())));
				}*/
				if (StringUtils.isNotBlank(notice.getSource())) {
					String sourceName = null;
					if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(noticeFilter.getSourceType())){
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
					}else if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(noticeFilter.getSourceType())){
						MemberDistributor distributor = (MemberDistributor) baseDao.get(MemberDistributor.class, notice.getSource());
						sourceName = distributor.getCompanyName();
					}
					noticeVO.setSourceName(sourceName);
				}
				if (StringUtils.isNotBlank(notice.getTarget())) {
					String targetDisplay = PropertyUtils.getPropertyValue(langCode, "notice.target." + notice.getTarget(), null);
					noticeVO.setTargetDisplay(targetDisplay);
				}
				if (StringUtils.isNotBlank(notice.getLevel())) {
					String levelDisplay = PropertyUtils.getPropertyValue(langCode, "notice.level." + notice.getLevel(), null);
					noticeVO.setLevelDisplay(levelDisplay);
				}
				vos.add(noticeVO);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}else{
			jsonPaging.setTotal(0);
		}
		return jsonPaging;
	}

	/**
	 * 获取一条公告信息
	 * @author wwluo
	 * @data 2017-03-15
	 * @param noticeId 公告Id
	 * @return
	 */
	@Override
	public Notice getNoticeById(String noticeId) {
		Notice notice = null;
		if (StringUtils.isNotBlank(noticeId)) {
			notice = (Notice) this.baseDao.get(Notice.class,noticeId);
		}
		return notice;
	}

	/**
	 * 保存公告信息
	 * @author wwluo
	 * @data 2017-03-15
	 * @param notice 公告实体
	 * @return
	 */
	@Override
	public Notice saveNotice(Notice notice) {
		return (Notice) this.baseDao.saveOrUpdate(notice);
	}

	/**
	 * 删除公告
	 * @author wwluo
	 * @data 2017-03-16
	 * @param 
	 * @return
	 */
	@Override
	public void deleteNotice(String id) {
		if (StringUtils.isNotBlank(id)) {
			Notice notice = (Notice) this.baseDao.get(Notice.class, id);
			this.baseDao.delete(notice);
		}
	}

	/**
	 * 获取公告信息 Announcement Fund
	 * @author wwluo
	 * @data 2017-03-16
	 * @param jsonPaging
	 * @param memberId
	 * @return
	 */
	@Override
	public JsonPaging getAnnouncementFund(JsonPaging jsonPaging, MemberBase loginUser, String langCode) {
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " n"
				+ " FROM"
				+ " FundAnno n"
				+ " LEFT JOIN"
				+ " FundInfo f"
				+ " ON"
				+ " f.id=n.fund.id"
				+ " LEFT JOIN"
				+ " PortfolioHoldProduct p"
				+ " ON"
				+ " f.product.id=p.product.id"
				+ " LEFT JOIN"
				+ " PortfolioHold h"
				+ " ON"
				+ " h.id=p.portfolioHold.id"
				+ " WHERE"
				+ " n.isValid=1"
				+ " AND"
				+ " n.langCode=?"
				+ " GROUP BY"
				+ " n.id"
				+ " ORDER BY"
				+ " h.lastUpdate"
				+ " DESC");
		List<Object> params = new ArrayList<Object>();
		params.add(langCode);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, true);
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			List<FundAnnoVO> vos = new ArrayList<FundAnnoVO>();
			List<FundAnno> fundAnnos = jsonPaging.getList();
			for (FundAnno fundAnno : fundAnnos) {
				FundAnnoVO vo = new FundAnnoVO();
				BeanUtils.copyProperties(fundAnno, vo);
				//vo.setAnnoContent(UriEncoder.encode(new String(fundAnno.getAnnoContent())));
				String dateFormat = loginUser.getDateFormat();
				if (StringUtils.isBlank(dateFormat)) {
					dateFormat = CommonConstants.FORMAT_DATE;
				}
				if(vo.getAnnoDate() != null){
					vo.setAnnoDateFormat(DateUtil.dateToDateString(vo.getAnnoDate(), dateFormat));
				}
				vos.add(vo);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}
		return jsonPaging;
	}

	/**
	 * 获取公告信息 Announcement System
	 * @author wwluo
	 * @data 2017-03-16
	 * @param jsonPaging
	 * @param loginUser
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getAnnouncementSys(JsonPaging jsonPaging, MemberBase loginUser, String langCode) {
		String dateFormat = loginUser.getDateFormat();
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = CommonConstants.FORMAT_DATE_TIME;
		}
		Object[] queryConditions = getQueryConditions(loginUser);
		List<Notice> noticeFind = baseDao.find((String) queryConditions[0], (Object[]) queryConditions[1], false);
		jsonPaging = this.baseDao.selectJsonPaging((String) queryConditions[0], (Object[]) queryConditions[1], jsonPaging, true);
		if(noticeFind != null){
			jsonPaging.setTotal(noticeFind.size());
		}
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			List<Notice> notices = jsonPaging.getList();
			List<NoticeVO> vos = null;
			if(notices != null && !notices.isEmpty()){
				vos = new ArrayList<NoticeVO>();
				for (Notice notice : notices) {
					NoticeVO vo = new NoticeVO();
					BeanUtils.copyProperties(notice, vo);
					/*if (notice.getContent() != null) {
						vo.setContent(UriEncoder.encode(new String(notice.getContent())));
					}*/
					String sourceName = null;
					if (CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
							&& StringUtils.isNotBlank(notice.getSource())
							&& CommonConstants.LANG_CODE_SC.equals(langCode)){
							MemberIfafirmSc ifafirmSc = (MemberIfafirmSc) baseDao.get(MemberIfafirmSc.class, notice.getSource());
							sourceName = ifafirmSc != null ? ifafirmSc.getCompanyName() : null;
					}else if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
							&& StringUtils.isNotBlank(notice.getSource())
							&& CommonConstants.LANG_CODE_TC.equals(langCode)){
						MemberIfafirmTc ifafirmTc = (MemberIfafirmTc) baseDao.get(MemberIfafirmTc.class, notice.getSource());
						sourceName = ifafirmTc != null ? ifafirmTc.getCompanyName() : null;
					}else if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
							&& StringUtils.isNotBlank(notice.getSource())
							&& CommonConstants.LANG_CODE_EN.equals(langCode)){
						MemberIfafirmEn ifafirmEn = (MemberIfafirmEn) baseDao.get(MemberIfafirmEn.class, notice.getSource());
						sourceName = ifafirmEn != null ? ifafirmEn.getCompanyName() : null;
					}else if (CommonConstantsWeb.NOTICE_SOURCE_TYPE_DISTRIBUTOR.equals(notice.getSourceType())
							&& StringUtils.isNotBlank(notice.getReleaseBy())) {
						MemberDistributor distributor = (MemberDistributor) baseDao.get(MemberDistributor.class, notice.getSource());
						sourceName = distributor != null ? distributor.getCompanyName() : null;
					}
					vo.setSourceName(sourceName);
					if (notice.getReleaseBy() != null) {
						vo.setReleaseByName(loginUser.getNickName());
					}
					if (notice.getCreateTime() != null) {
						vo.setCreateTimeStr(DateUtil.dateToDateString(notice.getCreateTime(), dateFormat));
					}
					if (notice.getReleaseDate() != null) {
						vo.setReleaseDateStr(DateUtil.dateToDateString(notice.getReleaseDate(), dateFormat));
					}
					// 是否为new公告（3天内发布的公告）
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(notice.getReleaseDate());
					calendar.add(Calendar.DATE, 3);
					if(calendar.getTime().getTime() >= new Date().getTime()){
						vo.setIsNew("1");
					}
					vos.add(vo);
				}
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}
		return jsonPaging;
	}

	/**
	 * 获取公告VO实体
	 * @author wwluo
	 * @data 2017-03-16
	 * @param id
	 * @param loginUser 当前登录用户
	 * @param langCode
	 * @return
	 */
	@Override
	public NoticeVO getNoticeVOById(String id, MemberBase loginUser,
			String langCode) {
		NoticeVO vo = new NoticeVO();
		String dateFormat = loginUser.getDateFormat();
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = CommonConstants.FORMAT_DATE_TIME;
		}
		Notice notice = getNoticeById(id);
		BeanUtils.copyProperties(notice, vo);
		/*if (notice.getContent() != null) {
			vo.setContent(UriEncoder.encode(new String(notice.getContent())));
		}*/
		String sourceName = null;
		if (CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
				&& StringUtils.isNotBlank(notice.getSource())
				&& CommonConstants.LANG_CODE_SC.equals(langCode)){
				MemberIfafirmSc ifafirmSc = (MemberIfafirmSc) baseDao.get(MemberIfafirmSc.class, notice.getSource());
				sourceName = ifafirmSc != null ? ifafirmSc.getCompanyName() : null;
		}else if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
				&& StringUtils.isNotBlank(notice.getSource())
				&& CommonConstants.LANG_CODE_TC.equals(langCode)){
			MemberIfafirmTc ifafirmTc = (MemberIfafirmTc) baseDao.get(MemberIfafirmTc.class, notice.getSource());
			sourceName = ifafirmTc != null ? ifafirmTc.getCompanyName() : null;
		}else if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
				&& StringUtils.isNotBlank(notice.getSource())
				&& CommonConstants.LANG_CODE_EN.equals(langCode)){
			MemberIfafirmEn ifafirmEn = (MemberIfafirmEn) baseDao.get(MemberIfafirmEn.class, notice.getSource());
			sourceName = ifafirmEn != null ? ifafirmEn.getCompanyName() : null;
		}else if (CommonConstantsWeb.NOTICE_SOURCE_TYPE_DISTRIBUTOR.equals(notice.getSourceType())
				&& StringUtils.isNotBlank(notice.getReleaseBy())) {
			MemberDistributor distributor = (MemberDistributor) baseDao.get(MemberDistributor.class, notice.getSource());
			sourceName = distributor != null ? distributor.getCompanyName() : null;
		}
		vo.setSourceName(sourceName);
		if (notice.getReleaseBy() != null) {
			vo.setReleaseByName(loginUser.getNickName());
		}
		if (notice.getCreateTime() != null) {
			vo.setCreateTimeStr(DateUtil.dateToDateString(notice.getCreateTime(), dateFormat));
		}
		if (notice.getReleaseDate() != null) {
			vo.setReleaseDateStr(DateUtil.dateToDateString(notice.getReleaseDate(), dateFormat));
		}
		return vo;
	}

	/**
	 * 公告数据获取(当前member有权限查看的)
	 * @author wwluo
	 * @data 2017-03-14
	 * @param loginUser
	 * @param langCode 多语言参数
	 * @param maxResult 限定数据数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NoticeVO> getMyNotices(MemberBase loginUser, String langCode,
			Integer maxResult) {
		List<NoticeVO> vos = null;
		String dateFormat = loginUser.getDateFormat();
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = CommonConstants.FORMAT_DATE_TIME;
		}
		Object[] queryConditions = getQueryConditions(loginUser);
		List<Notice> notices = this.baseDao.find((String) queryConditions[0], (Object[]) queryConditions[1], false, maxResult);
		if(notices != null && !notices.isEmpty()){
			vos = new ArrayList<NoticeVO>();
			for (Notice notice : notices) {
				NoticeVO vo = new NoticeVO();
				BeanUtils.copyProperties(notice, vo);
				/*if (notice.getContent() != null) {
					vo.setContent(UriEncoder.encode(new String(notice.getContent())));
				}*/
				String sourceName = null;
				if (CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
						&& StringUtils.isNotBlank(notice.getSource())
						&& CommonConstants.LANG_CODE_SC.equals(langCode)){
						MemberIfafirmSc ifafirmSc = (MemberIfafirmSc) baseDao.get(MemberIfafirmSc.class, notice.getSource());
						sourceName = ifafirmSc != null ? ifafirmSc.getCompanyName() : null;
				}else if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
						&& StringUtils.isNotBlank(notice.getSource())
						&& CommonConstants.LANG_CODE_TC.equals(langCode)){
					MemberIfafirmTc ifafirmTc = (MemberIfafirmTc) baseDao.get(MemberIfafirmTc.class, notice.getSource());
					sourceName = ifafirmTc != null ? ifafirmTc.getCompanyName() : null;
				}else if(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM.equals(notice.getSourceType())
						&& StringUtils.isNotBlank(notice.getSource())
						&& CommonConstants.LANG_CODE_EN.equals(langCode)){
					MemberIfafirmEn ifafirmEn = (MemberIfafirmEn) baseDao.get(MemberIfafirmEn.class, notice.getSource());
					sourceName = ifafirmEn != null ? ifafirmEn.getCompanyName() : null;
				}else if (CommonConstantsWeb.NOTICE_SOURCE_TYPE_DISTRIBUTOR.equals(notice.getSourceType())
						&& StringUtils.isNotBlank(notice.getReleaseBy())) {
					MemberDistributor distributor = (MemberDistributor) baseDao.get(MemberDistributor.class, notice.getSource());
					sourceName = distributor != null ? distributor.getCompanyName() : null;
				}
				vo.setSourceName(sourceName);
				if (notice.getReleaseBy() != null) {
					vo.setReleaseByName(loginUser.getNickName());
				}
				if (notice.getCreateTime() != null) {
					vo.setCreateTimeStr(DateUtil.dateToDateString(notice.getCreateTime(), dateFormat));
				}
				if (notice.getReleaseDate() != null) {
					vo.setReleaseDateStr(DateUtil.dateToDateString(notice.getReleaseDate(), dateFormat));
				}
				// 是否为new公告（3天内发布的公告）
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(notice.getReleaseDate());
				calendar.add(Calendar.DATE, 3);
				if(calendar.getTime().getTime() >= new Date().getTime()){
					vo.setIsNew("1");
				}
				vos.add(vo);
			}
		}
		return vos;
	}

	/**
	 * 公告数据查询语句
	 * @author wwluo
	 * @data 2017-03-14
	 * @param loginUser
	 * @return Object[]
	 */
	private Object[] getQueryConditions(MemberBase loginUser){
		Object[] result = new Object[2];
		Integer memberType = loginUser.getMemberType();
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " n"
				+ " FROM"
				+ " Notice n"
				
				+ " LEFT JOIN"
				+ " MemberIfaIfafirm mii"
				+ " ON"
				+ " n.sourceType='ifafirm'"
				+ " AND"
				+ " mii.ifafirm.id=n.source"
				
				+ " LEFT JOIN"
				+ " MemberIfa mi"
				+ " ON"
				+ " mi.id=mii.ifa.id"
				
				+ " LEFT JOIN"
				+ " CrmCustomer cc"
				+ " ON"
				+ " cc.ifa.id=mii.ifa.id"
				+ " AND"
				+ " cc.isValid=1"
				+ " AND"
				+ " cc.clientType=1"
				
				+ " LEFT JOIN"
				+ " InvestorAccount ia"
				+ " ON"
				+ " n.sourceType='distributor'"
				+ " AND"
				+ " ia.distributor.id=n.source"
				+ " AND"
				+ " ia.openStatus=3"
				
				+ " LEFT JOIN"
				+ " IfafirmDistributor a"
				+ " ON"
				+ " n.sourceType='distributor'"
				+ " AND"
				+ " a.distributor.id=n.source"
				
				+ " LEFT JOIN"
				+ " MemberIfa mi"
				+ " ON"
				+ " mi.id=mii.ifa.id"
				
				+ " WHERE"
				+ "	n.isValid=1"
				+ "");
		List<Object> params = new ArrayList<Object>();
		hql.append(" AND ( n.target=?");
		params.add(CommonConstantsWeb.NOTICE_TARGET_ALL);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType){
			hql.append(" OR ( n.target=? AND cc.member.id=? )");
			hql.append(" OR ( n.target=? AND ia.member.id=? )");
			params.add(CommonConstantsWeb.NOTICE_TARGET_I2);
			params.add(loginUser.getId());
			params.add(CommonConstantsWeb.NOTICE_TARGET_D2);
			params.add(loginUser.getId());
		}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberType){
			hql.append(" OR ( n.target=? AND mi.member.id=? )");
			hql.append(" OR ( n.target=? AND a.ifafirm.id=mii.ifafirm.id AND mi.member.id=? )");
			params.add(CommonConstantsWeb.NOTICE_TARGET_I1);
			params.add(loginUser.getId());
			params.add(CommonConstantsWeb.NOTICE_TARGET_D1);
			params.add(loginUser.getId());
		}
		hql.append(" )");
		hql.append(" GROUP BY n.id ORDER BY n.releaseDate DESC");
		result[0] = hql.toString();
		result[1] = params.toArray();
		return result;
	}

	/**
	 * 保存附件表信息
	 * @author wwluo
	 * @data 2017-03-14
	 * @param loginUser
	 * @param id 附件关联id accessory_file.relate_id
	 * @param filePath 附件路径
	 * @param orifilename 附件名称
	 * @return AccessoryFile
	 */
	@Override
	public AccessoryFile saveNoticeDoc(MemberBase loginUser, String id,
			String filePath, String orifilename) {
		AccessoryFile accessoryFile = null;
		if (StringUtils.isNotBlank(filePath)) {
			accessoryFile = new AccessoryFile();
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
			accessoryFile.setCreateBy(loginUser);
			accessoryFile.setCreateTime(new Date());
			accessoryFile = accessoryFileService.saveAccessoryFile(accessoryFile);
		}
		return accessoryFile;
	}

	/**
	 * 删除附件信息
	 * @author wwluo
	 * @data 2017-03-14
	 * @param loginUser
	 * @param accessoryfileId 附件关联id accessory_file.relate_id
	 * @return 
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
}
