import java.util.*;

/**
 * This file contains all solutions for the problem in CTCT chapter 4
 * 12.23.2014 by Dianshi
 */


public class Chapter4 {
	/**
	 * problem 1: check whether a tree is balanced, namely the height difference of left subtree and right subtree
	 * is not larger than 1 for all subtree
	 * Time complexity: O(N). every node is visited for one time
	 * Space complexity: O(log(N)) for recursion
	 */
	public boolean isBalanced(TreeNode root) {
		if(root == null) return false;
		return height(root) != -1;
	}
	//height return the maximum height rooted at root
	//return -1 if not balanced
	private int height(TreeNode root) {
		if(null == root) return 0;
		int left = height(root.left);
		if(left == -1) return -1;
		int right = height(root.right);
		if(right == -1) return -1;
		if(Math.abs(left - right) > 1) {
			return -1;
		}
		return left < right ? right + 1 : left + 1;
	}
	/**
	 * problem 1, follow up. implement tree balanced iteratively
	 * we can use post traveral to do this work
	 * Time complexity: O(N)
	 * Space complexity: O(log(N))
	 */
	public boolean isBalancedII(TreeNode root) {
		if(root == null) return false;
		Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.push(root);
		TreeNode pre = null;
		TreeNode p;
		//record the height for every node
		HashMap<TreeNode,Integer> map = new HashMap<TreeNode,Integer>();
		map.put(null,0); //null node's has height 0. it makes code clean
		while(! stack.isEmpty()) {
			p = stack.peek();
			//if a node is a leaf node or its left and right children have been visited, visit it
			if((p.left == null && p.right == null) || pre != null && (pre == p.left || pre == p.right)) {
				int left_height = map.get(p.left); //height of left subtree
				int right_height = map.get(p.right);//heigh of right subtree
				if(Math.abs(left_height - right_height) > 1) return false; //not balanced at p
				map.put(p,Math.max(left_height,right_height)+ 1); //compute height of p
				//if p.left or p.right is not null, remove them from the map since they will not be used again.
				if(p.left != null) map.remove(p.left);
				if(p.right != null) map.remove(p.right);
				pre = p;
				stack.pop();
			}
			else {
				if(p.right != null) stack.push(p.right);
				if(p.left != null) stack.push(p.left);
			}
		}
		return true;
	}
	/**
	 * problem 2
	 * check whether there is a path between two nodes a,b in a directed graph
	 * Just need to use DFS or BFS. here we use DFS
	 * Time complexity: O(E+V)
	 * Space complexity: O(E+V)
	 */
	
	public boolean hasPath(GraphNode a, GraphNode b) {
		if(a == null || b == null) return false;
		HashSet<GraphNode> set = new HashSet<GraphNode>();// use a set to avoid cycle
		return dfs(a,b,set);
	}
	private boolean dfs(GraphNode a, GraphNode b, HashSet<GraphNode> set) {
		if(a == b) return true;
		set.add(a);
		for(GraphNode n : a.adjs) {
			if(!set.contains(n)) {
				set.contains(n);
				if(dfs(n,b,set)) return true;
			}
		}
		return false;
	}
	/**
	 * problem 2 followup: use iteration
	 * Time complexity: O(E+V)
	 */
	public boolean hasPathII(GraphNode a, GraphNode b) {
		if(a == null || b == null) return false;
		Stack<GraphNode> stack = new Stack<GraphNode>();
		HashSet<GraphNode> set = new HashSet<GraphNode>();
		stack.push(a);
		while(! stack.isEmpty()) {
			GraphNode n = stack.peek();
			if(n == b) return true;
			boolean allvisited = true; //check whether all children have been visited
			for(GraphNode node : n.adjs) {
				if(! set.contains(node)) {
					allvisited = false;
					stack.push(node);
					set.add(node);
					break;//when find a child, break
				}
			}
			if(allvisited) {
				stack.pop();
			}
		}
		return false;
	}

	/**
	 * problem 3
	 * create bst from sorted array
	 * O(N)
	 */
	public TreeNode createBST(int[] array) {
		if(array == null || array.length == 0) return null;
		return createBST(array,0,array.length-1);
	}
	private TreeNode createBST(int[] array, int start, int end) {
		if(start > end) return null;
		int mid = start + (end - start) / 2;
		TreeNode root = new TreeNode(array[mid]);
		root.left = createBST(array,start,mid-1);
		root.right = createBST(array,mid+1,end);
		return root;
	}
	/**
	 * problem 3 follow up.
	 * create bst from sorted linkedlist
	 * O(N)
	 * use inorder traversal
	 */
	public TreeNode createBSTFromList(ListNode ls) {
		if(ls == null) return null;
		ListNode p = ls;
		//compute list length
		int len = 0;
		while(p != null) {
			len++;
			p = p.next;
		}
		ListNode[] head = new ListNode[]{ls};
		return createBSTFromList(head,0,len-1);
	}
	private TreeNode createBSTFromList(ListNode[] head, int start, int end) {
		if(start > end) return null;
		int mid = start + (end - start) / 2;
		TreeNode left = createBSTFromList(head,start, mid-1);
		TreeNode root = new TreeNode(head[0].val);
		head[0] = head[0].next;
		TreeNode right = createBSTFromList(head,mid+1,end);
		root.left = left;
		root.right = right;
		return root;
	}

