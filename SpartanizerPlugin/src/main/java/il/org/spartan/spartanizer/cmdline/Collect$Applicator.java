package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

public class Collect$Applicator {
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);
  // private static CSVStatistics report;
  public Toolbox toolbox;

  public void go(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        assert ¢ != null;
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢);
      }
    });
  }
  boolean go(final ASTNode input) {
    System.err.println("input: " + input);
    // tippersAppliedOnCurrentObject = 0;
    // final String output = fixedPoint(input + "");
    for (final Document $ = new Document(input + "");;) {
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get());
      // final BodyDeclaration u = (BodyDeclaration)
      // makeAST.CLASS_BODY_DECLARATIONS.from($.get());
      // final ASTRewrite $1 = ASTRewrite.create(u.getAST());
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
          System.out.println("tipper: " + tipper);
          if (tipper == null)
            return true;
          Tip s = null;
          try {
            System.out.println("tip!!!");
            s = tipper.tip(n, exclude);
            ReportGenerator.tip(s);
            ReportGenerator.nl("tips");
          } catch (final Exception x) {
            monitor.debug(this, x);
          }
          return true;
        }
        @Override protected void initialization(final ASTNode ¢) {
          disabling.scan(¢);
        }
      });
    }
  }
  // String fixedPoint(final ASTNode ¢) {
  // return fixedPoint(¢ + "");
  // }
  public String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
      final BodyDeclaration u = (BodyDeclaration) makeAST.CLASS_BODY_DECLARATIONS.from($.get());
      // final ASTRewrite $1 = ASTRewrite.create(u.getAST());
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
            ReportGenerator.tip(s);
            ReportGenerator.nl("tips");
          } catch (final Exception x) {
            monitor.debug(this, x);
          }
          return true;
        }
        @Override protected void initialization(final ASTNode ¢) {
          disabling.scan(¢);
        }
      });
    }
  }
  // public void consolidateTips(final ASTRewrite r, final BodyDeclaration u) {
  // toolbox = Toolbox.defaultInstance();
  // u.accept(new DispatchingVisitor() {
  // @Override protected <N extends ASTNode> boolean go(final N n) {
  // TrimmerLog.visitation(n);
  // if (disabling.on(n))
  // return true;
  // Tipper<N> tipper = null;
  // try {
  // tipper = getTipper(n);
  // } catch (final Exception x) {
  // monitor.debug(this, x);
  // }
  // if (tipper == null)
  // return true;
  // Tip s = null;
  // try {
  // s = tipper.tip(n, exclude);
  //
  // } catch (final TipperFailure f) {
  // monitor.debug(this, f);
  // } catch (final Exception x) {
  // monitor.debug(this, x);
  // }
  // if (s != null) {
  //// ++tippersAppliedOnCurrentObject;
  // TrimmerLog.application(r, s);
  // }
  // return true;
  // }
  //
  // @Override protected void initialization(final ASTNode ¢) {
  // disabling.scan(¢);
  // }
  // });
  // }
  <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }
}