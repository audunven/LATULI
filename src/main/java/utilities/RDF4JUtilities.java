package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.event.base.NotifyingRepositoryConnectionWrapper;
import org.eclipse.rdf4j.repository.event.base.RepositoryConnectionListenerAdapter;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class RDF4JUtilities {
	
	public static void main(String[] args) throws IOException {
		
		String dataDir = "/Users/audunvennesland/RDF4J_db_filteredByPeriod";
		String indexes = "spoc,posc,cosp";
		String baseURI = "https://w3id.org/latuli/ontology/m3#";
		String rdf4jServer = "http://78.91.106.145:8080/rdf4j-server";
		String repositoryId = "LATULIKGPERIOD";
		String outputPath = "./files/FILES/KG_Period.nt";
		
		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));
		//printKGToBinary(dataDir, indexes, connection, outputPath);
		printKGToNTriples(dataDir, indexes, repo, outputPath);
		
		//uploadTurtleFile(rdf4jServer, repositoryId, outputPath, baseURI);
				
		//System.out.println("The file contains " + countStatements(outputPath, baseURI) + " statements");
		
		//removeRepository(rdf4jServer, repositoryId);

	}
	
	//https://stackoverflow.com/questions/47789037/why-is-adding-an-rdf-dump-inputstream-to-a-rdf4j-repository-so-slow-in-java
	public void uploadFile(String context, RDFFormat format, String filePath) throws MalformedURLException, IOException {
	    Repository repo = new SailRepository(new NativeStore());
	    repo.initialize();
	    IRI contextIRI = repo.getValueFactory().createIRI(context);
	    //RDFFormat format = RDFFormat.NTRIPLES;
	    System.out.println("Load zip file of format " + format);
	    try (InputStream in = new FileInputStream(new File(filePath));
	                    NotifyingRepositoryConnectionWrapper con = new NotifyingRepositoryConnectionWrapper(repo,
	                                    repo.getConnection());) {
	        RepositoryConnectionListenerAdapter myListener = new RepositoryConnectionListenerAdapter() {
	            private long count = 0;
	            @Override
	            public void add(RepositoryConnection arg0, Resource arg1, IRI arg2, Value arg3, Resource... arg4) {
	                count++;
	                if (count % 100000 == 0)
	                    System.out.println("Add statement number " + count + "\n" + arg1 + " " + arg2 + " " + arg3);
	            }
	        };
	        con.addRepositoryConnectionListener(myListener);
	        con.add(in, "", format,contextIRI);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	

	public static void removeRepository(String rdf4jServer, String repositoryId) {
		
		RemoteRepositoryManager manager = new RemoteRepositoryManager(rdf4jServer);
		manager.init();
		System.out.println("Removing repository: " + rdf4jServer + ": " + repositoryId);
		manager.removeRepository(repositoryId);
		System.out.println("Repository: " + rdf4jServer + ": " + repositoryId + " is removed");
		manager.shutDown();
		
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
		}
		finally {
		  inputStream.close();
		}
		int numberOfStatements = myCounter.getCountedStatements();
		
		return numberOfStatements;
	}
	
	
	public static void uploadTurtleFileToWorkbench (String rdf4jServer, String repositoryId, String filePath, String baseURI) {
		
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
	
	public static void printKGToBinary(String dataDir, String indexes, Repository repo, String outputPath) throws FileNotFoundException {
		RepositoryConnection connection = repo.getConnection();
		FileOutputStream outputStream = new FileOutputStream(new File(outputPath));
		RDFWriter writer = Rio.createWriter(RDFFormat.BINARY, outputStream);
		connection.exportStatements(null, null, null, false, writer);
		IOUtils.closeQuietly(outputStream);
		connection.close();
		
	}
	
	public static void printKGToTurtle(String dataDir, String indexes, Repository repo, String outputPath) throws FileNotFoundException {
		RepositoryConnection connection = repo.getConnection();
		FileOutputStream outputStream = new FileOutputStream(new File(outputPath));
		RDFWriter writer = Rio.createWriter(RDFFormat.TURTLE, outputStream);
		connection.exportStatements(null, null, null, false, writer);
		IOUtils.closeQuietly(outputStream);
		connection.close();
		
	}
	
	public static void printKGToNQuads(String dataDir, String indexes, Repository repo, String outputPath) throws FileNotFoundException {
		RepositoryConnection connection = repo.getConnection();
		FileOutputStream outputStream = new FileOutputStream(new File(outputPath));
		RDFWriter writer = Rio.createWriter(RDFFormat.NQUADS, outputStream);
		connection.exportStatements(null, null, null, false, writer);
		IOUtils.closeQuietly(outputStream);
		connection.close();
		
	}
	
	public static void printKGToNTriples(String dataDir, String indexes, Repository repo, String outputPath) throws FileNotFoundException {
		RepositoryConnection connection = repo.getConnection();
		FileOutputStream outputStream = new FileOutputStream(new File(outputPath));
		RDFWriter writer = Rio.createWriter(RDFFormat.NTRIPLES, outputStream);
		connection.exportStatements(null, null, null, false, writer);
		IOUtils.closeQuietly(outputStream);
		connection.close();
		
	}

}
