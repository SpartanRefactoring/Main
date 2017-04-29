package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.RemoveCurlyBracesFromIfStatement;
import il.org.spartan.Leonidas.plugin.utils.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * General utils class
 *
 * @author 01-12-2016
 */
public enum Utils {
    ;
    static Logger logger = new Logger(Utils.class);

    /**
     * @param candidate the element that might be in the list
     * @param list      JD
     * @param <T>       the type of the list and the element.
     * @return true iff candidate is in the list.
     */
    @SafeVarargs
    public static <T> boolean in(T candidate, T... list) {
        return list != null && Arrays.stream(list).anyMatch(elem -> elem.equals(candidate));
    }

    /**
     * @param p the current project
     * @return the PsiManager of the project.
     */
    public static PsiManager getPsiManager(Project p) {
        return PsiManager.getInstance(p);
    }

    /**
     * @param root psi element that represents code blocks.
     * @param i identifier
     * @return list of all the appearance of the identifier.
     */
    public static List<PsiIdentifier> getAllReferences(PsiElement root, PsiIdentifier i) {
        List<PsiIdentifier> identifiers = new ArrayList<>();
        if (root != null && i != null)
			root.accept(new JavaRecursiveElementVisitor() {
				@Override
				public void visitIdentifier(PsiIdentifier i) {
					super.visitIdentifier(i);
					if (!i.getText().equals(i.getText()))
						return;
					PsiElement context = i.getContext();
					if (iz.variable(context) || iz.referenceExpression(context))
						identifiers.add(i);
				}
			});
        return identifiers;
    }

    /**
     * @param f
     * @return
     */
    public static PsiImportList getImportList(PsiFile f) {
        PsiJavaFile ff = (PsiJavaFile) f;
        ff.getImportList();
        return az.importList(PsiTreeUtil.getChildOfType(f, PsiImportList.class));
    }

    public static PsiClass getClassFromFile(PsiJavaFile f) {
        return f.getClasses()[0];
    }

    public static Document getDocumentFromPsiElement(PsiElement e) {
        PsiFile associatedFile = e.getContainingFile();
        return PsiDocumentManager.getInstance(associatedFile.getProject()).getDocument(associatedFile);
    }

    public static Project getProject() {
        return ProjectManager.getInstance().getOpenProjects()[0];
    }

    public static <T extends PsiElement> List<T> getChildrenOfType(@Nullable PsiElement e, @NotNull Class<T> aClass) {
        Wrapper<List<T>> w = new Wrapper<>(new LinkedList<T>());
        assert e != null;
        e.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement e) {
                super.visitElement(e);
                if (aClass.isInstance(e))
					w.get().add((T) e);
            }
        });
        return w.get();
    }

    public static void main(String[] args) {
        try {
            System.out.println(getSourceCode(RemoveCurlyBracesFromIfStatement.class));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public static String getSourceCode(Class<?> c) throws IOException {
        try (InputStream is = c.getClassLoader().getResourceAsStream(c.getName().replaceAll("\\.", "/") + ".java")) {
            return IOUtils.toString(new BufferedReader(new InputStreamReader(is)));
        } catch (IOException e) {
            logger.error("", e);
        }

        return "";
    }

    /**
     * fixed problems on machines where the project path has spaces in it:
     * the .getPath() of getResource inserts %20 instead of spaces
     *
     * @param path the path to be fixed
     * @return fixed path. on error, returns null
     */
    public static String fixSpacesProblemOnPath(String path) {
        String fixedPath = null;
        try {
            fixedPath = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        return fixedPath;
    }
}
