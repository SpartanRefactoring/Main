package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.nodes.metrics.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Filter projects which does not contain any Junit test (Dor Msc.)
 * @author Dor Ma'ayan
 * @since 2019-03-07 */
public class TestClassMiner extends TestTables {
  @SuppressWarnings({ "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final Set<String> classNames = new HashSet<>();
    // Maps from method names to counters
    final HashMap<String, Integer> totalTestMethodsMapCounter = new HashMap<>();
    final HashMap<String, Integer> vocabularyCounter = new HashMap<>();
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
    final HashMap<String, Integer> sumDephsMapCounter = new HashMap<>();
    final HashMap<String, Double> avgDephsMapCounter = new HashMap<>();
    final HashMap<String, Double> branchingMapCounter = new HashMap<>();


    
    


    
    
    //final HashMap<String, Integer> totalTokensMapCounter = new HashMap<>();
    //final HashMap<String, Integer> subTreeCouner = new HashMap<>();
    //final HashMap<String, Integer> LOCCounter = new HashMap<>();
    
    
/***
 * T & No. Test Methods & The number of tests methods in a test class \\
 * T & Vocabulary & The number of distinct words in the test class \\ 
T & No. NonWhiteCharacters & The number of non white space characters in the test class code \\
A & Size &  The number of nodes in the AST representing the tests class \\ 
A & Maximal depth & $\max_v \text{depth}(v)$ \\
M & No. Conditions & The number of condition statements in the test class code \\
A & *Dexterity & The number of distinct node types in the AST representing the tests class  \\ 
L & Junit Assertions & Is the test class uses Junit assertions, or customized assertions  \\
L & Hamcrest & Is the tests class uses fluent API Hamcrest assertions \\
L & Mockito & Is the test class uses Mockito \\
13 & BadApi  & Is there an inappropriate usage of the Junit interface? \\
M & No. Loop & The number of loop statements in the test class code \\
A & No. Expressions & The number of expression nodes in the AST representing the test class \\
M & *No. Try & The number of try-catch statements in the test class code \\
M & *No. Else  & The number of else clauses in the test class code \\
M & *No. Catch & The number of catch clauses statements in the test class code \\
A & Number of method invocations \\



 
A & *Total depth & $\sum_v \text{depth}(v)/N$  \\ 
A & *Branching & $\sum_v \binom{\text{deg}(v)}2}/N}$ \\ 

 */
    
    
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
        totalTestMethodsMapCounter.clear();
        vocabularyCounter.clear();
        nonWhiteCounter.clear();
        methodInvocationMapCounter.clear();
        astSize.clear();
        maximaldepthCounter.clear();
        sumDephsMapCounter.clear();
        avgDephsMapCounter.clear();
        branchingMapCounter.clear();
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
       
      }
      
      
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      
      
      public void summarize(final String path) {
        initializeWriter();
        for (String className : classNames) {
          table.col("Project", path).col("TestClassName", className)
          .col("No. Tests", totalTestMethodsMapCounter.get(className))
          .col("Vocabulary", vocabularyCounter.get(className))
          .col("Non Whithe Characters", nonWhiteCounter.get(className))
          .col("No. Method Invoctions", methodInvocationMapCounter.get(className))
          .col("AST size", astSize.get(className))
          .col("Max Depth", maximaldepthCounter.get(className))
          .col("Sum Depths", sumDephsMapCounter.get(className))
          .col("Avg Depth", avgDephsMapCounter.get(className))
          .col("Branching", branchingMapCounter.get(className))
          .col("Dexterity", dexterityCounter.get(className))
          .col("No. Expressions", expressionCounter.get(className))
          .col("No. Try", tryMapCounter.get(className))
          .col("No. Catch", catchMapCounter.get(className))
          .col("No. Loop", loopMapCounter.get(className))
          .col("No. Conditions", conditionsMapCounter.get(className))
          .col("No. Else", elseMapCounter.get(className))
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
            //if (isJunit4(¢) || isJunit5(¢)) {
              // Define per test class counters
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
              final Int totalTestMethodCounter = new Int();
              final Int javaAssertionsCounter = new Int();
              
              
              x.accept(new ASTVisitor(true) {
                public boolean visit(final MethodDeclaration m) {
                  List<String> annotations = extract.annotations(m).stream().map(a -> a.getTypeName().getFullyQualifiedName())
                      .collect(Collectors.toList());
                  if (annotations.contains("Test")
                      || (iz.typeDeclaration(m.getParent()) && az.typeDeclaration(m.getParent()).getSuperclassType() != null
                          && az.typeDeclaration(m.getParent()).getSuperclassType().toString().equals("TestCase"))) {
                    totalTestMethodCounter.step();
                  }
                  return true;
                }
              });
              classNames.add(extract.name(x)); // + "." + extract.name(m));

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
  
                       
              junitassertionMapCounter.put(extract.name(x), assertionCounter.get());
              conditionsMapCounter.put(extract.name(x), conditionsCounter.get());
              elseMapCounter.put(extract.name(x), elseCounter.get());
              tryMapCounter.put(extract.name(x), tryCounter.get());
              catchMapCounter.put(extract.name(x), catchCounter.get());
              loopMapCounter.put(extract.name(x), loopCounter.get());
              hamcrestMapCounter.put(extract.name(x), hamcrestCounter.get());
              mockitoMapCounter.put(extract.name(x), mockitoCounter.get());
              badApiMapCounter.put(extract.name(x), badApiCounter.get());
              methodInvocationMapCounter.put(extract.name(x), methodInvocationCounter.get());
              vocabularyCounter.put(extract.name(x), Metrics.vocabulary(x));
              astSize.put(extract.name(x), Metrics.bodySize(x));
              dexterityCounter.put(extract.name(x), Metrics.dexterity(x));
              nonWhiteCounter.put(extract.name(x), countOf.nonWhiteCharacters(x));
              totalTestMethodsMapCounter.put(extract.name(x), totalTestMethodCounter.get());
              expressionCounter.put(extract.name(x), Metrics.countExpressions(x));
              maximaldepthCounter.put(extract.name(x), Metrics.depth(x));
              sumDephsMapCounter.put(extract.name(x), Metrics.sumDepth(x));
              avgDephsMapCounter.put(extract.name(x), Metrics.avgDepth(x));
              branchingMapCounter.put(extract.name(x), Metrics.branching(x));


              return true;
            //}
            //return true;
          }
        });
        return super.visit(¢);
      }
    });
    table.close();
    System.err.println(table.description());
  }
}