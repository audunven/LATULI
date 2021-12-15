package testdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
public class Transports_simple {

	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		OWLClass transportClass = OntologyOperations.getClass("Transport", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);
		OWLDataFactory df = manager.getOWLDataFactory();
		OWLIndividual transportInd;
		OWLIndividual hubInd;
		OWLAxiom classAssertionAxiom; 
		OWLAxiom OPAssertionAxiom; 
		OWLAxiom DPAssertionAxiom; 
		AddAxiom addAxiomChange;

		String CSV_folder = "./files/CSV/test_split";
		File folder = new File(CSV_folder);
		File[] filesInDir = folder.listFiles();
		String[] params = null;

		BufferedReader br = null;

		for (int i = 0; i < filesInDir.length; i++) {

			System.out.println("Reading file: " + filesInDir[i].getName());

			try {

				String line;		

				br = new BufferedReader(new FileReader(filesInDir[i]));

				while ((line = br.readLine()) != null) {
					params = line.split(",");

					if (!params[1].equals("0") && !params[1].equalsIgnoreCase("TransportId")) {

						transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "_transport"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
						manager.applyChange(addAxiomChange);

						//OP 
						hubInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[2] + "_party"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, hubInd);
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);			

						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubParty", onto), transportInd, hubInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);	

						//DP
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportId", onto), transportInd, params[1]);
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);

						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubHashCode", onto), transportInd, params[2]);
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);

						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("transportType", onto), transportInd, params[3]);
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);

						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), transportInd, params[4]);
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);

					}

					line = br.readLine();

				}

			}		catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			manager.saveOntology(onto);

			//"force" garbage collection
			System.gc();

		}

		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");

		System.out.println("\nUsed Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB");  
	}

}
