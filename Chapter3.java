import java.util.*;

/**
 * This file contains solution for all problems in CC150 chapter3
 */

public class Chapter3 {
	public static void main(String[] args) {

	}
	/**
	 * problem 3.4: Hanoi tower
	 */
	public void hanoiTower(int N) {
			if(N < 1) throw new UnsupportedOperationException();
			hanoiTower(N,'A','C');
	}
	private void hanoiTower(int N, char src, char des) {
		if(N == 1) {
			System.out.println("Move:"+src+"->"+des); return;
		}
		char mid = findMid(src,des);
		hanoiTower(N-1,src,mid);
		hanoiTower(1,src,des);
		hanoiTower(N-1,mid,des);
	}
	private char findMid(char a, char b) {
		if(a == 'A' && b == 'B' || a == 'B' && b == 'A') return 'C';
		if(a == 'A' && b == 'C' || a == 'C' && b == 'A') return 'B';
		if(a == 'B' && b == 'C' || a == 'C' && b == 'B') return 'A';
		return ' ';
	}

	/**
	 * problem 3.6
	 * sort stack using another stack
	 * Time complexity: O(N^2), best case: O(N)
	 * space complexity: O(N)
	 */
	public void sortStack(Stack<Integer> stack) {
		if(stack == null || stack.size() < 2) return;
		Stack<Integer> s = new Stack<Integer>();
		while(!stack.isEmpty()) {
			int val = stack.pop();
			//pop back all variables to stack which is less than val
			while(!s.isEmpty() && s.peek() < val) {
				stack.push(s.pop());
			}
			s.push(val);
		}
		while(!s.isEmpty()) {
			stack.push(s.pop());
		}
	}
}

//problem 3.1:
//use an array to support three stacks
//we can extend this problem to N stack sharing the same array
class StackFullException extends RuntimeException {}

class NStack<E> {
	private E[] eles;
	class Index<E>{
		private int base;
		private int top;
		public Index() {
			base = 0;
			top = 0;
		}
	}
	private Index[] indexes;
	private int N;
	public NStack(int capacity, int n) {
		if(capacity < n || n < 0) throw new UnsupportedOperationException();
		N = n;
		eles = (E[]) new Object[capacity];
		indexes = new Index[n];
		for(int i = 0; i < n; i++) {
			indexes[i] = new Index();
			indexes[i].base = i * capacity / n; 
			indexes[i].top =  i * capacity / n;
		}
	}
	//push an element at stack i
	public void push(E e, int stacknum) {
		if(stacknum < 0 || stacknum >= N) throw new UnsupportedOperationException();
		//current stack is full
		if(indexes[stacknum].top == indexes[(stacknum+1) % N].base) {
			if(!shrink(stacknum,(stacknum+1) % N)) throw new StackFullException();
		}
		eles[indexes[stacknum].top] = e;
		indexes[stacknum].top = (indexes[stacknum].top+1) % eles.length;
	}
	//pop an element from stack i
	public E pop(int stacknum) {
		if(stacknum < 0 || stacknum >= N) throw new UnsupportedOperationException();
		if(indexes[stacknum].top == indexes[stacknum].base) throw new EmptyStackException();
		indexes[stacknum].top = (indexes[stacknum].top-1+eles.length) % eles.length;
		E e = eles[indexes[stacknum].top];
		return e;
	}
	//move the elements in stack i
	private boolean shrink(int oristack, int stacknum) {
		if(oristack == stacknum) return false; //cannot shrink the array
		if(indexes[stacknum].top == indexes[(stacknum+1) % N].base) {
			if(!shrink(oristack,(stacknum+1) % indexes.length)) return false;
		}
		moveRight(stacknum);
		return true;
	}
	//move a stack to right by 1
	private void moveRight(int stacknum) {
		int i = indexes[stacknum].top;
		while(i != indexes[stacknum].base) {
			eles[i] = eles[(i-1+eles.length) % eles.length];
			i = (i-1+eles.length) % eles.length;
		}
		//update the base and top index by increasing by 1.
		indexes[stacknum].base = (indexes[stacknum].base+1) % eles.length;
		indexes[stacknum].top = (indexes[stacknum].top+1) % eles.length;
	}
}


/**
 * problem 3.2
 * implement a stack supporting min
 */
