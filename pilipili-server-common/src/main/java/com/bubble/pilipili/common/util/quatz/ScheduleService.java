/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util.quatz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * 定时任务服务
 * @author Bubble
 * @date 2025/01/27 22:44
 */
public class ScheduleService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(6);
    private final Trigger trigger = new Trigger();

    void schedule(Runnable task, long delay) {
        Job job = new Job();
        job.setJobName(task.getClass().getSimpleName());
        job.setTask(task);
        job.setStartTime(System.currentTimeMillis() + delay);
        long delayOffset = 5;
        job.setDelay(delay> delayOffset ? delay- delayOffset : delay);
        trigger.queue.offer(job);
        trigger.wakeup();
    }


    class Trigger {

        PriorityBlockingQueue<Job> queue = new PriorityBlockingQueue<>();

        Thread thread = new Thread(() -> {
            while (true) {
                while (queue.isEmpty()) {
                    LockSupport.park();
                }
                // 检查一下队头的job
                Job latelyJob = queue.peek();
                assert latelyJob != null;
                if (latelyJob.getStartTime() < System.currentTimeMillis()) {
                    // 取出队头的job
                    latelyJob = queue.poll();
                    assert latelyJob != null;
                    executorService.execute(latelyJob.getTask());

                    // 更新下次该定时任务下次执行时间
                    latelyJob.setStartTime(System.currentTimeMillis() + latelyJob.getDelay());
                    queue.offer(latelyJob);
//                    Job nextJob = new Job();
//                    nextJob.setJobName(latelyJob.getTask().getClass().getSimpleName());
//                    nextJob.setTask(latelyJob.getTask());
//                    nextJob.setStartTime(System.currentTimeMillis() + latelyJob.getDelay());
//                    nextJob.setDelay(latelyJob.getDelay());
//                    queue.offer(nextJob);

                } else {
                    LockSupport.parkUntil(latelyJob.getStartTime());
                }
            }
        });

        {
            thread.start();
        }

        void wakeup() {
            LockSupport.unpark(thread);
        }
    }
}
