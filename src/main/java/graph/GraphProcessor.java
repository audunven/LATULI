package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GraphProcessor {
	
	public static void main(String[] args) throws IOException {
		
		String csvFile = "./files/GRAPH/cosmos_packed.csv";
		Set<String> vertexSinkSet = new HashSet<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String line;
		String[] params;
		
		while ((line = br.readLine()) != null) {
			
			params = line.split(",");
			
			vertexSinkSet.add(params[0] + params[1]);
			
		}
		
		System.out.println("File " + csvFile + " contains " + vertexSinkSet.size() + " entries");
		
		for (String s : vertexSinkSet) {
			System.out.println(s);
		}
	}

}
