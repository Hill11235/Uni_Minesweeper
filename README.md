# Uni_Minesweeper

Pretty broken

### Compilation instructions
To compile run the following commands in the src directory:

`javac -cp ":../libs/antlr-runtime-4.8.jar:../libs/logicng-2.2.0.jar:../libs/sat4j-pb.jar" support/*.java`
`javac -cp ":../libs/antlr-runtime-4.8.jar:../libs/logicng-2.2.0.jar:../libs/sat4j-pb.jar" agents/*.java`
`javac -cp ":../libs/antlr-runtime-4.8.jar:../libs/logicng-2.2.0.jar:../libs/sat4j-pb.jar" *.java`

### Run instructions
To run, please use the following command in the src directory:

`java -cp ":../libs/antlr-runtime-4.8.jar:../libs/logicng-2.2.0.jar:../libs/sat4j-pb.jar" A2main <P1|P2|P3|P4> ID <verbose>`

For example:

`java -cp ":../libs/antlr-runtime-4.8.jar:../libs/logicng-2.2.0.jar:../libs/sat4j-pb.jar" A2main P3 MEDIUM3`

### playSweeper problems
Try using `chmod 777` on playSweeper.sh
