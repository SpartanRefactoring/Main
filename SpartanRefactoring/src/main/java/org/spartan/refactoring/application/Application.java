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
import org.spartan.refactoring.handlers.CleanupHandler;
import org.spartan.utils.FileUtils;
import org.spartan.utils.Wrapper;

/**
 * An {@link IApplication} extension entry point, allowing execution of this
 * plug-in from a terminal
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19
 */
@SuppressWarnings("static-method") public class Application implements IApplication {
  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;
  boolean optPrintBase64 = false, optDoNotOverwrite = false, optIndividualStatistics = false, optVerbose = false;
  String optPath;
  @Override public Object start(final IApplicationContext arg0) throws Exception {
    if (parseArguments((String[]) arg0.getArguments().get(IApplicationContext.APPLICATION_ARGS)))
      return IApplication.EXIT_OK;
    try {
      final List<String> javaFiles = FileUtils.findAllJavaFiles(optPath);
      prepareTempIJavaProject();
      final int[] roundStats = new int[20];
      for (final String f : javaFiles) {
        final ICompilationUnit u = openCompilationUnit(f);
        for (int i = 0; i < 20; ++i) {
          final int n = CleanupHandler.countSuggestions(u);
          if (n == 0)
            break;
          roundStats[i] += n;
          ApplySpartanizationHandler.applySafeSpartanizationsTo(u);
        }
        FileUtils.writeToFile(f, u.getSource());
      }
      System.out.println("Files processed: " + javaFiles.size());
      System.out.println("\nTotal modifications made: ");
      for (int i = 0; i < 20; ++i)
        System.out.println("    Round #" + (i + 1) + ": " + (i < 9 ? " " : "") + roundStats[i]);
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      discardTempIProject();
    }
    return IApplication.EXIT_OK;
  }
  @Override public void stop() {
    // TODO Auto-generated method stub
  }
  void printHelpPrompt() {
    System.out.println("Spartan Refactoring plugin command line");
    System.out.println("Usage: eclipse -application org.spartan.refactoring.application -nosplash [OPTIONS] PATH");
    System.out.println("Executes the Spartan Refactoring Eclipse plug-in from the command line on all the Java source files "
        + "within the given PATH. Files are spartanized in place by default.");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -b    Prints the output in base64");
    System.out.println("  -n    Writes the spartanized source to a new file (in the same directory as the original source file)");
    System.out.println("  -e    Display statistics for each file separately");
    System.out.println("  -v    Be verbose");
  }
  boolean parseArguments(final String[] args) {
    if (args == null || args.length == 0) {
      printHelpPrompt();
      return true;
    }
    for (final String a : args)
      switch (a) {
        case "-b":
          optPrintBase64 = true;
          break;
        case "-n":
          optDoNotOverwrite = true;
          break;
        case "-e":
          optIndividualStatistics = true;
          break;
        case "-v":
          optVerbose = true;
          break;
        default:
          optPath = a;
          break;
      }
    return optPath == null;
  }
  void prepareTempIJavaProject() {
    try {
      final IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTemp");
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
  String getPackageNameFromSource(final String source) {
    final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    p.setSource(source.toCharArray());
    final Wrapper<String> $ = new Wrapper<>("");
    p.createAST(null).accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration node) {
        $.set(node.getName().toString());
        return false;
      }
    });
    return $.get();
  }
  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }
}
