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
public class HubReconstructionLocations
{

	public static void processHubReconstructionLocationsToTSV (File hubReconstructionLocationsFolder, String tsvFile) {

			String hubReconstructionLocationEntity;

			File[] filesInDir = hubReconstructionLocationsFolder.listFiles();

			BufferedReader br = null;
			BufferedWriter bw = null;

			List<String[]> line = new ArrayList<String[]>();


			for (int i = 0; i < filesInDir.length; i++) {


				try {


					br = new BufferedReader(new FileReader(filesInDir[i]));
					bw = new BufferedWriter(new FileWriter(tsvFile));



					System.out.println("Reading file: " + filesInDir[i].getName());

					for (String[] params : line) {

						hubReconstructionLocationEntity = params[0] + "_hubReconstructionLocation";
						
						bw.write(hubReconstructionLocationEntity + "\t" + "isType" + "\t" + "HubReconstructionLocation" + "\n");
						
						//hasHubParty						
						bw.write(hubReconstructionLocationEntity + "\t" + "hasHubParty" + "\t" + params[3] + "\n");


						//hasHubReconstructionLocation
						bw.write(hubReconstructionLocationEntity + "\t" + "hasHubReconstructionLocation" + "\t" + params[0] + "\n");

						
						//hasAdditionalPartyId
						bw.write(hubReconstructionLocationEntity + "\t" + "hasAdditionalPartyId" + "\t" + params[1] + "\n");

						
						//hasLaneId
						bw.write(hubReconstructionLocationEntity + "\t" + "hasLaneId" + "\t" + params[4] + "\n");


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
