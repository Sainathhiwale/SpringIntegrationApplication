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

