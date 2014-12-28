import java.util.*;

/**
 * This file contains solutions for all problems in Chapter9
 * 12/26/2014
 * Dynamical programming + Recursion
 */

public class Chapter9 {
	public static void main(String[] args) {
		Chapter9 sol = new Chapter9();
		System.out.println(sol.numParen("1^0|0|1",true));
		List<String> res = sol.numParenII("1^0|0|1",true);
		for(String s : res)
			System.out.println(s);
	}
	/**
	* problem 9.1. climb steps
	* find the ways to climb a N steps
	* can hop 1, 2 and 3 steps at a time
	* bottom up DP
	* F(n) = F(n-1) + F(n-2) + F(n-3)
	* Time complexity: O(N)
	* Space complexity: O(N)
	*/
	public int countSteps(int N) {
		if(N <= 0) return 0;
		int[] dp = new int[N+1];
		dp[1] = 1;
		dp[2] = 2;
		dp[3] = 4;
		if(N <= 3) return dp[N];
		for(int i = 4; i <= N; i++) {
			dp[i] = dp[i-1] + dp[i-2] + dp[i-3];
		}
		return dp[N];
	}
	/**
	 * problem 9.1 follow up: count the minimal way
	 */
	public int minSteps(int N) {
		if(N <= 0) return 0;
		if(N <= 3) return 1;
		int[] jumps = new int[]{1,2,3};
		int[] dp = new int[N+1];
		dp[1] = 1;
		dp[2] = 1;
		dp[3] = 1;
		for(int i = 1; i <= N; i++) {
			for(int j = 0; j < jumps.length; j++) {
				if(i >= jumps[j]) {
					dp[i] = dp[i] == 0 || dp[i] > dp[i-jumps[j]] + 1 ? dp[i-jumps[j]] + 1 : dp[i];
				}
			}
		}
		return dp[N];
	}
	/**
	 * problem 9.1 follow up. find all solution to climb a step N
	 * DFS
	 */
	public List<List<Integer>> allSteps(int N) {
		List<List<Integer>> res = new LinkedList<List<Integer>>();
		if(N <= 0) return res;
		List<Integer> path = new LinkedList<Integer>();
		int[] map = new int[]{1,2,3};
		dfs(N,map,path,res);
		return res;
	}
	private void dfs(int N, int[] map, List<Integer> path, List<List<Integer>> res) {
		if(N < 0) return;
		//find a solution
		if(N == 0) {
			res.add(new LinkedList<Integer>(path));
			return;
		}
		for(int i = 0; i < map.length; i++) {
			path.add(map[i]);
			dfs(N - map[i],map,path,res);
			path.remove(path.size()-1);
		}
	}
	/**
	 * problem 9.2
	 * robot
	 * 1D dynamical programming
	 * 
	 */
	public int uniquePath(int M, int N) {
		if(M <= 0 || N <= 0) return 0;
		int[] dp = new int[N];
		for(int i = 0; i < dp.length; i++) {
			dp[i] = 1; //initialization
		}
		for(int i = 1; i < M; i++) {
			for(int j = 1; j < N; j++) {
				dp[j] += dp[j-1];
			}
		}
		return dp[N-1];
	}
	/**
	 * problem 9.2 follow up
	 * some spots are marked as unpassed
	 * just mark dp[i] = 0 if meets obstacle. 
	 */
	public int uniquePathII(int[][] matrix) {
		if(matrix == null || matrix.length == 0) return 0;
		int m = matrix.length;
		int n = matrix[0].length;
		if(matrix[0][0] == 0 || matrix[m-1][n-1] == 0) return 0;
		int[] dp = new int[n];
		dp[0] = 1;
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if(matrix[i][j] == 0) {
					dp[j] = 0;
				} else {
					if(j > 0) {
						dp[j] += dp[j-1];
					}
				}
			}
		}
		return dp[n-1];
	}
	/**
	 * problem 9.3
	 * find the magical index. such that A[i] = i
	 * given a sorted array, find a magical index if exists.
	 * all values are distinct
	 * binary search
	 * Time complexity: O(log(N))
	 * space complexity: O(1)
	 */
	public int magicIndex(int[] A) {
		if(A == null || A.length == 0) return -1;
		int L = 0;
		int R = A.length - 1;
		while(L <= R) {
			int mid = L + (R - L) / 2;
			if(A[mid] == mid) return mid;
			else if(A[mid] < mid) L = mid + 1;
			else R = mid - 1;
		}
		return -1;
	}
	/**
	 * problem 9.3 follow up
	 * if values are not distinct
	 * need to search two sides
	 * Time complexity: O(N) in worst case
	 * Space complexity: O(log(N))
	 */
	public int magicIndexII(int[] A) {
		if(A == null || A.length == 0) return -1;
		return helper(A,0,A.length - 1);
	}
	private int helper(int[] A, int start, int end) {
		if(start > end) return -1;
		int mid = start + (end - start) / 2;
		if(A[mid] == mid) return mid;
		int left = helper(A,start,mid-1);
		if(left != -1) return left;
		return helper(A,mid+1,end);
	}
	/**
	 * problem 9.4
	 * find all subsets of a set (powerset)
	 * may contains duplicates
	 * recursion (DFS)
	 * Time complexity: O(2^N)
	 * Space complexity: O(2^N)
	 */
	public List<List<Integer>> allSubsets(int[] A) {
		List<List<Integer>> res = new LinkedList<List<Integer>>();
		if(A == null || A.length == 0) return res;
		Arrays.sort(A);
		List<Integer> path = new LinkedList<Integer>();
		dfs(A,0,path,res);
		res.add(new LinkedList<Integer>());
		return res;
	}
	private void dfs(int[] A, int start, List<Integer> path, List<List<Integer>> res) {
		if(start >= A.length) return;
		for(int i = start; i < A.length; i++) {
			path.add(A[i]);
			res.add(new LinkedList<Integer>(path));
			dfs(A,i+1,path,res);
			path.remove(path.size()-1);
			while(i < A.length - 1 && A[i] == A[i+1]) i++; //jump duplicates
		}
	}
	/**
	 * follow up. use iteration
	 */
	public List<List<Integer>> allSubsetsII(int[] A) {
		List<List<Integer>> res = new LinkedList<List<Integer>>();
		if(A == null || A.length == 0) return res;
		Arrays.sort(A);
		res.add(new LinkedList<Integer>());
		int start = 0;
		for(int i = 0; i < A.length; i++) {
			int size = res.size();
			for(int j = start; j < size; j++) {
				List<Integer> ls = new LinkedList<Integer>(res.get(j));
				ls.add(A[i]);
				res.add(ls);
			}
			//check duplicates
			if(i < A.length - 1 && A[i] == A[i+1]) {
				start = size;
			} else {
				start = 0;
			}
		}
		return res;
	}
	/**
	 * problem 9.5
	 * write a method to compute all permutation of a string
	 * duplicates may exist.
	 * Time complexity: O(n!)
	 */
	public List<String> allPermutations(String s) {
		List<String> res = new LinkedList<String>();
		if(s == null || s.length() == 0) return res;
		char[] chs = s.toCharArray();
		List<Character> path = new LinkedList<Character>();
		boolean[] visited = new boolean[chs.length];
		dfs(chs,visited,path,res);
		return res;
	}
	private void dfs(char[] chs, boolean[] visited, List<Character> path, List<String> res) {
		//find a solution
		if(path.size() == chs.length) {
			StringBuilder sb = new StringBuilder();
			for(Character c : path) {
				sb.append(c);
			}
			res.add(sb.toString()); return;
		}
		for(int i = 0; i < chs.length; i++) {
			if(! visited[i]) {
				visited[i] = true;
				path.add(chs[i]);
				dfs(chs,visited,path,res);
				path.remove(path.size()-1);
				visited[i] = false;
				while(i < chs.length - 1 && chs[i] == chs[i+1]) i++;//jump duplicates
			}
		}
	}
	/**
	 * follow up. use iteration
	 * use set to remove duplicates
	 */
	public List<String> allPermutationsI(String s) {
		LinkedList<String> res = new LinkedList<String>();
		if(s == null || s.length() == 0) return res;
		HashSet<String> set = new HashSet<String>();
		res.add("");
		for(int i = 0; i < s.length(); i++) {
			int size = res.size();
			for(int j = 0; j < size; j++) {
				String x = res.poll();
				for(int k = 0; k <= x.length(); k++) {
					String nstr = x.substring(0,k) + s.charAt(i) + x.substring(k);
					if(! set.contains(nstr)) {
						set.add(nstr);
						res.add(nstr);
					}
				}
			}
		}
		return res;
	}



	
	/**
	 * follow up. if only lowcase can be permutated
	 */
	public List<String> allPermutationsII(String s) {
		List<String> res = new LinkedList<String>();
		if(s == null || s.length() == 0) return res;
		char[] chs = s.toCharArray();
		List<Character> path = new LinkedList<Character>();
		boolean[] visited = new boolean[chs.length];
		dfsII(chs,visited,path,res);
		return res;
	}
	private void dfsII(char[] chs, boolean[] visited, List<Character> path, List<String> res) {
		if(path.size() == chs.length) {
			StringBuilder sb = new StringBuilder();
			for(Character c : path) {
				sb.append(c);
			}
			res.add(sb.toString()); return;
		}
		for(int i = 0; i < chs.length; i++) {
			if(chs[i] < 'a' || chs[i] > 'z') {
				//we can put chs[i] here
				if(path.size() == i) {
					path.add(chs[i]);
					dfsII(chs,visited,path,res);
					path.remove(path.size()-1);
				}
			} else {
				if(!visited[i]) {
					visited[i] = true;
					path.add(chs[i]);
					dfsII(chs,visited,path,res);
					visited[i] = false;
					path.remove(path.size()-1);
				}
			}
		}
	}
	/**
	 * problem 9.6
	 * generate all valid parenthesis
	 * DFS
	 * recursion
	 */
	public List<String> generateParen(int N) {
		List<String> res = new LinkedList<String>();
		if(N < 1) return res;
		dfs(N,0,0,"",res);
		return res;
	}
	private void dfs(int N, int left, int right, String path, List<String> res) {
		if(left == N && right == N) {
			res.add(path); return;
		}
		if(left == N) {
			dfs(N,left,right+1,path+")",res);
		} else {
			dfs(N,left+1,right,path+"(",res);
			if(left > right) {
				dfs(N,left,right+1,path+")",res);
			}
		}
	}
	/**
	 * problem 9.6 follow up
	 * iteration using stack
	 * Time complexity: O(2^N)
	 * Space complexity: O(N)
	 */
	public List<String> generateParenII(int N) {
		List<String> res = new LinkedList<String>();
		if(N < 1) return res;
		Stack<Character> stack = new Stack<Character>();
		stack.push('(');
		int left = 1;
		int right = 0;
		while(! stack.isEmpty()) {
			System.out.println(stack.size());
			if(stack.size() < N * 2) {
				if(left < N) {
					stack.push('(');
					left++;
				}
				else {
					stack.push(')');
					right++;
				}
			}
			if(stack.size() == N * 2) {
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < stack.size(); i++) {
					sb.append(stack.get(i));
				}
				res.add(sb.toString());
				//-------key code
				//find a valid position to replace '(' to ')'
				while(! stack.isEmpty() && (stack.peek() == ')' || left <= right + 1)) {
					char c = stack.pop();
					if(c == '(') left--;
					else
					right--;
				}
				if(stack.isEmpty()) break; //no any other solution
				//replace a '(' as ')'
				stack.pop();
				left--;
				stack.push(')');
				right++;
				//-----------
			}
		}
		return res;
	}
	/**
	 * problem 9.6 follow up
	 * iteration with queue
	 * Time complexity: O(2^N)
	 * Space complexity: O(2^N)
	 */
	public List<String> generateParenIII(int N) {
		List<String> res = new LinkedList<String>();
		if(N <= 0) return res;
		class Element {
			private int left;
			private int right;
			private String str;
			public Element(int l, int r, String s) {
				left = l;
				right = r;
				str = s;
			}
		}
		Queue<Element> q = new LinkedList<Element>();
		q.add(new Element(0,0,""));
		while(! q.isEmpty()) {
			System.out.println(q.size());
			Element ele = q.poll();
			if(ele.left == N && ele.right == N) {
				res.add(ele.str);
				continue;
			}
			if(ele.left < N) {
				q.add(new Element(ele.left+1,ele.right,ele.str+"("));
			}
			if(ele.left > ele.right) {
				q.add(new Element(ele.left,ele.right+1,ele.str+")"));
			}
		}
		return res;
	}
	/**
	 * problem 9.7
	 * implement paint fill
	 * BFS or DFS
	 * given a point and a new color, fill in surrouned region with the new color
	 */
	//DFS
	public void paintFill(int[][] array, int r, int c, int newcolor) {
		dfs(array,r,c,array[r][c],newcolor);
	}
	private void dfs(int[][] array, int r, int c, int oldcolor, int newcolor) {
		if(r < 0 || r >= array.length || c < 0 || c >= array[0].length) return;
		if(array[r][c] != oldcolor) return;
		array[r][c] = newcolor;
		dfs(array,r-1,c,oldcolor,newcolor);
		dfs(array,r+1,c,oldcolor,newcolor);
		dfs(array,r,c-1,oldcolor,newcolor);
		dfs(array,r,c+1,oldcolor,newcolor);
	}
	/**
	 * BFS
	 */
	public void paintFillII(int[][] array, int r, int c, int newcolor) {
		if(r < 0 || r >= array.length || c < 0 || c >= array[0].length) return;
		int oldcolor = array[r][c];
		Queue<Integer> rows = new LinkedList<Integer>();
		Queue<Integer> cols = new LinkedList<Integer>();
		rows.add(r);
		cols.add(c);
		while(! rows.isEmpty()) {
			int row = rows.poll();
			int col = cols.poll();
			array[row][col] = newcolor;
			if(row > 0 && array[row-1][col] == oldcolor) {
				rows.add(row-1);
				cols.add(col);
			}
			if(row < array.length - 1 && array[row+1][col] == oldcolor) {
				rows.add(row+1);
				cols.add(col);
			}
			if(col > 0 && array[row][col-1] == oldcolor) {
				rows.add(row);
				cols.add(col-1);
			}
			if(col < array[0].length - 1 && array[row][col+1] == oldcolor) {
				rows.add(row);
				cols.add(col+1);
			}
		}
	}
	/**
	 * problem 9.8
	 * given some cents, calculate the number of representing a cent
	 * we cannot use dp as used in count steps, since (1,2,5) is the same with (1,5,2)
	 * DFS
	 * cents is in decreasing order
	 */
	public int calculateCents(int[] cents, int N) {
		return dfs(cents,0,N);
	}
	private int dfs(int[] cents, int start, int N) {
		if(N < 0) return 0;
		if(N == 0) return 1;
		if(start == cents.length - 1) {
			if(N % cents[start] == 0) return 1;
			return 0;
		}
		int sum = 0;
		for(int i = 0; i <= N / cents[start]; i++) {
			sum += dfs(cents,start+1,N-cents[start] * i);
		}
		return sum;
	}
	/**
	 * problem 9.8 follow up
	 * compute all combinations
	 */
	public List<List<Integer>> calculateCentsII(int[] cents, int N) {
		List<List<Integer>> res = new LinkedList<List<Integer>>();
		if(N <= 0) return res;
		List<Integer> path = new LinkedList<Integer>();
		dfs(cents,0,N,path,res);
		return res;
	}
	private void dfs(int[] cents, int start, int N, List<Integer> path, List<List<Integer>> res) {
		if(N < 0) return;
		if(N == 0) {
			res.add(new LinkedList<Integer>(path)); return;
		}
		//get last cents
		if(start == cents.length - 1) {
			if(N % cents[start] == 0) {
				List<Integer> ls = new LinkedList<Integer>(path);
				for(int i = 0; i < N / cents[start]; i++) {
					ls.add(cents[start]);
				}
				res.add(ls);
				return;
			}
		}
		for(int i = 0; i <= N / cents[start]; i++) {
			//add cents[start] with i times
			for(int j = 0; j < i; j++) {
				path.add(cents[start]);
			}

			dfs(cents,start+1,N-cents[start]*i,path,res);

			for(int j = 0; j < i; j++) {
				path.remove(path.size()-1);
			}
		}
	}
	/**
	 * problem 9.8 follow up
	 * find the minimal number of cent ways
	 */
	public int miniCents(int[] cents, int N) {
		if(N <= 0) return 0;
		int[] dp = new int[N+1];
		for(int i = 1; i <= N; i++) {
			dp[i] = -1;
			for(int j = 0; j < cents.length; j++) {
				if(i >= cents[j] && dp[i-cents[j]] != -1) {
					dp[i] = dp[i] == -1 || dp[i] > dp[i-cents[j]] + 1 ? dp[i-cents[j]] + 1 : dp[i]; 
				}
			}
		}
		return dp[N];
	}
	/**
	 * problem 9.9
	 * 8-queen
	 */
	public List<String[]> NQueue(int N) {
		List<String[]> res = new LinkedList<String[]>();
		int[] places = new int[N]; //place queue i at j
		dfs(places,0,res);
		return res;
	}
	private void dfs(int[] places, int row, List<String[]> res) {
		if(row == places.length) {
			res.add(compute(places));
			return;
		}
		//try to find a solution
		for(int i = 0; i < places.length; i++) {
			if(isvalid(places,row,i)) {
				places[row] = i;
				dfs(places,row+1,res);
			}
		}
	}
	private boolean isvalid(int[] places, int row, int col) {
		for(int i = 0; i < row; i++) {
			if(places[i] == col || Math.abs(i-row) == Math.abs(places[i] - col)) return false;
		}
		return true;
	}
	private String[] compute(int[] places) {
		String[] strs = new String[places.length];
		for(int i = 0; i < places.length; i++) {
			StringBuilder sb = new StringBuilder();
			for(int j = 0; j < places.length; j++) {
				sb.append(j != places[i] ? '.' : 'Q');
			}
			strs[i] = sb.toString();
		}
		return strs;
	}

	/**
	 * problem 9.10
	 * find the tallest stack height
	 * dynamical programming
	 */
	public int maxHeight(Box[] boxes) {
		if(boxes == null || boxes.length == 0) return 0;
		//sort according to h
		Arrays.sort(boxes, new Comparator<Box>(){
			public int compare(Box b1, Box b2) {
				if(b1.H == b2.H) return 0;
				if(b1.H < b2.H) return 1;
				return -1;
			}
		});
		int[] dp = new int[boxes.length];
		for(int i = 0; i < boxes.length; i++) {
			dp[i] = boxes[i].H;
		}

		int max = 0;
		for(int i = 1; i < boxes.length; i++) {
			for(int j = i - 1; j >= 0; j--) {
				if(canBeAbove(boxes[i],boxes[j])) {
					dp[i] = Math.max(dp[i],boxes[i].H + dp[j]);
				}
			}
			max = max < dp[i] ? dp[i] : max;
		}
		return max;
	}
	//check whether b1 can be above b2
	private boolean canBeAbove(Box b1, Box b2) {
		return b1.H < b2.H && b1.W < b2.W && b1.L < b2.L;
	}

	/**
	 * follow up, find the solution
	 * DFS
	 */
	 public List<Box> maxHeightII(Box[] boxes) {
	 	List<List<Box>> res = new LinkedList<List<Box>>();
	 	res.add(new LinkedList<Box>());
	 	if(boxes == null || boxes.length == 0) return res.get(0);
	 	List<Box> path = new LinkedList<Box>();
	 	int[] h = new int[]{0};
	 	Arrays.sort(boxes,new Comparator<Box>(){
	 		public int compare(Box b1, Box b2) {
	 			if(b1.H == b2.H) return 0;
	 			if(b1.H < b2.H) return 1;
	 			return -1;
	 		}
	 	});
	 	dfs(boxes,0,0,h,path,res);
	 	return res.get(0);
	 }
	 private void dfs(Box[] boxes, int start, int currh, int[] h, List<Box> path, List<List<Box>> res) {
	 	if(start == boxes.length || (! path.isEmpty() && !(canBeAbove(boxes[start],path.get(0))))) {
	 		if(h[0] < currh) {
	 			h[0] = currh;
	 			res.set(0,new LinkedList<Box>(path));
	 		}
	 		return;
	 	}
	 	path.add(boxes[start]);
	 	for(int i = start + 1; i < boxes.length; i++) {
	 		if(canBeAbove(boxes[i],path.get(0))) {
	 			path.add(boxes[i]);
	 			dfs(boxes,i+1,currh+boxes[i].H,h,path,res);
	 			path.remove(path.size()-1);
	 		}
	 	}
	 }

	 /**
	  * use DP: LIS
	  */
	 public List<Box> maxHeightIII(Box[] boxes) {
	 	if(boxes == null || boxes.length == 0) return new LinkedList<Box>();
	 	HashMap<Box,List<Box>> map = new HashMap<Box,List<Box>>();
	 	Arrays.sort(boxes, new Comparator<Box>(){
	 		public int compare(Box b1, Box b2) {
	 			if(b1.H == b2.H) return 0;
	 			if(b1.H < b2.H) return -1;
	 			return 1;
	 		}
	 	});
	 	for(int i = 0; i < boxes.length; i++) {
	 		List<Box> ls = new LinkedList<Box>();
	 		ls.add(boxes[i]);
	 		map.put(boxes[i],ls);
	 		for(int j = i - 1; j >= 0; j--) {
	 			if(canBeAbove(boxes[j],boxes[i])) {
	 				if(stackheight(map.get(boxes[i])) < stackheight(map.get(boxes[j])) + boxes[i].H) {
	 					List<Box> tmp = new LinkedList<Box>(map.get(boxes[j]));
	 					tmp.add(boxes[i]);
	 					map.put(boxes[i],tmp);
	 				}
	 			}
	 		}
	 	}
	 	List<Box> best = null;
	 	int max = 0;
	 	for(Box key : map.keySet()) {
	 		int h = stackheight(map.get(key));
	 		if(h > max) {
	 			max = h;
	 			best = map.get(key);
	 		}
	 	}
	 	return best;
	 }
	 private int stackheight(List<Box> ls) {
	 	int res = 0;
	 	for(Box b : ls) res += b.H;
	 	return res;
	 }
	 /**
	  * problem 9.11
	  * given a boolean expression and a desired result, find the number of parenthezing
	  * we can use a cache to cache the intermediate result
	  */
	 public int numParen(String s, boolean result) {
	 	HashMap<String,Integer> map = new HashMap<String,Integer>();
	 	return helper(s,result,map);
	 }
	 private int helper(String s, boolean result, HashMap<String,Integer> map) {
	 	if(s == null || s.length() == 0) return 0;
	 	if(s.length() == 1) {
	 		if(s.charAt(0) == '1' && result || s.charAt(0) == '0' && !result) return 1;
	 		return 0;
	 	}
	 	if(map.containsKey(s+result)) return map.get(s+result);
	 	int res = 0;
	 	if(result) {
		 	for(int i = 0; i < s.length() - 1; i++) {
		 		if(s.charAt(i) == '&') {
		 			res += helper(s.substring(0,i),true,map) * helper(s.substring(i+1),true,map);
		 		} else if(s.charAt(i) == '^') {
		 			res += helper(s.substring(0,i),true,map) * helper(s.substring(i+1),false,map) + 
		 			helper(s.substring(0,i),false,map) * helper(s.substring(i+1),true,map);
		 		} else if(s.charAt(i) == '|') {
		 			res += helper(s.substring(0,i),true,map) * helper(s.substring(i+1),true,map) + 
		 			helper(s.substring(0,i),false,map) * helper(s.substring(i+1),true,map)+
		 			helper(s.substring(0,i),true,map) * helper(s.substring(i+1),false,map);
		 		}
		 	}
	 	} else {
	 		for(int i = 0; i < s.length() - 1; i++) {
		 		if(s.charAt(i) == '&') {
		 			res += helper(s.substring(0,i),false,map) * helper(s.substring(i+1),true,map) +
		 			helper(s.substring(0,i),true,map) * helper(s.substring(i+1),false,map) +
		 			helper(s.substring(0,i),false,map) * helper(s.substring(i+1),false,map);
		 		} else if(s.charAt(i) == '^') {
		 			res += helper(s.substring(0,i),true,map) * helper(s.substring(i+1),true,map) + 
		 			helper(s.substring(0,i),false,map) * helper(s.substring(i+1),false,map);
		 		} else if(s.charAt(i) == '|') {
		 			res += helper(s.substring(0,i),false,map) * helper(s.substring(i+1),false,map);
		 		}
		 	}
	 	}
	 	map.put(s+result,res);
	 	return res;
	 }
	 /**
	  * follow up. find all the solution
	  */
	 public List<String> numParenII(String s, boolean result) {
	 	HashMap<String,List<String>> map = new HashMap<String,List<String>>();
	 	return helperII(s,result,map);
	 }
	 private List<String> helperII(String s, boolean result, HashMap<String,List<String>> map) {
	 	if(s == null || s.length() == 0) return new LinkedList<String>();
	 	List<String> res = new LinkedList<String>();
	 	if(s.length() == 1) {
		 	if(s.charAt(0) == '1' && result || s.charAt(0) == '0' && ! result) {
		 		res.add(s);
		 	}
		 	return res;
	 	}
	 	if(map.containsKey(s+result)) return map.get(s+result);
	 	if(result) {
		 	for(int i = 0; i < s.length() - 1; i++) {
		 		if(s.charAt(i) == '&') {
		 			addResult(res,helperII(s.substring(0,i),true,map),helperII(s.substring(i+1),true,map),"&");
		 		} else if(s.charAt(i) == '^') {
		 			addResult(res,helperII(s.substring(0,i),true,map),helperII(s.substring(i+1),false,map),"^");
		 			addResult(res,helperII(s.substring(0,i),false,map),helperII(s.substring(i+1),true,map),"^");
		 		} else if(s.charAt(i) == '|') {
		 			addResult(res,helperII(s.substring(0,i),true,map),helperII(s.substring(i+1),true,map),"|");
					addResult(res,helperII(s.substring(0,i),false,map),helperII(s.substring(i+1),true,map),"|");
					addResult(res,helperII(s.substring(0,i),true,map),helperII(s.substring(i+1),false,map),"|");
		 		}
		 	}
	 	} else {
	 		for(int i = 0; i < s.length() - 1; i++) {
		 		if(s.charAt(i) == '&') {
		 			addResult(res,helperII(s.substring(0,i),false,map),helperII(s.substring(i+1),true,map),"&");
		 			addResult(res,helperII(s.substring(0,i),true,map),helperII(s.substring(i+1),false,map),"&");
		 			addResult(res,helperII(s.substring(0,i),false,map),helperII(s.substring(i+1),false,map),"&");
		 		} else if(s.charAt(i) == '^') {
		 			addResult(res,helperII(s.substring(0,i),true,map),helperII(s.substring(i+1),true,map),"^");
		 			addResult(res,helperII(s.substring(0,i),false,map),helperII(s.substring(i+1),false,map),"^");
		 		} else if(s.charAt(i) == '|') {
		 			addResult(res,helperII(s.substring(0,i),false,map),helperII(s.substring(i+1),false,map),"|");
		 		}
		 	}
	 	}
	 	map.put(s+result,res);
	 	return res;
	 }
	 private void addResult(List<String> res, List<String> left, List<String> right, String token) {
	 	for(String l : left) {
		 	for(String r : right) {
		 		res.add("("+l+token+r+")");
		 	}
		}
	 }
}

class Box {
	public int H;
	public int W;
	public int L;
	public Box(int l, int w, int h) {
		H = h;
		W = w;
		L = l;
	}
	public String toString() {
		return "(L:"+L+" W:"+W+" H:"+H+")";
	}
}








