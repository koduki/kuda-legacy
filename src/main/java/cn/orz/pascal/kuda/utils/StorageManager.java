/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda.utils;

/**
 *
 * @author koduki
 */
public class StorageManager {

    public static Storage get(String provider) {
        if (null == provider.toLowerCase()) {
            throw new IllegalArgumentException("Unkown provider: " + provider);
        } else {
            switch (provider.toLowerCase()) {
                case "file":
                    return new LocalFileStorage();
                case "gcs":
                    return new GCSStorage();
                default:
                    throw new IllegalArgumentException("Unkown provider: " + provider);
            }
        }
    }
}
