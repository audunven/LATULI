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
		//		String start = "2020-01-01T00:00:00";
		//		String end = "2020-06-30T00:00:00";
		//filterCSVOnPeriod(9, start, end, inputFile, outputFile);
		//filterCSVOnHub(13, hub, inputFile, outputFile);

		//1. Filter XDocLoadingUnit csv files on period (start-end)
		//filterXDocLoadingUnitFiles ("./files/CSV/Audun/split_xdlu", start, end);

		//2. Print files holding relevant consignment ids, loading unit ids and wave ids according to XDocLoadingUnit csv files filtered in (1).
		/*
		 * String filteredXDLUFilesPath = "./files/CSV/Audun/filtered_xdlu_01.2020-07.2020"; 
		 * String loadingUnitFolder = "./files/CSV/Audun/relevant_loading_unit_ids"; 
		 * String consignmentFolder = "./files/CSV/Audun/relevant_consignment_ids"; 
		 * String waveFolder = "./files/CSV/Audun/relevant_wave_ids"; 
		 * printRelevantConLoadWave (filteredXDLUFilesPath, loadingUnitFolder, consignmentFolder, waveFolder);
		 */

		//3. Filter the Consignments.csv using the consignments.txt produced in 2
		/*
		String split_consignments = "./files/CSV/Audun/consignments_split";
		String relevantConsignmentIds = "./files/CSV/Audun/relevant_consignment_ids/consignments.txt";
		filterConsignments(split_consignments, relevantConsignmentIds);
		*/

		//4. Filter the LoadingUnits.csv using the loadingUnits.txt produced in 2
		/*
		String split_loadingUnits = "./files/CSV/Audun/loadingunits_split";
		String relevantLoadingUnitIds = "./files/CSV/Audun/relevant_loading_unit_ids/loadingUnits.txt";
		filterWaves(split_loadingUnits, relevantLoadingUnitIds);
		 */

		//5. Filter the Waves.csv using the waves.txt produced in 2
		/*
		String split_waves = "./files/CSV/Audun/waves_split";
		String relevantWaveIds = "./files/CSV/Audun/relevant_wave_ids/waves.txt";
		filterWaves(split_waves, relevantWaveIds);
		 */

		//6. Get relevant transport ids from filtering the consignment csv according to 2 and write these to relevant_transport_ids
		/*
		String consignments_split = "./files/CSV/Audun/consignments_split";
		String filterPath = "./files/CSV/Audun/relevant_consignment_ids/consignments.txt";
		String transportFolder = "./files/CSV/Audun/relevant_transport_ids";
		printRelevantTransports(consignments_split, filterPath, transportFolder);
		 */

		//7. Filter the TradeItem by using the relevant loading unit ids according to 2
		/*
		String split_tradeItems = "./files/CSV/Audun/tradeitems_split";
		String relevantLoadingUnitIds = "./files/CSV/Audun/relevant_loading_unit_ids/loadingUnits.txt";
		filterTradeItems(split_tradeItems, relevantLoadingUnitIds);
		 */
		

		//8. Filter the DGR by using the relevant loading unit ids according to 2
		/*
		String split_dgr = "./files/CSV/Audun/drgs_split";
		String relevantLoadingUnitIds = "./files/CSV/Audun/relevant_loading_unit_ids/loadingUnits.txt";
		filterDangerousGoods(split_dgr, relevantLoadingUnitIds);
		 */
		
		//9. Get relevant shipment item ids from filtering the loading unit csv according to 2 and write these to relevant_shipment_item_ids
		/*
		String split_shipmentItems = "./files/CSV/Audun/shipmentitems_split";
		String relevantLoadingUnitIds = "./files/CSV/Audun/relevant_loading_unit_ids/loadingUnits.txt";
		String shipmentIdsFolder = "./files/CSV/Audun/relevant_shipment_ids";		
		printRelevantShipmentIds (split_shipmentItems, relevantLoadingUnitIds, shipmentIdsFolder);
		 */

		//10. Filter the ShipmentItem by using the relevant shipment ids according to 9
		/*
		String split_shipmentItems = "./files/CSV/Audun/shipmentitems_split";
		String relevantShipmentItemIds = "./files/CSV/Audun/relevant_shipment_ids/shipmentIds.txt";
		filterShipmentItems(split_shipmentItems, relevantShipmentItemIds);
		 */
		
		//11. Filter the Shipment by using the relevant shipment ids according to 9
		String split_shipments = "./files/CSV/Audun/shipments_split";
		String relevantShipmentItemIds = "./files/CSV/Audun/relevant_shipment_ids/shipmentIds.txt";
		filterShipments(split_shipments, relevantShipmentItemIds);
		
		//12. Filter the Transport by using the relevant transport ids according to 6
		

		//12. Produce triples for the entire Parties.csv

		//13. Produce triples for the entire HubReconstructionLocation.csv


	}
	
	public static void filterShipments (String inputFolder, String relevantIds) throws ParseException {

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

		System.out.println("shipmentItemIds contains " + shipmentItemIds.size() + " entries.");

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
				bw = new BufferedWriter(new FileWriter(filesInDir[i].getPath() + "_new"));
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
	
	public static void filterShipmentItems (String inputFolder, String relevantIds) throws ParseException {

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

		System.out.println("shipmentItemIds contains " + shipmentItemIds.size() + " entries.");

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
				bw = new BufferedWriter(new FileWriter(filesInDir[i].getPath() + "_new"));
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
	
	public static void filterDangerousGoods (String inputFolder, String relevantIds) throws ParseException {

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

		System.out.println("loadingUnitIds contains " + loadingUnitIds.size() + " entries.");

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
				bw = new BufferedWriter(new FileWriter(filesInDir[i].getPath() + "_new"));
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
	
	public static void filterTradeItems (String inputFolder, String relevantIds) throws ParseException {

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

		System.out.println("loadingUnitIds contains " + loadingUnitIds.size() + " entries.");

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
				bw = new BufferedWriter(new FileWriter(filesInDir[i].getPath() + "_new"));
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
	
	public static void filterXDocLoadingUnits (String inputFolder, String startDateTime, String endDateTime) throws ParseException {

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
				bw = new BufferedWriter(new FileWriter(filesInDir[i].getPath() + "_new"));
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

	public static void filterLoadingUnits (String inputFolder, String relevantIds) throws ParseException {

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

		System.out.println("loadingUnitIds contains " + loadingUnitIds.size() + " entries.");

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
				bw = new BufferedWriter(new FileWriter(filesInDir[i].getPath() + "_new"));
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
	
	public static void filterWaves (String inputFolder, String relevantIds) throws ParseException {

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

		System.out.println("waveIds contains " + waveIds.size() + " entries.");

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
				bw = new BufferedWriter(new FileWriter(filesInDir[i].getPath() + "_new"));
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


	public static void filterConsignments (String inputFolder, String relevantIds) throws ParseException {

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

		System.out.println("consignmentIds contains " + consignmentIds.size() + " entries.");

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
				bw = new BufferedWriter(new FileWriter(filesInDir[i].getPath() + "_new"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nReading file: " + filesInDir[i].getName());

			try {
				while ((line = br.readLine()) != null) {

					params = line.split(",");

					if (consignmentIds.contains(params[0])) {

						System.out.println("Writing line to " + filesInDir[i].getPath() + "_new");

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
	
	public static void printRelevantShipmentIds (String inputFolder, String filterPath, String shipmentsFolder) {

		Set<String> shipmentIds = new HashSet<String>();
		String shipmentIdsFile = shipmentsFolder + "/shipmentIds.txt";

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

		System.out.println("loadingUnitIds contains " + loadingUnitIds.size() + " entries.");

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


	public static void printRelevantTransports (String inputFolder, String filterPath, String transportFolder) {

		Set<String> transportIds = new HashSet<String>();
		String transportIdFile = transportFolder + "/transports.txt";

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

		System.out.println("consignmentIds contains " + consignmentIds.size() + " entries.");

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

	public static void printRelevantConLoadWave (String inputFolder, String loadingUnitFolder, String consignmentFolder, String waveFolder) throws ParseException {


		Set<String> consignmentIds = new HashSet<String>();
		Set<String> waveIds = new HashSet<String>();
		Set<String> loadingUnitIds = new HashSet<String>();

		//files for holding relevant loadingUnitIDs, consignmentIDs and WaveIDs
		String loadingUnitIdFile = loadingUnitFolder + "/loadingUnits.txt";
		String consignmentIdFile = consignmentFolder + "/consignments.txt";
		String waveIdFile = waveFolder + "/waves.txt";


		int nullCounter = 0;

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

			System.out.println("Temp size loading unit list (" + filesInDir[i].getName() + "): " + loadingUnitIds.size());
			System.out.println("Temp size consignment list (" + filesInDir[i].getName() + "): " + consignmentIds.size());
			System.out.println("Temp size wave list (" + filesInDir[i].getName() + "): " + waveIds.size());
			System.out.println("Number of invalid updatedOn in " + filesInDir[i].getName() + ": " + nullCounter);

		}

		System.out.println("Final size loading unit list: " + loadingUnitIds.size());
		System.out.println("Final size consignment list: " + consignmentIds.size());
		System.out.println("Final size wave list: " + waveIds.size());
		System.out.println("Number of invalid updatedOn: " + nullCounter);


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