	public void inOrder(TreeNode t) {
		if(t != null) {
			inOrder(t.left);
			System.out.print(t.val+" ");
			inOrder(t.right);
		}
	}

	/**
	 * problem 4
	 * create linked list for every level of a tree
	 * just use level traversal
	 * Time complexity: O(N)
	 * Space complexity: O(N)
	 * BFS
	 */
	public List<List<TreeNode>> createLevelNodes(TreeNode root) {
		List<List<TreeNode>> res = new LinkedList<List<TreeNode>>();
		LinkedList<TreeNode> q = new LinkedList<TreeNode>();
		q.add(root);
		while(! q.isEmpty()) {
			res.add(new LinkedList<TreeNode>(q));
			int size = q.size();
			for(int i = 0; i < size; i++) {
				TreeNode t = q.poll();
				if(t.left != null) q.add(t.left);
				if(t.right != null) q.add(t.right);
			}
		}
		return res;
	} 
	/**
	 * problem 4: follow up
	 * do it using DFS
	 */
	public List<List<TreeNode>> createLevelNodesDFS(TreeNode root) {
		List<List<TreeNode>> res = new LinkedList<List<TreeNode>>();
		int level = 0;
		dfs(root,level,res);
		return res;
	}
	private void dfs(TreeNode t, int level, List<List<TreeNode>> res) {
		if(t == null) return;
		if(res.size() == level) {
			List<TreeNode> ls = new LinkedList<TreeNode>();
			res.add(ls);
		}
		res.get(level).add(t);
		dfs(t.left,level+1,res);
		dfs(t.right,level+1,res);
	}

	public void printLevel(TreeNode t) {
		if(t == null) return;
		Queue<TreeNode> q = new LinkedList<TreeNode>();
		q.add(t);
		while(! q.isEmpty()) {
			int size = q.size();
			for(int i = 0; i < size; i++) {
				TreeNode n = q.poll();
				System.out.print(n.val+" ");
				if(n.left != null) q.add(n.left);
				if(n.right != null) q.add(n.right);
			}
			System.out.println();
		}
	}
	/**
	 * problem 5: check BST
	 * recursion
	 * Time complexity: O(N)
	 * space complexity: O(log(N))
	 */
	public boolean isBST(TreeNode t) {
		if(t == null) return false;
		return isBST(t,Integer.MIN_VALUE,Integer.MAX_VALUE);
	}
	private boolean isBST(TreeNode t, int min, int max) {
		if(t == null) return true;
		return t.val >= min && t.val <= max && isBST(t.left,min,t.val) && isBST(t.right,t.val,max);
	}
	/**
	 * problem 5: follow up use recursion
	 * use iteration
	 * Time complexity: O(N)
	 * space complexity: O(log(N))
	 */
	public boolean isBSTII(TreeNode t) {
		if(t == null) return false;
		Stack<TreeNode> stack = new Stack<TreeNode>();
		TreeNode p = t;
		TreeNode pre = null;
		while(p != null || !stack.isEmpty()) {
			while(p != null) {
				stack.push(p);
				p = p.left;
			}
			p = stack.pop();
			if(pre != null) {
				if(pre.val > p.val) return false;
			}
			pre = p;
			p = p.right;
		}
		return true;
	}

