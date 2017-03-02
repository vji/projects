/**
 * KPT_lab1.java - Return the Query result using InvertedIndex.java
 * Query1 - for single keyword
 * Query2 - for keyword1 AND keyword2
 * Query3 - for keyword1 OR keyword2
 * Query4 - For Multiple AND operation with optimization using sort on results arraylist.
 * 
 */
import java.io.*;
import java.util.*;

public class KPT_Lab1 {

	public KPT_Lab1() {
		// TODO Auto-generated constructor stub
	}
	/**Removing punctuation and other symbols. Only words and numbers stays. 
	 * Run Stemmer and write final words to output file.
	 * @param inFile
	 * @param sWord
	 * @return
	 */
	//public static String stemDoc(InputStream inFile, PrintWriter outFile, String[] sWord){
	public static String stemDoc(InputStream inFile, String[] sWord){
		String str = new String();  
		String str1 = new String();
		try {
			int fileSize = inFile.available();
			for(int i=0; i< fileSize; i++){
				str=str+(char)inFile.read();
			}
			inFile.close();
			String newDoc1 = str.replaceAll("[^\\w\\s]","").replaceAll("\\s+", " ");
			//Array of doc words
			String[] wordC1 = newDoc1.split(" ");
			ArrayList<String> docList1 = new ArrayList<String>();
			for (String word : wordC1){
				docList1.add(word);
			}
			for (int k=0; k<docList1.size(); k++){
				for (int m=0; m<sWord.length;m++){
					if(sWord[m].contains(docList1.get(k).toLowerCase())){
						docList1.remove(k);
						break;
					}
				}
			}
			//Call stemmer
			Stemmer st = new Stemmer();
			for (int n=0; n<docList1.size(); n++){
				char[] chArray = docList1.get(n).toLowerCase().toCharArray();
				st.add(chArray,chArray.length);
				st.stem();
				String result1 = st.toString();
				str1 = str1+result1+" ";
				//outFile.println(result1);
				//System.out.println(result1);
			}
			//outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str1;
	}
	/**
	 * andQuery with two results are input
	 * @param r1 - result for keyword 1
	 * @param r2 - result for keyword 2
	 * return AND operation results
	 */
	public static ArrayList<Integer> andQuery(ArrayList<Integer> r1,ArrayList<Integer> r2){
		int found = 0;
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(Integer i:r1){
			for(Integer j:r2){
				if(i.intValue()==j.intValue()){
					found =1;
					System.out.printf("doc "+(i.intValue()+1)+" ");
					res.add(i.intValue());
				}
			}}
		if(found==0)
			System.out.println("AND Query - No match found!");
		return res;
	}
	/**
	 * Query the invertedIndex results based given parameter
	 * @param invertedInd
	 * @param queryType  - 1 for AND, otherwise OR for 2 keywords case only
	 * @param keywordList - if only one keyword then returns the Query1 otherwise check the queryType if 2 keyword are provided,
	 * 						if more then 2 keywords, then it performs Query4 - multiple ANDs.
	 */
	public static void getResult(InvertedIndex invertedInd,int queryType, String[] keyWList){
		//Stem the given keywords
		Stemmer st1 = new Stemmer();
		String[] keywordList = keyWList;
		for (int n=0; n<keyWList.length; n++){
			char[] chArray = keyWList[n].toLowerCase().toCharArray();
			st1.add(chArray,chArray.length);
			st1.stem();
			keywordList[n] = st1.toString();
			//outFile.println(result1);
			//System.out.println(result1);
		}
		//For 1 keyword
		if(keywordList.length==1){
			ArrayList<Integer> result = invertedInd.search(keywordList[0]);
			if(result!=null){
				for(Integer i:result)
					System.out.printf("doc "+(i.intValue()+1)+" ");
			}
			else
				System.out.println("No match!");
			System.out.printf("\n--------------------------------------------------- \n");
		}
		//For AND and OR operation with only 2 Keywords
		else if(keywordList.length==2){
			if(queryType==1)
				System.out.printf("\nQuery2 : using AND : Test Case with : %s and %s \n",keywordList[0],keywordList[1]);
			else
				System.out.printf("\nQuery3 : using OR : Test Case with : %s and %s \n",keywordList[0],keywordList[1]);

			ArrayList<Integer> result1 = invertedInd.search(keywordList[0]);
			ArrayList<Integer> result2 = invertedInd.search(keywordList[1]);
			if(result1!=null){
				for(Integer i:result1)
					System.out.printf("doc "+(i.intValue()+1)+" ");
			}
			else
				System.out.println("No match!");
			System.out.printf("        ---------- 1st Query results !!\n");

			if(result2!=null){
				for(Integer i:result2)
					System.out.printf("doc "+(i.intValue()+1)+" ");
			}
			else
				System.out.println("No match!");
			System.out.printf("                 ------------------- 2nd Query results !!\n");
			//-------------------------------------------------------------------
			Set<Integer> resultOr = new HashSet<Integer>();
			if((result1!=null)&&(result2!=null)){
				resultOr.addAll(result1);
				resultOr.addAll(result2);
				if(queryType==1){
					andQuery(result1, result2);
				}
				else{
					for(Integer k:resultOr){
						System.out.printf("doc "+(k.intValue()+1)+" ");
					}
				}
			}
			else
				System.out.println("No match!");
			System.out.printf("\n--------------------------------------------------- \n");
		}
		else{
			System.out.printf("\nQuery4: using Multiple AND : Test Case with :");
			for(int x=0; x<keywordList.length; x++){
				System.out.printf(" %s",keywordList[x]);
			}
			System.out.println("");
			//Optimize to process shorter posting lists first
			ArrayList<ArrayList<Integer>> indx = new ArrayList<ArrayList<Integer>>();

			//int[] indx = {0,0,0,0,0};
			for(int x=0; x<keywordList.length; x++){
				indx.add(invertedInd.search(keywordList[x]));
			}
			Collections.sort(indx, new Comparator<ArrayList<Integer>>(){
				public int compare(ArrayList<Integer> d1, ArrayList<Integer> d2){
					int s1= d1.size();
					int s2 = d2.size();
					//System.out.printf("\nIn Comparision : %d and  %d\n",s1,s2);
					if(s1>s2)
						return 1;
					else if(s1<s2)
						return -1;
					else
					return 0;
				}
			});
			ArrayList<Integer> interRes = new ArrayList<Integer>();
			for(int y=1;y<indx.size();y++){
				interRes = andQuery(indx.get(y),indx.get(y+1));
				/*for(Integer k:interRes){
					System.out.printf("doc "+(k.intValue()+1)+" ");
				}for(Integer k:indx.get(y-1)){
					System.out.printf("doc "+(k.intValue()+1)+" ");
				}for(Integer k:indx.get(y)){
					System.out.printf("doc "+(k.intValue()+1)+" ");
				}	*/			
				indx.remove(y-1);
				indx.remove(y);
				indx.add(interRes);
			}
			System.out.printf("\nThe Docment returned by multiple AND query : ");
			for(Integer k:interRes){
				System.out.printf("doc "+(k.intValue()+1)+" ");
			}
		}
	}
	/**
	 * main()
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print("Inverse Indexing - Step 1 : reading documents, tokenize and running stemmer \n");
		try {
			System.out.println("Reading documents");
			InputStream inFile1 = new FileInputStream("src/cv000_29416.txt");
			InputStream inFile2 = new FileInputStream("src/cv001_19502.txt");
			InputStream inFile3 = new FileInputStream("src/cv002_17424.txt");
			InputStream inFile4 = new FileInputStream("src/cv003_12683.txt");
			InputStream inFile5 = new FileInputStream("src/cv004_12641.txt");
			InputStream inStopWord = new FileInputStream("src/stopwords.txt");
			/*PrintWriter outFile1 = new PrintWriter("src/doc1.txt", "UTF-8");
			PrintWriter outFile2 = new PrintWriter("src/doc2.txt", "UTF-8");
			PrintWriter outFile3 = new PrintWriter("src/doc3.txt", "UTF-8");
			PrintWriter outFile4 = new PrintWriter("src/doc4.txt", "UTF-8");
			PrintWriter outFile5 = new PrintWriter("src/doc5.txt", "UTF-8");*/
			int sizeStopWord = inStopWord.available();
			//Forming stop word list
			System.out.println("Preparing Stop word List");
			String sList = new String();
			for(int i=0; i< sizeStopWord; i++){
				sList=sList+(char)inStopWord.read();
			}
			String[] sWords = sList.split(" ");
			inStopWord.close();
			System.out.println("Running stemmer and preparing the docs String array");
			String doc1=stemDoc(inFile1, sWords);
			String doc2=stemDoc(inFile2, sWords);
			String doc3=stemDoc(inFile3, sWords);
			String doc4=stemDoc(inFile4, sWords);
			String doc5=stemDoc(inFile5, sWords);
			String[] docs = {doc1, doc2, doc3, doc4, doc5};
			System.out.println("Inverted Index Function call");
			InvertedIndex invertedInd = new InvertedIndex(docs);
			System.out.println(invertedInd);
			//---------------------------------------------------------------
			System.out.println("Query 1 : Single Keyword - Instead of args - using \"girlfriend\"");
			String[] str2= {"girlfriend"};
			getResult(invertedInd,0,str2);
			//----------------------------------------------------------
			//Query 2 : Keyword1 AND Keyword2
			String[] str3= {"movi","love"};
			String[] str4= {"drive","invent"};
			getResult(invertedInd,1,str3);
			getResult(invertedInd,1,str4);

			//----------------------------------------------------------
			//Query 3 : Keyword1 OR Keyword2
			String[] str5= {"movi","love"};
			String[] str6= {"drive","invent"};
			getResult(invertedInd,2,str5);
			getResult(invertedInd,2,str6);

			//----------------------------------------------------------
			//Query 4 : Keyword1 AND Keyword2 AND Keyword3 ...
			String[] str7= {"movi","attempt","break","the","girlfriend"};
			//String[] str8= {"drive","invent"};
			getResult(invertedInd,3,str7);
			//getResult(invertedInd,3,str8);
			//------------------------------------------------------------------------
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}