package com.hss.core.task.container;

import com.hss.core.task.executor.TaskRunExecutor;

import java.util.*;

/**
 * 列表容器
 * @author hd
 */
public class ListTaskContainer implements TaskContainer{


    private final LinkedList<TaskWrapper> container = new LinkedList<>();

    @Override
    public synchronized Collection<TaskRunExecutor> listByTime(long time) {
        ArrayList<TaskRunExecutor> taskRunExecutors = new ArrayList<>();
        for (TaskWrapper taskWrapper : container) {
            if (taskWrapper.getRunTime() <= time) {
                taskRunExecutors.add(taskWrapper.getTaskRunExecutor());
            }else {
                break;
            }

        }

        return taskRunExecutors;
    }

    @Override
    public synchronized void add(long time, TaskRunExecutor task) {
        TaskWrapper taskWrapper = new TaskWrapper(time, task);
        if (container.size() == 0){
            container.add(taskWrapper);
            return;
        }
        TaskWrapper first = container.getFirst();
        if (first.getRunTime() >= time){
            container.addFirst(taskWrapper);
            return;
        }
        TaskWrapper last = container.getLast();
        if (last.getRunTime() <= time){
            container.add(taskWrapper);
            return;
        }

        ListIterator<TaskWrapper> iterator = container.listIterator();
        while (iterator.hasNext()) {
            TaskWrapper next = iterator.next();
            if (next.getRunTime() > time){
                iterator.previous();
                iterator.add(taskWrapper);
                break;
            }
        }


    }

    @Override
    public synchronized void remove(String taskId) {
        Iterator<TaskWrapper> iterator = container.iterator();
        while (iterator.hasNext()){
            TaskWrapper taskRunExecutor = iterator.next();
            if (taskRunExecutor.getTaskRunExecutor().getId().equals(taskId)){
                iterator.remove();
                break;
            }

        }

    }

    @Override
    public synchronized long getNextExecuteTime() {
        int size = container.size();
        if (size == 0){
            return Long.MAX_VALUE;
        }
        TaskWrapper taskWrapper = container.get(0);
        return taskWrapper.getRunTime();
    }

    /**
     * 包装类
     */
    static class TaskWrapper{
        private final TaskRunExecutor taskRunExecutor;
        private final long runTime;
        public TaskWrapper(long runTime, TaskRunExecutor taskRunExecutor) {
            this.taskRunExecutor = taskRunExecutor;
            this.runTime = runTime;
        }

        public TaskRunExecutor getTaskRunExecutor() {
            return taskRunExecutor;
        }

        public long getRunTime() {
            return runTime;
        }
    }
}
