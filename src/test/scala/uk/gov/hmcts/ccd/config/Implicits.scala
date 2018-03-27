package uk.gov.hmcts.ccd.config

import scala.collection.JavaConverters._
import com.typesafe.config.Config

object Implicits {
  implicit class RichConfig(val underlying: Config) extends AnyVal {

    def getOptionalStringList(path: String): Option[List[String]] = if (underlying.hasPath(path)) {
      Some(underlying.getStringList(path).asScala.toList)
    } else {
      None
    }

    def getOptionalString(path: String): Option[String] = if (underlying.hasPath(path)) {
      Some(underlying.getString(path))
    } else {
      None
    }
  }
}
