package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.PsiFile;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


/**
 * A static util class that handles compilation inside the code
 * 
 * @author Roey Maor
 * @since 06-01-2017
 */
public class CompilationCenter {
	private static final String DUMMY_DIR_PATH = "/spartanizer/CompiledDummy/";
	private static final String DUMMY_FILE_NAME = "Test.java";
	private static File dummyCompilationTestFile;
	private static JavaCompiler compiler;
	private static boolean initialized;
	private static ByteArrayOutputStream output;
	private static ByteArrayOutputStream errors;

	/**
	 * initialize the compilation center: -dummy directory and file -compiler
	 * -output streams
	 */
	public static void initialize() {
		if (initialized)
			return;

		assert (CompilationCenter.class.getResource(DUMMY_DIR_PATH + DUMMY_FILE_NAME) != null);
		String fixedPath = Utils.fixSpacesProblemOnPath(
				CompilationCenter.class.getResource(DUMMY_DIR_PATH + DUMMY_FILE_NAME).getPath());
		dummyCompilationTestFile = new File(fixedPath);
		compiler = ToolProvider.getSystemJavaCompiler();
		output = new ByteArrayOutputStream();
		errors = new ByteArrayOutputStream();
		initialized = true;
	}

	/**
	 * Checks whether or not the given PsiFile has any compilation errors
	 *
     * @param f
     *            - JD
	 * @return has any compilation errors
	 */
    public static boolean hasCompilationErrors(PsiFile f) {
        compile(f);
        return errors.toString().length() != 0;
    }

	/**
	 * Compile the given PsiFile and update the output buffers accordingly
	 * 
	 * @param f
	 *            - JD
	 */
	private static void compile(PsiFile f) {
		if (!initialized)
			initialize();
		String source = f.getText();
		try {
			Files.write(dummyCompilationTestFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
        }
		output.reset();
		errors.reset();
		compiler.run(null, output, errors, dummyCompilationTestFile.getPath());
	}

	/**
	 *
	 * @return The compilation errors that result from the latest compile with
	 *         the compile function
	 */
	public static String getLatestCompilationErrors() {
		if (!initialized)
			initialize();
        return errors.toString();
    }

	/**
	 *
	 * @return The compilation output that result from the latest compile with
	 *         the compile function
	 */
	public static String getLatestCompilationOutput() {
		if (!initialized)
			initialize();
        return output.toString();
    }

}
