/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda;

import static cn.orz.pascal.jl2.Extentions.$;
import static cn.orz.pascal.jl2.collections.Tuples.*;
import cn.orz.pascal.kuda.utils.Storage;
import cn.orz.pascal.kuda.utils.StorageManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 *
 * @author koduki
 */
public class Jobq implements Iterable<Tuple2<Task, TaskStatus>> {

    private Map<String, Tuple2<Task, TaskStatus>> data = null;
    private final Config config;
    private final Storage storage;
    private String jobqPathBucket;
    private String jobqPathName;

    public Jobq(List<Task> tasks) throws ClassNotFoundException, IOException {
        config = ConfigProvider.getConfig();
        storage = StorageManager.get(config.getValue("kuda.jobq.provider", String.class));

        jobqPathBucket = config.getValue("kuda.jobq.path.bucket", String.class);
        jobqPathName = config.getValue("kuda.jobq.path.file", String.class);

        try (InputStream input = storage.read(jobqPathBucket, jobqPathName)) {
            try (ObjectInputStream inputdata = new ObjectInputStream(input)) {
                this.data = (Map<String, Tuple2<Task, TaskStatus>>) inputdata.readObject();
            }
        } catch (FileNotFoundException ex) {
            this.data = tasks.stream().collect(Collectors.toMap(
                    xs -> xs.getName(),
                    xs -> $(xs, TaskStatus.READY))
            );
        }
    }

    public TaskStatus getStatus(String status) {
        return data.get(status)._2();
    }

    public List<Tuple2<String, TaskStatus>> getAllStatus() {
        return data.entrySet()
                .stream()
                .map(xs -> $(xs.getKey(), xs.getValue()._2()))
                .collect(Collectors.toList());
    }

    public void update(Task task, TaskStatus status) {
        data.put(task.getName(), $(task, status));
        storeJobq(data);
    }

    public boolean clear() throws IOException {
        return storage.delete(jobqPathBucket, jobqPathName);
    }

    public boolean isDone() {
        return this.data.values().stream().allMatch(xs -> xs._2() == TaskStatus.DONE);
    }

    public boolean isREADY() {
        return this.data.values().stream().allMatch(xs -> xs._2() == TaskStatus.READY);
    }

    @Override
    public Iterator<Tuple2<Task, TaskStatus>> iterator() {
        return new Iterator<Tuple2<Task, TaskStatus>>() {
            Iterator<Map.Entry<String, Tuple2<Task, TaskStatus>>> it = data.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Tuple2<Task, TaskStatus> next() {
                return it.next().getValue();
            }
        };
    }

    private void storeJobq(Map<String, Tuple2<Task, TaskStatus>> jobq) {
        storage.create(jobqPathBucket, jobqPathName, jobq);
    }

}
