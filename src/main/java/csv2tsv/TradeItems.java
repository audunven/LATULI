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
public class TradeItems
{

	public static void processTradeItemsToTSV (File tradeItemsFolder, String tsvFile) {

			
			String tradeItemEntity;

			File[] filesInDir = tradeItemsFolder.listFiles();

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
						tradeItemEntity = params[0] + "-" + params[1] + "_tradeItem";
						
						bw.write(tradeItemEntity + "\t" + "isType" + "\t" + "TradeItem" + "\n");

						//belongsToShipment
						bw.write(tradeItemEntity + "\t" + "belongsToShipment" + "\t" + params[0] + "\n");
						
						//hasLoadingUnit
						bw.write(tradeItemEntity + "\t" + "hasLoadingUnit" + "\t" + params[1] + "\n");

						//hasGtin
						bw.write(tradeItemEntity + "\t" + "hasGtin" + "\t" + params[2] + "\n");
					
						//hasSupplierQuantity
						bw.write(tradeItemEntity + "\t" + "hasSupplierQuantity" + "\t" + params[6] + "\n");
						
						//hasCustomerQuantity
						bw.write(tradeItemEntity + "\t" + "hasCustomerQuantity" + "\t" + params[7] + "\n");
						
						//hasSupplierProductDescription
						bw.write(tradeItemEntity + "\t" + "hasSupplierProductDescription" + "\t" + params[8] + "\n");
						
						//hasSupplierProductId
						bw.write(tradeItemEntity + "\t" + "hasSupplierProductId" + "\t" + params[10] + "\n");

						
						//isModifiedOn
						if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
							bw.write(tradeItemEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[12]) + "\n");
						}
						
						//hasHandlingInstructions
						bw.write(tradeItemEntity + "\t" + "hasHandlingInstructions" + "\t" + params[16] + "\n");
	

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
