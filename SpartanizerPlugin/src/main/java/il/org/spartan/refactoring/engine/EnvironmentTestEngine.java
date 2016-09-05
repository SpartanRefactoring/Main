package il.org.spartan.refactoring.engine;

import java.util.*;
import java.util.Map.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;


public class EnvironmentTestEngine {
  private CompilationUnit cUnit;
  EnvironmentTestEngine(CompilationUnit $) {
    cUnit = $;
  }
  
  EnvironmentTestEngine(String ¢) {
    cUnit = getCompilationUnit(¢);
  }
  
  static Set<Entry<String, Information>> listToSet(List<Annotation> as) {
    Set<Entry<String, Information>> $ = Collections.unmodifiableSet(new HashSet<>());
    for(Annotation ¢ : as) {}
    return $;
  }
  
  static Set<Entry<String, Information>> generateSet() {
    return Collections.unmodifiableSet(new HashSet<>());
  }
  
  public void runTest() {
    //Set<Entry<String, Information>> testNestedENV = generateSet();
    //Set<Entry<String, Information>> testFlatENV = generateSet();
    //Set<Entry<String, Information>> testBeginEnd = generateSet();
    
    cUnit.accept(new ASTVisitor() {
      boolean nestedHendler(Annotation a) {
        return true;
      }
      boolean flatHendler(Annotation a) {
        return true;
      }
      boolean beginEndHendler(Annotation a) {
        return true;
      }
      void dispatch(Annotation a) {
        if (a.getTypeName() + "" == "@nestedENV")
          nestedHendler(a);
        else if (a.getTypeName() + "" == "@flatENV")
          flatHendler(a);
        else if (a.getTypeName() + "" == "@BegingEndENV")
          beginEndHendler(a);
      }
      void addAnnotations(List<Annotation> as) {
        for (Annotation ¢ : as)
          dispatch(¢);
      }
      @Override public boolean visit(final MethodDeclaration d) {
        /*Set<Entry<String, Information>> useCheckSet = Environment.uses(d);
        Set<Entry<String, Information>> declareCheckSet = Environment.declares(d);*/
        addAnnotations(extract.annotations(d));
        return true;
      }
    });
  }
  
  //s = "il.org.spartan.refactoring.java.EnvironmentCodeExamples.java"
  static CompilationUnit getCompilationUnit(String from) {
    IJavaProject javaProject = getJavaProject("spartenRefactoring");
    IType iType;
    CompilationUnit $ = null;
    try {
      iType = javaProject.findType(from);
      ICompilationUnit iCompilationUnit = iType.getCompilationUnit();

      @SuppressWarnings("deprecation")
      final ASTParser parser = ASTParser.newParser(AST.JLS3); 
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setSource(iCompilationUnit);
      parser.setResolveBindings(true); // we need bindings later on
      $ = (CompilationUnit) parser.createAST(null);
      
    } catch (JavaModelException e) {
      e.printStackTrace();
    }
    return $;
  }
  
  private static IJavaProject getJavaProject(String projectName) {
    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    if (project == null)
      return null;
    IJavaProject $ = JavaCore.create(project);
    return $ != null && $.exists() ? $ : null;
  }
}

// Don't delete yet:

/*
IWorkspace workspace = ResourcesPlugin.getWorkspace();
IWorkspaceRoot root = workspace.getRoot();
// Get all projects in the workspace
IProject[] projects = root.getProjects();
*/
/*
IPackageFragment[] packages = javaProject.getPackageFragments();
for (IPackageFragment mypackage : packages) {
  if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
    for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
      
    }
  }
}
*/