	/**
	 * problem 6
	 * find successor of a node with parent pointer
	 */
	private TreeNode leftMost(TreeNode t) {
		while(t != null && t.left != null) {
			t = t.left;
		}
		return t;
	}
	public TreeNode successor(TreeNode t) {
		if(t == null) return null;
		if(t.right != null) {
			return leftMost(t.right);
		}
		else {
			while(t.parent != null && t.parent.right == t) {
				t = t.parent;
			}
			return t.parent;
		}
	}
	/**
	 * problem 6: follow up. without parent pointer
	 * inorder traversal. binary tree
	 * Time complexity: O(N) in worst case
	 */
	public TreeNode successor(TreeNode root, TreeNode t) {
		if(t == null) return null;
		if(t.right != null) {
			return leftMost(t.right);
		}
		else {
			TreeNode p = root;
			TreeNode pre = null;
			Stack<TreeNode> stack = new Stack<TreeNode>();
			while(p != null || !stack.isEmpty()) {
				while(p != null) {
					stack.push(p);
					p = p.left;
				}
				p = stack.pop();
				if(p == t) return pre;
				pre = p;
				p = p.right;
			}
		}
		return null;
	}
	/**
	 * problem 6: followup. If the tree is binary search tree
	 * we can search for the sucessor in O(log(N)) on average
	 */
	public TreeNode successorII(TreeNode root, TreeNode t) {
		if(t == null) return null;
		if(t.right != null) return leftMost(t.right);
		TreeNode suc = null;
		while(root != null) {
			//search left
			if(t.val < root.val) {
				suc = root;
				root = root.left;
			}
			else if(t.val > root.val) {
				root = root.right;
			} else {
				break;
			}
		}
		return suc;
	}

	/**
	 * problem 4.7. without no parent pointer
	 * find the lowest common ancestor
	 * Time complexity: O(N)
	 * Space complexity: O(log(N))
	 */
	public TreeNode findLCA(TreeNode root, TreeNode t1, TreeNode t2) {
		if(root == null) return null;
		if(root == t1 || root == t2) return root;
		TreeNode left = findLCA(root.left,t1,t2);
		TreeNode right = findLCA(root.right,t1,t2);
		if(left != null && right != null) return root;
		return left == null ? right : left;
	}
	/**
	 * problem 4.7. follow up: with parent pointer
	 */
	public TreeNode findLCAII(TreeNode root, TreeNode t1, TreeNode t2) {
		int len1 = height(t1);
		int len2 = height(t2);
		TreeNode low = len1 < len2 ? t1 : t2;
		TreeNode high = low == t1 ? t2 : t1;
		int step = Math.abs(len1 - len2);
		for(int i = 0; i < step; i++) {
			high = high.parent;
		}
		while(high != low) {
			low = low.parent;
			high = high.parent;
		}
		return high;
	}

	/**
	 * problem 4.8
	 * check whether T2 is a subtree of T1
	 * Time complexity: O(N+kM). N is the node number in T1, M is node number in T2
	 * k is searching time
	 * Space complexity: O(log(N)+log(M))
	 */
	public boolean isSubTree(TreeNode t1, TreeNode t2) {
		if(t1 == null || t2 == null) return false;
		Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.push(t1);
		while(! stack.isEmpty()) {
			TreeNode t = stack.pop();
			if(identical(t,t2)) return true;
			if(t.right != null) stack.push(t.right);
			if(t.left != null) stack.push(t.left);
		}
		return false;
	}
	private boolean identical(TreeNode t1, TreeNode t2) {
		if(t1 == null && t2 == null) return true;
		if(t1 == null || t2 == null) return false;
		return t1.val == t2.val && identical(t1.left,t2.left) && identical(t1.right,t2.right);
	}
	/**
	 * problem 4.9 given a binary search tree, print all paths that sum to a given value
	 * analysis: dfs. at every node, try to search back to check
	 * Time complexity: O(nlog(n))
	 * Space complexity: O(log(n))
	 */
	public void printAllNeededPath(TreeNode t, int val) {
		if(t == null) return;
		List<Integer> path = new ArrayList<Integer>();
		xdfs(t,val,path);
	}
	private void xdfs(TreeNode t, int val, List<Integer> path) {
		if(t == null) return;
		path.add(t.val);

		//check solution
		int sum = 0;
		for(int i = path.size()-1; i >= 0; i--) {
			sum += path.get(i);
			if(sum == val) {
				for(int j = i; j < path.size(); j++) {
					System.out.print(path.get(j)+" ");
				}
				System.out.println();
			}
		}

		//find solution a left and right subtree
		xdfs(t.left,val,path);
		xdfs(t.right,val,path);
		//remove the node from the path
		path.remove(path.size()-1);
	}

	public static void main(String[] args) {

	}
}

class ListNode {
	public int val;
	public ListNode next;
	public ListNode(int v) {
		val = v;
		next = null;
	}
}
class TreeNode {
	public int val;
	public TreeNode left;
	public TreeNode right;
	public TreeNode parent;
	public TreeNode(int v) {
		val = v;
		left = null;
		right = null;
		parent = null;
	}
}
class GraphNode {
	public int val;
	public List<GraphNode> adjs;
	public GraphNode(int v) {
		val = v;
		adjs = new LinkedList<GraphNode>();
	}
	public void add(GraphNode n) {
		adjs.add(n);
	}
}

