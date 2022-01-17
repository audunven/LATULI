package csv2tsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author audunvennesland
 *
 */
public class ShipmentItems
{
	public static void processShipmentItemsToTSV(File partiesFolder, String tsvFile) {

			
			String shipmentItemEntity;

			File[] filesInDir = partiesFolder.listFiles();

			BufferedReader br = null;
			BufferedWriter bw = null;
			
			List<String[]> line = new ArrayList<String[]>();



			for (int i = 0; i < filesInDir.length; i++) {


				try {


					br = new BufferedReader(new FileReader(filesInDir[i]));
					bw = new BufferedWriter(new FileWriter(tsvFile));


					System.out.println("Reading file: " + filesInDir[i].getName());

					for (String[] params : line) {


						//adding type
						
						shipmentItemEntity = params[0] + "_" + params[1] + "_shipmentItem";
						
						bw.write(shipmentItemEntity + "\t" + "isType" + "\t" + "ShipmentItem" + "\n");

						//belongsToShipment						
						bw.write(shipmentItemEntity + "\t" + "belongsToShipment" + "\t" + params[0] + "\n");


						//hasLoadingUnit						
						bw.write(shipmentItemEntity + "\t" + "hasLoadingUnit" + "\t" + params[1] + "\n");


						//hasQuantity
						bw.write(shipmentItemEntity + "\t" + "hasQuantity" + "\t" + params[3] + "\n");


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
