/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;

/**
 *
 * @author koduki
 */
public class GCSStorage implements cn.orz.pascal.kuda.utils.Storage {

    public GCSStorage() {

    }

    @Override
    public void create(String bucket, String file, Object data) {
        try (ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bo)) {
            out.writeObject(data);
            out.flush();

            Storage storage = StorageOptions.getDefaultInstance().getService();
            BlobId blobId = BlobId.of(bucket, file);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            Blob blob = storage.create(blobInfo, bo.toByteArray());

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public InputStream read(String bucket, String file) throws FileNotFoundException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.get(BlobId.of(bucket, file));
        if (blob != null && blob.exists()) {
            byte[] content = blob.getContent();

            return new ByteArrayInputStream(content);
        } else {
            throw new FileNotFoundException("Not found: " + bucket + "/" + file);
        }
    }

    @Override
    public boolean delete(String bucket, String file) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(bucket, file);

        return storage.delete(blobId);
    }
}
