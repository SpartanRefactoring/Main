package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.lang.reflect.*;
import java.rmi.activation.*;
import java.util.List;

import org.eclipse.core.commands.*;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.internal.content.Activator;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.commands.*;
import org.eclipse.ui.ide.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.plugin.old.*;
import nano.ly.*;

/** Even better than 300! A handler that runs the spartanization process step by
 * step until completion.
 * @author Ori Roth
 * @author Matteo Orru'
 * @since 2016 */
@SuppressWarnings("all")
public class SpartanMovie2 extends AbstractHandler {
  private static final String NAME = "Spartan movie";
  private static final double SLEEP_BETWEEN = 0.5;
  private static final double SLEEP_END = 2;

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    
    final IWorkbench workbench = PlatformUI.getWorkbench();
    final List<ICompilationUnit> compilationUnits = getCompilationUnits();
    final IWorkbenchWindow window = workbench == null ? null : workbench.getActiveWorkbenchWindow();
    final IWorkbenchPage page = window == null ? null : window.getActivePage();
    final IProgressService progressService = workbench == null ? null : workbench.getProgressService();
    final GUITraversal traversal = new GUITraversal();
    if (compilationUnits == null || page == null || progressService == null) return null;

//    UIJob job = new UIJob(NAME) {
//      @Override public IStatus runInUIThread(IProgressMonitor monitor) {
//          monitor.beginTask("Preparing", 5000);
//          monitor.beginTask(NAME, IProgressMonitor.UNKNOWN);
          int changes = 0, filesModified = 0;
          for (final ICompilationUnit currentCompilationUnit : compilationUnits) {
//            System.out.println(currentCompilationUnit.getElementName());
            //mightNotBeSlick(page);
            final IResource file = currentCompilationUnit.getResource();
            try {
              IMarker[] markers = getMarkers(file);
              if (markers.length > 0)
                ++filesModified;
                for (; markers.length > 0; markers = getMarkers(file)) {
                  final IMarker marker = getFirstMarker(markers);
//                  monitor.subTask("Working on " + file.getName() + "\nCurrent tip: " + ((Class<?>)
//                                  marker.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY)).getSimpleName());
                  printout(marker);
                  delegateUIJob(page, marker);
                  //refresh(page);
                  //sleep(SLEEP_BETWEEN);
                  traversal.runAsMarkerFix(marker);
                  ++changes;
                  marker.delete(); // TODO Ori Roth: does not seem to make a 
                                   // difference
                                   // actually it removes the markers after the traversal
                                   // and avoid the infinite loop (it descreases markers.length at
                                   // each round -- mo
                  refresh(page);
                  sleep(SLEEP_BETWEEN);
                }
              } catch (final CoreException ¢) {
                note.bug(¢);
              }
           }
//           monitor.subTask("Done: Commited " + changes + " changes in " + filesModified + " " + English.plurals("file", filesModified));
           sleep(SLEEP_END);
//           monitor.done();
//           return Status.OK_STATUS;
//      }
//    }; // end job
    
    
//    ICommandService service = (ICommandService) 
//        PlatformUI.getWorkbench().getService(ICommandService.class);
//    Command command = service == null ? null : 
//      service.getCommand("il.org.spartan.SpartanMovie");
//    
//    if(command != null){
//      job.setProperty(IProgressConstants2.COMMAND_PROPERTY, 
//            ParameterizedCommand.generateCommand(command, null));
//      job.setProperty(IProgressConstants2.ICON_PROPERTY,
//          ImageDescriptor.createFromURL(SpartanMovie2.class.getResource("/icons/sample.gif")));
//      job.setProperty(IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY,
//          Boolean.TRUE);
//    }
    
//    job.schedule();
    return null;
  }


private void delegateUIJob(final IWorkbenchPage page, final IMarker marker) throws PartInitException {
  UIJob job = new UIJob(NAME) {
    @Override public IStatus runInUIThread(IProgressMonitor monitor) {
      try {
        IDE.openEditor(page, marker, true);
      } catch (PartInitException x) {
        x.printStackTrace();
      }
      return Status.OK_STATUS;
    }
  };
  job.schedule();  
}

//Boolean sleep(double howMuch) {
//  try {
//    Thread.sleep((int) (1000 * howMuch));
//    return true;
//  } catch (final InterruptedException ¢) {
//    note.bug(¢);
//    return false;
//  }
//}
  
