JAVA_MAIN=src/main/java
APP=app/*/*
CONTROL=control/*/*
MODEL=model/*/*
VIEW=view/*/*

JAVA_TEST=src/test/java
OUT=build
<<<<<<< HEAD
MAIN=app.launcher.App
=======
MAIN=app.launcher.NetworkDebug
>>>>>>> 89f18ed (Working on game sync between players)
LIB=lib


clean :
	@echo "⏳ Cleaning binary files..."
	@rm -rf $(OUT)/*
	@echo "✅ Cleaned successfully..."
compile :
	@echo "⏳ Compiling project..."
	@javac -cp "$(LIB)/*" -d $(OUT) $(JAVA_MAIN)/$(APP) $(JAVA_MAIN)/$(CONTROL) $(JAVA_MAIN)/$(MODEL) $(JAVA_MAIN)/$(VIEW) $(JAVA_TEST)/*/*
	@echo "✅ Compiled successfully"
run :
	@echo "⏳ Running project..."
	@java -cp "$(OUT):$(LIB)/*" $(MAIN)

test :
	@java -cp "$(LIB)/*:$(OUT)" org.junit.platform.console.ConsoleLauncher --scan-class-path

all : clean compile test run


# For debugging, please do not modify
PROCESS:=model/processor

processor:
	@javac -cp "$(JAVA_MAIN):$(LIB)/*" -d $(OUT) $(JAVA_MAIN)/$(PROCESS)/*

debug: clean processor
	@javac -cp "$(JAVA_MAIN):$(LIB)/*:$(OUT)" -processor model.processor.AutoSetterProcessor -d $(OUT) src/main/java/app/launcher/Debug.java
	@java -cp "$(OUT):$(LIB)/*" app.launcher.Debug

quickdeb: clean
	@javac -cp "$(JAVA_MAIN):$(LIB)/*:$(OUT)" -d $(OUT) src/main/java/app/Debug.java
	@java -cp "$(OUT):$(LIB)/*" app.Debug