package io.jenkins.plugins.cloudevents;

import hudson.model.*;
import hudson.slaves.OfflineCause;
import io.jenkins.plugins.cloudevents.listeners.CE_ItemListener;
import io.jenkins.plugins.cloudevents.listeners.CE_JobListener;
import io.jenkins.plugins.cloudevents.listeners.CE_NodeListener;
import io.jenkins.plugins.cloudevents.listeners.CE_QueueListener;
import io.jenkins.plugins.cloudevents.model.JobModel;
import io.jenkins.plugins.cloudevents.model.NodeModel;
import io.jenkins.plugins.cloudevents.model.QueueModel;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.RestartableJenkinsRule;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Stage.class)
public class StageTest {

    @Rule
    public RestartableJenkinsRule jenkinsRule = new RestartableJenkinsRule();


    @Test
    public void testShouldSendItemInfo() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method isShouldSendItemInfo = Stage.class.getDeclaredMethod("shouldSendItem", String.class);
        isShouldSendItemInfo.setAccessible(true);

        assertEquals(isShouldSendItemInfo.invoke(Stage.ENTERED_WAITING, "entered_waiting"), Boolean.TRUE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.LEFT, "entered_waiting"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.STARTED, "entered_waiting"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.COMPLETED, "entered_waiting"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.FINALIZED, "entered_waiting"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.CREATED, "entered_waiting"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.UPDATED, "entered_waiting"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ONLINE, "entered_waiting"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.OFFLINE, "entered_waiting"), Boolean.FALSE);

        assertEquals(isShouldSendItemInfo.invoke(Stage.LEFT, "left"), Boolean.TRUE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ENTERED_WAITING, "left"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.STARTED, "left"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.COMPLETED, "left"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.FINALIZED, "left"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.CREATED, "left"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.UPDATED, "left"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ONLINE, "left"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.OFFLINE, "left"), Boolean.FALSE);

        assertEquals(isShouldSendItemInfo.invoke(Stage.CREATED, "created"), Boolean.TRUE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ENTERED_WAITING, "created"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.LEFT, "created"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.STARTED, "created"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.COMPLETED, "created"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.FINALIZED, "created"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.UPDATED, "created"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ONLINE, "created"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.OFFLINE, "created"), Boolean.FALSE);

        assertEquals(isShouldSendItemInfo.invoke(Stage.UPDATED, "updated"), Boolean.TRUE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ENTERED_WAITING, "updated"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.LEFT, "updated"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.STARTED, "updated"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.COMPLETED, "updated"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.FINALIZED, "updated"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.CREATED, "updated"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ONLINE, "updated"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.OFFLINE, "updated"), Boolean.FALSE);

        assertEquals(isShouldSendItemInfo.invoke(Stage.ONLINE, "online"), Boolean.TRUE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ENTERED_WAITING, "online"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.LEFT, "online"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.STARTED, "online"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.COMPLETED, "online"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.FINALIZED, "online"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.CREATED, "online"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.UPDATED, "online"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.OFFLINE, "online"), Boolean.FALSE);

        assertEquals(isShouldSendItemInfo.invoke(Stage.OFFLINE, "offline"), Boolean.TRUE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ENTERED_WAITING, "offline"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.LEFT, "offline"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.STARTED, "offline"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.COMPLETED, "offline"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.FINALIZED, "offline"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.CREATED, "offline"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.UPDATED, "offline"), Boolean.FALSE);
        assertEquals(isShouldSendItemInfo.invoke(Stage.ONLINE, "offline"), Boolean.FALSE);
    }

    @Test
    public void testShouldSendBuildInfo() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method isShouldSendBuildInfo = Stage.class.getDeclaredMethod("shouldSendBuild", String.class, Result.class);
        isShouldSendBuildInfo.setAccessible(true);

        assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "started", Result.FAILURE), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "started", Result.ABORTED), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "started", Result.NOT_BUILT), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "started", Result.SUCCESS), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "started", Result.UNSTABLE), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.ENTERED_WAITING, "started", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.LEFT, "started", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "started", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "started", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.CREATED, "started", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.UPDATED, "started", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.ONLINE, "started", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.OFFLINE, "started", Result.NOT_BUILT), Boolean.FALSE);

        assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "completed", Result.FAILURE), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "completed", Result.ABORTED), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "completed", Result.NOT_BUILT), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "completed", Result.SUCCESS), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "completed", Result.UNSTABLE), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.ENTERED_WAITING, "completed", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.LEFT, "completed", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "completed", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "completed", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.CREATED, "completed", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.UPDATED, "completed", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.ONLINE, "completed", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.OFFLINE, "completed", Result.NOT_BUILT), Boolean.FALSE);

        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "finalized", Result.FAILURE), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "finalized", Result.ABORTED), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "finalized", Result.NOT_BUILT), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "finalized", Result.SUCCESS), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "finalized", Result.UNSTABLE), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.ENTERED_WAITING, "finalized", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.LEFT, "finalized", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.STARTED, "finalized", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.COMPLETED, "finalized", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.CREATED, "finalized", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.UPDATED, "finalized", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.ONLINE, "finalized", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.OFFLINE, "finalized", Result.NOT_BUILT), Boolean.FALSE);

        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "failed", Result.FAILURE), Boolean.TRUE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "failed", Result.ABORTED), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "failed", Result.NOT_BUILT), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "failed", Result.SUCCESS), Boolean.FALSE);
        assertEquals(isShouldSendBuildInfo.invoke(Stage.FINALIZED, "failed", Result.UNSTABLE), Boolean.FALSE);
    }


    @Test
    public void testHandleBuildEvent() {
        jenkinsRule.then(step -> {

            Run run = mock(Run.class);
            TaskListener taskListener = mock(TaskListener.class);
            Job job = mock(Job.class);
            JobModel jobModel = mock(JobModel.class);
            CE_JobListener jobListener = mock(CE_JobListener.class);
            Stage started = mock(Stage.STARTED.getClass());
            Stage completed = mock(Stage.COMPLETED.getClass());
            Stage finalized = mock(Stage.FINALIZED.getClass());

            // On Started
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);
                Object arg3 = invocationOnMock.getArgument(2);

                assertEquals(arg0.getClass().getName(), Run.class.getName());
                assertEquals(arg1.getClass().getName(), TaskListener.class.getName());
                assertEquals(arg3.getClass().getName(), long.class.getName());
                return null;
            }).when(jobListener).onStarted(run, taskListener);
            when(started.buildJobModel(job, run, 0, taskListener)).thenReturn(jobModel);

            // On Completed
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);
                Object arg3 = invocationOnMock.getArgument(2);

                assertEquals(arg0.getClass().getName(), Run.class.getName());
                assertEquals(arg1.getClass().getName(), TaskListener.class.getName());
                assertEquals(arg3.getClass().getName(), long.class.getName());
                return null;
            }).when(jobListener).onCompleted(run, taskListener);
            when(completed.buildJobModel(job, run, 0, taskListener)).thenReturn(jobModel);

            // On Finalized
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);
                Object arg3 = invocationOnMock.getArgument(2);

                assertEquals(arg0.getClass().getName(), Run.class.getName());
                Assert.assertNull(arg1);
                assertEquals(arg3.getClass().getName(), long.class.getName());
                return null;
            }).when(jobListener).onFinalized(run);
            when(finalized.buildJobModel(job, run, 0, taskListener)).thenReturn(jobModel);
        });
    }

    @Test
    public void testHandleItemEvent() {
        jenkinsRule.then(step -> {
            Stage created = mock(Stage.CREATED.getClass());
            Stage updated = mock(Stage.UPDATED.getClass());

            JobModel jobModel = mock(JobModel.class);

            Item item = mock(Item.class);

            CE_ItemListener itemListener = mock(CE_ItemListener.class);

            // On Created
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);
                assertEquals(arg0.getClass().getName(), Item.class.getName());
                assertEquals(arg1, "item");
                return null;
            }).when(itemListener).onCreated(item);
            when(created.buildJobModel(item)).thenReturn(jobModel);

            // On Updated
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);
                assertEquals(arg0.getClass().getName(), Item.class.getName());
                assertEquals(arg1, "item");
                return null;
            }).when(itemListener).onUpdated(item);
            when(updated.buildJobModel(item)).thenReturn(jobModel);
        });
    }

    @Test
    public void testHandleNodeEvent() {
        jenkinsRule.then(step -> {
            Stage online = mock(Stage.ONLINE.getClass());
            Stage offline = mock(Stage.OFFLINE.getClass());
            NodeModel nodeModel = mock(NodeModel.class);
            Computer computer = mock(Computer.class);
            TaskListener taskListener = mock(TaskListener.class);
            OfflineCause offlineCause = mock(OfflineCause.class);
            CE_NodeListener nodeListener = mock(CE_NodeListener.class);

            // Computer Online
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);

                assertEquals(arg0.getClass().getName(), Computer.class.getName());
                assertEquals(arg1, "node");
                return null;
            }).when(nodeListener).onOnline(computer, taskListener);
            when(online.buildNodeModel(computer)).thenReturn(nodeModel);

            // Computer Offline
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);

                assertEquals(arg0.getClass().getName(), Computer.class.getName());
                assertEquals(arg1, "node");
                return null;
            }).when(nodeListener).onOffline(computer, offlineCause);
            when(offline.buildNodeModel(computer)).thenReturn(nodeModel);
        });
    }

    @Test
    public void testHandleQueueEvent() {
        jenkinsRule.then(step -> {
            Stage enteredWaiting = mock(Stage.ENTERED_WAITING.getClass());
            Stage left = mock(Stage.LEFT.getClass());
            QueueModel queueModel = mock(QueueModel.class);
            Queue.WaitingItem queueWaitingItem = mock(Queue.WaitingItem.class);
            Queue.LeftItem queueLeftItem = mock(Queue.LeftItem.class);
            CE_QueueListener queueListener = mock(CE_QueueListener.class);

            // Queue Entered Waiting
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);

                assertEquals(arg0.getClass().getName(), Queue.WaitingItem.class.getName());
                assertEquals(arg1, "queue");
                return null;
            }).when(queueListener).onEnterWaiting(queueWaitingItem);
            when(enteredWaiting.buildQueueModel(queueWaitingItem)).thenReturn(queueModel);

            // Queue Left
            Mockito.doAnswer(invocationOnMock -> {
                Method method = invocationOnMock.getMethod();
                assertEquals(method.getName(), "handleEvent");

                Object arg0 = invocationOnMock.getArgument(0);
                Object arg1 = invocationOnMock.getArgument(1);

                assertEquals(arg0.getClass().getName(), Queue.WaitingItem.class.getName());
                assertEquals(arg1, "queue");
                return null;
            }).when(queueListener).onLeft(queueLeftItem);
            when(enteredWaiting.buildQueueModel(queueLeftItem)).thenReturn(queueModel);
        });
    }
}
