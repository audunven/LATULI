package csvfiltering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
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

		//		String inputFile = "./files/CSV/Truls/split/filtered_split/XDocLoadingUnits_short_1000.csv";
		//		String outputFile = "./files/CSV/Truls/split/filtered_split/XDocLoadingUnits_short_1000_filteredOnHub.csv";
		//		String hub = "C042";
		//

		//filterCSVOnPeriod(9, start, end, inputFile, outputFile);
		//filterCSVOnHub(13, hub, inputFile, outputFile);

		String startDateTime = "2020-01-01T00:00:00";
		String endDateTime = "2020-06-30T00:00:00";

		String xdlu_folder_in = "./files/CSV/Audun/split_xdlu";
		String xdlu_folder_out = "./files/CSV/Audun/filtered_xdlu_01.2020-07.2020";
		String loadingUnitIdFile = "./files/CSV/Audun/relevant_loading_unit_ids/loadingUnitIds.txt"; 
		String consignmentIdFile = "./files/CSV/Audun/relevant_consignment_ids/consignmentIds.txt"; 
		String waveIdFile = "./files/CSV/Audun/relevant_wave_ids/waveIds.txt"; 
		String shipmentIdFile = "./files/CSV/Audun/relevant_shipment_ids/shipmentIds.txt";
		String transportIdFile = "./files/CSV/Audun/relevant_transport_ids/transportIds.txt";
		String consignments_folder_in = "./files/CSV/Audun/consignments_split";
		String consignments_folder_filtered = "./files/CSV/Audun/consignments_split_filtered";
		String loadingUnits_folder_in = "./files/CSV/Audun/loadingunits_split";
		String loadingUnits_folder_filtered = "./files/CSV/Audun/loadingunits_split_filtered";
		String waves_folder_in = "./files/CSV/Audun/waves_split";
		String waves_folder_filtered = "./files/CSV/Audun/waves_split_filtered";
		String tradeItems_folder_in = "./files/CSV/Audun/tradeitems_split";
		String tradeItems_folder_filtered = "./files/CSV/Audun/tradeitems_split_filtered";
		String dgr_folder_in = "./files/CSV/Audun/drgs_split";
		String dgr_folder_filtered = "./files/CSV/Audun/drgs_split";
		String shipmentItems_folder_in = "./files/CSV/Audun/shipmentitems_split";
		String shipmentItems_folder_filtered = "./files/CSV/Audun/shipmentitems_split_filtered";
		String shipments_folder_in = "./files/CSV/Audun/shipments_split";
		String shipments_folder_filtered = "./files/CSV/Audun/shipments_split_filtered";
		String transports_folder_in = "./files/CSV/Audun/transports_split";
		String transports_folder_filtered = "./files/CSV/Audun/transports_split_filtered";

		filterXDocLoadingUnits (xdlu_folder_in, xdlu_folder_out, startDateTime, endDateTime);

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

		//12. Produce triples for the entire Parties.csv

		//13. Produce triples for the entire HubReconstructionLocation.csv


	}

	public static void filterTransports (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

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

	public static void filterShipments (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

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

	public static void filterShipmentItems (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

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

	public static void filterDangerousGoods (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

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

	public static void filterTradeItems (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

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

	public static void filterXDocLoadingUnits (String inputFolder, String filteredFolder, String startDateTime, String endDateTime) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());


			try {
				while ((line = br.readLine()) != null) {

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

	public static void filterLoadingUnits (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

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

	public static void filterWaves (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

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


	public static void filterConsignments (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

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

	public static void printRelevantShipmentIds (String inputFolder, String filterPath, String shipmentIdsFile) {

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


	public static void printRelevantTransports (String inputFolder, String filterPath, String transportIdFile) {

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

	public static void printRelevantConLoadWave (String inputFolder, String loadingUnitIdFile, String consignmentIdFile, String waveIdFile) throws ParseException {


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

				System.out.println("\nReading file: " + filesInDir[i].getName());

				while ((line = br.readLine()) != null) {

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






	public static void filterCSVOnPeriod(int csvFieldNumber, String startPeriod, String endPeriod, String inputFile, String outputFile) {

		String[] params = null;

		String line;		

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				if (withinPeriod(params[csvFieldNumber], startPeriod, endPeriod)) {

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

	public static void filterCSVOnHub(int csvFieldNumber, String hub, String inputFile, String outputFile) {

		String[] params = null;

		String line;		

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				if (params[csvFieldNumber].equals(hub)) {

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

	public static List<String[]> oneByOne(Reader reader) throws Exception {
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
