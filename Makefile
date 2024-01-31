JAVA=src/main/java
OUT=build
MAIN=control.GameController

compile :
	@javac -d $(OUT) $(JAVA)/control/GameController.java  

run :
	@java -cp $(OUT) $(MAIN)

all : compile run

default : all