package tel_ran.collections;

import java.util.function.Predicate;

public class PredicateEquals<T> implements Predicate<T> {
Object obj;
	@Override
	public boolean test(T t) {
		return t.equals(obj);
	}
	public PredicateEquals(Object obj) {
		super();
		this.obj=obj;
	}

}
