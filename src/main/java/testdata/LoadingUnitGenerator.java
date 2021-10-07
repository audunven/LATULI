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
public class LoadingUnitGenerator {
		
	String loadingUnitId;
	String orderNumber;
	OWLLiteral loadingUnitModifiedOn;
	String packageTypeId;


	public LoadingUnitGenerator() {}


	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		LoadingUnitGenerator data;

		BufferedReader br = new BufferedReader(new FileReader("./files/CSV/Last_10000/LoadingUnits_last_10000.csv"));

		String line = br.readLine();

		String[] params = null;

		Set<LoadingUnitGenerator> dataset = new HashSet<LoadingUnitGenerator>();
		
		//import manusquare ontology
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto.owl");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		

		while (line != null) {
			params = line.split(";");

			data = new LoadingUnitGenerator();
						
			data.setLoadingUnitId(params[0]);
			data.setPackageTypeId(params[1]);
			data.setOrderNumber(params[2]);
			data.setLoadingUnitModifiedOn(OntologyOperations.convertToDateTime(manager, params[3]));

			dataset.add(data);
			line = br.readLine();

		}

		br.close();



		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual loadingUnitInd = null;
		
		OWLAxiom classAssertionAxiom = null; 
		//OWLAxiom OPAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		int iterator = 0;

		//adding process chain
		for (LoadingUnitGenerator td : dataset) {
			iterator+=1;	

			//adding loading unit individual
			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getLoadingUnitId() + "_loadingUnit"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);


			//DP for expressing process chain name and id
			if (!td.getLoadingUnitModifiedOn().getLiteral().equals("0000-00-00T00:00:00")) {
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("loadingUnitModifiedOn", onto), loadingUnitInd, td.getLoadingUnitModifiedOn());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			}
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("packageTypeId", onto), loadingUnitInd, td.getPackageTypeId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);
			
			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("orderNumber", onto), loadingUnitInd, td.getOrderNumber());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);


		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}

	public String getLoadingUnitId() {
		return loadingUnitId;
	}


	public void setLoadingUnitId(String loadingUnitId) {
		this.loadingUnitId = loadingUnitId;
	}


	public String getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}


	public OWLLiteral getLoadingUnitModifiedOn() {
		return loadingUnitModifiedOn;
	}


	public void setLoadingUnitModifiedOn(OWLLiteral modifiedOn) {
		this.loadingUnitModifiedOn = modifiedOn;
	}


	public String getPackageTypeId() {
		return packageTypeId;
	}


	public void setPackageTypeId(String packageTypeId) {
		this.packageTypeId = packageTypeId;
	}


}
