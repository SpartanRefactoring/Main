package il.org.spartan.spartanizer.research.metatester;

import fluent.ly.as;
import fluent.ly.note;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.utils.format;
import il.org.spartan.utils.Int;
import il.org.spartan.utils.Pair;
import il.org.spartan.utils.UnderConstruction;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEdit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static il.org.spartan.spartanizer.research.metatester.AssertSilencer.shutDown;
import static il.org.spartan.spartanizer.research.metatester.FileUtils.makePath;
import static il.org.spartan.spartanizer.research.metatester.FileUtils.packageName;

/**
 * TODO orenafek: document class
 *
 * @author orenafek
 * @since 2017-04-12
 */
@UnderConstruction("OrenAfek -- 12/04/2017")
public class ASTTestClassGenerator implements TestClassGenerator {
    public final String packageName;
    private final Class<?> testClass;
    private final String sourcePath;
    private final Int testNoGenerator = Int.valueOf(0);
    Set<Test> sourceLines;
    private ASTNode root;
    public ASTTestClassGenerator(final Class<?> testClass) {
        this.testClass = testClass;
        sourcePath = makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\", testClass));
        packageName = packageName("\\.", testClass);
        sourceLines = new HashSet<>();
    }

    private static List<String> prefixes(final Test t) {
        final List<String> $ = an.empty.list();
        final StringBuilder prefix = new StringBuilder();
        step.statements((MethodDeclaration) t.source).stream().map(λ -> {
            prefix.append(λ + "\n");
            return prefix + "";
        }).forEach($::add);
        return $;
    }

    public static List<String> suffixes(final Test t) {
        List<String> allStatements = step.statements((MethodDeclaration) t.source).stream()
                .map(Object::toString)
                .map(TestTransformator::transformIfPossible)
                .collect(Collectors.toList());


        Int i = new Int(-1);
        return allStatements.stream()
                .map(s -> new Pair<>(i.next(), copyOf(allStatements)))
                .map(p -> {
                    p.second.set(p.first, shutDown(p.second.get(p.first)));
                    return p.second;
                })
                .map(l -> l.stream().reduce((s1, s2) -> s1 + s2).orElse(""))
                .collect(Collectors.toList());
    }

    private static <T> List<T> copyOf(List<T> l) {
        List<T> copy = new ArrayList<>(l);
        Collections.copy(copy, l);
        return copy;
    }

    private static <T> String removeBraces(final List<T> l) {
        final StringBuilder $ = new StringBuilder();
        l.stream().forEach(e -> $.append(e + ", "));
        return $.substring(0, $.length() - 2);
    }

    private static ASTNode makeAST(final File originalSourceFile) {
        return wizard.ast(readAll(originalSourceFile));
    }

    private static String readAll(final File f) {
        final StringBuilder $ = new StringBuilder();
        try (final BufferedReader linesStream = new BufferedReader(new FileReader(f))) {
            String line = linesStream.readLine();
            for (; line != null; ) {
                $.append(line).append("\n");
                line = linesStream.readLine();
            }
            return $ + "";
        } catch (final IOException x) {
            return note.bug(x);
        }
    }

    private String addImport(ASTNode source, String importDeclaration) {
        CompilationUnit cu = az.compilationUnit(source.getRoot());

        ListRewrite lr = ASTRewrite.create(cu.getAST()).getListRewrite(cu, CompilationUnit.IMPORTS_PROPERTY);
        final ImportDeclaration id = source.getAST().newImportDeclaration();
        id.setName(source.getAST().newName(importDeclaration));
        lr.insertLast(id, null);
        final TextEdit te;
        /*try {
            te = lr.getASTRewrite()
            te.apply(new Document());
        } catch (BadLocationException | JavaModelException e) {
            note.bug(e);
        }*/

        // step.importDeclarations(az.typeDeclaration(source))
        return "";
    }

    private void modifyClass() {
        root.accept(new ASTVisitor() {
            @Override
            public boolean visit(final TypeDeclaration node) {
                final SimpleName $ = az.typeDeclaration(node).getName();
                $.setIdentifier($ + "_Meta");
                final Annotation a = extract.annotations(node).stream()//
                        .filter(λ -> haz.name(λ, "RunWith"))//
                        .findAny().orElse(null);
                if (a != null)
                    node.modifiers().remove(a);

                return true;
            }
        });
    }

    void addToMap(final Test t) {
        sourceLines.add(t);
    }

    private void removeOriginalTestsFromTree() {
        sourceLines.stream() //
                .map(x -> x.source) //
                .forEach(λ -> az.abstractTypeDeclaration(λ.getParent()).bodyDeclarations().remove(λ));
    }

    @Override
    public Class<?> generate(final String testClassName, final File originalSourceFile) {
        root = makeAST(originalSourceFile);
        modifyClass();
        removeUnnecessaryImports();
        final String $ = testClassSkeleton(allTestMethods().stream().map(λ -> tests(λ, suffixes(λ))).collect(Collectors.toList()));
        return loadClass(testClassName, format.code($), testClass, sourcePath);
    }

    private void removeUnnecessaryImports() {
        final List<Class<?>> importsToRemove = as.list(MetaTester.class);
        root.accept(new ASTVisitor() {
            @Override
            public boolean visit(final ImportDeclaration node) {
                if (importsToRemove.stream().map(x -> x.getPackage().getName()).anyMatch(x -> node.getName().toString().contains(x))
                        || node.getName().toString().equals("org.junit.runner")) {
                    node.delete();
                    return true;
                }
                return false;
            }
        });
    }

    private String replaceLast(String str, String pattern, String replacer) {
        int start = str.lastIndexOf(pattern);
        return new StringBuilder(str).replace(start, pattern.length() + 1, replacer).toString();
    }

    private String testClassSkeleton(final List<List<String>> tests) {
        removeOriginalTestsFromTree();
        final StringBuilder $ = new StringBuilder();
        $.append(root + "");
        $.replace($.lastIndexOf("}"), $.length(), "");
        tests.stream().map(l -> l.stream().reduce((s1, s2) -> s1 + s2).orElse("")).forEach($::append);
        $.append("\n}\n");
        return $.toString().replaceFirst("@SuppressWarnings\\([\"a-zA-Z0-9-{}]*\\)[\n\t\r ]*public class", "public class").replace("public class",
                "@SuppressWarnings(\"all\") public class");
    }

    @SuppressWarnings("boxing")
    private List<String> tests(final Test t, final List<String> prefixes) {
        final String annotaionString = "@Test" + (t.values != null ? "(" + removeBraces(t.values) + ")" : "");
        return prefixes.stream().map(λ -> String.format("%s public void test%d(){\n%s}", annotaionString, testNoGenerator.next(), λ))
                .collect(Collectors.toList());
    }

    private List<Test> allTestMethods() {
        final List<Test> $ = an.empty.list();
        root.accept(new ASTVisitor() {
            @Override
            public boolean visit(final MethodDeclaration node) {
                final Annotation a = extract.annotations(node).stream().filter(λ -> haz.name(λ, "Test")).findAny().orElse(null);
                if (a != null) {
                    final List<MemberValuePair> l = a instanceof NormalAnnotation ? step.values((NormalAnnotation) a) : null;
                    final Test t = new Test(node, l);
                    addToMap(t);
                    $.add(t);
                }
                return a != null;
            }
        });
        return $;
    }

    enum SourceLineType {
        TEST, OTHER
    }

    class Test {
        public ASTNode source;
        public List<MemberValuePair> values;

        public Test(final ASTNode source, final List<MemberValuePair> values) {
            this.source = source;
            this.values = values;
        }
    }
}
