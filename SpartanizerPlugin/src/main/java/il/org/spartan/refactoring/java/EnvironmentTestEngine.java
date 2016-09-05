package il.org.spartan.refactoring.java;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

public class EnvironmentTestEngine {
  IWorkspace workspace = ResourcesPlugin.getWorkspace();
  IWorkspaceRoot root = workspace.getRoot();
  // Get all projects in the workspace
  IProject[] projects = root.getProjects();
  
  
  IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
  IPackageFragment mypackage = packages.get(0); // implement your own logic to select package
  ICompilationUnit[] unit = mypackage.getCompilationUnits();
  
  
  ASTParser parser = ASTParser.newParser(AST.JLS3); 
  parser.setKind(ASTParser.K_COMPILATION_UNIT);
  parser.setSource(unit);
  parser.setResolveBindings(true);
  CompilationUnit cUnit = parser.createAST(null);
}
