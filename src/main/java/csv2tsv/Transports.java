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
public class Transports {

	public static void processTransportsToTSV (File transportsFolder, String tsvFile) {


		String transportEntity;

		File[] filesInDir = transportsFolder.listFiles();

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

					transportEntity = params[0] + "_transport";

					//adding types						
					bw.write(transportEntity + "\t" + "isType" + "\t" + "Transport" + "\n");


					//hasHubParty
					bw.write(transportEntity + "\t" + "hasHubParty" + "\t" + params[8] + "\n");


					//hasTransportId
					bw.write(transportEntity + "\t" + "hasTransportId" + "\t" + params[0] + "\n");

					//hasExpectedArrival
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasExpectedArrival" + "\t" + StringUtilities.convertToDateTime(params[2]) + "\n");
					}

					//hasEffectiveArrival
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasEffectiveArrival" + "\t" + StringUtilities.convertToDateTime(params[3]) + "\n");
					}

					//hasTransportName
					bw.write(transportEntity + "\t" + "hasTransportName" + "\t" + params[9] + "\n");

					//isModifiedOn
					if (!StringUtilities.convertToDateTime(params[10]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[10]) + "\n");

					}

					//isTransportType
					bw.write(transportEntity + "\t" + "isTransportType" + "\t" + params[11] + "\n");


					//hasWavePlannedOn
					if (!StringUtilities.convertToDateTime(params[12]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWavePlannedOn" + "\t" + StringUtilities.convertToDateTime(params[12]) + "\n");

					}

					//hasWaveReleasedOn
					if (!StringUtilities.convertToDateTime(params[13]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWaveReleasedOn" + "\t" + StringUtilities.convertToDateTime(params[13]) + "\n");

					}

					//hasWaveClosedOn
					if (!StringUtilities.convertToDateTime(params[14]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWaveClosedOn" + "\t" + StringUtilities.convertToDateTime(params[14]) + "\n");

					}

					//hasWaveStartProcessingOn
					if (!StringUtilities.convertToDateTime(params[15]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWaveStartProcessingOn" + "\t" + StringUtilities.convertToDateTime(params[15]) + "\n");

					}

					//hasWaveEndProcessingOn
					if (!StringUtilities.convertToDateTime(params[16]).equals("0000-00-00T00:00:00")) {
						bw.write(transportEntity + "\t" + "hasWaveEndProcessingOn" + "\t" + StringUtilities.convertToDateTime(params[16]) + "\n");

					}

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
