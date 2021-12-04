package ontology;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;


public class OntologyFormatConverter {
	
	public static void main(String[] args) throws FileNotFoundException {
	
	OntModel m = ModelFactory.createOntologyModel();
	
	m.read(new FileInputStream(new File("./files/ONTOLOGIES/M3Onto_TBox.owl")), "RDF/XML");
		
	m.write(new FileOutputStream(new File("./files/ONTOLOGIER/M3Onto_TBox.nt")), "N-TRIPLE");
	
	}
	

}
