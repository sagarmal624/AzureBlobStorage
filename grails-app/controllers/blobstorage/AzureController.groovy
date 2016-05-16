package blobstorage

import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.CloudBlob
import com.microsoft.azure.storage.blob.CloudBlobClient
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import com.microsoft.azure.storage.blob.ListBlobItem

import org.springframework.beans.factory.annotation.Value

class AzureController {
    @Value('${cloud.azure.credentials.accessName}')
    private String accountName

    @Value('${cloud.azure.credentials.secretkey}')
    private String accessKey


    def upload() {
        def file = request.getFile('filePath')
        File source = new File(file.originalFilename)
        source.setBytes(file.getBytes());
        try {
            CloudBlobContainer container = getContainer()
            CloudBlockBlob blob = container.getBlockBlobReference(file.originalFilename);
            blob.upload(new FileInputStream(source), source.length());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        forward action: 'list'
    }

    def list() {
        List uriList = [];
        try {
            CloudBlobContainer container = getContainer()
            for (ListBlobItem blobItem : container.listBlobs()) {
                CloudBlob blob = (CloudBlob) blobItem;
                uriList.add(blob.getName());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        render view: "blobOperation", model: [uriList: uriList]
    }

    def download(String fileName) {
        try {
            CloudBlobContainer container = getContainer()

            for (ListBlobItem blobItem : container.listBlobs()) {
                if (blobItem instanceof CloudBlob) {
                    CloudBlob blob = (CloudBlob) blobItem;
                    if (blob.getName() == fileName) {

                        blob.download(new FileOutputStream("/home/sagar/Desktop/" + blob.getName()));
                        break
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        forward action: 'list'
    }

    def delete(String fileName) {
        try {
            CloudBlobContainer container = getContainer()
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);

            blob.deleteIfExists();
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        redirect action: 'list'
    }

    private CloudBlobContainer getContainer() {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(getConnection());
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("testapp");
        return container
    }

    private String getConnection() {
        return "DefaultEndpointsProtocol=http;" +
                "AccountName=${accountName};" +
                "AccountKey=${accessKey}";
    }
}
