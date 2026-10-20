#!/bin/sh
# standalone quickvo launcher - see README.md in this directory
# (sagacity-quickvo.jar and the JDBC driver jars go into libs/)
cd "$(dirname "$0")"
java -cp "./libs/*" org.sagacity.quickvo.QuickVOStart quickvo.xml
