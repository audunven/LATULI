package datavalidation;


public class Shipment {

	private String shipmentId;
	private String shippedOn;
	private String expectedDeliveryOn;
	private String shipperAdditionalPartyIdentification;	
	private String shipperGLN;
	private String shipperHashCode;
	private String receiverAdditionalPartyIdentification;
	private String receiverGLN;
	private String receiverHashCode;
	private String qttBoxesInShipment;
	private String qttPalletsInShipment;
	
	private Shipment(ShipmentBuilder builder) {
		
		
		this.shipmentId = builder.shipmentId;
		this.shippedOn = builder.shippedOn;
		this.expectedDeliveryOn = builder.expectedDeliveryOn;
		this.shipperAdditionalPartyIdentification = builder.shipperAdditionalPartyIdentification;
		this.shipperGLN = builder.shipperGLN;
		this.shipperHashCode = builder.shipperHashCode;
		this.receiverAdditionalPartyIdentification = builder.receiverAdditionalPartyIdentification;
		this.receiverGLN = builder.receiverGLN;
		this.receiverHashCode = builder.receiverHashCode;
		this.qttBoxesInShipment = builder.qttBoxesInShipment;
		this.qttPalletsInShipment = builder.qttPalletsInShipment;

		
	}
	
	public static class ShipmentBuilder {
		
		
		private String shipmentId;
		private String shippedOn;
		private String expectedDeliveryOn;
		private String shipperAdditionalPartyIdentification;	
		private String shipperGLN;
		private String shipperHashCode;
		private String receiverAdditionalPartyIdentification;
		private String receiverGLN;
		private String receiverHashCode;
		private String qttBoxesInShipment;
		private String qttPalletsInShipment;
		
		public ShipmentBuilder() {}
		
		

		public ShipmentBuilder setShipmentId(String shipmentId) {
			this.shipmentId = shipmentId;
			return this;
		}


		public ShipmentBuilder setShippedOn(String shippedOn) {
			this.shippedOn = shippedOn;
			return this;
		}


		public ShipmentBuilder setExpectedDeliveryOn(String expectedDeliveryOn) {
			this.expectedDeliveryOn = expectedDeliveryOn;
			return this;
		}


		public ShipmentBuilder setShipperAdditionalPartyIdentification(String shipperAdditionalPartyIdentification) {
			this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
			return this;
		}


		public ShipmentBuilder setShipperGLN(String shipperGLN) {
			this.shipperGLN = shipperGLN;
			return this;
		}


		public ShipmentBuilder setShipperHashCode(String shipperHashCode) {
			this.shipperHashCode = shipperHashCode;
			return this;
		}


		public ShipmentBuilder setReceiverAdditionalPartyIdentification(String receiverAdditionalPartyIdentification) {
			this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
			return this;
		}


		public ShipmentBuilder setReceiverGLN(String receiverGLN) {
			this.receiverGLN = receiverGLN;
			return this;
		}


		public ShipmentBuilder setReceiverHashCode(String receiverHashCode) {
			this.receiverHashCode = receiverHashCode;
			return this;
		}


		public ShipmentBuilder setQttBoxesInShipment(String qttBoxesInShipment) {
			this.qttBoxesInShipment = qttBoxesInShipment;
			return this;
		}


		public ShipmentBuilder setQttPalletsInShipment(String qttPalletsInShipment) {
			this.qttPalletsInShipment = qttPalletsInShipment;
			return this;
		}


		public Shipment build() {
			return new Shipment(this);
		}

		
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public String getShippedOn() {
		return shippedOn;
	}

	public String getExpectedDeliveryOn() {
		return expectedDeliveryOn;
	}

	public String getShipperAdditionalPartyIdentification() {
		return shipperAdditionalPartyIdentification;
	}

	public String getShipperGLN() {
		return shipperGLN;
	}

	public String getShipperHashCode() {
		return shipperHashCode;
	}

	public String getReceiverAdditionalPartyIdentification() {
		return receiverAdditionalPartyIdentification;
	}

	public String getReceiverGLN() {
		return receiverGLN;
	}

	public String getReceiverHashCode() {
		return receiverHashCode;
	}

	public String getQttBoxesInShipment() {
		return qttBoxesInShipment;
	}

	public String getQttPalletsInShipment() {
		return qttPalletsInShipment;
	}
	


	
	
	
}
