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
	OWLLiteral waveStartProcessingOn;
	OWLLiteral waveEndProcessingOn;
	OWLLiteral qttTrailers;
	OWLLiteral qttBoxesInWave;
	OWLLiteral qttPalletsInWave;
	OWLLiteral qttBoxesProcessed;
	OWLLiteral qttPalletsBuilt;
	OWLLiteral qttTasks;
	OWLLiteral qttShipments;


	public WaveGenerator(String waveId, OWLLiteral waveStartProcessingOn, OWLLiteral waveEndProcessingOn,
			OWLLiteral qttTrailers, OWLLiteral qttBoxesInWave, OWLLiteral qttPalletsInWave, OWLLiteral qttBoxesProcessed,
			OWLLiteral qttPalletsBuilt, OWLLiteral qttTasks, OWLLiteral qttShipments) {
		this.waveId = waveId;
		this.waveStartProcessingOn = waveStartProcessingOn;
		this.waveEndProcessingOn = waveEndProcessingOn;
		this.qttTrailers = qttTrailers;
		this.qttBoxesInWave = qttBoxesInWave;
		this.qttPalletsInWave = qttPalletsInWave;
		this.qttBoxesProcessed = qttBoxesProcessed;
		this.qttPalletsBuilt = qttPalletsBuilt;
		this.qttTasks = qttTasks;
		this.qttShipments = qttShipments;
	}

	public WaveGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		WaveGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/Waves_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<WaveGenerator> dataset = new HashSet<WaveGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new WaveGenerator();
						
			data.setWaveId(params[0]);
			data.setWaveStartProcessingOn(OntologyOperations.convertToDateTime(manager, params[13]));
			data.setWaveEndProcessingOn(OntologyOperations.convertToDateTime(manager, params[14]));
			data.setQttTrailers(OntologyOperations.convertToInt(manager, params[15]));
			data.setQttBoxesInWave(OntologyOperations.convertToInt(manager, params[16]));
			data.setQttPalletsInWave(OntologyOperations.convertToInt(manager, params[17]));
			data.setQttBoxesProcessed(OntologyOperations.convertToInt(manager, params[18]));			
			data.setQttPalletsBuilt(OntologyOperations.convertToInt(manager, params[19]));
			data.setQttTasks(OntologyOperations.convertToInt(manager, params[20]));
			data.setQttShipments(OntologyOperations.convertToInt(manager, params[21]));

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

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual waveInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		//OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		//adding process chain
		for (WaveGenerator td : dataset) {
			iterator+=1;	

			//adding process chain individual
			waveInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getWaveId() + "_wave"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(waveClass, waveInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);


			//DP for expressing process chain name and id
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
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttBoxesInWave", onto), waveInd, td.getQttBoxesInWave());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("qttPalletsInWave", onto), waveInd, td.getQttPalletsInWave());
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

	public OWLLiteral getQttBoxesInWave() {
		return qttBoxesInWave;
	}

	public void setQttBoxesInWave(OWLLiteral qttBoxes) {
		this.qttBoxesInWave = qttBoxes;
	}

	public OWLLiteral getQttPalletsInWave() {
		return qttPalletsInWave;
	}

	public void setQttPalletsInWave(OWLLiteral qttPallets) {
		this.qttPalletsInWave = qttPallets;
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

}
