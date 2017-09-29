package tel_ran.collections;

import java.util.Comparator;

public class ComparatorComparable<T> implements Comparator<T> {

	@SuppressWarnings("unchecked")
	@Override
	public int compare(T o1, T o2) {
		Comparable<T> co1 = (Comparable<T>)o1;
		return co1.compareTo(o2);
	}

}
