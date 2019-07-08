/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author koduki
 */
public interface Storage {

    void create(String bucket, String file, Object data);

    boolean delete(String bucket, String file);

    InputStream read(String bucket, String file) throws FileNotFoundException;
    
}
