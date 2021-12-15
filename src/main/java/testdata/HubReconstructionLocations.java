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
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class HubReconstructionLocations
{

	private String additionalPartyIdentification;
	private String hashCode;
	private String laneId;

	private HubReconstructionLocations(Builder builder) {

		this.additionalPartyIdentification = builder.additionalPartyIdentification;
		this.hashCode = builder.hashCode;
		this.laneId = builder.laneId;
	}


	public static class Builder {

		private String additionalPartyIdentification;
		private String hashCode;
		private String laneId;

		public Builder(String additionalPartyIdentification, String hashCode, String laneId) {		
			this.additionalPartyIdentification = additionalPartyIdentification;
			this.hashCode = hashCode;
			this.laneId = laneId;							
		}

		public HubReconstructionLocations build() {
			return new HubReconstructionLocations(this);
		}

	}


	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {

		HubReconstructionLocations data;
		Set<HubReconstructionLocations> dataset = new HashSet<HubReconstructionLocations>();

		String[] params = null;
		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/HubReconstructionLocations_multi_last_100000.csv"));

		String line = br.readLine();

		while (line != null) {
			params = line.split(",");

			data = new HubReconstructionLocations.Builder(StringUtilities.removeWhiteSpace(params[1]), params[1], params[2]).build();

			dataset.add(data);
			line = br.readLine();

		}

		br.close();


		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual hubReconstructionLocationInd = null;
		OWLIndividual partyInd = null;


		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (HubReconstructionLocations td : dataset) {
			iterator+=1;	

			//individuals and association to classes
			hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getAdditionalPartyIdentification() + "_hubreconstructionlocation"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);

			//object properties
			partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getHashCode() + "_party");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);

			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasParty", onto), hubReconstructionLocationInd, partyInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);


			//data properties
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("additionalPartyIdentification", onto), hubReconstructionLocationInd, td.getAdditionalPartyIdentification());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hashCode", onto), hubReconstructionLocationInd, td.getHashCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLane", onto), hubReconstructionLocationInd, td.getLaneId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);



		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}
	
	public String getAdditionalPartyIdentification() {
		return additionalPartyIdentification;
	}

	public String getHashCode() {
		return hashCode;
	}

	public String getLaneId() {
		return laneId;
	}

}
