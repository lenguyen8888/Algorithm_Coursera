// Randomized queue. A randomized queue is similar to a stack or queue, except 
// that the item removed is chosen uniformly at random among items in the data 
// structure. Create a generic data type RandomizedQueue that implements the following API:

// Iterator.  Each iterator must return the items in uniformly random order. 
// The order of two or more iterators to the same randomized queue must be 
// mutually independent; each iterator must maintain its own random order.

// Corner cases.  Throw the specified exception for the following corner cases:

// Throw an IllegalArgumentException if the client calls enqueue() with a null argument.
// 
// Throw a java.util.NoSuchElementException if the client calls either sample() or 
// dequeue() when the randomized queue is empty.
// 
// Throw a java.util.NoSuchElementException if the client calls the next() method in 
// the iterator when there are no more items to return.
// 
// Throw an UnsupportedOperationException if the client calls the remove() method
// in the iterator.
// 
// Unit testing.  Your main() method must call directly every public constructor and
// method to verify that they work as prescribed (e.g., by printing results to 
// standard output).

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // const initial size of array
    private static final int INIT_SIZE = 8;
    // declare private variables
    private Item[] items;
    private int size;
    // declare random iterator class
    private class RandomIterator implements Iterator<Item> {
        private int i = size;
        private final int[] indices = StdRandom.permutation(size);

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more items to return");
            return items[indices[--i]];
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        resize(INIT_SIZE);
        size = 0;
    }

    // resize array
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Item is null");
        // resize array if needed
        if (size == items.length) resize(2 * items.length);
        // add item to array
        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Randomized queue is empty");
        // get random index
        int index = StdRandom.uniformInt(size);
        // get item at random index
        Item item = items[index];
        // swap item at random index with last item
        items[index] = items[size - 1];
        // set last item to null
        items[size - 1] = null;
        // decrement size
        size--;
        // resize array if needed
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        // return item
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Randomized queue is empty");
        // get random index
        int index = StdRandom.uniformInt(size);
        // get item at random index
        Item item = items[index];
        // return item
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        // test RandomizedQueue
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        // test 10 enqueue using loop
        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }
        // test 10 dequeue using loop
        for (int i = 0; i < 10; i++) {
            System.out.println(rq.dequeue());
        }
    }

}