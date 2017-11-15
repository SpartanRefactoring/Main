package metatester;

import metatester.aux_layer.SophisticatedClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;

import static metatester.aux_layer.FileUtils.*;

/**
 * Basic operations for a generation of meta test class.
 * Meant to be implemented by an AST based interpreter and generator such as @link{{@link ASTTestClassGenerator} ASTTestClassGenerator}
 * that is based on eclipse's JDT AST library.
 * Future Note: This should also be implemented by PSI interpreter to match JetBrain's native AST Representation.
 * @author Oren Afek
 * @since 2017-04-12
 **/

@SuppressWarnings("unused")
public interface TestClassGenerator {
    String JAVA_SUFFIX = ".java";

    default Class<?> generate(final String testClassName) {
        return Object.class;
    }

    default Class<?> generate(final String testClassName, final File originalSourceFile) {
        return Object.class;
    }


    /**
     * Compiles new test source code on runtime.
     *
     * @param className  new compiled class's name
     * @param sourceCode the class's source code
     * @param testClass  the original test on whom the new compiled class is based on
     * @param sourcePath the path to testClass
     */
    @SuppressWarnings("resource")
    default File compileSourceCode(final String className, final String sourceCode, final Class<?> testClass,
                                   final String sourcePath) {
        FileWriter writer;
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, Locale.getDefault(), Charset.defaultCharset());
        final File sourceFile = new File(sourcePath);
        final File classFile = new File(generatedClassPath(testClass));
        try {
            if (sourceFile.exists()) {
                sourceFile.delete();
            }
            sourceFile.createNewFile();
            writer = new FileWriter(sourceFile);
            writer.write(sourceCode);
            writer.close();

            //remove binary class file:
            File oldBinary = new File(classFile.getPath() + FS + packageName(FS, testClass) + FS + className + ".class");
            if (oldBinary.exists()) {
                oldBinary.delete();
            }
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(classFile));
            compiler
                    .getTask(new StringWriter(), fileManager, null, null, null,
                            fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(sourceFile)))
                    .call();

        } catch (final IOException ignore) {
            System.out.println("");
        } finally {
            try {
                fileManager.close();
                classFile.deleteOnExit();
            } catch (IOException ignore) {
            }

        }
        return new File(makePath(classFile.getAbsolutePath(), packageName(FS, testClass), className + ".class"));
    }

    /**
     * Loads (and compiles) new test source code on runtime.
     *
     * @param $          new testClassName (including 'Meta' suffix)
     * @param sourceCode the classes source code
     * @param testClass  the original test on whom the new compiled class is based on
     * @param sourcePath the path to testClass
     * @return the class object of the new test class
     */
    @SuppressWarnings("resource")
    default Class<?> loadClass(final String $, final String sourceCode, final Class<?> testClass,
                               final String sourcePath) {
        File binary = compileSourceCode($, sourceCode, testClass, sourcePath);
        try {
            System.gc();
            Thread.sleep(1000); // to make sure it takes the appropriate file
        } catch (InterruptedException ignore) {
        }
        try {
            return /*ClassLoader.getSystemClassLoader()*/
                    new SophisticatedClassLoader(new URL[]
                            {
                                    new File("./").toURI().toURL(),
                                    binary.toURI().toURL()
                            })
                            .loadMetaClass(packageName("\\.", testClass) + "." + $, binary);
        } catch (/*final IOException |*/MalformedURLException | ClassNotFoundException ignore) {
            System.out.println("");
        }
        return Object.class;
    }

    /**
     * New tests source file path
     *
     * @param sourcePath path to file
     * @param className  class's name
     * @return the total path (including the file itself)
     */
    default String getMetaTestFilePath(String sourcePath, String className) {
        return makePath(removeFileName(sourcePath), className + JAVA_SUFFIX);
    }


}
