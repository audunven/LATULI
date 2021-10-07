package datavalidation;

public class XDocLoadingUnit {
	
	private String internalId;	
	private String preSortScanOn;
	private String reconstructedScanOn;
	private String finishedScanOn;
	private String loadingOn;
	private String loadingUnitForXDocLoadingUnit;	
	private String inboundConsignmentId;
	private String outboundConsignmentId;
	private String xDocLoadingUnitHubReconstructionLaneId;
	private String xDocLoadingUnitHubReconstructionLocationHashCode;
	private String xDocLoadingUnitWaveId;
	private String reconstructionTypeId;
	
	private String xDocLoadingUnitVolume;
	private String xDocLoadingUnitWeight;
	private String inboundParentLoadingUnitForXDocLoadingUnit;
	private String outboundParentLoadingUnitForXDocLoadingUnit;
	
	private XDocLoadingUnit(XDocLoadingUnitBuilder builder) {
		
		this.internalId = builder.internalId;
		this.preSortScanOn = builder.preSortScanOn;
		this.reconstructedScanOn = builder.reconstructedScanOn;
		this.finishedScanOn = builder.finishedScanOn;
		this.loadingOn = builder.loadingOn;
		this.loadingUnitForXDocLoadingUnit = builder.loadingUnitForXDocLoadingUnit;
		this.inboundConsignmentId = builder.inboundConsignmentId;
		this.outboundConsignmentId = builder.outboundConsignmentId;
		this.xDocLoadingUnitHubReconstructionLaneId = builder.xDocLoadingUnitHubReconstructionLaneId;
		this.xDocLoadingUnitHubReconstructionLocationHashCode = builder.xDocLoadingUnitHubReconstructionLocationHashCode;
		this.xDocLoadingUnitWaveId = builder.xDocLoadingUnitWaveId;
		this.reconstructionTypeId = builder.reconstructionTypeId;
		this.xDocLoadingUnitVolume = builder.xDocLoadingUnitVolume;
		this.xDocLoadingUnitWeight = builder.xDocLoadingUnitWeight;
		this.inboundParentLoadingUnitForXDocLoadingUnit = builder.inboundParentLoadingUnitForXDocLoadingUnit;
		this.outboundParentLoadingUnitForXDocLoadingUnit = builder.outboundParentLoadingUnitForXDocLoadingUnit;
	}
	
	
	public static class XDocLoadingUnitBuilder {
		
		
		private String internalId;	
		private String preSortScanOn;
		private String reconstructedScanOn;
		private String finishedScanOn;
		private String loadingOn;
		private String loadingUnitForXDocLoadingUnit;	
		private String inboundConsignmentId;
		private String outboundConsignmentId;
		private String xDocLoadingUnitHubReconstructionLaneId;
		private String xDocLoadingUnitHubReconstructionLocationHashCode;
		private String xDocLoadingUnitWaveId;
		private String reconstructionTypeId;
		
		private String xDocLoadingUnitVolume;
		private String xDocLoadingUnitWeight;
		private String inboundParentLoadingUnitForXDocLoadingUnit;
		private String outboundParentLoadingUnitForXDocLoadingUnit;
		
		public XDocLoadingUnitBuilder() {}
	
		public XDocLoadingUnitBuilder setInternalId(String internalId) {
			this.internalId = internalId;
			return this;
		}

		public XDocLoadingUnitBuilder setPreSortScanOn(String preSortScanOn) {
			this.preSortScanOn = preSortScanOn;
			return this;
		}

		public XDocLoadingUnitBuilder setReconstructedScanOn(String reconstructedScanOn) {
			this.reconstructedScanOn = reconstructedScanOn;
			return this;
		}

		public XDocLoadingUnitBuilder setFinishedScanOn(String finishedScanOn) {
			this.finishedScanOn = finishedScanOn;
			return this;
		}
		
		public XDocLoadingUnitBuilder setLoadingOn(String loadingOn) {
			this.loadingOn = loadingOn;
			return this;
		}

		public XDocLoadingUnitBuilder setLoadingUnitForXDocLoadingUnit(String loadingUnitForXDocLoadingUnit) {
			this.loadingUnitForXDocLoadingUnit = loadingUnitForXDocLoadingUnit;
			return this;
		}

