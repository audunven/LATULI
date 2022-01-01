package ui;

import java.io.File;

import testdata.Consignments;
import testdata.DangerousGoods;
import testdata.HubReconstructionLocations;
import testdata.LoadingUnits;
import testdata.Parties;
import testdata.ShipmentItems;
import testdata.Shipments;
import testdata.TradeItems;
import testdata.Transports;
import testdata.Waves;
import testdata.XDocLoadingUnits;

public class M3OntoGeneratorUI_FullDataset {

	public static void main(String[] args) {
		
		testRun();

	}

	//test-run against the local RDF4J workbench
	public static void testRun() {

		long startTime = System.nanoTime();

		String baseURI = "http://latuli.no/onto#";

		//if communicating with a http repository
		String rdf4jServer = "http://localhost:8080/rdf4j-server";
		String repositoryId = "LATKG1";


		//access all files in folder
		File parentFolder = new File("./files/CSV/Audun/TEST_RUN");
		File[] files = parentFolder.listFiles();

		for (File folder : files) {

			if (folder.getName().startsWith("filtered_xdlus")) {		
				
				System.out.println("\nProcessing XDocLoadingUnits\n");

				XDocLoadingUnits.processXDocLoadingUnitsHTTP (folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_consignments")) {
				
				System.out.println("\nProcessing Consignments\n");

				Consignments.processConsignmentsHTTP(folder, baseURI, rdf4jServer, repositoryId);
			}

			else if (folder.getName().startsWith("filtered_parties")) {
				
				System.out.println("\nProcessing Parties\n");

				Parties.processPartiesHTTP(folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_dgrs")) {
				
				System.out.println("\nProcessing Dangerous Goods\n");

				DangerousGoods.processDangerousGoodsHTTP (folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_shipmentitems")) {
				
				System.out.println("\nProcessing Shipment Items\n");

				ShipmentItems.processShipmentItemsHTTP (folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_shipments")) {
				
				System.out.println("\nProcessing Shipments\n");

				Shipments.processShipmentsHTTP (folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_waves")) {
				
				System.out.println("\nProcessing Waves\n");

				Waves.processWavesHTTP (folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_tradeitems")) {
				
				System.out.println("\nProcessing Trade Items\n");

				TradeItems.processTradeItemsHTTP (folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_transports")) {
				
				System.out.println("\nProcessing Transports\n");

				Transports.processTransportsHTTP (folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_loadingunits")) {
				
				System.out.println("\nProcessing Loading Units\n");

				LoadingUnits.processLoadingUnitsHTTP (folder, baseURI, rdf4jServer, repositoryId);

			} else if (folder.getName().startsWith("filtered_hubreconstructionlocations")) {
				
				System.out.println("\nProcessing Hub Reconstruction Locations\n");

				HubReconstructionLocations.processHubReconstructionLocationsHTTP (folder, baseURI, rdf4jServer, repositoryId);
			}


		}

		long endTime = System.nanoTime();		
		long timeElapsed = endTime - startTime;		
		System.err.println("The ontology generation process took: " + timeElapsed/60000000000.00 + " minutes");

		System.out.println("\nMEMORY USAGE for the entire transformation process: ");
		System.out.println("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB");  

	}

	//real-run against the local native store
	public static void realRun() {

		long startTime = System.nanoTime();

		String baseURI = "http://latuli.no/onto#";

		//if using a native repository
		String dataDir = "/Users/audunvennesland/RDF4j_db_test";
		String indexes = "spoc,posc,cosp";

		//access all files in folder
		File parentFolder = new File("./files/CSV/Audun");
		File[] files = parentFolder.listFiles();

		for (File folder : files) {

			if (folder.getName().startsWith("filtered_xdlus")) {		
				
				System.out.println("\nProcessing XDoc Loading Units\n");

				XDocLoadingUnits.processXDocLoadingUnits(folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_consignments")) {
				
				System.out.println("\nProcessing Consignments\n");

				Consignments.processConsignments(folder, baseURI, dataDir, indexes);
			}

			else if (folder.getName().startsWith("filtered_parties")) {
				
				System.out.println("\nProcessing Parties\n");

				Parties.processParties(folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_dgrs")) {
				
				System.out.println("\nProcessing Dangerous Goods\n");

				DangerousGoods.processDangerousGoods(folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_shipmentitems")) {
				
				System.out.println("\nProcessing Shipment Items\n");

				ShipmentItems.processShipmentItems(folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_shipments")) {
				
				System.out.println("\nProcessing Shipments\n");

				Shipments.processShipments(folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_waves")) {
				
				System.out.println("\nProcessing Waves\n");

				Waves.processWaves(folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_tradeitems")) {
				
				System.out.println("\nProcessing Trade Items\n");

				TradeItems.processTradeItems(folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_transports")) {
				
				System.out.println("\nProcessing Transports\n");

				Transports.processTransports (folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_loadingunits")) {
				
				System.out.println("\nProcessing Loading Units\n");

				LoadingUnits.processLoadingUnits (folder, baseURI, dataDir, indexes);

			} else if (folder.getName().startsWith("filtered_hubreconstructionlocations")) {
				
				System.out.println("\nProcessing Hub Reconstruction Locations\n");

				HubReconstructionLocations.processHubReconstructionLocations (folder, baseURI, dataDir, indexes);
			}


		}

		long endTime = System.nanoTime();		
		long timeElapsed = endTime - startTime;		
		System.err.println("The ontology generation process took: " + timeElapsed/60000000000.00 + " minutes");

		System.out.println("\nMEMORY USAGE for the entire transformation process: ");
		System.out.println("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000 + " MB");
		System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory()/1000000 + " MB");
		System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory()/1000000 + " MB");
		System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory()/1000000 + " MB");  

	}




	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}

}
