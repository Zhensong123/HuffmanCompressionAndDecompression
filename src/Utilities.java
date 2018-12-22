import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Utilities {
	String compressedLine, fileString;
	HashMap<Character, String> huffmanEncodingMap = new HashMap<>();
	HashMap<String, String> huffmanDecodingMap = new HashMap<>();
	ArrayList<Character> fileCharacters = new ArrayList<>();
	ArrayList<Integer> fileCharactersFrequency = new ArrayList<>();
	Comparator<HuffmanNode> huffmanComparator = new HuffmanNodeComparator();
	PriorityQueue<HuffmanNode> huffmanQueue = new PriorityQueue<HuffmanNode>(5, huffmanComparator);
	HuffmanDecodedTree huffmanDecodedTree = new HuffmanDecodedTree();

	public void readEncodedFile(String fileName) {
		File outputFile = new File(fileName);
		Scanner scanner;
		try {
			scanner = new Scanner(outputFile);
			while (scanner.hasNextLine()) {
				String keyCharacter = scanner.next();
				if (keyCharacter.equals("�"))
					break;
				String codeValue = scanner.next();
				huffmanDecodedTree.build(keyCharacter, codeValue);
			}
			scanner.nextLine();
			compressedLine = scanner.nextLine();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCompressedLine() {
		return compressedLine;
	}

	public HashMap<Character, String> getHuffmanEncodingMap() {
		return huffmanEncodingMap;
	}

	public void readInputFile(String fileName) {
		fileString = new String();
		File inputFile = new File(fileName);
		Scanner scanner;
		try {
			scanner = new Scanner(inputFile);
			while (scanner.hasNextLine())
				fileString += (scanner.nextLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getFileString() {
		return fileString;
	}

	public void setCharactersAndFrequencies() {
		for (int i = 0; i < fileString.length(); i++) {
			if (!fileCharacters.contains(fileString.charAt(i))) {
				fileCharacters.add(fileString.charAt(i));
				fileCharactersFrequency.add(1);
			} else {
				int index = fileCharacters.indexOf(fileString.charAt(i));
				int characterFrequency = fileCharactersFrequency.get(index);
				characterFrequency++;
				fileCharactersFrequency.set(index, characterFrequency);
			}
		}
	}

	public ArrayList<Character> getFileCharacters() {
		return fileCharacters;
	}

	public ArrayList<Integer> getFileCharactersFrequency() {
		return fileCharactersFrequency;
	}

	public void buildHuffmanQueue() {
		for (int i = 0; i < fileCharacters.size(); i++) {
			huffmanQueue.add(new HuffmanNode(fileCharactersFrequency.get(i), fileCharacters.get(i)));
		}
	}

	public void buildHuffmanTree() {
		HuffmanTree huffmanTreeBuilder = new HuffmanTree(huffmanQueue);
		huffmanEncodingMap = huffmanTreeBuilder.build();
	}

	public void writeEncodedFile() {
		String encodedOutput = new String();
		for (int i = 0; i < fileString.length(); i++) {
			encodedOutput += huffmanEncodingMap.get(fileString.charAt(i));
		}
		String test;
		BufferedWriter outputFile = null;
		try {
			outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("outputFile.txt")));
			outputFile.write("");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < huffmanEncodingMap.size(); i++) {
			try {
				char mapCharacter = fileCharacters.get(i);
				outputFile.append(mapCharacter + " " + huffmanEncodingMap.get(mapCharacter));
				outputFile.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			outputFile.append('�');
			outputFile.newLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < encodedOutput.length();) {
			if (i + 8 > encodedOutput.length()) {
				test = encodedOutput.substring(i, encodedOutput.length());
				System.out.println(test);
				i = encodedOutput.length();
			} else {
				test = encodedOutput.substring(i, i + 8);
				i += 8;
			}
			char x = (char) Integer.parseInt(test, 2);
			try {
				outputFile.append(x);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			outputFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeDecodedFile() {
		huffmanDecodedTree.decodeFile(compressedLine);
	}

}
