package il.org.spartan.spartanizer.cmdline;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;

import fluent.ly.___;
import fluent.ly.forget;
import fluent.ly.note;
import il.org.spartan.collections.FilesGenerator;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.utils.FileUtils;

/** An {@link IApplication} extension entry point, allowing execution of ***
 * @author Ori Marcovitch
 * @since Dec 16, 2016 */
final class BindingFun implements IApplication {
  private static final String C_USERS_SORIMAR_WORKSPACE_TEST_ADD_COMMENTS = "C:\\Users\\sorimar\\workspace\\testAddComments";

  private static void iterateMethodInvocations(final CompilationUnit u) {
    u.accept(new ASTVisitor(true) {
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
    return wizard.getPackageNameFromSource($.createAST(null));
  }
  private IJavaProject javaProject;
  private IPackageFragmentRoot srcRoot;
  private IPackageFragment pack;

  @Override public Object start(final IApplicationContext arg0) {
    forget.em(arg0);
    try {
      prepareTempIJavaProject();
    } catch (final CoreException ¢) {
      note.bug(¢);
      return IApplication.EXIT_OK;
    }
    for (final File f : new FilesGenerator(".java", ".JAVA").from(C_USERS_SORIMAR_WORKSPACE_TEST_ADD_COMMENTS))
      try {
        final ICompilationUnit u = openCompilationUnit(f);
        final ASTParser parser = ASTParser.newParser(AST.JLS14);
        parser.setResolveBindings(true);
        parser.setSource(u);
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        forget.em(new Object[] {});
        iterateMethodInvocations(cu);
      } catch (final IOException ¢) {
        note.io(¢, f + "");
      } catch (final JavaModelException ¢) {
        note.bug(this, ¢);
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
      note.bug(this, ¢);
    }
  }
  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException ¢) {
      note.bug(¢);
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
