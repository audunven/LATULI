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
public class TradeItemGenerator
{
	
	
	String gtin;
	String loadingUnitId;
	String shipmentId;
	OWLLiteral quantity;
	String material;
	String description;
	OWLLiteral supplierQuantity;
	OWLLiteral customerQuantity;
	String supplierProductDescription;
	String customerProductDescription;
	String supplierProductId;
	String customerProductId;
	OWLLiteral modifiedOn;
	String lotNumber;
	String purchaseOrder;
	String salesOrder;
	String handlingInstruction;
	String originalDataSource;

	public TradeItemGenerator(String gtin, String loadingUnitId, String shipmentId, OWLLiteral quantity,
			String material, String description, OWLLiteral supplierQuantity, OWLLiteral customerQuantity,
			String supplierProductDescription, String customerProductDescription, String supplierProductId,
			String customerProductId, OWLLiteral modifiedOn, String lotNumber, String purchaseOrder, String salesOrder,
			String handlingInstruction, String originalDataSource) {
		super();
		this.gtin = gtin;
		this.loadingUnitId = loadingUnitId;
		this.shipmentId = shipmentId;
		this.quantity = quantity;
		this.material = material;
		this.description = description;
		this.supplierQuantity = supplierQuantity;
		this.customerQuantity = customerQuantity;
		this.supplierProductDescription = supplierProductDescription;
		this.customerProductDescription = customerProductDescription;
		this.supplierProductId = supplierProductId;
		this.customerProductId = customerProductId;
		this.modifiedOn = modifiedOn;
		this.lotNumber = lotNumber;
		this.purchaseOrder = purchaseOrder;
		this.salesOrder = salesOrder;
		this.handlingInstruction = handlingInstruction;
		this.originalDataSource = originalDataSource;
	}


	public TradeItemGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		TradeItemGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_250000/TradeItems_last_250000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<TradeItemGenerator> dataset = new HashSet<TradeItemGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(",");

			data = new TradeItemGenerator();
						
			data.setShipmentId(params[0]);
			data.setLoadingUnitId(params[1]);	
			data.setGtin(params[2]);
			data.setQuantity(OntologyOperations.convertToDecimal(manager, params[3]));
			data.setMaterial(params[4]);
			data.setDescription(params[5]);
			data.setSupplierQuantity(OntologyOperations.convertToDecimal(manager, params[6]));
			data.setCustomerQuantity(OntologyOperations.convertToDecimal(manager, params[7]));
			data.setSupplierProductDescription(params[8]);
			data.setCustomerProductDescription(params[9]);
			data.setSupplierProductId(params[10]);
			data.setCustomerProductId(params[11]);
			data.setModifiedOn(OntologyOperations.convertToDateTime(manager, params[12]));
			data.setLotNumber(params[13]);
			data.setPurchaseOrder(params[14]);
			data.setSalesOrder(params[15]);
			data.setHandlingInstruction(params[16]);
			data.setOriginalDataSource(params[17]);

			dataset.add(data);
			
			line = br.readLine();

		}

		br.close();


		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass tradeItemClass = OntologyOperations.getClass("TradeItem", onto);
		OWLClass shipmentClass = OntologyOperations.getClass("Shipment", onto);
		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual tradeItemInd = null;
		OWLIndividual shipmentInd = null;
		OWLIndividual loadingUnitInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (TradeItemGenerator td : dataset) {
			iterator+=1;	

			//adding dangerous goods individual
			tradeItemInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipmentId() + "-" + td.getLoadingUnitId() + "_tradeitem"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(tradeItemClass, tradeItemInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);

			//object properties			
			shipmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getShipmentId() + "_shipment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(shipmentClass, shipmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("belongsToShipment", onto), tradeItemInd, shipmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitId() + "_loadingunit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), tradeItemInd, loadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			//data properties
			if (!td.getModifiedOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("modifiedOn", onto), tradeItemInd, td.getModifiedOn());
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


	public OWLLiteral getQuantity() {
		return quantity;
	}


	public void setQuantity(OWLLiteral quantity) {
		this.quantity = quantity;
	}


	public String getMaterial() {
		return material;
	}


	public void setMaterial(String material) {
		this.material = material;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public OWLLiteral getSupplierQuantity() {
		return supplierQuantity;
	}


	public void setSupplierQuantity(OWLLiteral supplierQuantity) {
		this.supplierQuantity = supplierQuantity;
	}


	public OWLLiteral getCustomerQuantity() {
		return customerQuantity;
	}


	public void setCustomerQuantity(OWLLiteral customerQuantity) {
		this.customerQuantity = customerQuantity;
	}


	public String getSupplierProductDescription() {
		return supplierProductDescription;
	}


	public void setSupplierProductDescription(String supplierProductDescription) {
		this.supplierProductDescription = supplierProductDescription;
	}


	public String getCustomerProductDescription() {
		return customerProductDescription;
	}


	public void setCustomerProductDescription(String customerProductDescription) {
		this.customerProductDescription = customerProductDescription;
	}


	public String getSupplierProductId() {
		return supplierProductId;
	}


	public void setSupplierProductId(String supplierProductId) {
		this.supplierProductId = supplierProductId;
	}


	public String getCustomerProductId() {
		return customerProductId;
	}


	public void setCustomerProductId(String customerProductId) {
		this.customerProductId = customerProductId;
	}


	public String getLotNumber() {
		return lotNumber;
	}


	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}


	public String getPurchaseOrder() {
		return purchaseOrder;
	}


	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}


	public String getSalesOrder() {
		return salesOrder;
	}


	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}


	public String getHandlingInstruction() {
		return handlingInstruction;
	}


	public void setHandlingInstruction(String handlingInstruction) {
		this.handlingInstruction = handlingInstruction;
	}


	public String getOriginalDataSource() {
		return originalDataSource;
	}


	public void setOriginalDataSource(String originalDataSource) {
		this.originalDataSource = originalDataSource;
	}
	
	

}
