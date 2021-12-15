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
public class ShipmentItems
{

	private String shipmentId;
	private String loadingUnitId;
	private OWLLiteral quantity;
	private String originalDataSource;

	private ShipmentItems(Builder builder) {
		
		this.shipmentId = builder.shipmentId;
		this.loadingUnitId = builder.loadingUnitId;
		this.quantity = builder.quantity;
		this.originalDataSource = builder.originalDataSource;
	}

	public static class Builder {
		
		private String shipmentId;
		private String loadingUnitId;
		private OWLLiteral quantity;
		private String originalDataSource;
		
		public Builder(String shipmentId, String loadingUnitId, OWLLiteral quantity, String originalDataSource) {
			this.shipmentId = shipmentId;
			this.loadingUnitId = loadingUnitId;
			this.quantity = quantity;
			this.originalDataSource = originalDataSource;

		}
		
		public Builder setLoadingUnitId(String loadingUnitId) {
			this.loadingUnitId = loadingUnitId;
			return this;
		}

		public Builder setQuantity(OWLLiteral quantity) {
			this.quantity = quantity;
			return this;
		}

		public Builder setOriginalDataSource(String originalDataSource) {
			this.originalDataSource = originalDataSource;
			return this;
		}
		
		public ShipmentItems build() {
			return new ShipmentItems(this);
		}
		
	}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
		
	    //measure memory footprint of ontology creation
	    Runtime runtimeOntologyCreation = Runtime.getRuntime();
	    long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
	    System.out.println("Used memory before running the program: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		ShipmentItems data;
		Set<ShipmentItems> dataset = new HashSet<ShipmentItems>();
		String[] params = null;
		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_250000/ShipmentItems_multi_last_250000.csv"));

		//import ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		String line = br.readLine();

		while (line != null) {
			params = line.split(",");

			data = new ShipmentItems.Builder(params[1], params[2], OntologyOperations.convertToDecimal(manager, params[3]), params[4]).build();

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

		for (ShipmentItems td : dataset) {

			//adding shipmentItem individual
			shipmentItemInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentId() + "-" + td.getLoadingUnitId() + "_shipmentitem"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentItemClass, shipmentItemInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			
			
			//object properties
			shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getShipmentId() + "_shipment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("belongsToShipment", onto), shipmentItemInd, shipmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitId() + "_loadingunit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), shipmentItemInd, loadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			//data properties

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), shipmentItemInd, td.getOriginalDataSource());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("quantity", onto), shipmentItemInd, td.getQuantity());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			

		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
		
		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
	    System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");
	    
	    System.out.println("\nUsed Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
        System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
        System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
        System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB");  
	}
	
	public String getShipmentId() {
		return shipmentId;
	}

	public String getLoadingUnitId() {
		return loadingUnitId;
	}
	
	public OWLLiteral getQuantity() {
		return quantity;
	}
	
	public void setShipmentId(String shipmentItemId) {
		this.shipmentId = shipmentItemId;
	}
	
	public String getOriginalDataSource() {
		return originalDataSource;
	}

}
