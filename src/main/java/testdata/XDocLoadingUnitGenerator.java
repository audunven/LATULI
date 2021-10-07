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
	OWLLiteral finishedScanOn;
	//OWLLiteral loadingOn;	omitted due to only NULL values in CSV
	String loadingUnitForXDocLoadingUnit;	
	String inboundConsignmentId;
	String outboundConsignmentId;
	String xDocLoadingUnitHubReconstructionLaneId;
	String xDocLoadingUnitHubReconstructionLocationHashCode;
	String xDocLoadingUnitWaveId;
	String reconstructionTypeId;
	
	OWLLiteral xDocLoadingUnitVolume;
	OWLLiteral xDocLoadingUnitWeight;
	String inboundParentLoadingUnitForXDocLoadingUnit;
	String outboundParentLoadingUnitForXDocLoadingUnit;


	public XDocLoadingUnitGenerator(String internalId, OWLLiteral preSortScanOn, OWLLiteral reconstructedScanOn,
			OWLLiteral finishedScanOn, String xDocLoadingUnitHubReconstructionLaneId,
			String xDocLoadingUnitHubReconstructionLocationHashCode, String inboundConsignmentId, String outboundConsignmentId,
			String xDocLoadingUnitWaveId, String loadingUnitForXDocLoadingUnit, String reconstructionTypeId, OWLLiteral xDocLoadingUnitVolume, OWLLiteral xDocLoadingUnitWeight,
			String inboundParentLoadingUnitForXDocLoadingUnit, String outboundParentLoadingUnitForXDocLoadingUnit) {
		this.internalId = internalId;
		this.preSortScanOn = preSortScanOn;
		this.reconstructedScanOn = reconstructedScanOn;
		this.finishedScanOn = finishedScanOn;
		//this.loadingOn = loadingOn; omitted due to only NULL values in CSV
		this.xDocLoadingUnitHubReconstructionLaneId = xDocLoadingUnitHubReconstructionLaneId;
		this.xDocLoadingUnitHubReconstructionLocationHashCode = xDocLoadingUnitHubReconstructionLocationHashCode;
		this.inboundConsignmentId = inboundConsignmentId;
		this.outboundConsignmentId = outboundConsignmentId;
		this.xDocLoadingUnitWaveId = xDocLoadingUnitWaveId;
		this.loadingUnitForXDocLoadingUnit = loadingUnitForXDocLoadingUnit;
		this.reconstructionTypeId = reconstructionTypeId;
		this.xDocLoadingUnitVolume = xDocLoadingUnitVolume;
		this.xDocLoadingUnitWeight = xDocLoadingUnitWeight;
		this.inboundParentLoadingUnitForXDocLoadingUnit = inboundParentLoadingUnitForXDocLoadingUnit;
		this.outboundParentLoadingUnitForXDocLoadingUnit = outboundParentLoadingUnitForXDocLoadingUnit;
	}


	public XDocLoadingUnitGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		XDocLoadingUnitGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/XDocLoadingUnits_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<XDocLoadingUnitGenerator> dataset = new HashSet<XDocLoadingUnitGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		while (line != null) {
			params = line.split(";");

			data = new XDocLoadingUnitGenerator();
						
			data.setInternalId(params[0]);
			data.setPreSortScanOn(OntologyOperations.convertToDateTime(manager, params[1]));
			data.setReconstructedScanOn(OntologyOperations.convertToDateTime(manager, params[2]));
			data.setFinishedScanOn(OntologyOperations.convertToDateTime(manager, params[3]));
			data.setxDocLoadingUnitVolume(OntologyOperations.convertToDecimal(manager,params[5]));
			data.setxDocLoadingUnitWeight(OntologyOperations.convertToDecimal(manager, params[6]));
			data.setLoadingUnitForXDocLoadingUnit(params[7]);
			data.setInboundConsignmentId(params[11]);
			data.setOutboundConsignmentId(params[12]);			
			data.setxDocLoadingUnitHubReconstructionLaneId(params[16]);
			data.setxDocLoadungUnitHubReconstructionLocationHashCode(params[20]);
			data.setxDocLoadingUnitWaveId(params[36]);
			data.setReconstructionTypeId(params[37]);
			data.setInboundParentLoadingUnitForXDocLoadingUnit(params[41]);
			data.setOutboundParentLoadingUnitForXDocLoadingUnit(params[42]);
			
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
		OWLClass hubReconstructionLocationClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual xDocLoadingUnitInd = null;
		OWLIndividual inboundConsignmentInd = null;
		OWLIndividual outboundConsignmentInd = null;
		OWLIndividual loadingUnitInd = null;
		OWLIndividual waveInd = null;
		OWLIndividual hubReconstructionLocationInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		for (XDocLoadingUnitGenerator td : dataset) {
			iterator+=1;	

			//adding individual
			xDocLoadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" +td.getInternalId() + "_xDocLoadingUnit"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(xDocLoadingUnitClass, xDocLoadingUnitInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			
			
			//object properties
			inboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getInboundConsignmentId() + "_consignment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, inboundConsignmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("inboundConsignmentId", onto), xDocLoadingUnitInd, inboundConsignmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			outboundConsignmentInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getOutboundConsignmentId() + "_consignment");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(consignmentClass, outboundConsignmentInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("outboundConsignmentId", onto), xDocLoadingUnitInd, outboundConsignmentInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getLoadingUnitForXDocLoadingUnit() + "_loadingUnit");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("loadingUnitForXDocLoadingUnit", onto), xDocLoadingUnitInd, loadingUnitInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			
			waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getxDocLoadingUnitWaveId() + "_wave");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("xDocLoadingUnitWaveId", onto), xDocLoadingUnitInd, waveInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			
			hubReconstructionLocationInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#") + td.getXDocLoadingUnitHubReconstructionLocationHashCode() + "_hubReconstructionLocation");
			classAssertionAxiom = df.getOWLClassAssertionAxiom(hubReconstructionLocationClass, hubReconstructionLocationInd);	
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("xDocLoadingUnitHubReconstructionLocationHashCode", onto), xDocLoadingUnitInd, hubReconstructionLocationInd);
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
			
			if (!td.getFinishedScanOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("finishedScanOn", onto), xDocLoadingUnitInd, td.getFinishedScanOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("xDocLoadingUnitHubReconstructionLaneId", onto), xDocLoadingUnitInd, td.getxDocLoadingUnitHubReconstructionLaneId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("reconstructionTypeId", onto), xDocLoadingUnitInd, td.getReconstructionTypeId());
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


	public OWLLiteral getFinishedScanOn() {
		return finishedScanOn;
	}


	public void setFinishedScanOn(OWLLiteral finishedScanOn) {
		this.finishedScanOn = finishedScanOn;
	}


