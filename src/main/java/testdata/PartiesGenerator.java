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
public class PartiesGenerator {

	String additionalPartyIdentification;
	String gln;
	String partiesHashCode;
	String partyName;
	String addressDetail;
	String code3;
	String code2;
	String location;
	String postalCode;
	OWLLiteral modifiedOn;
	String coordinates;


	public PartiesGenerator(String additionalPartyIdentification, String gln, String partiesHashCode, String partyName,
			String addressDetail, String code3, String code2, String location, String postalCode, OWLLiteral modifiedOn,
			String coordinates) {
		this.additionalPartyIdentification = additionalPartyIdentification;
		this.gln = gln;
		this.partiesHashCode = partiesHashCode;
		this.partyName = partyName;
		this.addressDetail = addressDetail;
		this.code3 = code3;
		this.code2 = code2;
		this.location = location;
		this.postalCode = postalCode;
		this.modifiedOn = modifiedOn;
		this.coordinates = coordinates;
	}


	public PartiesGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		PartiesGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/Parties-filtered.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<PartiesGenerator> dataset = new HashSet<PartiesGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new PartiesGenerator();
			
			data.setAdditionalPartyIdentification(params[0]);
			data.setGln(params[1]);
			data.setPartiesHashCode(params[2]);
			data.setPartyName(params[3]);
			data.setAddressDetail(params[4]);
			data.setCode3(params[5]);
			data.setCode2(params[6]);
			data.setLocation(params[7]);
			data.setPostalCode(params[8]);
			data.setModifiedOn(OntologyOperations.convertToDateTime(manager, params[9]));
			data.setCoordinates(params[10]);


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
			partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getPartiesHashCode() + "_Party"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);


			//DP for expressing party details
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("hashCode", onto), partyInd, td.getPartiesHashCode());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);


			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("partyName", onto), partyInd, td.getPartyName());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("addressDetail", onto), partyInd, td.getAddressDetail());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("code3", onto), partyInd, td.getCode3());
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
			
			if (!td.getModifiedOn().getLiteral().startsWith("0000")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("modifiedOn", onto), partyInd, td.getModifiedOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange); 
			}
						
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


	public String getPartiesHashCode() {
		return partiesHashCode;
	}


	public void setPartiesHashCode(String partiesHashCode) {
		this.partiesHashCode = partiesHashCode;
	}


	public String getPartyName() {
		return partyName;
	}


	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}


	public String getCountry() {
		return code3;
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


	public String getAddressDetail() {
		return addressDetail;
	}


	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}


	public String getCode3() {
		return code3;
	}


	public void setCode3(String code3) {
		this.code3 = code3;
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


	public OWLLiteral getModifiedOn() {
		return modifiedOn;
	}


	public void setModifiedOn(OWLLiteral modifiedOn) {
		this.modifiedOn = modifiedOn;
	}



}
