package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class RDF4JUtilities {
	
	public static void main(String[] args) throws IOException {
		
		String dataDir = "/Users/audunvennesland/RDF4J_db_filteredByPeriod";
		String indexes = "spoc,posc,cosp";
		String baseURI = "http://latuli.no/onto#";
		String rdf4jServer = "http://78.91.106.145:8080/rdf4j-server";
		String repositoryId = "LATKGHUB";
		String outputPath = "./files/FILES/KG_Period.ttl";
		
		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));
		RepositoryConnection connection = repo.getConnection();
		printKGToTurtle(dataDir, indexes, connection, outputPath);
		
		//uploadTurtleFile(rdf4jServer, repositoryId, outputPath, baseURI);
		
		//int numTriples = countStatements(outputPath, baseURI);
		
		//System.out.println("The file contains " + numTriples + " statements");

	}
	
	public static int countStatements(String filePath, String baseURI) throws IOException {
		
		RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
		
		StatementCounter myCounter = new StatementCounter();
		rdfParser.setRDFHandler(myCounter);
		
		File initialFile = new File(filePath);
	    InputStream inputStream = new FileInputStream(initialFile);

		
		try {
		   rdfParser.parse(inputStream, baseURI);
		}
		catch (Exception e) {
		  // oh no!
		}
		finally {
		  inputStream.close();
		}
		int numberOfStatements = myCounter.getCountedStatements();
		
		return numberOfStatements;
	}
	
	
	public static void uploadTurtleFile (String rdf4jServer, String repositoryId, String filePath, String baseURI) {
		
		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);
		
		try {
			   RepositoryConnection con = repo.getConnection();
			   try {
			      con.add(new File(filePath), baseURI, RDFFormat.TURTLE);

			   }
			   finally {
			      con.close();
			   }
			}
			catch (RDF4JException e) {
			}
			catch (java.io.IOException e) {
			}
		
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
