# Email Bootstrap Service
Bootstrap service is responsible for ingesting the historical data
into database. This will run only once(if nothing goes wrong in the run) for a
provided data source.

This will have a reader mechanism that can read the data from 
different sources and an email parsing mechanism that can parse a 
raw email and create a POJO from it to ingest into kafka.

The bootstrap data will be labelled(spam / ham) data. In some case it may not be
labelled we need to take care of that.

Apart from this we need to have a streaming ingestion service that will
cater to the incoming emails of the clients. This will be the main data
that we need to classify. This data will not be labelled(spam / ham), 
we need to correctly label it.

### Backend responsibilities.
#### Base requirements:
- Read files from different sources such as
  - files(from folders of a given machine)
- Transfer it into kafka in a standardised form.
- Try to preserve some metadata in the kafka message. Such as file name, message size in bytes.
- Save a local copy of the files in hdfs / s3. This will create a backup copy.

#### Optional requirements:
- Read files from different sources such as
  - hdfs
  - s3
  - http / https
  - email servers
  - databases (need some triage)
- Make the readers multithreaded.
- Log standardized reports to process later.
- Log system metrics and health-checks to manage load.

### User interface.
#### Base requirements:
- User should be able to provide links for the data sources.
- The links need to be verified as working and not harmful.
- Display:
  - Total number of records found and data size.
  - Total number of passed(ingested) records.
  - Total number of failed(not ingested) records.
  - Total data size of ingested and not ingested data.
- User should get some acknowledgement that the processing has finished.

#### Optional requirements:
- User should see the live time required to process.
- User should be able to re-run the pipeline in case of failure.
- User should get a proper reason of failure and list of failed records.
