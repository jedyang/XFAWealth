package com.fsll.app.ifa.market.vo;

/**
 * 投资策略配置实体类VO
 * @author zxtan
 * @date 2016-11-14
 */
public class AppStrategyAllocationVO {

		private String id;
		private String strategyId;	    
		private String type;		
		private String methodType;
		private String itemCode;
		private String itemName;		
		private String itemWeight;		
		private String layerLevel;

		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getStrategyId() {
			return strategyId;
		}

		public void setStrategyId(String strategyId) {
			this.strategyId = strategyId;
		}

		public String getMethodType() {
			return methodType;
		}

		public void setMethodType(String methodType) {
			this.methodType = methodType;
		}

		public String getItemCode() {
			return itemCode;
		}

		public void setItemCode(String itemCode) {
			this.itemCode = itemCode;
		}

		public String getItemWeight() {
			return itemWeight;
		}

		public void setItemWeight(String itemWeight) {
			this.itemWeight = itemWeight;
		}

		public String getLayerLevel() {
			return layerLevel;
		}

		public void setLayerLevel(String layerLevel) {
			this.layerLevel = layerLevel;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
}
