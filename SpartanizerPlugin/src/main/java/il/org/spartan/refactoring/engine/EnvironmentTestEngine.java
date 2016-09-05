package il.org.spartan.refactoring.engine;

import java.util.*;
import java.util.Map.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;

public class EnvironmentTestEngine {
  static Set<Entry<String, Environment.Information>> generateSet() {
    return Collections.unmodifiableSet(new HashSet<>());
  }

  // s = "il.org.spartan.refactoring.java.EnvironmentCodeExamples.java"
  static CompilationUnit getCompilationUnit(final String from) {
    final IJavaProject javaProject = getJavaProject("spartenRefactoring");
    IType iType;
    CompilationUnit $ = null;
    try {
      iType = javaProject.findType(from);
      final ICompilationUnit iCompilationUnit = iType.getCompilationUnit();
      @SuppressWarnings("deprecation") final ASTParser parser = ASTParser.newParser(AST.JLS3);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setSource(iCompilationUnit);
      parser.setResolveBindings(true); // we need bindings later on
      $ = (CompilationUnit) parser.createAST(null);
    } catch (final JavaModelException e) {
      e.printStackTrace();
    }
    return $;
  }

  private static IJavaProject getJavaProject(final String projectName) {
    final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    if (project == null)
      return null;
    final IJavaProject $ = JavaCore.create(project);
    return $ != null && $.exists() ? $ : null;
  }

  static Set<Entry<String, Environment.Information>> listToSet(final List<Annotation> as) {
    final Set<Entry<String, Environment.Information>> $ = Collections.unmodifiableSet(new HashSet<>());
    for (final Annotation ¢ : as) {
    }
    return $;
  }

  private final CompilationUnit cUnit;
  
  Set<Entry<String, Environment.Information>> testNestedENV;
  Set<Entry<String, Environment.Information>> testFlatENV;
  Set<Entry<String, Environment.Information>> testBeginEnd;

  EnvironmentTestEngine(final CompilationUnit $) {
    cUnit = $;
    testNestedENV = generateSet();
    testFlatENV = generateSet();
    testBeginEnd = generateSet();
  }

  EnvironmentTestEngine(final String ¢) {
    cUnit = getCompilationUnit(¢);
    testNestedENV = generateSet();
    testFlatENV = generateSet();
    testBeginEnd = generateSet();
  }

  public void runTest() {
    cUnit.accept(new ASTVisitor() {
      void addAnnotations(final List<Annotation> as) {
        for (final Annotation ¢ : as)
          dispatch(¢);
      }

      void dispatch(final Annotation a) {
        if (a.getTypeName() + "" == "@nestedENV")
          nestedHandler(a);
        else if (a.getTypeName() + "" == "@flatENV")
          flatHandler(a);
        else if (a.getTypeName() + "" == "@BegingEndENV")
          beginEndHandler(a);
      }

      void flatHandler(final Annotation a) {
      }

      void nestedHandler(final Annotation a) {
      }
      
      void beginEndHandler(final Annotation a) {
      }

      @Override public boolean visit(final MethodDeclaration d) {
        /* Set<Entry<String, Information>> useCheckSet = Environment.uses(d);
         * Set<Entry<String, Information>> declareCheckSet =
         * Environment.declares(d); */
        addAnnotations(extract.annotations(d));
        return true;
      }
    });
  }
}
// Don't delete yet:
/* IWorkspace workspace = ResourcesPlugin.getWorkspace(); IWorkspaceRoot root =
 * workspace.getRoot(); // Get all projects in the workspace IProject[] projects
 * = root.getProjects(); */
/* IPackageFragment[] packages = javaProject.getPackageFragments(); for
 * (IPackageFragment mypackage : packages) { if (mypackage.getKind() ==
 * IPackageFragmentRoot.K_SOURCE) { for (ICompilationUnit unit :
 * mypackage.getCompilationUnits()) {
 *
 * } } } */
