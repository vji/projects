/**
 * Lab 4
 * Naive Bayes Classifier
 * 
 * Virendra Rajpurohit
 * 
 */
import java.io.*;
import java.util.*;
public class NBClassifier{
	public static ArrayList<String> vocab = new ArrayList<String>();
	public static int nWords=0;
	public static int nFold=0;
	public static int[] nCount=new int[4];
	public static int nCb=0;
	public static HashMap<String, Integer> hm1= new HashMap<String, Integer>();
	public static HashMap<String, Integer> hm2= new HashMap<String, Integer>();
	public static String[] trainData=new String[4];

	/**
	 * Build a Naive Bayes classifier using a training document set
	 * @param trainDataFolder the training document folder
	 * @throws IOException 
	 */
	public NBClassifier(String trainDataFolder) throws IOException 
	{
		preprocess(trainDataFolder);
		System.out.println("str len : "+trainData[0].length()+" : "+trainData[1].length());
		calcPbt(trainData[1],hm1);
		calcPbt(trainData[0],hm2);		
		System.out.println("training done on: "+trainDataFolder);
	}
	/**
	 * Classify a test doc
	 * @param doc test doc
	 * @return class label: 1 for pos Class and 0 for neg Class
	 * @throws IOException 
	 */
	public int classify(String doc) throws IOException{
		File testDoc = new File(doc);
		double pC=1;
		double nC=1;
		String[] tWords=readFile(testDoc).split(" ");
		int p=1;
		int n=1;
		double tT=1;
		for(String tW:tWords){
			if(hm1.containsKey(tW))
				p=hm1.get(tW);
			else
				p=1;
			pC*=((p+1)/(double)(nCount[0]+hm1.size()+hm2.size()));
			if(hm2.containsKey(tW))
				n=hm2.get(tW);
			else
				n=1;
			nC*=((n+1)/(double)(nCount[1]-nCount[0]+hm2.size()+hm1.size()));
			tT=pC+nC;
			pC=pC/tT;
			nC=nC/tT;
		}
		//System.out.println(doc+": Classified: "+pC+" : "+nC+" : "+((pC>nC)?1:0));
		return (pC>nC)?1:0;
	}

	/**
	 *  Classify a set of testing documents and report the accuracy
	 * @param testDataFolder fold that contains the testing documents
	 * @return classification accuracy
	 * @throws IOException 
	 */
	public double classifyAll(String testDataFolder) throws IOException
	{
		final File folder = new File(testDataFolder);
		File[] pFiles = null;
		File[] nFiles=null;
		int tp=0;
		int fp=0;
		int tn=0;
		int fn=0;
		if(folder.exists()){
			File[] files =folder.listFiles();
			//System.out.println("num File: "+files.length);
			if(files[0].isDirectory()){
				//System.out.println("File in test folder: "+files[0].getName()+" : "+files[1].getName());
				pFiles=files[1].listFiles();
				nFiles=files[0].listFiles();
			}
			else{
				System.out.println("Some issue in test folder reading");
			}
		}
		else{
			System.out.println("Error in given Path : Please enter correct file path");
		}
		//System.out.println("Classfing all test documents ");
		for(File pFile:pFiles){
			if(classify(pFile+"")==0)
				fn++;
			else
				tp++;
		}
		for(File nFile:nFiles){
			if(classify(nFile+"")==0)
				tn++;
			else
				fp++;		
		}
		System.out.println("Details of classification: \n tp: "+tp+", tn: "+tn+", fp: "+fp+", fn: "+fn);
		return (double)(tp+tn)*100/(tp+fp+tn+fn);
	}

	/**
	 * Load the training documents
	 * @param trainDataFolder
	 */
	public static void preprocess(String trainDataFolder) throws IOException
	{
		final File folder = new File(trainDataFolder);
		StringBuilder  sBr = new StringBuilder();
		if(folder.exists()){
			File[] files =folder.listFiles();
			if(files[0].isDirectory()){
				for (int i = 0; i < files.length; i++){
					System.out.println("Training on: "+files[i].getName());
					preprocess(files[i].toString());
				}
			}
			else{
				for (int i = 0; i < files.length; i++){
					sBr.append(readFile(files[i]));
				}
				trainData[nFold++]=sBr.toString();
			}
		}
		else{
			System.out.println("Error in given Path : Please enter correct file path");
		}
	}
	public static void calcPbt(String str,HashMap<String, Integer> hmap){
		String[] myStr=str.split(" ");
		for(String s : myStr){
			nWords++;
			if(!hmap.containsKey(s)){
				vocab.add(s);
				hmap.put(s, 1);
			}
			else
			{
				int val=(int) hmap.get(s);
				val++;
				hmap.replace(s, val);
			}
		}
		nCount[nCb++]=nWords;
	}
/**
 * Reading the given file to a String
 * @param file
 * @return A string containing input file content
 * @throws IOException
 */
	private static String readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader (file));
		String         line = null;
		StringBuilder  sBuilder = new StringBuilder();
		try {
			while((line = reader.readLine()) != null) {
				sBuilder.append(line+" ");
			}
			return sBuilder.toString();
		} finally {
			reader.close();
		}
	}
	/**
	 * Main function
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{		
		//-NB Classifier-----------------------------------
		NBClassifier Nb= new NBClassifier("./data/train");
		System.out.println("Total words: "+nWords+"\n	Distinct words- \n		In Train\n			neg: "+hm1.size()+" \n			pos: "+hm2.size()+"\n	 Total: "+(hm1.size()+hm2.size()));
		System.out.println("\n********************************************\nClassifying one doc: ");
		System.out.println(Nb.classify("./data/test/pos/cv900_10331.txt"));
		System.out.println("\n********************************************\nClassify all docs: ");
		System.out.println("\n"+Nb.classifyAll("./data/test")+" % Accuracy Overall");
	}
}
