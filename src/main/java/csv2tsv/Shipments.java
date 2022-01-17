package csv2tsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class Shipments {

	

	public static void processShipmentsToTSV (File shipmentsFolder, String tsvFile) {

			
			String shipmentEntity;

			File[] filesInDir = shipmentsFolder.listFiles();

			BufferedReader br = null;
			BufferedWriter bw = null;

			
			List<String[]> line = new ArrayList<String[]>();

			for (int i = 0; i < filesInDir.length; i++) {

				try {

					br = new BufferedReader(new FileReader(filesInDir[i]));
					bw = new BufferedWriter(new FileWriter(tsvFile));


					System.out.println("Reading file: " + filesInDir[i].getName());
					
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
						bw.write(shipmentEntity + "\t" + "hasShipperParty" + "\t" + params[8] + "\n");


						//hasReceiverParty						
						bw.write(shipmentEntity + "\t" + "hasReceiverParty" + "\t" + params[11] + "\n");
						
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
	

}//end class