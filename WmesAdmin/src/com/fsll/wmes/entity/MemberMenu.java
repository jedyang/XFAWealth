package com.fsll.wmes.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.common.CommonConstants;

@Entity
@Table(name = "member_menu")
public class MemberMenu implements java.io.Serializable {
	private String id;
	private MemberMenu parent;
	private String code;
	private String nameSc;
	private String nameTc;
	private String nameEn;
	private String webUrl;
	private String icon;
	private Short orderBy;
	private String ifInner;
	private String isValid;
	private Set<MemberMenu> childSet = new HashSet<MemberMenu>(0);
	
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public MemberMenu getParent() {
		return parent;
	}

	public void setParent(MemberMenu parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "parent")
	@OrderBy("orderBy asc")
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<MemberMenu> getChildSet() {
		return childSet;
	}

	public void setChildSet(Set<MemberMenu> childSet) {
		this.childSet = childSet;
	}

	@Column(name = "code")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name_sc")
	public String getNameSc() {
		return this.nameSc;
	}

	public void setNameSc(String nameSc) {
		this.nameSc = nameSc;
	}

	@Column(name = "name_tc")
	public String getNameTc() {
		return this.nameTc;
	}

	public void setNameTc(String nameTc) {
		this.nameTc = nameTc;
	}

	@Column(name = "name_en")
	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	@Column(name = "web_url")
	public String getWebUrl() {
		return this.webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	@Column(name = "icon")
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "order_by")
	public Short getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Short orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "if_inner")
	public String getIfInner() {
		return this.ifInner;
	}

	public void setIfInner(String ifInner) {
		this.ifInner = ifInner;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	/**
	 * 用于根据语言编码返回菜单名称
	 * @param lang
	 * @return
	 */
	public String getMenuName(String lang){
		if (null!=lang){
			if (CommonConstants.LANG_CODE_EN.equalsIgnoreCase(lang))
				return nameEn;
			else if (CommonConstants.LANG_CODE_TC.equalsIgnoreCase(lang))
				return nameTc;
			else {
				return nameSc;
			}
		}
		return "";
	}
	private String keyWord;//搜索关键词
	@Transient
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	private Boolean isMenuTree;//是否获取菜单树
	@Transient
	public Boolean isMenuTree() {
		return isMenuTree;
	}
	public void setMenuTree(boolean isMenuTree) {
		this.isMenuTree = isMenuTree;
	}

	private String parentId;
	@Transient
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	private List<MemberMenu> childs;
	@Transient
	public List<MemberMenu> getChilds() {
		return childs;
	}
	public void setChilds(List<MemberMenu> childs) {
		this.childs = childs;
	}
	
	@Override  
    public boolean equals(Object o) {  
        if (o instanceof MemberMenu) {  
        	MemberMenu menu = (MemberMenu) o;  
            return this.id.equals(menu.id);  
        }  
        return super.equals(o);  
    } 

}