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
public class TradeItems {

	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";

	public static void processTradeItemsToNTriple (File tradeItemsFolder, String ntFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "TradeItem";
		String tripleClosure = "> .\n";

		String tradeItemEntity;

		File[] filesInDir = tradeItemsFolder.listFiles();

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
					tradeItemEntity = params[0] + "-" + params[1] + "_tradeItem>";
					bw.write(KGUtilities.createType(tradeItemEntity, baseURI, rdf_type, type, tripleClosure));

					//belongsToShipment
					bw.write(KGUtilities.createObjectProperty(tradeItemEntity, baseURI, "belongsToShipment", params[0], "_shipment", tripleClosure));


					//hasLoadingUnit
					bw.write(KGUtilities.createObjectProperty(tradeItemEntity, baseURI, "hasLoadingUnit", params[1], "_loadingUnit", tripleClosure));


					//gtin
					bw.write(KGUtilities.createDataProperty(tradeItemEntity, baseURI, "gtin", params[2], DATATYPE_STRING, tripleClosure));


					//supplierQuantity
					bw.write(KGUtilities.createDataProperty(tradeItemEntity, baseURI, "supplierQuantity", params[6], DATATYPE_DECIMAL, tripleClosure));


					//customerQuantity
					bw.write(KGUtilities.createDataProperty(tradeItemEntity, baseURI, "customerQuantity", params[7], DATATYPE_DECIMAL, tripleClosure));


					//supplierProductDescription
					bw.write(KGUtilities.createDataProperty(tradeItemEntity, baseURI, "supplierProductDescription", params[8], DATATYPE_STRING, tripleClosure));


					//supplierProductId
					bw.write(KGUtilities.createDataProperty(tradeItemEntity, baseURI, "supplierProductId", params[10], DATATYPE_STRING, tripleClosure));


					//modifiedOn
					if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
						bw.write(KGUtilities.createDataProperty(tradeItemEntity, baseURI, "modifiedOn", StringUtilities.convertToDateTime(params[12]), DATATYPE_DATETIME, tripleClosure));

					}

					//handlingInstructions
					bw.write(KGUtilities.createDataProperty(tradeItemEntity, baseURI, "handlingInstructions", params[16], DATATYPE_STRING, tripleClosure));


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

	public static void processTradeItemsToTSV (File tradeItemsFolder, String tsvFile) {


		String tradeItemEntity;

		File[] filesInDir = tradeItemsFolder.listFiles();

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
					tradeItemEntity = params[0] + "-" + params[1] + "_tradeItem";

					bw.write(tradeItemEntity + "\t" + "isType" + "\t" + "TradeItem" + "\n");

					//belongsToShipment
					bw.write(tradeItemEntity + "\t" + "belongsToShipment" + "\t" + params[0] + "_shipment" + "\n");

					//hasLoadingUnit
					bw.write(tradeItemEntity + "\t" + "hasLoadingUnit" + "\t" + params[1] + "_loadingUnit" + "\n");

					//hasGtin
					bw.write(tradeItemEntity + "\t" + "hasGtin" + "\t" + params[2] + "\n");

					//hasSupplierQuantity
					bw.write(tradeItemEntity + "\t" + "hasSupplierQuantity" + "\t" + params[6] + "\n");

					//hasCustomerQuantity
					bw.write(tradeItemEntity + "\t" + "hasCustomerQuantity" + "\t" + params[7] + "\n");

					//hasSupplierProductDescription
					bw.write(tradeItemEntity + "\t" + "hasSupplierProductDescription" + "\t" + params[8] + "\n");

					//hasSupplierProductId
					bw.write(tradeItemEntity + "\t" + "hasSupplierProductId" + "\t" + params[10] + "\n");


					//isModifiedOn
					if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
						bw.write(tradeItemEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[12]) + "\n");
					}

					//hasHandlingInstructions
					bw.write(tradeItemEntity + "\t" + "hasHandlingInstructions" + "\t" + params[16] + "\n");


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

	public static void processTradeItemsToLocalRepo (File tradeItemsFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI tradeItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;


			IRI tradeItemClass = vf.createIRI(baseURI, "TradeItem");
			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			File[] filesInDir = tradeItemsFolder.listFiles();

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
						tradeItemInd = vf.createIRI(baseURI, params[0] + "-" + params[1] + "_tradeItem" );
						connection.add(tradeItemInd, RDF.TYPE, tradeItemClass);

						//adding predicates
						shipmentInd = vf.createIRI(baseURI, params[0] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(tradeItemInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);

						loadingUnitInd = vf.createIRI(baseURI, params[1] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(tradeItemInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);								

						//adding literals
						connection.add(tradeItemInd, vf.createIRI(baseURI + "gtin"), vf.createLiteral(params[2]));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierQuantity"), vf.createLiteral(params[6], XMLSchema.DECIMAL));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "customerQuantity"), vf.createLiteral(params[7], XMLSchema.DECIMAL));								
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierProductDescription"), vf.createLiteral(params[8]));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierProductId"), vf.createLiteral(params[10]));

						if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
							connection.add(tradeItemInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[12]), XMLSchema.DATETIME));
						}

						connection.add(tradeItemInd, vf.createIRI(baseURI + "handlingInstruction"), vf.createLiteral(params[16]));


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

	public static void processTradeItemsToRemoteRepo (File tradeItemsFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI tradeItemInd;
			IRI shipmentInd;
			IRI loadingUnitInd;


			IRI tradeItemClass = vf.createIRI(baseURI, "TradeItem");
			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI loadingUnitClass = vf.createIRI(baseURI, "LoadingUnit");

			File[] filesInDir = tradeItemsFolder.listFiles();

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
						tradeItemInd = vf.createIRI(baseURI, params[0] + "-" + params[1] + "_tradeItem" );
						connection.add(tradeItemInd, RDF.TYPE, tradeItemClass);

						//adding predicates
						shipmentInd = vf.createIRI(baseURI, params[0] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);
						connection.add(tradeItemInd, vf.createIRI(baseURI + "belongsToShipment"), shipmentInd);

						loadingUnitInd = vf.createIRI(baseURI, params[1] + "_loadingUnit");
						connection.add(loadingUnitInd, RDF.TYPE, loadingUnitClass);
						connection.add(tradeItemInd, vf.createIRI(baseURI + "hasLoadingUnit"), loadingUnitInd);								

						//adding literals
						connection.add(tradeItemInd, vf.createIRI(baseURI + "gtin"), vf.createLiteral(params[2]));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierQuantity"), vf.createLiteral(params[6], XMLSchema.DECIMAL));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "customerQuantity"), vf.createLiteral(params[7], XMLSchema.DECIMAL));								
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierProductDescription"), vf.createLiteral(params[8]));
						connection.add(tradeItemInd, vf.createIRI(baseURI + "supplierProductId"), vf.createLiteral(params[10]));

						if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
							connection.add(tradeItemInd, vf.createIRI(baseURI + "modifiedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[12]), XMLSchema.DATETIME));
						}

						connection.add(tradeItemInd, vf.createIRI(baseURI + "handlingInstruction"), vf.createLiteral(params[16]));


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
