package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class CommandLine$Applicator {
  static List<Class<? extends ASTNode>> selectedNodeTypes = 
      as.list(MethodDeclaration.class, 
              InfixExpression.class, //
              VariableDeclarationFragment.class, //
              EnhancedForStatement.class, //
              Modifier.class, //
              VariableDeclarationExpression.class, //
              ThrowStatement.class, //
              CastExpression.class, //
              ClassInstanceCreation.class, //
              SuperConstructorInvocation.class, //
              SingleVariableDeclaration.class, //
              ForStatement.class, //
              WhileStatement.class, //
              Assignment.class, //
              Block.class, //
              PostfixExpression.class, //
              InfixExpression.class, //
              InstanceofExpression.class, //
              MethodDeclaration.class, //
              MethodInvocation.class, //
              IfStatement.class, //
              PrefixExpression.class, //
              ConditionalExpression.class, //
              TypeDeclaration.class, //
              EnumDeclaration.class, //
              FieldDeclaration.class, //
              CastExpression.class, //
              EnumConstantDeclaration.class, //
              NormalAnnotation.class, //
              Initializer.class, //
              VariableDeclarationFragment.class //
            );
  
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected PrintWriter afters;
  protected PrintWriter befores;
  File currentFile;
  int done;
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();
  final ChainStringToIntegerMap coverage = new ChainStringToIntegerMap();

  void go(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        assert ¢ != null;
        System.out.println("!selectedNodeTypes.contains(¢.getClass()): " + !selectedNodeTypes.contains(¢.getClass()));
//      System.out.println("!filter(¢): " + !filter(¢));
        System.out.println("¢.getClass(): " + ¢.getClass());
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // !selectedNodeTypes.contains(¢.getClass()) || 
      }
    });
  }

  boolean go(final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    final String output = fixedPoint(input);
//    final ASTNode outputASTNode = makeAST.CLASS_BODY_DECLARATIONS.from(output);
    final ASTNode outputASTNode = makeAST.COMPILATION_UNIT.from(output);
    Reports.printFile(input + "", "before");
    Reports.printFile(output, "after");
    computeMetrics(input, outputASTNode);
    return false;
  }

  @SuppressWarnings({ "boxing"}) protected void computeMetrics(final ASTNode input, final ASTNode output) {
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    Reports.summaryFileName("metrics");
    Reports.name(input);
    Reports.writeMetrics(input, output, null);
    Reports.write(input, output, "Δ ", (n1, n2) -> (n1 - n2));
    Reports.write(input, output, "δ ", (n1, n2) -> system.d(n1, n2));
    Reports.writePerc(input, output, "δ ");
    // Reports.writeRatio(input, output, "", (n1,n2)->(n1/n2));
    Reports.nl("metrics");
  }

  String fixedPoint(final ASTNode ¢) {
    return fixedPoint(¢ + "");
  }

  public String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
//      final BodyDeclaration u = (BodyDeclaration) makeAST.CLASS_BODY_DECLARATIONS.from($.get());
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get());
      final ASTRewrite r = createRewrite(u);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException x) {
        monitor.logEvaluationError(this, x);
        throw new AssertionError(x);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }
  
  /**
   * createRewrite on CompilationUnit 
   * @param ¢
   * @return
   */
  
  public ASTRewrite createRewrite(final CompilationUnit ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    consolidateTips($, ¢);
    return $;
  }
  
/**
 * createRewrite on BodyDeclaration 
 * TODO Matteo -- this gonna be removed? -- matteo
 * @param u
 * @return
 */

  public ASTRewrite createRewrite(final BodyDeclaration u) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u);
    return $;
  }
  
  /**
   * consolidate tips on CompilationUnit
   * @param r
   * @param u
   */
  
  public void consolidateTips(final ASTRewrite r, final CompilationUnit u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        TrimmerLog.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (s != null) {
          ++tippersAppliedOnCurrentObject;
          // tick2(tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }
  
  /**
   * consolidateTips on BodyDeclaration 
   * TODO Matteo -- this gonna be removed? -- matteo
   * @param r
   * @param u
   */

  public void consolidateTips(final ASTRewrite r, final BodyDeclaration u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        TrimmerLog.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (s != null) {
          ++tippersAppliedOnCurrentObject;
          // tick2(tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }
  
  /**
   * 
   * @param u
   * @param __
   * @return
   */

  public boolean apply(final WrappedCompilationUnit u, @SuppressWarnings("unused") final AbstractSelection<?> __) {
      apply(u);
    return false;
  }
  
  /**
   * Apply to single compilation unit
   * @param ¢
   * @return
   */
   
  public boolean apply(final WrappedCompilationUnit ¢) {
    System.out.println("*********");
    go(¢.compilationUnit);
    return false;
  }
  
  /** @param __
   * @return
   */
  
  public boolean apply(final AbstractSelection<?> __) {
  for (WrappedCompilationUnit w: ((CommandLineSelection) __).get())
    w.compilationUnit.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // || !filter(¢) 
      }
    });
    return false;
  }
}