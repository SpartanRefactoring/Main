package il.org.spartan.spartanizer.research.linguistic;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-05-11 */
public class FAPIGenerator {
  final FAPI fapi;
  IPath path;
  IProject project;
  IProgressMonitor monitor;
  private IFile classFile;

  protected FAPIGenerator(FAPI fapi) {
    this.fapi = Objects.requireNonNull(fapi);
  }
  public static FAPIGenerator by(FAPI fapi) {
    return new FAPIGenerator(fapi);
  }
  public FAPIGenerator in(IJavaProject p) {
    if (p == null)
      return this;
    try {
      for (IPackageFragmentRoot r : p.getAllPackageFragmentRoots())
        if (r.getKind() == IPackageFragmentRoot.K_SOURCE) {
          path = r.getPath().removeFirstSegments(1);
          project = p.getProject();
          return this;
        }
    } catch (JavaModelException x) {
      note.bug(x);
    }
    return this;
  }
  public boolean generateAll() {
    return generateFile() && generateCode();
  }
  public boolean generateFile() {
    if (path == null || project == null)
      return false;
    IPath classPath = path.append(separate.these(fapi.names).by('/')).addFileExtension("java");
    if (!Eclipse.recursiveCreateFolder(project.getFolder(classPath.removeLastSegments(1).toString()), monitor))
      return false;
    try {
      classFile = project.getFile(classPath.toString());
      if (!classFile.exists())
        classFile.create(new ByteArrayInputStream("".getBytes()), IResource.NONE, monitor);
    } catch (CoreException x) {
      note.bug(x);
      return false;
    }
    return true;
  }
  @SuppressWarnings("static-method") public boolean generateCode() {
    return true;
  }
}
