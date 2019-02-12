package queue;

public class ArrayQueueADT {
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
	public static Object get(ArrayQueueADT adt, int i) {
		assert 0 <= i && i < adt.size;
		int p = adt.head + i;
		if (p > adt.capacity)
			p -= adt.capacity;
		return adt.array[p];
	}

	public ArrayQueueADT() {
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
	private static void resize(ArrayQueueADT adt, int newcapacity) {
		Object[] old_queue = adt.array;
		// len(array) = capacity
		adt.array = new Object[newcapacity];
		// len(array') = newcapacity
		adt.capacity = newcapacity;
		// len(array') = capacity'
		if (adt.size > 0) {
			if (adt.head >= adt.tail) {
				// array[i] != null for i in [0, tail) and [head, capacity)
				// tail < size && head < capacity
				// && size - tail == capacity - head
				System.arraycopy(old_queue, adt.head, adt.array, 0, adt.size - adt.tail);
				// array'[i] == array[i + head] for i in [0, size - tail)
				System.arraycopy(old_queue, 0, adt.array, adt.size - adt.tail, adt.tail);
				// array'[i] == array[i + tail - size] for i in [size - tail, size)

			} else {
				// array[i] != null for i in [head, tail)
				System.arraycopy(old_queue, adt.head, adt.array, 0, adt.size);
				// array'[i] == array[i + head] for i in [0, size)
			}
		}
		adt.head = 0;
		adt.tail = adt.size;
	}

	// post: size' = size + 1
	// && queue'[size - 1] == elem
	// && queue`[i] = queue[i] for i in [0, size)
	public static void enqueue(ArrayQueueADT adt, Object elem) {
		if (adt.size == adt.capacity) {
			resize(adt, adt.capacity * 2);
		}
		adt.array[adt.tail] = elem;
		adt.tail = (adt.tail + 1) % adt.capacity;
		adt.size++;
	}

	// post: size' = size + 1
	// && queue'[0] == elem
	// && queue'[i + 1] = queue[i] for i in [0, size)
	public static void push(ArrayQueueADT adt, Object elem) {
		if (adt.size == adt.capacity) {
			resize(adt, adt.capacity * 2);
		}
		adt.head--;
		if (adt.head < 0) {
			adt.head = adt.capacity - 1;
		}
		adt.array[adt.head] = elem;
		adt.size++;
	}

	// pre: size > 0
	// post: R = queue[0]
	public static Object element(ArrayQueueADT adt) {
		assert adt.size > 0;
		return adt.array[adt.head];
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	public static Object peek(ArrayQueueADT adt) {
		assert adt.size > 0;
		return adt.array[adt.tail == 0 ? adt.capacity - 1 : adt.tail - 1];
	}

	// pre: size > 0
	// post: R = queue[0]
	// && size' = size - 1
	// && queue'[i - 1] = queue[i] for i in [1, size)
	public static Object dequeue(ArrayQueueADT adt) {
		assert adt.size > 0;
		Object r = element(adt);
		adt.head = (adt.head + 1) % adt.capacity;
		adt.size--;
		return r;
	}

	// pre: size > 0
	// post: R = queue[size - 1]
	// && size' = size - 1
	// && queue'[i] = queue[i] for i in [0, size - 1)
	public static Object remove(ArrayQueueADT adt) {
		assert adt.size > 0;
		Object r = peek(adt);
		adt.tail--;
		if (adt.tail < 0) {
			adt.tail = adt.capacity - 1;
		}
		adt.size--;
		return r;
	}

	// post: r = size
	public static int size(ArrayQueueADT adt) {
		return adt.size;
	}

	// post: r = size != 0
	public static boolean isEmpty(ArrayQueueADT adt) {
		return adt.size == 0;
	}

	// post: size' = 0
	public static void clear(ArrayQueueADT adt) {
		adt.size = 0;
		adt.head = 0;
		adt.tail = 0;
	}
}