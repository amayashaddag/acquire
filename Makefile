JAVA_MAIN=src/main/java
OUT=build
MAIN=control.GameController

compile :
	@javac -d $(OUT) $(JAVA_MAIN)/control/GameController.java  

run :
	@java -cp $(OUT) $(MAIN)

all : compile run

default : all