package il.org.spartan.spartanizer.research.metatester;

import fluent.ly.as;
import fluent.ly.note;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.utils.format;
import il.org.spartan.utils.Int;
import il.org.spartan.utils.Pair;
import il.org.spartan.utils.UnderConstruction;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
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
@SuppressWarnings("all")
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

    public static List<String> suffixes(final Test t) {
        final List<String> allStatements = step.statements((MethodDeclaration) t.source).stream().map(Object::toString)
                .map(TestTransformator::transformIfPossible).collect(Collectors.toList());
        final Int i = new Int(-1);
        return allStatements.stream().map(s -> new Pair<>(i.next(), copyOf(allStatements))).map(p -> {
            p.second.set(p.first, shutDown(p.second.get(p.first)));
            return p.second;
        }).map(l -> l.stream().reduce((s1, s2) -> s1 + s2).orElse("")).collect(Collectors.toList());
    }

    private static <T> List<T> copyOf(final List<T> l) {
        final List<T> copy = new ArrayList<>(l);
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


    public static String addStaticImports(ASTNode root, String importName, String simpleName) {
        if (!iz.compilationUnit(root)) {
            return root.toString();
        }

        ListRewrite lr = ASTRewrite.create(root.getAST()).getListRewrite(root, CompilationUnit.IMPORTS_PROPERTY);
        ImportDeclaration id = root.getAST().newImportDeclaration();
        id.setStatic(true);
        id.setName(root.getAST().newName(importName.split("\\.")));
        lr.insertLast(id, null);
        //ImportRewrite irw = ImportRewrite.create(az.compilationUnit(root), true);
        //irw.addStaticImport(importName, simpleName, false);
        try {
            IDocument doc = new Document(root.toString());
            TextEdit te = lr.getASTRewrite().rewriteAST(doc, null);
            // TextEdit te = irw.rewriteImports(new NullProgressMonitor()); //FIXME: @orenafek: Change null to something else...

            te.apply(doc);
            return doc.get();
        } catch (BadLocationException e) {
            note.bug(e);
        }
        //lr.insertLast();
        //lr.insertLast(ImportDeclaration.
        return root.toString();

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

    private String testClassSkeleton(final List<List<String>> tests) {
        removeOriginalTestsFromTree();
        final StringBuilder $ = new StringBuilder();
        addStaticImports(root, "org.assertj.core.api.Assertions.assertThat", "Assertions");
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
