// Dequeue. A double-ended queue or deque (pronounced “deck”) is a generalization of 
// a stack and a queue that supports adding and removing items from either the front
//  or the back of the data structure. Create a generic data type Deque that implements the following API:

// Corner cases.  Throw the specified exception for the following corner cases:

// Throw an IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
// Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the 
// deque is empty.
// Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there 
// are no more items to return.
// 
// Throw an UnsupportedOperationException if the client calls the remove() method in the iterator.
// Unit testing.  Your main() method must call directly every public constructor and method to help verify 
// that they work as prescribed (e.g., by printing results to standard output).

// Performance requirements.  Your deque implementation must support each deque operation
// (including construction) in constant worst-case time. A deque containing n items must use at 
// most 48n + 192 bytes of memory. Additionally, your iterator implementation must support each operation
//  (including construction) in constant worst-case time.

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    // declare private template Node class
    private class Node {
        Item item;
        Node next;
        Node prev;
    };

    // declare private template ListIterator class
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more items to return");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    // declare private variables
    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item){
        if (item == null) throw new IllegalArgumentException("Null argument");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (isEmpty()) last = first;
        else oldfirst.prev = first;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Null argument");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;
        first = first.next;
        size--;
        if (isEmpty()) last = null;
        else first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;
        last = last.prev;
        size--;
        if (isEmpty()) first = null;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        // test Deque class
        Deque<Integer> deque = new Deque<Integer>();
        // test 5 addFist using loop
        for (int i = 0; i < 5; i++) {
            deque.addFirst(i);
        }
        // test 5 addLast using loop
        for (int i = 0; i < 5; i++) {
            deque.addLast(i);
        }
        // test 5 removeFirst using loop
        for (int i = 0; i < 5; i++) {
            deque.removeFirst();
        }
        // test 5 removeLast using loop
        for (int i = 0; i < 5; i++) {
            deque.removeLast();
        }

    }

}
