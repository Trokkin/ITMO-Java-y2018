package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
	// inv: size >= 0
	protected int size = 0;

	protected abstract Object get_(int i);

	protected abstract void enqueue_(Object elem);

	protected abstract void push_(Object elem);

	protected abstract Object element_();

	protected abstract Object peek_();

	protected abstract void dequeue_();

	protected abstract void remove_();

	// pre: 0 <= i < size
	// post: R = queue[i]
	public Object get(int i) {
		assert 0 <= i && i < size;
		return get_(i);
	}

	// post: size' = size + 1
	// && queue'[size] == elem
	// && queue`[i] = queue[i] for i in [0, size)
	public void enqueue(Object elem) {
		enqueue_(elem);
		size++;
	}

	// post: size' = size + 1
	// && queue'[0] == elem
	// && queue'[i + 1] = queue[i] for i in [0, size)
	public void push(Object elem) {
		push_(elem);
		size++;
	}

	// pre: size > 0
	// post: R = queue[0]
	public Object element() {
		assert size > 0;
		return element_();
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	public Object peek() {
		assert size > 0;
		return peek_();
	}

	// pre: size > 0
	// post: R = queue[0]
	// && size' = size - 1
	// && queue'[i - 1] = queue[i] for i in [1, size)
	public Object dequeue() {
		assert size > 0;
		Object r = element_();
		dequeue_();
		size--;
		return r;
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	// && size' = size - 1
	// && queue'[i] = queue[i] for i in [0, size - 1)
	public Object remove() {
		assert size > 0;
		Object r = peek_();
		remove_();
		size--;
		return r;
	}

	// post: r = size
	public int size() {
		return size;
	}

	// post: r = size == 0
	public boolean isEmpty() {
		return size == 0;
	}

	// post: size' = 0
	public void clear() {
		while (!isEmpty()) {
			dequeue();
		}
	}
}