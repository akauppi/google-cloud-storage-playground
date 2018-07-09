package main

import com.google.cloud.datastore._

object MainRead {

  /*
  * Reads an entry expected to be there.
  */
  def main(args: Array[String]): Unit = {
    val ds: Datastore = DatastoreOptions.getDefaultInstance.getService()

    val keyFactory = ds.newKeyFactory.setKind(kind)
    val key: Key = keyFactory.newKey(keyName)

    val dsr: DatastoreReader = ds
    val entity: Entity = dsr.get(key)

    entity match {
      case ent if ent != null =>
        println( s"Fetched '$keyName': $ent" )

      case _ =>
        println( s"Did not find: $keyName" )
    }
  }

  private
  val keyName: String = "abc"

  private
  val kind: String = "ABC"
}
