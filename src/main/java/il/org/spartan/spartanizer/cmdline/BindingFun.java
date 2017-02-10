package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** An {@link IApplication} extension entry point, allowing execution of ***
 * @author Ori Marcovitch
 * @since Dec 16, 2016 */
final class BindingFun implements IApplication {
  private static final String C_USERS_SORIMAR_WORKSPACE_TEST_ADD_COMMENTS = "C:\\Users\\sorimar\\workspace\\testAddComments";

  private static void iterateMethodInvocations(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodInvocation ¢) {
        assert ¢.getAST().hasResolvedBindings();
        System.out.println(¢.resolveMethodBinding());
        return super.visit(¢);
      }
    });
  }

  private static String getPackageNameFromSource(final String source) {
    final ASTParser $ = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    $.setResolveBindings(true);
    $.setSource(source.toCharArray());
    return getPackageNameFromSource($.createAST(null));
  }

  private static String getPackageNameFromSource(final ASTNode n) {
    final Wrapper<String> $ = new Wrapper<>("");
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration ¢) {
        $.set(¢.getName() + "");
        return false;
      }
    });
    return $.get();
  }

  private IJavaProject javaProject;
  private IPackageFragmentRoot srcRoot;
  private IPackageFragment pack;

  @Override public Object start(final IApplicationContext arg0) {
    ___.unused(arg0);
    try {
      prepareTempIJavaProject();
    } catch (final CoreException ¢) {
      System.err.println(¢.getMessage());
      return IApplication.EXIT_OK;
    }
    for (final File f : new FilesGenerator(".java", ".JAVA").from(C_USERS_SORIMAR_WORKSPACE_TEST_ADD_COMMENTS))
      try {
        final ICompilationUnit u = openCompilationUnit(f);
        final ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setResolveBindings(true);
        parser.setSource(u);
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        ___.______unused();
        iterateMethodInvocations(cu);
      } catch (IOException ¢) {
        monitor.infoIOException(¢, f + "");
      } catch (JavaModelException ¢) {
        ¢.printStackTrace();
      }
    return IApplication.EXIT_OK;
  }

  @Override public void stop() {
    ___.nothing();
  }

  /** Discard compilation unit u
   * @param u */
  void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final NullPointerException | JavaModelException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
  }

  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException ¢) {
      ¢.printStackTrace();
    }
  }

  private ICompilationUnit openCompilationUnit(final File ¢) throws JavaModelException, IOException {
    final String $ = FileUtils.read(¢);
    setPackage(getPackageNameFromSource($));
    return pack.createCompilationUnit(¢.getName(), $, false, null);
  }

  private void prepareTempIJavaProject() throws CoreException {
    final IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("tempP");
    if (p.exists())
      p.delete(true, null);
    p.create(null);
    p.open(null);
    final IProjectDescription d = p.getDescription();
    d.setNatureIds(new String[] { JavaCore.NATURE_ID });
    p.setDescription(d, null);
    javaProject = JavaCore.create(p);
    final IFolder binFolder = p.getFolder("bin"), sourceFolder = p.getFolder("src");
    srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
    binFolder.create(false, true, null);
    sourceFolder.create(false, true, null);
    javaProject.setOutputLocation(binFolder.getFullPath(), null);
    final IClasspathEntry[] buildPath = new IClasspathEntry[1];
    buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
    javaProject.setRawClasspath(buildPath, null);
  }

  private void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
}
