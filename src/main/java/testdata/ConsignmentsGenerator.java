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
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class ConsignmentsGenerator {

	String consignmentId;
	String transportId;
	String consignmentType;
	String reconstructionLane;
	String reconstructionLocation;
	String carrierAdditionalPartyIdentification;
	String consignorAdditionalPartyIdentification;
	String consigneeAdditionalPartyIdentification;
	String consignmentWaveId;
	String hubAdditionalPartyIdentification;
	String reconstructionType;
	OWLLiteral taskClosedOn;
	String fullPalletConsignment;
	OWLLiteral qttBoxes;
	OWLLiteral qttPallets;
	OWLLiteral qttReconstructedPallets;
	OWLLiteral qttReconstructedParcels;
	String originalDataSource;
	String taskId;
	String taskDescription;
	OWLLiteral totalConsignmentVolume;
	OWLLiteral totalConsignmentWeight;
	OWLLiteral year;
	OWLLiteral season;
	OWLLiteral weekDay;
	

	public ConsignmentsGenerator(String consignmentId, String transportId, String consignmentType,
			String reconstructionLane, String reconstructionLocation, String carrierAdditionalPartyIdentification,
			String consignorAdditionalPartyIdentification, String consigneeAdditionalPartyIdentification,
			String consignmentWaveId, String hubAdditionalPartyIdentification, String reconstructionType,
			OWLLiteral taskClosedOn, String fullPalletConsignment, OWLLiteral qttBoxes,
			OWLLiteral qttPallets, OWLLiteral qttReconstructedPallets,
			OWLLiteral qttReconstructedParcels, String originalDataSource, String taskId,
			String taskDescription, OWLLiteral totalConsignmentVolume, OWLLiteral totalConsignmentWeight,
			OWLLiteral year, OWLLiteral season, OWLLiteral weekDay) {

		this.consignmentId = consignmentId;
		this.transportId = transportId;
		this.consignmentType = consignmentType;
		this.reconstructionLane = reconstructionLane;
		this.reconstructionLocation = reconstructionLocation;
		this.carrierAdditionalPartyIdentification = carrierAdditionalPartyIdentification;
		this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
		this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
		this.consignmentWaveId = consignmentWaveId;
		this.hubAdditionalPartyIdentification = hubAdditionalPartyIdentification;
		this.reconstructionType = reconstructionType;
		this.taskClosedOn = taskClosedOn;
		this.fullPalletConsignment = fullPalletConsignment;
		this.qttBoxes = qttBoxes;
		this.qttPallets = qttPallets;
		this.qttReconstructedPallets = qttReconstructedPallets;
		this.qttReconstructedParcels = qttReconstructedParcels;
		this.originalDataSource = originalDataSource;
		this.taskId = taskId;
		this.taskDescription = taskDescription;
		this.totalConsignmentVolume = totalConsignmentVolume;
		this.totalConsignmentWeight = totalConsignmentWeight;
		this.year = year;
		this.season = season;
		this.weekDay = weekDay;
	}



	public ConsignmentsGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		ConsignmentsGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/Consignments_multi_last_100000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<ConsignmentsGenerator> dataset = new HashSet<ConsignmentsGenerator>();
		
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(",");
			
			System.out.println("Params size: " + params.length);

			data = new ConsignmentsGenerator();
						
			data.setConsignmentId(params[1]);
			data.setTransportId(params[2]);
			data.setConsignmentType(params[3]);			
			data.setReconstructionLane(params[4]);
			data.setReconstructionLocation(params[5]);
			data.setCarrierAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[6]));
			data.setConsignorAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[7]));
			data.setConsigneeAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[8]));
			data.setConsignmentWaveId(params[9]);
			data.setHubAdditionalPartyidentification(StringUtilities.removeWhiteSpace(params[10]));
			data.setReconstructionType(params[11]);
			data.setTaskClosedOn(OntologyOperations.convertToDateTime(manager, params[12]));
			data.setFullPalletConsignment(params[13]);
			data.setQttBoxes(OntologyOperations.convertToInt(manager, params[14]));
			data.setQttPallets(OntologyOperations.convertToInt(manager, params[15]));
			data.setQttReconstructedPallets(OntologyOperations.convertToInt(manager, params[16]));
			data.setQttReconstructedParcels(OntologyOperations.convertToInt(manager, params[17]));
			data.setOriginalDataSource(params[18]);
			data.setTaskId(params[19]);
			data.setTaskDescription(params[20]);
			data.setTotalConsignmentVolume(OntologyOperations.convertToDecimal(manager, params[21]));
			data.setTotalConsignmentWeight(OntologyOperations.convertToDecimal(manager, params[22]));
			
			if (params.length == 26) {
		
			data.setYear(OntologyOperations.convertToDecimal(manager, params[23]));
			data.setSeason(OntologyOperations.convertToDecimal(manager, params[24]));
			data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[25]));
			 
			
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

		OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
		OWLClass transportClass = OntologyOperations.getClass("Transport", onto);
		OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual consignmentInd = null;
		OWLIndividual transportInd = null;
		OWLIndividual waveInd = null;
		OWLIndividual carrierInd = null;
		OWLIndividual consignorInd = null;
		OWLIndividual consigneeInd = null;
		OWLIndividual hubInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (ConsignmentsGenerator td : dataset) {
			iterator+=1;	

			//adding consignment individual
			consignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignmentId() + "_consignment"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, consignmentInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);

			//adding transport individual and OP			
			if (!td.getTransportId().equals("NULL")) {
				transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTransportId() + "_transport"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);

				//OP transportId from consignmentInd to transportInd
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("includesTransport", onto), consignmentInd, transportInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);			
			}

			//adding wave individual
			if (!td.getConsignmentWaveId().equals("NULL")) {
				waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignmentWaveId() + "_wave"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);

				//OP waveId from consignmentInd to waveInd
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("processedByWave", onto), consignmentInd, waveInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);			
			}
			
			//adding carrier party individual
			if (!td.getCarrierAdditionalPartyIdentification().equals("0") || !td.getCarrierAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				carrierInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getCarrierAdditionalPartyIdentification() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, carrierInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasCarrierParty", onto), consignmentInd, carrierInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding consignor party individual
			if (!td.getConsignorAdditionalPartyIdentification().equals("0") || !td.getConsignorAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				consignorInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignorAdditionalPartyIdentification() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consignorInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsignorParty", onto), consignmentInd, consignorInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding consignee party individual
			if (!td.getConsigneeAdditionalPartyIdentification().equals("0") || !td.getConsigneeAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				consigneeInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsigneeAdditionalPartyIdentification() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consigneeInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsigneeParty", onto), consignmentInd, consigneeInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding hub party individual
			if (!td.getHubAdditionalPartyidentification().equals("0") || !td.getHubAdditionalPartyidentification().equals("Hub internal movements")) {
				
				hubInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHubAdditionalPartyidentification() + "_party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, hubInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubParty", onto), consignmentInd, hubInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentId", onto), consignmentInd, td.getConsignmentId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentType", onto), consignmentInd, td.getConsignmentType());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLane", onto), consignmentInd, td.getReconstructionLane());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLocation", onto), consignmentInd, td.getReconstructionLocation());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			
			if (!td.getTaskClosedOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskClosedOn", onto), consignmentInd, td.getTaskClosedOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("fullPalletConsignment", onto), consignmentInd, td.getFullPalletConsignment());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), consignmentInd, td.getQttBoxes());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPallets", onto), consignmentInd, td.getQttPallets());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttReconstructedPallets", onto), consignmentInd, td.getQttReconstructedPallets());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttReconstructedParcels", onto), consignmentInd, td.getQttReconstructedParcels());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), consignmentInd, td.getOriginalDataSource());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskId", onto), consignmentInd, td.getTaskId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskDescription", onto), consignmentInd, td.getTaskDescription());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalConsignmentVolume", onto), consignmentInd, td.getTotalConsignmentVolume());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalConsignmentWeight", onto), consignmentInd, td.getTotalConsignmentWeight());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
		
			if (td.getYear() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), consignmentInd, td.getYear());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}

			if (td.getSeason() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), consignmentInd, td.getSeason());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			if (td.getWeekDay() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), consignmentInd, td.getWeekDay());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}



	public String getConsignmentId() {
		return consignmentId;
	}



	public void setConsignmentId(String consignmentId) {
		this.consignmentId = consignmentId;
	}



	public String getTransportId() {
		return transportId;
	}



	public void setTransportId(String transportId) {
		this.transportId = transportId;
	}



	public String getConsignmentType() {
		return consignmentType;
	}



	public void setConsignmentType(String consignmentType) {
		this.consignmentType = consignmentType;
	}



	public String getReconstructionLane() {
		return reconstructionLane;
	}



	public void setReconstructionLane(String reconstructionLane) {
		this.reconstructionLane = reconstructionLane;
	}



	public String getReconstructionLocation() {
		return reconstructionLocation;
	}



	public void setReconstructionLocation(String reconstructionLocation) {
		this.reconstructionLocation = reconstructionLocation;
	}



	public String getCarrierAdditionalPartyIdentification() {
		return carrierAdditionalPartyIdentification;
	}



	public void setCarrierAdditionalPartyIdentification(String carrierAdditionalPartyIdentification) {
		this.carrierAdditionalPartyIdentification = carrierAdditionalPartyIdentification;
	}



	public String getConsignorAdditionalPartyIdentification() {
		return consignorAdditionalPartyIdentification;
	}



	public void setConsignorAdditionalPartyIdentification(String consignorAdditionalPartyIdentification) {
		this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
	}



	public String getConsigneeAdditionalPartyIdentification() {
		return consigneeAdditionalPartyIdentification;
	}



	public void setConsigneeAdditionalPartyIdentification(String consigneeAdditionalPartyIdentification) {
		this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
	}



	public String getConsignmentWaveId() {
		return consignmentWaveId;
	}



	public void setConsignmentWaveId(String consignmentWaveId) {
		this.consignmentWaveId = consignmentWaveId;
	}



	public String getHubAdditionalPartyidentification() {
		return hubAdditionalPartyIdentification;
	}



	public void setHubAdditionalPartyidentification(String hubAdditionalPartyidentification) {
		this.hubAdditionalPartyIdentification = hubAdditionalPartyidentification;
	}



	public String getReconstructionType() {
		return reconstructionType;
	}



	public void setReconstructionType(String reconstructionType) {
		this.reconstructionType = reconstructionType;
	}



	public OWLLiteral getTaskClosedOn() {
		return taskClosedOn;
	}



	public void setTaskClosedOn(OWLLiteral consignmentTaskClosedOn) {
		this.taskClosedOn = consignmentTaskClosedOn;
	}



	public String getFullPalletConsignment() {
		return fullPalletConsignment;
	}



	public void setFullPalletConsignment(String fullPalletConsignment) {
		this.fullPalletConsignment = fullPalletConsignment;
	}



	public OWLLiteral getQttBoxes() {
		return qttBoxes;
	}



	public void setQttBoxes(OWLLiteral consignmentQttBoxes) {
		this.qttBoxes = consignmentQttBoxes;
	}



	public OWLLiteral getQttPallets() {
		return qttPallets;
	}



	public void setQttPallets(OWLLiteral consignmentQttPallets) {
		this.qttPallets = consignmentQttPallets;
	}



	public OWLLiteral getQttReconstructedPallets() {
		return qttReconstructedPallets;
	}



	public void setQttReconstructedPallets(OWLLiteral consignmentQttReconstructedPallets) {
		this.qttReconstructedPallets = consignmentQttReconstructedPallets;
	}



	public OWLLiteral getQttReconstructedParcels() {
		return qttReconstructedParcels;
	}



	public void setQttReconstructedParcels(OWLLiteral consignmentQttReconstructedParcels) {
		this.qttReconstructedParcels = consignmentQttReconstructedParcels;
	}



	public String getOriginalDataSource() {
		return originalDataSource;
	}



	public void setOriginalDataSource(String originalDataSource) {
		this.originalDataSource = originalDataSource;
	}



	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public String getTaskDescription() {
		return taskDescription;
	}



	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}



	public OWLLiteral getTotalConsignmentVolume() {
		return totalConsignmentVolume;
	}



	public void setTotalConsignmentVolume(OWLLiteral totalConsignmentVolume) {
		this.totalConsignmentVolume = totalConsignmentVolume;
	}



	public OWLLiteral getTotalConsignmentWeight() {
		return totalConsignmentWeight;
	}



	public void setTotalConsignmentWeight(OWLLiteral totalConsignmentWeight) {
		this.totalConsignmentWeight = totalConsignmentWeight;
	}



	public OWLLiteral getYear() {
		return year;
	}



	public void setYear(OWLLiteral year) {
		this.year = year;
	}



	public OWLLiteral getSeason() {
		return season;
	}



	public void setSeason(OWLLiteral season) {
		this.season = season;
	}



	public OWLLiteral getWeekDay() {
		return weekDay;
	}



	public void setWeekDay(OWLLiteral weekDay) {
		this.weekDay = weekDay;
	}


	




}
