package il.org.spartan.refactoring.engine;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

public class EnvironmentTestEngine {
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
