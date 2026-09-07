### Implementation

Implemented **file-based message processing** using Spring Boot and Spring Integration.

* `FileReadingMessageSource` — reads `.txt` files from `filesource/`
* `@InboundChannelAdapter` — polls for files every second
* `fileInput` — message channel between components
* `FileWritingMessageHandler` — writes files to `destination/`
* `SimplePatternFileListFilter` — processes only `.txt` files

**Flow:**

`filesource/test.txt → File Reader → fileInput → File Writer → destination/test.txt`



### Steps

1. Create a `filesource/` folder in the project root.
2. Create a `test.txt` file inside `filesource/`.
3. Add some text, for example:

   ```text
   hello1
   ```
4. The application polls the `filesource/` folder every **1 second**.
5. The `LastModifiedFileListFilter` waits until the file is **60 seconds old/unchanged**.
6. After 60 seconds, the file is read by `FileReadingMessageSource`.
7. The file message is sent through the `fileInput` channel.
8. `FileWritingMessageHandler` creates the `destination/` folder and writes the file there.

### Flow

```text
filesource/test.txt
       ↓
Wait until file is 60 seconds old
       ↓
FileReadingMessageSource
       ↓
   fileInput
       ↓
FileWritingMessageHandler
       ↓
destination/test.txt
```

### Components

* `FileReadingMessageSource` — reads files from `filesource/`
* `@InboundChannelAdapter` — polls the folder every second
* `LastModifiedFileListFilter` — processes files after 60 seconds
* `fileInput` — message channel between components
* `FileWritingMessageHandler` — writes files to `destination/`


Implemented **file-based message processing** using Spring Boot and Spring Integration.


**Tech:** Java 17, Spring Boot 4.1.1, Spring Integration, Maven.


### banking-system-spring-integration-adapter branch


# 🏦 Banking Transaction File Processing

A production-style **Spring Boot + Spring Integration** example that automatically reads banking transaction files, validates transactions, and stores valid transactions in a database.

## 🚀 Use Case

A banking partner periodically provides transaction files through an **SFTP server or shared folder**.

The Spring Boot service automatically detects and processes these files.

```text
Banking System
      ↓
transactions_2026_09_08.txt
      ↓
SFTP / Shared Folder
      ↓
Spring Boot
      ↓
FileReadingMessageSource
      ↓
Validate Transactions
      ↓
Transaction Service
      ↓
PostgreSQL / MySQL
```

## 📄 Input File

Create the following file:

```text
filesource/transactions_2026_09_08.txt
```

Example content:

```text
TX1001,ACC001,5000,INR
TX1002,ACC002,2500,INR
TX1003,ACC003,10000,INR
```

## 🔄 Processing Flow

```text
                    ┌─────────────────────┐
                    │   Banking Partner   │
                    └──────────┬──────────┘
                               │
                         Transaction File
                               │
                               ↓
                    ┌─────────────────────┐
                    │   SFTP / File       │
                    │      System         │
                    └──────────┬──────────┘
                               │
                               ↓
                 FileReadingMessageSource
                               │
                               ↓
                         fileInput
                               │
                               ↓
                      Transaction Handler
                               │
                               ↓
                    Transaction Validation
                               │
                               ↓
                     Transaction Service
                               │
                               ↓
                    Transaction Repository
                               │
                               ↓
                         Database
```

## 🧩 Components

### FileReadingMessageSource

Reads transaction files from the source directory.

```java
FileReadingMessageSource source =
        new FileReadingMessageSource();

source.setDirectory(new File("filesource"));
```

### InboundChannelAdapter

Polls the directory periodically and sends detected files into the Spring Integration flow.

```java
@InboundChannelAdapter(
    value = "fileInput",
    poller = @Poller(fixedDelay = "1000")
)
```

### Message Channel

```text
fileInput
```

Acts as the communication channel between the file reader and the transaction processor.

### Transaction Service

Responsible for:

* Reading transaction records
* Parsing transaction data
* Validating transactions
* Saving valid transactions
* Handling invalid transactions

### Database

Valid transactions are persisted using:

```text
Spring Data JPA
        ↓
PostgreSQL / MySQL
```

## ✅ Validation Example

A transaction is considered valid when:

```text
Transaction ID → Required
Account Number → Required
Amount         → Greater than 0
Currency       → Valid
```

Example:

```text
TX1001,ACC001,5000,INR
```

✅ Valid → Save to database

```text
TX1002,ACC002,-500,INR
```

❌ Invalid → Reject / move to error processing

## 🗄️ Database Result

After successful processing:

```text
+----+---------------+---------------+--------+----------+
| ID | Transaction ID | Account       | Amount | Currency |
+----+---------------+---------------+--------+----------+
| 1  | TX1001        | ACC001        | 5000   | INR      |
| 2  | TX1002        | ACC002        | 2500   | INR      |
| 3  | TX1003        | ACC003        | 10000  | INR      |
+----+---------------+---------------+--------+----------+
```

## 🏭 Production Enhancements

For a real banking production system, this example can be extended with:

* SFTP integration instead of a local folder
* PostgreSQL/MySQL
* Kafka for asynchronous transaction processing
* Duplicate transaction detection
* Retry mechanism
* Error/dead-letter processing
* Failed file directory
* Transaction auditing
* Spring Security
* Centralized logging
* Monitoring and alerting
* Database transaction management
* Docker/Kubernetes deployment

### Production Architecture

```text
                  Banking Partner
                         │
                        SFTP
                         │
                         ↓
                Spring Boot Service
                         │
                 Spring Integration
                         │
                 File Inbound Adapter
                         │
                         ↓
                    Validation
                         │
                         ↓
                       Kafka
                         │
             ┌───────────┴───────────┐
             ↓                       ↓
      Transaction Service       Audit Service
             │                       │
             ↓                       ↓
        PostgreSQL              PostgreSQL
```

## 🛠️ Technologies

* Java 17
* Spring Boot
* Spring Integration
* Spring Data JPA
* Maven
* H2 / PostgreSQL / MySQL
* SFTP
* Apache Kafka

## 🎯 Learning Objective

This project demonstrates how **Spring Integration adapters** can connect a Spring Boot application with external systems that exchange data through files.

The key pattern is:

```text
External System
      ↓
Integration Adapter
      ↓
Message Channel
      ↓
Business Logic
      ↓
Database
```

This pattern is commonly useful for **enterprise integration, banking, insurance, automotive, logistics, and legacy-system integrations**.


