package com.fsll.wmes.fund.vo;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductInfo;

public class FundDistributorVO {

    private String id;
	private String productId;
	private ProductInfo product;
	private String companyName;
	private String distributorId;
	private String distributorName;
	private MemberDistributor distributor;
	private String symbolCode;
//	private String code;
	private String isPublish;
	private String remark;
	private String cies;
	private Integer rpqLevel;
	private Double transactionFeeRate;
	private String transactionFeeCur;
	private Double transactionFeeMini;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

//	@JsonFormat(pattern="yyyy-MM-dd")
//	private Date beginDate;
//
//	@JsonFormat(pattern="yyyy-MM-dd")
//	private Date endDate;


//	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
//	private Date lastUpdate;
    
	private String isValid;

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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

//	public Date getBeginDate() {
//		return beginDate;
//	}
//
//	public void setBeginDate(Date beginDate) {
//		this.beginDate = beginDate;
//	}
//
//	public Date getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}


	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

//	public Date getLastUpdate() {
//		return lastUpdate;
//	}
//
//	public void setLastUpdate(Date lastUpdate) {
//		this.lastUpdate = lastUpdate;
//	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public ProductInfo getProduct() {
    	return product;
    }

	public void setProduct(ProductInfo product) {
    	this.product = product;
    }

	public String getDistributorId() {
    	return distributorId;
    }

	public void setDistributorId(String distributorId) {
    	this.distributorId = distributorId;
    }

	public MemberDistributor getDistributor() {
    	return distributor;
    }

	public void setDistributor(MemberDistributor distributor) {
    	this.distributor = distributor;
    }

	public String getSymbolCode() {
    	return symbolCode;
    }

	public void setSymbolCode(String symbolCode) {
    	this.symbolCode = symbolCode;
    }

	public String getRemark() {
    	return remark;
    }

	public void setRemark(String remark) {
    	this.remark = remark;
    }

	public String getCies() {
    	return cies;
    }

	public void setCies(String cies) {
    	this.cies = cies;
    }

	public Integer getRpqLevel() {
    	return rpqLevel;
    }

	public void setRpqLevel(Integer rpqLevel) {
    	this.rpqLevel = rpqLevel;
    }

	public Double getTransactionFeeRate() {
    	return transactionFeeRate;
    }

	public void setTransactionFeeRate(Double transactionFeeRate) {
    	this.transactionFeeRate = transactionFeeRate;
    }

	public String getTransactionFeeCur() {
    	return transactionFeeCur;
    }

	public void setTransactionFeeCur(String transactionFeeCur) {
    	this.transactionFeeCur = transactionFeeCur;
    }

	public Double getTransactionFeeMini() {
    	return transactionFeeMini;
    }

	public void setTransactionFeeMini(Double transactionFeeMini) {
    	this.transactionFeeMini = transactionFeeMini;
    }

	public void setDistributorName(String distributorName) {
	    this.distributorName = distributorName;
    }

	public String getDistributorName() {
	    return distributorName;
    }
}