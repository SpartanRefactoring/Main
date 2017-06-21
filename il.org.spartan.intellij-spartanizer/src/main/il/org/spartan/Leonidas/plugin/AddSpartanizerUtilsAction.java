package il.org.spartan.Leonidas.plugin;

import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Amir Sagiv
 * @since 14/06/2017
 */
public class AddSpartanizerUtilsAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        new WriteCommandAction.Simple(e.getProject()) {
            @Override
            protected void run() throws Throwable {
                createEnvironment(e);
            }
        }.execute();

    }

    public void createEnvironment(AnActionEvent e) {
        PsiFile pf;
        try {
            pf = getPsiClassFromContext(e).getContainingFile();
        }catch (NullPointerException exception){
            return;
        }
        PsiDirectory srcDir = pf.getContainingDirectory();
        // creates the directory and adds the file if needed
        try {
            srcDir.checkCreateSubdirectory("spartanizer");
            pf = createUtilsFile(srcDir.createSubdirectory("spartanizer"));
        } catch (IncorrectOperationException x) {
            PsiDirectory pd = Arrays.stream(srcDir.getSubdirectories()).filter(d -> "spartanizer".equals(d.getName())).findAny().get();
            try {
                pf = Arrays.stream(pd.getFiles()).noneMatch(f -> "SpartanizerUtils.java".equals(f.getName()))
                        ? createUtilsFile(pd)
                        : Arrays.stream(pd.getFiles()).filter(f -> "SpartanizerUtils.java".equals(f.getName())).findFirst()
                        .get();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    @Nullable
    private PsiClass getPsiClassFromContext(AnActionEvent ¢) {
        return PsiTreeUtil.getParentOfType(getPsiElementFromContext(¢), PsiClass.class);
    }


    @Nullable
    private PsiElement getPsiElementFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        return psiFile == null || editor == null ? null : psiFile.findElementAt(editor.getCaretModel().getOffset());
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "ResultOfMethodCallIgnored"})
    private PsiFile createUtilsFile(PsiDirectory d) throws IOException {
        URL is = getClass().getResource("/spartanizer/SpartanizerUtils.java");
        File file = new File(is.getPath());
        FileType type = FileTypeRegistry.getInstance().getFileTypeByFileName(file.getName());
        file.setReadable(true, false);
        String s = IOUtils.toString(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/spartanizer/SpartanizerUtils.java"))));
        PsiFile pf = PsiFileFactory.getInstance(Utils.getProject()).createFileFromText("SpartanizerUtils.java", type, s);
        d.add(pf);
        Arrays.stream(d.getFiles()).filter(f -> "SpartanizerUtils.java".equals(f.getName())).findFirst().get().getVirtualFile().setWritable(false);
        Toolbox.getInstance().excludeFile(pf);
        return pf;
    }
}