// omitted due to only NULL values in CSV	
//	public OWLLiteral getLoadingOn() {
//		return loadingOn;
//	}
//
// omitted due to only NULL values in CSV
//	public void setLoadingOn(OWLLiteral loadingOn) {
//		this.loadingOn = loadingOn;
//	}


	public String getxDocLoadingUnitHubReconstructionLaneId() {
		return xDocLoadingUnitHubReconstructionLaneId;
	}


	public void setxDocLoadingUnitHubReconstructionLaneId(String xDocLoadingUnitHubReconstructionLaneId) {
		this.xDocLoadingUnitHubReconstructionLaneId = xDocLoadingUnitHubReconstructionLaneId;
	}


	public String getXDocLoadingUnitHubReconstructionLocationHashCode() {
		return xDocLoadingUnitHubReconstructionLocationHashCode;
	}


	public void setxDocLoadungUnitHubReconstructionLocationHashCode(String xDocLoadingUnitHubReconstructionLocationHashCode) {
		this.xDocLoadingUnitHubReconstructionLocationHashCode = xDocLoadingUnitHubReconstructionLocationHashCode;
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


	public String getxDocLoadingUnitWaveId() {
		return xDocLoadingUnitWaveId;
	}


	public void setxDocLoadingUnitWaveId(String xDocLoadingUnitWaveId) {
		this.xDocLoadingUnitWaveId = xDocLoadingUnitWaveId;
	}


	public String getLoadingUnitForXDocLoadingUnit() {
		return loadingUnitForXDocLoadingUnit;
	}


	public void setLoadingUnitForXDocLoadingUnit(String loadingUnitForXDocLoadingUnit) {
		this.loadingUnitForXDocLoadingUnit = loadingUnitForXDocLoadingUnit;
	}


	public String getxDocLoadingUnitHubReconstructionLocationHashCode() {
		return xDocLoadingUnitHubReconstructionLocationHashCode;
	}


	public void setxDocLoadingUnitHubReconstructionLocationHashCode(
			String xDocLoadingUnitHubReconstructionLocationHashCode) {
		this.xDocLoadingUnitHubReconstructionLocationHashCode = xDocLoadingUnitHubReconstructionLocationHashCode;
	}


	public OWLLiteral getxDocLoadingUnitVolume() {
		return xDocLoadingUnitVolume;
	}


	public void setxDocLoadingUnitVolume(OWLLiteral xDocLoadingUnitVolume) {
		this.xDocLoadingUnitVolume = xDocLoadingUnitVolume;
	}


	public OWLLiteral getxDocLoadingUnitWeight() {
		return xDocLoadingUnitWeight;
	}


	public void setxDocLoadingUnitWeight(OWLLiteral xDocLoadingUnitWeight) {
		this.xDocLoadingUnitWeight = xDocLoadingUnitWeight;
	}


	public String getInboundParentLoadingUnitForXDocLoadingUnit() {
		return inboundParentLoadingUnitForXDocLoadingUnit;
	}


	public void setInboundParentLoadingUnitForXDocLoadingUnit(String inboundParentLoadingUnitForXDocLoadingUnit) {
		this.inboundParentLoadingUnitForXDocLoadingUnit = inboundParentLoadingUnitForXDocLoadingUnit;
	}


	public String getOutboundParentLoadingUnitForXDocLoadingUnit() {
		return outboundParentLoadingUnitForXDocLoadingUnit;
	}


	public void setOutboundParentLoadingUnitForXDocLoadingUnit(String outboundParentLoadingUnitForXDocLoadingUnit) {
		this.outboundParentLoadingUnitForXDocLoadingUnit = outboundParentLoadingUnitForXDocLoadingUnit;
	}

	
	
	
}
