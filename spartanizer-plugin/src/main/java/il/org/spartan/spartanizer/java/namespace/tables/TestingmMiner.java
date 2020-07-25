package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.tables.Table;
import il.org.spartan.utils.Int;

/** Filter projects which does not contain any Junit test (Dor Msc.)
 * @author Dor Ma'ayan
 * @since 2019-03-07 */
public class TestingmMiner extends TestTables {
  @SuppressWarnings({ "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final Set<String> testMethods = new HashSet<>();
    // Maps from method names to counters
    final HashMap<String, Integer> assertionMapCounter = new HashMap<>();
    final HashMap<String, Integer> ifElseMapCounter = new HashMap<>();
    final HashMap<String, Integer> loopMapCounter = new HashMap<>();
    final HashMap<String, Integer> tryCatchMapCounter = new HashMap<>();
    final HashMap<String, Integer> throwsExceptionMapCounter = new HashMap<>();
    final HashMap<String, Integer> statementMapCounter = new HashMap<>();
    final HashMap<String, Integer> totalTokensMapCounter = new HashMap<>();
    final HashMap<String, Integer> hamcrestMapCounter = new HashMap<>();
    final HashMap<String, Integer> mockitoMapCounter = new HashMap<>();
    final HashMap<String, Integer> badApiMapCounter = new HashMap<>();
    final HashMap<String, Integer> expressionCounter = new HashMap<>();
    final HashMap<String, Integer> depthCounter = new HashMap<>();
    final HashMap<String, Integer> vocabularyCounter = new HashMap<>();
    final HashMap<String, Integer> subTreeCouner = new HashMap<>();
    final HashMap<String, Integer> bodySizeCounter = new HashMap<>();
    final HashMap<String, Integer> dexterityCounter = new HashMap<>();
    final HashMap<String, Integer> LOCCounter = new HashMap<>();
    final HashMap<String, Integer> nonWhiteCounter = new HashMap<>();

    // TODO: #No Tokens, #Assertions with comments, #sum of tokens in assertions, #
    // final HashMap<String, Integer> printMapCounter = new HashMap<>();
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      void reset() {
        testMethods.clear();
        assertionMapCounter.clear();
        ifElseMapCounter.clear();
        loopMapCounter.clear();
        tryCatchMapCounter.clear();
        throwsExceptionMapCounter.clear();
        statementMapCounter.clear();
        totalTokensMapCounter.clear();
        hamcrestMapCounter.clear();
        mockitoMapCounter.clear();
        badApiMapCounter.clear();
        expressionCounter.clear();
        depthCounter.clear();
        vocabularyCounter.clear();
        subTreeCouner.clear();
        bodySizeCounter.clear();
        dexterityCounter.clear();
        LOCCounter.clear();
        nonWhiteCounter.clear();
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        for (String method : testMethods)
			table.col("Project", path).col("MethodName", method).col("#Assrtions", assertionMapCounter.get(method))
					.col("#Conditions", ifElseMapCounter.get(method)).col("#TryCatch", tryCatchMapCounter.get(method))
					.col("#Loop", loopMapCounter.get(method)).col("#Throws", throwsExceptionMapCounter.get(method))
					.col("#Statements", statementMapCounter.get(method))
					.col("#Tokens", totalTokensMapCounter.get(method)).col("#Hamcrest", hamcrestMapCounter.get(method))
					.col("#Mockito", mockitoMapCounter.get(method)).col("#BadApi", badApiMapCounter.get(method))
					.col("#LOC", LOCCounter.get(method)).col("#Expressions", expressionCounter.get(method))
					.col("#Depth", depthCounter.get(method)).col("#Vocabulary", vocabularyCounter.get(method))
					.col("#Understandability", subTreeCouner.get(method)).col("#BodySize", bodySizeCounter.get(method))
					.col("#Dexterity", dexterityCounter.get(method))
					.col("#NonWhiteCharacters", nonWhiteCounter.get(method)).nl();
          
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final TypeDeclaration x) {
            if (isJunit4(¢) || isJunit5(¢))
				x.accept(new ASTVisitor(true) {
					@SuppressWarnings("boxing")
					@Override
					public boolean visit(final MethodDeclaration m) {
						List<String> annotations = extract.annotations(m).stream()
								.map(a -> a.getTypeName().getFullyQualifiedName()).collect(Collectors.toList());
						if (annotations.contains("Test"))
							testMethods.add(extract.name(x) + "." + extract.name(m));
						final Int assertionCounter = new Int();
						final Int ifElseCounter = new Int();
						final Int tryCatchCounter = new Int();
						final Int loopCounter = new Int();
						final Int hamcrestCounter = new Int();
						final Int mockitoCounter = new Int();
						final Int badApiCounter = new Int();
						final int throwsExceptionCounter = m.thrownExceptionTypes().size();
						final int statementCounter = extract.statements(m.getBody()).size();
						int totalTokenCounter = 0;
						if (m.getBody() != null)
							totalTokenCounter = Metrics.tokens(m.getBody().toString());
						m.accept(new ASTVisitor() {
							@Override
							public boolean visit(final ExpressionStatement a) {
								if (iz.junitAssert(a) || iz.hamcrestAssert(a) || iz.mockitoVerify(a)
										|| iz.assertStatement(a))
									assertionCounter.step();
								if (iz.hamcrestAssert(a) || a.getExpression().toString().contains("assertThat("))
									hamcrestCounter.step();
								if (iz.mockitoVerify(a))
									mockitoCounter.step();
								if (iz.trueFalseAssertion(a)) {
									List<Expression> el = extract
											.methodInvocationArguments(az.methodInvocation(a.getExpression()));
									for (Expression e : el) {
										if ((iz.infixExpression(e)
												&& (az.infixExpression(e).getOperator().toString().equals("==")
														|| az.infixExpression(e).getOperator().toString().equals("!=")))
												|| a.getExpression().toString().contains("System.out.print"))
											badApiCounter.step();
										if (iz.prefixExpression(e)
												&& az.prefixExpression(e).getOperator().toString().equals("!"))
											badApiCounter.step();
										e.accept(new ASTVisitor(true) {
											@Override
											public boolean visit(final MethodInvocation t) {
												if (t.getName().toString().equals("equals"))
													badApiCounter.step();
												return true;
											}
										});
									}
								}
								return true;
							}

							@Override
							public boolean visit(final IfStatement a) {
								ifElseCounter.step();
								return true;
							}

							@Override
							public boolean visit(final TryStatement a) {
								tryCatchCounter.step();
								return true;
							}

							@Override
							public boolean visit(final ForStatement a) {
								loopCounter.step();
								return true;
							}

							@Override
							public boolean visit(final WhileStatement a) {
								loopCounter.step();
								return true;
							}

							@Override
							public boolean visit(final EnhancedForStatement a) {
								loopCounter.step();
								return true;
							}
						});
						assertionMapCounter.put(extract.name(x) + "." + extract.name(m), assertionCounter.get());
						ifElseMapCounter.put(extract.name(x) + "." + extract.name(m), ifElseCounter.get());
						tryCatchMapCounter.put(extract.name(x) + "." + extract.name(m), tryCatchCounter.get());
						loopMapCounter.put(extract.name(x) + "." + extract.name(m), loopCounter.get());
						throwsExceptionMapCounter.put(extract.name(x) + "." + extract.name(m), throwsExceptionCounter);
						hamcrestMapCounter.put(extract.name(x) + "." + extract.name(m), hamcrestCounter.get());
						mockitoMapCounter.put(extract.name(x) + "." + extract.name(m), mockitoCounter.get());
						badApiMapCounter.put(extract.name(x) + "." + extract.name(m), badApiCounter.get());
						statementMapCounter.put(extract.name(x) + "." + extract.name(m), statementCounter);
						totalTokensMapCounter.put(extract.name(x) + "." + extract.name(m), totalTokenCounter);
						expressionCounter.put(extract.name(x) + "." + extract.name(m), Metrics.countExpressions(m));
						depthCounter.put(extract.name(x) + "." + extract.name(m), Metrics.depth(m));
						vocabularyCounter.put(extract.name(x) + "." + extract.name(m), Metrics.vocabulary(m));
						subTreeCouner.put(extract.name(x) + "." + extract.name(m), Metrics.subtreeUnderstandability(m));
						bodySizeCounter.put(extract.name(x) + "." + extract.name(m), Metrics.bodySize(m));
						dexterityCounter.put(extract.name(x) + "." + extract.name(m), Metrics.dexterity(m));
						LOCCounter.put(extract.name(x) + "." + extract.name(m), countOf.lines(m));
						nonWhiteCounter.put(extract.name(x) + "." + extract.name(m), countOf.nonWhiteCharacters(m));
						return true;
					}
				});
            return true;
          }
        });
        return super.visit(¢);
      }
    });
    table.close();
    System.err.println(table.description());
  }
}