# Object-Oriented Assignment 2 part 2

We created a custome thread pool that can get asynchronous tasks with a priority dimention.

## Description

we created two classes:

1. Class with generic task with a Type that returns a result and may throw an exception.
   Extends FutuerTask and  implements Comparable, Callable.  
   Each task has a priority used for scheduling, inferred from the integer value of the task's Type.

2. Class with custom thread pool that defines a method for submitting a generic task as described in
   the section 1 to a priority queue, and a method for submitting a generic task created by a
   Callable<V> and a Type, passed as arguments.
   Extends ThreadPoolExecutor.

## Getting Started

### Task class
   
 This class, named "Task," represents a task that can be executed by a Java ThreadPollExecutor.<br />
 The class is generic and can be used with any type of result. It extends the "FutureTask" <br />
 class, which is a class that can be used to represent the result of an asynchronous computation. <br />
 The "Task" class also implements the "Comparable" and "Callable" interfaces.<br />

The "Task" class has two main purposes:

1. To encapsulate a "Callable" task and add a type to it, where the type is an enumeration called<br />
   "TaskType"and is used to indicate the priority of the task.<br />
2. To allow the tasks to be compared based on their priority value, so that they can be ordered <br />
   in a PriorityQueue.<br />
   
The "Task" class has several constructors and static factory methods to create tasks:

1. The first private constructor, which receives a "Callable" task and a "TaskType" as parameters,<br />
   is used to create a task with a specific priority type.<br />
2. The second private constructor, which receives only a "Callable" task, is used to create a <br />
   task with the default priority type "COMPUTATIONAL."<br />
3. Two static factory methods, "createTask(Callable<T> task, TaskType type)" and <br />
   "createTask (Callable<T> task)" which use the private constructors to create the tasks.<br />
   
Additionally, the class overrides the "compareTo" method of the "Comparable" interface, to allow <br />
the tasks to be compared based on their priority value.<br />
And also the class implements "call()" method of the "Callable" interface to allow the tasks to be <br />
able to compute a result and throw an exception if unable to do so.<br />

In Summary, This class is allow user to create task with certain priority<br />
and also allow user to compare tasks by its priority and execute them <br />
   by the implemented call method .<br />


### CustomExecutor class

This CustomExecutor class is a custom implementation of the ThreadPoolExecutor class in Java,<br />
which is a type of Executor that manages a pool of worker threads.<br />
The CustomExecutor class overrides the ThreadPoolExecutor class to add additional features and<br />
functionality to suit the needs of the application.<br />

The core functionality of this custom executor is implemented in the submit() method,<br />
which takes a Callable task and a task type (which represents the priority of the task) as parameters. <br />
The method first updates the priority counters based on the task type and then creates a task using<br />
the Task.<br />
createTask() method. This task is then added to the priority queue and executed by the<br />
ThreadPoolExecutor.<br />

The beforeExecute() method is called by the ThreadPoolExecutor before executing a task,<br />
and it updates the priority counters based on the task type, as the task is removed from the queue <br />
after execution.<br />
This way we can keep track on the highest priority task we have in the qeueu.<br />

The getCurrentMax() method returns the most high-priority task in the queue by checking the <br />
priority counters.<br />
   
The gracefullyTerminate() method is used to stop all the tasks and wait for the completion of the<br />
tasks that are nalready running.<br />
The method first calls the shutdown() method and then waits for the tasks to complete execution <br />
with a timeout of 2000ms.<br />
If the timeout elapses and the executor has not terminated, the shutdownNow() method is called to <br />
stop all actively executing tasks and returns a list of the tasks that were undone.<br />

With this implementation, the CustomExecutor can be used in a scenario <br />
where there are a large number of tasks that need to be executed concurrently<br />
and the order of execution is based on the priority of the tasks.<br />
The CustomExecutor uses a priority queue for its tasks and it uses a fixed size<br />
thread pool to work on these tasks concurrently.<br />
The queue determines the order of execution of these tasks.<br />
This way we can make sure that the high priority tasks are executed before the low priority ones.<br />

### Diagram


<img src="package.png" alt="Logo" width="900" height="600">




## Authors

@Moshe nahshon

@Yogev Ofir



