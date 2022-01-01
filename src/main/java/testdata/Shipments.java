package testdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class Shipments {

	

	public static void processShipments (File shipmentsFolder, String baseURI, String dataDir, String indexes) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");


		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI shipmentInd;
			IRI shipperInd;
			IRI receiverInd;

			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI shipperClass = vf.createIRI(baseURI, "Shipper");
			IRI receiverClass = vf.createIRI(baseURI, "Receiver");

			File[] filesInDir = shipmentsFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

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
	
	public static void processShipmentsHTTP (File shipmentsFolder, String baseURI, String rdf4jServer, String repositoryId) {

		//measure runtime
		long startTime = System.nanoTime();

		//measure memory footprint of ontology creation
		Runtime runtimeOntologyCreation = Runtime.getRuntime();
		long usedMemoryBeforeOntologyCreation = runtimeOntologyCreation.totalMemory() - runtimeOntologyCreation.freeMemory();
		System.out.println("Used Memory before ontology creation: " + usedMemoryBeforeOntologyCreation/1000000 + " MB");

		Repository repo = new HTTPRepository(rdf4jServer, repositoryId);

		try (RepositoryConnection connection = repo.getConnection()) {

			ValueFactory vf = connection.getValueFactory();

			IRI shipmentInd;
			IRI shipperInd;
			IRI receiverInd;

			IRI shipmentClass = vf.createIRI(baseURI, "Shipment");
			IRI shipperClass = vf.createIRI(baseURI, "Shipper");
			IRI receiverClass = vf.createIRI(baseURI, "Receiver");

			File[] filesInDir = shipmentsFolder.listFiles();
			String[] params = null;

			BufferedReader br = null;

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					String line;		

					br = new BufferedReader(new FileReader(filesInDir[i]));

					System.out.println("Reading file: " + filesInDir[i].getName());

					while ((line = br.readLine()) != null) {

						params = line.split(",");

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