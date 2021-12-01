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
public class WaveGenerator {

	String waveId;
	OWLLiteral releasedOn;
	String hubAdditionalPartyIdentification;
	OWLLiteral closedOn;
	String status;
	
	OWLLiteral waveStartProcessingOn;
	OWLLiteral waveEndProcessingOn;
	OWLLiteral qttTrailers;
	OWLLiteral qttBoxes;
	OWLLiteral qttBoxesProcessed;
	OWLLiteral qttPalletsBuilt;
	OWLLiteral qttTasks;
	OWLLiteral qttShipments;
	
	String originalDataSource;
	OWLLiteral year;
	OWLLiteral season;
	OWLLiteral weekDay;


	public WaveGenerator(String waveId, OWLLiteral releasedOn, String hubAdditionalPartyIdentification, OWLLiteral closedOn, String status,
			OWLLiteral waveStartProcessingOn, OWLLiteral waveEndProcessingOn, OWLLiteral qttTrailers,
			OWLLiteral qttBoxes, OWLLiteral qttBoxesProcessed, OWLLiteral qttPalletsBuilt,
			OWLLiteral qttTasks, OWLLiteral qttShipments, String originalDataSource, OWLLiteral year, OWLLiteral season,
			OWLLiteral weekDay) {
		this.waveId = waveId;
		this.releasedOn = releasedOn;
		this.hubAdditionalPartyIdentification = hubAdditionalPartyIdentification;
		this.closedOn = closedOn;
		this.status = status;
		this.waveStartProcessingOn = waveStartProcessingOn;
		this.waveEndProcessingOn = waveEndProcessingOn;
		this.qttTrailers = qttTrailers;
		this.qttBoxes = qttBoxes;
		this.qttBoxesProcessed = qttBoxesProcessed;
		this.qttPalletsBuilt = qttPalletsBuilt;
		this.qttTasks = qttTasks;
		this.qttShipments = qttShipments;
		this.originalDataSource = originalDataSource;
		this.year = year;
		this.season = season;
		this.weekDay = weekDay;
	}


	public WaveGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		WaveGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Truls/Tail_100000/Waves_multi_last_100000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<WaveGenerator> dataset = new HashSet<WaveGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(",");

			data = new WaveGenerator();
			
			System.out.println("Number of params: " + params.length);
						
			data.setWaveId(params[1]);
			data.setReleasedOn(OntologyOperations.convertToDateTime(manager, params[2]));
			data.setHubAdditionalPartyIdentification(params[3]);
			data.setClosedOn(OntologyOperations.convertToDateTime(manager, params[4]));
			data.setStatus(params[5]);
			data.setWaveStartProcessingOn(OntologyOperations.convertToDateTime(manager, params[6]));
			data.setWaveEndProcessingOn(OntologyOperations.convertToDateTime(manager, params[7]));
			data.setQttTrailers(OntologyOperations.convertToInt(manager, params[8]));
			data.setQttBoxes(OntologyOperations.convertToInt(manager, params[9]));
			data.setQttBoxesProcessed(OntologyOperations.convertToInt(manager, params[10]));			
			data.setQttPalletsBuilt(OntologyOperations.convertToInt(manager, params[11]));
			data.setQttTasks(OntologyOperations.convertToInt(manager, params[12]));
			data.setQttShipments(OntologyOperations.convertToInt(manager, params[13]));
			data.setOriginalDataSource(params[14]);
			
			if (params.length == 18) {
			
			data.setYear(OntologyOperations.convertToDecimal(manager, params[15]));
			data.setSeason(OntologyOperations.convertToDecimal(manager, params[16]));
			data.setWeekDay(OntologyOperations.convertToDecimal(manager, params[17]));

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

		OWLClass waveClass = OntologyOperations.getClass("Wave", onto);
		OWLClass partyClass = OntologyOperations.getClass("Party", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual waveInd = null;
		OWLIndividual partyInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		
		for (WaveGenerator td : dataset) {
			iterator+=1;	

			waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_wave"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);
			

			//adding hubParty	
			partyInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getHubAdditionalPartyIdentification() + "_Party"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(partyClass, partyInd);
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);
			manager.applyChange(addAxiomChange);
				
			//OP 
			OPAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(OntologyOperations.getObjectProperty("hasHubParty", onto), waveInd, partyInd);
			addAxiomChange = new AddAxiom(onto, OPAssertionAxiom);
			manager.applyChange(addAxiomChange);	



			//DPs
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveId", onto), waveInd, td.getWaveId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			if (!td.getReleasedOn().getLiteral().equals("0000-00-00T00:00:00")) {
				DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("releasedOn", onto), waveInd, td.getReleasedOn());
				addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
				manager.applyChange(addAxiomChange);
				}
			
