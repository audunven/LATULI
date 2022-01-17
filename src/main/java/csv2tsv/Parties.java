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
public class Parties {
	
	//test
	public static void main(String[] args) {
		
		String folder = "./files/CSV/Audun/_FILTER_HUB/parties";
		String outputFile = "./files/CSV/Audun/partiesTest.tsv";
		processPartiesToTSV(new File(folder), outputFile);
		
		
	}

	public static void processPartiesToTSV(File partiesFolder, String tsvFile) {

			
			String partyEntity;

			File[] filesInDir = partiesFolder.listFiles();

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

						//adding types
						
						partyEntity = params[2] + "_party";
						
						bw.write(partyEntity + "\t" + "isType" + "\t" + "Party" + "\n");

						//hasAdditionalPartyId
						bw.write(partyEntity + "\t" + "hasAdditionalPartyId" + "\t" + params[0] + "\n");

						
						//hasGln
						bw.write(partyEntity + "\t" + "hasGln" + "\t" + params[1] + "\n");

						
						//hasHashCode
						bw.write(partyEntity + "\t" + "hasHashCode" + "\t" + params[2] + "\n");

						
						//hasCountryCode
						bw.write(partyEntity + "\t" + "hasCountryCode" + "\t" + params[7] + "\n");

						
						//hasCity
						bw.write(partyEntity + "\t" + "hasCity" + "\t" + params[8] + "\n");

						
						//hasPostalCode
						bw.write(partyEntity + "\t" + "hasPostalCode" + "\t" + params[9] + "\n");

						
						//isHub
						bw.write(partyEntity + "\t" + "isHub" + "\t" + params[11] + "\n");

						
						//isShipper
						bw.write(partyEntity + "\t" + "isShipper" + "\t" + params[12] + "\n");

						
						//isCarrier
						bw.write(partyEntity + "\t" + "isCarrier" + "\t" + params[13] + "\n");

						
						//isConsignor
						bw.write(partyEntity + "\t" + "isConsignor" + "\t" + params[14] + "\n");

						
						//hasCoordinates
						bw.write(partyEntity + "\t" + "hasCoordinates" + "\t" + params[19] + "\n");

	

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
