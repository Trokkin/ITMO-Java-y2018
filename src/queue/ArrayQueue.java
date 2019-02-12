package queue;

import java.util.function.Predicate;
import java.util.function.Function;

public class ArrayQueue extends AbstractQueue {
	private static final int MINIMAL_CAPACITY = 16;
	// inv: MINIMAL_CAPACITY <= len(array)
	private Object[] array;
	// inv: capacity = len(array)
	// inv: size <= capacity
	private int capacity;

	// inv: size = tail - head + (tail < head ? capacity : 0)
	// inv: 0 <= head < capacity
	private int head;
	// inv: 0 <= tail < capacity
	private int tail;

	// assuming array is a ring (array[capacity + i] == array[i]):
	// inv: queue[i] = array[head + i] for i in [0, size)

	// pre: 0 <= i < size
	// post: R = queue[i]
	protected Object get_(int i) {
		int p = head + i;
		if (p > capacity)
			p -= capacity;
		return array[p];
	}

	public ArrayQueue() {
		size = head = tail = 0;
		capacity = MINIMAL_CAPACITY;
		array = new Object[MINIMAL_CAPACITY];
	}

	public ArrayQueue(int capacity) {
		size = head = tail = 0;
		this.capacity = capacity;
		array = new Object[capacity];
	}

	// inv: size, queue
	// pre: capacity != newcapacity
	// pre: capacity >= size
	// post: capacity' = newcapacity
	// && head' = 0 && tail' = size
	// && array' = queue
	private void resize(int newcapacity) {
		Object[] old_queue = array;
		// len(array) = capacity
		this.array = new Object[newcapacity];
		// len(array') = newcapacity
		capacity = newcapacity;
		// len(array') = capacity'
		if (size > 0) {
			if (head >= tail) {
				// array[i] != null for i in [0, tail) and [head, capacity)
				// tail < size && head < capacity
				// && size - tail == capacity - head
				System.arraycopy(old_queue, head, array, 0, size - tail);
				// array'[i] == array[i + head] for i in [0, size - tail)
				System.arraycopy(old_queue, 0, array, size - tail, tail);
				// array'[i] == array[i + tail - size] for i in [size - tail, size)

			} else {
				// array[i] != null for i in [head, tail)
				System.arraycopy(old_queue, head, array, 0, size);
				// array'[i] == array[i + head] for i in [0, size)
			}
		}
		head = 0;
		tail = size;
	}

	// post: size' = size + 1
	// && queue'[size - 1] == elem
	// && queue'[i] = queue[i] for i in [0, size)
	protected void enqueue_(Object elem) {
		if (size == capacity) {
			resize(capacity * 2);
		}
		array[tail] = elem;
		tail = (tail + 1) % capacity;
	}

	// post: size' = size + 1
	// && queue'[0] == elem
	// && queue'[i + 1] = queue[i] for i in [0, size)
	protected void push_(Object elem) {
		if (size == capacity) {
			resize(capacity * 2);
		}
		head--;
		if (head < 0) {
			head = capacity - 1;
		}
		array[head] = elem;
	}

	// pre: size > 0
	// post: R = queue[0]
	protected Object element_() {
		return array[head];
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	protected Object peek_() {
		return array[tail == 0 ? capacity - 1 : tail - 1];
	}

	// pre: size > 0
	// post: queue'[i - 1] = queue[i] for i in [1, size)
	protected void dequeue_() {
		array[head] = null;
		head = (head + 1) % capacity;
	}

	// pre: size > 0
	// post: queue'[i] = queue[i] for i in [0, size - 1)
	protected void remove_() {
		tail--;
		if (tail < 0) {
			tail = capacity - 1;
		}
		array[tail] = null;
	}

	public ArrayQueue filter(Predicate<Object> p) {
		ArrayQueue newQueue = new ArrayQueue(capacity);
		int head = this.head;
		for (int i = 0; i < size; i++) {
			if (p.test(array[head])) {
				newQueue.enqueue(array[head]);
			}
			head = (head + 1) % capacity;
		}
		return newQueue;

	}

	public ArrayQueue map(Function<Object, Object> f) {
		ArrayQueue newQueue = new ArrayQueue(capacity);
		int head = this.head;
		for (int i = 0; i < size; i++) {
			newQueue.enqueue(f.apply(array[head]));
			head = (head + 1) % capacity;
		}
		return newQueue;
	}
}