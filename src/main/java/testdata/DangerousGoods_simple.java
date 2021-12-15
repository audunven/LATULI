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
public class DangerousGoods_simple
{
	String loadingUnitId;
	String gtin;
	String shipmentId;
	OWLLiteral modifiedOn;


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
		OWLClass dangerousGoodsClass = OntologyOperations.getClass("DangerousGoods", onto);
		OWLClass tradeItemClass = OntologyOperations.getClass("TradeItem", onto);
		OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);
		OWLDataFactory df = manager.getOWLDataFactory();


		OWLIndividual dangerousGoodsInd;
		OWLIndividual tradeItemInd;
		OWLIndividual shipmentInd;
		OWLIndividual loadingUnitInd;		
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


					//adding dangerous goods individual
					dangerousGoodsInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "-" + params[0] + "_dgr"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(dangerousGoodsClass, dangerousGoodsInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					//object properties
					tradeItemInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + params[1] + "_tradeitem"));
					classAssertionAxiom = df.getOWLClassAssertionAxiom(tradeItemClass, tradeItemInd);			
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("relatesToTradeItem", onto), dangerousGoodsInd, tradeItemInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + params[2] + "_shipment");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("belongsToShipment", onto), dangerousGoodsInd, shipmentInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + params[0] + "_loadingunit");
					classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
					addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
					manager.applyChange(addAxiomChange);

					OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), dangerousGoodsInd, loadingUnitInd);
					addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
					manager.applyChange(addAxiomChange);

					//data properties
					if (!OntologyOperations.convertToDateTime(manager, params[3]).getLiteral().equals("0000-00-00T00:00:00")) {
						DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("modifiedOn", onto), dangerousGoodsInd, OntologyOperations.convertToDateTime(manager, params[3]));
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
