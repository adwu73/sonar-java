/*
 * Sonar Java
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.java.ast.lexer;

import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;
import org.sonar.channel.CodeReader;
import org.sonar.java.ast.api.JavaTokenType;


public class IntegerLiteralChannel extends NumericLiteralChannel {

  private static final String LONG_SUFFIX = "[lL]";

  public static final String INTEGER_LITERAL = "(?:" +
      // Hexadecimal
      "0[xX][0-9_a-fA-F]++" +
      // Binary (Java 7)
      "|" + "0[bB][01_]++" +
      // Decimal and Octal
      "|" + "[0-9][0-9_]*+" +
      ")";

  private final Token.Builder tokenBuilder = Token.builder();

  public IntegerLiteralChannel() {
    super(INTEGER_LITERAL + LONG_SUFFIX + "?+");
  }

  protected void consume(String value, CodeReader code, Lexer lexer) {
    Token token = tokenBuilder
        .setType(value.endsWith("L") || value.endsWith("l") ? JavaTokenType.LONG_LITERAL : JavaTokenType.INTEGER_LITERAL)
        .setValueAndOriginalValue(value)
        .setURI(lexer.getURI())
        .setLine(code.getPreviousCursor().getLine())
        .setColumn(code.getPreviousCursor().getColumn())
        .build();
    lexer.addToken(token);
  }

}
