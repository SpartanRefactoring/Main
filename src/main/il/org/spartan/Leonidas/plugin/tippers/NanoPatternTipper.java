package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import il.org.spartan.Leonidas.plugin.tipping.TipperCategory;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a tipper that changes the code of the user to a code that need the creation of
 * a special environment.
 *
 * @author Roey Maor, michalcohen
 * @since 26-12-2016
 */
public abstract class NanoPatternTipper<N extends PsiElement> implements Tipper<N>, TipperCategory.Nanos {
    protected static <N extends PsiElement> boolean anyTips(final Collection<Tipper<N>> ts, final N n) {
        return n != null && ts.stream().anyMatch(t -> t.canTip(n));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected static <N extends PsiElement> Tipper<N> firstTipperThatCanTip(final Collection<Tipper<N>> ts, final N n) {
        return ts.stream().filter(t -> t.canTip(n)).findFirst().get();
    }

    public static <N extends PsiElement> Tip firstTip(final Collection<Tipper<N>> ts, final N n) {
        return firstTipperThatCanTip(ts, n).tip(n);
    }

    String className() {
        return this.getClass().getSimpleName();
    }

    /**
     * @param e the PsiElement on which the tip will be applied
     * @return an element tip to apply on e.
     */
    public Tip tip(final N e) {
        return !canTip(e) ? null : new Tip(description(e), e, this.getClass()) {
            @Override
            public void go(PsiRewrite r) {
                PsiElement e_tag = createReplacement(e);
                new WriteCommandAction.Simple(e.getProject(), e.getContainingFile()) {
                    @Override
                    protected void run() throws Throwable {
                        if (!Toolbox.getInstance().playground) {
                            createEnvironment(e);
                        }
                        e.replace(e_tag);
                    }
                }.execute();
            }
        };
    }

    /**
     * This method should be override in order to create the psi element that will
     * replace e.
     *
     * @param e - the element to be replaced
     * @return the PsiElement that will replace e.
     */
    public abstract PsiElement createReplacement(N e);

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "ResultOfMethodCallIgnored"})
    private PsiFile createUtilsFile(PsiElement e, PsiDirectory d) throws IOException {
        URL is = getClass().getResource("/spartanizer/SpartanizerUtils.java");
        File file = new File(is.getPath());
        FileType type = FileTypeRegistry.getInstance().getFileTypeByFileName(file.getName());
        file.setReadable(true, false);
        String s = IOUtils.toString(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/spartanizer/SpartanizerUtils.java"))));
        PsiFile pf = PsiFileFactory.getInstance(e.getProject()).createFileFromText("SpartanizerUtils.java", type, s);
        d.add(pf);
        Arrays.stream(d.getFiles()).filter(f -> "SpartanizerUtils.java".equals(f.getName())).findFirst().get().getVirtualFile().setWritable(false);
        Toolbox.getInstance().excludeFile(pf);
        return pf;
    }

    /**
     * @param e the PsiElement that the tip is applied to
     * @return the PsiFile in which e is contained
     * @throws IOException if for some reason writing to the users disk throws exception.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private PsiFile insertSpartanizerUtils(PsiElement e) throws IOException {
        PsiFile pf;
        PsiDirectory srcDir = e.getContainingFile().getContainingDirectory();
        // creates the directory and adds the file if needed
        try {
            srcDir.checkCreateSubdirectory("spartanizer");
            pf = createUtilsFile(e, srcDir.createSubdirectory("spartanizer"));
        } catch (IncorrectOperationException x) {
            PsiDirectory pd = Arrays.stream(srcDir.getSubdirectories()).filter(d -> "spartanizer".equals(d.getName())).findAny().get();
            pf = Arrays.stream(pd.getFiles()).noneMatch(f -> "SpartanizerUtils.java".equals(f.getName()))
                    ? createUtilsFile(e, pd)
                    : Arrays.stream(pd.getFiles()).filter(f -> "SpartanizerUtils.java".equals(f.getName())).findFirst()
                    .get();
        }
        return pf;
    }

    /**
     * Inserts "import static spartanizer/SpartanizerUtils/*;" to the users code.
     *
     * @param e - the PsiElement on which the tip is applied.
     * @param f - the psi file in which e is contained.
     */
    @SuppressWarnings("ConstantConditions")
    private void insertImportStatement(PsiElement e, PsiFile f) {
        PsiImportStaticStatement piss = JavaPsiFacade.getElementFactory(e.getProject()).createImportStaticStatement(PsiTreeUtil.getChildOfType(f, PsiClass.class), "*");
        PsiImportList pil = ((PsiJavaFile) e.getContainingFile()).getImportList();
        if (Arrays.stream(pil.getImportStaticStatements()).noneMatch(x -> x.getText().contains("spartanizer")))
            pil.add(piss);

    }

    /**
     * Inserts import statement and copies file in order to make the nano patterns compile
     *
     * @param e - the PsiElement on which the tip is applied.
     * @throws IOException - if for some reason writing new file to the users disk throws exception.
     */
    private void createEnvironment(final N e) throws IOException {
        insertImportStatement(e, insertSpartanizerUtils(e));
    }

    @Override
    public String name() {
        return "NanoPatternTipper";
    }

    protected abstract Tip pattern(N ¢);
}
