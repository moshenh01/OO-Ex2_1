import java.io.FileInputStream;
import java.io.FileWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class Ex2_1{

    /**function that creates n numbers of file and writes random numbers of lines
    *  in each file, the function returns a string array of the files names.
     **/
    public static String[] createTextFiles(int n, int seed, int bound){

        String[] files = new String[n];
        Random rand = new Random(seed);

            for (int i = 0; i < n; i++) {
                try {

                        FileWriter myfile = new FileWriter( "file_" +i+ ".txt");

                        files[i] = "file_" +i+ ".txt";

                    int x = rand.nextInt(bound);
                        for(int j = 0; j < x; j++){


                                if(j < x-1) {
                                    myfile.write("Welcome to Petah Tikva \n");
                                }
                                else{
                                    myfile.write("Welcome to Petah Tikva ");
                                }
                        }
                        myfile.close();
                } catch ( Exception e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

            }

        return files;
    }
    /**
     * function that reads the files and counts the number of lines in each file
     * gets array of name files.
     * @return int - the number of lines in all files.
     */
    public static int getNumOfLines(String[] fileNames){
        int count = 0;
        for (int i = 0; i < fileNames.length; i++) {
            try {
                FileInputStream fis = new FileInputStream(fileNames[i]);
                Scanner sc = new Scanner(fis);
                while(sc.hasNextLine()) {
                    sc.nextLine();
                    count++;
                    //System.out.println(count +"\n");
                }
                sc.close();
                fis.close();
            }catch (Exception e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }
        return count;
    }

    /**
     * a class that extends thread and implements the run function that read thea file counts the number of lines in the file.
     * we use the sane code as the non threded function without the for loop for each file.
     */
    public static class myTread extends Thread{
           private String fileNames;
           private int lineNum;

           public myTread(String fileNames){
                this.fileNames = fileNames;
              }

           public void run(){
                lineNum = getNumOfL(this.fileNames);

           }

        private int getNumOfL(String fileNames) {
            int count = 0;
            try {
                FileInputStream fis = new FileInputStream(fileNames);
                Scanner sc = new Scanner(fis);
                while(sc.hasNextLine()) {
                    sc.nextLine();
                    count++;
                    //System.out.println(count +"\n");
                }
                sc.close();
                fis.close();
            }catch (Exception e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return count;
        }

        public int getLineNum(){
                 return this.lineNum;
              }
    }

    /**
     * a function that creates n threads and runs them in parallel.
     * in each thread we run the function that read file frome the array of files names and count the number of lines in that file.
     * @param fileNames - array of files names
     *
     * @return int - the number of lines in all files.
     */
    public static int getNumOfLinesThreads(String[] fileNames){
        int sum = 0;
        myTread[] threads = new myTread[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            threads[i] = new myTread(fileNames[i]);
            threads[i].start();
        }
        for (myTread thread : threads) {
            try {
                thread.join();
                sum += thread.getLineNum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
            return sum;
    }

    /**
     * a class that implements the Callable interface and implements the call function,
     * unlike the thread class, the call function returns a value.
     * the call function activate getNumOfL that read a file and counts the number of lines in the file
     * and returns the number of lines in the file.
     */
    public static class myThreadCallable implements Callable<Integer> {
        private String fileNames;

        public myThreadCallable(String fileNames) {
            this.fileNames = fileNames;
        }

        @Override
        public Integer call() throws Exception {
            return getNumOfL(this.fileNames);
        }

        private int getNumOfL(String fileNames) {
            int count = 0;
            try {
                FileInputStream fis = new FileInputStream(fileNames);
                Scanner sc = new Scanner(fis);
                while(sc.hasNextLine()) {
                    sc.nextLine();
                    count++;
                    //System.out.println(count +"\n");
                }
                sc.close();
                fis.close();
            }catch (Exception e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return count;
        }
    }/**
        in this function we create a thread pool and run the threads in parallel but in a better way.
        the thread pool it's a queue of threads that run in parallel the threas is assind to a task and
        when they are done with the task they go back to the queue and wait for another task.
        the function gets array of files names and return the number of lines in all files.
        in this fubction we use a thread pool , in the first loop we submit a task to every file and
        get a furure value of the number of lines in the file, that we will get when the tsak is done.
        we save all the future values in a future array and then caculate the number of lines.
        **/

    public static int getNumOfLinesThreadPool(String[] fileNames){
        int res = 0;
        ExecutorService threadPool = java.util.concurrent.Executors.newFixedThreadPool(fileNames.length);
        Future<Integer>[] futures = new Future[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            futures[i] = threadPool.submit(new myThreadCallable(fileNames[i]));
        }
        for (Future<Integer> future : futures) {
            try {
                res += future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
        return res;
    }




    public static void main(String[] args) {
      String [] arrSt = createTextFiles(1000,10,10);
      long start2 = System.currentTimeMillis();
      System.out.println(getNumOfLines(arrSt));
      long end2 = System.currentTimeMillis();
      System.out.println("Elapsed Time in milli seconds(normal): "+ (end2-start2));
      long start3 = System.currentTimeMillis();
      System.out.println(getNumOfLinesThreads(arrSt));
      long end3 = System.currentTimeMillis();
      System.out.println("Elapsed Time in milli seconds(thread): "+ (end3-start3));
      long start4 = System.currentTimeMillis();
      System.out.println(getNumOfLinesThreadPool(arrSt));
      long end4 = System.currentTimeMillis();
      System.out.println("Elapsed Time in milli seconds(threadPool): "+ (end4-start4));

      System.out.println("\nchange method\n");
      Instant start = Instant.now();
      System.out.println(getNumOfLines(arrSt));
      Instant end = Instant.now();
      System.out.println("Elapsed Time in milli seconds(normal): "+ Duration.between(start, end).toMillis());
      Instant start5 = Instant.now();
      System.out.println(getNumOfLinesThreads(arrSt));
      Instant end5 = Instant.now();
      System.out.println("Elapsed Time in milli seconds(thread): "+ Duration.between(start5, end5).toMillis());
      Instant start6 = Instant.now();
      System.out.println(getNumOfLinesThreadPool(arrSt));
      Instant end6 = Instant.now();
      System.out.println("Elapsed Time in milli seconds(threadpool): "+ Duration.between(start6, end6).toMillis());





    }
}