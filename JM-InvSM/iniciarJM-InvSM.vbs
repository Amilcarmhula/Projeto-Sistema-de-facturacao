Set shell = CreateObject("WScript.Shell")
cmd = "java --module-path ""C:\Program Files\Java\javafx-sdk-23.0.1\lib"" --add-modules javafx.controls,javafx.fxml -jar ""C:\Users\JM-Tecnologias\Documents\NetBeansProjects\Projeto Sistema de facturacao\JM-InvSM\dist\JM-InvSM.jar"""
shell.Run cmd, 0
Set shell = Nothing
