import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DialogActions extends AnAction {
    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public DialogActions() {
        // Set the menu item name.
        super();
    }

    @Override
    public void update(@NotNull AnActionEvent e){
        //Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor
        boolean isJava = false;
        try{
            isJava = e.getData(PlatformDataKeys.VIRTUAL_FILE).getFileType().getName().equals("JAVA");
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }
        e.getPresentation().setVisible(project != null && editor != null && isJava);
    }

    public void actionPerformed(@NotNull AnActionEvent event) {
        //Get all the required data from data keys
        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = event.getProject();
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final int offset = editor.getCaretModel().getOffset();
        //Making the replacement

        String code = "";
        if(this.toString().contains("Input")){
            code = "String eingabe = JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);";
        }else if(this.toString().contains("Options")){
            code = "int option = JOptionPane.showOptionDialog(null, message, title, options ...);";
        }else if(this.toString().contains("Message")){
            code = "JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);";
        }else if(this.toString().contains("Confirm")){
            code = "int result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION);";
        }else if(this.toString().contains("Custom")){
            code = "JTextField suchen = new JTextField();\n" +
                    "            JTextField ersetzen = new JTextField();\n" +
                    "            Object[] message = {\"Suchen\", suchen,\n" +
                    "                    \"Ersetzen\", ersetzen};\n" +
                    "\n" +
                    "            JOptionPane pane = new JOptionPane(message,\n" +
                    "                    JOptionPane.PLAIN_MESSAGE,\n" +
                    "                    JOptionPane.OK_CANCEL_OPTION);\n" +
                    "            pane.createDialog(null, title).setVisible(true);";
        }
        String finalCode = code;
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.insertString(offset, finalCode)
        );
    }
}