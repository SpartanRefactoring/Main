package il.org.spartan.classfiles.reify;

import static fluent.ly.azzert.is;

import java.io.EOFException;
import java.io.IOException;

import org.junit.Test;

import fluent.ly.___;
import fluent.ly.azzert;

/** @author Yossi Gil
 * @since 26 November 2011 */
public enum OpCode {
  NOP, // 00 (0x00)
  ACONST_NULL, // 01 (0x01)
  ICONST_M1, // 02 (0x02)
  ICONST_0, // 03 (0x03)
  ICONST_1, // 04 (0x04)
  ICONST_2, // 05 (0x05)
  ICONST_3, // 06 (0x06)
  ICONST_4, // 07 (0x07)
  ICONST_5, // 08 (0x08)
  LCONST_0, // 09 (0x09)
  LCONST_1, // 10 (0x0A)
  FCONST_0, // 11 (0x0B)
  FCONST_1, // 12 (0x0C)
  FCONST_2, // 13 (0x0D)
  DCONST_0, // 14 (0x0E)
  DCONST_1, // 15 (0x0F)
  BIPUSH(1), // 16 (0x10)
  SIPUSH(2), // 17 (0x11)
  LDC(1), // 18 (0x12)
  LDC_W(2), // 19 (0x13)
  LDC2_W(2), // 20 (0x14)
  ILOAD(1), // 21 (0x15)
  LLOAD(1), // 22 (0x16)
  FLOAD(1), // 23 (0x17)
  DLOAD(1), // 24 (0x18)
  ALOAD(1), // 25 (0x19)
  ILOAD_0, // 26 (0x1A)
  ILOAD_1, // 27 (0x1B)
  ILOAD_2, // 28 (0x1C)
  ILOAD_3, // 29 (0x1D)
  LLOAD_0, // 30 (0x1E)
  LLOAD_1, // 31 (0x1F)
  LLOAD_2, // 32 (0x20)
  LLOAD_3, // 33 (0x21)
  FLOAD_0, // 34 (0x22)
  FLOAD_1, // 35 (0x23)
  FLOAD_2, // 36 (0x24)
  FLOAD_3, // 37 (0x25)
  DLOAD_0, // 38 (0x26)
  DLOAD_1, // 39 (0x27)
  DLOAD_2, // 40 (0x28)
  DLOAD_3, // 41 (0x29)
  ALOAD_0, // 42 (0x2A)
  ALOAD_1, // 43 (0x2B)
  ALOAD_2, // 44 (0x2C)
  ALOAD_3, // 45 (0x2D)
  IALOAD, // 46 (0x2E)
  LALOAD, // 47 (0x2F)
  FALOAD, // 48 (0x30)
  DALOAD, // 49 (0x31)
  AALOAD, // 50 (0x32)
  BALOAD, // 51 (0x33)
  CALOAD, // 52 (0x34)
  SALOAD, // 53 (0x35)
  ISTORE(1), // 54 (0x36)
  LSTORE(1), // 55 (0x37)
  FSTORE(1), // 56 (0x38)
  DSTORE(1), // 57 (0x39)
  ASTORE(1), // 58 (0x3A)
  ISTORE_0, // 59 (0x3B)
  ISTORE_1, // 60 (0x3C)
  ISTORE_2, // 61 (0x3D)
  ISTORE_3, // 62 (0x3E)
  LSTORE_0, // 63 (0x3F)
  LSTORE_1, // 64 (0x40)
  LSTORE_2, // 65 (0x41)
  LSTORE_3, // 66 (0x42)
  FSTORE_0, // 67 (0x43)
  FSTORE_1, // 68 (0x44)
  FSTORE_2, // 69 (0x45)
  FSTORE_3, // 70 (0x46)
  DSTORE_0, // 71 (0x47)
  DSTORE_1, // 72 (0x48)
  DSTORE_2, // 73 (0x49)
  DSTORE_3, // 74 (0x4A)
  ASTORE_0, // 75 (0x4B)
  ASTORE_1, // 76 (0x4C)
  ASTORE_2, // 77 (0x4D)
  ASTORE_3, // 78 (0x4E)
  IASTORE, // 79 (0x4F)
  LASTORE, // 80 (0x50)
  FASTORE, // 81 (0x51)
  DASTORE, // 82 (0x52)
  AASTORE, // 83 (0x53)
  BASTORE, // 84 (0x54)
  CASTORE, // 85 (0x55)
  SASTORE, // 86 (0x56)
  POP, // 87 (0x57)
  POP2, // 88 (0x58)
  DUP, // 89 (0x59)
  DUP_X1, // 90 (0x5A)
  DUP_X2, // 91 (0x5B)
  DUP2, // 92 (0x5C)
  DUP2_X1, // 93 (0x5D)
  DUP2_X2, // 94 (0x5E)
  SWAP, // 95 (0x5F)
  IADD, // 96 (0x60)
  LADD, // 97 (0x61)
  FADD, // 98 (0x62)
  DADD, // 99 (0x63)
  ISUB, // 100 (0x64)
  LSUB, // 101 (0x65)
  FSUB, // 102 (0x66)
  DSUB, // 103 (0x67)
  IMUL, // 104 (0x68)
  LMUL, // 105 (0x69)
  FMUL, // 106 (0x6A)
  DMUL, // 107 (0x6B)
  IDIV, // 108 (0x6C)
  LDIV, // 109 (0x6D)
  FDIV, // 110 (0x6E)
  DDIV, // 111 (0x6F)
  IREM, // 112 (0x70)
  LREM, // 113 (0x71)
  FREM, // 114 (0x72)
  DREM, // 115 (0x73)
  INEG, // 116 (0x74).......
  LNEG, // 117 (0x75)
  FNEG, // 118 (0x76)
  DNEG, // 119 (0x77)
  ISHL, // 120 (0x78)
  LSHL, // 121 (0x79)
  ISHR, // 122 (0x7A)
  LSHR, // 123 (0x7B)
  IUSHR, // 124 (0x7C)
  LUSHR, // 125 (0x7D)
  IAND, // 126 (0x7E)
  LAND, // 127 (0x7F)
  IOR, // 128 (0x80)
  LOR, // 129 (0x81)
  IXOR, // 130 (0x82)
  LXOR, // 131 (0x83)
  IINC(2), // 132 (0x84)
  I2L, // 133 (0x85)
  I2F, // 134 (0x86)
  I2D, // 135 (0x87)
  L2I, // 136 (0x88)
  L2F, // 137 (0x89)
  L2D, // 138 (0x8A)
  F2I, // 139 (0x8B)
  F2L, // 140 (0x8C)
  F2D, // 141 (0x8D)
  D2I, // 142 (0x8E)
  D2L, // 143 (0x8F)
  D2F, // 144 (0x90)
  I2B, // 145 (0x91)
  I2C, // 146 (0x92)
  I2S, // 147 (0x93)
  LCMP, // 148 (0x94)
  FCMPL, // 149 (0x95)
  FCMPG, // 150 (0x96)
  DCMPL, // 151 (0x97)
  DCMPG, // 152 (0x98)
  IFEQ(2), // 153 (0x99)
  IFNE(2), // 154 (0x9A)
  IFLT(2), // 155 (0x9B)
  IFGE(2), // 156 (0x9C)
  IFGT(2), // 157 (0x9D)
  IFLE(2), // 158 (0x9E)
  IF_ICMPEQ(2), // 159 (0x9F)
  IF_ICMPNE(2), // 160 (0xA0)
  IF_ICMPLT(2), // 161 (0xA1)
  IF_ICMPGE(2), // 162 (0xA2)
  IF_ICMPGT(2), // 163 (0xA3)
  IF_ICMPLE(2), // 164 (0xA4)
  IF_ACMPEQ(2), // 165 (0xA5)
  IF_ACMPNE(2), // 166 (0xA6)
  GOTO(2), // 167 (0xA7)
  JSR(2), // 168 (0xA8)
  RET(1), // 169 (0xA9)
  TABLESWITCH { // 170 (0xAA)
    @Override Instruction readContent(final BufferDataInputStream s) {
      s.align4();
      try {
        final int $ = s.readInt(), low = s.readInt(), high = s.readInt();
        ___.sure(low <= high);
        final int offsets[] = new int[high - low + 1];
        for (int k = 0; k <= high - low; ++k)
          offsets[k] = s.readInt();
        return new Instruction(this, $, offsets);
      } catch (final IOException e) {
        throw new RuntimeException();
      }
    }
  },
  LOOKUPSWITCH { // 171 (0xAB)
    @Override Instruction readContent(final BufferDataInputStream s) {
      s.align4();
      try {
        final int $ = s.readInt(), nPairs = s.readInt(), offsets[] = new int[nPairs];
        for (int k = 0; k < nPairs; ++k) {
          s.skip(4);
          offsets[k] = s.readInt();
        }
        return new Instruction(this, $, offsets);
      } catch (final IOException e) {
        throw new RuntimeException();
      }
    }
  },
  IRETURN, // 172 (0xAC)
  LRETURN, // 173 (0xAD)
  FRETURN, // 174 (0xAE)
  DRETURN, // 175 (0xAF)
  ARETURN, // 176 (0xB0)
  RETURN, // 177 (0xB1)
  GETSTATIC(2), // 178 (0xB2)
  PUTSTATIC(2), // 179 (0xB3)
  GETFIELD(2), // 180 (0xB4)
  PUTFIELD(2), // 181 (0xB5)
  INVOKEVIRTUAL(2), // 182 (0xB6)
  INVOKESPECIAL(2), // 183 (0xB7)
  INVOKESTATIC(2), // 184 (0xB8)
  INVOKEINTERFACE(4), // 185 (0xB9)
  INVOKEDYNAMIC(4), // 186 (0xBA)
  NEW(2), // 187 (0xBB)
  NEWARRAY, // 188 (0xBC)
  ANEWARRAY(2), // 189 (0xBD)
  ARRAYLENGTH, // 190 (0xBE)
  ATHROW, // 191 (0xBF)
  CHECKCAST(2), // 192 (0xC0)
  INSTANCEOF(2), // 193 (0xC1)
  MONITORENTER, // 194 (0xC2)
  MONITOREXIT, // 195 (0xC3)
  WIDE { // 196 (0xC4)
    @Override Instruction readContent(final BufferDataInputStream $) {
      if (OpCode.values()[$.read()] != IINC)
        return readContent($);
      $.skip(4);
      return new Instruction(IINC, null);
    }
  },
  MULTIANEWARRAY(3), // 197 (0xC5)
  IFNULL(2), // 198 (0xC6)
  IFNonNull(2), // 199 (0xC7)
  GOTO_W(4), // 200 (0xC8)
  JSR_W(4), // 201 (0xC9)
  BREAKPOINT, // 202 (0xCA)
  UNUSED203(-1), // 203
  UNUSED204(-1), // 204
  UNUSED205(-1), // 205
  UNUSED206(-1), // 206
  UNUSED207(-1), // 207
  UNUSED208(-1), // 208
  UNUSED209(-1), // 209
  UNUSED210(-1), // 210
  UNUSED211(-1), // 211
  UNUSED212(-1), // 212
  UNUSED213(-1), // 213
  UNUSED214(-1), // 214
  UNUSED215(-1), // 215
  UNUSED216(-1), // 216
  UNUSED217(-1), // 217
  UNUSED218(-1), // 218
  UNUSED219(-1), // 219
  UNUSED220(-1), // 220
  UNUSED221(-1), // 221
  UNUSED222(-1), // 222
  UNUSED223(-1), // 223
  UNUSED224(-1), // 224inalid
  UNUSED225(-1), // 225
  UNUSED226(-1), // 226
  UNUSED227(-1), // 227
  UNUSED228(-1), // 228
  UNUSED229(-1), // 229
  UNUSED230(-1), // 230
  UNUSED231(-1), // 231
  UNUSED232(-1), // 232
  UNUSED233(-1), // 233
  UNUSED234(-1), // 234
  UNUSED235(-1), // 235
  UNUSED236(-1), // 236
  UNUSED237(-1), // 237
  UNUSED238(-1), // 238
  UNUSED239(-1), // 239
  UNUSED240(-1), // 240
  UNUSED241(-1), // 241
  UNUSED242(-1), // 242
  UNUSED243(-1), // 243
  UNUSED244(-1), // 244
  UNUSED245(-1), // 245
  UNUSED246(-1), // 246
  UNUSED247(-1), // 247
  UNUSED248(-1), // 248
  UNUSED249(-1), // 249
  UNUSED250(-1), // 250
  UNUSED251(-1), // 251
  UNUSED252(-1), // 252
  UNUSED253(-1), // 253
  IMPDEP1(-1), // 254 (0xFE)
  IMPDEP2(-1), // 255 (0xFF)
  ;

