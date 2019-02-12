package queue;

import java.util.function.Predicate;
import java.util.function.Function;

public interface Queue {

	// pre: 0 <= i < size
	// post: R = queue[i]
	public Object get(int i);

	// post: size' = size + 1
	// && queue'[size] == elem
	// && queue`[i] = queue[i] for i in [0, size)
	public void enqueue(Object elem);

	// post: size' = size + 1
	// && queue'[0] == elem
	// && queue'[i + 1] = queue[i] for i in [0, size)
	public void push(Object elem);

	// pre: size > 0
	// post: R = queue[0]
	public Object element();

	// pre: size > 0
	// post: R = queue[size - 1]
	public Object peek();

	// pre: size > 0
	// post: R = queue[0]
	// && size' = size - 1
	// && queue'[i - 1] = queue[i] for i in [1, size)
	public Object dequeue();

	// pre: size > 0
	// post: R = queue[size - 1]
	// && size' = size - 1
	// && queue'[i] = queue[i] for i in [0, size - 1)
	public Object remove();

	// post: r = size
	public int size();

	// post: r = size == 0
	public boolean isEmpty();

	// post: size' = 0
	public void clear();

	public Queue filter(Predicate<Object> p);

	public Queue map(Function<Object, Object> p);
}