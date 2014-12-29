import java.util.*;
import java.io.*;
import org.junit.Assert;
/**
 * This file contains solutions for all problems in Chapter 11
 * Sorting and Search
 */

public class Chapter11 {
	/**
	 * problem 11.1
	 * given two sorted array A and B, merge A and B
	 */
	public int[] merge(int[] a, int[] b) {
		if(a == null || a.length == 0) return b;
		if(b == null || b.length == 0) return a;
		int[] c = new int[a.length + b.length];
		int i = 0;
		int j = 0;
		int index = 0;
		while(i < a.length || j < b.length) {
			if(i == a.length) c[index++] = b[j++];
			else if(j == b.length) c[index++] = a[i++];
			else {
				if(a[i] < b[j]) {
					c[index++] = a[i++];
				} else {
					c[index++] = b[j++];
				}
			}
		}
		return c;
	}
	/**
	 * problem 11.2
	 * sort an array of strings so that all the anagrams next to each other
	 * bucket sort using hashmap
	 * Time complexity: O(N m log(m)) m is the average length of string
	 * we can use count sort to make the complexity O(N * m)
	 */
	public void sortAnagrams(String[] strs) {
		if(strs == null || strs.length == 0) return;
		HashMap<String,List<String>> map = new HashMap<String,List<String>>();
		for(int i = 0; i < strs.length; i++) {
			char[] chs = strs[i].toCharArray();
			Arrays.sort(chs); 
			String tmp = new String(chs);
			if(!map.containsKey(tmp)) {
				map.put(tmp,new LinkedList<String>());
			}
			map.get(tmp).add(strs[i]);
		}
		int index = 0;
		for(String key : map.keySet()) {
			for(String s : map.get(key)) {
				strs[index++] = s;
			}
		}
	}
	/**
	 * problem 11.3
	 * find element in a sorted array which is rotated for unknown times
	 * no duplicates
	 * Time complexity: O(log(N))
	 */
	public int findElement(int[] a, int key) {
		if(a == null || a.length == 0) return -1;
		int L = 0;
		int R = a.length - 1;
		while(L <= R) {
			int mid = L + (R - L) / 2;
			if(a[mid] == key) return mid;
			else if(a[mid] > a[L]) {
				if(a[mid] > key && a[L] <= key) {
					R = mid - 1;
				} else {
					L = mid + 1;
				}
			} else {
				if(a[mid] < key && a[R] >= key) {
					L = mid + 1;
				} else {
					R = mid - 1;
				}
			}
		}
		return -1;
	}
	/**
	 * problem 11.3 follow up. if duplicates exsits
	 */
	public int findElementII(int[] a, int key) {
		if(a == null || a.length == 0) return -1;
		return helper(a,0,a.length-1,key);
	}
	private int helper(int[] a, int L, int R, int key) {
		if(L > R) return -1;
		int mid  = L + (R - L) / 2;
		if(a[mid] == key) return mid;
		if(a[mid] > a[L]) {
			if(a[L] >= key) return helper(a,L,mid-1,key);
			return helper(a,mid+1,R,key);
		}
		else if(a[mid] < a[L]) {
			if(a[R] >= key) return helper(a,mid+1,R,key);
			return helper(a,L,mid-1,key);
		} else {//search two sides
			int index = helper(a,L,mid-1,key);
			if(index == -1) return helper(a,mid+1,R,key);
			return index;
		}
	}

