package io.emailradar.processor

import io.emailradar.commons.email.model.{CivilisedEmail, CivilisedEmailHeader, CivilisedEmailNodeInfo, EmailPayload}
import io.emailradar.commons.metadata.Metadata
import org.apache.spark.sql.{Encoders, SparkSession}

object EmailProcessorApplication {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local")
      .appName("EmailMongoSparkConnector")
      .config("spark.mongodb.read.connection.uri", "mongodb://rootuser:rootpass@127.0.0.1/email_spam_detection.emails2?authSource=admin")
      .getOrCreate()
    implicit val emailEncoder = Encoders.bean(classOf[CivilisedEmail])
    implicit val emailHeaderEncoder = Encoders.bean(classOf[CivilisedEmailHeader])
    implicit val emailNodeInfoEncoder = Encoders.bean(classOf[CivilisedEmailNodeInfo])
    implicit val metadataEncoder = Encoders.bean(classOf[Metadata])
    implicit val payloadEncoder = Encoders.bean(classOf[EmailPayload])

    val df = spark.read.format("mongodb").load.as[EmailPayload]

    df.printSchema()
    df.show()
  }
}
