package datavalidation;


public class Transport {
	
	private String transportId;
	private String expectedArrival;
	private String transportName;
	private String transportType;
	
	private Transport(TransportBuilder builder) {
		
		this.transportId = builder.transportId;
		this.expectedArrival = builder.expectedArrival;
		this.transportName = builder.transportName;
		this.transportType = builder.transportType;
	
	}
	
	public static class TransportBuilder {

		private String transportId;
		private String expectedArrival;
		private String transportName;
		private String transportType;
		
		public TransportBuilder() {}

		public TransportBuilder setTransportId(String transportId) {
			this.transportId = transportId;
			return this;
		}

		public TransportBuilder setExpectedArrival(String expectedArrival) {
			this.expectedArrival = expectedArrival;
			return this;
		}

		public TransportBuilder setTransportName(String transportName) {
			this.transportName = transportName;
			return this;
		}

		public TransportBuilder setTransportType(String transportType) {
			this.transportType = transportType;
			return this;
		}
		
		public Transport build() {
			return new Transport(this);
		}
		
		
	}

	public String getTransportId() {
		return transportId;
	}

	public String getExpectedArrival() {
		return expectedArrival;
	}

	public String getTransportName() {
		return transportName;
	}

	public String getTransportType() {
		return transportType;
	}
	
	

}
