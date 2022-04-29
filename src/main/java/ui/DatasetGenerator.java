package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import csv2KG.Consignments;
import csv2KG.DangerousGoods;
import csv2KG.HubReconstructionLocations;
import csv2KG.LoadingUnits;
import csv2KG.Parties;
import csv2KG.ShipmentItems;
import csv2KG.Shipments;
import csv2KG.TradeItems;
import csv2KG.Transports;
import csv2KG.Waves;
import csv2KG.XDocLoadingUnits;

public class DatasetGenerator {

	public static void main(String[] args) {

		//String baseURI = "https://w3id.org/latuli/ontology/m3#";

		//if storing data in local http repository
		//		String rdf4jServer = "http://localhost:8080/rdf4j-server";
		//		String repositoryId = "LATKG1";
		//		Repository http_repo = new HTTPRepository(rdf4jServer, repositoryId);
		//		testRun(rdf4jServer, repositoryId, http_repo, baseURI);

		//if filtering by period
//		String folderPath = "./files/CSV/Audun/_FILTER_PERIOD";
//		String dataDir = "/Users/audunvennesland/RDF4J_db_filteredByPeriod";
//		String indexes = "spoc,posc,cosp";	
//		Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));
//		createDatasetFilteredOnPeriod(folderPath, dataDir, indexes, repo, baseURI);


		//if filtering by particular hub
		//String dataDir = "/Users/audunvennesland/RDF4J_db_filteredByHub";
		//String indexes = "spoc,posc,cosp";
		//Repository repo = new SailRepository(new NativeStore(new File(dataDir), indexes));
		//realRunHub(dataDir, indexes, repo, baseURI);
		
		//if creating the full dataset to tsv
//		String folderPath = "./files/CSV/Audun/_ORIGINAL_CSV";
		
		//if creating a period-based dataset to tsv
		String folderPath = "./files/CSV/Audun/_FILTER_PERIOD_2019-12-01T00:00:00_2020-12-31T00:00:00";
		String outputFile = "files/CSV/Audun/Dataset_FILTER_PERIOD_2019-12-01T00:00:00_2020-12-31T00:00:00.tsv";
		
		createFullDatasetToTSV(folderPath, outputFile);

		//if creating the full dataset to n-triples (expensive!)
//		String folderPath = "./files/CSV/Audun/_ORIGINAL_CSV";
//		String outputFile = "files/CSV/Audun/full_dataset.nt";
//		createFullDatasetToNTriples(folderPath, outputFile);


	}
	
	
	/**
	 * Creates a dataset from csv data and represents the KG as N-Triples
	 * @param folderPath
	 * @param outputFile
	   15. jan. 2022
	 */
	public static void createFullDatasetToNTriples (String folderPath, String outputFile) {

		long startTime = System.nanoTime();

		//access all files in folder
		File parentFolder = new File(folderPath);
		File[] files = parentFolder.listFiles();

		for (File folder : files) {

			if (folder.getName().startsWith("xdlu")) {		

				System.out.println("\nProcessing XDocLoadingUnits\n");

				XDocLoadingUnits.processXDocLoadingUnitsToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("consignments")) {

				System.out.println("\nProcessing Consignments\n");

				Consignments.processConsignmentsToNTriple (folder, outputFile);
			}

			else if (folder.getName().startsWith("parties")) {

				System.out.println("\nProcessing Parties\n");

				Parties.processPartiesToNTriple(folder, outputFile);

			} else if (folder.getName().startsWith("dgr")) {

				System.out.println("\nProcessing Dangerous Goods\n");

				DangerousGoods.processDangerousGoodsToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("shipmentitems")) {

				System.out.println("\nProcessing Shipment Items\n");

				ShipmentItems.processShipmentItemsToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("shipments")) {

				System.out.println("\nProcessing Shipments\n");

				Shipments.processShipmentsToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("waves")) {

				System.out.println("\nProcessing Waves\n");

				Waves.processWavesToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("tradeitems")) {

				System.out.println("\nProcessing Trade Items\n");

				TradeItems.processTradeItemsToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("transports")) {

				System.out.println("\nProcessing Transports\n");

				Transports.processTransportsToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("loadingunits")) {

				System.out.println("\nProcessing Loading Units\n");

				LoadingUnits.processLoadingUnitsToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("hubreconstructionlocations")) {

				System.out.println("\nProcessing Hub Reconstruction Locations\n");

				HubReconstructionLocations.processHubReconstructionLocationsToNTriple (folder, outputFile);
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

	/**
	 * Creates a dataset from csv data and represents the KG as TSV
	 * @param folderPath
	 * @param outputFile
	   15. jan. 2022
	 */
	public static void createFullDatasetToTSV (String folderPath, String outputFile) {

		long startTime = System.nanoTime();

		//access all files in folder
		File parentFolder = new File(folderPath);
		File[] files = parentFolder.listFiles();

		for (File folder : files) {

			if (folder.getName().startsWith("xdlu")) {		

				System.out.println("\nProcessing XDocLoadingUnits\n");

				XDocLoadingUnits.processXDocLoadingUnitsToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("consignments")) {

				System.out.println("\nProcessing Consignments\n");

				Consignments.processConsignmentsToTSV (folder, outputFile);
			}

			else if (folder.getName().startsWith("parties")) {

				System.out.println("\nProcessing Parties\n");

				Parties.processPartiesToTSV(folder, outputFile);

			} else if (folder.getName().startsWith("dgr")) {

				System.out.println("\nProcessing Dangerous Goods\n");

				DangerousGoods.processDangerousGoodsToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("shipmentitems")) {

				System.out.println("\nProcessing Shipment Items\n");

				ShipmentItems.processShipmentItemsToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("shipments")) {

				System.out.println("\nProcessing Shipments\n");

				Shipments.processShipmentsToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("waves")) {

				System.out.println("\nProcessing Waves\n");

				Waves.processWavesToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("tradeitems")) {

				System.out.println("\nProcessing Trade Items\n");

				TradeItems.processTradeItemsToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("transports")) {

				System.out.println("\nProcessing Transports\n");

				Transports.processTransportsToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("loadingunits")) {

				System.out.println("\nProcessing Loading Units\n");

				LoadingUnits.processLoadingUnitsToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("hubreconstructionlocations")) {

				System.out.println("\nProcessing Hub Reconstruction Locations\n");

				HubReconstructionLocations.processHubReconstructionLocationsToTSV (folder, outputFile);
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



	//real-run against the local native store and csv data filtered on a particular period of time
	public static void createDatasetFilteredOnPeriod (String folderPath, String dataDir, String indexes, Repository repo, String baseURI) {


		long startTime = System.nanoTime();

		//access all files in folder
		File parentFolder = new File(folderPath);
		File[] files = parentFolder.listFiles();

		for (File folder : files) {

			if (folder.getName().startsWith("xdlu")) {		

				System.out.println("\nProcessing XDoc Loading Units\n");

				XDocLoadingUnits.processXDocLoadingUnitsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("consignments")) {

				System.out.println("\nProcessing Consignments\n");

				Consignments.processConsignmentsToLocalRepo(folder, baseURI, dataDir, indexes, repo);
			}

			else if (folder.getName().startsWith("parties")) {

				System.out.println("\nProcessing Parties\n");

				Parties.processPartiesToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("dgr_")) {

				System.out.println("\nProcessing Dangerous Goods\n");

				DangerousGoods.processDangerousGoodsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("shipmentitems")) {

				System.out.println("\nProcessing Shipment Items\n");

				ShipmentItems.processShipmentItemsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("shipments")) {

				System.out.println("\nProcessing Shipments\n");

				Shipments.processShipmentsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("waves")) {

				System.out.println("\nProcessing Waves\n");

				Waves.processWavesToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("tradeitems")) {

				System.out.println("\nProcessing Trade Items\n");

				TradeItems.processTradeItemsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("transports")) {

				System.out.println("\nProcessing Transports\n");

				Transports.processTransportsToLocalRepo (folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("loadingunits")) {

				System.out.println("\nProcessing Loading Units\n");

				LoadingUnits.processLoadingUnitsToLocalRepo (folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("hubreconstructionlocations")) {

				System.out.println("\nProcessing Hub Reconstruction Locations\n");

				HubReconstructionLocations.processHubReconstructionLocationsToLocalRepo (folder, baseURI, dataDir, indexes, repo);
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

	//real-run against the local native store and csv data filtered on a particular hub
	public static void createDatasetFilteredOnHub (String dataDir, String indexes, Repository repo) {

		long startTime = System.nanoTime();

		String baseURI = "http://latuli.no/onto#";



		//access all files in folder
		File parentFolder = new File("./files/CSV/Audun/_FILTER_HUB");
		File[] files = parentFolder.listFiles();

		for (File folder : files) {

			if (folder.getName().startsWith("xdlu")) {		

				System.out.println("\nProcessing XDoc Loading Units\n");

				XDocLoadingUnits.processXDocLoadingUnitsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("consignments")) {

				System.out.println("\nProcessing Consignments\n");

				Consignments.processConsignmentsToLocalRepo(folder, baseURI, dataDir, indexes, repo);
			}

			else if (folder.getName().startsWith("parties")) {

				System.out.println("\nProcessing Parties\n");

				Parties.processPartiesToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("dgr_")) {

				System.out.println("\nProcessing Dangerous Goods\n");

				DangerousGoods.processDangerousGoodsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("shipmentitems")) {

				System.out.println("\nProcessing Shipment Items\n");

				ShipmentItems.processShipmentItemsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("shipments")) {

				System.out.println("\nProcessing Shipments\n");

				Shipments.processShipmentsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("waves")) {

				System.out.println("\nProcessing Waves\n");

				Waves.processWavesToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("tradeitems")) {

				System.out.println("\nProcessing Trade Items\n");

				TradeItems.processTradeItemsToLocalRepo(folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("transports")) {

				System.out.println("\nProcessing Transports\n");

				Transports.processTransportsToLocalRepo (folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("loadingunits")) {

				System.out.println("\nProcessing Loading Units\n");

				LoadingUnits.processLoadingUnitsToLocalRepo (folder, baseURI, dataDir, indexes, repo);

			} else if (folder.getName().startsWith("hubreconstructionlocations")) {

				System.out.println("\nProcessing Hub Reconstruction Locations\n");

				HubReconstructionLocations.processHubReconstructionLocationsToLocalRepo (folder, baseURI, dataDir, indexes, repo);
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
