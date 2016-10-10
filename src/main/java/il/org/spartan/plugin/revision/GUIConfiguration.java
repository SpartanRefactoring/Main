package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.revision.EventListener.*;

/** @author Ori Roth
 * @since 2016 */
public enum GUIConfiguration {
  listener;
  void configure(final Object ¢) {
    if (listener.equals(this))
      configure((EventMapper) ¢);
  }

  @SuppressWarnings("rawtypes") static void configure(final EventMapper l) {
    l //
        .expend(EventMapper.recorderOf(event.visit_cu).rememberBy(ICompilationUnit.class)) //
        .expend(EventMapper.recorderOf(event.visit_node).rememberBy(ASTNode.class)) //
        .expend(EventMapper.recorderOf(event.visit_project).rememberLast(IJavaProject.class)) //
        .expend(EventMapper.recorderOf(event.run_pass).counter()) //
        .expend(EventMapper.inspectorOf(event.run_start).does(¢ -> {
          // TODO Roth: open a dialog box etc etc...
          System.out.println("Spartanizing " + ¢.get(event.visit_project));
        })) //
        .expend(EventMapper.inspectorOf(event.run_finish).does(¢ -> {
          // TODO Roth: open a dialog box etc etc...
          System.out.println("Done spartanizing " + ¢.get(event.visit_project));
          System.out.println("Spartanized " + ¢.get(event.visit_project) //
              + " with " + ((Collection) ¢.get(event.visit_cu)).size() + " files" //
              + " in " + ¢.get(event.run_pass) + " passes");
        }));
  }
}
