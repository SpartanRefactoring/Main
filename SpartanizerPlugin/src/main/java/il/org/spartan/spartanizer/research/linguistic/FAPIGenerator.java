package il.org.spartan.spartanizer.research.linguistic;

import static il.org.spartan.spartanizer.research.linguistic.FAPI.*;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-05-11 */
@UnderConstruction
public class FAPIGenerator {
  final FAPI fapi;
  private IPath path;
  private IProject project;
  private IProgressMonitor monitor;
  private IFile classFile;
  private AST ast;
  private CompilationUnit cu;
  private ASTRewrite r;
  private TypeDeclaration baseType;

  protected FAPIGenerator(final FAPI fapi) {
    this.fapi = Objects.requireNonNull(fapi);
  }
  public static FAPIGenerator by(final FAPI i) {
    return new FAPIGenerator(i);
  }
  public FAPIGenerator in(final IJavaProject p) {
    if (p == null)
      return this;
    try {
      for (final IPackageFragmentRoot fr : p.getAllPackageFragmentRoots())
        if (fr.getKind() == IPackageFragmentRoot.K_SOURCE) {
          path = fr.getPath().removeFirstSegments(1);
          project = p.getProject();
          return this;
        }
    } catch (final JavaModelException x) {
      note.bug(x);
    }
    return this;
  }
  public FAPIGenerator with(final IProgressMonitor m) {
    monitor = m;
    return this;
  }
  public boolean generateAll() {
    return generateFile() && generateCode();
  }
  private boolean generateFile() {
    if (path == null || project == null)
      return false;
    final IPath classPath = path.append(separate.these(fapi.names).by('/')).addFileExtension("java");
    if (!Eclipse.recursiveCreateFolder(project.getFolder(classPath.removeLastSegments(1).toString()), monitor))
      return false;
    try {
      classFile = project.getFile(classPath.toString());
      if (!classFile.exists())
        classFile.create(new ByteArrayInputStream("".getBytes()), IResource.NONE, monitor);
    } catch (final CoreException x) {
      note.bug(x);
      return false;
    }
    return true;
  }
  private boolean generateCode() {
    cu = az.compilationUnit(makeAST.COMPILATION_UNIT.fromWithBinding(classFile));
    if (cu == null)
      return false;
    ast = cu.getAST();
    r = ASTRewrite.create(cu.getAST());
    generatePackageDeclaration();
    generateBaseType();
    generateDeclarations();
    return true;
  }
  private void generatePackageDeclaration() {
    if (cu.getPackage() != null)
      return;
    final PackageDeclaration $ = ast.newPackageDeclaration();
    $.setName(ast.newName(separate.these(lisp.chopLast(fapi.names)).by('.')));
    r.set(cu, CompilationUnit.PACKAGE_PROPERTY, $, null);
  }
  @SuppressWarnings("unchecked") private void generateBaseType() {
    final List<AbstractTypeDeclaration> ds = cu.types();
    final String baseName = fapi.className.getIdentifier();
    for (final AbstractTypeDeclaration d : ds)
      if (iz.typeDeclaration(d) && d.getName().getIdentifier().equals(baseName)) {
        baseType = az.typeDeclaration(d);
        return;
      }
    baseType = ast.newTypeDeclaration();
    baseType.setName(ast.newSimpleName(baseName));
    baseType.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));
    r.getListRewrite(cu, CompilationUnit.TYPES_PROPERTY).insertFirst(baseType, null);
  }
  private void generateDeclarations() {
    TypeDeclaration d = baseType;
    int i = 0;
    outer: for (; i < fapi.invocations.size(); ++i) {
      if (!property.has(fapi.invocations.get(i), BINDING_PROPERTY))
        break;
      final ITypeBinding b = property.get(fapi.invocations.get(i), BINDING_PROPERTY);
      for (final TypeDeclaration t : d.getTypes())
        if (t.getName().getIdentifier().equals(b.getName())) {
          d = t;
          continue outer;
        }
      note.bug("FAPIGenerator#generateDeclarations: API not in class");
    }
    System.out.println(i);
    System.out.println(fapi.invocations.get(i));
    System.out.println(d);
  }
}
