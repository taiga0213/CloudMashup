package jp.taiga.cloudmashup;

import java.util.ArrayList;
import java.util.Random;

public class SelectWord {

	public int[] select(ArrayList<String> list) {
		int wordA = getRandom(list.size());
		int wordB = getRandom(list.size());
		int wordC = getRandom(list.size());
		
		while (true) {
			if(wordA == wordB){
				wordB = getRandom(list.size());
			}else if(wordA == wordC || wordB == wordC){
				wordC = getRandom(list.size());
			}else {
				break;
			}
		}
		
		int[] word = new int[3];
		word[0] = wordA;
		word[1] = wordB;
		word[2] = wordC;
		return word;
	}

	public int getRandom(int size) {
		Random random = new Random();
		int number = random.nextInt(size);
		return number;
	}

}
