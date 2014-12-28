import java.util.*;

/**
 * This file contains solution for all problems
 */

public class Chapter7 {
	/**
	 * given a random generator from 0 to 3
	 * generate random number 0 to 15
	 */
	public int randomNum() {
		Random r = new Random();
		int a = r.nextInt(4);
		int b = r.nextInt(4);
		return (a << 2) | b;
	}
	/**
	 * implement multiply, subtract and divide only with add operator
	 * @param args [description]
	 */
	public int multiply(int a, int b) {
		boolean negative = !(((a ^ b) >> 31) == 0);
		a = Math.abs(a);
		b = Math.abs(b);
		int res = 0;
		for(int i = 0; i < b; i++) {
			res += a;
		}
		return negative ? neg(res) : res;
	}
	public int divide(int a, int b) {
		if(b == 0) throw new ArithmeticException();
		boolean negative = !(((a ^ b) >> 31) == 0);
		a = Math.abs(a);
		b = Math.abs(b);
		int quo = 0;
		int tmp = 0;
		while(tmp < a) {
			tmp += b;
			quo += 1;
		}
		if(tmp > a) quo--;
		return negative ? neg(quo) : quo;
	}
	public int subtract(int a, int b) {
		return a + neg(b);
	}
	private int neg(int a) {
		int q = a < 0 ? 1 : -1;
		int res = 0;
		for(int i = 0; i < Math.abs(a); i++) {
			res += q;
		}
		System.out.println(q);
		return res;
	}
	/**
	 * multiply follow up. use fast algorithm, O(log(N))
	 * time complexity: O(log(N))
	 */
	public int multiplyII(int a, int b) {
		long n1 = a;
		long n2 = b;
		boolean negative = !(((a ^ b) >> 31) == 0);
		n1 = n1 < 0 ? -n1 : n1;
		n2 = n2 < 0 ? - n2: n2;
		long res = mulhelper(n1,n2);
		return negative ? (int)(-res) : (int)res;
	}
	private long mulhelper(long a, long b) {
		if(b == 0) return 0;
		if(b == 1) return a;
		long res = a;
		long t = 1;
		while(t < b) {
			res = res << 1;
			t = t << 1;
		}
		if(t == b) return res;
		return (res >> 1) + mulhelper(a,b - (t >> 1));
	}

	/**
	 * divide follow up: use fast algorithm, O(log(N))
	 * Time complexity: O(log(N))
	 */
	public int divideII(int a, int b) {
		if(b == 0) throw new ArithmeticException();
		long n1 = a;
		long n2 = b;
		boolean negative = !(((a ^ b) >> 31) == 0);
		n1 = n1 < 0 ? -n1 : n1;
		n2 = n2 < 0 ? -n2 : n2;
		long res = dividehelper(n1,n2);
		return negative ? (int)(-res) : (int)(res);
	}
	private long dividehelper(long a,long b) {
		if(a < b) return 0;
		long tmp = b;
		long quo = 1;
		while(tmp  < a) {
			tmp = tmp << 1;
			quo = quo << 1;
		}
		if(tmp == a) return quo;
		return (quo >> 1) + dividehelper(a - (tmp >> 1),b);
	}

