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
public class ShipmentsGenerator {

	String shipmentId;
	OWLLiteral shippedOn;
	OWLLiteral expectedDeliveryOn;
	String shipperAdditionalPartyIdentification;	
	String receiverAdditionalPartyIdentification;
	OWLLiteral plannedDeliveryDate;
	OWLLiteral qttBoxesInShipment;
	OWLLiteral qttPalletsInShipment;
	String originalDataSource;
	OWLLiteral year;
	OWLLiteral season;
	OWLLiteral weekDay;

	public ShipmentsGenerator(String shipmentId, String shipperGLN, String receiverGLN,
			String shipperAdditionalPartyIdentification, String shipperHashCode,
			String receiverAdditionalPartyIdentification, String receiverHashCode, OWLLiteral shippedOn,
			OWLLiteral expectedDeliveryOn, OWLLiteral plannedDeliveryDate, OWLLiteral qttBoxesInShipment, OWLLiteral qttPalletsInShipment,
			String originalDataSource, OWLLiteral year, OWLLiteral season, OWLLiteral weekDay) {

		this.shipmentId = shipmentId;
		this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
		this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
		this.shippedOn = shippedOn;
		this.expectedDeliveryOn = expectedDeliveryOn;
		this.plannedDeliveryDate = plannedDeliveryDate;
		this.qttBoxesInShipment = qttBoxesInShipment;
		this.qttPalletsInShipment = qttPalletsInShipment;
		this.originalDataSource = originalDataSource;
		this.year = year;
		this.season = season;
		this.weekDay = weekDay;
	}


	public ShipmentsGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		ShipmentsGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/Shipments_multi_last_100000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<ShipmentsGenerator> dataset = new HashSet<ShipmentsGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(",");

			data = new ShipmentsGenerator();
						
			data.setShipmentId(params[1]);
			data.setShippedOn(OntologyOperations.convertToDateTime(manager, params[2]));
			data.setExpectedDeliveryOn(OntologyOperations.convertToDateTime(manager, params[3]));
			data.setShipperAdditionalPartyIdentification(params[4]);
			data.setReceiverAdditionalPartyIdentification(params[5]);
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

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual shipmentInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		//OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		//adding process chain
		
		  for (ShipmentsGenerator td : dataset) { 
			  
		  iterator+=1;
		  
		  //adding shipment individual 
		  shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentId() + "_shipment")); 
		  classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd); 
		  addAxiomChange = new AddAxiom(onto, classAssertionAxiom); 
		  manager.applyChange(addAxiomChange);
		  
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
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipperAdditionalPartyIdentification", onto), shipmentInd,
		  td.getShipperAdditionalPartyIdentification()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("receiverAdditionalPartyIdentification", onto), shipmentInd,
		  td.getReceiverAdditionalPartyIdentification()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); manager.applyChange(addAxiomChange);
		  
		  if (!td.getPlannedDeliveryDate().getLiteral().startsWith("0000")) {
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("plannedDeliveryDate", onto), shipmentInd, td.getPlannedDeliveryDate());
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
		  manager.applyChange(addAxiomChange); 
		  }
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesInShipment", onto), shipmentInd, td.getQttBoxesInShipment());
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
		  manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsInShipment", onto), shipmentInd, td.getQttPalletsInShipment());
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
		  manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hasYear", onto), shipmentInd, td.getYear()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hasSeason", onto), shipmentInd, td.getSeason()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); manager.applyChange(addAxiomChange);
		  
		  DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hasWeekDay", onto), shipmentInd, td.getWeekDay()); 
		  addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); manager.applyChange(addAxiomChange);
		  
		  
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
		return qttBoxesInShipment;
	}


	public void setQttBoxesInShipment(OWLLiteral qttBoxesInShipment) {
		this.qttBoxesInShipment = qttBoxesInShipment;
	}


	public OWLLiteral getQttPalletsInShipment() {
		return qttPalletsInShipment;
	}


	public void setQttPalletsInShipment(OWLLiteral qttPalletsInShipment) {
		this.qttPalletsInShipment = qttPalletsInShipment;
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
		
		return shipmentId+""+shippedOn+""+expectedDeliveryOn+""+shipperAdditionalPartyIdentification+""+receiverAdditionalPartyIdentification+""+plannedDeliveryDate+""+qttBoxesInShipment+""+qttPalletsInShipment+""+
				originalDataSource+""+year+""+season+""+weekDay;
	}


}
