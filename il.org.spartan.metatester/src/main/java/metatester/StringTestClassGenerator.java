package metatester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static metatester.aux_layer.FileUtils.makePath;
import static metatester.aux_layer.FileUtils.packageName;

/**
 * Test Class generator based on String only.
 * THIS IS NOT WORKING, AND ONLY HERE FOR PORPOSES OF CONTINUATION.
 *
 * @author Oren Afek
 * @since 3/26/2017
 */

@SuppressWarnings("all")
public class StringTestClassGenerator implements TestClassGenerator {
    private final Class<?> testClass;
    private final String sourcePath;
    public final String packageName;
    private final String testName;
    private final File originalSourceFile;

    public StringTestClassGenerator(final Class<?> testClass, final String testName, final File sourceFile) {
        this.testClass = testClass;
        sourcePath = makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\", testClass));
        packageName = packageName("\\.", testClass);
        this.testName = testName;
        originalSourceFile = sourceFile;
    }

    @Override
    public Class<?> generate(final String testClassName) {
        final Collection<? extends SourceLine> $ = readAllLines();
        return loadClass(testClassName,
                getClassString($.stream().filter(λ -> λ instanceof TestLine).map(λ -> (TestLine) λ).collect(Collectors.toList()),
                        $.stream().filter(λ -> λ instanceof ImportLine).map(λ -> (ImportLine) λ).collect(Collectors.toList()), testClassName),
                testClass, sourcePath);
    }

    @SuppressWarnings({"unused", "resource"})
    private List<SourceLine> readAllLines() {
        final List<SourceLine> $ = new ArrayList<>();
        try {
            final BufferedReader linesStream = new BufferedReader(new FileReader(originalSourceFile));
            String line = linesStream.readLine();
            final SourceLine.SourceLineFactory factory = new SourceLine.SourceLineFactory(testName);
            for (int ¢ = 1; line != null; ++¢) {
                if (line.contains("void") && line.contains("()"))
                    factory.setTestMethodName(
                            line.replace("public void ", "").replace("()", "").replace("{", "").replace("@SuppressWarnings(\"static-method\")", "").trim());
                $.add(factory.createSourceLine(line, ¢));
                line = linesStream.readLine();
            }
            $.remove(SourceLine.EMPTY);
        } catch (final IOException ignore) {/**/}
        return $;
    }

    private String getClassString(final Collection<TestLine> testsLines, final Collection<ImportLine> ls, final String className) {
        return packageHeaderString(packageName) + "\n" + importStatementString(ls) + "\n" + classHeaderString(className) + "\n" + testMethods(testsLines)
                + "\n}";
    }

    private String testMethods(final Collection<TestLine> testsLines) {
        return testsLines.stream().map(TestLine::generateTestMethod).reduce((acc, s1) -> acc + "\n\n" + s1).orElse("");
    }

    private String importStatementString(final Collection<ImportLine> ls) {
        return ls.stream().filter(λ -> !"import org.junit.runner.RunWith;".equals(λ.getContent().trim())).reduce("",
                (s, importLine) -> s + importLine.getContent() + "\n", (s, s2) -> s + s2);
    }

    private String classHeaderString(final String className) {
        return "@SuppressWarnings(\"static-method\")\npublic class " + className + " { \n";
    }

    private String packageHeaderString(final String packageNameString) {
        return "package " + packageNameString + ";";
    }
}
