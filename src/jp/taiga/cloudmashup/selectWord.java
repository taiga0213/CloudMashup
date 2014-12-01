package jp.taiga.cloudmashup;

import java.util.ArrayList;
import java.util.Random;

public class selectWord {

	public int[] select(ArrayList<String> list) {
		int wordA = getRandom(list.size());
		int wordB = getRandom(list.size());
		
		while (true) {
			if(wordA == wordB){
				wordB = getRandom(list.size());
			}else{
				break;
			}
		}
		
		int[] word = new int[2];
		word[0] = wordA;
		word[1] = wordB;
		return word;
	}

	public int getRandom(int size) {
		Random random = new Random();
		int number = random.nextInt(size);
		return number;
	}

}
