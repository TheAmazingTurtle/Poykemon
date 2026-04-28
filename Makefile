JAVAC = javac
JAVA = java
MAIN = PoykemonExecutable
SOURCES = $(wildcard *.java)

.PHONY: all compile run clean

all: compile

compile:
	$(JAVAC) $(SOURCES)

run: compile
	$(JAVA) $(MAIN)

clean:
	powershell -NoProfile -Command "Remove-Item -Force *.class -ErrorAction SilentlyContinue"
