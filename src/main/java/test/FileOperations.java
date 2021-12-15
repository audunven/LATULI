package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOperations {
	
	public static void main(String[] args) throws IOException {
		
		String CSV_folder = "./files/CSV/Truls/split/XDocLoadingUnits";
		File folder = new File(CSV_folder);
		File[] filesInDir = folder.listFiles();
		
		File path;
		Scanner scanner;

		for (int i = 0; i < filesInDir.length; i++) {
			
			
			path = filesInDir[i];
			scanner = new Scanner(path);
			ArrayList<String> coll = new ArrayList<String>();
			
			scanner.nextLine();
			while (scanner.hasNextLine()) {
			    String line = scanner.nextLine();
			    coll.add(line);
			}

			scanner.close();

			FileWriter writer = new FileWriter(path);
			
			for (String line : coll) {
			    writer.write(line + "\n");
			}

			writer.close();
		}
		
	}
	
	

}
