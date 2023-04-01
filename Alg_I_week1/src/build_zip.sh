#!/usr/bin/env bash
javac-algs4 Percolation.java
javac-algs4 PercolationStats.java
javac-algs4 PercolationVisualizer.java

java-algs4 PercolationStats 200 100
java-algs4 PercolationStats 200 100
java-algs4 PercolationStats 2 10000
java-algs4 PercolationStats 2 10000

jar -cfM percolation.zip Percolation.java PercolationStats.java
