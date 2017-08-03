package com.fsll.wmes.company.vo;

import java.util.Date;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.ProductInfo;

/**
 * 产品运营企业关系管理
 * @author Yan
 * @date 2017-01-18
 */
public class ProductCompanyVO {
   
	private String id;
	private CompanyInfo company; 
	private ProductInfo product;
	private Date createTime;
	private String companyName;
	private String productName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CompanyInfo getCompany() {
		return company;
	}
	public void setCompany(CompanyInfo company) {
		this.company = company;
	}
	public ProductInfo getProduct() {
		return product;
	}
	public void setProduct(ProductInfo product) {
		this.product = product;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
}
