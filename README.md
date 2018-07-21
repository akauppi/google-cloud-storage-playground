# Google Cloud Storage playground

Study of how Google Cloud Storage can be used from Scala, programmatically.

- writing
- watching for changes
- extracting the history of a key
- access rights; can we restrict access by bucket?

The focus is on features needed for a "slow data" adapter. E.g. writing multiple values atomically in a batch, over writing just a single value.


## Requirements

- `sbt`

### Setting up Google Cloud Storage 

Note: We're expecting some knowledge on handling the [Google Cloud Console](https://console.cloud.google.com/).

1. Create a project, and a service account for it.

  Store the key for the service account under `secrets/`, and export it as (a sample):
  
  `export GOOGLE_APPLICATION_CREDENTIALS=secrets/storage-playground-080718-961906980253.json 
  `

2. Create a Cloud Storage bucket

  Choose the project and in the "Storage" page, creat the bucket.
  
  ![](.images/create-bucket.png)  

3. Enable "Storage API" and create service account

  APIs & Services > Google Cloud Storage > enable
  
  It suggests you create credentials, first.
  
  ![](.images/create-credentials1.png)

  You need to create a service account:

  ![](.images/create-service-account.png)

  Press `Save` and you'll get a JSON file that contains the private key needed to authenticate as the service account.
  
  Move the private key to a suitable location, e.g. `secrets/storage-playground-080718-523fc41d4fa1.json`.

  The "Storage Object Admin" has all the access we need. See [Cloud Storage IAM Roles](https://cloud.google.com/storage/docs/access-control/iam-roles) for the details.
  
4. Expose the service account key via an environment variable.

  `export GOOGLE_APPLICATION_CREDENTIALS=secrets/<your-key>.json`

  In the instructions below, we assume you have this env.var. set.  

5. Enable versioning for the bucket

  ```
  $ gsutil versioning set on gs://abc-080718/
  Enabling versioning for gs://abc-080718/...
  ```

  Note: does not seem to be possible to set versioning via the Google Cloud Console, nor to see if it is enabled (this is somewhat strange, since versioning is an important aspect of a bucket).

  Note: Give a little time for the versioning to propagate ([docs](https://cloud.google.com/storage/docs/consistency) recommend 30 seconds).


### Setting up Google Cloud Pub/Sub

To be able to watch changes to the bucket, we set up a Google Cloud Pub/Sub topic, and tie it to our bucket.

---

Note: This kind of things could be done programmatically, or we could provide a script in the repo for setting up the Google system. Some day.

---

To create the topic:

```
$ gcloud pubsub topics create abc-topic
```

A subscription to that topic:

```
$ gcloud pubsub subscriptions create abc-sub --topic abc-topic
```

Publishing changes to the topic:

```
$ gsutil notification create -t abc-topic -f json gs://abc-080718
```

This will start collecting changes right away, and keep them for 7 days. The `MainWatch` sample below can be used to list the changes.

  
## Kick the tires  

### MainWrite

```
$ sbt 
...
> runMain main.MainWrite
...
18:02:09.016 [run-main-0] INFO main.MainWrite$ - Going to write 'abc-080718'
18:02:10.014 [run-main-0] INFO main.MainWrite$ - 'abc' created
```

This writes an entry to a bucket, with metadata.

---

Note: Ideally, we would like to be able to write multiple entries, atomically, but that is not possible with Google Cloud Storage: 

>Google Cloud Storage does not support renaming buckets, or more generally an atomic way to operate on more than one object at a time. [^1]

[^1]: [How to upload multiple files to google cloud storage bucket as a transaction](https://stackoverflow.com/questions/14609007/how-to-upload-multiple-files-to-google-cloud-storage-bucket-as-a-transaction) (StackOverflow)

---

### MainWatch

The above writes should have gotten collected in the Google Cloud Pub/Sub topic and subscription that we set up, above. Let's see.

```

```






Q: Can we watch changes in the bucket, and/or objects' metadata?


<!--
<font color=red>There does not seem to be a way to observe changes to Google Cloud Datastore, in a streamed way.</font>

This would still be fine - we can do e.g. once a second polling for new information, using the "cursor" mechanism. 


### History

<font color=red>Something mentioned "version" information, but in practise it does not seem to be there. If we cannot read the history of the data store, we don't get auditing. Less incentive to use Google Cloud Datastore. 
</font>

We could bypass this by simply storing the events in the Datastore, never writing over the existing stuff. But it deviates the view seen natively in the store from the abstraction it's providing. It doesn't feel right.


### Authentication

tbd.

### MainDelete

tbd.
-->

## References

- [Google Cloud Storage Documentation](https://cloud.google.com/storage/docs/)
  