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
public class XDocLoadingUnitGenerator
{

	String internalId;	
	OWLLiteral preSortScanOn;
	OWLLiteral reconstructedScanOn;	
	OWLLiteral volume;
	OWLLiteral weight;
	String loadingUnit;	
	String inboundConsignmentId;
	String outboundConsignmentId;
	String additionalPartyIdentification;
	String hubReconstructionAdditionalPartyIdentification;
	String shipperAdditionalPartyIdentification;
	String receiverAdditionalPartyIdentification;
	String carrierAdditionalPartyIdentification;
	String consignorAdditionalPartyIdentification;
	String consigneeAdditionalPartyIdentification;
	String reconstructionTypeId;
	OWLLiteral splitShipment; 
	
	String waveId;
	
	String inboundParentLoadingUnitId;
	String outboundParentLoadingUnitId;
	
	String originalDataSource;
	OWLLiteral year;
	OWLLiteral season;
	OWLLiteral weekDay;
	

	public XDocLoadingUnitGenerator(String internalId, OWLLiteral preSortScanOn, OWLLiteral reconstructedScanOn,
			OWLLiteral volume, OWLLiteral weight, String loadingUnit, String inboundConsignmentId,
			String outboundConsignmentId, String additionalPartyIdentification,
			String hubReconstructionAdditionalPartyIdentification, String shipperAdditionalPartyIdentification,
			String receiverAdditionalPartyIdentification, String carrierAdditionalPartyIdentification,
			String consignorAdditionalPartyIdentification, String consigneeAdditionalPartyIdentification,
			String reconstructionTypeId, OWLLiteral splitShipment, String waveId, String inboundParentLoadingUnitId,
			String outboundParentLoadingUnitId, String originalDataSource, OWLLiteral year, OWLLiteral season,
			OWLLiteral weekDay) {
		this.internalId = internalId;
		this.preSortScanOn = preSortScanOn;
		this.reconstructedScanOn = reconstructedScanOn;
		this.volume = volume;
		this.weight = weight;
		this.loadingUnit = loadingUnit;
		this.inboundConsignmentId = inboundConsignmentId;
		this.outboundConsignmentId = outboundConsignmentId;
		this.additionalPartyIdentification = additionalPartyIdentification;
		this.hubReconstructionAdditionalPartyIdentification = hubReconstructionAdditionalPartyIdentification;
		this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
		this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
		this.carrierAdditionalPartyIdentification = carrierAdditionalPartyIdentification;
		this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
		this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
		this.reconstructionTypeId = reconstructionTypeId;
		this.splitShipment = splitShipment;
		this.waveId = waveId;
		this.inboundParentLoadingUnitId = inboundParentLoadingUnitId;
		this.outboundParentLoadingUnitId = outboundParentLoadingUnitId;
		this.originalDataSource = originalDataSource;
		this.year = year;
		this.season = season;
		this.weekDay = weekDay;
	}


	public XDocLoadingUnitGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		XDocLoadingUnitGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/XDocLoadingUnits_multi_last_100000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<XDocLoadingUnitGenerator> dataset = new HashSet<XDocLoadingUnitGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		while (line != null) {
			params = line.split(",");

			data = new XDocLoadingUnitGenerator();
						
			//TODO: add waveId
			data.setInternalId(params[1]);
			data.setPreSortScanOn(OntologyOperations.convertToDateTime(manager, params[2]));
			data.setReconstructedScanOn(OntologyOperations.convertToDateTime(manager, params[3]));
			data.setVolume(OntologyOperations.convertToDecimal(manager, params[4]));
			data.setWeight(OntologyOperations.convertToDecimal(manager, params[5]));
			data.setLoadingUnit(params[6]);
			data.setInboundConsignmentId(params[7]);
			data.setOutboundConsignmentId(params[8]);	
			data.setAdditionalPartyIdentification(params[9]);
			data.setHubReconstructionAdditionalPartyIdentification(params[10]);
			data.setShipperAdditionalPartyIdentification(params[11]);
			data.setReceiverAdditionalPartyIdentification(params[12]);
			data.setCarrierAdditionalPartyIdentification(params[13]);
			data.setConsignorAdditionalPartyIdentification(params[14]);
			data.setConsigneeAdditionalPartyIdentification(params[15]);			
			data.setReconstructionTypeId(params[16]);
			data.setSplitShipment(OntologyOperations.convertToInt(manager, params[17]));
			data.setInboundParentLoadingUnitId(params[18]);
			data.setOutboundParentLoadingUnitId(params[19]);
			data.setOriginalDataSource(params[20]);
			
			if (params.length == 25) {
				
			data.setYear(OntologyOperations.convertToDecimal(manager, params[23]));
			data.setSeason(OntologyOperations.convertToDecimal(manager, params[24]));
			data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[25]));
			 
			
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

