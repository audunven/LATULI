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
public class Shipments {

	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";

	public static void processShipmentsToNTriple (File shipmentsFolder, String ntFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "Shipment";
		String tripleClosure = "> .\n";

		String shipmentEntity;

		File[] filesInDir = shipmentsFolder.listFiles();

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
					shipmentEntity = params[0] + "_shipment>";
					bw.write(KGUtilities.createType(shipmentEntity, baseURI, rdf_type, type, tripleClosure));

					//hasShipperParty
					bw.write(KGUtilities.createObjectProperty(shipmentEntity, baseURI, "hasShipperParty", params[8], "_party", tripleClosure));


					//hasReceiverParty						
					bw.write(KGUtilities.createObjectProperty(shipmentEntity, baseURI, "hasReceiverParty", params[11], "_party", tripleClosure));

					//shipmentId						
					bw.write(KGUtilities.createDataProperty(shipmentEntity, baseURI, "shipmentId", params[0], DATATYPE_STRING, tripleClosure));


					//shippedOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {

						bw.write(KGUtilities.createDataProperty(shipmentEntity, baseURI, "shippedOn", StringUtilities.convertToDateTime(params[3]), DATATYPE_DATETIME, tripleClosure));

					}

					//expectedDeliveryOn						
					if (!StringUtilities.convertToDateTime(params[4]).equals("0000-00-00T00:00:00")) {

						bw.write(KGUtilities.createDataProperty(shipmentEntity, baseURI, "expectedDeliveryOn", StringUtilities.convertToDateTime(params[4]), DATATYPE_DATETIME, tripleClosure));

					}

					//plannedDeliveryDate					
					if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {

						bw.write(KGUtilities.createDataProperty(shipmentEntity, baseURI, "plannedDeliveryDate", StringUtilities.convertToDateTime(params[13]), DATATYPE_DATETIME, tripleClosure));

					}

					//qttBoxes
					bw.write(KGUtilities.createDataProperty(shipmentEntity, baseURI, "qttBoxes", params[18], DATATYPE_INT, tripleClosure));


					//qttPallets						
					bw.write(KGUtilities.createDataProperty(shipmentEntity, baseURI, "qttPallets", params[19], DATATYPE_INT, tripleClosure));


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

	public static void processShipmentsToTSV (File shipmentsFolder, String tsvFile) {


		String shipmentEntity;

		File[] filesInDir = shipmentsFolder.listFiles();

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
					shipmentEntity = params[0] + "_shipment";
					bw.write(shipmentEntity + "\t" + "isType" + "\t" + "Shipment" + "\n");


					//hasShipperParty
					bw.write(shipmentEntity + "\t" + "hasShipperParty" + "\t" + params[8] + "_party" + "\n");


					//hasReceiverParty						
					bw.write(shipmentEntity + "\t" + "hasReceiverParty" + "\t" + params[11] + "_party" + "\n");

					//hasShipmentId						
					bw.write(shipmentEntity + "\t" + "hasShipmentId" + "\t" + params[0] + "\n");


					//hasShippedOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {

						bw.write(shipmentEntity + "\t" + "hasShippedOn" + "\t" + StringUtilities.convertToDateTime(params[3]) + "\n");

					}

					//hasExpectedDeliveryOn						
					if (!StringUtilities.convertToDateTime(params[4]).equals("0000-00-00T00:00:00")) {

						bw.write(shipmentEntity + "\t" + "hasExpectedDeliveryOn" + "\t" + StringUtilities.convertToDateTime(params[4]) + "\n");

					}

					//hasPlannedDeliveryDate					
					if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {

						bw.write(shipmentEntity + "\t" + "hasPlannedDeliveryDate" + "\t" + StringUtilities.convertToDateTime(params[13]) + "\n");

					}

					//hasQttBoxes
					bw.write(shipmentEntity + "\t" + "hasQttBoxes" + "\t" + params[18] + "\n");


					//hasQttPallets						
					bw.write(shipmentEntity + "\t" + "hasQttPallets" + "\t" + params[19] + "\n");


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

