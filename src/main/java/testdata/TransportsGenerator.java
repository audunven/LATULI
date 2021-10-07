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
public class TransportsGenerator {
		
	String transportId;
	OWLLiteral expectedArrival;
	String transportName;
	String transportType;


	public TransportsGenerator(String transportId, OWLLiteral expectedArrival, String transportName, String transportType) {
		this.transportId = transportId;
		this.expectedArrival = expectedArrival;
		this.transportName = transportName;
		this.transportType = transportType;

	}

	public TransportsGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		TransportsGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/Transports_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<TransportsGenerator> dataset = new HashSet<TransportsGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new TransportsGenerator();
			
			if (!params[0].equals("0") && !params[0].equalsIgnoreCase("TransportId")) {
			
			data.setTransportId(params[0]);
			data.setExpectedArrival(OntologyOperations.convertToDateTime(manager, params[2]));
			data.setTransportName(params[9]);
			data.setTransportType(params[11]);
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
		//OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		//adding process chain
		for (TransportsGenerator td : dataset) {
			iterator+=1;	

			//adding process chain individual
			transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTransportId() + "_transport"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);


			//DP for expressing process chain name and id
			if (!td.getExpectedArrival().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("expectedArrival", onto), transportInd, td.getExpectedArrival());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportName", onto), transportInd, df.getOWLLiteral(td.getTransportName().replaceAll(",", "_")));
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportType", onto), transportInd, df.getOWLLiteral(td.getTransportType().replaceAll(",", "_")));
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



	public OWLLiteral getExpectedArrival() {
		return expectedArrival;
	}



	public void setExpectedArrival(OWLLiteral owlLiteral) {
		this.expectedArrival = owlLiteral;
	}



	public String getTransportName() {
		return transportName;
	}



	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}



	public String getTransportType() {
		return transportType;
	}



	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	

}
