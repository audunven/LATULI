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
public class ShipmentItemGenerator
{

	String shipmentItemId;
	String loadingUnitId;
	OWLLiteral shipmentItemModifiedOn;
	OWLLiteral quantity;



	public ShipmentItemGenerator(String shipmentItemId, String loadingUnitId, OWLLiteral shipmentItemModifiedOn, 
			OWLLiteral quantity) {
		this.shipmentItemId = shipmentItemId;
		this.loadingUnitId = loadingUnitId;
		this.shipmentItemModifiedOn = shipmentItemModifiedOn;
		this.quantity = quantity;
	}

	public ShipmentItemGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		ShipmentItemGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/ShipmentItems_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<ShipmentItemGenerator> dataset = new HashSet<ShipmentItemGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new ShipmentItemGenerator();
						
			data.setShipmentItemId(params[0]);
			data.setLoadingUnitId(params[1]);
			data.setShipmentItemModifiedOn(OntologyOperations.convertToDateTime(manager, params[2]));
			data.setQuantity(OntologyOperations.convertToInt(manager, params[3]));


			dataset.add(data);
			line = br.readLine();

		}

		br.close();



		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass shipmentItemClass = OntologyOperations.getClass("ShipmentItem", onto);
		OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual shipmentItemInd = null;
		OWLIndividual shipmentInd = null;
		OWLIndividual loadingUnitInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (ShipmentItemGenerator td : dataset) {
			iterator+=1;	

			//adding shipmentItem individual
			shipmentItemInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentItemId() + "-" + td.getLoadingUnitId() + "_shipmentItem"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentItemClass, shipmentItemInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			
			
			//object properties
			shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getShipmentItemId() + "_shipment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("shipmentItemShipmentId", onto), shipmentItemInd, shipmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitId() + "_loadingUnit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("shipmentItemLoadingUnit", onto), shipmentItemInd, loadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			//data properties
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipmentItemModifiedOn", onto), shipmentItemInd, td.getShipmentItemModifiedOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("quantity", onto), shipmentItemInd, td.getQuantity());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			


		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}


	public String getShipmentItemId() {
		return shipmentItemId;
	}

	public void setShipmentItemId(String shipmentItemId) {
		this.shipmentItemId = shipmentItemId;
	}
	

	public String getLoadingUnitId() {
		return loadingUnitId;
	}

	public void setLoadingUnitId(String loadingUnitId) {
		this.loadingUnitId = loadingUnitId;
	}

	public OWLLiteral getShipmentItemModifiedOn() {
		return shipmentItemModifiedOn;
	}

	public void setShipmentItemModifiedOn(OWLLiteral shipmentItemModifiedOn) {
		this.shipmentItemModifiedOn = shipmentItemModifiedOn;
	}


	public OWLLiteral getQuantity() {
		return quantity;
	}

	public void setQuantity(OWLLiteral quantity) {
		this.quantity = quantity;
	}


}
