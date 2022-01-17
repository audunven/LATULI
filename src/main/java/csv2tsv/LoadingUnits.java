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


public class LoadingUnits {

	public static void processLoadingUnitsToTSV (File loadingUnitsFolder, String tsvFile) {

			
			String loadingUnitEntity;
			
			File[] filesInDir = loadingUnitsFolder.listFiles();

			List<String[]> line = new ArrayList<String[]>();

		
			BufferedReader br = null;
			BufferedWriter bw = null;


			for (int i = 0; i < filesInDir.length; i++) {
				
				try {


					br = new BufferedReader(new FileReader(filesInDir[i]));
					bw = new BufferedWriter(new FileWriter(tsvFile));
					

					System.out.println("Reading file: " + filesInDir[i].getName());

					for (String[] params : line) {
						
						loadingUnitEntity = params[0] + "_loadingUnit";
						
						bw.write(loadingUnitEntity + "\t" + "isType" + "\t" + "LoadingUnit" + "\n");


						//hasPackageTypeId						
						bw.write(loadingUnitEntity + "\t" + "hasPackageTypeId" + "\t" + params[1] + "\n");

						
						//hasOrderNumber						
						bw.write(loadingUnitEntity + "\t" + "hasOrderNumber" + "\t" + params[2] + "\n");


						//isModifiedOn
						if (!StringUtilities.convertToDateTime(params[3]).equals("0000-00-00T00:00:00")) {
							bw.write(loadingUnitEntity + "\t" + "isModifiedOn" + "\t" + StringUtilities.convertToDateTime(params[3]) + "\n");
						
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



