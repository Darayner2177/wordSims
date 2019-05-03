import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
/*
 * David Rayner
 * Last Modified 04/13/18 @ 10:55 p.m.
 * 
 * Word Clusters
 * Creates hash table storing words and embedding provided in glove file
 * Creates a min heap inserting words based on similarity, larger similarities are on top of heap
 * Extracting from heap 'n' times creating a dsf with those words
 * Creates a dsf of cluster sets, using union and path compression functions to create grouping similar words
 * Printing the 'm' largest clusters in the dsf
 *
 */
public class HashTable {
	private hashNode [] H;
	public int count;
	
   
	public HashTable(int n){ // Initialize all lists to null
      H = new hashNode[n];
      for(int i=0;i<n;i++)
      H[i] = null;
   }
	/*
	 * Increases Hash by odd number.
	 * Traverse through old Hash Table assigning positions and nodes to new HashTable.
	 * Assign pointer to next to new given hash position.
	 * New hash and position assigned to previous.
	 * old array Hash is finally assigned the array new Hash.
	 * 
	 */
	public void increaseHash(){
		hashNode[] newH = new hashNode[H.length * 2 + 1];
		for(int i = 0; i < H.length; i++){
			for(hashNode temp = H[i]; temp != null;){
				 hashNode newHash = temp.next;
				 int pos = h(temp.word, newH.length);
				 temp.next = newH[pos];
				 newH[pos] = temp;
				 temp = newHash;
				 
			 }
		}
		H = newH;

	}
	/*
	 * Determines insertion position of given String and embedding
	 * Computes the size (load factor) to determine if Hash length should be increased
	 * if load factor is greater than 2 call increase hash method to increase
	 * otherwise, find position of hash
	 * assign the node to the insertion position
	 * 
	 */
	public void insert(String s, float[] embed){
		float size = (float) count / H.length;
		if(size > 2.0){
			increaseHash();
		}
		else{
		int pos = h(s);
		H[pos] = new hashNode(s,embed,H[pos]);
		}
	}
	/*
	 * Computes the cosine distance of two string, to determine similarity.
	 * search to find strings.
	 * by cosine distance formula, summation of dot product between embedding 1 and embedding 2.
	 * magnitude, embedding 1 and embedding 2 squared, summation to length of array.
	 * return the summation of dot product, divided by the square root of mantiude1 times square root of magnitude2.
	 * 
	 */
	public float sim(String first, String second){
		hashNode s1 = search(first);
		hashNode s2 = search(second);
		float summation = 0;
		float magnitude1 = 0;
		float magnitude2 = 0;
		for(int i = 0; i < s1.embedding.length; i++){
			summation += ((s1.embedding[i] * s2.embedding[i]));
			magnitude1 += Math.pow(s1.embedding[i], 2);
			magnitude2 += Math.pow(s2.embedding[i], 2);
		}
		return (float) (summation/((Math.sqrt(magnitude1)) * Math.sqrt(magnitude2)));
	}
	/*
	 * Searches through Hash table, finds word
	 * Find position of given string
	 * Traverse through the Hash table compares each word in each node to given string
	 * once found the reference to the node is returned.
	 * otherwise, return null
	 *
	 */
	public hashNode search(String k){
		int pos = h(k);
		for(hashNode t = H[pos]; t!=null; t=t.next){
			if(t.word.equals(k)){
				return t;
			}
		}
		return null;
		
	}
	
   private int h(String S){
   int h = 0;
   for(int i=0;i<S.length();i++)
      h = (h*27+S.charAt(i))%H.length;
   		return h; 
   }
   
   
   private int h(String s2, int length){
	   int h = 0;
	   for(int i=0;i<s2.length();i++)
	      h = (h*27+s2.charAt(i))%length;
	   		return h; 
   }
   
   
   
   private void printList(hashNode x){
	   for(;x!=null; x=x.next){
		   System.out.print(x.word + " ");
	   }
	   System.out.println();
	   
   }
 
   public void print(){
	   for(int i = 0; i < H.length; i++){
		   System.out.print("H["+i+"]: ");
		   printList(H[i]);
	   }
	   System.out.println();
   }
   
   /*
    * Creates dsf sets based on word similarities
    * extracts minimum from heap 'n' times, creating word cluster sets
    * Traverses word array to find position of word
    * Then takes union function to adjoin words, based on the larger set
    *
    */
   
   public static void createSet(int n, DSF dS, String[] wordPair, Heap heap){
	   int temp1 = 0;
       int temp2 = 0;
       for(int k = 0; k < n; k++){
       	HeapNode exMin = heap.extractMin();
       	for(int t = 0; t < wordPair.length; t++){
       		if(exMin.word0.equals(wordPair[t])){
       			temp1 = t;
       		}
       		if(exMin.word1.equals(wordPair[t])){
	        		temp2 = t;
       		}
       	}
       	dS.union_by_size(temp1, temp2);
       }
       dS.print();
   }
   /*
    * Finds the largest 'm' sets in the dsf cluster 
    * Root positions in array are stores in array
    * Traverse dsf set array and root array, compare and store index of root in new array
    * boolean to check for duplicates 
    * Traverse through m times find root position and print out clusters
    * 
    */
   
