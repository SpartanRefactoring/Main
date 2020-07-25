package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.StringLiteral;
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
public class TestClassMiner extends TestTables {
  @SuppressWarnings({ "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final Set<String> classNames = new HashSet<>();
    // Maps from method names to counters
    final HashMap<String, Integer> totalMethodsMapCounter = new HashMap<>();
    final HashMap<String, Integer> vocabularyCounter = new HashMap<>();
    final HashMap<String, Integer> wordCounter = new HashMap<>();
    final HashMap<String, Integer> specialCounter = new HashMap<>();
    final HashMap<String, Integer> nonWhiteCounter = new HashMap<>();
    final HashMap<String, Integer> astSize = new HashMap<>();
    final HashMap<String, Integer> maximaldepthCounter = new HashMap<>();
    final HashMap<String, Integer> conditionsMapCounter = new HashMap<>();
    final HashMap<String, Integer> elseMapCounter = new HashMap<>();
    final HashMap<String, Integer> dexterityCounter = new HashMap<>();
    final HashMap<String, Integer> junitassertionMapCounter = new HashMap<>();
    final HashMap<String, Integer> hamcrestMapCounter = new HashMap<>();
    final HashMap<String, Integer> mockitoMapCounter = new HashMap<>();
    final HashMap<String, Integer> expressionCounter = new HashMap<>();
    final HashMap<String, Integer> loopMapCounter = new HashMap<>();
    final HashMap<String, Integer> badApiMapCounter = new HashMap<>();
    final HashMap<String, Integer> tryMapCounter = new HashMap<>();
    final HashMap<String, Integer> catchMapCounter = new HashMap<>();
    final HashMap<String, Integer> methodInvocationMapCounter = new HashMap<>();
    final HashMap<String, Double> avgDephsMapCounter = new HashMap<>();
    final HashMap<String, Double> avgDephsMapCounter2 = new HashMap<>();
    final HashMap<String, Double> deg2MapCounter = new HashMap<>();
    final HashMap<String, Double> degPermMapCounter = new HashMap<>();
    final HashMap<String, Integer> breakMapCounter = new HashMap<>();
    final HashMap<String, Integer> continueMapCounter = new HashMap<>();
    final HashMap<String, Integer> stringLiteralsMapCounter = new HashMap<>();
    final HashMap<String, Integer> commentsMapCounter = new HashMap<>();
    final HashMap<String, Integer> fieldAccessMapCounter = new HashMap<>();
    final HashMap<String, Integer> primitivesMapCounter = new HashMap<>();
    final HashMap<String, Integer> numericLiteralsMapCounter = new HashMap<>();
    final HashMap<String, Integer> conjunctionMapCounter = new HashMap<>();
    final HashMap<String, Integer> disjunctionMapCounter = new HashMap<>();
    final HashMap<String, Integer> ternaryMapCounter = new HashMap<>();





    
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      void reset() {
        

        classNames.clear();
        totalMethodsMapCounter.clear();
        vocabularyCounter.clear();
        nonWhiteCounter.clear();
        specialCounter.clear();
        methodInvocationMapCounter.clear();
        astSize.clear();
        maximaldepthCounter.clear();
        avgDephsMapCounter.clear();
        avgDephsMapCounter2.clear();
        deg2MapCounter.clear();
        degPermMapCounter.clear();
        dexterityCounter.clear();
        expressionCounter.clear();
        tryMapCounter.clear();
        catchMapCounter.clear();
        loopMapCounter.clear();
        conditionsMapCounter.clear();
        elseMapCounter.clear();
        badApiMapCounter.clear();
        junitassertionMapCounter.clear();
        hamcrestMapCounter.clear();
        mockitoMapCounter.clear();
        breakMapCounter.clear();
        continueMapCounter.clear();
        wordCounter.clear();
        stringLiteralsMapCounter.clear();
        commentsMapCounter.clear();
        fieldAccessMapCounter.clear();
        primitivesMapCounter.clear();
        numericLiteralsMapCounter.clear();
        conjunctionMapCounter.clear();
        disjunctionMapCounter.clear();
        ternaryMapCounter.clear();
      }
      
      
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      
      
      public void summarize(final String path) {
        initializeWriter();
        for (String className : classNames) {
          table.col("Project", path).col("TestClassName", className)
          .col("No. Methods", totalMethodsMapCounter.get(className))
          .col("Vocabulary", vocabularyCounter.get(className))
          .col("Word", wordCounter.get(className))
          .col("Strings", stringLiteralsMapCounter.get(className))
          .col("Numeric Literals", numericLiteralsMapCounter.get(className))
          .col("Comments", commentsMapCounter.get(className))
          .col("Special", specialCounter.get(className))
          .col("Non Whithe Characters", nonWhiteCounter.get(className))
          .col("No. Method Invoctions", methodInvocationMapCounter.get(className))
          .col("No. Field Access", fieldAccessMapCounter.get(className))
          .col("No. Primitives", primitivesMapCounter.get(className))
          .col("AST size", astSize.get(className))
          .col("Max Depth", maximaldepthCounter.get(className))
          .col("Avg Depth", avgDephsMapCounter.get(className))
          .col("Avg Depth Squared", avgDephsMapCounter2.get(className))
          .col("Deg2", deg2MapCounter.get(className))
          .col("DegPerm", degPermMapCounter.get(className))
          .col("Dexterity", dexterityCounter.get(className))
          .col("No. Expressions", expressionCounter.get(className))
          .col("No. Try", tryMapCounter.get(className))
          .col("No. Catch", catchMapCounter.get(className))
          .col("No. Loop", loopMapCounter.get(className))
          .col("No. Break", breakMapCounter.get(className))
          .col("No. Continue", continueMapCounter.get(className))
          .col("No. Conditions", conditionsMapCounter.get(className))
          .col("No. Else", elseMapCounter.get(className))
          .col("No. &&", conjunctionMapCounter.get(className))
          .col("No. ||", disjunctionMapCounter.get(className))
          .col("No. Ternary", ternaryMapCounter.get(className))
          .col("Bad API", badApiMapCounter.get(className))
          .col("Junit", junitassertionMapCounter.get(className))
          .col("Hamcrest", hamcrestMapCounter.get(className))
          .col("Mockito", mockitoMapCounter.get(className)).nl();
        }
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @SuppressWarnings("boxing") @Override public boolean visit(final TypeDeclaration x) {
              final Int assertionCounter = new Int();
              final Int conditionsCounter = new Int();
              final Int elseCounter = new Int();
              final Int tryCounter = new Int();
              final Int catchCounter = new Int();
              final Int methodInvocationCounter = new Int();
              final Int loopCounter = new Int();
              final Int hamcrestCounter = new Int();
              final Int mockitoCounter = new Int();
              final Int badApiCounter = new Int();
              final Int totalMethodCounter = new Int();
              final Int javaAssertionsCounter = new Int();
              final Int breakCounter = new Int();
              final Int continueCounter = new Int();
              final Int stringCounter = new Int();
              final Int commentsCounter = new Int();
              final Int fieldAccessCounter = new Int();
              final Int primitivesCounter = new Int();
              final Int numericLiteralsCounter = new Int();
              final Int conjunctionCounter = new Int();
              final Int disjunctionCounter = new Int();
              final Int ternaryCounter = new Int();

              
              
              x.accept(new ASTVisitor(true) {
                public boolean visit(final MethodDeclaration m) {
                  totalMethodCounter.step();
                  return true;
                }
              });
              classNames.add(extract.name(x));

              x.accept(new ASTVisitor() {
                @Override public boolean visit(final ExpressionStatement a) {
                  if (iz.assertStatement(a)) {
                    javaAssertionsCounter.step();
                  }
                  if (iz.junitAssert(a) || iz.hamcrestAssert(a) || iz.mockitoVerify(a) || iz.assertStatement(a)) {
                    assertionCounter.step();
                  }
                  if (iz.hamcrestAssert(a) || a.getExpression().toString().contains("assertThat("))
                    hamcrestCounter.step();
                  if (iz.mockitoVerify(a))
                    mockitoCounter.step();
                  if (iz.trueFalseAssertion(a)) {
                    List<Expression> el = extract.methodInvocationArguments(az.methodInvocation(a.getExpression()));
                    for (Expression e : el) {
                      if ((iz.infixExpression(e) && (az.infixExpression(e).getOperator().toString().equals("==")
                          || az.infixExpression(e).getOperator().toString().equals("!=")))
                          || a.getExpression().toString().contains("System.out.print")) {
                        badApiCounter.step();
                      }
                      if (iz.prefixExpression(e) && az.prefixExpression(e).getOperator().toString().equals("!")) {
                        badApiCounter.step();
                      }
                      e.accept(new ASTVisitor(true) {
                        @Override public boolean visit(final MethodInvocation t) {
                          if (t.getName().toString().equals("equals"))
                            badApiCounter.step();
                          return true;
                        }
                      });
                    }
                  }
                  return true;
                }
                
                @Override public boolean visit(final IfStatement a) {
                  conditionsCounter.step();
                  if (a.getElseStatement() != null)
                    elseCounter.step();
                  return true;
                }
                
                @Override public boolean visit(final ConditionalExpression a) {
                  ternaryCounter.step();
                  return true;
                }
                
                @Override public boolean visit(final FieldAccess a) {
                    fieldAccessCounter.step();
                  return true;
                }
                
                
                @Override public boolean visit(final InfixExpression e) {
                  for (Operator o : extract.allOperators(e)) {
                    if (o == Operator.CONDITIONAL_AND) {
                      conjunctionCounter.step();
                     }
                    if (o == Operator.CONDITIONAL_OR) {
                      disjunctionCounter.step();
                     }
                 }
                  return true;
              }
                
                @Override public boolean visit(final NumberLiteral a) {
                  numericLiteralsCounter.step();
                return true;
              }
                
                @Override public boolean visit(final BreakStatement a) {
                  breakCounter.step();
                  return true;
                }
                
                @Override public boolean visit(final StringLiteral s) {
                  stringCounter.add(countOf.nonWhiteCharacters(s));
                  return true;
                }
                
                @Override public boolean visit(final PrimitiveType s) {
                  primitivesCounter.step();
                  return true;
                }
                
                @Override public boolean visit(final LineComment s) {
                  commentsCounter.add(countOf.nonWhiteCharacters(s));
                  return true;
                }
                
                @Override public boolean visit(final BlockComment s) {
                  commentsCounter.add(countOf.nonWhiteCharacters(s));
                  return true;
                }
                
                @Override public boolean visit(final Javadoc s) {
                  commentsCounter.add(countOf.nonWhiteCharacters(s));
                  return true;
                }
                
                @Override public boolean visit(final ContinueStatement a) {
                  continueCounter.step();
                  return true;
                }

                @Override public boolean visit(final TryStatement a) {
                  tryCounter.step();
                  for (int i=0; i < a.catchClauses().size(); i++)
                    catchCounter.step();
                  return true;
                }
                
                @Override public boolean visit(final ForStatement a) {
                  loopCounter.step();
                  return true;
                }
                
                @Override public boolean visit(final WhileStatement a) {
                  loopCounter.step();
                  return true;
                }
                
                @Override public boolean visit(final EnhancedForStatement a) {
                  loopCounter.step();
                  return true;
                }
                
                @Override public boolean visit(final MethodInvocation a) {
                  methodInvocationCounter.step();
                  return true;
                }
                
              });
  
                       
              junitassertionMapCounter.put(extract.name(x), assertionCounter.get() >= 1 ? 1 : 0);
              conditionsMapCounter.put(extract.name(x), conditionsCounter.get());
              elseMapCounter.put(extract.name(x), elseCounter.get());
              tryMapCounter.put(extract.name(x), tryCounter.get());
              catchMapCounter.put(extract.name(x), catchCounter.get());
              loopMapCounter.put(extract.name(x), loopCounter.get());
              hamcrestMapCounter.put(extract.name(x), hamcrestCounter.get() >= 1 ? 1 : 0);
              mockitoMapCounter.put(extract.name(x), mockitoCounter.get() >= 1 ? 1 : 0);
              badApiMapCounter.put(extract.name(x), badApiCounter.get());
              breakMapCounter.put(extract.name(x), breakCounter.get());
              continueMapCounter.put(extract.name(x), continueCounter.get());
              methodInvocationMapCounter.put(extract.name(x), methodInvocationCounter.get());
              vocabularyCounter.put(extract.name(x), Metrics.vocabulary(x));
              wordCounter.put(extract.name(x), Metrics.words(x));

              astSize.put(extract.name(x), Metrics.bodySize(x));
              dexterityCounter.put(extract.name(x), Metrics.dexterity(x));
              nonWhiteCounter.put(extract.name(x), countOf.nonWhiteCharacters(x));
              
              specialCounter.put(extract.name(x), countOf.specialCharacters(x));

              totalMethodsMapCounter.put(extract.name(x), totalMethodCounter.get());
              expressionCounter.put(extract.name(x), Metrics.countExpressions(x));
              maximaldepthCounter.put(extract.name(x), Metrics.depth(x));
              
              avgDephsMapCounter.put(extract.name(x), Metrics.avgDepth(x));
              avgDephsMapCounter2.put(extract.name(x), Metrics.avgDepth2(x));
              
              deg2MapCounter.put(extract.name(x), Metrics.deg2(x));
              degPermMapCounter.put(extract.name(x), Metrics.degPerm(x));


              stringLiteralsMapCounter.put(extract.name(x), stringCounter.get());
              commentsMapCounter.put(extract.name(x), commentsCounter.get());
              fieldAccessMapCounter.put(extract.name(x), fieldAccessCounter.get());
              primitivesMapCounter.put(extract.name(x), primitivesCounter.get());
              numericLiteralsMapCounter.put(extract.name(x), numericLiteralsCounter.get());
              conjunctionMapCounter.put(extract.name(x), conjunctionCounter.get());
              disjunctionMapCounter.put(extract.name(x), disjunctionCounter.get());
              ternaryMapCounter.put(extract.name(x), ternaryCounter.get());

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