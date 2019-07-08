/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author koduki
 */
public class LocalFileStorage implements cn.orz.pascal.kuda.utils.Storage {

    @Override
    public void create(String bucket, String file, Object data) {
        try (FileOutputStream fo = new FileOutputStream(bucket + "/" + file);
                ObjectOutputStream out = new ObjectOutputStream(fo)) {
            out.writeObject(data);
            out.flush();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public boolean delete(String bucket, String file) {
        try {
            return Files.deleteIfExists(Path.of(bucket + "/" + file));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public InputStream read(String bucket, String file) throws FileNotFoundException {
        return new FileInputStream(bucket + "/" + file);
    }

}
