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
public class Shipments {

	String shipmentId;
	OWLLiteral shippedOn;
	OWLLiteral expectedDeliveryOn;
	String shipperAdditionalPartyIdentification;	
	String receiverAdditionalPartyIdentification;
	OWLLiteral plannedDeliveryDate;
	OWLLiteral qttBoxes;
	OWLLiteral qttPallets;
	String originalDataSource;
	OWLLiteral year;
	OWLLiteral season;
	OWLLiteral weekDay;

	public Shipments(String shipmentId, String shipperGLN, String receiverGLN,
			String shipperAdditionalPartyIdentification, String shipperHashCode,
			String receiverAdditionalPartyIdentification, String receiverHashCode, OWLLiteral shippedOn,
			OWLLiteral expectedDeliveryOn, OWLLiteral plannedDeliveryDate, OWLLiteral qttBoxes, OWLLiteral qttPallets,
			String originalDataSource, OWLLiteral year, OWLLiteral season, OWLLiteral weekDay) {

		this.shipmentId = shipmentId;
		this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
		this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
		this.shippedOn = shippedOn;
		this.expectedDeliveryOn = expectedDeliveryOn;
		this.plannedDeliveryDate = plannedDeliveryDate;
		this.qttBoxes = qttBoxes;
		this.qttPallets = qttPallets;
		this.originalDataSource = originalDataSource;
		this.year = year;
		this.season = season;
		this.weekDay = weekDay;
	}


	public Shipments() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		Shipments data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/Shipments_multi_last_100000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<Shipments> dataset = new HashSet<Shipments>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(",");

			data = new Shipments();
						
			data.setShipmentId(params[1]);
			data.setShippedOn(OntologyOperations.convertToDateTime(manager, params[2]));
			data.setExpectedDeliveryOn(OntologyOperations.convertToDateTime(manager, params[3]));
			data.setShipperAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[4]));
			data.setReceiverAdditionalPartyIdentification(StringUtilities.removeWhiteSpace(params[5]));
			data.setPlannedDeliveryDate(OntologyOperations.convertToDateTime(manager,params[6]));
			data.setQttBoxesInShipment(OntologyOperations.convertToInt(manager, params[7]));
			data.setQttPalletsInShipment(OntologyOperations.convertToInt(manager, params[8]));
			data.setOriginalDataSource(params[9]);
			data.setYear(OntologyOperations.convertToDecimal(manager, params[10]));
			data.setSeason(OntologyOperations.convertToDecimal(manager, params[11]));
			data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[12]));
					
			dataset.add(data);
			line = br.readLine();

		}

		br.close();

		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
		OWLClass shipperClass = OntologyOperations.getClass("Shipper", onto);
		OWLClass receiverClass = OntologyOperations.getClass("Receiver", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual shipmentInd = null;
		OWLIndividual shipperInd = null;
		OWLIndividual receiverInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		//adding process chain
		
		  for (Shipments td : dataset) { 
			  
		  iterator+=1;
		  
		  //adding shipment individual 
		  shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentId() + "_shipment")); 
		  classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd); 
		  addAxiomChange = new AddAxiom(onto, classAssertionAxiom); 
		  manager.applyChange(addAxiomChange);
		  
		  	//adding shipper individual
			if (!td.getShipperAdditionalPartyIdentification().equals("NULL")) {
			shipperInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipperAdditionalPartyIdentification() + "_party"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(shipperClass, shipperInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			//OP waveId from shipmentInd to shipperInd
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasShipperParty", onto), shipmentInd, shipperInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);			
			}
			
		  	//adding receiver individual
			if (!td.getReceiverAdditionalPartyIdentification().equals("NULL")) {
			receiverInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getReceiverAdditionalPartyIdentification() + "_party"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(receiverClass, receiverInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
					
			//OP waveId from shipmentInd to receiverInd
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReceiverParty", onto), shipmentInd, receiverInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);			
			}
		  
		  
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipmentId", onto), shipmentInd, td.getShipmentId()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
		  manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shippedOn", onto), shipmentInd, td.getShippedOn()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
		  manager.applyChange(addAxiomChange);
		  
		  if (!td.getExpectedDeliveryOn().getLiteral().startsWith("0000")) {
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("expectedDeliveryOn", onto), shipmentInd, td.getExpectedDeliveryOn());
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
		  manager.applyChange(addAxiomChange); 
		  }
		  
		  if (!td.getPlannedDeliveryDate().getLiteral().startsWith("0000")) {
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("plannedDeliveryDate", onto), shipmentInd, td.getPlannedDeliveryDate());
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
		  manager.applyChange(addAxiomChange); 
		  }
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), shipmentInd, td.getQttBoxesInShipment());
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
		  manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPallets", onto), shipmentInd, td.getQttPalletsInShipment());
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
		  manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), shipmentInd, td.getYear()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
		  manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), shipmentInd, td.getSeason()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
		  manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), shipmentInd, td.getWeekDay()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
		  manager.applyChange(addAxiomChange);
		  
		  
		  }
		 
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}
	
	public void setShippedOn(OWLLiteral shippedOn) {
		this.shippedOn = shippedOn;
	}


	public OWLLiteral getShippedOn() {
		return shippedOn;
	}
	
	
	public OWLLiteral getExpectedDeliveryOn() {
		return expectedDeliveryOn;
	}


	public void setExpectedDeliveryOn(OWLLiteral expectedDeliveryOn) {
		this.expectedDeliveryOn = expectedDeliveryOn;
	}


	public OWLLiteral getQttBoxesInShipment() {
		return qttBoxes;
	}


	public void setQttBoxesInShipment(OWLLiteral qttBoxesInShipment) {
		this.qttBoxes = qttBoxesInShipment;
	}


	public OWLLiteral getQttPalletsInShipment() {
		return qttPallets;
	}


	public void setQttPalletsInShipment(OWLLiteral qttPalletsInShipment) {
		this.qttPallets = qttPalletsInShipment;
	}


	public String getShipmentId() {
		return shipmentId;
	}
	
	
	public String getShipperAdditionalPartyIdentification() {
		return shipperAdditionalPartyIdentification;
	}


	public String getReceiverAdditionalPartyIdentification() {
		return receiverAdditionalPartyIdentification;
	}



	public void setShipmentId(String consignmentId) {
		this.shipmentId = consignmentId;
	}


	public void setShipperAdditionalPartyIdentification(String consignorAdditionalPartyIdentification) {
		this.shipperAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
	}


	public void setReceiverAdditionalPartyIdentification(String consigneeAdditionalPartyIdentification) {
		this.receiverAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
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


	public OWLLiteral getPlannedDeliveryDate() {
		return plannedDeliveryDate;
	}


	public void setPlannedDeliveryDate(OWLLiteral plannedDeliveryDate) {
		this.plannedDeliveryDate = plannedDeliveryDate;
	}


	public String getOriginalDataSource() {
		return originalDataSource;
	}


	public void setOriginalDataSource(String originalDataSource) {
		this.originalDataSource = originalDataSource;
	}
	
	public String toString() {
		
		return shipmentId+""+shippedOn+""+expectedDeliveryOn+""+shipperAdditionalPartyIdentification+""+receiverAdditionalPartyIdentification+""+plannedDeliveryDate+""+qttBoxes+""+qttPallets+""+
				originalDataSource+""+year+""+season+""+weekDay;
	}


}
