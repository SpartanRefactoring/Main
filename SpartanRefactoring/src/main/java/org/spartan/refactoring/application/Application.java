package org.spartan.refactoring.application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.spartan.refactoring.handlers.ApplySpartanizationHandler;
import org.spartan.utils.FileUtils;
import org.spartan.utils.Wrapper;

/**
 * An {@link IApplication} extension entry point, allowing execution of this
 * plug-in from a terminal
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19
 */
public class Application implements IApplication {
  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;
  @Override public Object start(final IApplicationContext arg0) throws Exception {
    final String[] args = (String[]) arg0.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    for (final String p : args)
      System.out.println("Path: " + p + ", Java files found: " + FileUtils.findAllJavaFiles(p).size());
    final List<String> javaFiles = FileUtils.findAllJavaFiles("/home/mittelman/Corpus/k-9/Current/src/com/fsck/k9/activity");
    prepareTempIJavaProject();
    final ICompilationUnit u = openCompilationUnit(javaFiles.get(0));
    for (int i = 0; i < 20; i++)
      ApplySpartanizationHandler.applySafeSpartanizationsTo(u);
    FileUtils.writeToFile(javaFiles.get(0), u.getSource());
    discardTempIProject();
    return IApplication.EXIT_OK;
  }
  @Override public void stop() {
    // TODO Auto-generated method stub
  }
  void prepareTempIJavaProject() {
    try {
      final IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTemp7");
      final boolean isNew = !proj.exists();
      if (isNew)
        proj.create(null);
      proj.open(null);
      if (isNew) {
        final IProjectDescription desc = proj.getDescription();
        desc.setNatureIds(new String[] { JavaCore.NATURE_ID });
        proj.setDescription(desc, null);
      }
      javaProject = JavaCore.create(proj);
      final IFolder binFolder = proj.getFolder("bin");
      final IFolder sourceFolder = proj.getFolder("src");
      srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
      if (isNew) {
        binFolder.create(false, true, null);
        sourceFolder.create(false, true, null);
      }
      javaProject.setOutputLocation(binFolder.getFullPath(), null);
      final IClasspathEntry[] buildPath = new IClasspathEntry[1];
      buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
      javaProject.setRawClasspath(buildPath, null);
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }
  void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
  ICompilationUnit openCompilationUnit(final String path) throws IOException, JavaModelException {
    final String source = FileUtils.readFromFile(path);
    final String packageName = getPackageNameFromSource(source);
    setPackage(packageName);
    return pack.createCompilationUnit(new File(path).getName(), source, false, null);
  }
  static String getPackageNameFromSource(final String source) {
    final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    p.setSource(source.toCharArray());
    final Wrapper<String> $ = new Wrapper<>("");
    p.createAST(null).accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration node) {
        $.set(node.getName().toString());
        return false;
      }
    });
    return $.toString();
  }
  void discardTempIProject() throws CoreException {
    javaProject.getProject().delete(true, null);
  }
}
