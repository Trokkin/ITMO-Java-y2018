package queue;

public class ArrayQueue {
	private static final int MINIMAL_CAPACITY = 16;
	// inv: MINIMAL_CAPACITY <= len(array)
	private Object[] array;
	// inv: capacity = len(array)
	private int capacity;

	// inv: 0 <= size <= capacity
	// && size = tail - head + (tail < head ? capacity : 0)
	private int size;
	// inv: 0 <= head < capacity
	private int head;
	// inv: 0 <= tail < capacity
	private int tail;

	// assuming array is a ring (array[capacity + i] == array[i]):
	// inv: queue[i] = array[head + i] for i in [0, size)

	// pre: 0 <= i < size
	// post: R = queue[i]
	public Object get(int i) {
		assert 0 <= i && i < size;
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
	public void enqueue(Object elem) {
		if (size == capacity) {
			resize(capacity * 2);
		}
		array[tail] = elem;
		tail = (tail + 1) % capacity;
		size++;
	}

	// post: size' = size + 1
	// && queue'[0] == elem
	// && queue'[i + 1] = queue[i] for i in [0, size)
	public void push(Object elem) {
		if (size == capacity) {
			resize(capacity * 2);
		}
		head--;
		if (head < 0) {
			head = capacity - 1;
		}
		array[head] = elem;
		size++;
	}

	// pre: size > 0
	// post: R = queue[0]
	public Object element() {
		assert size > 0;
		return array[head];
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	public Object peek() {
		assert size > 0;
		return array[tail == 0 ? capacity - 1 : tail - 1];
	}

	// pre: size > 0
	// post: R = queue[0]
	// && size' = size - 1
	// && queue'[i - 1] = queue[i] for i in [1, size)
	public Object dequeue() {
		assert size > 0;
		Object r = array[head];
		array[head] = null;
		head = (head + 1) % capacity;
		size--;
		return r;
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	// && size' = size - 1
	// && queue'[i] = queue[i] for i in [0, size - 1)
	public Object remove() {
		assert size > 0;
		tail--;
		if (tail < 0) {
			tail = capacity - 1;
		}
		Object r = array[tail];
		array[tail] = null;
		size--;
		return r;
	}

	// post: r = size
	public int size() {
		return size;
	}

	// post: r = size != 0
	public boolean isEmpty() {
		return size == 0;
	}

	// post: size' = 0
	public void clear() {
		size = 0;
		head = 0;
		tail = 0;
	}
}