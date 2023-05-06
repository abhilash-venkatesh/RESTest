package es.us.isa.restest.util;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.HashMap;
import java.util.Map;

public class MyTestListener extends RunListener {
    private Map<String, Object> results = new HashMap<>();

    @Override
    public void testFinished(Description description) throws Exception {
        Object result = getResult(description.getClassName(), description.getMethodName());
        results.put(description.getDisplayName(), result);
    }

    private Object getResult(String className, String methodName) {
        try {
            Class<?> testClass = Class.forName(className);
            Object testObject = testClass.newInstance();
            testClass.getMethod(methodName).invoke(testObject);
            return null; // test passed
        } catch (Exception e) {
            return e.getCause(); // test failed with exception
        }
    }

    public Map<String, Object> getResults() {
        return results;
    }
}
