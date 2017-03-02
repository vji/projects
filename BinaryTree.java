package lab3;

import java.util.*;

import org.w3c.dom.Node;

import lab3.BTreeIndex.DocId;

/**
 * 
 * @author 
 * a node in a binary search tree
 */
class BTNode{
	BTNode left, right;
	String term;
	ArrayList<Integer> docLists;

	/**
	 * Create a tree node using a term and a document list
	 * @param root the term in the node
	 * @param docList the ids of the documents that contain the term
	 */
	public BTNode(String term, ArrayList<Integer> docList)
	{
		this.term = term;
		this.docLists = docList;
	}

	public String toString(){
		String s1=new String();
		for (int d:docLists)
			s1+=" "+d;
		return term+": ("+s1+")";
	}

}

/**
 * 
 * @author qyuvks
 * Binary search tree structure to store the term dictionary
 */
public class BinaryTree {

	/**
	 * insert a node to a subtree 
	 * @param node root node of a subtree
	 * @param iNode the node to be inserted into the subtree
	 */
	static BTNode root;
	static int k = 0;
	public static void add(BTNode parent,BTNode node)
	{	
		if(root==null){
			System.out.println("ROOT term  -  "+node.term);
			root=node;
		}
		else{
			if(parent.term.compareTo(node.term)>0){
				if(parent.left!=null){
					add(parent.left,node);
				}
				else{
					System.out.println(" adding " + node.term + " to left of "
							+ parent.term);
					parent.left = node;
				}
			}
			else if(parent.term.compareTo(node.term)<0){
				if(parent.right!=null){
					add(parent.right,node);
				}
				else{
					System.out.println(" adding " + node.term + " to right of "
							+ parent.term);
					parent.right = node;
				}
			}
		}
	}

	/**
	 * Search a term in a subtree
	 * @param n root node of a subtree
	 * @param key a query term
	 * @return tree nodes with term that match the query term or null if no match
	 */
	public static BTNode search(BTNode n, String key)
	{
		if(n==null)
			return null;
		
		//System.out.println("search:"+key+" - "+n.term);
		if(n.term.equals(key))
			//System.out.println("Match: "+n.docLists.size());
			return n;
		
		else if(n.term.compareTo(key)>0)
			return search(n.left,key);
		
		else
			return search(n.right,key);
	}

	/**
	 * Do a wildcard search
	 * h in a subtree
	 * @param n the root node of a subtree
	 * @param key a wild card term, e.g., ho (terms like home will be returned)
	 * @return tree nodes that match the wild card
	 */
	ArrayList<BTNode> result = new ArrayList<BTNode>();	
	public ArrayList<BTNode> wildCardSearch(BTNode n, String key)
	{
		//TO BE COMPLETED

		if(n!=null) {
			wildCardSearch(n.left,key);
			if(n.term.startsWith(key)) {
				result.add(n);
			}
			wildCardSearch(n.right,key);
		}
		return result;
	}


	/**
	 * Print the inverted index based on the increasing order of the terms in a subtree
	 * @param node the root node of the subtree
	 */
	public void printInOrder(BTNode node)
	{
		System.out.println("Printing Inverted index - tree terms");
		if(node!=null){
			printInOrder(node.left);
			System.out.println(node);
			printInOrder(node.right);
		}
		//TO BE COMPLETED
	}
}

