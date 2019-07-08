/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.kuda;

import cn.orz.pascal.jl2.collections.Tuples;
import cn.orz.pascal.kuda.utils.GCSStorage;
import cn.orz.pascal.kuda.utils.HttpClient;
import cn.orz.pascal.kuda.utils.HttpResponse;
import cn.orz.pascal.kuda.utils.Storage;
import cn.orz.pascal.kuda.utils.StorageManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author koduki
 */
public class WorkflowManager {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public List<Task> loadTasks() {
        System.setProperty("java.runtime.name", "Java(TM) SE Runtime Environment");
        Yaml yaml = new Yaml();

        Config config = ConfigProvider.getConfig();
        String bucket = config.getValue("kuda.workflow.path.bucket", String.class);
        String file = config.getValue("kuda.workflow.path.file", String.class);
        Storage storage = StorageManager.get(config.getValue("kuda.workflow.provider", String.class));

        try (InputStream input = storage.read(bucket, file)) {
            Map<String, Object> data = yaml.load(input);
            List<Task> tasks = ((List<Map<String, Object>>) data.get("tasks"))
                    .stream().map(xs -> new Task(
                    (String) xs.get("name"),
                    (String) xs.get("url"),
                    (List<String>) xs.get("dependencies")))
                    .collect(Collectors.toList());
            return tasks;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void executeTasks(Jobq jobq) {
        for (Tuples.Tuple2<Task, TaskStatus> job : jobq) {
            Task t = job._1();
            if (job._2() == TaskStatus.READY) {
                CompletableFuture.runAsync(() -> {
                    execTaskHandler(t, jobq);
                });
            }
        }
    }

    private void execTaskHandler(Task t, Jobq jobq) {
        try {
            TaskStatus status = execTask(t, jobq);
            jobq.update(t, status);

            if (status == TaskStatus.DONE) {
                execAfter();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "execute task error", ex);
        }
    }

    private void execAfter() {
        Config config = ConfigProvider.getConfig();
        String url = config.getValue("kuda.url", String.class);

        HttpClient client = new HttpClient(url + "/exec");
        HttpResponse resonse = client.get();
    }

    private TaskStatus execTask(Task task, Jobq jobq) {
        if (task.getDependencies() == null) {
            jobq.update(task, TaskStatus.RUNNING);
            return executeTask(task);
        } else if (task.getDependencies().stream().allMatch(x -> jobq.getStatus(x) == TaskStatus.DONE)) {
            jobq.update(task, TaskStatus.RUNNING);
            return executeTask(task);
        } else {
            return TaskStatus.READY;
        }
    }

    private TaskStatus executeTask(Task task) {
        logger.log(Level.INFO, "START:JOB {0}", task.getName());

        HttpClient client = new HttpClient(task.getUrl());
        HttpResponse resonse = client.get();

        logger.log(Level.INFO, "END:JOB {0}", task.getName());

        return TaskStatus.DONE;
    }
}
