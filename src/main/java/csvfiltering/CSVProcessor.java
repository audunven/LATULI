package csvfiltering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CSVProcessor {

	public static void main(String[] args) throws ParseException, IOException {

		/*
		String startDateTime = "2019-12-01T00:00:00";
		String endDateTime = "2020-12-31T00:00:00";
		String sourceFolder = "./files/CSV/Audun/_ORIGINAL_CSV/";
		String targetFolder = "./files/CSV/Audun/_FILTER_PERIOD_122019_122020_2/";
		String sourceFolderFile = "";
		String targetFolderFile = "./files/CSV/Audun/csv_target_folders.txt";
		filterOnPeriod(startDateTime, endDateTime, sourceFolder, targetFolder, targetFolderFile);

		int csvFieldNumber = 13;
		String hub = "C042";		
		//filterOnHub(csvFieldNumber, hub);
		 */

		//test list files
		String outputFolder = "./files/output/";
		
		/*
		String dir = "./files/ORIGINAL_CSV/";	
		File output = new File(outputFolder);

		if (!output.exists()) {
			output.mkdir();
		}
		
		List<File> list = createFileList(dir);

		for (File file : list) {
			System.out.println("Processing file: " + file.getName());
			splitCSV(file.getPath(), outputFolder, 50);
		}
		*/
		
	
		String startDateTime = "2019-12-01T00:00:00";
		String endDateTime = "2020-12-31T00:00:00";
		String sourceFolder = outputFolder;
		String targetFolder = "./files/CSV/Audun/_FILTER_PERIOD_"+startDateTime+"_"+endDateTime+"/";
		String targetFolderFile = "./files/CSV/Audun/csv_target_folders.txt";
		filterOnPeriod(startDateTime, endDateTime, sourceFolder, targetFolder, targetFolderFile);
		
	}
	
	private static void splitCSV(String inputFile, String outputFolder, int chunkSizeInMb) throws IOException {

		//create a folder to hold the chunks
		File chunkFolder = new File(outputFolder + "/" + inputFile.substring(inputFile.lastIndexOf("/"), inputFile.lastIndexOf(".")) + "_split");

		if (!chunkFolder.exists()) {
			chunkFolder.mkdir();
		}
		
		//get the core filename
		String fileName = inputFile.substring(inputFile.lastIndexOf("/"), inputFile.lastIndexOf("."));

		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line="";
		int fileSize = 0;
		BufferedWriter fos = new BufferedWriter(new FileWriter(chunkFolder + "/" + fileName + "_" +new Date().getTime()+".csv",true));
		while((line = bufferedReader.readLine()) != null) {
				if(fileSize + line.getBytes().length > chunkSizeInMb * 1024 * 1024){
						fos.flush();
						fos.close();
						fos = new BufferedWriter(new FileWriter(chunkFolder + "/" + fileName + "_" +new Date().getTime()+".csv",true));
						fos.write(line+"\n");
						fileSize = line.getBytes().length;
				}else{
						fos.write(line+"\n");
						fileSize += line.getBytes().length;
				}
		}          
		fos.flush();
		fos.close();
		bufferedReader.close();
		}
	

	private static List<File> createFileList(String dir) throws IOException {
		List<File> fileList = new ArrayList<File>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : stream) {
				if (!Files.isDirectory(path)) {
					fileList.add(path.toFile());
				}
			}
		}
		return fileList;
	}

	private static void createFolders(String targetFolder, String targetFolderFile) throws IOException {

		//create the parent target folder
		File p_folder = new File(targetFolder);

		if (!p_folder.exists()) {
			p_folder.mkdir();
		}

		//create the id folders and target folders
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(targetFolderFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		File t_folder = null;
		//String filePath = null;
		String filePath = targetFolderFile;
		try {
			while ((filePath = br.readLine()) != null) {
				t_folder = new File(targetFolder + filePath);
				if (!t_folder.exists()) {
					t_folder.mkdir();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		br.close();

	}

	public static void filterOnPeriod(String startDateTime, String endDateTime, String sourceFolder, String targetFolder, String targetFolderFile) throws ParseException, IOException {

		createFolders(targetFolder, targetFolderFile);

		String xdlu_folder_in = sourceFolder + "xdlu_split";
		String consignments_folder_in = sourceFolder + "consignments_split";
		String loadingUnits_folder_in = sourceFolder + "loadingunits_split";
		String waves_folder_in = sourceFolder + "waves_split";
		String tradeItems_folder_in = sourceFolder + "tradeitems_split";
		String dgr_folder_in = sourceFolder + "dgr_split";
		String shipmentItems_folder_in = sourceFolder + "shipmentitems_split";
		String shipments_folder_in = sourceFolder + "shipments_split";
		String transports_folder_in = sourceFolder + "transports_split";		

		String loadingUnitIdFile = targetFolder + "relevant_loading_unit_ids/loadingUnitIds.txt"; 
		String consignmentIdFile = targetFolder + "relevant_consignment_ids/consignmentIds.txt"; 
		String waveIdFile = targetFolder + "relevant_wave_ids/waveIds.txt"; 
		String shipmentIdFile = targetFolder + "relevant_shipment_ids/shipmentIds.txt";
		String transportIdFile = targetFolder + "relevant_transport_ids/transportIds.txt";

		String xdlu_folder_out = targetFolder + "xdlu_split_filtered";
		String consignments_folder_filtered = targetFolder + "consignments_split_filtered";
		String loadingUnits_folder_filtered = targetFolder + "loadingunits_split_filtered";
		String waves_folder_filtered = targetFolder + "waves_split_filtered";
		String tradeItems_folder_filtered = targetFolder + "tradeitems_split_filtered";
		String dgr_folder_filtered = targetFolder + "dgr_split_filtered";
		String shipmentItems_folder_filtered = targetFolder + "shipmentitems_split_filtered";
		String shipments_folder_filtered = targetFolder + "shipments_split_filtered";
		String transports_folder_filtered = targetFolder + "transports_split_filtered";

		//the following calls need to run in the correct order
		filterXDocLoadingUnitsOnPeriod (xdlu_folder_in, xdlu_folder_out, startDateTime, endDateTime);		
		printRelevantConLoadWave (xdlu_folder_out, loadingUnitIdFile, consignmentIdFile, waveIdFile);
		filterConsignments(consignments_folder_in, consignments_folder_filtered, consignmentIdFile);		
		filterLoadingUnits(loadingUnits_folder_in, loadingUnits_folder_filtered, loadingUnitIdFile);		
		filterWaves(waves_folder_in, waves_folder_filtered, waveIdFile);		
		filterTradeItems(tradeItems_folder_in, tradeItems_folder_filtered, loadingUnitIdFile);	
		filterDangerousGoods(dgr_folder_in, dgr_folder_filtered, loadingUnitIdFile);		
		printRelevantShipmentIds (shipmentItems_folder_in, loadingUnitIdFile, shipmentIdFile);			
		filterShipmentItems(shipmentItems_folder_in, shipmentItems_folder_filtered, shipmentIdFile);			
		filterShipments(shipments_folder_in, shipments_folder_filtered, shipmentIdFile);			
		printRelevantTransports(consignments_folder_in, consignmentIdFile, transportIdFile);
		filterTransports (transports_folder_in, transports_folder_filtered, transportIdFile);

	}

	public static void filterOnHub(int csvFieldNumber, String hub) throws ParseException {

		String xdlu_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/xdlu_split";
		String consignments_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/consignments_split";
		String loadingUnits_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/loadingunits_split";
		String waves_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/waves_split";
		String tradeItems_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/tradeitems_split";
		String dgr_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/drgs_split";
		String shipmentItems_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/shipmentitems_split";
		String shipments_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/shipments_split";
		String transports_folder_in = "./files/CSV/Audun/_ORIGINAL_CSV/transports_split";

		String xdlu_folder_out = "./files/CSV/Audun/_FILTER_HUB/xdlu_split_filtered";
		String loadingUnitIdFile = "./files/CSV/Audun/_FILTER_HUB/relevant_loading_unit_ids/loadingUnitIds.txt"; 
		String consignmentIdFile = "./files/CSV/Audun/_FILTER_HUB/relevant_consignment_ids/consignmentIds.txt"; 
		String waveIdFile = "./files/CSV/Audun/_FILTER_HUB/relevant_wave_ids/waveIds.txt"; 
		String shipmentIdFile = "./files/CSV/Audun/_FILTER_HUB/relevant_shipment_ids/shipmentIds.txt";
		String transportIdFile = "./files/CSV/Audun/_FILTER_HUB/relevant_transport_ids/transportIds.txt";
		String consignments_folder_filtered = "./files/CSV/Audun/_FILTER_HUB/consignments_split_filtered";
		String loadingUnits_folder_filtered = "./files/CSV/Audun/_FILTER_HUB/loadingunits_split_filtered";
		String waves_folder_filtered = "./files/CSV/Audun/_FILTER_HUB/waves_split_filtered";
		String tradeItems_folder_filtered = "./files/CSV/Audun/_FILTER_HUB/tradeitems_split_filtered";
		String dgr_folder_filtered = "./files/CSV/Audun/_FILTER_HUB/drgs_split_filtered";
		String shipmentItems_folder_filtered = "./files/CSV/Audun/_FILTER_HUB/shipmentitems_split_filtered";
		String shipments_folder_filtered = "./files/CSV/Audun/_FILTER_HUB/shipments_split_filtered";
		String transports_folder_filtered = "./files/CSV/Audun/_FILTER_HUB/transports_split_filtered";

		filterXDocLoadingUnitsOnHub(csvFieldNumber, hub, xdlu_folder_in, xdlu_folder_out);

		printRelevantConLoadWave (xdlu_folder_out, loadingUnitIdFile, consignmentIdFile, waveIdFile);
		filterConsignments(consignments_folder_in, consignments_folder_filtered, consignmentIdFile);		
		filterLoadingUnits(loadingUnits_folder_in, loadingUnits_folder_filtered, loadingUnitIdFile);		
		filterWaves(waves_folder_in, waves_folder_filtered, waveIdFile);		
		filterTradeItems(tradeItems_folder_in, tradeItems_folder_filtered, loadingUnitIdFile);	
		filterDangerousGoods(dgr_folder_in, dgr_folder_filtered, loadingUnitIdFile);		
		printRelevantShipmentIds (shipmentItems_folder_in, loadingUnitIdFile, shipmentIdFile);			
		filterShipmentItems(shipmentItems_folder_in, shipmentItems_folder_filtered, shipmentIdFile);			
		filterShipments(shipments_folder_in, shipments_folder_filtered, shipmentIdFile);			
		printRelevantTransports(consignments_folder_in, consignmentIdFile, transportIdFile);
		filterTransports (transports_folder_in, transports_folder_filtered, transportIdFile);

	}

	private static void filterTransports (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read transport ids from file
		Set<String> transportIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(relevantIds));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				transportIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);
		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());

			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");

					if (transportIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void filterShipments (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read shipment item ids from file
		Set<String> shipmentItemIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(relevantIds));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {
				

				shipmentItemIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());

			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");

					if (shipmentItemIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void filterShipmentItems (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read shipment item ids from file
		Set<String> shipmentItemIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(relevantIds));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				shipmentItemIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());

			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");

					if (shipmentItemIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void filterDangerousGoods (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read loading unit ids from file
		Set<String> loadingUnitIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(relevantIds));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				loadingUnitIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());

			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");

					if (loadingUnitIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void filterTradeItems (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read loading unit ids from file
		Set<String> loadingUnitIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(relevantIds));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				loadingUnitIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());

			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");

					if (loadingUnitIds.contains(params[1])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}



	private static void filterLoadingUnits (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read loading unit ids from file
		Set<String> loadingUnitIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(relevantIds));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				loadingUnitIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());

			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");

					if (loadingUnitIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void filterWaves (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read wave ids from file
		Set<String> waveIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(relevantIds));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				waveIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());

			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");

					if (waveIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}


	private static void filterConsignments (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read consignment ids from file
		Set<String> consignmentIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(relevantIds));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				consignmentIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());

			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");

					if (consignmentIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void printRelevantShipmentIds (String inputFolder, String filterPath, String shipmentIdsFile) {

		Set<String> shipmentIds = new HashSet<String>();

		File splitFolder = new File(inputFolder);
		File[] filesInDir = splitFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;


		//read loading unit ids from file
		Set<String> loadingUnitIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(filterPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				loadingUnitIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();

		for (int i = 0; i < filesInDir.length; i++) {

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				line = oneByOne(br);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (String[] s : line) {
				if (loadingUnitIds.contains(s[1])) {
					shipmentIds.add(s[0]);
				}
			}

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}


		}

		//print relevant shipment ids to file
		try {
			bw = new BufferedWriter(new FileWriter(shipmentIdsFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String tid : shipmentIds) {
			try {
				bw.write(tid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}


	private static void printRelevantTransports (String inputFolder, String filterPath, String transportIdFile) {

		Set<String> transportIds = new HashSet<String>();

		File splitFolder = new File(inputFolder);
		File[] filesInDir = splitFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;


		//read consignment ids from file
		Set<String> consignmentIds = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(filterPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				consignmentIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();

		for (int i = 0; i < filesInDir.length; i++) {

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				line = oneByOne(br);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (String[] s : line) {
				if (consignmentIds.contains(s[0])) {
					transportIds.add(s[3]);
				}
			}

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}


		}

		//print relevant transport ids to file
		try {
			bw = new BufferedWriter(new FileWriter(transportIdFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String tid : transportIds) {
			try {
				bw.write(tid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}

	private static void printRelevantConLoadWave (String inputFolder, String loadingUnitIdFile, String consignmentIdFile, String waveIdFile) throws ParseException {


		Set<String> consignmentIds = new HashSet<String>();
		Set<String> waveIds = new HashSet<String>();
		Set<String> loadingUnitIds = new HashSet<String>();

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedReader br = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			try {

				String line;		

				br = new BufferedReader(new FileReader(filesInDir[i]));

				System.out.println("\nReading file: " + filesInDir[i].getPath());

				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");


					loadingUnitIds.add(params[7]);
					consignmentIds.add(params[11]);
					consignmentIds.add(params[12]);
					waveIds.add(params[36]);
					loadingUnitIds.add(params[41]);
					loadingUnitIds.add(params[42]);


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


		//print relevant loading unit ids to file
		try {
			bw = new BufferedWriter(new FileWriter(loadingUnitIdFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String luid : loadingUnitIds) {
			try {
				bw.write(luid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//print relevant consignment ids to file
		try {
			bw = new BufferedWriter(new FileWriter(consignmentIdFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String cid : consignmentIds) {
			try {
				bw.write(cid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//print relevant wave ids to file
		try {
			bw = new BufferedWriter(new FileWriter(waveIdFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String wid : waveIds) {
			try {
				bw.write(wid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void filterXDocLoadingUnitsOnPeriod (String inputFolder, String filteredFolder, String startDateTime, String endDateTime) throws ParseException {

//		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
//		File outputFolder = new File(filteredFolder);
//
//		if (!outputFolder.exists()) {
//			outputFolder.mkdir();
//		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedReader br = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());


			try {
				while ((line = br.readLine()) != null) {
				//while ((line = br.readLine()) != null && !line.startsWith(",") && Character.isDigit(line.charAt(0))) {

					params = line.split(",");


					if (!params[8].equals("NULL") && withinPeriod(params[8], startDateTime, endDateTime)) {

						bw.write(line);
						bw.newLine();


					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void filterXDocLoadingUnitsOnHub (int csvFieldNumber, String hub, String inputFolder, String filteredFolder) throws ParseException {

		//FIXME: Probably a more elegant way of creating a folder if it doesn´t exist
		File outputFolder = new File(filteredFolder);

		if (!outputFolder.exists()) {
			outputFolder.mkdir();
		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedReader br = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				br = new BufferedReader(new FileReader(filesInDir[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getPath());


			try {
				while ((line = br.readLine()) != null) {

					params = line.split(",");


					if (params[csvFieldNumber].equals (hub)) {

						bw.write(line);
						bw.newLine();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	//	private static void filterCSVOnHub(int csvFieldNumber, String hub, String inputFile, String outputFile) {
	//
	//		String[] params = null;
	//
	//		String line;		
	//
	//		BufferedReader br = null;
	//
	//		try {
	//			br = new BufferedReader(new FileReader(inputFile));
	//		} catch (FileNotFoundException e) {
	//			e.printStackTrace();
	//		}
	//
	//		BufferedWriter bw = null;
	//		try {
	//			bw = new BufferedWriter(new FileWriter(outputFile));
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//
	//		try {
	//			while ((line = br.readLine()) != null) {
	//
	//				params = line.split(",");
	//
	//				if (params[csvFieldNumber].equals(hub)) {
	//
	//					bw.write(line);
	//					bw.newLine();
	//
	//				}
	//
	//			}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//
	//		try {
	//			br.close();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		try {
	//			bw.close();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//
	//
	//	}

	private static List<String[]> oneByOne(Reader reader) throws Exception {
		List<String[]> list = new ArrayList<>();

		CSVParser parser = new CSVParserBuilder()
				.withSeparator(',')
				.withIgnoreQuotations(false)
				.build();

		CSVReader csvReader = new CSVReaderBuilder(reader)
				.withSkipLines(0)
				.withCSVParser(parser)
				.build();

		String[] line;
		while ((line = csvReader.readNext()) != null) {
			list.add(line);
		}
		reader.close();
		csvReader.close();
		return list;
	}


	private static boolean withinPeriod (String input, String start, String end) throws ParseException {

		boolean withinPeriod = false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date startDT = sdf.parse(start);
		Date endDT = sdf.parse(end);
		Date inputDT = sdf.parse(formatDateTime(input));

		if (inputDT.after(startDT) && inputDT.before(endDT)) {
			withinPeriod = true;
		}

		return withinPeriod;
	}

	private static String formatDateTime (String input) {
		String dateTime = input.substring(0, input.lastIndexOf("."));

		dateTime = dateTime.replaceAll(" ", "T");

		return dateTime;
	}



}
