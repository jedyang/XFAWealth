/**
 * 
 */
package com.fsll.wmes.fund.vo;

/**
 * @author Yan
 *	基金基础信息vo
 */
public class FundProductVO {

	private String id;	//fundId
	private String productId;
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
