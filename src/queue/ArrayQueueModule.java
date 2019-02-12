package queue;

public class ArrayQueueModule {
	private static final int MINIMAL_CAPACITY = 16;
	// inv: MINIMAL_CAPACITY <= len(array)
	private static Object[] array;
	// inv: capacity = len(array)
	private static int capacity;

	// inv: 0 <= size <= capacity
	// && size = tail - head + (tail < head ? capacity : 0)
	private static int size;
	// inv: 0 <= head < capacity
	private static int head;
	// inv: 0 <= tail < capacity
	private static int tail;

	// assuming array is a ring (array[capacity + i] == array[i]):
	// inv: queue[i] = array[head + i] for i in [0, size)

	// pre: 0 <= i < size
	// post: R = queue[i]
	public static Object get(int i) {
		assert 0 <= i && i < size;
		int p = head + i;
		if (p > capacity)
			p -= capacity;
		return array[p];
	}

	public ArrayQueueModule() {
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
	private static void resize(int newcapacity) {
		Object[] old_queue = array;
		// len(array) = capacity
		array = new Object[newcapacity];
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
	// && queue'[size] == elem
	// && queue`[i] = queue[i] for i in [0, size)
	public static void enqueue(Object elem) {
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
	public static void push(Object elem) {
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
	public static Object element() {
		assert size > 0;
		return array[head];
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	public static Object peek() {
		assert size > 0;
		return array[tail == 0 ? capacity - 1 : tail - 1];
	}

	// pre: size > 0
	// post: R = queue[0]
	// && size' = size - 1
	// && queue'[i - 1] = queue[i] for i in [1, size)
	public static Object dequeue() {
		assert size > 0;
		Object r = element();
		head = (head + 1) % capacity;
		size--;
		return r;
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	// && size' = size - 1
	// && queue'[i] = queue[i] for i in [0, size - 1)
	public static Object remove() {
		assert size > 0;
		Object r = peek();
		tail--;
		if (tail < 0) {
			tail = capacity - 1;
		}
		size--;
		return r;
	}

	// post: r = size
	public static int size() {
		return size;
	}

	// post: r = size == 0
	public static boolean isEmpty() {
		return size == 0;
	}

	// post: size' = 0
	public static void clear() {
		size = 0;
		head = 0;
		tail = 0;
	}
}
