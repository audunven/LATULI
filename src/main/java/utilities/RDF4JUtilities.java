package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class RDF4JUtilities {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		String dataDir = "/Users/audunvennesland/RDF4j_db_test";
		String indexes = "spoc,posc,cosp";
		String outputPath = "./files/FILES/export.ttl";
		
		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));
		RepositoryConnection connection = repo.getConnection();

		FileOutputStream outputStream = new FileOutputStream(new File("./files/FILES/export.ttl"));
		RDFWriter writer = Rio.createWriter(RDFFormat.TURTLE, outputStream);
		connection.exportStatements(null, null, null, false, writer);
		IOUtils.closeQuietly(outputStream);
	}
	
	public static void printKGToTurtle(String dataDir, String indexes, RepositoryConnection connection, String outputPath) throws FileNotFoundException {
		
		FileOutputStream outputStream = new FileOutputStream(new File(outputPath));
		RDFWriter writer = Rio.createWriter(RDFFormat.TURTLE, outputStream);
		connection.exportStatements(null, null, null, false, writer);
		IOUtils.closeQuietly(outputStream);
		
	}
	
	public static void printKGToNQuads(String dataDir, String indexes, RepositoryConnection connection, String outputPath) throws FileNotFoundException {
		
		FileOutputStream outputStream = new FileOutputStream(new File(outputPath));
		RDFWriter writer = Rio.createWriter(RDFFormat.NQUADS, outputStream);
		connection.exportStatements(null, null, null, false, writer);
		IOUtils.closeQuietly(outputStream);
		
	}

}
