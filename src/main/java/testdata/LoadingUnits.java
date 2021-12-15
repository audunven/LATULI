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
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import owlprocessing.OntologyOperations;


public class LoadingUnits {

	private String loadingUnitId;
	private String packageTypeId;
	private String originalDataSource;

	public String getLoadingUnitId() {
		return loadingUnitId;
	}

	public String getPackageTypeId() {
		return packageTypeId;
	}

	public String getOriginalDataSource() {
		return originalDataSource;
	}

	private LoadingUnits(Builder builder) {

		this.loadingUnitId = builder.loadingUnitId;
		this.packageTypeId = builder.packageTypeId;
		this.originalDataSource = builder.originalDataSource;
	}

	public static class Builder {

		private  String loadingUnitId;
		private String packageTypeId;
		private String originalDataSource;

		public Builder(String loadingUnitId, String packageTypeId, String originalDataSource) {
			this.loadingUnitId = loadingUnitId;
			this.packageTypeId = packageTypeId;
			this.originalDataSource = originalDataSource;
		}

		public LoadingUnits build() {
			return new LoadingUnits(this);
		}
	}



	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {


		LoadingUnits data;

		String[] params = null;
		Set<LoadingUnits> dataset = new HashSet<LoadingUnits>();
		
		String CSV_folder = "./files/CSV/Truls/LoadingUnits_split/";
		
		File folder = new File(CSV_folder);
		File[] filesInDir = folder.listFiles();
		
		BufferedReader br;

		for (int i = 0; i < filesInDir.length; i++) {
			
			System.out.println("Reading file: " + filesInDir[i].getName());
			
			br = new BufferedReader(new FileReader(filesInDir[i]));

			String line = br.readLine();

			while (line != null) {
				params = line.split(",");

				data = new LoadingUnits.Builder(params[1], params[2], params[3])
						.build();

				dataset.add(data);
				line = br.readLine();			
			}

			br.close();		
			
			System.out.println("Completed processing file: " + filesInDir[i].getName());

		}
		
		System.out.println("Completed processing all CSV files in folder " + CSV_folder);


		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File ontoFile = new File("./files/ONTOLOGIES/M3Onto_TBox.owl");

		//point to a local folder containing local copies of ontologies to sort out the imports
		AutoIRIMapper mapper=new AutoIRIMapper(new File("./files/ONTOLOGIES"), true);
		manager.addIRIMapper(mapper);

		OWLOntology onto = manager.loadOntologyFromOntologyDocument(ontoFile);
		System.out.println("The ontology contains " + onto.getClassesInSignature().size() + " classes");

		OWLClass loadingUnitClass = OntologyOperations.getClass("LoadingUnit", onto);

		OWLDataFactory df = manager.getOWLDataFactory();

		OWLIndividual loadingUnitInd = null;

		OWLAxiom classAssertionAxiom = null; 
		OWLAxiom DPAssertionAxiom = null; 

		AddAxiom addAxiomChange = null;

		for (LoadingUnits td : dataset) {

			//adding loading unit individual
			loadingUnitInd = df.getOWLNamedIndividual(IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + td.getLoadingUnitId() + "_loadingunit"));
			classAssertionAxiom = df.getOWLClassAssertionAxiom(loadingUnitClass, loadingUnitInd);			
			addAxiomChange = new AddAxiom(onto, classAssertionAxiom);		
			manager.applyChange(addAxiomChange);


			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("packageTypeId", onto), loadingUnitInd, td.getPackageTypeId());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);

			DPAssertionAxiom = df.getOWLDataPropertyAssertionAxiom(OntologyOperations.getDataProperty("originalDataSource", onto), loadingUnitInd, td.getOriginalDataSource());
			addAxiomChange = new AddAxiom(onto, DPAssertionAxiom);
			manager.applyChange(addAxiomChange);


		}
		//save the ontology in each iteration
		manager.saveOntology(onto);
	}


}



