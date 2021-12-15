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
public class Waves_simple {
	
	public static void processWaves(File wavesFolder, String ontoFilePath) throws OWLOntologyCreationException {
		
		//import ontology
		File ontoFile = new File(ontoFilePath);
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File(ontoFilePath.substring(0, ontoFilePath.lastIndexOf("/"))), true);		
		manager.addIRIMapper(mapper);
				
		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);

		OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);
		OWLDataFactory df = manager.getOWLDataFactory();
		OWLIndividual waveInd;
		OWLIndividual partyInd;
		OWLAxiom classAssertionAxiom; 
		OWLAxiom OPAssertionAxiom; 
		OWLAxiom DPAssertionAxiom; 
		AddAxiom addAxiomChange;


		File[] filesInDir = wavesFolder.listFiles();
		String[] params = null;
		BufferedReader br = null;

		for (int i = 0; i < filesInDir.length; i++) {

			System.out.println("Reading file: " + filesInDir[i].getName());

			try {

				String line;		

				br = new BufferedReader(new FileReader(filesInDir[i]));

				while ((line = br.readLine()) != null) {
					params = line.split(",");

					waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "_wave"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);


					//adding hubParty	
					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[4] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//OP 
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubParty", onto), waveInd, partyInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	



					//DPs
					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveId", onto), waveInd, params[1]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					if (!OntologyOperations.convertToDateTime(manager, params[2]).getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("releasedOn", onto), waveInd, OntologyOperations.convertToDateTime(manager, params[2]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
					}

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubHashCode", onto), waveInd, params[4]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					if (!OntologyOperations.convertToDateTime(manager, params[5]).getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("closedOn", onto), waveInd, OntologyOperations.convertToDateTime(manager, params[5]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
					}

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("status", onto), waveInd, params[6]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					if (!OntologyOperations.convertToDateTime(manager, params[7]).getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveStartProcessingOn", onto), waveInd, OntologyOperations.convertToDateTime(manager, params[7]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
					}

					if (!OntologyOperations.convertToDateTime(manager, params[8]).getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveEndProcessingOn", onto), waveInd, OntologyOperations.convertToDateTime(manager, params[8]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
					}

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttTrailers", onto), waveInd, OntologyOperations.convertToInt(manager, params[9]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), waveInd, OntologyOperations.convertToInt(manager, params[10]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);


					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesProcessed", onto), waveInd, OntologyOperations.convertToInt(manager, params[11]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsBuilt", onto), waveInd, OntologyOperations.convertToInt(manager, params[12]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttTasks", onto), waveInd, OntologyOperations.convertToInt(manager, params[13]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttShipments", onto), waveInd, OntologyOperations.convertToInt(manager, params[14]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), waveInd, OntologyOperations.convertToInt(manager, params[15]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					if (params.length == 19) {

						if (OntologyOperations.convertToDecimal(manager, params[16]) != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), waveInd, OntologyOperations.convertToDecimal(manager, params[16])); 
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
							manager.applyChange(addAxiomChange);
						}

						if (OntologyOperations.convertToDecimal(manager, params[17]) != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), waveInd, OntologyOperations.convertToDecimal(manager, params[17])); 
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
							manager.applyChange(addAxiomChange);
						}

						if (OntologyOperations.convertToDecimal(manager, params[18]) != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), waveInd, OntologyOperations.convertToDecimal(manager, params[18])); 
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
							manager.applyChange(addAxiomChange);
						} 
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

			try {
				manager.saveOntology(onto);
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//"force" garbage collection
			System.gc();

		}
		
		System.out.println("\nMemory profile for " + wavesFolder.getName() + ": ");
		System.out.println("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB"); 
		
		
	}

//	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
//
//		//measure memory footprint of ontology creation
//		Runtime runtimeOntologyCreation = Runtime.getRuntime();
//		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
//		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");
//
//
//		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");
//
//		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//		//point to a local folder containing local copies of ontologies to sort out the imports
//		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
//		manager.addIRIMapper(mapper);
//
//		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
//		OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
//		OWLClass partyClass = OntologyOperations.getClass("Party", onto);
//		OWLDataFactory df = manager.getOWLDataFactory();
//		OWLIndividual waveInd;
//		OWLIndividual partyInd;
//		OWLAxiom classAssertionAxiom; 
//		OWLAxiom OPAssertionAxiom; 
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
//					params = line.split(",");
//
//					waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "_wave"));
//					classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
//					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
//					manager.applyChange(addAxiomChange);
//
//
//					//adding hubParty	
//					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[4] + "_party"));
//					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);
//					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					//OP 
//					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubParty", onto), waveInd, partyInd);
//					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
//					manager.applyChange(addAxiomChange);	
//
//
//
//					//DPs
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveId", onto), waveInd, params[1]);
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					if (!OntologyOperations.convertToDateTime(manager, params[2]).getLiteral().equals("0000-00-00T00:00:00")) {
//						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("releasedOn", onto), waveInd, OntologyOperations.convertToDateTime(manager, params[2]));
//						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//						manager.applyChange(addAxiomChange);
//					}
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hubHashCode", onto), waveInd, params[4]);
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					if (!OntologyOperations.convertToDateTime(manager, params[5]).getLiteral().equals("0000-00-00T00:00:00")) {
//						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("closedOn", onto), waveInd, OntologyOperations.convertToDateTime(manager, params[5]));
//						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//						manager.applyChange(addAxiomChange);
//					}
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("status", onto), waveInd, params[6]);
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					if (!OntologyOperations.convertToDateTime(manager, params[7]).getLiteral().equals("0000-00-00T00:00:00")) {
//						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveStartProcessingOn", onto), waveInd, OntologyOperations.convertToDateTime(manager, params[7]));
//						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//						manager.applyChange(addAxiomChange);
//					}
//
//					if (!OntologyOperations.convertToDateTime(manager, params[8]).getLiteral().equals("0000-00-00T00:00:00")) {
//						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveEndProcessingOn", onto), waveInd, OntologyOperations.convertToDateTime(manager, params[8]));
//						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//						manager.applyChange(addAxiomChange);
//					}
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttTrailers", onto), waveInd, OntologyOperations.convertToInt(manager, params[9]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), waveInd, OntologyOperations.convertToInt(manager, params[10]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesProcessed", onto), waveInd, OntologyOperations.convertToInt(manager, params[11]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsBuilt", onto), waveInd, OntologyOperations.convertToInt(manager, params[12]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttTasks", onto), waveInd, OntologyOperations.convertToInt(manager, params[13]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttShipments", onto), waveInd, OntologyOperations.convertToInt(manager, params[14]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), waveInd, OntologyOperations.convertToInt(manager, params[15]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					if (params.length == 19) {
//
//						if (OntologyOperations.convertToDecimal(manager, params[16]) != null) {
//							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), waveInd, OntologyOperations.convertToDecimal(manager, params[16])); 
//							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
//							manager.applyChange(addAxiomChange);
//						}
//
//						if (OntologyOperations.convertToDecimal(manager, params[17]) != null) {
//							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), waveInd, OntologyOperations.convertToDecimal(manager, params[17])); 
//							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
//							manager.applyChange(addAxiomChange);
//						}
//
//						if (OntologyOperations.convertToDecimal(manager, params[18]) != null) {
//							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), waveInd, OntologyOperations.convertToDecimal(manager, params[18])); 
//							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
//							manager.applyChange(addAxiomChange);
//						} 
//					}
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
//	}


}
