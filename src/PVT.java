import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;




public class PVT {

	static ArrayList<String> words;
	static Random rand;
	static String[] paired_words;
	
	public static void main(String[] args) throws IOException {

		FileReader reader = new FileReader("./all_4_letter_Words.txt");
		FileWriter writer = new FileWriter("./paired_4letter.txt");


		BufferedReader bufferedReader = new BufferedReader(reader);

		String word;
		words = new ArrayList<String>(); 

		while ((word = bufferedReader.readLine()) != null) {
			words.add(word);
		}
		reader.close();

		rand = new Random();
		
		Collections.shuffle(words, new Random());
		
		for (int i = 0; i < 200; i++) {
			System.out.println(words.get(i)+ " "+
					randomInteger(1,4 , rand));
		}




	}

	private static int randomInteger(int Start, int End, Random aRandom) {
		if (Start > End) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		// get the range, casting to long to avoid overflow problems
		long range = (long) End - (long) Start + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * aRandom.nextDouble());
		int randomNumber = (int) (fraction + Start);
		return randomNumber;
	}


}
