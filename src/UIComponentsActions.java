import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class UIComponentsActions extends AnAction {
    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public UIComponentsActions() {
        // Set the menu item name.
        super();
    }

    @Override
    public void update(@NotNull AnActionEvent e){
        //Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project, editor and open java file
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
        if(this.toString().contains("JFrame")){
            code = "this.add(jpanel);\n" +
                    "this.setSize(1000, 600);\n" +
                    "this.setTitle(title);\n" +
                    "this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n" +
                    "this.setVisible(true);";
        }else if(this.toString().contains("JPanel")){
            code = "JPanel jpanel = new JPanel()\n" +
                    "//jpanel.setLayout(new GridLayout(row, col));";
        }else if(this.toString().contains("JButton")){
            code = "JButton jbutton = new JButton(text)\n" +
                    "jbutton.addActionListener(e -> {/*Do something*/} );\n" +
                    "jpanel.add(jbutton)";
        }
        //Insert final codesnippet
        String finalCode = code;
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.insertString(offset, finalCode)
        );
    }
}

