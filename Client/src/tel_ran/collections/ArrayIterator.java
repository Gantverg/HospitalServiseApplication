package tel_ran.collections;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
private Object []array;
private int current;
private int size;
	public ArrayIterator(Object[]array, int size) {
		this.array=array;
		current=0;
		this.size=size;
	}

	@Override
	public boolean hasNext() {
		
		return current < size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		return (T)array[current++];
	}

}
