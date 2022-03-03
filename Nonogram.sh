#!/bin/bash

cd src/ && javac nonogrampkg/*.java
echo "compile complete"

if [ $# -lt 1 ]; then 
    echo "Executing Nonograms"
    java nonogrampkg/Nonogram
elif [ $# -eq 1 ]; then
    echo "Executing Nonograms with [$1]"
    java nonogrampkg/Nonogram $1
else
    echo "Usage [./Nonogram.sh [optional tester command]"
fi

rm nonogrampkg/*.class
echo "Cleaning Up .class Files"

cd ..
