package main

import com.google.cloud.storage._

import collection.JavaConverters._

import com.typesafe.scalalogging._

import java.nio.charset.StandardCharsets.UTF_8

/*
* Write to Google Storage bucket (with batch write)
*/
object MainWrite extends LazyLogging {
  import System.{currentTimeMillis => nowMs}

  def main(args: Array[String]): Unit = {
    val sto: Storage = StorageOptions.getDefaultInstance.getService()
    val bucket: Bucket = sto.get(bucketName /*, BucketGetOption*/)     // tbd. would like to use 'skip_lookup' so that 'storage.buckets.get' role is not needed

    logger.info(s"Going to write '$bucketName'")

    // Note: '.create' offers only single writes
    //
    bucket.create("abc", "ABC!".getBytes(UTF_8) )
    logger.info(s"'abc' created")

    /***
    // Batch write
    //
    Blob.writer
    Blob.Builder

    val bw: BlobW = new BlobWriteChannel

    sto.delete
    ***/
  }

  // A bucket we have created manually
  //
  val bucketName: String = "abc-080718"
}
