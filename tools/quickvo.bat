@echo off
rem standalone quickvo launcher - see README.md in this directory
rem (sagacity-quickvo.jar and the JDBC driver jars go into libs\)
cd /d %~dp0
java -cp "libs/*" org.sagacity.quickvo.QuickVOStart quickvo.xml
