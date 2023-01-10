import java.util.concurrent.*;

public class CustomExecutor extends ThreadPoolExecutor {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    protected static int priorityOne = 0;
    protected static int priorityTwo = 0;
    protected static int priorityThree = 0;


    public CustomExecutor() {
        super(corePoolSize / 2, corePoolSize - 1,
                300, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
    }

    public <T> Task<T> submit(Callable<T> tsk, TaskType type) {
        Task<T> task = Task.createTask(tsk, type);
        if (type.getPriorityValue() == 1) {
            priorityOne++;
        } else if (type.getPriorityValue() == 2) {
            priorityTwo++;
        } else if (type.getPriorityValue() == 3) {
            priorityThree++;
        } else {
            System.err.println("ERROR in priority value");
        }
        //System.out.println("ONE: " + priorityOne + " TWO: " + priorityTwo + " THREE: " + priorityThree);
        subTask(task);
        return task;
    }


    public <T> Task<T> submit(Task<T> task) {
        Task.createTask(task);
        priorityOne++;
        subTask(task);

        return task;
    }

    private <T> void subTask(Task<T> task) {
        Future future = super.submit(task);
        task.setFuture(future);
    }

    public int getCurrentMax() {
        //System.out.println("ONE: " + priorityOne + " TWO: " + priorityTwo + " THREE: " + priorityThree);

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

    public void gracefullyTerminate() {
        try {
            super.awaitTermination(3000, TimeUnit.MILLISECONDS);
            super.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    public static int getPriorityOne() {
//
//        return priorityOne;
//    }
//
//    public static void setPriorityOne(int priorityOne) {
//        System.out.println("priority 1 is " + priorityOne);
//        priorityOne = priorityOne;
//    }
//
//    public static int getPriorityTwo() {
//
//        return priorityTwo;
//    }
//
//    public static void setPriorityTwo(int priorityTwo) {
//        System.out.println("priority 2 is " + priorityTwo);
//        priorityTwo = priorityTwo;
//    }
//
//    public static int getPriorityThree() {
//
//        return priorityThree;
//    }
//
//    public static void setPriorityThree(int priorityThree) {
//        System.out.println("priority 3 is " + priorityThree);
//        priorityThree = priorityThree;
//    }

}

