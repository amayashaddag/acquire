JAVA=src/main/java
OUT=build
MAIN=Debug
LIB=lib

default : all

compile :
	javac -d $(OUT) $(JAVA)/view/$(MAIN).java  

run :
	java -cp $(OUT):$(LIB) view.$(MAIN)

all : compile run