   public static void largestSet(int m, String[] wordPair, int[] set, int[] root,  DSF disFor){
	   int[] rootPos = new int[root.length];
	   int k = 0;
	   boolean[] checked = new boolean[set.length];
		   for(int j = 0; j < set.length; j++){
			   if(k >= root.length){
				   break;
			   }
			   if(set[j] == root[k] && checked[j] == false){
				   checked[j] = true;
				   rootPos[k] = j;
				   k++;
				   j = -1;
				   continue;
			   }
	   }
	   System.out.println(Arrays.toString(rootPos));
	   for(int i = 0; i < m; i++){
       		System.out.println("Largest cluster" + (i + 1) + ": contains " + ((root[i] / -1)) + " words ");
       		for(int z = 0; z < set.length; z++){
       			if(disFor.find_c(z) == rootPos[i]){
       				System.out.print(wordPair[z] + ", ");
       		}
       	}
       	System.out.println();
       }
       
   }
   
  
	public static void main(String[] args) throws IOException{
		ReadFile();
		
	}
	/*
	 * Reads files, reads each line of text files (2)
	 * Reads user input retrieving file
	 * Stores in temporary array of strings by delimiter " " in file, insert objects into Hash table
	 * Each line is inserted in Hash Table, consisting of string followed by the 50 float embedding
	 * Reads word pair file, computing similarities of each pair of words.
	 * Creates heap instance that inserts words and similarities, creating a min heap
	 * Creates disjoint forest creating word clusters based on word similarity
	 * Handles IO Exception
	 * 
	 */
	public static void ReadFile() throws IOException{
		try{
		String userInput = "";
		Scanner scan = new Scanner(System.in);
		System.out.println("Hello user, Please enter FileName: ");
		userInput = scan.nextLine();
		
		
		FileReader fr = new FileReader(userInput);
        BufferedReader textReader = new BufferedReader(fr);

		hashNode s = null;
		HashTable hT = new HashTable(71); // create Hash Table
		
		String stringLine; 
	
	        while((stringLine = textReader.readLine()) != null) { //read line
				String[] temp = stringLine.split(" "); //split store in array
				float[] embed = new float[temp.length-1];
					for(int i = 1; i < temp.length; i++){
						embed[i-1] = Float.parseFloat(temp[i]); //Embedding
					}
					 s = new hashNode(temp[0],embed,s);
					 hT.count++;                     //counter for load factor
					 hT.insert(temp[0], embed);
	        }
	    
			FileReader fr2 = new FileReader(new File("Lab6Words.txt"));
			FileReader fr3 = new FileReader(new File("Lab6Words.txt"));
	        BufferedReader textReader2 = new BufferedReader(fr2);
	        BufferedReader textReader3 = new BufferedReader(fr3);
	        
	        int countLines = 0;
	       
	        while(textReader2.readLine() != null) { 
	        	countLines++;
	        }
	        String[] wordPair = new String[countLines];
	        int i = 0;
	        while(i < countLines){
	        	String word = textReader3.readLine();
	        	wordPair[i] = word;
	        	i++;
	        	
	        }
	        Heap heap = new Heap(countLines * countLines);
	        HeapNode hSim = null;
	        
	        for(int j = 0; j < wordPair.length; j++){
	        	for(int k = j+1; k < wordPair.length; k++){
	        			hSim = new HeapNode(wordPair[j], wordPair[k], hT.sim(wordPair[j], wordPair[k]));
	    	        	heap.insert(hSim);
	        	}
	        }
	        
	        
	        DSF disFor = new DSF(countLines);
	        
	
			Scanner scan2 = new Scanner(System.in);  // user input for extractions
			System.out.println("Hello user, Please enter number of extractions: ");
			int userInput2 = scan2.nextInt();
	
			createSet(userInput2, disFor, wordPair, heap); //creates dsf sets 
			
			System.out.println(Arrays.toString(disFor.sortRoot()));
			int[] root = disFor.sortRoot();
		    int[] set = disFor.set();
		     
		    Scanner scan3 = new Scanner(System.in); // user input for largest clusters 
			System.out.println("Hello user, Please enter number of Largest Clusters: ");
			int userInput3 = scan3.nextInt();
		     
			
			largestSet(userInput3, wordPair, set, root, disFor);
			
			
	        scan.close();
	        scan2.close();
	        scan3.close();
	        textReader.close();
	        textReader2.close();
	        textReader3.close();
		}
		catch(FileNotFoundException e){
			System.out.print("Error: File Not Found!");
			System.exit(1);
			
			}
			return;
	        }
	
		 
		 
}


