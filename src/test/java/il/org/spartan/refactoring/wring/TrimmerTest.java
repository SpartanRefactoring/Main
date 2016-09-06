package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.spartanizations.GuessedContext.*;
import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.ast.step.*;
import static il.org.spartan.refactoring.ast.hop.*;
import static il.org.spartan.refactoring.ast.extract.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.ExpressionComparator.*;
import static il.org.spartan.refactoring.engine.into.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.ExpressionComparator.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.engine.*;
import static il.org.spartan.refactoring.ast.step.*;
import static il.org.spartan.refactoring.ast.hop.*;
import static il.org.spartan.refactoring.ast.extract.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) public class TrimmerTest {
  /** END OF STABLING TESTS */
  @Test public void actualExampleForSortAddition() {
    trimming.of("1 + b.statements().indexOf(declarationStmt)").stays();
  }

  @Test public void actualExampleForSortAdditionInContext() {
    final String from = "2 + a < b";
    final String expected = "a + 2 < b";
    final GuessedContext w = GuessedContext.expression_or_something_that_may_be_passed_as_argument;
    final String wrap = w.on(from);
    azzert.that(from, is(w.off(wrap)));
    final Trimmer t = new Trimmer();
    final String unpeeled = apply(t, wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + from);
    final String peeled = w.off(unpeeled);
    if (peeled.equals(from))
      azzert.that("No similification of " + from, from, is(not(peeled)));
    final String compressSpaces = tide.clean(peeled);
    final String compressSpaces2 = tide.clean(from);
    azzert.that("Simpification of " + from + " is just reformatting", compressSpaces2, not(compressSpaces));
    assertSimilar(w, expected, peeled);
  }

  @Test public void andWithCLASS_CONSTANT() {
    trimming.of("(x >> 18) & MASK_BITS").stays();
    trimming.of("(x >> 18) & MASK_6BITS").stays();
  }

  @Test public void assignmentAssignmentChain1() {
    trimming.of("c = a = 13; b = 13;").to("b = c = a = 13;");
  }

  @Test public void assignmentAssignmentChain2() {
    trimming.of("a = 13; b= c = 13;").to("b = c = a = 13;");
  }

  @Test public void assignmentAssignmentChain3() {
    trimming.of("a = b = 13; c = d = 13;").to("c = d = a = b = 13;");
  }

  @Test public void assignmentAssignmentChain4() {
    trimming.of("a1 = a2 = a3 = a4 = 13; b1 = b2 = b3 = b4 = b5 = 13;")//
        .to("b1 = b2 = b3 = b4 = b5 = a1 = a2 = a3 = a4 = 13;");
  }

  @Test public void assignmentAssignmentChain5() {
    trimming.of("a1 = (a2 = (a3 = (a4 = 13))); b1 = b2 = b3 = ((((b4 = (b5 = 13)))));")//
        .to("b1=b2=b3=((((b4=(b5=a1=(a2=(a3=(a4=13))))))));");
  }

  @Test public void assignmentAssignmentNew() {
    trimming.of("a = new B(); b= new B();").stays();
  }

  @Test public void assignmentAssignmentNewArray() {
    trimming.of("a = new A[3]; b= new A[3];").stays();
  }

  @Test public void assignmentAssignmentNull() {
    trimming.of("c = a = null; b = null;").stays();
  }

  @Test public void assignmentAssignmentSideEffect() {
    trimming.of("a = f(); b= f();").stays();
  }

  @Test public void assignmentAssignmentVanilla() {
    trimming.of("a = 13; b= 13;").to("b = a = 13;");
  }

  @Test public void assignmentAssignmentVanilla0() {
    trimming.of("a = 0; b = 0;").to("b = a = 0;");
  }

  @Test public void assignmentAssignmentVanillaScopeIncludes() {
    included("a = 3; b = 3;", Assignment.class).in(new AssignmentAndAssignment());
  }

  @Test public void assignmentAssignmentVanillaScopeIncludesNull() {
    included("a = null; b = null;", Assignment.class).notIn(new AssignmentAndAssignment());
  }

  @Test public void assignmentReturn0() {
    trimming.of("a = 3; return a;").to("return a = 3;");
  }

  @Test public void assignmentReturn1() {
    trimming.of("a = 3; return (a);").to("return a = 3;");
  }

  @Test public void assignmentReturn2() {
    trimming.of("a += 3; return a;").to("return a += 3;");
  }

  @Test public void assignmentReturn3() {
    trimming.of("a *= 3; return a;").to("return a *= 3;");
  }

  @Test public void assignmentReturniNo() {
    trimming.of("b = a = 3; return a;").stays();
  }

  @Test public void blockSimplifyVanilla() {
    trimming.of("if (a) {f(); }").to("if (a) f();");
  }

  @Test public void blockSimplifyVanillaSimplified() {
    trimming.of(" {f(); }").to("f();");
  }

  @Test public void bugInLastIfInMethod() {
    trimming
        .of("" + //
            "        @Override public void messageFinished(final LocalMessage myMessage, final int number, final int ofTotal) {\n" + //
            "          if (!isMessageSuppressed(myMessage)) {\n" + //
            "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
            "            messages.add(myMessage);\n" + //
            "            stats.unreadMessageCount += myMessage.isSet(Flag.SEEN) ? 0 : 1;\n" + //
            "            stats.flaggedMessageCount += myMessage.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
            "            if (listener != null)\n" + //
            "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
            "          }\n" + //
            "        }")
        .to("@Override public void messageFinished(final LocalMessage myMessage,final int number,final int ofTotal){if(isMessageSuppressed(myMessage))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(myMessage);stats.unreadMessageCount+=myMessage.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=myMessage.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod1() {
    trimming
        .of("" + //
            "        @Override public void f() {\n" + //
            "          if (!isMessageSuppressed(message)) {\n" + //
            "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
            "            messages.add(message);\n" + //
            "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
            "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
            "            if (listener != null)\n" + //
            "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
            "          }\n" + //
            "        }")
        .to("@Override public void f(){if(isMessageSuppressed(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod2() {
    trimming
        .of("" + //
            "        public void f() {\n" + //
            "          if (!g(message)) {\n" + //
            "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
            "            messages.add(message);\n" + //
            "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
            "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
            "            if (listener != null)\n" + //
            "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
            "          }\n" + //
            "        }")
        .to("public void f(){if(g(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod3() {
    trimming
        .of("" + //
            "        public void f() {\n" + //
            "          if (!g(a)) {\n" + //
            "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
            "            messages.add(message);\n" + //
            "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
            "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
            "            if (listener != null)\n" + //
            "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
            "          }\n" + //
            "        }")
        .to("public void f(){if(g(a))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod4() {
    trimming
        .of("" + //
            "        public void f() {\n" + //
            "          if (!g) {\n" + //
            "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
            "            messages.add(message);\n" + //
            "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
            "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
            "            if (listener != null)\n" + //
            "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
            "          }\n" + //
            "        }")
        .to("public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod5() {
    trimming
        .of("" + //
            "        public void f() {\n" + //
            "          if (!g) {\n" + //
            "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
            "            messages.add(message);\n" + //
            "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
            "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
            "          }\n" + //
            "        }")
        .to("public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void bugInLastIfInMethod6() {
    trimming
        .of("" + //
            "        public void f() {\n" + //
            "          if (!g) {\n" + //
            "            final int messages = 3;\n" + //
            "            messages.add(message);\n" + //
            "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
            "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
            "          }\n" + //
            "        }")
        .to("public void f(){if(g)return;final int messages=3;messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void bugInLastIfInMethod7() {
    trimming.of("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            foo();\n" + //
        "            bar();\n" + //
        "          }\n" + //
        "        }").to("public void f(){if(g)return;foo();bar();}");
  }

  @Test public void bugInLastIfInMethod8() {
    trimming.of("" + //
        "        public void f() {\n" + //
        "          if (g) {\n" + //
        "            foo();\n" + //
        "            bar();\n" + //
        "          }\n" + //
        "        }").to("public void f(){if(!g)return;foo();bar();}");
  }

  @Test public void bugIntroducingMISSINGWord1() {
    trimming.of("b.f(a) && -1 == As.g(f).h(c) ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("b.f(a) && As.g(f).h(c) == -1 ? o(s,b,g(f)) : b.f(\".in\") && !y(d,b)? o(b.z(u,v),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord1a() {
    trimming.of("-1 == As.g(f).h(c)").to("As.g(f).h(c)==-1");
  }

  @Test public void bugIntroducingMISSINGWord1b() {
    trimming.of("b.f(a) && X ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("b.f(a)&&X?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1c() {
    trimming.of("Y ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("Y?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1d() {
    trimming.of("Y ? Z : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)").to("Y?Z:b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1e() {
    trimming.of("Y ? Z : R ? null : S ? null : T").to("Y?Z:!R&&!S?T:null");
  }

  @Test public void bugIntroducingMISSINGWord2() {
    trimming
        .of("name.endsWith(testSuffix) &&  MakeAST.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2a() {
    trimming
        .of("name.endsWith(testSuffix) &&  MakeAST.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2b() {
    trimming
        .of("name.endsWith(testSuffix) &&  T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("name.endsWith(testSuffix) && T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2c() {
    trimming
        .of("X && T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X && T ? objects(s,name,makeInFile(f)) : name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2d() {
    trimming.of("X && T ? E : Y ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X && T ? E : !Y && !dotOutExists(d,name) ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e() {
    trimming.of("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e1() {
    trimming.of("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(x, Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(x,Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e2() {
    trimming.of("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(g, Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(g,Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2f() {
    trimming.of("X &&  T ? E : Y ? null : Z ? null : F").to("X&&T?E:!Y&&!Z?F:null");
  }

  @Test public void bugIntroducingMISSINGWord3() {
    trimming
        .of("name.endsWith(testSuffix) && -1 == MakeAST.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
        .to("name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord3a() {
    trimming.of("!name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
        .to("name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWordTry1() {
    trimming
        .of("name.endsWith(testSuffix) && -1 == MakeAST.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("name.endsWith(testSuffix) && MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWordTry2() {
    trimming.of("!(intent.getBooleanExtra(EXTRA_FROM_SHORTCUT, false) && !K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName()))")
        .to("!intent.getBooleanExtra(EXTRA_FROM_SHORTCUT,false)||K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName())");
  }

  @Test public void bugIntroducingMISSINGWordTry3() {
    trimming.of("!(f.g(X, false) && !a.b.e(m.h()))").to("!f.g(X,false)||a.b.e(m.h())");
  }

  @Test public void bugOfMissingTry() {
    trimming.of("!(A && B && C && true && D)").to("!A||!B||!C||false||!D");
  }

  @Test public void canonicalFragementExamples() {
    trimming.of("int a; a = 3;").to("int a = 3;");
    trimming.of("int a = 2; if (b) a = 3; ").to("int a = b ? 3 : 2;");
    trimming.of("int a = 2; a += 3; ").to("int a = 2 + 3;");
    trimming.of("int a = 2; a = 3 * a; ").to("int a = 3 * 2;");
    trimming.of("int a = 2; return 3 * a; ").to("return 3 * 2;");
    trimming.of("int a = 2; return a; ").to("return 2;");
  }

  @Test public void canonicalFragementExamplesWithExraFragments() {
    trimming.of("int a = 2; a = 3 * a * b; ").to("int a = 3 * 2 * b;");
    trimming.of("int a = 2; a = 3 * a; ").to("int a = 3 * 2;");
    trimming.of("int a = 2; a += 3; ").to("int a = 2 + 3;");
    trimming.of("int a = 2; a += b; ").to("int a = 2 + b;");
    trimming.of("int a = 2, b = 11; a = 3 * a * b; ")//
        .to("int a=2;a=3*a*11;")//
        .to("int a=3*2*11;")//
        .to("int a=2*3* 11;");
    trimming.of("int a = 2, b=1; a += b; ").to("int a=2;a+=1;").to("int a=2+1;");
    trimming.of("int a = 2,b=1; if (b) a = 3; ").to("int a=2;if(1)a=3;").to("int a=1?3:2;");
    trimming.of("int a = 2, b = 1; return a + 3 * b; ").to("int b=1;return 2+3*b;");
    trimming.of("int a =2,b=2; if (x) a = 2*a;").to("int a=x?2*2:2, b=2;");
    trimming.of("int a = 2, b; a = 3 * a * b; ").stays();
    trimming.of("int a = 2, b; a += b; ").stays();
    trimming.of("int a =2,b; if (x) a = 2*a;").to("int a=x?2*2:2, b;");
    trimming.of("int a = 2, b; return a + 3 * b; ").to("return 2 + 3*b;");
    trimming.of("int a =2; if (x) a = 3*a;").to("int a=x?3*2:2;");
    trimming.of("int a = 2; return 3 * a * a; ").to("return 3 * 2 * 2;");
    trimming.of("int a = 2; return 3 * a * b; ").to("return 3 * 2 * b;");
    trimming.of("int a = 2; return a; ").to("return 2;");
    trimming.of("int a,b=2; a = b;").to("int a;a=2;").to("int a=2;");
    trimming.of("int a,b; a = 3;").to("int a = 3, b;");
    trimming.of("int a; if (x) a = 3; else a++;").to("int a;if(x)a=3;else++a;");
    trimming.of("int b=5,a = 2,c=4; return 3 * a * b * c; ").to("int a=2,c=4;return 3*a*5*c;");
    trimming.of("int b=5,a = 2,c; return 3 * a * b * c; ").to("int a = 2; return 3 * a * 5 * c;");
  }

  @Test public void canonicalFragementExamplesWithExraFragmentsX() {
    trimming.of("int a; if (x) a = 3; else a++;").to("int a;if(x)a=3;else++a;");
  }

  @Test public void chainComparison() {
    final InfixExpression e = i("a == true == b == c");
    azzert.that(right(e).toString(), is("c"));
    trimming.of("a == true == b == c").to("a == b == c");
  }

  @Test public void chainCOmparisonTrueLast() {
    trimming.of("a == b == c == true").to("a == b == c");
  }

  @Test public void comaprisonWithBoolean1() {
    trimming.of("s.equals(532)==true").to("s.equals(532)");
  }

  @Test public void comaprisonWithBoolean2() {
    trimming.of("s.equals(532)==false ").to("!s.equals(532)");
  }

  @Test public void comaprisonWithBoolean3() {
    trimming.of("(false==s.equals(532))").to("(!s.equals(532))");
  }

  @Test public void comaprisonWithSpecific0() {
    trimming.of("this != a").to("a != this");
  }

  @Test public void comaprisonWithSpecific0Legibiliy00() {
    final InfixExpression e = i("this != a");
    assert in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS);
    assert !iz.booleanLiteral(step.right(e));
    assert !iz.booleanLiteral(step.left(e));
    assert in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS);
  }

  @Test public void comaprisonWithSpecific1() {
    trimming.of("null != a").to("a != null");
  }

  @Test public void comaprisonWithSpecific2() {
    trimming.of("null != a").to("a != null");
    trimming.of("this == a").to("a == this");
    trimming.of("null == a").to("a == null");
    trimming.of("this >= a").to("a <= this");
    trimming.of("null >= a").to("a <= null");
    trimming.of("this <= a").to("a >= this");
    trimming.of("null <= a").to("a >= null");
  }

  @Test public void comaprisonWithSpecific2a() {
    trimming.of("s.equals(532)==false").to("!s.equals(532)");
  }

  @Test public void comaprisonWithSpecific3() {
    trimming.of("(this==s.equals(532))").to("(s.equals(532)==this)");
  }

  @Test public void comaprisonWithSpecific4() {
    trimming.of("(0 < a)").to("(a>0)");
  }

  @Test public void comaprisonWithSpecificInParenthesis() {
    trimming.of("(null==a)").to("(a==null)");
  }

  @Test public void commonPrefixEntirelyIfBranches() {
    trimming.of("if (s.equals(532)) S.out.close();else S.out.close();").to("S.out.close(); ");
  }

  @Test public void commonPrefixIfBranchesInFor() {
    trimming.of("for (;;) if (a) {i++;j++;j++;} else { i++;j++; i++;}").to("for(;;){i++;j++;if(a)j++;else i++;}");
  }

  @Test public void commonSuffixIfBranches() {
    trimming.of("if (a) { \n" + //
        "++i;\n" + //
        "f();\n" + //
        "} else {\n" + //
        "++j;\n" + //
        "f();\n" + //
        "}").to("if (a)  \n" + //
            "++i;\n" + //
            "else \n" + //
            "++j;\n" + //
            "\n" + //
            "f();");//
  }

  @Test public void commonSuffixIfBranchesDisappearingElse() {
    trimming.of("if (a) { \n" + //
        "++i;\n" + //
        "f();\n" + //
        "} else {\n" + //
        "f();\n" + //
        "}").to("if (a)  \n" + //
            "++i;\n" + //
            "\n" + //
            "f();");//
  }

  @Test public void commonSuffixIfBranchesDisappearingThen() {
    trimming.of("if (a) { \n" + //
        "f();\n" + //
        "} else {\n" + //
        "++j;\n" + //
        "f();\n" + //
        "}").to("if (!a)  \n" + //
            "++j;\n" + //
            "\n" + //
            "f();");//
  }

  @Test public void commonSuffixIfBranchesDisappearingThenWithinIf() {
    trimming.of("if (x)  if (a) { \n" + //
        "f();\n" + //
        "} else {\n" + //
        "++j;\n" + //
        "f();\n" + //
        "} else { h(); ++i; ++j; ++k; if (a) f(); else g(); }").to("if (x) { if (!a)  \n" + //
            "++j;\n" + //
            "\n" + //
            "f(); } else { h(); ++i; ++j; ++k;  if (a) f(); else g();}");//
  }

  @Test public void compareWithBoolean00() {
    trimming.of("a == true").to("a");
  }

  @Test public void compareWithBoolean01() {
    trimming.of("a == false").to("!a");
  }

  @Test public void compareWithBoolean10() {
    trimming.of("true == a").to("a");
  }

  @Test public void compareWithBoolean100() {
    trimming.of("a != true").to("!a");
  }

  @Test public void compareWithBoolean100a() {
    trimming.of("(((a))) != true").to("!a");
  }

  @Test public void compareWithBoolean101() {
    trimming.of("a != false").to("a");
  }

  @Test public void compareWithBoolean11() {
    trimming.of("false == a").to("!a");
  }

  @Test public void compareWithBoolean110() {
    trimming.of("true != a").to("!a");
  }

  @Test public void compareWithBoolean111() {
    trimming.of("false != a").to("a");
  }

  @Test public void compareWithBoolean2() {
    trimming.of("false != false").to("false");
  }

  @Test public void compareWithBoolean3() {
    trimming.of("false != true").to("true");
  }

  @Test public void compareWithBoolean4() {
    trimming.of("false == false").to("true");
  }

  @Test public void compareWithBoolean5() {
    trimming.of("false == true").to("false");
  }

  @Test public void compareWithBoolean6() {
    trimming.of("false != false").to("false");
  }

  @Test public void compareWithBoolean7() {
    trimming.of("true != true").to("false");
  }

  @Test public void compareWithBoolean8() {
    trimming.of("true != false").to("true");
  }

  @Test public void compareWithBoolean9() {
    trimming.of("true != true").to("false");
  }

  @Test public void comparison01() {
    trimming.of("1+2+3<3").stays();
  }

  @Test public void comparison02() {
    trimming.of("f(2)<a").stays();
  }

  @Test public void comparison03() {
    trimming.of("this==null").stays();
  }

  @Test public void comparison04() {
    trimming.of("6-7<2+1").to("6-7<1+2");
  }

  @Test public void comparison05() {
    trimming.of("a==11").stays();
  }

  @Test public void comparison06() {
    trimming.of("1<102333").stays();
  }

  @Test public void comparison08() {
    trimming.of("a==this").stays();
  }

  @Test public void comparison09() {
    trimming.of("1+2<3&7+4>2+1").to("1+2<3&4+7>1+2");
  }

  @Test public void comparison10() {
    trimming.of("1+2+3<3-4").stays();
  }

  @Test public void comparison11() {
    trimming.of("12==this").to("this==12");
  }

  @Test public void comparison12() {
    trimming.of("1+2<3&7+4>2+1||6-7<2+1").to("1+2<3&4+7>1+2||6-7<1+2");
  }

  @Test public void comparison13() {
    trimming.of("13455643294<22").stays();
  }

  @Test public void comparisonWithCharacterConstant() {
    trimming.of("'d' == s.charAt(i)").to("s.charAt(i)=='d'");
  }

  @Test public void compreaeExpressionToExpression() {
    trimming.of("6 - 7 < 2 + 1   ").to("6 -7 < 1 + 2");
  }

  @Test public void correctSubstitutionInIfAssignment() {
    trimming.of("int a = 2+3; if (a+b > a << b) a =(((((a *7 << a)))));")//
        .to("int a=2+3+b>2+3<<b?(2+3)*7<<2+3:2+3;");
  }

  @Test public void declarationAssignmentUpdateWithIncrement() {
    trimming.of("int a=0; a+=++a;").stays();
  }

  @Test public void declarationAssignmentUpdateWithPostIncrement() {
    trimming.of("int a=0; a+=a++;").stays();
  }

  @Test public void declarationAssignmentWithIncrement() {
    trimming.of("int a=0; a=++a;").stays();
  }

  @Test public void declarationAssignmentWithPostIncrement() {
    trimming.of("int a=0; a=a++;").stays();
  }

  @Test public void declarationIfAssignment() {
    trimming.of("" + //
        "    String res = s;\n" + //
        "    if (s.equals(y))\n" + //
        "      res = s + blah;\n" + //
        "    S.out.println(res);").to("" + //
            "    String res = s.equals(y) ? s + blah :s;\n" + //
            "    S.out.println(res);");
  }

  @Test public void declarationIfAssignment3() {
    trimming.of("int a =2; if (a != 2) a = 3;").to("int a = 2 != 2 ? 3 : 2;");
  }

  @Test public void declarationIfAssignment4() {
    trimming.of("int a =2; if (x) a = 2*a;").to("int a = x ? 2*2: 2;");
  }

  @Test public void declarationIfUpdateAssignment() {
    trimming.of("" + //
        "    String res = s;\n" + //
        "    if (s.equals(y))\n" + //
        "      res += s + blah;\n" + //
        "    S.out.println(res);").to("" + //
            "    String res = s.equals(y) ? s + s + blah :s;\n" + //
            "    S.out.println(res);");
  }

  @Test public void declarationIfUsesLaterVariable() {
    trimming.of("int a=0, b=0;if (b==3)   a=4;")//
        .to(" int a=0;if(0==3)a=4;") //
        .to(" int a=0==3?4:0;");
  }

  @Test public void declarationIfUsesLaterVariable1() {
    trimming.of("int a=0, b=0;if (b==3)   a=4; f();").stays();
  }

  @Test public void declarationInitializeRightShift() {
    trimming.of("int a = 3;a>>=2;").to("int a = 3 >> 2;");
  }

  @Test public void declarationInitializerReturnAssignment() {
    trimming.of("int a = 3; return a = 2 * a;").to("return 2 * 3;");
  }

  @Test public void declarationInitializerReturnExpression() {
    trimming.of("" //
        + "String t = Bob + Wants + To + \"Sleep \"; "//
        + "  return (right_now + t);    ").to("return(right_now+Bob+Wants+To+\"Sleep \");");
  }

  @Test public void declarationInitializesRotate() {
    trimming.of("int a = 3;a>>>=2;").to("int a = 3 >>> 2;");
  }

  @Test public void declarationInitializeUpdateAnd() {
    trimming.of("int a = 3;a&=2;").to("int a = 3 & 2;");
  }

  @Test public void declarationInitializeUpdateAssignment() {
    trimming.of("int a = 3;a += 2;").to("int a = 3+2;");
  }

  @Test public void declarationInitializeUpdateAssignmentFunctionCallWithReuse() {
    trimming.of("int a = f();a += 2*f();").to("int a=f()+2*f();");
  }

  @Test public void declarationInitializeUpdateAssignmentFunctionCallWIthReuse() {
    trimming.of("int a = x;a += a + 2*f();").to("int a=x+x+2*f();");
  }

  @Test public void declarationInitializeUpdateAssignmentIncrement() {
    trimming.of("int a = ++i;a += j;").to("int a = ++i + j;");
  }

  @Test public void declarationInitializeUpdateAssignmentIncrementTwice() {
    trimming.of("int a = ++i;a += a + j;").stays();
  }

  @Test public void declarationInitializeUpdateAssignmentWithReuse() {
    trimming.of("int a = 3;a += 2*a;").to("int a = 3+2*3;");
  }

  @Test public void declarationInitializeUpdateDividies() {
    trimming.of("int a = 3;a/=2;").to("int a = 3 / 2;");
  }

  @Test public void declarationInitializeUpdateLeftShift() {
    trimming.of("int a = 3;a<<=2;").to("int a = 3 << 2;");
  }

  @Test public void declarationInitializeUpdateMinus() {
    trimming.of("int a = 3;a-=2;").to("int a = 3 - 2;");
  }

  @Test public void declarationInitializeUpdateModulo() {
    trimming.of("int a = 3;a%= 2;").to("int a = 3 % 2;");
  }

  @Test public void declarationInitializeUpdatePlus() {
    trimming.of("int a = 3;a+=2;").to("int a = 3 + 2;");
  }

  @Test public void declarationInitializeUpdateTimes() {
    trimming.of("int a = 3;a*=2;").to("int a = 3 * 2;");
  }

  @Test public void declarationInitializeUpdateXor() {
    trimming.of("int a = 3;a^=2;").to("int a = 3 ^ 2;");
  }

  @Test public void declarationInitializeUpdatOr() {
    trimming.of("int a = 3;a|=2;").to("int a = 3 | 2;");
  }

  @Test public void declarationUpdateReturn() {
    trimming.of("int a = 3; return a += 2;").to("return 3 + 2;");
  }

  @Test public void declarationUpdateReturnNone() {
    trimming.of("int a = f(); return a += 2 * a;").stays();
  }

  @Test public void declarationUpdateReturnTwice() {
    trimming.of("int a = 3; return a += 2 * a;").to("return 3 + 2 *3 ;");
  }

  @Test public void delcartionIfAssignmentNotPlain() {
    trimming.of("int a=0;   if (y) a+=3; ").to("int a = y ? 0 + 3 : 0;");
  }

  @Test public void doNotConsolidateNewArrayActual() {
    trimming.of("" + //
        "occupied = new boolean[capacity];\n" + //
        "placeholder = new boolean[capacity];").stays();
  }

  @Test public void doNotConsolidateNewArraySimplifiedl() {
    trimming.of("" + //
        "a = new int[1];\n" + //
        "b = new int[1];").stays();
  }

  @Test public void doNotConsolidatePlainNew() {
    trimming.of("" + //
        "a = new A();\n" + //
        "b = new B();").stays();
  }

  @Ignore("bug") public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimming.of("" + //
        "    @SuppressWarnings() int $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }").stays();
  }

  @Test public void doNotInlineWithDeclaration() {
    trimming.of("  private Class<? extends T> retrieveClazz() throws ClassNotFoundException {\n" + //
        "    nonnull(className);\n" + //
        "    @SuppressWarnings(\"unchecked\") final Class<T> $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }").stays();
  }

  @Test public void doNotIntroduceDoubleNegation() {
    trimming.of("!Y ? null :!Z ? null : F").to("Y&&Z?F:null");
  }

  @Test public void donotSorMixedTypes() {
    trimming.of("if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;").stays();
  }

  @Test public void dontELiminateCatchBlock() {
    trimming.of("try { f(); } catch (Exception e) { } finally {}").stays();
  }

  @Test public void dontELiminateSwitch() {
    trimming.of("switch (a) { default: }").stays();
  }

  @Test public void dontSimplifyCatchBlock() {
    trimming.of("try { {} ; {} } catch (Exception e) {{} ; {}  } finally {{} ; {}}")//
        .to(" try {}          catch (Exception e) {}          finally {}");
  }

  @Test public void duplicatePartialIfBranches() {
    trimming.of("" + //
        "    if (a) {\n" + //
        "      f();\n" + //
        "      g();\n" + //
        "      ++i;\n" + //
        "    } else {\n" + //
        "      f();\n" + //
        "      g();\n" + //
        "      --i;\n" + //
        "    }").to("" + // //
            "   f();\n" + //
            "   g();\n" + //
            "    if (a) \n" + //
            "      ++i;\n" + //
            "    else \n" + //
            "      --i;");
  }

  @Test public void emptyElse() {
    trimming.of("if (x) b = 3; else ;").to("if (x) b = 3;");
  }

  @Test public void emptyElseBlock() {
    trimming.of("if (x) b = 3; else { ;}").to("if (x) b = 3;");
  }

  @Test public void emptyIsNotChangedExpression() {
    trimming.of("").stays();
  }

  @Test public void emptyIsNotChangedStatement() {
    trimming.of("").stays();
  }

  @Test public void emptyThen1() {
    trimming.of("if (b) ; else x();").to("if (!b) x();");
  }

  @Test public void emptyThen2() {
    trimming.of("if (b) {;;} else {x() ;}").to("if (!b) x();");
  }

  @Test public void factorOutAnd() {
    trimming.of("(a || b) && (a || c)").to("a || b && c");
  }

  @Test public void factorOutOr() {
    trimming.of("a && b || a && c").to("a && (b || c)");
  }

  @Test public void factorOutOr3() {
    trimming.of("a && b && x  && f() || a && c && y ").to("a && (b && x && f() || c && y)");
  }

  @Test public void forLoopBug() {
    trimming.of("" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          res += 2;\n" + //
        "        else "//
        + "       if (s.charAt(i) == 'd')\n" + //
        "          res -= 1;\n" + //
        "      return res;\n" + //
        " if (b) i = 3;").stays();
  }

  @Ignore @Test public void forwardDeclaration1() {
    trimming.of("/*    * This is a comment    */      int i = 6;   int j = 2;   int k = i+2;   S.out.println(i-j+k); ")
        .to(" /*    * This is a comment    */      int j = 2;   int i = 6;   int k = i+2;   S.out.println(i-j+k); ");
  }

  @Ignore @Test public void forwardDeclaration2() {
    trimming.of("/*    * This is a comment    */      int i = 6, h = 7;   int j = 2;   int k = i+2;   S.out.println(i-j+k); ")
        .to(" /*    * This is a comment    */      int h = 7;   int j = 2;   int i = 6;   int k = i+2;   S.out.println(i-j+k); ");
  }

  @Ignore @Test public void forwardDeclaration3() {
    trimming
        .of("/*    * This is a comment    */      int i = 6;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   y(i);   y(i+m); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   int i = 6;   y(i);   y(i+m); ");
  }

  @Ignore @Test public void forwardDeclaration4() {
    trimming
        .of(" /*    * This is a comment    */      int i = 6;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   final BlahClass bc = new BlahClass(i);   y(i+m+bc.j);    private static class BlahClass {   public BlahClass(int i) {    j = 2*i;      public final int j; ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   int i = 6;   final BlahClass bc = new BlahClass(i);   y(i+m+bc.j);    private static class BlahClass {   public BlahClass(int i) {    j = 2*i;      public final int j; ");
  }

  @Ignore @Test public void forwardDeclaration5() {
    trimming
        .of("/*    * This is a comment    */      int i = y(0);   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int i = y(0);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ");
  }

  @Ignore @Test public void forwardDeclaration6() {
    trimming
        .of(" /*    * This is a comment    */      int i = y(0);   int h = 8;   int j = 3;   int k = j+2 + y(i);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ")
        .to(" /*    * This is a comment    */      int h = 8;   int i = y(0);   int j = 3;   int k = j+2 + y(i);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ");
  }

  @Ignore @Test public void forwardDeclaration7() {
    trimming
        .of("  j = 2*i;   }      public final int j;    private BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println(res.j);   return res; ")
        .to("  j = 2*i;   }      public final int j;    private BlahClass yada6() {   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   final BlahClass res = new BlahClass(6);   S.out.println(res.j);   return res; ");
  }

  @Test public void IfBarFooElseBazFooExtractDefinedSuffix() {
    trimming
        .of("" //
            + "public static void f() {\n" //
            + "  int i = 0;\n" //
            + "  if (f()) {\n" //
            + "    i += 1;\n" //
            + "    System.out.println('!');\n" //
            + "    System.out.println('!');\n" //
            + "    ++i;\n" //
            + "  } else {\n" //
            + "    i += 2;\n" //
            + "    System.out.println('@');\n" //
            + "    System.out.println('@');\n" //
            + "    ++i;\n" //
            + "  }\n" //
            + "}")
        .to("" //
            + "public static void f() {\n" //
            + "  int i = 0;\n" //
            + "  if (f()) {\n" //
            + "    i += 1;\n" //
            + "    System.out.println('!');\n" //
            + "    System.out.println('!');\n" //
            + "  } else {\n" //
            + "    i += 2;\n" //
            + "    System.out.println('@');\n" //
            + "    System.out.println('@');\n" //
            + "  }\n" //
            + "  ++i;" //
            + "}");
  }

  @Test public void IfBarFooElseBazFooExtractUndefinedSuffix() {
    trimming.of("" //
        + "public final static final void f() {\n" //
        + "  if (tr()) {\n" //
        + "    int i = 0;\n" //
        + "    System.out.println(i + 0);\n" //
        + "    ++i;\n" //
        + "  } else {\n" //
        + "    int i = 1;\n" //
        + "    System.out.println(i * 1);\n" //
        + "    ++i;\n" //
        + "  }\n" //
        + "}");
  }

  @Test public void ifBugSecondTry() {
    trimming.of("" + //
        " final int c = 2;\n" + //
        "    if (c == c + 1) {\n" + //
        "      if (c == c + 2)\n" + //
        "        return null;\n" + //
        "      c = f().charAt(3);\n" + //
        "    } else if (Character.digit(c, 16) == -1)\n" + //
        "      return null;\n" + //
        "    return null;").to("" + //
            "    final int c = 2;\n" + //
            "    if (c != c + 1) {\n" + //
            "      if (Character.digit(c, 16) == -1)\n" + //
            "        return null;\n" + //
            "    } else {\n" + //
            "      if (c == c + 2)\n" + //
            "        return null;\n" + //
            "      c = f().charAt(3);\n" + //
            "    }\n" + //
            "    return null;");//
  }

  @Test public void ifBugSimplified() {
    trimming.of("" + //
        "    if (x) {\n" + //
        "      if (z)\n" + //
        "        return null;\n" + //
        "      c = f().charAt(3);\n" + //
        "    } else if (y)\n" + //
        "      return;\n" + //
        "").to("" + //
            "    if (!x) {\n" + //
            "      if (y)\n" + //
            "        return;\n" + //
            "    } else {\n" + //
            "      if (z)\n" + //
            "        return null;\n" + //
            "      c = f().charAt(3);\n" + //
            "    }\n" + //
            "");//
  }

  @Test public void ifBugWithPlainEmptyElse() {
    trimming.of("" + //
        "      if (z)\n" + //
        "        f();\n" + //
        "      else\n" + //
        "         ; \n" + //
        "").to("" + //
            "      if (z)\n" + //
            "        f();\n" + //
            "");//
  }

  @Test public void ifDegenerateThenInIf() {
    trimming.of("if (a) if (b) {} else f(); x();")//
        .to(" if (a) if (!b) f(); x();");
  }

  @Test public void ifEmptyElsewWithinIf() {
    trimming.of("if (a) if (b) {;;;f();} else {}")//
        .to("if(a&&b){;;;f();}");
  }

  @Test public void ifEmptyThenThrow() {
    trimming
        .of("" //
            + "if (b) {\n" //
            + " /* empty */" //
            + "} else {\n" //
            + " throw new Excpetion();\n" //
            + "}")
        .to("" //
            + "if (!b) " //
            + "  throw new Excpetion();" //
            + "");
  }

  @Test public void ifEmptyThenThrowVariant() {
    trimming
        .of("" //
            + "if (b) {\n" //
            + " /* empty */" //
            + "; \n" //
            + "} // no else \n" //
            + " throw new Exception();\n" //
            + "")
        .to("" //
            + "  throw new Exception();" //
            + "");
  }

  @Test public void ifEmptyThenThrowWitinIf() {
    trimming
        .of("" //
            + "if (x) if (b) {\n" //
            + " /* empty */" //
            + "} else {\n" //
            + " throw new Excpetion();\n" //
            + "} else { f();f();f();f();f();f();f();f();}")
        .to("" //
            + "if (x) { if (!b) \n" //
            + "  throw new Excpetion();" //
            + "} else { f();f();f();f();f();f();f();f();}");
  }

  @Test public void ifFunctionCall() {
    trimming.of("if (x) f(a); else f(b);").to("f(x ? a: b);");
  }

  @Test public void ifPlusPlusPost() {
    trimming.of("if (x) a++; else b++;").to("if(x)++a;else++b;");
  }

  @Test public void ifPlusPlusPostExpression() {
    trimming.of("x? a++:b++").stays();
  }

  @Test public void ifPlusPlusPre() {
    trimming.of("if (x) ++a; else ++b;").stays();
  }

  @Test public void ifPlusPlusPreExpression() {
    trimming.of("x? ++a:++b").stays();
  }

  @Test public void ifSequencerNoElseSequencer0() {
    trimming.of("if (a) return; break;").stays();
  }

  @Test public void ifSequencerNoElseSequencer01() {
    trimming.of("if (a) throw e; break;").stays();
  }

  @Test public void ifSequencerNoElseSequencer02() {
    trimming.of("if (a) break; break;").to("break;");
  }

  @Test public void ifSequencerNoElseSequencer03() {
    trimming.of("if (a) continue; break;").stays();
  }

  @Test public void ifSequencerNoElseSequencer04() {
    trimming.of("if (a) break; return;").to("if (!a) return; break;");
  }

  @Test public void ifSequencerNoElseSequencer05() {
    trimming.of("if (a) {x(); return;} continue;").stays();
  }

  @Test public void ifSequencerNoElseSequencer06() {
    trimming.of("if (a) throw e; break;").stays();
  }

  @Test public void ifSequencerNoElseSequencer07() {
    trimming.of("if (a) break; throw e;").to("if (!a) throw e; break;");
  }

  @Test public void ifSequencerNoElseSequencer08() {
    trimming.of("if (a) throw e; continue;").stays();
  }

  @Test public void ifSequencerNoElseSequencer09() {
    trimming.of("if (a) break; throw e;").to("if (!a) throw e; break;");
  }

  @Test public void ifSequencerNoElseSequencer10() {
    trimming.of("if (a) continue; return;").to("if (!a) return; continue;");
  }

  @Test public void ifSequencerThenSequencer0() {
    trimming.of("if (a) return 4; else break;").to("if (a) return 4; break;");
  }

  @Test public void ifSequencerThenSequencer1() {
    trimming.of("if (a) break; else return 2;").to("if (!a) return 2; break;");
  }

  @Test public void ifSequencerThenSequencer3() {
    trimming.of("if (a) return 10; else continue;").to("if (a) return 10; continue;");
  }

  @Test public void ifSequencerThenSequencer4() {
    trimming.of("if (a) continue; else return 2;").to("if (!a) return 2; continue;");
  }

  @Test public void ifSequencerThenSequencer5() {
    trimming.of("if (a) throw e; else break;").to("if (a) throw e; break;");
  }

  @Test public void ifSequencerThenSequencer6() {
    trimming.of("if (a) break; else throw e;").to("if (!a) throw e; break;");
  }

  @Test public void ifSequencerThenSequencer7() {
    trimming.of("if (a) throw e; else continue;").to("if (a) throw e; continue;");
  }

  @Test public void ifSequencerThenSequencer8() {
    trimming.of("if (a) break; else throw e;").to("if (!a) throw e; break;");
  }

  @Test public void ifThrowNoElseThrow() {
    trimming
        .of("" //
            + "if (!(e.getCause() instanceof Error))\n" //
            + "  throw e;\n" //
            + "throw (Error) e.getCause();")//
        .to(" throw !(e.getCause()instanceof Error)?e:(Error)e.getCause();");//
  }

  @Test public void ifWithCommonNotInBlock() {
    trimming.of("for (;;) if (a) {i++;j++;f();} else { i++;j++; g();}").to("for(;;){i++;j++;if(a)f();else g();}");
  }

  @Test public void ifWithCommonNotInBlockDegenerate() {
    trimming.of("for (;;) if (a) {i++; f();} else { i++;j++; }").to("for(;;){i++; if(a)f(); else j++;}");
  }

  @Test public void ifWithCommonNotInBlockiLongerElse() {
    trimming.of("for (;;) if (a) {i++;j++;f();} else { i++;j++;  f(); h();}").to("for(;;){i++;j++; f(); if(!a) h();}");
  }

  @Test public void ifWithCommonNotInBlockiLongerThen() {
    trimming.of("for (;;) if (a) {i++;j++;f();} else { i++;j++; }").to("for(;;){i++;j++; if(a)f();}");
  }

  @Test public void ifWithCommonNotInBlockNothingLeft() {
    trimming.of("for (;;) if (a) {i++;j++;} else { i++;j++; }").to("for(;;){i++;j++;}");
  }

  @Test public void infiniteLoopBug1() {
    trimming.of("static boolean hasAnnotation(final VariableDeclarationFragment f) {\n" + //
        "      return hasAnnotation((VariableDeclarationStatement) f.getParent());\n" + //
        "    }").stays();
  }

  @Test public void infiniteLoopBug2() {
    trimming.of(" static boolean hasAnnotation(final VariableDeclarationStatement n) {\n" + //
        "      return hasAnnotation(n.modifiers());\n" + //
        "    }").to(" static boolean hasAnnotation(final VariableDeclarationStatement s) {\n" + //
            "      return hasAnnotation(s.modifiers());\n" + //
            "    }");
  }

  @Test public void infiniteLoopBug3() {
    trimming.of("  boolean f(final VariableDeclarationStatement n) {\n" + //
        "      return false;\n" + //
        "    }").to("  boolean f(final VariableDeclarationStatement s) {\n" + //
            "      return false;\n" + //
            "    }");
  }

  @Test public void infiniteLoopBug4() {
    trimming.of("void f(final VariableDeclarationStatement n) {}")//
        .to(" void f(final VariableDeclarationStatement s) { }");
  }

  @Ignore @Test public void inline00() {
    trimming.of("" + //
        "  Object a() { " + //
        "    class a {\n" + //
        "      a a;\n" + //
        "      Object a() {\n" + //
        "        return a;\n" + // /
        "      }" + //
        "    }\n" + //
        "    final Object a = new Object();\n" + //
        "    if (a instanceof a)\n" + //
        "      new Object();  \n" + //
        "    final Object a = new Object();\n" + //
        "    if (a instanceof a)\n" + //
        "      new Object();" + //
        "}\n" + //
        "").to(//
            "  Object a() { " + //
                "    class a {\n" + //
                "      Object a() {\n" + //
                "        return a;\n" + // /
                "    }\n" + //
                "    final Object a = new Object();\n" + //
                "    if (a instanceof a)\n" + //
                "      new Object();  \n" + //
                "    final Object a = new Object();\n" + //
                "    if (a instanceof a)\n" + //
                "      new Object();" + //
                "}\n" + //
                "");
  }

  @Test public void inline01() {
    trimming.of("" + //
        "  public int y() {\n" + //
        "    final Z res = new Z(6);\n" + //
        "    S.out.println(res.j);\n" + //
        "    return res;\n" + //
        " }\n" + //
        "").to(//
            "  public int y() {\n" + // //
                "    final Z $ = new Z(6);\n" + // //
                "    S.out.println($.j);\n" + // //
                "    return $;\n" + // //
                "  }\n" + //
                "");
  }

  @Test public void inlineArrayInitialization1() {
    trimming.of("" //
        + "public void multiDimensionalIntArraysAreEqual() {\n" //
        + "  int[][] int1 = {{1, 2, 3}, {4, 5, 6}};\n" //
        + "  int[][] int2 = {{1, 2, 3}, {4, 5, 6}};\n" //
        + "  assertArrayEquals(int1, int2);\n" //
        + "}").stays();
  }

  @Test public void inlineArrayInitialization2() {
    trimming.of("" //
        + "public double[] solve() {\n" //
        + "  final SimpleRegression regress = new SimpleRegression(true);\n" //
        + "  for (double[] d : points)\n" //
        + "    regress.addData(d[0], d[1]);\n" //
        + "  final double[] $ = { regress.getSlope(), regress.getIntercept() };\n" //
        + "  return $;\n" //
        + "}").stays();
  }

  @Test public void inlineInitializers() {
    trimming.of("int b,a = 2; return 3 * a * b; ").to("return 3*2*b;");
  }

  @Test public void inlineInitializersFirstStep() {
    trimming.of("int b=4,a = 2; return 3 * a * b; ").to("int a = 2; return 3*a*4;");
  }

  @Test public void inlineInitializersSecondStep() {
    trimming.of("int a = 2; return 3*a*4;").to("return 3 * 2 * 4;");
  }

  /** START OF STABLING TESTS */
  @Test public void inlineIntoInstanceCreation() {
    trimming.of("" //
        + "public Statement methodBlock(FrameworkMethod m) {\n" //
        + "  final Statement statement = methodBlock(m);\n" //
        + "  return new Statement() {\n" //
        + "     public void evaluate() throws Throwable {\n" //
        + "       try {\n" //
        + "         statement.evaluate();\n" //
        + "         handleDataPointSuccess();\n" //
        + "       } catch (AssumptionViolatedException e) {\n" //
        + "         handleAssumptionViolation(e);\n" //
        + "       } catch (Throwable e) {\n" //
        + "         reportParameterizedError(e, complete.getArgumentStrings(nullsOk()));\n" //
        + "       }\n" //
        + "     }\n" //
        + "   };\n" //
        + "}").stays();
  }

  @Test public void inlineIntoNextStatementWithSideEffects() {
    trimming.of("int a = f(); if (a) g(a); else h(u(a));").stays();
  }

  @Ignore @Test public void inlineSingleUse01() {
    trimming.of("/*    * This is a comment    */      int i = y(0);   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + (y(0))); ");
  }

  @Ignore @Test public void inlineSingleUse02() {
    trimming.of("/*    * This is a comment    */      int i = 5,j=3;   int k = j+2;   int m = k + j -19 +i;   y(k); ")
        .to(" /*    * This is a comment    */      int j=3;   int k = j+2;   int m = k + j -19 +(5);   y(k); ");
  }

  @Ignore @Test public void inlineSingleUse03() {
    trimming.of("/*    * This is a comment    */      int i = 5;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + (5)); ");
  }

  @Ignore @Test public void inlineSingleUse04() {
    trimming.of("int x = 6;   final BlahClass b = new BlahClass(x);   int y = 2+b.j;   y(y-b.j);   y(y*2); ")
        .to(" final BlahClass b = new BlahClass((6));   int y = 2+b.j;   y(y-b.j);   y(y*2); ");
  }

  @Ignore @Test public void inlineSingleUse05() {
    trimming.of("int x = 6;   final BlahClass b = new BlahClass(x);   int y = 2+b.j;   y(y+x);   y(y*x); ")
        .to(" int x = 6;   int y = 2+(new BlahClass(x)).j;   y(y+x);   y(y*x); ");
  }

  @Ignore @Test public void inlineSingleUse06() {
    trimming
        .of("   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     for (final Integer pi : outdated)      coes.remove(pi);     S.out.println(coes.size()); ")
        .stays();
  }

  @Test public void inlineSingleUse07() {
    trimming
        .of("   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     S.out.println(coes.size()); ")
        .stays();
  }

  @Ignore @Test public void inlineSingleUse08() {
    trimming
        .of("   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     S.out.println(coes.size());     S.out.println(outdated.size()); ")
        .stays();
  }

  @Ignore @Test public void inlineSingleUse09() {
    trimming
        .of(" final A a = new D().new A(V){\nABRA\n{\nCADABRA\n{V;);   assertEquals(5, a.new Context().lineCount());   final PureIterable&lt;Mutant&gt; ms = a.generateMutants();   assertEquals(2, count(ms));   final PureIterator&lt;Mutant&gt; i = ms.iterator();    azzert.aye(i.hasNext());   assertEquals(V;{\nABRA\nABRA\n{\nCADABRA\n{\nV;, i.next().text);    azzert.aye(i.hasNext());   assertEquals(V;{\nABRA\n{\nCADABRA\nCADABRA\n{\nV;, i.next().text);    azzert.nay(i.hasNext());  ")
        .stays();
  }

  @Ignore @Test public void inlineSingleUse10() {
    trimming
        .of("      final A a = new A(\"{\nABRA\n{\nCADABRA\n{\");        assertEquals(5, a.new Context().lineCount());        final PureIterable<Mutant> ms = a.mutantsGenerator();        assertEquals(2, count(ms));        final PureIterator<Mutant> i = ms.iterator();         azzert.aye(i.hasNext());        assertEquals(\"{\nABRA\nABRA\n{\nCADABRA\n{\n\", i.next().text);         azzert.aye(i.hasNext());        assertEquals(\"{\nABRA\n{\nCADABRA\nCADABRA\n{\n\", i.next().text);         azzert.nay(i.hasNext());")
        .stays();
  }

  @Test public void inlineSingleUseKillingVariable() {
    trimming.of("int a,b=2; a = b;").to("int a;a=2;");
  }

  @Test public void inlineSingleUseKillingVariables() {
    trimming.of("int $, xi=0, xj=0, yi=0, yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")
        .to(" int $, xj=0, yi=0, yj=0;        if (0>xj==yi>yj)$++;else $--;");
  }

  @Test public void inlineSingleUseKillingVariablesSimplified() {
    trimming.of("int $=1,xi=0,xj=0,yi=0,yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")//
        .to(" int $=1,xj=0,yi=0,yj=0;       if(0>xj==yi>yj)$++;else $--;")//
        .to(" int $=1,yi=0,yj=0;            if(0>0==yi>yj)$++;else $--;") //
        .to(" int $=1,yj=0;                 if(0>0==0>yj)$++;else $--;") //
        .to(" int $=1;                      if(0>0==0>0)$++;else $--;") //
        .to(" int $=1;                      if(0>0==0>0)++$;else--$;") //
    ;
  }

  @Test public void inlineSingleUseTrivial() {
    trimming.of(" int $=1,yj=0;                 if(0>0==yj<0)++$;else--$;") //
        .to("  int $=1;                      if(0>0==0<0)++$;else--$;") //
    ;
  }

  @Test public void inlineSingleUseVanilla() {
    trimming.of("int a = f(); if (a) f();").to("if (f()) f();");
  }

  @Test public void inlineSingleUseWithAssignment() {
    trimming.of("int a = 2; while (true) if (f()) f(a); else a = 2;")//
        .stays();
  }

  @Test public void inlineSingleVariableIntoPlusPlus() {
    trimming.of("int $ = 0;  if (a)  ++$;  else --$;").stays();
  }

  @Test public void inliningWithVariableAssignedTo() {
    trimming.of("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
    ;
  }

  @Test public void isGreaterTrue() {
    final InfixExpression e = i("f(a,b,c,d,e) * f(a,b,c)");
    azzert.that(right(e).toString(), is("f(a,b,c)"));
    azzert.that(left(e).toString(), is("f(a,b,c,d,e)"));
    final Wring<InfixExpression> s = Toolbox.defaultInstance().find(e);
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    azzert.notNull(s);
    azzert.aye(s.claims(e));
    final Expression e1 = left(e);
    final Expression e2 = right(e);
    azzert.nay(hasNull(e1, e2));
    final boolean tokenWiseGreater = nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD;
    azzert.aye(tokenWiseGreater);
    azzert.aye(ExpressionComparator.moreArguments(e1, e2));
    azzert.aye(ExpressionComparator.longerFirst(e));
    azzert.aye(e.toString(), s.canMake(e));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that("" + replacement, is("f(a,b,c) * f(a,b,c,d,e)"));
  }

  @Test public void isGreaterTrueButAlmostNot() {
    final InfixExpression e = i("f(a,b,c,d) * f(a,b,c)");
    azzert.that(right(e).toString(), is("f(a,b,c)"));
    azzert.that(left(e).toString(), is("f(a,b,c,d)"));
    final Wring<InfixExpression> s = Toolbox.defaultInstance().find(e);
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    azzert.notNull(s);
    azzert.aye(s.claims(e));
    final Expression e1 = left(e);
    final Expression e2 = right(e);
    azzert.nay(hasNull(e1, e2));
    final boolean tokenWiseGreater = nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD;
    azzert.nay(tokenWiseGreater);
    azzert.aye(ExpressionComparator.moreArguments(e1, e2));
    azzert.aye(ExpressionComparator.longerFirst(e));
    azzert.aye(e.toString(), s.canMake(e));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that("" + replacement, is("f(a,b,c) * f(a,b,c,d)"));
  }

  @Test public void issue06() {
    trimming.of("a*-b").to("-a * b");
  }

  @Ignore("bug") @Test public void issue06A() {
    trimming.of("x/a*-b/-c*- - - d / d")//
        .to("-x/a * b/ c * d/d")//
        .stays();
  }

  @Test public void issue06B() {
    trimming.of("x/a*-b/-c*- - - d / -d")//
        .to("x/a * b/ c * d/d")//
        .to("d*x/a*b/c/d");
  }

  @Test public void issue06C1() {
    trimming.of("a*-b/-c*- - - d / d").to("-a * b/ c * d/d");
  }

  @Ignore("bug") @Test public void issue06C2() {
    trimming.of("-a * b/ c * d/d").stays();
  }

  @Ignore("bug") @Test public void issue06C3() {
    trimming.of("-a * b/ c * d").stays();
  }

  @Test public void issue06C4() {
    trimming.of("-a * b/ c ").stays();
  }

  @Test public void issue06D() {
    trimming.of("a*b*c*d*-e").to("-a*b*c*d*e").stays();
  }

  @Test public void issue06E() {
    trimming.of("-a*b*c*d*f*g*h*i*j*k").stays();
  }

  @Test public void issue06F() {
    trimming.of("x*a*-b*-c*- - - d * d")//
        .to("-x*a*b*c*d*d")//
        .stays();
  }

  @Test public void issue06G() {
    trimming.of("x*a*-b*-c*- - - d / d")//
        .to("-x*a*b*c*d/d")//
        .stays();
  }

  @Test public void issue06H() {
    trimming.of("x/a*-b/-c*- - - d ")//
        .to("-x/a * b/ c * d")//
    ;
  }

  @Test public void issue06I() {
    trimming.of("41 * - 19")//
        .to("-41 * 19 ") //
    ;
  }

  @Test public void issue06J() {
    trimming.of("41 * a * - 19")//
        .to("-41*a*19")//
        .to("-41*19*a") //
    ;
  }

  @Test public void issue110_01() {
    trimming("polite ? \"Eat your meal.\" :  \"Eat your meal, please\"") //
        .to("\"Eat your meal\" + (polite ? \".\" : \", please\")");
  }

  @Test public void issue110_02() {
    trimming("polite ? \"Eat your meal.\" :  \"Eat your meal\"") //
        .to("\"Eat your meal\" + (polite ? \".\" : \"\")");
  }

  @Test public void issue110_03() {
    trimming("polite ? \"thanks for the meal\" :  \"I hated the meal\"") //
        .to("!polite ? \"I hated the meal\": \"thanks for the meal\"") //
        .to("(!polite ? \"I hated\" : \"thanks for\" )+ \" the meal\"");
  }

  @Test public void issue110_04() {
    trimming("polite ? \"thanks.\" :  \"I hated the meal.\"") //
        .to("(polite ? \"thanks\" :\"I hated the meal\")+ \".\"");
  }

  @Test public void issue110_05() {
    trimming("a ? \"abracadabra\" : \"abba\"") //
        .to("!a ? \"abba\" : \"abracadabra\"")//
        .to("\"ab\" +(!a ? \"ba\" : \"racadabra\")")//
        .to("\"ab\" +((!a ? \"b\" : \"racadabr\")+ \"a\")")//
        .to("\"ab\" +(!a ? \"b\" : \"racadabr\")+ \"a\"").to(null);
  }

  @Test public void issue110_06() {
    trimming("receiver ==null ? \"Use \" + \"x\" : \"Use \" + receiver")//
        .to("\"Use \"+(receiver==null ? \"x\" : receiver)").to(null);
  }

  @Test public void issue110_07() {
    trimming("receiver ==null ? \"Use x\" : \"Use \" + receiver")//
        .to("\"Use \"+(receiver==null ? \"x\" : \"\"+receiver)").to(null);
  }

  @Test public void issue110_08() {
    trimming("receiver ==null ? \"Use\" : receiver + \"Use\"")//
        .to("(receiver==null ? \"\" : receiver+\"\") + \"Use\"").to(null);
  }

  @Test public void issue110_09() {
    trimming("receiver ==null ? \"user a\" : receiver + \"something a\"")//
        .to("(receiver==null ? \"user\" : receiver+\"something\") + \" a\"").to(null);
  }

  @Test public void issue110_10() {
    trimming("receiver ==null ? \"Something Use\" : \"Something\" + receiver + \"Use\"")//
        .to("\"Something\"+ (receiver==null ? \" Use\" : \"\"+receiver + \"Use\")")//
        .to("\"Something\"+ ((receiver==null ? \" \" : \"\"+receiver+\"\") + \"Use\")");
  }

  @Test public void issue110_11() {
    trimming("f() ? \"first\" + d() + \"second\" : \"first\" + g() + \"third\"")//
        .to("\"first\" + (f() ? \"\" + d()  + \"second\" : \"\" + g()  + \"third\")");
  }

  @Test public void issue110_12() {
    trimming("f() ? \"first\" + d() + \"second\" : \"third\" + g() + \"second\"")//
        .to("(f() ? \"first\" +  d() + \"\": \"third\" + g()+\"\") + \"second\"");
  }

  @Test public void issue110_13() {
    trimming("f() ? \"first is:\" + d() + \"second\" : \"first are:\" + g() + \"and second\"")//
        .to("\"first \" + (f() ? \"is:\" + d() + \"second\": \"are:\" + g() + \"and second\")")//
        .to("\"first \" + ((f() ? \"is:\" + d() + \"\": \"are:\" + g() + \"and \") + \"second\")");
  }

  @Ignore("Cocnat String currently not in toolbox, see issue #120") @Test public void issue120_1() {
    trimming("\"a\"+\"b\"").to("\"ab\"");
  }

  @Ignore("Cocnat String currently not in toolbox, see issue #120") @Test public void issue120_2() {
    trimming("\"abc\"+\"de\"+\"fgh\"").to("\"abcdefgh\"");
  }

  @Ignore("Cocnat String currently not in toolbox, see issue #120") @Test public void issue120_3() {
    trimming("\"abc\"+a.toString()+\"de\"+\"fgh\"").to("\"abc\"+a.toString()+\"defgh\"");
  }

  @Ignore("Cocnat String currently not in toolbox, see issue #120") @Test public void issue120_4() {
    trimming("c.toString()+\"abc\"+a.toString()+\"de\"+\"fgh\"") //
        .to("c.toString()+\"abc\"+a.toString()+\"defgh\"");
  }

  @Test public void issue37Simplified() {
    trimming.of("" + //
        "    int a = 3;\n" + //
        "    a = 31 * a;" + //
        "").to("int a = 31 * 3; ");
  }

  @Test public void issue37SimplifiedVariant() {
    trimming.of("" + //
        "    int a = 3;\n" + //
        "    a += 31 * a;").to("int a=3+31*3;");
  }

  @Test public void issue37WithSimplifiedBlock() {
    trimming.of("if (a) { {} ; if (b) f(); {} } else { g(); f(); ++i; ++j; }")//
        .to(" if (a) {  if (b) f(); } else { g(); f(); ++i; ++j; }");
  }

  @Test public void issue38() {
    trimming.of("    return o == null ? null\n" + //
        "        : o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
        "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
        "                : null;").stays();
  }

  @Test public void issue38Simplfiied() {
    trimming.of(//
        "         o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
            "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
            "                : null")
        .stays();
  }

  @Test public void issue39base() {
    trimming.of("" + //
        "if (name == null) {\n" + //
        "    if (other.name != null)\n" + //
        "        return false;\n" + //
        "} else if (!name.equals(other.name))\n" + //
        "    return false;\n" + //
        "return true;").stays(); //
  }

  public void issue39baseDual() {
    trimming.of("if (name != null) {\n" + //
        "    if (!name.equals(other.name))\n" + //
        "        return false;\n" + //
        "} else if (other.name != null)\n" + //
        "    return false;\n" + //
        "return true;").to("" + //
            "if (name == null) {\n" + //
            "    if (other.name != null)\n" + //
            "        return false;\n" + //
            "} else if (!name.equals(other.name))\n" + //
            "    return false;\n" + //
            "return true;");
  }

  @Test(timeout = 100) public void issue39versionA() {
    trimming.of("" + //
        "if (varArgs) {\n" + //
        "    if (argumentTypes.length < parameterTypes.length - 1) {\n" + //
        "        return false;\n" + //
        "    }\n" + //
        "} else if (parameterTypes.length != argumentTypes.length) {\n" + //
        "    return false;\n" + //
        "}").to("" + //
            "if (!varArgs) {\n" + //
            "    if (parameterTypes.length != argumentTypes.length) {\n" + //
            "        return false;\n" + //
            "    }\n" + //
            "} else if (argumentTypes.length < parameterTypes.length - 1) {\n" + //
            "    return false;\n" + //
            "}");
  }

  public void issue39versionAdual() {
    trimming.of("" + //
        "if (!varArgs) {\n" + //
        "    if (parameterTypes.length != argumentTypes.length) {\n" + //
        "        return false;\n" + //
        "    }\n" + //
        "} else if (argumentTypes.length < parameterTypes.length - 1) {\n" + //
        "    return false;\n" + //
        "}" + //
        "").stays();
  }

  @Test public void issue41FunctionCall() {
    trimming.of("int a = f();a += 2;").to("int a = f()+2;");
  }

  @Test public void issue43() {
    trimming
        .of("" //
            + "String t = Z2;  "//
            + " t = t.f(A).f(b) + t.f(c);   "//
            + "return (t + 3);    ")
        .to(""//
            + "String t = Z2.f(A).f(b) + Z2.f(c);" //
            + "return (t + 3);" //
            + "");
  }

  @Test public void issue46() {
    trimming
        .of("" + //
            "int f() {\n" + //
            "  x++;\n" + //
            "  y++;\n" + //
            "  if (a) {\n" + //
            "     i++; \n" + //
            "     j++; \n" + //
            "     k++;\n" + //
            "  }\n" + //
            "}")//
        .to("" + //
            "int f() {\n" + //
            "  ++x;\n" + //
            "  ++y;\n" + //
            "  if (!a)\n" + //
            "    return;\n" + //
            "  ++i;\n" + //
            "  ++j; \n" + //
            "  ++k;\n" + //
            "}");
  }

  @Test public void issue49() {
    trimming.of("int f() { int f = 0; for (int i: X) $ += f(i); return f;}")//
        .to("int f(){int $=0;for(int i:X)$+=f(i);return $;}");
  }

  @Test public void issue51g() {
    trimming.of("abstract abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue52a() {
    trimming.of("abstract abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue52A() {
    trimming.of( //
        "void m() { return; }").to( //
            "void m() {}");
  }

  @Test public void issue52A1() {
    trimming.of( //
        "void m() { return a; }").to( //
            "");
  }

  @Test public void issue52b() {
    trimming.of("abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue52B1() {
    trimming.of( //
        "void m() { if (a) { f(); return; }}").to( //
            "void m() { if (a) { f(); ; }}");
  }

  @Test public void issue52B2() {
    trimming.of( //
        "void m() { if (a) ++i; else { f(); return; }}").to( //
            "void m() { if (a) ++i; else { f(); ; }}");
  }

  @Test public void issue52c() {
    trimming.of("interface a"//
        + "{}").stays();
  }

  @Test public void issue52d() {
    trimming.of(//
        "public interface A {\n"//
            + "public abstract void add();\n"//
            + "abstract void remove()\n; "//
            + "static final void remove()\n; "//
            + "}"//
    ).to("" + "public interface A {\n"//
        + "void add();\n"//
        + "void remove()\n; "//
        + "static void remove()\n; "//
        + "}"//
    );
  }

  @Test public void issue52e() {
    trimming.of(//
        "public interface A {\n"//
            + "static void remove()\n; "//
            + "public static int i = 3\n; "//
            + "}")
        .to( //
            "public interface A {\n"//
                + "static void remove()\n; "//
                + "static int i = 3\n; "//
                + "}");
  }

  @Test public void issue52f() {
    trimming.of(//
        "public interface A {\n"//
            + "static void remove()\n; "//
            + "public static int i\n; "//
            + "}")
        .to( //
            "public interface A {\n"//
                + "static void remove()\n; "//
                + "static int i\n; "//
                + "}");
  }

  @Test public void issue52g() {
    trimming
        .of("final class ClassTest {\n"//
            + "final void remove();\n"//
            + "}")
        .to("final class ClassTest {\n"//
            + "void remove();\n "//
            + "}");
  }

  @Test public void issue52h() {
    trimming.of("final class ClassTest {\n"//
        + "public final void remove();\n"//
        + "}"//
    ).to(//
        "final class ClassTest {\n"//
            + "public void remove();\n "//
            + "}");
  }

  @Test public void issue52i() {
    trimming.of("public class ClassTest {\n"//
        + "static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "SUNDAY, MONDAY\n"//
        + "}");
  }

  @Test public void issue52j() {
    trimming.of("public class ClassTest {\n"//
        + "private static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}");
  }

  @Test public void issue52k() {
    trimming.of("public class ClassTest {\n"//
        + "public  ClassTest(){}\n"//
        + "}").stays();
  }

  @Test public void issue52l() {
    trimming.of("abstract class A { final void f() { }}").stays();
  }

  @Test public void issue52n() {
    trimming.of(//
        "abstract class A {\n"//
            + "static void f() {}\n "//
            + "public final static int i = 3; "//
            + "}")
        .stays();
  }

  @Test public void issue52o() {
    trimming.of(//
        "final class A {\n"//
            + "static void f() {}\n "//
            + "public final static int i = 3; "//
            + "}")
        .stays();
  }

  @Test public void issue52p() {
    trimming
        .of(//
            "enum A {a1, a2; static enum B {b1, b2; static class C { static enum D {c1, c2}}}")//
        .to("enum A {a1, a2; enum B {b1, b2; static class C { static enum D {c1, c2}}}")//
        .to("enum A {a1, a2; enum B {b1, b2; static class C { enum D {c1, c2}}}")//
    ;
  }

  @Test public void issue53() {
    trimming
        .of( //
            "int[] is = f(); for (int i: is) f(i);")//
        .to( //
            "for (int i: f()) f(i);");
  }

  @Test public void issue53a() {
    trimming
        .of( //
            "int f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .to( //
            "int f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void issue54DoNonSideEffect() {
    trimming
        .of( //
            "int a  = f; do { b[i] = a; } while (b[i] != a);")//
        .to( //
            "do { b[i] = f; } while (b[i] != f);");
  }

  @Test public void issue54DoNonSideEffectEmptyBody() {
    trimming
        .of( //
            "int a = f(); do ; while (a != 1);")//
        .to( //
            "");
  }

  @Test public void issue54DoWhile() {
    trimming
        .of( //
            "int a  = f(); do { b[i] = 2; ++i; } while (b[i] != a);")//
        .to( //
            "");
  }

  @Test public void issue54DoWithBlock() {
    trimming
        .of( //
            "int a  = f(); do { b[i] = a;  ++i; } while (b[i] != a);")//
        .stays();
  }

  @Test public void issue54doWithoutBlock() {
    trimming.of("int a  = f(); do b[i] = a; while (b[i] != a);")//
        .stays();
  }

  @Test public void issue54ForEnhanced() {
    trimming.of("int a  = f(); for (int i: a) b[i] = x;")//
        .to(" for (int i: f()) b[i] = x;");
  }

  @Test public void issue54ForEnhancedNonSideEffectLoopHeader() {
    trimming.of("int a  = f; for (int i: a) b[i] = b[i-1];")//
        .to("for (int i: f) b[i] = b[i-1];");
  }

  @Test public void issue54ForEnhancedNonSideEffectWithBody() {
    trimming.of("int a  = f; for (int i: j) b[i] = a;")//
        .to(" for(int i:j)b[i]=f; ");
  }

  @Test public void issue54ForPlain() {
    trimming.of("int a  = f(); for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .stays();
  }

  @Test public void issue54ForPlainNonSideEffect() {
    trimming.of("int a  = f; for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .to("for (int i = 0; i < 100;  ++i) b[i] = f;");
  }

  @Test public void issue54ForPlainUseInCondition() {
    trimming.of("int a  = f(); for (int i = 0; a < 100;  ++i) b[i] = 3;")//
        .stays();
  }

  @Test public void issue54ForPlainUseInConditionNonSideEffect() {
    trimming.of("int a  = f; for (int i = 0; a < 100;  ++i) b[i] = 3;")//
        .to("for (int i = 0; f < 100;  ++i) b[i] = 3;");
  }

  @Test public void issue54ForPlainUseInInitializer() {
    trimming.of("int a  = f(); for (int i = a; i < 100; i++) b[i] = 3;")//
        .to(" for (int i = f(); i < 100; i++) b[i] = 3;");
  }

  @Test public void issue54ForPlainUseInInitializerNonSideEffect() {
    trimming.of("int a  = f; for (int i = a; i < 100; i *= a) b[i] = 3;")//
        .to(" for (int i = f; i < 100; i *= f) b[i] = 3;");
  }

  @Test public void issue54ForPlainUseInUpdaters() {
    trimming.of("int a  = f(); for (int i = 0; i < 100; i *= a) b[i] = 3;")//
        .stays();
  }

  @Test public void issue54ForPlainUseInUpdatersNonSideEffect() {
    trimming.of("int a  = f; for (int i = 0; i < 100; i *= a) b[i] = 3;")//
        .to("for (int i = 0; i < 100; i *= f) b[i] = 3;");
  }

  @Test public void issue54While() {
    trimming.of("int a  = f(); while (c) b[i] = a;")//
        .stays();
  }

  @Test public void issue54WhileNonSideEffect() {
    trimming.of("int a  = f; while (c) b[i] = a;")//
        .to("while (c) b[i] = f;");
  }

  @Test public void issue54WhileScopeDoesNotInclude() {
    included("int a  = f(); while (c) b[i] = a;", VariableDeclarationFragment.class)//
        .notIn(new DeclarationInitializerStatementTerminatingScope());
  }

  @Test public void issue57a() {
    trimming.of("void m(List<Expression>... expressions) { }").to("void m(List<Expression>... ess) {}");
  }

  @Test public void issue57b() {
    trimming.of("void m(Expression... expression) { }").to("void m(Expression... es) {}");
  }

  @Test public void issue58a() {
    trimming.of("X f(List<List<Expression>> expressions){}").to("X f(List<List<Expression>> ess){}");
  }

  @Test public void issue58b() {
    trimming.of("X f(List<Expression>[] expressions){}").to("X f(List<Expression>[] ess){}");
  }

  @Test public void issue58c() {
    trimming.of("X f(List<Expression>[] expressions){}").to("X f(List<Expression>[] ess){}");
  }

  @Test public void issue58d() {
    trimming.of("X f(List<Expression>... expressions){}").to("X f(List<Expression>... ess){}");
  }

  @Test public void issue58e() {
    trimming.of("X f(Expression[]... expressions){}").to("X f(Expression[]... ess){}");
  }

  @Test public void issue58f() {
    trimming.of("X f(Expression[][]... expressions){}").to("X f(Expression[][]... esss){}");
  }

  @Test public void issue58g() {
    trimming.of("X f(List<Expression[][]>... expressions){}").to("X f(List<Expression[][]>... essss){}");
  }

  @Test public void issue62a() {
    trimming.of("int f(int i) { for(;;++i) if(false) break; return i; }").stays();
  }

  @Test public void issue62b() {
    trimming.of("int f(int i) { for(;i<100;i=i+1) if(false) break; return i; }").stays();
  }

  @Test public void issue62c() {
    trimming.of("int f(int i) { while(++i > 999) if(i>99) break; return i;}").stays();
  }

  @Test public void issue64a() {
    trimming.of("void f() {" + //
        "    final int a = f();\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").stays();
  }

  @Test public void issue64b() {
    trimming.of("void f() {" + //
        "    final int a = 3;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").stays();
  }

  @Test public void issue73a() {
    trimming.of("void foo(StringBuilder sb) {}").to("void foo(StringBuilder b) {}");
  }

  @Test public void issue73b() {
    trimming.of("void foo(DataOutput dataOutput) {}").to("void foo(DataOutput o) {}");
  }

  @Test public void issue73c() {
    trimming.of("void foo(Integer integer, ASTNode astn) {}").to("void foo(Integer i, ASTNode astn) {}");
  }

  @Test public void linearTransformation() {
    trimming.of("plain * the + kludge").to("the*plain+kludge");
  }

  @Test public void literalVsLiteral() {
    trimming.of("1 < 102333").stays();
  }

  @Test public void longChainComparison() {
    trimming.of("a == b == c == d").stays();
  }

  @Test public void longChainParenthesisComparison() {
    trimming.of("(a == b == c) == d").stays();
  }

  @Test public void longChainParenthesisNotComparison() {
    trimming.of("(a == b == c) != d").stays();
  }

  @Test public void longerChainParenthesisComparison() {
    trimming.of("(a == b == c == d == e) == d").stays();
  }

  @Test public void massiveInlining() {
    trimming.of("int a,b,c;String t = zE4;if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;")//
        .to("int a,b,c;if(2*3.1415*180>a||zE4.concat(sS)==1922&&zE4.length()>3)return c>5;") //
        .stays();
  }

  @Test public void methodWithLastIf() {
    trimming.of("int f() { if (a) { f(); g(); h();}}").to("int f() { if (!a) return;  f(); g(); h();}");
  }

  @Test public void nestedIf1() {
    trimming.of("if (a) if (b) i++;").to("if (a && b) i++;");
  }

  @Test public void nestedIf2() {
    trimming.of("if (a) if (b) i++; else ; else ; ").to("if (a && b) i++; else ;");
  }

  @Test public void nestedIf3() {
    trimming.of("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else;else{++y;f();g();z();}");
  }

  @Test public void nestedIf33() {
    trimming.of("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .to(" if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}")//
        .to(" if(x){if(a&&b)++i;}else{++y;f();g();}")//
    ;
  }

  @Test public void nestedIf33a() {
    trimming.of("if (x) { if (a && b) i++; } else { y++; f(); g(); }")//
        .to(" if (x) {if(a&&b)++i;} else{++y;f();g();}");
  }

  @Test public void nestedIf33b() {
    trimming.of("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .to("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3c() {
    trimming.of("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .to(" if(x) {if(a&&b)i++;} else {++y;f();g();}");
  }

  @Test public void nestedIf3d() {
    trimming.of("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else; else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)i++;} else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)++i;} else{++y;f();g();z();}") //
    ;
  }

  @Test public void nestedIf3e() {
    trimming.of("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to(" if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }

  @Test public void nestedIf3f() {
    trimming.of("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .to(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3f1() {
    trimming.of(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3x() {
    trimming.of("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)i++;}else{++y;f();g();z();}") //
    ;
  }

  @Test public void nestedTernaryAlignment() {
    trimming.of("int b=3==4?5==3?2:3:5==3?2:3*3;").to("int b=3==4?5==3?2:3:5!=3?3*3:2;");
  }

  @Test public void noChange() {
    trimming.of("12").stays();
    trimming.of("true").stays();
    trimming.of("null").stays();
    trimming.of("on*of*no*notion*notion").to("no*of*on*notion*notion");
  }

  @Test public void noChangeA() {
    trimming.of("true").stays();
  }

  @Test public void noChange0() {
    trimming.of("kludge + the * plain ").stays();
  }

  @Test public void noChange1() {
    trimming.of("the * plain").stays();
  }

  @Test public void noChange2() {
    trimming.of("plain + kludge").stays();
  }

  @Test public void noinliningIntoSynchronizedStatement() {
    trimming.of("int a  = f(); synchronized(this) { int b = a; }")//
        .stays();
  }

  @Test public void noinliningIntoSynchronizedStatementEvenWithoutSideEffect() {
    trimming.of("int a  = f; synchronized(this) { int b = a; }")//
        .stays();
  }

  @Test public void noinliningIntoTryStatement() {
    trimming.of("int a  = f(); try { int b = a; } catch (Exception E) {}")//
        .stays();
  }

  @Test public void noinliningIntoTryStatementEvenWithoutSideEffect() {
    trimming.of("int a  = f; try { int b = a; } catch (Exception E) {}")//
        .stays();
  }

  @Test public void notOfAnd() {
    trimming.of("!(A && B)").to("!A || !B");
  }

  @Test public void oneMultiplication() {
    trimming.of("f(a,b,c,d) * f(a,b,c)").to("f(a,b,c) * f(a,b,c,d)");
  }

  @Test public void oneMultiplicationAlternate() {
    trimming.of("f(a,b,c,d,e) * f(a,b,c)").to("f(a,b,c) * f(a,b,c,d,e)");
  }

  @Test public void orFalse3ORTRUE() {
    trimming.of("false || false || false").to("false");
  }

  @Test public void orFalse4ORTRUE() {
    trimming.of("false || false || false || false").to("false");
  }

  @Test public void orFalseANDOf3WithoutBoolean() {
    trimming.of("a && b && false").stays();
  }

  @Test public void orFalseANDOf3WithoutBooleanA() {
    trimming.of("x && a && b").stays();
  }

  @Test public void orFalseANDOf3WithTrue() {
    trimming.of("true && x && true && a && b").to("x && a && b");
  }

  @Test public void orFalseANDOf3WithTrueA() {
    trimming.of("a && b && true").to("a && b");
  }

  @Test public void orFalseANDOf4WithoutBoolean() {
    trimming.of("a && b && c && false").stays();
  }

  @Test public void orFalseANDOf4WithoutBooleanA() {
    trimming.of("x && a && b && c").stays();
  }

  @Test public void orFalseANDOf4WithTrue() {
    trimming.of("x && true && a && b && c").to("x && a && b && c");
  }

  @Test public void orFalseANDOf4WithTrueA() {
    trimming.of("a && b && c && true").to("a && b && c");
  }

  @Test public void orFalseANDOf5WithoutBoolean() {
    trimming.of("false && a && b && c && d").stays();
  }

  @Test public void orFalseANDOf5WithoutBooleanA() {
    trimming.of("x && a && b && c && d").stays();
  }

  @Test public void orFalseANDOf5WithTrue() {
    trimming.of("x && a && b && c && true && true && true && d").to("x && a && b && c && d");
  }

  @Test public void orFalseANDOf5WithTrueA() {
    trimming.of("true && a && b && c && d").to("a && b && c && d");
  }

  @Test public void orFalseANDOf6WithoutBoolean() {
    trimming.of("a && b && c && false && d && e").stays();
  }

  @Test public void orFalseANDOf6WithoutBooleanA() {
    trimming.of("x && a && b && c && d && e").stays();
  }

  @Test public void orFalseANDOf6WithoutBooleanWithParenthesis() {
    trimming.of("(x && (a && b)) && (c && (d && e))").stays();
  }

  @Test public void orFalseANDOf6WithTrue() {
    trimming.of("x && a && true && b && c && d && e").to("x && a && b && c && d && e");
  }

  @Test public void orFalseANDOf6WithTrueA() {
    trimming.of("a && b && c && true && d && e").to("a && b && c && d && e");
  }

  @Test public void orFalseANDOf6WithTrueWithParenthesis() {
    trimming.of("x && (true && (a && b && true)) && (c && (d && e))").to("x && a && b && c && d && e");
  }

  @Test public void orFalseANDOf7WithMultipleTrueValue() {
    trimming.of("(a && (b && true)) && (c && (d && (e && (true && true))))").to("a &&b &&c &&d &&e ");
  }

  @Test public void orFalseANDOf7WithoutBooleanAndMultipleFalseValue() {
    trimming.of("(a && (b && false)) && (c && (d && (e && (false && false))))").stays();
  }

  @Test public void orFalseANDOf7WithoutBooleanWithParenthesis() {
    trimming.of("(a && b) && (c && (d && (e && false)))").stays();
  }

  @Test public void orFalseANDOf7WithTrueWithParenthesis() {
    trimming.of("true && (a && b) && (c && (d && (e && true)))").to("a &&b &&c &&d &&e ");
  }

  @Test public void orFalseANDWithFalse() {
    trimming.of("b && a").stays();
  }

  @Test public void orFalseANDWithoutBoolean() {
    trimming.of("b && a").stays();
  }

  @Test public void orFalseANDWithTrue() {
    trimming.of("true && b && a").to("b && a");
  }

  @Test public void orFalseFalseOrFalse() {
    trimming.of("false ||false").to("false");
  }

  @Test public void orFalseORFalseWithSomething() {
    trimming.of("true || a").stays();
  }

  @Test public void orFalseORFalseWithSomethingB() {
    trimming.of("false || a || false").to("a");
  }

  @Test public void orFalseOROf3WithFalse() {
    trimming.of("x || false || b").to("x || b");
  }

  @Test public void orFalseOROf3WithFalseB() {
    trimming.of("false || a || b || false").to("a || b");
  }

  @Test public void orFalseOROf3WithoutBoolean() {
    trimming.of("a || b").stays();
  }

  @Test public void orFalseOROf3WithoutBooleanA() {
    trimming.of("x || a || b").stays();
  }

  @Test public void orFalseOROf4WithFalse() {
    trimming.of("x || a || b || c || false").to("x || a || b || c");
  }

  @Test public void orFalseOROf4WithFalseB() {
    trimming.of("a || b || false || c").to("a || b || c");
  }

  @Test public void orFalseOROf4WithoutBoolean() {
    trimming.of("a || b || c").stays();
  }

  @Test public void orFalseOROf4WithoutBooleanA() {
    trimming.of("x || a || b || c").stays();
  }

  @Test public void orFalseOROf5WithFalse() {
    trimming.of("x || a || false || c || d").to("x || a || c || d");
  }

  @Test public void orFalseOROf5WithFalseB() {
    trimming.of("a || b || c || d || false").to("a || b || c || d");
  }

  @Test public void orFalseOROf5WithoutBoolean() {
    trimming.of("a || b || c || d").stays();
  }

  @Test public void orFalseOROf5WithoutBooleanA() {
    trimming.of("x || a || b || c || d").stays();
  }

  @Test public void orFalseOROf6WithFalse() {
    trimming.of("false || x || a || b || c || d || e").to("x || a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithFalseWithParenthesis() {
    trimming.of("x || (a || (false) || b) || (c || (d || e))").to("x || a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithFalseWithParenthesisB() {
    trimming.of("(a || b) || false || (c || false || (d || e || false))").to("a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithoutBoolean() {
    trimming.of("a || b || c || d || e").stays();
  }

  @Test public void orFalseOROf6WithoutBooleanA() {
    trimming.of("x || a || b || c || d || e").stays();
  }

  @Test public void orFalseOROf6WithoutBooleanWithParenthesis() {
    trimming.of("(a || b) || (c || (d || e))").stays();
  }

  @Test public void orFalseOROf6WithoutBooleanWithParenthesisA() {
    trimming.of("x || (a || b) || (c || (d || e))").stays();
  }

  @Test public void orFalseOROf6WithTwoFalse() {
    trimming.of("a || false || b || false || c || d || e").to("a || b || c || d || e");
  }

  @Test public void orFalseORSomethingWithFalse() {
    trimming.of("false || a || false").to("a");
  }

  @Test public void orFalseORSomethingWithTrue() {
    trimming.of("a || true").stays();
  }

  @Test public void orFalseORWithoutBoolean() {
    trimming.of("b || a").stays();
  }

  @Test public void orFalseProductIsNotANDDivOR() {
    trimming.of("2*a").stays();
  }

  @Test public void orFalseTrueAndTrueA() {
    trimming.of("true && true").to("true");
  }

  @Test public void overridenDeclaration() {
    trimming.of("int a = 3; a = f() ? 3 : 4;").to("int a = f() ? 3: 4;");
  }

  @Test public void paramAbbreviateBasic1() {
    trimming.of("void m(XMLDocument xmlDocument) {" + //
        "xmlDocument.exec(p);}").to("void m(XMLDocument d) {" + //
            "d.exec(p);}");
  }

  @Test public void paramAbbreviateBasic2() {
    trimming.of("int m(StringBuilder builder) {" + //
        "if(builder.exec())" + //
        "builder.clear();").to("int m(StringBuilder b) {" + //
            "if(b.exec())" + //
            "b.clear();");
  }

  @Test public void paramAbbreviateCollision() {
    trimming.of("void m(Expression exp, Expression expresssion) { }").to("void m(Expression e, Expression expresssion) { }");
  }

  @Test public void paramAbbreviateConflictingWithLocal1() {
    trimming.of("void m(String string) {" + //
        "String s = null;" + //
        "string.substring(s, 2, 18);}").to("void m(String string){string.substring(null,2,18);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal1Simplified() {
    trimming.of("void m(String string) {" + //
        "String s = X;" + //
        "string.substring(s, 2, 18);}").to("void m(String string){string.substring(X,2,18);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal1SimplifiedFurther() {
    trimming.of("void m(String string) {" + //
        "String s = X;" + //
        "string.f(s);}").to("void m(String string){string.f(X);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal2() {
    trimming.of("TCPConnection conn(TCPConnection tcpCon) {" + //
        " UDPConnection c = new UDPConnection(57);" + //
        " if(tcpCon.isConnected()) " + //
        "   c.disconnect();}").to("TCPConnection conn(TCPConnection tcpCon){" //
            + " if(tcpCon.isConnected())" //
            + "   (new UDPConnection(57)).disconnect();"//
            + "}");
  }

  @Test public void paramAbbreviateConflictingWithMethodName() {
    trimming.of("void m(BitmapManipulator bitmapManipulator) {" + //
        "bitmapManipulator.x().y();").stays();
  }

  @Test public void paramAbbreviateMultiple() {
    trimming.of("void m(StringBuilder stringBuilder, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
        "stringBuilder.clear();" + //
        "xmlDocument.open(stringBuilder.toString());" + //
        "dog.eat(xmlDocument.asEdible(cat));}").to("void m(StringBuilder b, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
            "b.clear();" + //
            "xmlDocument.open(b.toString());" + //
            "dog.eat(xmlDocument.asEdible(cat));}");
  }

  @Test public void paramAbbreviateNestedMethod() {
    trimming.of("void f(Iterator iterator) {" + //
        "iterator = new Iterator<Object>() {" + //
        "int i = 0;" + //
        "@Override public boolean hasNext() { return false; }" + //
        "@Override public Object next() { return null; } };").to("void f(Iterator i) {" + //
            "i = new Iterator<Object>() {" + //
            "int i = 0;" + //
            "@Override public boolean hasNext() { return false; }" + //
            "@Override public Object next() { return null; } };");
  }

  @Test public void parenthesizeOfpushdownTernary() {
    trimming.of("a ? b+x+e+f:b+y+e+f").to("b+(a ? x : y)+e+f");
  }

  @Test public void postDecreementReturn() {
    trimming.of("a--; return a;").to("--a;return a;");
  }

  @Test public void postDecremntInFunctionCall() {
    trimming.of("f(a++, i--, b++, ++b);").stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopCondition() {
    trimming.of("for (int s = i; ++i; ++s);").stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopInitializer() {
    trimming.of("for (int s = i++; i < 10; ++s);").stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnVariableDeclaration() {
    // We expect to print 2, but ++s will make it print 3
    trimming.of("int s = 2;" + //
        "int n = s++;" + //
        "S.out.print(n);").to("int s=2;S.out.print(s++);");
  }

  @Test public void postIncrementInFunctionCall() {
    trimming.of("f(i++);").stays();
  }

  @Test public void postIncrementReturn() {
    trimming.of("a++; return a;").to("++a;return a;");
  }

  @Test public void preDecreementReturn() {
    trimming.of("--a.b.c; return a.b.c;").to("return--a.b.c;");
  }

  @Test public void preDecrementReturn() {
    trimming.of("--a; return a;").to("return --a;");
  }

  @Test public void preDecrementReturn1() {
    trimming.of("--this.a; return this.a;").to("return --this.a;");
  }

  @Test public void prefixToPosfixIncreementSimple() {
    trimming.of("i++").to("++i");
  }

  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    azzert.that(s, iz("{" + from + "}"));
    assert s != null;
    final PostfixExpression e = extract.findFirstPostfix(s);
    assert e != null;
    azzert.that(e, iz("i--"));
    final ASTNode parent = e.getParent();
    assert parent != null;
    azzert.that(parent, iz(from));
    azzert.that(parent, is(not(instanceOf(Expression.class))));
    azzert.that(new PostfixToPrefix().claims(e), is(true));
    azzert.that(new PostfixToPrefix().canMake(e), is(true));
    final Expression r = new PostfixToPrefix().replacement(e);
    azzert.that(r, iz("--i"));
    trimming.of(from).to("for(int i=0;i<100;--i)--i;");
  }

  @Test public void prefixToPostfixIncreement() {
    trimming.of("for (int i = 0; i < 100; i++) i++;").to("for(int i=0;i<100;++i)++i;");
  }

  @Test public void preIncrementReturn() {
    trimming.of("++a; return a;").to("return ++a;");
  }

  @Test public void pushdowConditionalActualExampleFirstPass() {
    trimming
        .of("" //
            + "return determineEncoding(bytes) == Encoding.B " //
            + "? f((ENC_WORD_PREFIX + mimeCharset + B), text, charset, bytes)\n" //
            + ": f((ENC_WORD_PREFIX + mimeCharset + Q), text, charset, bytes)\n" //
            + ";")
        .to("" //
            + "return f("//
            + "   determineEncoding(bytes)==Encoding.B" //
            + "     ? ENC_WORD_PREFIX+mimeCharset+B" //
            + "     : ENC_WORD_PREFIX+mimeCharset+Q," //
            + "text,charset,bytes)" //
            + ";" //
            + "");
  }

  @Test public void pushdowConditionalActualExampleSecondtest() {
    trimming
        .of("" //
            + "return f("//
            + "   determineEncoding(bytes)==Encoding.B" //
            + "     ? ENC_WORD_PREFIX+mimeCharset+B" //
            + "     : ENC_WORD_PREFIX+mimeCharset+Q," //
            + "text,charset,bytes)" //
            + ";" //
            + "")
        .to("" //
            + "return f("//
            + "  ENC_WORD_PREFIX + mimeCharset + " //
            + " (determineEncoding(bytes)==Encoding.B ?B : Q)," //
            + "   text,charset,bytes" //
            + ")" //
            + ";" //
            + "");
  }

  @Test public void pushdownNot2LevelNotOfFalse() {
    trimming.of("!!false").to("false");
  }

  @Test public void pushdownNot2LevelNotOfTrue() {
    trimming.of("!!true").to("true");
  }

  @Test public void pushdownNotActualExample() {
    trimming.of("!inRange(m, e)").stays();
  }

  @Test public void pushdownNotDoubleNot() {
    trimming.of("!!f()").to("f()");
  }

  @Test public void pushdownNotDoubleNotDeeplyNested() {
    trimming.of("!(((!f())))").to("f()");
  }

  @Test public void pushdownNotDoubleNotNested() {
    trimming.of("!(!f())").to("f()");
  }

  @Test public void pushdownNotEND() {
    trimming.of("a&&b").stays();
  }

  @Test public void pushdownNotMultiplication() {
    trimming.of("a*b").stays();
  }

  @Test public void pushdownNotNotOfAND() {
    trimming.of("!(a && b && c)").to("!a || !b || !c");
  }

  @Test public void pushdownNotNotOfAND2() {
    trimming.of("!(f() && f(5))").to("!f() || !f(5)");
  }

  @Test public void pushdownNotNotOfANDNested() {
    trimming.of("!(f() && (f(5)))").to("!f() || !f(5)");
  }

  @Test public void pushdownNotNotOfEQ() {
    trimming.of("!(3 == 5)").to("3 != 5");
  }

  @Test public void pushdownNotNotOfEQNested() {
    trimming.of("!((((3 == 5))))").to("3 != 5");
  }

  @Test public void pushdownNotNotOfFalse() {
    trimming.of("!false").to("true");
  }

  @Test public void pushdownNotNotOfGE() {
    trimming.of("!(3 >= 5)").to("3 < 5");
  }

  @Test public void pushdownNotNotOfGT() {
    trimming.of("!(3 > 5)").to("3 <= 5");
  }

  @Test public void pushdownNotNotOfLE() {
    trimming.of("!(3 <= 5)").to("3 > 5");
  }

  @Test public void pushdownNotNotOfLT() {
    trimming.of("!(3 < 5)").to("3 >= 5");
  }

  @Test public void pushdownNotNotOfNE() {
    trimming.of("!(3 != 5)").to("3 == 5");
  }

  @Test public void pushdownNotNotOfOR() {
    trimming.of("!(a || b || c)").to("!a && !b && !c");
  }

  @Test public void pushdownNotNotOfOR2() {
    trimming.of("!(f() || f(5))").to("!f() && !f(5)");
  }

  @Test public void pushdownNotNotOfTrue() {
    trimming.of("!true").to("false");
  }

  @Test public void pushdownNotNotOfTrue2() {
    trimming.of("!!true").to("true");
  }

  @Test public void pushdownNotNotOfWrappedOR() {
    trimming.of("!((a) || b || c)").to("!a && !b && !c");
  }

  @Test public void pushdownNotOR() {
    trimming.of("a||b").stays();
  }

  @Test public void pushdownNotSimpleNot() {
    trimming.of("!a").stays();
  }

  @Test public void pushdownNotSimpleNotOfFunction() {
    trimming.of("!f(a)").stays();
  }

  @Test public void pushdownNotSummation() {
    trimming.of("a+b").stays();
  }

  @Test public void pushdownTernaryActualExample() {
    trimming.of("next < values().length").stays();
  }

  @Test public void pushdownTernaryActualExample2() {
    trimming.of("!inRange(m, e) ? true : inner.go(r, e)").to("!inRange(m, e) || inner.go(r, e)");
  }

  @Test public void pushdownTernaryAlmostIdentical2Addition() {
    trimming.of("a ? b+d :b+ c").to("b+(a ? d : c)");
  }

  @Test public void pushdownTernaryAlmostIdentical3Addition() {
    trimming.of("a ? b+d +x:b+ c + x").to("b+(a ? d : c) + x");
  }

  @Test public void pushdownTernaryAlmostIdentical4AdditionLast() {
    trimming.of("a ? b+d+e+y:b+d+e+x").to("b+d+e+(a ? y : x)");
  }

  @Test public void pushdownTernaryAlmostIdentical4AdditionSecond() {
    trimming.of("a ? b+x+e+f:b+y+e+f").to("b+(a ? x : y)+e+f");
  }

  @Test public void pushdownTernaryAlmostIdenticalAssignment() {
    trimming.of("a ? (b=c) :(b=d)").to("b = a ? c : d");
  }

  @Test public void pushdownTernaryAlmostIdenticalFunctionCall() {
    trimming.of("a ? f(b) :f(c)").to("f(a ? b : c)");
  }

  @Test public void pushdownTernaryAlmostIdenticalMethodCall() {
    trimming.of("a ? y.f(b) :y.f(c)").to("y.f(a ? b : c)");
  }

  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall1Div2() {
    trimming.of("a ? f(b,x) :f(c,x)").to("f(a ? b : c,x)");
  }

  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall2Div2() {
    trimming.of("a ? f(x,b) :f(x,c)").to("f(x,a ? b : c)");
  }

  @Test public void pushdownTernaryAMethodCallDistinctReceiver() {
    trimming.of("a ? x.f(c) : y.f(d)").stays();
  }

  @Test public void pushdownTernaryDifferentTargetFieldRefernce() {
    trimming.of("a ? 1 + x.a : 1 + y.a").to("1+(a ? x.a : y.a)");
  }

  @Test public void pushdownTernaryFieldReferneceShort() {
    trimming.of("a ? R.b.c : R.b.d").stays();
  }

  @Test public void pushdownTernaryFunctionCall() {
    trimming.of("a ? f(b,c) : f(c)").to("!a?f(c):f(b,c)");
  }

  @Test public void pushdownTernaryFX() {
    trimming.of("a ? false : c").to("!a && c");
  }

  @Test public void pushdownTernaryIdenticalAddition() {
    trimming.of("a ? b+d :b+ d").to("b+d");
  }

  @Test public void pushdownTernaryIdenticalAdditionWtihParenthesis() {
    trimming.of("a ? (b+d) :(b+ d)").to("b+d");
  }

  @Test public void pushdownTernaryIdenticalAssignment() {
    trimming.of("a ? (b=c) :(b=c)").to("b = c");
  }

  @Test public void pushdownTernaryIdenticalAssignmentVariant() {
    trimming.of("a ? (b=c) :(b=d)").to("b=a?c:d");
  }

  @Test public void pushdownTernaryIdenticalFunctionCall() {
    trimming.of("a ? f(b) :f(b)").to("f(b)");
  }

  @Test public void pushdownTernaryIdenticalIncrement() {
    trimming.of("a ? b++ :b++").to("b++");
  }

  @Test public void pushdownTernaryIdenticalMethodCall() {
    trimming.of("a ? y.f(b) :y.f(b)").to("y.f(b)");
  }

  @Test public void pushdownTernaryIntoConstructor1Div1Location() {
    trimming.of("a.equal(b) ? new S(new Integer(4)) : new S(new Ineger(3))").to("new S(a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void pushdownTernaryIntoConstructor1Div3() {
    trimming.of("a.equal(b) ? new S(new Integer(4),a,b) : new S(new Ineger(3),a,b)")
        .to("new S(a.equal(b)? new Integer(4): new Ineger(3), a, b)");
  }

  @Test public void pushdownTernaryIntoConstructor2Div3() {
    trimming.of("a.equal(b) ? new S(a,new Integer(4),b) : new S(a, new Ineger(3), b)")
        .to("new S(a,a.equal(b)? new Integer(4): new Ineger(3),b)");
  }

  @Test public void pushdownTernaryIntoConstructor3Div3() {
    trimming.of("a.equal(b) ? new S(a,b,new Integer(4)) : new S(a,b,new Ineger(3))")
        .to("new S(a, b, a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void pushdownTernaryIntoConstructorNotSameArity() {
    trimming.of("a ? new S(a,new Integer(4),b) : new S(new Ineger(3))").to(
        "!a?new S(new Ineger(3)):new S(a,new Integer(4),b)                                                                                                                  ");
  }

  @Test public void pushdownTernaryIntoPrintln() {
    trimming.of("    if (s.equals(t))\n"//
        + "      S.out.println(Hey + res);\n"//
        + "    else\n"//
        + "      S.out.println(Ho + x + a);").to("S.out.println(s.equals(t)?Hey+res:Ho+x+a);");
  }

  @Test public void pushdownTernaryLongFieldRefernece() {
    trimming.of("externalImage ? R.string.webview_contextmenu_image_download_action : R.string.webview_contextmenu_image_save_action")
        .to("!externalImage ? R.string.webview_contextmenu_image_save_action : R.string.webview_contextmenu_image_download_action");
  }

  @Test public void pushdownTernaryMethodInvocationFirst() {
    trimming.of("a?b():c").to("!a?c:b()");
  }

  @Test public void pushdownTernaryNoBoolean() {
    trimming.of("a?b:c").stays();
  }

  @Test public void pushdownTernaryNoReceiverReceiver() {
    trimming.of("a < b ? f() : a.f()").stays();
  }

  @Test public void pushdownTernaryNotOnMINUS() {
    trimming.of("a ? -c :-d").stays();
  }

  @Test public void pushdownTernaryNotOnMINUSMINUS1() {
    trimming.of("a ? --c :--d").stays();
  }

  @Test public void pushdownTernaryNotOnMINUSMINUS2() {
    trimming.of("a ? c-- :d--").stays();
  }

  @Test public void pushdownTernaryNotOnNOT() {
    trimming.of("a ? !c :!d").stays();
  }

  @Test public void pushdownTernaryNotOnPLUS() {
    trimming.of("a ? +x : +y").to("a ? x : y").stays();
  }

  @Test public void pushdownTernaryNotOnPLUSPLUS() {
    trimming.of("a ? x++ :y++").stays();
  }

  @Test public void pushdownTernaryNotSameFunctionInvocation() {
    trimming.of("a?b(x):d(x)").stays();
  }

  @Test public void pushdownTernaryNotSameFunctionInvocation2() {
    trimming.of("a?x.f(x):x.d(x)").stays();
  }

  @Test public void pushdownTernaryOnMethodCall() {
    trimming.of("a ? y.f(c,b) :y.f(c)").to("!a?y.f(c):y.f(c,b)");
  }

  @Test public void pushdownTernaryParFX() {
    trimming.of("a ?( false):true").to("!a && true");
  }

  @Test public void pushdownTernaryParTX() {
    trimming.of("a ? (((true ))): c").to("a || c");
  }

  @Test public void pushdownTernaryParXF() {
    trimming.of("a ? b : (false)").to("a && b");
  }

  @Test public void pushdownTernaryParXT() {
    trimming.of("a ? b : ((true))").to("!a || b");
  }

  @Test public void pushdownTernaryReceiverNoReceiver() {
    trimming.of("a < b ? a.f() : f()").to("a>=b?f():a.f()");
  }

  @Test public void pushdownTernaryToClasConstrctor() {
    trimming.of("a ? new B(a,b,c) : new B(a,x,c)").to("new B(a,a ? b : x ,c)");
  }

  @Test public void pushdownTernaryToClasConstrctorTwoDifferenes() {
    trimming.of("a ? new B(a,b,c) : new B(a,x,y)").stays();
  }

  @Test public void pushdownTernaryToClassConstrctorNotSameNumberOfArgument() {
    trimming.of("a ? new B(a,b) : new B(a,b,c)").stays();
  }

  @Test public void pushdownTernaryTX() {
    trimming.of("a ? true : c").to("a || c");
  }

  @Test public void pushdownTernaryXF() {
    trimming.of("a ? b : false").to("a && b");
  }

  @Test public void pushdownTernaryXT() {
    trimming.of("a ? b : true").to("!a || b");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar01() {
    trimming
        .of(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   S.out.println(res.j);   return res; ")
        .to(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   S.out.println($.j);   return $; ");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar02() {
    trimming
        .of(" int res = blah.length();   if (blah.contains(0xDEAD))    return res * 2;   if (res % 2 ==0)    return ++res;   if (blah.startsWith(\"y\")) {    return y(res);   int x = res + 6;   if (x>1)    return res + x;   res -= 1;   return res; ")
        .to(" int $ = blah.length();   if (blah.contains(0xDEAD))    return $ * 2;   if ($ % 2 ==0)    return ++$;   if (blah.startsWith(\"y\")) {    return y($);   int x = $ + 6;   if (x>1)    return $ + x;   $ -= 1;   return $; ");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar03() {
    trimming
        .of(" public BlahClass(int i) {    j = 2*i;      public final int j;   public int yada7(final String blah) {   final BlahClass res = new BlahClass(blah.length());   if (blah.contains(0xDEAD))    return res.j;   int x = blah.length()/2;   if (x==3)    return x;   x = y(res.j - x);   return x; ")
        .to(" public BlahClass(int i) {    j = 2*i;      public final int j;   public int yada7(final String blah) {   final BlahClass res = new BlahClass(blah.length());   if (blah.contains(0xDEAD))    return res.j;   int $ = blah.length()/2;   if ($==3)    return $;   $ = y(res.j - $);   return $; ");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar04() {
    trimming.of("int res = 0;   String $ = blah + known;   y(res + $.length());   return res + $.length();").stays();
  }

  @Ignore @Test public void reanmeReturnVariableToDollar05() {
    trimming
        .of("  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private void doStuff(final BlahClass res) {     S.out.println(res.j);   S.out.println(res.j);   return res; ")
        .to("  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass($.j);     S.out.println(res2.j);     doStuff(res2);        private void doStuff(final BlahClass res) {     S.out.println(res.j);   S.out.println($.j);   return $; ");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar06() {
    trimming
        .of("  j = 2*i;   }      public final int j;    public void yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private int doStuff(final BlahClass r) {     final BlahClass res = new BlahClass(r.j);     return res.j + 1;   S.out.println(res.j); ")
        .to("  j = 2*i;   }      public final int j;    public void yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private int doStuff(final BlahClass r) {     final BlahClass $ = new BlahClass(r.j);     return $.j + 1;   S.out.println(res.j); ");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar07() {
    trimming
        .of("  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println(res.j);   return res; ")
        .to("  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println($.j);   return $; ");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar08() {
    trimming
        .of(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   if (res.j == 0)    return null;   S.out.println(res.j);   return res; ")
        .to(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   if ($.j == 0)    return null;   S.out.println($.j);   return $; ");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar09() {
    trimming
        .of(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   if (res.j == 0)    return null;   S.out.println(res.j);   return null;")
        .stays();
  }

  @Ignore @Test public void reanmeReturnVariableToDollar10() {
    trimming
        .of("@Override public IMarkerResolution[] getResolutions(final IMarker m) {   try {    final Spartanization s = All.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY)); ")
        .to("@Override public IMarkerResolution[] getResolutions(final IMarker m) {   try {    final Spartanization $ = All.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY)); ");
  }

  @Ignore @Test public void reanmeReturnVariableToDollar11() {
    trimming.of("").stays();
  }

  @Test public void redundantButNecessaryBrackets1() {
    trimming.of("" //
        + "if (windowSize != INFINITE_WINDOW) {\n" //
        + "  if (getN() == windowSize)\n" //
        + "    eDA.addElementRolling(v);\n" //
        + "  else if (getN() < windowSize)\n" //
        + "    eDA.addElement(v);\n" //
        + "} else {\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  eDA.addElement(v);\n" //
        + "}").stays();
  }

  @Test public void redundantButNecessaryBrackets2() {
    trimming.of("" //
        + "if (windowSize != INFINITE_WINDOW) {\n" //
        + "  if (getN() == windowSize)\n" //
        + "    eDA.addElementRolling(v);\n" //
        + "} else {\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  System.out.println('!');\n" //
        + "  eDA.addElement(v);\n" //
        + "}").stays();
  }

  @Test public void redundantButNecessaryBrackets3() {
    trimming.of("" //
        + "if (b1)\n" //
        + "  if (b2)\n" //
        + "    print1('!');\n" //
        + "  else {\n" //
        + "    if (b3)\n" //
        + "      print3('#');\n" //
        + "  }\n" //
        + "else {\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "  print4('$');\n" //
        + "}").stays();
  }

  @Test public void removeSuper() {
    trimming.of("class T {T(){super();}}").to("class T { T() { }}");
  }

  @Test public void removeSuperWithArgument() {
    trimming.of("class T { T() { super(a); a();}}").stays();
  }

  @Test public void removeSuperWithStatemen() {
    trimming.of("class T { T() { super(); a++;}}").to("class T { T() { ++a;}}");
  }

  @Test public void renameToDollarActual() {
    trimming.of(//
        "        public static DeletePolicy fromInt(int initialSetting) {\n" + //
            "            for (DeletePolicy policy: values()) {\n" + //
            "                if (policy.setting == initialSetting) {\n" + //
            "                    return policy;\n" + //
            "                }\n" + //
            "            }\n" + //
            "            throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");\n" + //
            "        }")
        .to(//
            "        public static DeletePolicy fromInt(int initialSetting) {\n" + //
                "            for (DeletePolicy $: values()) {\n" + //
                "                if ($.setting == initialSetting) {\n" + //
                "                    return $;\n" + //
                "                }\n" + //
                "            }\n" + //
                "            throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");\n" + //
                "        }");
  }

  @Test public void renameToDollarEnhancedFor() {
    trimming.of("int f() { for (int a: as) return a; }")//
        .to(" int f() {for(int $:as)return $;}");
  }

  @Test public void replaceInitializationInReturn() {
    trimming.of("int a = 3; return a + 4;").to("return 3 + 4;");
  }

  @Test public void replaceTwiceInitializationInReturn() {
    trimming.of("int a = 3; return a + 4 << a;").to("return 3 + 4 << 3;");
  }

  @Test public void rightSimplificatioForNulNNVariableReplacement() {
    final InfixExpression e = i("null != a");
    final Wring<InfixExpression> w = Toolbox.defaultInstance().find(e);
    azzert.notNull(w);
    azzert.aye(w.claims(e));
    azzert.aye(w.canMake(e));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) w).replacement(e);
    assert replacement != null;
    azzert.that("" + replacement, is("a != null"));
  }

  @Test public void rightSipmlificatioForNulNNVariable() {
    azzert.that(Toolbox.defaultInstance().find(i("null != a")), instanceOf(InfixComparisonSpecific.class));
  }

  @Ignore @Test public void sameAssignmentDifferentTypes() {
    trimming.of("" //
        + "public void f() {\n" //
        + "  double x;\n" //
        + "  int y;\n" //
        + "  x = 0;\n" //
        + "  y = 0;\n" //
        + "}").stays();
  }

  @Test public void sequencerFirstInElse() {
    trimming.of("if (a) {b++; c++; ++d;} else { f++; g++; return x;}").to("if (!a) {f++; g++; return x;} b++; c++; ++d; ");
  }

  @Test public void shorterChainParenthesisComparison() {
    trimming.of("a == b == c").stays();
  }

  @Test public void shorterChainParenthesisComparisonLast() {
    trimming.of("b == a * b * c * d * e * f * g * h == a").stays();
  }

  @Test public void shortestBranchIfWithComplexNestedIf3() {
    trimming.of("if (a) {f(); g(); h();} else if (a) ++i; else ++j;").stays();
  }

  @Test public void shortestBranchIfWithComplexNestedIf4() {
    trimming.of("if (a) {f(); g(); h(); ++i;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf5() {
    trimming.of("if (a) {f(); g(); h(); ++i; f();} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf6() {
    trimming.of("if (a) {f(); g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;")
        .to("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf7() {
    trimming.of("if (a) {f(); ++i; g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;")
        .to("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf8() {
    trimming.of("if (a) {f(); ++i; g(); h(); ++i; u++; f(); j++;} else if (a) ++i; else j++;")
        .to("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;u++;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIfPlain() {
    trimming.of("if (a) {f(); g(); h();} else { i++; j++;}").to("if(!a){i++;j++;}else{f();g();h();}");
  }

  @Test public void shortestBranchIfWithComplexSimpler() {
    trimming.of("if (a) {f(); g(); h();} else  i++; j++;").to("if(!a)i++;else{f();g();h();}++j;");
  }

  @Test public void shortestBranchInIf() {
    trimming.of("   int a=0;\n" + //
        "   if (s.equals(known)){\n" + //
        "     S.console();\n" + //
        "   } else {\n" + //
        "     a=3;\n" + //
        "   }\n" + //
        "").to("int a=0; if(!s.equals(known))a=3;else S.console();");
  }

  @Test public void shortestFirstAlignment() {
    trimming.of("n.isSimpleName() ? (SimpleName) n //\n" + //
        "            : n.isQualifiedName() ? ((QualifiedName) n).getName() //\n" + //
        "                : null").stays();//
  }

  @Test public void shortestFirstAlignmentShortened() {
    trimming.of("n.isF() ? (SimpleName) n \n" + //
        "            : n.isG() ? ((QualifiedName) n).getName() \n" + //
        "                : null").stays();//
  }

  @Test public void shortestFirstAlignmentShortenedFurther() {
    trimming.of("n.isF() ? (A) n : n.isG() ? ((B) n).f() \n" + //
        "                : null").stays();//
  }

  @Test public void shortestFirstAlignmentShortenedFurtherAndFurther() {
    trimming.of("n.isF() ? (A) n : n.isG() ? (B) n :  null").stays();//
  }

  @Test public void shortestIfBranchFirst01() {
    trimming
        .of(""//
            + "if (s.equals(0xDEAD)) {\n"//
            + " int res=0; "//
            + " for (int i=0; i<s.length(); ++i)     "//
            + " if (s.charAt(i)=='a')      "//
            + "   res += 2;    "//
            + "} else "//
            + " if (s.charAt(i)=='d') "//
            + "  res -= 1;  "//
            + "return res;  ")
        .to(""//
            + "if (!s.equals(0xDEAD)) {"//
            + " if(s.charAt(i)=='d')"//
            + "  res-=1;"//
            + "} else {"//
            + "  int res=0;"//
            + "  for(int i=0;i<s.length();++i)"//
            + "   if(s.charAt(i)=='a')"//
            + "     res+=2;"//
            + " }"//
            + " return res;");
  }

  @Test public void shortestIfBranchFirst02() {
    trimming
        .of("" //
            + "if (!s.equals(0xDEAD)) { "//
            + " int res=0;"//
            + " for (int i=0;i<s.length();++i)     "//
            + "   if (s.charAt(i)=='a')      "//
            + "     res += 2;"//
            + "   else "//
            + "  if (s.charAt(i)=='d')      "//
            + "       res -= 1;"//
            + "  return res;"//
            + "} else {    "//
            + " return 8;"//
            + "}")
        .to("" //
            + " if (s.equals(0xDEAD)) \n" + //
            "    return 8;" + //
            "      int res = 0;\n" + //
            "      for (int i = 0;i < s.length();++i)\n" + //
            "       if (s.charAt(i) == 'a')\n" + //
            "          res += 2;\n" + //
            "        else " + //
            "       if (s.charAt(i) == 'd')\n" + //
            "          res -= 1;\n" + //
            "      return res;\n");
  }

  @Test public void shortestIfBranchFirst02a() {
    trimming.of("" + //
        " if (!s.equals(0xDEAD)) {\n" + //
        "      int res = 0;\n" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          res += 2;\n" + //
        "        else " + //
        "       if (s.charAt(i) == 'd')\n" + //
        "          res -= 1;\n" + //
        "      return res;\n" + //
        "    }\n" + //
        "    return 8;" + //
        "").to(" if (s.equals(0xDEAD)) "//
            + "return 8; " + //
            "      int res = 0;\n" + //
            "      for (int i = 0;i < s.length();++i)\n" + //
            "       if (s.charAt(i) == 'a')\n" + //
            "          res += 2;\n" + //
            "        else "//
            + "       if (s.charAt(i) == 'd')\n" + //
            "          res -= 1;\n" + //
            "      return res;\n" + //
            "");
  }

  @Test public void shortestIfBranchFirst02b() {
    trimming.of("" + //
        "      int res = 0;\n" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          res += 2;\n" + //
        "        else " + //
        "       if (s.charAt(i) == 'd')\n" + //
        "          --res;\n" + //
        "      return res;\n" + //
        "").stays();
  }

  @Test public void shortestIfBranchFirst02c() {
    final CompilationUnit u = GuessedContext.statement_or_something_that_may_occur_in_a_method.intoCompilationUnit("" + //
        "      int res = 0;\n" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          res += 2;\n" + //
        "        else " + //
        "       if (s.charAt(i) == 'd')\n" + //
        "          res -= 1;\n" + //
        "      return res;\n" + //
        ""//
    );
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    assert f != null;
    azzert.that(f, iz(" res = 0"));
    azzert.that(extract.nextStatement(f),
        iz(" for (int i = 0;i < s.length();++i)\n"//
            + "       if (s.charAt(i) == 'a')\n"//
            + "          res += 2;\n"//
            + "        else "//
            + "       if (s.charAt(i) == 'd')\n"//
            + "          res -= 1;\n"));
  }

  @Test public void shortestIfBranchWithFollowingCommandsSequencer() {
    trimming.of("" + //
        "if (a) {" + //
        " f();" + //
        " g();" + //
        " h();" + //
        " return a;" + //
        "}\n" + //
        "return c;").to("" + //
            "if (!a) return c;" + //
            "f();" + //
            "g();" + //
            "h();" + //
            "return a;" + //
            "");
  }

  @Test public void shortestOperand01() {
    trimming.of("x + y > z").stays();
  }

  @Test public void shortestOperand02() {
    trimming.of("k = k + 4;if (2 * 6 + 4 == k) return true;").stays();
  }

  @Test public void shortestOperand05() {
    trimming
        .of("    W s = new W(\"bob\");\n" + //
            "    return s.l(hZ).l(\"-ba\").toString() == \"bob-ha-banai\";")
        .to("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }

  @Ignore("bug") @Test public void shortestOperand09() {
    trimming.of("return 2 - 4 < 50 - 20 - 10 - 5;").to("return 2 - 4 < 50 - 5 - 10 - 20 ;");
  }

  @Test public void shortestOperand10() {
    trimming.of("return b == true;").to("return b;");
  }

  @Test public void shortestOperand11() {
    trimming.of("int h,u,m,a,n;return b == true && n + a > m - u || h > u;").to("int h,u,m,a,n;return b&&a+n>m-u||h>u;");
  }

  @Test public void shortestOperand12() {
    trimming.of("int k = 15; return 7 < k; ").to("return 7<15;");
  }

  @Test public void shortestOperand13() {
    trimming.of("return (2 > 2 + a) == true;").to("return 2>a+2;");
  }

  @Test public void shortestOperand13a() {
    trimming.of("(2 > 2 + a) == true").to("2>a+2 ");
  }

  @Test public void shortestOperand13b() {
    trimming.of("(2) == true").to("2 ");
  }

  @Test public void shortestOperand13c() {
    trimming.of("2 == true").to("2 ");
  }

  @Test public void shortestOperand14() {
    trimming.of("Integer t = new Integer(5);   return (t.toString() == null);    ").to("return((new Integer(5)).toString()==null);");
  }

  @Test public void shortestOperand17() {
    trimming.of("5 ^ a.getNum()").to("a.getNum() ^ 5");
  }

  @Test public void shortestOperand19() {
    trimming.of("k.get().operand() ^ a.get()").to("a.get() ^ k.get().operand()");
  }

  @Test public void shortestOperand20() {
    trimming.of("k.get() ^ a.get()").to("a.get() ^ k.get()");
  }

  @Test public void shortestOperand22() {
    trimming.of("return f(a,b,c,d,e) + f(a,b,c,d) + f(a,b,c) + f(a,b) + f(a) + f();").stays();
  }

  @Test public void shortestOperand23() {
    trimming.of("return f() + \".\";     }").stays();
  }

  @Test public void shortestOperand24() {
    trimming.of("f(a,b,c,d) & 175 & 0").to("f(a,b,c,d) & 0 & 175");
  }

  @Test public void shortestOperand25() {
    trimming.of("f(a,b,c,d) & bob & 0 ").to("bob & f(a,b,c,d) & 0");
  }

  @Test public void shortestOperand27() {
    trimming.of("return f(a,b,c,d) + f(a,b,c) + f();     } ").stays();
  }

  @Test public void shortestOperand28() {
    trimming.of("return f(a,b,c,d) * f(a,b,c) * f();").to("return f()*f(a,b,c)*f(a,b,c,d);");
  }

  @Test public void shortestOperand29() {
    trimming.of("f(a,b,c,d) ^ f() ^ 0").to("f() ^ f(a,b,c,d) ^ 0");
  }

  @Test public void shortestOperand30() {
    trimming.of("f(a,b,c,d) & f()").to("f() & f(a,b,c,d)");
  }

  @Test public void shortestOperand31() {
    trimming.of("return f(a,b,c,d) | \".\";     }").stays();
  }

  @Test public void shortestOperand32() {
    trimming.of("return f(a,b,c,d) && f();     }").stays();
  }

  @Test public void shortestOperand33() {
    trimming.of("return f(a,b,c,d) || f();     }").stays();
  }

  @Test public void shortestOperand34() {
    trimming.of("return f(a,b,c,d) + someVar; ").stays();
  }

  @Test public void shortestOperand37() {
    trimming.of("return sansJavaExtension(f) + n + \".\"+ extension(f);").stays();
  }

  @Test public void simpleBooleanMethod() {
    trimming.of("boolean f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .to("boolean f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void simplifyBlockComplexEmpty0() {
    trimming.of("{;}").to("/* empty */    ");
  }

  @Test public void simplifyBlockComplexEmpty0A() {
    trimming.of("{}").to("/* empty */");
  }

  @Test public void simplifyBlockComplexEmpty0B() {
    trimming.of("{;}").to("/* empty */");
  }

  @Test public void simplifyBlockComplexEmpty0C() {
    trimming.of("{{;}}").to("/* empty */");
  }

  @Test public void simplifyBlockComplexEmpty0D() {
    trimming.of("{;;;{;;;}{;}}").to("/* empty */    ");
  }

  @Test public void simplifyBlockComplexEmpty1() {
    trimming.of("{;;{;{{}}}{}{};}").to("/* empty */ ");
  }

  @Test public void simplifyBlockComplexSingleton() {
    assertSimplifiesTo("{;{{;;return b; }}}", "return b;", new BlockSimplify(), GuessedContext.statement_or_something_that_may_occur_in_a_method);
  }

  @Test public void simplifyBlockDeeplyNestedReturn() {
    assertSimplifiesTo("{{{;return c;};;};}", "return c;", new BlockSimplify(), GuessedContext.statement_or_something_that_may_occur_in_a_method);
  }

  @Test public void simplifyBlockEmpty() {
    assertSimplifiesTo("{;;}", "", new BlockSimplify(), GuessedContext.statement_or_something_that_may_occur_in_a_method);
  }

  @Test public void simplifyBlockExpressionVsExpression() {
    trimming.of("6 - 7 < a * 3").to("6 - 7 < 3 * a");
  }

  @Test public void simplifyBlockLiteralVsLiteral() {
    trimming.of("if (a) return b; else c();").to("if(a)return b;c();");
  }

  @Test public void simplifyBlockThreeStatements() {
    assertSimplifiesTo("{i++;{{;;return b; }}j++;}", "i++;return b;j++;", new BlockSimplify(),
        GuessedContext.statement_or_something_that_may_occur_in_a_method);
  }

  @Test public void simplifyLogicalNegationNested() {
    trimming.of("!((a || b == c) && (d || !(!!c)))").to("!a && b != c || !d && c");
  }

  @Test public void simplifyLogicalNegationNested1() {
    trimming.of("!(d || !(!!c))").to("!d && c");
  }

  @Test public void simplifyLogicalNegationNested2() {
    trimming.of("!(!d || !!!c)").to("d && c");
  }

  @Test public void simplifyLogicalNegationOfAnd() {
    trimming.of("!(f() && f(5))").to("!f() || !f(5)");
  }

  @Test public void simplifyLogicalNegationOfEquality() {
    trimming.of("!(3 == 5)").to("3!=5");
  }

  @Test public void simplifyLogicalNegationOfGreater() {
    trimming.of("!(3 > 5)").to("3 <= 5");
  }

  @Test public void simplifyLogicalNegationOfGreaterEquals() {
    trimming.of("!(3 >= 5)").to("3 < 5");
  }

  @Test public void simplifyLogicalNegationOfInequality() {
    trimming.of("!(3 != 5)").to("3 == 5");
  }

  @Test public void simplifyLogicalNegationOfLess() {
    trimming.of("!(3 < 5)").to("3 >= 5");
  }

  @Test public void simplifyLogicalNegationOfLessEquals() {
    trimming.of("!(3 <= 5)").to("3 > 5");
  }

  @Test public void simplifyLogicalNegationOfMultipleAnd() {
    trimming.of("!(a && b && c)").to("!a || !b || !c");
  }

  @Test public void simplifyLogicalNegationOfMultipleOr() {
    trimming.of("!(a || b || c)").to("!a && !b && !c");
  }

  @Test public void simplifyLogicalNegationOfNot() {
    trimming.of("!!f()").to("f()");
  }

  @Test public void simplifyLogicalNegationOfOr() {
    trimming.of("!(f() || f(5))").to("!f() && !f(5)");
  }

  @Test public void sortAddition1() {
    trimming.of("1 + 2 - 3 - 4 + 5 / 6 - 7 + 8 * 9  + A> k + 4").to("8*9+1+2-3-4+5 / 6-7+A>k+4");
  }

  @Test public void sortAddition2() {
    trimming.of("1 + 2 < 3 & 7 + 4 > 2 + 1 || 6 - 7 < 2 + 1").to("1+2 <3&4+7>1+2||6-7<1+2");
  }

  @Test public void sortAddition3() {
    trimming.of("6 - 7 < 1 + 2").stays();
  }

  @Test public void sortAddition4() {
    trimming.of("a + 11 + 2 < 3 & 7 + 4 > 2 + 1").to("7 + 4 > 2 + 1 & a + 11 + 2 < 3");
  }

  @Test public void sortAdditionClassConstantAndLiteral() {
    trimming.of("1+A< 12").to("A+1<12");
  }

  @Test public void sortAdditionFunctionClassConstantAndLiteral() {
    trimming.of("1+A+f()< 12").to("f()+A+1<12");
  }

  @Test public void sortAdditionThreeOperands1() {
    trimming.of("1.0+2222+3").stays();
  }

  @Test public void sortAdditionThreeOperands2() {
    trimming.of("1.0+1+124+1").stays();
  }

  @Test public void sortAdditionThreeOperands3() {
    trimming.of("1+2F+33+142+1").stays();
  }

  @Test public void sortAdditionThreeOperands4() {
    trimming.of("1+2+'a'").stays();
  }

  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_a() {
    trimming.of("1111+211").to("211+1111");
  }

  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_b() {
    trimming.of("211+1111").stays();
  }

  @Test public void sortAdditionTwoOperands1() {
    trimming.of("1+2F").stays();
  }

  @Test public void sortAdditionTwoOperands2() {
    trimming.of("2.0+1").to("1+2.0");
  }

  @Test public void sortAdditionTwoOperands3() {
    trimming.of("1+2L").stays();
  }

  @Test public void sortAdditionTwoOperands4() {
    trimming.of("2L+1").to("1+2L");
  }

  @Test public void sortAdditionUncertain() {
    trimming.of("1+a").stays();
  }

  @Test public void sortAdditionVariableClassConstantAndLiteral() {
    trimming.of("1+A+a< 12").to("a+A+1<12");
  }

  @Test public void sortConstantMultiplication() {
    trimming.of("a*2").to("2*a");
  }

  @Test public void sortDivision() {
    trimming.of("2.1/34.2/1.0").to("2.1/1.0/34.2");
  }

  @Test public void sortDivisionLetters() {
    trimming.of("x/b/a").to("x/a/b");
  }

  @Test public void sortDivisionNo() {
    trimming.of("2.1/3").stays();
  }

  @Ignore("bug") @Test public void sortSubstraction() {
    trimming.of("1-c-b").to("1-b-c");
  }

  @Test public void sortThreeOperands1() {
    trimming.of("1.0*2222*3").stays();
  }

  @Test public void sortThreeOperands2() {
    trimming.of("1.0*11*124").stays();
  }

  @Test public void sortThreeOperands3() {
    trimming.of("2*2F*33*142").stays();
  }

  @Test public void sortThreeOperands4() {
    trimming.of("2*3*'a'").stays();
  }

  @Test public void sortTwoOperands0CheckThatWeSortByLength_a() {
    trimming.of("1111*211").to("211*1111");
  }

  @Test public void sortTwoOperands0CheckThatWeSortByLength_b() {
    trimming.of("211*1111").stays();
  }

  @Test public void sortTwoOperands1() {
    trimming.of("1F*2F").stays();
  }

  @Test public void sortTwoOperands2() {
    trimming.of("2.0*2").to("2*2.0");
  }

  @Test public void sortTwoOperands3() {
    trimming.of("2*3L").stays();
  }

  @Test public void sortTwoOperands4() {
    trimming.of("2L*1L").to("1L*2L");
  }

  @Test public void synchronizedBraces() {
    trimming.of("" //
        + "    synchronized (variables) {\n" //
        + "      for (final String key : variables.keySet())\n"//
        + "        $.variables.put(key, variables.get(key));\n" //
        + "    }").stays();
  }

  @Test public void ternarize05() {
    trimming.of(" int res = 0; "//
        + "if (s.equals(532))    "//
        + "res += 6;   "//
        + "else    "//
        + "res += 9;      ").to("int res=0;res+=s.equals(532)?6:9;");
  }

  @Test public void ternarize05a() {
    trimming.of(" int res = 0; "//
        + "if (s.equals(532))    "//
        + "res += 6;   "//
        + "else    "//
        + "res += 9;      "//
        + "return res; ").to("int res=0;res+=s.equals(532)?6:9;return res;");
  }

  @Test public void ternarize07() {
    trimming
        .of("" //
            + "String res;" //
            + "res = s;   " //
            + "if (res.equals(532)==true)    " //
            + "  res = s + 0xABBA;   " //
            + "S.out.println(res); " //
            + "")
        .to("" //
            + "String res =s ;" //
            + "if (res.equals(532))    " //
            + "  res = s + 0xABBA;   " //
            + "S.out.println(res); " //
            + "");
  }

  @Test public void ternarize07a() {
    trimming.of("" //
        + "String res;" //
        + "res = s;   " //
        + "if (res==true)    " //
        + "  res = s + 0xABBA;   " //
        + "S.out.println(res); " //
        + "").to("String res=s;if(res)res=s+0xABBA;S.out.println(res);");
  }

  @Test public void ternarize07aa() {
    trimming.of("String res=s;if(res==true)res=s+0xABBA;S.out.println(res);").to("String res=s==true?s+0xABBA:s;S.out.println(res);");
  }

  @Test public void ternarize07b() {
    trimming
        .of("" //
            + "String res =s ;" //
            + "if (res.equals(532)==true)    " //
            + "  res = s + 0xABBA;   " //
            + "S.out.println(res); ")
        .to("" //
            + "String res=s.equals(532)==true?s+0xABBA:s;S.out.println(res);");
  }

  @Test public void ternarize09() {
    trimming.of("if (s.equals(532)) {    return 6;}else {    return 9;}").to("return s.equals(532)?6:9; ");
  }

  @Test public void ternarize10() {
    trimming.of("String res = s, foo = bar;   "//
        + "if (res.equals(532)==true)    " //
        + "res = s + 0xABBA;   "//
        + "S.out.println(res); ").to("String res=s.equals(532)==true?s+0xABBA:s,foo=bar;S.out.println(res);");
  }

  @Test public void ternarize12() {
    trimming.of("String res = s;   if (s.equals(532))    res = res + 0xABBA;   S.out.println(res); ")
        .to("String res=s.equals(532)?s+0xABBA:s;S.out.println(res);");
  }

  @Test public void ternarize13() {
    trimming.of("String res = m, foo;  if (m.equals(f())==true)   foo = M; ")//
        .to("String foo;if(m.equals(f())==true)foo=M;")//
        .to("String foo;if(m.equals(f()))foo=M;");
  }

  @Test public void ternarize13Simplified() {
    trimming.of("String r = m, f;  if (m.e(f()))   f = M; ")//
        .to("String f;if(m.e(f()))f=M;");
  }

  @Test public void ternarize13SimplifiedMore() {
    trimming.of("if (m.equals(f())==true)   foo = M; ").to("if (m.equals(f())) foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreAndMore() {
    trimming.of("f (m.equals(f())==true); foo = M; ").to("f (m.equals(f())); foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreAndMoreAndMore() {
    trimming.of("f (m.equals(f())==true);  ").to("f (m.equals(f()));");
  }

  @Test public void ternarize13SimplifiedMoreVariant() {
    trimming.of("if (m==true)   foo = M; ").to("if (m) foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreVariantShorter() {
    trimming.of("if (m==true)   f(); ").to("if (m) f();");
  }

  @Test public void ternarize13SimplifiedMoreVariantShorterAsExpression() {
    trimming.of("f (m==true);   f(); ").to("f (m); f();");
  }

  @Test public void ternarize14() {
    trimming.of("String res=m,foo=GY;if (res.equals(f())==true){foo = M;int k = 2;k = 8;S.out.println(foo);}f();")
        .to("String res=m,foo=GY;if(res.equals(f())){foo=M;int k=8;S.out.println(foo);}f();");
  }

  @Test public void ternarize16() {
    trimming.of("String res = m;  int num1, num2, num3;  if (m.equals(f()))   num2 = 2; ").stays();
  }

  @Test public void ternarize16a() {
    trimming.of("int n1, n2 = 0, n3;\n" + //
        "  if (d)\n" + //
        "    n2 = 2;").to("int n1, n2 = d ? 2: 0, n3;");
  }

  public void ternarize18() {
    trimming.of("final String res=s;System.out.println(s.equals(res)?tH3+res:h2A+res+0);")//
        .to("System.out.println(s.equals(s)?tH3+res:h2A+s+0);");
  }

  @Test public void ternarize21() {
    trimming.of("if (s.equals(532)){    S.out.println(gG);    S.out.l(kKz);} f(); ").stays();
  }

  @Test public void ternarize21a() {
    trimming.of("   if (s.equals(known)){\n" + //
        "     S.out.l(gG);\n" + //
        "   } else {\n" + //
        "     S.out.l(kKz);\n" + //
        "   }").to("S.out.l(s.equals(known)?gG:kKz);");
  }

  @Test public void ternarize22() {
    trimming.of("int a=0;   if (s.equals(532)){    S.console();    a=3;} f(); ").stays();
  }

  @Test public void ternarize26() {
    trimming.of("int a=0;   if (s.equals(532)){    a+=2;   a-=2; } f(); ").stays();
  }

  @Test public void ternarize33() {
    trimming.of("int a, b=0;   if (b==3){    a=4; } ")//
        .to("int a;if(0==3){a=4;}") //
        .to("int a;if(0==3)a=4;") //
        .stays();
  }

  @Test public void ternarize35() {
    trimming.of("int a,b=0,c=0;a=4;if(c==3){b=2;}")//
        .to("int a=4,b=0,c=0;if(c==3)b=2;");
  }

  @Test public void ternarize36() {
    trimming.of("int a,b=0,c=0;a=4;if (c==3){  b=2;   a=6; } f();").to("int a=4,b=0,c=0;if(c==3){b=2;a=6;} f();");
  }

  @Test public void ternarize38() {
    trimming.of("int a, b=0;if (b==3){    a+=2+r();a-=6;} f();").stays();
  }

  @Test public void ternarize41() {
    trimming.of("int a,b,c,d;a = 3;b = 5; d = 7;if (a == 4)while (b == 3) c = a; else while (d == 3)c =a*a; ")
        .to("int a=3,b,c,d;b=5;d=7;if(a==4)while(b==3)c=a;else while(d==3)c=a*a;");
  }

  @Test public void ternarize42() {
    trimming
        .of(" int a, b; a = 3;b = 5; if (a == 4) if (b == 3) b = 2; else{b = a; b=3;}  else if (b == 3) b = 2; else{ b = a*a;         b=3; }")//
        .to("int a=3,b;b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=b=3;}else if(b==3)b=2;else{b=3*3;b=3;}")//
        .to("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=3*3;b=3;}")//
        .stays();
  }

  @Test public void ternarize45() {
    trimming.of("if (m.equals(f())==true) if (b==3){ return 3; return 7;}   else    if (b==3){ return 2;}     a=7; ")//
        .to("if (m.equals(f())) {if (b==3){ return 3; return 7;} if (b==3){ return 2;}   }  a=7; ");
  }

  @Test public void ternarize46() {
    trimming.of(//
        "   int a , b=0;\n" + //
            "   if (m.equals(NG)==true)\n" + //
            "     if (b==3){\n" + //
            "       return 3;\n" + //
            "     } else {\n" + //
            "       a+=7;\n" + //
            "     }\n" + //
            "   else\n" + //
            "     if (b==3){\n" + //
            "       return 2;\n" + //
            "     } else {\n" + //
            "       a=7;\n" + //
            "     }")
        .to("int a;if(m.equals(NG)==true)if(0==3){return 3;}else{a+=7;}else if(0==3){return 2;}else{a=7;}");
  }

  @Test public void ternarize49() {
    trimming.of("if (s.equals(532)){ S.out.println(gG); S.out.l(kKz); } f();").stays();
  }

  @Test public void ternarize52() {
    trimming.of("int a=0,b = 0,c,d = 0,e = 0;if (a < b) {c = d;c = e;} f();")//
        .stays();
  }

  @Test public void ternarize54() {
    trimming.of("int $=1,xi=0,xj=0,yi=0,yj=0; if(xi > xj == yi > yj)++$;else--$;")//
        .to(" int $=1,xj=0,yi=0,yj=0;      if(0>xj==yi>yj)++$;else--$;");
  }

  @Test public void ternarize55() {
    trimming.of("if (key.equals(markColumn))\n" + //
        " to.put(key, a.toString());\n" + //
        "else\n" + //
        "  to.put(key, missing(key, a) ? Z2 : get(key, a));").to("to.put(key,key.equals(markColumn)?a.toString():missing(key,a)?Z2:get(key,a));");
  }

  @Test public void ternarize56() {
    trimming.of("if (target == 0) {p.f(X); p.v(0); p.f(q +  target); p.v(q * 100 / target); } f();") //
        .to("if(target==0){p.f(X);p.v(0);p.f(q+target);p.v(100*q / target); } f();");
  }

  @Test public void ternarizeIntoSuperMethodInvocation() {
    trimming.of("a ? super.f(a, b, c) : super.f(a, x, c)").to("super.f(a, a ? b : x, c)");
  }

  @Test public void ternaryPushdownOfReciever() {
    trimming.of("a ? b.f():c.f()").to("(a?b:c).f()");
  }

  @Test public void testPeel() {
    azzert.that(
        GuessedContext.expression_or_something_that_may_be_passed_as_argument
            .off(GuessedContext.expression_or_something_that_may_be_passed_as_argument.on("on * notion * of * no * nothion != the * plain + kludge")),
        is("on * notion * of * no * nothion != the * plain + kludge"));
  }

  @Test public void twoMultiplication1() {
    trimming.of("f(a,b,c,d) * f()").to("f() * f(a,b,c,d)");
  }

  @Test public void twoOpportunityExample() {
    azzert.that(
        trimming.countOpportunities(new Trimmer(),
            (CompilationUnit) MakeAST.COMPILATION_UNIT.from(
                GuessedContext.expression_or_something_that_may_be_passed_as_argument.on("on * notion * of * no * nothion != the * plain + kludge"))),
        is(2));
    azzert.that(
        trimming.countOpportunities(new Trimmer(),
            (CompilationUnit) MakeAST.COMPILATION_UNIT.from(
                GuessedContext.expression_or_something_that_may_be_passed_as_argument.on("on * notion * of * no * nothion != the * plain + kludge"))),
        is(2));
  }

  @Test public void unsafeBlockSimlify() {
    trimming.of("" //
        + "public void testParseInteger() {\n" //
        + "  String source = \"10\";\n" //
        + "  {\n" //
        + "    BigFraction c = properFormat.parse(source);\n" //
        + "    azzert.notNull(c);\n" //
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" //
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    BigFraction c = improperFormat.parse(source);\n" //
        + "    azzert.notNull(c);\n" //
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" //
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" //
        + "  }\n" //
        + "}").stays();
  }

  @Test public void useOutcontextToManageStringAmbiguity() {
    trimming.of("1+2+s<3").to("s+1+2<3");
  }

  @Test public void vanillaShortestFirstConditionalNoChange() {
    trimming.of("literal ? CONDITIONAL_OR : CONDITIONAL_AND").stays();
  }

  @Test public void xorSortClassConstantsAtEnd() {
    trimming.of("f(a,b,c,d) ^ BOB").stays();
  }
}
