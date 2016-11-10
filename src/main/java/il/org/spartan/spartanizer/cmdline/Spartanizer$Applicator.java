package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Matteo Orru'
 * @since 2016 */
public class Spartanizer$Applicator {
  Toolbox toolbox;
  int tippersAppliedOnCurrentObject;
  private int done;
  private CSVStatistics report;
  static List<Class<? extends ASTNode>> selectedNodeTypes = as.list(MethodDeclaration.class, InfixExpression.class, //
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
  
  CSVStatistics spectrumStats; 
  
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();

  /** Instantiates this class */
  public Spartanizer$Applicator() {
    this(Toolbox.defaultInstance());
  }
  /** @param defaultInstance */
  public Spartanizer$Applicator(final Toolbox toolbox) {
    this.toolbox = toolbox;
  }
  /** Apply the spartanization to a selection of CompilationUnits
   * @param u
   * @param s
   * @return */
  public boolean apply(final AbstractSelection<?> __) {
    List<WrappedCompilationUnit> list = ((CommandLineSelection) __).get();
    for (WrappedCompilationUnit w : list) {
      assert w != null;
      assert w.compilationUnit != null;
      System.out.println(w.compilationUnit);
      w.compilationUnit.accept(new ASTVisitor() {
        @Override public boolean preVisit2(final ASTNode ¢) {
          return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // ||
                                                                     // !filter(¢)
        }
      });
    }
    return false;
  }
  /** Apply the spartanization to a single CompilationUnit
   * @param u
   * @param s
   * @return
   * @author matteo */
  @SuppressWarnings("unused") public boolean apply(final WrappedCompilationUnit u, final AbstractSelection<?> __) {
    go(u.compilationUnit);
    return false;
  }
  void go(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        System.out.println("!selectedNodeTypes.contains(¢.getClass()): " + !selectedNodeTypes.contains(¢.getClass()));
        // System.out.println("!filter(¢): " + !filter(¢));
        System.out.println("¢.getClass(): " + ¢.getClass());
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // ||
                                                                   // !filter(¢)
      }
    });
  }
  
  boolean go(final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    final String output = fixedPoint(input + "");
    final ASTNode outputASTNode = makeAST.COMPILATION_UNIT.from(output); // makeAST.CLASS_BODY_DECLARATIONS.from(output);
    Reports.printFile(input + "", "before");
    Reports.printFile(output, "after");
    computeMetrics(input, outputASTNode);
    return false;
  }
  
  @SuppressWarnings({ "boxing" }) protected void computeMetrics(final ASTNode input, final ASTNode output) {
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
  
  boolean go2(final ASTNode input) {
    System.out.println(input);
    System.out.println("ASTNode input: " + az.methodDeclaration(input));
    tippersAppliedOnCurrentObject = 0;
    final int length = input.getLength();
    final int tokens = metrics.tokens(input + "");
    final int nodes = count.nodes(input);
    final int body = metrics.bodySize(input);
    final int statements = extract.statements(az.methodDeclaration(input).getBody()).size();
    final int tide = clean(input + "").length();
    final int essence = Essence.of(input + "").length();
    final String out = fixedPoint(input + "");
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = Essence.of(out + "").length();
    final int wordCount = code.wc(il.org.spartan.spartanizer.cmdline.Essence.of(out + ""));
    final ASTNode to = makeAST.CLASS_BODY_DECLARATIONS.from(out); // TODO MATTEO
                                                                  // - pay
                                                                  // attention
                                                                  // to this
                                                                  // to is a
                                                                  // CLASS_BODY_DECLARATION
                                                                  // -- matteo
    final int nodes2 = count.nodes(to);
    final int body2 = metrics.bodySize(to);
    final MethodDeclaration methodDeclaration = az.methodDeclaration(to);
    final int statements2 = methodDeclaration == null ? -1 : extract.statements(methodDeclaration.getBody()).size();
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    // befores.print(input);
    // afters.print(out);
    // report.summaryFileName();
    report//
        // .put("File", currentFile)//
        .put("Category", extract.category(input))//
        .put("Name", extract.name(input))//
        .put("# Tippers", tippersAppliedOnCurrentObject) //
        .put("Nodes1", nodes)//
        .put("Nodes2", nodes2)//
        .put("Δ Nodes", nodes - nodes2)//
        .put("δ Nodes", system.d(nodes, nodes2))//
        .put("δ Nodes %", system.p(nodes, nodes2))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", system.d(body, body2))//
        .put("% Body", system.p(body, body2))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", system.d(tokens, tokens2))//
        .put("% Tokens", system.p(tokens, tokens2))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", system.d(length, length2))//
        .put("% Length", system.p(length, length2))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", system.d(tide, tide2))//
        .put("δ Tide2", system.p(tide, tide2))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", system.d(essence, essence2))//
        .put("% Essence", system.p(essence, essence2))//
        .put("Statements1", statements)//
        .put("Statement2", statements2)//
        .put("Δ Statement", statements - statements2)//
        .put("δ Statement", system.d(statements, statements2))//
        .put("% Statement", system.p(essence, essence2))//
        .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
        .put("R(E/L)", system.ratio(length, essence)) //
        .put("R(E/T)", system.ratio(tide, essence)) //
        .put("R(B/S)", system.ratio(nodes, body)) //
    ;
    report.nl();
    return false;
  }
  /** @param input
   * @return */
  private String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
      // final BodyDeclaration u = (BodyDeclaration)
      // makeAST.CLASS_BODY_DECLARATIONS.from($.get());
      // TODO Matteo: apply to CompilationUnit and not to
      // CLASS_BODY_DECLARATIONS -- matteo
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
  /** This method
   * @param u
   * @return */
  public ASTRewrite createRewrite(final BodyDeclaration u) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u);
    return $;
  }
  /** Rewrite CompilationUnit
   * @param ¢
   * @return */
  public ASTRewrite createRewrite(final CompilationUnit ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    consolidateTips($, ¢);
    return $;
  }
  /** ConsolidateTips on CompilationUnit
   * @param r
   * @param u */
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
          tick(n, tipper);
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
      <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
        return toolbox.firstTipper(¢);
      }
      /** @param n
       * @param w */
      <N extends ASTNode> void tick(final N n, final Tipper<N> w) {
        tick(w);
        TrimmerLog.tip(w, n);
      }
      /** @param w */
      <N extends ASTNode> void tick(final Tipper<N> w) {
        final String key = monitor.className(w.getClass());
        if (!spectrum.containsKey(key))
          spectrum.put(key, 0);
        spectrum.put(key, spectrum.get(key) + 1);
      }
      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }
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
          tick(n, tipper);
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
      // <N extends ASTNode> void tick2(final Tipper<N> w) {
      // final String key = presentFileName + "-" + presentMethod +
      // monitor.className(w.getClass());
      //// if (!coverage.containsKey(key))
      //// coverage.put(key, 0);
      //// coverage.put(key, coverage.get(key) + 1);
      // }
      <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
        return toolbox.firstTipper(¢);
      }
      /** @param n
       * @param w */
      <N extends ASTNode> void tick(final N n, final Tipper<N> w) {
        tick(w);
        TrimmerLog.tip(w, n);
      }
      /** @param w */
      <N extends ASTNode> void tick(final Tipper<N> w) {
        final String key = monitor.className(w.getClass());
        if (!spectrum.containsKey(key))
          spectrum.put(key, 0);
        spectrum.put(key, spectrum.get(key) + 1);
      }
      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }
  static boolean filter(@SuppressWarnings("unused") final ASTNode __) {
    return false;
  }
  @SuppressWarnings("static-method") public void selectedNodes(@SuppressWarnings("unchecked") final Class<? extends BodyDeclaration>... ¢) {
    selectedNodeTypes = as.list(¢);
  }
}
