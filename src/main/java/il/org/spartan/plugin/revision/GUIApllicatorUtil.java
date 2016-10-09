package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.revision.EventListener.*;
import il.org.spartan.plugin.revision.EventMapper.*;

/** @author Ori Roth
 * @since 2016 */
public class GUIApllicatorUtil {
  @SuppressWarnings("rawtypes") public static Listener basicGUIListener() {
    return EventMapper.empty() //
        .expend( //
            EventMapperFunctor.recorderOf(event.visit_cu) //
                .collectBy(ICompilationUnit.class)) //
        .expend( //
            EventMapperFunctor.recorderOf(event.visit_node) //
                .collectBy(ASTNode.class)) //
        .expend( //
            EventMapperFunctor.recorderOf(event.visit_project) //
                .remember(IJavaProject.class)) //
        .expend( //
            EventMapperFunctor.recorderOf(event.run_pass) //
                .counter()) //
        .expend(new EventFunctor(event.run_start) {
          @Override void update(final Map ¢) {
            // TODO Roth: open a dialog box etc etc...
            System.out.println("Spartanizing " + ¢.get(event.visit_project));
          }
        }).expend(new EventFunctor(event.run_finish) {
          @Override void update(final Map ¢) {
            // TODO Roth: open a dialog box etc etc...
            System.out.println("Done spartanizing " + ¢.get(event.visit_project));
            System.out.println("Spartanized " + ¢.get(event.visit_project) //
                + " with " + ((Collection) ¢.get(event.visit_cu)).size() + " files" //
                + " in " + ¢.get(event.run_pass) + " passes ");
          }
        });
  }
}
