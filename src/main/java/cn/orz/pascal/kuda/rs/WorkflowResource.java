package cn.orz.pascal.kuda.rs;

import cn.orz.pascal.kuda.Jobq;
import cn.orz.pascal.kuda.Task;
import cn.orz.pascal.kuda.WorkflowManager;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class WorkflowResource {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/exec")
    public String exec() throws Exception {
        WorkflowManager manager = new WorkflowManager();
        List<Task> workflow = manager.loadTasks();
        Jobq jobq = new Jobq(workflow);

        if (jobq.isREADY()) {
            logger.info("END:WORKFLOW");
            manager.executeTasks(jobq);
        } else if (jobq.isDone()) {
            logger.info("END:WORKFLOW");
            if (jobq.clear()) {
                logger.info("CLEAR:JOBQ");
            }
        } else {
            manager.executeTasks(jobq);
        }

        return "done";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/jobq")
    public String getJobq() throws Exception {
        WorkflowManager manager = new WorkflowManager();
        List<Task> workflow = manager.loadTasks();
        Jobq jobq = new Jobq(workflow);

        return jobq.getAllStatus().toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/workflow")
    public String getWorkflow() throws Exception {
        WorkflowManager manager = new WorkflowManager();
        List<Task> workflow = manager.loadTasks();

        return workflow.toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/clear")
    public Boolean clear() throws Exception {
        WorkflowManager manager = new WorkflowManager();
        List<Task> workflow = manager.loadTasks();
        Jobq jobq = new Jobq(workflow);

        return jobq.clear();
    }
}