	/**
	 * problem 7.5
	 * find a line which pass the most points
	 * Time complexity: O(N^2)
	 * assumption: no duplicates
	 */
	public Line findBestLines(Point[] points) {
		HashMap<Line,Integer> map = new HashMap<Line,Integer>();
		Line bestline = null;
		for(int i = 0; i < points.length; i++) {
			for(int j = i + 1; j < points.length; j++) {
				Line l = new Line(points[i],points[j]);
				if(! map.containsKey(l)) {
					map.put(l,0);
				} else {
					map.put(l,map.get(l)+1);
				}
				if(bestline == null || map.get(bestline) < map.get(l)) {
					bestline = l;
				}
			}
		}
		return bestline;
	}
	/**
	 * problem 7.6. there are duplicates
	 * we compute the best line across a points.
	 * Time complexity: O(N^2)
	 */
	public Line findBestLinesII(Point[] points) {
		HashMap<Double,Integer> map = new HashMap<Double,Integer>();
		int maxpoint = 0;
		Line bestline = new Line();
		for(int i = 0; i < points.length; i++) {
			int duplicates = 0;
			for(int j = 0; j < points.length; j++) {
				if(points[i].x == points[j].x && points[i].y == points[i].y) {
					duplicates++;
					continue;
				}
				double slope = (points[i].x == points[j].x) ? Integer.MAX_VALUE : (points[i].y - points[j].y) / (points[i].x - points[j].x);
				if(! map.containsKey(slope)) {
					map.put(slope,1);
				} else {
					map.put(slope,map.get(slope)+1);
				}
			}
			int currmax = 0;
			double s = 0;
			for(Double key : map.keySet()) {
				if(currmax < map.get(key)) {
					currmax = map.get(key);
					s = key;
				}
			}
			currmax += duplicates;
			if(currmax > maxpoint) {
				maxpoint = currmax;
				bestline.slope = s;
				bestline.intersect = points[i].y - points[i].x * s;
			}
		}
		return bestline;
	}
	/**
	 * problem 7.6
	 * design an algorithm to find the kth number such that only prime factors are 3,5, and 7
	 */
	public int findKthNum(int k) {
		Queue<Integer> q3 = new LinkedList<Integer>();
		Queue<Integer> q5 = new LinkedList<Integer>();
		Queue<Integer> q7 = new LinkedList<Integer>();
		q3.add(3);
		q5.add(5);
		q7.add(7);
		int val = 0;
		for(int i = 0; i < k; i++) {
			val = Math.min(q3.peek(),Math.min(q5.peek(), q7.peek()));
			if(val == q3.peek()) {
				q3.add(q3.peek()*3);
				q5.add(q5.peek()*3);
				q7.add(q7.peek()*3);
				q3.poll();
			}
			else if(val == q5.peek()) {
				q5.add(q5.peek()*5);
				q7.add(q7.peek()*5);
				q5.poll();
			}
			else if(val == q7.peek()) {
				q7.add(q7.peek()*7);
				q7.poll();
			}
		}
		return val;
	}
	/**
	 * problem 7.6 follow up, use set and priority queue
	 */
	public int findKthNumII(int k){
		HashSet<Integer> set = new HashSet<Integer>();
		PriorityQueue<Integer> q = new PriorityQueue<Integer>();
		int[] primes = new int[]{3,5,7};
		for(int i = 0; i < primes.length; i++) {
			q.add(primes[i]);
			set.add(primes[i]);
		}
		int val = 0;
		for(int i = 0; i < k; i++) {
			val = q.poll();
			for(int j = 0; j < primes.length; j++) {
				if(! set.contains(val * primes[j])) {
					set.add(val * primes[j]);
					q.add(val * primes[j]);
				}
			}
		}
		return val;
	}
	public static void main(String[] args) {
		Chapter7 sol = new Chapter7();
		for(int i = 1; i <= 10; i++) {
			System.out.println(sol.findKthNumII(i));
		}
	}
}

class Point {
	public double x;
	public double y;
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
}

class Line {
	public double slope;
	public double intersect;
	public boolean vertical;
	public Line() {}
	public Line(Point p1, Point p2) {
		if(Math.abs(p1.x - p2.x) > 0.0001) {
			slope = (p1.y - p2.y) / (p1.x - p2.x);
			intersect = p1.y - slope * p1.x;
		} else {
			vertical = true;
			intersect = p1.x;
		}
	}
	public int hashCode() {
		int s = (int)(slope * 1000);
		int in = (int)(intersect * 1000);
		return s | in;
	}
	public boolean equals(Object obj) {
		Line l = (Line) obj;
		return isEqual(l.slope,slope) && isEqual(l.intersect,intersect) &&
		vertical == l.vertical;
	}
	private boolean isEqual(double a, double b) {
		return Math.abs(a-b) < 0.0001;
	}
	public String toString() {
		if(vertical) return "y=Inf x+"+intersect;
		return "y="+slope+"x+"+intersect;
	}
}



