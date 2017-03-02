package lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BTreeIndex {
	String[] myDocs;
	BinaryTree docTree;
	/**
	 * Construct binary search tree to store the term dictionary 
	 * @param docs List of input strings
	 * 
	 */
	public BTreeIndex(String[] docs)
	{
		myDocs = docs;
		docTree = new BinaryTree();
		ArrayList<Integer> docList;
		BTNode node;
		for(int i=0;i<myDocs.length;i++){
			String[] tokens = myDocs[i].split(" ");			
			for(String token:tokens){
				if(docTree.search(docTree.root,token)==null){//a new term
					docList = new ArrayList<Integer>();
					docList.add(new Integer(i));
					node=new BTNode(token,docList);
					docTree.add(docTree.root,node);
				}
				else{//an existing term
					BTNode index = BinaryTree.search(docTree.root,token);
					docList = index.docLists;
					if(!docList.contains(new Integer(i))){
						docList.add(new Integer(i));
						index.docLists=docList;
						node=new BTNode(token,docList);
						docTree.add(docTree.root,node);
					}
				}
			}
		}
	}
	/**
	 * Single keyword search
	 * @param query the query string
	 * @return doclists that contain the term
	 */
	public ArrayList<Integer> search(String query)
	{
		BTNode node1 = BinaryTree.search(docTree.root, query);
		//System.out.println("returned: "+node.term+": "+node.docLists.size());
		if(node1==null)
			return null;
		return node1.docLists;
	}

	/**
	 * conjunctive query search
	 * @param query the set of query terms
	 * @return doclists that contain all the query terms
	 */
	public ArrayList<Integer> search(String[] query)
	{
		ArrayList<Integer> result = search(query[0]);
		int termId = 1;
		while(termId<query.length)
		{
			ArrayList<Integer> result1 = search(query[termId]);
			result = merge(result,result1);
			termId++;
		}		
		return result;
	}

	/**
	 * 
	 * @param wildcard the wildcard query, e.g., ho (so that home can be located)
	 * @return a list of ids of documents that contain terms matching the wild card
	 */
	public ArrayList<Integer> wildCardSearch(String wildcard)
	{
		//TO BE COMPLETED
		ArrayList<BTNode> res = docTree.wildCardSearch(docTree.root, wildcard);
		System.out.println("\nWild Card Searched: "+wildcard);
		
		ArrayList<Integer> r=new ArrayList<Integer>();
		for (int i = 0; i < res.size(); i++) 
		{
			ArrayList<Integer> rr = res.get(i).docLists;
			System.out.println(rr);
			r.add(rr.size());
		}
		return null;
	}


	private ArrayList<Integer> merge(ArrayList<Integer> l1, ArrayList<Integer> l2)
	{
		ArrayList<Integer> mergedList = new ArrayList<Integer>();
		int id1 = 0, id2=0;
		while(id1<l1.size()&&id2<l2.size()){
			if(l1.get(id1).intValue()==l2.get(id2).intValue()){
				mergedList.add(l1.get(id1));
				id1++;
				id2++;

			}
			else if(l1.get(id1)<l2.get(id2))
				id1++;
			else
				id2++;
		}
		return mergedList;
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


	/**
	 * Test cases
	 * @param args commandline input
	 */
	public static void main(String[] args)
	{
		String[] docs = {"new home sales top forecasts",
				"home sales rise in july",
				"increase in home sales in july",
				"july new home sales rise"
		};
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		BTreeIndex B = new BTreeIndex(docs);
		System.out.println("------------------------------\nTree Index\n-------------------------------");
		printInOrder(BinaryTree.root);
		System.out.println("-------------------------------\nSEARCH: ");
		//System.out.println(B.search("new"));
		System.out.println("Enter single term");
		String s;
		try {
			s = br.readLine();
			ArrayList<Integer> q1=B.search(s);
			System.out.println(q1);
			System.out.println();
			System.out.println("enter multiple terms");
			s=br.readLine();
			ArrayList<Integer> q2=B.search(s.split(" "));
			System.out.println(q2);
			System.out.println("Enter wildcard term");
			String q3= br.readLine();
			B.wildCardSearch(q3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Print the inverted index based on the increasing order of the terms in a subtree
	 * @param node the root node of the subtree
	 */
	public static void printInOrder(BTNode node)
	{
		if(node!=null){
			printInOrder(node.left);
			System.out.println(node);
			printInOrder(node.right);
		}
		//TO BE COMPLETED
	}

}