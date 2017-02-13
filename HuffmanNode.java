/* This program defines HuffmanNode class. 
 */

public class HuffmanNode implements Comparable<HuffmanNode> {

	// the ascii of char
	public int value;
	// the occurance of char
	public int count;
	// left child of this node
	public HuffmanNode left;
	// right child of this node
	public HuffmanNode right;

	// In: tmpValue: the ascii of char
	//     tmpCount: the occurance of char
	// Consturctor, creates a node with null left and right child.
	public HuffmanNode(int tmpValue, int tmpCount) {
		value = tmpValue;
		count = tmpCount;
		left = null;
		right = null;		
	}

	// In: other: another Huffmannode to compare to
	// Out: the difference of count in two nodes
	public int compareTo(HuffmanNode other) {
		return this.count - other.count;
	}
}
