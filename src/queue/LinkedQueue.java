package queue;

import java.util.function.Predicate;
import java.util.function.Function;

public class LinkedQueue extends AbstractQueue {

	private Node tail;
	private Node head;

	public LinkedQueue() {
		tail = null;
		head = null;
	}

	protected Object get_(int i) {
		Node n = head;
		while (i-- != 0) {
			n = n.prev;
		}
		return n.value;
	}

	protected void enqueue_(Object elem) {
		tail = new Node(elem, null, tail);
		if (head == null) {
			head = tail;
		}
		if (tail.next != null) {
			tail.next.prev = tail;
		}
	}

	protected void push_(Object elem) {
		head = new Node(elem, head, null);
		if (tail == null) {
			tail = head;
		}
		if (head.prev != null) {
			head.prev.next = head;
		}
	}

	protected Object element_() {
		return head.value;
	}

	protected Object peek_() {
		return tail.value;
	}

	protected void dequeue_() {
		if (tail == head) {
			tail = null;
		}
		head = head.prev;
		if (head != null) {
			head.next = null;
		}
	}

	protected void remove_() {
		if (tail == head) {
			head = null;
		}
		tail = tail.next;
		if (tail != null) {
			tail.prev = null;
		}
	}

	private class Node {
		Object value;
		Node prev;
		Node next;

		Node(Object element, Node prev, Node next) {
			value = element;
			this.prev = prev;
			this.next = next;
		}
	}

	public LinkedQueue filter(Predicate<Object> p) {
		LinkedQueue newQueue = new LinkedQueue();
		Node n = head;
		while (n != null) {
			if (p.test(n.value)) {
				newQueue.enqueue(n.value);
			}
			n = n.prev;
		}
		return newQueue;
	}
	// enqueue, tail, size, -> next-> ... <-prev <- 0, head, push

	public LinkedQueue map(Function<Object, Object> f) {
		LinkedQueue newQueue = new LinkedQueue();
		Node n = head;
		while (n != null) {
			newQueue.enqueue(f.apply(n.value));
			n = n.prev;
		}
		return newQueue;
	}
}