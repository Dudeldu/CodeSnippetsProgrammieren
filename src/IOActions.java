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

public class IOActions extends AnAction {
    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public IOActions() {
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
        if(this.toString().contains("Append")){
            code = "try(FileOutputStream fileOutputStream =\n" +
                    "                    new FileOutputStream(new File(filename), true)){\n" +
                    "            fileOutputStream.write((text).getBytes());\n" +
                    "        }catch (IOException e){\n" +
                    "            e.printStackTrace();\n" +
                    "        }";
        }else if(this.toString().contains("whole")){
            code = "String entireFileText = new Scanner(new File(filename)).useDelimiter(\"\\\\A\").next();";
        }else if(this.toString().contains("Read file")){
            code = "try (BufferedReader reader = new BufferedReader(new FileReader(new File(file)))){\n" +
                    "                   while ((strCurrentLine = objReader.readLine()) != null) {\n" +
                    "                       System.out.println(strCurrentLine);\n" +
                    "                   }" +
                    "                } catch (IOException e1) {\n" +
                    "                    e1.printStackTrace();\n" +
                    "                }";
        }else if(this.toString().contains("Write to file")){
            code = "try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file)))) {\n" +
                    "                    writer.write(content);\n" +
                    "                } catch (IOException e1) {\n" +
                    "                    e1.printStackTrace();\n" +
                    "                }";
        }
        String finalCode = code;
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.insertString(offset, finalCode)
        );
    }
}
