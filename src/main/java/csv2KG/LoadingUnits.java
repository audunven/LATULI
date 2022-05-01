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


public class LoadingUnits {

	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";

	public static void processLoadingUnitsToNTriple (File loadingUnitsFolder, String tsvFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "LoadingUnit";
		String tripleClosure = "> .\n";

		String loadingUnitEntity;

		File[] filesInDir = loadingUnitsFolder.listFiles();

		List<String[]> line = new ArrayList<String[]>();


		BufferedReader br = null;
		BufferedWriter bw = null;


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

					loadingUnitEntity = params[0] + "_loadingUnit>";

					//rdf:type
					bw.write(KGUtilities.createType(loadingUnitEntity, baseURI, rdf_type, type, tripleClosure));
					
					//packageTypeId						
					bw.write(KGUtilities.createDataProperty(loadingUnitEntity, baseURI, "hasLoadingUnitId", params[0], DATATYPE_STRING, tripleClosure));

					//packageTypeId						
					bw.write(KGUtilities.createDataProperty(loadingUnitEntity, baseURI, "packageTypeId", params[1], DATATYPE_STRING, tripleClosure));

					//orderNumber						
					bw.write(KGUtilities.createDataProperty(loadingUnitEntity, baseURI, "orderNumber", params[2], DATATYPE_STRING, tripleClosure));

					//modifiedOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(loadingUnitEntity, baseURI, "modifiedOn", StringUtilities.convertToDateTime(params[3]), DATATYPE_DATETIME, tripleClosure));

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

	public static void processLoadingUnitsToTSV (File loadingUnitsFolder, String tsvFile) {


		String loadingUnitEntity;

		File[] filesInDir = loadingUnitsFolder.listFiles();

		List<String[]> line = new ArrayList<String[]>();


		BufferedReader br = null;
		BufferedWriter bw = null;


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

					loadingUnitEntity = params[0] + "_loadingUnit";

					bw.write(loadingUnitEntity + "\t" + "isType" + "\t" + "LoadingUnit" + "\n");

					//hasLoadingUnitId						
					bw.write(loadingUnitEntity + "\t" + "hasLoadingUnitId" + "\t" + params[0] + "\n");

					//hasPackageTypeId						
					bw.write(loadingUnitEntity + "\t" + "hasPackageTypeId" + "\t" + params[1] + "\n");


					//hasOrderNumber						
					bw.write(loadingUnitEntity + "\t" + "hasOrderNumber" + "\t" + params[2] + "\n");


					//isModifiedOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						bw.write(loadingUnitEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[3]) + "\n");

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

	public static void processLoadingUnitsToLocalRepo (File loadingUnitsFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI loadingUnitInd;
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			File[] filesInDir = loadingUnitsFolder.listFiles();
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
						loadingUnitInd = vf.createIRI(baseURI, params[0] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);

						//adding literals
						connection.add(loadingUnitInd, vf.createIRI(baseURI + "loadingUnitId"), vf.createLiteral(params[0]));
						
						connection.add(loadingUnitInd, vf.createIRI(baseURI + "packageTypeId"), vf.createLiteral(params[1]));

						connection.add(loadingUnitInd, vf.createIRI(baseURI + "orderNumber"), vf.createLiteral(params[2]));

						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							connection.add(loadingUnitInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));

						}
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

	public static void processLoadingUnitsToRemoteRepo (File loadingUnitsFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI loadingUnitInd;
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			File[] filesInDir = loadingUnitsFolder.listFiles();
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
						loadingUnitInd = vf.createIRI(baseURI, params[0] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);

						//adding literals
						connection.add(loadingUnitInd, vf.createIRI(baseURI + "loadingUnitId"), vf.createLiteral(params[0]));
						
						connection.add(loadingUnitInd, vf.createIRI(baseURI + "packageTypeId"), vf.createLiteral(params[1]));

						connection.add(loadingUnitInd, vf.createIRI(baseURI + "orderNumber"), vf.createLiteral(params[2]));

						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							connection.add(loadingUnitInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));

						}
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



