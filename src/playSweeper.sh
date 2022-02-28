#!/bin/sh

DIRM=`pwd`

DIRM_L="$DIRM/../libs"

SAT4J_DIR="$DIRM_L/sat4j-pb.jar"

LOGICNG_DIR="$DIRM_L/logicng-2.2.0.jar"

ANTLR_DIR="$DIRM_L/antlr-runtime-4.8.jar"

#Add paths to other libraries here

CLASSPATH=".:$CLASSPATH:$DIRM:$DIRM_L:$SAT4J_DIR:$LOGICNG_DIR:$ANTLR_DIR"

export CLASSPATH

javac *.java

java A2main $*
