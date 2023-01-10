package Ex2_2;

import java.util.concurrent.*;

public class Task <T> extends FutureTask<T> implements Comparable<Task<T>>, Callable<T> {

    private TaskType type;
    private Callable<T> callable;


    /**
     * constructor.
     * @param task the task to be executed(callable type task).
     * @param type the type of the task(priority).
     */
    private Task(Callable<T> task, TaskType type) {
        //this.future = new FutureTask<>(task);
        super(task);//Creates a FutureTask that will, upon running, execute the given Callable.
        this.type = type;
        this.callable = task;
    }
    /**
     * a constructor.
     * as above this method is Create a task with a constant type.
     * by build a future task that we inherit from "FutureTask" class.
     * @param task a Callable task to be executed.
     *  Create a task
     */
    private Task(Callable<T> task){
        super(task);
        this.callable = task;
        this.type = TaskType.COMPUTATIONAL;
    }

    /**
     * Create a task with a given type.
     * using the private constructor.
     * @param task
     * @param type
     * @return new task.
     * @param <T>
     */
    public static <T> Task<T> createTask(Callable<T> task, TaskType type) {
        return new Task<T>(task, type);
    }
    /**
     * Create a task with a constant type.
     * using the private constructor.
     * @param task
     * @return
     * @param <T>
     */
    public static <T> Task<T> createTask(Callable<T> task) {
        return new Task<T>(task);
    }


    /**
     * compare the tasks priority values.
     * @param task the object to be compared.
     * @return if this < task return 1, if this > task return -1, if they are even return 0.
     */

    public int compareTo(Task<T> task) {
        if(task.type.getPriorityValue() > this.type.getPriorityValue()){
            return 1;
        }
        return (task.type.getPriorityValue() == this.type.getPriorityValue()) ? 0 : -1;
    }
    /**
     * Computes a result, or throws an exception if unable to do so.

     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public T call() throws Exception {
        return this.callable.call();
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }
    public Callable<T> getCallable() {
        return callable;
    }

    public void setCallable(Callable<T> callable) {
        this.callable = callable;
    }


}


