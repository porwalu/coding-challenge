# coding-challenge
Contains the coding challenge solutions

**Problem Statement:**

Prototype a Report generator

**Functional Requirements:**

Report App should generate hourly csv report transaction data.

**Non-Functional Requirements:**

Performance improvements.
High Availability.

DB1 – has encrypted transaction Data. Read Only
DB2 – has symmetric key for decryption. Read Only
Components are across network

![image](https://user-images.githubusercontent.com/75112899/127434430-bfcaf200-cc36-493e-979f-f0812d996a32.png)

**Assumptions**

Databases are read only from reporting app perspective.

Report should extract all transaction records.

Data is encrypted/decrypted using the same symmetric key as present in db2.

All transactions for a bank(bankId) are encrypted with same key.

**High Level Design**

![image](https://user-images.githubusercontent.com/75112899/127434530-9d363795-60e9-456d-8835-3ee523818158.png)

**Solution**

Data simulation for DB1 and DB2

DB2 – Created 10 records programmatically with BankId and symmetric key using simple sequence.

DB1 – Ensured that the transactions for BankIds are  encrypted with same keys as in DB2 

While inserting data into DB1, I am using 
Encryption – data is encrypted using the symmetric key specific to a bank id before being stored in DB(in memory DS) 

**Microservices**

Created Two microservices exposing Http GET endpoints for data access from DB1 and DB2.

![image](https://user-images.githubusercontent.com/75112899/127434755-45d0c5d8-735d-4907-a59a-d104b78a2acb.png)

**Report Application**

This is a SpringBoot application which uses ExecutorFramework to get a newSingleThreadScheduledExecutor.

This application runs periodically (can be adjusted) to query data using the rest api for DB1, it then for the transaction data, fetches the key from DB2 and decrypts the data matching on BankId.

The data reconciliation and creation of csv happens for each run of the scheduler. 

**Caching**

Ideally, we should save network calls by caching the key data as well as the Transaction data on the Report Application. The api call to get data from db should be appropriately made considering the data we already have so as to get the delta. This will improve performance.


**TODO/Future**

Project can be extended to use JpaRepository and actual db in Springboot.
Cache implementation can be improved further to evict data and create query range for new data and evicted data.
Creating a config file to control configurable properties
SSL implementation for Rest API access. 
