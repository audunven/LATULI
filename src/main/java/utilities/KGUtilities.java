package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
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
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class KGUtilities {

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

	/**
	 * Retrieves the frequency of each entity type in the knowledge graph
	 * @param kg Path to the knowledge graph file
	 * @return A map where the entity type is key and its frequency is the value
	   29. jan. 2022
	 */
	public Map<String, Integer> getEntityStatistics (String kg) {

		Map<String, Integer> statisticsMap = new HashMap<String, Integer>();		

		return statisticsMap;

	}
	
	/**
	 * Retrieves the frequency of each relation in the knowledge graph
	 * @param kg Path to the knowledge graph file
	 * @return A map where the relation (object property) is key and its frequency is the value
	   29. jan. 2022
	 */
	public Map<String, Integer> getRelationStatistics (String kg) {

		Map<String, Integer> statisticsMap = new HashMap<String, Integer>();		

		return statisticsMap;

	}
	
	/**
	 * Counts the number of statements (triples) in a knowledge graph
	 * @param filePath The path to the knowledge graph file
	 * @param baseURI The baseURI used for the knowledge graph
	 * @return Number of statements in knowledge graph
	 * @throws IOException
	   29. jan. 2022
	 */
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

	

	/**
	 * Creates a type assignment in N-Triple format
	 * @param entity The entity being typed
	 * @param baseURI The baseURI of the ontology the type is defined in
	 * @param rdf_type The URI of rdf:type (<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>)
	 * @param type The OWL class used as the type of the entity
	 * @param tripleClosure Completes the N-Triple statement
	 * @return
	   29. jan. 2022
	 */
	public static String createType (String entity, String baseURI, String rdf_type, String type, String tripleClosure) {

		return baseURI + entity + rdf_type + baseURI + type + tripleClosure;	

	}

	/**
	 * 
	 * @param entity The entity being typed
	 * @param baseURI The baseURI of the ontology the type is defined in
	 * @param objectProperty The OWL object property used in the relation
	 * @param value The object in the subject-predicate-object triple
	 * @param typeSuffix
	 * @param tripleClosure
	 * @return
	   29. jan. 2022
	 */
	public static String createObjectProperty(String entity, String baseURI, String objectProperty, String value, String typeSuffix, String tripleClosure) {

		return baseURI + entity + " " + baseURI + objectProperty + "> " + " " + baseURI + value + typeSuffix + tripleClosure;
	}

	public static String createDataProperty(String entity, String baseURI, String dataProperty, String value, String dataType, String tripleClosure) {

		return baseURI + entity + " " + baseURI + dataProperty + "> " + " " + "\"" + value + "\"" + dataType + tripleClosure;
	}

	public static String createGeoDataProperty(String entity, String baseURI, String geoSparqlDataProperty, String value, String geoSparqlTripleClosure) {

		return baseURI + entity + " " + geoSparqlDataProperty + " " + "\"" + value + "\"" + geoSparqlTripleClosure;
	}




	public static void removeRepository(String rdf4jServer, String repositoryId) {

		RemoteRepositoryManager manager = new RemoteRepositoryManager(rdf4jServer);
		manager.init();
		System.out.println("Removing repository: " + rdf4jServer + ": " + repositoryId);
		manager.removeRepository(repositoryId);
		System.out.println("Repository: " + rdf4jServer + ": " + repositoryId + " is removed");
		manager.shutDown();

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


