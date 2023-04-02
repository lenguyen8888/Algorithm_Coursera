#!/usr/bin/env bash

javac-algs4 BruteCollinearPoints.java
javac-algs4 FastCollinearPoints.java

java-algs4 BruteCollinearPoints ../input8.txt
java-algs4 BruteCollinearPoints ../input6.txt

java-algs4 FastCollinearPoints  ../input8.txt
java-algs4 FastCollinearPoints  ../input6.txt

jar -cfM colinear.zip BruteCollinearPoints.java FastCollinearPoints.java Point.java
