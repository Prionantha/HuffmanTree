/* This program is able to compress files in the way of Huffman Coding. The program consists 
 * of two parts:
 * Part 1: making a code: arrange the characters according to their frequency, change the 
 * 		   corresponding code to compress characters.
 * Part 2: encoding and decoding: creating a code tree and use it to decompress the input file.
 */

import java.util.*;
import java.io.*;

public class HuffmanTree {
	
	// Part 1

	// the overallRoot of Huffman tree
	private HuffmanNode overallRoot;

	// In: count: An array of int representing frequency
	// construct initial tree using given frequency
	// note: there's an extra EOF node representing end of file
	public HuffmanTree(int[] count) {
		// create node and add into priority queue
		Queue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>();
		HuffmanNode tempNode;
		int len = count.length;
		for (int i = 0; i < len; i++) {
			if (count[i] > 0) {
				tempNode = new HuffmanNode(i,count[i]);
				queue.add(tempNode);
			}
		}
		// add the EOF node
		tempNode = new HuffmanNode(256,1);
		queue.add(tempNode);
		// build tree
		HuffmanNode first;
		HuffmanNode second;
		HuffmanNode dad;		
		while (queue.size() > 1) {
			// combine two nodes with smallest frequency
			first = queue.remove();
			second = queue.remove();
			dad = new HuffmanNode(-1, first.count + second.count);
			dad.left = first;
			dad.right = second;
			queue.add(dad);
		}
		// overallRoot is the node left
		overallRoot = queue.remove();
	}
	
	// In: output: a printstream
	// inorder visit all characters
	public void write(PrintStream output) {
		//encode(overallRoot);
		String ans = "";
		writeHelper(overallRoot, output, ans);
	}
	
	// In: root: current node 
	//     output: printstream
	//     ans: the 01 string of given node
	// print out all characters and codes
	private HuffmanNode writeHelper(HuffmanNode root, PrintStream output, String ans) {
		// if it's not a leaf node then keep backtracking
		if (root.value == -1) {
			root.left = writeHelper(root.left, output, ans + '0');
			root.right = writeHelper(root.right, output, ans + '1');
		// if it's a leaf node then print the ascii and answer 01 string
		} else {
			output.println(root.value);
			output.println(ans);
		}
		return root;
	}

	// Part 2

	// In: input: scanner
	// reconstruct the tree from file
	public HuffmanTree(Scanner input) {
		overallRoot = new HuffmanNode(-1, -1);
		while (input.hasNextLine()) {
			// read in character and code pairs
			int n = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			HuffmanTreeHelper(overallRoot, n, code, 0);
		}
	}
	
	// In: root: current node
	//     n: the value of given target node
	//     code: the code of given target node
	//     curr: the current digit to be processed in the 01 string
	// recursively create the tree
	private HuffmanNode HuffmanTreeHelper(HuffmanNode root, int n, String code, int curr) {
		// if current root is null create a new node
		if (root == null) {
			root = new HuffmanNode(-1, -1);
		}
		// base case: if finding the target node then change the value
		if (curr == code.length()) {
			root.value = n;
		} else {
			// if current number is 0 go left, else go right
			if(code.charAt(curr) == '0') {
				root.left = HuffmanTreeHelper(root.left, n, code, curr + 1);
			} else {
				root.right = HuffmanTreeHelper(root.right, n, code, curr + 1);
			}
		}
		return root;
	}

	// In: input: bitInputStream
	//     output: printstream
	//     eof: the number notating end of file
	// read individual bits from input stream and write the corresponding characters
	// to output 
	// print out the decompressed words
	public void decode(BitInputStream input, PrintStream output, int eof) {
		HuffmanNode pos = overallRoot;
		int n;
		// denoting whether confronting end of file
		boolean end = false;
		while (!end) {
			n = input.readBit();
			//System.output.write(n);
			if (n == 0) {
				pos = pos.left;
			} else {
				pos = pos.right;
			}
			if (pos.left == null && pos.right == null) {
				if (pos.value == eof) {
				// if this is eof then stop entering while next time
					end = true;
				} else {
					output.write((char)pos.value);
					// position back to overall root
					pos = overallRoot;
				}
			}
		}
	}
}
