package csv2KG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import utilities.KGUtilities;
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class Transports {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	public static void processTransportsToNTriple (File transportsFolder, String ntFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "Transport";
		String tripleClosure = "> .\n";

		String transportEntity;

		File[] filesInDir = transportsFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;


		List<String[]> line = new ArrayList<String[]>();

		for (int i = 0; i < filesInDir.length; i++) {

			try {

				br = new BufferedReader(new FileReader(filesInDir[i]));
				bw = new BufferedWriter(new FileWriter(ntFile, true));


				//System.out.println("Reading file: " + filesInDir[i].getName());

				try {
					line = StringUtilities.oneByOne(br);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (String[] params : line) {

					transportEntity = params[0] + "_transport>";

					//adding types						
					bw.write(KGUtilities.createType(transportEntity, baseURI, rdf_type, type, tripleClosure));

					//hasHubParty
					bw.write(KGUtilities.createObjectProperty(transportEntity, baseURI, "hasHubParty", params[8], "_party", tripleClosure));

					//transportId
					bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "transportId", params[0], DATATYPE_STRING, tripleClosure));

					//expectedArrival
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "expectedArrival", StringUtilities.convertToDateTime(params[2]), DATATYPE_DATETIME, tripleClosure));

					}

					//effectiveArrival
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "effectiveArrival", StringUtilities.convertToDateTime(params[3]), DATATYPE_DATETIME, tripleClosure));

					}

					//transportName
					bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "transportName", params[9], DATATYPE_STRING, tripleClosure));


					//modifiedOn
					if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "modifiedOn", StringUtilities.convertToDateTime(params[10]), DATATYPE_DATETIME, tripleClosure));

					}

					//transportType
					bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "transportType", params[11], DATATYPE_STRING, tripleClosure));


					//wavePlannedOn
					if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "wavePlannedOn", StringUtilities.convertToDateTime(params[12]), DATATYPE_DATETIME, tripleClosure));

					}

					//waveReleasedOn
					if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "waveReleasedOn", StringUtilities.convertToDateTime(params[13]), DATATYPE_DATETIME, tripleClosure));

					}

					//waveClosedOn
					if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "waveClosedOn", StringUtilities.convertToDateTime(params[14]), DATATYPE_DATETIME, tripleClosure));

					}

					//waveStartProcessingOn
					if (!StringUtilities.convertToDateTime(params[15]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "waveStartProcessingOn", StringUtilities.convertToDateTime(params[15]), DATATYPE_DATETIME, tripleClosure));

					}

					//waveEndProcessingOn
					if (!StringUtilities.convertToDateTime(params[16]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(transportEntity, baseURI, "waveEndProcessingOn", StringUtilities.convertToDateTime(params[16]), DATATYPE_DATETIME, tripleClosure));

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

				try {
					if (bw != null)
						bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}
	}
	
	public static void processTransportsToTSV (File transportsFolder, String tsvFile) {


		String transportEntity;

		File[] filesInDir = transportsFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;


		List<String[]> line = new ArrayList<String[]>();

		for (int i = 0; i < filesInDir.length; i++) {

			try {

				br = new BufferedReader(new FileReader(filesInDir[i]));
				bw = new BufferedWriter(new FileWriter(tsvFile, true));


				//System.out.println("Reading file: " + filesInDir[i].getName());

				try {
					line = StringUtilities.oneByOne(br);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (String[] params : line) {

					transportEntity = params[0] + "_transport";

					//adding types						
					bw.write(transportEntity + "\t" + "isType" + "\t" + "Transport" + "\n");


					//hasHubParty
					bw.write(transportEntity + "\t" + "hasHubParty" + "\t" + params[8] + "_party" + "\n");


					//hasTransportId
					bw.write(transportEntity + "\t" + "hasTransportId" + "\t" + params[0] + "\n");

					//hasExpectedArrival
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasExpectedArrival" + "\t" + StringUtilities.convertToDateTime(params[2]) + "\n");
					}

					//hasEffectiveArrival
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasEffectiveArrival" + "\t" + StringUtilities.convertToDateTime(params[3]) + "\n");
					}

					//hasTransportName
					bw.write(transportEntity + "\t" + "hasTransportName" + "\t" + params[9] + "\n");

					//isModifiedOn
					if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[10]) + "\n");

					}

					//isTransportType
					bw.write(transportEntity + "\t" + "isTransportType" + "\t" + params[11] + "\n");


					//hasWavePlannedOn
					if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWavePlannedOn" + "\t" + StringUtilities.convertToDateTime(params[12]) + "\n");

					}

					//hasWaveReleasedOn
					if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWaveReleasedOn" + "\t" + StringUtilities.convertToDateTime(params[13]) + "\n");

					}

					//hasWaveClosedOn
					if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWaveClosedOn" + "\t" + StringUtilities.convertToDateTime(params[14]) + "\n");

					}

					//hasWaveStartProcessingOn
					if (!StringUtilities.convertToDateTime(params[15]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWaveStartProcessingOn" + "\t" + StringUtilities.convertToDateTime(params[15]) + "\n");

					}

					//hasWaveEndProcessingOn
					if (!StringUtilities.convertToDateTime(params[16]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWaveEndProcessingOn" + "\t" + StringUtilities.convertToDateTime(params[16]) + "\n");

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

				try {
					if (bw != null)
						bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}
	}
		
	public static void processTransportsToLocalRepo (File transportsFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

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

					//System.out.println("Reading file: " + filesInDir[i].getName());

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
	
	public static void processTransportsToRemoteRepo (File transportsFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

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

					//System.out.println("Reading file: " + filesInDir[i].getName());

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
