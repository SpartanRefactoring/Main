package il.org.spartan.spartanizer.research.analyses;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;

public class NanoInstancesCollector extends FolderASTVisitor {
  static final NanoPatternTipper<Block> nano = new ReturnHoldsForAny();
  static final InteractiveSpartanizer spartanalyzer = new InteractiveSpartanizer();
  static final File out = new File(Table.temporariesFolder + system.fileSeparator + nano.getClass().getSimpleName() + ".txt");

  public static void main(final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = NanoInstancesCollector.class;
    spartanalyzer.add(Block.class, new NanoPatternTipper<Block>() {
      @Override public Tip pattern(final Block ¢) {
        return new Tip("", ¢, this.getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            Files.appendFile(out, ¢ + "_________________\n");
            nano.tip(¢).go(r, g);
          }
        };
      }

      @Override public boolean canTip(final Block n) {
        return nano.canTip(n);
      }

      @Override public String description(final Block n) {
        return nano.description(n);
      }
    });
    FolderASTVisitor.main(args);
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    spartanalyzer.fixedPoint(¢);
    return true;
  }

  @Override protected void visit(final String path) {
    Files.appendFile(out, "-------" + path + "-------\n");
    super.visit(path);
  }
}
