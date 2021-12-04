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
public class DangerousGoodsGenerator
{
	String loadingUnitId;
	String gtin;
	String shipmentId;
	OWLLiteral modifiedOn;


	public DangerousGoodsGenerator(String loadingUnitId, String gtin, String shipmentId, OWLLiteral modifiedOn) {
		this.loadingUnitId = loadingUnitId;
		this.gtin = gtin;
		this.shipmentId = shipmentId;
		this.modifiedOn = modifiedOn;
	}
	
	public DangerousGoodsGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		DangerousGoodsGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/DRGs_last_100000_Audun.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<DangerousGoodsGenerator> dataset = new HashSet<DangerousGoodsGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new DangerousGoodsGenerator();
			
			data.setLoadingUnitId(params[0]);	
			data.setGtin(params[1]);
			data.setShipmentId(params[2]);
			data.setModifiedOn(OntologyOperations.convertToDateTime(manager, params[3]));

			dataset.add(data);
			
			line = br.readLine();

		}

		br.close();



		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass dangerousGoodsClass = OntologyOperations.getClass("DangerousGoods", onto);
		OWLClass tradeItemClass = OntologyOperations.getClass("TradeItem", onto);
		OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual dangerousGoodsInd = null;
		OWLIndividual tradeItemInd = null;
		OWLIndividual shipmentInd = null;
		OWLIndividual loadingUnitInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (DangerousGoodsGenerator td : dataset) {
			iterator+=1;	

			//adding dangerous goods individual
			dangerousGoodsInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getGtin() + "-" + td.getLoadingUnitId() + "_dgr"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(dangerousGoodsClass, dangerousGoodsInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);

			//object properties
			tradeItemInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getGtin() + "_tradeitem"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(tradeItemClass, tradeItemInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("relatesToTradeItem", onto), dangerousGoodsInd, tradeItemInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getShipmentId() + "_shipment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("belongsToShipment", onto), dangerousGoodsInd, shipmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitId() + "_loadingunit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), dangerousGoodsInd, loadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			//data properties
			if (!td.getModifiedOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("modifiedOn", onto), dangerousGoodsInd, td.getModifiedOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}

		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}


	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentItemId) {
		this.shipmentId = shipmentItemId;
	}
	

	public String getLoadingUnitId() {
		return loadingUnitId;
	}

	public void setLoadingUnitId(String loadingUnitId) {
		this.loadingUnitId = loadingUnitId;
	}

	public String getGtin() {
		return gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	public OWLLiteral getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(OWLLiteral modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}
