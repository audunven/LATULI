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
public class Consignments {

	//test
	public static void main(String[] args) {

		String folder = "./files/CSV/Audun/_FILTER_HUB/consignments_split_filtered";
		String outputFile = "./files/CSV/Audun/consignmentsTest.tsv";
		processConsignmentsToTSV(new File(folder), outputFile);


	}

	public static void processConsignmentsToTSV (File consignmentFolder, String tsvFile) {

		String consignmentEntity;

		File[] filesInDir = consignmentFolder.listFiles();

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
					consignmentEntity = params[0];

					bw.write(consignmentEntity + "\t" + "isType" + "\t" + "Consignment" + "\n");


					//includesTransport
					if (!params[3].equals("NULL")) {

						bw.write(consignmentEntity + "\t" + "includesTransport" + "\t" + params[3] + "\n");

					}

					//isProcessedByWave
					if (!params[18].equals("NULL")) {								

						bw.write(consignmentEntity + "\t" + "isProcessedByWave" + "\t" + params[18] + "\n");
					}

					//hasCarrierParty						
					bw.write(consignmentEntity + "\t" + "hasCarrierParty" + "\t" + params[11] + "\n");

					//hasConsignorParty						
					bw.write(consignmentEntity + "\t" + "hasConsignorParty" + "\t" + params[14] + "\n");


					//hasConsigneeParty					
					bw.write(consignmentEntity + "\t" + "hasConsigneeParty" + "\t" + params[17] + "\n");


					//hasTaskClosedOn
					if (!StringUtilities.convertToDateTime(params[23]).equals("0000-00-00T00:00:00")) {

						bw.write(consignmentEntity + "\t" + "hasTaskClosedOn" + "\t" + StringUtilities.convertToDateTime(params[23]) + "\n");
					}

					//hasConsignmentId						
					bw.write(consignmentEntity + "\t" + "hasConsignmentId" + "\t" + params[0] + "\n");


					//isConsignmentType						
					bw.write(consignmentEntity + "\t" + "isConsignmentType" + "\t" + params[6] + "\n");


					//isFullPalletConsignment						
					bw.write(consignmentEntity + "\t" + "isFullPalletConsignment" + "\t" + params[32] + "\n");


					//hasQttBoxes						
					bw.write(consignmentEntity + "\t" + "hasQttBoxes" + "\t" + params[33] + "\n");


					//hasQttPallets						
					bw.write(consignmentEntity + "\t" + "hasQttPallets" + "\t" + params[34] + "\n");


					//hasQttReconstructedPallets						
					bw.write(consignmentEntity + "\t" + "hasQttReconstructedPallets" + "\t" + params[35] + "\n");


					//hasQttReconstructedParcels						
					bw.write(consignmentEntity + "\t" + "hasQttReconstructedParcels" + "\t" + params[36] + "\n");


					//hasTotalConsignmentVolume						
					bw.write(consignmentEntity + "\t" + "hasTotalConsignmentVolume" + "\t" + params[40] + "\n");


					//hasTotalConsignmentWeight
					bw.write(consignmentEntity + "\t" + "hasTotalConsignmentWeight" + "\t" + params[41] + "\n");


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

}