		public XDocLoadingUnitBuilder setInboundConsignmentId(String inboundConsignmentId) {
			this.inboundConsignmentId = inboundConsignmentId;
			return this;
		}

		public XDocLoadingUnitBuilder setOutboundConsignmentId(String outboundConsignmentId) {
			this.outboundConsignmentId = outboundConsignmentId;
			return this;
		}

		public XDocLoadingUnitBuilder setxDocLoadingUnitHubReconstructionLaneId(String xDocLoadingUnitHubReconstructionLaneId) {
			this.xDocLoadingUnitHubReconstructionLaneId = xDocLoadingUnitHubReconstructionLaneId;
			return this;
		}

		public XDocLoadingUnitBuilder setxDocLoadingUnitHubReconstructionLocationHashCode(
				String xDocLoadingUnitHubReconstructionLocationHashCode) {
			this.xDocLoadingUnitHubReconstructionLocationHashCode = xDocLoadingUnitHubReconstructionLocationHashCode;
			return this;
		}

		public XDocLoadingUnitBuilder setxDocLoadingUnitWaveId(String xDocLoadingUnitWaveId) {
			this.xDocLoadingUnitWaveId = xDocLoadingUnitWaveId;
			return this;
		}

		public XDocLoadingUnitBuilder setReconstructionTypeId(String reconstructionTypeId) {
			this.reconstructionTypeId = reconstructionTypeId;
			return this;
		}

		public XDocLoadingUnitBuilder setxDocLoadingUnitVolume(String xDocLoadingUnitVolume) {
			this.xDocLoadingUnitVolume = xDocLoadingUnitVolume;
			return this;
		}

		public XDocLoadingUnitBuilder setxDocLoadingUnitWeight(String xDocLoadingUnitWeight) {
			this.xDocLoadingUnitWeight = xDocLoadingUnitWeight;
			return this;
		}

		public XDocLoadingUnitBuilder setInboundParentLoadingUnitForXDocLoadingUnit(String inboundParentLoadingUnitForXDocLoadingUnit) {
			this.inboundParentLoadingUnitForXDocLoadingUnit = inboundParentLoadingUnitForXDocLoadingUnit;
			return this;
		}

		public XDocLoadingUnitBuilder setOutboundParentLoadingUnitForXDocLoadingUnit(String outboundParentLoadingUnitForXDocLoadingUnit) {
			this.outboundParentLoadingUnitForXDocLoadingUnit = outboundParentLoadingUnitForXDocLoadingUnit;
			return this;
		}

		public XDocLoadingUnit build() {
			return new XDocLoadingUnit(this);
		}

		
	}


	public String getInternalId() {
		return internalId;
	}


	public String getPreSortScanOn() {
		return preSortScanOn;
	}


	public String getReconstructedScanOn() {
		return reconstructedScanOn;
	}


	public String getFinishedScanOn() {
		return finishedScanOn;
	}


	public String getLoadingOn() {
		return loadingOn;
	}


	public String getLoadingUnitForXDocLoadingUnit() {
		return loadingUnitForXDocLoadingUnit;
	}


	public String getInboundConsignmentId() {
		return inboundConsignmentId;
	}


	public String getOutboundConsignmentId() {
		return outboundConsignmentId;
	}


	public String getxDocLoadingUnitHubReconstructionLaneId() {
		return xDocLoadingUnitHubReconstructionLaneId;
	}


	public String getxDocLoadingUnitHubReconstructionLocationHashCode() {
		return xDocLoadingUnitHubReconstructionLocationHashCode;
	}


	public String getxDocLoadingUnitWaveId() {
		return xDocLoadingUnitWaveId;
	}


	public String getReconstructionTypeId() {
		return reconstructionTypeId;
	}


	public String getxDocLoadingUnitVolume() {
		return xDocLoadingUnitVolume;
	}


	public String getxDocLoadingUnitWeight() {
		return xDocLoadingUnitWeight;
	}


	public String getInboundParentLoadingUnitForXDocLoadingUnit() {
		return inboundParentLoadingUnitForXDocLoadingUnit;
	}


	public String getOutboundParentLoadingUnitForXDocLoadingUnit() {
		return outboundParentLoadingUnitForXDocLoadingUnit;
	}
	
	
	
	
	
	
	
}
