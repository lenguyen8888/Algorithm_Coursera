#!/usr/bin/env bash

javac-algs4 Deque.java
javac-algs4 RandomizedQueue.java
javac-algs4 Permutation.java

java-algs4 Deque
java-algs4 Permutation 3  < ../distinct.txt
java-algs4 Permutation 3  < ../distinct.txt

java-algs4 Permutation 8  < ../duplicates.txt

jar -cfM queues.zip RandomizedQueue.java Deque.java Permutation.java

