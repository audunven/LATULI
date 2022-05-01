package ui;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

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
import csvfiltering.CSVProcessor;

public class DatasetGenerator {

	public static void main(String[] args) {
		
		  Stopwatch stopwatch = Stopwatch.createStarted();

		  
		  Scanner input = new Scanner(System.in);
		  
		  System.out.println("Enter start datetime (yyyy-MM-dd'T'HH:mm:ss): "); 
		  String startDateTime = input.nextLine();
		  
		  System.out.println("Enter end datetime (yyyy-MM-dd'T'HH:mm:ss): "); 
		  String endDateTime = input.nextLine();
		  
		  System.out.println("Enter source folder for CSV files: "); 
		  String csvSource = input.nextLine();
		  		  
		  System.out.println("Enter folder where the generated knowledge graph will be stored:");
		  String kg = input.nextLine();
		  
		  input.close(); 
		  
		  System.out.println("This process may take several minutes to complete...");	  
		  
		  String tmpSplitFiles = "tmpSplitFiles/";
		  String tmpSplitFilesFiltered = "tmpSplitFilesFiltered/";
		  
		
//TESTING  
//		String startDateTime = "2019-12-01T00:00:00";
//		String endDateTime = "2020-03-28T00:00:00";
//		String csvSource = "./files/TEST_ORIGINAL_CSV/";
//		String kg = "./files/kg-output/";
//		String tmpSplitFiles = "./files/tmpSplitFiles/";
//		String tmpSplitFilesFiltered = "./files/tmpSplitFilesFiltered/";
		
		int chunkSize = 50;
		
		//File csvSourceFolder = new File(csvSource);
		File tmpSplitFilesFolder = new File(tmpSplitFiles);
		File tmpSplitFilesFilteredFolder = new File (tmpSplitFilesFiltered);
		File csvSourceFolder = new File (csvSource);
		File kgFolder = new File (kg);
		
		if (!csvSourceFolder.exists()) {
			csvSourceFolder.mkdir();
		} if (!tmpSplitFilesFolder.exists()) {
			tmpSplitFilesFolder.mkdir();
		} if (!tmpSplitFilesFilteredFolder.exists()) {
			tmpSplitFilesFilteredFolder.mkdir();
		} if (!kgFolder.exists()) {
			kgFolder.mkdir();
		} 
		
		List<File> list = new ArrayList<>();
		
		try {
			list = CSVProcessor.createFileList(csvSource);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (File file : list) {
			System.out.println("Splitting file " + file.getName() + " (" + file.length() / (1024 * 1024) + " MB) into chunks of max size " + chunkSize + " MB");
			try {
				CSVProcessor.splitCSV(file.getPath(), tmpSplitFiles, chunkSize);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			System.out.println("Starting filtering on period...");
			CSVProcessor.filterOnPeriod(startDateTime, endDateTime, tmpSplitFiles, tmpSplitFilesFiltered);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String outputFile = kg + "/KG.tsv";
		
		System.out.println("Creating the knowledge graph and storing the KG file as " + outputFile);
		createFullDatasetToTSV(tmpSplitFilesFiltered, outputFile);

		//if creating the full dataset to n-triples (expensive!)
//		String folderPath = "./files/CSV/Audun/_ORIGINAL_CSV";
//		String outputFile = "files/CSV/Audun/full_dataset.nt";
//		createFullDatasetToNTriples(folderPath, outputFile);
		
		//remove the tmp dirs
		tmpSplitFilesFolder.delete();
		tmpSplitFilesFilteredFolder.delete();

		stopwatch.stop();
		System.out.println("The knowledge graph generation process took: " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes.");

	}
	
	/**
	 * Creates a dataset from csv data and represents the KG as TSV
	 * @param tmpSplitFilesFilteredFolder
	 * @param outputFile
	   15. jan. 2022
	 */
	public static void createFullDatasetToTSV (String tmpSplitFilesFiltered, String outputFile) {

		//access all files in folder
		File parentFolder = new File(tmpSplitFilesFiltered);
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

	}
		
	
	/**
	 * Creates a dataset from csv data and represents the KG as N-Triples
	 * @param folderPath
	 * @param outputFile
	   15. jan. 2022
	 */
	public static void createFullDatasetToNTriples (String folderPath, String outputFile) {

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

	}

	

	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}

}
