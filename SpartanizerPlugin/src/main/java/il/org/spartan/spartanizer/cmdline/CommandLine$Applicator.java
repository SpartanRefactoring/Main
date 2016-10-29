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
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected PrintWriter afters;
  protected PrintWriter befores;
  File currentFile;
  int done;
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();
  final ChainStringToIntegerMap coverage = new ChainStringToIntegerMap();

  // TODO Matteo (reminder for himself): same as AbstractCommandLineSpartanizer
  // (code duplication to be resolved)
  void go(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        assert ¢ != null;
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢);
      }
    });
  }

  boolean go(final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    final String output = fixedPoint(input);
    final ASTNode outputASTNode = makeAST.CLASS_BODY_DECLARATIONS.from(output);
    Reports.printFile(input + "", "before");
    Reports.printFile(output, "after");
    computeMetrics(input, outputASTNode);
    return false;
  }

  protected void computeMetrics(final ASTNode input, final ASTNode output) {
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    Reports.summaryFileName("metrics");
    Reports.name(input);
    Reports.writeMetrics(input,output,null);
    Reports.write(input, output, "Δ ", (n1,n2)->(n1 - n2));
    Reports.write(input, output, "δ ", (n1,n2)->system.d(n1,n2));
    Reports.writePerc(input, output, "δ ");
    Reports.nl("metrics");
  }
  // public void reportDifferences(@SuppressWarnings("hiding") final
  // ASTNodeMetrics nm1, @SuppressWarnings("hiding") final ASTNodeMetrics nm2){
  // report //
  // .put("Δ Nodes", nm1.nodes() - nm2.nodes())//
  // .put("δ Nodes", system.d(nm1.nodes(), nm2.nodes()))//
  // .put("δ Nodes %", system.p(nm1.nodes(), nm2.nodes()))//
  // .put("Δ Body", nm1.body() - nm2.body())//
  // .put("δ Body", system.d(nm1.body(), nm2.body()))//
  // .put("% Body", system.p(nm1.body(), nm2.body()))//
  // .put("Δ Tokens", nm1.tokens() - nm2.tokens())//
  // .put("δ Tokens", system.d(nm1.tokens(), nm2.tokens()))//
  // .put("% Tokens", system.p(nm1.tokens(), nm2.tokens()))//
  // .put("Δ Length", nm1.length() - nm2.length())//
  // .put("δ Length", system.d(nm1.length(), nm2.length()))//
  // .put("% Length", system.p(nm1.length(), nm2.length()))//
  // .put("Δ Tide2", nm1.tide() - nm2.tide())//
  // .put("δ Tide2", system.d(nm1.tide(), nm2.tide()))//
  // .put("δ Tide2", system.p(nm1.tide(), nm2.tide()))//
  // .put("Δ Essence", nm1.essence() - nm2.essence())//
  // .put("δ Essence", system.d(nm1.essence(), nm2.essence()))//
  // .put("% Essence", system.p(nm1.essence(), nm2.essence()))//
  // .put("Δ Statement", nm1.statements() - nm2.statements())//
  // .put("δ Statement", system.d(nm1.statements(), nm2.statements()))//
  // .put("% Statement", system.p(nm1.statements(), nm2.statements()));//
  // }
  /** @param nm */
  // public void reportRatio(final ASTNodeMetrics nm, final String id){
  // report //
  //// .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
  // .put("R(E/L)" + id, system.ratio(nm.length(), nm.essence())) //
  // .put("R(E/T)" + id, system.ratio(nm.tide(), nm.essence())) //
  // .put("R(B/S)" + id, system.ratio(nm.nodes(), nm.body())); //
  // }
  String fixedPoint(final ASTNode ¢) {
    return fixedPoint(¢ + "");
  }

  public String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
      final BodyDeclaration u = (BodyDeclaration) makeAST.CLASS_BODY_DECLARATIONS.from($.get());
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

  public ASTRewrite createRewrite(final BodyDeclaration u) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u);
    return $;
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
          // tick(n, tipper);
        } catch (final TipperFailure f) {
          monitor.debug(this, f);
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
}