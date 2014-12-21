/**
 * this file contains all the problems in Chapter 0
 */

import java.util.*;

public class Chapter0 {
	public static void main(String[] args) {
		Chapter0 sol = new Chapter0();
		
		String res = sol.convertToString(1294,9);
		System.out.println(res);
	} 

	/**
	 * compute the angle between hour and minute hands
	 */
	public double angleBetweenHourMin(int hour, int min) {
		//corner case
		if(hour < 0 || hour >= 12 || min < 0 || min >= 60) return 0;
		double h = (hour / 12.0 * 360 + min / 60.0 * 1 / 12 * 360);
		double m = min / 60.0 * 360;
		return Math.abs(h - m);
	}
	/**
	 * find the minimum element in a rotated sorted array
	 * assume no duplicates in the array
	 * binary search
	 */
	public int findMinInRotated(int[] array) {
		if(array == null || array.length == 0) return -1;
		int L = 0;
		int R = array.length - 1;
		while(L <= R) {
			int mid = (L + R) >> 1;
			if(array[L] <= array[mid] && array[mid] <= array[R]) return array[L];
			if(array[L] < array[mid]) L = mid;
			else if(array[L] > array[mid]) R = mid;
			else return Math.min(array[L],array[R]);
		}
		return -1;
	}
	/**
	 * add two simple polynomials
	 */
	public PolyTerm[] addPoly(PolyTerm[] pt1, PolyTerm[] pt2) {
		if(pt1 == null || pt1.length == 0) return pt2;
		if(pt2 == null || pt2.length == 0) return pt1;
		//sort the two input polynomials according to exponent
		Comparator<PolyTerm> comp = new Comparator<PolyTerm>() {
			public int compare(PolyTerm p1, PolyTerm p2) {
				if(p1.exponent == p2.exponent) return 0;
				if(p1.exponent < p2.exponent) return -1;
				return 1;
			}
		};
		Arrays.sort(pt1,comp);
		Arrays.sort(pt2,comp);
		PolyTerm[] res = new PolyTerm[pt1.length+pt2.length];
		int i = 0;
		int j = 0;
		int index = 0;
		while(i < pt1.length || j < pt2.length) {
			if(i == pt1.length) {
				res[index++] = new PolyTerm(pt2[j].cofficeint,pt2[j].exponent);
				j++;
			} else if(j == pt2.length) {
				res[index++] = new PolyTerm(pt1[i].cofficeint,pt1[i].exponent);
				i++;
			} else {
				if(pt1[i].exponent < pt2[j].exponent) {
					res[index++] = new PolyTerm(pt1[i].cofficeint,pt1[i].exponent);
					i++;
				} else if(pt1[i].exponent > pt2[j].exponent){
					res[index++] = new PolyTerm(pt2[j].cofficeint,pt2[j].exponent);
					j++;
				} else {
					res[index++] = new PolyTerm(pt1[i].cofficeint+pt2[j].cofficeint,pt1[i].exponent);
					i++;
					j++;
				}
			}
		}
		return Arrays.copyOfRange(res,0,index);
	}
	/**
	 * convert a number string to integer according to its base
	 * support base from 2 to 16
	 * assume that only numeric and 'A'-'Z' is contained
	 */
	public int convertToInteger(String s, int base) {
		if(base < 2 || base > 16) return 0;
		if(! isValid(s,base)) throw new NumberFormatException();
		long res = 0;
		char[] chs = s.toCharArray();
		for(int i = 0; i < chs.length; i++) {
			if(res >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
			if(chs[i] >= '0' && chs[i] <= '9') {
				res = res * base + chs[i] - '0';
			} else {
				res = res * base + chs[i] - 'A' + 10;
			}
		}
		return (int)res;
	}
	private boolean isValid(String num, int base) {
		if(num == null || num.length() == 0) return false;
		char[] chs = num.toCharArray();
		for(int i = 0; i < chs.length; i++) {
			if(chs[i] >= '0' && chs[i] <= '9' && chs[i] - '0' < base) continue;
			if(chs[i] >= 'A' && chs[i] <= 'F' && chs[i] - 'A' < base - 10) continue;
			return false;
		}
		return true;
	}
	/**
	 * convert an 10-based integer to string representation according to base
	 * base is from 2 to 16
	 */
	public String convertToString(int num, int base) {
		if(base < 2 || base > 16) throw new ArithmeticException();
		long a = num;
		a = a < 0 ? -a : a;
		StringBuilder sb = new StringBuilder();
		while(num > 0) {
			int v = num % base;
			sb.append(v < 10 ? v : (char)(v-10+'A')+"");
			num /= 10;
		}
		String res = sb.toString();
		return num < 0 ? "-"+res : res;
	}
}

class PolyTerm {
	public double cofficeint;
	public double exponent;
	public PolyTerm(double c, double e) {
		cofficeint = c;
		exponent = e;
	}
	public String toString() {
		return cofficeint+"x^"+exponent;
	}
}

/**
 * track the median of a stream of integer
 */
class IntegerStream {
	private PriorityQueue<Integer> mins;
	private PriorityQueue<Integer> maxs;
	public IntegerStream() {
		mins = new PriorityQueue<Integer>(1,new Comparator<Integer>() {
			public int compare(Integer a, Integer b) {
				if(a == b) return 0;
				if(a < b) return -1;
				return 1;
			}
		});
		maxs = new PriorityQueue<Integer>(1,new Comparator<Integer>(){
			public int compare(Integer a, Integer b) {
				if(a == b) return 0;
				if(a < b) return 1;
				return -1;
			}
		});
	}
	public void add(int num) {
		if(maxs.isEmpty()) {
			maxs.add(num);
		} else if(maxs.size() == mins.size()) {
			if(mins.peek() < num) {
				maxs.add(mins.poll());
				mins.add(num);
			} else {
				maxs.add(num);
			}
		} else {
			if(maxs.peek() > num) {
				mins.add(maxs.poll());
				maxs.add(num);
			} else {
				mins.add(num);
			}
		}
	}
	public double getMedian() {
		if(maxs.isEmpty()) return -1;
		else if(maxs.size() == mins.size()) {
			return (maxs.peek() + mins.peek()) / 2.0;
		} else {
			return maxs.peek();
		}
	}
}