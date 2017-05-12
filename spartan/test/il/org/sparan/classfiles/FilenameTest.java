package il.org.sparan.classfiles;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.classfiles.*;

@SuppressWarnings("static-method") public class FilenameTest {
  @Test public void Head() {
    azzert.that(Filename.headPart(""), is(""));
    azzert.that(Filename.headPart("a"), is(""));
    azzert.that(Filename.headPart("$"), is(""));
    azzert.that(Filename.headPart("$a"), is(""));
    azzert.that(Filename.headPart("a$"), is(""));
    azzert.that(Filename.headPart("a.b.c"), is("a.b"));
    azzert.that(Filename.headPart("a.b.c$1"), is("a.b"));
    azzert.that(Filename.headPart("a.b.c$d"), is("a.b"));
    azzert.that(Filename.headPart("a.b.$d"), is("a.b"));
    azzert.that(Filename.headPart("a.b$1.c$d.X"), is("a.b$1.c$d"));
    azzert.that(Filename.headPart("a.b$a.c$d"), is("a.b$a"));
    azzert.that(Filename.headPart("a.b.c"), is("a.b"));
    azzert.that(Filename.headPart("a.b.c.$.XXX"), is("a.b.c.$"));
    azzert.that(Filename.headPart("a.b$c.x$d"), is("a.b$c"));
    azzert.that(Filename.headPart("com.sun.corba.se.impl.io.ValueUtility$IdentityKeyValueStack$KeyValuePair"), is("com.sun.corba.se.impl.io"));
    azzert.that(Filename.headPart("mypackage.Myclass$1"), is("mypackage"));
    azzert.that(Filename.headPart("a$1"), is(""));
    azzert.that(Filename.headPart("b.a$1"), is("b"));
    azzert.that(Filename.headPart("b.a$c"), is("b"));
    azzert.that(Filename.headPart("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1"), is("com.sun.corba.se.impl.encoding"));
    azzert.that(Filename.headPart("com.sun.security.auth.callback.DialogCallbackHandler$2"), is("com.sun.security.auth.callback"));
    azzert.that(Filename.headPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"), is("javax.swing"));
    azzert.that(Filename.headPart("$1"), is(""));
    azzert.that(Filename.headPart("a"), is(""));
    azzert.that(Filename.headPart("mypackage.Myclass$A"), is("mypackage"));
    azzert.that(Filename.headPart("a$1"), is(""));
    azzert.that(Filename.headPart("a$b"), is(""));
    azzert.that(Filename.headPart("b.a$1"), is("b"));
    azzert.that(Filename.headPart("b.a$b"), is("b"));
    azzert.that(Filename.headPart("b.a$c"), is("b"));
    azzert.that(Filename.headPart("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1"), is("com.sun.corba.se.impl.encoding"));
    azzert.that(Filename.headPart("com.sun.security.auth.callback.DialogCallbackHandler$2"), is("com.sun.security.auth.callback"));
    azzert.that(Filename.headPart("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter"), is("com.sun.corba.se.impl.encoding"));
    azzert.that(Filename.headPart("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$__ByteArrayInputStream"),
        is("com.sun.corba.se.impl.encoding"));
    azzert.that(Filename.headPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"), is("javax.swing"));
  }

  @Test public void IsAllInner() {
    assert !Filename.isAllInner("$1");
    assert !Filename.isAllInner("a");
    assert Filename.isAllInner("mypackage.Myclass$A");
    assert !Filename.isAllInner("a$1");
    assert Filename.isAllInner("a$b");
    assert !Filename.isAllInner("b.a$1");
    assert Filename.isAllInner("b.a$b");
    assert Filename.isAllInner("b.a$c");
    assert !Filename.isAllInner("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1");
    assert !Filename.isAllInner("com.sun.security.auth.callback.DialogCallbackHandler$2");
    assert Filename.isAllInner("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter");
    assert Filename.isAllInner("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$__ByteArrayInputStream");
    assert !Filename.isAllInner("javax.swing.JSlider$1SmartHashtable$LabelUIResource");
  }

  @Test public void IsAnonymous() {
    assert Filename.isAnonymous("A$1");
    assert !Filename.isAnonymous("a");
    assert Filename.isAnonymous("mypackage.Myclass$1");
    assert Filename.isAnonymous("a$1");
    assert Filename.isAnonymous("b.a$1");
    assert !Filename.isAnonymous("b.a$c");
    assert Filename.isAnonymous("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1");
    assert Filename.isAnonymous("com.sun.security.auth.callback.DialogCallbackHandler$2");
    assert !Filename.isAnonymous("javax.swing.JSlider$1SmartHashtable$LabelUIResource");
  }

  @Test public void IsInner() {
    assert !Filename.isInner("$1");
    assert !Filename.isInner("a");
    assert Filename.isInner("mypackage.Myclass$A");
    assert !Filename.isInner("a$1");
    assert Filename.isInner("a$b");
    assert !Filename.isInner("b.a$1");
    assert Filename.isInner("b.a$b");
    assert Filename.isInner("b.a$c");
    assert !Filename.isInner("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1");
    assert !Filename.isInner("com.sun.security.auth.callback.DialogCallbackHandler$2");
    assert Filename.isInner("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter");
    assert Filename.isInner("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$__ByteArrayInputStream");
    assert Filename.isInner("javax.swing.JSlider$1SmartHashtable$LabelUIResource");
  }

  @Test public void IsLocal() {
    assert !Filename.isLocal("$1");
    assert !Filename.isLocal("a");
    assert Filename.isLocal("mypackage.Myclass$2A");
    assert !Filename.isLocal("a$1");
    assert !Filename.isLocal("a$b");
    assert !Filename.isLocal("b.a$1");
    assert !Filename.isLocal("b.a$b");
    assert Filename.isLocal("b.a$2c");
    assert !Filename.isLocal("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1");
    assert !Filename.isLocal("com.sun.security.auth.callback.DialogCallbackHandler$2");
    assert !Filename.isLocal("com.sun.corba.se.impl.activation.RepositoryImpl$DBServerDef");
    assert !Filename.isLocal("javax.swing.JSlider$1SmartHashtable$LabelUIResource");
  }

  @Test public void Name2Canonical() {
    azzert.that(Filename.name2Canonical(""), is(""));
    azzert.that(Filename.name2Canonical("a"), is("a"));
    azzert.that(Filename.name2Canonical("a.b.c"), is("a.b.c"));
    azzert.that(Filename.name2Canonical("a.b.c$d"), is("a.b.c.d"));
    azzert.that(Filename.name2Canonical("a.b$c.x$d"), is("a.b$c.x.d"));
    azzert.that(Filename.name2Canonical("com.sun.corba.se.impl.io.ValueUtility$IdentityKeyValueStack$KeyValuePair"),
        is("com.sun.corba.se.impl.io.ValueUtility.IdentityKeyValueStack.KeyValuePair"));
  }

  @Test public void Path2Class() {
    azzert.that(Filename.path2class("a.b.c"), is("a.b.c"));
    azzert.that(Filename.path2class("a/b/c"), is("a.b.c"));
    azzert.that(Filename.path2class("a/b/c.class"), is("a.b.c"));
    azzert.that(Filename.path2class("a/b/c$1.class"), is("a.b.c$1"));
    azzert.that(Filename.path2class("a/b/c$d.class"), is("a.b.c$d"));
    azzert.that(Filename.path2class("a/b$1/c$d.class"), is("a.b$1.c$d"));
    azzert.that(Filename.path2class("a/b$a/c$d.class"), is("a.b$a.c$d"));
  }

  @Test public void simpleClass2Path() {
    azzert.that(Filename.class2path(this.getClass()), is("il/ac/technion/cs/ssdl/classfiles/FilenameTest"));
  }

  @Test public void simpleObject2Path() {
    azzert.that(Filename.class2path(this), is("il/ac/technion/cs/ssdl/classfiles/FilenameTest"));
  }

  @Test public void simpleString2Path() {
    azzert.that(Filename.class2path("a.b"), is("a/b"));
  }

  @Test public void Tail() {
    azzert.that(Filename.tailPart(""), is(""));
    azzert.that(Filename.tailPart("a"), is("a"));
    azzert.that(Filename.tailPart("$"), is("$"));
    azzert.that(Filename.tailPart("$a"), is("$a"));
    azzert.that(Filename.tailPart("a$"), is("a$"));
    azzert.that(Filename.tailPart("a.b.c"), is("c"));
    azzert.that(Filename.tailPart("a.b.c$1"), is("c$1"));
    azzert.that(Filename.tailPart("a.b.c$d"), is("c$d"));
    azzert.that(Filename.tailPart("a.b.$d"), is("$d"));
    azzert.that(Filename.tailPart("a.b$1.c$d.X"), is("X"));
    azzert.that(Filename.tailPart("a.b$a.c$d"), is("c$d"));
    azzert.that(Filename.tailPart("a.b.c"), is("c"));
    azzert.that(Filename.tailPart("a.b.c.$.XXX"), is("XXX"));
    azzert.that(Filename.tailPart("a.b$c.x$d"), is("x$d"));
    azzert.that(Filename.tailPart("com.sun.corba.se.impl.io.ValueUtility$IdentityKeyValueStack$KeyValuePair"),
        is("ValueUtility$IdentityKeyValueStack$KeyValuePair"));
    azzert.that(Filename.tailPart("mypackage.Myclass$1"), is("Myclass$1"));
    azzert.that(Filename.tailPart("a$1"), is("a$1"));
    azzert.that(Filename.tailPart("b.a$1"), is("a$1"));
    azzert.that(Filename.tailPart("b.a$c"), is("a$c"));
    azzert.that(Filename.tailPart("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1"), is("BufferManagerWriteCollect$1"));
    azzert.that(Filename.tailPart("com.sun.security.auth.callback.DialogCallbackHandler$2"), is("DialogCallbackHandler$2"));
    azzert.that(Filename.tailPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"), is("JSlider$1SmartHashtable$LabelUIResource"));
    azzert.that(Filename.tailPart("$1"), is("$1"));
    azzert.that(Filename.tailPart("a"), is("a"));
    azzert.that(Filename.tailPart("mypackage.Myclass$A"), is("Myclass$A"));
    azzert.that(Filename.tailPart("a$1"), is("a$1"));
    azzert.that(Filename.tailPart("a$b"), is("a$b"));
    azzert.that(Filename.tailPart("b.a$1"), is("a$1"));
    azzert.that(Filename.tailPart("b.a$b"), is("a$b"));
    azzert.that(Filename.tailPart("b.a$c"), is("a$c"));
    azzert.that(Filename.tailPart("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter"), is("CodeSetConversion$UTF16BTCConverter"));
    azzert.that(Filename.tailPart("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$__ByteArrayInputStream"),
        is("IDLJavaSerializationInputStream$__ByteArrayInputStream"));
    azzert.that(Filename.tailPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"), is("JSlider$1SmartHashtable$LabelUIResource"));
  }

  @Test public void Trailer() {
    azzert.that(Filename.trailerPart(""), is(""));
    azzert.that(Filename.trailerPart("a"), is(""));
    azzert.that(Filename.trailerPart("$"), is(""));
    azzert.that(Filename.trailerPart("a$1"), is("1"));
    azzert.that(Filename.trailerPart("$1"), is(""));
    azzert.that(Filename.trailerPart("abc$XYZ"), is("XYZ"));
    azzert.that(Filename.trailerPart("ab$XYZ"), is("XYZ"));
    azzert.that(Filename.trailerPart("a$XYZ"), is("XYZ"));
    azzert.that(Filename.trailerPart("abc$XYZ$1"), is("1"));
    azzert.that(Filename.trailerPart("ab$XYZ$1"), is("1"));
    azzert.that(Filename.trailerPart("a$XYZ$1"), is("1"));
    azzert.that(Filename.trailerPart("$XYZ"), is(""));
    azzert.that(Filename.trailerPart("$abc"), is(""));
    azzert.that(Filename.trailerPart("a$"), is(""));
    azzert.that(Filename.trailerPart("a.b.c"), is(""));
    azzert.that(Filename.trailerPart("a.b.c$1"), is("1"));
    azzert.that(Filename.trailerPart("a.b.c$d"), is("d"));
    azzert.that(Filename.trailerPart("a.b.$d"), is(""));
    azzert.that(Filename.trailerPart("a.b$1.c$d.X"), is(""));
    azzert.that(Filename.trailerPart("a.b$a.c$d"), is("d"));
    azzert.that(Filename.trailerPart("a.b.c"), is(""));
    azzert.that(Filename.trailerPart("a.b.c.$.XXX"), is(""));
    azzert.that(Filename.trailerPart("a.b$c.x$d"), is("d"));
    azzert.that(Filename.trailerPart("com.sun.corba.se.impl.io.ValueUtility$IdentityKeyValueStack$KeyValuePair"), is("KeyValuePair"));
    azzert.that(Filename.trailerPart("mypackage.Myclass$1"), is("1"));
    azzert.that(Filename.trailerPart("a$1"), is("1"));
    azzert.that(Filename.trailerPart("b.a$1"), is("1"));
    azzert.that(Filename.trailerPart("b.a$c"), is("c"));
    azzert.that(Filename.trailerPart("com.sun.corba.se.impl.encoding.BufferManagerWriteCollect$1"), is("1"));
    azzert.that(Filename.trailerPart("com.sun.security.auth.callback.DialogCallbackHandler$2"), is("2"));
    azzert.that(Filename.trailerPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"), is("LabelUIResource"));
    azzert.that(Filename.trailerPart("$1"), is(""));
    azzert.that(Filename.trailerPart("a"), is(""));
    azzert.that(Filename.trailerPart("mypackage.Myclass$A"), is("A"));
    azzert.that(Filename.trailerPart("a$1"), is("1"));
    azzert.that(Filename.trailerPart("a$b"), is("b"));
    azzert.that(Filename.trailerPart("b.a$1"), is("1"));
    azzert.that(Filename.trailerPart("b.a$b"), is("b"));
    azzert.that(Filename.trailerPart("b.a$c"), is("c"));
    azzert.that(Filename.trailerPart("com.sun.corba.se.impl.encoding.CodeSetConversion$UTF16BTCConverter"), is("UTF16BTCConverter"));
    azzert.that(Filename.trailerPart("com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream$__ByteArrayInputStream"),
        is("__ByteArrayInputStream"));
    azzert.that(Filename.trailerPart("javax.swing.JSlider$1SmartHashtable$LabelUIResource"), is("LabelUIResource"));
  }
}
