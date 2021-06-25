package io.jenkins.plugins.cloudevents;

import hudson.model.Result;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StageTest {


    @Test
    public void testShouldSendInfo() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

            Method isShouldSendItemInfo = Stage.class.getDeclaredMethod("shouldSendItem", String.class);
            isShouldSendItemInfo.setAccessible(true);

            Method isShouldSendBuildInfo = Stage.class.getDeclaredMethod("shouldSendBuild", String.class, Result.class);
            isShouldSendBuildInfo.setAccessible(true);

            assertEquals(isShouldSendItemInfo.invoke(Stage.ENTERED_WAITING, "entered_waiting"), Boolean.TRUE);
            assertEquals(isShouldSendItemInfo.invoke(Stage.LEFT, "entered_waiting"), Boolean.FALSE);
            assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "entered_waiting", Result.FAILURE), Boolean.FALSE);
            assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "entered_waiting", Result.FAILURE), Boolean.FALSE);
            assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "entered_waiting", Result.FAILURE), Boolean.FALSE);


            assertEquals(isShouldSendItemInfo.invoke(Stage.LEFT, "left"), Boolean.TRUE);
            assertEquals(isShouldSendItemInfo.invoke(Stage.ENTERED_WAITING, "left"), Boolean.FALSE);
            assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "left", Result.FAILURE), Boolean.FALSE);
            assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "left", Result.FAILURE), Boolean.FALSE);
            assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "left", Result.FAILURE), Boolean.FALSE);


            assertEquals(isShouldSendItemInfo.invoke(Stage.CREATED, "created"), Boolean.TRUE);

            // TODO: Add more test-cases for events.
    }
}
