package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0312 {
  /** Introduced by Yogi on Tue-Apr-11-22:59:10-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void forInta1a7Ifa5b9Returncd15ReturncReturnc() {
    trimminKof("for (int a = 1; a < 7;) { if (a == 5) { b += 9; return c; } d += 15; return c; } return c;") //
        .using(new ForFiniteConvertReturnToBreak(), ForStatement.class) //
        .gives("for(int a=1;a<7;){if(a==5){b+=9;break;}d+=15;return c;}return c;") //
        .using(new ForFiniteConvertReturnToBreak(), ForStatement.class) //
        .gives("for(int a=1;a<7;){if(a==5){b+=9;break;}d+=15;break;}return c;") //
        .using(new IfStatementBlockSequencerBlockSameSequencer(), IfStatement.class) //
        .gives("for(int a=1;a<7;){if(a==5){b+=9;}else{d+=15;}break;}return c;") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("for(int a=1;a<7;){if(a==5)b+=9;else d+=15;break;}return c;") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Fri-Mar-31-00:30:47-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void forInta1a7Ifa5b9Breakc15BreakReturnd() {
    trimminKof("for (int a = 1; a < 7;) { if (a == 5) { b += 9; break; } c += 15; break; } return d;") //
        .using(new IfStatementBlockSequencerBlockSameSequencer(), IfStatement.class) //
        .gives("for(int a=1;a<7;){if(a==5){b+=9;}else{c+=15;}break;}return d;") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("for(int a=1;a<7;){if(a==5)b+=9;else c+=15;break;}return d;") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Fri-Mar-31-00:39:20-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_forInta1a7Ifa5b9Breakc15BreakReturnd() {
    trimminKof("for (int a = 1; a < 7;) { if (a == 5) { b += 9; break; } c += 15; break; } return d;") //
        .using(new IfStatementBlockSequencerBlockSameSequencer(), IfStatement.class) //
        .gives("for(int a=1;a<7;){if(a==5){b+=9;}else{c+=15;}break;}return d;") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("for(int a=1;a<7;){if(a==5)b+=9;else c+=15;break;}return d;") //
        .stays() //
    ;
  }

  @Test public void bugInLastIfInMethod1() {
    trimminKof("       @Override public void f() {\n          if (!isMessageSuppressed(message)) {\n"
        + "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n            messages.add(message);\n"
        + "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n            if (listener != null)\n"
        + "              listener.listLocalMessagesAddMessages(account, null, messages);\n          }\n        }")//
            .gives(
                "@Override public void f(){if(isMessageSuppressed(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void chocolate1() {
    trimminKof("for(int $=0;$<a.length;++$)sum +=$;")//
        .stays();
  }

  @Test public void chocolate2() {
    trimminKof("for(int i=0, j=0;i<a.length;++j)sum +=i+j;")//
        .gives("for(int ¢=0, j=0;¢<a.length;++j)sum +=¢+j;")//
        .stays();
  }

  @Test public void issue54ForPlain() {
    trimminKof("int a  = f(); for (int i = 0; i <100;  ++i) b[i] = a;")//
        .gives("for (int i = 0; i <100;  ++i) b[i] = f();")//
        .gives("for (int ¢ = 0; ¢ <100;  ++¢) b[¢] = f();")//
        .stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopInitializer() {
    trimminKof("for (int s = i++; i <10; ++s) sum+=s;")//
        .gives("for (int ¢ = i++; i <10; ++¢) sum+=¢;")//
        .stays();
  }

  @Test public void t18() {
    trimminKof("while(b==q){int i;double tipper; x=tipper+i;}")//
        .stays();
  }
}