	/**
	 * problem 11.5
	 * given a sorted array of strings which is interspersed with empty strings
	 * write a method to find the location of a given string
	 * binary search
	 */
	public int searchString(String[] strs, String key) {
		if(strs == null || strs.length == 0) return -1;
		int L = 0;
		int R = strs.length - 1;
		while(L <= R) {
			int mid = L + (R - L) / 2;
			mid = nearestNonEmpty(strs,mid,L,R,key);
			if(mid < 0 || mid >= strs.length) return -1; //all are empty  
			if(strs[mid].equals(key)) return mid;
			else if(strs[mid].compareTo(key) < 0) L = mid + 1;
			else R = mid -1 ;
		}
		return -1;
	}
	//find the nearest proper non-empty string
	private int nearestNonEmpty(String[] strs, int mid, int L, int R, String key) {
		int left = mid;
		int right = mid;
		while(left >= L && strs[left].length() == 0) {
			left--;
		}
		while(right <= R && strs[right].length() == 0) {
			right++;
		}
		if(left == L-1 && right == R+1) return -1;
		if(left == L-1) return right;
		if(right == R+1) return left;
		if(strs[left].compareTo(key) >= 0) return left;
		return right;
	}
	/**
	 * problem 11.6
	 * given an N * M matrix in which each row and column is sorted in ascending order
	 * find an element
	 * Time complexity: O(M+N)
	 */
	public boolean searchMatrix(int[][] matrix, int key) {
		int r = 0;
		int col = matrix[0].length - 1;
		while(r < matrix.length && col >= 0) {
			if(matrix[r][col] == key) return true;
			else if(matrix[r][col] < key) col--;
			else r++;
		}
		return false;
	}
	/**
	 * problem 11.7
	 * find the largest possible number of people
	 * this is a longest increasing sequence problem
	 * sort in h and search in w
	 */
	public int maxPerson(Person[] ps) {
		if(ps == null || ps.length == 0) return 0;
		//sort in descending order
		Arrays.sort(ps, new Comparator<Person>(){
			public int compare(Person p1, Person p2) {
				if(p1.height == p2.height) return 0;
				if(p1.height < p2.height) return 1;
				return -1;
			}
		});
		int[] dp = new int[ps.length];
		int max = 0;
		for(int i = 0; i < ps.length; i++) {
			dp[i] = 1;
			for(int j = i - 1; j >= 0; j--) {
				if(ps[j].weight > ps[i].weight && ps[j].height > ps[i].height) {
					dp[i] = Math.max(dp[i],dp[j]+1);
				}
			}
			max = Math.max(max,dp[i]);
		}
		return max;
	}	
	/**
	 * problem 11.7 follow up
	 * find the solution
	 * DP
	 * time complexity: O(N^2)
	 */
	public List<Person> maxPersonII(Person[] ps) {
		if(ps == null || ps.length == 0) return new LinkedList<Person>();
		List<Person>[] res = (LinkedList<Person>[]) new LinkedList[1];
		Arrays.sort(ps,new Comparator<Person>(){
			public int compare(Person p1, Person p2) {
				if(p1.height == p2.height) return 0;
				if(p1.height > p2.height) return -1;
				return 1;
			}
		});
		HashMap<Person,List<Person>> map = new HashMap<Person,List<Person>>();
		for(int i = 0; i < ps.length; i++) {
			List<Person> ls = new LinkedList<Person>();
			ls.add(ps[i]);
			map.put(ps[i],ls);
			for(int j = i - 1; j >= 0; j--) {
				if(ps[j].weight > ps[i].weight && ps[j].height > ps[i].height) {
					if(map.get(ps[j]).size() + 1 > map.get(ps[i]).size()) {
						List<Person> nls = new LinkedList<Person>(map.get(ps[j]));
						nls.add(ps[i]);
						map.put(ps[i],nls);
					}
				}
			}
			if(res[0] == null || map.get(ps[i]).size() > res[0].size()) {
				res[0] = map.get(ps[i]);
			}
		}
		return res[0];
	}
	public static void main(String[] args) {
		Chapter11 sol = new Chapter11();
		RankStream rs = new RankStream();
		int[] a = new int[]{5,1,9,4,7,4,5,13,3};
		for(int i = 0; i < a.length; i++) {
			rs.track(a[i]);
		}
		assertEquals(5,rs.getRank(5));
		assertEquals(0,rs.getRank(1));
		assertEquals(1,rs.getRank(3));
		assertEquals(3,rs.getRank(4));
		assertEquals(6,rs.getRank(7));
		assertEquals(7,rs.getRank(9));
		assertEquals(8,rs.getRank(13));
	}
}

class Person {
	public int height;
	public int weight;
	public Person(int h, int w) {
		height = h;
		weight = w;
	}
	public String toString() {
		return "("+height+","+weight+")";
	}
}

/**
 * problem 11.8
 * implement data structure for integer rank in a stream
 * use binary search tree
 * store the left size of a node
 */
class RankStream {
	class Node {
		private int val;
		private Node left;
		private Node right;
		private int leftsize;
		private int dup;
		public Node(int v) {
			val = v;
			left = null;
			right = null;
			leftsize = 0;
			dup = 0;
		}
	}
	private Node root = null;
	private HashMap<Integer,Node> map = new HashMap<Integer,Node>();
	public void track(int a) {
		if(root == null) {
			root = new Node(a);
			map.put(a,root);
		} else {
			Node p = root;
			Node q = null;
			while(p != null) {
				q = p;
				if(a == p.val) {//meet a node with the same value, increase leftsize and dup
					p.dup++;
					p.leftsize++; return;
				}
				else if(a < p.val) {
					p.leftsize++;
					p = p.left;
				} else {
					p = p.right;
				}
			}
			Node n = new Node(a);
			if(a < q.val) {
				q.left = n;
				n.leftsize = q.leftsize-1-q.dup;//left size of n, need to subtract dup
			} else if(a > q.val) {
				q.right = n;
			}
			map.put(a,n);
		}
	}
	public int getRank(int a) {
		if(!map.containsKey(a)) return -1;
		Node n = map.get(a);
		return getRank(root,a);
	}
	private int getRank(Node n, int a) {
		if(n.val == a) return n.leftsize;
		else if(n.val > a) return getRank(n.left,a);
		else return n.leftsize + 1 + getRank(n.right,a);//the node at right side
	}
}


