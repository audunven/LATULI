package datavalidation;


public class LoadingUnit {
	
	private String loadingUnitId;
	private String orderNumber;
	private String loadingUnitModifiedOn;
	private String packageTypeId;
	
	
	private LoadingUnit(LoadingUnitBuilder builder) {
		
		
		this.loadingUnitId = builder.loadingUnitId;
		this.orderNumber = builder.orderNumber;
		this.loadingUnitModifiedOn = builder.loadingUnitModifiedOn;
		this.packageTypeId = builder.packageTypeId;
		
		
	}
	
	public static class LoadingUnitBuilder {
		
		private String loadingUnitId;
		private String orderNumber;
		private String loadingUnitModifiedOn;
		private String packageTypeId;
		
		public LoadingUnitBuilder() {}
		

		public LoadingUnitBuilder setLoadingUnitId(String loadingUnitId) {
			this.loadingUnitId = loadingUnitId;
			return this;
		}


		public LoadingUnitBuilder setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
			return this;
		}


		public LoadingUnitBuilder setLoadingUnitModifiedOn(String loadingUnitModifiedOn) {
			this.loadingUnitModifiedOn = loadingUnitModifiedOn;
			return this;
		}

		
		public LoadingUnitBuilder setPackageTypeId(String packageTypeId) {
			this.packageTypeId = packageTypeId;
			return this;
		}



		public LoadingUnit build() {
			return new LoadingUnit(this);
		}


		
		
		
	}

	public String getLoadingUnitId() {
		return loadingUnitId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public String getLoadingUnitModifiedOn() {
		return loadingUnitModifiedOn;
	}

	public String getPackageTypeId() {
		return packageTypeId;
	}


	
	
}
