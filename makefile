compile:
	cd src/ && javac nonogrampkg/*.java

run:
	cd src/ && java nonogrampkg/Nonogram

clean:
	rm src/nonogrampkg/*.class

all: compile run clean
	echo "all done"

rocco: compile