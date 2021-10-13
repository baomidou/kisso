/*
 * Copyright (c) 2017-2022, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.common.encrypt;

import java.io.IOException;
import java.math.BigInteger;

/**
 * An ASN.1 TLV. The object is not parsed. It can
 * only handle integers and strings.
 *
 * @author zhang
 */
public class Asn1Object {
  protected final int type;
  protected final int length;
  protected final byte[] value;
  protected final int tag;

  /**
   * Construct a ASN.1 TLV. The TLV could be either a
   * constructed or primitive entity.
   *
   * <p/>The first byte in DER encoding is made of following fields,
   * <pre>
   *-------------------------------------------------
   *|Bit 8|Bit 7|Bit 6|Bit 5|Bit 4|Bit 3|Bit 2|Bit 1|
   *-------------------------------------------------
   *|  Class    | CF  |     +      Type             |
   *-------------------------------------------------
   * </pre>
   * <ul>
   * <li>Class: Universal, Application, Context or Private
   * <li>CF: Constructed flag. If 1, the field is constructed.
   * <li>Type: This is actually called tag in ASN.1. It
   * indicates data type (Integer, String) or a construct
   * (sequence, choice, set).
   * </ul>
   *
   * @param tag Tag or Identifier
   * @param length Length of the field
   * @param value Encoded octet string for the field.
   */
  public Asn1Object(int tag, int length, byte[] value) {
    this.tag = tag;
    this.type = tag & 0x1F;
    this.length = length;
    this.value = value;
  }

  public int getType() {
    return type;
  }

  public int getLength() {
    return length;
  }

  public byte[] getValue() {
    return value;
  }

  public boolean isConstructed() {
    return (tag & DerParser.CONSTRUCTED) == DerParser.CONSTRUCTED;
  }

  /**
   * For constructed field, return a parser for its content.
   *
   * @return A parser for the construct.
   */
  public DerParser getParser() throws IOException {
    if (!isConstructed()) {
      throw new IOException("Invalid DER: can't parse primitive entity");
    }
    return new DerParser(value);
  }

  /**
   * Get the value as integer
   */
  public BigInteger getInteger() throws IOException {
    if (type != DerParser.INTEGER) {
      throw new IOException("Invalid DER: object is not integer");
    }
    return new BigInteger(value);
  }

  /**
   * Get value as string. Most strings are treated
   * as Latin-1.
   */
  public String getString() throws IOException {
    switch (type) {
      // Not all are Latin-1 but it's the closest thing
      case DerParser.NUMERIC_STRING:
      case DerParser.PRINTABLE_STRING:
      case DerParser.VIDEOTEX_STRING:
      case DerParser.IA5_STRING:
      case DerParser.GRAPHIC_STRING:
      case DerParser.ISO646_STRING:
      case DerParser.GENERAL_STRING:
        return new String(value, "ISO-8859-1");
      case DerParser.BMP_STRING:
        return new String(value, "UTF-16BE");
      case DerParser.UTF8_STRING:
        return new String(value, "UTF-8");
      case DerParser.UNIVERSAL_STRING:
        throw new IOException("Invalid DER: can't handle UCS-4 string");
      default:
        throw new IOException("Invalid DER: object is not a string");
    }
  }
}
