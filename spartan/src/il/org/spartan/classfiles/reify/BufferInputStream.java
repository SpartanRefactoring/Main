/* Copyright (c) 1994, 2005, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. This code is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License version 2 only, as published by the Free
 * Software Foundation. Oracle designates this particular file as subject to the
 * "Classpath" exception as provided by Oracle in the LICENSE file that
 * accompanied this code. This code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License version 2 for more details (a copy is included in the LICENSE
 * file that accompanied this code). You should have received a copy of the GNU
 * General Public License version 2 along with this work; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA. Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA
 * 94065 USA or visit www.oracle.com if you need additional information or have
 * any questions. */
package il.org.spartan.classfiles.reify;

import java.io.*;

import org.jetbrains.annotations.*;

import il.org.spartan.utils.*;

/** A sane re-implementation of {@link ByteArrayInputStream}.
 * @author Yossi Gil */
public class BufferInputStream extends InputStream {
  @NotNull protected final byte[] bytes;
  protected int position;
  protected int mark;
  protected final int length;

  public BufferInputStream(@NotNull final byte bytes[]) {
    this.bytes = bytes;
    length = bytes.length;
  }

  @Override public synchronized int available() {
    return length - position;
  }

  @Override public void close() {
    ___.nothing();
  }

  public boolean done() {
    return position >= length;
  }

  public boolean eof() {
    return done();
  }

  @Override public synchronized void mark(final int limit) {
    mark = position;
  }

  @Override public boolean markSupported() {
    return true;
  }

  public final int position() {
    return position;
  }

  @Override public synchronized int read() {
    return done() ? -1 : bytes[position++] & 0xff;
  }

  @Override public synchronized int read(final byte bs[], final int offset, final int len) {
    if (bs == null)
      throw new NullPointerException();
    if (offset < 0 || len < 0 || len > bs.length - offset)
      throw new IndexOutOfBoundsException();
    if (eof())
      return -1;
    if (len <= 0)
      return 0;
    final int $ = len + position <= length ? len : length - position;
    System.arraycopy(bytes, position, bs, offset, $);
    position += $;
    return $;
  }

  @Override public synchronized void reset() {
    position = mark;
  }

  @Override public synchronized long skip(long ¢) {
    if (¢ + position > length)
      ¢ = length - position;
    if (¢ < 0)
      return 0;
    position += ¢;
    return ¢;
  }
}