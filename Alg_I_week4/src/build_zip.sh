#!/usr/bin/env bash

javac-algs4 Board.java
javac-algs4 Solver.java

java-algs4 -Xmx1600m Solver ../puzzle00.txt 1
java-algs4 -Xmx1600m Solver ../puzzle01.txt 1
java-algs4 -Xmx1600m Solver ../puzzle02.txt 1
java-algs4 -Xmx1600m Solver ../puzzle03.txt 1
java-algs4 -Xmx1600m Solver ../puzzle04.txt 1
java-algs4 -Xmx1600m Solver ../puzzle05.txt 1
java-algs4 -Xmx1600m Solver ../puzzle06.txt 1
java-algs4 -Xmx1600m Solver ../puzzle07.txt 1
java-algs4 -Xmx1600m Solver ../puzzle08.txt 1
java-algs4 -Xmx1600m Solver ../puzzle09.txt 1
java-algs4 -Xmx1600m Solver ../puzzle10.txt 1
java-algs4 -Xmx1600m Solver ../puzzle11.txt 1
java-algs4 -Xmx1600m Solver ../puzzle12.txt 1
java-algs4 -Xmx1600m Solver ../puzzle13.txt 1
java-algs4 -Xmx1600m Solver ../puzzle14.txt 1
java-algs4 -Xmx1600m Solver ../puzzle15.txt 1
java-algs4 -Xmx1600m Solver ../puzzle16.txt 1
java-algs4 -Xmx1600m Solver ../puzzle17.txt 1
java-algs4 -Xmx1600m Solver ../puzzle18.txt 1
java-algs4 -Xmx1600m Solver ../puzzle19.txt 1
java-algs4 -Xmx1600m Solver ../puzzle20.txt 1
java-algs4 -Xmx1600m Solver ../puzzle21.txt 1
java-algs4 -Xmx1600m Solver ../puzzle22.txt 1
java-algs4 -Xmx1600m Solver ../puzzle23.txt 1
java-algs4 -Xmx1600m Solver ../puzzle24.txt 1
java-algs4 -Xmx1600m Solver ../puzzle25.txt 1

jar -cfM 8puzzle.zip Board.java Solver.java
