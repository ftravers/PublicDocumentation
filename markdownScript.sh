#!/bin/bash

for filename in `find . -name \*.md`; do
    NAME=`echo $filename | sed -e 's/\.md//g'`
    echo "doing $NAME"
    pandoc -o $NAME.html -i $NAME.md
done
