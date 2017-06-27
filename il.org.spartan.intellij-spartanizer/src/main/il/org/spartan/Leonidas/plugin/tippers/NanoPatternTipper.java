package il.org.spartan.Leonidas.plugin.tippers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiImportStaticStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

import icons.Icons;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import il.org.spartan.Leonidas.plugin.tipping.TipperCategory;

/**
 * Represents a tipper that changes the code of the user to a code that need the
 * creation of a special environment.
 *
 * @author Roey Maor, michalcohen
 * @since 26-12-2016
 */
public abstract class NanoPatternTipper<N extends PsiElement> implements Tipper<N>, TipperCategory.Nanos {

	/**
	 * @param e
	 *            the PsiElement on which the tip will be applied
	 * @return an element tip to apply on e.
	 */
	@Override
	public Tip tip(final N $) {
		final PsiDirectory srcDir = $.getContainingFile().getContainingDirectory();
		try {
			srcDir.checkCreateSubdirectory("spartanizer");
			final Object[] options = { "Accept", "Cancel" };

			if (JOptionPane.showOptionDialog(new JFrame(),
					"You are about to apply a nano pattern.\n" + "Please notice that nano pattern tippers are "
							+ "code transformations that require adding a '.java' file "
							+ "to your project directory.\n" + "To apply the tip, press the Accept button.",
					"SpartanizerUtils", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Icons.Leonidas,
					options, options[1]) == 1)
				return new Tip(description($), $, this.getClass()) {
					@Override
					public void go(final PsiRewrite r) {
					}
				};
		} catch (final Exception ex) {
		}
		return !canTip($) ? null : new Tip(description($), $, this.getClass()) {
			@Override
			public void go(final PsiRewrite r) {
				final PsiElement e_tag = createReplacement($);
				new WriteCommandAction.Simple($.getProject(), $.getContainingFile()) {
					@Override
					protected void run() throws Throwable {
						if (!Toolbox.getInstance().playground && !Toolbox.getInstance().testing)
							createEnvironment($);
						$.replace(e_tag);
					}
				}.execute();
			}
		};
	}

	/**
	 * This method should be override in order to create the psi element that
	 * will replace e.
	 *
	 * @param e
	 *            - the element to be replaced
	 * @return the PsiElement that will replace e.
	 */
	public abstract PsiElement createReplacement(N e);

	@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ResultOfMethodCallIgnored" })
	private PsiFile createUtilsFile(final PsiElement e, final PsiDirectory d) throws IOException {
		final URL is = getClass().getResource("/spartanizer/SpartanizerUtils.java");
		final File file = new File(is.getPath());
		final FileType type = FileTypeRegistry.getInstance().getFileTypeByFileName(file.getName());
		file.setReadable(true, false);
		final PsiFile $ = PsiFileFactory.getInstance(e.getProject()).createFileFromText("SpartanizerUtils.java", type,
				IOUtils.toString(new BufferedReader(
						new InputStreamReader(getClass().getResourceAsStream("/spartanizer/SpartanizerUtils.java")))));
		d.add($);
		Arrays.stream(d.getFiles()).filter(λ -> "SpartanizerUtils.java".equals(λ.getName())).findFirst().get()
				.getVirtualFile().setWritable(false);
		Toolbox.getInstance().excludeFile($);
		return $;
	}

	/**
	 * @param e
	 *            the PsiElement that the tip is applied to
	 * @return the PsiFile in which e is contained
	 * @throws IOException
	 *             if for some reason writing to the users disk throws
	 *             exception.
	 */
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	private PsiFile insertSpartanizerUtils(final PsiElement e) throws IOException {
		PsiFile $;
		final PsiDirectory srcDir = e.getContainingFile().getContainingDirectory();
		// creates the directory and adds the file if needed
		try {
			srcDir.checkCreateSubdirectory("spartanizer");
			$ = createUtilsFile(e, srcDir.createSubdirectory("spartanizer"));
		} catch (final IncorrectOperationException x) {
			final PsiDirectory pd = Arrays.stream(srcDir.getSubdirectories())
					.filter(λ -> "spartanizer".equals(λ.getName())).findAny().get();
			$ = Arrays.stream(pd.getFiles()).noneMatch(λ -> "SpartanizerUtils.java".equals(λ.getName()))
					? createUtilsFile(e, pd)
					: Arrays.stream(pd.getFiles()).filter(λ -> "SpartanizerUtils.java".equals(λ.getName())).findFirst()
							.get();
		}
		return $;
	}

	/**
	 * Inserts "import static spartanizer/SpartanizerUtils/*;" to the users
	 * code.
	 *
	 * @param e
	 *            - the PsiElement on which the tip is applied.
	 * @param f
	 *            - the psi file in which e is contained.
	 */
	@SuppressWarnings("ConstantConditions")
	private void insertImportStatement(final PsiElement e, final PsiFile f) {
		final PsiImportStaticStatement piss = JavaPsiFacade.getElementFactory(e.getProject())
				.createImportStaticStatement(PsiTreeUtil.getChildOfType(f, PsiClass.class), "*");
		final PsiImportList pil = ((PsiJavaFile) e.getContainingFile()).getImportList();
		if (Arrays.stream(pil.getImportStaticStatements()).noneMatch(λ -> λ.getText().contains("spartanizer")))
			pil.add(piss);

	}

	/**
	 * Inserts import statement and copies file in order to make the nano
	 * patterns compile
	 *
	 * @param e
	 *            - the PsiElement on which the tip is applied.
	 * @throws IOException
	 *             - if for some reason writing new file to the users disk
	 *             throws exception.
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
