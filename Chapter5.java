import java.util.*;

/**
 * This file contains solutions for all problems in Chapter5
 * 12.23.14 by Dianshi
 */

public class Chapter5 {
	/**
	 * get bit at i
	 */
	public int getBit(int a, int i) {
		if(i < 0) return -1;
		return (a & (1 << i)) == 0 ? 0 : 1;
	}
	/**
	 * set bit at i
	 */
	public int setBit(int a, int i) {
		if(i < 0) return a;
		return a | (1 << i);
	}
	/**
	 * clear bit at i. set to zero
	 * a & 1111111011111...
	 */
	public int clearBit(int a, int i) {
		if(a < 0) return a;
		return a & (~(1 << i));
	}
	/**
	 * update bit at i
	 * v can be 0 or 1
	 */
	public int updateBit(int a, int i, int v) {
		if(i < 0) return a;
		return a & (~(1 << i)) | (v << i); 
	}
	/**
	 * clear least significant bit
	 */
	public int clearLSB(int a) {
		return a & (a - 1);
	}

	/**
	 * problem 5.1
	 * insert M in N
	 */
	public int insert(int M, int N, int i, int j) {
		if(i > j) return N;
		int m1 = ~0 << (j+1); ///create 11111...1100000..0
		int m2 = (1 << (i+1))-1; //create 0000..000111..1
		int mask = m1 | m2;
		return (mask & N) | (M << i);
	}
	/**
	 * problem 5.2
	 * given a double number between 0 and 1, print its binary representation
	 */
	public void printDouble(double a) {
		int i = 32;
		int res = 0;
		while(a > 0 && i > 0) {
			double l = a * 2;
			if(l >= 1.0) {
				res |= (1 << (i-1));
				a = l - 1;
			} else {
				a = l;
			}
			i--;
		}
		if(a == 0) System.out.println(Integer.toBinaryString(res));
		else
			System.out.println("ERROR");
	}
	/**
	 * problem 5.3
	 * given a positive integer, find the next small and next large number
	 * with the same 1
	 */
	public int nextSmall(int a) {
		int c1 = 0;
		int c = a;
		while(c != 0 && (c & 1) == 1) {
			c1++;
			c = c >> 1;
		}
		int c0 = 0;
		while(c != 0 && (c & 1) == 0) {
			c0++;
			c = c >> 1;
		}
		if(c0 + c1 == 31 || c0 + c0 == 0) return -1;
		a &= ((~0) << (c0+c1+1));
		a |= ((1 << (c1+1)) - 1) << (c0-1);
		return a;
	}
	public int nextLarge(int a) {
		int c0 = 0;
		int c = a;
		//count the rightmost zero
		while(c != 0 && (c & 1) ==0) {
			c0++;
			c = c >> 1;
		}
		int c1 = 0;
		while(c != 0 && (c & 1) == 1) {
			c1++;
			c = c >> 1;
		}
		if(c0 + c1 == 31 || c0 + c1 == 0) {
			return -1;
		}
		a |= (1 << (c1+c0));
		a &= ~((1 << (c1+c0))-1);
		a |= (1 << (c1-1)) - 1;
		return a;
	}
	/**
	 * problem 5.4 check whether a number is 2^n
	 */
	public boolean isTwoExp(int a) {
		if(a <= 0) return false;
		return (a & (a-1)) == 0;
	}
	/**
	 * problem 5.5
	 * determine the number of bits to convert a to b
	 * count the 1 of a ^ b
	 * c & (c-1) to clear the least significant bit
	 */
	public int convertBit(int a, int b) {
		int c = a ^ b;
		int count = 0;
		while(c != 0) {
			count++;
			c &= (c-1);
		}
		return count;
	}
	/**
	 * problem 5.6
	 * swap the odd and even bits in an integer
	 */
	public int swapBit(int a) {
		int even = a & 0x55555555;
		int odd = a & 0xaaaaaaaa;
		return (even << 1) | (odd >> 1);
	}
	/**
	 * problem 5.7
	 * find the missing number
	 */
	public int findMissingNumber(List<BitInteger> ls) {
		return findMissingNumber(ls,BitInteger.INTEGER_SIZE-1);
	}
	public int findMissingNumber(List<BitInteger> ls, int col) {
		if(col < 0) return 0;
		List<BitInteger> zerobits = new ArrayList<BitInteger>();
		List<BitInteger> onebits = new ArrayList<BitInteger>();
		for(BitInteger bi : ls) {
			if(bi.fetch(col) == 0) {
				zerobits.add(bi);
			} else {
				onebits.add(bi);
			}
		}
		//0 is lost
		if(zerobits.size() <= onebits.size()) {
			int v = findMissingNumber(zerobits,col-1);
			return (v << 1) | 0;
		} else {
			int v = findMissingNumber(onebits,col-1);
			return (v << 1) | 1;
		}
	}

	/**
	 * problem 5.8
	 * draw a horizontal line from (x1,y) to (x2,y) 
	 * the width of the image is w
	 */
	public void draw(byte[] screen, int width, int x1, int x2, int y) {
		int start_offset = x1 % 8;
		int start_full_index = x1 / 8;
		if(start_offset == 0) {
			start_full_index++;
		}
		int end_offset = x2 % 8;
		int end_full_index = x2 / 8;
		if(end_offset != 7) {
			end_full_index--;
		}
		//set full bytes 0xFF
		for(int i = start_full_index; i <= end_full_index; i++) {
			screen[width/8*y+i] = (byte)0xFF;
		}
		byte start_mask = (byte)(0xff >> start_offset);
		byte end_mask = (byte)(~(0xff >> (end_offset+1)));
		//check whether x1 and x2 in the same byte
		if(x1 / 8 == x2 / 8) {
			screen[width/8*y+x1/8] |= (start_mask & end_mask);
		} else {
			if(start_offset != 0) {
				screen[width/8*y+start_full_index-1] |= start_mask;
			}
			if(end_offset != 7) {
				screen[width/8*y+end_full_index+1] |= end_mask;
			}
		}
	}

	public static void main(String[] args) {

	}
}

/**
 * BitInteger class
 */
class BitInteger {
	private int num;
	public static int INTEGER_SIZE = 32;
	public BitInteger(int a) {
		num = a;
	}
	public int fetch(int col) {
		return (num >> (31-col)) & 1;
	}
}





