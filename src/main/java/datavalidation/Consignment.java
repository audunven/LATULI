package datavalidation;


public class Consignment {
	
	private String consignmentId;
	private String transportId;
	private String consignmentModifiedOn;
	private String reconstructionLocation;
	private String carrierHashCode;
	private String consignorAdditionalPartyIdentification;
	private String consignorGLN;
	private String consignorHashCode;
	private String consigneeAdditionalPartyIdentification;
	private String consigneeGLN;
	private String consigneeHashCode;
	private String consignmentWaveId;
	private String consignmentTaskClosedOn;
	
	private Consignment(ConsignmentBuilder builder) {
		
		this.consignmentId = builder.consignmentId;
		this.transportId = builder.transportId;
		this.consignmentModifiedOn = builder.consignmentModifiedOn;
		this.reconstructionLocation = builder.reconstructionLocation;
		this.carrierHashCode = builder.carrierHashCode;
		this.consignorAdditionalPartyIdentification = builder.consignorAdditionalPartyIdentification;
		this.consignorGLN = builder.consignorGLN;
		this.consignorHashCode = builder.consignorHashCode;
		this.consigneeAdditionalPartyIdentification = builder.consigneeAdditionalPartyIdentification;
		this.consigneeGLN = builder.consigneeGLN;
		this.consigneeHashCode = builder.consigneeHashCode;
		this.consignmentWaveId = builder.consignmentWaveId;
		this.consignmentTaskClosedOn = builder.consignmentTaskClosedOn;
	}
	
	public static class ConsignmentBuilder {
			
		private String consignmentId;
		private String transportId;
		private String consignmentModifiedOn;
		private String reconstructionLocation;
		private String carrierHashCode;
		private String consignorAdditionalPartyIdentification;
		private String consignorGLN;
		private String consignorHashCode;
		private String consigneeAdditionalPartyIdentification;
		private String consigneeGLN;
		private String consigneeHashCode;
		private String consignmentWaveId;
		private String consignmentTaskClosedOn;
		
		public ConsignmentBuilder() {}
		
		public ConsignmentBuilder setConsignmentId(String consignmentId) {
			this.consignmentId = consignmentId;
			return this;
		}
		
		public ConsignmentBuilder setTransportId(String transportId) {
			this.transportId = transportId;
			return this;
		}
		
		public ConsignmentBuilder setConsignmentModifiedOn(String consignmentModifiedOn) {
			this.consignmentModifiedOn = consignmentModifiedOn;
			return this;
		}
		
		
		public ConsignmentBuilder setReconstructionLocation(String reconstructionLocation) {
			this.reconstructionLocation = reconstructionLocation;
			return this;
		}

		public ConsignmentBuilder setCarrierHashCode(String carrierHashCode) {
			this.carrierHashCode = carrierHashCode;
			return this;
		}

		public ConsignmentBuilder setConsignorAdditionalPartyIdentification(String consignorAdditionalPartyIdentification) {
			this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
			return this;
		}

		public ConsignmentBuilder setConsignorGLN(String consignorGLN) {
			this.consignorGLN = consignorGLN;
			return this;
		}

		public ConsignmentBuilder setConsignorHashCode(String consignorHashCode) {
			this.consignorHashCode = consignorHashCode;
			return this;
		}

		public ConsignmentBuilder setConsigneeAdditionalPartyIdentification(String consigneeAdditionalPartyIdentification) {
			this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
			return this;
		}

		public ConsignmentBuilder setConsigneeGLN(String consigneeGLN) {
			this.consigneeGLN = consigneeGLN;
			return this;
		}

		public ConsignmentBuilder setConsigneeHashCode(String consigneeHashCode) {
			this.consigneeHashCode = consigneeHashCode;
			return this;
		}

		public ConsignmentBuilder setConsignmentWaveId(String consignmentWaveId) {
			this.consignmentWaveId = consignmentWaveId;
			return this;
		}

		public ConsignmentBuilder setConsignmentTaskClosedOn(String consignmentTaskClosedOn) {
			this.consignmentTaskClosedOn = consignmentTaskClosedOn;
			return this;
		}

		public Consignment build() {
			return new Consignment(this);
		}

	}

	public String getConsignmentId() {
		return consignmentId;
	}

	public String getTransportId() {
		return transportId;
	}

	public String getConsignmentModifiedOn() {
		return consignmentModifiedOn;
	}

	public String getReconstructionLocation() {
		return reconstructionLocation;
	}

	public String getCarrierHashCode() {
		return carrierHashCode;
	}

	public String getConsignorAdditionalPartyIdentification() {
		return consignorAdditionalPartyIdentification;
	}

	public String getConsignorGLN() {
		return consignorGLN;
	}

	public String getConsignorHashCode() {
		return consignorHashCode;
	}

	public String getConsigneeAdditionalPartyIdentification() {
		return consigneeAdditionalPartyIdentification;
	}

	public String getConsigneeGLN() {
		return consigneeGLN;
	}

	public String getConsigneeHashCode() {
		return consigneeHashCode;
	}

	public String getConsignmentWaveId() {
		return consignmentWaveId;
	}

	public String getConsignmentTaskClosedOn() {
		return consignmentTaskClosedOn;
	}
	
	

}
