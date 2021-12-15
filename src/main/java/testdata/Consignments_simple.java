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
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class Consignments_simple {


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);

		OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
		OWLClass transportClass = OntologyOperations.getClass("Transport", onto);
		OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual consignmentInd;
		OWLIndividual transportInd;
		OWLIndividual waveInd;
		OWLIndividual carrierInd;
		OWLIndividual consignorInd;
		OWLIndividual consigneeInd;
		OWLIndividual hubInd;
		OWLAxiom classAssertionAxiom; 
		OWLAxiom OPAssertionAxiom; 
		OWLAxiom DPAssertionAxiom; 
		AddAxiom addAxiomChange = null;

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

					//adding consignment individual
					consignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "_consignment"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, consignmentInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					//adding transport individual and OP			
					if (!params[2].equals("NULL")) {
						transportInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[2] + "_transport"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(transportClass, transportInd);			
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);

						//OP transportId from consignmentInd to transportInd
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("includesTransport", onto), consignmentInd, transportInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);			
					}

					//adding wave individual
					if (!params[12].equals("NULL")) {
						waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[12] + "_wave"));
						classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
						addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
						manager.applyChange(addAxiomChange);

						//OP waveId from consignmentInd to waveInd
						OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("processedByWave", onto), consignmentInd, waveInd);
						addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
						manager.applyChange(addAxiomChange);			
					}

					carrierInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[7] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, carrierInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//OP 
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasCarrierParty", onto), consignmentInd, carrierInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					consignorInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[9] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consignorInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//OP 
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsignorParty", onto), consignmentInd, consignorInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					consigneeInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[11] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consigneeInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//OP 
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsigneeParty", onto), consignmentInd, consigneeInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					hubInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[14] + "_party"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, hubInd);
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//OP 
					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubParty", onto), consignmentInd, hubInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);	

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentId", onto), consignmentInd, params[1]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("consignmentType", onto), consignmentInd, params[3]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLane", onto), consignmentInd, params[4]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionLocation", onto), consignmentInd, params[5]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);


					if (!OntologyOperations.convertToDateTime(manager, params[16]).getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskClosedOn", onto), consignmentInd, OntologyOperations.convertToDateTime(manager, params[16]));
						addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
						manager.applyChange(addAxiomChange);
					}

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("fullPalletConsignment", onto), consignmentInd, params[17]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), consignmentInd, OntologyOperations.convertToInt(manager, params[18]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPallets", onto), consignmentInd, OntologyOperations.convertToInt(manager, params[19]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttReconstructedPallets", onto), consignmentInd, OntologyOperations.convertToInt(manager, params[20]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttReconstructedParcels", onto), consignmentInd, OntologyOperations.convertToInt(manager, params[21]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), consignmentInd, params[22]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskId", onto), consignmentInd, params[23]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("taskDescription", onto), consignmentInd, params[24]);
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalConsignmentVolume", onto), consignmentInd, OntologyOperations.convertToDecimal(manager, params[25]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalConsignmentWeight", onto), consignmentInd, OntologyOperations.convertToDecimal(manager, params[26]));
					addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					if (params.length == 30) {

						if (params[27] != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), consignmentInd, OntologyOperations.convertToDecimal(manager, params[27]));
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
							manager.applyChange(addAxiomChange);
						}

						if (params[28] != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), consignmentInd, OntologyOperations.convertToDecimal(manager, params[28]));
							addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
							manager.applyChange(addAxiomChange);
						}
						if (params[29] != null) {
							DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), consignmentInd, OntologyOperations.convertToDecimal(manager, params[29]));
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
