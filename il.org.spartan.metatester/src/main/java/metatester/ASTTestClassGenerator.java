package metatester;

import il.org.spartan.spartanizer.ast.navigate.compute;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.java.haz;
import metatester.aux_layer.ASTUtils;
import metatester.aux_layer.FluentList;
import metatester.aux_layer.Pair;
import metatester.aux_layer.mutables.Int;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.Document;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static il.org.spartan.spartanizer.ast.factory.makeAST.COMPILATION_UNIT;
import static metatester.AssertJTransformator.*;
import static metatester.AssertSilencer.shutDown;
import static metatester.aux_layer.ASTUtils.addStaticImports;
import static metatester.aux_layer.ASTUtils.makeASTNode;
import static metatester.aux_layer.CollectionsUtils.copyOf;
import static metatester.aux_layer.FileUtils.*;
import static metatester.aux_layer.MetaTesterStringUtils.removeBraces;

/**
 * MetaTest class generator based on JDT's AST library and Spartanizer Project.
 *
 * @author Oren Afek
 * @since 12.04.17
 */
@SuppressWarnings("all")
public class ASTTestClassGenerator implements TestClassGenerator {
	public final String packageName;
	private final Class<?> testClass;
	private final String sourcePath;
	private final String generatedPath;
	private final Int testNoGenerator = Int.valueOf(0);
	Set<Test> sourceLines;
	private ASTNode root;
	private Document document;

	public ASTTestClassGenerator(final Class<?> testClass) {
		this.testClass = testClass;
		sourcePath = testSourcePath(testClass);
		generatedPath = generatedSourcePath(testClass);
		packageName = packageName("\\.", testClass);
		sourceLines = new HashSet<>();
	}

	/**
	 * Creates infixes of tests. Each test with 2n lines is duplicated into: - n
	 * tests where each line is asserted only once. - n tests (maximum) where each
	 * lines is null chcked only once (if the line had a dereference expression in
	 * it).
	 *
	 * @param ¢
	 *            a test object. (that contains a code of aa method annotated with a
	 *            JUnit 4 @Test)
	 * @return the new tests as strings.
	 */
	private static List<String> infixes(final Test ¢) {
		final List<String> allStatements = step.statements((MethodDeclaration) ¢.source).stream().map(Object::toString)
				.map(TestTransformator::transformIfPossible).flatMap(s -> statementsWithNullChecks(s).stream())
				.collect(Collectors.toList());
		final Int i = new Int(0);
		return allStatements.stream().map(s -> new Pair<>(i.next(), copyOf(allStatements)))
				.map(p -> shutDown(p.second, p.first)).map(l -> l.stream().map(s -> s.trim())
						.filter(s -> !s.matches("[\n\r\t ;]*")).reduce((s1, s2) -> s1 + "\n" + s2).orElse(""))
				.collect(Collectors.toList());
	}

	/**
	 * Adds null checks for dereferencing objects.
	 *
	 * @param s
	 *            line of code
	 * @return null checked and un-null checked version of s
	 */
	private static List<String> statementsWithNullChecks(String s) {
		return compute.usedIdentifiers(makeASTNode(s)).map(i -> String.format(ASSERT_NOT_NULL_TEMPLATE, i))
				.collect(FluentList.toFluentList()).add(s).list();
	}

	/**
	 * Generates a MetaClass based on a test file.
	 *
	 * @param testClassName
	 *            the meta test name (including the _Meta suffix)
	 * @param originalSourceFile
	 *            the test file
	 * @return a compiled and loaded class of the new meta test.
	 */
	@Override
	public Class<?> generate(final String testClassName, final File originalSourceFile) {
		root = makeAST(originalSourceFile);
		removeUnnecessaryImports();
		final String $ = generateNewClassString(
				allTestMethods().stream().map(λ -> tests(λ, infixes(λ))).collect(Collectors.toList()));
		return loadClass(testClassName, $, testClass, generatedPath);
	}

	/**
	 * Creates a compilation unit out of a java file
	 *
	 * @param originalSourceFile
	 *            java file
	 * @return root of the AST representation of the file.
	 */
	private ASTNode makeAST(final File originalSourceFile) {
		document = new Document(readAll(originalSourceFile));
		return COMPILATION_UNIT.from(document.get());
	}

