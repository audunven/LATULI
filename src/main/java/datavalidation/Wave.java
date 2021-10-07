package datavalidation;

public class Wave {
	
	private String waveId;
	private String waveStartProcessingOn;
	private String waveEndProcessingOn;
	private String qttTrailers;
	private String qttBoxesInWave;
	private String qttPalletsInWave;
	private String qttBoxesProcessed;
	private String qttPalletsBuilt;
	private String qttTasks;
	private String qttShipments;

	private Wave(WaveBuilder builder) {
		
		this.waveId = builder.waveId;
		this.waveStartProcessingOn = builder.waveStartProcessingOn;
		this.waveEndProcessingOn = builder.waveEndProcessingOn;
		this.qttTrailers = builder.qttTrailers;
		this.qttBoxesInWave = builder.qttBoxesInWave;
		this.qttPalletsInWave = builder.qttPalletsInWave;
		this.qttBoxesProcessed = builder.qttBoxesProcessed;
		this.qttPalletsBuilt = builder.qttPalletsBuilt;
		this.qttTasks = builder.qttTasks;
		this.qttShipments = builder.qttShipments;
	}
		
	public static class WaveBuilder {
		
		private String waveId;
		private String waveStartProcessingOn;
		private String waveEndProcessingOn;
		private String qttTrailers;
		private String qttBoxesInWave;
		private String qttPalletsInWave;
		private String qttBoxesProcessed;
		private String qttPalletsBuilt;
		private String qttTasks;
		private String qttShipments;
		
		public WaveBuilder() {}
			

	public WaveBuilder setWaveId(String waveId) {
		this.waveId = waveId;
		return this;
	}

	public WaveBuilder setWaveStartProcessingOn(String waveStartProcessingOn) {
		this.waveStartProcessingOn = waveStartProcessingOn;
		return this;
	}

	public WaveBuilder setWaveEndProcessingOn(String waveEndProcessingOn) {
		this.waveEndProcessingOn = waveEndProcessingOn;
		return this;
	}

	public WaveBuilder setQttTrailers(String qttTrailers) {
		this.qttTrailers = qttTrailers;
		return this;
	}

	public WaveBuilder setQttBoxesInWave(String qttBoxesInWave) {
		this.qttBoxesInWave = qttBoxesInWave;
		return this;
	}

	public WaveBuilder setQttPalletsInWave(String qttPalletsInWave) {
		this.qttPalletsInWave = qttPalletsInWave;
		return this;
	}

	public WaveBuilder setQttBoxesProcessed(String qttBoxesProcessed) {
		this.qttBoxesProcessed = qttBoxesProcessed;
		return this;
	}

	public WaveBuilder setQttPalletsBuilt(String qttPalletsBuilt) {
		this.qttPalletsBuilt = qttPalletsBuilt;
		return this;
	}

	public WaveBuilder setQttTasks(String qttTasks) {
		this.qttTasks = qttTasks;
		return this;
	}

	public WaveBuilder setQttShipments(String qttShipments) {
		this.qttShipments = qttShipments;
		return this;
	}
	
	public Wave build() {
		return new Wave(this);
	}
	
}

	public String getWaveId() {
		return waveId;
	}

	public String getWaveStartProcessingOn() {
		return waveStartProcessingOn;
	}

	public String getWaveEndProcessingOn() {
		return waveEndProcessingOn;
	}

	public String getQttTrailers() {
		return qttTrailers;
	}

	public String getQttBoxesInWave() {
		return qttBoxesInWave;
	}

	public String getQttPalletsInWave() {
		return qttPalletsInWave;
	}

	public String getQttBoxesProcessed() {
		return qttBoxesProcessed;
	}

	public String getQttPalletsBuilt() {
		return qttPalletsBuilt;
	}

	public String getQttTasks() {
		return qttTasks;
	}

	public String getQttShipments() {
		return qttShipments;
	}
	
	
	
}
