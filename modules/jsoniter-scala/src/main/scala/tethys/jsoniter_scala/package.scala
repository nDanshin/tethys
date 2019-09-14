package tethys

import java.io.{OutputStream, Reader, Writer}
import java.nio.ByteBuffer

import com.github.plokhotnyuk.jsoniter_scala.core.WriterConfig
import tethys.readers.{FieldName, ReaderError}
import tethys.readers.tokens.{TokenIterator, TokenIteratorProducer}
import tethys.writers.tokens.{TokenWriter, TokenWriterProducer}

import scala.reflect.runtime.universe

package object jsoniter_scala {


  implicit def jsoniterScalaTokenWriterProducer: TokenWriterProducer = new TokenWriterProducer {
    override def forWriter(writer: Writer): TokenWriter = {
      val m = universe.runtimeMirror(getClass.getClassLoader)
      val classJsonWriter = universe.typeOf[com.github.plokhotnyuk.jsoniter_scala.core.JsonWriter].typeSymbol.asClass
      val cm = m.reflectClass(classJsonWriter)
      val ctor = universe.typeOf[com.github.plokhotnyuk.jsoniter_scala.core.JsonWriter].decl(universe.termNames.CONSTRUCTOR).asMethod
      val ctorm = cm.reflectConstructor(ctor)
      val jsonWriter = ctorm(
        new Array[Byte](16384), 0, 16384, 0, false, false, null, null, null
      ).asInstanceOf[com.github.plokhotnyuk.jsoniter_scala.core.JsonWriter]
      new JsoniterTokenWriter(jsonWriter)
    }
  }

  implicit def jsoniterTokenIteratorProducer: TokenIteratorProducer = new TokenIteratorProducer {
    override def fromReader(reader: Reader): Either[ReaderError, TokenIterator] = {
      val m = universe.runtimeMirror(getClass.getClassLoader)
      val classJsonWriter = universe.typeOf[com.github.plokhotnyuk.jsoniter_scala.core.JsonReader].typeSymbol.asClass
      val cm = m.reflectClass(classJsonWriter)
      val ctor = universe.typeOf[com.github.plokhotnyuk.jsoniter_scala.core.JsonReader].decl(universe.termNames.CONSTRUCTOR).asMethod
      val ctorm = cm.reflectConstructor(ctor)
      val jsonReader = ctorm(
        new Array[Byte](16384), 0, 0, 2147483647, new Array[Char](1024), null, null, 0, null
      ).asInstanceOf[com.github.plokhotnyuk.jsoniter_scala.core.JsonReader]
      ReaderError.catchNonFatal(JsoniterTokenIterator.fromFreshParser(jsonReader))(FieldName())
    }
  }

}
