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
public class Parties {

	private String additionalPartyIdentification;
	private String gln;
	private String hashCode;
	private String code2;
	private String location;
	private String postalCode;
	private OWLLiteral isHub;
	private OWLLiteral isShipper;
	private OWLLiteral isCarrier;
	private OWLLiteral isConsignor;
	private String coordinates;
	
	private Parties(Builder builder) {
		
		this.additionalPartyIdentification = builder.additionalPartyIdentification;
		this.gln = builder.gln;
		this.hashCode = builder.hashCode;
		this.code2 = builder.code2;
		this.location = builder.location;
		this.postalCode = builder.postalCode;
		this.isHub = builder.isHub;
		this.isShipper = builder.isShipper;
		this.isConsignor = builder.isConsignor;
		this.isCarrier = builder.isCarrier;
		this.coordinates = builder.coordinates;
		
	}
	
	public static class Builder {
		
		private String additionalPartyIdentification;
		private String gln;
		private String hashCode;
		private String code2;
		private String location;
		private String postalCode;
		private OWLLiteral isHub;
		private OWLLiteral isShipper;
		private OWLLiteral isCarrier;
		private OWLLiteral isConsignor;
		private String coordinates;
		
		
		public Builder(String additionalPartyIdentification, String gln, String hashCode, String code2, String location, String postalCode, 
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
		
		public Parties build() {
			return new Parties(this);
		}	
	}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		 //measure memory footprint of object creation
		Runtime runtimeObjectCreation = Runtime.getRuntime();
	    long usedMemoryBeforeObjectCreation = runtimeObjectCreation.totalMemory() - runtimeObjectCreation.freeMemory();
	    System.out.println("Used Memory before object creation: " + usedMemoryBeforeObjectCreation/1000000 + " MB");
	    
		Parties data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_250000/Parties_multi.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<Parties> dataset = new HashSet<Parties>();
		
		//import ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		while (line != null) {
			params = line.split(",");

			data = new Parties.Builder(params[1], params[2], params[3], params[4],
					StringUtilities.removeWhiteSpace(params[5]),
					StringUtilities.removeWhiteSpace(params[6]),
					OntologyOperations.convertToBoolean(manager, params[7]),
					OntologyOperations.convertToBoolean(manager, params[8]),
					OntologyOperations.convertToBoolean(manager, params[9]),
					OntologyOperations.convertToBoolean(manager, params[10]),
							params[10] + "," + params[11]).build();

			dataset.add(data);
			line = br.readLine();

		}

		br.close();
		
		long usedMemoryAfterObjectCreation = runtimeObjectCreation.totalMemory() - runtimeObjectCreation.freeMemory();
	    System.out.println("Memory increased after object creation: " + (usedMemoryAfterObjectCreation-usedMemoryBeforeObjectCreation)/1000000 + " MB");
	    
	    //measure memory footprint of ontology creation
	    Runtime runtimeOntologyCreation = Runtime.getRuntime();
	    long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
	    System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

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

		//adding new parties
		for (Parties td : dataset) {
			
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
		//save the ontology
		manager.saveOntology(onto);
		
		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
	    System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");
	}
	
	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}


	public String getPostalCode() {
		return postalCode;
	}


	public String getCoordinates() {
		return coordinates;
	}

	public String getAdditionalPartyIdentification() {
		return additionalPartyIdentification;
	}

	public String getGln() {
		return gln;
	}

	public String getCode2() {
		return code2;
	}

	public String getLocation() {
		return location;
	}


	public String getHashCode() {
		return hashCode;
	}


	public OWLLiteral getIsHub() {
		return isHub;
	}

	public OWLLiteral getIsShipper() {
		return isShipper;
	}


	public OWLLiteral getIsCarrier() {
		return isCarrier;
	}


	public OWLLiteral getIsConsignor() {
		return isConsignor;
	}



}
