package main

import com.google.cloud.storage._

import com.typesafe.scalalogging._
import java.nio.charset.StandardCharsets.UTF_8

import collection.JavaConverters._

/*
* Write to Google Storage bucket
*
* Note: No batch (atomic) write possibility for Google Cloud Storage.
*
*     "Google Cloud Storage does not support ... an atomic way to operate on more than one object at a time."
*     https://stackoverflow.com/questions/14609007/how-to-upload-multiple-files-to-google-cloud-storage-bucket-as-a-transaction
*/
object MainWrite extends LazyLogging {
  import System.{currentTimeMillis => nowMs}

  def main(args: Array[String]): Unit = {
    val sto: Storage = StorageOptions.getDefaultInstance.getService()

    // This requires 'storage.buckets.get' permission (unlike the second way), at least on 1.31.0 and 1.35.0, and does
    // not seem to allow setting custom metadata.
    //
    if (false) {
      val bucket: Bucket = sto.get(bucketName /*, BucketGetOption*/)
      val name: String = "abc"

      logger.info(s"Going to write '$name'")

      // Note: '.create' offers only single writes
      //
      //      ..but that may be okay, as long as we can piggy-back metadata on the write
      //
      bucket.create(name, "ABC!".getBytes(UTF_8))
      logger.info(s"'abc' created")
    }

    // Supports custom metadata
    //
    if (true) {
      val name: String = "abc2"
      val meta: Map[String,String] = Map(
        "a" -> "42",
        "b" -> "25"
      )

      logger.info(s"Going to write '$name', with metadata")

      val bId: BlobId = BlobId.of(bucketName, name)
      val bInfo: BlobInfo = BlobInfo.newBuilder(bId)
        //.setContentType("text/plain")
        .setMetadata( meta.asJava )
        .build()

      sto.create(bInfo, "ABC!".getBytes(UTF_8))
    }
  }

  // A bucket we have created manually
  //
  val bucketName: String = "abc-100718"
}
