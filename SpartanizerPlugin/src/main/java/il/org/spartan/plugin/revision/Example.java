package il.org.spartan.plugin.revision;

import org.junit.*;

import il.org.spartan.plugin.revision.GUIApplicator.*;

/** A use example. Will be deleted as soon as possible.
 * @author Ori Roth
 * @since 2016 */
public class Example {
  @SuppressWarnings("static-method") @Test public void example() {
    System.out.println("############\nExample 1:");
    final GUIApplicator a = new GUIApplicator();
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
    // pass 2
    a.listener.tick(event.run_pass);
    a.listener.tick(event.visit_cu, "MyCU1");
    a.listener.tick(event.visit_node, "MyNode5");
    a.listener.tick(event.visit_node, "MyNode6");
    // finish running
    a.listener.tick(event.run_finish);
  }

  // a replacement for Listener.Tracing...
  enum traceOperation {
    trace, print
  }

  @Test public void exampleTracing() {
    System.out.println("############\nExample 2:");
    class Container {
      int index;
      StringBuilder builder;
    }
    final Listener listener = EventMapper.empty(traceOperation.class) //
        .expend( //
            EventMapper.recorderOf(traceOperation.trace) //
                .startWithSupplyOf(() -> {
                  final Container $ = new Container();
                  $.index = 0;
                  $.builder = new StringBuilder();
                  return $;
                }) //
                .gets(Object[].class) //
                .does((c, os) -> {
                  c.builder.append(++c.index + ": ");
                  for (final Object ¢ : os)
                    c.builder.append("," + ((¢ + "").length() < 36 ? ¢ + "" : (¢ + "").substring(1, 35)));
                  c.builder.append('\n');
                })) //
        .expend(EventMapper.inspectorOf(traceOperation.print) //
            .does(¢ -> {
              final Container c = (Container) ¢.get(traceOperation.trace);
              if (c != null)
                System.out.println(c.builder); // or whatever you want
            }));
    // Pay attention to wrapper functions below. No for the testing:
    trace(listener, "Project", "MyProject");
    trace(listener, "CU", "MyCU1");
    trace(listener, "CU", "MyCU2");
    print(listener);
  }

  public static void trace(final Listener l, final Object... os) {
    l.tick(traceOperation.trace, os);
  }

  public static void print(final Listener ¢) {
    ¢.tick(traceOperation.print);
  }
}
