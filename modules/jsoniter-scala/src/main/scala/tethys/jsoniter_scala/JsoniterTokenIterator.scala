package tethys.jsoniter_scala

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonReaderException}
import tethys.commons.Token
import tethys.commons.Token.{ArrayEndToken, ArrayStartToken, BooleanValueToken, FieldNameToken, NullValueToken, NumberValueToken, ObjectEndToken, ObjectStartToken, StringValueToken}
import tethys.readers.tokens.{BaseTokenIterator, TokenIterator}

import scala.annotation.switch
import scala.collection.mutable

final class JsoniterTokenIterator(jsonReader: JsonReader) extends BaseTokenIterator {
  private val context: mutable.ArrayStack[JsonContext] = mutable.ArrayStack()
  private[this] var token: Token = fromByte(jsonReader.nextToken())

  override def currentToken(): Token = token

  override def nextToken(): Token = {
    val t = try fromByte(jsonReader.nextToken()) catch {
      case _: JsonReaderException => Token.Empty
    }
    token = t
    token
  }

  override def fieldName(): String = jsonReader.readKeyAsString()

  // default = "" means that null id converted into ""
  override def string(): String = jsonReader.readString("")

  // default = null means that null is converted into TokenError
  override def number(): Number = jsonReader.readBigInt(null)

  override def short(): Short = jsonReader.readShort()

  override def int(): Int = jsonReader.readInt

  override def long(): Long = jsonReader.readLong()

  override def float(): Float = jsonReader.readFloat()

  override def double(): Double = jsonReader.readDouble()

  override def boolean(): Boolean = jsonReader.readBoolean

  private def fromByte(byte: Byte): Token = (byte: @switch) match {
    case '{' => push(ObjectStartToken, JsonContext.Object)
    case '}' => pop(ObjectEndToken)
    case '[' => push(ArrayStartToken, JsonContext.Array)
    case ']' => pop(ArrayEndToken)
    case '"' if context.top == JsonContext.Object => push(FieldNameToken, JsonContext.Field)
    case '"' => popValue(StringValueToken)
    case 't' => popValue(BooleanValueToken)
    case 'f' => popValue(BooleanValueToken)
    case 'n' => popValue(NullValueToken)
    case b if "-0123456789".getBytes.contains(b) => popValue(NumberValueToken)
    case _ => pop(Token.Empty)
  }

  private def pop(token: Token): Token = {
    context.pop()
    token
  }

  private def push(token: Token, jsonContext: JsonContext): Token = {
    context.push(jsonContext)
    token
  }

  private def popValue(token: Token): Token = {
    if (context.top == JsonContext.Field) context.pop()
    token
  }
}

object JsoniterTokenIterator {
  def fromFreshParser(parser: JsonReader): TokenIterator = {
    new JsoniterTokenIterator(parser)
  }
}
