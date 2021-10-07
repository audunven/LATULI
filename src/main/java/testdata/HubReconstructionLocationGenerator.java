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
public class HubReconstructionLocationGenerator
{

	String hubReconstructionLocationId;
	String hubReconstructionLocationAdditionalPartyIdentification;
	String hubReconstructionLocationHashCode;
	String hubReconstructionLocationLaneId;


	public HubReconstructionLocationGenerator(String hubReconstructionLocationId, String hubReconstructionLocationAdditionalPartyIdentification, String hubReconstructionLocationHashCode, String hubReconstructionLocationLaneId) {
		this.hubReconstructionLocationId = hubReconstructionLocationId;
		this.hubReconstructionLocationAdditionalPartyIdentification = hubReconstructionLocationAdditionalPartyIdentification;
		this.hubReconstructionLocationHashCode = hubReconstructionLocationHashCode;
		this.hubReconstructionLocationLaneId = hubReconstructionLocationLaneId;
	}

	public HubReconstructionLocationGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		HubReconstructionLocationGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/HubReconstructionLocations_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<HubReconstructionLocationGenerator> dataset = new HashSet<HubReconstructionLocationGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new HubReconstructionLocationGenerator();
						
			data.setHubReconstructionLocationId(params[0]);
			data.setHubReconstructionLocationAdditionalPartyIdentification(params[1]);
			data.setHubReconstructionLocationHashCode(params[3]);
			data.setHubReconstructionLocationLaneId(params[4]);


			dataset.add(data);
			line = br.readLine();

		}

		br.close();



		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual hubReconstructionLocationInd = null;

		
		OWLAxiom classAssertionAxiom = null; 
		//OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (HubReconstructionLocationGenerator td : dataset) {
			iterator+=1;	

			//individuals and association to classes
			hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHubReconstructionLocationHashCode() + "_hubReconstructionLocation"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			

			//data properties
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubReconstructionLocationAdditionalPartyIdentification", onto), hubReconstructionLocationInd, td.getHubReconstructionLocationAdditionalPartyIdentification());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubReconstructionLocationId", onto), hubReconstructionLocationInd, td.getHubReconstructionLocationId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubReconstructionLocationLaneId", onto), hubReconstructionLocationInd, td.getHubReconstructionLocationLaneId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			


		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}


	public String getHubReconstructionLocationId() {
		return hubReconstructionLocationId;
	}

	public void setHubReconstructionLocationId(String shipmentItemId) {
		this.hubReconstructionLocationId = shipmentItemId;
	}
	

	public String getHubReconstructionLocationAdditionalPartyIdentification() {
		return hubReconstructionLocationAdditionalPartyIdentification;
	}

	public void setHubReconstructionLocationAdditionalPartyIdentification(String loadingUnitId) {
		this.hubReconstructionLocationAdditionalPartyIdentification = loadingUnitId;
	}


	public String getHubReconstructionLocationHashCode() {
		return hubReconstructionLocationHashCode;
	}

	public void setHubReconstructionLocationHashCode(String hashCode) {
		this.hubReconstructionLocationHashCode = hashCode;
	}

	public String getHubReconstructionLocationLaneId() {
		return hubReconstructionLocationLaneId;
	}

	public void setHubReconstructionLocationLaneId(String laneId) {
		this.hubReconstructionLocationLaneId = laneId;
	}

}
