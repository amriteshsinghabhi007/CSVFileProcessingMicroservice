FLOW:
 1.processCsv() is called when the user uploads a file.
 2.It runs in a separate thread (@Async).
 3.Reads CSV line-by-line usinng bufferReader.
 4.Groups lines into 5-record chunks.
 5.Sends each chunk via sendToClient().
 6.sendToClient() sends the chunk to DataConsumerAPP (http://localhost:8081/api/data/receive)
 7.Postman - http://localhost:8080/file/csv/upload?filePath=C:/Projects/CSVFileProcessingMicroservice&fileName=data1.csv
