package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

public class FalloutsCollector_loops extends FolderASTVisitor {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  static final File out = new File(Table.temporariesFolder + "/" + "loops" + ".txt");

  public static void main(final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = FalloutsCollector_loops.class;
    blank(out);
    FolderASTVisitor.main(args);
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    final String after = spartanalyzer.fixedPoint(¢);
    for (final EnhancedForStatement l : searchDescendants.forClass(EnhancedForStatement.class).from(into.cu(after)))
      if (!iz.block(body(l)))
        appendFile(out, l + "");
    return true;
  }

  @Override protected void visit(final String path) {
    appendFile(out, "-------" + path + "-------\n");
    super.visit(path);
  }
}
