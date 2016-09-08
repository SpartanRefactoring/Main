package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.ExpressionComparator.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static il.org.spartan.spartanizer.spartanizations.TESTUtils.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.apply;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;
import il.org.spartan.spartanizer.utils.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) public class ValidTrimmerTest {
  @Test public void actualExampleForSortAddition() {
    trimming("1 + b.statements().indexOf(declarationStmt)").to(null);
  }

  @Test public void actualExampleForSortAdditionInContext() {
    final String from = "2 + a < b";
    final String expected = "a + 2 < b";
    final Wrap w = Wrap.Expression;
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
    assertSimilar(expected, peeled);
  }

  @Test public void andWithCLASS_CONSTANT() {
    trimming("(x >> 18) & MASK_BITS").to(null);
    trimming("(x >> 18) & MASK_6BITS").to(null);
  }

  @Test public void assignmentAssignmentChain1() {
    trimming("c = a = 13; b = 13;").to("b = c = a = 13;");
  }

  @Test public void assignmentAssignmentChain2() {
    trimming("a = 13; b= c = 13;").to("b = c = a = 13;");
  }

  @Test public void assignmentAssignmentChain3() {
    trimming("a = b = 13; c = d = 13;").to("c = d = a = b = 13;");
  }

  @Test public void assignmentAssignmentChain4() {
    trimming("a1 = a2 = a3 = a4 = 13; b1 = b2 = b3 = b4 = b5 = 13;")//
        .to("b1 = b2 = b3 = b4 = b5 = a1 = a2 = a3 = a4 = 13;");
  }

  @Test public void assignmentAssignmentChain5() {
    trimming("a1 = (a2 = (a3 = (a4 = 13))); b1 = b2 = b3 = ((((b4 = (b5 = 13)))));")//
        .to("b1=b2=b3=((((b4=(b5=a1=(a2=(a3=(a4=13))))))));");
  }

  @Test public void assignmentAssignmentNew() {
    trimming("a = new B(); b= new B();").to(null);
  }

  @Test public void assignmentAssignmentNewArray() {
    trimming("a = new A[3]; b= new A[3];").to(null);
  }

  @Test public void assignmentAssignmentNull() {
    trimming("c = a = null; b = null;").to(null);
  }

  @Test public void assignmentAssignmentSideEffect() {
    trimming("a = f(); b= f();").to(null);
  }

  @Test public void assignmentAssignmentVanilla() {
    trimming("a = 13; b= 13;").to("b = a = 13;");
  }

  @Test public void assignmentAssignmentVanilla0() {
    trimming("a = 0; b = 0;").to("b = a = 0;");
  }

  @Test public void assignmentAssignmentVanillaScopeIncludes() {
    included("a = 3; b = 3;", Assignment.class).in(new AssignmentAndAssignment());
  }

  @Test public void assignmentAssignmentVanillaScopeIncludesNull() {
    included("a = null; b = null;", Assignment.class).notIn(new AssignmentAndAssignment());
  }

  @Test public void assignmentReturn0() {
    trimming("a = 3; return a;").to("return a = 3;");
  }

  @Test public void assignmentReturn1() {
    trimming("a = 3; return (a);").to("return a = 3;");
  }

  @Test public void assignmentReturn2() {
    trimming("a += 3; return a;").to("return a += 3;");
  }

  @Test public void assignmentReturn3() {
    trimming("a *= 3; return a;").to("return a *= 3;");
  }

  @Test public void assignmentReturniNo() {
    trimming("b = a = 3; return a;").to(null);
  }

  @Test public void blockSimplifyVanilla() {
    trimming("if (a) {f(); }").to("if (a) f();");
  }

  @Test public void blockSimplifyVanillaSimplified() {
    trimming(" {f(); }").to("f();");
  }

  @Test public void bugInLastIfInMethod() {
    trimming("" + //
        "        @Override public void messageFinished(final LocalMessage myMessage, final int number, final int ofTotal) {\n" + //
        "          if (!isMessageSuppressed(myMessage)) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(myMessage);\n" + //
        "            stats.unreadMessageCount += myMessage.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += myMessage.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "@Override public void messageFinished(final LocalMessage myMessage,final int number,final int ofTotal){if(isMessageSuppressed(myMessage))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(myMessage);stats.unreadMessageCount+=myMessage.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=myMessage.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod1() {
    trimming("" + //
        "        @Override public void f() {\n" + //
        "          if (!isMessageSuppressed(message)) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "@Override public void f(){if(isMessageSuppressed(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod2() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g(message)) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod3() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g(a)) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g(a))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod4() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod5() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void bugInLastIfInMethod6() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            final int messages = 3;\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g)return;final int messages=3;messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void bugInLastIfInMethod7() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            foo();\n" + //
        "            bar();\n" + //
        "          }\n" + //
        "        }").to("public void f(){if(g)return;foo();bar();}");
  }

  @Test public void bugInLastIfInMethod8() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (g) {\n" + //
        "            foo();\n" + //
        "            bar();\n" + //
        "          }\n" + //
        "        }").to("public void f(){if(!g)return;foo();bar();}");
  }

  @Test public void bugIntroducingMISSINGWord1() {
    trimming("b.f(a) && -1 == As.g(f).h(c) ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("b.f(a) && As.g(f).h(c) == -1 ? o(s,b,g(f)) : b.f(\".in\") && !y(d,b)? o(b.z(u,v),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord1a() {
    trimming("-1 == As.g(f).h(c)").to("As.g(f).h(c)==-1");
  }

  @Test public void bugIntroducingMISSINGWord1b() {
    trimming("b.f(a) && X ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("b.f(a)&&X?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1c() {
    trimming("Y ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("Y?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1d() {
    trimming("Y ? Z : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)").to("Y?Z:b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1e() {
    trimming("Y ? Z : R ? null : S ? null : T").to("Y?Z:!R&&!S?T:null");
  }

  @Test public void bugIntroducingMISSINGWord2() {
    trimming(
        "name.endsWith(testSuffix) &&  MakeAST.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2a() {
    trimming(
        "name.endsWith(testSuffix) &&  MakeAST.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2b() {
    trimming(
        "name.endsWith(testSuffix) &&  T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("name.endsWith(testSuffix) && T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2c() {
    trimming(
        "X && T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("X && T ? objects(s,name,makeInFile(f)) : name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2d() {
    trimming("X && T ? E : Y ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X && T ? E : !Y && !dotOutExists(d,name) ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e() {
    trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e1() {
    trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(x, Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(x,Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e2() {
    trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(g, Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(g,Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2f() {
    trimming("X &&  T ? E : Y ? null : Z ? null : F").to("X&&T?E:!Y&&!Z?F:null");
  }

  @Test public void bugIntroducingMISSINGWord3() {
    trimming(
        "name.endsWith(testSuffix) && -1 == MakeAST.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
            .to("name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord3a() {
    trimming("!name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
        .to("name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWordTry1() {
    trimming(
        "name.endsWith(testSuffix) && -1 == MakeAST.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("name.endsWith(testSuffix) && MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWordTry2() {
    trimming("!(intent.getBooleanExtra(EXTRA_FROM_SHORTCUT, false) && !K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName()))")
        .to("!intent.getBooleanExtra(EXTRA_FROM_SHORTCUT,false)||K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName())");
  }

  @Test public void bugIntroducingMISSINGWordTry3() {
    trimming("!(f.g(X, false) && !a.b.e(m.h()))").to("!f.g(X,false)||a.b.e(m.h())");
  }

  @Test public void bugOfMissingTry() {
    trimming("!(A && B && C && true && D)").to("!A||!B||!C||false||!D");
  }

  @Test public void canonicalFragementExamples() {
    trimming("int a; a = 3;").to("int a = 3;");
    trimming("int a = 2; if (b) a = 3; ").to("int a = b ? 3 : 2;");
    trimming("int a = 2; a += 3; ").to("int a = 2 + 3;");
    trimming("int a = 2; a = 3 * a; ").to("int a = 3 * 2;");
    trimming("int a = 2; return 3 * a; ").to("return 3 * 2;");
    trimming("int a = 2; return a; ").to("return 2;");
  }

  @Test public void canonicalFragementExamplesWithExraFragments() {
    trimming("int a = 2; a = 3 * a * b; ").to("int a = 3 * 2 * b;");
    trimming("int a = 2; a = 3 * a; ").to("int a = 3 * 2;");
    trimming("int a = 2; a += 3; ").to("int a = 2 + 3;");
    trimming("int a = 2; a += b; ").to("int a = 2 + b;");
    trimming("int a = 2, b = 11; a = 3 * a * b; ")//
        .to("int a=2;a=3*a*11;")//
        .to("int a=3*2*11;")//
        .to("int a=66;");
    trimming("int a = 2, b=1; a += b; ").to("int a=2;a+=1;").to("int a=2+1;");
    trimming("int a = 2,b=1; if (b) a = 3; ").to("int a=2;if(1)a=3;").to("int a=1?3:2;");
    trimming("int a = 2, b = 1; return a + 3 * b; ").to("int b=1;return 2+3*b;");
    trimming("int a =2,b=2; if (x) a = 2*a;").to("int a=x?2*2:2, b=2;");
    trimming("int a = 2, b; a = 3 * a * b; ").to("int a = 2, b; a *= 3 * b; ").to(null);
    trimming("int a = 2, b; a += b; ").to(null);
    trimming("int a =2,b; if (x) a = 2*a;").to("int a=x?2*2:2, b;");
    trimming("int a = 2, b; return a + 3 * b; ").to("return 2 + 3*b;");
    trimming("int a =2; if (x) a = 3*a;").to("int a=x?3*2:2;");
    trimming("int a = 2; return 3 * a * a; ").to("return 3 * 2 * 2;");
    trimming("int a = 2; return 3 * a * b; ").to("return 3 * 2 * b;");
    trimming("int a = 2; return a; ").to("return 2;");
    trimming("int a,b=2; a = b;").to("int a;a=2;").to("int a=2;");
    trimming("int a,b; a = 3;").to("int a = 3, b;");
    trimming("int a; if (x) a = 3; else a++;").to("int a;if(x)a=3;else++a;");
    trimming("int b=5,a = 2,c=4; return 3 * a * b * c; ").to("int a=2,c=4;return 3*a*5*c;");
    trimming("int b=5,a = 2,c; return 3 * a * b * c; ").to("int a = 2; return 3 * a * 5 * c;");
  }

  @Test public void canonicalFragementExamplesWithExraFragmentsX() {
    trimming("int a; if (x) a = 3; else a++;").to("int a;if(x)a=3;else++a;");
  }

  @Test public void chainComparison() {
    final InfixExpression e = i("a == true == b == c");
    azzert.that("" + step.right(e), is("c"));
    trimming("a == true == b == c").to("a == b == c");
  }

  @Test public void chainCOmparisonTrueLast() {
    trimming("a == b == c == true").to("a == b == c");
  }

  @Test public void comaprisonWithBoolean1() {
    trimming("s.equals(532)==true").to("s.equals(532)");
  }

  @Test public void comaprisonWithBoolean2() {
    trimming("s.equals(532)==false ").to("!s.equals(532)");
  }

  @Test public void comaprisonWithBoolean3() {
    trimming("(false==s.equals(532))").to("(!s.equals(532))");
  }

  @Test public void comaprisonWithSpecific0() {
    trimming("this != a").to("a != this");
  }

  @Test public void comaprisonWithSpecific0Legibiliy00() {
    final InfixExpression e = i("this != a");
    assert in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS);
    assert !iz.booleanLiteral(step.right(e));
    assert !iz.booleanLiteral(step.left(e));
    assert in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS);
  }

  @Test public void comaprisonWithSpecific1() {
    trimming("null != a").to("a != null");
  }

  @Test public void comaprisonWithSpecific2() {
    trimming("null != a").to("a != null");
    trimming("this == a").to("a == this");
    trimming("null == a").to("a == null");
    trimming("this >= a").to("a <= this");
    trimming("null >= a").to("a <= null");
    trimming("this <= a").to("a >= this");
    trimming("null <= a").to("a >= null");
  }

  @Test public void comaprisonWithSpecific2a() {
    trimming("s.equals(532)==false").to("!s.equals(532)");
  }

  @Test public void comaprisonWithSpecific3() {
    trimming("(this==s.equals(532))").to("(s.equals(532)==this)");
  }

  @Test public void comaprisonWithSpecific4() {
    trimming("(0 < a)").to("(a>0)");
  }

  @Test public void comaprisonWithSpecificInParenthesis() {
    trimming("(null==a)").to("(a==null)");
  }

  @Test public void commonPrefixEntirelyIfBranches() {
    trimming("if (s.equals(532)) S.out.close();else S.out.close();").to("S.out.close(); ");
  }

  @Test public void commonPrefixIfBranchesInFor() {
    trimming("for (;;) if (a) {i++;j++;j++;} else { i++;j++; i++;}").to("for(;;){i++;j++;if(a)j++;else i++;}");
  }

  @Test public void commonSuffixIfBranches() {
    trimming("if (a) { \n" + //
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
    trimming("if (a) { \n" + //
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
    trimming("if (a) { \n" + //
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
    trimming("if (x)  if (a) { \n" + //
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
    trimming("a == true").to("a");
  }

  @Test public void compareWithBoolean01() {
    trimming("a == false").to("!a");
  }

  @Test public void compareWithBoolean10() {
    trimming("true == a").to("a");
  }

  @Test public void compareWithBoolean100() {
    trimming("a != true").to("!a");
  }

  @Test public void compareWithBoolean100a() {
    trimming("(((a))) != true").to("!a");
  }

  @Test public void compareWithBoolean101() {
    trimming("a != false").to("a");
  }

  @Test public void compareWithBoolean11() {
    trimming("false == a").to("!a");
  }

  @Test public void compareWithBoolean110() {
    trimming("true != a").to("!a");
  }

  @Test public void compareWithBoolean111() {
    trimming("false != a").to("a");
  }

  @Test public void compareWithBoolean2() {
    trimming("false != false").to("false");
  }

  @Test public void compareWithBoolean3() {
    trimming("false != true").to("true");
  }

  @Test public void compareWithBoolean4() {
    trimming("false == false").to("true");
  }

  @Test public void compareWithBoolean5() {
    trimming("false == true").to("false");
  }

  @Test public void compareWithBoolean6() {
    trimming("false != false").to("false");
  }

  @Test public void compareWithBoolean7() {
    trimming("true != true").to("false");
  }

  @Test public void compareWithBoolean8() {
    trimming("true != false").to("true");
  }

  @Test public void compareWithBoolean9() {
    trimming("true != true").to("false");
  }

  @Test public void comparison01() {
    trimming("1+2+3<3").to("6<3").to(null);
  }

  @Test public void comparison02() {
    trimming("f(2)<a").to(null);
  }

  @Test public void comparison03() {
    trimming("this==null").to(null);
  }

  @Test public void comparison04() {
    trimming("6-7<2+1").to("-1<3");
  }

  @Test public void comparison05() {
    trimming("a==11").to(null);
  }

  @Test public void comparison06() {
    trimming("1<102333").to(null);
  }

  @Test public void comparison08() {
    trimming("a==this").to(null);
  }

  @Test public void comparison09() {
    trimming("1+2<3&7+4>2+1").to("3<3&11>3");
  }

  @Test public void comparison11() {
    trimming("12==this").to("this==12");
  }

  @Test public void comparison12() {
    trimming("1+2<3&7+4>2+1||6-7<2+1").to("3<3&11>3||-1<3").to(null);
  }

  @Test public void comparison13() {
    trimming("13455643294<22").to(null);
  }

  @Test public void comparisonWithCharacterConstant() {
    trimming("'d' == s.charAt(i)").to("s.charAt(i)=='d'");
  }

  @Test public void compreaeExpressionToExpression() {
    trimming("6 - 7 < 2 + 1   ").to("-1<3");
  }

  @Test public void correctSubstitutionInIfAssignment() {
    trimming("int a = 2+3; if (a+b > a << b) a =(((((a *7 << a)))));")//
        .to("int a=2+3+b>2+3<<b?(2+3)*7<<2+3:2+3;");
  }

  @Test public void declarationAssignmentUpdateWithIncrement() {
    trimming("int a=0; a+=++a;").to(null);
  }

  @Test public void declarationAssignmentUpdateWithPostIncrement() {
    trimming("int a=0; a+=a++;").to(null);
  }

  @Test public void declarationAssignmentWithIncrement() {
    trimming("int a=0; a=++a;").to(null);
  }

  @Test public void declarationAssignmentWithPostIncrement() {
    trimming("int a=0; a=a++;").to(null);
  }

  @Test public void declarationIfAssignment() {
    trimming("" + //
        "    String res = s;\n" + //
        "    if (s.equals(y))\n" + //
        "      res = s + blah;\n" + //
        "    S.out.println(res);").to("" + //
            "    String res = s.equals(y) ? s + blah :s;\n" + //
            "    S.out.println(res);");
  }

  @Test public void declarationIfAssignment3() {
    trimming("int a =2; if (a != 2) a = 3;").to("int a = 2 != 2 ? 3 : 2;");
  }

  @Test public void declarationIfAssignment4() {
    trimming("int a =2; if (x) a = 2*a;").to("int a = x ? 2*2: 2;");
  }

  @Test public void declarationIfUpdateAssignment() {
    trimming("" + //
        "    String res = s;\n" + //
        "    if (s.equals(y))\n" + //
        "      res += s + blah;\n" + //
        "    S.out.println(res);").to("" + //
            "    String res = s.equals(y) ? s + s + blah :s;\n" + //
            "    S.out.println(res);");
  }

  @Test public void declarationIfUsesLaterVariable() {
    trimming("int a=0, b=0;if (b==3)   a=4;")//
        .to(" int a=0;if(0==3)a=4;") //
        .to(" int a=0==3?4:0;");
  }

  @Test public void declarationIfUsesLaterVariable1() {
    trimming("int a=0, b=0;if (b==3)   a=4; f();").to(null);
  }

  @Test public void declarationInitializeRightShift() {
    trimming("int a = 3;a>>=2;").to("int a = 3 >> 2;");
  }

  @Test public void declarationInitializerReturnAssignment() {
    trimming("int a = 3; return a = 2 * a;").to("return 2 * 3;");
  }

  @Test public void declarationInitializerReturnExpression() {
    trimming("" //
        + "String t = Bob + Wants + To + \"Sleep \"; "//
        + "  return (right_now + t);    ").to("return(right_now+Bob+Wants+To+\"Sleep \");");
  }

  @Test public void declarationInitializesRotate() {
    trimming("int a = 3;a>>>=2;").to("int a = 3 >>> 2;");
  }

  @Test public void declarationInitializeUpdateAnd() {
    trimming("int a = 3;a&=2;").to("int a = 3 & 2;");
  }

  @Test public void declarationInitializeUpdateAssignment() {
    trimming("int a = 3;a += 2;").to("int a = 3+2;");
  }

  @Test public void declarationInitializeUpdateAssignmentFunctionCallWithReuse() {
    trimming("int a = f();a += 2*f();").to("int a=f()+2*f();");
  }

  @Test public void declarationInitializeUpdateAssignmentFunctionCallWIthReuse() {
    trimming("int a = x;a += a + 2*f();").to("int a=x+x+2*f();");
  }

  @Test public void declarationInitializeUpdateAssignmentIncrement() {
    trimming("int a = ++i;a += j;").to("int a = ++i + j;");
  }

  @Test public void declarationInitializeUpdateAssignmentIncrementTwice() {
    trimming("int a = ++i;a += a + j;").to(null);
  }

  @Test public void declarationInitializeUpdateAssignmentWithReuse() {
    trimming("int a = 3;a += 2*a;").to("int a = 3+2*3;");
  }

  @Test public void declarationInitializeUpdateDividies() {
    trimming("int a = 3;a/=2;").to("int a = 3 / 2;");
  }

  @Test public void declarationInitializeUpdateLeftShift() {
    trimming("int a = 3;a<<=2;").to("int a = 3 << 2;");
  }

  @Test public void declarationInitializeUpdateMinus() {
    trimming("int a = 3;a-=2;").to("int a = 3 - 2;");
  }

  @Test public void declarationInitializeUpdateModulo() {
    trimming("int a = 3;a%= 2;").to("int a = 3 % 2;");
  }

  @Test public void declarationInitializeUpdatePlus() {
    trimming("int a = 3;a+=2;").to("int a = 3 + 2;");
  }

  @Test public void declarationInitializeUpdateTimes() {
    trimming("int a = 3;a*=2;").to("int a = 3 * 2;");
  }

  @Test public void declarationInitializeUpdateXor() {
    trimming("int a = 3;a^=2;").to("int a = 3 ^ 2;");
  }

  @Test public void declarationInitializeUpdatOr() {
    trimming("int a = 3;a|=2;").to("int a = 3 | 2;");
  }

  @Test public void declarationUpdateReturn() {
    trimming("int a = 3; return a += 2;").to("return 3 + 2;");
  }

  @Test public void declarationUpdateReturnNone() {
    trimming("int a = f(); return a += 2 * a;").to(null);
  }

  @Test public void declarationUpdateReturnTwice() {
    trimming("int a = 3; return a += 2 * a;").to("return 3 + 2 *3 ;");
  }

  @Test public void delcartionIfAssignmentNotPlain() {
    trimming("int a=0;   if (y) a+=3; ").to("int a = y ? 0 + 3 : 0;");
  }

  @Test public void doNotConsolidateNewArrayActual() {
    trimming("" + //
        "occupied = new boolean[capacity];\n" + //
        "placeholder = new boolean[capacity];").to(null);
  }

  @Test public void doNotConsolidateNewArraySimplifiedl() {
    trimming("" + //
        "a = new int[1];\n" + //
        "b = new int[1];").to(null);
  }

  @Test public void doNotConsolidatePlainNew() {
    trimming("" + //
        "a = new A();\n" + //
        "b = new B();").to(null);
  }

  @Test public void doNotInlineWithDeclaration() {
    trimming("  private Class<? extends T> retrieveClazz() throws ClassNotFoundException {\n" + //
        "    nonnull(className);\n" + //
        "    @SuppressWarnings(\"unchecked\") final Class<T> $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }").to(null);
  }

  @Test public void doNotIntroduceDoubleNegation() {
    trimming("!Y ? null :!Z ? null : F").to("Y&&Z?F:null");
  }

  @Test public void donotSorMixedTypes() {
    trimming("if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;") //
        .to("if (1130.94> a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;");
  }

  @Test public void dontELiminateCatchBlock() {
    trimming("try { f(); } catch (Exception e) { } finally {}").to(null);
  }

  @Test public void dontELiminateSwitch() {
    trimming("switch (a) { default: }").to(null);
  }

  @Test public void dontSimplifyCatchBlock() {
    trimming("try { {} ; {} } catch (Exception e) {{} ; {}  } finally {{} ; {}}")//
        .to(" try {}          catch (Exception e) {}          finally {}");
  }

  @Test public void duplicatePartialIfBranches() {
    trimming("" + //
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
    trimming("if (x) b = 3; else ;").to("if (x) b = 3;");
  }

  @Test public void emptyElseBlock() {
    trimming("if (x) b = 3; else { ;}").to("if (x) b = 3;");
  }

  @Test public void emptyIsNotChangedExpression() {
    trimming("").to(null);
  }

  @Test public void emptyIsNotChangedStatement() {
    trimming("").to(null);
  }

  @Test public void emptyThen1() {
    trimming("if (b) ; else x();").to("if (!b) x();");
  }

  @Test public void emptyThen2() {
    trimming("if (b) {;;} else {x() ;}").to("if (!b) x();");
  }

  @Test public void factorOutAnd() {
    trimming("(a || b) && (a || c)").to("a || b && c");
  }

  @Test public void factorOutOr() {
    trimming("a && b || a && c").to("a && (b || c)");
  }

  @Test public void factorOutOr3() {
    trimming("a && b && x  && f() || a && c && y ").to("a && (b && x && f() || c && y)");
  }

  @Test public void forLoopBug() {
    trimming("" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          res += 2;\n" + //
        "        else "//
        + "       if (s.charAt(i) == 'd')\n" + //
        "          res -= 1;\n" + //
        "      return res;\n" + //
        " if (b) i = 3;")//
            .to("" + //
                "      for (int i = 0;i < s.length();++i)\n" + //
                "       if (s.charAt(i) == 'a')\n" + //
                "          res += 2;\n" + //
                "        else "//
                + "       if (s.charAt(i) == 'd')\n" + //
                "          res--;\n" + //
                "      return res;\n" + //
                " if (b) i = 3;")//
            .to("" + //
                "      for (int i = 0;i < s.length();++i)\n" + //
                "       if (s.charAt(i) == 'a')\n" + //
                "          res += 2;\n" + //
                "        else "//
                + "       if (s.charAt(i) == 'd')\n" + //
                "          --res;\n" + //
                "      return res;\n" + //
                " if (b) i = 3;")//
            .to(null);
  }

  @Test public void IfBarFooElseBazFooExtractDefinedSuffix() {
    trimming("" //
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
    trimming("" //
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
    trimming("" + //
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
    trimming("" + //
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
    trimming("" + //
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
    trimming("if (a) if (b) {} else f(); x();")//
        .to(" if (a) if (!b) f(); x();");
  }

  @Test public void ifEmptyElsewWithinIf() {
    trimming("if (a) if (b) {;;;f();} else {}")//
        .to("if(a&&b){;;;f();}");
  }

  @Test public void ifEmptyThenThrow() {
    trimming("" //
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
    trimming("" //
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
    trimming("" //
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
    trimming("if (x) f(a); else f(b);").to("f(x ? a: b);");
  }

  @Test public void ifPlusPlusPost() {
    trimming("if (x) a++; else b++;").to("if(x)++a;else++b;");
  }

  @Test public void ifPlusPlusPostExpression() {
    trimming("x? a++:b++").to(null);
  }

  @Test public void ifPlusPlusPre() {
    trimming("if (x) ++a; else ++b;").to(null);
  }

  @Test public void ifPlusPlusPreExpression() {
    trimming("x? ++a:++b").to(null);
  }

  @Test public void ifSequencerNoElseSequencer0() {
    trimming("if (a) return; break;").to(null);
  }

  @Test public void ifSequencerNoElseSequencer01() {
    trimming("if (a) throw e; break;").to(null);
  }

  @Test public void ifSequencerNoElseSequencer02() {
    trimming("if (a) break; break;").to("break;");
  }

  @Test public void ifSequencerNoElseSequencer03() {
    trimming("if (a) continue; break;").to(null);
  }

  @Test public void ifSequencerNoElseSequencer04() {
    trimming("if (a) break; return;").to("if (!a) return; break;");
  }

  @Test public void ifSequencerNoElseSequencer05() {
    trimming("if (a) {x(); return;} continue;").to(null);
  }

  @Test public void ifSequencerNoElseSequencer06() {
    trimming("if (a) throw e; break;").to(null);
  }

  @Test public void ifSequencerNoElseSequencer07() {
    trimming("if (a) break; throw e;").to("if (!a) throw e; break;");
  }

  @Test public void ifSequencerNoElseSequencer08() {
    trimming("if (a) throw e; continue;").to(null);
  }

  @Test public void ifSequencerNoElseSequencer09() {
    trimming("if (a) break; throw e;").to("if (!a) throw e; break;");
  }

  @Test public void ifSequencerNoElseSequencer10() {
    trimming("if (a) continue; return;").to("if (!a) return; continue;");
  }

  @Test public void ifSequencerThenSequencer0() {
    trimming("if (a) return 4; else break;").to("if (a) return 4; break;");
  }

  @Test public void ifSequencerThenSequencer1() {
    trimming("if (a) break; else return 2;").to("if (!a) return 2; break;");
  }

  @Test public void ifSequencerThenSequencer3() {
    trimming("if (a) return 10; else continue;").to("if (a) return 10; continue;");
  }

  @Test public void ifSequencerThenSequencer4() {
    trimming("if (a) continue; else return 2;").to("if (!a) return 2; continue;");
  }

  @Test public void ifSequencerThenSequencer5() {
    trimming("if (a) throw e; else break;").to("if (a) throw e; break;");
  }

  @Test public void ifSequencerThenSequencer6() {
    trimming("if (a) break; else throw e;").to("if (!a) throw e; break;");
  }

  @Test public void ifSequencerThenSequencer7() {
    trimming("if (a) throw e; else continue;").to("if (a) throw e; continue;");
  }

  @Test public void ifSequencerThenSequencer8() {
    trimming("if (a) break; else throw e;").to("if (!a) throw e; break;");
  }

  @Test public void ifThrowNoElseThrow() {
    trimming("" //
        + "if (!(e.getCause() instanceof Error))\n" //
        + "  throw e;\n" //
        + "throw (Error) e.getCause();")//
            .to(" throw !(e.getCause()instanceof Error)?e:(Error)e.getCause();");//
  }

  @Test public void ifWithCommonNotInBlock() {
    trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++; g();}").to("for(;;){i++;j++;if(a)f();else g();}");
  }

  @Test public void ifWithCommonNotInBlockDegenerate() {
    trimming("for (;;) if (a) {i++; f();} else { i++;j++; }").to("for(;;){i++; if(a)f(); else j++;}");
  }

  @Test public void ifWithCommonNotInBlockiLongerElse() {
    trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++;  f(); h();}").to("for(;;){i++;j++; f(); if(!a) h();}");
  }

  @Test public void ifWithCommonNotInBlockiLongerThen() {
    trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++; }").to("for(;;){i++;j++; if(a)f();}");
  }

  @Test public void ifWithCommonNotInBlockNothingLeft() {
    trimming("for (;;) if (a) {i++;j++;} else { i++;j++; }").to("for(;;){i++;j++;}");
  }

  @Test public void infiniteLoopBug1() {
    trimming("static boolean hasAnnotation(final VariableDeclarationFragment f) {\n" + //
        "      return hasAnnotation((VariableDeclarationStatement) f.getParent());\n" + //
        "    }").to(null);
  }

  @Test public void infiniteLoopBug2() {
    trimming(" static boolean hasAnnotation(final VariableDeclarationStatement n) {\n" + //
        "      return hasAnnotation(n.modifiers());\n" + //
        "    }").to(" static boolean hasAnnotation(final VariableDeclarationStatement s) {\n" + //
            "      return hasAnnotation(s.modifiers());\n" + //
            "    }");
  }

  @Test public void infiniteLoopBug3() {
    trimming("  boolean f(final VariableDeclarationStatement n) {\n" + //
        "      return false;\n" + //
        "    }").to("  boolean f(final VariableDeclarationStatement s) {\n" + //
            "      return false;\n" + //
            "    }");
  }

  @Test public void infiniteLoopBug4() {
    trimming("void f(final VariableDeclarationStatement n) {}")//
        .to(" void f(final VariableDeclarationStatement s) { }");
  }

  @Test public void inline01() {
    trimming("" + //
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
    trimming("" //
        + "public void multiDimensionalIntArraysAreEqual() {\n" //
        + "  int[][] int1 = {{1, 2, 3}, {4, 5, 6}};\n" //
        + "  int[][] int2 = {{1, 2, 3}, {4, 5, 6}};\n" //
        + "  assertArrayEquals(int1, int2);\n" //
        + "}").to(null);
  }

  @Test public void inlineArrayInitialization2() {
    trimming("" //
        + "public double[] solve() {\n" //
        + "  final SimpleRegression regress = new SimpleRegression(true);\n" //
        + "  for (double[] d : points)\n" //
        + "    regress.addData(d[0], d[1]);\n" //
        + "  final double[] $ = { regress.getSlope(), regress.getIntercept() };\n" //
        + "  return $;\n" //
        + "}").to(null);
  }

  @Test public void inlineInitializers() {
    trimming("int b,a = 2; return 3 * a * b; ").to("return 3*2*b;");
  }

  @Test public void inlineInitializersFirstStep() {
    trimming("int b=4,a = 2; return 3 * a * b; ").to("int a = 2; return 3*a*4;");
  }

  @Test public void inlineInitializersSecondStep() {
    trimming("int a = 2; return 3*a*4;").to("return 3 * 2 * 4;");
  }

  /** START OF STABLING TESTS */
  @Test public void inlineintoInstanceCreation() {
    trimming("" //
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
        + "}").to(null);
  }

  @Test public void inlineintoNextStatementWithSideEffects() {
    trimming("int a = f(); if (a) g(a); else h(u(a));").to(null);
  }

  @Test public void inlineSingleUse07() {
    trimming(
        "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     S.out.println(coes.size()); ")
            .to(null);
  }

  @Test public void inlineSingleUseKillingVariable() {
    trimming("int a,b=2; a = b;").to("int a;a=2;");
  }

  @Test public void inlineSingleUseKillingVariables() {
    trimming("int $, xi=0, xj=0, yi=0, yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")
        .to(" int $, xj=0, yi=0, yj=0;        if (0>xj==yi>yj)$++;else $--;");
  }

  @Test public void inlineSingleUseKillingVariablesSimplified() {
    trimming("int $=1,xi=0,xj=0,yi=0,yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")//
        .to(" int $=1,xj=0,yi=0,yj=0;       if(0>xj==yi>yj)$++;else $--;")//
        .to(" int $=1,yi=0,yj=0;            if(0>0==yi>yj)$++;else $--;") //
        .to(" int $=1,yj=0;                 if(0>0==0>yj)$++;else $--;") //
        .to(" int $=1;                      if(0>0==0>0)$++;else $--;") //
        .to(" int $=1;                      if(0>0==0>0)++$;else--$;") //
    ;
  }

  @Test public void inlineSingleUseTrivial() {
    trimming(" int $=1,yj=0;                 if(0>0==yj<0)++$;else--$;") //
        .to("  int $=1;                      if(0>0==0<0)++$;else--$;") //
    ;
  }

  @Test public void inlineSingleUseVanilla() {
    trimming("int a = f(); if (a) f();").to("if (f()) f();");
  }

  @Test public void inlineSingleUseWithAssignment() {
    trimming("int a = 2; while (true) if (f()) f(a); else a = 2;")//
        .to(null);
  }

  @Test public void inlineSingleVariableintoPlusPlus() {
    trimming("int $ = 0;  if (a)  ++$;  else --$;").to(null);
  }

  @Test public void inliningWithVariableAssignedTo() {
    trimming("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
    ;
  }

  @Test public void isGreaterTrue() {
    final InfixExpression e = i("f(a,b,c,d,e) * f(a,b,c)");
    azzert.that("" + step.right(e), is("f(a,b,c)"));
    azzert.that("" + step.left(e), is("f(a,b,c,d,e)"));
    final Wring<InfixExpression> s = Toolbox.instance.find(e);
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    assert s != null;
    assert s.scopeIncludes(e);
    final Expression e1 = step.left(e);
    final Expression e2 = step.right(e);
    assert !hasNull(e1, e2);
    final boolean tokenWiseGreater = nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD;
    assert tokenWiseGreater;
    assert ExpressionComparator.moreArguments(e1, e2);
    assert ExpressionComparator.longerFirst(e);
    assert s.eligible(e) : "e=" + e + " s=" + s;
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that("" + replacement, is("f(a,b,c) * f(a,b,c,d,e)"));
  }

  @Test public void isGreaterTrueButAlmostNot() {
    final InfixExpression e = i("f(a,b,c,d) * f(a,b,c)");
    azzert.that("" + step.right(e), is("f(a,b,c)"));
    azzert.that("" + step.left(e), is("f(a,b,c,d)"));
    final Wring<InfixExpression> s = Toolbox.instance.find(e);
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    assert s != null;
    assert s.scopeIncludes(e);
    final Expression e1 = step.left(e);
    final Expression e2 = step.right(e);
    assert !hasNull(e1, e2);
    final boolean tokenWiseGreater = nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD;
    assert !tokenWiseGreater;
    assert ExpressionComparator.moreArguments(e1, e2);
    assert ExpressionComparator.longerFirst(e);
    assert s.eligible(e) : "e=" + e + " s=" + s;
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that("" + replacement, is("f(a,b,c) * f(a,b,c,d)"));
  }

  @Test public void issue06() {
    trimming("a*-b").to("-a * b");
  }

  @Test public void issue06B() {
    trimming("x/a*-b/-c*- - - d / -d")//
        .to("x/a * b/ c * d/d")//
        .to("d*x/a*b/c/d");
  }

  @Test public void issue06C1() {
    trimming("a*-b/-c*- - - d / d").to("-a * b/ c * d/d");
  }

  @Test public void issue06C4() {
    trimming("-a * b/ c ").to(null);
  }

  @Test public void issue06D() {
    trimming("a*b*c*d*-e").to("-a*b*c*d*e").to(null);
  }

  @Test public void issue06E() {
    trimming("-a*b*c*d*f*g*h*i*j*k").to(null);
  }

  @Test public void issue06F() {
    trimming("x*a*-b*-c*- - - d * d")//
        .to("-x*a*b*c*d*d")//
        .to(null);
  }

  @Test public void issue06G() {
    trimming("x*a*-b*-c*- - - d / d")//
        .to("-x*a*b*c*d/d")//
        .to(null);
  }

  @Test public void issue06H() {
    trimming("x/a*-b/-c*- - - d ")//
        .to("-x/a * b/ c * d")//
    ;
  }

  @Test public void issue06I() {
    trimming("41 * - 19")//
        .to("-779 ") //
    ;
  }

  @Test public void issue06J() {
    trimming("41 * a * - 19")//
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
        .to("\"Use \"+(receiver==null ? \"x\" : \"\"+receiver)").to("\"Use \"+(receiver==null ? \"x\" : receiver+\"\")").to(null);
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

  @Test public void issue110_14() {
    trimming("x == null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\"")//
        .to("\"Use \" + (x==null ? \"isEmpty()\" : \"\"+ x +  \".isEmpty()\")")//
        .to("\"Use \" + ((x==null ? \"\" : \"\"+ x +  \".\")+\"isEmpty()\")");
  }

  @Test public void issue37Simplified() {
    trimming("" + //
        "    int a = 3;\n" + //
        "    a = 31 * a;" + //
        "").to("int a = 31 * 3; ");
  }

  @Test public void issue37SimplifiedVariant() {
    trimming("" + //
        "    int a = 3;\n" + //
        "    a += 31 * a;").to("int a=3+31*3;");
  }

  @Test public void issue37WithSimplifiedBlock() {
    trimming("if (a) { {} ; if (b) f(); {} } else { g(); f(); ++i; ++j; }")//
        .to(" if (a) {  if (b) f(); } else { g(); f(); ++i; ++j; }");
  }

  @Test public void issue38() {
    trimming("    return o == null ? null\n" + //
        "        : o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
        "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
        "                : null;").to(null);
  }

  @Test public void issue38Simplfiied() {
    trimming(//
        "         o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
            "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
            "                : null").to(null);
  }

  @Test public void issue39base() {
    trimming("" + //
        "if (name == null) {\n" + //
        "    if (other.name != null)\n" + //
        "        return false;\n" + //
        "} else if (!name.equals(other.name))\n" + //
        "    return false;\n" + //
        "return true;").to(null); //
  }

  public void issue39baseDual() {
    trimming("if (name != null) {\n" + //
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
    trimming("" + //
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
    trimming("" + //
        "if (!varArgs) {\n" + //
        "    if (parameterTypes.length != argumentTypes.length) {\n" + //
        "        return false;\n" + //
        "    }\n" + //
        "} else if (argumentTypes.length < parameterTypes.length - 1) {\n" + //
        "    return false;\n" + //
        "}" + //
        "").to(null);
  }

  @Test public void issue41FunctionCall() {
    trimming("int a = f();a += 2;").to("int a = f()+2;");
  }

  @Test public void issue43() {
    trimming("" //
        + "String t = Z2;  "//
        + " t = t.f(A).f(b) + t.f(c);   "//
        + "return (t + 3);    ")
            .to(""//
                + "String t = Z2.f(A).f(b) + Z2.f(c);" //
                + "return (t + 3);" //
                + "");
  }

  @Test public void issue46() {
    trimming("" + //
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
    trimming("int f() { int f = 0; for (int i: X) $ += f(i); return f;}")//
        .to("int f(){int $=0;for(int i:X)$+=f(i);return $;}");
  }

  @Test public void issue51g() {
    trimming("abstract abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue52a() {
    trimming("abstract abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue52A() {
    trimming( //
        "void m() { return; }").to( //
            "void m() {}");
  }

  @Test public void issue52A1() {
    trimming( //
        "void m() { return a; }").to( //
            "");
  }

  @Test public void issue52b() {
    trimming("abstract interface a"//
        + "{}").to("interface a {}");
  }

  @Test public void issue52B1() {
    trimming( //
        "void m() { if (a) { f(); return; }}").to( //
            "void m() { if (a) { f(); ; }}");
  }

  @Test public void issue52B2() {
    trimming( //
        "void m() { if (a) ++i; else { f(); return; }}").to( //
            "void m() { if (a) ++i; else { f(); ; }}");
  }

  @Test public void issue52c() {
    trimming("interface a"//
        + "{}").to(null);
  }

  @Test public void issue52d() {
    trimming(//
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
    trimming(//
        "public interface A {\n"//
            + "static void remove()\n; "//
            + "public static int i = 3\n; "//
            + "}").to( //
                "public interface A {\n"//
                    + "static void remove()\n; "//
                    + "static int i = 3\n; "//
                    + "}");
  }

  @Test public void issue52f() {
    trimming(//
        "public interface A {\n"//
            + "static void remove()\n; "//
            + "public static int i\n; "//
            + "}").to( //
                "public interface A {\n"//
                    + "static void remove()\n; "//
                    + "static int i\n; "//
                    + "}");
  }

  @Test public void issue52g() {
    trimming("final class ClassTest {\n"//
        + "final void remove();\n"//
        + "}")
            .to("final class ClassTest {\n"//
                + "void remove();\n "//
                + "}");
  }

  @Test public void issue52h() {
    trimming("final class ClassTest {\n"//
        + "public final void remove();\n"//
        + "}"//
    ).to(//
        "final class ClassTest {\n"//
            + "public void remove();\n "//
            + "}");
  }

  @Test public void issue52i() {
    trimming("public class ClassTest {\n"//
        + "static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "SUNDAY, MONDAY\n"//
        + "}");
  }

  @Test public void issue52j() {
    trimming("public class ClassTest {\n"//
        + "private static enum Day {\n"//
        + "SUNDAY, MONDAY\n"//
        + "}");
  }

  @Test public void issue52k() {
    trimming("public class ClassTest {\n"//
        + "public  ClassTest(){}\n"//
        + "}").to(null);
  }

  @Test public void issue52l() {
    trimming("abstract class A { final void f() { }}").to(null);
  }

  @Test public void issue52n() {
    trimming(//
        "abstract class A {\n"//
            + "static void f() {}\n "//
            + "public final static int i = 3; "//
            + "}")
                .to(//
                    "abstract class A {\n"//
                        + "static void f() {}\n "//
                        + "public static final int i = 3; "//
                        + "}")
                .to(null);
  }

  @Test public void issue52o() {
    trimming(//
        "final class A {\n"//
            + "static void f() {}\n "//
            + "public final static int i = 3; "//
            + "}")//
                .to(//
                    "final class A {\n"//
                        + "static void f() {}\n "//
                        + "public static final int i = 3; "//
                        + "}")//
                .to(null);
  }

  @Test public void issue52p() {
    trimming(//
        "enum A {a1, a2; static enum B {b1, b2; static class C { static enum D {c1, c2}}}")//
            .to("enum A {a1, a2; enum B {b1, b2; static class C { static enum D {c1, c2}}}")//
            .to("enum A {a1, a2; enum B {b1, b2; static class C { enum D {c1, c2}}}");
  }

  @Test public void issue53() {
    trimming( //
        "int[] is = f(); for (int i: is) f(i);")//
            .to( //
                "for (int i: f()) f(i);");
  }

  @Test public void issue53a() {
    trimming( //
        "int f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
            .to( //
                "int f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void issue54DoNonSideEffect() {
    trimming( //
        "int a  = f; do { b[i] = a; } while (b[i] != a);")//
            .to( //
                "do { b[i] = f; } while (b[i] != f);");
  }

  @Test public void issue54DoNonSideEffectEmptyBody() {
    trimming( //
        "int a = f(); do ; while (a != 1);")//
            .to( //
                "");
  }

  @Test public void issue54DoWhile() {
    trimming( //
        "int a  = f(); do { b[i] = 2; ++i; } while (b[i] != a);")//
            .to( //
                "");
  }

  @Test public void issue54DoWithBlock() {
    trimming( //
        "int a  = f(); do { b[i] = a;  ++i; } while (b[i] != a);")//
            .to(null);
  }

  @Test public void issue54doWithoutBlock() {
    trimming("int a  = f(); do b[i] = a; while (b[i] != a);")//
        .to(null);
  }

  @Test public void issue54ForEnhanced() {
    trimming("int a  = f(); for (int i: a) b[i] = x;")//
        .to(" for (int i: f()) b[i] = x;");
  }

  @Test public void issue54ForEnhancedNonSideEffectLoopHeader() {
    trimming("int a  = f; for (int i: a) b[i] = b[i-1];")//
        .to("for (int i: f) b[i] = b[i-1];");
  }

  @Test public void issue54ForEnhancedNonSideEffectWithBody() {
    trimming("int a  = f; for (int i: j) b[i] = a;")//
        .to(" for(int i:j)b[i]=f; ");
  }

  @Test public void issue54ForPlain() {
    trimming("int a  = f(); for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .to("for (int i = 0; i < 100;  ++i) b[i] = f();")//
        .to(null);
  }

  @Test public void issue54ForPlainNonSideEffect() {
    trimming("int a  = f; for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .to("for (int i = 0; i < 100;  ++i) b[i] = f;");
  }

  @Test public void issue54ForPlainUseInConditionNonSideEffect() {
    trimming("int a  = f; for (int i = 0; a < 100;  ++i) b[i] = 3;")//
        .to("for (int i = 0; f < 100;  ++i) b[i] = 3;");
  }

  @Test public void issue54ForPlainUseInInitializerNonSideEffect() {
    trimming("int a  = f; for (int i = a; i < 100; i *= a) b[i] = 3;")//
        .to(" for (int i = f; i < 100; i *= f) b[i] = 3;");
  }

  @Test public void issue54ForPlainUseInUpdatersNonSideEffect() {
    trimming("int a  = f; for (int i = 0; i < 100; i *= a) b[i] = 3;")//
        .to("for (int i = 0; i < 100; i *= f) b[i] = 3;");
  }

  @Test public void issue54While() {
    trimming("int a  = f(); while (c) b[i] = a;")//
        .to(null);
  }

  @Test public void issue54WhileNonSideEffect() {
    trimming("int a  = f; while (c) b[i] = a;")//
        .to("while (c) b[i] = f;");
  }

  @Test public void issue54WhileScopeDoesNotInclude() {
    included("int a  = f(); while (c) b[i] = a;", VariableDeclarationFragment.class)//
        .notIn(new DeclarationInitializerStatementTerminatingScope());
  }

  @Test public void issue57a() {
    trimming("void m(List<Expression>... expressions) { }").to("void m(List<Expression>... xss) {}");
  }

  @Test public void issue57b() {
    trimming("void m(Expression... expression) { }").to("void m(Expression... xs) {}");
  }

  @Test public void issue58a() {
    trimming("X f(List<List<Expression>> expressions){}").to("X f(List<List<Expression>> xss){}");
  }

  @Test public void issue58b() {
    trimming("X f(List<Expression>[] expressions){}").to("X f(List<Expression>[] xss){}");
  }

  @Test public void issue58c() {
    trimming("X f(List<Expression>[] expressions){}").to("X f(List<Expression>[] xss){}");
  }

  @Test public void issue58d() {
    trimming("X f(List<Expression>... expressions){}").to("X f(List<Expression>... xss){}");
  }

  @Test public void issue58e() {
    trimming("X f(Expression[]... expressions){}").to("X f(Expression[]... xss){}");
  }

  @Test public void issue58f() {
    trimming("X f(Expression[][]... expressions){}").to("X f(Expression[][]... xsss){}");
  }

  @Test public void issue58g() {
    trimming("X f(List<Expression[][]>... expressions){}").to("X f(List<Expression[][]>... xssss){}");
  }

  @Test public void issue62a() {
    trimming("int f(int i) { for(;;++i) if(false) break; return i; }")//
        .to("int f(int i) { for(;;++i){} return i; }")//
        .to(null);
  }

  @Test public void issue62b_1() {
    trimming("int f(int i) { for(;i<100;i=i+1) if(false) break; return i; }")//
        .to("int f(int i) { for(;i<100;i+=1){} return i; }")//
        .to("int f(int i) { for(;i<100;i++){} return i; }")//
        .to("int f(int i) { for(;i<100;++i){} return i; }").to(null);//
  }

  @Test public void issue62c() {
    trimming("int f(int i) { while(++i > 999) if(i>99) break; return i;}").to(null);
  }

  @Test public void issue64a() {
    trimming("void f() {" + //
        "    final int a = f();\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").to(null);
  }

  @Test public void issue64b() {
    trimming("void f() {" + //
        "    final int a = 3;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").to(null);
  }

  @Test public void issue73a() {
    trimming("void foo(StringBuilder sb) {}").to("void foo(StringBuilder b) {}");
  }

  @Test public void issue73b() {
    trimming("void foo(DataOutput dataOutput) {}").to("void foo(DataOutput o) {}");
  }

  @Test public void issue73c() {
    trimming("void foo(Integer integer, ASTNode astn) {}").to("void foo(Integer i, ASTNode astn) {}");
  }

  @Test public void issue74d() {
    trimming("int[] a = new int[] {2,3};").to(null);
  }

  @Test public void linearTransformation() {
    trimming("plain * the + kludge").to("the*plain+kludge");
  }

  @Test public void literalVsLiteral() {
    trimming("1 < 102333").to(null);
  }

  @Test public void longChainComparison() {
    trimming("a == b == c == d").to(null);
  }

  @Test public void longChainParenthesisComparison() {
    trimming("(a == b == c) == d").to(null);
  }

  @Test public void longChainParenthesisNotComparison() {
    trimming("(a == b == c) != d").to(null);
  }

  @Test public void longerChainParenthesisComparison() {
    trimming("(a == b == c == d == e) == d").to(null);
  }

  @Test public void massiveInlining() {
    trimming("int a,b,c;String t = zE4;if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;")//
        .to("int a,b,c;if(2 * 3.1415 * 180>a||zE4.concat(sS)==1922&&zE4.length()>3)return c>5;");
  }

  @Test public void methodWithLastIf() {
    trimming("int f() { if (a) { f(); g(); h();}}").to("int f() { if (!a) return;  f(); g(); h();}");
  }

  @Test public void nestedIf1() {
    trimming("if (a) if (b) i++;").to("if (a && b) i++;");
  }

  @Test public void nestedIf2() {
    trimming("if (a) if (b) i++; else ; else ; ").to("if (a && b) i++; else ;");
  }

  @Test public void nestedIf3() {
    trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else;else{++y;f();g();z();}");
  }

  @Test public void nestedIf33() {
    trimming("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .to(" if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}")//
        .to(" if(x){if(a&&b)++i;}else{++y;f();g();}")//
    ;
  }

  @Test public void nestedIf33a() {
    trimming("if (x) { if (a && b) i++; } else { y++; f(); g(); }")//
        .to(" if (x) {if(a&&b)++i;} else{++y;f();g();}");
  }

  @Test public void nestedIf33b() {
    trimming("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .to("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3c() {
    trimming("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .to(" if(x) {if(a&&b)i++;} else {++y;f();g();}");
  }

  @Test public void nestedIf3d() {
    trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else; else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)i++;} else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)++i;} else{++y;f();g();z();}") //
    ;
  }

  @Test public void nestedIf3e() {
    trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to(" if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }

  @Test public void nestedIf3f() {
    trimming("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .to(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3f1() {
    trimming(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3x() {
    trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)i++;}else{++y;f();g();z();}") //
    ;
  }

  @Test public void nestedTernaryAlignment() {
    trimming("int b=3==4?5==3?2:3:5==3?2:3*3;").to("int b=3==4?5==3?2:3:5!=3?3*3:2;");
  }

  @Test public void noChange() {
    trimming("12").to(null);
    trimming("true").to(null);
    trimming("null").to(null);
    trimming("on*of*no*notion*notion").to("no*of*on*notion*notion");
  }

  @Test public void noChange0() {
    trimming("kludge + the * plain ").to(null);
  }

  @Test public void noChange1() {
    trimming("the * plain").to(null);
  }

  @Test public void noChange2() {
    trimming("plain + kludge").to(null);
  }

  @Test public void noChangeA() {
    trimming("true").to(null);
  }

  @Test public void noinliningintoSynchronizedStatement() {
    trimming("int a  = f(); synchronized(this) { int b = a; }")//
        .to(null);
  }

  @Test public void noinliningintoSynchronizedStatementEvenWithoutSideEffect() {
    trimming("int a  = f; synchronized(this) { int b = a; }")//
        .to(null);
  }

  @Test public void noinliningintoTryStatement() {
    trimming("int a  = f(); try { int b = a; } catch (Exception E) {}")//
        .to(null);
  }

  @Test public void noinliningintoTryStatementEvenWithoutSideEffect() {
    trimming("int a  = f; try { int b = a; } catch (Exception E) {}")//
        .to(null);
  }

  @Test public void notOfAnd() {
    trimming("!(A && B)").to("!A || !B");
  }

  @Test public void oneMultiplication() {
    trimming("f(a,b,c,d) * f(a,b,c)").to("f(a,b,c) * f(a,b,c,d)");
  }

  @Test public void oneMultiplicationAlternate() {
    trimming("f(a,b,c,d,e) * f(a,b,c)").to("f(a,b,c) * f(a,b,c,d,e)");
  }

  @Test public void orFalse3ORTRUE() {
    trimming("false || false || false").to("false");
  }

  @Test public void orFalse4ORTRUE() {
    trimming("false || false || false || false").to("false");
  }

  @Test public void orFalseANDOf3WithoutBoolean() {
    trimming("a && b && false").to(null);
  }

  @Test public void orFalseANDOf3WithoutBooleanA() {
    trimming("x && a && b").to(null);
  }

  @Test public void orFalseANDOf3WithTrue() {
    trimming("true && x && true && a && b").to("x && a && b");
  }

  @Test public void orFalseANDOf3WithTrueA() {
    trimming("a && b && true").to("a && b");
  }

  @Test public void orFalseANDOf4WithoutBoolean() {
    trimming("a && b && c && false").to(null);
  }

  @Test public void orFalseANDOf4WithoutBooleanA() {
    trimming("x && a && b && c").to(null);
  }

  @Test public void orFalseANDOf4WithTrue() {
    trimming("x && true && a && b && c").to("x && a && b && c");
  }

  @Test public void orFalseANDOf4WithTrueA() {
    trimming("a && b && c && true").to("a && b && c");
  }

  @Test public void orFalseANDOf5WithoutBoolean() {
    trimming("false && a && b && c && d").to(null);
  }

  @Test public void orFalseANDOf5WithoutBooleanA() {
    trimming("x && a && b && c && d").to(null);
  }

  @Test public void orFalseANDOf5WithTrue() {
    trimming("x && a && b && c && true && true && true && d").to("x && a && b && c && d");
  }

  @Test public void orFalseANDOf5WithTrueA() {
    trimming("true && a && b && c && d").to("a && b && c && d");
  }

  @Test public void orFalseANDOf6WithoutBoolean() {
    trimming("a && b && c && false && d && e").to(null);
  }

  @Test public void orFalseANDOf6WithoutBooleanA() {
    trimming("x && a && b && c && d && e").to(null);
  }

  @Test public void orFalseANDOf6WithoutBooleanWithParenthesis() {
    trimming("(x && (a && b)) && (c && (d && e))").to(null);
  }

  @Test public void orFalseANDOf6WithTrue() {
    trimming("x && a && true && b && c && d && e").to("x && a && b && c && d && e");
  }

  @Test public void orFalseANDOf6WithTrueA() {
    trimming("a && b && c && true && d && e").to("a && b && c && d && e");
  }

  @Test public void orFalseANDOf6WithTrueWithParenthesis() {
    trimming("x && (true && (a && b && true)) && (c && (d && e))").to("x && a && b && c && d && e");
  }

  @Test public void orFalseANDOf7WithMultipleTrueValue() {
    trimming("(a && (b && true)) && (c && (d && (e && (true && true))))").to("a &&b &&c &&d &&e ");
  }

  @Test public void orFalseANDOf7WithoutBooleanAndMultipleFalseValue() {
    trimming("(a && (b && false)) && (c && (d && (e && (false && false))))").to(null);
  }

  @Test public void orFalseANDOf7WithoutBooleanWithParenthesis() {
    trimming("(a && b) && (c && (d && (e && false)))").to(null);
  }

  @Test public void orFalseANDOf7WithTrueWithParenthesis() {
    trimming("true && (a && b) && (c && (d && (e && true)))").to("a &&b &&c &&d &&e ");
  }

  @Test public void orFalseANDWithFalse() {
    trimming("b && a").to(null);
  }

  @Test public void orFalseANDWithoutBoolean() {
    trimming("b && a").to(null);
  }

  @Test public void orFalseANDWithTrue() {
    trimming("true && b && a").to("b && a");
  }

  @Test public void orFalseFalseOrFalse() {
    trimming("false ||false").to("false");
  }

  @Test public void orFalseORFalseWithSomething() {
    trimming("true || a").to(null);
  }

  @Test public void orFalseORFalseWithSomethingB() {
    trimming("false || a || false").to("a");
  }

  @Test public void orFalseOROf3WithFalse() {
    trimming("x || false || b").to("x || b");
  }

  @Test public void orFalseOROf3WithFalseB() {
    trimming("false || a || b || false").to("a || b");
  }

  @Test public void orFalseOROf3WithoutBoolean() {
    trimming("a || b").to(null);
  }

  @Test public void orFalseOROf3WithoutBooleanA() {
    trimming("x || a || b").to(null);
  }

  @Test public void orFalseOROf4WithFalse() {
    trimming("x || a || b || c || false").to("x || a || b || c");
  }

  @Test public void orFalseOROf4WithFalseB() {
    trimming("a || b || false || c").to("a || b || c");
  }

  @Test public void orFalseOROf4WithoutBoolean() {
    trimming("a || b || c").to(null);
  }

  @Test public void orFalseOROf4WithoutBooleanA() {
    trimming("x || a || b || c").to(null);
  }

  @Test public void orFalseOROf5WithFalse() {
    trimming("x || a || false || c || d").to("x || a || c || d");
  }

  @Test public void orFalseOROf5WithFalseB() {
    trimming("a || b || c || d || false").to("a || b || c || d");
  }

  @Test public void orFalseOROf5WithoutBoolean() {
    trimming("a || b || c || d").to(null);
  }

  @Test public void orFalseOROf5WithoutBooleanA() {
    trimming("x || a || b || c || d").to(null);
  }

  @Test public void orFalseOROf6WithFalse() {
    trimming("false || x || a || b || c || d || e").to("x || a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithFalseWithParenthesis() {
    trimming("x || (a || (false) || b) || (c || (d || e))").to("x || a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithFalseWithParenthesisB() {
    trimming("(a || b) || false || (c || false || (d || e || false))").to("a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithoutBoolean() {
    trimming("a || b || c || d || e").to(null);
  }

  @Test public void orFalseOROf6WithoutBooleanA() {
    trimming("x || a || b || c || d || e").to(null);
  }

  @Test public void orFalseOROf6WithoutBooleanWithParenthesis() {
    trimming("(a || b) || (c || (d || e))").to(null);
  }

  @Test public void orFalseOROf6WithoutBooleanWithParenthesisA() {
    trimming("x || (a || b) || (c || (d || e))").to(null);
  }

  @Test public void orFalseOROf6WithTwoFalse() {
    trimming("a || false || b || false || c || d || e").to("a || b || c || d || e");
  }

  @Test public void orFalseORSomethingWithFalse() {
    trimming("false || a || false").to("a");
  }

  @Test public void orFalseORSomethingWithTrue() {
    trimming("a || true").to(null);
  }

  @Test public void orFalseORWithoutBoolean() {
    trimming("b || a").to(null);
  }

  @Test public void orFalseProductIsNotANDDivOR() {
    trimming("2*a").to(null);
  }

  @Test public void orFalseTrueAndTrueA() {
    trimming("true && true").to("true");
  }

  @Test public void overridenDeclaration() {
    trimming("int a = 3; a = f() ? 3 : 4;").to("int a = f() ? 3: 4;");
  }

  @Test public void paramAbbreviateBasic1() {
    trimming("void m(XMLDocument xmlDocument) {" + //
        "xmlDocument.exec(p);}").to("void m(XMLDocument d) {" + //
            "d.exec(p);}");
  }

  @Test public void paramAbbreviateBasic2() {
    trimming("int m(StringBuilder builder) {" + //
        "if(builder.exec())" + //
        "builder.clear();").to("int m(StringBuilder b) {" + //
            "if(b.exec())" + //
            "b.clear();");
  }

  @Test public void paramAbbreviateCollision() {
    trimming("void m(Expression exp, Expression expresssion) { }").to("void m(Expression x, Expression expresssion) { }");
  }

  @Test public void paramAbbreviateConflictingWithLocal1() {
    trimming("void m(String string) {" + //
        "String s = null;" + //
        "string.substring(s, 2, 18);}").to("void m(String string){string.substring(null,2,18);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal1Simplified() {
    trimming("void m(String string) {" + //
        "String s = X;" + //
        "string.substring(s, 2, 18);}").to("void m(String string){string.substring(X,2,18);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal1SimplifiedFurther() {
    trimming("void m(String string) {" + //
        "String s = X;" + //
        "string.f(s);}").to("void m(String string){string.f(X);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal2() {
    trimming("TCPConnection conn(TCPConnection tcpCon) {" + //
        " UDPConnection c = new UDPConnection(57);" + //
        " if(tcpCon.isConnected()) " + //
        "   c.disconnect();}").to("TCPConnection conn(TCPConnection tcpCon){" //
            + " if(tcpCon.isConnected())" //
            + "   (new UDPConnection(57)).disconnect();"//
            + "}");
  }

  @Test public void paramAbbreviateConflictingWithMethodName() {
    trimming("void m(BitmapManipulator bitmapManipulator) {" + //
        "bitmapManipulator.x().y();").to(null);
  }

  @Test public void paramAbbreviateMultiple() {
    trimming("void m(StringBuilder stringBuilder, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
        "stringBuilder.clear();" + //
        "xmlDocument.open(stringBuilder.toString());" + //
        "dog.eat(xmlDocument.asEdible(cat));}").to("void m(StringBuilder b, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
            "b.clear();" + //
            "xmlDocument.open(b.toString());" + //
            "dog.eat(xmlDocument.asEdible(cat));}");
  }

  @Test public void paramAbbreviateNestedMethod() {
    trimming("void f(Iterator iterator) {" + //
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
    trimming("a ? b+x+e+f:b+y+e+f").to("b+(a ? x : y)+e+f");
  }

  @Test public void postDecreementReturn() {
    trimming("a--; return a;").to("--a;return a;");
  }

  @Test public void postDecremntInFunctionCall() {
    trimming("f(a++, i--, b++, ++b);").to(null);
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopCondition() {
    trimming("for (int s = i; ++i; ++s);").to(null);
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopInitializer() {
    trimming("for (int s = i++; i < 10; ++s);").to(null);
  }

  @Test public void postfixToPrefixAvoidChangeOnVariableDeclaration() {
    // We expect to print 2, but ++s will make it print 3
    trimming("int s = 2;" + //
        "int n = s++;" + //
        "S.out.print(n);").to("int s=2;S.out.print(s++);");
  }

  @Test public void postIncrementInFunctionCall() {
    trimming("f(i++);").to(null);
  }

  @Test public void postIncrementReturn() {
    trimming("a++; return a;").to("++a;return a;");
  }

  @Test public void preDecreementReturn() {
    trimming("--a.b.c; return a.b.c;").to("return--a.b.c;");
  }

  @Test public void preDecrementReturn() {
    trimming("--a; return a;").to("return --a;");
  }

  @Test public void preDecrementReturn1() {
    trimming("--this.a; return this.a;").to("return --this.a;");
  }

  @Test public void prefixToPosfixIncreementSimple() {
    trimming("i++").to("++i");
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
    azzert.that(new PostfixToPrefix().scopeIncludes(e), is(true));
    azzert.that(new PostfixToPrefix().eligible(e), is(true));
    final Expression r = new PostfixToPrefix().replacement(e);
    azzert.that(r, iz("--i"));
    trimming(from).to("for(int i=0;i<100;--i)--i;");
  }

  @Test public void prefixToPostfixIncreement() {
    trimming("for (int i = 0; i < 100; i++) i++;").to("for(int i=0;i<100;++i)++i;");
  }

  @Test public void preIncrementReturn() {
    trimming("++a; return a;").to("return ++a;");
  }

  @Test public void pushdowConditionalActualExampleFirstPass() {
    trimming("" //
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
    trimming("" //
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
    trimming("!!false").to("false");
  }

  @Test public void pushdownNot2LevelNotOfTrue() {
    trimming("!!true").to("true");
  }

  @Test public void pushdownNotActualExample() {
    trimming("!inRange(m, e)").to(null);
  }

  @Test public void pushdownNotDoubleNot() {
    trimming("!!f()").to("f()");
  }

  @Test public void pushdownNotDoubleNotDeeplyNested() {
    trimming("!(((!f())))").to("f()");
  }

  @Test public void pushdownNotDoubleNotNested() {
    trimming("!(!f())").to("f()");
  }

  @Test public void pushdownNotEND() {
    trimming("a&&b").to(null);
  }

  @Test public void pushdownNotMultiplication() {
    trimming("a*b").to(null);
  }

  @Test public void pushdownNotNotOfAND() {
    trimming("!(a && b && c)").to("!a || !b || !c");
  }

  @Test public void pushdownNotNotOfAND2() {
    trimming("!(f() && f(5))").to("!f() || !f(5)");
  }

  @Test public void pushdownNotNotOfANDNested() {
    trimming("!(f() && (f(5)))").to("!f() || !f(5)");
  }

  @Test public void pushdownNotNotOfEQ() {
    trimming("!(3 == 5)").to("3 != 5");
  }

  @Test public void pushdownNotNotOfEQNested() {
    trimming("!((((3 == 5))))").to("3 != 5");
  }

  @Test public void pushdownNotNotOfFalse() {
    trimming("!false").to("true");
  }

  @Test public void pushdownNotNotOfGE() {
    trimming("!(3 >= 5)").to("3 < 5");
  }

  @Test public void pushdownNotNotOfGT() {
    trimming("!(3 > 5)").to("3 <= 5");
  }

  @Test public void pushdownNotNotOfLE() {
    trimming("!(3 <= 5)").to("3 > 5");
  }

  @Test public void pushdownNotNotOfLT() {
    trimming("!(3 < 5)").to("3 >= 5");
  }

  @Test public void pushdownNotNotOfNE() {
    trimming("!(3 != 5)").to("3 == 5");
  }

  @Test public void pushdownNotNotOfOR() {
    trimming("!(a || b || c)").to("!a && !b && !c");
  }

  @Test public void pushdownNotNotOfOR2() {
    trimming("!(f() || f(5))").to("!f() && !f(5)");
  }

  @Test public void pushdownNotNotOfTrue() {
    trimming("!true").to("false");
  }

  @Test public void pushdownNotNotOfTrue2() {
    trimming("!!true").to("true");
  }

  @Test public void pushdownNotNotOfWrappedOR() {
    trimming("!((a) || b || c)").to("!a && !b && !c");
  }

  @Test public void pushdownNotOR() {
    trimming("a||b").to(null);
  }

  @Test public void pushdownNotSimpleNot() {
    trimming("!a").to(null);
  }

  @Test public void pushdownNotSimpleNotOfFunction() {
    trimming("!f(a)").to(null);
  }

  @Test public void pushdownNotSummation() {
    trimming("a+b").to(null);
  }

  @Test public void pushdownTernaryActualExample() {
    trimming("next < values().length").to(null);
  }

  @Test public void pushdownTernaryActualExample2() {
    trimming("!inRange(m, e) ? true : inner.go(r, e)").to("!inRange(m, e) || inner.go(r, e)");
  }

  @Test public void pushdownTernaryAlmostIdentical2Addition() {
    trimming("a ? b+d :b+ c").to("b+(a ? d : c)");
  }

  @Test public void pushdownTernaryAlmostIdentical3Addition() {
    trimming("a ? b+d +x:b+ c + x").to("b+(a ? d : c) + x");
  }

  @Test public void pushdownTernaryAlmostIdentical4AdditionLast() {
    trimming("a ? b+d+e+y:b+d+e+x").to("b+d+e+(a ? y : x)");
  }

  @Test public void pushdownTernaryAlmostIdentical4AdditionSecond() {
    trimming("a ? b+x+e+f:b+y+e+f").to("b+(a ? x : y)+e+f");
  }

  @Test public void pushdownTernaryAlmostIdenticalAssignment() {
    trimming("a ? (b=c) :(b=d)").to("b = a ? c : d");
  }

  @Test public void pushdownTernaryAlmostIdenticalFunctionCall() {
    trimming("a ? f(b) :f(c)").to("f(a ? b : c)");
  }

  @Test public void pushdownTernaryAlmostIdenticalMethodCall() {
    trimming("a ? y.f(b) :y.f(c)").to("y.f(a ? b : c)");
  }

  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall1Div2() {
    trimming("a ? f(b,x) :f(c,x)").to("f(a ? b : c,x)");
  }

  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall2Div2() {
    trimming("a ? f(x,b) :f(x,c)").to("f(x,a ? b : c)");
  }

  @Test public void pushdownTernaryAMethodCallDistinctReceiver() {
    trimming("a ? x.f(c) : y.f(d)").to(null);
  }

  @Test public void pushdownTernaryDifferentTargetFieldRefernce() {
    trimming("a ? 1 + x.a : 1 + y.a").to("1+(a ? x.a : y.a)");
  }

  @Test public void pushdownTernaryFieldReferneceShort() {
    trimming("a ? R.b.c : R.b.d").to(null);
  }

  @Test public void pushdownTernaryFunctionCall() {
    trimming("a ? f(b,c) : f(c)").to("!a?f(c):f(b,c)");
  }

  @Test public void pushdownTernaryFX() {
    trimming("a ? false : c").to("!a && c");
  }

  @Test public void pushdownTernaryIdenticalAddition() {
    trimming("a ? b+d :b+ d").to("b+d");
  }

  @Test public void pushdownTernaryIdenticalAdditionWtihParenthesis() {
    trimming("a ? (b+d) :(b+ d)").to("b+d");
  }

  @Test public void pushdownTernaryIdenticalAssignment() {
    trimming("a ? (b=c) :(b=c)").to("b = c");
  }

  @Test public void pushdownTernaryIdenticalAssignmentVariant() {
    trimming("a ? (b=c) :(b=d)").to("b=a?c:d");
  }

  @Test public void pushdownTernaryIdenticalFunctionCall() {
    trimming("a ? f(b) :f(b)").to("f(b)");
  }

  @Test public void pushdownTernaryIdenticalIncrement() {
    trimming("a ? b++ :b++").to("b++");
  }

  @Test public void pushdownTernaryIdenticalMethodCall() {
    trimming("a ? y.f(b) :y.f(b)").to("y.f(b)");
  }

  @Test public void pushdownTernaryintoConstructor1Div1Location() {
    trimming("a.equal(b) ? new S(new Integer(4)) : new S(new Ineger(3))").to("new S(a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void pushdownTernaryintoConstructor1Div3() {
    trimming("a.equal(b) ? new S(new Integer(4),a,b) : new S(new Ineger(3),a,b)").to("new S(a.equal(b)? new Integer(4): new Ineger(3), a, b)");
  }

  @Test public void pushdownTernaryintoConstructor2Div3() {
    trimming("a.equal(b) ? new S(a,new Integer(4),b) : new S(a, new Ineger(3), b)").to("new S(a,a.equal(b)? new Integer(4): new Ineger(3),b)");
  }

  @Test public void pushdownTernaryintoConstructor3Div3() {
    trimming("a.equal(b) ? new S(a,b,new Integer(4)) : new S(a,b,new Ineger(3))").to("new S(a, b, a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void pushdownTernaryintoConstructorNotSameArity() {
    trimming("a ? new S(a,new Integer(4),b) : new S(new Ineger(3))").to(
        "!a?new S(new Ineger(3)):new S(a,new Integer(4),b)                                                                                                                  ");
  }

  @Test public void pushdownTernaryintoPrintln() {
    trimming("    if (s.equals(t))\n"//
        + "      S.out.println(Hey + res);\n"//
        + "    else\n"//
        + "      S.out.println(Ho + x + a);").to("S.out.println(s.equals(t)?Hey+res:Ho+x+a);");
  }

  @Test public void pushdownTernaryLongFieldRefernece() {
    trimming("externalImage ? R.string.webview_contextmenu_image_download_action : R.string.webview_contextmenu_image_save_action")
        .to("!externalImage ? R.string.webview_contextmenu_image_save_action : R.string.webview_contextmenu_image_download_action");
  }

  @Test public void pushdownTernaryMethodInvocationFirst() {
    trimming("a?b():c").to("!a?c:b()");
  }

  @Test public void pushdownTernaryNoBoolean() {
    trimming("a?b:c").to(null);
  }

  @Test public void pushdownTernaryNoReceiverReceiver() {
    trimming("a < b ? f() : a.f()").to(null);
  }

  @Test public void pushdownTernaryNotOnMINUS() {
    trimming("a ? -c :-d").to(null);
  }

  @Test public void pushdownTernaryNotOnMINUSMINUS1() {
    trimming("a ? --c :--d").to(null);
  }

  @Test public void pushdownTernaryNotOnMINUSMINUS2() {
    trimming("a ? c-- :d--").to(null);
  }

  @Test public void pushdownTernaryNotOnNOT() {
    trimming("a ? !c :!d").to(null);
  }

  @Test public void pushdownTernaryNotOnPLUS() {
    trimming("a ? +x : +y").to("a ? x : y").to(null);
  }

  @Test public void pushdownTernaryNotOnPLUSPLUS() {
    trimming("a ? x++ :y++").to(null);
  }

  @Test public void pushdownTernaryNotSameFunctionInvocation() {
    trimming("a?b(x):d(x)").to(null);
  }

  @Test public void pushdownTernaryNotSameFunctionInvocation2() {
    trimming("a?x.f(x):x.d(x)").to(null);
  }

  @Test public void pushdownTernaryOnMethodCall() {
    trimming("a ? y.f(c,b) :y.f(c)").to("!a?y.f(c):y.f(c,b)");
  }

  @Test public void pushdownTernaryParFX() {
    trimming("a ?( false):true").to("!a && true");
  }

  @Test public void pushdownTernaryParTX() {
    trimming("a ? (((true ))): c").to("a || c");
  }

  @Test public void pushdownTernaryParXF() {
    trimming("a ? b : (false)").to("a && b");
  }

  @Test public void pushdownTernaryParXT() {
    trimming("a ? b : ((true))").to("!a || b");
  }

  @Test public void pushdownTernaryReceiverNoReceiver() {
    trimming("a < b ? a.f() : f()").to("a>=b?f():a.f()");
  }

  @Test public void pushdownTernaryToClasConstrctor() {
    trimming("a ? new B(a,b,c) : new B(a,x,c)").to("new B(a,a ? b : x ,c)");
  }

  @Test public void pushdownTernaryToClasConstrctorTwoDifferenes() {
    trimming("a ? new B(a,b,c) : new B(a,x,y)").to(null);
  }

  @Test public void pushdownTernaryToClassConstrctorNotSameNumberOfArgument() {
    trimming("a ? new B(a,b) : new B(a,b,c)").to(null);
  }

  @Test public void pushdownTernaryTX() {
    trimming("a ? true : c").to("a || c");
  }

  @Test public void pushdownTernaryXF() {
    trimming("a ? b : false").to("a && b");
  }

  @Test public void pushdownTernaryXT() {
    trimming("a ? b : true").to("!a || b");
  }

  @Test public void redundantButNecessaryBrackets1() {
    trimming("" //
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
        + "}").to(null);
  }

  @Test public void redundantButNecessaryBrackets2() {
    trimming("" //
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
        + "}").to(null);
  }

  @Test public void redundantButNecessaryBrackets3() {
    trimming("" //
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
        + "}").to(null);
  }

  @Test public void removeSuper() {
    trimming("class T {T(){super();}}").to("class T { T() { }}");
  }

  @Test public void removeSuperWithArgument() {
    trimming("class T { T() { super(a); a();}}").to(null);
  }

  @Test public void removeSuperWithStatemen() {
    trimming("class T { T() { super(); a++;}}").to("class T { T() { ++a;}}");
  }

  @Test public void renameToDollarActual() {
    trimming(//
        "        public static DeletePolicy fromInt(int initialSetting) {\n" + //
            "            for (DeletePolicy policy: values()) {\n" + //
            "                if (policy.setting == initialSetting) {\n" + //
            "                    return policy;\n" + //
            "                }\n" + //
            "            }\n" + //
            "            throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");\n" + //
            "        }").to(//
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
    trimming("int f() { for (int a: as) return a; }")//
        .to(" int f() {for(int $:as)return $;}");
  }

  @Test public void replaceInitializationInReturn() {
    trimming("int a = 3; return a + 4;").to("return 3 + 4;");
  }

  @Test public void replaceTwiceInitializationInReturn() {
    trimming("int a = 3; return a + 4 << a;").to("return 3 + 4 << 3;");
  }

  @Test public void rightSimplificatioForNulNNVariableReplacement() {
    final InfixExpression e = i("null != a");
    final Wring<InfixExpression> w = Toolbox.instance.find(e);
    assert w != null;
    assert w.scopeIncludes(e);
    assert w.eligible(e);
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) w).replacement(e);
    assert replacement != null;
    azzert.that("" + replacement, is("a != null"));
  }

  @Test public void rightSipmlificatioForNulNNVariable() {
    azzert.that(Toolbox.instance.find(i("null != a")), instanceOf(InfixComparisonSpecific.class));
  }

  @Test public void sequencerFirstInElse() {
    trimming("if (a) {b++; c++; ++d;} else { f++; g++; return x;}").to("if (!a) {f++; g++; return x;} b++; c++; ++d; ");
  }

  @Test public void shorterChainParenthesisComparison() {
    trimming("a == b == c").to(null);
  }

  @Test public void shorterChainParenthesisComparisonLast() {
    trimming("b == a * b * c * d * e * f * g * h == a").to(null);
  }

  @Test public void shortestBranchIfWithComplexNestedIf3() {
    trimming("if (a) {f(); g(); h();} else if (a) ++i; else ++j;").to(null);
  }

  @Test public void shortestBranchIfWithComplexNestedIf4() {
    trimming("if (a) {f(); g(); h(); ++i;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf5() {
    trimming("if (a) {f(); g(); h(); ++i; f();} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf6() {
    trimming("if (a) {f(); g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf7() {
    trimming("if (a) {f(); ++i; g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;")
        .to("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf8() {
    trimming("if (a) {f(); ++i; g(); h(); ++i; u++; f(); j++;} else if (a) ++i; else j++;")
        .to("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;u++;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIfPlain() {
    trimming("if (a) {f(); g(); h();} else { i++; j++;}").to("if(!a){i++;j++;}else{f();g();h();}");
  }

  @Test public void shortestBranchIfWithComplexSimpler() {
    trimming("if (a) {f(); g(); h();} else  i++; j++;").to("if(!a)i++;else{f();g();h();}++j;");
  }

  @Test public void shortestBranchInIf() {
    trimming("   int a=0;\n" + //
        "   if (s.equals(known)){\n" + //
        "     S.console();\n" + //
        "   } else {\n" + //
        "     a=3;\n" + //
        "   }\n" + //
        "").to("int a=0; if(!s.equals(known))a=3;else S.console();");
  }

  @Test public void shortestFirstAlignment() {
    trimming("n.isSimpleName() ? (SimpleName) n //\n" + //
        "            : n.isQualifiedName() ? ((QualifiedName) n).getName() //\n" + //
        "                : null").to(null);//
  }

  @Test public void shortestFirstAlignmentShortened() {
    trimming("n.isF() ? (SimpleName) n \n" + //
        "            : n.isG() ? ((QualifiedName) n).getName() \n" + //
        "                : null").to(null);//
  }

  @Test public void shortestFirstAlignmentShortenedFurther() {
    trimming("n.isF() ? (A) n : n.isG() ? ((B) n).f() \n" + //
        "                : null").to(null);//
  }

  @Test public void shortestFirstAlignmentShortenedFurtherAndFurther() {
    trimming("n.isF() ? (A) n : n.isG() ? (B) n :  null").to(null);//
  }

  @Test public void shortestIfBranchFirst01() {
    trimming(""//
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
    trimming("" //
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
    trimming("" + //
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
    trimming("" + //
        "      int res = 0;\n" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          res += 2;\n" + //
        "        else " + //
        "       if (s.charAt(i) == 'd')\n" + //
        "          --res;\n" + //
        "      return res;\n" + //
        "")//
            .to(null);
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
    trimming("" + //
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
    trimming("x + y > z").to(null);
  }

  @Test public void shortestOperand02() {
    trimming("k = k + 4;if (2 * 6 + 4 == k) return true;").to("k += 4;if (12 + 4 == k) return true;");
  }

  @Test public void shortestOperand05() {
    trimming("    W s = new W(\"bob\");\n" + //
        "    return s.l(hZ).l(\"-ba\").toString() == \"bob-ha-banai\";").to("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }

  @Test public void shortestOperand10() {
    trimming("return b == true;").to("return b;");
  }

  @Test public void shortestOperand11() {
    trimming("int h,u,m,a,n;return b == true && n + a > m - u || h > u;").to("int h,u,m,a,n;return b&&a+n>m-u||h>u;");
  }

  @Test public void shortestOperand12() {
    trimming("int k = 15; return 7 < k; ").to("return 7<15;");
  }

  @Test public void shortestOperand13() {
    trimming("return (2 > 2 + a) == true;").to("return 2>a+2;");
  }

  @Test public void shortestOperand13a() {
    trimming("(2 > 2 + a) == true").to("2>a+2 ");
  }

  @Test public void shortestOperand13b() {
    trimming("(2) == true").to("2 ");
  }

  @Test public void shortestOperand13c() {
    trimming("2 == true").to("2 ");
  }

  @Test public void shortestOperand14() {
    trimming("Integer t = new Integer(5);   return (t.toString() == null);    ").to("return((new Integer(5)).toString()==null);");
  }

  @Test public void shortestOperand17() {
    trimming("5 ^ a.getNum()").to("a.getNum() ^ 5");
  }

  @Test public void shortestOperand19() {
    trimming("k.get().operand() ^ a.get()").to("a.get() ^ k.get().operand()");
  }

  @Test public void shortestOperand20() {
    trimming("k.get() ^ a.get()").to("a.get() ^ k.get()");
  }

  @Test public void shortestOperand22() {
    trimming("return f(a,b,c,d,e) + f(a,b,c,d) + f(a,b,c) + f(a,b) + f(a) + f();").to(null);
  }

  @Test public void shortestOperand23() {
    trimming("return f() + \".\";     }").to(null);
  }

  @Test public void shortestOperand24() {
    trimming("f(a,b,c,d) & 175 & 0").to("f(a,b,c,d) & 0 & 175");
  }

  @Test public void shortestOperand25() {
    trimming("f(a,b,c,d) & bob & 0 ").to("bob & f(a,b,c,d) & 0");
  }

  @Test public void shortestOperand27() {
    trimming("return f(a,b,c,d) + f(a,b,c) + f();     } ").to(null);
  }

  @Test public void shortestOperand28() {
    trimming("return f(a,b,c,d) * f(a,b,c) * f();").to("return f()*f(a,b,c)*f(a,b,c,d);");
  }

  @Test public void shortestOperand29() {
    trimming("f(a,b,c,d) ^ f() ^ 0").to("f() ^ f(a,b,c,d) ^ 0");
  }

  @Test public void shortestOperand30() {
    trimming("f(a,b,c,d) & f()").to("f() & f(a,b,c,d)");
  }

  @Test public void shortestOperand31() {
    trimming("return f(a,b,c,d) | \".\";     }").to(null);
  }

  @Test public void shortestOperand32() {
    trimming("return f(a,b,c,d) && f();     }").to(null);
  }

  @Test public void shortestOperand33() {
    trimming("return f(a,b,c,d) || f();     }").to(null);
  }

  @Test public void shortestOperand34() {
    trimming("return f(a,b,c,d) + someVar; ").to(null);
  }

  @Test public void shortestOperand37() {
    trimming("return sansJavaExtension(f) + n + \".\"+ extension(f);").to(null);
  }

  @Test public void simpleBooleanMethod() {
    trimming("boolean f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .to("boolean f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void simplifyBlockComplexEmpty0() {
    trimming("{;}").to("/* empty */    ");
  }

  @Test public void simplifyBlockComplexEmpty0A() {
    trimming("{}").to("/* empty */");
  }

  @Test public void simplifyBlockComplexEmpty0B() {
    trimming("{;}").to("/* empty */");
  }

  @Test public void simplifyBlockComplexEmpty0C() {
    trimming("{{;}}").to("/* empty */");
  }

  @Test public void simplifyBlockComplexEmpty0D() {
    trimming("{;;;{;;;}{;}}").to("/* empty */    ");
  }

  @Test public void simplifyBlockComplexEmpty1() {
    trimming("{;;{;{{}}}{}{};}").to("/* empty */ ");
  }

  @Test public void simplifyBlockComplexSingleton() {
    assertSimplifiesTo("{;{{;;return b; }}}", "return b;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void simplifyBlockDeeplyNestedReturn() {
    assertSimplifiesTo("{{{;return c;};;};}", "return c;", new BlockSimplify(), Wrap.Statement);
  }

  /* Begin of already good tests */
  @Test public void simplifyBlockEmpty() {
    assertSimplifiesTo("{;;}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void simplifyBlockExpressionVsExpression() {
    trimming("6 - 7 < a * 3").to("-1 < 3 * a");
  }

  @Test public void simplifyBlockLiteralVsLiteral() {
    trimming("if (a) return b; else c();").to("if(a)return b;c();");
  }

  @Test public void simplifyBlockThreeStatements() {
    assertSimplifiesTo("{i++;{{;;return b; }}j++;}", "i++;return b;j++;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void simplifyLogicalNegationNested() {
    trimming("!((a || b == c) && (d || !(!!c)))").to("!a && b != c || !d && c");
  }

  @Test public void simplifyLogicalNegationNested1() {
    trimming("!(d || !(!!c))").to("!d && c");
  }

  @Test public void simplifyLogicalNegationNested2() {
    trimming("!(!d || !!!c)").to("d && c");
  }

  @Test public void simplifyLogicalNegationOfAnd() {
    trimming("!(f() && f(5))").to("!f() || !f(5)");
  }

  @Test public void simplifyLogicalNegationOfEquality() {
    trimming("!(3 == 5)").to("3!=5");
  }

  @Test public void simplifyLogicalNegationOfGreater() {
    trimming("!(3 > 5)").to("3 <= 5");
  }

  @Test public void simplifyLogicalNegationOfGreaterEquals() {
    trimming("!(3 >= 5)").to("3 < 5");
  }

  @Test public void simplifyLogicalNegationOfInequality() {
    trimming("!(3 != 5)").to("3 == 5");
  }

  @Test public void simplifyLogicalNegationOfLess() {
    trimming("!(3 < 5)").to("3 >= 5");
  }

  @Test public void simplifyLogicalNegationOfLessEquals() {
    trimming("!(3 <= 5)").to("3 > 5");
  }

  @Test public void simplifyLogicalNegationOfMultipleAnd() {
    trimming("!(a && b && c)").to("!a || !b || !c");
  }

  @Test public void simplifyLogicalNegationOfMultipleOr() {
    trimming("!(a || b || c)").to("!a && !b && !c");
  }

  @Test public void simplifyLogicalNegationOfNot() {
    trimming("!!f()").to("f()");
  }

  @Test public void simplifyLogicalNegationOfOr() {
    trimming("!(f() || f(5))").to("!f() && !f(5)");
  }

  @Test public void sortAddition1() {
    trimming("1 + 2 - 3 - 4 + 5 / 6 - 7 + 8 * 9  + A> k + 4").to("8*9+1+2-3-4+5 / 6-7+A>k+4");
  }

  @Test public void sortAddition2() {
    trimming("1 + 2 < 3 & 7 + 4 > 2 + 1 || 6 - 7 < 2 + 1").to("3 <3&11>3||-1<3");
  }

  @Test public void sortAddition3() {
    trimming("6 - 7 < 1 + 2").to("-1<3").to(null);
  }

  @Test public void sortAddition4() {
    trimming("a + 11 + 2 < 3 & 7 + 4 > 2 + 1").to("7 + 4 > 2 + 1 & a + 11 + 2 < 3");
  }

  @Test public void sortAdditionClassConstantAndLiteral() {
    trimming("1+A< 12").to("A+1<12");
  }

  @Test public void sortAdditionFunctionClassConstantAndLiteral() {
    trimming("1+A+f()< 12").to("f()+A+1<12");
  }

  @Test public void sortAdditionThreeOperands1() {
    trimming("1.0+2222+3").to("2226.0").to(null);
  }

  @Test public void sortAdditionThreeOperands2() {
    trimming("1.0+1+124+1").to("127.0");
  }

  @Test public void sortAdditionThreeOperands3() {
    trimming("1+2F+33+142+1").to(null);
  }

  @Test public void sortAdditionThreeOperands4() {
    trimming("1+2+'a'").to(null);
  }

  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_a() {
    trimming("1111+211").to("1322");
  }

  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_b() {
    trimming("211+1111").to("1322").to(null);
  }

  @Test public void sortAdditionTwoOperands1() {
    trimming("1+2F").to(null);
  }

  @Test public void sortAdditionTwoOperands2() {
    trimming("2.0+1").to("3.0");
  }

  @Test public void sortAdditionTwoOperands3() {
    trimming("1+2L").to("3L");
  }

  @Test public void sortAdditionTwoOperands4() {
    trimming("2L+1").to("3L");
  }

  @Test public void sortAdditionUncertain() {
    trimming("1+a").to(null);
  }

  @Test public void sortAdditionVariableClassConstantAndLiteral() {
    trimming("1+A+a< 12").to("a+A+1<12");
  }

  @Test public void sortConstantMultiplication() {
    trimming("a*2").to("2*a");
  }

  @Test public void sortDivision() {
    trimming("2.1/34.2/1.0").to("0.06140350877192982");
  }

  @Test public void sortDivisionLetters() {
    trimming("x/b/a").to("x/a/b");
  }

  @Test public void sortDivisionNo() {
    trimming("2.1/3").to("0.7000000000000001");
  }

  @Test public void sortThreeOperands1() {
    trimming("1.0*2222*3").to("6666.0");
  }

  @Test public void sortThreeOperands2() {
    trimming("1.0*11*124").to("1364.0");
  }

  @Test public void sortThreeOperands3() {
    trimming("2*2F*33*142").to(null);
  }

  @Test public void sortThreeOperands4() {
    trimming("2*3*'a'").to(null);
  }

  @Test public void sortTwoOperands0CheckThatWeSortByLength_a() {
    trimming("1111*211").to("234421");
  }

  @Test public void sortTwoOperands0CheckThatWeSortByLength_b() {
    trimming("211*1111").to("234421");
  }

  @Test public void sortTwoOperands1() {
    trimming("1F*2F").to(null);
  }

  @Test public void sortTwoOperands2() {
    trimming("2.0*2").to("4.0");
  }

  @Test public void sortTwoOperands3() {
    trimming("2*3L").to("6L");
  }

  @Test public void sortTwoOperands4() {
    trimming("2L*1L").to("2L");
  }

  @Test public void synchronizedBraces() {
    trimming("" //
        + "    synchronized (variables) {\n" //
        + "      for (final String key : variables.keySet())\n"//
        + "        $.variables.put(key, variables.get(key));\n" //
        + "    }").to(null);
  }

  @Test public void ternarize05() {
    trimming(" int res = 0; "//
        + "if (s.equals(532))    "//
        + "res += 6;   "//
        + "else    "//
        + "res += 9;      ").to("int res=0;res+=s.equals(532)?6:9;");
  }

  @Test public void ternarize05a() {
    trimming(" int res = 0; "//
        + "if (s.equals(532))    "//
        + "res += 6;   "//
        + "else    "//
        + "res += 9;      "//
        + "return res; ").to("int res=0;res+=s.equals(532)?6:9;return res;");
  }

  @Test public void ternarize07() {
    trimming("" //
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
    trimming("" //
        + "String res;" //
        + "res = s;   " //
        + "if (res==true)    " //
        + "  res = s + 0xABBA;   " //
        + "S.out.println(res); " //
        + "").to("String res=s;if(res)res=s+0xABBA;S.out.println(res);");
  }

  @Test public void ternarize07aa() {
    trimming("String res=s;if(res==true)res=s+0xABBA;S.out.println(res);").to("String res=s==true?s+0xABBA:s;S.out.println(res);");
  }

  @Test public void ternarize07b() {
    trimming("" //
        + "String res =s ;" //
        + "if (res.equals(532)==true)    " //
        + "  res = s + 0xABBA;   " //
        + "S.out.println(res); ")
            .to("" //
                + "String res=s.equals(532)==true?s+0xABBA:s;S.out.println(res);");
  }

  @Test public void ternarize09() {
    trimming("if (s.equals(532)) {    return 6;}else {    return 9;}").to("return s.equals(532)?6:9; ");
  }

  @Test public void ternarize10() {
    trimming("String res = s, foo = bar;   "//
        + "if (res.equals(532)==true)    " //
        + "res = s + 0xABBA;   "//
        + "S.out.println(res); ").to("String res=s.equals(532)==true?s+0xABBA:s,foo=bar;S.out.println(res);");
  }

  @Test public void ternarize12() {
    trimming("String res = s;   if (s.equals(532))    res = res + 0xABBA;   S.out.println(res); ")
        .to("String res=s.equals(532)?s+0xABBA:s;S.out.println(res);");
  }

  @Test public void ternarize13() {
    trimming("String res = m, foo;  if (m.equals(f())==true)   foo = M; ")//
        .to("String foo;if(m.equals(f())==true)foo=M;")//
        .to("String foo;if(m.equals(f()))foo=M;");
  }

  @Test public void ternarize13Simplified() {
    trimming("String r = m, f;  if (m.e(f()))   f = M; ")//
        .to("String f;if(m.e(f()))f=M;");
  }

  @Test public void ternarize13SimplifiedMore() {
    trimming("if (m.equals(f())==true)   foo = M; ").to("if (m.equals(f())) foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreAndMore() {
    trimming("f (m.equals(f())==true); foo = M; ").to("f (m.equals(f())); foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreAndMoreAndMore() {
    trimming("f (m.equals(f())==true);  ").to("f (m.equals(f()));");
  }

  @Test public void ternarize13SimplifiedMoreVariant() {
    trimming("if (m==true)   foo = M; ").to("if (m) foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreVariantShorter() {
    trimming("if (m==true)   f(); ").to("if (m) f();");
  }

  @Test public void ternarize13SimplifiedMoreVariantShorterAsExpression() {
    trimming("f (m==true);   f(); ").to("f (m); f();");
  }

  @Test public void ternarize14() {
    trimming("String res=m,foo=GY;if (res.equals(f())==true){foo = M;int k = 2;k = 8;S.out.println(foo);}f();")
        .to("String res=m,foo=GY;if(res.equals(f())){foo=M;int k=8;S.out.println(foo);}f();");
  }

  @Test public void ternarize16() {
    trimming("String res = m;  int num1, num2, num3;  if (m.equals(f()))   num2 = 2; ").to(null);
  }

  @Test public void ternarize16a() {
    trimming("int n1, n2 = 0, n3;\n" + //
        "  if (d)\n" + //
        "    n2 = 2;").to("int n1, n2 = d ? 2: 0, n3;");
  }

  public void ternarize18() {
    trimming("final String res=s;System.out.println(s.equals(res)?tH3+res:h2A+res+0);")//
        .to("System.out.println(s.equals(s)?tH3+res:h2A+s+0);");
  }

  @Test public void ternarize21() {
    trimming("if (s.equals(532)){    S.out.println(gG);    S.out.l(kKz);} f(); ").to(null);
  }

  @Test public void ternarize21a() {
    trimming("   if (s.equals(known)){\n" + //
        "     S.out.l(gG);\n" + //
        "   } else {\n" + //
        "     S.out.l(kKz);\n" + //
        "   }").to("S.out.l(s.equals(known)?gG:kKz);");
  }

  @Test public void ternarize22() {
    trimming("int a=0;   if (s.equals(532)){    S.console();    a=3;} f(); ").to(null);
  }

  @Test public void ternarize26() {
    trimming("int a=0;   if (s.equals(532)){    a+=2;   a-=2; } f(); ").to(null);
  }

  @Test public void ternarize33() {
    trimming("int a, b=0;   if (b==3){    a=4; } ")//
        .to("int a;if(0==3){a=4;}") //
        .to("int a;if(0==3)a=4;") //
        .to(null);
  }

  @Test public void ternarize35() {
    trimming("int a,b=0,c=0;a=4;if(c==3){b=2;}")//
        .to("int a=4,b=0,c=0;if(c==3)b=2;");
  }

  @Test public void ternarize36() {
    trimming("int a,b=0,c=0;a=4;if (c==3){  b=2;   a=6; } f();").to("int a=4,b=0,c=0;if(c==3){b=2;a=6;} f();");
  }

  @Test public void ternarize38() {
    trimming("int a, b=0;if (b==3){    a+=2+r();a-=6;} f();").to(null);
  }

  @Test public void ternarize41() {
    trimming("int a,b,c,d;a = 3;b = 5; d = 7;if (a == 4)while (b == 3) c = a; else while (d == 3)c =a*a; ")
        .to("int a=3,b,c,d;b=5;d=7;if(a==4)while(b==3)c=a;else while(d==3)c=a*a;");
  }

  @Test public void ternarize42() {
    trimming(" int a, b; a = 3;b = 5; if (a == 4) if (b == 3) b = 2; else{b = a; b=3;}  else if (b == 3) b = 2; else{ b = a*a;         b=3; }")//
        .to("int a=3,b;b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=b=3;}else if(b==3)b=2;else{b=9;b=3;}")//
        .to("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=9;b=3;}")//
        .to(null);
  }

  @Test public void ternarize45() {
    trimming("if (m.equals(f())==true) if (b==3){ return 3; return 7;}   else    if (b==3){ return 2;}     a=7; ")//
        .to("if (m.equals(f())) {if (b==3){ return 3; return 7;} if (b==3){ return 2;}   }  a=7; ");
  }

  @Test public void ternarize46() {
    trimming(//
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
            "     }").to("int a;if(m.equals(NG)==true)if(0==3){return 3;}else{a+=7;}else if(0==3){return 2;}else{a=7;}");
  }

  @Test public void ternarize49() {
    trimming("if (s.equals(532)){ S.out.println(gG); S.out.l(kKz); } f();").to(null);
  }

  @Test public void ternarize52() {
    trimming("int a=0,b = 0,c,d = 0,e = 0;if (a < b) {c = d;c = e;} f();")//
        .to(null);
  }

  @Test public void ternarize54() {
    trimming("int $=1,xi=0,xj=0,yi=0,yj=0; if(xi > xj == yi > yj)++$;else--$;")//
        .to(" int $=1,xj=0,yi=0,yj=0;      if(0>xj==yi>yj)++$;else--$;");
  }

  @Test public void ternarize55() {
    trimming("if (key.equals(markColumn))\n" + //
        " to.put(key, a.toString());\n" + //
        "else\n" + //
        "  to.put(key, missing(key, a) ? Z2 : get(key, a));").to("to.put(key,key.equals(markColumn)?a.toString():missing(key,a)?Z2:get(key,a));");
  }

  @Test public void ternarize56() {
    trimming("if (target == 0) {p.f(X); p.v(0); p.f(q +  target); p.v(q * 100 / target); } f();") //
        .to("if(target==0){p.f(X);p.v(0);p.f(q+target);p.v(100*q / target); } f();");
  }

  @Test public void ternarizeintoSuperMethodInvocation() {
    trimming("a ? super.f(a, b, c) : super.f(a, x, c)").to("super.f(a, a ? b : x, c)");
  }

  @Test public void ternaryPushdownOfReciever() {
    trimming("a ? b.f():c.f()").to("(a?b:c).f()");
  }

  @Test public void testPeel() {
    azzert.that(Wrap.Expression.off(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge")),
        is("on * notion * of * no * nothion != the * plain + kludge"));
  }

  @Test public void twoMultiplication1() {
    trimming("f(a,b,c,d) * f()").to("f() * f(a,b,c,d)");
  }

  @Test public void twoOpportunityExample() {
    azzert.that(TrimmerTestsUtils.countOpportunities(new Trimmer(),
        (CompilationUnit) makeAST.COMPILATION_UNIT.from(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge"))), is(2));
    azzert.that(TrimmerTestsUtils.countOpportunities(new Trimmer(),
        (CompilationUnit) makeAST.COMPILATION_UNIT.from(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge"))), is(2));
  }

  @Test public void unsafeBlockSimlify() {
    trimming("" //
        + "public void testParseInteger() {\n" //
        + "  String source = \"10\";\n" //
        + "  {\n" //
        + "    BigFraction c = properFormat.parse(source);\n" //
        + "   assert c != null;\n" //
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" //
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    BigFraction c = improperFormat.parse(source);\n" //
        + "   assert c != null;\n" //
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" //
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void useOutcontextToManageStringAmbiguity() {
    trimming("1+2+s<3").to("s+1+2<3");
  }

  @Test public void vanillaShortestFirstConditionalNoChange() {
    trimming("literal ? CONDITIONAL_OR : CONDITIONAL_AND").to(null);
  }

  @Test public void xorSortClassConstantsAtEnd() {
    trimming("f(a,b,c,d) ^ BOB").to(null);
  }

  @Test public void issue131_1() {
    trimming("for(int i=4 ; i<s.length() ; ++i){i+=9;i++;return xxx;}return xxx;")
        .to("for(int i=4 ; i<s.length() ; ++i){i+=9;++i;break;}return xxx;");
  }

  @Test public void issue131_2() {
    trimming("for(int i=4 ; i<s.length() ; ++i){i+=9;return xxx;}return xxx;").to("for(int i=4 ; i<s.length() ; ++i){i+=9;break;}return xxx;");
  }

  @Test public void issue131_3() {
    trimming("for(int i=4 ; i<s.length() ; ++i)return xxx;return xxx;").to("for(int i=4 ; i<s.length() ; ++i)break;return xxx;");
  }

  @Test public void issue131_4() {
    trimming("for(int i=4 ; i<s.length() ; ++i)if(t=4)return xxx;return xxx;").to("for(int i=4 ; i<s.length() ; ++i)if(t=4)break;return xxx;");
  }

  @Test public void issue131_5() {
    trimming("while(i>5){i+=9;i++;return xxx;}return xxx;").to("while(i>5){i+=9;++i;break;}return xxx;");
  }

  @Test public void issue131_6() {
    trimming("while(i>5){i+=9;return xxx;}return xxx;").to("while(i>5){i+=9;break;}return xxx;");
  }

  @Test public void issue131_7() {
    trimming("while(i>5)return xxx;return xxx;").to("while(i>5)break;return xxx;");
  }

  @Test public void issue131_8() {
    trimming("while(i>5)if(t=4)return xxx;return xxx;").to("while(i>5)if(t=4)break;return xxx;");
  }

  @Test public void DUPLICATED_actualExampleForSortAddition() {
    trimming("1 + b.statements().indexOf(declarationStmt)").to(null);
  }

  @Test public void DUPLICATED_actualExampleForSortAdditionInContext() {
    final String from = "2 + a < b";
    final String expected = "a + 2 < b";
    final Wrap w = Wrap.Expression;
    final String wrap = w.on(from);
    that(w.off(wrap), is(from));
    final Trimmer t = new Trimmer();
    final String unpeeled = TrimmerTestsUtils.apply(t, wrap);
    azzert.that("Nothing done on " + from, wrap, not(unpeeled));
    final String peeled = w.off(unpeeled);
    azzert.that("No similification of " + from, from, not(peeled));
    azzert.that("Simpification of " + from + " is just reformatting", compressSpaces(peeled), not(compressSpaces(from)));
    assertSimilar(expected, peeled);
  }

  @Test public void DUPLICATED_andWithCLASS_CONSTANT() {
    trimming("(x >> 18) & MASK_BITS").to(null);
    trimming("(x >> 18) & MASK_6BITS").to(null);
  }

  @Test public void annotationDoNotRemoveSingleMemberNotCalledValue() {
    trimming("@SuppressWarnings(sky = \"blue\") void m() {}").to(null);
  }

  @Test public void annotationDoNotRemoveValueAndSomethingElse() {
    trimming("@SuppressWarnings(value = \"something\", x = 2) void m() {}").to(null);
  }

  @Test public void annotationRemoveEmptyParentheses() {
    trimming("@Override() void m() {}").to("@Override void m() {}");
  }

  @Test public void annotationRemoveValueFromMultipleAnnotations() {
    trimming("@SuppressWarnings(value = \"javadoc\") @TargetApi(value = 23) void m() {}") //
        .to("@SuppressWarnings(\"javadoc\") @TargetApi(23) void m() {}");
  }

  @Test public void annotationRemoveValueMemberArrayValue() {
    trimming("@SuppressWarnings(value = { \"something\", \"something else\" }) void m() {}") //
        .to("@SuppressWarnings({ \"something\", \"something else\" }) void m() {}");
  }

  @Test public void annotationRemoveValueMemberSingleValue() {
    trimming("@SuppressWarnings(value = \"something\") void m() {}") //
        .to("@SuppressWarnings(\"something\") void m() {}");
  }

  @Test public void DUPLICATED_assignmentAssignmentChain1() {
    trimming("c = a = 13; b = 13;").to("b = c = a = 13;");
  }

  @Test public void DUPLICATED_assignmentAssignmentChain2() {
    trimming("a = 13; b= c = 13;").to("b = c = a = 13;");
  }

  @Test public void DUPLICATED_assignmentAssignmentChain3() {
    trimming("a = b = 13; c = d = 13;").to("c = d = a = b = 13;");
  }

  @Test public void DUPLICATED_assignmentAssignmentChain4() {
    trimming("a1 = a2 = a3 = a4 = 13; b1 = b2 = b3 = b4 = b5 = 13;")//
        .to("b1 = b2 = b3 = b4 = b5 = a1 = a2 = a3 = a4 = 13;");
  }

  @Test public void DUPLICATED_assignmentAssignmentChain5() {
    trimming("a1 = (a2 = (a3 = (a4 = 13))); b1 = b2 = b3 = ((((b4 = (b5 = 13)))));")//
        .to("b1=b2=b3=((((b4=(b5=a1=(a2=(a3=(a4=13))))))));");
  }

  @Test public void DUPLICATED_assignmentAssignmentNew() {
    trimming("a = new B(); b= new B();").to(null);
  }

  @Test public void DUPLICATED_assignmentAssignmentNewArray() {
    trimming("a = new A[3]; b= new A[3];").to(null);
  }

  @Test public void DUPLICATED_assignmentAssignmentNull() {
    trimming("c = a = null; b = null;").to(null);
  }

  @Test public void DUPLICATED_assignmentAssignmentSideEffect() {
    trimming("a = f(); b= f();").to(null);
  }

  @Test public void DUPLICATED_assignmentAssignmentVanilla() {
    trimming("a = 13; b= 13;").to("b = a = 13;");
  }

  @Test public void DUPLICATED_assignmentAssignmentVanilla0() {
    trimming("a = 0; b = 0;").to("b = a = 0;");
  }

  @Test public void DUPLICATED_assignmentAssignmentVanillaScopeIncludes() {
    included("a = 3; b = 3;", Assignment.class).in(new AssignmentAndAssignment());
  }

  @Test public void DUPLICATED_assignmentAssignmentVanillaScopeIncludesNull() {
    included("a = null; b = null;", Assignment.class).notIn(new AssignmentAndAssignment());
  }

  @Test public void DUPLICATED_assignmentReturn0() {
    trimming("a = 3; return a;").to("return a = 3;");
  }

  @Test public void DUPLICATED_assignmentReturn1() {
    trimming("a = 3; return (a);").to("return a = 3;");
  }

  @Test public void DUPLICATED_assignmentReturn2() {
    trimming("a += 3; return a;").to("return a += 3;");
  }

  @Test public void DUPLICATED_assignmentReturn3() {
    trimming("a *= 3; return a;").to("return a *= 3;");
  }

  @Test public void DUPLICATED_assignmentReturniNo() {
    trimming("b = a = 3; return a;").to(null);
  }

  @Test public void DUPLICATED_blockSimplifyVanilla() {
    trimming("if (a) {f(); }").to("if (a) f();");
  }

  @Test public void DUPLICATED_blockSimplifyVanillaSimplified() {
    trimming(" {f(); }").to("f();");
  }

  @Test public void booleanChangeValueOfToConstant() {
    trimming("Boolean b = Boolean.valueOf(true);").to("Boolean b = Boolean.TRUE;");
    trimming("Boolean b = Boolean.valueOf(false);").to("Boolean b = Boolean.FALSE;");
  }

  @Test public void booleanChangeValueOfToConstantNotConstant() {
    trimming("Boolean.valueOf(expected);").to(null); // from junit source
  }

  @Test public void DUPLICATED_bugInLastIfInMethod() {
    trimming("" + //
        "        @Override public void messageFinished(final LocalMessage myMessage, final int number, final int ofTotal) {\n" + //
        "          if (!isMessageSuppressed(myMessage)) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(myMessage);\n" + //
        "            stats.unreadMessageCount += myMessage.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += myMessage.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "@Override public void messageFinished(final LocalMessage myMessage,final int number,final int ofTotal){if(isMessageSuppressed(myMessage))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(myMessage);stats.unreadMessageCount+=myMessage.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=myMessage.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void DUPLICATED_bugInLastIfInMethod1() {
    trimming("" + //
        "        @Override public void f() {\n" + //
        "          if (!isMessageSuppressed(message)) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "@Override public void f(){if(isMessageSuppressed(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void DUPLICATED_bugInLastIfInMethod2() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g(message)) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void DUPLICATED_bugInLastIfInMethod3() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g(a)) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g(a))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void DUPLICATED_bugInLastIfInMethod4() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "            if (listener != null)\n" + //
        "              listener.listLocalMessagesAddMessages(account, null, messages);\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void DUPLICATED_bugInLastIfInMethod5() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void DUPLICATED_bugInLastIfInMethod6() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            final int messages = 3;\n" + //
        "            messages.add(message);\n" + //
        "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n" + //
        "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n" + //
        "          }\n" + //
        "        }").to(
            "public void f(){if(g)return;final int messages=3;messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void DUPLICATED_bugInLastIfInMethod7() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (!g) {\n" + //
        "            foo();\n" + //
        "            bar();\n" + //
        "          }\n" + //
        "        }").to("public void f(){if(g)return;foo();bar();}");
  }

  @Test public void DUPLICATED_bugInLastIfInMethod8() {
    trimming("" + //
        "        public void f() {\n" + //
        "          if (g) {\n" + //
        "            foo();\n" + //
        "            bar();\n" + //
        "          }\n" + //
        "        }").to("public void f(){if(!g)return;foo();bar();}");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord1() {
    trimming("b.f(a) && -1 == As.g(f).h(c) ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("b.f(a) && As.g(f).h(c) == -1 ? o(s,b,g(f)) : b.f(\".in\") && !y(d,b)? o(b.z(u,v),s,f) : null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord1a() {
    trimming("-1 == As.g(f).h(c)").to("As.g(f).h(c)==-1");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord1b() {
    trimming("b.f(a) && X ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("b.f(a)&&X?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord1c() {
    trimming("Y ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .to("Y?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord1d() {
    trimming("Y ? Z : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)").to("Y?Z:b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord1e() {
    trimming("Y ? Z : R ? null : S ? null : T").to("Y?Z:!R&&!S?T:null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2() {
    trimming(
        "name.endsWith(testSuffix) &&  As.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("name.endsWith(testSuffix)&&As.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2a() {
    trimming(
        "name.endsWith(testSuffix) &&  As.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("name.endsWith(testSuffix)&&As.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2b() {
    trimming(
        "name.endsWith(testSuffix) &&  T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("name.endsWith(testSuffix) && T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2c() {
    trimming(
        "X && T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("X && T ? objects(s,name,makeInFile(f)) : name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2d() {
    trimming("X && T ? E : Y ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X && T ? E : !Y && !dotOutExists(d,name) ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2e() {
    trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2e1() {
    trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(x, Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(x,Z2),s,f) : null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2e2() {
    trimming("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(g, Z2), s, f)")
        .to("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(g,Z2),s,f) : null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord2f() {
    trimming("X &&  T ? E : Y ? null : Z ? null : F").to("X&&T?E:!Y&&!Z?F:null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord3() {
    trimming(
        "name.endsWith(testSuffix) && -1 == As.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
            .to("name.endsWith(testSuffix)&&As.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWord3a() {
    trimming("!name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
        .to("name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWordTry1() {
    trimming(
        "name.endsWith(testSuffix) && -1 == As.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .to("name.endsWith(testSuffix) && As.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWordTry2() {
    trimming("!(intent.getBooleanExtra(EXTRA_FROM_SHORTCUT, false) && !K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName()))")
        .to("!intent.getBooleanExtra(EXTRA_FROM_SHORTCUT,false)||K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName())");
  }

  @Test public void DUPLICATED_bugIntroducingMISSINGWordTry3() {
    trimming("!(f.g(X, false) && !a.b.e(m.h()))").to("!f.g(X,false)||a.b.e(m.h())");
  }

  @Test public void DUPLICATED_bugOfMissingTry() {
    trimming("!(A && B && C && true && D)").to("!A||!B||!C||false||!D");
  }

  @Test public void DUPLICATED_canonicalFragementExamples() {
    trimming("int a; a = 3;").to("int a = 3;");
    trimming("int a = 2; if (b) a = 3; ").to("int a = b ? 3 : 2;");
    trimming("int a = 2; a += 3; ").to("int a = 2 + 3;");
    trimming("int a = 2; a = 3 * a; ").to("int a = 3 * 2;");
    trimming("int a = 2; return 3 * a; ").to("return 3 * 2;");
    trimming("int a = 2; return a; ").to("return 2;");
  }

  @Test public void DUPLICATED_canonicalFragementExamplesWithExraFragmentsX() {
    trimming("int a; if (x) a = 3; else a++;").to("int a;if(x)a=3;else++a;");
  }

  @Test public void DUPLICATED_chainComparison() {
    final InfixExpression e = i("a == true == b == c");
    that("" + step.right(e), iz("c"));
    trimming("a == true == b == c").to("a == b == c");
  }

  @Test public void DUPLICATED_chainCOmparisonTrueLast() {
    trimming("a == b == c == true").to("a == b == c");
  }

  @Test public void DUPLICATED_comaprisonWithBoolean1() {
    trimming("s.equals(532)==true").to("s.equals(532)");
  }

  @Test public void DUPLICATED_comaprisonWithBoolean2() {
    trimming("s.equals(532)==false ").to("!s.equals(532)");
  }

  @Test public void DUPLICATED_comaprisonWithBoolean3() {
    trimming("(false==s.equals(532))").to("(!s.equals(532))");
  }

  @Test public void DUPLICATED_comaprisonWithSpecific0() {
    trimming("this != a").to("a != this");
  }

  @Test public void DUPLICATED_comaprisonWithSpecific0Legibiliy00() {
    final InfixExpression e = i("this != a");
    that(in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS), is(true));
    that(iz.booleanLiteral(step.right(e)), is(false));
    that(iz.booleanLiteral(step.left(e)), is(false));
    that(in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS), is(true));
  }

  @Test public void DUPLICATED_comaprisonWithSpecific1() {
    trimming("null != a").to("a != null");
  }

  @Test public void DUPLICATED_comaprisonWithSpecific2() {
    trimming("null != a").to("a != null");
    trimming("this == a").to("a == this");
    trimming("null == a").to("a == null");
    trimming("this >= a").to("a <= this");
    trimming("null >= a").to("a <= null");
    trimming("this <= a").to("a >= this");
    trimming("null <= a").to("a >= null");
  }

  @Test public void DUPLICATED_comaprisonWithSpecific2a() {
    trimming("s.equals(532)==false").to("!s.equals(532)");
  }

  @Test public void DUPLICATED_comaprisonWithSpecific3() {
    trimming("(this==s.equals(532))").to("(s.equals(532)==this)");
  }

  @Test public void DUPLICATED_comaprisonWithSpecific4() {
    trimming("(0 < a)").to("(a>0)");
  }

  @Test public void DUPLICATED_comaprisonWithSpecificInParenthesis() {
    trimming("(null==a)").to("(a==null)");
  }

  @Test public void DUPLICATED_commonPrefixEntirelyIfBranches() {
    trimming("if (s.equals(532)) S.out.close();else S.out.close();").to("S.out.close(); ");
  }

  @Test public void DUPLICATED_commonPrefixIfBranchesInFor() {
    trimming("for (;;) if (a) {i++;j++;j++;} else { i++;j++; i++;}").to("for(;;){i++;j++;if(a)j++;else i++;}");
  }

  @Test public void DUPLICATED_commonSuffixIfBranches() {
    trimming("if (a) { \n" + //
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

  @Test public void DUPLICATED_commonSuffixIfBranchesDisappearingElse() {
    trimming("if (a) { \n" + //
        "++i;\n" + //
        "f();\n" + //
        "} else {\n" + //
        "f();\n" + //
        "}").to("if (a)  \n" + //
            "++i;\n" + //
            "\n" + //
            "f();");//
  }

  @Test public void DUPLICATED_commonSuffixIfBranchesDisappearingThen() {
    trimming("if (a) { \n" + //
        "f();\n" + //
        "} else {\n" + //
        "++j;\n" + //
        "f();\n" + //
        "}").to("if (!a)  \n" + //
            "++j;\n" + //
            "\n" + //
            "f();");//
  }

  @Test public void DUPLICATED_commonSuffixIfBranchesDisappearingThenWithinIf() {
    trimming("if (x)  if (a) { \n" + //
        "f();\n" + //
        "} else {\n" + //
        "++j;\n" + //
        "f();\n" + //
        "} else { h(); ++i; ++j; ++k; if (a) f(); else g(); }").to("if (x) { if (!a)  \n" + //
            "++j;\n" + //
            "\n" + //
            "f(); } else { h(); ++i; ++j; ++k;  if (a) f(); else g();}");//
  }

  @Test public void DUPLICATED_compareWithBoolean00() {
    trimming("a == true").to("a");
  }

  @Test public void DUPLICATED_compareWithBoolean01() {
    trimming("a == false").to("!a");
  }

  @Test public void DUPLICATED_compareWithBoolean10() {
    trimming("true == a").to("a");
  }

  @Test public void DUPLICATED_compareWithBoolean100() {
    trimming("a != true").to("!a");
  }

  @Test public void DUPLICATED_compareWithBoolean100a() {
    trimming("(((a))) != true").to("!a");
  }

  @Test public void DUPLICATED_compareWithBoolean101() {
    trimming("a != false").to("a");
  }

  @Test public void DUPLICATED_compareWithBoolean11() {
    trimming("false == a").to("!a");
  }

  @Test public void DUPLICATED_compareWithBoolean110() {
    trimming("true != a").to("!a");
  }

  @Test public void DUPLICATED_compareWithBoolean111() {
    trimming("false != a").to("a");
  }

  @Test public void DUPLICATED_compareWithBoolean2() {
    trimming("false != false").to("false");
  }

  @Test public void DUPLICATED_compareWithBoolean3() {
    trimming("false != true").to("true");
  }

  @Test public void DUPLICATED_compareWithBoolean4() {
    trimming("false == false").to("true");
  }

  @Test public void DUPLICATED_compareWithBoolean5() {
    trimming("false == true").to("false");
  }

  @Test public void DUPLICATED_compareWithBoolean6() {
    trimming("false != false").to("false");
  }

  @Test public void DUPLICATED_compareWithBoolean7() {
    trimming("true != true").to("false");
  }

  @Test public void DUPLICATED_compareWithBoolean8() {
    trimming("true != false").to("true");
  }

  @Test public void DUPLICATED_compareWithBoolean9() {
    trimming("true != true").to("false");
  }

  @Test public void DUPLICATED_comparison01() {
    trimming("1+2+3<3").to("6<3").to(null);
  }

  @Test public void DUPLICATED_comparison02() {
    trimming("f(2)<a").to(null);
  }

  @Test public void DUPLICATED_comparison03() {
    trimming("this==null").to(null);
  }

  @Test public void DUPLICATED_comparison04() {
    trimming("6-7<2+1").to("-1<3");
  }

  @Test public void DUPLICATED_comparison05() {
    trimming("a==11").to(null);
  }

  @Test public void DUPLICATED_comparison06() {
    trimming("1<102333").to(null);
  }

  @Test public void DUPLICATED_comparison08() {
    trimming("a==this").to(null);
  }

  @Test public void DUPLICATED_comparison09() {
    trimming("1+2<3&7+4>2+1").to("3<3&11>3");
  }

  @Test public void DUPLICATED_comparison11() {
    trimming("12==this").to("this==12");
  }

  @Test public void DUPLICATED_comparison12() {
    trimming("1+2<3&7+4>2+1||6-7<2+1").to("3<3&11>3||-1<3");
  }

  @Test public void DUPLICATED_comparison13() {
    trimming("13455643294<22").to(null);
  }

  @Test public void DUPLICATED_comparisonWithCharacterConstant() {
    trimming("'d' == s.charAt(i)").to("s.charAt(i)=='d'");
  }

  @Test public void DUPLICATED_compreaeExpressionToExpression() {
    trimming("6 - 7 < 2 + 1   ").to("-1<3");
  }

  @Test public void DUPLICATED_correctSubstitutionInIfAssignment() {
    trimming("int a = 2+3; if (a+b > a << b) a =(((((a *7 << a)))));")//
        .to("int a=2+3+b>2+3<<b?(2+3)*7<<2+3:2+3;");
  }

  @Test public void DUPLICATED_declarationAssignmentUpdateWithIncrement() {
    trimming("int a=0; a+=++a;").to(null);
  }

  @Test public void DUPLICATED_declarationAssignmentUpdateWithPostIncrement() {
    trimming("int a=0; a+=a++;").to(null);
  }

  @Test public void DUPLICATED_declarationAssignmentWithIncrement() {
    trimming("int a=0; a=++a;").to(null);
  }

  @Test public void DUPLICATED_declarationAssignmentWithPostIncrement() {
    trimming("int a=0; a=a++;").to(null);
  }

  @Test public void DUPLICATED_declarationIfAssignment() {
    trimming("" + //
        "    String $ = s;\n" + //
        "    if (s.equals(y))\n" + //
        "      $ = s + blah;\n" + //
        "    S.out.println($);").to("" + //
            "    String $ = s.equals(y) ? s + blah :s;\n" + //
            "    S.out.println($);");
  }

  @Test public void DUPLICATED_declarationIfAssignment3() {
    trimming("int a =2; if (a != 2) a = 3;").to("int a = 2 != 2 ? 3 : 2;");
  }

  @Test public void DUPLICATED_declarationIfAssignment4() {
    trimming("int a =2; if (x) a = 2*a;").to("int a = x ? 2*2: 2;");
  }

  @Test public void DUPLICATED_declarationIfUpdateAssignment() {
    trimming("" + //
        "    String $ = s;\n" + //
        "    if (s.equals(y))\n" + //
        "      $ += s + blah;\n" + //
        "    S.out.println($);").to("" + //
            "    String $ = s.equals(y) ? s + s + blah :s;\n" + //
            "    S.out.println($);");
  }

  @Test public void DUPLICATED_declarationIfUsesLaterVariable() {
    trimming("int a=0, b=0;if (b==3)   a=4;")//
        .to(" int a=0;if(0==3)a=4;") //
        .to(" int a=0==3?4:0;");
  }

  @Test public void DUPLICATED_declarationIfUsesLaterVariable1() {
    trimming("int a=0, b=0;if (b==3)   a=4; f();").to(null);
  }

  @Test public void DUPLICATED_declarationInitializeRightShift() {
    trimming("int a = 3;a>>=2;").to("int a = 3 >> 2;");
  }

  @Test public void DUPLICATED_declarationInitializerReturnAssignment() {
    trimming("int a = 3; return a = 2 * a;").to("return 2 * 3;");
  }

  @Test public void DUPLICATED_declarationInitializerReturnExpression() {
    trimming("" //
        + "String t = Bob + Wants + To + \"Sleep \"; "//
        + "  return (right_now + t);    ").to("return (right_now+Bob+Wants+To+\"Sleep \");");
  }

  @Test public void DUPLICATED_declarationInitializesRotate() {
    trimming("int a = 3;a>>>=2;").to("int a = 3 >>> 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateAnd() {
    trimming("int a = 3;a&=2;").to("int a = 3 & 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateAssignment() {
    trimming("int a = 3;a += 2;").to("int a = 3+2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateAssignmentFunctionCallWithReuse() {
    trimming("int a = f();a += 2*f();").to("int a=f()+2*f();");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateAssignmentFunctionCallWIthReuse() {
    trimming("int a = x;a += a + 2*f();").to("int a=x+x+2*f();");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateAssignmentIncrement() {
    trimming("int a = ++i;a += j;").to("int a = ++i + j;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateAssignmentIncrementTwice() {
    trimming("int a = ++i;a += a + j;").to(null);
  }

  @Test public void DUPLICATED_declarationInitializeUpdateAssignmentWithReuse() {
    trimming("int a = 3;a += 2*a;").to("int a = 3+2*3;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateDividies() {
    trimming("int a = 3;a/=2;").to("int a = 3 / 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateLeftShift() {
    trimming("int a = 3;a<<=2;").to("int a = 3 << 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateMinus() {
    trimming("int a = 3;a-=2;").to("int a = 3 - 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateModulo() {
    trimming("int a = 3;a%= 2;").to("int a = 3 % 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdatePlus() {
    trimming("int a = 3;a+=2;").to("int a = 3 + 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateTimes() {
    trimming("int a = 3;a*=2;").to("int a = 3 * 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdateXor() {
    trimming("int a = 3;a^=2;").to("int a = 3 ^ 2;");
  }

  @Test public void DUPLICATED_declarationInitializeUpdatOr() {
    trimming("int a = 3;a|=2;").to("int a = 3 | 2;");
  }

  @Test public void DUPLICATED_declarationUpdateReturn() {
    trimming("int a = 3; return a += 2;").to("return 3 + 2;");
  }

  @Test public void DUPLICATED_declarationUpdateReturnNone() {
    trimming("int a = f(); return a += 2 * a;").to(null);
  }

  @Test public void DUPLICATED_declarationUpdateReturnTwice() {
    trimming("int a = 3; return a += 2 * a;").to("return 3 + 2 *3 ;");
  }

  @Test public void DUPLICATED_delcartionIfAssignmentNotPlain() {
    trimming("int a=0;   if (y) a+=3; ").to("int a = y ? 0 + 3 : 0;");
  }

  @Test public void DUPLICATED_doNotConsolidateNewArrayActual() {
    trimming("" + //
        "occupied = new boolean[capacity];\n" + //
        "placeholder = new boolean[capacity];").to(null);
  }

  @Test public void DUPLICATED_doNotConsolidateNewArraySimplifiedl() {
    trimming("" + //
        "a = new int[1];\n" + //
        "b = new int[1];").to(null);
  }

  @Test public void DUPLICATED_doNotConsolidatePlainNew() {
    trimming("" + //
        "a = new A();\n" + //
        "b = new B();").to(null);
  }

  @Test public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimming("" + //
        "    @SuppressWarnings int $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }").to(null);
  }

  @Test public void DUPLICATED_doNotInlineWithDeclaration() {
    trimming("  private Class<? extends T> retrieveClazz() throws ClassNotFoundException {\n" + //
        "    nonnull(className);\n" + //
        "    @SuppressWarnings(\"unchecked\") final Class<T> $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }").to(null);
  }

  @Test public void DUPLICATED_doNotIntroduceDoubleNegation() {
    trimming("!Y ? null :!Z ? null : F").to("Y&&Z?F:null");
  }

  @Test public void DUPLICATED_donotSorMixedTypes() {
    trimming("if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;") //
        .to("if (1130.94 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;");
  }

  @Test public void DUPLICATED_dontELiminateCatchBlock() {
    trimming("try { f(); } catch (Exception e) { } finally {}").to(null);
  }

  @Test public void DUPLICATED_dontSimplifyCatchBlock() {
    trimming("try { {} ; {} } catch (Exception e) {{} ; {}  } finally {{} ; {}}")//
        .to(" try {}          catch (Exception e) {}          finally {}");
  }

  @Test public void DUPLICATED_duplicatePartialIfBranches() {
    trimming("" + //
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

  @Test public void DUPLICATED_emptyElse() {
    trimming("if (x) b = 3; else ;").to("if (x) b = 3;");
  }

  @Test public void DUPLICATED_emptyElseBlock() {
    trimming("if (x) b = 3; else { ;}").to("if (x) b = 3;");
  }

  @Test public void DUPLICATED_emptyIsNotChangedExpression() {
    trimming("").to(null);
  }

  @Test public void DUPLICATED_emptyIsNotChangedStatement() {
    trimming("").to(null);
  }

  @Test public void DUPLICATED_emptyThen1() {
    trimming("if (b) ; else x();").to("if (!b) x();");
  }

  @Test public void DUPLICATED_emptyThen2() {
    trimming("if (b) {;;} else {x() ;}").to("if (!b) x();");
  }

  @Test public void DUPLICATED_factorOutAnd() {
    trimming("(a || b) && (a || c)").to("a || b && c");
  }

  @Test public void DUPLICATED_factorOutOr() {
    trimming("a && b || a && c").to("a && (b || c)");
  }

  @Test public void DUPLICATED_factorOutOr3() {
    trimming("a && b && x  && f() || a && c && y ").to("a && (b && x && f() || c && y)");
  }

  @Test public void DUPLICATED_forLoopBug() {
    trimming("" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          $ += 2;\n" + //
        "        else "//
        + "       if (s.charAt(i) == 'd')\n" + //
        "          $ -= 3;\n" + //
        "      return $;\n" + //
        " if (b) i = 3;").to(null);
  }

  @Test public void DUPLICATED_ifBugSecondTry() {
    trimming("" + //
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

  @Test public void DUPLICATED_ifBugSimplified() {
    trimming("" + //
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

  @Test public void DUPLICATED_ifBugWithPlainEmptyElse() {
    trimming("" + //
        "      if (z)\n" + //
        "        f();\n" + //
        "      else\n" + //
        "         ; \n" + //
        "").to("" + //
            "      if (z)\n" + //
            "        f();\n" + //
            "");//
  }

  @Test public void DUPLICATED_ifDegenerateThenInIf() {
    trimming("if (a) if (b) {} else f(); x();")//
        .to(" if (a) if (!b) f(); x();");
  }

  @Test public void DUPLICATED_ifEmptyElsewWithinIf() {
    trimming("if (a) if (b) {;;;f();} else {;}")//
        .to("if(a&&b){;;;f();}");
  }

  @Test public void DUPLICATED_ifEmptyThenThrow() {
    trimming("" //
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

  @Test public void DUPLICATED_ifEmptyThenThrowVariant() {
    trimming("" //
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

  @Test public void DUPLICATED_ifEmptyThenThrowWitinIf() {
    trimming("" //
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

  @Test public void DUPLICATED_ifFunctionCall() {
    trimming("if (x) f(a); else f(b);").to("f(x ? a: b);");
  }

  @Test public void DUPLICATED_ifPlusPlusPost() {
    trimming("if (x) a++; else b++;").to("if(x)++a;else++b;");
  }

  @Test public void DUPLICATED_ifPlusPlusPostExpression() {
    trimming("x? a++:b++").to(null);
  }

  @Test public void DUPLICATED_ifPlusPlusPre() {
    trimming("if (x) ++a; else ++b;").to(null);
  }

  @Test public void DUPLICATED_ifPlusPlusPreExpression() {
    trimming("x? ++a:++b").to(null);
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer0() {
    trimming("if (a) return; break;").to(null);
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer01() {
    trimming("if (a) throw e; break;").to(null);
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer02() {
    trimming("if (a) break; break;").to("break;");
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer03() {
    trimming("if (a) continue; break;").to(null);
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer04() {
    trimming("if (a) break; return;").to("if (!a) return; break;");
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer05() {
    trimming("if (a) {x(); return;} continue;").to(null);
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer06() {
    trimming("if (a) throw e; break;").to(null);
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer07() {
    trimming("if (a) break; throw e;").to("if (!a) throw e; break;");
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer08() {
    trimming("if (a) throw e; continue;").to(null);
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer09() {
    trimming("if (a) break; throw e;").to("if (!a) throw e; break;");
  }

  @Test public void DUPLICATED_ifSequencerNoElseSequencer10() {
    trimming("if (a) continue; return;").to("if (!a) return; continue;");
  }

  @Test public void DUPLICATED_ifSequencerThenSequencer0() {
    trimming("if (a) return 4; else break;").to("if (a) return 4; break;");
  }

  @Test public void DUPLICATED_ifSequencerThenSequencer1() {
    trimming("if (a) break; else return 2;").to("if (!a) return 2; break;");
  }

  @Test public void DUPLICATED_ifSequencerThenSequencer3() {
    trimming("if (a) return 10; else continue;").to("if (a) return 10; continue;");
  }

  @Test public void DUPLICATED_ifSequencerThenSequencer4() {
    trimming("if (a) continue; else return 2;").to("if (!a) return 2; continue;");
  }

  @Test public void DUPLICATED_ifSequencerThenSequencer5() {
    trimming("if (a) throw e; else break;").to("if (a) throw e; break;");
  }

  @Test public void DUPLICATED_ifSequencerThenSequencer6() {
    trimming("if (a) break; else throw e;").to("if (!a) throw e; break;");
  }

  @Test public void DUPLICATED_ifSequencerThenSequencer7() {
    trimming("if (a) throw e; else continue;").to("if (a) throw e; continue;");
  }

  @Test public void DUPLICATED_ifSequencerThenSequencer8() {
    trimming("if (a) break; else throw e;").to("if (!a) throw e; break;");
  }

  @Test public void DUPLICATED_ifThrowNoElseThrow() {
    trimming("" //
        + "if (!(e.getCause() instanceof Error))\n" //
        + "  throw e;\n" //
        + "throw (Error) e.getCause();")//
            .to(" throw !(e.getCause()instanceof Error)?e:(Error)e.getCause();");//
  }

  @Test public void DUPLICATED_ifWithCommonNotInBlock() {
    trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++; g();}").to("for(;;){i++;j++;if(a)f();else g();}");
  }

  @Test public void DUPLICATED_ifWithCommonNotInBlockDegenerate() {
    trimming("for (;;) if (a) {i++; f();} else { i++;j++; }").to("for(;;){i++; if(a)f(); else j++;}");
  }

  @Test public void DUPLICATED_ifWithCommonNotInBlockiLongerElse() {
    trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++;  f(); h();}").to("for(;;){i++;j++; f(); if(!a) h();}");
  }

  @Test public void DUPLICATED_ifWithCommonNotInBlockiLongerThen() {
    trimming("for (;;) if (a) {i++;j++;f();} else { i++;j++; }").to("for(;;){i++;j++; if(a)f();}");
  }

  @Test public void DUPLICATED_ifWithCommonNotInBlockNothingLeft() {
    trimming("for (;;) if (a) {i++;j++;} else { i++;j++; }").to("for(;;){i++;j++;}");
  }

  @Test public void DUPLICATED_infiniteLoopBug1() {
    trimming("static boolean hasAnnotation(final VariableDeclarationFragment f) {\n" + //
        "      return hasAnnotation((VariableDeclarationStatement) f.getParent());\n" + //
        "    }").to(null);
  }

  @Test public void DUPLICATED_infiniteLoopBug2() {
    trimming(" static boolean hasAnnotation(final VariableDeclarationStatement n) {\n" + //
        "      return hasAnnotation(n.modifiers());\n" + //
        "    }").to(" static boolean hasAnnotation(final VariableDeclarationStatement s) {\n" + //
            "      return hasAnnotation(s.modifiers());\n" + //
            "    }");
  }

  @Test public void DUPLICATED_infiniteLoopBug3() {
    trimming("  boolean f(final VariableDeclarationStatement n) {\n" + //
        "      return false;\n" + //
        "    }").to("  boolean f(final VariableDeclarationStatement s) {\n" + //
            "      return false;\n" + //
            "    }");
  }

  @Test public void DUPLICATED_infiniteLoopBug4() {
    trimming("void f(final VariableDeclarationStatement n) {}")//
        .to(" void f(final VariableDeclarationStatement s) { }");
  }

  @Test public void DUPLICATED_inline01() {
    trimming("" + //
        "  public int y() {\n" + //
        "    final Z $ = new Z(6);\n" + //
        "    S.out.println($.j);\n" + //
        "    return $;\n" + //
        "  }\n" + //
        "}\n" + //
        "").to(null);
  }

  @Test public void DUPLICATED_inlineInitializers() {
    trimming("int b,a = 2; return 3 * a * b; ").to("return 3*2*b;");
  }

  @Test public void DUPLICATED_inlineInitializersFirstStep() {
    trimming("int b=4,a = 2; return 3 * a * b; ").to("int a = 2; return 3*a*4;");
  }

  @Test public void DUPLICATED_inlineInitializersSecondStep() {
    trimming("int a = 2; return 3*a*4;").to("return 3 * 2 * 4;");
  }

  @Test public void DUPLICATED_inlineintoNextStatementWithSideEffects() {
    trimming("int a = f(); if (a) g(a); else h(u(a));").to(null);
  }

  @Test public void DUPLICATED_inlineSingleUse07() {
    trimming(
        "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     S.out.println(coes.size()); ")
            .to(null);
  }

  @Test public void DUPLICATED_inlineSingleUseKillingVariable() {
    trimming("int a,b=2; a = b;").to("int a;a=2;");
  }

  @Test public void DUPLICATED_inlineSingleUseKillingVariables() {
    trimming("int $, xi=0, xj=0, yi=0, yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")
        .to(" int $, xj=0, yi=0, yj=0;        if (0>xj==yi>yj)$++;else $--;");
  }

  @Test public void DUPLICATED_inlineSingleUseKillingVariablesSimplified() {
    trimming("int $=1,xi=0,xj=0,yi=0,yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")//
        .to(" int $=1,xj=0,yi=0,yj=0;       if(0>xj==yi>yj)$++;else $--;")//
        .to(" int $=1,yi=0,yj=0;            if(0>0==yi>yj)$++;else $--;") //
        .to(" int $=1,yj=0;                 if(0>0==0>yj)$++;else $--;") //
        .to(" int $=1;                      if(0>0==0>0)$++;else $--;") //
        .to(" int $=1;                      if(0>0==0>0)++$;else--$;") //
    ;
  }

  @Test public void DUPLICATED_inlineSingleUseTrivial() {
    trimming(" int $=1,yj=0;                 if(0>0==yj<0)++$;else--$;") //
        .to("  int $=1;                      if(0>0==0<0)++$;else--$;") //
    ;
  }

  @Test public void DUPLICATED_inlineSingleUseVanilla() {
    trimming("int a = f(); if (a) f();").to("if (f()) f();");
  }

  @Test public void DUPLICATED_inlineSingleUseWithAssignment() {
    trimming("int a = 2; while (true) if (f()) f(a); else a = 2;")//
        .to(null);
  }

  @Test public void DUPLICATED_inlineSingleVariableintoPlusPlus() {
    trimming("int $ = 0;  if (a)  ++$;  else --$;").to(null);
  }

  @Test public void DUPLICATED_inliningWithVariableAssignedTo() {
    trimming("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
    ;
  }

  @Test public void DUPLICATED_isGreaterTrue() {
    final InfixExpression e = i("f(a,b,c,d,e) * f(a,b,c)");
    that("" + step.right(e), is("f(a,b,c)"));
    that("" + step.left(e), is("f(a,b,c,d,e)"));
    final Wring<InfixExpression> s = Toolbox.instance.find(e);
    that(s, instanceOf(InfixMultiplicationSort.class));
    that(s, notNullValue());
    that(s.scopeIncludes(e), is(true));
    final Expression e1 = step.left(e);
    final Expression e2 = step.right(e);
    that(has.nulls(e1, e2), is(false));
    final boolean tokenWiseGreater = nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD;
    that(tokenWiseGreater, is(true));
    that(ExpressionComparator.moreArguments(e1, e2), is(true));
    that(ExpressionComparator.longerFirst(e), is(true));
    that(s.eligible(e), is(true));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    that(replacement, notNullValue());
    that("" + replacement, is("f(a,b,c) * f(a,b,c,d,e)"));
  }

  @Test public void DUPLICATED_isGreaterTrueButAlmostNot() {
    final InfixExpression e = i("f(a,b,c,d) * f(a,b,c)");
    that("" + step.right(e), is("f(a,b,c)"));
    that("" + step.left(e), is("f(a,b,c,d)"));
    final Wring<InfixExpression> s = Toolbox.instance.find(e);
    that(s, instanceOf(InfixMultiplicationSort.class));
    that(s, notNullValue());
    that(s.scopeIncludes(e), is(true));
    final Expression e1 = step.left(e);
    final Expression e2 = step.right(e);
    that(has.nulls(e1, e2), is(false));
    final boolean tokenWiseGreater = nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD;
    that(tokenWiseGreater, is(false));
    that(ExpressionComparator.moreArguments(e1, e2), is(true));
    that(ExpressionComparator.longerFirst(e), is(true));
    that(s.eligible(e), is(true));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    that(replacement, notNullValue());
    that("" + replacement, is("f(a,b,c) * f(a,b,c,d)"));
  }

  @Test public void DUPLICATED_issue06() {
    trimming("a*-b").to("-a * b");
  }

  @Test public void DUPLICATED_issue06B() {
    trimming("x/a*-b/-c*- - - d / -d")//
        .to("x/a * b/ c * d/d")//
        .to("d*x/a*b/c/d");
  }

  @Test public void DUPLICATED_issue06C4() {
    trimming("-a * b/ c ").to(null);
  }

  @Test public void DUPLICATED_issue06D() {
    trimming("a*b*c*d*-e").to("-a*b*c*d*e").to(null);
  }

  @Test public void DUPLICATED_issue06E() {
    trimming("-a*b*c*d*f*g*h*i*j*k").to(null);
  }

  @Test public void DUPLICATED_issue06F() {
    trimming("x*a*-b*-c*- - - d * d")//
        .to("-x*a*b*c*d*d")//
        .to(null);
  }

  @Test public void DUPLICATED_issue06G() {
    trimming("x*a*-b*-c*- - - d / d")//
        .to("-x*a*b*c*d/d")//
        .to(null);
  }

  @Test public void DUPLICATED_issue06H() {
    trimming("x/a*-b/-c*- - - d ")//
        .to("-x/a * b/ c * d")//
    ;
  }

  @Test public void DUPLICATED_issue06I() {
    trimming("41 * - 19")//
        .to("-779 ") //
    ;
  }

  @Test public void DUPLICATED_issue06J() {
    trimming("41 * a * - 19")//
        .to("-41*a*19")//
        .to("-41*19*a") //
    ;
  }

  @Test public void issue21a() {
    trimming("a.equals(\"a\")").to("\"a\".equals(a)");
  }

  @Test public void issue21b() {
    trimming("a.equals(\"ab\")").to("\"ab\".equals(a)");
  }

  @Test public void issue21d() {
    trimming("a.equalsIgnoreCase(\"a\")").to("\"a\".equalsIgnoreCase(a)");
  }

  @Test public void issue21e() {
    trimming("a.equalsIgnoreCase(\"ab\")").to("\"ab\".equalsIgnoreCase(a)");
  }

  @Test public void DUPLICATED_issue37Simplified() {
    trimming("" + //
        "    int a = 3;\n" + //
        "    a = 31 * a;" + //
        "").to("int a = 31 * 3; ");
  }

  @Test public void DUPLICATED_issue37SimplifiedVariant() {
    trimming("" + //
        "    int a = 3;\n" + //
        "    a += 31 * a;").to("int a=3+31*3;");
  }

  @Test public void DUPLICATED_issue37WithSimplifiedBlock() {
    trimming("if (a) { {} ; if (b) f(); {} } else { g(); f(); ++i; ++j; }")//
        .to(" if (a) {  if (b) f(); } else { g(); f(); ++i; ++j; }");
  }

  @Test public void DUPLICATED_issue38() {
    trimming("    return o == null ? null\n" + //
        "        : o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
        "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
        "                : null;").to(null);
  }

  @Test public void DUPLICATED_issue38Simplfiied() {
    trimming(//
        "         o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
            "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
            "                : null").to(null);
  }

  @Test public void DUPLICATED_issue39base() {
    trimming("" + //
        "if (name == null) {\n" + //
        "    if (other.name != null)\n" + //
        "        return false;\n" + //
        "} else if (!name.equals(other.name))\n" + //
        "    return false;\n" + //
        "return true;").to(null);
  }

  public void DUPLICATED_issue39baseDual() {
    trimming("if (name != null) {\n" + //
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

  @Test(timeout = 100) public void DUPLICATED_issue39versionA() {
    trimming("" + //
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

  public void DUPLICATED_issue39versionAdual() {
    trimming("" + //
        "if (!varArgs) {\n" + //
        "    if (parameterTypes.length != argumentTypes.length) {\n" + //
        "        return false;\n" + //
        "    }\n" + //
        "} else if (argumentTypes.length < parameterTypes.length - 1) {\n" + //
        "    return false;\n" + //
        "}" + //
        "").to(null);
  }

  @Test public void DUPLICATED_issue41FunctionCall() {
    trimming("int a = f();a += 2;").to("int a = f()+2;");
  }

  @Test public void DUPLICATED_issue43() {
    trimming("" //
        + "String t = Z2;  "//
        + " t = t.f(A).f(b) + t.f(c);   "//
        + "return (t + 3);    ")
            .to(""//
                + "String t = Z2.f(A).f(b) + Z2.f(c);" //
                + "return (t + 3);" //
                + "");
  }

  @Test public void DUPLICATED_issue46() {
    trimming("" + //
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

  @Test public void DUPLICATED_issue49() {
    trimming("int f() { int f = 0; for (int i: X) $ += f(i); return f;}")//
        .to("int f(){int $=0;for(int i:X)$+=f(i);return $;}");
  }

  @Test public void issue51() {
    trimming("int f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .to("int f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void DUPLICATED_issue52A() {
    trimming("void m() { return; }").to("void m() {}");
  }

  @Test public void DUPLICATED_issue52A1() {
    trimming("void m() { return a; }").to(null);
  }

  @Test public void DUPLICATED_issue52B1() {
    trimming("void m() { if (a) { f(); return; }}").to("void m() { if (a) { f(); ; }}");
  }

  @Test public void DUPLICATED_issue52B2() {
    trimming("void m() { if (a) ++i; else { f(); return; }").to("void m() { if (a) ++i; else { f(); ; }");
  }

  @Test public void DUPLICATED_issue53() {
    trimming("int[] is = f(); for (int i: is) f(i);")//
        .to("for (int i: f()) f(i);");
  }

  @Test public void DUPLICATED_issue54DoNonSideEffect() {
    trimming("int a  = f; do { b[i] = a; } while (b[i] != a);")//
        .to("do { b[i] = f; } while (b[i] != f);");
  }

  @Test public void DUPLICATED_issue54DoNonSideEffectEmptyBody() {
    trimming("int a = f(); do ; while (a != 1);")//
        .to(null);
  }

  @Test public void DUPLICATED_issue54DoWhile() {
    trimming("int a  = f(); do { b[i] = 2; ++i; } while (b[i] != a);")//
        .to(null);
  }

  @Test public void DUPLICATED_issue54DoWithBlock() {
    trimming("int a  = f(); do { b[i] = a;  ++i; } while (b[i] != a);")//
        .to(null);
  }

  @Test public void DUPLICATED_issue54doWithoutBlock() {
    trimming("int a  = f(); do b[i] = a; while (b[i] != a);")//
        .to(null);
  }

  @Test public void DUPLICATED_issue54ForEnhanced() {
    trimming("int a  = f(); for (int i: a) b[i] = x;")//
        .to(" for (int i: f()) b[i] = x;");
  }

  @Test public void DUPLICATED_issue54ForEnhancedNonSideEffectLoopHeader() {
    trimming("int a  = f; for (int i: a) b[i] = b[i-1];")//
        .to("for (int i: f) b[i] = b[i-1];");
  }

  @Test public void DUPLICATED_issue54ForEnhancedNonSideEffectWithBody() {
    trimming("int a  = f; for (int i: j) b[i] = a;")//
        .to(" for(int i:j)b[i]=f; ");
  }

  @Test public void DUPLICATED_issue54ForPlainNonSideEffect() {
    trimming("int a  = f; for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .to("for (int i = 0; i < 100;  ++i) b[i] = f;");
  }

  @Test public void DUPLICATED_issue54ForPlainUseInConditionNonSideEffect() {
    trimming("int a  = f; for (int i = 0; a < 100;  ++i) b[i] = 3;")//
        .to("for (int i = 0; f < 100;  ++i) b[i] = 3;");
  }

  @Test public void DUPLICATED_issue54ForPlainUseInInitializerNonSideEffect() {
    trimming("int a  = f; for (int i = a; i < 100; i *= a) b[i] = 3;")//
        .to(" for (int i = f; i < 100; i *= f) b[i] = 3;");
  }

  @Test public void DUPLICATED_issue54ForPlainUseInUpdatersNonSideEffect() {
    trimming("int a  = f; for (int i = 0; i < 100; i *= a) b[i] = 3;")//
        .to("for (int i = 0; i < 100; i *= f) b[i] = 3;");
  }

  @Test public void DUPLICATED_issue54While() {
    trimming("int a  = f(); while (c) b[i] = a;")//
        .to(null);
  }

  @Test public void DUPLICATED_issue54WhileNonSideEffect() {
    trimming("int a  = f; while (c) b[i] = a;")//
        .to("while (c) b[i] = f;");
  }

  @Test public void DUPLICATED_issue54WhileScopeDoesNotInclude() {
    included("int a  = f(); while (c) b[i] = a;", VariableDeclarationFragment.class)//
        .notIn(new DeclarationInitializerStatementTerminatingScope());
  }

  @Test public void DUPLICATED_issue57a() {
    trimming("void m(List<Expression>... expressions) { }").to("void m(List<Expression>... xss) {}");
  }

  @Test public void DUPLICATED_issue57b() {
    trimming("void m(Expression... expression) { }").to("void m(Expression... xs) {}");
  }

  @Test public void DUPLICATED_issue58a() {
    trimming("X f(List<List<Expression>> expressions){}").to("X f(List<List<Expression>> xss){}");
  }

  @Test public void DUPLICATED_issue58b() {
    trimming("X f(List<Expression>[] expressions){}").to("X f(List<Expression>[] xss){}");
  }

  @Test public void DUPLICATED_issue58c() {
    trimming("X f(List<Expression>[] expressions){}").to("X f(List<Expression>[] xss){}");
  }

  @Test public void DUPLICATED_issue58d() {
    trimming("X f(List<Expression>... expressions){}").to("X f(List<Expression>... xss){}");
  }

  @Test public void DUPLICATED_issue58e() {
    trimming("X f(Expression[]... expressions){}").to("X f(Expression[]... xss){}");
  }

  @Test public void DUPLICATED_issue58f() {
    trimming("X f(Expression[][]... expressions){}").to("X f(Expression[][]... xsss){}");
  }

  @Test public void DUPLICATED_issue58g() {
    trimming("X f(List<Expression[][]>... expressions){}").to("X f(List<Expression[][]>... xssss){}");
  }

  @Test public void DUPLICATED_issue62a() {
    trimming("int f(int i) { for(;;++i) if(false) break; return i; }")//
        .to("int f(int i) { for(;;++i) {} return i; }")//
        .to(null);
  }

  @Test public void DUPLICATED_issue62c() {
    trimming("int f(int i) { while(++i > 999) if(i>99) break; return i;}").to(null);
  }

  @Test public void DUPLICATED_issue64a() {
    trimming("void f() {" + //
        "    final int a = f();\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").to(null);
  }

  @Test public void DUPLICATED_issue64b() {
    trimming("void f() {" + //
        "    final int a = 3;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").to(null);
  }

  @Test public void issue64c() {
    trimming("void f(int x) {" + //
        "    ++x;\n" + //
        "    final int a = x;\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };" + "}").to(null);
  }

  @Test public void DUPLICATED_issue73a() {
    trimming("void foo(StringBuilder sb) {}").to("void foo(StringBuilder b) {}");
  }

  @Test public void DUPLICATED_issue73b() {
    trimming("void foo(DataOutput dataOutput) {}").to("void foo(DataOutput o) {}");
  }

  @Test public void DUPLICATED_issue73c() {
    trimming("void foo(Integer integer, ASTNode astn) {}").to("void foo(Integer i, ASTNode astn) {}");
  }

  @Test public void DUPLICATED_linearTransformation() {
    trimming("plain * the + kludge").to("the*plain+kludge");
  }

  @Test public void DUPLICATED_literalVsLiteral() {
    trimming("1 < 102333").to(null);
  }

  @Test public void DUPLICATED_longChainComparison() {
    trimming("a == b == c == d").to(null);
  }

  @Test public void DUPLICATED_longChainParenthesisComparison() {
    trimming("(a == b == c) == d").to(null);
  }

  @Test public void DUPLICATED_longChainParenthesisNotComparison() {
    trimming("(a == b == c) != d").to(null);
  }

  @Test public void DUPLICATED_longerChainParenthesisComparison() {
    trimming("(a == b == c == d == e) == d").to(null);
  }

  @Test public void DUPLICATED_massiveInlining() {
    trimming("int a,b,c;String t = zE4;if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;")//
        .to("int a,b,c;if(2*3.1415*180>a||zE4.concat(sS)==1922&&zE4.length()>3)return c>5;") //
        .to("int a,b,c;if(1130.94>a||zE4.concat(sS)==1922&&zE4.length()>3)return c>5;");
  }

  @Test public void DUPLICATED_methodWithLastIf() {
    trimming("int f() { if (a) { f(); g(); h();}").to("int f() { if (!a) return;  f(); g(); h();");
  }

  @Test public void DUPLICATED_nestedIf1() {
    trimming("if (a) if (b) i++;").to("if (a && b) i++;");
  }

  @Test public void DUPLICATED_nestedIf2() {
    trimming("if (a) if (b) i++; else ; else ; ").to("if (a && b) i++; else ;");
  }

  @Test public void DUPLICATED_nestedIf3() {
    trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else;else{++y;f();g();z();}");
  }

  @Test public void DUPLICATED_nestedIf33() {
    trimming("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .to(" if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}")//
        .to(" if(x){if(a&&b)++i;}else{++y;f();g();}")//
    ;
  }

  @Test public void DUPLICATED_nestedIf33a() {
    trimming("if (x) { if (a && b) i++; } else { y++; f(); g(); }")//
        .to(" if (x) {if(a&&b)++i;} else{++y;f();g();}");
  }

  @Test public void DUPLICATED_nestedIf33b() {
    trimming("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .to("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void DUPLICATED_nestedIf3c() {
    trimming("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .to(" if(x) {if(a&&b)i++;} else {++y;f();g();}");
  }

  @Test public void DUPLICATED_nestedIf3d() {
    trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else; else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)i++;} else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)++i;} else{++y;f();g();z();}") //
    ;
  }

  @Test public void DUPLICATED_nestedIf3e() {
    trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to(" if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }

  @Test public void DUPLICATED_nestedIf3f() {
    trimming("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .to(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void DUPLICATED_nestedIf3f1() {
    trimming(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .to(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void DUPLICATED_nestedIf3x() {
    trimming("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .to("if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .to("if(x){if(a&&b)i++;}else{++y;f();g();z();}") //
    ;
  }

  @Test public void DUPLICATED_nestedTernaryAlignment() {
    trimming("int b=3==4?5==3?2:3:5==3?2:3*3;").to("int b=3==4?5==3?2:3:5!=3?3*3:2;");
  }

  @Test public void DUPLICATED_noChange0() {
    trimming("kludge + the * plain ").to(null);
  }

  @Test public void DUPLICATED_noChange1() {
    trimming("the * plain").to(null);
  }

  @Test public void DUPLICATED_noChange2() {
    trimming("plain + kludge").to(null);
  }

  @Test public void DUPLICATED_noinliningintoSynchronizedStatement() {
    trimming("int a  = f(); synchronized(this) { int b = a; }")//
        .to(null);
  }

  @Test public void DUPLICATED_noinliningintoSynchronizedStatementEvenWithoutSideEffect() {
    trimming("int a  = f; synchronized(this) { int b = a; }")//
        .to(null);
  }

  @Test public void DUPLICATED_noinliningintoTryStatement() {
    trimming("int a  = f(); try { int b = a; } catch (Exception E) {}")//
        .to(null);
  }

  @Test public void DUPLICATED_noinliningintoTryStatementEvenWithoutSideEffect() {
    trimming("int a  = f; try { int b = a; } catch (Exception E) {}")//
        .to(null);
  }

  @Test public void DUPLICATED_notOfAnd() {
    trimming("!(A && B)").to("!A || !B");
  }

  @Test public void DUPLICATED_oneMultiplication() {
    trimming("f(a,b,c,d) * f(a,b,c)").to("f(a,b,c) * f(a,b,c,d)");
  }

  @Test public void DUPLICATED_oneMultiplicationAlternate() {
    trimming("f(a,b,c,d,e) * f(a,b,c)").to("f(a,b,c) * f(a,b,c,d,e)");
  }

  @Test public void DUPLICATED_orFalse3ORTRUE() {
    trimming("false || false || false").to("false");
  }

  @Test public void DUPLICATED_orFalse4ORTRUE() {
    trimming("false || false || false || false").to("false");
  }

  @Test public void DUPLICATED_orFalseANDOf3WithoutBoolean() {
    trimming("a && b && false").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf3WithoutBooleanA() {
    trimming("x && a && b").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf3WithTrue() {
    trimming("true && x && true && a && b").to("x && a && b");
  }

  @Test public void DUPLICATED_orFalseANDOf3WithTrueA() {
    trimming("a && b && true").to("a && b");
  }

  @Test public void DUPLICATED_orFalseANDOf4WithoutBoolean() {
    trimming("a && b && c && false").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf4WithoutBooleanA() {
    trimming("x && a && b && c").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf4WithTrue() {
    trimming("x && true && a && b && c").to("x && a && b && c");
  }

  @Test public void DUPLICATED_orFalseANDOf4WithTrueA() {
    trimming("a && b && c && true").to("a && b && c");
  }

  @Test public void DUPLICATED_orFalseANDOf5WithoutBoolean() {
    trimming("false && a && b && c && d").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf5WithoutBooleanA() {
    trimming("x && a && b && c && d").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf5WithTrue() {
    trimming("x && a && b && c && true && true && true && d").to("x && a && b && c && d");
  }

  @Test public void DUPLICATED_orFalseANDOf5WithTrueA() {
    trimming("true && a && b && c && d").to("a && b && c && d");
  }

  @Test public void DUPLICATED_orFalseANDOf6WithoutBoolean() {
    trimming("a && b && c && false && d && e").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf6WithoutBooleanA() {
    trimming("x && a && b && c && d && e").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf6WithoutBooleanWithParenthesis() {
    trimming("(x && (a && b)) && (c && (d && e))").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf6WithTrue() {
    trimming("x && a && true && b && c && d && e").to("x && a && b && c && d && e");
  }

  @Test public void DUPLICATED_orFalseANDOf6WithTrueA() {
    trimming("a && b && c && true && d && e").to("a && b && c && d && e");
  }

  @Test public void DUPLICATED_orFalseANDOf6WithTrueWithParenthesis() {
    trimming("x && (true && (a && b && true)) && (c && (d && e))").to("x && a && b && c && d && e");
  }

  @Test public void DUPLICATED_orFalseANDOf7WithMultipleTrueValue() {
    trimming("(a && (b && true)) && (c && (d && (e && (true && true))))").to("a &&b &&c &&d &&e ");
  }

  @Test public void DUPLICATED_orFalseANDOf7WithoutBooleanAndMultipleFalseValue() {
    trimming("(a && (b && false)) && (c && (d && (e && (false && false))))").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf7WithoutBooleanWithParenthesis() {
    trimming("(a && b) && (c && (d && (e && false)))").to(null);
  }

  @Test public void DUPLICATED_orFalseANDOf7WithTrueWithParenthesis() {
    trimming("true && (a && b) && (c && (d && (e && true)))").to("a &&b &&c &&d &&e ");
  }

  @Test public void DUPLICATED_orFalseANDWithFalse() {
    trimming("b && a").to(null);
  }

  @Test public void DUPLICATED_orFalseANDWithoutBoolean() {
    trimming("b && a").to(null);
  }

  @Test public void DUPLICATED_orFalseANDWithTrue() {
    trimming("true && b && a").to("b && a");
  }

  @Test public void DUPLICATED_orFalseFalseOrFalse() {
    trimming("false ||false").to("false");
  }

  @Test public void DUPLICATED_orFalseORFalseWithSomething() {
    trimming("true || a").to(null);
  }

  @Test public void DUPLICATED_orFalseORFalseWithSomethingB() {
    trimming("false || a || false").to("a");
  }

  @Test public void DUPLICATED_orFalseOROf3WithFalse() {
    trimming("x || false || b").to("x || b");
  }

  @Test public void DUPLICATED_orFalseOROf3WithFalseB() {
    trimming("false || a || b || false").to("a || b");
  }

  @Test public void DUPLICATED_orFalseOROf3WithoutBoolean() {
    trimming("a || b").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf3WithoutBooleanA() {
    trimming("x || a || b").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf4WithFalse() {
    trimming("x || a || b || c || false").to("x || a || b || c");
  }

  @Test public void DUPLICATED_orFalseOROf4WithFalseB() {
    trimming("a || b || false || c").to("a || b || c");
  }

  @Test public void DUPLICATED_orFalseOROf4WithoutBoolean() {
    trimming("a || b || c").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf4WithoutBooleanA() {
    trimming("x || a || b || c").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf5WithFalse() {
    trimming("x || a || false || c || d").to("x || a || c || d");
  }

  @Test public void DUPLICATED_orFalseOROf5WithFalseB() {
    trimming("a || b || c || d || false").to("a || b || c || d");
  }

  @Test public void DUPLICATED_orFalseOROf5WithoutBoolean() {
    trimming("a || b || c || d").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf5WithoutBooleanA() {
    trimming("x || a || b || c || d").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf6WithFalse() {
    trimming("false || x || a || b || c || d || e").to("x || a || b || c || d || e");
  }

  @Test public void DUPLICATED_orFalseOROf6WithFalseWithParenthesis() {
    trimming("x || (a || (false) || b) || (c || (d || e))").to("x || a || b || c || d || e");
  }

  @Test public void DUPLICATED_orFalseOROf6WithFalseWithParenthesisB() {
    trimming("(a || b) || false || (c || false || (d || e || false))").to("a || b || c || d || e");
  }

  @Test public void DUPLICATED_orFalseOROf6WithoutBoolean() {
    trimming("a || b || c || d || e").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf6WithoutBooleanA() {
    trimming("x || a || b || c || d || e").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf6WithoutBooleanWithParenthesis() {
    trimming("(a || b) || (c || (d || e))").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf6WithoutBooleanWithParenthesisA() {
    trimming("x || (a || b) || (c || (d || e))").to(null);
  }

  @Test public void DUPLICATED_orFalseOROf6WithTwoFalse() {
    trimming("a || false || b || false || c || d || e").to("a || b || c || d || e");
  }

  @Test public void DUPLICATED_orFalseORSomethingWithFalse() {
    trimming("false || a || false").to("a");
  }

  @Test public void DUPLICATED_orFalseORSomethingWithTrue() {
    trimming("a || true").to(null);
  }

  @Test public void DUPLICATED_orFalseORWithoutBoolean() {
    trimming("b || a").to(null);
  }

  @Test public void DUPLICATED_orFalseProductIsNotANDDivOR() {
    trimming("2*a").to(null);
  }

  @Test public void DUPLICATED_orFalseTrueAndTrueA() {
    trimming("true && true").to("true");
  }

  @Test public void DUPLICATED_overridenDeclaration() {
    trimming("int a = 3; a = f() ? 3 : 4;").to("int a = f() ? 3: 4;");
  }

  @Test public void DUPLICATED_paramAbbreviateBasic1() {
    trimming("void m(XMLDocument xmlDocument) {" + //
        "xmlDocument.exec(p);}").to("void m(XMLDocument d) {" + //
            "d.exec(p);}");
  }

  @Test public void DUPLICATED_paramAbbreviateBasic2() {
    trimming("int m(StringBuilder builder) {" + //
        "if(builder.exec())" + //
        "builder.clear();").to("int m(StringBuilder b) {" + //
            "if(b.exec())" + //
            "b.clear();");
  }

  @Test public void DUPLICATED_paramAbbreviateCollision() {
    trimming("void m(Expression exp, Expression expresssion) { }").to("void m(Expression x, Expression expresssion) { }");
  }

  @Test public void DUPLICATED_paramAbbreviateConflictingWithLocal1() {
    trimming("void m(String string) {" + //
        "String s = null;" + //
        "string.substring(s, 2, 18);}").to("void m(String string){string.substring(null,2,18);}");
  }

  @Test public void DUPLICATED_paramAbbreviateConflictingWithLocal1Simplified() {
    trimming("void m(String string) {" + //
        "String s = X;" + //
        "string.substring(s, 2, 18);}").to("void m(String string){string.substring(X,2,18);}");
  }

  @Test public void DUPLICATED_paramAbbreviateConflictingWithLocal1SimplifiedFurther() {
    trimming("void m(String string) {" + //
        "String s = X;" + //
        "string.f(s);}").to("void m(String string){string.f(X);}");
  }

  @Test public void DUPLICATED_paramAbbreviateConflictingWithLocal2() {
    trimming("TCPConnection conn(TCPConnection tcpCon) {" + //
        " UDPConnection c = new UDPConnection(57);" + //
        " if(tcpCon.isConnected()) " + //
        "   c.disconnect();}").to("TCPConnection conn(TCPConnection tcpCon){" //
            + " if(tcpCon.isConnected())" //
            + "   (new UDPConnection(57)).disconnect();"//
            + "}");
  }

  @Test public void DUPLICATED_paramAbbreviateConflictingWithMethodName() {
    trimming("void m(BitmapManipulator bitmapManipulator) {" + //
        "bitmapManipulator.x().y();").to(null);
  }

  @Test public void DUPLICATED_paramAbbreviateMultiple() {
    trimming("void m(StringBuilder stringBuilder, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
        "stringBuilder.clear();" + //
        "xmlDocument.open(stringBuilder.toString());" + //
        "dog.eat(xmlDocument.asEdible(cat));}").to("void m(StringBuilder b, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
            "b.clear();" + //
            "xmlDocument.open(b.toString());" + //
            "dog.eat(xmlDocument.asEdible(cat));}");
  }

  @Test public void DUPLICATED_paramAbbreviateNestedMethod() {
    trimming("void f(Iterator iterator) {" + //
        "iterator = new Iterator<Object>() {" + //
        "int i = 0;" + //
        "@Override public boolean hasNext() { return false; }" + //
        "@Override public Object next() { return null; } };").to("void f(Iterator i) {" + //
            "i = new Iterator<Object>() {" + //
            "int i = 0;" + //
            "@Override public boolean hasNext() { return false; }" + //
            "@Override public Object next() { return null; } };");
  }

  @Test public void DUPLICATED_parenthesizeOfpushdownTernary() {
    trimming("a ? b+x+e+f:b+y+e+f").to("b+(a ? x : y)+e+f");
  }

  @Test public void DUPLICATED_postDecreementReturn() {
    trimming("a--; return a;").to("--a;return a;");
  }

  @Test public void DUPLICATED_postDecremntInFunctionCall() {
    trimming("f(a++, i--, b++, ++b);").to(null);
  }

  @Test public void DUPLICATED_postfixToPrefixAvoidChangeOnLoopCondition() {
    trimming("for (int s = i; ++i; ++s);").to(null);
  }

  @Test public void DUPLICATED_postfixToPrefixAvoidChangeOnLoopInitializer() {
    trimming("for (int s = i++; i < 10; ++s);").to(null);
  }

  @Test public void DUPLICATED_postfixToPrefixAvoidChangeOnVariableDeclaration() {
    // We expect to print 2, but ++s will make it print 3
    trimming("int s = 2;" + //
        "int n = s++;" + //
        "S.out.print(n);").to("int s=2;S.out.print(s++);");
  }

  @Test public void DUPLICATED_postIncrementInFunctionCall() {
    trimming("f(i++);").to(null);
  }

  @Test public void DUPLICATED_postIncrementReturn() {
    trimming("a++; return a;").to("++a;return a;");
  }

  @Test public void DUPLICATED_preDecreementReturn() {
    trimming("--a.b.c; return a.b.c;").to("return--a.b.c;");
  }

  @Test public void DUPLICATED_preDecrementReturn() {
    trimming("--a; return a;").to("return --a;");
  }

  @Test public void DUPLICATED_preDecrementReturn1() {
    trimming("--this.a; return this.a;").to("return --this.a;");
  }

  @Test public void DUPLICATED_prefixToPosfixIncreementSimple() {
    trimming("i++").to("++i");
  }

  @Test public void DUPLICATED_prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    that(s, iz("{" + from + "}"));
    that(s, notNullValue());
    final PostfixExpression e = extract.findFirstPostfix(s);
    that(e, notNullValue());
    that(e, iz("i--"));
    final ASTNode parent = e.getParent();
    that(parent, notNullValue());
    that(parent, iz(from));
    that(parent, is(not(instanceOf(Expression.class))));
    that(new PostfixToPrefix().scopeIncludes(e), is(true));
    that(new PostfixToPrefix().eligible(e), is(true));
    final Expression r = new PostfixToPrefix().replacement(e);
    that(r, iz("--i"));
    trimming(from).to("for(int i=0;i<100;--i)--i;");
  }

  @Test public void DUPLICATED_prefixToPostfixIncreement() {
    trimming("for (int i = 0; i < 100; i++) i++;").to("for(int i=0;i<100;++i)++i;");
  }

  @Test public void DUPLICATED_preIncrementReturn() {
    trimming("++a; return a;").to("return ++a;");
  }

  @Test public void DUPLICATED_pushdowConditionalActualExampleFirstPass() {
    trimming("" //
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

  @Test public void DUPLICATED_pushdowConditionalActualExampleSecondtest() {
    trimming("" //
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

  @Test public void DUPLICATED_pushdownNot2LevelNotOfFalse() {
    trimming("!!false").to("false");
  }

  @Test public void DUPLICATED_pushdownNot2LevelNotOfTrue() {
    trimming("!!true").to("true");
  }

  @Test public void DUPLICATED_pushdownNotActualExample() {
    trimming("!inRange(m, e)").to(null);
  }

  @Test public void DUPLICATED_pushdownNotDoubleNot() {
    trimming("!!f()").to("f()");
  }

  @Test public void DUPLICATED_pushdownNotDoubleNotDeeplyNested() {
    trimming("!(((!f())))").to("f()");
  }

  @Test public void DUPLICATED_pushdownNotDoubleNotNested() {
    trimming("!(!f())").to("f()");
  }

  @Test public void DUPLICATED_pushdownNotEND() {
    trimming("a&&b").to(null);
  }

  @Test public void DUPLICATED_pushdownNotMultiplication() {
    trimming("a*b").to(null);
  }

  @Test public void DUPLICATED_pushdownNotNotOfAND() {
    trimming("!(a && b && c)").to("!a || !b || !c");
  }

  @Test public void DUPLICATED_pushdownNotNotOfAND2() {
    trimming("!(f() && f(5))").to("!f() || !f(5)");
  }

  @Test public void DUPLICATED_pushdownNotNotOfANDNested() {
    trimming("!(f() && (f(5)))").to("!f() || !f(5)");
  }

  @Test public void DUPLICATED_pushdownNotNotOfEQ() {
    trimming("!(3 == 5)").to("3 != 5");
  }

  @Test public void DUPLICATED_pushdownNotNotOfEQNested() {
    trimming("!((((3 == 5))))").to("3 != 5");
  }

  @Test public void DUPLICATED_pushdownNotNotOfFalse() {
    trimming("!false").to("true");
  }

  @Test public void DUPLICATED_pushdownNotNotOfGE() {
    trimming("!(3 >= 5)").to("3 < 5");
  }

  @Test public void DUPLICATED_pushdownNotNotOfGT() {
    trimming("!(3 > 5)").to("3 <= 5");
  }

  @Test public void DUPLICATED_pushdownNotNotOfLE() {
    trimming("!(3 <= 5)").to("3 > 5");
  }

  @Test public void DUPLICATED_pushdownNotNotOfLT() {
    trimming("!(3 < 5)").to("3 >= 5");
  }

  @Test public void DUPLICATED_pushdownNotNotOfNE() {
    trimming("!(3 != 5)").to("3 == 5");
  }

  @Test public void DUPLICATED_pushdownNotNotOfOR() {
    trimming("!(a || b || c)").to("!a && !b && !c");
  }

  @Test public void DUPLICATED_pushdownNotNotOfOR2() {
    trimming("!(f() || f(5))").to("!f() && !f(5)");
  }

  @Test public void DUPLICATED_pushdownNotNotOfTrue() {
    trimming("!true").to("false");
  }

  @Test public void DUPLICATED_pushdownNotNotOfTrue2() {
    trimming("!!true").to("true");
  }

  @Test public void DUPLICATED_pushdownNotNotOfWrappedOR() {
    trimming("!((a) || b || c)").to("!a && !b && !c");
  }

  @Test public void DUPLICATED_pushdownNotOR() {
    trimming("a||b").to(null);
  }

  @Test public void DUPLICATED_pushdownNotSimpleNot() {
    trimming("!a").to(null);
  }

  @Test public void DUPLICATED_pushdownNotSimpleNotOfFunction() {
    trimming("!f(a)").to(null);
  }

  @Test public void DUPLICATED_pushdownNotSummation() {
    trimming("a+b").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryActualExample() {
    trimming("next < values().length").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryActualExample2() {
    trimming("!inRange(m, e) ? true : inner.go(r, e)").to("!inRange(m, e) || inner.go(r, e)");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdentical2Addition() {
    trimming("a ? b+d :b+ c").to("b+(a ? d : c)");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdentical3Addition() {
    trimming("a ? b+d +x:b+ c + x").to("b+(a ? d : c) + x");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdentical4AdditionLast() {
    trimming("a ? b+d+e+y:b+d+e+x").to("b+d+e+(a ? y : x)");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdentical4AdditionSecond() {
    trimming("a ? b+x+e+f:b+y+e+f").to("b+(a ? x : y)+e+f");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdenticalAssignment() {
    trimming("a ? (b=c) :(b=d)").to("b = a ? c : d");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdenticalFunctionCall() {
    trimming("a ? f(b) :f(c)").to("f(a ? b : c)");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdenticalMethodCall() {
    trimming("a ? y.f(b) :y.f(c)").to("y.f(a ? b : c)");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall1Div2() {
    trimming("a ? f(b,x) :f(c,x)").to("f(a ? b : c,x)");
  }

  @Test public void DUPLICATED_pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall2Div2() {
    trimming("a ? f(x,b) :f(x,c)").to("f(x,a ? b : c)");
  }

  @Test public void DUPLICATED_pushdownTernaryAMethodCallDistinctReceiver() {
    trimming("a ? x.f(c) : y.f(d)").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryDifferentTargetFieldRefernce() {
    trimming("a ? 1 + x.a : 1 + y.a").to("1+(a ? x.a : y.a)");
  }

  @Test public void DUPLICATED_pushdownTernaryFieldReferneceShort() {
    trimming("a ? R.b.c : R.b.d").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryFunctionCall() {
    trimming("a ? f(b,c) : f(c)").to("!a?f(c):f(b,c)");
  }

  @Test public void DUPLICATED_pushdownTernaryFX() {
    trimming("a ? false : c").to("!a && c");
  }

  @Test public void DUPLICATED_pushdownTernaryIdenticalAddition() {
    trimming("a ? b+d :b+ d").to("b+d");
  }

  @Test public void DUPLICATED_pushdownTernaryIdenticalAdditionWtihParenthesis() {
    trimming("a ? (b+d) :(b+ d)").to("b+d");
  }

  @Test public void DUPLICATED_pushdownTernaryIdenticalAssignment() {
    trimming("a ? (b=c) :(b=c)").to("b = c");
  }

  @Test public void DUPLICATED_pushdownTernaryIdenticalAssignmentVariant() {
    trimming("a ? (b=c) :(b=d)").to("b=a?c:d");
  }

  @Test public void DUPLICATED_pushdownTernaryIdenticalFunctionCall() {
    trimming("a ? f(b) :f(b)").to("f(b)");
  }

  @Test public void DUPLICATED_pushdownTernaryIdenticalIncrement() {
    trimming("a ? b++ :b++").to("b++");
  }

  @Test public void DUPLICATED_pushdownTernaryIdenticalMethodCall() {
    trimming("a ? y.f(b) :y.f(b)").to("y.f(b)");
  }

  @Test public void DUPLICATED_pushdownTernaryintoConstructor1Div1Location() {
    trimming("a.equal(b) ? new S(new Integer(4)) : new S(new Ineger(3))").to("new S(a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void DUPLICATED_pushdownTernaryintoConstructor1Div3() {
    trimming("a.equal(b) ? new S(new Integer(4),a,b) : new S(new Ineger(3),a,b)").to("new S(a.equal(b)? new Integer(4): new Ineger(3), a, b)");
  }

  @Test public void DUPLICATED_pushdownTernaryintoConstructor2Div3() {
    trimming("a.equal(b) ? new S(a,new Integer(4),b) : new S(a, new Ineger(3), b)").to("new S(a,a.equal(b)? new Integer(4): new Ineger(3),b)");
  }

  @Test public void DUPLICATED_pushdownTernaryintoConstructor3Div3() {
    trimming("a.equal(b) ? new S(a,b,new Integer(4)) : new S(a,b,new Ineger(3))").to("new S(a, b, a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void DUPLICATED_pushdownTernaryintoConstructorNotSameArity() {
    trimming("a ? new S(a,new Integer(4),b) : new S(new Ineger(3))").to(
        "!a?new S(new Ineger(3)):new S(a,new Integer(4),b)                                                                                                                  ");
  }

  @Test public void DUPLICATED_pushdownTernaryintoPrintln() {
    trimming("    if (s.equals(t))\n"//
        + "      S.out.println(Hey + $);\n"//
        + "    else\n"//
        + "      S.out.println(Ho + x + a);").to("S.out.println(s.equals(t)?Hey+$:Ho+x+a);");
  }

  @Test public void DUPLICATED_pushdownTernaryLongFieldRefernece() {
    trimming("externalImage ? R.string.webview_contextmenu_image_download_action : R.string.webview_contextmenu_image_save_action")
        .to("!externalImage ? R.string.webview_contextmenu_image_save_action : R.string.webview_contextmenu_image_download_action");
  }

  @Test public void DUPLICATED_pushdownTernaryMethodInvocationFirst() {
    trimming("a?b():c").to("!a?c:b()");
  }

  @Test public void DUPLICATED_pushdownTernaryNoBoolean() {
    trimming("a?b:c").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNoReceiverReceiver() {
    trimming("a < b ? f() : a.f()").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNotOnMINUS() {
    trimming("a ? -c :-d").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNotOnMINUSMINUS1() {
    trimming("a ? --c :--d").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNotOnMINUSMINUS2() {
    trimming("a ? c-- :d--").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNotOnNOT() {
    trimming("a ? !c :!d").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNotOnPLUS() {
    trimming("a ? +x : +y").to("a ? x : y").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNotOnPLUSPLUS() {
    trimming("a ? x++ :y++").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNotSameFunctionInvocation() {
    trimming("a?b(x):d(x)").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryNotSameFunctionInvocation2() {
    trimming("a?x.f(x):x.d(x)").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryOnMethodCall() {
    trimming("a ? y.f(c,b) :y.f(c)").to("!a?y.f(c):y.f(c,b)");
  }

  @Test public void DUPLICATED_pushdownTernaryParFX() {
    trimming("a ?( false):true").to("!a && true");
  }

  @Test public void DUPLICATED_pushdownTernaryParTX() {
    trimming("a ? (((true ))): c").to("a || c");
  }

  @Test public void DUPLICATED_pushdownTernaryParXF() {
    trimming("a ? b : (false)").to("a && b");
  }

  @Test public void DUPLICATED_pushdownTernaryParXT() {
    trimming("a ? b : ((true))").to("!a || b");
  }

  @Test public void DUPLICATED_pushdownTernaryReceiverNoReceiver() {
    trimming("a < b ? a.f() : f()").to("a>=b?f():a.f()");
  }

  @Test public void DUPLICATED_pushdownTernaryToClasConstrctor() {
    trimming("a ? new B(a,b,c) : new B(a,x,c)").to("new B(a,a ? b : x ,c)");
  }

  @Test public void DUPLICATED_pushdownTernaryToClasConstrctorTwoDifferenes() {
    trimming("a ? new B(a,b,c) : new B(a,x,y)").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryToClassConstrctorNotSameNumberOfArgument() {
    trimming("a ? new B(a,b) : new B(a,b,c)").to(null);
  }

  @Test public void DUPLICATED_pushdownTernaryTX() {
    trimming("a ? true : c").to("a || c");
  }

  @Test public void DUPLICATED_pushdownTernaryXF() {
    trimming("a ? b : false").to("a && b");
  }

  @Test public void DUPLICATED_pushdownTernaryXT() {
    trimming("a ? b : true").to("!a || b");
  }

  @Test public void DUPLICATED_removeSuper() {
    trimming("class T { T() { super(); }").to("class T { T() { }");
  }

  @Test public void DUPLICATED_removeSuperWithArgument() {
    trimming("class T { T() { super(a); a();}").to(null);
  }

  @Test public void DUPLICATED_removeSuperWithStatemen() {
    trimming("class T { T() { super(); a++;}").to("class T { T() { ++a;}");
  }

  @Test public void removeSuperWithReceiver() {
    trimming("class X{X(Y o){o.super();}}").to(null);
  }

  @Test public void DUPLICATED_renameToDollarActual() {
    trimming(//
        "        public static DeletePolicy fromInt(int initialSetting) {\n" + //
            "            for (DeletePolicy policy: values()) {\n" + //
            "                if (policy.setting == initialSetting) {\n" + //
            "                    return policy;\n" + //
            "                }\n" + //
            "            }\n" + //
            "            throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");\n" + //
            "        }").to(//
                "        public static DeletePolicy fromInt(int initialSetting) {\n" + //
                    "            for (DeletePolicy $: values()) {\n" + //
                    "                if ($.setting == initialSetting) {\n" + //
                    "                    return $;\n" + //
                    "                }\n" + //
                    "            }\n" + //
                    "            throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");\n" + //
                    "        }");
  }

  @Test public void DUPLICATED_renameToDollarEnhancedFor() {
    trimming("int f() { for (int a: as) return a; }")//
        .to(" int f() {for(int $:as)return $;}");
  }

  @Test public void renameUnusedVariableToDoubleUnderscore1() {
    trimming("void f(int x) {System.out.println(x);}").to(null);
  }

  @Test public void renameUnusedVariableToDoubleUnderscore2() {
    trimming("void f(int x) {}").to(null);
  }

  @Test public void renameUnusedVariableToDoubleUnderscore3() {
    trimming("void f(@SuppressWarnings({\"unused\"}) int x) {}").to("void f(@SuppressWarnings({\"unused\"}) int __) {}");
  }

  @Test public void renameUnusedVariableToDoubleUnderscore4() {
    trimming("void f(int x, @SuppressWarnings(\"unused\") int y) {}").to("void f(int x, @SuppressWarnings(\"unused\") int __) {}");
  }

  @Test public void renameVariableUnderscore1() {
    trimming("void f(int _) {System.out.println(_);}").to("void f(int __) {System.out.println(__);}");
  }

  // TODO Ori: add binding in tests
  @Ignore @Test public void replaceClassInstanceCreationWithFactoryInfixExpression() {
    trimming("Integer x = new Integer(1 + 9);").to("Integer x = Integer.valueOf(1 + 9);");
  }

  // TODO Ori: add binding in tests
  @Ignore @Test public void replaceClassInstanceCreationWithFactoryInvokeMethode() {
    trimming("String x = new String(f());").to("String x = String.valueOf(f());");
  }

  @Test public void DUPLICATED_replaceInitializationInReturn() {
    trimming("int a = 3; return a + 4;").to("return 3 + 4;");
  }

  @Test public void DUPLICATED_replaceTwiceInitializationInReturn() {
    trimming("int a = 3; return a + 4 << a;").to("return 3 + 4 << 3;");
  }

  @Test public void DUPLICATED_rightSimplificatioForNulNNVariableReplacement() {
    final InfixExpression e = i("null != a");
    final Wring<InfixExpression> w = Toolbox.instance.find(e);
    that(w, notNullValue());
    that(w.scopeIncludes(e), is(true));
    that(w.eligible(e), is(true));
    final ASTNode replacement = ((Wring.ReplaceCurrentNode<InfixExpression>) w).replacement(e);
    that(replacement, notNullValue());
    that("" + replacement, is("a != null"));
  }

  @Test public void DUPLICATED_rightSipmlificatioForNulNNVariable() {
    that(Toolbox.instance.find(i("null != a")), instanceOf(InfixComparisonSpecific.class));
  }

  @Test public void DUPLICATED_sequencerFirstInElse() {
    trimming("if (a) {b++; c++; ++d;} else { f++; g++; return x;}").to("if (!a) {f++; g++; return x;} b++; c++; ++d; ");
  }

  @Test public void DUPLICATED_shorterChainParenthesisComparison() {
    trimming("a == b == c").to(null);
  }

  @Test public void DUPLICATED_shorterChainParenthesisComparisonLast() {
    trimming("b == a * b * c * d * e * f * g * h == a").to(null);
  }

  @Test public void DUPLICATED_shortestBranchIfWithComplexNestedIf3() {
    trimming("if (a) {f(); g(); h();} else if (a) ++i; else ++j;").to(null);
  }

  @Test public void DUPLICATED_shortestBranchIfWithComplexNestedIf4() {
    trimming("if (a) {f(); g(); h(); ++i;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;}");
  }

  @Test public void DUPLICATED_shortestBranchIfWithComplexNestedIf5() {
    trimming("if (a) {f(); g(); h(); ++i; f();} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();}");
  }

  @Test public void DUPLICATED_shortestBranchIfWithComplexNestedIf6() {
    trimming("if (a) {f(); g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;").to("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();j++;}");
  }

  @Test public void DUPLICATED_shortestBranchIfWithComplexNestedIf7() {
    trimming("if (a) {f(); ++i; g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;")
        .to("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;f();j++;}");
  }

  @Test public void DUPLICATED_shortestBranchIfWithComplexNestedIf8() {
    trimming("if (a) {f(); ++i; g(); h(); ++i; u++; f(); j++;} else if (a) ++i; else j++;")
        .to("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;u++;f();j++;}");
  }

  @Test public void DUPLICATED_shortestBranchIfWithComplexNestedIfPlain() {
    trimming("if (a) {f(); g(); h();} else { i++; j++;}").to("if(!a){i++;j++;}else{f();g();h();}");
  }

  @Test public void DUPLICATED_shortestBranchIfWithComplexSimpler() {
    trimming("if (a) {f(); g(); h();} else  i++; j++;").to("if(!a)i++;else{f();g();h();}++j;");
  }

  @Test public void DUPLICATED_shortestBranchInIf() {
    trimming("   int a=0;\n" + //
        "   if (s.equals(known)){\n" + //
        "     S.console();\n" + //
        "   } else {\n" + //
        "     a=3;\n" + //
        "   }\n" + //
        "").to("int a=0; if(!s.equals(known))a=3;else S.console();");
  }

  @Test public void DUPLICATED_shortestFirstAlignment() {
    trimming("n.isSimpleName() ? (SimpleName) n //\n" + //
        "            : n.isQualifiedName() ? ((QualifiedName) n).getName() //\n" + //
        "                : null").to(null);
  }

  @Test public void DUPLICATED_shortestFirstAlignmentShortened() {
    trimming("n.isF() ? (SimpleName) n \n" + //
        "            : n.isG() ? ((QualifiedName) n).getName() \n" + //
        "                : null").to(null);
  }

  @Test public void DUPLICATED_shortestFirstAlignmentShortenedFurther() {
    trimming("n.isF() ? (A) n : n.isG() ? ((B) n).f() \n" + //
        "                : null").to(null);
  }

  @Test public void DUPLICATED_shortestFirstAlignmentShortenedFurtherAndFurther() {
    trimming("n.isF() ? (A) n : n.isG() ? (B) n :  null").to(null);
  }

  @Test public void DUPLICATED_shortestIfBranchFirst01() {
    trimming(""//
        + "if (s.equals(0xDEAD)) {\n"//
        + " int $=0; "//
        + " for (int i=0; i<s.length(); ++i)     "//
        + " if (s.charAt(i)=='a')      "//
        + "   $ += 2;    "//
        + "} else "//
        + " if (s.charAt(i)=='d') "//
        + "  $ -= 1;  "//
        + "return $;  ")
            .to(""//
                + "if (!s.equals(0xDEAD)) {"//
                + " if(s.charAt(i)=='d')"//
                + "  $-=1;"//
                + "} else {"//
                + "  int $=0;"//
                + "  for(int i=0;i<s.length();++i)"//
                + "   if(s.charAt(i)=='a')"//
                + "     $+=2;"//
                + " }"//
                + " return $;");
  }

  @Test public void DUPLICATED_shortestIfBranchFirst02() {
    trimming("" //
        + "if (!s.equals(0xDEAD)) { "//
        + " int $=0;"//
        + " for (int i=0;i<s.length();++i)     "//
        + "   if (s.charAt(i)=='a')      "//
        + "     $ += 2;"//
        + "   else "//
        + "  if (s.charAt(i)=='d')      "//
        + "       $ -= 1;"//
        + "  return $;"//
        + "} else {    "//
        + " return 8;"//
        + "}")
            .to("" //
                + " if (s.equals(0xDEAD)) \n" + //
                "    return 8;" + //
                "      int $ = 0;\n" + //
                "      for (int i = 0;i < s.length();++i)\n" + //
                "       if (s.charAt(i) == 'a')\n" + //
                "          $ += 2;\n" + //
                "        else " + //
                "       if (s.charAt(i) == 'd')\n" + //
                "          $ -= 1;\n" + //
                "      return $;\n");
  }

  @Test public void DUPLICATED_shortestIfBranchFirst02a() {
    trimming("" + //
        " if (!s.equals(0xDEAD)) {\n" + //
        "      int $ = 0;\n" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          $ += 2;\n" + //
        "        else " + //
        "       if (s.charAt(i) == 'd')\n" + //
        "          $ -= 1;\n" + //
        "      return $;\n" + //
        "    }\n" + //
        "    return 8;" + //
        "").to(" if (s.equals(0xDEAD)) "//
            + "return 8; " + //
            "      int $ = 0;\n" + //
            "      for (int i = 0;i < s.length();++i)\n" + //
            "       if (s.charAt(i) == 'a')\n" + //
            "          $ += 2;\n" + //
            "        else "//
            + "       if (s.charAt(i) == 'd')\n" + //
            "          $ -= 1;\n" + //
            "      return $;\n" + //
            "");
  }

  @Test public void DUPLICATED_shortestIfBranchFirst02b() {
    trimming("" + //
        "      int $ = 0;\n" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          $ += 2;\n" + //
        "        else " + //
        "       if (s.charAt(i) == 'd')\n" + //
        "          $ -= 17;\n" + //
        "      return $;\n" + //
        "").to(null);
  }

  @Test public void DUPLICATED_shortestIfBranchFirst02c() {
    final CompilationUnit u = Wrap.Statement.intoCompilationUnit("" + //
        "      int $ = 0;\n" + //
        "      for (int i = 0;i < s.length();++i)\n" + //
        "       if (s.charAt(i) == 'a')\n" + //
        "          $ += 2;\n" + //
        "        else " + //
        "       if (s.charAt(i) == 'd')\n" + //
        "          $ -= 1;\n" + //
        "      return $;\n" + //
        ""//
    );
    final VariableDeclarationFragment f = extract.firstVariableDeclarationFragment(u);
    that(f, notNullValue());
    that(f, iz(" $ = 0"));
    that(extract.nextStatement(f),
        iz(" for (int i = 0;i < s.length();++i)\n"//
            + "       if (s.charAt(i) == 'a')\n"//
            + "          $ += 2;\n"//
            + "        else "//
            + "       if (s.charAt(i) == 'd')\n"//
            + "          $ -= 1;\n"));
  }

  @Test public void DUPLICATED_shortestIfBranchWithFollowingCommandsSequencer() {
    trimming("" + //
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

  @Test public void DUPLICATED_shortestOperand01() {
    trimming("x + y > z").to(null);
  }

  @Test public void DUPLICATED_shortestOperand10() {
    trimming("return b == true;} ").to("return b;}");
  }

  @Test public void DUPLICATED_shortestOperand11() {
    trimming("int h,u,m,a,n;return b == true && n + a > m - u || h > u;").to("int h,u,m,a,n;return b&&a+n>m-u||h>u;");
  }

  @Test public void DUPLICATED_shortestOperand12() {
    trimming("int k = 15; return 7 < k; ").to("return 7<15;");
  }

  @Test public void DUPLICATED_shortestOperand13() {
    trimming("return (2 > 2 + a) == true;").to("return 2>a+2;");
  }

  @Test public void DUPLICATED_shortestOperand13a() {
    trimming("(2 > 2 + a) == true").to("2>a+2 ");
  }

  @Test public void DUPLICATED_shortestOperand13b() {
    trimming("(2) == true").to("2 ");
  }

  @Test public void DUPLICATED_shortestOperand13c() {
    trimming("2 == true").to("2 ");
  }

  @Test public void DUPLICATED_shortestOperand14() {
    trimming("Integer t = new Integer(5);   return (t.toString() == null);    ").to("return((new Integer(5)).toString()==null);");
  }

  @Test public void DUPLICATED_shortestOperand17() {
    trimming("5 ^ a.getNum()").to("a.getNum() ^ 5");
  }

  @Test public void DUPLICATED_shortestOperand19() {
    trimming("k.get().operand() ^ a.get()").to("a.get() ^ k.get().operand()");
  }

  @Test public void DUPLICATED_shortestOperand20() {
    trimming("k.get() ^ a.get()").to("a.get() ^ k.get()");
  }

  @Test public void DUPLICATED_shortestOperand22() {
    trimming("return f(a,b,c,d,e) + f(a,b,c,d) + f(a,b,c) + f(a,b) + f(a) + f();").to(null);
  }

  @Test public void DUPLICATED_shortestOperand23() {
    trimming("return f() + \".\";     }").to(null);
  }

  @Test public void DUPLICATED_shortestOperand24() {
    trimming("f(a,b,c,d) & 175 & 0").to("f(a,b,c,d) & 0 & 175");
  }

  @Test public void DUPLICATED_shortestOperand25() {
    trimming("f(a,b,c,d) & bob & 0 ").to("bob & f(a,b,c,d) & 0");
  }

  @Test public void DUPLICATED_shortestOperand27() {
    trimming("return f(a,b,c,d) + f(a,b,c) + f();     } ").to(null);
  }

  @Test public void DUPLICATED_shortestOperand28() {
    trimming("return f(a,b,c,d) * f(a,b,c) * f();     } ").to("return f()*f(a,b,c)*f(a,b,c,d);}");
  }

  @Test public void DUPLICATED_shortestOperand29() {
    trimming("f(a,b,c,d) ^ f() ^ 0").to("f() ^ f(a,b,c,d) ^ 0");
  }

  @Test public void DUPLICATED_shortestOperand30() {
    trimming("f(a,b,c,d) & f()").to("f() & f(a,b,c,d)");
  }

  @Test public void DUPLICATED_shortestOperand31() {
    trimming("return f(a,b,c,d) | \".\";     }").to(null);
  }

  @Test public void DUPLICATED_shortestOperand32() {
    trimming("return f(a,b,c,d) && f();     }").to(null);
  }

  @Test public void DUPLICATED_shortestOperand33() {
    trimming("return f(a,b,c,d) || f();     }").to(null);
  }

  @Test public void DUPLICATED_shortestOperand34() {
    trimming("return f(a,b,c,d) + someVar; ").to(null);
  }

  @Test public void DUPLICATED_shortestOperand37() {
    trimming("return sansJavaExtension(f) + n + \".\"+ extension(f);").to(null);
  }

  @Test public void DUPLICATED_simpleBooleanMethod() {
    trimming("boolean f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .to("boolean f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void DUPLICATED_simplifyBlockComplexEmpty0() {
    trimming("{}").to("/* empty */    ");
  }

  @Test public void DUPLICATED_simplifyBlockComplexEmpty1() {
    trimming("{;;{;{{}}}{;}{};}").to(" ");
  }

  @Test public void DUPLICATED_simplifyBlockComplexSingleton() {
    assertSimplifiesTo("{;{{;;return b; }}}", "return b;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void DUPLICATED_simplifyBlockDeeplyNestedReturn() {
    assertSimplifiesTo("{{{;return c;};;};}", "return c;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void DUPLICATED_simplifyBlockExpressionVsExpression() {
    trimming("6 - 7 < a * 3").to("-1 < 3 * a");
  }

  @Test public void DUPLICATED_simplifyBlockLiteralVsLiteral() {
    trimming("if (a) return b; else c();").to("if(a)return b;c();");
  }

  @Test public void DUPLICATED_simplifyBlockThreeStatements() {
    assertSimplifiesTo("{i++;{{;;return b; }}j++;}", "i++;return b;j++;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void DUPLICATED_simplifyLogicalNegationNested() {
    trimming("!((a || b == c) && (d || !(!!c)))").to("!a && b != c || !d && c");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationNested1() {
    trimming("!(d || !(!!c))").to("!d && c");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationNested2() {
    trimming("!(!d || !!!c)").to("d && c");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfAnd() {
    trimming("!(f() && f(5))").to("!f() || !f(5)");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfEquality() {
    trimming("!(3 == 5)").to("3!=5");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfGreater() {
    trimming("!(3 > 5)").to("3 <= 5");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfGreaterEquals() {
    trimming("!(3 >= 5)").to("3 < 5");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfInequality() {
    trimming("!(3 != 5)").to("3 == 5");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfLess() {
    trimming("!(3 < 5)").to("3 >= 5");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfLessEquals() {
    trimming("!(3 <= 5)").to("3 > 5");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfMultipleAnd() {
    trimming("!(a && b && c)").to("!a || !b || !c");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfMultipleOr() {
    trimming("!(a || b || c)").to("!a && !b && !c");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfNot() {
    trimming("!!f()").to("f()");
  }

  @Test public void DUPLICATED_simplifyLogicalNegationOfOr() {
    trimming("!(f() || f(5))").to("!f() && !f(5)");
  }

  @Test public void DUPLICATED_sortAddition2() {
    trimming("1 + 2 < 3 & 7 + 4 > 2 + 1 || 6 - 7 < 2 + 1").to("3 <3&11>3||-1<3");
  }

  @Test public void DUPLICATED_sortAddition3() {
    trimming("6 - 7 < 1 + 2").to("-1<3").to(null);
  }

  @Test public void DUPLICATED_sortAddition4() {
    trimming("a + 11 + 2 < 3 & 7 + 4 > 2 + 1").to("7 + 4 > 2 + 1 & a + 11 + 2 < 3");
  }

  @Test public void DUPLICATED_sortAdditionClassConstantAndLiteral() {
    trimming("1+A< 12").to("A+1<12");
  }

  @Test public void DUPLICATED_sortAdditionFunctionClassConstantAndLiteral() {
    trimming("1+A+f()< 12").to("f()+A+1<12");
  }

  @Test public void DUPLICATED_sortAdditionThreeOperands1() {
    trimming("1.0+2222+3").to("2226.0");
  }

  @Test public void DUPLICATED_sortAdditionThreeOperands2() {
    trimming("1.0+1+124+1").to("127.0");
  }

  @Test public void DUPLICATED_sortAdditionThreeOperands3() {
    trimming("1+2F+33+142+1").to(null);
  }

  @Test public void DUPLICATED_sortAdditionThreeOperands4() {
    trimming("1+2+'a'").to(null);
  }

  @Test public void DUPLICATED_sortAdditionTwoOperands0CheckThatWeSortByLength_a() {
    trimming("1111+211").to("1322");
  }

  @Test public void DUPLICATED_sortAdditionTwoOperands0CheckThatWeSortByLength_b() {
    trimming("211+1111").to("1322").to(null);
  }

  @Test public void DUPLICATED_sortAdditionTwoOperands1() {
    trimming("1+2F").to(null);
  }

  @Test public void DUPLICATED_sortAdditionTwoOperands2() {
    trimming("2.0+1").to("3.0");
  }

  @Test public void DUPLICATED_sortAdditionTwoOperands3() {
    trimming("1+2L").to("3L");
  }

  @Test public void DUPLICATED_sortAdditionTwoOperands4() {
    trimming("2L+1").to("3L");
  }

  @Test public void DUPLICATED_sortAdditionUncertain() {
    trimming("1+a").to(null);
  }

  @Test public void DUPLICATED_sortAdditionVariableClassConstantAndLiteral() {
    trimming("1+A+a< 12").to("a+A+1<12");
  }

  @Test public void DUPLICATED_sortConstantMultiplication() {
    trimming("a*2").to("2*a");
  }

  @Test public void DUPLICATED_sortDivision() {
    trimming("2.1/34.2/1.0").to("0.06140350877192982");
  }

  @Test public void DUPLICATED_sortDivisionLetters() {
    trimming("x/b/a").to("x/a/b");
  }

  @Test public void DUPLICATED_sortDivisionNo() {
    trimming("2.1/3").to("0.7000000000000001");
  }

  @Test public void DUPLICATED_sortThreeOperands1() {
    trimming("1.0*2222*3").to("6666.0");
  }

  @Test public void DUPLICATED_sortTwoOperands0CheckThatWeSortByLength_a() {
    trimming("1111*211").to("234421");
  }

  @Test public void DUPLICATED_sortTwoOperands0CheckThatWeSortByLength_b() {
    trimming("211*1111").to("234421");
  }

  // TODO Ori: add binding for tests
  @Ignore @Test public void SwitchFewCasesReplaceWithIf1() {
    trimming("" //
        + " int x;\n" //
        + " switch (x) {\n" //
        + " case 1:\n"//
        + "   System.out.println(\"1\");\n" //
        + "   break;\n" //
        + " default:\n"//
        + "   System.out.println(\"error\");\n" //
        + "   break;\n" //
        + " }\n")
            .to("" //
                + " int x;\n" //
                + " if (x == 1) {\n" //
                + "   System.out.println(\"1\");\n" //
                + "   return 2;\n" //
                + " } else\n"//
                + "   System.out.println(\"3\");\n");
  }

  @Test public void switchSimplifyCaseAfterDefault() {
    trimming("" //
        + "switch (n.getNodeType()) {\n" //
        + "default:\n" //
        + "  return -1;\n" //
        + "case BREAK_STATEMENT:\n" //
        + "  return 0;\n" //
        + "case CONTINUE_STATEMENT:\n" //
        + "  return 1;\n" //
        + "case RETURN_STATEMENT:\n" //
        + "  return 2;\n" //
        + "case THROW_STATEMENT:\n" //
        + "  return 3;\n" //
        + "}").to(null);
  }

  @Test public void switchSimplifyCaseAfterDefault1() {
    trimming("" //
        + "switch (n.getNodeType()) {" //
        + "  default:" //
        + "    return -1;" //
        + "  case BREAK_STATEMENT:" //
        + "    return 0;" //
        + "  case CONTINUE_STATEMENT:" //
        + "    return 1;" //
        + "  case RETURN_STATEMENT:" //
        + "    return 2;" //
        + "  case THROW_STATEMENT:" //
        + "    return 3;" //
        + "  }").to(null);
  }

  @Test public void switchSimplifyWithDefault2() {
    trimming("" + "switch (a) {\n" //
        + "case \"-N\":" //
        + "  optDoNotOverwrite = true;" //
        + "  break;" //
        + "case \"-E\":" //
        + "  optIndividualStatistics = true;" //
        + "  break;" //
        + "case \"-V\":" //
        + "  optVerbose = true;" //
        + "  break;" //
        + "case \"-l\":" //
        + "  optStatsLines = true;" //
        + "  break;" //
        + "case \"-r\":" //
        + "  optStatsChanges = true;" //
        + "  break;" //
        + "default:" //
        + "  if (!a.startsWith(\"-\"))" //
        + "    optPath = a;" //
        + "  try {" //
        + "    if (a.startsWith(\"-C\"))" //
        + "      optRounds = Integer.parseUnsignedInt(a.substring(2));" //
        + "  } catch (final NumberFormatException e) {" //
        + "    throw e;" //
        + "  }" //
        + "}").to(null);
  }

  @Test public void DUPLICATED_synchronizedBraces() {
    trimming("" //
        + "    synchronized (variables) {\n" //
        + "      for (final String key : variables.keySet())\n"//
        + "        $.variables.put(key, variables.get(key));\n" //
        + "    }").to(null);
  }

  @Test public void DUPLICATED_ternarize05() {
    trimming(" int $ = 0; "//
        + "if (s.equals(532))    "//
        + "$ += 6;   "//
        + "else    "//
        + "$ += 9;      ").to("int $=0;$+=s.equals(532)?6:9;");
  }

  @Test public void DUPLICATED_ternarize05a() {
    trimming(" int $ = 0; "//
        + "if (s.equals(532))    "//
        + "$ += 6;   "//
        + "else    "//
        + "$ += 9;      "//
        + "return $; ").to("int $=0;$+=s.equals(532)?6:9;return $;");
  }

  @Test public void DUPLICATED_ternarize07() {
    trimming("" //
        + "String $;" //
        + "$ = s;   " //
        + "if ($.equals(532)==true)    " //
        + "  $ = s + 0xABBA;   " //
        + "S.out.println($); " //
        + "")
            .to("" //
                + "String $ =s ;" //
                + "if ($.equals(532))    " //
                + "  $ = s + 0xABBA;   " //
                + "S.out.println($); " //
                + "");
  }

  @Test public void DUPLICATED_ternarize07a() {
    trimming("" //
        + "String $;" //
        + "$ = s;   " //
        + "if ($==true)    " //
        + "  $ = s + 0xABBA;   " //
        + "S.out.println($); " //
        + "").to("String $=s;if($)$=s+0xABBA;S.out.println($);");
  }

  @Test public void DUPLICATED_ternarize07aa() {
    trimming("String $=s;if($==true)$=s+0xABBA;S.out.println($);").to("String $=s==true?s+0xABBA:s;S.out.println($);");
  }

  @Test public void DUPLICATED_ternarize07b() {
    trimming("" //
        + "String $ =s ;" //
        + "if ($.equals(532)==true)    " //
        + "  $ = s + 0xABBA;   " //
        + "S.out.println($); ")
            .to("" //
                + "String $=s.equals(532)==true?s+0xABBA:s;S.out.println($);");
  }

  @Test public void DUPLICATED_ternarize09() {
    trimming("if (s.equals(532)) {    return 6;}else {    return 9;}").to("return s.equals(532)?6:9; ");
  }

  @Test public void DUPLICATED_ternarize10() {
    trimming("String $ = s, foo = bar;   "//
        + "if ($.equals(532)==true)    " //
        + "$ = s + 0xABBA;   "//
        + "S.out.println($); ").to("String $=s.equals(532)==true?s+0xABBA:s,foo=bar;S.out.println($);");
  }

  @Test public void DUPLICATED_ternarize12() {
    trimming("String $ = s;   if (s.equals(532))    $ = $ + 0xABBA;   S.out.println($); ").to("String $=s.equals(532)?s+0xABBA:s;S.out.println($);");
  }

  @Test public void DUPLICATED_ternarize13() {
    trimming("String $ = m, foo;  if (m.equals(f())==true)   foo = M; ")//
        .to("String foo;if(m.equals(f())==true)foo=M;")//
        .to("String foo;if(m.equals(f()))foo=M;");
  }

  @Test public void DUPLICATED_ternarize13Simplified() {
    trimming("String r = m, f;  if (m.e(f()))   f = M; ")//
        .to("String f;if(m.e(f()))f=M;");
  }

  @Test public void DUPLICATED_ternarize13SimplifiedMore() {
    trimming("if (m.equals(f())==true)   foo = M; ").to("if (m.equals(f())) foo=M;");
  }

  @Test public void DUPLICATED_ternarize13SimplifiedMoreAndMore() {
    trimming("f (m.equals(f())==true); foo = M; ").to("f (m.equals(f())); foo=M;");
  }

  @Test public void DUPLICATED_ternarize13SimplifiedMoreAndMoreAndMore() {
    trimming("f (m.equals(f())==true);  ").to("f (m.equals(f()));");
  }

  @Test public void DUPLICATED_ternarize13SimplifiedMoreVariant() {
    trimming("if (m==true)   foo = M; ").to("if (m) foo=M;");
  }

  @Test public void DUPLICATED_ternarize13SimplifiedMoreVariantShorter() {
    trimming("if (m==true)   f(); ").to("if (m) f();");
  }

  @Test public void DUPLICATED_ternarize13SimplifiedMoreVariantShorterAsExpression() {
    trimming("f (m==true);   f(); ").to("f (m); f();");
  }

  @Test public void DUPLICATED_ternarize14() {
    trimming("String $=m,foo=GY;if ($.equals(f())==true){foo = M;int k = 2;k = 8;S.out.println(foo);}f();")
        .to("String $=m,foo=GY;if($.equals(f())){foo=M;int k=8;S.out.println(foo);}f();");
  }

  @Test public void DUPLICATED_ternarize16() {
    trimming("String $ = m;  int num1, num2, num3;  if (m.equals(f()))   num2 = 2; ").to(null);
  }

  @Test public void DUPLICATED_ternarize16a() {
    trimming("int n1, n2 = 0, n3;\n" + //
        "  if (d)\n" + //
        "    n2 = 2;").to("int n1, n2 = d ? 2: 0, n3;");
  }

  public void DUPLICATED_ternarize18() {
    trimming("final String $=s;System.out.println(s.equals($)?tH3+$:h2A+$+0);")//
        .to("System.out.println(s.equals(s)?tH3+$:h2A+s+0);");
  }

  @Test public void DUPLICATED_ternarize21() {
    trimming("if (s.equals(532)){    S.out.println(gG);    S.out.l(kKz);} f(); ").to(null);
  }

  @Test public void DUPLICATED_ternarize21a() {
    trimming("   if (s.equals(known)){\n" + //
        "     S.out.l(gG);\n" + //
        "   } else {\n" + //
        "     S.out.l(kKz);\n" + //
        "   }").to("S.out.l(s.equals(known)?gG:kKz);");
  }

  @Test public void DUPLICATED_ternarize22() {
    trimming("int a=0;   if (s.equals(532)){    S.console();    a=3;} f(); ").to(null);
  }

  @Test public void DUPLICATED_ternarize26() {
    trimming("int a=0;   if (s.equals(532)){    a+=2;   a-=2; } f(); ").to(null);
  }

  @Test public void DUPLICATED_ternarize33() {
    trimming("int a, b=0;   if (b==3){    a=4; } ")//
        .to("int a;if(0==3){a=4;}") //
        .to("int a;if(0==3)a=4;") //
        .to(null);
  }

  @Test public void DUPLICATED_ternarize35() {
    trimming("int a,b=0,c=0;a=4;if(c==3){b=2;}")//
        .to("int a=4,b=0,c=0;if(c==3)b=2;");
  }

  @Test public void DUPLICATED_ternarize36() {
    trimming("int a,b=0,c=0;a=4;if (c==3){  b=2;   a=6; } f();").to("int a=4,b=0,c=0;if(c==3){b=2;a=6;} f();");
  }

  @Test public void DUPLICATED_ternarize38() {
    trimming("int a, b=0;if (b==3){    a+=2+r();a-=6;} f();").to(null);
  }

  @Test public void DUPLICATED_ternarize41() {
    trimming("int a,b,c,d;a = 3;b = 5; d = 7;if (a == 4)while (b == 3) c = a; else while (d == 3)c =a*a; ")
        .to("int a=3,b,c,d;b=5;d=7;if(a==4)while(b==3)c=a;else while(d==3)c=a*a;");
  }

  @Test public void DUPLICATED_ternarize42() {
    trimming(" int a, b; a = 3;b = 5; if (a == 4) if (b == 3) b = 2; else{b = a; b=3;}  else if (b == 3) b = 2; else{ b = a*a;         b=3; }")//
        .to("int a=3,b;b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .to("int b=5;if(3==4)if(b==3)b=2;else{b=b=3;}else if(b==3)b=2;else{b=9;b=3;}")//
        .to("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=9;b=3;}")//
        .to(null);
  }

  @Test public void DUPLICATED_ternarize45() {
    trimming("if (m.equals(f())==true) if (b==3){ return 3; return 7;}   else    if (b==3){ return 2;}     a=7; ")//
        .to("if (m.equals(f())) {if (b==3){ return 3; return 7;} if (b==3){ return 2;}   }  a=7; ");
  }

  @Test public void DUPLICATED_ternarize46() {
    trimming(//
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
            "     }").to("int a;if(m.equals(NG)==true)if(0==3){return 3;}else{a+=7;}else if(0==3){return 2;}else{a=7;}");
  }

  @Test public void DUPLICATED_ternarize49() {
    trimming("if (s.equals(532)){ S.out.println(gG); S.out.l(kKz); } f();").to(null);
  }

  @Test public void DUPLICATED_ternarize52() {
    trimming("int a=0,b = 0,c,d = 0,e = 0;if (a < b) {c = d;c = e;} f();")//
        .to(null);
  }

  @Test public void DUPLICATED_ternarize54() {
    trimming("int $=1,xi=0,xj=0,yi=0,yj=0; if(xi > xj == yi > yj)++$;else--$;")//
        .to(" int $=1,xj=0,yi=0,yj=0;      if(0>xj==yi>yj)++$;else--$;");
  }

  @Test public void DUPLICATED_ternarize55() {
    trimming("if (key.equals(markColumn))\n" + //
        " to.put(key, a.toString());\n" + //
        "else\n" + //
        "  to.put(key, missing(key, a) ? Z2 : get(key, a));").to("to.put(key,key.equals(markColumn)?a.toString():missing(key,a)?Z2:get(key,a));");
  }

  @Test public void DUPLICATED_ternarize56() {
    trimming("if (target == 0) {p.f(X); p.v(0); p.f(q +  target); p.v(q * 100 / target); } f();") //
        .to("if(target==0){p.f(X);p.v(0);p.f(q+target);p.v(100*q / target); } f();");
  }

  @Test public void DUPLICATED_ternarizeintoSuperMethodInvocation() {
    trimming("a ? super.f(a, b, c) : super.f(a, x, c)").to("super.f(a, a ? b : x, c)");
  }

  @Test public void DUPLICATED_ternaryPushdownOfReciever() {
    trimming("a ? b.f():c.f()").to("(a?b:c).f()");
  }

  @Test public void DUPLICATED_testPeel() {
    that(Wrap.Expression.off(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge")),
        is("on * notion * of * no * nothion != the * plain + kludge"));
  }

  @Test public void DUPLICATED_twoMultiplication1() {
    trimming("f(a,b,c,d) * f()").to("f() * f(a,b,c,d)");
  }

  @Test public void DUPLICATED_twoOpportunityExample() {
    that(TrimmerTestsUtils.countOpportunities(new Trimmer(),
        (CompilationUnit) makeAST.COMPILATION_UNIT.from(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge"))), is(2));
    that(TrimmerTestsUtils.countOpportunities(new Trimmer(),
        (CompilationUnit) makeAST.COMPILATION_UNIT.from(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge"))), is(2));
  }

  @Test public void DUPLICATED_useOutcontextToManageStringAmbiguity() {
    trimming("1+2+s<3").to("s+1+2<3");
  }

  @Test public void DUPLICATED_vanillaShortestFirstConditionalNoChange() {
    trimming("literal ? CONDITIONAL_OR : CONDITIONAL_AND").to(null);
  }

  @Test public void DUPLICATED_xorSortClassConstantsAtEnd() {
    trimming("f(a,b,c,d) ^ BOB").to(null);
  }
}
