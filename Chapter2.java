import java.util.*;

public class Chapter2 {
	public static void main(String[] args) {
		Chapter2 sol = new Chapter2();

		ListNode ls1 = new ListNode(1);
		ListNode ls2 = new ListNode(2);
		ListNode ls3 = new ListNode(3);
		ListNode ls4 = new ListNode(2);
		ListNode ls5 = new ListNode(1);
		ListNode ls6 = new ListNode(1);

		System.out.println(sol.isPalindromeII(ls1));

	}
	/**
	 * problem 2.1 remove duplicates in unsorted array
	 * use hashmap
	 * Time complexity: O(N)
	 * Space complexity: O(N)
	 */
	public ListNode removeDups(ListNode head) {
		ListNode dummy = new ListNode(0);
		ListNode p = dummy;
		HashSet<Integer> set = new HashSet<Integer>();
		while(head != null) {
			if(! set.contains(head.val)) {
				set.add(head.val);
				p.next = head;
				p = p.next;
			}
			head = head.next;
		}
		return dummy.next;
	}
	//without hashmap
	//Time complexity O(N^2)
	//Space complexity: O(1)
	public ListNode removeDupsII(ListNode head) {
		ListNode p = head;
		while(p != null) {
			ListNode q = p;
			while(q.next != null) {
				if(q.next.val == p.val) {
					q.next = q.next.next;
				} else {
					q = q.next;
				}
			}
			p = p.next;
		}
		return head;
	}
	/**
	 * problem 2.2
	 * implement an algorithm to find the kth to last element of a single linked list
	 * use two pointers
	 * Time complexity: O(N)
	 * Space complexity: O(1)
	 * iteratively
	 */
	public ListNode findKthToLast(ListNode head, int k) {
		if(k <= 0) return null;
		ListNode p = head;
		int i = 0;
		while(p != null && i < k - 1) {
			p = p.next;
			i++;
		}
		if(p == null) return null;
		ListNode q = head;
		while(p.next != null) {
			p = p.next;
			q = q.next;
		}
		return q;
	}
	//recursively
	//Time complexity: O(N)
	//Space complexity: O(N) for recursive call
	//use a variable to record the steps of back
	public ListNode findKthToLastII(ListNode head, int k) {
		int[] a = new int[]{0};
		return findKthToLastII(head,k,a);
	}
	private ListNode findKthToLastII(ListNode head, int k, int[] a) {
		if(null == head) return null;
		ListNode node = findKthToLastII(head.next,k,a);
		a[0]++;
		if(a[0] == k) {
			return head;
		}
		return node;
	}
	/**
	 * problem 2.3
	 * implement an algorithm to delete the node in a single linked list
	 * given access to that node
	 * Time complexity: O(1) on average
	 * Space complexity: O(1)
	 */
	public void removeNode(ListNode ls) {
		if(ls == null || ls.next == null) throw new UnsupportedOperationException();
		ls.val = ls.next.val;
		ls.next = ls.next.next;
	}
	/**
	 * problem 2.4
	 * partition a linked list around a value x, such that all nodes less than x come before
	 * all nodes greater than or equal to x
	 */
	public ListNode partitionList(ListNode head, int x) {
		if(head == null || head.next == null) return head;
		ListNode left = new ListNode(0);
		ListNode right = new ListNode(0);
		ListNode p = left;
		ListNode q = right;
		while(head != null) {
			if(head.val < x) {
				p.next = head;
				p = p.next;
			} else {
				q.next = head;
				q = q.next;
			}
			head = head.next;
		}
		p.next = right.next;
		q.next = null;//set null to avoid cycle
		return left.next;
	}
	/**
	 * problem 2.5
	 * Time complexity: O(N)
	 * Space complexity: O(1)
	 */
	public ListNode addNumber(ListNode l1, ListNode l2) {
		if(l1 == null) return l2;
		if(l2 == null) return l1;
		ListNode dummy = new ListNode(0);
		ListNode p = dummy;
		int carry = 0;
		while(l1 != null || l2 != null) {
			int v1 = l1 == null ? 0 : l1.val;
			int v2 = l2 == null ? 0 : l2.val;
			int v = v1 + v2 + carry;
			p.next = new ListNode(v % 10);
			carry = v / 10;
			p = p.next;
			l1 = l1 == null ? l1 : l1.next;
			l2 = l2 == null ? l2 : l2.next;
		}
		if(carry != 0) {
			p.next = new ListNode(carry);
		}
		return dummy.next;
	}
	/**
	 * follow up
	 * the number is represented in increasing order
	 * solution 1: reverse input
	 * add two lists -> get output -> reverse output
	 * reverse input
	 * solution 2: use recursion
	 * it is given here
	 * Time complexity: O(N)
	 * Space complexity: O(N)
	 */
	public ListNode addNumberII(ListNode l1, ListNode l2) {
		int len1 = listLength(l1);
		int len2 = listLength(l2);
		PartSum s = helper(l1,len1,l2,len2);
		if(s.carry == 0)
			return s.node;
		//check the last carry
		else {
			ListNode res = new ListNode(s.carry);
			res.next = s.node;
			return res;
		}
	}
	//compute the length of a list
	private int listLength(ListNode l) {
		int len = 0;
		while(l != null) {
			len++;
			l = l.next;
		}
		return len;
	} 
	//store carry and node
	class PartSum {
		public int carry;
		public ListNode node;
		public PartSum(int c, int val) {
			carry = c;
			node = new ListNode(val);
		}
	}
	//add the two lists by recursion
	public PartSum helper(ListNode l1, int len1, ListNode l2, int len2) {
		if(l1.next == null && l2.next == null) {
			int val = l1.val + l2.val;
			return new PartSum(val / 10, val % 10);
		}
		PartSum p;
		int val = 0;
		if(len1 > len2) { //when ls1 is longer, set 0 at the bit for ls2
			p = helper(l1.next,len1-1,l2,len2);
			val = l1.val;
		} else if(len1 < len2) { //when ls2 is longer, set 0 at bit for ls1
			p = helper(l1,len1,l2.next,len2-1);
			val = l2.val;
		} else { //when they are equal, add
			p = helper(l1.next,len1-1,l2.next,len2-1);
			val = l1.val + l2.val;
		}
		val = val + p.carry;
		PartSum res = new PartSum(val / 10, val % 10);
		res.node.next = p.node;
		return res;
	}
	/**
	 * iteratively
	 * use a stack!!!
	 * Time complexity: O(N)
	 * Space complexity: O(N)
	 */
	public ListNode addNumberIII(ListNode ls1, ListNode ls2) {
		int len1 = listLength(ls1);
		int len2 = listLength(ls2);
		int len = len1 > len2 ? len1 : len2;
		Stack<Integer> vals = new Stack<Integer>();
		while(len != 0) {
			int v1 = len > len1 ? 0 : ls1.val;
			int v2 = len > len2 ? 0 : ls2.val;
			vals.push(v1 + v2);
			if(len <= len1) ls1 = ls1.next;
			if(len <= len2) ls2 = ls2.next;
			len--;
		}
		ListNode p = null;
		int carry = 0;
		while(! vals.isEmpty()) {
			int v = vals.pop() + carry;
			ListNode newnode = new ListNode(v % 10);
			if(p == null) {
				p = newnode;
			} else {
				newnode.next = p;
				p = newnode;
			}
			carry = v / 10;
		}
		ListNode res = new ListNode(carry);
		if(carry != 0) {
			res.next = p;
			return res;
		}
		return p;
	}
	/**
	 * problem 2.6
	 * return the beginning of a circular list if there is loop
	 * return null if not circle
	 */
	public ListNode findCircle(ListNode ls) {
		if(ls == null || ls.next == null) return null;
		ListNode slow = ls;
		ListNode fast = ls;
		while(fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			//detect circle
			if(slow == fast) {
				slow = ls;
				while(slow != fast) {
					slow = slow.next;
					fast = fast.next;
				}
				return slow;
			}
		}
		return null;
	}
	/**
	 * problem 2.7
	 * check if a list is palindrome
	 * 1. use extra space
	 * 2. reverse the first part of the list, compare and restore the list . no extra space is needed.
	 * 3. use recursion.
	 * Time complexity: O(N)
	 * Space complexity: O(N)
	 */
	public boolean isPalindrome(ListNode ls) {
		if(null == ls) return false;
		int len = listLength(ls);
		return isPalindrome(ls,len).ispalin;
	}
	private PalinPair isPalindrome(ListNode ls, int len) {
		if(ls == null) return new PalinPair(true,ls);
		if(len == 1) return new PalinPair(true,ls.next);
		if(len == 2) return new PalinPair(ls.val == ls.next.val, ls.next.next);
		PalinPair res = isPalindrome(ls.next,len-2);
		res.ispalin = res.ispalin && ls.val == res.node.val;
		res.node = res.node.next;
		return res;
	}
	class PalinPair {
		public boolean ispalin;
		public ListNode node;
		public PalinPair(boolean pa, ListNode node) {
			ispalin = pa;
			this.node = node;
		}
	}
	/**
	 * use iteration
	 * reverse the first part, compare with the second part, restore the list
	 * Time complexity: O(N)
	 * Space complexity: O(1)
	 */
	public boolean isPalindromeII(ListNode head) {
		if(head == null) return false;
		if(head.next == null) return true;
		int len = listLength(head);
		ListNode mid = findMid(head);
		ListNode h2 = mid.next;
		mid.next = null;
		ListNode h1 = reverse(head);
		boolean res = len % 2 == 0 ? compareList(h1,h2) : compareList(h1.next,h2);
		reverse(h1);
		mid.next = h2;
		return res;
	}
	private ListNode findMid(ListNode ls) {
		ListNode slow = ls;
		ListNode fast = ls;
		while(fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		return slow;
	}
	private ListNode reverse(ListNode ls) {
		ListNode p = ls;
		ListNode q = p.next;
		p.next = null;
		while(q != null) {
			ListNode r = q.next;
			q.next = p;
			p = q;
			q = r;
		}
		return p;
	}
	private boolean compareList(ListNode ls1, ListNode ls2) {
		while(ls1 != null && ls2 != null) {
			if(ls1.val != ls2.val) return false;
			ls1 = ls1.next;
			ls2 = ls2.next;
		}
		return ls1 == null && ls2 == null;
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