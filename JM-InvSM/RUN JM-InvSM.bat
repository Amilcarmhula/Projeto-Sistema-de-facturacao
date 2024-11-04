@echo off
set JFX_PATH="C:\Program Files\Java\javafx-sdk-23.0.1\lib"
cd /d "%~dp0dist"
java --module-path %JFX_PATH% --add-modules javafx.controls,javafx.fxml -jar JM-InvSM.jar
