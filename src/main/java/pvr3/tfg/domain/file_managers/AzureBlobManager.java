package pvr3.tfg.domain.file_managers;

import com.microsoft.windowsazure.services.blob.client.*;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.StorageException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Created by Pablo on 08/07/2015.
 */
public class AzureBlobManager {

    public static final String storageConnectionString = "DefaultEndpointsProtocol=http;"+
            "AccountName=tfgearthquakes;"+
            "AccountKey=ITGSaZwUB7bMZYssccSB9EEvlnHXtCUe9kmnhpYaHZ7wmNzWHPzz7EWLAwc1jvDVRoyiNOCe8+F+oGAqewU1Tg==";


    public static void crearBlobKml(){
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer blobContainer = blobClient.getContainerReference("kml-files");
            blobContainer.createIfNotExist();

            // Create a permissions object.
            BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

            // Include public access in the permissions object.
            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

            // Set the permissions on the container.
            blobContainer.uploadPermissions(containerPermissions);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    public URI putAtKmlAzureBlob(File file, String name){
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer blobContainer = blobClient.getContainerReference("kml-files");

            CloudBlockBlob blob = blobContainer.getBlockBlobReference(name);
            blob.upload(new FileInputStream(file), file.length());

            return blob.getUri();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
