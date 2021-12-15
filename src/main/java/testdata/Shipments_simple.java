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
//import org.semanticweb.owlapi.model.OWLDocumentFormat;
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
public class Shipments_simple {
	
	public static void processShipments(File shipmentsFolder, String ontoFilePath) throws OWLOntologyCreationException {
		
		//import ontology
		File ontoFile = new File(ontoFilePath);
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File(ontoFilePath.substring(0, ontoFilePath.lastIndexOf("/"))), true);		
		manager.addIRIMapper(mapper);
				
		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);

		//OWLDocumentFormat format = manager.getOntologyFormat(onto);

		OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
		OWLClass shipperClass = OntologyOperations.getClass("Shipper", onto);
		OWLClass receiverClass = OntologyOperations.getClass("Receiver", onto);
		OWLDataFactory df = manager.getOWLDataFactory();
		OWLIndividual shipmentInd;
		OWLIndividual shipperInd;
		OWLIndividual receiverInd;
		OWLAxiom classAssertionAxiom; 
		OWLAxiom OPAssertionAxiom; 
		OWLAxiom DPAssertionAxiom; 

		AddAxiom addAxiomChange;


		File[] filesInDir = shipmentsFolder.listFiles();
		
		System.out.println("Folder with csv files: " + shipmentsFolder.getName() + " holds " + filesInDir.length + " csv files");
		
		String[] params = null;
		BufferedReader br = null;
		
		for (int i = 0; i < filesInDir.length; i++) {

			System.out.println("Reading file: " + filesInDir[i].getName());

			try {

				String line;		

				br = new BufferedReader(new FileReader(filesInDir[i]));

				while ((line = br.readLine()) != null) {
					params = line.split(",");


					//adding shipment individual 
					shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "_shipment")); 
					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd); 
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom); 
					manager.applyChange(addAxiomChange);

					//adding shipper individual

					shipperInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[5]+ "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipperClass, shipperInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//OP waveId from shipmentInd to shipperInd
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasShipperParty", onto), shipmentInd, shipperInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);			


					//adding receiver individual

					receiverInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[7] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(receiverClass, receiverInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//OP waveId from shipmentInd to receiverInd
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReceiverParty", onto), shipmentInd, receiverInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);			


					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipmentId", onto), shipmentInd, params[1]); 
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shippedOn", onto), shipmentInd, OntologyOperations.convertToDateTime(manager, params[2])); 
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					manager.applyChange(addAxiomChange);

					if (!OntologyOperations.convertToDateTime(manager, params[3]).getLiteral().startsWith("0000")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("expectedDeliveryOn", onto), shipmentInd, OntologyOperations.convertToDateTime(manager, params[3]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange); 
					}

					if (!OntologyOperations.convertToDateTime(manager, params[8]).getLiteral().startsWith("0000")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("plannedDeliveryDate", onto), shipmentInd, OntologyOperations.convertToDateTime(manager, params[8]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange); 
					}

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), shipmentInd, OntologyOperations.convertToInt(manager, params[9]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPallets", onto), shipmentInd, OntologyOperations.convertToInt(manager, params[10]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), shipmentInd, OntologyOperations.convertToDecimal(manager, params[12])); 
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), shipmentInd, OntologyOperations.convertToDecimal(manager, params[13])); 
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), shipmentInd, OntologyOperations.convertToDecimal(manager, params[14])); 
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
					manager.applyChange(addAxiomChange);


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
		
	}

//	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
//
//		//measure memory footprint of ontology creation
//		Runtime runtimeOntologyCreation = Runtime.getRuntime();
//		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
//		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");
//
//		//import manusquare ontology
//		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");
//
//		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//
//		//point to a local folder containing local copies of ontologies to sort out the imports
//		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
//		manager.addIRIMapper(mapper);
//
//		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
//
//		OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
//		OWLClass shipperClass = OntologyOperations.getClass("Shipper", onto);
//		OWLClass receiverClass = OntologyOperations.getClass("Receiver", onto);
//		OWLDataFactory df = manager.getOWLDataFactory();
//		OWLIndividual shipmentInd;
//		OWLIndividual shipperInd;
//		OWLIndividual receiverInd;
//		OWLAxiom classAssertionAxiom; 
//		OWLAxiom OPAssertionAxiom; 
//		OWLAxiom DPAssertionAxiom; 
//
//		AddAxiom addAxiomChange;
//
//		String CSV_folder = "./files/CSV/test_split";
//		File folder = new File(CSV_folder);
//		File[] filesInDir = folder.listFiles();
//		String[] params = null;
//
//		BufferedReader br = null;
//
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
//
//					//adding shipment individual 
//					shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "_shipment")); 
//					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd); 
//					addAxiomChange = new AddAxiom(onto, classAssertionAxiom); 
//					manager.applyChange(addAxiomChange);
//
//					//adding shipper individual
//
//					shipperInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[5]+ "_party"));
//					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipperClass, shipperInd);			
//					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					//OP waveId from shipmentInd to shipperInd
//					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasShipperParty", onto), shipmentInd, shipperInd);
//					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
//					manager.applyChange(addAxiomChange);			
//
//
//					//adding receiver individual
//
//					receiverInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[7] + "_party"));
//					classAssertionAxiom = df.getOWLClassAssertionAxiom(receiverClass, receiverInd);			
//					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					//OP waveId from shipmentInd to receiverInd
//					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReceiverParty", onto), shipmentInd, receiverInd);
//					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
//					manager.applyChange(addAxiomChange);			
//
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shipmentId", onto), shipmentInd, params[1]); 
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("shippedOn", onto), shipmentInd, OntologyOperations.convertToDateTime(manager, params[2])); 
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
//					manager.applyChange(addAxiomChange);
//
//					if (!OntologyOperations.convertToDateTime(manager, params[3]).getLiteral().startsWith("0000")) {
//						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("expectedDeliveryOn", onto), shipmentInd, OntologyOperations.convertToDateTime(manager, params[3]));
//						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//						manager.applyChange(addAxiomChange); 
//					}
//
//					if (!OntologyOperations.convertToDateTime(manager, params[8]).getLiteral().startsWith("0000")) {
//						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("plannedDeliveryDate", onto), shipmentInd, OntologyOperations.convertToDateTime(manager, params[8]));
//						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//						manager.applyChange(addAxiomChange); 
//					}
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), shipmentInd, OntologyOperations.convertToInt(manager, params[9]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPallets", onto), shipmentInd, OntologyOperations.convertToInt(manager, params[10]));
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), shipmentInd, OntologyOperations.convertToDecimal(manager, params[12])); 
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), shipmentInd, OntologyOperations.convertToDecimal(manager, params[13])); 
//					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
//					manager.applyChange(addAxiomChange);
//
//					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), shipmentInd, OntologyOperations.convertToDecimal(manager, params[14])); 
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
//	}



}
