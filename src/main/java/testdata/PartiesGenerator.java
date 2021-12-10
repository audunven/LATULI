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
public class PartiesGenerator {

	String additionalPartyIdentification;
	String gln;
	String hashCode;
	String code2;
	String location;
	String postalCode;
	OWLLiteral isHub;
	OWLLiteral isShipper;
	OWLLiteral isCarrier;
	OWLLiteral isConsignor;
	String coordinates;


	public PartiesGenerator(String additionalPartyIdentification, String gln, String hashCode, String code2, String location, String postalCode, 
			OWLLiteral isHub, OWLLiteral isShipper, OWLLiteral isCarrier, OWLLiteral isConsignor, String coordinates) {
		this.additionalPartyIdentification = additionalPartyIdentification;
		this.gln = gln;
		this.hashCode = hashCode;
		this.code2 = code2;
		this.location = location;
		this.postalCode = postalCode;
		this.isHub = isHub;
		this.isShipper = isShipper;
		this.isConsignor = isConsignor;
		this.isCarrier = isCarrier;
		this.coordinates = coordinates;
	}


	public PartiesGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		PartiesGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_250000/Parties_multi.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<PartiesGenerator> dataset = new HashSet<PartiesGenerator>();
		
		//import ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		String coordinates = null;

		while (line != null) {
			params = line.split(",");

			data = new PartiesGenerator();
			
			data.setAdditionalPartyIdentification(params[1]);
			data.setGln(params[2]);
			data.setHashCode(params[3]);
			data.setCode2(params[4]);
			data.setLocation(StringUtilities.removeWhiteSpace(params[5]));
			data.setPostalCode(StringUtilities.removeWhiteSpace(params[6]));
			data.setIsHub(OntologyOperations.convertToBoolean(manager, params[7]));
			data.setIsShipper(OntologyOperations.convertToBoolean(manager, params[8]));
			data.setIsCarrier(OntologyOperations.convertToBoolean(manager, params[9]));
			data.setIsConsignor(OntologyOperations.convertToBoolean(manager, params[10]));
			
			coordinates = params[11] + "," + params[12];
			
			System.out.println("Coordinates: " + coordinates);
			
			data.setCoordinates(coordinates);


			dataset.add(data);
			line = br.readLine();

		}

		br.close();

		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass partyClass = OntologyOperations.getClass("Party", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual partyInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		//adding new parties
		for (PartiesGenerator td : dataset) {
			iterator+=1;	

			//adding party individual
			if (!td.getAdditionalPartyIdentification().equals("nan")) {
			partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHashCode() + "_party"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			}


			//DP for expressing party details
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("additionalPartyIdentification", onto), partyInd, td.getAdditionalPartyIdentification());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("gln", onto), partyInd, td.getGln());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hashCode", onto), partyInd, td.getHashCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("code2", onto), partyInd, td.getCode2());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("location", onto), partyInd, td.getLocation());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("postalCode", onto), partyInd, td.getPostalCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isHub", onto), partyInd, td.getIsHub());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isShipper", onto), partyInd, td.getIsShipper());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isCarrier", onto), partyInd, td.getIsCarrier());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("isConsignor", onto), partyInd, td.getIsConsignor());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
						
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("asWKT", onto), partyInd, formatCoordinates(td.getCoordinates()));
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);


		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}
	
	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}


	public String getPostalCode() {
		return postalCode;
	}


	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}


	public String getCoordinates() {
		return coordinates;
	}


	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}


	public String getAdditionalPartyIdentification() {
		return additionalPartyIdentification;
	}


	public void setAdditionalPartyIdentification(String additionalPartyIdentification) {
		this.additionalPartyIdentification = additionalPartyIdentification;
	}


	public String getGln() {
		return gln;
	}


	public void setGln(String gln) {
		this.gln = gln;
	}


	public String getCode2() {
		return code2;
	}


	public void setCode2(String code2) {
		this.code2 = code2;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getHashCode() {
		return hashCode;
	}


	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}


	public OWLLiteral getIsHub() {
		return isHub;
	}


	public void setIsHub(OWLLiteral isHub) {
		this.isHub = isHub;
	}


	public OWLLiteral getIsShipper() {
		return isShipper;
	}


	public void setIsShipper(OWLLiteral isShipper) {
		this.isShipper = isShipper;
	}


	public OWLLiteral getIsCarrier() {
		return isCarrier;
	}


	public void setIsCarrier(OWLLiteral isCarrier) {
		this.isCarrier = isCarrier;
	}


	public OWLLiteral getIsConsignor() {
		return isConsignor;
	}


	public void setIsConsignor(OWLLiteral isConsignor) {
		this.isConsignor = isConsignor;
	}
	
	


}
