JAVA_MAIN=src/main/java
JAVA_TEST=src/test/java
OUT=build
MAIN=app.App
LIB=lib

clean :
	@echo "⏳ Cleaning binary files..."
	@rm -rf $(OUT)/*
	@echo "✅ Cleaned successfully..."
compile :
	@echo "⏳ Compiling project..."
	@javac -cp "$(LIB)/*" -d $(OUT) $(JAVA_MAIN)/*/* $(JAVA_TEST)/*/*
	@echo "✅ Compiled successfully"
run :
	@echo "⏳ Running project..."
	@java -cp "$(OUT):$(LIB)/*" $(MAIN)

test :
	@java -cp "$(LIB)/*:$(OUT)" org.junit.platform.console.ConsoleLauncher --scan-class-path

all : clean compile test run

default : all