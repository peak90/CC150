import java.util.*;


public class Chapter1 {
	public static void main(String[] args) {
		Chapter1 sol = new Chapter1();
		String s1 = "ACDDX";
		String s2 = "DDXAC";
		System.out.println(sol.isRotate(s1,s2));
	}
	/**
	 * problem 1: implement an algorithm to determine if a string has all unique characters
	 * what if you cannot use additional data structure
	 * simple solution: use hashmap or array to check dupliate characters
	 * better solution: use 8 integer to check duplicate characters
	 * use every bit to represent a character
	 * Time complexity: O(N)
	 * Space complexity: O(1)
	 */
	public boolean isUniqueCharacter(String s) {
		if(s == null || s.length() == 0 || s.length() > 256) return false;
		char[] chs = s.toCharArray();
		int[] check = new int[8];
		for(int i = 0; i < chs.length; i++) {
			int index = chs[i] / 32;
			int offset = chs[i] - index * 32;
			if(((check[index] >> offset) & 1) == 1) return false;
			check[index] |= (1 << offset);
		}
		return true;
	}
	/**
	 * problem 3: given two string, write a method to decide if one is a permutation of the other
	 * solution1: sort the two strings and compare
	 * better solution: count the frequency of characters
	 * use a 256-length ineger array. If we know the maximum frequency, we may be able to use bit vector
	 * like, max-frequency is 7, we can use 3-bit for every character.
	 * Time complexity: O(N)
	 * Space complexity: O(1) with constant factor 256
	 */
	public boolean isPermutation(String s1, String s2) {
		if(s1 == null || s2 == null || s1.length() == 0 || 
		   s2.length() == 0 || s1.length() != s2.length()) 
		   return false;
		int[] checker = new int[256];
		char[] chs1 = s1.toCharArray();
		char[] chs2 = s2.toCharArray();
		for(int i = 0; i < chs1.length; i++) {
			checker[chs1[i]]++;
		}
		for(int i = 0; i < chs2.length; i++) {
			checker[chs2[i]]--;
			if(checker[chs2[i]] < 0) return false;
		}
		return true;
	}
	//write a method to replace all spaces in a string with '%20'. there is efficient space at the end of the array
	//two traversal:
	//1. first traversal to count the number of space
	//2. copy the original array to that point.
	//Time complexity: O(N). constant factor = 2
	//Space complexity: O(1). No extra space is needed
	public String replaceSpaces(char[] chs, int len) {
		int spacenum = 0;
		for(int i = 0; i < len; i++) {
			if(chs[i] == ' ') spacenum++;
		}
		int index = len + 2 * spacenum - 1;
		for(int i = len - 1; i >= 0; i--) {
			if(chs[i] != ' ') {
				chs[index--] = chs[i];
			} else {
				chs[index] = '0';
				chs[index-1] = '2';
				chs[index-2] = '%';
				index -= 3;
			}
		}
		return new String(chs,0,len + 2 * spacenum);
	}
	/**
	 * problem 1.5
	 * implement a method to perform basic string compression using counts
	 * of repeated characters.
	 * Time complexity: O(N)
	 * Space complexity: O(N)
	 */
	public String compressStr(String s) {
		if(null == s || s.length() == 0) return "";
		StringBuilder sb = new StringBuilder();
		char[] chs = s.toCharArray();
		int i = 0;
		while(i < chs.length) {
			int count = 0;
			char c = chs[i];
			while(i < chs.length && chs[i] == c) {
				i++;
				count++;
			}
			sb.append(c);
			sb.append(count);
		}
		String res = sb.toString();
		return res.length() < s.length() ? res : s;
	}
	/**
	 * problem 1.6
	 * given an image represented by N * N matrix, rotate the image by 90 degree clockwise
	 * divide the image into four parts
	 * take care when N is even or odd
	 * Time complexity: O(N^2)
	 * Space complexity: O(1)
	 * (i,j) -> (j,n-i-1) -> (n-1-i,n-1-j) -> (n-j-1,i)
	 */
	public void rotateImage(int[][] matrix) {
		if(matrix == null || matrix.length == 0) return;
		int n = matrix.length;
		int rowlen = n % 2 == 0 ? n / 2 : n / 2 + 1;
		for(int i = 0; i < rowlen; i++) {
			for(int j = 0; j < n / 2; j++) {
				int tmp = matrix[i][j];
				matrix[i][j] = matrix[n-j-1][i];
				matrix[n-j-1][i] = matrix[n-1-i][n-1-j];
				matrix[n-1-i][n-1-j] = matrix[j][n-i-1];
				matrix[j][n-i-1] = tmp;
			}
		}
	}
	/**
	 * problem 1.7
	 * use the first row and first col to store the flags
	 * Time complexity: O(N^2)
	 * Space complexity: O(1)
	 */
	public void setZero(int[][] matrix) {
		if(matrix == null || matrix.length == 0) return;
		int m = matrix.length;
		int n = matrix[0].length;
		boolean firstrowzero = false;
		boolean firstcolzero = false;
		//check whether first row has zero
		for(int i = 0; i < n; i++) {
			if(matrix[0][i] == 0) {
				firstrowzero = true;
				break;
			}
		}
		//check whether first col has zero
		for(int i = 0; i < m; i++) {
			if(matrix[i][0] == 0) {
				firstcolzero = true;
				break;
			}
		}
		//set flag
		for(int i = 1; i < m; i++) {
			for(int j = 1; j < n; j++) {
				if(matrix[i][j] == 0) {
					matrix[0][j] = 0;
					matrix[i][0] = 0;
				}
			}
		}
		for(int i = 1; i < m; i++) {
			for(int j = 1; j < n; j++) {
				if(matrix[i][0] == 0 || matrix[0][j] == 0) {
					matrix[i][j] = 0;
				}
			}
		}
		if(firstrowzero) {
			for(int i = 0; i < n; i++) {
				matrix[0][i] = 0;
			}
		}
		if(firstcolzero) {
			for(int i = 0; i < m; i++) {
				matrix[i][0] = 0;
			}
		}
	}
	/**
	 * problem 1.8 check whether a string is a rotated of another string
	 * Time complexity: O(M+N) using KMP
	 * Space complexity: O(M+N)
	 */
	public boolean isRotate(String s1, String s2) {
		if(s1 == null || s2 == null || s1.length() == 0 || 
		   s2.length() == 0 || s1.length() != s2.length()) 
		   return false;
		String res = s1 + s1;
		return res.indexOf(s2) != -1;
	}
}

//support string
class XStringBuffer {
	private char[] chs;
	private int top;
	public XStringBuffer() {
		top = 0;
		chs = new char[1];
	}
	//append a string to the buffer
	public void append(String s) {
		if(s == null || s.length() == 0) return;
		if(top + s.length() > chs.length) {
			resize((top+s.length())*2);
		}
		for(int i = 0; i < s.length(); i++) {
			chs[top++] = s.charAt(i);
		}
	}
	public String toString() {
		return new String(chs,0,top);
	}
	private void resize(int newsize) {
		char[] newchs = new char[newsize];
		for(int i = 0; i < top; i++) {
			newchs[i] = chs[i];
		}
		chs = newchs;
	}
}