class MinStack<E> {
	private Comparator<E> comp;
	private Stack<E> eles = new Stack<E>();
	private Stack<E> mins = new Stack<E>();
	public MinStack(Comparator<E> comp) {
		this.comp = comp;
	}
	public void push(E e) {
		eles.push(e);
		if(mins.isEmpty() || comp.compare(mins.peek(),e) >= 0) {
			mins.push(e);
		}
	}
	public E pop() {
		E e = eles.pop();
		if(comp.compare(e,mins.peek()) == 0) {
			mins.pop();
		}
		return e;
	}
	public E peek() {
		return eles.peek();
	}
	public E min() {
		return mins.peek();
	}
	public boolean isEmpty() {
		return eles.isEmpty();
	}
	public int size() {
		return eles.size();
	}
}

/**
 * problem 3.3
 * implement set of stacks that supports popAt()
 * we need to consider the setofstacks as a single stack consisting of continuous sub stacks.
 */
class NotPositiveCapacityException extends RuntimeException{}

class SetOfStacks<E> {
	private int capacity;
	private List<Stack<E>> ls;
	public SetOfStacks(int capacity) {
		if(capacity <= 0) throw new NotPositiveCapacityException();
		this.capacity = capacity;
		ls = new ArrayList<Stack<E>>();
		ls.add(new Stack<E>());
	}
	public void push(E e) {
		Stack<E> s = ls.get(ls.size()-1);
		if(s.size() == capacity) {
			s = new Stack<E>();
			ls.add(s);
		}
		s.push(e);
	}	
	public E pop() {
		Stack<E> s = ls.get(ls.size()-1);
		E e = s.pop();
		if(s.isEmpty()) ls.remove(ls.size()-1);
		return e;
	}
	public E peek() {
		return ls.get(ls.size()-1).peek();
	}
	//pop an element at index-th sub-stack
	//after we pop at, we need to fill it with the bottom element of the next stack
	public E popAt(int index) {
		if(index < 0 || index >= ls.size()) throw new IndexOutOfBoundsException();
		E e = ls.get(index).pop();
		for(int i = index; i < ls.size()-1; i++) {
			shift(ls.get(i),ls.get(i+1));
		}
		if(ls.get(ls.size()-1).isEmpty()) {
			ls.remove(ls.size()-1);
		}
		return e;
	}
	//move the bottom element of src to the top of des
	private void shift(Stack<E> des, Stack<E> src) {
		Stack<E> tmp = new Stack<E>();
		while(! src.isEmpty()) {
			tmp.push(src.pop());
		}
		des.push(tmp.pop());
		while(!tmp.isEmpty()) {
			src.push(tmp.pop());
		}
	} 
	public boolean isEmpty() {
		return ls.size() == 0;
	}
}

/**
 * problem 3.5
 * implement a MyQueue class using two stacks
 * Time complexity: 
 * push(): O(1)
 * pop(): O(1) on average
 */
class MyQueue<E> {
	private Stack<E> s1;
	private Stack<E> s2;
	public MyQueue() {
		s1 = new Stack<E>();
		s2 = new Stack<E>();
	}
	public void add(E e) {
		s1.push(e);
	}
	public E poll() {
		shiftStack();
		return s2.pop();
	}
	public E peek() {
		shiftStack();
		return s2.peek();
	}
	private void shiftStack() {
		if(s2.isEmpty()) {
			while(!s1.isEmpty()) {
				s2.push(s1.pop());
			}
		}
	}
	public boolean isEmpty() {
		return s1.isEmpty() && s2.isEmpty();
	}
}

/**
 * problem 3.7
 * 
 */
class Animal {
	protected int time;
	protected String name;
	public Animal(String s) {
		name = s;
	}
	public void setTime(int t) {
		time = t;
	}
}

class Cat extends Animal {
	public Cat(String name) {
		super(name);
	}
	public String toString() {
		return "Cat:"+time;
	}
}

class Dog extends Animal {
	public Dog(String name) {
		super(name);
	}
	public String toString() {
		return "Dog:"+time;
	}
}

class Shelter {
	private int time = 0;
	private LinkedList<Animal> cats = new LinkedList<Animal>();
	private LinkedList<Animal> dogs = new LinkedList<Animal>();
	public void enqueue(Animal ani) {
		ani.setTime(time++);
		if(ani instanceof Cat) {
			cats.add(ani);
		} else if(ani instanceof Dog) {
			dogs.add(ani);
		}
	}
	public Animal dequeueAny() {
		if(cats.isEmpty()) return dogs.poll();
		else if(dogs.isEmpty()) return cats.poll();
		else {
			if(cats.peek().time < dogs.peek().time) return cats.poll();
			return dogs.poll();
		}
	}
	public Cat dequeueCat() {
		return (Cat)cats.poll();
	}
	public Dog dequeueDog() {
		return (Dog) dogs.poll();
	}
	public boolean isEmpty() {
		return cats.isEmpty() && dogs.isEmpty();
	}
}





