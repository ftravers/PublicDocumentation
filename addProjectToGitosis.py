#!/usr/bin/python

'''Purpose of this module is to allow programmatic access to the gitosis.conf file

Inputs are: 
file - gitosis.conf file path
group - the group you want to update
project - the name of the project you want to add
'''
file = ""
group = ""
project = ""

def addProject():
    gitosisFile = open(file, 'r')
    for line in gitosisFile:
        if isGroupLine(line):
            

def main():
    setValidateArgs(sys.argv)
    addProject()

def setValidateArgs(args):
    global file
    global group
    global project
    incorrectNumArgs = false
    fail = false
    if len(args) != 4:
        incorrectNumArgs = true
    if incorrectNumArgs:
        fail = true
        print "Incorrect number of args, should be 3 was: " + str(len(args))
    file = args[1]
    group = args[2]
    project = args[3]





















import re
import sys
import os

if __name__ == '__main__':
  main()


