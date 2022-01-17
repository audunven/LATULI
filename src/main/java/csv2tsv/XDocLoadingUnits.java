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
public class XDocLoadingUnits
{

	public static void processXDocLoadingUnitsToTSV (File xdluFolder, String tsvFile) {

		String xDocLoadingUnitEntity;

		File[] filesInDir = xdluFolder.listFiles();

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
					xDocLoadingUnitEntity = params[0] + "_xDocLoadingUnit";

					bw.write(xDocLoadingUnitEntity + "\t" + "isType" + "\t" + "XDocLoadingUnit" + "\n");

					//hasHubReconstructionLocation
					bw.write(xDocLoadingUnitEntity + "\t" + "hasHubReconstructionLocation" + "\t" + params[17] + "\n");

					//hasLoadingUnit
					bw.write(xDocLoadingUnitEntity + "\t" + "hasLoadingUnit" + "\t" + params[7] + "\n");

					//hasInboundParentLoadingUnit
					bw.write(xDocLoadingUnitEntity + "\t" + "hasInboundParentLoadingUnit" + "\t" + params[41] + "\n");

					//hasOutboundParentLoadingUnit
					bw.write(xDocLoadingUnitEntity + "\t" + "hasOutboundParentLoadingUnit" + "\t" + params[42] + "\n");

					//hasInboundConsignment
					bw.write(xDocLoadingUnitEntity + "\t" + "hasInboundConsignment" + "\t" + params[11] + "\n");

					//hasOutboundConsignment
					bw.write(xDocLoadingUnitEntity + "\t" + "hasOutboundConsignment" + "\t" + params[12] + "\n");

					//hasHubParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasHubParty" + "\t" + params[15] + "\n");

					//hasShipperParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasShipperParty" + "\t" + params[23] + "\n");

					//hasReceiverParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasReceiverParty" + "\t" + params[26] + "\n");

					//hasCarrierParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasCarrierParty" + "\t" + params[29] + "\n");

					//hasConsignorParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasConsignorParty" + "\t" + params[32] + "\n");

					//hasConsigneeParty
					bw.write(xDocLoadingUnitEntity + "\t" + "hasConsigneeParty" + "\t" + params[35] + "\n");

					//isProcessedByWave
					bw.write(xDocLoadingUnitEntity + "\t" + "isProcessedByWave" + "\t" + params[36] + "\n");

					//hasInternalId
					bw.write(xDocLoadingUnitEntity + "\t" + "hasInternalId" + "\t" + params[0] + "\n");


					//hasPresortScanOn
					if (!StringUtilities.convertToDateTime(params[1]).equals("0000-00-00T00:00:00")) {
						bw.write(xDocLoadingUnitEntity + "\t" + "hasPresortScanOn" + "\t" + StringUtilities.convertToDateTime(params[1]) + "\n");

					}

					//hasReconstructedScanOn
					if (!StringUtilities.convertToDateTime(params[2]).equals("0000-00-00T00:00:00")) {
						bw.write(xDocLoadingUnitEntity + "\t" + "hasReconstructedScanOn" + "\t" + StringUtilities.convertToDateTime(params[2]) + "\n");

					}

					//hasFinishedScanOn
					if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
						bw.write(xDocLoadingUnitEntity + "\t" + "hasFinishedScanOn" + "\t" + StringUtilities.convertToDateTime(params[3]) + "\n");

					}

					//hasVolume
					bw.write(xDocLoadingUnitEntity + "\t" + "hasVolume" + "\t" + params[5] + "\n");

					//hasWeight
					bw.write(xDocLoadingUnitEntity + "\t" + "hasWeight" + "\t" + params[6] + "\n");

					//hasReconstructionType
					bw.write(xDocLoadingUnitEntity + "\t" + "hasReconstructionType" + "\t" + params[37] + "\n");

					//isSplitShipment
					bw.write(xDocLoadingUnitEntity + "\t" + "isSplitShipment" + "\t" + params[40] + "\n");


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
