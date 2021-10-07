package datavalidation;

public class Test {
	
	public static void main(String[] args) {
		
		String coordinates = "47.56129,7.58107";
		
		String formattedCoordinates = formatCoordinates(coordinates);
		
		
		System.out.println(formattedCoordinates);
		
	}
	
	//45.48602,9.33269
	public static String formatCoordinates (String coordinates) {
		
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}

}
