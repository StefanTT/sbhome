@echo off
setlocal

cd lib
set CLASSPATH=%CLASSPATH%;.
java -jar selfbus-sbhome-web-0.1-SNAPSHOT.jar org.selfbus.sbhome.web.WebServer

if %ERRORLEVEL% neq 0 (
   echo.
   echo Please note: sbhome requires Java 7
   echo (This might not be related to the problem above)
   echo.
   pause
)
