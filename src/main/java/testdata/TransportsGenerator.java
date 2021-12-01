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
public class TransportsGenerator {
		
	String transportId;
	String transportType;
	String originalDataSource;


	public TransportsGenerator(String transportId, String transportType, String originalDataSource) {
		this.transportId = transportId;
		this.transportType = transportType;
		this.originalDataSource = originalDataSource;

	}

	public TransportsGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		TransportsGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/Transports_multi_last_100000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<TransportsGenerator> dataset = new HashSet<TransportsGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(",");

			data = new TransportsGenerator();
			
			if (!params[1].equals("0") && !params[1].equalsIgnoreCase("TransportId")) {
			
			data.setTransportId(params[1]);
			data.setTransportType(params[2]);
			data.setOriginalDataSource(params[3]);
			
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

		OWLClass transportClass = OntologyOperations.getClass("Transport", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual transportInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;


		for (TransportsGenerator td : dataset) {
			iterator+=1;	

			transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTransportId() + "_transport"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportId", onto), transportInd, td.getTransportId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportType", onto), transportInd, td.getTransportType());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), transportInd, td.getOriginalDataSource());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);


		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}


	public String getTransportId() {
		return transportId;
	}



	public void setTransportId(String transportId) {
		this.transportId = transportId;
	}

	public String getTransportType() {
		return transportType;
	}


	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public String getOriginalDataSource() {
		return originalDataSource;
	}

	public void setOriginalDataSource(String originalDataSource) {
		this.originalDataSource = originalDataSource;
	}
	
	

	

}
