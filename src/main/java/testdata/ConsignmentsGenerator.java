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

/**
 * @author audunvennesland
 *
 */
public class ConsignmentsGenerator {

	String consignmentId;
	String transportId;
	OWLLiteral consignmentModifiedOn;
	String reconstructionLocation;
	String carrierHashCode;
	String consignorAdditionalPartyIdentification;
	String consignorGLN;
	String consignorHashCode;
	String consigneeAdditionalPartyIdentification;
	String consigneeGLN;
	String consigneeHashCode;
	String consignmentWaveId;
	OWLLiteral consignmentTaskClosedOn;


	public ConsignmentsGenerator(String consignmentId, String transportId, OWLLiteral consignmentModifiedOn,
			String reconstructionLocation, String carrierHashCode, String consignorAdditionalPartyIdentification,
			String consignorGLN, String consignorHashCode, String consigneeAdditionalPartyIdentification,
			String consigneeGLN, String consigneeHashCode, String consignmentWaveId, OWLLiteral consignmentTaskClosedOn) {
		super();
		this.consignmentId = consignmentId;
		this.transportId = transportId;
		this.consignmentModifiedOn = consignmentModifiedOn;
		this.reconstructionLocation = reconstructionLocation;
		this.carrierHashCode = carrierHashCode;
		this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
		this.consignorGLN = consignorGLN;
		this.consignorHashCode = consignorHashCode;
		this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
		this.consigneeGLN = consigneeGLN;
		this.consigneeHashCode = consigneeHashCode;
		this.consignmentWaveId = consignmentWaveId;
		this.consignmentTaskClosedOn = consignmentTaskClosedOn;
	}


	public ConsignmentsGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		ConsignmentsGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/Consignments_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<ConsignmentsGenerator> dataset = new HashSet<ConsignmentsGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new ConsignmentsGenerator();
						
			data.setConsignmentId(params[0]);
			data.setTransportId(params[3]);
			data.setConsignmentModifiedOn(OntologyOperations.convertToDateTime(manager, params[5]));
			data.setReconstructionLocation(params[8]);
			data.setCarrierHashCode(params[11]);
			data.setConsignorAdditionalPartyIdentification(params[12]);
			data.setConsignorGLN(params[13]);
			data.setConsignorHashCode(params[14]);
			data.setConsigneeAdditionalPartyIdentification(params[15]);
			data.setConsigneeGLN(params[16]);
			data.setConsigneeHashCode(params[17]);
			data.setWaveId(params[18]);
			data.setConsignmentTaskClosedOn(OntologyOperations.convertToDateTime(manager, params[23]));
			
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

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual consignmentInd = null;
		OWLIndividual transportInd = null;
		OWLIndividual waveInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		//adding process chain
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
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("transportId", onto), consignmentInd, transportInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			}
			
			//adding wave individual
			if (!td.getWaveId().equals("NULL")) {
			waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_wave"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			//OP waveId from consignmentInd to waveInd
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("consignmentWaveId", onto), consignmentInd, waveInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			}
			
			if (!td.getConsignmentModifiedOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentModifiedOn", onto), consignmentInd, td.getConsignmentModifiedOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLocation", onto), consignmentInd, td.getReconstructionLocation());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("carrierHashCode", onto), consignmentInd, td.getCarrierHashCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignorAdditionalPartyIdentification", onto), consignmentInd, td.getConsignorAdditionalPartyIdentification());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignorGLN", onto), consignmentInd, td.getConsignorGLN());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignorHashCode", onto), consignmentInd, td.getConsignorHashCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consigneeAdditionalPartyIdentification", onto), consignmentInd, td.getConsigneeAdditionalPartyIdentification());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consigneeGLN", onto), consignmentInd, td.getConsigneeGLN());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consigneeHashCode", onto), consignmentInd, td.getConsigneeHashCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			if (!td.getConsignmentTaskClosedOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentTaskClosedOn", onto), consignmentInd, td.getConsignmentTaskClosedOn());
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


	public OWLLiteral getConsignmentModifiedOn() {
		return consignmentModifiedOn;
	}


	public void setConsignmentModifiedOn(OWLLiteral modifiedOn) {
		this.consignmentModifiedOn = modifiedOn;
	}


	public String getReconstructionLocation() {
		return reconstructionLocation;
	}


	public void setReconstructionLocation(String reconstructionLocation) {
		this.reconstructionLocation = reconstructionLocation;
	}


	public String getCarrierHashCode() {
		return carrierHashCode;
	}


	public void setCarrierHashCode(String carrierHashCode) {
		this.carrierHashCode = carrierHashCode;
	}


	public String getConsignorAdditionalPartyIdentification() {
		return consignorAdditionalPartyIdentification;
	}


	public void setConsignorAdditionalPartyIdentification(String consignorAdditionalPartyIdentification) {
		this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
	}


	public String getConsignorGLN() {
		return consignorGLN;
	}


	public void setConsignorGLN(String consignorGLN) {
		this.consignorGLN = consignorGLN;
	}


	public String getConsignorHashCode() {
		return consignorHashCode;
	}


	public void setConsignorHashCode(String consignorHashCode) {
		this.consignorHashCode = consignorHashCode;
	}


	public String getConsigneeAdditionalPartyIdentification() {
		return consigneeAdditionalPartyIdentification;
	}


	public void setConsigneeAdditionalPartyIdentification(String consigneeAdditionalPartyIdentification) {
		this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
	}


	public String getConsigneeGLN() {
		return consigneeGLN;
	}


	public void setConsigneeGLN(String consigneeGLN) {
		this.consigneeGLN = consigneeGLN;
	}


	public String getConsigneeHashCode() {
		return consigneeHashCode;
	}


	public void setConsigneeHashCode(String consigneeHashCode) {
		this.consigneeHashCode = consigneeHashCode;
	}


	public String getWaveId() {
		return consignmentWaveId;
	}


	public void setWaveId(String consignmentWaveId) {
		this.consignmentWaveId = consignmentWaveId;
	}


	public OWLLiteral getConsignmentTaskClosedOn() {
		return consignmentTaskClosedOn;
	}


	public void setConsignmentTaskClosedOn(OWLLiteral consignmentTaskClosedOn) {
		this.consignmentTaskClosedOn = consignmentTaskClosedOn;
	}


	





}
