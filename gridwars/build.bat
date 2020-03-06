@echo off
if not exist ..\build mkdir ..\build
javac -d ..\build -cp "." --module-path %JAVAFX% --add-modules javafx.controls *.java buildings\*.java timers\*.java 

pushd ..\build
if not exist gridwars\resources mkdir gridwars\resources
Xcopy /E /Y /D "..\gridwars\resources" "gridwars\resources\"

java -cp . --module-path %JAVAFX% --add-modules javafx.controls gridwars.Main
popd