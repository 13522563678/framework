package com.kcwl.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author ckwl
 */
@Slf4j
public class AsyncTestSuit {

    private static final int TEST_THREAD_COUNT = 1;

    private List<ITestRunner> testRunnerGroup = new ArrayList<ITestRunner>();

    public AsyncTestSuit(ITestRunner testRunner){
        testRunnerGroup.add(testRunner);
    }

    public void addTestRunner(ITestRunner testRunner) {
        testRunnerGroup.add(testRunner);
    }

    public void execute(){
        CountDownLatch countDownLatch = new CountDownLatch(TEST_THREAD_COUNT);
        Thread testThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("start test runner group ……");
                for ( int i=0; i<testRunnerGroup.size(); i++ ) {
                    int testRunnerSeq = i+1;
                    log.info("start testRunner-[{}] ……",testRunnerSeq);
                    runTest(testRunnerGroup.get(i));
                    log.info("end testRunner-[{}] ……", testRunnerSeq);
                }
                log.info("end test runner group, testRunner count: {}", testRunnerGroup.size());
            }
        });
        testThread.start();
        waitTest(countDownLatch);
        log.info("exit test runner ……");
    }

    private void waitTest(CountDownLatch c){
        try {
            c.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void runTest(ITestRunner testRunner){
        try {
            testRunner.run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
