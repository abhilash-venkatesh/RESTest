package es.us.isa.restest.util;

import org.checkerframework.checker.units.qual.s;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.junit.runner.Result;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

public class JUnitTestCaseTimer extends RunListener {
    private long startTime;
    Dictionary<String, Long> durations = new Hashtable<>();
    // Dictionary<String, Long> averageDurationsPerAPIEndpoint = new Hashtable<>();
    HashMap<String, Long> averageDurationsPerAPIEndpoint = new HashMap<>();
    private long averageResponseTime;

    @Override
    public void testStarted(Description description) throws Exception {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void testFinished(Description description) throws Exception {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        // System.out.println("Current dur " + duration);
        this.durations.put(description.getClassName() + "." + description.getMethodName(), duration);
        String[] methodName = description.getMethodName().split("_");
        String apiEndpoint = methodName[methodName.length - 1];
        if (this.averageDurationsPerAPIEndpoint.containsKey(apiEndpoint)) {
            // System.out.println("Prev dur " +
            // this.averageDurationsPerAPIEndpoint.get(apiEndpoint));
            Long averageResponseTime = (this.averageDurationsPerAPIEndpoint.get(apiEndpoint) + duration) / 2;
            // System.out.println("Av dur " + averageResponseTime);
            this.averageDurationsPerAPIEndpoint.put(apiEndpoint, averageResponseTime);
        } else {
            this.averageDurationsPerAPIEndpoint.put(methodName[methodName.length - 1], endTime - startTime);
        }
        calculateAverageResponseTime();
        // System.out
        // .println("ALOI " + description.getClassName() + "." +
        // description.getMethodName() + ": "
        // + this.durations.get(description.getMethodName().spli)
        // + "ms");
    }

    private void calculateAverageResponseTime() {
        long sum = 0;
        for (long value : this.averageDurationsPerAPIEndpoint.values()) {
            sum += value;
        }
        this.averageResponseTime = sum / this.averageDurationsPerAPIEndpoint.size();
    }

    public Dictionary<String, Long> getDuration() {
        return this.durations;
    }

    public HashMap<String, Long> getAverageDurationsPerAPIEndpoint() {
        return this.averageDurationsPerAPIEndpoint;
    }

    public long getAverageResponseTime() {
        return this.averageResponseTime;
    }
}
