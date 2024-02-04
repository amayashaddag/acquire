JAVA=src/main/java
OUT=build
MAIN=control.GameController
LIB:=

default : all

compile :
	javac -d $(OUT) $(JAVA)/control/GameController.java  

run :
	java -cp $(OUT):$(LIB) $(MAIN)

all : compile run