			if (!td.getClosedOn().getLiteral().equals("0000-00-00T00:00:00")) {
				DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("closedOn", onto), waveInd, td.getClosedOn());
				addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
				manager.applyChange(addAxiomChange);
				}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("status", onto), waveInd, td.getStatus());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			if (!td.getWaveStartProcessingOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveStartProcessingOn", onto), waveInd, td.getWaveStartProcessingOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			if (!td.getWaveEndProcessingOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("waveEndProcessingOn", onto), waveInd, td.getWaveEndProcessingOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttTrailers", onto), waveInd, td.getQttTrailers());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxes", onto), waveInd, td.getQttBoxes());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesProcessed", onto), waveInd, td.getQttBoxesProcessed());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsBuilt", onto), waveInd, td.getQttPalletsBuilt());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttTasks", onto), waveInd, td.getQttTasks());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttShipments", onto), waveInd, td.getQttShipments());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			if (td.getYear() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("year", onto), waveInd, td.getYear()); 
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
			manager.applyChange(addAxiomChange);
			}
			  
			if (td.getSeason() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("season", onto), waveInd, td.getSeason()); 
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
			manager.applyChange(addAxiomChange);
			}
			  
			if (td.getWeekDay() != null) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("weekDay", onto), waveInd, td.getWeekDay()); 
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom); 
			manager.applyChange(addAxiomChange);
			} 


		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}


	public String getWaveId() {
		return waveId;
	}

	public void setWaveId(String waveId) {
		this.waveId = waveId;
	}

	public OWLLiteral getWaveStartProcessingOn() {
		return waveStartProcessingOn;
	}

	public void setWaveStartProcessingOn(OWLLiteral waveStartProcessingOn) {
		this.waveStartProcessingOn = waveStartProcessingOn;
	}

	public OWLLiteral getWaveEndProcessingOn() {
		return waveEndProcessingOn;
	}

	public void setWaveEndProcessingOn(OWLLiteral waveEndProcessingOn) {
		this.waveEndProcessingOn = waveEndProcessingOn;
	}

	public OWLLiteral getQttTrailers() {
		return qttTrailers;
	}

	public void setQttTrailers(OWLLiteral qttTrailers) {
		this.qttTrailers = qttTrailers;
	}

	public OWLLiteral getQttBoxes() {
		return qttBoxes;
	}

	public void setQttBoxes(OWLLiteral qttBoxes) {
		this.qttBoxes = qttBoxes;
	}


	public OWLLiteral getQttBoxesProcessed() {
		return qttBoxesProcessed;
	}

	public void setQttBoxesProcessed(OWLLiteral qttBoxesProcessed) {
		this.qttBoxesProcessed = qttBoxesProcessed;
	}

	public OWLLiteral getQttPalletsBuilt() {
		return qttPalletsBuilt;
	}

	public void setQttPalletsBuilt(OWLLiteral qttPalletsBuilt) {
		this.qttPalletsBuilt = qttPalletsBuilt;
	}

	public OWLLiteral getQttTasks() {
		return qttTasks;
	}

	public void setQttTasks(OWLLiteral qttTasks) {
		this.qttTasks = qttTasks;
	}

	public OWLLiteral getQttShipments() {
		return qttShipments;
	}

	public void setQttShipments(OWLLiteral qttShipments) {
		this.qttShipments = qttShipments;
	}


	public OWLLiteral getReleasedOn() {
		return releasedOn;
	}


	public void setReleasedOn(OWLLiteral releasedOn) {
		this.releasedOn = releasedOn;
	}


	public String getHubAdditionalPartyIdentification() {
		return hubAdditionalPartyIdentification;
	}


	public void setHubAdditionalPartyIdentification(String hubAdditionalPartyIdentification) {
		this.hubAdditionalPartyIdentification = hubAdditionalPartyIdentification;
	}


	public OWLLiteral getClosedOn() {
		return closedOn;
	}


	public void setClosedOn(OWLLiteral closedOn) {
		this.closedOn = closedOn;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
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
	
	

}
