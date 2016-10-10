package il.org.spartan.plugin.revision;

import org.junit.*;

import il.org.spartan.plugin.revision.EventListener.*;

/** A use example. Will be deleted as soon as possible.
 * @author Ori Roth
 * @since 2016 */
public class Example {
  @SuppressWarnings("static-method") @Test public void example() {
    GUIApplicator a = new GUIApplicator();
    // start running
    a.listener.tick(event.visit_project, "MyProject");
    a.listener.tick(event.run_start);
    // visiting stuff
    // pass 1
    a.listener.tick(event.run_pass);
    a.listener.tick(event.visit_cu, "MyCU1");
    a.listener.tick(event.visit_node, "MyNode1");
    a.listener.tick(event.visit_node, "MyNode2");
    a.listener.tick(event.visit_cu, "MyCU2");
    a.listener.tick(event.visit_node, "MyNode3");
    a.listener.tick(event.visit_node, "MyNode4");
    //pass 2
    a.listener.tick(event.run_pass);
    a.listener.tick(event.visit_cu, "MyCU1");
    a.listener.tick(event.visit_node, "MyNode5");
    a.listener.tick(event.visit_node, "MyNode6");
    // finish running
    a.listener.tick(event.run_finish);
  }
}
