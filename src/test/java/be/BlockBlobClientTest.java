package be;

import be.App;
import com.azure.storage.blob.BlobOutputStream;
import com.azure.storage.blob.BlockBlobClient;
import com.azure.storage.blob.ContainerClient;
import com.azure.storage.blob.ContainerClientBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Ruben Vervaeke
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlockBlobClientTest {

    @TestConfiguration
    static class Config {

        @Bean
        public ContainerClient containerClient(@Value("${connection-string}") String connectionString,
                                               @Value("${container-name}") String containerName) {
            return new ContainerClientBuilder().connectionString(connectionString)
                                               .containerName(containerName)
                                               .buildClient();
        }
    }

    @Autowired
    private ContainerClient containerClient;

    @Test
    public void create() throws IOException {
        final UUID id = UUID.randomUUID();
        BlockBlobClient client = containerClient.getBlockBlobClient(id.toString());
        try (BlobOutputStream blobOutputStream = client.getBlobOutputStream()) {
            IOUtils.copy(new ByteArrayInputStream(new byte[] {0x00}), blobOutputStream);
        }
    }
}
