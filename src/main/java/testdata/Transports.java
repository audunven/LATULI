package testdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class Transports {
		
	public static void processTransports (File transportsFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("lat", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI transportInd;
			IRI hubInd;

			IRI transportClass = vf.createIRI(baseURI, "Transport");
			IRI terminalOperatorClass = vf.createIRI(baseURI, "TerminalOperator");

			File[] filesInDir = transportsFolder.listFiles();

			BufferedReader br = null;

			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					try {
						line = StringUtilities.oneByOne(br);
					} catch (Exception e) {
						e.printStackTrace();
					}

					for (String[] params : line) {

						//adding types
						transportInd = vf.createIRI(baseURI, params[0] + "_transport" );
						connection.add(transportInd, RDF.TYPE, transportClass);

						//adding predicates
						hubInd = vf.createIRI(baseURI, params[8] + "_party");
						connection.add(hubInd, RDF.TYPE, terminalOperatorClass);
						connection.add(transportInd, vf.createIRI(baseURI + "hasHubParty"), hubInd);
						
						//adding literals
						
						connection.add(transportInd, vf.createIRI(baseURI + "transportId"), vf.createLiteral(params[0]));
						
						if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "expectedArrival"), vf.createLiteral(StringUtilities.convertToDateTime(params[2]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "effectiveArrival"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));
						}
											
						connection.add(transportInd, vf.createIRI(baseURI + "transportName"), vf.createLiteral(params[9]));
						
						if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[10]), XMLSchema.DATETIME));
						}
						
						connection.add(transportInd, vf.createIRI(baseURI + "transportType"), vf.createLiteral(params[11]));
						
						if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "wavePlannedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[12]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "waveReleasedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[13]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "waveClosedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[14]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[15]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "waveStartProcessingOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[15]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[16]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "waveEndProcessingOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[16]), XMLSchema.DATETIME));
						}

					}//end for

				} catch (IOException e) {

					e.printStackTrace();

				} finally {

					try {
						if (br != null)
							br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}


			}

		}
		repo.shutDown();

		long endTime = System.nanoTime();		
		long timeElapsed = endTime - startTime;		
		System.err.println("The ontology generation process took: " + timeElapsed/60000000000.00 + " minutes");

		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");

		System.out.println("\nUsed Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB"); 


	}
	
	public static void processTransportsHTTP (File transportsFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("lat", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI transportInd;
			IRI hubInd;

			IRI transportClass = vf.createIRI(baseURI, "Transport");
			IRI terminalOperatorClass = vf.createIRI(baseURI, "TerminalOperator");

			File[] filesInDir = transportsFolder.listFiles();

			BufferedReader br = null;

			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					try {
						line = StringUtilities.oneByOne(br);
					} catch (Exception e) {
						e.printStackTrace();
					}

					for (String[] params : line) {

						//adding types
						transportInd = vf.createIRI(baseURI, params[0] + "_transport" );
						connection.add(transportInd, RDF.TYPE, transportClass);

						//adding predicates
						hubInd = vf.createIRI(baseURI, params[8] + "_party");
						connection.add(hubInd, RDF.TYPE, terminalOperatorClass);
						connection.add(transportInd, vf.createIRI(baseURI + "hasHubParty"), hubInd);
						
						//adding literals
						connection.add(transportInd, vf.createIRI(baseURI + "transportId"), vf.createLiteral(params[0]));
						
						if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "expectedArrival"), vf.createLiteral(StringUtilities.convertToDateTime(params[2]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "effectiveArrival"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));
						}
											
						connection.add(transportInd, vf.createIRI(baseURI + "transportName"), vf.createLiteral(params[9]));
						
						if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[10]), XMLSchema.DATETIME));
						}
						
						connection.add(transportInd, vf.createIRI(baseURI + "transportType"), vf.createLiteral(params[11]));
						
						if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "wavePlannedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[12]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "waveReleasedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[13]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "waveClosedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[14]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[15]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "waveStartProcessingOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[15]), XMLSchema.DATETIME));
						}
						
						if (!StringUtilities.convertToDateTime(params[16]).equals("0000-00-00T00:00:00")) {
							connection.add(transportInd, vf.createIRI(baseURI + "waveEndProcessingOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[16]), XMLSchema.DATETIME));
						}

					}//end for

				} catch (IOException e) {

					e.printStackTrace();

				} finally {

					try {
						if (br != null)
							br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}


			}

		}
		repo.shutDown();

		long endTime = System.nanoTime();		
		long timeElapsed = endTime - startTime;		
		System.err.println("The ontology generation process took: " + timeElapsed/60000000000.00 + " minutes");

		long usedMemoryAfterOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Memory increased after ontology creation: " + (usedMemoryAfterOntologyCreation-usedMemoryBeforeOntologyCreation)/1000000 + " MB");

		System.out.println("\nUsed Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB"); 


	}

}
