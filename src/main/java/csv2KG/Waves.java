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
public class Waves {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	public static void processWavesToNTriple (File wavesFolder, String ntFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "Wave";
		String tripleClosure = "> .\n";

		String waveEntity;

		File[] filesInDir = wavesFolder.listFiles();

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


					//isType					
					waveEntity = params[0] + "_wave>";

					bw.write(KGUtilities.createType(waveEntity, baseURI, rdf_type, type, tripleClosure));


					//hasHubParty				
					bw.write(KGUtilities.createObjectProperty(waveEntity, baseURI, "hasHubParty", params[6], "_party", tripleClosure));


					//waveid
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "waveId", params[0], DATATYPE_STRING, tripleClosure));


					//plannedOn
					if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "plannedOn", StringUtilities.convertToDateTime(params[1]), DATATYPE_DATETIME, tripleClosure));

					}

					//releasedOn
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "releasedOn", StringUtilities.convertToDateTime(params[2]), DATATYPE_DATETIME, tripleClosure));


					}

					//modifiedOn
					if (!StringUtilities.convertToDateTime(params[7]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "modifiedOn", StringUtilities.convertToDateTime(params[7]), DATATYPE_DATETIME, tripleClosure));


					}

					//closedOn
					if (!StringUtilities.convertToDateTime(params[8]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "closedOn", StringUtilities.convertToDateTime(params[8]), DATATYPE_DATETIME, tripleClosure));


					}

					//createdOn
					if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "createdOn", StringUtilities.convertToDateTime(params[10]), DATATYPE_DATETIME, tripleClosure));

					}

					//waveStartProcessingOn
					if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "waveStartProcessingOn", StringUtilities.convertToDateTime(params[13]), DATATYPE_DATETIME, tripleClosure));


					}

					//waveEndProcessingOn
					if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "waveEndProcessingOn", StringUtilities.convertToDateTime(params[14]), DATATYPE_DATETIME, tripleClosure));


					}

					//qttTrailers
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttTrailers", params[15], DATATYPE_INT, tripleClosure));


					//qttBoxes
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttBoxes", params[16], DATATYPE_INT, tripleClosure));


					//qttPallets
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttPallets", params[17], DATATYPE_INT, tripleClosure));


					//qttBoxesProcessed
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttBoxesProcessed", params[18], DATATYPE_INT, tripleClosure));


					//qttPalletsBuilt
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttPalletsBuilt", params[19], DATATYPE_INT, tripleClosure));


					//qttTasks
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttTasks", params[20], DATATYPE_INT, tripleClosure));


					//qttShipments
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttShipments", params[21], DATATYPE_INT, tripleClosure));



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
	
	public static void processWavesToTSV (File wavesFolder, String tsvFile) {


		String waveEntity;

		File[] filesInDir = wavesFolder.listFiles();

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


					//isType					
					waveEntity = params[0] + "_wave";
					bw.write(waveEntity + "\t" + "isType" + "\t" + "Wave" + "\n");



					//hasHubParty				
					bw.write(waveEntity + "\t" + "hasHubParty" + "\t" + params[6] + "_party" + "\n");


					//hasWaveid
					bw.write(waveEntity + "\t" + "hasWaveid" + "\t" + params[0] + "\n");

					//isPlannedOn
					if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isPlannedOn" + "\t" + StringUtilities.convertToDateTime(params[1]) + "\n");

					}

					//isReleasedOn
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isReleasedOn" + "\t" + StringUtilities.convertToDateTime(params[2]) + "\n");

					}

					//isModifiedOn
					if (!StringUtilities.convertToDateTime(params[7]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[7]) + "\n");

					}

					//isClosedOn
					if (!StringUtilities.convertToDateTime(params[8]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isClosedOn" + "\t" + StringUtilities.convertToDateTime(params[8]) + "\n");

					}

					//isCreatedOn
					if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isCreatedOn" + "\t" + StringUtilities.convertToDateTime(params[10]) + "\n");

					}

					//hasWaveStartProcessingOn
					if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "hasWaveStartProcessingOn" + "\t" + StringUtilities.convertToDateTime(params[13]) + "\n");

					}

					//hasWaveEndProcessingOn
					if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "hasWaveEndProcessingOn" + "\t" + StringUtilities.convertToDateTime(params[14]) + "\n");

					}

					//hasQttTrailers
					bw.write(waveEntity + "\t" + "hasQttTrailers" + "\t" + params[15] + "\n");


					//hasQttBoxes
					bw.write(waveEntity + "\t" + "hasQttBoxes" + "\t" + params[16] + "\n");

					//hasQttPallets
					bw.write(waveEntity + "\t" + "hasQttPallets" + "\t" + params[17] + "\n");

					//hasQttBoxesProcessed
					bw.write(waveEntity + "\t" + "hasQttBoxesProcessed" + "\t" + params[18] + "\n");

					//hasQttPalletsBuilt
					bw.write(waveEntity + "\t" + "hasQttPalletsBuilt" + "\t" + params[19] + "\n");

					//hasQttTasks
					bw.write(waveEntity + "\t" + "hasQttTasks" + "\t" + params[20] + "\n");

					//hasQttShipments
					bw.write(waveEntity + "\t" + "hasQttShipments" + "\t" + params[21] + "\n");


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

	public static void processWavesToLocalRepo (File wavesFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI waveInd;
			IRI partyInd;

			IRI waveClass = vf.createIRI(baseURI, "Wave");
			IRI terminalOperatorClass = vf.createIRI(baseURI, "TerminalOperator");

			File[] filesInDir = wavesFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					//System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

						//adding types
						waveInd = vf.createIRI(baseURI, params[0] + "_wave");
						connection.add(waveInd, RDF.TYPE, waveClass);

						//adding predicates
						partyInd = vf.createIRI(baseURI, params[6] + "_party");
						connection.add(partyInd, RDF.TYPE, terminalOperatorClass);
						connection.add(waveInd, vf.createIRI(baseURI + "hasHubParty"), partyInd);

						//adding literals
						connection.add(waveInd, vf.createIRI(baseURI + "waveId"), vf.createLiteral(params[0]));
						
						if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "plannedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[1]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "releasedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[2]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[7]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[7]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[8]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "closedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[8]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "createdOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[10]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "waveStartProcessingOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[13]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "waveEndProcessingOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[14]), XMLSchema.DATETIME));								
						}

						connection.add(waveInd, vf.createIRI(baseURI + "qttTrailers"), vf.createLiteral(params[15], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttBoxes"), vf.createLiteral(params[16], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttPallets"), vf.createLiteral(params[17], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttBoxesProcessed"), vf.createLiteral(params[18], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttPalletsBuilt"), vf.createLiteral(params[19], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttTasks"), vf.createLiteral(params[20], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttShipments"), vf.createLiteral(params[21], XMLSchema.INT));


					}

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
	
	public static void processWavesToRemoteRepo (File wavesFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {
			
			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI waveInd;
			IRI partyInd;

			IRI waveClass = vf.createIRI(baseURI, "Wave");
			IRI terminalOperatorClass = vf.createIRI(baseURI, "TerminalOperator");

			File[] filesInDir = wavesFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					//System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

						//adding types
						waveInd = vf.createIRI(baseURI, params[0] + "_wave");
						connection.add(waveInd, RDF.TYPE, waveClass);

						//adding predicates
						partyInd = vf.createIRI(baseURI, params[6] + "_party");
						connection.add(partyInd, RDF.TYPE, terminalOperatorClass);
						connection.add(waveInd, vf.createIRI(baseURI + "hasHubParty"), partyInd);

						//adding literals
						connection.add(waveInd, vf.createIRI(baseURI + "waveId"), vf.createLiteral(params[0]));
						
						if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "plannedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[1]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "releasedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[2]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[7]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[7]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[8]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "closedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[8]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "createdOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[10]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "waveStartProcessingOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[13]), XMLSchema.DATETIME));								
						}
						
						if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
							connection.add(waveInd, vf.createIRI(baseURI + "waveEndProcessingOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[14]), XMLSchema.DATETIME));								
						}

						connection.add(waveInd, vf.createIRI(baseURI + "qttTrailers"), vf.createLiteral(params[15], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttBoxes"), vf.createLiteral(params[16], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttPallets"), vf.createLiteral(params[17], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttBoxesProcessed"), vf.createLiteral(params[18], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttPalletsBuilt"), vf.createLiteral(params[19], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttTasks"), vf.createLiteral(params[20], XMLSchema.INT));
						connection.add(waveInd, vf.createIRI(baseURI + "qttShipments"), vf.createLiteral(params[21], XMLSchema.INT));


					}

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
