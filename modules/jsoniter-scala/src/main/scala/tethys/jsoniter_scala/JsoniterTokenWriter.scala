package tethys.jsoniter_scala

import com.github.plokhotnyuk.jsoniter_scala.core.JsonWriter
import tethys.writers.tokens.TokenWriter

class JsoniterTokenWriter(jsonWriter: JsonWriter) extends TokenWriter {
  override def writeArrayStart(): JsoniterTokenWriter.this.type = {
    jsonWriter.writeArrayStart()
    this
  }

  override def writeArrayEnd(): JsoniterTokenWriter.this.type = {
    jsonWriter.writeArrayEnd()
    this
  }

  override def writeObjectStart(): JsoniterTokenWriter.this.type = {
    jsonWriter.writeObjectStart()
    this
  }

  override def writeObjectEnd(): JsoniterTokenWriter.this.type = {
    jsonWriter.writeObjectEnd()
    this
  }

  override def writeFieldName(name: String): JsoniterTokenWriter.this.type = {
    jsonWriter.writeKey(name)
    this
  }

  override def writeString(v: String): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeNumber(v: Short): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeNumber(v: Int): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeNumber(v: Long): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeNumber(v: BigInt): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeNumber(v: Double): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeNumber(v: Float): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeNumber(v: BigDecimal): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeBoolean(v: Boolean): JsoniterTokenWriter.this.type = {
    jsonWriter.writeVal(v)
    this
  }

  override def writeNull(): JsoniterTokenWriter.this.type = {
    jsonWriter.writeNull()
    this
  }

  override def writeRawJson(json: String): JsoniterTokenWriter.this.type = {
    //todo: reflective call writeOptionalCommaAndIndentionBeforeValue
    jsonWriter.writeRawVal(json.getBytes)
    this
  }

  override def close(): Unit = {}

  override def flush(): Unit = {}
}
