package Ex2_2;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.*;
public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);

    @Test
    public void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        //logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());// *** opt delete
        var task = Task.createTask(() -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);  // ***************changed************************ from 1
        var sumTask = customExecutor.submit(task);//****** add 1 priority
        //logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());// *** opt delete
        final int sum;
        try {
            sum = sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        Callable<Double> callable1 = () -> {
            int n = 10000000;
            double q = 1;
            for (int i = 2; i < n; i++)
                q = q*i;
            return q;
        };
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        // var is used to infer the declared type automatically****effect on priority from here

        var priceTask = customExecutor.submit(() -> {return 1000 * Math.pow(1.02, 5);}, TaskType.OTHER);//***************changed************************ from 1
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
   //     var priceTask2 = customExecutor.submit(callable1, TaskType.OTHER);//       var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        ArrayList<Callable> tasks = new ArrayList<>();
        ArrayList<Future> ftasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Callable t = Task.createTask(() -> {
                int n = 10000000;// increase or decrease to check left undone tasks.( now its on the edge)
                double q = 1;
                for (int j = 2; j < n; j++)
                    q = q * j;
                return q;
            }, TaskType.OTHER);//***********changed for test queue***********
            tasks.add(t);
        }
        for (Callable t : tasks) {
            ftasks.add(customExecutor.submit(t, TaskType.OTHER));
        }


        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());// *** opt delete
        final Double totalPrice;
        //final Double totalPrice2;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            //totalPrice2 = priceTask2.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        //logger.info(() -> String.valueOf("Total Price2 = " + totalPrice2));
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
    }
}