		OWLClass xDocLoadingUnitClass = OntologyOperations.getClass("XDocLoadingUnit", onto);
		OWLClass consignmentClass = OntologyOperations.getClass("Consignment", onto);
		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);
		OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);
		OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual xDocLoadingUnitInd = null;
		OWLIndividual inboundConsignmentInd = null;
		OWLIndividual outboundConsignmentInd = null;
		OWLIndividual loadingUnitInd = null;
		OWLIndividual inboundParentLoadingUnitInd = null;
		OWLIndividual outboundParentLoadingUnitInd = null;
		OWLIndividual waveInd = null;
		OWLIndividual hubReconstructionLocationInd = null;
		OWLIndividual partyInd = null;
		OWLIndividual hubReconstructionInd = null;
		OWLIndividual shipperInd = null;
		OWLIndividual receiverInd = null;
		OWLIndividual carrierInd = null;
		OWLIndividual consignorInd = null;
		OWLIndividual consigneeInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (XDocLoadingUnitGenerator td : dataset) {
			iterator+=1;	

			//adding individual
			xDocLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" +td.getInternalId() + "_XDocLoadingUnit"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(xDocLoadingUnitClass, xDocLoadingUnitInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			
			
			//object properties
			hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getHubReconstructionAdditionalPartyIdentification() + "_HubReconstructionLocation");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReconstructionLocation", onto), xDocLoadingUnitInd, hubReconstructionLocationInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			
			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitForXDocLoadingUnit() + "_LoadingUnit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasLoadingUnit", onto), xDocLoadingUnitInd, loadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			inboundParentLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getInboundParentLoadingUnitId() + "_LoadingUnit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, inboundParentLoadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasInboundParentLoadingUnit", onto), xDocLoadingUnitInd, inboundParentLoadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			outboundParentLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getOutboundParentLoadingUnitId() + "_LoadingUnit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, outboundParentLoadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasOutboundParentLoadingUnit", onto), xDocLoadingUnitInd, outboundParentLoadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			inboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getInboundConsignmentId() + "_Consignment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, inboundConsignmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasInboundConsignment", onto), xDocLoadingUnitInd, inboundConsignmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			outboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getOutboundConsignmentId() + "_Consignment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, outboundConsignmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasOutboundConsignment", onto), xDocLoadingUnitInd, outboundConsignmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			
			if (!td.getAdditionalPartyIdentification().equals("0") || !td.getAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getAdditionalPartyIdentification() + "_Party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasParty", onto), xDocLoadingUnitInd, partyInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding hub reconstruction party
			if (!td.getHubReconstructionAdditionalPartyIdentification().equals("0") || !td.getHubReconstructionAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				hubReconstructionInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHubReconstructionAdditionalPartyIdentification() + "_Party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, hubReconstructionInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubReconstructionParty", onto), xDocLoadingUnitInd, hubReconstructionInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding shipper party individual
			if (!td.getShipperAdditionalPartyIdentification().equals("0") || !td.getShipperAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				shipperInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getShipperAdditionalPartyIdentification() + "_Party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, shipperInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasShipperParty", onto), xDocLoadingUnitInd, shipperInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding receiver party individual
			if (!td.getReceiverAdditionalPartyIdentification().equals("0") || !td.getReceiverAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				receiverInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getReceiverAdditionalPartyIdentification() + "_Party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, receiverInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasReceiverParty", onto), xDocLoadingUnitInd, receiverInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding carrier party individual
			if (!td.getCarrierAdditionalPartyIdentification().equals("0") || !td.getCarrierAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				carrierInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getCarrierAdditionalPartyIdentification() + "_Party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, carrierInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasCarrierParty", onto), xDocLoadingUnitInd, carrierInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding consignor party individual
			if (!td.getConsignorAdditionalPartyIdentification().equals("0") || !td.getConsignorAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				consignorInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsignorAdditionalPartyIdentification() + "_Party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consignorInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsignorParty", onto), xDocLoadingUnitInd, consignorInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			
			//adding consignee party individual
			if (!td.getConsigneeAdditionalPartyIdentification().equals("0") || !td.getConsigneeAdditionalPartyIdentification().equals("Hub internal movements")) {
				
				consigneeInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getConsigneeAdditionalPartyIdentification() + "_Party"));
				classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, consigneeInd);
				addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
				manager.applyChange(addAxiomChange);
				
				//OP 
				OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasConsigneeParty", onto), xDocLoadingUnitInd, consigneeInd);
				addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
				manager.applyChange(addAxiomChange);	

			}
			

			waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_Wave"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);

			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("processedByWave", onto), xDocLoadingUnitInd, waveInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			


			//data properties
			if (!td.getPreSortScanOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("preSortScanOn", onto), xDocLoadingUnitInd, td.getPreSortScanOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			if (!td.getReconstructedScanOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionScanOn", onto), xDocLoadingUnitInd, td.getReconstructedScanOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("volume", onto), xDocLoadingUnitInd, td.getVolume());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weight", onto), xDocLoadingUnitInd, td.getWeight());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionTypeId", onto), xDocLoadingUnitInd, td.getReconstructionTypeId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("splitShipment", onto), xDocLoadingUnitInd, td.getSplitShipment());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), xDocLoadingUnitInd, td.getOriginalDataSource());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), xDocLoadingUnitInd, td.getYear());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), xDocLoadingUnitInd, td.getSeason());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), xDocLoadingUnitInd, td.getWeekDay());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);


		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}


	


	public String getReconstructionTypeId() {
		return reconstructionTypeId;
	}


	public void setReconstructionTypeId(String reconstructionTypeId) {
		this.reconstructionTypeId = reconstructionTypeId;
	}


	public String getInternalId() {
		return internalId;
	}


	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}


	public OWLLiteral getPreSortScanOn() {
		return preSortScanOn;
	}


	public void setPreSortScanOn(OWLLiteral preSortScanOn) {
		this.preSortScanOn = preSortScanOn;
	}


	public OWLLiteral getReconstructedScanOn() {
		return reconstructedScanOn;
	}


	public void setReconstructedScanOn(OWLLiteral reconstructedScanOn) {
		this.reconstructedScanOn = reconstructedScanOn;
	}





	public String getInboundConsignmentId() {
		return inboundConsignmentId;
	}


	public void setInboundConsignmentId(String inboundConsignmentId) {
		this.inboundConsignmentId = inboundConsignmentId;
	}


	public String getOutboundConsignmentId() {
		return outboundConsignmentId;
	}


	public void setOutboundConsignmentId(String outboundConsignmentId) {
		this.outboundConsignmentId = outboundConsignmentId;
	}




	public String getLoadingUnitForXDocLoadingUnit() {
		return loadingUnit;
	}


	public void setLoadingUnitForXDocLoadingUnit(String loadingUnitForXDocLoadingUnit) {
		this.loadingUnit = loadingUnitForXDocLoadingUnit;
	}



	public OWLLiteral getxDocLoadingUnitVolume() {
		return volume;
	}


	public void setxDocLoadingUnitVolume(OWLLiteral xDocLoadingUnitVolume) {
		this.volume = xDocLoadingUnitVolume;
	}


	public OWLLiteral getxDocLoadingUnitWeight() {
		return weight;
	}


	public void setxDocLoadingUnitWeight(OWLLiteral xDocLoadingUnitWeight) {
		this.weight = xDocLoadingUnitWeight;
	}


	public String getInboundParentLoadingUnitForXDocLoadingUnit() {
		return inboundParentLoadingUnitId;
	}


	public void setInboundParentLoadingUnitForXDocLoadingUnit(String inboundParentLoadingUnitForXDocLoadingUnit) {
		this.inboundParentLoadingUnitId = inboundParentLoadingUnitForXDocLoadingUnit;
	}


	public String getOutboundParentLoadingUnitForXDocLoadingUnit() {
		return outboundParentLoadingUnitId;
	}


	public void setOutboundParentLoadingUnitForXDocLoadingUnit(String outboundParentLoadingUnitForXDocLoadingUnit) {
		this.outboundParentLoadingUnitId = outboundParentLoadingUnitForXDocLoadingUnit;
	}


	public OWLLiteral getVolume() {
		return volume;
	}


	public void setVolume(OWLLiteral volume) {
		this.volume = volume;
	}


	public OWLLiteral getWeight() {
		return weight;
	}


	public void setWeight(OWLLiteral weight) {
		this.weight = weight;
	}


	public String getLoadingUnit() {
		return loadingUnit;
	}


	public void setLoadingUnit(String loadingUnit) {
		this.loadingUnit = loadingUnit;
	}


	public String getAdditionalPartyIdentification() {
		return additionalPartyIdentification;
	}


	public void setAdditionalPartyIdentification(String additionalPartyIdentification) {
		this.additionalPartyIdentification = additionalPartyIdentification;
	}


	public String getHubReconstructionAdditionalPartyIdentification() {
		return hubReconstructionAdditionalPartyIdentification;
	}


	public void setHubReconstructionAdditionalPartyIdentification(String hubReconstructionAdditionalPartyIdentification) {
		this.hubReconstructionAdditionalPartyIdentification = hubReconstructionAdditionalPartyIdentification;
	}


	public String getShipperAdditionalPartyIdentification() {
		return shipperAdditionalPartyIdentification;
	}


	public void setShipperAdditionalPartyIdentification(String shipperAdditionalPartyIdentification) {
		this.shipperAdditionalPartyIdentification = shipperAdditionalPartyIdentification;
	}


	public String getReceiverAdditionalPartyIdentification() {
		return receiverAdditionalPartyIdentification;
	}


	public void setReceiverAdditionalPartyIdentification(String receiverAdditionalPartyIdentification) {
		this.receiverAdditionalPartyIdentification = receiverAdditionalPartyIdentification;
	}


	public String getCarrierAdditionalPartyIdentification() {
		return carrierAdditionalPartyIdentification;
	}


	public void setCarrierAdditionalPartyIdentification(String carrierAdditionalPartyIdentification) {
		this.carrierAdditionalPartyIdentification = carrierAdditionalPartyIdentification;
	}


	public String getConsignorAdditionalPartyIdentification() {
		return consignorAdditionalPartyIdentification;
	}


	public void setConsignorAdditionalPartyIdentification(String consignorAdditionalPartyIdentification) {
		this.consignorAdditionalPartyIdentification = consignorAdditionalPartyIdentification;
	}


	public String getConsigneeAdditionalPartyIdentification() {
		return consigneeAdditionalPartyIdentification;
	}


	public void setConsigneeAdditionalPartyIdentification(String consigneeAdditionalPartyIdentification) {
		this.consigneeAdditionalPartyIdentification = consigneeAdditionalPartyIdentification;
	}


	public OWLLiteral getSplitShipment() {
		return splitShipment;
	}


	public void setSplitShipment(OWLLiteral splitShipment) {
		this.splitShipment = splitShipment;
	}


	public String getInboundParentLoadingUnitId() {
		return inboundParentLoadingUnitId;
	}


	public void setInboundParentLoadingUnitId(String inboundParentLoadingUnitId) {
		this.inboundParentLoadingUnitId = inboundParentLoadingUnitId;
	}


	public String getOutboundParentLoadingUnitId() {
		return outboundParentLoadingUnitId;
	}


	public void setOutboundParentLoadingUnitId(String outboundParentLoadingUnitId) {
		this.outboundParentLoadingUnitId = outboundParentLoadingUnitId;
	}


	public String getOriginalDataSource() {
		return originalDataSource;
	}


	public void setOriginalDataSource(String originalDataSource) {
		this.originalDataSource = originalDataSource;
	}


	public OWLLiteral getYear() {
		return year;
	}


	public void setYear(OWLLiteral year) {
		this.year = year;
	}


	public OWLLiteral getSeason() {
		return season;
	}


	public void setSeason(OWLLiteral season) {
		this.season = season;
	}


	public OWLLiteral getWeekDay() {
		return weekDay;
	}


	public void setWeekDay(OWLLiteral weekDay) {
		this.weekDay = weekDay;
	}


	public String getWaveId() {
		return waveId;
	}


	public void setWaveId(String waveId) {
		this.waveId = waveId;
	}
	
	

	
	
	
}
