package tel_ran.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")

public class Array<T> implements Iterable<T>{
private static final int CAPACITY = 10;
private int size;
private int capacity=CAPACITY;
private Object []array;
public Array(int capacity){
	this.capacity=capacity;
	array=new Object[capacity];
}
public Array() {
	this(CAPACITY);
}
public int size(){
	return size;
}

public T get(int index){
	if (index < 0 || index >= size)
		return null;
	return (T) array[index];
}
public void add(T obj){
	if(size==capacity)
		allocate();
	array[size++]=obj;
}
private void allocate() {
	capacity=capacity*2;
	Object[] tmp=new Object[capacity];
	//copying 'size' objects from array to tmp
	for(int i=0;i<size;i++)
		tmp[i]=array[i];
	array=tmp;
	
}
public boolean insert (T obj, int index){
	if(index < 0 || index > size)
		return false;
	if(size==capacity)
		allocate();
	for(int i=size;i>index; i--){
		array[i]=array[i-1];
	}
	array[index]=obj;
	size++;
	return true;
}
public T removeLast(){
	if(size == 0)
		return null;
	Object res=array[size-1];
	array[size-1]=null;
	size--;
	return (T) res;
	
}
public T remove(int index){
	if(index<0 || index>=size)
		return null;
	Object res=array[index];
	for(int i=index+1;i<size;i++){
		array[i-1]=array[i];
	}
	array[size-1]=null;
	size--;
	return (T) res;
}

public int indexOf(T obj){
	return indexOf(new PredicateEquals<T>(obj));
}
public int indexOf(Predicate<T> predicate){
	for(int i=0;i<size;i++){
		if(predicate.test((T) array[i]))
			return i;
	}
	return -1;
}
public int indexOfLast(T obj){
	return indexOfLast(new PredicateEquals<T>(obj));
}
public int indexOfLast(Predicate<T> predicate){
	for(int i=size-1;i>=0;i--){
		if(predicate.test((T)array[i]))
			return i;
	}
	return -1;
}
public void sort(Comparator<T> comp) {
	boolean fl_unsort=true;
	int nElements=size;
	do {
		nElements--;
		fl_unsort=false;
		for(int i=0;i<nElements;i++){
			if(comp.compare((T)array[i],(T)array[i+1])>0){
				fl_unsort=true;
				swap(i,i+1);
			}
		}
	}while(fl_unsort);
}
private void swap( int i, int j) {
	Object tmp=array[i];
	array[i]=array[j];
	array[j]=tmp;
	
}
public void sort() {
	sort(new ComparatorComparable<T>());
	
}
public Array<T> filter(Predicate<T> predicate){
	Array<T> res=new Array<>();
	for(int i=0; i<size; i++)
		if(predicate.test((T)array[i]))
			res.add((T)array[i]);
	return res;
}
public void removeIf(Predicate<T> predicate){
	Object[]tmp=new Object[array.length];
	int iTmp=0;
	for(int i=0;i<size;i++){
		if(!predicate.test((T)array[i]))
			tmp[iTmp++]=array[i];
	}
	array=tmp;
	size=iTmp;
}
@Override
public Iterator<T> iterator() {
	// TODO Auto-generated method stub
	return new ArrayIterator<T>(array,size);
}
}
