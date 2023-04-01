#!/usr/bin/env bash
javac-algs4 HelloWorld.java
javac-algs4 HelloGoodbye.java
javac-algs4 RandomWord.java

java-algs4 HelloWorld

java-algs4 HelloGoodbye Kevin Bob
java-algs4 HelloGoodbye Alejandra Bahati

java-algs4 RandomWord < ../animals8.txt

jar -cfM hello.zip HelloWorld.java HelloGoodbye.java RandomWord.java

