
/**
 * Document clustering
 * @author Virendra Rajpurohit
 *
 */
public class Clustering {
	//ArrayList[String[]] docsVector=new ArrayList[String[]];
	Doc[] dVector=new Doc[10];
	String[] docsIn;
	//Declare attributes here
	String[] clustr=new String[2];
	Doc c[];
	/**
	 * Constructor for attribute initialization
	 * @param numC number of clusters
	 */
	public Clustering(int numC)
	{//initial cluster centroids;
		c=new Doc[numC];
	}

	/**
	 * Load the documents to build the vector representations
	 * @param docs
	 */
	public void preprocess(String[] docs){
		docsIn=docs;
		for(int i=0;i<docs.length;i++){
			String[] doc=docs[i].split(" ");
			dVector[i]=new Doc(i,tfIdfScore(doc));
			//System.out.println(dVector[i]);
		}
		//Setting initial cluster center values
		c[0]=new Doc(0,dVector[0].data);
		c[1]=new Doc(9,dVector[9].data);
	}
	private double[] tfIdfScore(String[] doc) {
		double tf[]=tfScore(doc);
		double idf[]=new double[4];
		double tiScore[]=new double[4];
		for(int i=0;i<doc.length;i++){
			idf[i]=idfScore(doc[i]);
			tiScore[i]=tf[i]*idf[i];
			//System.out.println(doc[i]+" - tf("+tf[i]+","+idf[i]+")idf: "+tiScore[i]);
		}
		return tiScore;
	}

	/**
	 * return the tf score for term
	 * @param doc
	 * @return
	 */
	private double[] tfScore(String[] doc) {
		double c=0;
		String d1="";
		double score[]=new double[4];
		for(int i=0;i<doc.length;i++){
			d1=doc[i];
			for(String d2:doc){
				if(d1.equals(d2))
					c++;
			}
			score[i]=c/doc.length;
			c=0;
		}
		return score;
	}
	/**
	 * idf Score
	 */
	private double idfScore(String term) {
		double n = 0;
		for(String s1:docsIn){
			String[] x=s1.split(" ");
			for(String s:x){
				if(s.equalsIgnoreCase(term)){
					n++;
					break;
				}
			}
		}

		return Math.log(docsIn.length/n);
	}

	/**
	 * Cluster
	 *  the documents
	 * For kmeans clustering, use the first and the ninth documents as the initial centroids
	 */
	public void cluster(){
		System.out.println("Clustering");
		int iter=0;
		Doc clusters[]=new Doc[2];
		while(iter++<5){
			//Calculating the Cosine similarity between cluster point to each document
			for (int i=0;i<c.length;i++){
				double[] d1 = new double[10];
				for(int j=0;j<dVector.length;j++){					
					d1[j]=calcCos(c[i].data,dVector[j].data);
					//System.out.printf("|("+i+","+j+"): "+d1[j]);
				}
				//System.out.println();
				clusters[i]=new Doc(i,d1);
			}
			String s1="-";
			String s2="-";
			double[] dt1=clusters[0].data;
			double[] dt2=clusters[1].data;
			clustr[0]="Cluster 0:	 \n";
			clustr[1]="Cluster 1: 	\n";
			/*Cluster array - setting 0 value doc not belonging to cluster i */
			for(int k=0;k<10;k++){
				if(dt1[k]<dt2[k]){
					dt1[k]=0;
					//clustr[1]+=k+"	";
				}
				else{
					dt2[k]=0;
					//clustr[0]+=k+"	";
				}

				s1+="|(0,"+k+"): "+dt1[k];
				s2+="|(1,"+k+"): "+dt2[k];
			}
			for(int k=0;k<5;k++){
				clustr[0]+=k+"	";				
				clustr[1]+=(k+5)+"	";}
			//System.out.println(s1);
			//System.out.println(s2);
			/*getting the new centre points after cluster assignment*/
			c[0].data=reCalcCentre(dt1);
			c[1].data=reCalcCentre(dt2);
		}
		System.out.println(clustr[0]);
		System.out.println(clustr[1]);
	}
	/*private int getMedian(double[] dt) {
		double[] cl1=new double[10];
		int l1=0;
		double cen=0;
		HashMap<Double,Integer> hs=new HashMap<Double,Integer>();
		ArrayList<Double> al=new ArrayList<Double>();
		for(int m=0;m<dt.length;m++){
			hs.put(dt[m],m);
			if(dt[m]!=0){
				al.add(dt[m]);
			}
			al.sort(null);
		}
		//get the doc number corresponding to median val and then set the new center
		double dr=al.get((al.size()/2));
		System.out.println("New centre: "+dr+", "+hs.get(dr));
		return hs.get(dr);
	}*/
	/**
	 * recalculate the new center of given cluster points.
	 * @param dt
	 * @return
	 */
	private double[] reCalcCentre(double[] dt) {
		double[] c=new double[4];
		for(int m=0;m<dt.length;m++){
			if(dt[m]!=0){
				for(int n=0;n<4;n++){
					c[n]+=dVector[m].data[n];
					//System.out.println("Added: "+c[n]+", ("+m+","+n);
				}
			}
			//System.out.println();

		}
		for(int k=0;k<4;k++){
			c[k]=c[k]/dt.length;
			//	System.out.println("--"+c[k]);
		}
		//System.out.println();
		return c;
	}
	/**
	 * Calculate the cosine similarity between 2 doc based on input tf-score arrays
	 * @param a
	 * @param b
	 * @return
	 */
	private double calcCos(double[] a, double[] b) {	
		double dotp=0, maga=0, magb=0;
		for(int i=0;i<((a.length<b.length)?a.length:b.length);i++){
			dotp+=a[i]*b[i];
			maga+=Math.pow(a[i],2);
			magb+=Math.pow(b[i],2);
		}
		maga = Math.sqrt(maga);
		magb = Math.sqrt(magb);
		double d = dotp / (maga * magb);
		return d==Double.NaN?0:d;
	}
	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args){
		String[] docs = {"hot chocolate cocoa beans",
				"cocoa ghana africa",
				"beans harvest ghana",
				"cocoa butter",
				"butter truffles",
				"sweet chocolate can",
				"brazil sweet sugar can",
				"suger can brazil",
				"sweet cake icing",
				"cake black forest"
		};
		Clustering c = new Clustering(2);

		c.preprocess(docs);

		c.cluster();
		/*
		 * Expected result:
		 * Cluster: 0
			0	1	2	3	4	
		   Cluster: 1
			5	6	7	8	9	
		 */
	}
}

/**
 * 
 * @author qyuvks
 * Document class for the vector representation of a document
 */
class Doc{
	int id;
	double[] data;
	int cId;
	public Doc(int n,double[] d){
		id=n;
		data=d;
	}
	public Doc(int n){
		id=n;
		//data=d;
	}
	public void setCluster(int ic){
		cId=ic;
	}
	public String toString(){
		String s=": doc id - "+(id+1);
		for(double d:data){
			s+=" "+d;
		}
		return s;
	}
}