	/**
	 * Removes unnecessary imports from the meta test AST.
	 */
	private void removeUnnecessaryImports() {
		final List<Class<?>> importsToRemove = Collections.singletonList(MetaTester.class);
		root.accept(new ASTVisitor() {
			@Override
			public boolean visit(final ImportDeclaration ¢) {
				if (importsToRemove.stream().map(x -> x.getPackage().getName()).anyMatch(
						x -> ¢.getName().toString().contains(x)) || ¢.getName().toString().equals("org.junit.runner")) {
					¢.delete();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Generates a list of @link{{@link Test Test} objects containing the original
	 * tests.}
	 *
	 * @return list of tests.
	 */
	private List<Test> allTestMethods() {
		final List<Test> $ = new ArrayList<>();
		root.accept(new ASTVisitor() {
			@Override
			public boolean visit(final MethodDeclaration ¢) {
				final Annotation a = extract.annotations(¢).stream().filter(λ -> haz.name(λ, "Test")).findAny()
						.orElse(null);
				if (a != null) {
					final List<MemberValuePair> l = a instanceof NormalAnnotation ? step.values((NormalAnnotation) a)
							: null;
					final Test t = new Test(¢, l);
					sourceLines.add(t);
					$.add(t);
				}
				return a != null;
			}
		});
		return $;
	}

	/**
	 * Modifying the root of the AST to adapt it to a MetaTest format: - Adding the
	 * apropriate static imports of the external testing libraries (AssertJ /
	 * Hamcrest ...) - Adding "_Meta" suffix to the test's name - Removing the
	 * "@RunWith(MetaTester.class)" annotation to prevent from rerunning this code
	 * to infinity.
	 *
	 * @return the root after the modifications.
	 */
	private ASTNode modifyAST() {
		root = removeOriginalTestsFromTree();
		root = addStaticImports(root, document, ASSERTJ_STATIC_IMPORT_STATEMENT,
				ASSERTJ_STATIC_IMPORT_STATEMENT_SIMPLE_NAME);
		root = addMetaToClassName();
		root = removeMetaTesterAnnotation();
		return root;
	}

	/**
	 * Creating a String skelaton of the meta test and inserts the new lines within.
	 *
	 * @param ¢
	 *            lines of codes.
	 * @return the meta test class as string
	 */
	private String generateNewClassString(final List<List<String>> ¢) {
		root = modifyAST();
		final StringBuilder $ = new StringBuilder();
		$.append(root + "");
		$.replace($.lastIndexOf("}"), $.length(), "");
		¢.stream().map(l -> l.stream().reduce((s1, s2) -> s1 + "\n" + s2).orElse("")).forEach($::append);
		$.append("\n}\n");
		return $.toString()
				.replaceFirst("@SuppressWarnings\\([\"a-zA-Z0-9-{}]*\\)[\n\t\r ]*public class", "public class")
				.replace("public class", "@SuppressWarnings(\"all\") public class");
	}

	/**
	 * Removes the "@RunWith(MetaTester.class)" annotation
	 *
	 * @return root.
	 */
	private ASTNode removeMetaTesterAnnotation() {
		root.accept(new ASTVisitor() {
			@Override
			public boolean visit(final TypeDeclaration node) {
				final Annotation a = extract.annotations(node).stream()//
						.filter(λ -> haz.name(λ, "RunWith"))//
						.findAny().orElse(null);
				if (a != null)
					node.modifiers().remove(a);

				return true;
			}
		});
		return root;
	}

	/**
	 * Adding "_Meta" suffix to class name
	 *
	 * @return root.
	 */
	private ASTNode addMetaToClassName() {
		root.accept(new ASTVisitor() {
			@Override
			public boolean visit(final TypeDeclaration node) {
				final SimpleName $ = az.typeDeclaration(node).getName();
				$.setIdentifier($ + "_Meta");
				return true;
			}
		});

		return root;
	}

	/**
	 * Removing the original tests from root. The new tests will be inserted
	 * afterwards instead.
	 *
	 * @return root.
	 */
	private ASTNode removeOriginalTestsFromTree() {
		ASTUtils remover = ASTUtils.createRemover(root, document);
		sourceLines.stream() //
				.map(x -> x.source) //
				.forEach(remover::remove);

		return remover.commit();
	}

	/**
	 * Returns new test method name in the format: [test name]_meta[i] where i is
	 * the index of the method in the new meta test file.
	 *
	 * @param test
	 *            - the test object.
	 * @param codeLine
	 *            i
	 * @return new test name
	 */
	private String testName(Test test, int codeLine) {
		String originalTestName = test.testName;
		return String.format("%s_meta%d", originalTestName, codeLine);
	}

	/**
	 * Generates the new test methods from the infixes of t/
	 *
	 * @param t
	 *            the test
	 * @param infixes
	 *            t's infixes.
	 * @return List of test methods.
	 */
	@SuppressWarnings("boxing")
	private List<String> tests(final Test t, final List<String> infixes) {
		final String annotaionString = "@Test" + (t.values != null ? "(" + removeBraces(t.values) + ")" : "");
		return infixes.stream().map(λ -> String.format("%s public void %s(){\n%s}", annotaionString,
				testName(t, testNoGenerator.next()), λ)).collect(Collectors.toList());
	}

	/**
	 * Returns the meta test file path by its name
	 *
	 * @param ¢
	 *            the class name
	 * @return the whole path of ¢.
	 */
	public String getMetaTestFilePath(String ¢) {
		return getMetaTestFilePath(sourcePath, ¢);
	}

	/**
	 * A POJO representing a single test in the oriinal test file.
	 */
	class Test {
		public ASTNode source;
		public List<MemberValuePair> values;
		public String testName;

		public Test(final ASTNode source, final List<MemberValuePair> values) {
			this.source = source;
			this.values = values;
			this.testName = step.name(az.methodDeclaration(source)).getFullyQualifiedName();
		}

	}

}
