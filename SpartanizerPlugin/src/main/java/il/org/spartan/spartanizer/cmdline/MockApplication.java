package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.old.*;
// import il.org.spartan.plugin.revision.QuickFixer.SingleTipper;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

/** @author Matteo Orru'
 * @since 2016 */
public class MockApplication implements IApplication {
  private final String optPath = "/home/matteo/git/commons-lang";
  private IPackageFragment pack;
  private IPackageFragmentRoot srcRoot;
  private int optRounds;

  /* (non-Javadoc)
   *
   * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
   * IApplicationContext) */
  @Override public Object start(final IApplicationContext arg0) throws Exception {
    System.out.println("This is a mock application for testing purposes");
    if (arg0.getArguments().isEmpty())
      System.out.println("No args");
    prepareTempIJavaProject();
    System.out.println(srcRoot);
    for (final File ¢ : new FilesGenerator(".java", ".JAVA").from(optPath)) {
      System.out.println(¢);
      processCU(openCompilationUnit(¢));
    }
    return IApplication.EXIT_OK;
  }
  /** @param u */
  private void processCU(final ICompilationUnit u) {
    for (int i = 0; i < optRounds; ++i) {
      final int n = new LaconizeProject().countTips();
      if (n == 0)
        break;
      new Trimmer().apply(u);
    }
  }
  ICompilationUnit openCompilationUnit(final File f) throws IOException, JavaModelException {
    final String source = FileUtils.read(f);
    // System.out.println(source);
    setPackage(getPackageNameFromSource(source));
    return pack.createCompilationUnit(f.getName(), source, false, null);
  }
  static String getPackageNameFromSource(final String source) {
    final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    p.setSource(source.toCharArray());
    return getPackageNameFromSource(new Wrapper<>(""), p.createAST(null));
  }
  private static String getPackageNameFromSource(final Wrapper<String> $, final ASTNode n) {
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration ¢) {
        $.set(¢.getName() + "");
        return false;
      }
    });
    return $.get();
  }
  void setPackage(final String name) throws JavaModelException {
    System.out.println(srcRoot);
    pack = srcRoot.createPackageFragment(name, false, null);
  }
  /* (non-Javadoc)
   *
   * @see org.eclipse.equinox.app.IApplication#stop() */
  @Override public void stop() {
    ___.nothing();
  }
  void prepareTempIJavaProject() throws CoreException {
    final IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTemp");
    if (p.exists())
      p.delete(true, null);
    p.create(null);
    p.open(null);
    final IProjectDescription d = p.getDescription();
    d.setNatureIds(new String[] { JavaCore.NATURE_ID });
    p.setDescription(d, null);
    final IJavaProject javaProject = JavaCore.create(p);
    final IFolder binFolder = p.getFolder("bin");
    final IFolder sourceFolder = p.getFolder("src");
    srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
    binFolder.create(false, true, null);
    sourceFolder.create(false, true, null);
    javaProject.setOutputLocation(binFolder.getFullPath(), null);
    final IClasspathEntry[] buildPath = new IClasspathEntry[1];
    buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
    javaProject.setRawClasspath(buildPath, null);
    System.err.println("spartanTemp is ready.");
  }
}
