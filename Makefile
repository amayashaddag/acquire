JAVA_MAIN=src/main/java
JAVA_TEST=src/test/java
OUT=build
MAIN=app.App
LIB=lib

clean :
	@rm -rf $(OUT)/*

compile :
	@javac -cp "$(LIB)/*" -d $(OUT) $(JAVA_MAIN)/*/* $(JAVA_TEST)/*/*

run :
	@java -cp "$(OUT):$(LIB)/*" $(MAIN)

test :
	@java -cp "$(LIB)/*:$(OUT)" org.junit.platform.console.ConsoleLauncher --scan-class-path

all : clean compile test run

default : all