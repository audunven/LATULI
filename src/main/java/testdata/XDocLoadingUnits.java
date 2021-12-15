package testdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import owlprocessing.OntologyOperations;
import sparqlresult.BPSparqlResult;
import sparqlresult.BPSparqlResult.Builder;
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class XDocLoadingUnits
{

	private String internalId;	
	private OWLLiteral preSortScanOn;
	private OWLLiteral reconstructedScanOn;	
	private OWLLiteral volume;
	private OWLLiteral weight;
	private String loadingUnit;	
	private String inboundConsignmentId;
	private String outboundConsignmentId;
	private String additionalPartyIdentification;
	private String hashCode;
	private String hubReconstructionAdditionalPartyIdentification;
	private String hubHashCode;
	private String shipperAdditionalPartyIdentification;
	private String shipperHashCode;
	private String receiverAdditionalPartyIdentification;
	private String receiverHashCode;
	private String carrierAdditionalPartyIdentification;
	private String carrierHashCode;
	private String consignorAdditionalPartyIdentification;
	private String consignorHashCode;
	private String consigneeAdditionalPartyIdentification;
	private String consigneeHashCode;
	private String reconstructionTypeId;
	private OWLLiteral splitShipment; 	
	private String waveId;	
	private String inboundParentLoadingUnitId;
	private String outboundParentLoadingUnitId;	
	private String originalDataSource;
	private OWLLiteral year;
	private OWLLiteral season;
	private OWLLiteral weekDay;
	private OWLLiteral delta;
	
	XDocLoadingUnits(Builder<?> builder) {
		this.internalId = builder.internalId;
		this.preSortScanOn = builder.preSortScanOn;
		this.reconstructedScanOn = builder.reconstructedScanOn;
		this.volume = builder.volume;
		this.weight = builder.weight;
		this.loadingUnit = builder.loadingUnit;
		this.inboundConsignmentId = builder.inboundConsignmentId;
		this.outboundConsignmentId = builder.outboundConsignmentId;
		this.additionalPartyIdentification = builder.additionalPartyIdentification;
		this.hashCode = builder.hashCode;
		this.hubReconstructionAdditionalPartyIdentification = builder.hubReconstructionAdditionalPartyIdentification;
		this.hubHashCode = builder.hubHashCode;
		this.shipperAdditionalPartyIdentification = builder.shipperAdditionalPartyIdentification;
		this.shipperHashCode = builder.shipperHashCode;
		this.receiverAdditionalPartyIdentification = builder.receiverAdditionalPartyIdentification;
		this.receiverHashCode = builder.receiverHashCode;
		this.carrierAdditionalPartyIdentification = builder.carrierAdditionalPartyIdentification;
		this.carrierHashCode = builder.carrierHashCode;
		this.consignorAdditionalPartyIdentification = builder.consignorAdditionalPartyIdentification;
		this.consignorHashCode = builder.consignorHashCode;
		this.consigneeAdditionalPartyIdentification = builder.consigneeAdditionalPartyIdentification;
		this.consigneeHashCode = builder.consigneeHashCode;
		this.reconstructionTypeId = builder.reconstructionTypeId;
		this.splitShipment = builder.splitShipment;
		this.waveId = builder.waveId;
		this.inboundParentLoadingUnitId = builder.inboundParentLoadingUnitId;
		this.outboundParentLoadingUnitId = builder.outboundParentLoadingUnitId;
		this.originalDataSource = builder.originalDataSource;
		this.year = builder.year;
		this.season = builder.season;
		this.weekDay = builder.weekDay;
		this.delta = builder.delta;
	}
	
	public static class Builder extends Builder<T extends Builder> {
		
		private String internalId;	
		private OWLLiteral preSortScanOn;
		private OWLLiteral reconstructedScanOn;	
		private OWLLiteral volume;
		private OWLLiteral weight;
		private String loadingUnit;	
		private String inboundConsignmentId;
		private String outboundConsignmentId;
		private String additionalPartyIdentification;
		private String hashCode;
		private String hubReconstructionAdditionalPartyIdentification;
		private String hubHashCode;
		private String shipperAdditionalPartyIdentification;
		private String shipperHashCode;
		private String receiverAdditionalPartyIdentification;
		private String receiverHashCode;
		private String carrierAdditionalPartyIdentification;
		private String carrierHashCode;
		private String consignorAdditionalPartyIdentification;
		private String consignorHashCode;
		private String consigneeAdditionalPartyIdentification;
		private String consigneeHashCode;
		private String reconstructionTypeId;
		private OWLLiteral splitShipment; 	
		private String waveId;	
		private String inboundParentLoadingUnitId;
		private String outboundParentLoadingUnitId;	
		private String originalDataSource;
		
		//optional
		private OWLLiteral year;
		private OWLLiteral season;
		private OWLLiteral weekDay;
		private OWLLiteral delta;
		
		public Builder(String internalId, OWLLiteral preSortScanOn, OWLLiteral reconstructedScanOn,
				OWLLiteral volume, OWLLiteral weight, String loadingUnit, String inboundConsignmentId,
				String outboundConsignmentId, String additionalPartyIdentification, String hashCode,
				String hubReconstructionAdditionalPartyIdentification, String hubHashCode,
				String shipperAdditionalPartyIdentification, String shipperHashCode,
				String receiverAdditionalPartyIdentification, String receiverHashCode,
				String carrierAdditionalPartyIdentification, String carrierHashCode,
				String consignorAdditionalPartyIdentification, String consignorHashCode,
				String consigneeAdditionalPartyIdentification, String consigneeHashCode, String reconstructionTypeId,
				OWLLiteral splitShipment, String waveId, String inboundParentLoadingUnitId,
				String outboundParentLoadingUnitId, String originalDataSource, OWLLiteral year, OWLLiteral season,
				OWLLiteral weekDay, OWLLiteral delta) {
			this.internalId = internalId;
			this.preSortScanOn = preSortScanOn;
			this.reconstructedScanOn = reconstructedScanOn;
			this.volume = volume;
			this.weight = weight;
			this.loadingUnit = loadingUnit;
			this.inboundConsignmentId = inboundConsignmentId;
			this.outboundConsignmentId = outboundConsignmentId;
			this.additionalPartyIdentification = additionalPartyIdentification;
			this.hashCode = hashCode;
			this.hubReconstructionAdditionalPartyIdentification = hubReconstructionAdditionalPartyIdentification;
			this.hubHashCode = hubHashCode;
			this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
			this.shipperHashCode = shipperHashCode;
			this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
			this.receiverHashCode = receiverHashCode;
			this.carrierAdditionalPartyIdentification = carrierAdditionalPartyIdentification;
			this.carrierHashCode = carrierHashCode;
			this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
			this.consignorHashCode = consignorHashCode;
			this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
			this.consigneeHashCode = consigneeHashCode;
			this.reconstructionTypeId = reconstructionTypeId;
			this.splitShipment = splitShipment;
			this.waveId = waveId;
			this.inboundParentLoadingUnitId = inboundParentLoadingUnitId;
			this.outboundParentLoadingUnitId = outboundParentLoadingUnitId;
			this.originalDataSource = originalDataSource;
			this.year = year;
			this.season = season;
			this.weekDay = weekDay;
			this.delta = delta;	
	}
		
		public T setYear(OWLLiteral year) {
			this.year = year;f
			return self();
		}

		public void setSeason(OWLLiteral season) {
			this.season = season;
		}

		public void setWeekDay(OWLLiteral weekDay) {
			this.weekDay = weekDay;
		}

		public void setDelta(OWLLiteral delta) {
			this.delta = delta;
		}
		
		

		public void setInternalId(String internalId) {
			this.internalId = internalId;
		}

		public void setPreSortScanOn(OWLLiteral preSortScanOn) {
			this.preSortScanOn = preSortScanOn;
		}

		public void setReconstructedScanOn(OWLLiteral reconstructedScanOn) {
			this.reconstructedScanOn = reconstructedScanOn;
		}

		public void setVolume(OWLLiteral volume) {
			this.volume = volume;
		}

		public void setWeight(OWLLiteral weight) {
			this.weight = weight;
		}

		public void setLoadingUnit(String loadingUnit) {
			this.loadingUnit = loadingUnit;
		}

		public void setInboundConsignmentId(String inboundConsignmentId) {
			this.inboundConsignmentId = inboundConsignmentId;
		}

		public void setOutboundConsignmentId(String outboundConsignmentId) {
			this.outboundConsignmentId = outboundConsignmentId;
		}

		public void setAdditionalPartyIdentification(String additionalPartyIdentification) {
			this.additionalPartyIdentification = additionalPartyIdentification;
		}

		public void setHashCode(String hashCode) {
			this.hashCode = hashCode;
		}

		public void setHubReconstructionAdditionalPartyIdentification(String hubReconstructionAdditionalPartyIdentification) {
			this.hubReconstructionAdditionalPartyIdentification = hubReconstructionAdditionalPartyIdentification;
		}

		public void setHubHashCode(String hubHashCode) {
			this.hubHashCode = hubHashCode;
		}

		public void setShipperAdditionalPartyIdentification(String shipperAdditionalPartyIdentification) {
			this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
		}

		public void setShipperHashCode(String shipperHashCode) {
			this.shipperHashCode = shipperHashCode;
		}

		public void setReceiverAdditionalPartyIdentification(String receiverAdditionalPartyIdentification) {
			this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
		}

		public void setReceiverHashCode(String receiverHashCode) {
			this.receiverHashCode = receiverHashCode;
		}

		public void setCarrierAdditionalPartyIdentification(String carrierAdditionalPartyIdentification) {
			this.carrierAdditionalPartyIdentification = carrierAdditionalPartyIdentification;
		}

		public void setCarrierHashCode(String carrierHashCode) {
			this.carrierHashCode = carrierHashCode;
		}

		public void setConsignorAdditionalPartyIdentification(String consignorAdditionalPartyIdentification) {
			this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
		}

		public void setConsignorHashCode(String consignorHashCode) {
			this.consignorHashCode = consignorHashCode;
		}

		public void setConsigneeAdditionalPartyIdentification(String consigneeAdditionalPartyIdentification) {
			this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
		}

		public void setConsigneeHashCode(String consigneeHashCode) {
			this.consigneeHashCode = consigneeHashCode;
		}

		public void setReconstructionTypeId(String reconstructionTypeId) {
			this.reconstructionTypeId = reconstructionTypeId;
		}

		public void setSplitShipment(OWLLiteral splitShipment) {
			this.splitShipment = splitShipment;
		}

		public void setWaveId(String waveId) {
			this.waveId = waveId;
		}

		public void setInboundParentLoadingUnitId(String inboundParentLoadingUnitId) {
			this.inboundParentLoadingUnitId = inboundParentLoadingUnitId;
		}

		public void setOutboundParentLoadingUnitId(String outboundParentLoadingUnitId) {
			this.outboundParentLoadingUnitId = outboundParentLoadingUnitId;
		}

		public void setOriginalDataSource(String originalDataSource) {
			this.originalDataSource = originalDataSource;
		}


		
		@Override
		public XDocLoadingUnitGenerator build() {
			return new XDocLoadingUnitGenerator(this);
		}

		@Override
		protected Builder self() {
			return this;
		}
		
		

		
		
	}
	

	public XDocLoadingUnits(String internalId, OWLLiteral preSortScanOn, OWLLiteral reconstructedScanOn,
			OWLLiteral volume, OWLLiteral weight, String loadingUnit, String inboundConsignmentId,
			String outboundConsignmentId, String additionalPartyIdentification, String hashCode,
			String hubReconstructionAdditionalPartyIdentification, String hubHashCode,
			String shipperAdditionalPartyIdentification, String shipperHashCode,
			String receiverAdditionalPartyIdentification, String receiverHashCode,
			String carrierAdditionalPartyIdentification, String carrierHashCode,
			String consignorAdditionalPartyIdentification, String consignorHashCode,
			String consigneeAdditionalPartyIdentification, String consigneeHashCode, String reconstructionTypeId,
			OWLLiteral splitShipment, String waveId, String inboundParentLoadingUnitId,
			String outboundParentLoadingUnitId, String originalDataSource, OWLLiteral year, OWLLiteral season,
			OWLLiteral weekDay, OWLLiteral delta) {
		this.internalId = internalId;
		this.preSortScanOn = preSortScanOn;
		this.reconstructedScanOn = reconstructedScanOn;
		this.volume = volume;
		this.weight = weight;
		this.loadingUnit = loadingUnit;
		this.inboundConsignmentId = inboundConsignmentId;
		this.outboundConsignmentId = outboundConsignmentId;
		this.additionalPartyIdentification = additionalPartyIdentification;
		this.hashCode = hashCode;
		this.hubReconstructionAdditionalPartyIdentification = hubReconstructionAdditionalPartyIdentification;
		this.hubHashCode = hubHashCode;
		this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
		this.shipperHashCode = shipperHashCode;
		this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
		this.receiverHashCode = receiverHashCode;
		this.carrierAdditionalPartyIdentification = carrierAdditionalPartyIdentification;
		this.carrierHashCode = carrierHashCode;
		this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
		this.consignorHashCode = consignorHashCode;
		this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
		this.consigneeHashCode = consigneeHashCode;
		this.reconstructionTypeId = reconstructionTypeId;
		this.splitShipment = splitShipment;
		this.waveId = waveId;
		this.inboundParentLoadingUnitId = inboundParentLoadingUnitId;
		this.outboundParentLoadingUnitId = outboundParentLoadingUnitId;
		this.originalDataSource = originalDataSource;
		this.year = year;
		this.season = season;
		this.weekDay = weekDay;
		this.delta = delta;
	}


	public XDocLoadingUnits() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		XDocLoadingUnitGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Full/XDLU.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<XDocLoadingUnitGenerator> dataset = new HashSet<XDocLoadingUnitGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox_XDLU.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		while (line != null) {
			params = line.split(",");

			data = new XDocLoadingUnitGenerator();
						
			data.setInternalId(params[1]);
			data.setPreSortScanOn(OntologyOperations.convertToDateTime(manager, params[2]));
			data.setReconstructedScanOn(OntologyOperations.convertToDateTime(manager, params[3]));
			data.setVolume(OntologyOperations.convertToDecimal(manager, params[4]));
			data.setWeight(OntologyOperations.convertToDecimal(manager, params[5]));
			data.setLoadingUnit(params[6]);
			data.setInboundConsignmentId(params[7]);
			data.setOutboundConsignmentId(params[8]);	
			data.setAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[9]));
			data.setHashCode(params[10]);
			data.setHubReconstructionAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[11]));
			data.setHubHashCode(params[12]);
			data.setShipperAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[13]));
			data.setShipperHashCode(params[14]);
			data.setReceiverAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[15]));
			data.setReceiverHashCode(params[16]);
			data.setCarrierAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[17]));
			data.setCarrierHashCode(params[18]);
			data.setConsignorAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[19]));
			data.setConsignorHashCode(params[20]);
			data.setConsigneeAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[21]));
			data.setConsigneeHashCode(params[22]);
			data.setWaveId(params[23]);
			data.setReconstructionTypeId(params[24]);
			data.setSplitShipment(OntologyOperations.convertToInt(manager, params[25]));
			data.setInboundParentLoadingUnitId(params[26]);
			data.setOutboundParentLoadingUnitId(params[27]);
			data.setOriginalDataSource(params[28]);
			
			if (params.length == 33) {
				
			data.setYear(OntologyOperations.convertToDecimal(manager, params[29]));
			data.setSeason(OntologyOperations.convertToDecimal(manager, params[30]));
			data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[31]));
			data.setDelta(OntologyOperations.convertToDecimal(manager, params[32]));
			 
			
			}
			
			dataset.add(data);
			line = br.readLine();

		}

		br.close();



		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass xDocLoadingUnitClass = OntologyOperations.getClass("XDocLoadingUnit", onto);
		OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);
		OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);
		OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual xDocLoadingUnitInd = null;
		OWLIndividual inboundConsignmentInd = null;
		OWLIndividual outboundConsignmentInd = null;
		OWLIndividual loadingUnitInd = null;
		OWLIndividual inboundParentLoadingUnitInd = null;
		OWLIndividual outboundParentLoadingUnitInd = null;
		OWLIndividual waveInd = null;
		OWLIndividual hubReconstructionLocationInd = null;
		OWLIndividual partyInd = null;
		OWLIndividual hubReconstructionInd = null;
		OWLIndividual shipperInd = null;
		OWLIndividual receiverInd = null;
		OWLIndividual carrierInd = null;
		OWLIndividual consignorInd = null;
		OWLIndividual consigneeInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (XDocLoadingUnitGenerator td : dataset) {
			iterator+=1;	

			//adding individual
			xDocLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" +td.getInternalId() + "_xdocloadingunit"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(xDocLoadingUnitClass, xDocLoadingUnitInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			
			
			//object properties
			//if (td.getHubReconstructionAdditionalPartyIdentification() != null) {
			hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getHubHashCode() + "_party");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
						
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubReconstructionParty", onto), xDocLoadingUnitInd, hubReconstructionLocationInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			//}
			
			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitForXDocLoadingUnit() + "_loadingunit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), xDocLoadingUnitInd, loadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			inboundParentLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getInboundParentLoadingUnitId() + "_loadingunit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, inboundParentLoadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasInboundParentLoadingUnit", onto), xDocLoadingUnitInd, inboundParentLoadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			outboundParentLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getOutboundParentLoadingUnitId() + "_loadingunit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, outboundParentLoadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasOutboundParentLoadingUnit", onto), xDocLoadingUnitInd, outboundParentLoadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			inboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getInboundConsignmentId() + "_consignment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, inboundConsignmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasInboundConsignment", onto), xDocLoadingUnitInd, inboundConsignmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			outboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getOutboundConsignmentId() + "_consignment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, outboundConsignmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasOutboundConsignment", onto), xDocLoadingUnitInd, outboundConsignmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			
			//if (!td.getAdditionalPartyIdentification().equals("0") || !td.getAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHashCode() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasParty", onto), xDocLoadingUnitInd, partyInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			//}
			
			
			//adding shipper party individual
			//if (!td.getShipperAdditionalPartyIdentification().equals("0") || !td.getShipperAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				shipperInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipperHashCode() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, shipperInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasShipperParty", onto), xDocLoadingUnitInd, shipperInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			//}
			
			//adding receiver party individual
			//if (!td.getReceiverAdditionalPartyIdentification().equals("0") || !td.getReceiverAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				receiverInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getReceiverHashCode() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, receiverInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReceiverParty", onto), xDocLoadingUnitInd, receiverInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			//}
			
			//adding carrier party individual
			//if (!td.getCarrierAdditionalPartyIdentification().equals("0") || !td.getCarrierAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				carrierInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getCarrierHashCode() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, carrierInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasCarrierParty", onto), xDocLoadingUnitInd, carrierInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			//}
			
			//adding consignor party individual
			//if (!td.getConsignorAdditionalPartyIdentification().equals("0") || !td.getConsignorAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				consignorInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignorHashCode() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consignorInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsignorParty", onto), xDocLoadingUnitInd, consignorInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			//}
			
			//adding consignee party individual
			//if (!td.getConsigneeAdditionalPartyIdentification().equals("0") || !td.getConsigneeAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				consigneeInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsigneeHashCode() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consigneeInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsigneeParty", onto), xDocLoadingUnitInd, consigneeInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			//}
			

			waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_wave"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);

			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("processedByWave", onto), xDocLoadingUnitInd, waveInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			


			//data properties
			if (!td.getPreSortScanOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("preSortScanOn", onto), xDocLoadingUnitInd, td.getPreSortScanOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			if (!td.getReconstructedScanOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructedScanOn", onto), xDocLoadingUnitInd, td.getReconstructedScanOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("volume", onto), xDocLoadingUnitInd, td.getVolume());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weight", onto), xDocLoadingUnitInd, td.getWeight());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionTypeId", onto), xDocLoadingUnitInd, td.getReconstructionTypeId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("splitShipment", onto), xDocLoadingUnitInd, td.getSplitShipment());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), xDocLoadingUnitInd, td.getOriginalDataSource());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			if (td.getYear() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), xDocLoadingUnitInd, td.getYear());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			if (td.getSeason() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), xDocLoadingUnitInd, td.getSeason());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			if (td.getWeekDay() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), xDocLoadingUnitInd, td.getWeekDay());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			if (td.getDelta() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("delta", onto), xDocLoadingUnitInd, td.getDelta());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}

		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}

	

}
