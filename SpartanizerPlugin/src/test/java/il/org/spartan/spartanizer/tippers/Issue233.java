package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) //

public class Issue233 {
  @Test public void bugInLastIfInMethod1() {
    trimmingOf("        @Override public void f() {\n" + "          if (!isMessageSuppressed(message)) {\n"
        + "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + "            messages.add(message);\n"
        + "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + "            if (listener != null)\n"
        + "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + "          }\n" + "        }")//
            .gives(
                "@Override public void f(){if(isMessageSuppressed(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void issue54ForPlain() {
    trimmingOf("int a  = f(); for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .gives("for (int i = 0; i < 100;  ++i) b[i] = f();")//
        .gives("for (int i = 0; i < 100;  b[i] = f(), ++i);")//
        .stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopInitializer() {
    trimmingOf("for (int s = i++; i < 10; ++s) sum+=s;")//
        .gives("for (int s = i++; i < 10; ++s,sum+=s);")//
        .stays();
  }
}
