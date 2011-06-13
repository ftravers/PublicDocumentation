#!/bin/bash -x

cd ..
for dir in `ls -d */`
do
    echo $dir
    echo `pwd`
    cd $dir
    echo `pwd`
    git pull origin master
    git commit -am'.'
    git push origin master
    cd ..
done
