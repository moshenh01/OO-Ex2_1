import java.util.concurrent.*;

public class Task <T>  implements Comparable<Task<T>>, Callable<T> {

    private TaskType type;
    private Callable<T> callable;
    private Future<T> future;


    private Task(Callable<T> task, TaskType type) {
        this.type = type;
        this.callable = task;
    }

    private Task(Callable<T> task){
        this(task, TaskType.COMPUTATIONAL);//***************changed************************ from 1
    }
    public static <T> Task<T> createTask(Callable<T> task, TaskType type) {
        return new Task<T>(task, type);
    }

    public static <T> Task<T> createTask(Callable<T> task) {
        return new Task<T>(task);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural        return this.type.compareTo(o.type);g that is
     * inconsistent with equals."
     */
    //   @Override
//    public int compareTo(Task<T> task) {
//        int compared = task.type.compareTo(this.type);
//        return compared;
//    }


    public int compareTo(Task<T> task) {
        if(task.type.getPriorityValue() > this.type.getPriorityValue()){
            return 1;
        }
        return (task.type.getPriorityValue() == this.type.getPriorityValue()) ? 0 : -1;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public T call() throws Exception {
        //System.out.println(CustomExecutor.getPriorityOne());
        //System.out.println(getPriorityTwo());
        //System.out.println(getPriorityThree());
        if(this.type.getPriorityValue() == 1){
            CustomExecutor.priorityOne--;
            //CustomExecutor.setPriorityOne(CustomExecutor.getPriorityOne()-1);
        }
        else if(this.type.getPriorityValue() == 2){
            CustomExecutor.priorityTwo--;
            //CustomExecutor.setPriorityTwo(CustomExecutor.getPriorityTwo()-1);
        }
        else if(this.type.getPriorityValue() == 3){
            CustomExecutor.priorityThree--;
            //CustomExecutor.setPriorityThree(CustomExecutor.getPriorityThree()-1);
        }else {
            System.err.println("ERROR in priority value");}

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

    public T get(long result, TimeUnit tu) throws ExecutionException, InterruptedException, TimeoutException {
        return future.get(result, tu);
    }
    public T get() throws ExecutionException, InterruptedException {
        return future.get();
    }

    public void setFuture(Future<T> future) {

        this.future = future;
    }
}


