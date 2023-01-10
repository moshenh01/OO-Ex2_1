package Ex2_2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CustomExecutor extends ThreadPoolExecutor {

    /**
     * in readme.
     */
    private final static int corePoolSize = Runtime.getRuntime().availableProcessors();
    protected static int priorityOne = 0;
    protected static int priorityTwo = 0;
    protected static int priorityThree = 0;

    /**
     * build a CustomExecutor by the given parameters (in the assigment).
     */
    public CustomExecutor() {
        super(corePoolSize / 2, corePoolSize - 1,
                300, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
    }

    /**
     * first update the priority counters, by the task type.
     * then, create a task and execute it.
     * @param tsk  the callable task to be executed.
     * @param type type of task.(priority)
     * @return future object of the task.
     * @param <T> the type of the task result.
     */
    public <T> Future<T> submit(Callable<T> tsk, TaskType type) {
        if (type.getPriorityValue() == 1) {
            priorityOne++;
        } else if (type.getPriorityValue() == 2) {
            priorityTwo++;
        } else if (type.getPriorityValue() == 3) {
            priorityThree++;
        } else {
            System.err.println("ERROR in priority value");
        }

        RunnableFuture<T> futureT = Task.createTask(tsk, type);
        execute(futureT);
        return futureT;
    }

    /**
     * first update the priority counters, by the task type.
     * then, create a task and execute it.
     * @param task  the
     *             task to be executed.
     * @return future object of the task.
     * @param <T> the type of the task result.
     */
    public <T> Future<T> submit(Task<T> task) {
        priorityOne++;
        RunnableFuture<T> futureT = Task.createTask(task);
        execute(futureT);
        return futureT;
    }

    /**
     * Method invoked prior to executing the given Runnable/Callable in the given thread.
     * before executing the task, update the priority counters(because after executing the task,
     * the task will be removed from the queue).
     * @param t the thread that will run task {@code r}
     * @param r the task that will be executed
     */
    protected void beforeExecute(Thread t, Runnable r){
        if(getCurrentMax() == 1){
            priorityOne--;
        }
        else if(getCurrentMax() == 2){
            priorityTwo--;
        }
        else if(getCurrentMax() == 3){
            priorityThree--;
        }else {
            System.err.println("ERROR in priority value");
        }
    }

    /**
     * give the most high priority task in the queue.
     * By checking the priority counters.
     * @return the most high priority task in the queue (in int).
     */
    public int getCurrentMax() {
        if (priorityOne > 0) {
            return 1;
        } else if (priorityTwo > 0) {
            return 2;
        } else if (priorityThree > 0) {
            return 3;
        } else {
            System.out.println("There are no tasks");
        }
        return 0;
    }

    /**
     * we wait for previously submitted tasks to complete execution.
     * @throws InterruptedException if interrupted while waiting
     * @throws NullPointerException if the given time is null
     * ...
     * the "awaitTermination" returns true if this executor terminated
     * and false if the timeout (2000) elapsed before termination
     * and the "shutdownNow()" method attempts to stop all actively executing tasks,
     * gives a list of the undone tasks.
     */
    public void gracefullyTerminate() {
        List<Runnable> tasks = new ArrayList<>();
        super.shutdown();
        try {
            if(!super.awaitTermination(2000, TimeUnit.MILLISECONDS))
              tasks = super.shutdownNow();
              tasks.forEach(System.out::println);//will print task that was not done.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}