  public static Instruction read(final BufferDataInputStream $) {
    if ($.eof())
      return null;
    try {
      return OpCode.values()[$.readUnsignedByte()].readContent($);
    } catch (@SuppressWarnings("unused") final EOFException e) {
      return null;
    } catch (final IOException ¢) {
      throw new CorruptClassFile(¢);
    }
  }

  public int size;

  OpCode() {
    this(0);
  }
  OpCode(final int bytes) {
    size = bytes;
  }
  public boolean invalid() {
    return size < 0;
  }
  Instruction readContent(final BufferDataInputStream s) throws IOException {
    final short[] $ = new short[size];
    for (int ¢ = 0; ¢ < size; ++¢)
      $[¢] = (short) (s.readByte() & 0x000000FF);
    return new Instruction(this, $);
  }

  public class Instruction {
    public final OpCode opCode;
    final short[] args;
    // for tableswhitch and lookupswitch
    final int defaultOffset;
    final int[] offsets;

    public Instruction(final OpCode opCode, final int defaultOffset, final int[] offsets) {
      this.opCode = opCode;
      args = null;
      this.defaultOffset = defaultOffset;
      this.offsets = offsets;
    }
    public Instruction(final OpCode opCode, final short[] args) {
      this.opCode = opCode;
      this.args = args;
      defaultOffset = 0;
      offsets = null;
    }
    public short[] args() {
      return args;
    }
    public boolean invalid() {
      return opCode.invalid();
    }
    public boolean isFieldAccessInstruction() {
      return opCode == GETFIELD || opCode == PUTFIELD;
    }
    public boolean isInvokeInstruction() {
      return opCode == INVOKEINTERFACE || opCode == INVOKESPECIAL || opCode == INVOKEVIRTUAL;
    }
    public boolean isNewInstruction() {
      return opCode == NEW;
    }
    public boolean isStaticFieldAccessInstruction() {
      return opCode == GETSTATIC || opCode == PUTSTATIC;
    }
    public boolean isStaticMethodInvocation() {
      return opCode == INVOKESTATIC;
    }
    public int size() {
      return opCode.size;
    }
  }

  @SuppressWarnings("static-method") //
  public static class TEST {
    @Test public void valuesCount() {
      azzert.that(OpCode.values().length, is(256));
    }
  }
}
