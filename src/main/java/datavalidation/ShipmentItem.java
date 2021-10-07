package datavalidation;

public class ShipmentItem {
	
	private String shipmentItemId;
	private String loadingUnitId;
	private String shipmentItemModifiedOn;
	private String quantity;
	
	private ShipmentItem(ShipmentItemBuilder builder) {
		
		this.shipmentItemId = builder.shipmentItemId;
		this.loadingUnitId = builder.loadingUnitId;
		this.shipmentItemModifiedOn = builder.shipmentItemModifiedOn;
		this.quantity = builder.quantity;
	}
	
	
	public static class ShipmentItemBuilder {
		
		private String shipmentItemId;
		private String loadingUnitId;
		private String shipmentItemModifiedOn;
		private String quantity;
		
		public ShipmentItemBuilder() {}

		public ShipmentItemBuilder setShipmentItemId(String shipmentItemId) {
			this.shipmentItemId = shipmentItemId;
			return this;
		}

		public ShipmentItemBuilder setLoadingUnitId(String loadingUnitId) {
			this.loadingUnitId = loadingUnitId;
			return this;
		}

		public ShipmentItemBuilder setShipmentItemModifiedOn(String shipmentItemModifiedOn) {
			this.shipmentItemModifiedOn = shipmentItemModifiedOn;
			return this;
		}

		public ShipmentItemBuilder setQuantity(String quantity) {
			this.quantity = quantity;
			return this;
		}
		
		public ShipmentItem build() {
			return new ShipmentItem(this);
		}
		
	}


	public String getShipmentItemId() {
		return shipmentItemId;
	}


	public String getLoadingUnitId() {
		return loadingUnitId;
	}


	public String getShipmentItemModifiedOn() {
		return shipmentItemModifiedOn;
	}


	public String getQuantity() {
		return quantity;
	}


	
	

}
