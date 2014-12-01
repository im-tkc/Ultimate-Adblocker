@echo off

set "javaExePath=Java\jre1.8.0_25\bin\java.exe"
set "fullJavaExePath=%ProgramFiles%\%javaExePath%"

IF NOT EXIST "%fullJavaExePath%" (
	set "fullJavaExePath=%ProgramFiles(x86)%\%javaExePath%"
)

IF NOT EXIST "%fullJavaExePath%" (
	echo Looks like Java Runtime Environment 8 is not installed. Please install it on the default directory.
	exit
)

"%fullJavaExePath%" -jar Adblocker.jar