//  private Object foo(){
//    
//    final IWorkbench workbench = PlatformUI.getWorkbench();
//    final List<ICompilationUnit> compilationUnits = getCompilationUnits();
//    final IWorkbenchWindow window = workbench == null ? null : workbench.getActiveWorkbenchWindow();
//    final IWorkbenchPage page = window == null ? null : window.getActivePage();
//    final IProgressService progressService = workbench == null ? null : workbench.getProgressService();
//    final GUITraversal traversal = new GUITraversal();
//    if (compilationUnits == null || page == null || progressService == null) return null;
//
//    int changes = 0, filesModified = 0;
//    for (final ICompilationUnit currentCompilationUnit : compilationUnits) {
////      System.out.println(currentCompilationUnit.getElementName());
//      //mightNotBeSlick(page);
//      final IResource file = currentCompilationUnit.getResource();
//      
//        IMarker[] markers = getMarkers(file);
//        if (markers.length > 0)
//          ++filesModified;
//    
//        UIJob job = new UIJob(NAME) {
//          @Override public IStatus runInUIThread(IProgressMonitor monitor) {
//    //          monitor.beginTask("Preparing", 5000);
//              monitor.beginTask(NAME, IProgressMonitor.UNKNOWN);
//              
//                    for (; markers.length > 0; markers = getMarkers(file)) {
//                      final IMarker marker = getFirstMarker(markers);
//                      try {
//                        monitor.subTask("Working on " + file.getName() + "\nCurrent tip: " + ((Class<?>)
//                                        marker.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY)).getSimpleName());
//                      } catch (CoreException x) {
//                        x.printStackTrace();
//                      }
//                      printout(marker);
//                      try {
//                        UIJob job2 = new UIJob(NAME) {
//                          @Override public IStatus runInUIThread(IProgressMonitor monitor) {
//                            IDE.openEditor(page, marker, true);
//                            return Status.OK_STATUS;
//                        }
//                        job2.schedule();
//                      } catch (PartInitException x) {
//                        x.printStackTrace();
//                      }
//                      refresh(page);
//                      sleep(SLEEP_BETWEEN);
//                      try {
//                        traversal.runAsMarkerFix(marker);
//                      } catch (CoreException x) {
//                        x.printStackTrace();
//                      }
//                      ++changes;
//                      try {
//                        marker.delete();
//                      } catch (CoreException x) {
//                        // TODO Auto-generated catch block
//                        x.printStackTrace();
//                      } // TODO Ori Roth: does not seem to make a 
//                                       // difference
//                                       // actually it removes the markers after the traversal
//                                       // and avoid the infinite loop (it descreases markers.length at
//                                       // each round -- mo
//                      refresh(page);
//                      sleep(SLEEP_BETWEEN);
//                    }
//    
//               monitor.subTask("Done: Commited " + changes + " changes in " + filesModified + " " + English.plurals("file", filesModified));
//               sleep(SLEEP_END);
//               monitor.done();
//               return Status.OK_STATUS;
//          }
//          
//          Boolean sleep(double howMuch) {
//            try {
//              Thread.sleep((int) (1000 * howMuch));
//              return true;
//            } catch (final InterruptedException ¢) {
//              note.bug(¢);
//              return false;
//            }
//          }
//    
//        }; // end job
//    
//    job.schedule();
//    return null;
//    
//    }
//        
////    ICommandService service = (ICommandService) 
////        PlatformUI.getWorkbench().getService(ICommandService.class);
////    Command command = service == null ? null : 
////      service.getCommand("il.org.spartan.SpartanMovie");
////    
////    if(command != null){
////      job.setProperty(IProgressConstants2.COMMAND_PROPERTY, 
////            ParameterizedCommand.generateCommand(command, null));
////      job.setProperty(IProgressConstants2.ICON_PROPERTY,
////          ImageDescriptor.createFromURL(SpartanMovie2.class.getResource("/icons/sample.gif")));
////      job.setProperty(IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY,
////          Boolean.TRUE);
////    }
//     
//  }

  private void printout(final IMarker marker) {
    try {
      System.out.println("Resource: " + marker.getResource().getName() + "; Type: " + marker.getType());
    } catch (CoreException x) {
      x.printStackTrace();
    }
  }

  /** Just in case, so that editors don't pile up. Not sure this is the right
   * behavior
   * <p>
   * Ori Roth says: it just looks better this way. Editors do not pile up and
   * create a mess.
   * @author Yossi Gil
   * @param ¢ JD */
  // sure this is the right behavior
  public static void mightNotBeSlick(final IWorkbenchPage ¢) {
    close(¢);
  }

  private static IMarker[] getMarkers(final IResource $) {
    try {
      return $.findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_ONE);
    } catch (final CoreException m) {
      note.bug(m);
      return new IMarker[0];
    }
  }

  private static List<ICompilationUnit> getCompilationUnits() {
    try {
      return eclipse.compilationUnits(eclipse.currentCompilationUnit(), nullProgressMonitor);
    } catch (final JavaModelException ¢) {
      note.bug(¢);
      return an.empty.list();
    }
  }

  static boolean focus(final IWorkbenchPage p, final IFile f) {
    try {
      IDE.openEditor(p, f, true);
    } catch (final PartInitException ¢) {
      note.bug(¢);
      return false;
    }
    return true;
  }

  static void close(final IWorkbenchPage ¢) {
    ¢.closeAllEditors(true);
  }

  /** The current SpartanMovie is not releaseable. Some big changes should be
   * made.
   * @author Ori Roth
   * @param howMuch
   * @return */
  static boolean sleep(final double howMuch) {
    try {
      Thread.sleep((int) (1000 * howMuch));
      return true;
    } catch (final InterruptedException ¢) {
      note.bug(¢);
      return false;
    }
  }

  static void refresh(final IWorkbenchPage ¢) {
    ¢.getWorkbenchWindow().getShell().update();
    ¢.getWorkbenchWindow().getShell().layout(true);
  }

  static void moveProgressDialog() {
    final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell(), parentShell = shell == null ? null : shell.getParent().getShell();
    if (shell != null && parentShell != null)
      shell.setLocation(parentShell.getBounds().x + parentShell.getBounds().width - shell.getBounds().width, parentShell.getBounds().y);
  }

  /** Finds the first marker in array in terms of textual location. The
   * "CHAR_START" attribute is not something I have added, but an existing and
   * well maintained marker attribute.
   * @author Ori Roth */
  @SuppressWarnings("boxing") static IMarker getFirstMarker(final IMarker[] ms) {
    int $ = 0;
    for (final Integer i : range.from(0).to(ms.length))
      try {
        if (((Integer) ms[i].getAttribute(IMarker.CHAR_START)).intValue() < ((Integer) ms[$].getAttribute(IMarker.CHAR_START)).intValue())
          $ = i;
      } catch (final CoreException ¢) {
        note.bug(¢);
        break;
      }
    return ms[$];
  }
}
