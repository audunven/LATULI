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
public class InboundTrucksGenerator {

	String truckId;
	OWLLiteral firstScan;
	OWLLiteral inboundTruckExpectedArrival;
	OWLLiteral palletNr;
	OWLLiteral totalWeight;
	OWLLiteral totalVolume;
	OWLLiteral numberOfLooseCartons;
	String trailerIdentification;
	OWLLiteral numberOfShipments;
	String m3HubId;
	
	

	public InboundTrucksGenerator(String truckId, OWLLiteral firstScan, OWLLiteral inboundTruckExpectedArrival, OWLLiteral palletNr,
			OWLLiteral totalWeight, OWLLiteral totalVolume, OWLLiteral numberOfLooseCartons,
			String trailerIdentification, OWLLiteral numberOfShipments, String m3HubId) {
		this.truckId = truckId;
		this.firstScan = firstScan;
		this.inboundTruckExpectedArrival = inboundTruckExpectedArrival;
		this.palletNr = palletNr;
		this.totalWeight = totalWeight;
		this.totalVolume = totalVolume;
		this.numberOfLooseCartons = numberOfLooseCartons;
		this.trailerIdentification = trailerIdentification;
		this.numberOfShipments = numberOfShipments;
		this.m3HubId = m3HubId;
	}


	public InboundTrucksGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		InboundTrucksGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/InboundTruck_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<InboundTrucksGenerator> dataset = new HashSet<InboundTrucksGenerator>();
		
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new InboundTrucksGenerator();
						
			data.setTruckId(params[1]);
			data.setTrailerIdentification(params[2]);
			data.setFirstScan(OntologyOperations.convertToDateTime(manager, params[3]));
			data.setNumberOfShipments(OntologyOperations.convertToInt(manager, params[4]));
			data.setExpectedArrival(OntologyOperations.convertToDateTime(manager, params[5]));
			data.setNumberOfLooseCartons(OntologyOperations.convertToInt(manager, params[6]));
			data.setTotalVolume(OntologyOperations.convertToDecimal(manager, params[7]));
			data.setTotalWeight(OntologyOperations.convertToDecimal(manager, params[8]));
			data.setPalletNr(OntologyOperations.convertToInt(manager, params[9]));
			data.setM3HubId(params[10]);

			
			dataset.add(data);
			line = br.readLine();

		}

		br.close();

		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass inboundTruckClass = OntologyOperations.getClass("InboundTruck", onto);
		OWLClass m3HubClass = OntologyOperations.getClass("HubReconstructionLocation", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual inboundTruckInd = null;
		OWLIndividual m3HubInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		//adding process chain
		for (InboundTrucksGenerator td : dataset) {
			iterator+=1;	

			//adding inbound truck individual
			inboundTruckInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getTruckId() + "_inboundTruck"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(inboundTruckClass, inboundTruckInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			
			if (!td.getM3HubId().equals("NULL")) {
			m3HubInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getM3HubId() + "m3HubId"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(m3HubClass, m3HubInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("m3HubId", onto), inboundTruckInd, m3HubInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			}

			
			if (!td.getFirstScan().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("firstScan", onto), inboundTruckInd, td.getFirstScan());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			if (!td.getExpectedArrival().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("inboundTruckExpectedArrival", onto), inboundTruckInd, td.getExpectedArrival());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("palletNr", onto), inboundTruckInd, td.getPalletNr());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalWeight", onto), inboundTruckInd, td.getTotalWeight());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("totalVolume", onto), inboundTruckInd, td.getTotalVolume());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("numberOfLooseCartons", onto), inboundTruckInd, td.getNumberOfLooseCartons());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("trailerIdentification", onto), inboundTruckInd, td.getTrailerIdentification());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("numberOfShipments", onto), inboundTruckInd, td.getNumberOfShipments());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			

		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}


	public String getTruckId() {
		return truckId;
	}


	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}


	public OWLLiteral getFirstScan() {
		return firstScan;
	}


	public void setFirstScan(OWLLiteral firstScan) {
		this.firstScan = firstScan;
	}


	public OWLLiteral getExpectedArrival() {
		return inboundTruckExpectedArrival;
	}


	public void setExpectedArrival(OWLLiteral expectedArrival) {
		this.inboundTruckExpectedArrival = expectedArrival;
	}


	public OWLLiteral getPalletNr() {
		return palletNr;
	}


	public void setPalletNr(OWLLiteral palletNr) {
		this.palletNr = palletNr;
	}


	public OWLLiteral getTotalWeight() {
		return totalWeight;
	}


	public void setTotalWeight(OWLLiteral totalWeight) {
		this.totalWeight = totalWeight;
	}


	public OWLLiteral getTotalVolume() {
		return totalVolume;
	}


	public void setTotalVolume(OWLLiteral totalVolume) {
		this.totalVolume = totalVolume;
	}


	public OWLLiteral getNumberOfLooseCartons() {
		return numberOfLooseCartons;
	}


	public void setNumberOfLooseCartons(OWLLiteral numberOfLooseCartons) {
		this.numberOfLooseCartons = numberOfLooseCartons;
	}


	public String getTrailerIdentification() {
		return trailerIdentification;
	}


	public void setTrailerIdentification(String trailerIdentification) {
		this.trailerIdentification = trailerIdentification;
	}


	public OWLLiteral getNumberOfShipments() {
		return numberOfShipments;
	}


	public void setNumberOfShipments(OWLLiteral numberOfShipments) {
		this.numberOfShipments = numberOfShipments;
	}


	public String getM3HubId() {
		return m3HubId;
	}


	public void setM3HubId(String m3HubId) {
		this.m3HubId = m3HubId;
	}




	

}
