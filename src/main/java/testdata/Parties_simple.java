package testdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

/**
 * @author audunvennesland
 *
 */
public class Parties_simple {

	public static void processParties(File partiesFolder, String rdf4jServer, String repositoryId, String baseURI) {

		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);
		String geosparqlURI = "http://www.opengis.net/ont/geosparql#";

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI partyInd;
			IRI partyClass = vf.createIRI(baseURI, "Party");;

			File[] filesInDir = partiesFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {


				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

						//adding types
						partyInd = vf.createIRI(baseURI, params[1] + "-" + params[3] + "_party");
						connection.add(partyInd, RDF.TYPE, partyClass);

						//adding literals
						connection.add(partyInd, vf.createIRI(baseURI + "additionalPartyIdentification"), vf.createLiteral(params[1]));
						connection.add(partyInd, vf.createIRI(baseURI + "gln"), vf.createLiteral(params[2]));
						connection.add(partyInd, vf.createIRI(baseURI + "hashCode"), vf.createLiteral(params[3]));
						connection.add(partyInd, vf.createIRI(baseURI + "code2"), vf.createLiteral(params[4]));
						connection.add(partyInd, vf.createIRI(baseURI + "location"), vf.createLiteral(params[51]));
						connection.add(partyInd, vf.createIRI(baseURI + "postalCode"), vf.createLiteral(params[6]));
						connection.add(partyInd, vf.createIRI(baseURI + "isHub"), vf.createLiteral(params[7]));
						connection.add(partyInd, vf.createIRI(baseURI + "isShipper"), vf.createLiteral(params[8]));
						connection.add(partyInd, vf.createIRI(baseURI + "isCarrier"), vf.createLiteral(params[9]));
						connection.add(partyInd, vf.createIRI(baseURI + "isConsignor"), vf.createLiteral(params[10]));
						connection.add(partyInd, vf.createIRI(geosparqlURI + "asWKT"), vf.createLiteral(formatCoordinates(params[11] + "," + params[12])));

					}//end while

				} catch (IOException e) {

					e.printStackTrace();

				} finally {

					try {
						if (br != null)
							br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}


			}

		}
		repo.shutDown();

	}

	//	public static void processParties(File partiesFolder, String ontoFilePath) throws OWLOntologyCreationException {
	//		
	//		//import ontology
	//		File ontoFile = new File(ontoFilePath);
	//		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	//
	//		//point to a local folder containing local copies of ontologies to sort out the imports
	//		AutoIRIMapper mapper=new AutoIRIMapper(new File(ontoFilePath.substring(0, ontoFilePath.lastIndexOf("/"))), true);		
	//		manager.addIRIMapper(mapper);
	//				
	//		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
	//		
	//		OWLClass partyClass = OntologyOperations.getClass("Party", onto);
	//		OWLDataFactory df = manager.getOWLDataFactory();
	//		OWLIndividual partyInd;
	//		OWLAxiom classAssertionAxiom; 
	//		OWLAxiom DPAssertionAxiom; 
	//		AddAxiom addAxiomChange;
	//		
	//		File[] filesInDir = partiesFolder.listFiles();
	//		String[] params = null;
	//		BufferedReader br = null;
	//		
	//		for (int i = 0; i < filesInDir.length; i++) {
	//
	//			System.out.println("Reading file: " + filesInDir[i].getName());
	//
	//			try {
	//
	//				String line;		
	//
	//				br = new BufferedReader(new FileReader(filesInDir[i]));
	//
	//				while ((line = br.readLine()) != null) {
	//
	//					params = line.split(",");
	//
	//
	//					//adding party individual using hashcode
	//					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[3] + "_party"));
	//					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);			
	//					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
	//					manager.applyChange(addAxiomChange);
	//
	//					//DP for expressing party details		
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("additionalPartyIdentification", onto), partyInd, params[1]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("gln", onto), partyInd, params[2]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hashCode", onto), partyInd, params[3]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("code2", onto), partyInd, params[4]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("location", onto), partyInd, params[5]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("postalCode", onto), partyInd, params[6]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isHub", onto), partyInd, params[7]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isShipper", onto), partyInd, params[8]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isCarrier", onto), partyInd, params[9]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isConsignor", onto), partyInd, params[10]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("asWKT", onto), partyInd, formatCoordinates(params[11] + "," + params[12]));
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//
	//					line = br.readLine();
	//
	//				}
	//
	//			}		catch (IOException e) {
	//
	//				e.printStackTrace();
	//
	//			} finally {
	//
	//				try {
	//					if (br != null)
	//						br.close();
	//				} catch (IOException ex) {
	//					ex.printStackTrace();
	//				}
	//			}
	//
	//			try {
	//				manager.saveOntology(onto);
	//			} catch (OWLOntologyStorageException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//
	//			//"force" garbage collection
	//			System.gc();
	//
	//		}
	//		
	//		System.out.println("\nMemory profile for " + partiesFolder.getName() + ": ");
	//		System.out.println("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
	//		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
	//		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
	//		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB");  
	//		
	//	}


	//	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
	//
	//		//measure memory footprint of ontology creation
	//		Runtime runtimeOntologyCreation = Runtime.getRuntime();
	//		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
	//		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");
	//
	//
	//		//import ontology
	//		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");
	//
	//		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	//		//point to a local folder containing local copies of ontologies to sort out the imports
	//		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
	//		manager.addIRIMapper(mapper);
	//
	//		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
	//		OWLClass partyClass = OntologyOperations.getClass("Party", onto);
	//		OWLDataFactory df = manager.getOWLDataFactory();
	//		OWLIndividual partyInd;
	//		OWLAxiom classAssertionAxiom; 
	//		OWLAxiom DPAssertionAxiom; 
	//		AddAxiom addAxiomChange;
	//
	//		String CSV_folder = "./files/CSV/test_split";
	//		File folder = new File(CSV_folder);
	//		File[] filesInDir = folder.listFiles();
	//		String[] params = null;
	//
	//		BufferedReader br = null;
	//
	//		for (int i = 0; i < filesInDir.length; i++) {
	//
	//			System.out.println("Reading file: " + filesInDir[i].getName());
	//
	//			try {
	//
	//				String line;		
	//
	//				br = new BufferedReader(new FileReader(filesInDir[i]));
	//
	//				while ((line = br.readLine()) != null) {
	//
	//					params = line.split(",");
	//
	//
	//					//adding party individual using hashcode
	//					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[3] + "_party"));
	//					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);			
	//					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
	//					manager.applyChange(addAxiomChange);
	//
	//					//DP for expressing party details		
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("additionalPartyIdentification", onto), partyInd, params[1]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("gln", onto), partyInd, params[2]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hashCode", onto), partyInd, params[3]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("code2", onto), partyInd, params[4]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("location", onto), partyInd, params[5]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("postalCode", onto), partyInd, params[6]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isHub", onto), partyInd, params[7]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isShipper", onto), partyInd, params[8]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isCarrier", onto), partyInd, params[9]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isConsignor", onto), partyInd, params[10]);
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("asWKT", onto), partyInd, formatCoordinates(params[11] + "," + params[12]));
	//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
	//					manager.applyChange(addAxiomChange);
	//
	//
	//					line = br.readLine();
	//
	//				}
	//
	//			}		catch (IOException e) {
	//
	//				e.printStackTrace();
	//
	//			} finally {
	//
	//				try {
	//					if (br != null)
	//						br.close();
	//				} catch (IOException ex) {
	//					ex.printStackTrace();
	//				}
	//			}
	//
	//			manager.saveOntology(onto);
	//
	//			//"force" garbage collection
	//			System.gc();
	//
	//		}
	//
	//		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
	//		System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");
	//
	//		System.out.println("\nUsed Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
	//		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
	//		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
	//		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB");  
	//
	//	}

	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}


}
