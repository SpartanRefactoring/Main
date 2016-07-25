package il.org.spartan.refactoring.suggestions;


import java.util.*;

import il.org.spartan.*;
import il.org.spartan.lazy.*;

import static il.org.spartan.lazy.Cookbook.cook;
import static il.org.spartan.lazy.Cookbook.from;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;

/**
 * @author Yossi Gil
 *
 * @since 2016`
 */
@SuppressWarnings("javadoc") 
public class Bench implements Cookbook {
  // @formatter:off vim: +,/formatter:off/-!sort -u | indent -t
  public  final ICompilationUnit  compilationUnit()        {  return  compilationUnit.get();        }
  public  final IEditorInput      editorInput()            {  return  editorInput.get();            }
  public  final IEditorPart       activeEditor()           {  return  activeEditor.get();           }
  public  final IFile             iFile()                  {  return  iFile.get();                  }
  public  final IJavaProject      javaProject()            {  return  javaProject.get();            }
  public  final IResource         resource()               {  return  resource.get();               }
  public  final IWorkbenchPage    activePage()             {  return  activePage.get();             }
  public  final IWorkbenchWindow  activeWorkBenchWindow()  {  return  activeWorkBenchWindow.get();  }
  public  final IWorkbench        workbench()              {  return  workbench.get();              }
  public  final IProgressMonitor  progressMonitor()        {  return  progressMonitor.get();        }
  // @formatter:on

  final Cell<IProgressMonitor> progressMonitor = from().make(() -> new NullProgressMonitor());
  final Cell<IWorkbench> workbench = cook(()->PlatformUI.getWorkbench());
  final Cell<IWorkbenchWindow> activeWorkBenchWindow = cook(()->workbench().getActiveWorkbenchWindow());
  final Cell<IWorkbenchPage> activePage = cook(()->activeWorkBenchWindow().getActivePage());
  final Cell<IEditorPart> activeEditor = cook(()->activePage().getActiveEditor());
  final Cell<IEditorInput> editorInput = cook(()->activeEditor().getEditorInput());
  final Cell<IResource> resource = cook(()->(IResource) editorInput().getAdapter(IResource.class));
  final Cell<IFile> iFile = cook(()->(IFile) resource());
  final Cell<ICompilationUnit> compilationUnit = cook(()->JavaCore.createCompilationUnitFrom(iFile()));
  final Cell<IJavaProject> javaProject = cook(()-> compilationUnit().getJavaProject());
  final Cell<List<ICompilationUnit>> allCompilationUnits = from(compilationUnit).make(//
      () -> {
        progressMonitor().beginTask("Collecting all project's compilation units...", 1);
        final List<ICompilationUnit> $ = collect();
        progressMonitor().done();
        return $;
      }); 
  private Void collectInto(final Collection<ICompilationUnit> $, final IPackageFragmentRoot[] rs) {
   for (final IPackageFragmentRoot r : rs)
     try {
       progressMonitor().worked(1);
       if (r.getKind() != IPackageFragmentRoot.K_SOURCE)
         continue;
       progressMonitor().worked(1);
       for (final IJavaElement e : r.getChildren()) {
         progressMonitor().worked(1);
         if (e.getElementType() != IJavaElement.PACKAGE_FRAGMENT)
           break;
         $.addAll(as.list(((IPackageFragment) e).getCompilationUnits()));
         progressMonitor().worked(1);
       }
       progressMonitor().worked(1);
     } catch (final JavaModelException x) {
       x.printStackTrace();
       continue;
     }
   return null;
 }
  private List<ICompilationUnit> collectInto(ICompilationUnit compilationUnit2, List<ICompilationUnit> $) {
    // TODO Auto-generated method stub
    
  }  

}

