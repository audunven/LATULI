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
public class Waves {

	public static void processWavesToTSV (File wavesFolder, String tsvFile) {


		String waveEntity;

		File[] filesInDir = wavesFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;

		List<String[]> line = new ArrayList<String[]>();


		for (int i = 0; i < filesInDir.length; i++) {

			try {


				br = new BufferedReader(new FileReader(filesInDir[i]));
				bw = new BufferedWriter(new FileWriter(tsvFile));


				System.out.println("Reading file: " + filesInDir[i].getName());

				for (String[] params : line) {


					//isType					
					waveEntity = params[0] + "_wave";
					bw.write(waveEntity + "\t" + "isType" + "\t" + "Wave" + "\n");



					//hasHubParty				
					bw.write(waveEntity + "\t" + "hasHubParty" + "\t" + params[6] + "\n");


					//hasWaveid
					bw.write(waveEntity + "\t" + "hasWaveid" + "\t" + params[0] + "\n");

					//isPlannedOn
					if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isPlannedOn" + "\t" + StringUtilities.convertToDateTime(params[1]) + "\n");

					}

					//isReleasedOn
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isReleasedOn" + "\t" + StringUtilities.convertToDateTime(params[2]) + "\n");

					}

					//isModifiedOn
					if (!StringUtilities.convertToDateTime(params[7]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[7]) + "\n");

					}

					//isClosedOn
					if (!StringUtilities.convertToDateTime(params[8]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isClosedOn" + "\t" + StringUtilities.convertToDateTime(params[8]) + "\n");

					}

					//isCreatedOn
					if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "isCreatedOn" + "\t" + StringUtilities.convertToDateTime(params[10]) + "\n");

					}

					//hasWaveStartProcessingOn
					if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "hasWaveStartProcessingOn" + "\t" + StringUtilities.convertToDateTime(params[13]) + "\n");

					}

					//hasWaveEndProcessingOn
					if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
						bw.write(waveEntity + "\t" + "hasWaveEndProcessingOn" + "\t" + StringUtilities.convertToDateTime(params[14]) + "\n");

					}

					//hasQttTrailers
					bw.write(waveEntity + "\t" + "hasQttTrailers" + "\t" + params[15] + "\n");


					//hasQttBoxes
					bw.write(waveEntity + "\t" + "hasQttBoxes" + "\t" + params[16] + "\n");

					//hasQttPallets
					bw.write(waveEntity + "\t" + "hasQttPallets" + "\t" + params[17] + "\n");

					//hasQttBoxesProcessed
					bw.write(waveEntity + "\t" + "hasQttBoxesProcessed" + "\t" + params[18] + "\n");

					//hasQttPalletsBuilt
					bw.write(waveEntity + "\t" + "hasQttPalletsBuilt" + "\t" + params[19] + "\n");

					//hasQttTasks
					bw.write(waveEntity + "\t" + "hasQttTasks" + "\t" + params[20] + "\n");

					//hasQttShipments
					bw.write(waveEntity + "\t" + "hasQttShipments" + "\t" + params[21] + "\n");


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
