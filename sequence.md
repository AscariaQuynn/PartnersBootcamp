# Upload file diagram

According to Googling, file that contains a virus is generally safe to store in database. We should not run parsers, etc. on it before checking it with antivirus. So I'm assuming it is safe for this scenario and will proceed this way. Change to this flow is possible, for example store it elsewhere, encode it into base64 first to be "unusable", so it can be checked by antivirus and don't make client blocking operation of it. 

```mermaid
sequenceDiagram
    participant Client
    participant RestAPI
    participant FileUploadService
    participant MQServiceProducer
    participant StorageService
    Client->>RestAPI: client will upload file
    RestAPI->>FileUploadService: file goes into analysis (for viruses)
    FileUploadService->>StorageService: file is stored and marked for analysis (for viruses)
    FileUploadService->>MQServiceProducer: send message that a new file is ready to be analyzed (for viruses)
    FileUploadService->>RestAPI: status: analyzing (for viruses)
    RestAPI->>Client: forward status to client
    Client->>RestAPI: get status
    RestAPI->>FileUploadService: get status
    FileUploadService->>RestAPI: status case 1: analyzing (for viruses)
    FileUploadService->>RestAPI: status case 2: hasVirus (termination status)
    FileUploadService->>RestAPI: status case 3: processing (it is clean)
    FileUploadService->>RestAPI: status case 4: processed (completed status)
    RestAPI->>Client: forward status to client
```

```mermaid
sequenceDiagram
    participant MQServiceConsumer
    participant StorageService
    participant AntivirusService
    participant MQServiceProducer
    MQServiceConsumer->>StorageService: received message, getting new file
    StorageService->>MQServiceConsumer: return new file
    MQServiceConsumer->>AntivirusService: send file for analysis
    AntivirusService->>MQServiceConsumer: file status case 1: hasVirus
    MQServiceConsumer->>StorageService: file has virus, marked as virus, delete underlying file
    MQServiceConsumer->>MQServiceProducer: file is deleted, inform about it whoever is listening
    AntivirusService->>MQServiceConsumer: file status case 2: clean
    MQServiceConsumer->>StorageService: file is clean, marking it as processing so it is available further
    MQServiceConsumer->>MQServiceProducer: file is clean, inform about it whoever is listening
```

```mermaid
sequenceDiagram
    participant MQServiceConsumer
    participant ProcessingService
    participant StorageService
    participant MQServiceProducer
    MQServiceConsumer->>ProcessingService: received message, new file is clean and ready to be processed
    ProcessingService->>StorageService: get new file
    StorageService->>ProcessingService: returned new file for processing
    ProcessingService->>ProcessingService: do file processing somehow
    ProcessingService->>StorageService: processed file is stored and marked as processed
    ProcessingService->>MQServiceProducer: file is processed, inform about it whoever is listening
```
