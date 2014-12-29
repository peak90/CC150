import java.util.*;

/**
 * This file contains solutions for all problems in Chapter17
 */

public class Chapter17 {
	/**
	 * problem 17.1
	 * swap two integers without tempral value
	 */
	public void swapInteger(int a, int b) {
		if(a != b) {
			a = a ^ b;
			b = a ^ b;
			a= a ^ b;
		}
		System.out.println(a+" "+b);
	}
	/**
	 * problem 17.2
	 * design an algorithm to check whether a person won a game of tic-tac-toe
	 * a player wins when there is a row or col or diagnonal
	 */
	public String checkTicTacToe(Piece[][] board) {
		//check row
		for(int i = 0; i < board.length; i++) {
			if(!board[i][0].name.equals("")) {
				int j = 1;
				for(; j < board[0].length; j++) {
					if(!board[i][j].name.equals(board[i][0].name)) break;
				}
				if(j == board[0].length) return board[i][0].name;
			}
		}
		//check col
		for(int i = 0; i < board[0].length; i++) {
			if(!board[0][i].name.equals("")) {
				int j = 1;
				for(; j < board.length; j++) {
					if(!board[j][i].name.equals(board[0][i].name)) break;
				}
				if(j == board.length) return board[0][i].name;
			}
		}
		//check diagonal
		if(!board[0][0].name.equals("")) {
			int i = 1;
			for(; i < board.length; i++) {
				if(!board[i][i].name.equals(board[0][0].name)) break;
			}
			if(i == board.length) return board[0][0].name;
		}
		if(!board[0][board.length-1].name.equals("")) {
			int i = 1;
			for(; i < board.length; i++) {
				if(!board[i][board.length-i-1].name.equals(board[0][board.length-1].name)) break;
			}
			if(i == board.length) return board[0][board.length-1].name;
		}
		return "";
	}
	/**
	 * problem 17.3
	 * write an algorithm which computes the number of trailing zeros in factorial
	 * just need to count the factors of 5
	 * N! = 2^m * 3^i*5^n*...
	 * we can know that m >= n for all N!
	 * The number of trailing zeor = n
	 */
	public int trailingZero(int n) {
		if(n <= 0) return 0;
		int res = 0;
		for(int i = 5; n / i > 0; i *= 5) {
			res += n / i;
		}
		return res;
	}
	/**
	 * problem 17.4
	 * find the maximum of two numbers without if-else or comparision operations
	 * check the sign of a-b
	 */
	public int maxNum(int a, int b) {
		long c = (long)a - (long)b;
		int k = (int)((c >> 63) & 1);
		int q = 1 ^ k;
		return a * q + b * k;
	}
	
