/**
 * KPT Lab2
 * Positional Index
 * @author - Virendra Rajpurohit
 * @date - 03/20/2016
 * Have used some code snippet and logic from the provided InvertedIndex.java file
 */
package lab2;
import java.util.ArrayList;

public class PositionalIndex {
	String[] myDocs;
	ArrayList<String> termList;
	ArrayList<ArrayList<DocId>> docLists;
	/**
	 * Construct a positional index 
	 * @param docs List of input strings or file names
	 * 
	 */
	public PositionalIndex(String[] docs)
	{
		myDocs = docs;
		termList = new ArrayList<String>();
		docLists = new ArrayList<ArrayList<DocId>>();
		ArrayList<DocId> docList;
		DocId docTerm;
		int iterm=0;
		for(int i=0;i<myDocs.length;i++){
			String[] tokens = myDocs[i].split(" ");
			iterm=-1;
			for(String token:tokens){
				iterm++;
				if(!termList.contains(token)){//a new term
					termList.add(token);
					docTerm = new DocId(i,iterm);
					docList = new ArrayList<DocId>();
					docList.add(docTerm);
					docLists.add(docList);
				}
				else{//an existing term
					int index = termList.indexOf(token);
					docList = docLists.get(index);
					try{
						for(DocId d : docList){
							if (d.docId==i){
								d.insertPosition(iterm);
							}
							else{
								docTerm = new DocId(i,iterm);
								docList.add(docTerm);
							}
							docLists.set(index, docList);	
						}
					}catch (Exception e){
						//System.out.println("in exp");
						//just continuing when DocId in not found
					}	
				}
			}
		}
		//TASK1: COMPLETED
	}
	/**
	 * Return the string representation of a positional index
	 */
	public String toString()
	{
		String matrixString = new String();
		ArrayList<DocId> docList;
		for(int i=0;i<termList.size();i++){
			matrixString += String.format("%-15s", termList.get(i));
			docList = docLists.get(i);
			for(int j=0;j<docList.size();j++)
			{
				matrixString += docList.get(j).toString()+ "\t";
			}
			matrixString += "\n";
		}
		return matrixString;
	}
	
	/**
	 * 
	 * @param l1 first postings
	 * @param l2 second postings
	 * @return merged result of two postings
	 */
	public ArrayList<DocId> intersect(ArrayList<DocId> l1, ArrayList<DocId> l2, int k1)// Where is K - e.g. length of "to be or not to be"
	{
		ArrayList<DocId> IntersectList = new ArrayList<DocId>();
		ArrayList<Integer> pp1;
		ArrayList<Integer> pp2;
		int id1=0, id2=0;
		int k = k1;
		while(id1<l1.size()&&id2<l2.size()){
			if(l1.get(id1).docId==l2.get(id2).docId){
				//ArrayList<Integer> posList = new ArrayList<Integer>();
				pp1=l1.get(id1).positionList;
				pp2=l2.get(id2).positionList;
				int pos1=0,pos2=0;
				while(pos1<pp1.size() && pos2<pp2.size()){
					if(pp2.get(pos2)-pp1.get(pos1)==k){
						// to be or not to be - Check here if to is followed by be - pos1-pos2 = 1 ?
						//posList.add(pos2);
						// if found then we add pos2 is list
						IntersectList.add(l2.get(id2));
						pos1++;
						pos2++;
					}
					else if(pp2.get(pos2)-pp1.get(pos1)>k)//if pos2 is greater - then is will not match any further so break
						pos1++;	
					else
					pos2++;
				}
				id1++;
				id2++;
			}
			else if(l1.get(id1).docId<l2.get(id2).docId)
				id1++;
			else
				id2++;
		}
		return IntersectList;
		//TASK2: COMPLETED
	}
	/**
	 * Returns the merged output for the words in given phrase
	 * @param query a phrase query that consists of any number of terms in the sequential order
	 * @return ids of documents that contain the phrase
	 */
	public ArrayList<DocId> phraseQuery(String query){
		int index = termList.indexOf(query);
		if(index <0)
			return null;
		return docLists.get(index);
	}
	public ArrayList<DocId> phraseQuery(String[] query){
		ArrayList<DocId> result = phraseQuery(query[0]);
		int termId = 1;
		while(termId<query.length){
			//System.out.println(query[termId]);
			ArrayList<DocId> result1 = phraseQuery(query[termId]);
			result = intersect(result, result1,1);
			termId++;
		}
		return result;
	}
	/**
	 * Prints the docs which contains the input phrase
	 * @param docs - input docs
	 * @param result - Query result
	 */
	public static void printResult(String[] docs,ArrayList<DocId> result){
	if(result.size()!=0){
		for(DocId i:result)
			System.out.println("doc#"+i.docId+" -> "+docs[i.docId]);
	}
	else
		System.out.println("No match!");
	}
	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args)
	{
		String[] docs = {"new home sales top forecasts",
				"home sales rise in july",
				"increase in home sales in july",
				"july new home sales rise"
		};
		PositionalIndex pi = new PositionalIndex(docs);
		System.out.println("Positional Index :");
		System.out.print(pi);
		System.out.println("-------------------------------------------");
		System.out.println("Query Results : ");
		System.out.println("-------------------------------------------");
		System.out.println("Phrase : 'home sales' : \nresult:");
		String[] query1= {"home","sales"};
		ArrayList<DocId> result = pi.phraseQuery(query1);
		printResult(docs,result);
		System.out.println("-------------------------------------------");
		System.out.println("Phrase : 'home sales rise' : \nresult:");
		String[] query2= {"home","sales","rise"};
		ArrayList<DocId> result2 = pi.phraseQuery(query2);
		printResult(docs,result2);
		System.out.println("-------------------------------------------");
		System.out.println("Phrase : 'new home sales rise' : \nresult:");
		String[] query3= {"new","home","sales","rise"};
		ArrayList<DocId> result3 = pi.phraseQuery(query3);
		printResult(docs,result3);
		System.out.println("-------------------------------------------");
		System.out.println("Phrase : 'home sales in july' : \nresult:");
		String[] query4= {"home","sales","in","july"};
		ArrayList<DocId> result4 = pi.phraseQuery(query4);
		printResult(docs,result4);
		
	}
}
/**
 * 
 * @author qyuvks
 * Document id class that contains the document id and the position list
 */
class DocId{
	int docId;
	ArrayList<Integer> positionList;
	public DocId(int did)
	{
		docId = did;
		positionList = new ArrayList<Integer>();
	}
	public DocId(int did, int position)
	{
		docId = did;
		positionList = new ArrayList<Integer>();
		positionList.add(new Integer(position));
	}
	public void insertPosition(int position)
	{
		positionList.add(new Integer(position));
	}
	public String toString()
	{
		String docIdString = ""+docId + ":<";
		for(Integer pos:positionList)
			docIdString += pos + ",";
		docIdString = docIdString.substring(0,docIdString.length()-1) + ">";
		return docIdString;		
	}
}
