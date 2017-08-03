package com.fsll.wmes.community.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.CommunitySection;

public interface CommunitySectionService {

	/**
	 *  社区栏目设置列表数据获取
	 * @author wwluo
	 * @date 2017-05-26
	 * @param jsonPaging
	 * @return jsonPaging
	 */
	public JsonPaging getSections(JsonPaging jsonPaging);

	/**
	 *  获取社区所有栏目
	 * @author wwluo
	 * @date 2017-06-02
	 * @return List<CommunitySection> 社区栏目实体集合
	 */
	public List<CommunitySection> findAllSection();

	/**
	 *  根据ID获取实体（CommunitySection）
	 * @author wwluo
	 * @date 2017-06-02
	 * @return CommunitySection
	 */
	public CommunitySection findById(String sectionId);

	/**
	 *  保存社区栏目信息
	 * @author wwluo
	 * @date 2017-06-01
	 */
	public void saveSection(CommunitySection section);

}
