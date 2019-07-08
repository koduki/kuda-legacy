/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author koduki
 */
public class Task implements Serializable {

    private final String name;
    private final String url;
    private final List<String> dependencies;

    public Task(String name, String url, List<String> dependencies) {
        this.name = name;
        this.dependencies = dependencies;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return "Task{" + "name=" + name + ", dependencies=" + dependencies + ", url=" + url + '}';
    }

}
