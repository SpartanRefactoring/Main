package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.ExpressionComparator.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static il.org.spartan.spartanizer.wrings.TESTUtils.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;
import il.org.spartan.spartanizer.wringing.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) public final class TrimmerTest {
  @Test public void actualExampleForSortAddition() {
    trimmingOf("1 + b.statements().indexOf(declarationStmt)")//
        .stays();
  }

  @Test public void actualExampleForSortAdditionInContext() {
    final String from = "2 + a < b";
    final String expected = "a + 2 < b";
    final Wrap w = Wrap.Expression;
    final String wrap = w.on(from);
    azzert.that(from, is(w.off(wrap)));
    final Trimmer t = new Trimmer();
    final String unpeeled = applyTrimmer(t, wrap);
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
    trimmingOf("(x >> 18) & MASK_BITS")//
        .stays();
    trimmingOf("(x >> 18) & MASK_6BITS")//
        .stays();
  }

  @Test public void annotationDoNotRemoveSingleMemberNotCalledValue() {
    trimmingOf("@SuppressWarnings(sky = \"blue\") void m() {}")//
        .stays();
  }

  @Test public void annotationDoNotRemoveValueAndSomethingElse() {
    trimmingOf("@SuppressWarnings(value = \"something\", x = 2) void m() {}")//
        .stays();
  }

  @Test public void annotationRemoveEmptyParentheses() {
    trimmingOf("@Override() void m() {}")//
        .gives("@Override void m() {}");
  }

  @Test public void annotationRemoveValueFromMultipleAnnotations() {
    trimmingOf("@SuppressWarnings(value = \"javadoc\") @TargetApi(value = 23) void m() {}") //
        .gives("@SuppressWarnings(\"javadoc\") @TargetApi(23) void m() {}");
  }

  @Test public void annotationRemoveValueMemberArrayValue() {
    trimmingOf("@SuppressWarnings(value = { \"something\", \"something else\" }) void m() {}") //
        .gives("@SuppressWarnings({ \"something\", \"something else\" }) void m() {}");
  }

  @Test public void annotationRemoveValueMemberSingleValue() {
    trimmingOf("@SuppressWarnings(value = \"something\") void m() {}") //
        .gives("@SuppressWarnings(\"something\") void m() {}");
  }

  @Test public void assignmentAssignmentChain1() {
    trimmingOf("c = a = 13; b = 13;")//
        .gives("b = c = a = 13;");
  }

  @Test public void assignmentAssignmentChain2() {
    trimmingOf("a = 13; b= c = 13;")//
        .gives("b = c = a = 13;");
  }

  @Test public void assignmentAssignmentChain3() {
    trimmingOf("a = b = 13; c = d = 13;")//
        .gives("c = d = a = b = 13;");
  }

  @Test public void assignmentAssignmentChain4() {
    trimmingOf("a1 = a2 = a3 = a4 = 13; b1 = b2 = b3 = b4 = b5 = 13;")//
        .gives("b1 = b2 = b3 = b4 = b5 = a1 = a2 = a3 = a4 = 13;");
  }

  @Test public void assignmentAssignmentChain5() {
    trimmingOf("a1 = (a2 = (a3 = (a4 = 13))); b1 = b2 = b3 = ((((b4 = (b5 = 13)))));")//
        .gives("b1=b2=b3=((((b4=(b5=a1=(a2=(a3=(a4=13))))))));");
  }

  @Test public void assignmentAssignmentNew() {
    trimmingOf("a = new B(); b= new B();")//
        .stays();
  }

  @Test public void assignmentAssignmentNewArray() {
    trimmingOf("a = new A[3]; b= new A[3];")//
        .stays();
  }

  @Test public void assignmentAssignmentNull() {
    trimmingOf("c = a = null; b = null;")//
        .stays();
  }

  @Test public void assignmentAssignmentSideEffect() {
    trimmingOf("a = f(); b= f();")//
        .stays();
  }

  @Test public void assignmentAssignmentVanilla() {
    trimmingOf("a = 13; b= 13;")//
        .gives("b = a = 13;");
  }

  @Test public void assignmentAssignmentVanilla0() {
    trimmingOf("a = 0; b = 0;")//
        .gives("b = a = 0;");
  }

  @Test public void assignmentAssignmentVanillaScopeIncludes() {
    included("a = 3; b = 3;", Assignment.class).in(new AssignmentAndAssignment());
  }

  @Test public void assignmentAssignmentVanillaScopeIncludesNull() {
    included("a = null; b = null;", Assignment.class).notIn(new AssignmentAndAssignment());
  }

  @Test public void assignmentReturn0() {
    trimmingOf("a = 3; return a;")//
        .gives("return a = 3;");
  }

  @Test public void assignmentReturn1() {
    trimmingOf("a = 3; return (a);")//
        .gives("return a = 3;");
  }

  @Test public void assignmentReturn2() {
    trimmingOf("a += 3; return a;")//
        .gives("return a += 3;");
  }

  @Test public void assignmentReturn3() {
    trimmingOf("a *= 3; return a;")//
        .gives("return a *= 3;");
  }

  @Test public void assignmentReturniNo() {
    trimmingOf("b = a = 3; return a;")//
        .stays();
  }

  @Test public void blockSimplifyVanilla() {
    trimmingOf("if (a) {f(); }")//
        .gives("if (a) f();");
  }

  @Test public void blockSimplifyVanillaSimplified() {
    trimmingOf(" {f(); }")//
        .gives("f();");
  }

  @Test public void booleanChangeValueOfToConstant() {
    trimmingOf("Boolean b = Boolean.valueOf(true);")//
        .gives("Boolean b = Boolean.TRUE;");
    trimmingOf("Boolean b = Boolean.valueOf(false);")//
        .gives("Boolean b = Boolean.FALSE;");
  }

  @Test public void booleanChangeValueOfToConstantNotConstant() {
    trimmingOf("Boolean.valueOf(expected);")//
        .stays(); // from junit source
  }

  @Test public void bugInLastIfInMethod() {
    trimmingOf("        @Override public void messageFinished(final LocalMessage myMessage, final int number, final int ofTotal) {\n"
        + "          if (!isMessageSuppressed(myMessage)) {\n"//
        + "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n" + "            messages.add(myMessage);\n"//
        + "            stats.unreadMessageCount += myMessage.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += myMessage.isSet(Flag.FLAGGED) ? 1 : 0;\n"//
        + "            if (listener != null)\n" + "              listener.listLocalMessagesAddMessages(account, null, messages);\n"//
        + "          }\n"//
        + "        }")//
            .gives(
                "@Override public void messageFinished(final LocalMessage myMessage,final int number,final int ofTotal){if(isMessageSuppressed(myMessage))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(myMessage);stats.unreadMessageCount+=myMessage.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=myMessage.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod1() {
    trimmingOf("        @Override public void f() {\n"//
        + "          if (!isMessageSuppressed(message)) {\n" + "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n"//
        + "            messages.add(message);\n" + "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n"//
        + "            if (listener != null)\n" + "              listener.listLocalMessagesAddMessages(account, null, messages);\n"//
        + "          }\n"//
        + "        }")//
            .gives(
                "@Override public void f(){if(isMessageSuppressed(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod2() {
    trimmingOf("        public void f() {\n"//
        + "          if (!g(message)) {\n" + "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n"//
        + "            messages.add(message);\n" + "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n"//
        + "            if (listener != null)\n" + "              listener.listLocalMessagesAddMessages(account, null, messages);\n"//
        + "          }\n"//
        + "        }")//
            .gives(
                "public void f(){if(g(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod3() {
    trimmingOf("        public void f() {\n"//
        + "          if (!g(a)) {\n" + "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n"//
        + "            messages.add(message);\n" + "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n"//
        + "            if (listener != null)\n" + "              listener.listLocalMessagesAddMessages(account, null, messages);\n"//
        + "          }\n"//
        + "        }")//
            .gives(
                "public void f(){if(g(a))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod4() {
    trimmingOf("        public void f() {\n"//
        + "          if (!g) {\n" + "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n"//
        + "            messages.add(message);\n" + "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n"//
        + "            if (listener != null)\n" + "              listener.listLocalMessagesAddMessages(account, null, messages);\n"//
        + "          }\n"//
        + "        }")//
            .gives(
                "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod5() {
    trimmingOf("        public void f() {\n"//
        + "          if (!g) {\n" + "            final List<LocalMessage> messages = new ArrayList<LocalMessage>();\n"//
        + "            messages.add(message);\n" + "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n"//
        + "          }\n"//
        + "        }")//
            .gives(
                "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void bugInLastIfInMethod6() {
    trimmingOf("        public void f() {\n"//
        + "          if (!g) {\n"//
        + "            final int messages = 3;\n" + "            messages.add(message);\n"//
        + "            stats.unreadMessageCount += message.isSet(Flag.SEEN) ? 0 : 1;\n"
        + "            stats.flaggedMessageCount += message.isSet(Flag.FLAGGED) ? 1 : 0;\n"//
        + "          }\n"//
        + "        }")//
            .gives(
                "public void f(){if(g)return;final int messages=3;messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void bugInLastIfInMethod7() {
    trimmingOf("        public void f() {\n"//
        + "          if (!g) {\n"//
        + "            foo();\n"//
        + "            bar();\n"//
        + "          }\n"//
        + "        }").gives("public void f(){if(g)return;foo();bar();}");
  }

  @Test public void bugInLastIfInMethod8() {
    trimmingOf("        public void f() {\n"//
        + "          if (g) {\n"//
        + "            foo();\n"//
        + "            bar();\n"//
        + "          }\n"//
        + "        }").gives("public void f(){if(!g)return;foo();bar();}");
  }

  @Test public void bugIntroducingMISSINGWord1() {
    trimmingOf("b.f(a) && -1 == As.g(f).h(c) ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .gives("b.f(a) && As.g(f).h(c) == -1 ? o(s,b,g(f)) : b.f(\".in\") && !y(d,b)? o(b.z(u,v),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord1a() {
    trimmingOf("-1 == As.g(f).h(c)")//
        .gives("As.g(f).h(c)==-1");
  }

  @Test public void bugIntroducingMISSINGWord1b() {
    trimmingOf("b.f(a) && X ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .gives("b.f(a)&&X?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1c() {
    trimmingOf("Y ? o(s, b, g(f)) : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")
        .gives("Y?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1d() {
    trimmingOf("Y ? Z : !b.f(\".in\") ? null : y(d, b) ? null : o(b.z(u, v), s, f)")//
        .gives("Y?Z:b.f(\".in\")&&!y(d,b)?o(b.z(u,v),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1e() {
    trimmingOf("Y ? Z : R ? null : S ? null : T")//
        .gives("Y?Z:!R&&!S?T:null");
  }

  @Test public void bugIntroducingMISSINGWord2() {
    trimmingOf(
        "name.endsWith(testSuffix) &&  MakeAST.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2a() {
    trimmingOf(
        "name.endsWith(testSuffix) &&  MakeAST.stringBuilder(f).indexOf(testKeyword) == -1? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2b() {
    trimmingOf(
        "name.endsWith(testSuffix) &&  T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix) && T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2c() {
    trimmingOf(
        "X && T ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "X && T ? objects(s,name,makeInFile(f)) : name.endsWith(\".in\") && !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2d() {
    trimmingOf("X && T ? E : Y ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .gives("X && T ? E : !Y && !dotOutExists(d,name) ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e() {
    trimmingOf("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .gives("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e1() {
    trimmingOf("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(x, Z2), s, f)")
        .gives("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(x,Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2e2() {
    trimmingOf("X &&  T ? E : Y ? null : Z ? null : objects(name.replaceAll(g, Z2), s, f)")
        .gives("X &&  T ? E : !Y && !Z ? objects(name.replaceAll(g,Z2),s,f) : null");
  }

  @Test public void bugIntroducingMISSINGWord2f() {
    trimmingOf("X &&  T ? E : Y ? null : Z ? null : F")//
        .gives("X&&T?E:!Y&&!Z?F:null");
  }

  @Test public void bugIntroducingMISSINGWord3() {
    trimmingOf(
        "name.endsWith(testSuffix) && -1 == MakeAST.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord3a() {
    trimmingOf("!name.endsWith(x) ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(3, 56), s, f)")
        .gives("name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWordTry1() {
    trimmingOf(
        "name.endsWith(testSuffix) && -1 == MakeAST.stringBuilder(f).indexOf(testKeyword) ? objects(s, name, makeInFile(f)) : !name.endsWith(\".in\") ? null : dotOutExists(d, name) ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix) && MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWordTry2() {
    trimmingOf("!(intent.getBooleanExtra(EXTRA_FROM_SHORTCUT, false) && !K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName()))")
        .gives("!intent.getBooleanExtra(EXTRA_FROM_SHORTCUT,false)||K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName())");
  }

  @Test public void bugIntroducingMISSINGWordTry3() {
    trimmingOf("!(f.g(X, false) && !a.b.e(m.h()))")//
        .gives("!f.g(X,false)||a.b.e(m.h())");
  }

  @Test public void bugOfMissingTry() {
    trimmingOf("!(A && B && C && true && D)")//
        .gives("!A||!B||!C||false||!D");
  }

  @Test public void canonicalFragementExample1() {
    trimmingOf("int a; a = 3;")//
        .gives("int a = 3;");
  }

  @Test public void canonicalFragementExample2() {
    trimmingOf("int a = 2; if (b) a = 3; ")//
        .gives("int a = b ? 3 : 2;");
  }

  @Test public void canonicalFragementExample3() {
    trimmingOf("int a = 2; a += 3; ")//
        .gives("int a = 2 + 3;");
  }

  @Test public void canonicalFragementExample4() {
    trimmingOf("int a = 2; a = 3 * a; ")//
        .gives("int a = 3 * 2;");
  }

  @Test public void canonicalFragementExample5() {
    trimmingOf("int a = 2; return 3 * a; ")//
        .gives("return 3 * 2;");
  }

  @Test public void canonicalFragementExample6() {
    trimmingOf("int a = 2; return a; ")//
        .gives("return 2;");
  }

  @Test public void canonicalFragementExamples() {
    trimmingOf("int a; a = 3;")//
        .gives("int a = 3;");
    trimmingOf("int a = 2; if (b) a = 3; ")//
        .gives("int a = b ? 3 : 2;");
    trimmingOf("int a = 2; a += 3; ")//
        .gives("int a = 2 + 3;");
    trimmingOf("int a = 2; a = 3 * a; ")//
        .gives("int a = 3 * 2;");
    trimmingOf("int a = 2; return 3 * a; ")//
        .gives("return 3 * 2;");
    trimmingOf("int a = 2; return a; ")//
        .gives("return 2;");
  }

  @Test public void canonicalFragementExamplesWithExraFragments() {
    trimmingOf("int a = 2; a = 3 * a * b; ")//
        .gives("int a = 3 * 2 * b;");
    trimmingOf("int a = 2; a = 3 * a; ")//
        .gives("int a = 3 * 2;");
    trimmingOf("int a = 2; a += 3; ")//
        .gives("int a = 2 + 3;");
    trimmingOf("int a = 2; a += b; ")//
        .gives("int a = 2 + b;");
    trimmingOf("int a = 2, b = 11; a = 3 * a * b; ")//
        .gives("int a=2;a=3*a*11;")//
        .gives("int a=3*2*11;")//
        .gives("int a=66;");
    trimmingOf("int a = 2, b=1; a += b; ")//
        .gives("int a=2;a+=1;")//
        .gives("int a=2+1;");
    trimmingOf("int a = 2,b=1; if (b) a = 3; ")//
        .gives("int a=2;if(1)a=3;")//
        .gives("int a=1?3:2;");
    trimmingOf("int a = 2, b = 1; return a + 3 * b; ")//
        .gives("int b=1;return 2+3*b;");
    trimmingOf("int a =2,b=2; if (x) a = 2*a;")//
        .gives("int a=x?2*2:2, b=2;");
    trimmingOf("int a = 2, b; a = 3 * a * b; ")//
        .gives("int a = 2, b; a *= 3 * b; ")//
        .stays();
    trimmingOf("int a = 2, b; a += b; ")//
        .stays();
    trimmingOf("int a =2,b; if (x) a = 2*a;")//
        .gives("int a=x?2*2:2, b;");
    trimmingOf("int a = 2, b; return a + 3 * b; ")//
        .gives("return 2 + 3*b;");
    trimmingOf("int a =2; if (x) a = 3*a;")//
        .gives("int a=x?3*2:2;");
    trimmingOf("int a = 2; return 3 * a * a; ")//
        .gives("return 3 * 2 * 2;");
    trimmingOf("int a = 2; return 3 * a * b; ")//
        .gives("return 3 * 2 * b;");
    trimmingOf("int a = 2; return a; ")//
        .gives("return 2;");
    trimmingOf("int a,b=2; a = b;")//
        .gives("int a;a=2;")//
        .gives("int a=2;");
    trimmingOf("int a,b; a = 3;")//
        .gives("int a = 3, b;");
    trimmingOf("int a; if (x) a = 3; else a++;")//
        .gives("int a;if(x)a=3;else++a;");
    trimmingOf("int b=5,a = 2,c=4; return 3 * a * b * c; ")//
        .gives("int a=2,c=4;return 3*a*5*c;");
    trimmingOf("int b=5,a = 2,c; return 3 * a * b * c; ")//
        .gives("int a = 2; return 3 * a * 5 * c;");
  }

  @Test public void canonicalFragementExamplesWithExraFragmentsX() {
    trimmingOf("int a; if (x) a = 3; else a++;")//
        .gives("int a;if(x)a=3;else++a;");
  }

  @Test public void chainComparison() {
    final InfixExpression e = i("a == true == b == c");
    azzert.that(right(e) + "", is("c"));
    trimmingOf("a == true == b == c")//
        .gives("a == b == c");
  }

  @Test public void chainCOmparisonTrueLast() {
    trimmingOf("a == b == c == true")//
        .gives("a == b == c");
  }

  @Test public void comaprisonWithBoolean1() {
    trimmingOf("s.equals(532)==true")//
        .gives("s.equals(532)");
  }

  @Test public void comaprisonWithBoolean2() {
    trimmingOf("s.equals(532)==false ")//
        .gives("!s.equals(532)");
  }

  @Test public void comaprisonWithBoolean3() {
    trimmingOf("(false==s.equals(532))")//
        .gives("(!s.equals(532))");
  }

  @Test public void comaprisonWithSpecific0() {
    trimmingOf("this != a")//
        .gives("a != this");
  }

  @Test public void comaprisonWithSpecific0Legibiliy00() {
    final InfixExpression e = i("this != a");
    assert in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS);
    assert !iz.booleanLiteral(right(e));
    assert !iz.booleanLiteral(left(e));
    assert in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS);
  }

  @Test public void comaprisonWithSpecific1() {
    trimmingOf("null != a")//
        .gives("a != null");
  }

  @Test public void comaprisonWithSpecific2() {
    trimmingOf("null != a")//
        .gives("a != null");
    trimmingOf("this == a")//
        .gives("a == this");
    trimmingOf("null == a")//
        .gives("a == null");
    trimmingOf("this >= a")//
        .gives("a <= this");
    trimmingOf("null >= a")//
        .gives("a <= null");
    trimmingOf("this <= a")//
        .gives("a >= this");
    trimmingOf("null <= a")//
        .gives("a >= null");
  }

  @Test public void comaprisonWithSpecific2a() {
    trimmingOf("s.equals(532)==false")//
        .gives("!s.equals(532)");
  }

  @Test public void comaprisonWithSpecific3() {
    trimmingOf("(this==s.equals(532))")//
        .gives("(s.equals(532)==this)");
  }

  @Test public void comaprisonWithSpecific4() {
    trimmingOf("(0 < a)")//
        .gives("(a>0)");
  }

  @Test public void comaprisonWithSpecificInParenthesis() {
    trimmingOf("(null==a)")//
        .gives("(a==null)");
  }

  @Test public void commonPrefixEntirelyIfBranches() {
    trimmingOf("if (s.equals(532)) S.out.close();else S.out.close();")//
        .gives("S.out.close(); ");
  }

  @Test public void commonPrefixIfBranchesInFor() {
    trimmingOf("for (;;) if (a) {i++;j++;j++;} else { i++;j++; i++;}")//
        .gives("for(;;){i++;j++;if(a)j++;else i++;}");
  }

  @Test public void commonSuffixIfBranches() {
    trimmingOf("if (a) { \n" + //
        "++i;\n" + //
        "f();\n" + //
        "} else {\n" + //
        "++j;\n" + //
        "f();\n" + //
        "}")//
            .gives("if (a)  \n" + //
                "++i;\n" + //
                "else \n" + //
                "++j;\n" + //
                "\n" + //
                "f();");//
  }

  @Test public void commonSuffixIfBranchesDisappearingElse() {
    trimmingOf("if (a) { \n" + //
        "++i;\n" + //
        "f();\n" + //
        "} else {\n" + //
        "f();\n" + //
        "}")//
            .gives("if (a)  \n" + //
                "++i;\n" + //
                "\n" + //
                "f();");//
  }

  @Test public void commonSuffixIfBranchesDisappearingThen() {
    trimmingOf("if (a) { \n" + //
        "f();\n" + //
        "} else {\n" + //
        "++j;\n" + //
        "f();\n" + //
        "}")//
            .gives("if (!a)  \n" + //
                "++j;\n" + //
                "\n" + //
                "f();");//
  }

  @Test public void commonSuffixIfBranchesDisappearingThenWithinIf() {
    trimmingOf("if (x)  if (a) { \n" + //
        "f();\n" + //
        "} else {\n" + //
        "++j;\n" + //
        "f();\n" + //
        "} else { h(); ++i; ++j; ++k; if (a) f(); else g(); }")//
            .gives("if (x) { if (!a)  \n" + //
                "++j;\n" + //
                "\n" + //
                "f(); } else { h(); ++i; ++j; ++k;  if (a) f(); else g();}");//
  }

  @Test public void compareWithBoolean00() {
    trimmingOf("a == true")//
        .gives("a");
  }

  @Test public void compareWithBoolean01() {
    trimmingOf("a == false")//
        .gives("!a");
  }

  @Test public void compareWithBoolean10() {
    trimmingOf("true == a")//
        .gives("a");
  }

  @Test public void compareWithBoolean100() {
    trimmingOf("a != true")//
        .gives("!a");
  }

  @Test public void compareWithBoolean100a() {
    trimmingOf("(((a))) != true")//
        .gives("!a");
  }

  @Test public void compareWithBoolean101() {
    trimmingOf("a != false")//
        .gives("a");
  }

  @Test public void compareWithBoolean11() {
    trimmingOf("false == a")//
        .gives("!a");
  }

  @Test public void compareWithBoolean110() {
    trimmingOf("true != a")//
        .gives("!a");
  }

  @Test public void compareWithBoolean111() {
    trimmingOf("false != a")//
        .gives("a");
  }

  @Test public void compareWithBoolean2() {
    trimmingOf("false != false")//
        .gives("false");
  }

  @Test public void compareWithBoolean3() {
    trimmingOf("false != true")//
        .gives("true");
  }

  @Test public void compareWithBoolean4() {
    trimmingOf("false == false")//
        .gives("true");
  }

  @Test public void compareWithBoolean5() {
    trimmingOf("false == true")//
        .gives("false");
  }

  @Test public void compareWithBoolean6() {
    trimmingOf("false != false")//
        .gives("false");
  }

  @Test public void compareWithBoolean7() {
    trimmingOf("true != true")//
        .gives("false");
  }

  @Test public void compareWithBoolean8() {
    trimmingOf("true != false")//
        .gives("true");
  }

  @Test public void compareWithBoolean9() {
    trimmingOf("true != true")//
        .gives("false");
  }

  @Test public void comparison01() {
    trimmingOf("1+2+3<3")//
        .gives("6<3")//
        .stays();
  }

  @Test public void comparison02() {
    trimmingOf("f(2)<a")//
        .stays();
  }

  @Test public void comparison03() {
    trimmingOf("this==null")//
        .stays();
  }

  @Test public void comparison04() {
    trimmingOf("6-7<2+1")//
        .gives("-1<3");
  }

  @Test public void comparison05() {
    trimmingOf("a==11")//
        .stays();
  }

  @Test public void comparison06() {
    trimmingOf("1<102333")//
        .stays();
  }

  @Test public void comparison08() {
    trimmingOf("a==this")//
        .stays();
  }

  @Test public void comparison09() {
    trimmingOf("1+2<3&7+4>2+1")//
        .gives("3<3&11>3");
  }

  @Test public void comparison11() {
    trimmingOf("12==this")//
        .gives("this==12");
  }

  @Test public void comparison12() {
    trimmingOf("1+2<3&7+4>2+1||6-7<2+1")//
        .gives("3<3&11>3||-1<3")//
        .stays();
  }

  @Test public void comparison13() {
    trimmingOf("13455643294<22")//
        .stays();
  }

  @Test public void comparisonWithCharacterConstant() {
    trimmingOf("'d' == s.charAt(i)")//
        .gives("s.charAt(i)=='d'");
  }

  @Test public void compreaeExpressionToExpression() {
    trimmingOf("6 - 7 < 2 + 1   ")//
        .gives("-1<3");
  }

  @Test public void correctSubstitutionInIfAssignment() {
    trimmingOf("int a = 2+3; if (a+b > a << b) a =(((((a *7 << a)))));")//
        .gives("int a=2+3+b>2+3<<b?(2+3)*7<<2+3:2+3;");
  }

  @Test public void declarationAssignmentUpdateWithIncrement() {
    trimmingOf("int a=0; a+=++a;")//
        .stays();
  }

  @Test public void declarationAssignmentUpdateWithPostIncrement() {
    trimmingOf("int a=0; a+=a++;")//
        .stays();
  }

  @Test public void declarationAssignmentWithIncrement() {
    trimmingOf("int a=0; a=++a;")//
        .stays();
  }

  @Test public void declarationAssignmentWithPostIncrement() {
    trimmingOf("int a=0; a=a++;")//
        .stays();
  }

  @Test public void declarationIfAssignment() {
    trimmingOf("    String res = s;\n"//
        + "    if (s.equals(y))\n"//
        + "      res = s + blah;\n"//
        + "    S.h(res);")
            .gives("    String res = s.equals(y) ? s + blah :s;\n"//
                + "    S.h(res);");
  }

  @Test public void declarationIfAssignment3() {
    trimmingOf("int a =2; if (a != 2) a = 3;")//
        .gives("int a = 2 != 2 ? 3 : 2;");
  }

  @Test public void declarationIfAssignment4() {
    trimmingOf("int a =2; if (x) a = 2*a;")//
        .gives("int a = x ? 2*2: 2;");
  }

  @Test public void declarationIfUpdateAssignment() {
    trimmingOf("    String res = s;\n"//
        + "    if (s.equals(y))\n"//
        + "      res += s + blah;\n"//
        + "    S.h(res);")
            .gives("    String res = s.equals(y) ? s + (s + blah) :s;\n"//
                + "    S.h(res);");
  }

  @Test public void declarationIfUsesLaterVariable() {
    trimmingOf("int a=0, b=0;if (b==3)   a=4;")//
        .gives(" int a=0;if(0==3)a=4;") //
        .gives(" int a=0==3?4:0;");
  }

  @Test public void declarationIfUsesLaterVariable1() {
    trimmingOf("int a=0, b=0;if (b==3)   a=4; f();")//
        .stays();
  }

  @Test public void declarationInitializeRightShift() {
    trimmingOf("int a = 3;a>>=2;")//
        .gives("int a = 3 >> 2;");
  }

  @Test public void declarationInitializerReturnAssignment() {
    trimmingOf("int a = 3; return a = 2 * a;")//
        .gives("return 2 * 3;");
  }

  @Test public void declarationInitializerReturnExpression() {
    trimmingOf("String t = Bob + Wants + To + \"Sleep \"; "//
        + "  return (right_now + t);    ")//
            .gives("return(right_now+(Bob+Wants+To+\"Sleep \"));");
  }

  @Test public void declarationInitializesRotate() {
    trimmingOf("int a = 3;a>>>=2;")//
        .gives("int a = 3 >>> 2;");
  }

  @Test public void declarationInitializeUpdateAnd() {
    trimmingOf("int a = 3;a&=2;")//
        .gives("int a = 3 & 2;");
  }

  @Test public void declarationInitializeUpdateAssignment() {
    trimmingOf("int a = 3;a += 2;")//
        .gives("int a = 3+2;");
  }

  @Test public void declarationInitializeUpdateAssignmentFunctionCallWithReuse() {
    trimmingOf("int a = f();a += 2*f();")//
        .gives("int a=f()+2*f();");
  }

  @Test public void declarationInitializeUpdateAssignmentFunctionCallWIthReuse() {
    trimmingOf("int a = x;a += a + 2*f();")//
        .gives("int a=x+(x+2*f());");
  }

  @Test public void declarationInitializeUpdateAssignmentIncrement() {
    trimmingOf("int a = ++i;a += j;")//
        .gives("int a = ++i + j;");
  }

  @Test public void declarationInitializeUpdateAssignmentIncrementTwice() {
    trimmingOf("int a = ++i;a += a + j;")//
        .stays();
  }

  @Test public void declarationInitializeUpdateAssignmentWithReuse() {
    trimmingOf("int a = 3;a += 2*a;")//
        .gives("int a = 3+2*3;");
  }

  @Test public void declarationInitializeUpdateDividies() {
    trimmingOf("int a = 3;a/=2;")//
        .gives("int a = 3 / 2;");
  }

  @Test public void declarationInitializeUpdateLeftShift() {
    trimmingOf("int a = 3;a<<=2;")//
        .gives("int a = 3 << 2;");
  }

  @Test public void declarationInitializeUpdateMinus() {
    trimmingOf("int a = 3;a-=2;")//
        .gives("int a = 3 - 2;");
  }

  @Test public void declarationInitializeUpdateModulo() {
    trimmingOf("int a = 3;a%= 2;")//
        .gives("int a = 3 % 2;");
  }

  @Test public void declarationInitializeUpdatePlus() {
    trimmingOf("int a = 3;a+=2;")//
        .gives("int a = 3 + 2;");
  }

  @Test public void declarationInitializeUpdateTimes() {
    trimmingOf("int a = 3;a*=2;")//
        .gives("int a = 3 * 2;");
  }

  @Test public void declarationInitializeUpdateXor() {
    trimmingOf("int a = 3;a^=2;")//
        .gives("int a = 3 ^ 2;");
  }

  @Test public void declarationInitializeUpdatOr() {
    trimmingOf("int a = 3;a|=2;")//
        .gives("int a = 3 | 2;");
  }

  @Test public void declarationUpdateReturn() {
    trimmingOf("int a = 3; return a += 2;")//
        .gives("return 3 + 2;");
  }

  @Test public void declarationUpdateReturnNone() {
    trimmingOf("int a = f(); return a += 2 * a;")//
        .stays();
  }

  @Test public void declarationUpdateReturnTwice() {
    trimmingOf("int a = 3; return a += 2 * a;")//
        .gives("return 3 + 2 *3 ;");
  }

  @Test public void delcartionIfAssignmentNotPlain() {
    trimmingOf("int a=0;   if (y) a+=3; ")//
        .gives("int a = y ? 0 + 3 : 0;");
  }

  @Test public void doNotConsolidateNewArrayActual() {
    trimmingOf("occupied = new boolean[capacity];\n"//
        + "placeholder = new boolean[capacity];")//
            .stays();
  }

  @Test public void doNotConsolidateNewArraySimplifiedl() {
    trimmingOf("a = new int[1];\n"//
        + "b = new int[1];")//
            .stays();
  }

  @Test public void doNotConsolidatePlainNew() {
    trimmingOf("a = new A();\n"//
        + "b = new B();")//
            .stays();
  }

  @Test public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimmingOf("    @SuppressWarnings int $ = (Class<T>) findClass(className);\n"//
        + "    return $;\n"//
        + "  }")//
            .stays();
  }

  @Test public void doNotInlineWithDeclaration() {
    trimmingOf("  private Class<? extends T> retrieveClazz() throws ClassNotFoundException {\n" + //
        "    nonnull(className);\n" + //
        "    @SuppressWarnings(\"unchecked\") final Class<T> $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }")//
            .stays();
  }

  @Test public void doNotIntroduceDoubleNegation() {
    trimmingOf("!Y ? null :!Z ? null : F")//
        .gives("Y&&Z?F:null");
  }

  @Test public void donotSorMixedTypes() {
    trimmingOf("if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;") //
        .gives("if (1130.94> a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;");
  }

  @Test public void dontELiminateCatchBlock() {
    trimmingOf("try { f(); } catch (Exception e) { } finally {}")//
        .stays();
  }

  @Test public void dontELiminateSwitch() {
    trimmingOf("switch (a) { default: }")//
        .stays();
  }

  @Test public void dontSimplifyCatchBlock() {
    trimmingOf("try { {} ; {} } catch (Exception e) {{} ; {}  } finally {{} ; {}}")//
        .gives(" try {}          catch (Exception e) {}          finally {}");
  }

  @Test public void duplicatePartialIfBranches() {
    trimmingOf("    if (a) {\n"//
        + "      f();\n"//
        + "      g();\n"//
        + "      ++i;\n"//
        + "    } else {\n"//
        + "      f();\n"//
        + "      g();\n"//
        + "      --i;\n"//
        + "    }")
            .gives("   f();\n"//
                + "   g();\n"//
                + "    if (a) \n"//
                + "      ++i;\n"//
                + "    else \n"//
                + "      --i;");
  }

  @Test public void emptyElse() {
    trimmingOf("if (x) b = 3; else ;")//
        .gives("if (x) b = 3;");
  }

  @Test public void emptyElseBlock() {
    trimmingOf("if (x) b = 3; else { ;}")//
        .gives("if (x) b = 3;");
  }

  @Test public void emptyIsNotChangedExpression() {
    trimmingOf("")//
        .stays();
  }

  @Test public void emptyIsNotChangedStatement() {
    trimmingOf("")//
        .stays();
  }

  @Test public void emptyThen1() {
    trimmingOf("if (b) ; else x();")//
        .gives("if (!b) x();");
  }

  @Test public void emptyThen2() {
    trimmingOf("if (b) {;;} else {x() ;}")//
        .gives("if (!b) x();");
  }

  @Test public void factorOutAnd() {
    trimmingOf("(a || b) && (a || c)")//
        .gives("a || b && c");
  }

  @Test public void factorOutOr() {
    trimmingOf("a && b || a && c")//
        .gives("a && (b || c)");
  }

  @Test public void factorOutOr3() {
    trimmingOf("a && b && x  && f() || a && c && y ")//
        .gives("a && (b && x && f() || c && y)");
  }

  @Test public void forLoopBug() {
    trimmingOf("      for (int i = 0;i < s.length();++i)\n"//
        + "       if (s.charAt(i) == 'a')\n"//
        + "          res += 2;\n"//
        + "        else "//
        + "       if (s.charAt(i) == 'd')\n"//
        + "          res -= 1;\n"//
        + "      return res;\n"//
        + " if (b) i = 3;")//
            .gives("      for (int ¢ = 0;¢ < s.length();++¢)\n"//
                + "       if (s.charAt(¢) == 'a')\n"//
                + "          res += 2;\n"//
                + "        else " //
                + "       if (s.charAt(¢) == 'd')\n"//
                + "          res-=1;\n"//
                + "      return res;\n"//
                + " if (b) i = 3;")//
            .gives("      for (int ¢ = 0;¢ < s.length();++¢)\n"//
                + "       if (s.charAt(¢) == 'a')\n"//
                + "          res += 2;\n"//
                + "        else " //
                + "       if (s.charAt(¢) == 'd')\n"//
                + "          res --;\n"//
                + "      return res;\n"//
                + " if (b) i = 3;")//
            .gives("      for (int ¢ = 0;¢ < s.length();++¢)\n"//
                + "       if (s.charAt(¢) == 'a')\n"//
                + "          res += 2;\n"//
                + "        else " //
                + "       if (s.charAt(¢) == 'd')\n"//
                + "          --res;\n"//
                + "      return res;\n"//
                + " if (b) i = 3;")//
            .stays();
  }

  @Test public void IfBarFooElseBazFooExtractDefinedSuffix() {
    trimmingOf("public static void f() {\n"//
        + "  int i = 0;\n"//
        + "  if (f()) {\n"//
        + "    i += 1;\n"//
        + "    System.h('!');\n"//
        + "    System.h('!');\n" + "    ++i;\n"//
        + "  } else {\n"//
        + "    i += 2;\n"//
        + "    System.h('@');\n"//
        + "    System.h('@');\n"//
        + "    ++i;\n"//
        + "  }\n"//
        + "}")//
            .gives("public static void f() {\n"//
                + "  int i = 0;\n"//
                + "  if (f()) {\n"//
                + "    i += 1;\n"//
                + "    System.h('!');\n"//
                + "    System.h('!');\n" + "  } else {\n"//
                + "    i += 2;\n"//
                + "    System.h('@');\n"//
                + "    System.h('@');\n"//
                + "  }\n"//
                + "  ++i;"//
                + "}");
  }

  @Test public void IfBarFooElseBazFooExtractUndefinedSuffix() {
    trimmingOf("public final static final void f() {\n"//
        + "  if (tr()) {\n"//
        + "    int i = 0;\n"//
        + "    System.h(i + 0);\n"//
        + "    ++i;\n" + "  } else {\n"//
        + "    int i = 1;\n"//
        + "    System.h(i * 1);\n"//
        + "    ++i;\n"//
        + "  }\n"//
        + "}");
  }

  @Test public void ifBugSecondTry() {
    trimmingOf(" final int c = 2;\n"//
        + "    if (c == c + 1) {\n"//
        + "      if (c == c + 2)\n"//
        + "        return null;\n"//
        + "      c = f().charAt(3);\n" + "    } else if (Character.digit(c, 16) == -1)\n"//
        + "      return null;\n"//
        + "    return null;")
            .gives("    final int c = 2;\n"//
                + "    if (c != c + 1) {\n"//
                + "      if (Character.digit(c, 16) == -1)\n"//
                + "        return null;\n" + "    } else {\n"//
                + "      if (c == c + 2)\n"//
                + "        return null;\n"//
                + "      c = f().charAt(3);\n"//
                + "    }\n" + "    return null;");//
  }

  @Test public void ifBugSimplified() {
    trimmingOf("    if (x) {\n"//
        + "      if (z)\n"//
        + "        return null;\n"//
        + "      c = f().charAt(3);\n"//
        + "    } else if (y)\n"//
        + "      return;\n")
            .gives("    if (!x) {\n"//
                + "      if (y)\n"//
                + "        return;\n"//
                + "    } else {\n"//
                + "      if (z)\n"//
                + "        return null;\n" + "      c = f().charAt(3);\n"//
                + "    }\n");//
  }

  @Test public void ifBugWithPlainEmptyElse() {
    trimmingOf("      if (z)\n"//
        + "        f();\n"//
        + "      else\n"//
        + "         ; \n")//
            .gives("      if (z)\n"//
                + "        f();\n");//
  }

  @Test public void ifDegenerateThenInIf() {
    trimmingOf("if (a) if (b) {} else f(); x();")//
        .gives(" if (a) if (!b) f(); x();");
  }

  @Test public void ifEmptyElsewWithinIf() {
    trimmingOf("if (a) if (b) {;;;f();} else {}")//
        .gives("if(a&&b){;;;f();}");
  }

  @Test public void ifEmptyThenThrow() {
    trimmingOf("if (b) {\n"//
        + " /* empty */"//
        + "} else {\n"//
        + " throw new Excpetion();\n"//
        + "}")//
            .gives("if (!b) "//
                + "  throw new Excpetion();");
  }

  @Test public void ifEmptyThenThrowVariant() {
    trimmingOf("if (b) {\n"//
        + " /* empty */"//
        + "; \n"//
        + "} // no else \n"//
        + " throw new Exception();\n")//
            .gives("  throw new Exception();");
  }

  @Test public void ifEmptyThenThrowWitinIf() {
    trimmingOf("if (x) if (b) {\n"//
        + " /* empty */"//
        + "} else {\n"//
        + " throw new Excpetion();\n"//
        + "} else { f();f();f();f();f();f();f();f();}")
            .gives("if (x) { if (!b) \n"//
                + "  throw new Excpetion();"//
                + "} else { f();f();f();f();f();f();f();f();}");
  }

  @Test public void ifFunctionCall() {
    trimmingOf("if (x) f(a); else f(b);")//
        .gives("f(x ? a: b);");
  }

  @Test public void ifPlusPlusPost() {
    trimmingOf("if (x) a++; else b++;")//
        .gives("if(x)++a;else++b;");
  }

  @Test public void ifPlusPlusPostExpression() {
    trimmingOf("x? a++:b++")//
        .stays();
  }

  @Test public void ifPlusPlusPre() {
    trimmingOf("if (x) ++a; else ++b;")//
        .stays();
  }

  @Test public void ifPlusPlusPreExpression() {
    trimmingOf("x? ++a:++b")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer00() {
    trimmingOf("for(;;){if (a) return; break;}a = 3;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer01() {
    trimmingOf("if (a) throw e; break;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer02() {
    trimmingOf("if (a) break; break;")//
        .gives("break;");
  }

  @Test public void ifSequencerNoElseSequencer03() {
    trimmingOf("if (a) continue; break;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer04() {
    trimmingOf("if (a) break; return;")//
        .gives("if (!a) return; break;");
  }

  @Test public void ifSequencerNoElseSequencer04a() {
    trimmingOf("for (;;) {if (a) break; return;} a =3;")//
        .gives("for (;;) {if (!a) return; break;}a=3;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer05() {
    trimmingOf("for(;;){if(a){x();return;}continue;} a=2;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer05a() {
    trimmingOf("if (a) {x(); return;} continue; a=3;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer06() {
    trimmingOf("if (a) throw e; break;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer07() {
    trimmingOf("if (a) break; throw e;")//
        .gives("if (!a) throw e; break;");
  }

  @Test public void ifSequencerNoElseSequencer08() {
    trimmingOf("if (a) throw e; continue;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer09() {
    trimmingOf("if (a) break; throw e;")//
        .gives("if (!a) throw e; break;");
  }

  @Test public void ifSequencerNoElseSequencer10() {
    trimmingOf("if (a) continue; return;")//
        .gives("if (!a) return; continue;");
  }

  @Test public void ifSequencerThenSequencer0() {
    trimmingOf("if (a) return 4; else break;")//
        .gives("if (a) return 4; break;");
  }

  @Test public void ifSequencerThenSequencer1() {
    trimmingOf("if (a) break; else return 2;")//
        .gives("if (!a) return 2; break;");
  }

  @Test public void ifSequencerThenSequencer3() {
    trimmingOf("if (a) return 10; else continue;")//
        .gives("if (a) return 10; continue;");
  }

  @Test public void ifSequencerThenSequencer4() {
    trimmingOf("if (a) continue; else return 2;")//
        .gives("if (!a) return 2; continue;");
  }

  @Test public void ifSequencerThenSequencer5() {
    trimmingOf("if (a) throw e; else break;")//
        .gives("if (a) throw e; break;");
  }

  @Test public void ifSequencerThenSequencer6() {
    trimmingOf("if (a) break; else throw e;")//
        .gives("if (!a) throw e; break;");
  }

  @Test public void ifSequencerThenSequencer7() {
    trimmingOf("if (a) throw e; else continue;")//
        .gives("if (a) throw e; continue;");
  }

  @Test public void ifSequencerThenSequencer8() {
    trimmingOf("if (a) break; else throw e;")//
        .gives("if (!a) throw e; break;");
  }

  @Test public void ifThrowNoElseThrow() {
    trimmingOf("if (!(e.getCause() instanceof Error))\n"//
        + "  throw e;\n"//
        + "throw (Error) e.getCause();")//
            .gives(" throw !(e.getCause()instanceof Error)?e:(Error)e.getCause();");//
  }

  @Test public void ifWithCommonNotInBlock() {
    trimmingOf("for (;;) if (a) {i++;j++;f();} else { i++;j++; g();}")//
        .gives("for(;;){i++;j++;if(a)f();else g();}");
  }

  @Test public void ifWithCommonNotInBlockDegenerate() {
    trimmingOf("for (;;) if (a) {i++; f();} else { i++;j++; }")//
        .gives("for(;;){i++; if(a)f(); else j++;}");
  }

  @Test public void ifWithCommonNotInBlockiLongerElse() {
    trimmingOf("for (;;) if (a) {i++;j++;f();} else { i++;j++;  f(); h();}")//
        .gives("for(;;){i++;j++; f(); if(!a) h();}");
  }

  @Test public void ifWithCommonNotInBlockiLongerThen() {
    trimmingOf("for (;;) if (a) {i++;j++;f();} else { i++;j++; }")//
        .gives("for(;;){i++;j++; if(a)f();}");
  }

  @Test public void ifWithCommonNotInBlockNothingLeft() {
    trimmingOf("for (;;) if (a) {i++;j++;} else { i++;j++; }")//
        .gives("for(;;){i++;j++;}");
  }

  @Test public void infiniteLoopBug1() {
    trimmingOf("static boolean hasAnnotation(final VariableDeclarationFragment zet) {\n" + //
        "      return hasAnnotation((VariableDeclarationStatement) f.getParent());\n" + //
        "    }")//
            .stays();
  }

  @Test public void infiniteLoopBug2() {
    trimmingOf(" static boolean hasAnnotation(final VariableDeclarationStatement n, int abcd) {\n" + //
        "      return hasAnnotation(n.modifiers());\n" + //
        "    }")//
            .gives(" static boolean hasAnnotation(final VariableDeclarationStatement s, int abcd) {\n" + //
                "      return hasAnnotation(s.modifiers());\n" + //
                "    }");
  }

  @Test public void infiniteLoopBug3() {
    trimmingOf("  boolean f(final VariableDeclarationStatement n) {\n" + //
        "      return false;\n" + //
        "    }")//
            .gives("  boolean f(final VariableDeclarationStatement s) {\n" + //
                "      return false;\n" + //
                "    }");
  }

  @Test public void infiniteLoopBug4() {
    trimmingOf("void f(final VariableDeclarationStatement n) {}")//
        .gives(" void f(final VariableDeclarationStatement s) { }");
  }

  @Test public void initializer101() {
    trimmingOf("int a = b; return a;").gives("return b;")//
        .stays();
  }

  @Test public void inline01() {
    trimmingOf("  public int y() {\n"//
        + "    final Z res = new Z(6);\n"//
        + "    S.h(res.j);\n"//
        + "    return res;\n"//
        + " }\n")//
            .gives(//
                "  public int y() {\n"//
                    + "    final Z $ = new Z(6);\n"//
                    + "    S.h($.j);\n"//
                    + "    return $;\n"//
                    + "  }\n");
  }

  @Test public void inlineArrayInitialization1() {
    trimmingOf("public void multiDimensionalIntArraysAreEqual() {\n"//
        + "  int[][] int1 = {{1, 2, 3}, {4, 5, 6}};\n" + "  int[][] int2 = {{1, 2, 3}, {4, 5, 6}};\n"//
        + "  assertArrayEquals(int1, int2);\n"//
        + "}")//
            .stays();
  }

  @Test public void inlineArrayInitialization2() {
    trimmingOf("public double[] solve() {\n"//
        + "  final SimpleRegression regress = new SimpleRegression(true);\n"//
        + "  for (double[] dxx : points)\n" + "    regress.addData(d[0], d[1]);\n"//
        + "  final double[] $ = { regress.getSlope(), regress.getIntercept() };\n"//
        + "  return $;\n"//
        + "}").stays();
  }

  @Test public void inlineInitializers() {
    trimmingOf("int b,a = 2; return 3 * a * b; ")//
        .gives("return 3*2*b;");
  }

  @Test public void inlineInitializersFirstStep() {
    trimmingOf("int b=4,a = 2; return 3 * a * b; ")//
        .gives("int a = 2; return 3*a*4;");
  }

  @Test public void inlineInitializersSecondStep() {
    trimmingOf("int a = 2; return 3*a*4;")//
        .gives("return 3 * 2 * 4;");
  }

  /** START OF STABLING TESTS */
  @Test public void inlineintoInstanceCreation() {
    trimmingOf("public Statement methodBlock(FrameworkMethod m) {\n"//
        + "  final Statement statement = methodBlock(m);\n" + "  return new Statement() {\n"//
        + "     public void evaluate() throws Throwable {\n"//
        + "       try {\n"//
        + "         statement.evaluate();\n" + "         handleDataPointSuccess();\n"//
        + "       } catch (AssumptionViolatedException e) {\n"//
        + "         handleAssumptionViolation(e);\n" + "       } catch (Throwable e) {\n"//
        + "         reportParameterizedError(e, complete.getArgumentStrings(nullsOk()));\n"//
        + "       }\n" + "     }\n"//
        + "   };\n"//
        + "}")//
            .stays();
  }

  @Test public void inlineintoNextStatementWithSideEffects() {
    trimmingOf("int a = f(); if (a) g(a); else h(u(a));")//
        .stays();
  }

  @Test public void inlineSingleUse07() {
    trimmingOf(
        "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.h(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     S.h(coes.size()); ")
            .stays();
  }

  @Test public void inlineSingleUseKillingVariable() {
    trimmingOf("int a,b=2; a = b;")//
        .gives("int a;a=2;");
  }

  @Test public void inlineSingleUseKillingVariables() {
    trimmingOf("int $, xi=0, xj=0, yi=0, yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")
        .gives(" int $, xj=0, yi=0, yj=0;        if (0>xj==yi>yj)$++;else $--;");
  }

  @Test public void inlineSingleUseKillingVariablesSimplified() {
    trimmingOf("int $=1,xi=0,xj=0,yi=0,yj=0;  if (xi > xj == yi > yj)    $++;   else    $--;")//
        .gives(" int $=1,xj=0,yi=0,yj=0;       if(0>xj==yi>yj)$++;else $--;")//
        .gives(" int $=1,yi=0,yj=0;            if(0>0==yi>yj)$++;else $--;") //
        .gives(" int $=1,yj=0;                 if(0>0==0>yj)$++;else $--;") //
        .gives(" int $=1;                      if(0>0==0>0)$++;else $--;") //
        .gives(" int $=1;                      if(0>0==0>0)++$;else--$;") //
    ;
  }

  @Test public void inlineSingleUseTrivial() {
    trimmingOf(" int $=1,yj=0;                 if(0>0==yj<0)++$;else--$;") //
        .gives("  int $=1;                      if(0>0==0<0)++$;else--$;") //
    ;
  }

  @Test public void inlineSingleUseVanilla() {
    trimmingOf("int a = f(); if (a) f();")//
        .gives("if (f()) f();");
  }

  @Test public void inlineSingleUseWithAssignment() {
    trimmingOf("int a = 2; while (true) if (f()) f(a); else a = 2;")//
        .stays();
  }

  @Test public void inlineSingleVariableintoPlusPlus() {
    trimmingOf("int $ = 0;  if (a)  ++$;  else --$;")//
        .stays();
  }

  @Test public void inliningWithVariableAssignedTo() {
    trimmingOf("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
    ;
  }

  @Test public void isGreaterTrue() {
    final InfixExpression e = i("f(a,b,c,d,e) * f(a,b,c)");
    assert e != null;
    azzert.that(right(e) + "", is("f(a,b,c)"));
    azzert.that(left(e) + "", is("f(a,b,c,d,e)"));
    final Wring<InfixExpression> s = Toolbox.defaultInstance().find(e);
    assert s != null;
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    assert s.canSuggest(e);
    final Expression e1 = left(e);
    final Expression e2 = right(e);
    assert !hasNull(e1, e2);
    final boolean tokenWiseGreater = metrics.nodesCount(e1) > metrics.nodesCount(e2) + NODES_THRESHOLD;
    assert tokenWiseGreater;
    assert ExpressionComparator.moreArguments(e1, e2);
    assert ExpressionComparator.longerFirst(e);
    assert s.canSuggest(e) : "e=" + e + " s=" + s;
    final ASTNode replacement = ((ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that(replacement + "", is("f(a,b,c) * f(a,b,c,d,e)"));
  }

  @Test public void isGreaterTrueButAlmostNot() {
    final InfixExpression e = i("f(a,b,c,d) * f(a,b,c)");
    assert e != null;
    azzert.that(right(e) + "", is("f(a,b,c)"));
    azzert.that(left(e) + "", is("f(a,b,c,d)"));
    final Wring<InfixExpression> s = Toolbox.defaultInstance().find(e);
    assert s != null;
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    assert s.canSuggest(e);
    final Expression e1 = left(e);
    final Expression e2 = right(e);
    assert !hasNull(e1, e2);
    final boolean tokenWiseGreater = metrics.nodesCount(e1) > metrics.nodesCount(e2) + NODES_THRESHOLD;
    assert !tokenWiseGreater;
    assert ExpressionComparator.moreArguments(e1, e2);
    assert ExpressionComparator.longerFirst(e);
    assert s.canSuggest(e) : "e=" + e + " s=" + s;
    final ASTNode replacement = ((ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that(replacement + "", is("f(a,b,c) * f(a,b,c,d)"));
  }

  @Test public void issue06() {
    trimmingOf("a*-b")//
        .gives("-a * b");
  }

  @Test public void issue06B() {
    trimmingOf("x/a*-b/-c*- - - d / -d")//
        .gives("x/a * b/ c * d/d")//
        .gives("d*x/a*b/c/d");
  }

  @Test public void issue06C1() {
    trimmingOf("a*-b/-c*- - - d / d")//
        .gives("-a * b/ c * d/d");
  }

  @Test public void issue06C4() {
    trimmingOf("-a * b/ c ")//
        .stays();
  }

  @Test public void issue06D() {
    trimmingOf("a*b*c*d*-e")//
        .gives("-a*b*c*d*e")//
        .stays();
  }

  @Test public void issue06E() {
    trimmingOf("-a*b*c*d*f*g*h*i*j*k")//
        .stays();
  }

  @Test public void issue06F() {
    trimmingOf("x*a*-b*-c*- - - d * d")//
        .gives("-x*a*b*c*d*d")//
        .stays();
  }

  @Test public void issue06G() {
    trimmingOf("x*a*-b*-c*- - - d / d")//
        .gives("-x*a*b*c*d/d")//
        .stays();
  }

  @Test public void issue06H() {
    trimmingOf("x/a*-b/-c*- - - d ")//
        .gives("-x/a * b/ c * d")//
    ;
  }

  @Test public void issue06I() {
    trimmingOf("41 * - 19")//
        .gives("-779 ") //
    ;
  }

  @Test public void issue06J() {
    trimmingOf("41 * a * - 19")//
        .gives("-41*a*19")//
        .gives("-41*19*a") //
    ;
  }

  @Test public void issue110_01() {
    trimmingOf("polite ? \"Eat your meal.\" :  \"Eat your meal, please\"") //
        .gives("\"Eat your meal\" + (polite ? \".\" : \", please\")");
  }

  @Test public void issue110_02() {
    trimmingOf("polite ? \"Eat your meal.\" :  \"Eat your meal\"") //
        .gives("\"Eat your meal\" + (polite ? \".\" : \"\")");
  }

  @Test public void issue110_03() {
    trimmingOf("polite ? \"thanks for the meal\" :  \"I hated the meal\"") //
        .gives("!polite ? \"I hated the meal\": \"thanks for the meal\"") //
        .gives("(!polite ? \"I hated\" : \"thanks for\" )+ \" the meal\"");
  }

  @Test public void issue110_04() {
    trimmingOf("polite ? \"thanks.\" :  \"I hated the meal.\"")//
        .gives("(polite ? \"thanks\" :  \"I hated the meal\")+\".\"");
  }

  @Test public void issue110_05() {
    trimmingOf("a ? \"abracadabra\" : \"abba\"") //
        .gives("!a ? \"abba\" : \"abracadabra\"")//
        .stays();
  }

  @Test public void issue110_06() {
    trimmingOf("receiver ==null ? \"Use \" + \"x\" : \"Use \" + receiver")//
        .gives("\"Use \"+(receiver==null ? \"x\" : receiver)")//
        .stays();
  }

  @Test public void issue110_07() {
    trimmingOf("receiver ==null ? \"Use x\" : \"Use \" + receiver")//
        .gives("\"Use \"+(receiver==null ? \"x\" : \"\"+receiver)");
  }

  @Test public void issue110_08() {
    trimmingOf("receiver ==null ? \"Use\" : receiver + \"Use\"")//
        .gives("(receiver==null ? \"\" : receiver+\"\") + \"Use\"")//
        .stays();
  }

  @Test public void issue110_09() {
    trimmingOf("receiver ==null ? \"user a\" : receiver + \"something a\"")//
        .gives("(receiver==null ? \"user\" : receiver+\"something\") + \" a\"")//
        .stays();
  }

  @Test public void issue110_10() {
    trimmingOf("receiver ==null ? \"Something Use\" : \"Something\" + receiver + \"Use\"")//
        .gives("\"Something\"+ (receiver==null ? \" Use\" : \"\"+receiver + \"Use\")")//
        .gives("\"Something\"+ ((receiver==null ? \" \" : \"\"+receiver+\"\") + \"Use\")");
  }

  @Test public void issue110_11() {
    trimmingOf("f() ? \"first\" + d() + \"second\" : \"first\" + g() + \"third\"")//
        .gives("\"first\" + (f() ? \"\" + d()  + \"second\" : \"\" + g()  + \"third\")");
  }

  @Test public void issue110_12() {
    trimmingOf("f() ? \"first\" + d() + \"second\" : \"third\" + g() + \"second\"")//
        .gives("(f() ? \"first\" +  d() + \"\": \"third\" + g()+\"\") + \"second\"");
  }

  @Test public void issue110_13() {
    trimmingOf("f() ? \"first is:\" + d() + \"second\" : \"first are:\" + g() + \"and second\"")//
        .gives("\"first\" + (f() ? \" is:\" + d() + \"second\": \" are:\" + g() + \"and second\")")//
        .gives("\"first\" + ((f() ? \" is:\" + d() + \"\": \" are:\" + g() + \"and \") + \"second\")");
  }

  @Test public void issue110_14() {
    trimmingOf("x == null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\"")//
        .gives("\"Use \" + (x==null ? \"isEmpty()\" : \"\"+x +  \".isEmpty()\")")//
        .gives("\"Use \" + ((x==null ? \"\" : \"\"+ x +  \".\")+\"isEmpty()\")");
  }

  @Test public void issue110_15() {
    trimmingOf("$.setName(b.simpleName(booleanLiteral ? \"TRU\" : \"TALS\"));")//
        .stays();
  }

  @Test public void issue110_16() {
    trimmingOf("$.setName(b.simpleName(booleanLiteral ? \"TRUE\" : \"FALSE\"));")//
        .stays();
  }

  @Test public void issue110_17() {
    trimmingOf("$.setName(b.simpleName(booleanLiteral ? \"TRUE Story\" : \"FALSE Story\"));")
        .gives("$.setName(b.simpleName((booleanLiteral ? \"TRUE\" : \"FALSE\")+\" Story\"));");
  }

  @Test public void issue110_18() {
    trimmingOf("booleanLiteral==0 ? \"asss\" : \"assfad\"")//
        .stays();
  }

  @Test public void issue141_01() {
    trimmingOf("public static void go(final Object os[], final String... ss) {  \n"//
        + "for (final String saa : ss) \n"//
        + "out(saa);  \n"//
        + "out(\"elements\", os);   \n"//
        + "}")//
            .stays();
  }

  @Test public void issue141_02() {
    trimmingOf("public static void go(final List<Object> os, final String... ss) {  \n"//
        + "for (final String saa : ss) \n"//
        + "out(saa);  \n"//
        + "out(\"elements\", os);   \n" //
        + "}")//
            .stays();
  }

  @Test public void issue141_03() {
    trimmingOf("public static void go(final String ss[],String abracadabra) {  \n"//
        + "for (final String a : ss) \n"//
        + "out(a);  \n" + "out(\"elements\",abracadabra);   \n"//
        + "}")//
            .stays();
  }

  @Test public void issue141_04() {
    trimmingOf("public static void go(final String ss[]) {  \n"//
        + "for (final String a : ss) \n"//
        + "out(a);  \n"//
        + "out(\"elements\");   \n"//
        + "}").stays();
  }

  @Test public void issue141_05() {
    trimmingOf("public static void go(final String s[]) {  \n"//
        + "for (final String a : s) \n"//
        + "out(a);  \n"//
        + "out(\"elements\");   \n"//
        + "}")
            .gives("public static void go(final String ss[]) {  \n"//
                + "for (final String a : ss) \n"//
                + "out(a);  \n"//
                + "out(\"elements\");   \n"//
                + "}")
            .stays();
  }

  @Test public void issue141_06() {
    trimmingOf("public static void go(final String s[][][]) {  \n"//
        + "for (final String a : s) \n"//
        + "out(a);  \n"//
        + "out(\"elements\");   \n"//
        + "}")
            .gives("public static void go(final String ssss[][][]) {  \n"//
                + "for (final String a : ssss) \n"//
                + "out(a);  \n"//
                + "out(\"elements\");   \n" + "}")
            .stays();
  }

  @Test public void issue141_07() {
    trimmingOf("public static void go(final Stringssssss ssss[]) {  \n"//
        + "for (final Stringssssss a : ssss) \n"//
        + "out(a);  \n" + "out(\"elements\");   \n"//
        + "}")
            .gives("public static void go(final Stringssssss ss[]) {  \n"//
                + "for (final Stringssssss a : ss) \n"//
                + "out(a);  \n" + "out(\"elements\");   \n"//
                + "}")
            .stays();
  }

  @Test public void issue141_08() {
    trimmingOf("public static void go(final Integer ger[]) {  \n"//
        + "for (final Integer a : ger) \n"//
        + "out(a);  \n"//
        + "out(\"elements\");   \n"//
        + "}")
            .gives("public static void go(final Integer is[]) {  \n"//
                + "for (final Integer a : is) \n"//
                + "out(a);  \n"//
                + "out(\"elements\");   \n"//
                + "}")
            .stays();
  }

  @Test public void issue21a() {
    trimmingOf("a.equals(\"a\")")//
        .gives("\"a\".equals(a)");
  }

  @Test public void issue21b() {
    trimmingOf("a.equals(\"ab\")")//
        .gives("\"ab\".equals(a)");
  }

  @Test public void issue21d() {
    trimmingOf("a.equalsIgnoreCase(\"a\")")//
        .gives("\"a\".equalsIgnoreCase(a)");
  }

  @Test public void issue21e() {
    trimmingOf("a.equalsIgnoreCase(\"ab\")")//
        .gives("\"ab\".equalsIgnoreCase(a)");
  }

  @Test public void issue37Simplified() {
    trimmingOf("    int a = 3;\n"//
        + "    a = 31 * a;")//
            .gives("int a = 31 * 3; ");
  }

  @Test public void issue37SimplifiedVariant() {
    trimmingOf("    int a = 3;\n"//
        + "    a += 31 * a;")//
            .gives("int a=3+31*3;");
  }

  @Test public void issue37WithSimplifiedBlock() {
    trimmingOf("if (a) { {} ; if (b) f(); {} } else { g(); f(); ++i; ++j; }")//
        .gives(" if (a) {  if (b) f(); } else { g(); f(); ++i; ++j; }");
  }

  @Test public void issue38() {
    trimmingOf("    return o == null ? null\n" + //
        "        : o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
        "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
        "                : null;")//
            .stays();
  }

  @Test public void issue38Simplfiied() {
    trimmingOf(//
        "         o == CONDITIONAL_AND ? CONDITIONAL_OR \n" + //
            "            : o == CONDITIONAL_OR ? CONDITIONAL_AND \n" + //
            "                : null")//
                .stays();
  }

  @Test public void issue39base() {
    trimmingOf("if (name == null) {\n"//
        + "    if (other.name != null)\n"//
        + "        return false;\n"//
        + "} else if (!name.equals(other.name))\n" + "    return false;\n"//
        + "return true;")//
            .stays(); //
  }

  public void issue39baseDual() {
    trimmingOf("if (name != null) {\n" + //
        "    if (!name.equals(other.name))\n" + //
        "        return false;\n" + //
        "} else if (other.name != null)\n" + //
        "    return false;\n" + //
        "return true;")//
            .gives("if (name == null) {\n"//
                + "    if (other.name != null)\n"//
                + "        return false;\n"//
                + "} else if (!name.equals(other.name))\n" + "    return false;\n"//
                + "return true;");
  }

  @Test(timeout = 100) public void issue39versionA() {
    trimmingOf("if (varArgs) {\n"//
        + "    if (argumentTypes.length < parameterTypes.length - 1) {\n"//
        + "        return false;\n"//
        + "    }\n" + "} else if (parameterTypes.length != argumentTypes.length) {\n"//
        + "    return false;\n"//
        + "}")
            .gives("if (!varArgs) {\n"//
                + "    if (parameterTypes.length != argumentTypes.length) {\n"//
                + "        return false;\n"//
                + "    }\n" + "} else if (argumentTypes.length < parameterTypes.length - 1) {\n"//
                + "    return false;\n"//
                + "}");
  }

  public void issue39versionAdual() {
    trimmingOf("if (!varArgs) {\n"//
        + "    if (parameterTypes.length != argumentTypes.length) {\n"//
        + "        return false;\n"//
        + "    }\n" + "} else if (argumentTypes.length < parameterTypes.length - 1) {\n"//
        + "    return false;\n"//
        + "}").stays();
  }

  @Test public void issue41FunctionCall() {
    trimmingOf("int a = f();a += 2;")//
        .gives("int a = f()+2;");
  }

  @Test public void issue43() {
    trimmingOf("String t = Z2;  "//
        + " t = t.f(A).f(b) + t.f(c);   "//
        + "return (t + 3);    ")
            .gives("String t = Z2.f(A).f(b) + Z2.f(c);"//
                + "return (t + 3);");
  }

  @Test public void issue46() {
    trimmingOf("int f() {\n"//
        + "  x++;\n"//
        + "  y++;\n"//
        + "  if (a) {\n"//
        + "     i++; \n"//
        + "     j++; \n"//
        + "     k++;\n"//
        + "  }\n"//
        + "}")//
            .gives("int f() {\n"//
                + "  ++x;\n"//
                + "  ++y;\n"//
                + "  if (!a)\n"//
                + "    return;\n"//
                + "  ++i;\n"//
                + "  ++j; \n"//
                + "  ++k;\n"//
                + "}");
  }

  @Test public void issue49() {
    trimmingOf("int f() { int f = 0; for (int i: X) $ += f(i); return f;}")//
        .gives("int f(){int $=0;for(int i:X)$+=f(i);return $;}");
  }

  @Test public void issue51() {
    trimmingOf("int f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .gives("int f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void issue51g() {
    trimmingOf("abstract abstract interface a"//
        + "{}")//
            .gives("interface a {}");
  }

  @Test public void issue53() {
    trimmingOf( //
        "int[] is = f(); for (int i: is) f(i);")//
            .gives("for (int i: f()) f(i);");
  }

  @Test public void issue53a() {
    trimmingOf( //
        "int f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
            .gives("int f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void issue54DoNonSideEffect() {
    trimmingOf( //
        "int a  = f; do { b[i] = a; } while (b[i] != a);")//
            .gives("do { b[i] = f; } while (b[i] != f);");
  }

  @Test public void issue54DoNonSideEffectEmptyBody() {
    trimmingOf( //
        "int a = f(); do ; while (a != 1);")//
            .stays();
  }

  @Test public void issue54DoWhile() {
    trimmingOf( //
        "int a  = f(); do { b[i] = 2; ++i; } while (b[i] != a);")//
            .stays();
  }

  @Test public void issue54DoWithBlock() {
    trimmingOf( //
        "int a  = f(); do { b[i] = a;  ++i; } while (b[i] != a);")//
            .stays();
  }

  @Test public void issue54doWithoutBlock() {
    trimmingOf("int a  = f(); do b[i] = a; while (b[i] != a);")//
        .stays();
  }

  @Test public void issue54ForEnhanced() {
    trimmingOf("int a  = f(); for (int i: a) b[i] = x;")//
        .gives(" for (int i: f()) b[i] = x;");
  }

  @Test public void issue54ForEnhancedNonSideEffectLoopHeader() {
    trimmingOf("int a  = f; for (int i: a) b[i] = b[i-1];")//
        .gives("for (int i: f) b[i] = b[i-1];");
  }

  @Test public void issue54ForEnhancedNonSideEffectWithBody() {
    trimmingOf("int a  = f; for (int i: j) b[i] = a;")//
        .gives(" for(int i:j)b[i]=f; ");
  }

  @Test public void issue54ForPlain() {
    trimmingOf("int a  = f(); for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .gives("for (int i = 0; i < 100;  ++i) b[i] = f();")//
        .gives("for (int ¢ = 0; ¢ < 100;  ++¢) b[¢] = f();")//
        .stays();
  }

  @Test public void issue54ForPlainNonSideEffect() {
    trimmingOf("int a  = f; for (int i = 0; i < 100;  ++i) b[i] = a;")//
        .gives("for (int i = 0; i < 100;  ++i) b[i] = f;");
  }

  @Test public void issue54ForPlainUseInConditionNonSideEffect() {
    trimmingOf("int a  = f; for (int i = 0; a < 100;  ++i) b[i] = 3;")//
        .gives("for (int i = 0; f < 100;  ++i) b[i] = 3;");
  }

  @Test public void issue54ForPlainUseInInitializerNonSideEffect() {
    trimmingOf("int a  = f; for (int i = a; i < 100; i *= a) b[i] = 3;")//
        .gives(" for (int i = f; i < 100; i *= f) b[i] = 3;");
  }

  @Test public void issue54ForPlainUseInUpdatersNonSideEffect() {
    trimmingOf("int a  = f; for (int i = 0; i < 100; i *= a) b[i] = 3;")//
        .gives("for (int i = 0; i < 100; i *= f) b[i] = 3;");
  }

  @Test public void issue54While() {
    trimmingOf("int a  = f(); while (c) b[i] = a;")//
        .stays();
  }

  @Test public void issue54WhileNonSideEffect() {
    trimmingOf("int a  = f; while (c) b[i] = a;")//
        .gives("while (c) b[i] = f;");
  }

  @Test public void issue54WhileScopeDoesNotInclude() {
    included("int a  = f(); while (c) b[i] = a;", VariableDeclarationFragment.class)//
        .notIn(new DeclarationInitializerStatementTerminatingScope());
  }

  @Test public void issue57a() {
    trimmingOf("void m(List<Expression>... expressions) { }")//
        .gives("void m(List<Expression>... xss) {}");
  }

  @Test public void issue57b() {
    trimmingOf("void m(Expression... expression) { }")//
        .gives("void m(Expression... xs) {}");
  }

  @Test public void issue58a() {
    trimmingOf("X f(List<List<Expression>> expressions){}")//
        .gives("X f(List<List<Expression>> xss){}");
  }

  @Test public void issue58b() {
    trimmingOf("X f(List<Expression>[] expressions){}")//
        .gives("X f(List<Expression>[] xss){}");
  }

  @Test public void issue58c() {
    trimmingOf("X f(List<Expression>[] expressions){}")//
        .gives("X f(List<Expression>[] xss){}");
  }

  @Test public void issue58d() {
    trimmingOf("X f(List<Expression>... expressions){}")//
        .gives("X f(List<Expression>... xss){}");
  }

  @Test public void issue58e() {
    trimmingOf("X f(Expression[]... expressions){}")//
        .gives("X f(Expression[]... xss){}");
  }

  @Test public void issue58f() {
    trimmingOf("X f(Expression[][]... expressions){}")//
        .gives("X f(Expression[][]... xsss){}");
  }

  @Test public void issue58g() {
    trimmingOf("X f(List<Expression[][]>... expressions){}")//
        .gives("X f(List<Expression[][]>... xssss){}");
  }

  @Test public void issue62a() {
    trimmingOf("int f(int ixx) { for(;;++ixx) if(false) break; return ixx; }")//
        .gives("int f(int ixx) { for(;;++ixx){} return ixx; }")//
        .stays();
  }

  @Test public void issue62b_1() {
    trimmingOf("int f(int ixx) { for(;ixx<100;ixx=ixx+1) if(false) break; return ixx; }")//
        .gives("int f(int ixx) { for(;ixx<100;ixx+=1){} return ixx; }")//
        .stays();// ixx is not provably not String.
  }

  @Test public void issue62c() {
    trimmingOf("int f(int ixx) { while(++ixx > 999) if(ixx>99) break; return ixx;}")//
        .stays();
  }

  @Test public void issue64a() {
    trimmingOf("void f() {" + //
        "    final int a = f();\n" + //
        "    new Object() {\n" + //
        "      @Override public int hashCode() { return a; }\n" + //
        "    };"//
        + "}")//
            .stays();
  }

  @Test public void issue73a() {
    trimmingOf("void foo(StringBuilder sb) {}")//
        .gives("void foo(StringBuilder b) {}");
  }

  @Test public void issue73b() {
    trimmingOf("void foo(DataOutput dataOutput) {}")//
        .gives("void foo(DataOutput o) {}");
  }

  @Test public void issue73c() {
    trimmingOf("void foo(Integer integer, ASTNode astn) {}")//
        .gives("void foo(Integer i, ASTNode astn) {}");
  }

  @Test public void issue74d() {
    trimmingOf("int[] a = new int[] {2,3};")//
        .stays();
  }

  @Test public void linearTransformation() {
    trimmingOf("plain * the + kludge")//
        .gives("the*plain+kludge");
  }

  @Test public void literalVsLiteral() {
    trimmingOf("1 < 102333")//
        .stays();
  }

  @Test public void longChainComparison() {
    trimmingOf("a == b == c == d")//
        .stays();
  }

  @Test public void longChainParenthesisComparison() {
    trimmingOf("(a == b == c) == d")//
        .stays();
  }

  @Test public void longChainParenthesisNotComparison() {
    trimmingOf("(a == b == c) != d")//
        .stays();
  }

  @Test public void longerChainParenthesisComparison() {
    trimmingOf("(a == b == c == d == e) == d")//
        .stays();
  }

  @Test public void massiveInlining() {
    trimmingOf("int a,b,c;String t = zE4;if (2 * 3.1415 * 180 > a || t.concat(sS) ==1922 && t.length() > 3)    return c > 5;")//
        .gives("int a,b,c;if(2 * 3.1415 * 180>a||zE4.concat(sS)==1922&&zE4.length()>3)return c>5;");
  }

  @Test public void methodWithLastIf() {
    trimmingOf("int f() { if (a) { f(); g(); h();}}")//
        .gives("int f() { if (!a) return;  f(); g(); h();}");
  }

  @Test public void nestedIf1() {
    trimmingOf("if (a) if (b) i++;")//
        .gives("if (a && b) i++;");
  }

  @Test public void nestedIf2() {
    trimmingOf("if (a) if (b) i++; else ; else ; ")//
        .gives("if (a && b) i++; else ;");
  }

  @Test public void nestedIf3() {
    trimmingOf("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}");
  }

  @Test public void nestedIf33() {
    trimmingOf("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .gives(" if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .gives(" if(x){if(a&&b)i++;}else{++y;f();g();}")//
        .gives(" if(x){if(a&&b)++i;}else{++y;f();g();}")//
    ;
  }

  @Test public void nestedIf33a() {
    trimmingOf("if (x) { if (a && b) i++; } else { y++; f(); g(); }")//
        .gives(" if (x) {if(a&&b)++i;} else{++y;f();g();}");
  }

  @Test public void nestedIf33b() {
    trimmingOf("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3c() {
    trimmingOf("if (x) if (a && b) i++; else; else { y++; f(); g(); }")//
        .gives(" if(x) {if(a&&b)i++;} else {++y;f();g();}");
  }

  @Test public void nestedIf3d() {
    trimmingOf("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .gives("if(x)if(a&&b)i++;else; else{++y;f();g();z();}") //
        .gives("if(x){if(a&&b)i++;} else{++y;f();g();z();}") //
        .gives("if(x){if(a&&b)++i;} else{++y;f();g();z();}") //
    ;
  }

  @Test public void nestedIf3e() {
    trimmingOf("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .gives(" if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .gives(" if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }

  @Test public void nestedIf3f() {
    trimmingOf("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .gives(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .gives(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3f1() {
    trimmingOf(" if(x)if(a&&b)i++; else; else{++y;f();g();}") //
        .gives(" if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3x() {
    trimmingOf("if (x) if (a) if (b) i++; else ; else ; else { y++; f(); g(); z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}") //
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();z();}") //
    ;
  }

  @Test public void nestedTernaryAlignment() {
    trimmingOf("int b=3==4?5==3?2:3:5==3?2:3*3;")//
        .gives("int b=3==4?5==3?2:3:5!=3?3*3:2;");
  }

  @Test public void noChange() {
    trimmingOf("12")//
        .stays();
    trimmingOf("true")//
        .stays();
    trimmingOf("null")//
        .stays();
    trimmingOf("on*of*no*notion*notion")//
        .gives("no*of*on*notion*notion");
  }

  @Test public void noChange0() {
    trimmingOf("kludge + the * plain ")//
        .stays();
  }

  @Test public void noChange1() {
    trimmingOf("the * plain")//
        .stays();
  }

  @Test public void noChange2() {
    trimmingOf("plain + kludge")//
        .stays();
  }

  @Test public void noChangeA() {
    trimmingOf("true")//
        .stays();
  }

  @Test public void noinliningintoSynchronizedStatement() {
    trimmingOf("int a  = f(); synchronized(this) { int b = a; }")//
        .stays();
  }

  @Test public void noinliningintoSynchronizedStatementEvenWithoutSideEffect() {
    trimmingOf("int a  = f; synchronized(this) { int b = a; }")//
        .stays();
  }

  @Test public void noinliningintoTryStatement() {
    trimmingOf("int a  = f(); try { int b = a; } catch (Exception E) {}")//
        .stays();
  }

  @Test public void noinliningintoTryStatementEvenWithoutSideEffect() {
    trimmingOf("int a  = f; try { int b = a; } catch (Exception E) {}")//
        .stays();
  }

  @Test public void notOfAnd() {
    trimmingOf("!(A && B)")//
        .gives("!A || !B");
  }

  @Test public void oneMultiplication() {
    trimmingOf("f(a,b,c,d) * f(a,b,c)")//
        .gives("f(a,b,c) * f(a,b,c,d)");
  }

  @Test public void oneMultiplicationAlternate() {
    trimmingOf("f(a,b,c,d,e) * f(a,b,c)")//
        .gives("f(a,b,c) * f(a,b,c,d,e)");
  }

  @Test public void orFalse3ORTRUE() {
    trimmingOf("false || false || false")//
        .gives("false");
  }

  @Test public void orFalse4ORTRUE() {
    trimmingOf("false || false || false || false")//
        .gives("false");
  }

  @Test public void orFalseANDOf3WithoutBoolean() {
    trimmingOf("a && b && false")//
        .stays();
  }

  @Test public void orFalseANDOf3WithoutBooleanA() {
    trimmingOf("x && a && b")//
        .stays();
  }

  @Test public void orFalseANDOf3WithTrue() {
    trimmingOf("true && x && true && a && b")//
        .gives("x && a && b");
  }

  @Test public void orFalseANDOf3WithTrueA() {
    trimmingOf("a && b && true")//
        .gives("a && b");
  }

  @Test public void orFalseANDOf4WithoutBoolean() {
    trimmingOf("a && b && c && false")//
        .stays();
  }

  @Test public void orFalseANDOf4WithoutBooleanA() {
    trimmingOf("x && a && b && c")//
        .stays();
  }

  @Test public void orFalseANDOf4WithTrue() {
    trimmingOf("x && true && a && b && c")//
        .gives("x && a && b && c");
  }

  @Test public void orFalseANDOf4WithTrueA() {
    trimmingOf("a && b && c && true")//
        .gives("a && b && c");
  }

  @Test public void orFalseANDOf5WithoutBoolean() {
    trimmingOf("false && a && b && c && d")//
        .stays();
  }

  @Test public void orFalseANDOf5WithoutBooleanA() {
    trimmingOf("x && a && b && c && d")//
        .stays();
  }

  @Test public void orFalseANDOf5WithTrue() {
    trimmingOf("x && a && b && c && true && true && true && d")//
        .gives("x && a && b && c && d");
  }

  @Test public void orFalseANDOf5WithTrueA() {
    trimmingOf("true && a && b && c && d")//
        .gives("a && b && c && d");
  }

  @Test public void orFalseANDOf6WithoutBoolean() {
    trimmingOf("a && b && c && false && d && e")//
        .stays();
  }

  @Test public void orFalseANDOf6WithoutBooleanA() {
    trimmingOf("x && a && b && c && d && e")//
        .stays();
  }

  @Test public void orFalseANDOf6WithoutBooleanWithParenthesis() {
    trimmingOf("(x && (a && b)) && (c && (d && e))")//
        .stays();
  }

  @Test public void orFalseANDOf6WithTrue() {
    trimmingOf("x && a && true && b && c && d && e")//
        .gives("x && a && b && c && d && e");
  }

  @Test public void orFalseANDOf6WithTrueA() {
    trimmingOf("a && b && c && true && d && e")//
        .gives("a && b && c && d && e");
  }

  @Test public void orFalseANDOf6WithTrueWithParenthesis() {
    trimmingOf("x && (true && (a && b && true)) && (c && (d && e))")//
        .gives("x && a && b && c && d && e");
  }

  @Test public void orFalseANDOf7WithMultipleTrueValue() {
    trimmingOf("(a && (b && true)) && (c && (d && (e && (true && true))))")//
        .gives("a &&b &&c &&d &&e ");
  }

  @Test public void orFalseANDOf7WithoutBooleanAndMultipleFalseValue() {
    trimmingOf("(a && (b && false)) && (c && (d && (e && (false && false))))")//
        .stays();
  }

  @Test public void orFalseANDOf7WithoutBooleanWithParenthesis() {
    trimmingOf("(a && b) && (c && (d && (e && false)))")//
        .stays();
  }

  @Test public void orFalseANDOf7WithTrueWithParenthesis() {
    trimmingOf("true && (a && b) && (c && (d && (e && true)))")//
        .gives("a &&b &&c &&d &&e ");
  }

  @Test public void orFalseANDWithFalse() {
    trimmingOf("b && a")//
        .stays();
  }

  @Test public void orFalseANDWithoutBoolean() {
    trimmingOf("b && a")//
        .stays();
  }

  @Test public void orFalseANDWithTrue() {
    trimmingOf("true && b && a")//
        .gives("b && a");
  }

  @Test public void orFalseFalseOrFalse() {
    trimmingOf("false ||false")//
        .gives("false");
  }

  @Test public void orFalseORFalseWithSomething() {
    trimmingOf("true || a")//
        .stays();
  }

  @Test public void orFalseORFalseWithSomethingB() {
    trimmingOf("false || a || false")//
        .gives("a");
  }

  @Test public void orFalseOROf3WithFalse() {
    trimmingOf("x || false || b")//
        .gives("x || b");
  }

  @Test public void orFalseOROf3WithFalseB() {
    trimmingOf("false || a || b || false")//
        .gives("a || b");
  }

  @Test public void orFalseOROf3WithoutBoolean() {
    trimmingOf("a || b")//
        .stays();
  }

  @Test public void orFalseOROf3WithoutBooleanA() {
    trimmingOf("x || a || b")//
        .stays();
  }

  @Test public void orFalseOROf4WithFalse() {
    trimmingOf("x || a || b || c || false")//
        .gives("x || a || b || c");
  }

  @Test public void orFalseOROf4WithFalseB() {
    trimmingOf("a || b || false || c")//
        .gives("a || b || c");
  }

  @Test public void orFalseOROf4WithoutBoolean() {
    trimmingOf("a || b || c")//
        .stays();
  }

  @Test public void orFalseOROf4WithoutBooleanA() {
    trimmingOf("x || a || b || c")//
        .stays();
  }

  @Test public void orFalseOROf5WithFalse() {
    trimmingOf("x || a || false || c || d")//
        .gives("x || a || c || d");
  }

  @Test public void orFalseOROf5WithFalseB() {
    trimmingOf("a || b || c || d || false")//
        .gives("a || b || c || d");
  }

  @Test public void orFalseOROf5WithoutBoolean() {
    trimmingOf("a || b || c || d")//
        .stays();
  }

  @Test public void orFalseOROf5WithoutBooleanA() {
    trimmingOf("x || a || b || c || d")//
        .stays();
  }

  @Test public void orFalseOROf6WithFalse() {
    trimmingOf("false || x || a || b || c || d || e")//
        .gives("x || a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithFalseWithParenthesis() {
    trimmingOf("x || (a || (false) || b) || (c || (d || e))")//
        .gives("x || a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithFalseWithParenthesisB() {
    trimmingOf("(a || b) || false || (c || false || (d || e || false))")//
        .gives("a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithoutBoolean() {
    trimmingOf("a || b || c || d || e")//
        .stays();
  }

  @Test public void orFalseOROf6WithoutBooleanA() {
    trimmingOf("x || a || b || c || d || e")//
        .stays();
  }

  @Test public void orFalseOROf6WithoutBooleanWithParenthesis() {
    trimmingOf("(a || b) || (c || (d || e))")//
        .stays();
  }

  @Test public void orFalseOROf6WithoutBooleanWithParenthesisA() {
    trimmingOf("x || (a || b) || (c || (d || e))")//
        .stays();
  }

  @Test public void orFalseOROf6WithTwoFalse() {
    trimmingOf("a || false || b || false || c || d || e")//
        .gives("a || b || c || d || e");
  }

  @Test public void orFalseORSomethingWithFalse() {
    trimmingOf("false || a || false")//
        .gives("a");
  }

  @Test public void orFalseORSomethingWithTrue() {
    trimmingOf("a || true")//
        .stays();
  }

  @Test public void orFalseORWithoutBoolean() {
    trimmingOf("b || a")//
        .stays();
  }

  @Test public void orFalseProductIsNotANDDivOR() {
    trimmingOf("2*a")//
        .stays();
  }

  @Test public void orFalseTrueAndTrueA() {
    trimmingOf("true && true")//
        .gives("true");
  }

  @Test public void overridenDeclaration() {
    trimmingOf("int a = 3; a = f() ? 3 : 4;")//
        .gives("int a = f() ? 3: 4;");
  }

  @Test public void paramAbbreviateBasic1() {
    trimmingOf("void m(XMLDocument xmlDocument, int abcd) {" + //
        "xmlDocument.exec(p);}")//
            .gives("void m(XMLDocument d, int abcd) {" + //
                "d.exec(p);}");
  }

  @Test public void paramAbbreviateBasic2() {
    trimmingOf("int m(StringBuilder builder, int abcd) {" + //
        "if(builder.exec())" + //
        "builder.clear();")//
            .gives("int m(StringBuilder b, int abcd) {" + //
                "if(b.exec())" + //
                "b.clear();");
  }

  @Test public void paramAbbreviateCollision() {
    trimmingOf("void m(Expression exp, Expression expresssion) { }")//
        .gives("void m(Expression x, Expression expresssion) { }");
  }

  @Test public void paramAbbreviateConflictingWithLocal1() {
    trimmingOf("void m(String string) {" + //
        "String s = null;" + //
        "string.substring(s, 2, 18);}")//
            .gives("void m(String string){string.substring(null,2,18);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal1Simplified() {
    trimmingOf("void m(String string) {" + //
        "String s = X;" + //
        "string.substring(s, 2, 18);}")//
            .gives("void m(String string){string.substring(X,2,18);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal1SimplifiedFurther() {
    trimmingOf("void m(String string) {" + //
        "String s = X;" + //
        "string.f(s);}")//
            .gives("void m(String string){string.f(X);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal2() {
    trimmingOf("TCPConnection conn(TCPConnection tcpCon) {" + //
        " UDPConnection c = new UDPConnection(57);" + //
        " if(tcpCon.isConnected()) " + //
        "   c.disconnect();}")//
            .gives("TCPConnection conn(TCPConnection tcpCon){" //
                + " if(tcpCon.isConnected())" //
                + "   (new UDPConnection(57)).disconnect();"//
                + "}");
  }

  @Test public void paramAbbreviateConflictingWithMethodName() {
    trimmingOf("void m(BitmapManipulator bitmapManipulator, int abcd) {" + //
        "bitmapManipulator.x().y();")//
            .stays();
  }

  @Test public void paramAbbreviateMultiple() {
    trimmingOf("void m(StringBuilder stringBuilder, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
        "stringBuilder.clear();" + //
        "xmlDocument.open(stringBuilder.toString());" + //
        "dog.eat(xmlDocument.asEdible(cat));}")//
            .gives("void m(StringBuilder b, XMLDocument xmlDocument, Dog dog, Dog cat) {" + //
                "b.clear();" + //
                "xmlDocument.open(b.toString());" + //
                "dog.eat(xmlDocument.asEdible(cat));}");
  }

  @Test public void paramAbbreviateNestedMethod() {
    trimmingOf("void f(Iterator iterator) {" + //
        "iterator = new Iterator<Object>() {" + //
        "int i = 0;" + //
        "@Override public boolean hasNext() { return false; }" + //
        "@Override public Object next() { return null; } };")//
            .gives("void f(Iterator i) {" + //
                "i = new Iterator<Object>() {" + //
                "int i = 0;" + //
                "@Override public boolean hasNext() { return false; }" + //
                "@Override public Object next() { return null; } };");
  }

  @Test public void parenthesizeOfpushdownTernary() {
    trimmingOf("a ? b+x+e+f:b+y+e+f")//
        .gives("b+(a ? x : y)+e+f");
  }

  @Test public void postDecreementReturn() {
    trimmingOf("a--; return a;")//
        .gives("--a;return a;");
  }

  @Test public void postDecremntInFunctionCall() {
    trimmingOf("f(a++, i--, b++, ++b);")//
        .stays();
  }

  @Test public void postfixToPrefix101() {
    trimmingOf("i++;").gives("++i;")//
        .stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopCondition() {
    trimmingOf("for (int s = i; ++i; ++s);")//
        .stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopInitializer() {
    trimmingOf("for (int s = i++; i < 10; ++s) sum+=s;")//
        .gives("for (int ¢ = i++; i < 10; ++¢) sum+=¢;")//
        .stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnVariableDeclaration() {
    // We expect to print 2, but ++s will make it print 3
    trimmingOf("int s = 2;" + //
        "int n = s++;" + //
        "S.out.print(n);")//
            .gives("int s=2;S.out.print(s++);");
  }

  @Test public void postIncrementInFunctionCall() {
    trimmingOf("f(i++);")//
        .stays();
  }

  @Test public void postIncrementReturn() {
    trimmingOf("a++; return a;")//
        .gives("++a;return a;");
  }

  @Test public void preDecreementReturn() {
    trimmingOf("--a.b.c; return a.b.c;")//
        .gives("return--a.b.c;");
  }

  @Test public void preDecrementReturn() {
    trimmingOf("--a; return a;")//
        .gives("return --a;");
  }

  @Test public void preDecrementReturn1() {
    trimmingOf("--this.a; return this.a;")//
        .gives("return --this.a;");
  }

  @Test public void prefixToPosfixIncreementSimple() {
    trimmingOf("i++")//
        .gives("++i");
  }

  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  j--;";
    final Statement s = s(from);
    azzert.that(s, iz("{" + from + "}"));
    assert s != null;
    final PostfixExpression e = findFirst.postfixExpression(s);
    assert e != null;
    azzert.that(e, iz("i--"));
    final ASTNode parent = e.getParent();
    assert parent != null;
    azzert.that(parent, iz(from));
    azzert.that(parent, is(not(instanceOf(Expression.class))));
    azzert.that(new PostfixToPrefix().canSuggest(e), is(true));
    azzert.that(new PostfixToPrefix().canSuggest(e), is(true));
    final Expression r = new PostfixToPrefix().replacement(e);
    azzert.that(r, iz("--i"));
    trimmingOf(from)//
        .gives("for(int i=0;i<100;--i)--j;") //
        .stays();
  }

  @Test public void prefixToPostfixDecrementEssence() {
    trimmingOf("for(int i=0;i< 100;i--)j--;")//
        .gives("for(int i=0;i<100;--i)--j;") //
        .stays();
  }

  @Test public void prefixToPostfixIncreement() {
    trimmingOf("for (int i = 0; i < 100; i++) i++;")//
        .gives("for(int ¢=0;¢<100;¢++)¢++;") //
        .gives("for(int ¢=0;¢<100;++¢)++¢;") //
        .stays();
  }

  @Test public void preIncrementReturn() {
    trimmingOf("++a; return a;")//
        .gives("return ++a;");
  }

  @Test public void pushdowConditionalActualExampleFirstPass() {
    trimmingOf("return determineEncoding(bytes) == Encoding.B "//
        + "? f((ENC_WORD_PREFIX + mimeCharset + B), text, charset, bytes)\n" + ": f((ENC_WORD_PREFIX + mimeCharset + Q), text, charset, bytes)\n"//
        + ";")
            .gives("return f("//
                + "   determineEncoding(bytes)==Encoding.B"//
                + "     ? ENC_WORD_PREFIX+mimeCharset+B" + "     : ENC_WORD_PREFIX+mimeCharset+Q,"//
                + "text,charset,bytes)"//
                + ";");
  }

  @Test public void pushdowConditionalActualExampleSecondtest() {
    trimmingOf("return f("//
        + "   determineEncoding(bytes)==Encoding.B"//
        + "     ? ENC_WORD_PREFIX+mimeCharset+B" + "     : ENC_WORD_PREFIX+mimeCharset+Q,"//
        + "text,charset,bytes)"//
        + ";")
            .gives("return f("//
                + "  ENC_WORD_PREFIX + mimeCharset + "//
                + " (determineEncoding(bytes)==Encoding.B ?B : Q),"//
                + "   text,charset,bytes" + ")"//
                + ";");
  }

  @Test public void pushdownNot2LevelNotOfFalse() {
    trimmingOf("!!false")//
        .gives("false");
  }

  @Test public void pushdownNot2LevelNotOfTrue() {
    trimmingOf("!!true")//
        .gives("true");
  }

  @Test public void pushdownNotActualExample() {
    trimmingOf("!inRange(m, e)")//
        .stays();
  }

  @Test public void pushdownNotDoubleNot() {
    trimmingOf("!!f()")//
        .gives("f()");
  }

  @Test public void pushdownNotDoubleNotDeeplyNested() {
    trimmingOf("!(((!f())))")//
        .gives("f()");
  }

  @Test public void pushdownNotDoubleNotNested() {
    trimmingOf("!(!f())")//
        .gives("f()");
  }

  @Test public void pushdownNotEND() {
    trimmingOf("a&&b")//
        .stays();
  }

  @Test public void pushdownNotMultiplication() {
    trimmingOf("a*b")//
        .stays();
  }

  @Test public void pushdownNotNotOfAND() {
    trimmingOf("!(a && b && c)")//
        .gives("!a || !b || !c");
  }

  @Test public void pushdownNotNotOfAND2() {
    trimmingOf("!(f() && f(5))")//
        .gives("!f() || !f(5)");
  }

  @Test public void pushdownNotNotOfANDNested() {
    trimmingOf("!(f() && (f(5)))")//
        .gives("!f() || !f(5)");
  }

  @Test public void pushdownNotNotOfEQ() {
    trimmingOf("!(3 == 5)")//
        .gives("3 != 5");
  }

  @Test public void pushdownNotNotOfEQNested() {
    trimmingOf("!((((3 == 5))))")//
        .gives("3 != 5");
  }

  @Test public void pushdownNotNotOfFalse() {
    trimmingOf("!false")//
        .gives("true");
  }

  @Test public void pushdownNotNotOfGE() {
    trimmingOf("!(3 >= 5)")//
        .gives("3 < 5");
  }

  @Test public void pushdownNotNotOfGT() {
    trimmingOf("!(3 > 5)")//
        .gives("3 <= 5");
  }

  @Test public void pushdownNotNotOfLE() {
    trimmingOf("!(3 <= 5)")//
        .gives("3 > 5");
  }

  @Test public void pushdownNotNotOfLT() {
    trimmingOf("!(3 < 5)")//
        .gives("3 >= 5");
  }

  @Test public void pushdownNotNotOfNE() {
    trimmingOf("!(3 != 5)")//
        .gives("3 == 5");
  }

  @Test public void pushdownNotNotOfOR() {
    trimmingOf("!(a || b || c)")//
        .gives("!a && !b && !c");
  }

  @Test public void pushdownNotNotOfOR2() {
    trimmingOf("!(f() || f(5))")//
        .gives("!f() && !f(5)");
  }

  @Test public void pushdownNotNotOfTrue() {
    trimmingOf("!true")//
        .gives("false");
  }

  @Test public void pushdownNotNotOfTrue2() {
    trimmingOf("!!true")//
        .gives("true");
  }

  @Test public void pushdownNotNotOfWrappedOR() {
    trimmingOf("!((a) || b || c)")//
        .gives("!a && !b && !c");
  }

  @Test public void pushdownNotOR() {
    trimmingOf("a||b")//
        .stays();
  }

  @Test public void pushdownNotSimpleNot() {
    trimmingOf("!a")//
        .stays();
  }

  @Test public void pushdownNotSimpleNotOfFunction() {
    trimmingOf("!f(a)")//
        .stays();
  }

  @Test public void pushdownNotSummation() {
    trimmingOf("a+b")//
        .stays();
  }

  @Test public void pushdownTernaryActualExample() {
    trimmingOf("next < values().length")//
        .stays();
  }

  @Test public void pushdownTernaryActualExample2() {
    trimmingOf("!inRange(m, e) ? true : inner.go(r, e)")//
        .gives("!inRange(m, e) || inner.go(r, e)");
  }

  @Test public void pushdownTernaryAlmostIdentical2Addition() {
    trimmingOf("a ? b+d :b+ c")//
        .gives("b+(a ? d : c)");
  }

  @Test public void pushdownTernaryAlmostIdentical3Addition() {
    trimmingOf("a ? b+d +x:b+ c + x")//
        .gives("b+(a ? d : c) + x");
  }

  @Test public void pushdownTernaryAlmostIdentical4AdditionLast() {
    trimmingOf("a ? b+d+e+y:b+d+e+x")//
        .gives("b+d+e+(a ? y : x)");
  }

  @Test public void pushdownTernaryAlmostIdentical4AdditionSecond() {
    trimmingOf("a ? b+x+e+f:b+y+e+f")//
        .gives("b+(a ? x : y)+e+f");
  }

  @Test public void pushdownTernaryAlmostIdenticalAssignment() {
    trimmingOf("a ? (b=c) :(b=d)")//
        .gives("b = a ? c : d");
  }

  @Test public void pushdownTernaryAlmostIdenticalFunctionCall() {
    trimmingOf("a ? f(b) :f(c)")//
        .gives("f(a ? b : c)");
  }

  @Test public void pushdownTernaryAlmostIdenticalMethodCall() {
    trimmingOf("a ? y.f(b) :y.f(c)")//
        .gives("y.f(a ? b : c)");
  }

  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall1Div2() {
    trimmingOf("a ? f(b,x) :f(c,x)")//
        .gives("f(a ? b : c,x)");
  }

  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall2Div2() {
    trimmingOf("a ? f(x,b) :f(x,c)")//
        .gives("f(x,a ? b : c)");
  }

  @Test public void pushdownTernaryAMethodCallDistinctReceiver() {
    trimmingOf("a ? x.f(c) : y.f(d)")//
        .stays();
  }

  @Test public void pushdownTernaryDifferentTargetFieldRefernce() {
    trimmingOf("a ? 1 + x.a : 1 + y.a")//
        .gives("1+(a ? x.a : y.a)");
  }

  @Test public void pushdownTernaryFieldReferneceShort() {
    trimmingOf("a ? R.b.c : R.b.d")//
        .stays();
  }

  @Test public void pushdownTernaryFunctionCall() {
    trimmingOf("a ? f(b,c) : f(c)")//
        .gives("!a?f(c):f(b,c)");
  }

  @Test public void pushdownTernaryFX() {
    trimmingOf("a ? false : c")//
        .gives("!a && c");
  }

  @Test public void pushdownTernaryIdenticalAddition() {
    trimmingOf("a ? b+d :b+ d")//
        .gives("b+d");
  }

  @Test public void pushdownTernaryIdenticalAdditionWtihParenthesis() {
    trimmingOf("a ? (b+d) :(b+ d)")//
        .gives("b+d");
  }

  @Test public void pushdownTernaryIdenticalAssignment() {
    trimmingOf("a ? (b=c) :(b=c)")//
        .gives("b = c");
  }

  @Test public void pushdownTernaryIdenticalAssignmentVariant() {
    trimmingOf("a ? (b=c) :(b=d)")//
        .gives("b=a?c:d");
  }

  @Test public void pushdownTernaryIdenticalFunctionCall() {
    trimmingOf("a ? f(b) :f(b)")//
        .gives("f(b)");
  }

  @Test public void pushdownTernaryIdenticalIncrement() {
    trimmingOf("a ? b++ :b++")//
        .gives("b++");
  }

  @Test public void pushdownTernaryIdenticalMethodCall() {
    trimmingOf("a ? y.f(b) :y.f(b)")//
        .gives("y.f(b)");
  }

  @Test public void pushdownTernaryintoConstructor1Div1Location() {
    trimmingOf("a.equal(b) ? new S(new Integer(4)) : new S(new Ineger(3))")//
        .gives("new S(a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void pushdownTernaryintoConstructor1Div3() {
    trimmingOf("a.equal(b) ? new S(new Integer(4),a,b) : new S(new Ineger(3),a,b)")//
        .gives("new S(a.equal(b)? new Integer(4): new Ineger(3), a, b)");
  }

  @Test public void pushdownTernaryintoConstructor2Div3() {
    trimmingOf("a.equal(b) ? new S(a,new Integer(4),b) : new S(a, new Ineger(3), b)")//
        .gives("new S(a,a.equal(b)? new Integer(4): new Ineger(3),b)");
  }

  @Test public void pushdownTernaryintoConstructor3Div3() {
    trimmingOf("a.equal(b) ? new S(a,b,new Integer(4)) : new S(a,b,new Ineger(3))")//
        .gives("new S(a, b, a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void pushdownTernaryintoConstructorNotSameArity() {
    trimmingOf("a ? new S(a,new Integer(4),b) : new S(new Ineger(3))")//
        .gives(
            "!a?new S(new Ineger(3)):new S(a,new Integer(4),b)                                                                                                                  ");
  }

  @Test public void pushdownTernaryintoPrintln() {
    trimmingOf("    if (s.equals(t))\n"//
        + "      S.h(Hey + res);\n"//
        + "    else\n"//
        + "      S.h(Ho + x + a);")//
            .gives("S.h(s.equals(t)?Hey+res:Ho+x+a);");
  }

  @Test public void pushdownTernaryLongFieldRefernece() {
    trimmingOf("externalImage ? R.string.webview_contextmenu_image_download_action : R.string.webview_contextmenu_image_save_action")
        .gives("!externalImage ? R.string.webview_contextmenu_image_save_action : R.string.webview_contextmenu_image_download_action");
  }

  @Test public void pushdownTernaryMethodInvocationFirst() {
    trimmingOf("a?b():c")//
        .gives("!a?c:b()");
  }

  @Test public void pushdownTernaryNoBoolean() {
    trimmingOf("a?b:c")//
        .stays();
  }

  @Test public void pushdownTernaryNoReceiverReceiver() {
    trimmingOf("a < b ? f() : a.f()")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnMINUS() {
    trimmingOf("a ? -c :-d")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnMINUSMINUS1() {
    trimmingOf("a ? --c :--d")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnMINUSMINUS2() {
    trimmingOf("a ? c-- :d--")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnNOT() {
    trimmingOf("a ? !c :!d")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnPLUS() {
    trimmingOf("a ? +x : +y")//
        .gives("a ? x : y")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnPLUSPLUS() {
    trimmingOf("a ? x++ :y++")//
        .stays();
  }

  @Test public void pushdownTernaryNotSameFunctionInvocation() {
    trimmingOf("a?b(x):d(x)")//
        .stays();
  }

  @Test public void pushdownTernaryNotSameFunctionInvocation2() {
    trimmingOf("a?x.f(x):x.d(x)")//
        .stays();
  }

  @Test public void pushdownTernaryOnMethodCall() {
    trimmingOf("a ? y.f(c,b) :y.f(c)")//
        .gives("!a?y.f(c):y.f(c,b)");
  }

  @Test public void pushdownTernaryParFX() {
    trimmingOf("a ?( false):true")//
        .gives("!a && true");
  }

  @Test public void pushdownTernaryParTX() {
    trimmingOf("a ? (((true ))): c")//
        .gives("a || c");
  }

  @Test public void pushdownTernaryParXF() {
    trimmingOf("a ? b : (false)")//
        .gives("a && b");
  }

  @Test public void pushdownTernaryParXT() {
    trimmingOf("a ? b : ((true))")//
        .gives("!a || b");
  }

  @Test public void pushdownTernaryReceiverNoReceiver() {
    trimmingOf("a < b ? a.f() : f()")//
        .gives("a>=b?f():a.f()");
  }

  @Test public void pushdownTernaryToClasConstrctor() {
    trimmingOf("a ? new B(a,b,c) : new B(a,x,c)")//
        .gives("new B(a,a ? b : x ,c)");
  }

  @Test public void pushdownTernaryToClasConstrctorTwoDifferenes() {
    trimmingOf("a ? new B(a,b,c) : new B(a,x,y)")//
        .stays();
  }

  @Test public void pushdownTernaryToClassConstrctorNotSameNumberOfArgument() {
    trimmingOf("a ? new B(a,b) : new B(a,b,c)")//
        .stays();
  }

  @Test public void pushdownTernaryTX() {
    trimmingOf("a ? true : c")//
        .gives("a || c");
  }

  @Test public void pushdownTernaryXF() {
    trimmingOf("a ? b : false")//
        .gives("a && b");
  }

  @Test public void pushdownTernaryXT() {
    trimmingOf("a ? b : true")//
        .gives("!a || b");
  }

  @Test public void redundantButNecessaryBrackets1() {
    trimmingOf("if (windowSize != INFINITE_WINDOW) {\n"//
        + "  if (getN() == windowSize)\n"//
        + "    eDA.addElementRolling(v);\n" + "  else if (getN() < windowSize)\n"//
        + "    eDA.addElement(v);\n"//
        + "} else {\n"//
        + "  System.h('!');\n"//
        + "  System.h('!');\n" + "  System.h('!');\n"//
        + "  System.h('!');\n"//
        + "  System.h('!');\n"//
        + "  System.h('!');\n"//
        + "  System.h('!');\n"//
        + "  eDA.addElement(v);\n" + "}")//
            .stays();
  }

  @Test public void redundantButNecessaryBrackets2() {
    trimmingOf("if (windowSize != INFINITE_WINDOW) {\n"//
        + "  if (getN() == windowSize)\n"//
        + "    eDA.addElementRolling(v);\n"//
        + "} else {\n" + "  System.h('!');\n"//
        + "  System.h('!');\n"//
        + "  System.h('!');\n"//
        + "  System.h('!');\n"//
        + "  System.h('!');\n"//
        + "  System.h('!');\n" + "  System.h('!');\n"//
        + "  eDA.addElement(v);\n"//
        + "}")//
            .stays();
  }

  @Test public void redundantButNecessaryBrackets3() {
    trimmingOf("if (b1)\n"//
        + "  if (b2)\n"//
        + "    print1('!');\n"//
        + "  else {\n"//
        + "    if (b3)\n"//
        + "      print3('#');\n"//
        + "  }\n"//
        + "else {\n" + "  print4('$');\n"//
        + "  print4('$');\n"//
        + "  print4('$');\n"//
        + "  print4('$');\n"//
        + "  print4('$');\n"//
        + "  print4('$');\n" + "  print4('$');\n"//
        + "  print4('$');\n"//
        + "  print4('$');\n"//
        + "  print4('$');\n"//
        + "  print4('$');\n"//
        + "}")//
            .stays();
  }

  @Test public void removeSuper() {
    trimmingOf("class T {T(){super();}}")//
        .gives("class T { T() { }}");
  }

  @Test public void removeSuperWithArgument() {
    trimmingOf("class T { T() { super(a); a();}}")//
        .stays();
  }

  @Test public void removeSuperWithReceiver() {
    trimmingOf("class X{X(Y o){o.super();}}")//
        .stays();
  }

  @Test public void removeSuperWithStatemen() {
    trimmingOf("class T { T() { super(); a++;}}")//
        .gives("class T { T() { ++a;}}");
  }

  @Test public void renameToDollarActual() {
    trimmingOf(//
        "        public static DeletePolicy fromInt(int initialSetting) {\n" + //
            "            for (DeletePolicy policy: values()) {\n" + //
            "                if (policy.setting == initialSetting) {\n" + //
            "                    return policy;\n" + //
            "                }\n" + //
            "            }\n" + //
            "            throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");\n" + //
            "        }")//
                .gives(//
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
    trimmingOf("int f() { for (int a: as) return a; }")//
        .gives(" int f() {for(int $:as)return $;}");
  }

  @Test public void renameUnusedVariableToDoubleUnderscore1() {
    trimmingOf("void f(int x) {System.h(x);}")//
        .stays();
  }

  @Test public void renameUnusedVariableToDoubleUnderscore2() {
    trimmingOf("void f(int x) {}")//
        .stays();
  }

  @Test public void renameUnusedVariableToDoubleUnderscore3() {
    trimmingOf("void f(@SuppressWarnings({\"unused\"}) int x) {}")//
        .gives("void f(@SuppressWarnings({\"unused\"}) int __){}");
  }

  @Test public void renameUnusedVariableToDoubleUnderscore4() {
    trimmingOf("void f(int x, @SuppressWarnings(\"unused\") int y) {}")//
        .gives("void f(int x, @SuppressWarnings(\"unused\") int __) {}");
  }

  @Test public void renameUnusedVariableToDoubleUnderscore5() {
    trimmingOf("void f(int x, @SuppressWarnings @SuppressWarnings(\"unused\") int y) {}")//
        .gives("void f(int x, @SuppressWarnings @SuppressWarnings(\"unused\") int __) {}");
  }

  @Test public void renameVariableUnderscore1() {
    trimmingOf("void f(int _) {System.h(_);}")//
        .gives("void f(int __) {System.h(__);}");
  }

  @Test public void replaceInitializationInReturn() {
    trimmingOf("int a = 3; return a + 4;")//
        .gives("return 3 + 4;");
  }

  @Test public void replaceTwiceInitializationInReturn() {
    trimmingOf("int a = 3; return a + 4 << a;")//
        .gives("return 3 + 4 << 3;");
  }

  @Test public void rightSimplificatioForNulNNVariableReplacement() {
    final InfixExpression e = i("null != a");
    final Wring<InfixExpression> w = Toolbox.defaultInstance().find(e);
    assert w != null;
    assert w.canSuggest(e);
    assert w.canSuggest(e);
    final ASTNode replacement = ((ReplaceCurrentNode<InfixExpression>) w).replacement(e);
    assert replacement != null;
    azzert.that(replacement + "", is("a != null"));
  }

  @Test public void rightSipmlificatioForNulNNVariable() {
    azzert.that(Toolbox.defaultInstance().find(i("null != a")), instanceOf(InfixComparisonSpecific.class));
  }

  @Test public void sequencerFirstInElse() {
    trimmingOf("if (a) {b++; c++; ++d;} else { f++; g++; return x;}")//
        .gives("if (!a) {f++; g++; return x;} b++; c++; ++d; ");
  }

  @Test public void shorterChainParenthesisComparison() {
    trimmingOf("a == b == c")//
        .stays();
  }

  @Test public void shorterChainParenthesisComparisonLast() {
    trimmingOf("b == a * b * c * d * e * f * g * h == a")//
        .stays();
  }

  @Test public void shortestBranchIfWithComplexNestedIf3() {
    trimmingOf("if (a) {f(); g(); h();} else if (a) ++i; else ++j;")//
        .stays();
  }

  @Test public void shortestBranchIfWithComplexNestedIf4() {
    trimmingOf("if (a) {f(); g(); h(); ++i;} else if (a) ++i; else j++;")//
        .gives("if(!a)if(a)++i;else j++;else{f();g();h();++i;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf5() {
    trimmingOf("if (a) {f(); g(); h(); ++i; f();} else if (a) ++i; else j++;")//
        .gives("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf7() {
    trimmingOf("if (a) {f(); ++i; g(); h(); ++i; f(); j++;} else if (a) ++i; else j++;")
        .gives("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf8() {
    trimmingOf("if (a) {f(); ++i; g(); h(); ++i; u++; f(); j++;} else if (a) ++i; else j++;")
        .gives("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;u++;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIfPlain() {
    trimmingOf("if (a) {f(); g(); h();} else { i++; j++;}")//
        .gives("if(!a){i++;j++;}else{f();g();h();}");
  }

  @Test public void shortestBranchIfWithComplexSimpler() {
    trimmingOf("if (a) {f(); g(); h();} else  i++; j++;")//
        .gives("if(!a)i++;else{f();g();h();}++j;");
  }

  @Test public void shortestBranchInIf() {
    trimmingOf("   int a=0;\n"//
        + "   if (s.equals(known)){\n"//
        + "     S.console();\n"//
        + "   } else {\n"//
        + "     a=3;\n"//
        + "   }\n").gives("int a=0; if(!s.equals(known))a=3;else S.console();");
  }

  @Test public void shortestFirstAlignment() {
    trimmingOf("n.isSimpleName() ? (SimpleName) n //\n" + //
        "            : n.isQualifiedName() ? ((QualifiedName) n).getName() //\n" + //
        "                : null")//
            .stays();//
  }

  @Test public void shortestFirstAlignmentShortened() {
    trimmingOf("n.isF() ? (SimpleName) n \n"//
        + //
        "            : n.isG() ? ((QualifiedName) n).getName() \n"//
        + //
        "                : null")//
            .stays();//
  }

  @Test public void shortestFirstAlignmentShortenedFurther() {
    trimmingOf("n.isF() ? (A) n : n.isG() ? ((B) n).f() \n"//
        + //
        "                : null")//
            .stays();//
  }

  @Test public void shortestFirstAlignmentShortenedFurtherAndFurther() {
    trimmingOf("n.isF() ? (A) n : n.isG() ? (B) n :  null")//
        .stays();//
  }

  @Test public void shortestIfBranchFirst01() {
    trimmingOf("if (s.equals(0xDEAD)) {\n"//
        + " int res=0; "//
        + " for (int i=0; i<s.length(); ++i)     "//
        + " if (s.charAt(i)=='a')      "//
        + "   res += 2;    "//
        + "} else "//
        + " if (s.charAt(i)=='d') "//
        + "  res -= 1;  "//
        + "return res;  ")
            .gives("if (!s.equals(0xDEAD)) {"//
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
    trimmingOf("if (!s.equals(0xDEAD)) { "//
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
            .gives(" if (s.equals(0xDEAD)) \n"//
                + "    return 8;"//
                + "      int res = 0;\n"//
                + "      for (int i = 0;i < s.length();++i)\n"//
                + "       if (s.charAt(i) == 'a')\n"//
                + "          res += 2;\n"//
                + "        else "//
                + "       if (s.charAt(i) == 'd')\n"//
                + "          res -= 1;\n"//
                + "      return res;\n");
  }

  @Test public void shortestIfBranchFirst02a() {
    trimmingOf(" if (!s.equals(0xDEAD)) {\n"//
        + "      int res = 0;\n"//
        + "      for (int i = 0;i < s.length();++i)\n"//
        + "       if (s.charAt(i) == 'a')\n"//
        + "          res += 2;\n"//
        + "        else "//
        + "       if (s.charAt(i) == 'd')\n"//
        + "          res -= 1;\n"//
        + "      return res;\n"//
        + "    }\n"//
        + "    return 8;")
            .gives(" if (s.equals(0xDEAD)) "//
                + "return 8; "//
                + "      int res = 0;\n"//
                + "      for (int i = 0;i < s.length();++i)\n"//
                + "       if (s.charAt(i) == 'a')\n"//
                + "          res += 2;\n"//
                + "        else "//
                + "       if (s.charAt(i) == 'd')\n"//
                + "          res -= 1;\n"//
                + "      return res;\n");
  }

  @Test public void shortestIfBranchFirst02b() {
    trimmingOf("      int res = 0;\n"//
        + "      for (int i = 0;i < s.length();++i)\n"//
        + "       if (s.charAt(i) == 'a')\n"//
        + "          res += 2;\n"//
        + "        else "//
        + "       if (s.charAt(i) == 'd')\n"//
        + "          --res;\n"//
        + "      return res;\n")//
            .gives("      int res = 0;\n"//
                + "      for (int ¢ = 0;¢ < s.length();++¢)\n"//
                + "       if (s.charAt(¢) == 'a')\n"//
                + "          res += 2;\n"//
                + "        else "//
                + "       if (s.charAt(¢) == 'd')\n"//
                + "          --res;\n"//
                + "      return res;\n")//
            .stays()//
    ;
  }

  @Test public void shortestIfBranchFirst02c() {
    final CompilationUnit u = Wrap.Statement.intoCompilationUnit("      int res = 0;\n"//
        + "      for (int i = 0;i < s.length();++i)\n"//
        + "       if (s.charAt(i) == 'a')\n"//
        + "          res += 2;\n"//
        + "        else "//
        + "       if (s.charAt(i) == 'd')\n"//
        + "          res -= 1;\n"//
        + "      return res;\n");
    final VariableDeclarationFragment f = findFirst.variableDeclarationFragment(u);
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
    trimmingOf("if (a) {"//
        + " f();"//
        + " g();"//
        + " h();"//
        + " return a;"//
        + "}\n"//
        + "return c;")
            .gives("if (!a) return c;"//
                + "f();"//
                + "g();"//
                + "h();"//
                + "return a;");
  }

  @Test public void shortestOperand01() {
    trimmingOf("x + y > z")//
        .stays();
  }

  @Test public void shortestOperand02() {
    trimmingOf("k = k + 4;if (2 * 6 + 4 == k) return true;")//
        .gives("k += 4;if (12 + 4 == k) return true;");
  }

  @Test public void shortestOperand05() {
    trimmingOf("    W s = new W(\"bob\");\n" + //
        "    return s.l(hZ).l(\"-ba\").toString() == \"bob-ha-banai\";")
            .gives("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }

  @Test public void shortestOperand10() {
    trimmingOf("return b == true;")//
        .gives("return b;");
  }

  @Test public void shortestOperand11() {
    trimmingOf("int h,u,m,a,n;return b == true && n + a > m - u || h > u;")//
        .gives("int h,u,m,a,n;return b&&a+n>m-u||h>u;");
  }

  @Test public void shortestOperand12() {
    trimmingOf("int k = 15; return 7 < k; ")//
        .gives("return 7<15;");
  }

  @Test public void shortestOperand13() {
    trimmingOf("return (2 > 2 + a) == true;")//
        .gives("return 2>a+2;");
  }

  @Test public void shortestOperand13a() {
    trimmingOf("(2 > 2 + a) == true")//
        .gives("2>a+2 ");
  }

  @Test public void shortestOperand13b() {
    trimmingOf("(2) == true")//
        .gives("2 ");
  }

  @Test public void shortestOperand13c() {
    trimmingOf("2 == true")//
        .gives("2 ");
  }

  @Test public void shortestOperand14() {
    trimmingOf("Integer t = new Integer(5);   return (t.toString() == null);    ")//
        .gives("return((new Integer(5)).toString()==null);");
  }

  @Test public void shortestOperand17() {
    trimmingOf("5 ^ a.getNum()")//
        .gives("a.getNum() ^ 5");
  }

  @Test public void shortestOperand19() {
    trimmingOf("k.get().operand() ^ a.get()")//
        .gives("a.get() ^ k.get().operand()");
  }

  @Test public void shortestOperand20() {
    trimmingOf("k.get() ^ a.get()")//
        .gives("a.get() ^ k.get()");
  }

  @Test public void shortestOperand22() {
    trimmingOf("return f(a,b,c,d,e) + f(a,b,c,d) + f(a,b,c) + f(a,b) + f(a) + f();")//
        .stays();
  }

  @Test public void shortestOperand23() {
    trimmingOf("return f() + \".\";     }")//
        .stays();
  }

  @Test public void shortestOperand24() {
    trimmingOf("f(a,b,c,d) & 175 & 0")//
        .gives("f(a,b,c,d) & 0 & 175");
  }

  @Test public void shortestOperand25() {
    trimmingOf("f(a,b,c,d) & bob & 0 ")//
        .gives("bob & f(a,b,c,d) & 0");
  }

  @Test public void shortestOperand27() {
    trimmingOf("return f(a,b,c,d) + f(a,b,c) + f();     } ")//
        .stays();
  }

  @Test public void shortestOperand28() {
    trimmingOf("return f(a,b,c,d) * f(a,b,c) * f();")//
        .gives("return f()*f(a,b,c)*f(a,b,c,d);");
  }

  @Test public void shortestOperand29() {
    trimmingOf("f(a,b,c,d) ^ f() ^ 0")//
        .gives("f() ^ f(a,b,c,d) ^ 0");
  }

  @Test public void shortestOperand30() {
    trimmingOf("f(a,b,c,d) & f()")//
        .gives("f() & f(a,b,c,d)");
  }

  @Test public void shortestOperand31() {
    trimmingOf("return f(a,b,c,d) | \".\";     }")//
        .stays();
  }

  @Test public void shortestOperand32() {
    trimmingOf("return f(a,b,c,d) && f();     }")//
        .stays();
  }

  @Test public void shortestOperand33() {
    trimmingOf("return f(a,b,c,d) || f();     }")//
        .stays();
  }

  @Test public void shortestOperand34() {
    trimmingOf("return f(a,b,c,d) + someVar; ")//
        .stays();
  }

  @Test public void shortestOperand37() {
    trimmingOf("return sansJavaExtension(f) + n + \".\"+ extension(f);")//
        .stays();
  }

  @Test public void simpleBooleanMethod() {
    trimmingOf("boolean f() { int x = 0; for (int i = 0; i < 10; ++i) x += i; return x;}")//
        .gives("boolean f() { int $ = 0; for (int i = 0; i < 10; ++i) $ += i; return $;}");
  }

  @Test public void simplifyLogicalNegationNested() {
    trimmingOf("!((a || b == c) && (d || !(!!c)))")//
        .gives("!a && b != c || !d && c");
  }

  @Test public void simplifyLogicalNegationNested1() {
    trimmingOf("!(d || !(!!c))")//
        .gives("!d && c");
  }

  @Test public void simplifyLogicalNegationNested2() {
    trimmingOf("!(!d || !!!c)")//
        .gives("d && c");
  }

  @Test public void simplifyLogicalNegationOfAnd() {
    trimmingOf("!(f() && f(5))")//
        .gives("!f() || !f(5)");
  }

  @Test public void simplifyLogicalNegationOfEquality() {
    trimmingOf("!(3 == 5)")//
        .gives("3!=5");
  }

  @Test public void simplifyLogicalNegationOfGreater() {
    trimmingOf("!(3 > 5)")//
        .gives("3 <= 5");
  }

  @Test public void simplifyLogicalNegationOfGreaterEquals() {
    trimmingOf("!(3 >= 5)")//
        .gives("3 < 5");
  }

  @Test public void simplifyLogicalNegationOfInequality() {
    trimmingOf("!(3 != 5)")//
        .gives("3 == 5");
  }

  @Test public void simplifyLogicalNegationOfLess() {
    trimmingOf("!(3 < 5)")//
        .gives("3 >= 5");
  }

  @Test public void simplifyLogicalNegationOfLessEquals() {
    trimmingOf("!(3 <= 5)")//
        .gives("3 > 5");
  }

  @Test public void simplifyLogicalNegationOfMultipleAnd() {
    trimmingOf("!(a && b && c)")//
        .gives("!a || !b || !c");
  }

  @Test public void simplifyLogicalNegationOfMultipleOr() {
    trimmingOf("!(a || b || c)")//
        .gives("!a && !b && !c");
  }

  @Test public void simplifyLogicalNegationOfNot() {
    trimmingOf("!!f()")//
        .gives("f()");
  }

  @Test public void simplifyLogicalNegationOfOr() {
    trimmingOf("!(f() || f(5))")//
        .gives("!f() && !f(5)");
  }

  @Test public void sortAddition1() {
    trimmingOf("1 + 2 - 3 - 4 + 5 / 6 - 7 + 8 * 9  + A> k + 4")//
        .gives("8*9+1+2-3-4+5 / 6-7+A>k+4");
  }

  @Test public void sortAddition2() {
    trimmingOf("1 + 2 < 3 & 7 + 4 > 2 + 1 || 6 - 7 < 2 + 1")//
        .gives("3 <3&11>3||-1<3");
  }

  @Test public void sortAddition3() {
    trimmingOf("6 - 7 < 1 + 2")//
        .gives("-1<3")//
        .stays();
  }

  @Test public void sortAddition4() {
    trimmingOf("a + 11 + 2 < 3 & 7 + 4 > 2 + 1")//
        .gives("7 + 4 > 2 + 1 & a + 11 + 2 < 3");
  }

  @Test public void sortAdditionClassConstantAndLiteral() {
    trimmingOf("1+A< 12")//
        .gives("A+1<12");
  }

  @Test public void sortAdditionFunctionClassConstantAndLiteral() {
    trimmingOf("1+A+f()< 12")//
        .gives("f()+A+1<12");
  }

  @Test public void sortAdditionThreeOperands1() {
    trimmingOf("1.0+2222+3")//
        .gives("2226.0")//
        .stays();
  }

  @Test public void sortAdditionThreeOperands2() {
    trimmingOf("1.0+1+124+1")//
        .gives("127.0");
  }

  @Test public void sortAdditionThreeOperands3() {
    trimmingOf("1+2F+33+142+1")//
        .stays();
  }

  @Test public void sortAdditionThreeOperands4() {
    trimmingOf("1+2+'a'")//
        .stays();
  }

  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_a() {
    trimmingOf("1111+211")//
        .gives("1322");
  }

  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_b() {
    trimmingOf("211+1111")//
        .gives("1322")//
        .stays();
  }

  @Test public void sortAdditionTwoOperands1() {
    trimmingOf("1+2F")//
        .stays();
  }

  @Test public void sortAdditionTwoOperands2() {
    trimmingOf("2.0+1")//
        .gives("3.0");
  }

  @Test public void sortAdditionTwoOperands3() {
    trimmingOf("1+2L")//
        .gives("3L");
  }

  @Test public void sortAdditionTwoOperands4() {
    trimmingOf("2L+1")//
        .gives("3L");
  }

  @Test public void sortAdditionUncertain() {
    trimmingOf("1+a")//
        .stays();
  }

  @Test public void sortAdditionVariableClassConstantAndLiteral() {
    trimmingOf("1+A+a< 12")//
        .gives("a+A+1<12");
  }

  @Test public void sortConstantMultiplication() {
    trimmingOf("a*2")//
        .gives("2*a");
  }

  @Test public void sortDivision() {
    trimmingOf("2.1/34.2/1.0")//
        .gives("2.1/1.0/34.2");
  }

  @Test public void sortDivisionLetters() {
    trimmingOf("x/b/a")//
        .gives("x/a/b");
  }

  @Test public void sortDivisionNo() {
    trimmingOf("2.1/3")//
        .stays();
  }

  @Test public void sortThreeOperands1() {
    trimmingOf("1.0*2222*3")//
        .gives("6666.0");
  }

  @Test public void sortThreeOperands2() {
    trimmingOf("1.0*11*124")//
        .gives("1364.0");
  }

  @Test public void sortThreeOperands3() {
    trimmingOf("2*2F*33*142")//
        .stays();
  }

  @Test public void sortThreeOperands4() {
    trimmingOf("2*3*'a'")//
        .stays();
  }

  @Test public void sortTwoOperands0CheckThatWeSortByLength_a() {
    trimmingOf("1111*211")//
        .gives("234421");
  }

  @Test public void sortTwoOperands0CheckThatWeSortByLength_b() {
    trimmingOf("211*1111")//
        .gives("234421");
  }

  @Test public void sortTwoOperands1() {
    trimmingOf("1F*2F")//
        .stays();
  }

  @Test public void sortTwoOperands2() {
    trimmingOf("2.0*2")//
        .gives("4.0");
  }

  @Test public void sortTwoOperands3() {
    trimmingOf("2*3L")//
        .gives("6L");
  }

  @Test public void sortTwoOperands4() {
    trimmingOf("2L*1L")//
        .gives("2L");
  }

  // TODO Ori: add binding for tests
  @Ignore @Test public void SwitchFewCasesReplaceWithIf1() {
    trimmingOf(" int x;\n"//
        + " switch (x) {\n"//
        + " case 1:\n"//
        + "   System.h(\"1\");\n"//
        + "   break;\n"//
        + " default:\n"//
        + "   System.h(\"error\");\n" + "   break;\n"//
        + " }\n")
            .gives(" int x;\n"//
                + " if (x == 1) {\n"//
                + "   System.h(\"1\");\n"//
                + "   return 2;\n"//
                + " } else\n"//
                + "   System.h(\"3\");\n");
  }

  @Test public void switchSimplifyCaseAfterDefault() {
    trimmingOf("switch (n.getNodeType()) {\n"//
        + "default:\n"//
        + "  return -1;\n"//
        + "case BREAK_STATEMENT:\n"//
        + "  return 0;\n"//
        + "case CONTINUE_STATEMENT:\n" + "  return 1;\n"//
        + "case RETURN_STATEMENT:\n"//
        + "  return 2;\n"//
        + "case THROW_STATEMENT:\n"//
        + "  return 3;\n"//
        + "}")//
            .stays();
  }

  @Test public void switchSimplifyCaseAfterDefault1() {
    trimmingOf("switch (n.getNodeType()) {"//
        + "  default:"//
        + "    return -1;"//
        + "  case BREAK_STATEMENT:"//
        + "    return 0;"//
        + "  case CONTINUE_STATEMENT:" + "    return 1;"//
        + "  case RETURN_STATEMENT:"//
        + "    return 2;"//
        + "  case THROW_STATEMENT:"//
        + "    return 3;"//
        + "  }")//
            .stays();
  }

  @Test public void switchSimplifyWithDefault2() {
    trimmingOf("switch (a) {\n"//
        + "case \"-N\":"//
        + "  optDoNotOverwrite = true;"//
        + "  break;"//
        + "case \"-E\":"//
        + "  optIndividualStatistics = true;" + "  break;"//
        + "case \"-V\":"//
        + "  optVerbose = true;"//
        + "  break;"//
        + "case \"-l\":"//
        + "  optStatsLines = true;"//
        + "  break;"//
        + "case \"-r\":" + "  optStatsChanges = true;"//
        + "  break;"//
        + "default:"//
        + "  if (!a.startsWith(\"-\"))"//
        + "    optPath = a;"//
        + "  try {" + "    if (a.startsWith(\"-C\"))"//
        + "      optRounds = Integer.parseUnsignedInt(a.substring(2));" + "  } catch (final NumberFormatException e) {"//
        + "    throw e;"//
        + "  }"//
        + "}")//
            .stays();
  }

  @Test public void synchronizedBraces() {
    trimmingOf("    synchronized (variables) {\n"//
        + "      for (final String key : variables.keySet())\n" + "        $.variables.put(key, variables.get(key));\n"//
        + "    }")//
            .stays();
  }

  @Test public void ternarize05() {
    trimmingOf(" int res = 0; "//
        + "if (s.equals(532))    "//
        + "res += 6;   "//
        + "else    "//
        + "res += 9;      ")//
            .gives("int res=0;res+=s.equals(532)?6:9;");
  }

  @Test public void ternarize05a() {
    trimmingOf(" int res = 0; "//
        + "if (s.equals(532))    "//
        + "res += 6;   "//
        + "else    "//
        + "res += 9;      "//
        + "return res; ")//
            .gives("int res=0;res+=s.equals(532)?6:9;return res;");
  }

  @Test public void ternarize07() {
    trimmingOf("String res;"//
        + "res = s;   "//
        + "if (res.equals(532)==true)    "//
        + "  res = s + 0xABBA;   "//
        + "S.h(res); ")
            .gives("String res =s ;"//
                + "if (res.equals(532))    "//
                + "  res = s + 0xABBA;   "//
                + "S.h(res); ");
  }

  @Test public void ternarize07a() {
    trimmingOf("String res;"//
        + "res = s;   "//
        + "if (res==true)    "//
        + "  res = s + 0xABBA;   "//
        + "S.h(res); ").gives("String res=s;if(res)res=s+0xABBA;S.h(res);");
  }

  @Test public void ternarize07aa() {
    trimmingOf("String res=s;if(res==true)res=s+0xABBA;S.h(res);")//
        .gives("String res=s==true?s+0xABBA:s;S.h(res);");
  }

  @Test public void ternarize07b() {
    trimmingOf("String res =s ;"//
        + "if (res.equals(532)==true)    "//
        + "  res = s + 0xABBA;   "//
        + "S.h(res); ").gives("String res=s.equals(532)==true?s+0xABBA:s;S.h(res);");
  }

  @Test public void ternarize09() {
    trimmingOf("if (s.equals(532)) {    return 6;}else {    return 9;}")//
        .gives("return s.equals(532)?6:9; ");
  }

  @Test public void ternarize10() {
    trimmingOf("String res = s, foo = bar;   "//
        + "if (res.equals(532)==true)    " //
        + "res = s + 0xABBA;   "//
        + "S.h(res); ")//
            .gives("String res=s.equals(532)==true?s+0xABBA:s,foo=bar;S.h(res);");
  }

  @Test public void ternarize12() {
    trimmingOf("String res = s;   if (s.equals(532))    res = res + 0xABBA;   S.h(res); ")//
        .gives("String res=s.equals(532)?s+0xABBA:s;S.h(res);");
  }

  @Test public void ternarize13() {
    trimmingOf("String res = m, foo;  if (m.equals(f())==true)   foo = M; ")//
        .gives("String foo;if(m.equals(f())==true)foo=M;")//
        .gives("String foo;if(m.equals(f()))foo=M;");
  }

  @Test public void ternarize13Simplified() {
    trimmingOf("String r = m, f;  if (m.e(f()))   f = M; ")//
        .gives("String f;if(m.e(f()))f=M;");
  }

  @Test public void ternarize13SimplifiedMore() {
    trimmingOf("if (m.equals(f())==true)   foo = M; ")//
        .gives("if (m.equals(f())) foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreAndMore() {
    trimmingOf("f (m.equals(f())==true); foo = M; ")//
        .gives("f (m.equals(f())); foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreAndMoreAndMore() {
    trimmingOf("f (m.equals(f())==true);  ")//
        .gives("f (m.equals(f()));");
  }

  @Test public void ternarize13SimplifiedMoreVariant() {
    trimmingOf("if (m==true)   foo = M; ")//
        .gives("if (m) foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreVariantShorter() {
    trimmingOf("if (m==true)   f(); ")//
        .gives("if (m) f();");
  }

  @Test public void ternarize13SimplifiedMoreVariantShorterAsExpression() {
    trimmingOf("f (m==true);   f(); ")//
        .gives("f (m); f();");
  }

  @Test public void ternarize14() {
    trimmingOf("String res=m,foo=GY;if (res.equals(f())==true){foo = M;int k = 2;k = 8;S.h(foo);}f();")
        .gives("String res=m,foo=GY;if(res.equals(f())){foo=M;int k=8;S.h(foo);}f();");
  }

  @Test public void ternarize16() {
    trimmingOf("String res = m;  int num1, num2, num3;  if (m.equals(f()))   num2 = 2; ")//
        .stays();
  }

  @Test public void ternarize16a() {
    trimmingOf("int n1, n2 = 0, n3;\n" + //
        "  if (d)\n" + //
        "    n2 = 2;")//
            .gives("int n1, n2 = d ? 2: 0, n3;");
  }

  public void ternarize18() {
    trimmingOf("final String res=s;System.h(s.equals(res)?tH3+res:h2A+res+0);")//
        .gives("System.h(s.equals(s)?tH3+res:h2A+s+0);");
  }

  @Test public void ternarize21() {
    trimmingOf("if (s.equals(532)){    S.h(gG);    S.out.l(kKz);} f(); ")//
        .stays();
  }

  @Test public void ternarize21a() {
    trimmingOf("   if (s.equals(known)){\n" + //
        "     S.out.l(gG);\n" + //
        "   } else {\n" + //
        "     S.out.l(kKz);\n" + //
        "   }")//
            .gives("S.out.l(s.equals(known)?gG:kKz);");
  }

  @Test public void ternarize22() {
    trimmingOf("int a=0;   if (s.equals(532)){    S.console();    a=3;} f(); ")//
        .stays();
  }

  @Test public void ternarize26() {
    trimmingOf("int a=0;   if (s.equals(532)){    a+=2;   a-=2; } f(); ")//
        .stays();
  }

  @Test public void ternarize33() {
    trimmingOf("int a, b=0;   if (b==3){    a=4; } ")//
        .gives("int a;if(0==3){a=4;}") //
        .gives("int a;if(0==3)a=4;") //
        .stays();
  }

  @Test public void ternarize35() {
    trimmingOf("int a,b=0,c=0;a=4;if(c==3){b=2;}")//
        .gives("int a=4,b=0,c=0;if(c==3)b=2;");
  }

  @Test public void ternarize36() {
    trimmingOf("int a,b=0,c=0;a=4;if (c==3){  b=2;   a=6; } f();")//
        .gives("int a=4,b=0,c=0;if(c==3){b=2;a=6;} f();");
  }

  @Test public void ternarize38() {
    trimmingOf("int a, b=0;if (b==3){    a+=2+r();a-=6;} f();")//
        .stays();
  }

  @Test public void ternarize41() {
    trimmingOf("int a,b,c,d;a = 3;b = 5; d = 7;if (a == 4)while (b == 3) c = a; else while (d == 3)c =a*a; ")
        .gives("int a=3,b,c,d;b=5;d=7;if(a==4)while(b==3)c=a;else while(d==3)c=a*a;");
  }

  @Test public void ternarize42() {
    trimmingOf(" int a, b; a = 3;b = 5; if (a == 4) if (b == 3) b = 2; else{b = a; b=3;}  else if (b == 3) b = 2; else{ b = a*a;         b=3; }")//
        .gives("int a=3,b;b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .gives("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=b=3;}else if(b==3)b=2;else{b=9;b=3;}")//
        .gives("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=9;b=3;}")//
        .stays();
  }

  @Test public void ternarize45() {
    trimmingOf("if (m.equals(f())==true) if (b==3){ return 3; return 7;}   else    if (b==3){ return 2;}     a=7; ")//
        .gives("if (m.equals(f())) {if (b==3){ return 3; return 7;} if (b==3){ return 2;}   }  a=7; ");
  }

  @Test public void ternarize46() {
    trimmingOf(//
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
            "     }")//
                .gives("int a;if(m.equals(NG)==true)if(0==3){return 3;}else{a+=7;}else if(0==3){return 2;}else{a=7;}");
  }

  @Test public void ternarize49() {
    trimmingOf("if (s.equals(532)){ S.h(gG); S.out.l(kKz); } f();")//
        .stays();
  }

  @Test public void ternarize52() {
    trimmingOf("int a=0,b = 0,c,d = 0,e = 0;if (a < b) {c = d;c = e;} f();")//
        .stays();
  }

  @Test public void ternarize54() {
    trimmingOf("int $=1,xi=0,xj=0,yi=0,yj=0; if(xi > xj == yi > yj)++$;else--$;")//
        .gives(" int $=1,xj=0,yi=0,yj=0;      if(0>xj==yi>yj)++$;else--$;");
  }

  @Test public void ternarize55() {
    trimmingOf("if (key.equals(markColumn))\n" + //
        " to.put(key, a.toString());\n" + //
        "else\n" + //
        "  to.put(key, missing(key, a) ? Z2 : get(key, a));")//
            .gives("to.put(key,key.equals(markColumn)?a.toString():missing(key,a)?Z2:get(key,a));");
  }

  @Test public void ternarize56() {
    trimmingOf("if (target == 0) {p.f(X); p.v(0); p.f(q +  target); p.v(q * 100 / target); } f();") //
        .gives("if(target==0){p.f(X);p.v(0);p.f(q+target);p.v(100*q / target); } f();");
  }

  @Test public void ternarizeintoSuperMethodInvocation() {
    trimmingOf("a ? super.f(a, b, c) : super.f(a, x, c)")//
        .gives("super.f(a, a ? b : x, c)");
  }

  @Test public void ternaryPushdownOfReciever() {
    trimmingOf("a ? b.f():c.f()")//
        .gives("(a?b:c).f()");
  }

  @Test public void testPeel() {
    azzert.that(Wrap.Expression.off(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge")),
        is("on * notion * of * no * nothion != the * plain + kludge"));
  }

  @Test public void twoMultiplication1() {
    trimmingOf("f(a,b,c,d) * f()")//
        .gives("f() * f(a,b,c,d)");
  }

  /* There is in fact only one Opportunity here, but because of a bug in
   * stringType two where found. In this case, the * plain + kludge can be a
   * String, and thus can't be sorted, although stringType thought it couldn't
   * be a String and thus allowed sorting before */
  @Test public void twoOpportunityExample() {
    azzert.that(TrimmerTestsUtils.countOpportunities(new Trimmer(),
        (CompilationUnit) makeAST.COMPILATION_UNIT.from(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge"))), is(1));
    azzert.that(TrimmerTestsUtils.countOpportunities(new Trimmer(),
        (CompilationUnit) makeAST.COMPILATION_UNIT.from(Wrap.Expression.on("on * notion * of * no * nothion != the * plain + kludge"))), is(1));
  }

  @Test public void unsafeBlockSimlify() {
    trimmingOf("public void testParseInteger() {\n"//
        + "  String source = \"10\";\n"//
        + "  {\n"//
        + "    BigFraction c = properFormat.parse(source);\n" + "   assert c != null;\n"//
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n"//
        + "  }\n"//
        + "  {\n"//
        + "    BigFraction c = improperFormat.parse(source);\n" + "   assert c != null;\n"//
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n"//
        + "  }\n"//
        + "}")//
            .stays();
  }

  @Test public void useOutcontextToManageStringAmbiguity() {
    trimmingOf("1+2+s<3")//
        .gives("s+1+2<3");
  }

  @Test public void vanillaShortestFirstConditionalNoChange() {
    trimmingOf("literal ? CONDITIONAL_OR : CONDITIONAL_AND")//
        .stays();
  }

  @Test public void xorSortClassConstantsAtEnd() {
    trimmingOf("f(a,b,c,d) ^ BOB")//
        .stays();
  }
}
