package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.MatcherAssert.iz;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.assertSimilar;
import static il.org.spartan.refactoring.utils.ExpressionComparator.NODES_THRESHOLD;
import static il.org.spartan.refactoring.utils.ExpressionComparator.nodesCount;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Funcs.right;
import static il.org.spartan.refactoring.utils.Into.i;
import static il.org.spartan.refactoring.utils.Into.s;
import static il.org.spartan.utils.Utils.compressSpaces;
import static il.org.spartan.utils.Utils.hasNull;
import static il.org.spartan.utils.Utils.in;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.refactoring.spartanizations.Wrap;
import il.org.spartan.refactoring.utils.*;

/**
 * * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 *
 * @author Yossi Gil
 * @since 2014-07-10
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) public class TrimmerTest {
  @Test public void actualExampleForSortAddition() {
    TrimmerTestsUtils.trimming("1 + b.statements().indexOf(declarationStmt)").to("");
  }
  @Test public void actualExampleForSortAdditionInContext() {
    final String from = "2 + a < b";
    final String expected = "a + 2 < b";
    final Wrap w = Wrap.Expression;
    final String wrap = w.on(from);
    assertEquals(from, w.off(wrap));
    final Trimmer t = new Trimmer();
    final String unpeeled = TrimmerTestsUtils.apply(t, wrap);
    if (wrap.equals(unpeeled))
      fail("Nothing done on " + from);
    final String peeled = w.off(unpeeled);
    if (peeled.equals(from))
      assertNotEquals("No similification of " + from, from, peeled);
    if (compressSpaces(peeled).equals(compressSpaces(from)))
      assertNotEquals("Simpification of " + from + " is just reformatting", compressSpaces(peeled), compressSpaces(from));
    assertSimilar(expected, peeled);
  }
  @Test public void andWithCLASS_CONSTANT() {
    TrimmerTestsUtils.trimming("(x >> 18) & MASK_BITS").to("");
    TrimmerTestsUtils.trimming("(x >> 18) & MASK_6BITS").to("");
  }
  @Test public void annotationRemoveValueMemberSingleValue() {
    TrimmerTestsUtils.trimming("@SuppressWarnings(value = \"something\") void m() {}") //
        .to("@SuppressWarnings(\"something\") void m() {}");
  }
  @Test public void annotationRemoveValueMemberArrayValue() {
    TrimmerTestsUtils.trimming("@SuppressWarnings(value = { \"something\", \"something else\" }) void m() {}") //
        .to("@SuppressWarnings({ \"something\", \"something else\" }) void m() {}");
  }
  @Test public void annotationDoNotRemoveValueAndSomethingElse() {
    TrimmerTestsUtils.trimming("@SuppressWarnings(value = \"something\", x = 2) void m() {}").to("");
  }
  @Test public void annotationDoNotRemoveSingleMemberNotCalledValue() {
    TrimmerTestsUtils.trimming("@SuppressWarnings(sky = \"blue\") void m() {}").to("");
  }
  @Test public void annotationRemoveValueFromMultipleAnnotations() {
    TrimmerTestsUtils.trimming("@SuppressWarnings(value = \"javadoc\") @TargetApi(value = 23) void m() {}") //
        .to("@SuppressWarnings(\"javadoc\") @TargetApi(23) void m() {}");
  }
  @Test public void annotationRemoveEmptyParentheses() {
    TrimmerTestsUtils.trimming("@Override() void m() {}").to("@Override void m() {}");
  }
  @Test public void assignmentAssignmentChain1() {
    TrimmerTestsUtils.trimming("c = a = 13; b = 13;").to("b = c = a = 13;");
  }
  @Test public void assignmentAssignmentChain2() {
    TrimmerTestsUtils.trimming("a = 13; b= c = 13;").to("b = c = a = 13;");
  }
  @Test public void assignmentAssignmentChain3() {
    TrimmerTestsUtils.trimming("a = b = 13; c = d = 13;").to("c = d = a = b = 13;");
  }
  @Test public void assignmentAssignmentChain4() {
    TrimmerTestsUtils.trimming("a1 = a2 = a3 = a4 = 13; b1 = b2 = b3 = b4 = b5 = 13;")//
        .to("b1 = b2 = b3 = b4 = b5 = a1 = a2 = a3 = a4 = 13;");
  }
  @Test public void assignmentAssignmentChain5() {
    TrimmerTestsUtils.trimming("a1 = (a2 = (a3 = (a4 = 13))); b1 = b2 = b3 = ((((b4 = (b5 = 13)))));")//
        .to("b1=b2=b3=((((b4=(b5=a1=(a2=(a3=(a4=13))))))));");
  }
  @Test public void assignmentAssignmentNew() {
    TrimmerTestsUtils.trimming("a = new B(); b= new B();").to("");
  }
  @Test public void assignmentAssignmentNewArray() {
    TrimmerTestsUtils.trimming("a = new A[3]; b= new A[3];").to("");
  }
  @Test public void assignmentAssignmentNull() {
    TrimmerTestsUtils.trimming("c = a = null; b = null;").to("");
  }
  @Test public void assignmentAssignmentSideEffect() {
    TrimmerTestsUtils.trimming("a = f(); b= f();").to("");
  }
  @Test public void assignmentAssignmentVanilla() {
    TrimmerTestsUtils.trimming("a = 13; b= 13;").to("b = a = 13;");
  }
  @Test public void assignmentAssignmentVanilla0() {
    TrimmerTestsUtils.trimming("a = 0; b = 0;").to("b = a = 0;");
  }
  @Test public void assignmentAssignmentVanillaScopeIncludes() {
    TrimmerTestsUtils.included("a = 3; b = 3;", Assignment.class).in(new AssignmentAndAssignment());
  }
  @Test public void assignmentAssignmentVanillaScopeIncludesNull() {
    TrimmerTestsUtils.included("a = null; b = null;", Assignment.class).notIn(new AssignmentAndAssignment());
  }
  @Test public void assignmentReturn0() {
    TrimmerTestsUtils.trimming("a = 3; return a;").to("return a = 3;");
  }
  @Test public void assignmentReturn1() {
    TrimmerTestsUtils.trimming("a = 3; return (a);").to("return a = 3;");
  }
  @Test public void assignmentReturn2() {
    TrimmerTestsUtils.trimming("a += 3; return a;").to("return a += 3;");
  }
  @Test public void assignmentReturn3() {
    TrimmerTestsUtils.trimming("a *= 3; return a;").to("return a *= 3;");
  }
  @Test public void assignmentReturniNo() {
    TrimmerTestsUtils.trimming("b = a = 3; return a;").to("");
  }
  @Test public void blockSimplifyVanilla() {
    TrimmerTestsUtils.trimming("if (a) {f(); }").to("if (a) f();");
  }
  @Test public void blockSimplifyVanillaSimplified() {
    TrimmerTestsUtils.trimming(" {f(); }").to("f();");
  }
  @Test public void booleanChangeValueOfToConstant() {
    TrimmerTestsUtils.trimming("Boolean b = Boolean.valueOf(true);").to("Boolean b = Boolean.TRUE;");
    TrimmerTestsUtils.trimming("Boolean b = Boolean.valueOf(false);").to("Boolean b = Boolean.FALSE;");
  }
  @Test public void bugInLastIfInMethod() {
    TrimmerTestsUtils
        .trimming("" + //
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
    TrimmerTestsUtils
        .trimming("" + //
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
    TrimmerTestsUtils
        .trimming("" + //
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
    TrimmerTestsUtils
        .trimming("" + //
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
    TrimmerTestsUtils
        .trimming("" + //
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
    TrimmerTestsUtils
        .trimming("" + //
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
    TrimmerTestsUtils
        .trimming("" + //
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
    TrimmerTestsUtils.trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            foo();\n" + //
        "            bar();\n" + //
        "          }\n" + //
        "        }").to("public void f(){if(g)return;foo();bar();}");
  }
  @Test public void bugInLastIfInMethod8() {
    TrimmerTestsUtils.trimming("" + //
        "        public void f() {\n" + //
        "          if (g) {\n" + //
        "            foo();\n" + //
        "            bar();\n" + //
        "          }\n" + //
        "        }").to("public void f(){if(!g)return;foo();bar();}");
  }
  @Test public void bugIntroducingMISSINGWord1() {
    TrimmerTestsUtils.trimming("b.f(a) && -1 == As.g(f).h(c) ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("b.f(a) && As.g(f).h(c) == -1 ? o(s,b,g(f)) : b.f(\".in\") && !y(d,b)? o(b.z(u,v),s,f) : null");
  }
  @Test public void bugIntroducingMISSINGWord1a() {
    TrimmerTestsUtils.trimming("-1 == As.g(f).h(c)").to("As.g(f).h(c)==-1");
  }
  @Test public void bugIntroducingMISSINGWord1b() {
    TrimmerTestsUtils.trimming("b.f(a) && X ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("b.f(a)&&X?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord1c() {
    TrimmerTestsUtils.trimming("Y ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)").to("Y?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord1d() {
    TrimmerTestsUtils.trimming("Y ? Z : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)").to("Y?Z:b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord1e() {
    TrimmerTestsUtils.trimming("Y ? Z : R ? null : S ? null : T").to("Y?Z:!R&&!S?T:null");
  }
  @Test public void bugIntroducingMISSINGWord2() {
    TrimmerTestsUtils
        .trimming(
            "name.endsWith(testSuffix) &&  As.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("name.endsWith(testSuffix)&&As.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord2a() {
    TrimmerTestsUtils
        .trimming(
            "name.endsWith(testSuffix) &&  As.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("name.endsWith(testSuffix)&&As.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord2b() {
    TrimmerTestsUtils
        .trimming(
            "name.endsWith(testSuffix) &&  T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("name.endsWith(testSuffix) && T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord2c() {
    TrimmerTestsUtils
        .trimming("X && T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X && T ? objects(s,name,makeInFile(f)) : name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord2d() {
    TrimmerTestsUtils.trimming("X && T ? E : Y ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X && T ? E : !Y && !dotOutExists(d,name) ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }
  @Test public void bugIntroducingMISSINGWord2e() {
    TrimmerTestsUtils.trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }
  @Test public void bugIntroducingMISSINGWord2e1() {
    TrimmerTestsUtils.trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(x, Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(x,Z2),s,f) : null");
  }
  @Test public void bugIntroducingMISSINGWord2e2() {
    TrimmerTestsUtils.trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(g, Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(g,Z2),s,f) : null");
  }
  @Test public void bugIntroducingMISSINGWord2f() {
    TrimmerTestsUtils.trimming("X &&  T ? E : Y ? null : Z ? null : F").to("X&&T?E:!Y&&!Z?F:null");
  }
  @Test public void bugIntroducingMISSINGWord3() {
    TrimmerTestsUtils
        .trimming(
            "name.endsWith(testSuffix) && -1 == As.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
        .to("name.endsWith(testSuffix)&&As.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord3a() {
    TrimmerTestsUtils.trimming("!name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
        .to("name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWordTry1() {
    TrimmerTestsUtils
        .trimming(
            "name.endsWith(testSuffix) && -1 == As.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("name.endsWith(testSuffix) && As.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWordTry2() {
    TrimmerTestsUtils.trimming("!(intent.getBooleanExtra(EXTRA_FROM_SHORTCUT, false) && !K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName()))")
        .to("!intent.getBooleanExtra(EXTRA_FROM_SHORTCUT,false)||K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName())");
  }
  @Test public void bugIntroducingMISSINGWordTry3() {
    TrimmerTestsUtils.trimming("!(f.g(X, false) && !a.b.e(m.h()))").to("!f.g(X,false)||a.b.e(m.h())");
  }
  @Test public void bugOfMissingTry() {
    TrimmerTestsUtils.trimming("!(A && B && C && true && D)").to("!A||!B||!C||false||!D");
  }
  @Test public void canonicalFragementExamples() {
    TrimmerTestsUtils.trimming("int a; a = 3;").to("int a = 3;");
    TrimmerTestsUtils.trimming("int a = 2; if (b) a = 3; ").to("int a = b ? 3 : 2;");
    TrimmerTestsUtils.trimming("int a = 2; a += 3; ").to("int a = 2 + 3;");
    TrimmerTestsUtils.trimming("int a = 2; a = 3 * a; ").to("int a = 3 * 2;");
    TrimmerTestsUtils.trimming("int a = 2; return 3 * a; ").to("return 3 * 2;");
    TrimmerTestsUtils.trimming("int a = 2; return a; ").to("return 2;");
  }
  @Test public void canonicalFragementExamplesWithExraFragments() {
    TrimmerTestsUtils.trimming("int a,b; a = 3;").to("int a = 3, b;");
    TrimmerTestsUtils.trimming("int a,b=2; a = b;").to("int a;a=2;").to("int a=2;");
    TrimmerTestsUtils.trimming("int a = 2,b=1; if (b) a = 3; ").to("int a=2;if(1)a=3;").to("int a=1?3:2;");
    TrimmerTestsUtils.trimming("int a = 2; a += 3; ").to("int a = 2 + 3;");
    TrimmerTestsUtils.trimming("int a = 2; a += b; ").to("int a = 2 + b;");
    TrimmerTestsUtils.trimming("int a = 2, b; a += b; ").to("");
    TrimmerTestsUtils.trimming("int a = 2, b=1; a += b; ").to("int a=2;a+=1;").to("int a=2+1;");
    TrimmerTestsUtils.trimming("int a = 2; a = 3 * a; ").to("int a = 3 * 2;");
    TrimmerTestsUtils.trimming("int a = 2; a = 3 * a * b; ").to("int a = 3 * 2 * b;");
    TrimmerTestsUtils.trimming("int a = 2, b; a = 3 * a * b; ").to("");
    TrimmerTestsUtils.trimming("int a = 2, b = 1; a = 3 * a * b; ").to("int a=2;a=3*a*1;").to("int a=3*2*1;").to("int a=1*2*3;");
    TrimmerTestsUtils.trimming("int a = 2; return 3 * a * a; ").to("return 3 * 2 * 2;");
    TrimmerTestsUtils.trimming("int a = 2; return 3 * a * b; ").to("return 3 * 2 * b;");
    TrimmerTestsUtils.trimming("int b=5,a = 2,c; return 3 * a * b * c; ").to("int a = 2; return 3 * a * 5 * c;");
    TrimmerTestsUtils.trimming("int b=5,a = 2,c=4; return 3 * a * b * c; ").to("int a=2,c=4;return 3*a*5*c;");
    TrimmerTestsUtils.trimming("int a = 2, b; return a + 3 * b; ").to("return 2 + 3*b;");
    TrimmerTestsUtils.trimming("int a = 2, b = 1; return a + 3 * b; ").to("int b=1;return 2+3*b;");
    TrimmerTestsUtils.trimming("int a = 2; return a; ").to("return 2;");
    TrimmerTestsUtils.trimming("int a; if (x) a = 3; else a++;").to("int a;if(x)a=3;else++a;");
    TrimmerTestsUtils.trimming("int a =2; if (x) a = 3*a;").to("int a=x?3*2:2;");
    TrimmerTestsUtils.trimming("int a =2,b; if (x) a = 2*a;").to("int a=x?2*2:2, b;");
    TrimmerTestsUtils.trimming("int a =2,b=2; if (x) a = 2*a;").to("int a=x?2*2:2, b=2;");
  }
  @Test public void canonicalFragementExamplesWithExraFragmentsX() {
    TrimmerTestsUtils.trimming("int a; if (x) a = 3; else a++;").to("int a;if(x)a=3;else++a;");
  }
  @Test public void chainComparison() {
    final InfixExpression e = i("a == true == b == c");
    assertEquals("c", right(e).toString());
    TrimmerTestsUtils.trimming("a == true == b == c").to("a == b == c");
  }
  @Test public void chainCOmparisonTrueLast() {
    TrimmerTestsUtils.trimming("a == b == c == true").to("a == b == c");
  }
  @Test public void comaprisonWithBoolean1() {
    TrimmerTestsUtils.trimming("s.equals(532)==true").to("s.equals(532)");
  }
  @Test public void comaprisonWithBoolean2() {
    TrimmerTestsUtils.trimming("s.equals(532)==false ").to("!s.equals(532)");
  }
  @Test public void comaprisonWithBoolean3() {
    TrimmerTestsUtils.trimming("(false==s.equals(532))").to("(!s.equals(532))");
  }
  @Test public void comaprisonWithSpecific0() {
    TrimmerTestsUtils.trimming("this != a").to("a != this");
  }
  @Test public void comaprisonWithSpecific0Legibiliy00() {
    final InfixExpression e = i("this != a");
    assertTrue(in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS));
    assertFalse(Is.booleanLiteral(right(e)));
    assertFalse(Is.booleanLiteral(left(e)));
    assertTrue(in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS));
  }
  @Test public void comaprisonWithSpecific1() {
    TrimmerTestsUtils.trimming("null != a").to("a != null");
  }
  @Test public void comaprisonWithSpecific2() {
    TrimmerTestsUtils.trimming("null != a").to("a != null");
    TrimmerTestsUtils.trimming("this == a").to("a == this");
    TrimmerTestsUtils.trimming("null == a").to("a == null");
    TrimmerTestsUtils.trimming("this >= a").to("a <= this");
    TrimmerTestsUtils.trimming("null >= a").to("a <= null");
    TrimmerTestsUtils.trimming("this <= a").to("a >= this");
    TrimmerTestsUtils.trimming("null <= a").to("a >= null");
  }
  @Test public void comaprisonWithSpecific2a() {
    TrimmerTestsUtils.trimming("s.equals(532)==false").to("!s.equals(532)");
  }
  @Test public void comaprisonWithSpecific3() {
    TrimmerTestsUtils.trimming("(this==s.equals(532))").to("(s.equals(532)==this)");
  }
  @Test public void comaprisonWithSpecific4() {
    TrimmerTestsUtils.trimming("(0 < a)").to("(a>0)");
  }
  @Test public void comaprisonWithSpecificInParenthesis() {
    TrimmerTestsUtils.trimming("(null==a)").to("(a==null)");
  }
  @Test public void commonPrefixEntirelyIfBranches() {
    TrimmerTestsUtils.trimming("if (s.equals(532)) S.out.close();else S.out.close();").to("S.out.close(); ");
  }
  @Test public void commonPrefixIfBranchesInFor() {
    TrimmerTestsUtils.trimming("for (;;) if (a) {i++;j++;j++;} else { i++;j++; i++;}").to("for(;;){i++;j++;if(a)j++;else i++;}");
  }
  @Test public void commonSuffixIfBranches() {
    TrimmerTestsUtils.trimming("if (a) { \n" + //
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
    TrimmerTestsUtils.trimming("if (a) { \n" + //
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
    TrimmerTestsUtils.trimming("if (a) { \n" + //
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
    TrimmerTestsUtils.trimming("if (x)  if (a) { \n" + //
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
    TrimmerTestsUtils.trimming("a == true").to("a");
  }
  @Test public void compareWithBoolean01() {
    TrimmerTestsUtils.trimming("a == false").to("!a");
  }
  @Test public void compareWithBoolean10() {
    TrimmerTestsUtils.trimming("true == a").to("a");
  }
  @Test public void compareWithBoolean100() {
    TrimmerTestsUtils.trimming("a != true").to("!a");
  }
  @Test public void compareWithBoolean100a() {
    TrimmerTestsUtils.trimming("(((a))) != true").to("!a");
  }
  @Test public void compareWithBoolean101() {
    TrimmerTestsUtils.trimming("a != false").to("a");
  }
  @Test public void compareWithBoolean11() {
    TrimmerTestsUtils.trimming("false == a").to("!a");
  }
  @Test public void compareWithBoolean110() {
    TrimmerTestsUtils.trimming("true != a").to("!a");
  }
  @Test public void compareWithBoolean111() {
    TrimmerTestsUtils.trimming("false != a").to("a");
  }
  @Test public void compareWithBoolean2() {
    TrimmerTestsUtils.trimming("false != false").to("false");
  }
  @Test public void compareWithBoolean3() {
    TrimmerTestsUtils.trimming("false != true").to("true");
  }
  @Test public void compareWithBoolean4() {
    TrimmerTestsUtils.trimming("false == false").to("true");
  }
  @Test public void compareWithBoolean5() {
    TrimmerTestsUtils.trimming("false == true").to("false");
  }
  @Test public void compareWithBoolean6() {
    TrimmerTestsUtils.trimming("false != false").to("false");
  }
  @Test public void compareWithBoolean7() {
    TrimmerTestsUtils.trimming("true != true").to("false");
  }
  @Test public void compareWithBoolean8() {
    TrimmerTestsUtils.trimming("true != false").to("true");
  }
  @Test public void compareWithBoolean9() {
    TrimmerTestsUtils.trimming("true != true").to("false");
  }
  @Test public void comparison01() {
    TrimmerTestsUtils.trimming("1+2+3<3").to("");
  }
  @Test public void comparison02() {
    TrimmerTestsUtils.trimming("f(2)<a").to("");
  }
  @Test public void comparison03() {
    TrimmerTestsUtils.trimming("this==null").to("");
  }
  @Test public void comparison04() {
    TrimmerTestsUtils.trimming("6-7<2+1").to("6-7<1+2");
  }
  @Test public void comparison05() {
    TrimmerTestsUtils.trimming("a==11").to("");
  }
  @Test public void comparison06() {
    TrimmerTestsUtils.trimming("1<102333").to("");
  }
  @Test public void comparison08() {
    TrimmerTestsUtils.trimming("a==this").to("");
  }
  @Test public void comparison09() {
    TrimmerTestsUtils.trimming("1+2<3&7+4>2+1").to("1+2<3&4+7>1+2");
  }
  @Test public void comparison10() {
    TrimmerTestsUtils.trimming("1+2+3<3-4").to("");
  }
  @Test public void comparison11() {
    TrimmerTestsUtils.trimming("12==this").to("this==12");
  }
  @Test public void comparison12() {
    TrimmerTestsUtils.trimming("1+2<3&7+4>2+1||6-7<2+1").to("1+2<3&4+7>1+2||6-7<1+2");
  }
  @Test public void comparison13() {
    TrimmerTestsUtils.trimming("13455643294<22").to("");
  }
  @Test public void comparisonWithCharacterConstant() {
    TrimmerTestsUtils.trimming("'d' == s.charAt(i)").to("s.charAt(i)=='d'");
  }
  @Test public void compreaeExpressionToExpression() {
    TrimmerTestsUtils.trimming("6 - 7 < 2 + 1   ").to("6 -7 < 1 + 2");
  }
  @Test public void correctSubstitutionInIfAssignment() {
    TrimmerTestsUtils.trimming("int a = 2+3; if (a+b > a << b) a =(((((a *7 << a)))));")//
        .to("int a=2+3+b>2+3<<b?(2+3)*7<<2+3:2+3;");
  }
  @Test public void declarationAssignmentUpdateWithIncrement() {
    TrimmerTestsUtils.trimming("int a=0; a+=++a;").to("");
  }
  @Test public void declarationAssignmentUpdateWithPostIncrement() {
    TrimmerTestsUtils.trimming("int a=0; a+=a++;").to("");
  }
  @Test public void declarationAssignmentWithIncrement() {
    TrimmerTestsUtils.trimming("int a=0; a=++a;").to("");
  }
  @Test public void declarationAssignmentWithPostIncrement() {
    TrimmerTestsUtils.trimming("int a=0; a=a++;").to("");
  }
  @Test public void declarationIfAssignment() {
    TrimmerTestsUtils.trimming("" + //
        "    String res = s;\n" + //
        "    if (s.equals(y))\n" + //
        "      res = s + blah;\n" + //
        "    S.out.println(res);").to("" + //
            "    String res = s.equals(y) ? s + blah :s;\n" + //
            "    S.out.println(res);");
  }
  @Test public void declarationIfAssignment3() {
    TrimmerTestsUtils.trimming("int a =2; if (a != 2) a = 3;").to("int a = 2 != 2 ? 3 : 2;");
  }
  @Test public void declarationIfAssignment4() {
    TrimmerTestsUtils.trimming("int a =2; if (x) a = 2*a;").to("int a = x ? 2*2: 2;");
  }
  @Test public void declarationIfUpdateAssignment() {
    TrimmerTestsUtils.trimming("" + //
        "    String res = s;\n" + //
        "    if (s.equals(y))\n" + //
        "      res += s + blah;\n" + //
        "    S.out.println(res);").to("" + //
            "    String res = s.equals(y) ? s + s + blah :s;\n" + //
            "    S.out.println(res);");
  }
  @Test public void declarationIfUsesLaterVariable() {
    TrimmerTestsUtils.trimming("int a=0, b=0;if (b==3)   a=4;")//
        .to(" int a=0;if(0==3)a=4;") //
        .to(" int a=0==3?4:0;");
  }
  @Test public void declarationIfUsesLaterVariable1() {
    TrimmerTestsUtils.trimming("int a=0, b=0;if (b==3)   a=4; f();").to("");
  }
  @Test public void declarationInitializeRightShift() {
    TrimmerTestsUtils.trimming("int a = 3;a>>=2;").to("int a = 3 >> 2;");
  }
  @Test public void declarationInitializerReturnAssignment() {
    TrimmerTestsUtils.trimming("int a = 3; return a = 2 * a;").to("return 2 * 3;");
  }
  @Test public void declarationInitializerReturnExpression() {
    TrimmerTestsUtils.trimming("" //
        + "String t = Bob + Wants + To + \"Sleep \"; "//
        + "  return (right_now + t);    ").to("return(right_now+Bob+Wants+To+\"Sleep \");");
  }
  @Test public void declarationInitializesRotate() {
    TrimmerTestsUtils.trimming("int a = 3;a>>>=2;").to("int a = 3 >>> 2;");
  }
  @Test public void declarationInitializeUpdateAnd() {
    TrimmerTestsUtils.trimming("int a = 3;a&=2;").to("int a = 3 & 2;");
  }
  @Test public void declarationInitializeUpdateAssignment() {
    TrimmerTestsUtils.trimming("int a = 3;a += 2;").to("int a = 3+2;");
  }
  @Test public void declarationInitializeUpdateAssignmentFunctionCallWithReuse() {
    TrimmerTestsUtils.trimming("int a = f();a += 2*f();").to("int a=f()+2*f();");
  }
  @Test public void declarationInitializeUpdateAssignmentFunctionCallWIthReuse() {
    TrimmerTestsUtils.trimming("int a = x;a += a + 2*f();").to("int a=x+x+2*f();");
  }
  @Test public void declarationInitializeUpdateAssignmentIncrement() {
    TrimmerTestsUtils.trimming("int a = ++i;a += j;").to("int a = ++i + j;");
  }
  @Test public void declarationInitializeUpdateAssignmentIncrementTwice() {
    TrimmerTestsUtils.trimming("int a = ++i;a += a + j;").to("");
  }
  @Test public void declarationInitializeUpdateAssignmentWithReuse() {
    TrimmerTestsUtils.trimming("int a = 3;a += 2*a;").to("int a = 3+2*3;");
  }
  @Test public void declarationInitializeUpdateDividies() {
    TrimmerTestsUtils.trimming("int a = 3;a/=2;").to("int a = 3 / 2;");
  }
  @Test public void declarationInitializeUpdateLeftShift() {
    TrimmerTestsUtils.trimming("int a = 3;a<<=2;").to("int a = 3 << 2;");
  }
  @Test public void declarationInitializeUpdateMinus() {
    TrimmerTestsUtils.trimming("int a = 3;a-=2;").to("int a = 3 - 2;");
  }
  @Test public void declarationInitializeUpdateModulo() {
    TrimmerTestsUtils.trimming("int a = 3;a%= 2;").to("int a = 3 % 2;");
  }
  @Test public void declarationInitializeUpdatePlus() {
    TrimmerTestsUtils.trimming("int a = 3;a+=2;").to("int a = 3 + 2;");
  }
  @Test public void declarationInitializeUpdateTimes() {
    TrimmerTestsUtils.trimming("int a = 3;a*=2;").to("int a = 3 * 2;");
  }
  @Test public void declarationInitializeUpdateXor() {
    TrimmerTestsUtils.trimming("int a = 3;a^=2;").to("int a = 3 ^ 2;");
  }
  @Test public void declarationInitializeUpdatOr() {
    TrimmerTestsUtils.trimming("int a = 3;a|=2;").to("int a = 3 | 2;");
  }
  @Test public void declarationUpdateReturn() {
    TrimmerTestsUtils.trimming("int a = 3; return a += 2;").to("return 3 + 2;");
  }
  @Test public void declarationUpdateReturnNone() {
    TrimmerTestsUtils.trimming("int a = f(); return a += 2 * a;").to("");
  }
  @Test public void declarationUpdateReturnTwice() {
    TrimmerTestsUtils.trimming("int a = 3; return a += 2 * a;").to("return 3 + 2 *3 ;");
  }
  @Test public void delcartionIfAssignmentNotPlain() {
    TrimmerTestsUtils.trimming("int a=0;   if (y) a+=3; ").to("int a = y ? 0 + 3 : 0;");
  }
  @Test public void doNotConsolidateNewArrayActual() {
    TrimmerTestsUtils.trimming("" + //
        "occupied = new boolean[capacity];\n" + //
        "placeholder = new boolean[capacity];").to("");
  }
  @Test public void doNotConsolidateNewArraySimplifiedl() {
    TrimmerTestsUtils.trimming("" + //
        "a = new int[1];\n" + //
        "b = new int[1];").to("");
  }
  @Test public void doNotConsolidatePlainNew() {
    TrimmerTestsUtils.trimming("" + //
        "a = new A();\n" + //
        "b = new B();").to("");
  }
  @Test public void doNotInlineDeclarationWithAnnotationSimplified() {
    TrimmerTestsUtils.trimming("" + //
        "    @SuppressWarnings int $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }").to("");
  }
  @Test public void doNotInlineWithDeclaration() {
    TrimmerTestsUtils.trimming("  private Class<? extends T> retrieveClazz() throws ClassNotFoundException {\n" + //
        "    nonnull(className);\n" + //
        "    @SuppressWarnings(\"unchecked\") final Class<T> $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }").to("");
  }
  @Test public void doNotIntroduceDoubleNegation() {
    TrimmerTestsUtils.trimming("!Y ? null :!Z ? null : F").to("Y&&Z?F:null");
  }
  @Test public void donotSorMixedTypes() {
    TrimmerTestsUtils.trimming("if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;").to("");
  }
  @Test public void dontELiminateCatchBlock() {
    TrimmerTestsUtils.trimming("try { f(); } catch (Exception e) { } finally {}").to("");
  }
  @Test public void dontELiminateSwitch() {
    TrimmerTestsUtils.trimming("switch (a) { default: }").to("");
  }
  @Test public void dontSimplifyCatchBlock() {
    TrimmerTestsUtils.trimming("try { {} ; {} } catch (Exception e) {{} ; {}  } finally {{} ; {}}")//
        .to(" try {}          catch (Exception e) {}          finally {}");
  }
  @Test public void duplicatePartialIfBranches() {
    TrimmerTestsUtils.trimming("" + //
        "    if (a) {\n" + //
        "      f();\n" + //
        "      g();\n" + //
        "      ++i;\n" + //
        "    } else {\n" + //
        "      f();\n" + //
        "      g();\n" + //
        "      --i;\n" + //
        "    }").to("" + ////
            "   f();\n" + //
            "   g();\n" + //
            "    if (a) \n" + //
            "      ++i;\n" + //
            "    else \n" + //
            "      --i;");
  }
  @Test public void emptyElse() {
    TrimmerTestsUtils.trimming("if (x) b = 3; else ;").to("if (x) b = 3;");
  }
  @Test public void emptyElseBlock() {
    TrimmerTestsUtils.trimming("if (x) b = 3; else { ;}").to("if (x) b = 3;");
  }
  @Test public void emptyIsNotChangedExpression() {
    TrimmerTestsUtils.trimming("").to("");
  }
  @Test public void emptyIsNotChangedStatement() {
    TrimmerTestsUtils.trimming("").to("");
  }
  @Test public void emptyThen1() {
    TrimmerTestsUtils.trimming("if (b) ; else x();").to("if (!b) x();");
  }
  @Test public void emptyThen2() {
    TrimmerTestsUtils.trimming("if (b) {;;} else {x() ;}").to("if (!b) x();");
  }
  @Test public void factorOutAnd() {
    TrimmerTestsUtils.trimming("(a || b) && (a || c)").to("a || b && c");
  }
  @Test public void factorOutOr() {
    TrimmerTestsUtils.trimming("a && b || a && c").to("a && (b || c)");
  }
  @Test public void factorOutOr3() {
    TrimmerTestsUtils.trimming("a && b && x  && f() || a && c && y ").to("a && (b && x && f() || c && y)");
  }
  @Test public void forLoopBug() {
    TrimmerTestsUtils.trimming("" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          res += 2;\n" + //
        "        else "//
        + "       if (s.charAt(i) == 'd')\n" + //
        "          res -= 1;\n" + //
        "      return res;\n" + //
        " if (b) i = 3;").to("");
  }
  @Ignore @Test public void forwardDeclaration1() {
    TrimmerTestsUtils.trimming("/*    * This is a comment    */      int i = 6;   int j = 2;   int k = i+2;   S.out.println(i-j+k); ")
        .to(" /*    * This is a comment    */      int j = 2;   int i = 6;   int k = i+2;   S.out.println(i-j+k); ");
  }
  @Ignore @Test public void forwardDeclaration2() {
    TrimmerTestsUtils.trimming("/*    * This is a comment    */      int i = 6, h = 7;   int j = 2;   int k = i+2;   S.out.println(i-j+k); ")
        .to(" /*    * This is a comment    */      int h = 7;   int j = 2;   int i = 6;   int k = i+2;   S.out.println(i-j+k); ");
  }
  @Ignore @Test public void forwardDeclaration3() {
    TrimmerTestsUtils.trimming("/*    * This is a comment    */      int i = 6;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   y(i);   y(i+m); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   int i = 6;   y(i);   y(i+m); ");
  }
  @Ignore @Test public void forwardDeclaration4() {
    TrimmerTestsUtils
        .trimming(
            " /*    * This is a comment    */      int i = 6;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   final BlahClass bc = new BlahClass(i);   y(i+m+bc.j);    private static class BlahClass {   public BlahClass(int i) {    j = 2*i;      public final int j; ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   int i = 6;   final BlahClass bc = new BlahClass(i);   y(i+m+bc.j);    private static class BlahClass {   public BlahClass(int i) {    j = 2*i;      public final int j; ");
  }
  @Ignore @Test public void forwardDeclaration5() {
    TrimmerTestsUtils.trimming("/*    * This is a comment    */      int i = y(0);   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int i = y(0);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ");
  }
  @Ignore @Test public void forwardDeclaration6() {
    TrimmerTestsUtils
        .trimming(" /*    * This is a comment    */      int i = y(0);   int h = 8;   int j = 3;   int k = j+2 + y(i);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ")
        .to(" /*    * This is a comment    */      int h = 8;   int i = y(0);   int j = 3;   int k = j+2 + y(i);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ");
  }
  @Ignore @Test public void forwardDeclaration7() {
    TrimmerTestsUtils
        .trimming(
            "  j = 2*i;   }      public final int j;    private BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println(res.j);   return res; ")
        .to("  j = 2*i;   }      public final int j;    private BlahClass yada6() {   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   final BlahClass res = new BlahClass(6);   S.out.println(res.j);   return res; ");
  }
  @Test public void ifBugSecondTry() {
    TrimmerTestsUtils.trimming("" + //
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
    TrimmerTestsUtils.trimming("" + //
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
    TrimmerTestsUtils.trimming("" + //
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
    TrimmerTestsUtils.trimming("if (a) if (b) {} else f(); x();")//
        .to(" if (a) if (!b) f(); x();");
  }
  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement() {
    TrimmerTestsUtils.trimming("if(a) { int i = 3; }").to("");
  }
  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement2() {
    TrimmerTestsUtils.trimming("if(a) { Object o; }").to("");
  }
  @Test public void ifEmptyElsewWithinIf() {
    TrimmerTestsUtils.trimming("if (a) if (b) {;;;f();} else {;}")//
        .to("if(a&&b){;;;f();}");
  }
  @Test public void ifEmptyThenThrow() {
    TrimmerTestsUtils
        .trimming("" //
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
    TrimmerTestsUtils
        .trimming("" //
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
    TrimmerTestsUtils
        .trimming("" //
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
    TrimmerTestsUtils.trimming("if (x) f(a); else f(b);").to("f(x ? a: b);");
  }
  @Test public void ifPlusPlusPost() {
    TrimmerTestsUtils.trimming("if (x) a++; else b++;").to("if(x)++a;else++b;");
  }
  @Test public void ifPlusPlusPostExpression() {
    TrimmerTestsUtils.trimming("x? a++:b++").to("");
  }
  @Test public void ifPlusPlusPre() {
    TrimmerTestsUtils.trimming("if (x) ++a; else ++b;").to("");
  }
  @Test public void ifPlusPlusPreExpression() {
    TrimmerTestsUtils.trimming("x? ++a:++b").to("");
  }
  @Test public void ifSequencerNoElseSequencer0() {
    TrimmerTestsUtils.trimming("if (a) return; break;").to("");
  }
  @Test public void ifSequencerNoElseSequencer01() {
    TrimmerTestsUtils.trimming("if (a) throw e; break;").to("");
  }
  @Test public void ifSequencerNoElseSequencer02() {
    TrimmerTestsUtils.trimming("if (a) break; break;").to("break;");
  }
  @Test public void ifSequencerNoElseSequencer03() {
    TrimmerTestsUtils.trimming("if (a) continue; break;").to("");
  }
  @Test public void ifSequencerNoElseSequencer04() {
    TrimmerTestsUtils.trimming("if (a) break; return;").to("if (!a) return; break;");
  }
  @Test public void ifSequencerNoElseSequencer05() {
    TrimmerTestsUtils.trimming("if (a) {x(); return;} continue;").to("");
  }
  @Test public void ifSequencerNoElseSequencer06() {
    TrimmerTestsUtils.trimming("if (a) throw e; break;").to("");
  }
  @Test public void ifSequencerNoElseSequencer07() {
    TrimmerTestsUtils.trimming("if (a) break; throw e;").to("if (!a) throw e; break;");
  }
  @Test public void ifSequencerNoElseSequencer08() {
    TrimmerTestsUtils.trimming("if (a) throw e; continue;").to("");
  }
  @Test public void ifSequencerNoElseSequencer09() {
    TrimmerTestsUtils.trimming("if (a) break; throw e;").to("if (!a) throw e; break;");
  }
  @Test public void ifSequencerNoElseSequencer10() {
    TrimmerTestsUtils.trimming("if (a) continue; return;").to("if (!a) return; continue;");
  }
  @Test public void ifSequencerThenSequencer0() {
    TrimmerTestsUtils.trimming("if (a) return 4; else break;").to("if (a) return 4; break;");
  }
  @Test public void ifSequencerThenSequencer1() {
    TrimmerTestsUtils.trimming("if (a) break; else return 2;").to("if (!a) return 2; break;");
  }
  @Test public void ifSequencerThenSequencer3() {
    TrimmerTestsUtils.trimming("if (a) return 10; else continue;").to("if (a) return 10; continue;");
  }
  @Test public void ifSequencerThenSequencer4() {
    TrimmerTestsUtils.trimming("if (a) continue; else return 2;").to("if (!a) return 2; continue;");
  }
  @Test public void ifSequencerThenSequencer5() {
    TrimmerTestsUtils.trimming("if (a) throw e; else break;").to("if (a) throw e; break;");
  }
  @Test public void ifSequencerThenSequencer6() {
    TrimmerTestsUtils.trimming("if (a) break; else throw e;").to("if (!a) throw e; break;");
  }
  @Test public void ifSequencerThenSequencer7() {
    TrimmerTestsUtils.trimming("if (a) throw e; else continue;").to("if (a) throw e; continue;");
  }
  @Test public void ifSequencerThenSequencer8() {
    TrimmerTestsUtils.trimming("if (a) break; else throw e;").to("if (!a) throw e; break;");
  }
  @Test public void ifThrowNoElseThrow() {
    TrimmerTestsUtils
        .trimming("" //
            + "if (!(e.getCause() instanceof Error))\n" //
            + "  throw e;\n" //
            + "throw (Error) e.getCause();")//
        .to(" throw !(e.getCause()instanceof Error)?e:(Error)e.getCause();");//
  }
  @Test public void ifWithCommonNotInBlock() {
    TrimmerTestsUtils.trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++; g();}").to("for(;;){i++;j++;if(a)f();else g();}");
  }
  @Test public void ifWithCommonNotInBlockDegenerate() {
    TrimmerTestsUtils.trimming("for (;;) if (a) {i++; f();} else { i++;j++; }").to("for(;;){i++; if(a)f(); else j++;}");
  }
  @Test public void ifWithCommonNotInBlockiLongerElse() {
    TrimmerTestsUtils.trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++;  f(); h();}").to("for(;;){i++;j++; f(); if(!a) h();}");
  }
  @Test public void ifWithCommonNotInBlockiLongerThen() {
    TrimmerTestsUtils.trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++; }").to("for(;;){i++;j++; if(a)f();}");
  }
  @Test public void ifWithCommonNotInBlockNothingLeft() {
    TrimmerTestsUtils.trimming("for (;;) if (a) {i++;j++;} else { i++;j++; }").to("for(;;){i++;j++;}");
  }
  @Test public void infiniteLoopBug1() {
    TrimmerTestsUtils.trimming("static boolean hasAnnotation(final VariableDeclarationFragment f) {\n" + //
        "      return hasAnnotation((VariableDeclarationStatement) f.getParent());\n" + //
        "    }").to("");
  }
  @Test public void infiniteLoopBug2() {
    TrimmerTestsUtils.trimming(" static boolean hasAnnotation(final VariableDeclarationStatement n) {\n" + //
        "      return hasAnnotation(n.modifiers());\n" + //
        "    }").to(" static boolean hasAnnotation(final VariableDeclarationStatement s) {\n" + //
            "      return hasAnnotation(s.modifiers());\n" + //
            "    }");
  }
  @Test public void infiniteLoopBug3() {
    TrimmerTestsUtils.trimming("  boolean f(final VariableDeclarationStatement n) {\n" + //
        "      return false;\n" + //
        "    }").to("  boolean f(final VariableDeclarationStatement s) {\n" + //
            "      return false;\n" + //
            "    }");
  }
  @Test public void infiniteLoopBug4() {
    TrimmerTestsUtils.trimming("void f(final VariableDeclarationStatement n) {}")//
        .to(" void f(final VariableDeclarationStatement s) { }");
  }
  @Ignore @Test public void inline00() {
    TrimmerTestsUtils.trimming("" + //
        "  Object a() { " + //
        "    class a {\n" + //
        "      a a;\n" + //
        "      Object a() {\n" + //
        "        return a;\n" + ///
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
                "        return a;\n" + ///
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
    TrimmerTestsUtils.trimming("" + //
        "  public int y() {\n" + //
        "    final Z res = new Z(6);\n" + //
        "    S.out.println(res.j);\n" + //
        "    return res;\n" + //
        "  }\n" + //
        "}\n" + //
        "").to(//
            "  public int y() {\n" + // //
                "    final Z $ = new Z(6);\n" + ////
                "    S.out.println($.j);\n" + ////
                "    return $;\n" + // //
                "  }\n" + //
                "}\n" + //
                "");
  }
  @Test public void inlineInitializers() {
    TrimmerTestsUtils.trimming("int b,a = 2; return 3 * a * b; ").to("return 3*2*b;");
  }
  @Test public void inlineInitializersFirstStep() {
    TrimmerTestsUtils.trimming("int b=4,a = 2; return 3 * a * b; ").to("int a = 2; return 3*a*4;");
  }
  @Test public void inlineInitializersSecondStep() {
    TrimmerTestsUtils.trimming("int a = 2; return 3*a*4;").to("return 3 * 2 * 4;");
  }
  @Test public void inlineIntoNextStatementWithSideEffects() {
    TrimmerTestsUtils.trimming("int a = f(); if (a) g(a); else h(u(a));").to("");
  }
  @Ignore @Test public void inlineSingleUse01() {
    TrimmerTestsUtils.trimming("/*    * This is a comment    */      int i = y(0);   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + (y(0))); ");
  }
  @Ignore @Test public void inlineSingleUse02() {
    TrimmerTestsUtils.trimming("/*    * This is a comment    */      int i = 5,j=3;   int k = j+2;   int m = k + j -19 +i;   y(k); ")
        .to(" /*    * This is a comment    */      int j=3;   int k = j+2;   int m = k + j -19 +(5);   y(k); ");
  }
  @Ignore @Test public void inlineSingleUse03() {
    TrimmerTestsUtils.trimming("/*    * This is a comment    */      int i = 5;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + (5)); ");
  }
  @Ignore @Test public void inlineSingleUse04() {
    TrimmerTestsUtils.trimming("int x = 6;   final BlahClass b = new BlahClass(x);   int y = 2+b.j;   y(y-b.j);   y(y*2); ")
        .to(" final BlahClass b = new BlahClass((6));   int y = 2+b.j;   y(y-b.j);   y(y*2); ");
  }
  @Ignore @Test public void inlineSingleUse05() {
    TrimmerTestsUtils.trimming("int x = 6;   final BlahClass b = new BlahClass(x);   int y = 2+b.j;   y(y+x);   y(y*x); ")
        .to(" int x = 6;   int y = 2+(new BlahClass(x)).j;   y(y+x);   y(y*x); ");
  }
  @Ignore @Test public void inlineSingleUse06() {
    TrimmerTestsUtils
        .trimming(
            "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     for (final Integer pi : outdated)      coes.remove(pi);     S.out.println(coes.size()); ")
        .to("");
  }
  @Test public void inlineSingleUse07() {
    TrimmerTestsUtils
        .trimming(
            "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     S.out.println(coes.size()); ")
        .to("");
  }
  @Ignore @Test public void inlineSingleUse08() {
    TrimmerTestsUtils
        .trimming(
            "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     S.out.println(coes.size());     S.out.println(outdated.size()); ")
        .to("");
  }
  @Ignore @Test public void inlineSingleUse09() {
    TrimmerTestsUtils
        .trimming(
            " final A a = new D().new A(V){\nABRA\n{\nCADABRA\n{V;);   assertEquals(5, a.new Context().lineCount());   final PureIterable&lt;Mutant&gt; ms = a.generateMutants();   assertEquals(2, count(ms));   final PureIterator&lt;Mutant&gt; i = ms.iterator();   assertTrue(i.hasNext());   assertEquals(V;{\nABRA\nABRA\n{\nCADABRA\n{\nV;, i.next().text);   assertTrue(i.hasNext());   assertEquals(V;{\nABRA\n{\nCADABRA\nCADABRA\n{\nV;, i.next().text);   assertFalse(i.hasNext());  ")
        .to("");
  }
  @Ignore @Test public void inlineSingleUse10() {
    TrimmerTestsUtils
        .trimming(
            "      final A a = new A(\"{\nABRA\n{\nCADABRA\n{\");        assertEquals(5, a.new Context().lineCount());        final PureIterable<Mutant> ms = a.mutantsGenerator();        assertEquals(2, count(ms));        final PureIterator<Mutant> i = ms.iterator();        assertTrue(i.hasNext());        assertEquals(\"{\nABRA\nABRA\n{\nCADABRA\n{\n\", i.next().text);        assertTrue(i.hasNext());        assertEquals(\"{\nABRA\n{\nCADABRA\nCADABRA\n{\n\", i.next().text);        assertFalse(i.hasNext());")
        .to("");
  }
  @Test public void inlineSingleUseKillingVariable() {
    TrimmerTestsUtils.trimming("int a,b=2; a = b;").to("int a;a=2;");
  }
  @Test public void inlineSingleUseKillingVariables() {
    TrimmerTestsUtils.trimming("int $, xi=0, xj=0, yi=0, yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")
        .to(" int $, xj=0, yi=0, yj=0;        if (0>xj==yi>yj)$++;else $--;");
  }
  @Test public void inlineSingleUseKillingVariablesSimplified() {
    TrimmerTestsUtils.trimming("int $=1,xi=0,xj=0,yi=0,yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")//
        .to(" int $=1,xj=0,yi=0,yj=0;       if(0>xj==yi>yj)$++;else $--;")//
        .to(" int $=1,yi=0,yj=0;            if(0>0==yi>yj)$++;else $--;") //
        .to(" int $=1,yj=0;                 if(0>0==0>yj)$++;else $--;") //
        .to(" int $=1;                      if(0>0==0>0)$++;else $--;") //
        .to(" int $=1;                      if(0>0==0>0)++$;else--$;") //
    ;
  }
  @Test public void inlineSingleUseTrivial() {
    TrimmerTestsUtils.trimming(" int $=1,yj=0;                 if(0>0==yj<0)++$;else--$;") //
        .to("  int $=1;                      if(0>0==0<0)++$;else--$;") //
    ;
  }
  @Test public void inlineSingleUseVanilla() {
    TrimmerTestsUtils.trimming("int a = f(); if (a) f();").to("if (f()) f();");
  }
  @Test public void inlineSingleUseWithAssignment() {
    TrimmerTestsUtils.trimming("int a = 2; while (true) if (f()) f(a); else a = 2;")//
        .to("");
  }
  @Test public void inlineSingleVariableIntoPlusPlus() {
    TrimmerTestsUtils.trimming("int $ = 0;  if (a)  ++$;  else --$;").to("");
  }
  @Test public void inliningWithVariableAssignedTo() {
    TrimmerTestsUtils.trimming("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
    ;
  }
  @Test public void isGreaterTrue() {
    final InfixExpression e = i("f(a,b,c,d,e) * f(a,b,c)");
    assertEquals("f(a,b,c)", right(e).toString());
    assertEquals("f(a,b,c,d,e)", left(e).toString());
    final Wring<InfixExpression> s = Toolbox.instance.find(e);
    assertThat(s, instanceOf(InfixSortMultiplication.class));
    assertNotNull(s);
    assertTrue(s.scopeIncludes(e));
    final Expression e1 = left(e);
    final Expression e2 = right(e);
    assertFalse(hasNull(e1, e2));
    final boolean tokenWiseGreater = nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD;
    assertTrue(tokenWiseGreater);
    assertTrue(ExpressionComparator.moreArguments(e1, e2));
    assertTrue(ExpressionComparator.longerFirst(e));
    assertTrue(e.toString(), s.eligible(e));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assertNotNull(replacement);
    assertEquals("f(a,b,c) * f(a,b,c,d,e)", replacement.toString());
  }
  @Test public void isGreaterTrueButAlmostNot() {
    final InfixExpression e = i("f(a,b,c,d) * f(a,b,c)");
    assertEquals("f(a,b,c)", right(e).toString());
    assertEquals("f(a,b,c,d)", left(e).toString());
    final Wring<InfixExpression> s = Toolbox.instance.find(e);
    assertThat(s, instanceOf(InfixSortMultiplication.class));
    assertNotNull(s);
    assertTrue(s.scopeIncludes(e));
    final Expression e1 = left(e);
    final Expression e2 = right(e);
    assertFalse(hasNull(e1, e2));
    final boolean tokenWiseGreater = nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD;
    assertFalse(tokenWiseGreater);
    assertTrue(ExpressionComparator.moreArguments(e1, e2));
    assertTrue(ExpressionComparator.longerFirst(e));
    assertTrue(e.toString(), s.eligible(e));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assertNotNull(replacement);
    assertEquals("f(a,b,c) * f(a,b,c,d)", replacement.toString());
  }
  @Test public void issue06() {
    TrimmerTestsUtils.trimming("a*-b").to("-a * b");
  }
  @Test public void issue06A() {
    TrimmerTestsUtils.trimming("x/a*-b/-c*- - - d / d")//
        .to("-x/a * b/ c * d/d")//
        .to("");
  }
  @Test public void issue06B() {
    TrimmerTestsUtils.trimming("x/a*-b/-c*- - - d / -d")//
        .to("x/a * b/ c * d/d")//
        .to("d*x/a*b/c/d");
  }
  @Test public void issue06C1() {
    TrimmerTestsUtils.trimming("a*-b/-c*- - - d / d").to("-a * b/ c * d/d");
  }
  @Test public void issue06C2() {
    TrimmerTestsUtils.trimming("-a * b/ c * d/d").to("");
  }
  @Test public void issue06C3() {
    TrimmerTestsUtils.trimming("-a * b/ c * d").to("");
  }
  @Test public void issue06C4() {
    TrimmerTestsUtils.trimming("-a * b/ c ").to("");
  }
  @Test public void issue06D() {
    TrimmerTestsUtils.trimming("a*b*c*d*-e").to("-a*b*c*d*e").to("");
  }
  @Test public void issue06E() {
    TrimmerTestsUtils.trimming("-a*b*c*d*f*g*h*i*j*k").to("");
  }
  @Test public void issue06F() {
    TrimmerTestsUtils.trimming("x*a*-b*-c*- - - d * d")//
        .to("-x*a*b*c*d*d")//
        .to("");
  }
  @Test public void issue06G() {
    TrimmerTestsUtils.trimming("x*a*-b*-c*- - - d / d")//
        .to("-x*a*b*c*d/d")//
        .to("");
  }
  @Test public void issue06H() {
    TrimmerTestsUtils.trimming("x/a*-b/-c*- - - d ")//
        .to("-x/a * b/ c * d")//
    ;
  }
  @Test public void issue06I() {
    TrimmerTestsUtils.trimming("41 * - 19")//
        .to("-41 * 19 ") //
    ;
  }
  @Test public void issue06J() {
    TrimmerTestsUtils.trimming("41 * a * - 19")//
        .to("-41*a*19")//
        .to("-41*19*a") //
    ;
  }
  @Test public void issue37Simplified() {
    TrimmerTestsUtils.trimming("" + //
        "    int a = 3;\n" + //
        "    a = 31 * a;" + //
        "").to("int a = 31 * 3; ");
  }
  @Test public void issue37SimplifiedVariant() {
    TrimmerTestsUtils.trimming("" + //
        "    int a = 3;\n" + //
        "    a += 31 * a;").to("int a=3+31*3;");
  }
  @Test public void issue37WithSimplifiedBlock() {
    TrimmerTestsUtils.trimming("if (a) { {} ; if (b) f(); {} } else { g(); f(); ++i; ++j; }")//
        .to(" if (a) {  if (b) f(); } else { g(); f(); ++i; ++j; }");
  }
  @Test public void issue38() {
    TrimmerTestsUtils.trimming("    return o == null ? null\n" + //
        "        : o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
        "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
        "                : null;").to("");
  }
  @Test public void issue38Simplfiied() {
    TrimmerTestsUtils.trimming(//
        "         o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
            "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
            "                : null")
        .to("");
  }
  @Test public void issue39base() {
    TrimmerTestsUtils.trimming("" + //
        "if (name == null) {\n" + //
        "    if (other.name != null)\n" + //
        "        return false;\n" + //
        "} else if (!name.equals(other.name))\n" + //
        "    return false;\n" + //
        "return true;").to(""); //
  }
  public void issue39baseDual() {
    TrimmerTestsUtils.trimming("if (name != null) {\n" + //
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
    TrimmerTestsUtils.trimming("" + //
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
    TrimmerTestsUtils.trimming("" + //
        "if (!varArgs) {\n" + //
        "    if (parameterTypes.length != argumentTypes.length) {\n" + //
        "        return false;\n" + //
        "    }\n" + //
        "} else if (argumentTypes.length < parameterTypes.length - 1) {\n" + //
        "    return false;\n" + //
        "}" + //
        "").to("");
  }
  @Test public void issue41FunctionCall() {
    TrimmerTestsUtils.trimming("int a = f();a += 2;").to("int a = f()+2;");
  }
  @Test public void issue43() {
    TrimmerTestsUtils
        .trimming("" //
            + "String t = Z2;  "//
            + " t = t.f(A).f(b) + t.f(c);   "//
            + "return (t + 3);    ")
        .to(""//
            + "String t = Z2.f(A).f(b) + Z2.f(c);" //
            + "return (t + 3);" //
            + "");
  }
  @Test public void issue46() {
    TrimmerTestsUtils
        .trimming("" + //
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
    TrimmerTestsUtils.trimming("int f() { int f = 0; for (int i: X) $ += f(i); return f;}")//
        .to("int f(){int $=0;for(int i:X)$+=f(i);return $;}");
  }
  @Test public void issue51() {
    TrimmerTestsUtils.trimming("int f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .to("int f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }
  @Test public void issue52A() {
    TrimmerTestsUtils.trimming("void m() { return; }").to("void m() {}");
  }
  @Test public void issue52A1() {
    TrimmerTestsUtils.trimming("void m() { return a; }").to("");
  }
  @Test public void issue52B1() {
    TrimmerTestsUtils.trimming("void m() { if (a) { f(); return; }}").to("void m() { if (a) { f(); ; }}");
  }
  @Test public void issue52B2() {
    TrimmerTestsUtils.trimming("void m() { if (a) ++i; else { f(); return; }").to("void m() { if (a) ++i; else { f(); ; }");
  }
  @Test public void issue53() {
    TrimmerTestsUtils.trimming("int[] is = f(); for (int i: is) f(i);")//
        .to("for (int i: f()) f(i);");
  }
  @Test public void issue54DoNonSideEffect() {
    TrimmerTestsUtils.trimming("int a  = f; do { b[i] = a; } while (b[i] != a);")//
        .to("do { b[i] = f; } while (b[i] != f);");
  }
  @Test public void issue54DoNonSideEffectEmptyBody() {
    TrimmerTestsUtils.trimming("int a = f(); do ; while (a != 1);")//
        .to("");
  }
  @Test public void issue54DoWhile() {
    TrimmerTestsUtils.trimming("int a  = f(); do { b[i] = 2; ++i; } while (b[i] != a);")//
        .to("");
  }
  @Test public void issue54DoWithBlock() {
    TrimmerTestsUtils.trimming("int a  = f(); do { b[i] = a;  ++i; } while (b[i] != a);")//
        .to("");
  }
  @Test public void issue54doWithoutBlock() {
    TrimmerTestsUtils.trimming("int a  = f(); do b[i] = a; while (b[i] != a);")//
        .to("");
  }
  @Test public void issue54ForEnhanced() {
    TrimmerTestsUtils.trimming("int a  = f(); for (int i: a) b[i] = x;")//
        .to(" for (int i: f()) b[i] = x;");
  }
  @Test public void issue54ForEnhancedNonSideEffectLoopHeader() {
    TrimmerTestsUtils.trimming("int a  = f; for (int i: a) b[i] = b[i-1];")//
        .to("for (int i: f) b[i] = b[i-1];");
  }
  @Test public void issue54ForEnhancedNonSideEffectWithBody() {
    TrimmerTestsUtils.trimming("int a  = f; for (int i: j) b[i] = a;")//
        .to(" for(int i:j)b[i]=f; ");
  }
  @Test public void issue54ForPlain() {
    TrimmerTestsUtils.trimming("int a  = f(); for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .to("");
  }
  @Test public void issue54ForPlainNonSideEffect() {
    TrimmerTestsUtils.trimming("int a  = f; for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .to("for (int i = 0; i < 100;  ++i) b[i] = f;");
  }
  @Test public void issue54ForPlainUseInCondition() {
    TrimmerTestsUtils.trimming("int a  = f(); for (int i = 0; a < 100;  ++i) b[i] = 3;")//
        .to("");
  }
  @Test public void issue54ForPlainUseInConditionNonSideEffect() {
    TrimmerTestsUtils.trimming("int a  = f; for (int i = 0; a < 100;  ++i) b[i] = 3;")//
        .to("for (int i = 0; f < 100;  ++i) b[i] = 3;");
  }
  @Test public void issue54ForPlainUseInInitializer() {
    TrimmerTestsUtils.trimming("int a  = f(); for (int i = a; i < 100; i++) b[i] = 3;")//
        .to(" for (int i = f(); i < 100; i++) b[i] = 3;");
  }
  @Test public void issue54ForPlainUseInInitializerNonSideEffect() {
    TrimmerTestsUtils.trimming("int a  = f; for (int i = a; i < 100; i *= a) b[i] = 3;")//
        .to(" for (int i = f; i < 100; i *= f) b[i] = 3;");
  }
  @Test public void issue54ForPlainUseInUpdaters() {
    TrimmerTestsUtils.trimming("int a  = f(); for (int i = 0; i < 100; i *= a) b[i] = 3;")//
        .to("");
  }
  @Test public void issue54ForPlainUseInUpdatersNonSideEffect() {
    TrimmerTestsUtils.trimming("int a  = f; for (int i = 0; i < 100; i *= a) b[i] = 3;")//
        .to("for (int i = 0; i < 100; i *= f) b[i] = 3;");
  }
  @Test public void issue54While() {
    TrimmerTestsUtils.trimming("int a  = f(); while (c) b[i] = a;")//
        .to("");
  }
  @Test public void issue54WhileNonSideEffect() {
    TrimmerTestsUtils.trimming("int a  = f; while (c) b[i] = a;")//
        .to("while (c) b[i] = f;");
  }
  @Test public void issue54WhileScopeDoesNotInclude() {
    TrimmerTestsUtils.included("int a  = f(); while (c) b[i] = a;", VariableDeclarationFragment.class)//
        .notIn(new DeclarationInitializerStatementTerminatingScope());
  }
  @Test public void issue57a() {
    TrimmerTestsUtils.trimming("void m(List<Expression>... expressions) { }").to("void m(List<Expression>... ess) {}");
  }
  @Test public void issue57b() {
    TrimmerTestsUtils.trimming("void m(Expression... expression) { }").to("void m(Expression... es) {}");
  }
  @Test public void issue58a() {
    TrimmerTestsUtils.trimming("X f(List<List<Expression>> expressions){}").to("X f(List<List<Expression>> ess){}");
  }
  @Test public void issue58b() {
    TrimmerTestsUtils.trimming("X f(List<Expression>[] expressions){}").to("X f(List<Expression>[] ess){}");
  }
  @Test public void issue58c() {
    TrimmerTestsUtils.trimming("X f(List<Expression>[] expressions){}").to("X f(List<Expression>[] ess){}");
  }
  @Test public void issue58d() {
    TrimmerTestsUtils.trimming("X f(List<Expression>... expressions){}").to("X f(List<Expression>... ess){}");
  }
  @Test public void issue58e() {
    TrimmerTestsUtils.trimming("X f(Expression[]... expressions){}").to("X f(Expression[]... ess){}");
  }
  @Test public void issue58f() {
    TrimmerTestsUtils.trimming("X f(Expression[][]... expressions){}").to("X f(Expression[][]... esss){}");
  }
  @Test public void issue58g() {
    TrimmerTestsUtils.trimming("X f(List<Expression[][]>... expressions){}").to("X f(List<Expression[][]>... essss){}");
  }
  @Test public void issue62a() {
    TrimmerTestsUtils.trimming("int f(int i) { for(;;++i) if(false) break; return i; }").to("");
  }
  @Test public void issue62b() {
    TrimmerTestsUtils.trimming("int f(int i) { for(;i<100;i=i+1) if(false) break; return i; }").to("");
  }
  @Test public void issue62c() {
    TrimmerTestsUtils.trimming("int f(int i) { while(++i > 999) if(i>99) break; return i;}").to("");
  }
  @Test public void issue64a() {
    TrimmerTestsUtils.trimming("void f() {" + //
        "    final int a = f();\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").to("");
  }
  @Test public void issue64b() {
    TrimmerTestsUtils.trimming("void f() {" + //
        "    final int a = 3;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").to("");
  }
  @Test public void issue73a() {
    TrimmerTestsUtils.trimming("void foo(StringBuilder sb) {}").to("void foo(StringBuilder b) {}");
  }
  @Test public void issue73b() {
    TrimmerTestsUtils.trimming("void foo(DataOutput dataOutput) {}").to("void foo(DataOutput o) {}");
  }
  @Test public void issue73c() {
    TrimmerTestsUtils.trimming("void foo(Integer integer, ASTNode astn) {}").to("void foo(Integer i, ASTNode n) {}");
  }
  @Test public void linearTransformation() {
    TrimmerTestsUtils.trimming("plain * the + kludge").to("the*plain+kludge");
  }
  @Test public void literalVsLiteral() {
    TrimmerTestsUtils.trimming("1 < 102333").to("");
  }
  @Test public void longChainComparison() {
    TrimmerTestsUtils.trimming("a == b == c == d").to("");
  }
  @Test public void longChainParenthesisComparison() {
    TrimmerTestsUtils.trimming("(a == b == c) == d").to("");
  }
  @Test public void longChainParenthesisNotComparison() {
    TrimmerTestsUtils.trimming("(a == b == c) != d").to("");
  }
  @Test public void longerChainParenthesisComparison() {
    TrimmerTestsUtils.trimming("(a == b == c == d == e) == d").to("");
  }
  @Test public void massiveInlining() {
    TrimmerTestsUtils.trimming("int a,b,c;String t = zE4;if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;")//
        .to("int a,b,c;if(2*3.1415*180>a||zE4.concat(sS)==1922&&zE4.length()>3)return c>5;") //
        .to("");
  }
  @Test public void methodWithLastIf() {
    TrimmerTestsUtils.trimming("int f() { if (a) { f(); g(); h();}").to("int f() { if (!a) return;  f(); g(); h();");
  }
  @Test public void nestedIf1() {
    TrimmerTestsUtils.trimming("if (a) if (b) i++;").to("if (a && b) i++;");
  }
  @Test public void nestedIf2() {
    TrimmerTestsUtils.trimming("if (a) if (b) i++; else ; else ; ").to("if (a && b) i++; else ;");
  }
  @Test public void nestedIf3() {
    TrimmerTestsUtils.trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else;else{++y;f();g();z();}");
  }
  @Test public void nestedIf33() {
    TrimmerTestsUtils.trimming("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .to(" if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}")//
        .to(" if(x){if(a&&b)++i;}else{++y;f();g();}")//
    ;
  }
  @Test public void nestedIf33a() {
    TrimmerTestsUtils.trimming("if (x) { if (a && b) i++; } else { y++; f(); g(); }")//
        .to(" if (x) {if(a&&b)++i;} else{++y;f();g();}");
  }
  @Test public void nestedIf33b() {
    TrimmerTestsUtils.trimming("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .to("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }
  @Test public void nestedIf3c() {
    TrimmerTestsUtils.trimming("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .to(" if(x) {if(a&&b)i++;} else {++y;f();g();}");
  }
  @Test public void nestedIf3d() {
    TrimmerTestsUtils.trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else; else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)i++;} else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)++i;} else{++y;f();g();z();}") //
    ;
  }
  @Test public void nestedIf3e() {
    TrimmerTestsUtils.trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to(" if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }
  @Test public void nestedIf3f() {
    TrimmerTestsUtils.trimming("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .to(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }
  @Test public void nestedIf3f1() {
    TrimmerTestsUtils.trimming(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }
  @Test public void nestedIf3x() {
    TrimmerTestsUtils.trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)i++;}else{++y;f();g();z();}") //
    ;
  }
  @Test public void nestedTernaryAlignment() {
    TrimmerTestsUtils.trimming("int b=3==4?5==3?2:3:5==3?2:3*3;").to("int b=3==4?5==3?2:3:5!=3?3*3:2;");
  }
  @Test public void noChange() {
    TrimmerTestsUtils.trimming("12").to("");
    TrimmerTestsUtils.trimming("true").to("");
    TrimmerTestsUtils.trimming("null").to("");
    TrimmerTestsUtils.trimming("on*of*no*notion*notion").to("no*of*on*notion*notion");
  }
  @Test public void noChange0() {
    TrimmerTestsUtils.trimming("kludge + the * plain ").to("");
  }
  @Test public void noChange1() {
    TrimmerTestsUtils.trimming("the * plain").to("");
  }
  @Test public void noChange2() {
    TrimmerTestsUtils.trimming("plain + kludge").to("");
  }
  @Test public void noinliningIntoSynchronizedStatement() {
    TrimmerTestsUtils.trimming("int a  = f(); synchronized(this) { int b = a; }")//
        .to("");
  }
  @Test public void noinliningIntoSynchronizedStatementEvenWithoutSideEffect() {
    TrimmerTestsUtils.trimming("int a  = f; synchronized(this) { int b = a; }")//
        .to("");
  }
  @Test public void noinliningIntoTryStatement() {
    TrimmerTestsUtils.trimming("int a  = f(); try { int b = a; } catch (Exception E) {}")//
        .to("");
  }
  @Test public void noinliningIntoTryStatementEvenWithoutSideEffect() {
    TrimmerTestsUtils.trimming("int a  = f; try { int b = a; } catch (Exception E) {}")//
        .to("");
  }
  @Test public void notOfAnd() {
    TrimmerTestsUtils.trimming("!(A && B)").to("!A || !B");
  }
  @Test public void oneMultiplication() {
    TrimmerTestsUtils.trimming("f(a,b,c,d) * f(a,b,c)").to("f(a,b,c) * f(a,b,c,d)");
  }
  @Test public void oneMultiplicationAlternate() {
    TrimmerTestsUtils.trimming("f(a,b,c,d,e) * f(a,b,c)").to("f(a,b,c) * f(a,b,c,d,e)");
  }
  @Test public void orFalse3ORTRUE() {
    TrimmerTestsUtils.trimming("false || false || false").to("false");
  }
  @Test public void orFalse4ORTRUE() {
    TrimmerTestsUtils.trimming("false || false || false || false").to("false");
  }
  @Test public void orFalseANDOf3WithoutBoolean() {
    TrimmerTestsUtils.trimming("a && b && false").to("");
  }
  @Test public void orFalseANDOf3WithoutBooleanA() {
    TrimmerTestsUtils.trimming("x && a && b").to("");
  }
  @Test public void orFalseANDOf3WithTrue() {
    TrimmerTestsUtils.trimming("true && x && true && a && b").to("x && a && b");
  }
  @Test public void orFalseANDOf3WithTrueA() {
    TrimmerTestsUtils.trimming("a && b && true").to("a && b");
  }
  @Test public void orFalseANDOf4WithoutBoolean() {
    TrimmerTestsUtils.trimming("a && b && c && false").to("");
  }
  @Test public void orFalseANDOf4WithoutBooleanA() {
    TrimmerTestsUtils.trimming("x && a && b && c").to("");
  }
  @Test public void orFalseANDOf4WithTrue() {
    TrimmerTestsUtils.trimming("x && true && a && b && c").to("x && a && b && c");
  }
  @Test public void orFalseANDOf4WithTrueA() {
    TrimmerTestsUtils.trimming("a && b && c && true").to("a && b && c");
  }
  @Test public void orFalseANDOf5WithoutBoolean() {
    TrimmerTestsUtils.trimming("false && a && b && c && d").to("");
  }
  @Test public void orFalseANDOf5WithoutBooleanA() {
    TrimmerTestsUtils.trimming("x && a && b && c && d").to("");
  }
  @Test public void orFalseANDOf5WithTrue() {
    TrimmerTestsUtils.trimming("x && a && b && c && true && true && true && d").to("x && a && b && c && d");
  }
  @Test public void orFalseANDOf5WithTrueA() {
    TrimmerTestsUtils.trimming("true && a && b && c && d").to("a && b && c && d");
  }
  @Test public void orFalseANDOf6WithoutBoolean() {
    TrimmerTestsUtils.trimming("a && b && c && false && d && e").to("");
  }
  @Test public void orFalseANDOf6WithoutBooleanA() {
    TrimmerTestsUtils.trimming("x && a && b && c && d && e").to("");
  }
  @Test public void orFalseANDOf6WithoutBooleanWithParenthesis() {
    TrimmerTestsUtils.trimming("(x && (a && b)) && (c && (d && e))").to("");
  }
  @Test public void orFalseANDOf6WithTrue() {
    TrimmerTestsUtils.trimming("x && a && true && b && c && d && e").to("x && a && b && c && d && e");
  }
  @Test public void orFalseANDOf6WithTrueA() {
    TrimmerTestsUtils.trimming("a && b && c && true && d && e").to("a && b && c && d && e");
  }
  @Test public void orFalseANDOf6WithTrueWithParenthesis() {
    TrimmerTestsUtils.trimming("x && (true && (a && b && true)) && (c && (d && e))").to("x && a && b && c && d && e");
  }
  @Test public void orFalseANDOf7WithMultipleTrueValue() {
    TrimmerTestsUtils.trimming("(a && (b && true)) && (c && (d && (e && (true && true))))").to("a &&b &&c &&d &&e ");
  }
  @Test public void orFalseANDOf7WithoutBooleanAndMultipleFalseValue() {
    TrimmerTestsUtils.trimming("(a && (b && false)) && (c && (d && (e && (false && false))))").to("");
  }
  @Test public void orFalseANDOf7WithoutBooleanWithParenthesis() {
    TrimmerTestsUtils.trimming("(a && b) && (c && (d && (e && false)))").to("");
  }
  @Test public void orFalseANDOf7WithTrueWithParenthesis() {
    TrimmerTestsUtils.trimming("true && (a && b) && (c && (d && (e && true)))").to("a &&b &&c &&d &&e ");
  }
  @Test public void orFalseANDWithFalse() {
    TrimmerTestsUtils.trimming("b && a").to("");
  }
  @Test public void orFalseANDWithoutBoolean() {
    TrimmerTestsUtils.trimming("b && a").to("");
  }
  @Test public void orFalseANDWithTrue() {
    TrimmerTestsUtils.trimming("true && b && a").to("b && a");
  }
  @Test public void orFalseFalseOrFalse() {
    TrimmerTestsUtils.trimming("false ||false").to("false");
  }
  @Test public void orFalseORFalseWithSomething() {
    TrimmerTestsUtils.trimming("true || a").to("");
  }
  @Test public void orFalseORFalseWithSomethingB() {
    TrimmerTestsUtils.trimming("false || a || false").to("a");
  }
  @Test public void orFalseOROf3WithFalse() {
    TrimmerTestsUtils.trimming("x || false || b").to("x || b");
  }
  @Test public void orFalseOROf3WithFalseB() {
    TrimmerTestsUtils.trimming("false || a || b || false").to("a || b");
  }
  @Test public void orFalseOROf3WithoutBoolean() {
    TrimmerTestsUtils.trimming("a || b").to("");
  }
  @Test public void orFalseOROf3WithoutBooleanA() {
    TrimmerTestsUtils.trimming("x || a || b").to("");
  }
  @Test public void orFalseOROf4WithFalse() {
    TrimmerTestsUtils.trimming("x || a || b || c || false").to("x || a || b || c");
  }
  @Test public void orFalseOROf4WithFalseB() {
    TrimmerTestsUtils.trimming("a || b || false || c").to("a || b || c");
  }
  @Test public void orFalseOROf4WithoutBoolean() {
    TrimmerTestsUtils.trimming("a || b || c").to("");
  }
  @Test public void orFalseOROf4WithoutBooleanA() {
    TrimmerTestsUtils.trimming("x || a || b || c").to("");
  }
  @Test public void orFalseOROf5WithFalse() {
    TrimmerTestsUtils.trimming("x || a || false || c || d").to("x || a || c || d");
  }
  @Test public void orFalseOROf5WithFalseB() {
    TrimmerTestsUtils.trimming("a || b || c || d || false").to("a || b || c || d");
  }
  @Test public void orFalseOROf5WithoutBoolean() {
    TrimmerTestsUtils.trimming("a || b || c || d").to("");
  }
  @Test public void orFalseOROf5WithoutBooleanA() {
    TrimmerTestsUtils.trimming("x || a || b || c || d").to("");
  }
  @Test public void orFalseOROf6WithFalse() {
    TrimmerTestsUtils.trimming("false || x || a || b || c || d || e").to("x || a || b || c || d || e");
  }
  @Test public void orFalseOROf6WithFalseWithParenthesis() {
    TrimmerTestsUtils.trimming("x || (a || (false) || b) || (c || (d || e))").to("x || a || b || c || d || e");
  }
  @Test public void orFalseOROf6WithFalseWithParenthesisB() {
    TrimmerTestsUtils.trimming("(a || b) || false || (c || false || (d || e || false))").to("a || b || c || d || e");
  }
  @Test public void orFalseOROf6WithoutBoolean() {
    TrimmerTestsUtils.trimming("a || b || c || d || e").to("");
  }
  @Test public void orFalseOROf6WithoutBooleanA() {
    TrimmerTestsUtils.trimming("x || a || b || c || d || e").to("");
  }
  @Test public void orFalseOROf6WithoutBooleanWithParenthesis() {
    TrimmerTestsUtils.trimming("(a || b) || (c || (d || e))").to("");
  }
  @Test public void orFalseOROf6WithoutBooleanWithParenthesisA() {
    TrimmerTestsUtils.trimming("x || (a || b) || (c || (d || e))").to("");
  }
  @Test public void orFalseOROf6WithTwoFalse() {
    TrimmerTestsUtils.trimming("a || false || b || false || c || d || e").to("a || b || c || d || e");
  }
  @Test public void orFalseORSomethingWithFalse() {
    TrimmerTestsUtils.trimming("false || a || false").to("a");
  }
  @Test public void orFalseORSomethingWithTrue() {
    TrimmerTestsUtils.trimming("a || true").to("");
  }
  @Test public void orFalseORWithoutBoolean() {
    TrimmerTestsUtils.trimming("b || a").to("");
  }
  @Test public void orFalseProductIsNotANDDivOR() {
    TrimmerTestsUtils.trimming("2*a").to("");
  }
  @Test public void orFalseTrueAndTrueA() {
    TrimmerTestsUtils.trimming("true && true").to("true");
  }
  @Test public void overridenDeclaration() {
    TrimmerTestsUtils.trimming("int a = 3; a = f() ? 3 : 4;").to("int a = f() ? 3: 4;");
  }
  @Test public void paramAbbreviateBasic1() {
    TrimmerTestsUtils.trimming("void m(XMLDocument xmlDocument) {" + //
        "xmlDocument.exec(p);}").to("void m(XMLDocument d) {" + //
            "d.exec(p);}");
  }
  @Test public void paramAbbreviateBasic2() {
    TrimmerTestsUtils.trimming("int m(StringBuilder builder) {" + //
        "if(builder.exec())" + //
        "builder.clear();").to("int m(StringBuilder sb) {" + //
            "if(sb.exec())" + //
            "sb.clear();");
  }
  @Test public void paramAbbreviateCollision() {
    TrimmerTestsUtils.trimming("void m(Expression exp, Expression expresssion) { }").to("void m(Expression e, Expression expresssion) { }");
  }
  @Test public void paramAbbreviateConflictingWithLocal1() {
    TrimmerTestsUtils.trimming("void m(String string) {" + //
        "String s = null;" + //
        "string.substring(s, 2, 18);}").to("void m(String string){string.substring(null,2,18);}");
  }
  @Test public void paramAbbreviateConflictingWithLocal1Simplified() {
    TrimmerTestsUtils.trimming("void m(String string) {" + //
        "String s = X;" + //
        "string.substring(s, 2, 18);}").to("void m(String string){string.substring(X,2,18);}");
  }
  @Test public void paramAbbreviateConflictingWithLocal1SimplifiedFurther() {
    TrimmerTestsUtils.trimming("void m(String string) {" + //
        "String s = X;" + //
        "string.f(s);}").to("void m(String string){string.f(X);}");
  }
  @Test public void paramAbbreviateConflictingWithLocal2() {
    TrimmerTestsUtils.trimming("TCPConnection conn(TCPConnection tcpCon) {" + //
        " UDPConnection c = new UDPConnection(57);" + //
        " if(tcpCon.isConnected()) " + //
        "   c.disconnect();}").to("TCPConnection conn(TCPConnection tcpCon){" //
            + " if(tcpCon.isConnected())" //
            + "   (new UDPConnection(57)).disconnect();"//
            + "}");
  }
  @Test public void paramAbbreviateConflictingWithMethodName() {
    TrimmerTestsUtils.trimming("void m(BitmapManipulator bitmapManipulator) {" + //
        "bitmapManipulator.x().y();").to("void m(BitmapManipulator bm) {" + //
            "bm.x().y();");
  }
  @Test public void paramAbbreviateMultiple() {
    TrimmerTestsUtils.trimming("void m(StringBuilder stringBuilder, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
        "stringBuilder.clear();" + //
        "xmlDocument.open(stringBuilder.toString());" + //
        "dog.eat(xmlDocument.asEdible(cat));}").to("void m(StringBuilder sb, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
            "sb.clear();" + //
            "xmlDocument.open(sb.toString());" + //
            "dog.eat(xmlDocument.asEdible(cat));}");
  }
  @Test public void paramAbbreviateNestedMethod() {
    TrimmerTestsUtils.trimming("void f(Iterator iterator) {" + //
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
    TrimmerTestsUtils.trimming("a ? b+x+e+f:b+y+e+f").to("b+(a ? x : y)+e+f");
  }
  @Test public void postDecreementReturn() {
    TrimmerTestsUtils.trimming("a--; return a;").to("--a;return a;");
  }
  @Test public void postDecremntInFunctionCall() {
    TrimmerTestsUtils.trimming("f(a++, i--, b++, ++b);").to("");
  }
  @Test public void postfixToPrefixAvoidChangeOnLoopCondition() {
    TrimmerTestsUtils.trimming("for (int s = i; ++i; ++s);").to("");
  }
  @Test public void postfixToPrefixAvoidChangeOnLoopInitializer() {
    TrimmerTestsUtils.trimming("for (int s = i++; i < 10; ++s);").to("");
  }
  @Test public void postfixToPrefixAvoidChangeOnVariableDeclaration() {
    // We expect to print 2, but ++s will make it print 3
    TrimmerTestsUtils.trimming("int s = 2;" + //
        "int n = s++;" + //
        "S.out.print(n);").to("int s=2;S.out.print(s++);");
  }
  @Test public void postIncrementInFunctionCall() {
    TrimmerTestsUtils.trimming("f(i++);").to("");
  }
  @Test public void postIncrementReturn() {
    TrimmerTestsUtils.trimming("a++; return a;").to("++a;return a;");
  }
  @Test public void preDecreementReturn() {
    TrimmerTestsUtils.trimming("--a.b.c; return a.b.c;").to("return--a.b.c;");
  }
  @Test public void preDecrementReturn() {
    TrimmerTestsUtils.trimming("--a; return a;").to("return --a;");
  }
  @Test public void preDecrementReturn1() {
    TrimmerTestsUtils.trimming("--this.a; return this.a;").to("return --this.a;");
  }
  @Test public void prefixToPosfixIncreementSimple() {
    TrimmerTestsUtils.trimming("i++").to("++i");
  }
  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    assertThat(s, iz("{" + from + "}"));
    assertNotNull(s);
    final PostfixExpression e = Extract.findFirstPostfix(s);
    assertNotNull(e);
    assertThat(e, iz("i--"));
    final ASTNode parent = e.getParent();
    assertThat(parent, notNullValue());
    assertThat(parent, iz(from));
    assertThat(parent, is(not(instanceOf(Expression.class))));
    assertThat(new PostfixToPrefix().scopeIncludes(e), is(true));
    assertThat(new PostfixToPrefix().eligible(e), is(true));
    final Expression r = new PostfixToPrefix().replacement(e);
    assertThat(r, iz("--i"));
    TrimmerTestsUtils.trimming(from).to("for(int i=0;i<100;--i)--i;");
  }
  @Test public void prefixToPostfixIncreement() {
    TrimmerTestsUtils.trimming("for (int i = 0; i < 100; i++) i++;").to("for(int i=0;i<100;++i)++i;");
  }
  @Test public void preIncrementReturn() {
    TrimmerTestsUtils.trimming("++a; return a;").to("return ++a;");
  }
  @Test public void pushdowConditionalActualExampleFirstPass() {
    TrimmerTestsUtils
        .trimming("" //
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
    TrimmerTestsUtils
        .trimming("" //
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
    TrimmerTestsUtils.trimming("!!false").to("false");
  }
  @Test public void pushdownNot2LevelNotOfTrue() {
    TrimmerTestsUtils.trimming("!!true").to("true");
  }
  @Test public void pushdownNotActualExample() {
    TrimmerTestsUtils.trimming("!inRange(m, e)").to("");
  }
  @Test public void pushdownNotDoubleNot() {
    TrimmerTestsUtils.trimming("!!f()").to("f()");
  }
  @Test public void pushdownNotDoubleNotDeeplyNested() {
    TrimmerTestsUtils.trimming("!(((!f())))").to("f()");
  }
  @Test public void pushdownNotDoubleNotNested() {
    TrimmerTestsUtils.trimming("!(!f())").to("f()");
  }
  @Test public void pushdownNotEND() {
    TrimmerTestsUtils.trimming("a&&b").to("");
  }
  @Test public void pushdownNotMultiplication() {
    TrimmerTestsUtils.trimming("a*b").to("");
  }
  @Test public void pushdownNotNotOfAND() {
    TrimmerTestsUtils.trimming("!(a && b && c)").to("!a || !b || !c");
  }
  @Test public void pushdownNotNotOfAND2() {
    TrimmerTestsUtils.trimming("!(f() && f(5))").to("!f() || !f(5)");
  }
  @Test public void pushdownNotNotOfANDNested() {
    TrimmerTestsUtils.trimming("!(f() && (f(5)))").to("!f() || !f(5)");
  }
  @Test public void pushdownNotNotOfEQ() {
    TrimmerTestsUtils.trimming("!(3 == 5)").to("3 != 5");
  }
  @Test public void pushdownNotNotOfEQNested() {
    TrimmerTestsUtils.trimming("!((((3 == 5))))").to("3 != 5");
  }
  @Test public void pushdownNotNotOfFalse() {
    TrimmerTestsUtils.trimming("!false").to("true");
  }
  @Test public void pushdownNotNotOfGE() {
    TrimmerTestsUtils.trimming("!(3 >= 5)").to("3 < 5");
  }
  @Test public void pushdownNotNotOfGT() {
    TrimmerTestsUtils.trimming("!(3 > 5)").to("3 <= 5");
  }
  @Test public void pushdownNotNotOfLE() {
    TrimmerTestsUtils.trimming("!(3 <= 5)").to("3 > 5");
  }
  @Test public void pushdownNotNotOfLT() {
    TrimmerTestsUtils.trimming("!(3 < 5)").to("3 >= 5");
  }
  @Test public void pushdownNotNotOfNE() {
    TrimmerTestsUtils.trimming("!(3 != 5)").to("3 == 5");
  }
  @Test public void pushdownNotNotOfOR() {
    TrimmerTestsUtils.trimming("!(a || b || c)").to("!a && !b && !c");
  }
  @Test public void pushdownNotNotOfOR2() {
    TrimmerTestsUtils.trimming("!(f() || f(5))").to("!f() && !f(5)");
  }
  @Test public void pushdownNotNotOfTrue() {
    TrimmerTestsUtils.trimming("!true").to("false");
  }
  @Test public void pushdownNotNotOfTrue2() {
    TrimmerTestsUtils.trimming("!!true").to("true");
  }
  @Test public void pushdownNotNotOfWrappedOR() {
    TrimmerTestsUtils.trimming("!((a) || b || c)").to("!a && !b && !c");
  }
  @Test public void pushdownNotOR() {
    TrimmerTestsUtils.trimming("a||b").to("");
  }
  @Test public void pushdownNotSimpleNot() {
    TrimmerTestsUtils.trimming("!a").to("");
  }
  @Test public void pushdownNotSimpleNotOfFunction() {
    TrimmerTestsUtils.trimming("!f(a)").to("");
  }
  @Test public void pushdownNotSummation() {
    TrimmerTestsUtils.trimming("a+b").to("");
  }
  @Test public void pushdownTernaryActualExample() {
    TrimmerTestsUtils.trimming("next < values().length").to("");
  }
  @Test public void pushdownTernaryActualExample2() {
    TrimmerTestsUtils.trimming("!inRange(m, e) ? true : inner.go(r, e)").to("!inRange(m, e) || inner.go(r, e)");
  }
  @Test public void pushdownTernaryAlmostIdentical2Addition() {
    TrimmerTestsUtils.trimming("a ? b+d :b+ c").to("b+(a ? d : c)");
  }
  @Test public void pushdownTernaryAlmostIdentical3Addition() {
    TrimmerTestsUtils.trimming("a ? b+d +x:b+ c + x").to("b+(a ? d : c) + x");
  }
  @Test public void pushdownTernaryAlmostIdentical4AdditionLast() {
    TrimmerTestsUtils.trimming("a ? b+d+e+y:b+d+e+x").to("b+d+e+(a ? y : x)");
  }
  @Test public void pushdownTernaryAlmostIdentical4AdditionSecond() {
    TrimmerTestsUtils.trimming("a ? b+x+e+f:b+y+e+f").to("b+(a ? x : y)+e+f");
  }
  @Test public void pushdownTernaryAlmostIdenticalAssignment() {
    TrimmerTestsUtils.trimming("a ? (b=c) :(b=d)").to("b = a ? c : d");
  }
  @Test public void pushdownTernaryAlmostIdenticalFunctionCall() {
    TrimmerTestsUtils.trimming("a ? f(b) :f(c)").to("f(a ? b : c)");
  }
  @Test public void pushdownTernaryAlmostIdenticalMethodCall() {
    TrimmerTestsUtils.trimming("a ? y.f(b) :y.f(c)").to("y.f(a ? b : c)");
  }
  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall1Div2() {
    TrimmerTestsUtils.trimming("a ? f(b,x) :f(c,x)").to("f(a ? b : c,x)");
  }
  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall2Div2() {
    TrimmerTestsUtils.trimming("a ? f(x,b) :f(x,c)").to("f(x,a ? b : c)");
  }
  @Test public void pushdownTernaryAMethodCallDistinctReceiver() {
    TrimmerTestsUtils.trimming("a ? x.f(c) : y.f(d)").to("");
  }
  @Test public void pushdownTernaryDifferentTargetFieldRefernce() {
    TrimmerTestsUtils.trimming("a ? 1 + x.a : 1 + y.a").to("1+(a ? x.a : y.a)");
  }
  @Test public void pushdownTernaryFieldReferneceShort() {
    TrimmerTestsUtils.trimming("a ? R.b.c : R.b.d").to("");
  }
  @Test public void pushdownTernaryFunctionCall() {
    TrimmerTestsUtils.trimming("a ? f(b,c) : f(c)").to("!a?f(c):f(b,c)");
  }
  @Test public void pushdownTernaryFX() {
    TrimmerTestsUtils.trimming("a ? false : c").to("!a && c");
  }
  @Test public void pushdownTernaryIdenticalAddition() {
    TrimmerTestsUtils.trimming("a ? b+d :b+ d").to("b+d");
  }
  @Test public void pushdownTernaryIdenticalAdditionWtihParenthesis() {
    TrimmerTestsUtils.trimming("a ? (b+d) :(b+ d)").to("b+d");
  }
  @Test public void pushdownTernaryIdenticalAssignment() {
    TrimmerTestsUtils.trimming("a ? (b=c) :(b=c)").to("b = c");
  }
  @Test public void pushdownTernaryIdenticalAssignmentVariant() {
    TrimmerTestsUtils.trimming("a ? (b=c) :(b=d)").to("b=a?c:d");
  }
  @Test public void pushdownTernaryIdenticalFunctionCall() {
    TrimmerTestsUtils.trimming("a ? f(b) :f(b)").to("f(b)");
  }
  @Test public void pushdownTernaryIdenticalIncrement() {
    TrimmerTestsUtils.trimming("a ? b++ :b++").to("b++");
  }
  @Test public void pushdownTernaryIdenticalMethodCall() {
    TrimmerTestsUtils.trimming("a ? y.f(b) :y.f(b)").to("y.f(b)");
  }
  @Test public void pushdownTernaryIntoConstructor1Div1Location() {
    TrimmerTestsUtils.trimming("a.equal(b) ? new S(new Integer(4)) : new S(new Ineger(3))").to("new S(a.equal(b)? new Integer(4): new Ineger(3))");
  }
  @Test public void pushdownTernaryIntoConstructor1Div3() {
    TrimmerTestsUtils.trimming("a.equal(b) ? new S(new Integer(4),a,b) : new S(new Ineger(3),a,b)").to("new S(a.equal(b)? new Integer(4): new Ineger(3), a, b)");
  }
  @Test public void pushdownTernaryIntoConstructor2Div3() {
    TrimmerTestsUtils.trimming("a.equal(b) ? new S(a,new Integer(4),b) : new S(a, new Ineger(3), b)").to("new S(a,a.equal(b)? new Integer(4): new Ineger(3),b)");
  }
  @Test public void pushdownTernaryIntoConstructor3Div3() {
    TrimmerTestsUtils.trimming("a.equal(b) ? new S(a,b,new Integer(4)) : new S(a,b,new Ineger(3))").to("new S(a, b, a.equal(b)? new Integer(4): new Ineger(3))");
  }
  @Test public void pushdownTernaryIntoConstructorNotSameArity() {
    TrimmerTestsUtils.trimming("a ? new S(a,new Integer(4),b) : new S(new Ineger(3))")
        .to("!a?new S(new Ineger(3)):new S(a,new Integer(4),b)                                                                                                                  ");
  }
  @Test public void pushdownTernaryIntoPrintln() {
    TrimmerTestsUtils.trimming("    if (s.equals(t))\n"//
        + "      S.out.println(Hey + res);\n"//
        + "    else\n"//
        + "      S.out.println(Ho + x + a);").to("S.out.println(s.equals(t)?Hey+res:Ho+x+a);");
  }
  @Test public void pushdownTernaryLongFieldRefernece() {
    TrimmerTestsUtils.trimming("externalImage ? R.string.webview_contextmenu_image_download_action : R.string.webview_contextmenu_image_save_action")
        .to("!externalImage ? R.string.webview_contextmenu_image_save_action : R.string.webview_contextmenu_image_download_action");
  }
  @Test public void pushdownTernaryMethodInvocationFirst() {
    TrimmerTestsUtils.trimming("a?b():c").to("!a?c:b()");
  }
  @Test public void pushdownTernaryNoBoolean() {
    TrimmerTestsUtils.trimming("a?b:c").to("");
  }
  @Test public void pushdownTernaryNoReceiverReceiver() {
    TrimmerTestsUtils.trimming("a < b ? f() : a.f()").to("");
  }
  @Test public void pushdownTernaryNotOnMINUS() {
    TrimmerTestsUtils.trimming("a ? -c :-d").to("");
  }
  @Test public void pushdownTernaryNotOnMINUSMINUS1() {
    TrimmerTestsUtils.trimming("a ? --c :--d").to("");
  }
  @Test public void pushdownTernaryNotOnMINUSMINUS2() {
    TrimmerTestsUtils.trimming("a ? c-- :d--").to("");
  }
  @Test public void pushdownTernaryNotOnNOT() {
    TrimmerTestsUtils.trimming("a ? !c :!d").to("");
  }
  @Test public void pushdownTernaryNotOnPLUS() {
    TrimmerTestsUtils.trimming("a ? +x : +y").to("");
  }
  @Test public void pushdownTernaryNotOnPLUSPLUS() {
    TrimmerTestsUtils.trimming("a ? x++ :y++").to("");
  }
  @Test public void pushdownTernaryNotSameFunctionInvocation() {
    TrimmerTestsUtils.trimming("a?b(x):d(x)").to("");
  }
  @Test public void pushdownTernaryNotSameFunctionInvocation2() {
    TrimmerTestsUtils.trimming("a?x.f(x):x.d(x)").to("");
  }
  @Test public void pushdownTernaryOnMethodCall() {
    TrimmerTestsUtils.trimming("a ? y.f(c,b) :y.f(c)").to("!a?y.f(c):y.f(c,b)");
  }
  @Test public void pushdownTernaryParFX() {
    TrimmerTestsUtils.trimming("a ?( false):true").to("!a && true");
  }
  @Test public void pushdownTernaryParTX() {
    TrimmerTestsUtils.trimming("a ? (((true ))): c").to("a || c");
  }
  @Test public void pushdownTernaryParXF() {
    TrimmerTestsUtils.trimming("a ? b : (false)").to("a && b");
  }
  @Test public void pushdownTernaryParXT() {
    TrimmerTestsUtils.trimming("a ? b : ((true))").to("!a || b");
  }
  @Test public void pushdownTernaryReceiverNoReceiver() {
    TrimmerTestsUtils.trimming("a < b ? a.f() : f()").to("a>=b?f():a.f()");
  }
  @Test public void pushdownTernaryToClasConstrctor() {
    TrimmerTestsUtils.trimming("a ? new B(a,b,c) : new B(a,x,c)").to("new B(a,a ? b : x ,c)");
  }
  @Test public void pushdownTernaryToClasConstrctorTwoDifferenes() {
    TrimmerTestsUtils.trimming("a ? new B(a,b,c) : new B(a,x,y)").to("");
  }
  @Test public void pushdownTernaryToClassConstrctorNotSameNumberOfArgument() {
    TrimmerTestsUtils.trimming("a ? new B(a,b) : new B(a,b,c)").to("");
  }
  @Test public void pushdownTernaryTX() {
    TrimmerTestsUtils.trimming("a ? true : c").to("a || c");
  }
  @Test public void pushdownTernaryXF() {
    TrimmerTestsUtils.trimming("a ? b : false").to("a && b");
  }
  @Test public void pushdownTernaryXT() {
    TrimmerTestsUtils.trimming("a ? b : true").to("!a || b");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar01() {
    TrimmerTestsUtils
        .trimming(
            " public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   S.out.println(res.j);   return res; ")
        .to(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   S.out.println($.j);   return $; ");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar02() {
    TrimmerTestsUtils
        .trimming(
            " int res = blah.length();   if (blah.contains(0xDEAD))    return res * 2;   if (res % 2 ==0)    return ++res;   if (blah.startsWith(\"y\")) {    return y(res);   int x = res + 6;   if (x>1)    return res + x;   res -= 1;   return res; ")
        .to(" int $ = blah.length();   if (blah.contains(0xDEAD))    return $ * 2;   if ($ % 2 ==0)    return ++$;   if (blah.startsWith(\"y\")) {    return y($);   int x = $ + 6;   if (x>1)    return $ + x;   $ -= 1;   return $; ");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar03() {
    TrimmerTestsUtils
        .trimming(
            " public BlahClass(int i) {    j = 2*i;      public final int j;   public int yada7(final String blah) {   final BlahClass res = new BlahClass(blah.length());   if (blah.contains(0xDEAD))    return res.j;   int x = blah.length()/2;   if (x==3)    return x;   x = y(res.j - x);   return x; ")
        .to(" public BlahClass(int i) {    j = 2*i;      public final int j;   public int yada7(final String blah) {   final BlahClass res = new BlahClass(blah.length());   if (blah.contains(0xDEAD))    return res.j;   int $ = blah.length()/2;   if ($==3)    return $;   $ = y(res.j - $);   return $; ");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar04() {
    TrimmerTestsUtils.trimming("int res = 0;   String $ = blah + known;   y(res + $.length());   return res + $.length();").to("");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar05() {
    TrimmerTestsUtils
        .trimming(
            "  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private void doStuff(final BlahClass res) {     S.out.println(res.j);   S.out.println(res.j);   return res; ")
        .to("  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass($.j);     S.out.println(res2.j);     doStuff(res2);        private void doStuff(final BlahClass res) {     S.out.println(res.j);   S.out.println($.j);   return $; ");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar06() {
    TrimmerTestsUtils
        .trimming(
            "  j = 2*i;   }      public final int j;    public void yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private int doStuff(final BlahClass r) {     final BlahClass res = new BlahClass(r.j);     return res.j + 1;   S.out.println(res.j); ")
        .to("  j = 2*i;   }      public final int j;    public void yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private int doStuff(final BlahClass r) {     final BlahClass $ = new BlahClass(r.j);     return $.j + 1;   S.out.println(res.j); ");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar07() {
    TrimmerTestsUtils
        .trimming(
            "  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println(res.j);   return res; ")
        .to("  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println($.j);   return $; ");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar08() {
    TrimmerTestsUtils
        .trimming(
            " public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   if (res.j == 0)    return null;   S.out.println(res.j);   return res; ")
        .to(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   if ($.j == 0)    return null;   S.out.println($.j);   return $; ");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar09() {
    TrimmerTestsUtils
        .trimming(
            " public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   if (res.j == 0)    return null;   S.out.println(res.j);   return null;")
        .to("");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar10() {
    TrimmerTestsUtils
        .trimming(
            "@Override public IMarkerResolution[] getResolutions(final IMarker m) {   try {    final Spartanization s = All.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY)); ")
        .to("@Override public IMarkerResolution[] getResolutions(final IMarker m) {   try {    final Spartanization $ = All.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY)); ");
  }
  @Ignore @Test public void reanmeReturnVariableToDollar11() {
    TrimmerTestsUtils.trimming("").to("");
  }
  @Test public void removeSuper() {
    TrimmerTestsUtils.trimming("class T { T() { super(); }").to("class T { T() { }");
  }
  @Test public void removeSuperWithArgument() {
    TrimmerTestsUtils.trimming("class T { T() { super(a); a();}").to("");
  }
  @Test public void removeSuperWithStatemen() {
    TrimmerTestsUtils.trimming("class T { T() { super(); a++;}").to("class T { T() { ++a;}");
  }
  @Test public void renameToDollarActual() {
    TrimmerTestsUtils.trimming(//
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
    TrimmerTestsUtils.trimming("int f() { for (int a: as) return a; }")//
        .to(" int f() {for(int $:as)return $;}");
  }
  @Test public void replaceClassInstanceCreationWithFactoryInfixExpression() {
    TrimmerTestsUtils.trimming("Integer x = new Integer(1 + 9);").to("Integer x = Integer.valueOf(1 + 9);");
  }
  @Test public void replaceClassInstanceCreationWithFactoryInvokeMethode() {
    TrimmerTestsUtils.trimming("String x = new String(f());").to("String x = String.valueOf(f());");
  }
  @Ignore @Test public void replaceClassInstanceCreationWithFactoryClassInstanceCreation() {
    TrimmerTestsUtils.trimming("Character x = new Character(new Character(f()));").to("Character x = Character.valueOf(Character.valueOf(f()));");
  }
  @Test public void replaceInitializationInReturn() {
    TrimmerTestsUtils.trimming("int a = 3; return a + 4;").to("return 3 + 4;");
  }
  @Test public void replaceTwiceInitializationInReturn() {
    TrimmerTestsUtils.trimming("int a = 3; return a + 4 << a;").to("return 3 + 4 << 3;");
  }
  @Test public void rightSimplificatioForNulNNVariableReplacement() {
    final InfixExpression e = i("null != a");
    final Wring<InfixExpression> w = Toolbox.instance.find(e);
    assertNotNull(w);
    assertTrue(w.scopeIncludes(e));
    assertTrue(w.eligible(e));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) w).replacement(e);
    assertNotNull(replacement);
    assertEquals("a != null", replacement.toString());
  }
  @Test public void rightSipmlificatioForNulNNVariable() {
    assertThat(Toolbox.instance.find(i("null != a")), instanceOf(InfixComparisonSpecific.class));
  }
  @Test public void sequencerFirstInElse() {
    TrimmerTestsUtils.trimming("if (a) {b++; c++; ++d;} else { f++; g++; return x;}").to("if (!a) {f++; g++; return x;} b++; c++; ++d; ");
  }
  @Test public void shorterChainParenthesisComparison() {
    TrimmerTestsUtils.trimming("a == b == c").to("");
  }
  @Test public void shorterChainParenthesisComparisonLast() {
    TrimmerTestsUtils.trimming("b == a * b * c * d * e * f * g * h == a").to("");
  }
  @Test public void shortestBranchIfWithComplexNestedIf3() {
    TrimmerTestsUtils.trimming("if (a) {f(); g(); h();} else if (a) ++i; else ++j;").to("");
  }
  @Test public void shortestBranchIfWithComplexNestedIf4() {
    TrimmerTestsUtils.trimming("if (a) {f(); g(); h(); ++i;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;}");
  }
  @Test public void shortestBranchIfWithComplexNestedIf5() {
    TrimmerTestsUtils.trimming("if (a) {f(); g(); h(); ++i; f();} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();}");
  }
  @Test public void shortestBranchIfWithComplexNestedIf6() {
    TrimmerTestsUtils.trimming("if (a) {f(); g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();j++;}");
  }
  @Test public void shortestBranchIfWithComplexNestedIf7() {
    TrimmerTestsUtils.trimming("if (a) {f(); ++i; g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;f();j++;}");
  }
  @Test public void shortestBranchIfWithComplexNestedIf8() {
    TrimmerTestsUtils.trimming("if (a) {f(); ++i; g(); h(); ++i; u++; f(); j++;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;u++;f();j++;}");
  }
  @Test public void shortestBranchIfWithComplexNestedIfPlain() {
    TrimmerTestsUtils.trimming("if (a) {f(); g(); h();} else { i++; j++;}").to("if(!a){i++;j++;}else{f();g();h();}");
  }
  @Test public void shortestBranchIfWithComplexSimpler() {
    TrimmerTestsUtils.trimming("if (a) {f(); g(); h();} else  i++; j++;").to("if(!a)i++;else{f();g();h();}++j;");
  }
  @Test public void shortestBranchInIf() {
    TrimmerTestsUtils.trimming("   int a=0;\n" + //
        "   if (s.equals(known)){\n" + //
        "     S.console();\n" + //
        "   } else {\n" + //
        "     a=3;\n" + //
        "   }\n" + //
        "").to("int a=0; if(!s.equals(known))a=3;else S.console();");
  }
  @Test public void shortestFirstAlignment() {
    TrimmerTestsUtils.trimming("n.isSimpleName() ? (SimpleName) n //\n" + //
        "            : n.isQualifiedName() ? ((QualifiedName) n).getName() //\n" + //
        "                : null").to("");//
  }
  @Test public void shortestFirstAlignmentShortened() {
    TrimmerTestsUtils.trimming("n.isF() ? (SimpleName) n \n" + //
        "            : n.isG() ? ((QualifiedName) n).getName() \n" + //
        "                : null").to("");//
  }
  @Test public void shortestFirstAlignmentShortenedFurther() {
    TrimmerTestsUtils.trimming("n.isF() ? (A) n : n.isG() ? ((B) n).f() \n" + //
        "                : null").to("");//
  }
  @Test public void shortestFirstAlignmentShortenedFurtherAndFurther() {
    TrimmerTestsUtils.trimming("n.isF() ? (A) n : n.isG() ? (B) n :  null").to("");//
  }
  @Test public void shortestIfBranchFirst01() {
    TrimmerTestsUtils
        .trimming(""//
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
    TrimmerTestsUtils
        .trimming("" //
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
    TrimmerTestsUtils.trimming("" + //
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
    TrimmerTestsUtils.trimming("" + //
        "      int res = 0;\n" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          res += 2;\n" + //
        "        else " + //
        "       if (s.charAt(i) == 'd')\n" + //
        "          res -= 1;\n" + //
        "      return res;\n" + //
        "").to("");
  }
  @Test public void shortestIfBranchFirst02c() {
    final CompilationUnit u = Wrap.Statement.intoCompilationUnit("" + //
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
    final VariableDeclarationFragment f = Extract.firstVariableDeclarationFragment(u);
    assertThat(f, notNullValue());
    assertThat(f, iz(" res = 0"));
    assertThat(Extract.nextStatement(f),
        iz(" for (int i = 0;i < s.length();++i)\n"//
            + "       if (s.charAt(i) == 'a')\n"//
            + "          res += 2;\n"//
            + "        else "//
            + "       if (s.charAt(i) == 'd')\n"//
            + "          res -= 1;\n"));
  }
  @Test public void shortestIfBranchWithFollowingCommandsSequencer() {
    TrimmerTestsUtils.trimming("" + //
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
    TrimmerTestsUtils.trimming("x + y > z").to("");
  }
  @Test public void shortestOperand02() {
    TrimmerTestsUtils.trimming("k = k + 4;if (2 * 6 + 4 == k) return true;").to("");
  }
  @Test public void shortestOperand05() {
    TrimmerTestsUtils.trimming("    final W s = new W(\"bob\");\n" + //
        "    return s.l(hZ).l(\"-ba\").toString() == \"bob-ha-banai\";").to("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }
  @Test public void shortestOperand09() {
    TrimmerTestsUtils.trimming("return 2 - 4 < 50 - 20 - 10 - 5;").to("return 2 - 4 < 50 - 5 - 10 - 20 ;");
  }
  @Test public void shortestOperand10() {
    TrimmerTestsUtils.trimming("return b == true;} ").to("return b;}");
  }
  @Test public void shortestOperand11() {
    TrimmerTestsUtils.trimming("int h,u,m,a,n;return b == true && n + a > m - u || h > u;").to("int h,u,m,a,n;return b&&a+n>m-u||h>u;");
  }
  @Test public void shortestOperand12() {
    TrimmerTestsUtils.trimming("int k = 15; return 7 < k; ").to("return 7<15;");
  }
  @Test public void shortestOperand13() {
    TrimmerTestsUtils.trimming("return (2 > 2 + a) == true;").to("return 2>a+2;");
  }
  @Test public void shortestOperand13a() {
    TrimmerTestsUtils.trimming("(2 > 2 + a) == true").to("2>a+2 ");
  }
  @Test public void shortestOperand13b() {
    TrimmerTestsUtils.trimming("(2) == true").to("2 ");
  }
  @Test public void shortestOperand13c() {
    TrimmerTestsUtils.trimming("2 == true").to("2 ");
  }
  @Test public void shortestOperand14() {
    TrimmerTestsUtils.trimming("Integer t = new Integer(5);   return (t.toString() == null);    ").to("return((new Integer(5)).toString()==null);");
  }
  @Test public void shortestOperand17() {
    TrimmerTestsUtils.trimming("5 ^ a.getNum()").to("a.getNum() ^ 5");
  }
  @Test public void shortestOperand19() {
    TrimmerTestsUtils.trimming("k.get().operand() ^ a.get()").to("a.get() ^ k.get().operand()");
  }
  @Test public void shortestOperand20() {
    TrimmerTestsUtils.trimming("k.get() ^ a.get()").to("a.get() ^ k.get()");
  }
  @Test public void shortestOperand22() {
    TrimmerTestsUtils.trimming("return f(a,b,c,d,e) + f(a,b,c,d) + f(a,b,c) + f(a,b) + f(a) + f();").to("");
  }
  @Test public void shortestOperand23() {
    TrimmerTestsUtils.trimming("return f() + \".\";     }").to("");
  }
  @Test public void shortestOperand24() {
    TrimmerTestsUtils.trimming("f(a,b,c,d) & 175 & 0").to("f(a,b,c,d) & 0 & 175");
  }
  @Test public void shortestOperand25() {
    TrimmerTestsUtils.trimming("f(a,b,c,d) & bob & 0 ").to("bob & f(a,b,c,d) & 0");
  }
  @Test public void shortestOperand27() {
    TrimmerTestsUtils.trimming("return f(a,b,c,d) + f(a,b,c) + f();     } ").to("");
  }
  @Test public void shortestOperand28() {
    TrimmerTestsUtils.trimming("return f(a,b,c,d) * f(a,b,c) * f();     } ").to("return f()*f(a,b,c)*f(a,b,c,d);}");
  }
  @Test public void shortestOperand29() {
    TrimmerTestsUtils.trimming("f(a,b,c,d) ^ f() ^ 0").to("f() ^ f(a,b,c,d) ^ 0");
  }
  @Test public void shortestOperand30() {
    TrimmerTestsUtils.trimming("f(a,b,c,d) & f()").to("f() & f(a,b,c,d)");
  }
  @Test public void shortestOperand31() {
    TrimmerTestsUtils.trimming("return f(a,b,c,d) | \".\";     }").to("");
  }
  @Test public void shortestOperand32() {
    TrimmerTestsUtils.trimming("return f(a,b,c,d) && f();     }").to("");
  }
  @Test public void shortestOperand33() {
    TrimmerTestsUtils.trimming("return f(a,b,c,d) || f();     }").to("");
  }
  @Test public void shortestOperand34() {
    TrimmerTestsUtils.trimming("return f(a,b,c,d) + someVar; ").to("");
  }
  @Test public void shortestOperand37() {
    TrimmerTestsUtils.trimming("return sansJavaExtension(f) + n + \".\"+ extension(f);").to("");
  }
  @Test public void simpleBooleanMethod() {
    TrimmerTestsUtils.trimming("boolean f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .to("boolean f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }
  @Test public void simplifyBlockComplexEmpty0() {
    TrimmerTestsUtils.trimming("{}").to("/* empty */    ");
  }
  @Test public void simplifyBlockComplexEmpty1() {
    TrimmerTestsUtils.trimming("{;;{;{{}}}{;}{};}").to(" ");
  }
  @Test public void simplifyBlockComplexSingleton() {
    TrimmerTestsUtils.assertSimplifiesTo("{;{{;;return b; }}}", "return b;", new BlockSimplify(), Wrap.Statement);
  }
  @Test public void simplifyBlockDeeplyNestedReturn() {
    TrimmerTestsUtils.assertSimplifiesTo("{{{;return c;};;};}", "return c;", new BlockSimplify(), Wrap.Statement);
  }
  /* Begin of already good tests */
  @Test public void simplifyBlockEmpty() {
    TrimmerTestsUtils.assertSimplifiesTo("{;;}", "", new BlockSimplify(), Wrap.Statement);
  }
  @Test public void simplifyBlockExpressionVsExpression() {
    TrimmerTestsUtils.trimming("6 - 7 < a * 3").to("6 - 7 < 3 * a");
  }
  @Test public void simplifyBlockLiteralVsLiteral() {
    TrimmerTestsUtils.trimming("if (a) return b; else c();").to("if(a)return b;c();");
  }
  @Test public void simplifyBlockThreeStatements() {
    TrimmerTestsUtils.assertSimplifiesTo("{i++;{{;;return b; }}j++;}", "i++;return b;j++;", new BlockSimplify(), Wrap.Statement);
  }
  @Test public void simplifyLogicalNegationNested() {
    TrimmerTestsUtils.trimming("!((a || b == c) && (d || !(!!c)))").to("!a && b != c || !d && c");
  }
  @Test public void simplifyLogicalNegationNested1() {
    TrimmerTestsUtils.trimming("!(d || !(!!c))").to("!d && c");
  }
  @Test public void simplifyLogicalNegationNested2() {
    TrimmerTestsUtils.trimming("!(!d || !!!c)").to("d && c");
  }
  @Test public void simplifyLogicalNegationOfAnd() {
    TrimmerTestsUtils.trimming("!(f() && f(5))").to("!f() || !f(5)");
  }
  @Test public void simplifyLogicalNegationOfEquality() {
    TrimmerTestsUtils.trimming("!(3 == 5)").to("3!=5");
  }
  @Test public void simplifyLogicalNegationOfGreater() {
    TrimmerTestsUtils.trimming("!(3 > 5)").to("3 <= 5");
  }
  @Test public void simplifyLogicalNegationOfGreaterEquals() {
    TrimmerTestsUtils.trimming("!(3 >= 5)").to("3 < 5");
  }
  @Test public void simplifyLogicalNegationOfInequality() {
    TrimmerTestsUtils.trimming("!(3 != 5)").to("3 == 5");
  }
  @Test public void simplifyLogicalNegationOfLess() {
    TrimmerTestsUtils.trimming("!(3 < 5)").to("3 >= 5");
  }
  @Test public void simplifyLogicalNegationOfLessEquals() {
    TrimmerTestsUtils.trimming("!(3 <= 5)").to("3 > 5");
  }
  @Test public void simplifyLogicalNegationOfMultipleAnd() {
    TrimmerTestsUtils.trimming("!(a && b && c)").to("!a || !b || !c");
  }
  @Test public void simplifyLogicalNegationOfMultipleOr() {
    TrimmerTestsUtils.trimming("!(a || b || c)").to("!a && !b && !c");
  }
  @Test public void simplifyLogicalNegationOfNot() {
    TrimmerTestsUtils.trimming("!!f()").to("f()");
  }
  @Test public void simplifyLogicalNegationOfOr() {
    TrimmerTestsUtils.trimming("!(f() || f(5))").to("!f() && !f(5)");
  }
  @Test public void sortAddition1() {
    TrimmerTestsUtils.trimming("1 + 2 - 3 - 4 + 5 / 6 - 7 + 8 * 9  + A> k + 4").to("8*9+1+2-3-4+5 / 6-7+A>k+4");
  }
  @Test public void sortAddition2() {
    TrimmerTestsUtils.trimming("1 + 2 < 3 & 7 + 4 > 2 + 1 || 6 - 7 < 2 + 1").to("1+2 <3&4+7>1+2||6-7<1+2");
  }
  @Test public void sortAddition3() {
    TrimmerTestsUtils.trimming("6 - 7 < 1 + 2").to("");
  }
  @Test public void sortAddition4() {
    TrimmerTestsUtils.trimming("a + 11 + 2 < 3 & 7 + 4 > 2 + 1").to("7 + 4 > 2 + 1 & a + 11 + 2 < 3");
  }
  @Test public void sortAdditionClassConstantAndLiteral() {
    TrimmerTestsUtils.trimming("1+A< 12").to("A+1<12");
  }
  @Test public void sortAdditionFunctionClassConstantAndLiteral() {
    TrimmerTestsUtils.trimming("1+A+f()< 12").to("f()+A+1<12");
  }
  @Test public void sortAdditionThreeOperands1() {
    TrimmerTestsUtils.trimming("1.0+2222+3").to("");
  }
  @Test public void sortAdditionThreeOperands2() {
    TrimmerTestsUtils.trimming("1.0+1+124+1").to("");
  }
  @Test public void sortAdditionThreeOperands3() {
    TrimmerTestsUtils.trimming("1+2F+33+142+1").to("");
  }
  @Test public void sortAdditionThreeOperands4() {
    TrimmerTestsUtils.trimming("1+2+'a'").to("");
  }
  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_a() {
    TrimmerTestsUtils.trimming("1111+211").to("211+1111");
  }
  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_b() {
    TrimmerTestsUtils.trimming("211+1111").to("");
  }
  @Test public void sortAdditionTwoOperands1() {
    TrimmerTestsUtils.trimming("1+2F").to("");
  }
  @Test public void sortAdditionTwoOperands2() {
    TrimmerTestsUtils.trimming("2.0+1").to("1+2.0");
  }
  @Test public void sortAdditionTwoOperands3() {
    TrimmerTestsUtils.trimming("1+2L").to("");
  }
  @Test public void sortAdditionTwoOperands4() {
    TrimmerTestsUtils.trimming("2L+1").to("1+2L");
  }
  @Test public void sortAdditionUncertain() {
    TrimmerTestsUtils.trimming("1+a").to("");
  }
  @Test public void sortAdditionVariableClassConstantAndLiteral() {
    TrimmerTestsUtils.trimming("1+A+a< 12").to("a+A+1<12");
  }
  @Test public void sortConstantMultiplication() {
    TrimmerTestsUtils.trimming("a*2").to("2*a");
  }
  @Test public void sortDivision() {
    TrimmerTestsUtils.trimming("2.1/34.2/1.0").to("2.1/1.0/34.2");
  }
  @Test public void sortDivisionLetters() {
    TrimmerTestsUtils.trimming("x/b/a").to("x/a/b");
  }
  @Test public void sortDivisionNo() {
    TrimmerTestsUtils.trimming("2.1/3").to("");
  }
  @Test public void sortSubstraction() {
    TrimmerTestsUtils.trimming("1-c-b").to("1-b-c");
  }
  @Test public void sortThreeOperands1() {
    TrimmerTestsUtils.trimming("1.0*2222*3").to("");
  }
  @Test public void sortThreeOperands2() {
    TrimmerTestsUtils.trimming("1.0*1*124*1").to("");
  }
  @Test public void sortThreeOperands3() {
    TrimmerTestsUtils.trimming("1*2F*33*142*1").to("");
  }
  @Test public void sortThreeOperands4() {
    TrimmerTestsUtils.trimming("1*2*'a'").to("");
  }
  @Test public void sortTwoOperands0CheckThatWeSortByLength_a() {
    TrimmerTestsUtils.trimming("1111*211").to("211*1111");
  }
  @Test public void sortTwoOperands0CheckThatWeSortByLength_b() {
    TrimmerTestsUtils.trimming("211*1111").to("");
  }
  @Test public void sortTwoOperands1() {
    TrimmerTestsUtils.trimming("1*2F").to("");
  }
  @Test public void sortTwoOperands2() {
    TrimmerTestsUtils.trimming("2.0*1").to("1*2.0");
  }
  @Test public void sortTwoOperands3() {
    TrimmerTestsUtils.trimming("1*2L").to("");
  }
  @Test public void sortTwoOperands4() {
    TrimmerTestsUtils.trimming("2L*1").to("1*2L");
  }
  @Test public void stringFromBuilderGeneral() {
    TrimmerTestsUtils.trimming("new StringBuilder(myName).append(\"\'s grade is \").append(100).toString()").to("myName + \"\'s grade is \" + 100");
  }
  @Test public void stringFromBuilderNoStringComponents() {
    TrimmerTestsUtils.trimming("new StringBuilder(0).append(1).toString()").to("\"\" + 0 + 1");
  }
  @Test public void stringFromBuilderAddParenthesis() {
    TrimmerTestsUtils.trimming("new StringBuilder(f()).append(1+1).toString()").to("\"\" + f() + (1+1)");
  }
  @Test public void synchronizedBraces() {
    TrimmerTestsUtils.trimming("" //
        + "    synchronized (variables) {\n" //
        + "      for (final String key : variables.keySet())\n"//
        + "        $.variables.put(key, variables.get(key));\n" //
        + "    }").to("");
  }
  @Test public void ternarize05() {
    TrimmerTestsUtils.trimming(" int res = 0; "//
        + "if (s.equals(532))    "//
        + "res += 6;   "//
        + "else    "//
        + "res += 9;      ").to("int res=0;res+=s.equals(532)?6:9;");
  }
  @Test public void ternarize05a() {
    TrimmerTestsUtils.trimming(" int res = 0; "//
        + "if (s.equals(532))    "//
        + "res += 6;   "//
        + "else    "//
        + "res += 9;      "//
        + "return res; ").to("int res=0;res+=s.equals(532)?6:9;return res;");
  }
  @Test public void ternarize07() {
    TrimmerTestsUtils
        .trimming("" //
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
    TrimmerTestsUtils.trimming("" //
        + "String res;" //
        + "res = s;   " //
        + "if (res==true)    " //
        + "  res = s + 0xABBA;   " //
        + "S.out.println(res); " //
        + "").to("String res=s;if(res)res=s+0xABBA;S.out.println(res);");
  }
  @Test public void ternarize07aa() {
    TrimmerTestsUtils.trimming("String res=s;if(res==true)res=s+0xABBA;S.out.println(res);").to("String res=s==true?s+0xABBA:s;S.out.println(res);");
  }
  @Test public void ternarize07b() {
    TrimmerTestsUtils
        .trimming("" //
            + "String res =s ;" //
            + "if (res.equals(532)==true)    " //
            + "  res = s + 0xABBA;   " //
            + "S.out.println(res); ")
        .to("" //
            + "String res=s.equals(532)==true?s+0xABBA:s;S.out.println(res);");
  }
  @Test public void ternarize09() {
    TrimmerTestsUtils.trimming("if (s.equals(532)) {    return 6;}else {    return 9;}").to("return s.equals(532)?6:9; ");
  }
  @Test public void ternarize10() {
    TrimmerTestsUtils.trimming("String res = s, foo = bar;   "//
        + "if (res.equals(532)==true)    " //
        + "res = s + 0xABBA;   "//
        + "S.out.println(res); ").to("String res=s.equals(532)==true?s+0xABBA:s,foo=bar;S.out.println(res);");
  }
  @Test public void ternarize12() {
    TrimmerTestsUtils.trimming("String res = s;   if (s.equals(532))    res = res + 0xABBA;   S.out.println(res); ").to("String res=s.equals(532)?s+0xABBA:s;S.out.println(res);");
  }
  @Test public void ternarize13() {
    TrimmerTestsUtils.trimming("String res = m, foo;  if (m.equals(f())==true)   foo = M; ")//
        .to("String foo;if(m.equals(f())==true)foo=M;")//
        .to("String foo;if(m.equals(f()))foo=M;");
  }
  @Test public void ternarize13Simplified() {
    TrimmerTestsUtils.trimming("String r = m, f;  if (m.e(f()))   f = M; ")//
        .to("String f;if(m.e(f()))f=M;");
  }
  @Test public void ternarize13SimplifiedMore() {
    TrimmerTestsUtils.trimming("if (m.equals(f())==true)   foo = M; ").to("if (m.equals(f())) foo=M;");
  }
  @Test public void ternarize13SimplifiedMoreAndMore() {
    TrimmerTestsUtils.trimming("f (m.equals(f())==true); foo = M; ").to("f (m.equals(f())); foo=M;");
  }
  @Test public void ternarize13SimplifiedMoreAndMoreAndMore() {
    TrimmerTestsUtils.trimming("f (m.equals(f())==true);  ").to("f (m.equals(f()));");
  }
  @Test public void ternarize13SimplifiedMoreVariant() {
    TrimmerTestsUtils.trimming("if (m==true)   foo = M; ").to("if (m) foo=M;");
  }
  @Test public void ternarize13SimplifiedMoreVariantShorter() {
    TrimmerTestsUtils.trimming("if (m==true)   f(); ").to("if (m) f();");
  }
  @Test public void ternarize13SimplifiedMoreVariantShorterAsExpression() {
    TrimmerTestsUtils.trimming("f (m==true);   f(); ").to("f (m); f();");
  }
  @Test public void ternarize14() {
    TrimmerTestsUtils.trimming("String res=m,foo=GY;if (res.equals(f())==true){foo = M;int k = 2;k = 8;S.out.println(foo);}f();")
        .to("String res=m,foo=GY;if(res.equals(f())){foo=M;int k=8;S.out.println(foo);}f();");
  }
  @Test public void ternarize16() {
    TrimmerTestsUtils.trimming("String res = m;  int num1, num2, num3;  if (m.equals(f()))   num2 = 2; ").to("");
  }
  @Test public void ternarize16a() {
    TrimmerTestsUtils.trimming("int n1, n2 = 0, n3;\n" + //
        "  if (d)\n" + //
        "    n2 = 2;").to("int n1, n2 = d ? 2: 0, n3;");
  }
  public void ternarize18() {
    TrimmerTestsUtils.trimming("final String res=s;System.out.println(s.equals(res)?tH3+res:h2A+res+0);")//
        .to("System.out.println(s.equals(s)?tH3+res:h2A+s+0);");
  }
  @Test public void ternarize21() {
    TrimmerTestsUtils.trimming("if (s.equals(532)){    S.out.println(gG);    S.out.l(kKz);} f(); ").to("");
  }
  @Test public void ternarize21a() {
    TrimmerTestsUtils.trimming("   if (s.equals(known)){\n" + //
        "     S.out.l(gG);\n" + //
        "   } else {\n" + //
        "     S.out.l(kKz);\n" + //
        "   }").to("S.out.l(s.equals(known)?gG:kKz);");
  }
  @Test public void ternarize22() {
    TrimmerTestsUtils.trimming("int a=0;   if (s.equals(532)){    S.console();    a=3;} f(); ").to("");
  }
  @Test public void ternarize26() {
    TrimmerTestsUtils.trimming("int a=0;   if (s.equals(532)){    a+=2;   a-=2; } f(); ").to("");
  }
  @Test public void ternarize33() {
    TrimmerTestsUtils.trimming("int a, b=0;   if (b==3){    a=4; } ")//
        .to("int a;if(0==3){a=4;}") //
        .to("int a;if(0==3)a=4;") //
        .to(null);
  }
  @Test public void ternarize35() {
    TrimmerTestsUtils.trimming("int a,b=0,c=0;a=4;if(c==3){b=2;}")//
        .to("int a=4,b=0,c=0;if(c==3)b=2;");
  }
  @Test public void ternarize36() {
    TrimmerTestsUtils.trimming("int a,b=0,c=0;a=4;if (c==3){  b=2;   a=6; } f();").to("int a=4,b=0,c=0;if(c==3){b=2;a=6;} f();");
  }
  @Test public void ternarize38() {
    TrimmerTestsUtils.trimming("int a, b=0;if (b==3){    a+=2+r();a-=6;} f();").to("");
  }
  @Test public void ternarize41() {
    TrimmerTestsUtils.trimming("int a,b,c,d;a = 3;b = 5; d = 7;if (a == 4)while (b == 3) c = a; else while (d == 3)c =a*a; ")
        .to("int a=3,b,c,d;b=5;d=7;if(a==4)while(b==3)c=a;else while(d==3)c=a*a;");
  }
  @Test public void ternarize42() {
    TrimmerTestsUtils.trimming(" int a, b; a = 3;b = 5; if (a == 4) if (b == 3) b = 2; else{b = a; b=3;}  else if (b == 3) b = 2; else{ b = a*a;         b=3; }")//
        .to("int a=3,b;b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=b=3;}else if(b==3)b=2;else{b=3*3;b=3;}")//
        .to("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=3*3;b=3;}")//
        .to("");
  }
  @Test public void ternarize45() {
    TrimmerTestsUtils.trimming("if (m.equals(f())==true) if (b==3){ return 3; return 7;}   else    if (b==3){ return 2;}     a=7; ")//
        .to("if (m.equals(f())) {if (b==3){ return 3; return 7;} if (b==3){ return 2;}   }  a=7; ");
  }
  @Test public void ternarize46() {
    TrimmerTestsUtils.trimming(//
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
    TrimmerTestsUtils.trimming("if (s.equals(532)){ S.out.println(gG); S.out.l(kKz); } f();").to("");
  }
  @Test public void ternarize52() {
    TrimmerTestsUtils.trimming("int a=0,b = 0,c,d = 0,e = 0;if (a < b) {c = d;c = e;} f();")//
        .to("");
  }
  @Test public void ternarize54() {
    TrimmerTestsUtils.trimming("int $=1,xi=0,xj=0,yi=0,yj=0; if(xi > xj == yi > yj)++$;else--$;")//
        .to(" int $=1,xj=0,yi=0,yj=0;      if(0>xj==yi>yj)++$;else--$;");
  }
  @Test public void ternarize55() {
    TrimmerTestsUtils.trimming("if (key.equals(markColumn))\n" + //
        " to.put(key, a.toString());\n" + //
        "else\n" + //
        "  to.put(key, missing(key, a) ? Z2 : get(key, a));").to("to.put(key,key.equals(markColumn)?a.toString():missing(key,a)?Z2:get(key,a));");
  }
  @Test public void ternarize56() {
    TrimmerTestsUtils.trimming("if (target == 0) {p.f(X); p.v(0); p.f(q +  target); p.v(q * 100 / target); } f();") //
        .to("if(target==0){p.f(X);p.v(0);p.f(q+target);p.v(100*q / target); } f();");
  }
  @Test public void ternarizeIntoSuperMethodInvocation() {
    TrimmerTestsUtils.trimming("a ? super.f(a, b, c) : super.f(a, x, c)").to("super.f(a, a ? b : x, c)");
  }
  @Test public void ternaryPushdownOfReciever() {
    TrimmerTestsUtils.trimming("a ? b.f():c.f()").to("(a?b:c).f()");
  }
  @Test public void testPeel() {
    assertEquals("on * notion * of * no * nothion != the * plain + kludge", Wrap.Expression.off(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge")));
  }
  @Test public void twoMultiplication1() {
    TrimmerTestsUtils.trimming("f(a,b,c,d) * f()").to("f() * f(a,b,c,d)");
  }
  @Test public void twoOpportunityExample() {
    assertThat(TrimmerTestsUtils.countOpportunities(new Trimmer(),
        (CompilationUnit) As.COMPILIATION_UNIT.ast(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge"))), is(2));
    assertThat(TrimmerTestsUtils.countOpportunities(new Trimmer(),
        (CompilationUnit) As.COMPILIATION_UNIT.ast(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge"))), is(2));
  }
  @Test public void useOutcontextToManageStringAmbiguity() {
    TrimmerTestsUtils.trimming("1+2+s<3").to("s+1+2<3");
  }
  @Test public void vanillaShortestFirstConditionalNoChange() {
    TrimmerTestsUtils.trimming("literal ? CONDITIONAL_OR : CONDITIONAL_AND").to("");
  }
  @Test public void xorSortClassConstantsAtEnd() {
    TrimmerTestsUtils.trimming("f(a,b,c,d) ^ BOB").to("");
  }
}
