#!/bin/bash
binDir="$HOME/bin"

if [ ! -d "$binDir" ]; then
    mkdir $binDir
fi

webStageDir="$binDir/webStageDir"
if [ ! -d "$webStageDir" ]; then
    mkdir $webStageDir
fi

cd $webStageDir

if [ ! -d "$webStageDir/training" ]; then
    mkdir "$webStageDir/training"
fi

if [ ! -d "$webStageDir/jeos" ]; then
    mkdir "$webStageDir/jeos"
fi

if [ ! -d "$webStageDir/styles" ]; then
    mkdir "$webStageDir/styles"
fi

if [ ! -d "$webStageDir/images" ]; then
    mkdir "$webStageDir/images"
fi

docsSrcDir="$HOME/projects/documentation/"
cd $docsSrcDir/training
git archive master | tar -x -C "$webStageDir/training"

cd $docsSrcDir/jeos
git archive master | tar -x -C "$webStageDir/jeos"

cd $docsSrcDir/styles
git archive master | tar -x -C "$webStageDir/styles"

cd $docsSrcDir/images
git archive master | tar -x -C "$webStageDir/images"


cd $docsSrcDir
cp index.textile build.xml $webStageDir
cd $webStageDir
ant generate-html -DwebStageDir=$webStageDir