@echo off

::
:: Gradle start up script for Windows
::

setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%\

set DEFAULT_JVM_OPTS=

:: Find java.exe
set JAVA_EXE=
if defined JAVA_HOME set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if not defined JAVA_EXE set JAVA_EXE=java.exe

:: Execute Gradle wrapper jar
set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

set CMD_LINE_ARGS=
:setupArgs
if "%1"=="" goto doneArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% "%1"
shift
goto setupArgs
:doneArgs

%JAVA_EXE% %DEFAULT_JVM_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%

endlocal
