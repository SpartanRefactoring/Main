package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** Like FolderASTVisitor but with binding. Needs to be run as an Application,
 * which is not fun at all. Not ready yet.
 * @author Ori Marcovitch
 * @since Dec 16, 2016 */
public abstract class FolderWithBindingASTVisitor extends DeprecatedFolderASTVisitor implements IApplication {
  @Override void visit(final File ¢) {
    dotter.click();
    collect(¢);
  }

  void collect(final File f) {
    try {
      final ICompilationUnit u = openCompilationUnit(f);
      final ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setResolveBindings(true);
      parser.setSource(u);
      collect(az.compilationUnit(parser.createAST(null)));
    } catch (final JavaModelException ¢) {
      ¢.printStackTrace();
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, f + "");
    }
  }

  static String getPackageNameFromSource(final String source) {
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

  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;

  public abstract void Main(String... args);

  @Override public Object start(final IApplicationContext arg0) {
    ___.unused(arg0);
    try {
      prepareTempIJavaProject();
    } catch (final CoreException ¢) {
      System.err.println(¢.getMessage());
      return IApplication.EXIT_OK;
    }
    return IApplication.EXIT_OK;
  }

  @Override public void stop() {
    discardTempIProject();
  }

  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException ¢) {
      ¢.printStackTrace();
    }
  }

  ICompilationUnit openCompilationUnit(final File ¢) throws IOException, JavaModelException {
    final String $ = FileUtils.read(¢);
    setPackage(getPackageNameFromSource($));
    return pack.createCompilationUnit(¢.getName(), $, false, null);
  }

  void prepareTempIJavaProject() throws CoreException {
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

  void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
}
