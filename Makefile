JAVA_MAIN=src/main/java
APP=app/*
CONTROL=/control/*
MODEL=model/*
TOOLS=tools/*
GAME_VIEW=view/game/*.java
ANNOTATIONS=view/game/annotations/*
GAME_FRAME=view/*.java

JAVA_TEST=src/test/java
OUT=build
MAIN=view.Debug
LIB=lib


clean :
	@echo "⏳ Cleaning binary files..."
	@rm -rf $(OUT)/*
	@echo "✅ Cleaned successfully..."
compile :
	@echo "⏳ Compiling project..."
	@javac -cp "$(LIB)/*" -d $(OUT) $(JAVA_MAIN)/$(APP) $(JAVA_MAIN)/$(CONTROL) $(JAVA_MAIN)/$(MODEL) $(JAVA_MAIN)/$(TOOLS) $(JAVA_MAIN)/$(GAME_VIEW) $(JAVA_MAIN)/$(ANNOTATIONS) $(JAVA_MAIN)/$(GAME_FRAME) $(JAVA_TEST)/*/*
	@echo "✅ Compiled successfully"
run :
	@echo "⏳ Running project..."
	@java -cp "$(OUT):$(LIB)/*" $(MAIN)

test :
	@java -cp "$(LIB)/*:$(OUT)" org.junit.platform.console.ConsoleLauncher --scan-class-path

all : clean compile test run


# For debugging, please do not modify
PROCESS:=processor

processor:
	javac -cp "$(JAVA_MAIN):$(LIB)/*" -d $(OUT) $(JAVA_MAIN)/$(PROCESS)/*

debug:
	javac -cp "$(JAVA_MAIN):$(LIB)/*:$(OUT)" -processor $(PROCESS).AutoSetterProcessor -d $(OUT) src/main/java/view/Debug.java
	java -cp "$(OUT):$(LIB)/*" view.Debug