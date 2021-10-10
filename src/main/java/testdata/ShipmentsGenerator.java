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
	String shipperGLN;
	String shipperHashCode;
	String receiverAdditionalPartyIdentification;
	String receiverGLN;
	String receiverHashCode;
	OWLLiteral qttBoxesInShipment;
	OWLLiteral qttPalletsInShipment;

	public ShipmentsGenerator(String shipmentId, String shipperGLN, String receiverGLN,
			String shipperAdditionalPartyIdentification, String shipperHashCode,
			String receiverAdditionalPartyIdentification, String receiverHashCode, OWLLiteral shippedOn,
			OWLLiteral expectedDeliveryOn, OWLLiteral qttBoxesInShipment, OWLLiteral qttPalletsInShipment) {

		this.shipmentId = shipmentId;
		this.shipperGLN = shipperGLN;
		this.receiverGLN = receiverGLN;
		this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
		this.shipperHashCode = shipperHashCode;
		this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
		this.receiverHashCode = receiverHashCode;
		this.shippedOn = shippedOn;
		this.expectedDeliveryOn = expectedDeliveryOn;
		this.qttBoxesInShipment = qttBoxesInShipment;
		this.qttPalletsInShipment = qttPalletsInShipment;
	}


	public ShipmentsGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		ShipmentsGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/Shipments_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<ShipmentsGenerator> dataset = new HashSet<ShipmentsGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new ShipmentsGenerator();
						
			data.setShipmentId(params[0]);
			data.setShippedOn(OntologyOperations.convertToDateTime(manager, params[3]));
			data.setExpectedDeliveryOn(OntologyOperations.convertToDateTime(manager, params[4]));
			data.setShipperAdditionalPartyIdentification(params[6]);
			data.setShipperGLN(params[7]);
			data.setShipperHashCode(params[8]);
			data.setReceiverAdditionalPartyIdentification(params[9]);
			data.setReceiverGLN(params[10]);
			data.setReceiverHashCode(params[11]);
			data.setQttBoxesInShipment(OntologyOperations.convertToInt(manager, params[18]));
			data.setQttPalletsInShipment(OntologyOperations.convertToInt(manager, params[19]));
			
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
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipperAdditionalPartyIdentification", onto), shipmentInd, td.getShipperAdditionalPartyIdentification());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipperGLN", onto), shipmentInd, td.getShipperGLN());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipperHashCode", onto), shipmentInd, td.getShipperHashCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("receiverAdditionalPartyIdentification", onto), shipmentInd, td.getReceiverAdditionalPartyIdentification());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("receiverGLN", onto), shipmentInd, td.getReceiverGLN());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("receiverHashCode", onto), shipmentInd, td.getReceiverHashCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesInShipment", onto), shipmentInd, td.getQttBoxesInShipment());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsInShipment", onto), shipmentInd, td.getQttPalletsInShipment());
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

	public void setShipmentId(String consignmentId) {
		this.shipmentId = consignmentId;
	}


	public void setShipperAdditionalPartyIdentification(String consignorAdditionalPartyIdentification) {
		this.shipperAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
	}


	public void setShipperGLN(String consignorGLN) {
		this.shipperGLN = consignorGLN;
	}


	public void setShipperHashCode(String consignorHashCode) {
		this.shipperHashCode = consignorHashCode;
	}



	public void setReceiverAdditionalPartyIdentification(String consigneeAdditionalPartyIdentification) {
		this.receiverAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
	}



	public void setReceiverGLN(String consigneeGLN) {
		this.receiverGLN = consigneeGLN;
	}


	public String getConsigneeHashCode() {
		return receiverHashCode;
	}


	public void setReceiverHashCode(String consigneeHashCode) {
		this.receiverHashCode = consigneeHashCode;
	}

}
