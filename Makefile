JAVA_MAIN=src/main/java
APP=app/*/*
CONTROL=control/*/*
MODEL=model/*/*
VIEW=view/*/*

JAVA_TEST=src/test/java
OUT=build
MAIN=app.launcher.App
LIB=lib


# For debugging, please do not modify
PROCESS:=model/processor

processor:
	@javac -implicit:class -cp "$(JAVA_MAIN):$(LIB)/*" -d $(OUT) $(JAVA_MAIN)/$(PROCESS)/*

clean :
	@echo "⏳ Cleaning binary files..."
	@rm -rf $(OUT)/*
	@echo "✅ Cleaned successfully..."
compile : processor
	@echo "⏳ Compiling project..."
	@javac -implicit:class -cp "$(JAVA_MAIN):$(LIB)/*:$(OUT)" -processor model.processor.AutoSetterProcessor -d $(OUT) src/main/java/app/launcher/App.java
	@echo "✅ Compiled successfully"
run :
	@echo "⏳ Running project..."
	@java -cp "$(OUT):$(LIB)/*" $(MAIN)

all : clean compile run

debug-compile: clean processor
	@javac -cp "$(JAVA_MAIN):$(LIB)/*:$(OUT)" -processor model.processor.AutoSetterProcessor -d $(OUT) src/main/java/app/launcher/Debug.java
	@java -cp "$(OUT):$(LIB)/*" app.launcher.Debug

debug-run:
	@java -cp "$(OUT):$(LIB)/*" app.launcher.Debug

quickdeb: clean
	@javac -cp "$(JAVA_MAIN):$(LIB)/*:$(OUT)" -d $(OUT) src/main/java/app/launcher/Debug.java
	@java -cp "$(OUT):$(LIB)/*" app.launcher.Debug