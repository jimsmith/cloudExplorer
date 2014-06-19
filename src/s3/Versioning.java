package s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import java.util.List;
import static s3.NewJFrame.jTextArea1;

public class Versioning {

    NewJFrame mainFrame;
    Delete del;
    public static Boolean delete = false;

    public Versioning(NewJFrame Frame) {
        mainFrame = Frame;
    }

    void getVersions(String key, String access_key, String secret_key, String bucket, String endpoint) {
        String results = null;
        try {
            mainFrame.jTextArea1.append("\nPlease wait, loading versions.");
            mainFrame.calibrateTextArea();
            AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
            AmazonS3 s3Client = new AmazonS3Client(credentials);
            s3Client.setEndpoint(endpoint);

            VersionListing vListing;
            if (key == null) {
                vListing = s3Client.listVersions(bucket, null);
            } else {
                vListing = s3Client.listVersions(bucket, key);
            }

            List<S3VersionSummary> summary;
            do {
                summary = vListing.getVersionSummaries();

                for (S3VersionSummary foo : summary) {
                    if (Versioning.delete) {
                        del = new Delete(foo.getKey(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), foo.getVersionId());
                        del.startc(foo.getKey(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), foo.getVersionId());
                    }
                    mainFrame.versioning_date.add(foo.getLastModified().toString());
                    mainFrame.versioning_id.add(foo.getVersionId());
                    mainFrame.versioning_name.add(foo.getKey());
                    System.gc();
                }

            } while (vListing.isTruncated());

        } catch (Exception getVersions) {

        }
        if (Versioning.delete) {
            Versioning.delete = false;
        }

    }

}
