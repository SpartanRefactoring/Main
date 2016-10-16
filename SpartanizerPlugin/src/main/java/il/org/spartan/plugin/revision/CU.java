package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;

/** @author Ori Roth
 * @since 2016 */
public class CU {
  public ICompilationUnit descriptor;
  public CompilationUnit compilationUnit;
  private static final IProgressMonitor npm = new NullProgressMonitor();

  public CU(final ICompilationUnit compilationUnit) {
    descriptor = compilationUnit;
  }

  public CU build() {
    if (compilationUnit == null)
      compilationUnit = (CompilationUnit) Make.COMPILATION_UNIT.parser(descriptor).createAST(npm);
    return this;
  }

  public CU dispose() {
    compilationUnit = null;
    return this;
  }

  public String name() {
    return descriptor == null ? null : descriptor.getElementName();
  }

  public static CU of(final ICompilationUnit ¢) {
    return new CU(¢);
  }

  /** [[SuppressWarningsSpartan]] */
  public static List<CU> of(final List<ICompilationUnit> ¢) {
    List<CU> $ = new ArrayList<>();
    for (final ICompilationUnit u : ¢)
      $.add(new CU(u));
    return $;
  }
}