	/**
	 * problem 17.5
	 * Game of master mind
	 */
	public MasterMindNode mastermind(String solution, String guess) {
		MasterMindNode mm = new MasterMindNode();
		if(solution == null || solution.length() == 0 || guess == null || guess.length() == 0) {
			return mm;
		}
		HashMap<Character,Integer> map = new HashMap<Character,Integer>();
		for(int i = 0; i < solution.length(); i++) {
			if(i < guess.length() && solution.charAt(i) == guess.charAt(i)) {
				mm.hit++;
			} else {
				if(!map.containsKey(solution.charAt(i))) {
					map.put(solution.charAt(i),1);
				} else {
					map.put(solution.charAt(i),map.get(solution.charAt(i))+1);
				}
			}
		}
		for(int i = 0; i < guess.length(); i++) {
			//hit
			if(i < solution.length() && solution.charAt(i) == guess.charAt(i)) continue;
			//pseudo hit
			if(map.containsKey(guess.charAt(i)) && map.get(guess.charAt(i)) > 0) {
				mm.phit++;
				map.put(guess.charAt(i),map.get(guess.charAt(i))-1);//decrease 1
			}
		}
		return mm;
	}
	/**
	 * problem 17.6!!
	 * given an array of integers, find the indices m and n such that sort through m to n, 
	 * the entire array would be sorted. Minimize n - m
	 * Time complexity: O(N)
	 * Space complexity:O(N)
	 * compute the mins and maxs array.
	 */
	public int[] minimalSeq(int[] array) {
		if(array == null || array.length == 0) return new int[]{0,0};
		int[] mins = new int[array.length]; //mins[i] is the minimal element from i to array.length-1
		int[] maxs = new int[array.length]; //maxs[i] is the maximal element from 0 to i
		mins[mins.length - 1] = array[array.length - 1];
		for(int i = array.length - 2; i >= 0; i--) {
			mins[i] = Math.min(array[i],mins[i+1]);
		}
		maxs[0] = array[0];
		for(int i = 1; i < array.length; i++) {
			maxs[i] = Math.max(maxs[i-1],array[i]);
		}
		//the farthest of the left index
		int start = 0;
		while(start < array.length - 1) {
			if(array[start+1] >= array[start] && array[start] <= mins[start]) {
				start++;
			} else {
				break;
			}
		}
		//the farthest of the right index
		int end = array.length-1;
		while(end > 0) {
			if(array[end-1] <= array[end] && array[end] >= maxs[end]) {
				end--;
			} else {
				break;
			}
		}
		if(start > end) return new int[]{0,-1};//has been sorted in ascending order
		return new int[]{start,end};
	}
	/**
	 * problem 17.7
	 * better solution
	 * Time complexity: O(N)
	 * Space complexity: O(1)
	 * step 1: find the sorted part at left
	 * step 2: find the sorted part at right
	 * step 3: find the min and max between the unsorted part
	 * step 4: shrink left part
	 * step 5: shrink right part
	 */
	public int[] minimalSeqII(int[] array) {
		if(array == null || array.length == 0) return new int[]{0,-1};
		//find the left sorted part
		int start = 0;
		while(start < array.length - 1 && array[start] <= array[start+1]) {
			start++;
		}
		if(start == array.length - 1) return new int[]{0,-1};
		//find right sorted part
		int end = array.length - 1;
		while(end > 0 && array[end] >= array[end-1]) {
			end--;
		}
		if(end == 0) return new int[]{0,-1};
		//find min and max element between [start+1,end-1]
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i = start + 1; i <= end - 1; i++) {
			min = min > array[i] ? array[i] : min;
			max = max < array[i] ? array[i] : max;
		}
		//shrink left sorted and right sorted part
		System.out.println(min+" "+max);
		while(start >= 0 && array[start] > min) {
			start--;
		}
		while(end < array.length && array[end] < max) {
			end++;
		}
		return new int[]{start+1,end-1};
	}
	/**
	 * problem 17.7
	 * given any integer, print an English phrase that describle the integer
	 */
	public String numPhrase(int number) {
		long num = number;
		num = Math.abs(num);
		int[] base = new int[]{1000000000,1000000,1000,1};
		String[] tokens = new String[]{"billion","million","thousand",""};
		StringBuilder sb = new StringBuilder();
		while(num > 0) {
			for(int j = 0; j < base.length; j++) {
				if(num >= base[j]) {
					sb.append(phrase((int)(num / base[j])) + " "+tokens[j]);
					num -= num / base[j] * base[j];
					if(num != 0) sb.append(",");
				}
			}
		}
		String res = sb.toString().trim();
		if(number < 0) res = "Negative "+ res;
		return res;
	}
	//num is between 1 to 999
	private String phrase(int num) {
		StringBuilder sb = new StringBuilder();
		String[] strs = new String[]{"","One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten",
		"Eleven","Twelve","Thirteen","Fourteen","Fifteen","Sixteen","Seventeen","Eighteen","Nineteen"};
		String[] strs1 = new String[]{"Twenty","Thirty","Forty","Fifty","Sixty","Seventy","Eighty","Ninty"};
		if(num >= 100) {
			sb.append(strs[num / 100]+" Hundred ");
			num -= num / 100 * 100;
		}
		if(num < 20) {
			sb.append(strs[num]);
		} else {
			sb.append(strs1[num / 10-2]+" ");
			sb.append(strs[num - num / 10 * 10]);
		}
		return sb.toString();
	}
	/**
	 * problem 17.8
	 * given an array of integers(both positive and negative). Find the contigous
	 * sequence with the largest sum. return the sum
	 * DP
	 */
	public int maxSum(int[] array) {
		if(array == null || array.length == 0) return 0;
		int[] dp = new int[array.length];
		dp[0] = array[0];
		int max = dp[0];
		for(int i = 1; i < array.length; i++) {
			dp[i] = array[i] + (dp[i-1] > 0 ? dp[i-1] : 0);
			max = max < dp[i] ? dp[i] : max;
		}
		return max;
	}
	/**
	 * problem 17.8 follow up, using constant space
	 */
	public int maxSumII(int[] array) {
		if(array == null || array.length == 0) return 0;
		int dp = array[0];
		int max = dp;
		for(int i = 1; i < array.length; i++) {
			dp = array[i] + (dp > 0 ? dp : 0);
			max = max < dp ? dp : max;
		}
		return max;
	}
	/**
	 * problem 17.8 follow up: find the solution
	 * dp
	 * just compute the solution when dp
	 */
	public List<Integer> maxSumIII(int[] array) {
		if(array == null || array.length == 0) return new LinkedList<Integer>();
		List<Integer>[] sols = (LinkedList<Integer>[]) new LinkedList[array.length];
		for(int i = 0; i < sols.length; i++) {
			sols[i] = new LinkedList<Integer>();
		}
		int dp = array[0];
		sols[0].add(array[0]);
		List<Integer> best = null;
		int max = Integer.MIN_VALUE;
		for(int i = 1; i < array.length; i++) {
			if(dp > 0) {
				sols[i].addAll(sols[i-1]);
			}
			dp = array[i] + (dp <= 0 ? 0 : dp);
			sols[i].add(array[i]);
			if(max < dp) {
				max = dp;
				best = sols[i];
			}
		}
		return best;
	}
	/**
	 * problem 17.11
	 *implement a method rand7() given rand5(). That is, given a method that generates
	 *a random number between 0 and 4, write a method to generate a random number between 0 and 6
	 *genereate two times
	 * num = randm()*m^i-1+...randm()
	 */
	public int rand7() {
		Random r = new Random();
		while(true) {
			int res = r.nextInt(5) * 5 + r.nextInt(5);
			if(res < 21) {
				return res % 7;
			}
		}
	}
	/**
	 *problem 17.11 follow up, use rand4() generates rand17()
	 *need to generate 3 times
	 */
	public int rand17() {
		Random r = new Random();
		while(true) {
			int res = 4 * 4 * r.nextInt(4) + 4 * r.nextInt(4) + r.nextInt(4);//[0,63]
			if(res < 51) {
				return res % 17;
			}
		}
	}
	/**
	 * problem 17.12
	 * design an algorithm to find all pairs of integers within an array which sum to a specific value
	 * do not consider duplicates
	 */
	public List<Pair> findAllPairs(int[] array, int val) {
		if(array == null || array.length < 2) return new LinkedList<Pair>();
		HashSet<Integer> set = new HashSet<Integer>();
		List<Pair> res = new LinkedList<Pair>();
		for(int i = 0; i < array.length; i++) {
			if(!set.contains(val-array[i])) {
				set.add(array[i]);
			} else {
				res.add(new Pair(array[i],val - array[i]));
				set.remove(val-array[i]);
			}
		}
		return res;
	}

	/**
	 * problem 17.13
	 * change binary tree to double linkedlist
	 * just use inorder traversal
	 * iteration
	 */
	public BiNode bstToDL(BiNode root) {
		if(root == null) return root;
		BiNode head = root;
		while(head.node1 != null) head = head.node1;
		Stack<BiNode> stack = new Stack<BiNode>();
		BiNode p = root;
		BiNode pre = null;
		while(p != null || ! stack.isEmpty()) {
			while(p != null) {
				stack.push(p);
				p = p.node1;
			}
			p = stack.pop();
			p.node1 = pre;
			if(pre != null) pre.node2 = p;
			pre = p;
			p = p.node2;
		}
		return head;
	}
	/**
	 * problem 17.13 follow up
	 * use recursion
	 */
	public BiNode bstToDLII(BiNode root) {
		if(root == null) return root;
		BiNode head = root;
		while(head.node1 != null) head = head.node1;
		BiNode[] pre = new BiNode[1];
		bstToDLII(root,pre);
		return head;
	} 
	private void bstToDLII(BiNode root, BiNode[] pre) {
		if(root != null) {
			bstToDLII(root.node1,pre);
			root.node1 = pre[0];
			if(pre[0] != null) pre[0].node2 = root;
			pre[0] = root;
			bstToDLII(root.node2,pre);
		}
	}
	/**
	 * problem 17.14
	 * word break, find the minimal breaks
	 * dynamical programming
	 */
	public int optimalBreak(Set<String> set, String s) {
		if(set == null || set.size() == 0 || s == null || s.length() == 0) return 0;
		int[] dp = new int[s.length()+1];
		for(int i = 1; i < dp.length; i++) {
			dp[i] = -1;
		}
		for(int i = 1; i <= s.length(); i++) {
			for(int j = i - 1; j >= 0; j--) {
				String sub = s.substring(j,i);
				if(set.contains(sub)) {
					dp[i] = dp[i] == -1 || dp[i] > dp[j] + 1 ? dp[j]+1 : dp[i];
				}
			}
		}
		return dp[dp.length-1];
	}
	/**
	 * problem 17.14 follow up
	 * find the solution
	 */
	public List<String> optimalBreakII(Set<String> set, String s) {
		if(set == null || set.size() == 0 || s == null || s.length() == 0) return new LinkedList<String>();
		int[] dp = new int[s.length()+1];
		List<String>[] sols = (LinkedList<String>[]) new LinkedList[s.length()+1];
		for(int i = 0; i < sols.length; i++) {
			sols[i] = new LinkedList<String>();
			dp[i] = -1;
		}
		dp[0] = 0;
		for(int i = 1; i <= s.length(); i++){
			for(int j = i - 1; j >= 0; j--) {
				String sub = s.substring(j,i);
				if(set.contains(sub)) {
					if(dp[i] == -1 || dp[i] > dp[j] + 1) {
						dp[i] = dp[j] + 1;
						List<String> ls = new LinkedList<String>(sols[j]);
						ls.add(sub);
						sols[i] = ls;
					}
				}
			}
		}
		return sols[sols.length-1];
	}

	public static void main(String[] args) {
		Chapter17 sol = new Chapter17();
		Set<String> set = new HashSet<String>();
		for(char c = 'a'; c <= 'z'; c++) {
			set.add(c+"");
		}
		String[] strs = new String[]{"jess","looked","just","li","ke","like","tim","time","her","brother"};
		for(String s : strs) set.add(s);

		System.out.println(sol.optimalBreak(set,"jesslookedjustlikeherbrother"));
		List<String> res = sol.optimalBreakII(set,"jesslookedjustlikeherbrother");
		for(String s : res){
		System.out.println(s);
		}
	}
}
class Piece {
	public String name;
	public Piece(String n) {
		name = n;
	}
}
class MasterMindNode {
	public int hit;
	public int phit;
	public MasterMindNode() {
		hit = 0;
		phit = 0;
	}
}
class Pair {
	public int a;
	public int b;
	public Pair(int v1, int v2) {
		a = v1;
		b = v2;
	}
	public String toString() {
		return "("+a+","+b+")";
	}
}
class BiNode {
	public BiNode node1;
	public BiNode node2;
	public int data;
	public BiNode(int d) {
		data = d;
		node1 = null;
		node2 = null;
	}
}
/**
 * problem 17.9
 * find the frequency of any given word in a book
 * just use hashmap or trie to pre-process the book and provide O(1) visit time
 */
class EmptyStringInputException extends RuntimeException{}
class WordLoopup {
	private HashMap<String,Integer> map;
	public WordLoopup(String[] strs) {
		if(strs == null || strs.length == 0) throw new EmptyStringInputException();
		map = new HashMap<String,Integer>();
		for(String str : strs) {
			if(str.trim() == "") continue;
			str = str.toLowerCase();
			if(!map.containsKey(str)) {
				map.put(str,1);
			} else {
				map.put(str,map.get(str)+1);
			}
		}
	}
	public int getFrequency(String s) {
		if(s == null || s.length() == 0 || !map.containsKey(s.toLowerCase())) return 0;
		return map.get(s.toLowerCase());
	}
}	

