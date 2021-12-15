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
public class XDocLoadingUnits_simple {


	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {

		//measure runtime
		long startTime = System.nanoTime();

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

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		OWLClass xDocLoadingUnitClass = OntologyOperations.getClass("XDocLoadingUnit", onto);
		OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);
		OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);
		OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

		OWLIndividual xDocLoadingUnitInd;
		OWLIndividual inboundConsignmentInd;
		OWLIndividual outboundConsignmentInd;
		OWLIndividual loadingUnitInd;
		OWLIndividual inboundParentLoadingUnitInd;
		OWLIndividual outboundParentLoadingUnitInd;
		OWLIndividual waveInd;
		OWLIndividual hubReconstructionLocationInd;
		OWLIndividual partyInd;
		OWLIndividual shipperInd;
		OWLIndividual receiverInd;
		OWLIndividual carrierInd;
		OWLIndividual consignorInd;
		OWLIndividual consigneeInd;

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

					//adding individual
					xDocLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "_xdocloadingunit"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(xDocLoadingUnitClass, xDocLoadingUnitInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					//object properties
					hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + params[12] + "_party");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubReconstructionParty", onto), xDocLoadingUnitInd, hubReconstructionLocationInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + params[6] + "_loadingunit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), xDocLoadingUnitInd, loadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					inboundParentLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + params[26] + "_loadingunit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, inboundParentLoadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasInboundParentLoadingUnit", onto), xDocLoadingUnitInd, inboundParentLoadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					outboundParentLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + params[27] + "_loadingunit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, outboundParentLoadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasOutboundParentLoadingUnit", onto), xDocLoadingUnitInd, outboundParentLoadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					inboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + params[7] + "_consignment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, inboundConsignmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasInboundConsignment", onto), xDocLoadingUnitInd, inboundConsignmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					outboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + params[8] + "_consignment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, outboundConsignmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasOutboundConsignment", onto), xDocLoadingUnitInd, outboundConsignmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[10] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasParty", onto), xDocLoadingUnitInd, partyInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					shipperInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[14] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, shipperInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasShipperParty", onto), xDocLoadingUnitInd, shipperInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					receiverInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[16] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, receiverInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReceiverParty", onto), xDocLoadingUnitInd, receiverInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					carrierInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[18] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, carrierInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasCarrierParty", onto), xDocLoadingUnitInd, carrierInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					consignorInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[20] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consignorInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsignorParty", onto), xDocLoadingUnitInd, consignorInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					consigneeInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[22] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consigneeInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsigneeParty", onto), xDocLoadingUnitInd, consigneeInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[23] + "_wave"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("processedByWave", onto), xDocLoadingUnitInd, waveInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//data properties
					if (!OntologyOperations.convertToDateTime(manager, params[2]).getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("preSortScanOn", onto), xDocLoadingUnitInd, OntologyOperations.convertToDateTime(manager, params[2]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
					}

					if (!OntologyOperations.convertToDateTime(manager, params[3]).getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructedScanOn", onto), xDocLoadingUnitInd, OntologyOperations.convertToDateTime(manager, params[3]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
					}

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("volume", onto), xDocLoadingUnitInd, OntologyOperations.convertToDecimal(manager, params[4]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weight", onto), xDocLoadingUnitInd, OntologyOperations.convertToDecimal(manager, params[5]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionTypeId", onto), xDocLoadingUnitInd, params[24]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("splitShipment", onto), xDocLoadingUnitInd, OntologyOperations.convertToInt(manager, params[25]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), xDocLoadingUnitInd, params[28]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					if (params.length == 33) {

						if (OntologyOperations.convertToDecimal(manager, params[29]) != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), xDocLoadingUnitInd, OntologyOperations.convertToDecimal(manager, params[29]));
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
							manager.applyChange(addAxiomChange);
						}
						if (OntologyOperations.convertToDecimal(manager, params[30]) != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), xDocLoadingUnitInd, OntologyOperations.convertToDecimal(manager, params[30]));
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
							manager.applyChange(addAxiomChange);
						}
						if (OntologyOperations.convertToDecimal(manager, params[31]) != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), xDocLoadingUnitInd, OntologyOperations.convertToDecimal(manager, params[31]));
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
							manager.applyChange(addAxiomChange);
						}
						if (OntologyOperations.convertToDecimal(manager, params[32]) != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("delta", onto), xDocLoadingUnitInd, OntologyOperations.convertToDecimal(manager, params[32]));
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
							manager.applyChange(addAxiomChange);
						}

					}

					line = br.readLine();

				}

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

			manager.saveOntology(onto);
			
			//"force" garbage collection
			System.gc();

		}



		long endTime = System.nanoTime();		
		long timeElapsed = endTime - startTime;		
		System.err.println("The ontology generation process took: " + timeElapsed/60000000000.00 + " minutes");

		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");

		System.out.println("\nUsed Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB"); 
	}


}