	public static void processShipmentsToLocalRepo (File shipmentsFolder, String baseURI, String dataDir, String indexes, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI shipmentInd;
			IRI shipperInd;
			IRI receiverInd;

			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI shipperClass = vf.createIRI(baseURI, "Shipper");
			IRI receiverClass = vf.createIRI(baseURI, "Receiver");

			File[] filesInDir = shipmentsFolder.listFiles();

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
						shipmentInd = vf.createIRI(baseURI, params[0] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);

						//adding predicates
						shipperInd = vf.createIRI(baseURI, params[8] + "_party");
						connection.add(shipperInd, RDF.TYPE, shipperClass);
						connection.add(shipmentInd, vf.createIRI(baseURI + "hasShipperParty"), shipperInd);

						receiverInd = vf.createIRI(baseURI, params[11] + "_party");
						connection.add(receiverInd, RDF.TYPE, receiverClass);
						connection.add(shipmentInd, vf.createIRI(baseURI + "hasReceiverParty"), receiverInd);

						connection.add(shipmentInd, vf.createIRI(baseURI + "shipmentId"), vf.createLiteral(params[0]));

						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							connection.add(shipmentInd, vf.createIRI(baseURI + "shippedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));
						}

						if (!StringUtilities.convertToDateTime(params[4]).equals("0000-00-00T00:00:00")) {
							connection.add(shipmentInd, vf.createIRI(baseURI + "expectedDeliveryOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[4]), XMLSchema.DATETIME));
						}

						if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
							connection.add(shipmentInd, vf.createIRI(baseURI + "plannedDeliveryDate"), vf.createLiteral(StringUtilities.convertToDateTime(params[13]), XMLSchema.DATETIME));
						}

						connection.add(shipmentInd, vf.createIRI(baseURI + "qttBoxes"), vf.createLiteral(params[18], XMLSchema.INT));
						connection.add(shipmentInd, vf.createIRI(baseURI + "qttPallets"), vf.createLiteral(params[19], XMLSchema.INT));

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

	public static void processShipmentsToRemoteRepo (File shipmentsFolder, String baseURI, String rdf4jServer, String repositoryId, Repository repo) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		try (RepositoryConnection connection = repo.getConnection()) {

			connection.setNamespace("m3", baseURI);

			ValueFactory vf = connection.getValueFactory();

			IRI shipmentInd;
			IRI shipperInd;
			IRI receiverInd;

			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI shipperClass = vf.createIRI(baseURI, "Shipper");
			IRI receiverClass = vf.createIRI(baseURI, "Receiver");

			File[] filesInDir = shipmentsFolder.listFiles();

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
						shipmentInd = vf.createIRI(baseURI, params[0] + "_shipment");
						connection.add(shipmentInd, RDF.TYPE, shipmentClass);

						//adding predicates
						shipperInd = vf.createIRI(baseURI, params[8] + "_party");
						connection.add(shipperInd, RDF.TYPE, shipperClass);
						connection.add(shipmentInd, vf.createIRI(baseURI + "hasShipperParty"), shipperInd);

						receiverInd = vf.createIRI(baseURI, params[11] + "_party");
						connection.add(receiverInd, RDF.TYPE, receiverClass);
						connection.add(shipmentInd, vf.createIRI(baseURI + "hasReceiverParty"), receiverInd);

						connection.add(shipmentInd, vf.createIRI(baseURI + "shipmentId"), vf.createLiteral(params[0]));

						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							connection.add(shipmentInd, vf.createIRI(baseURI + "shippedOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[3]), XMLSchema.DATETIME));
						}

						if (!StringUtilities.convertToDateTime(params[4]).equals("0000-00-00T00:00:00")) {
							connection.add(shipmentInd, vf.createIRI(baseURI + "expectedDeliveryOn"), vf.createLiteral(StringUtilities.convertToDateTime(params[4]), XMLSchema.DATETIME));
						}

						if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
							connection.add(shipmentInd, vf.createIRI(baseURI + "plannedDeliveryDate"), vf.createLiteral(StringUtilities.convertToDateTime(params[13]), XMLSchema.DATETIME));
						}

						connection.add(shipmentInd, vf.createIRI(baseURI + "qttBoxes"), vf.createLiteral(params[18], XMLSchema.INT));
						connection.add(shipmentInd, vf.createIRI(baseURI + "qttPallets"), vf.createLiteral(params[19], XMLSchema.INT));

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