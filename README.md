CloudEvents-Plugin
======================================

   * [Introduction](#introduction)
   * [Getting Started](#getting-started)
   * [Events Documentation for Jenkins as Source](#events-documentation-for-jenkins-as-source)
      * [Queue Events](#queue-events)
      * [Build Events](#build-events)
      * [Job Events](#job-events)
      * [Node Events](#node-events)
   * [Issues](#issues)
   * [Contributing](#contributing)

Introduction
======================================

The CloudEvents Plugin for Jenkins allows interoperability between different CI/CD tools by adopting industry-standard
specification for events called [CloudEvents](https://cloudevents.io/).

By using this plugin in your workflows, you can emit and consume CloudEvents for various events in Jenkins relating to
Jenkins objects (jobs, projects, queues, etc).

The standardization of events by adopting CloudEvents will make it easier to interoperate systems without having to
write adapters.

-------

Getting Started
==============

There are 2 usages of this plugin:

1. **Jenkins as a Source.**

    1. To configure Jenkins as a Source, first click on *Manage Jenkins* in the Root-Actions menu on the left.
       ![Manage Jenkins](public/manage_jenkins.png)

    2. Once you are in Manage Jenkins UI, search for *Configure System* under *System Configuration*.
       ![Configure System](public/configure_system.png)

    3. Inside the *Configure System* UI, scroll down to the *CloudEvents Plugin* section and configure the following:
        - Select the type of the sink.
          ![Type of Sink](public/sinkType.png)
        - Enter the URL of the sink where requests from Jenkins will be routed.
          ![URL of the SInk](public/sinkURL.png)
        - Select the events this sink will receive.
          ![Select event/s](public/events-select.png)


2. **Jenkins as a Sink.**
    1. To use Jenkins as a Sink, enter the Jenkins Sink URL as the Sink to send CloudEvents from other systems.
    2. TODO: Jenkins Sink URL.

-------

Events Documentation for Jenkins as Source
============== 

When users configure Jenkins to be used as a Source by providing the type of the Sink along with its URL, Jenkins will
emit CloudEvents-compliant events which contains event-metadata and events-data. In this iteration of development,
events will be sent over to the configured sink sequentially, that is, in the oder they happen.

CloudEvents emanating from Jenkins are sent over to the sink as an HTTP request. All events follow
the [CloudEvents binary content mode](https://github.com/cloudevents/spec/blob/v1.0.1/http-protocol-binding.md#31-binary-content-mode)
where the events metadata is present inside HTTP request-headers.

Below are the events this plugin supports, alongside event-metadata and event-data which will be emitted for each of
these events.

Queue Events
-------

1. **Queue Entered Waiting**: Emitted as a job enters the queue.

    - Event Metada
        ```
        ce-specversion: 1.0
        ce-id: c42d1f19-9908-43da-9a7f-404405c52b60
        ce-type: org.jenkinsci.queue.entered_waiting
        ce-source: job/test2
        ```

   - Event Data
        ```json
        {
          "ciUrl": "http://3.101.116.80/",
          "displayName": "test2",
          "entryTime": 1626611053609,
          "exitTime": null,
          "startedBy": "shruti chaturvedi",
          "jenkinsQueueId": 25,
          "status": "ENTERED_WAITING",
          "duration": 0,
          "queueCauses": [
            {
            "reasonForWaiting": "In the quiet period. Expires in 0 ms",
            "type": "entered_waiting"
            }
          ]
        }
        ```
2. **Queue Left**: Emitted as a job leaves the queue
    - Event Metada
        ```
        ce-specversion: 1.0
        ce-id: 345acb40-5b5a-4e1a-a3da-b5b31dbabb08
        ce-type: org.jenkinsci.queue.left
        ce-source: job/test2
        ```

    - Event Data
         ```json
         {
            "ciUrl": "http://3.101.116.80/",
            "displayName": "test2",
            "entryTime": 1626611053609,
            "exitTime": 1626611053624,
            "startedBy": "shruti chaturvedi",
            "jenkinsQueueId": 25,
            "status": "LEFT",
            "duration": 15
         }
         ```

Build Events
-------

1. **Job Started**: Emitted as build of a job has started.
    - Event Metada
        ```
        ce-specversion: 1.0
        ce-id: 00feb6d9-400a-4b4d-b661-ab68a563179e
        ce-type: org.jenkinsci.job.started
        ce-source: job/test2
        ```

    - Event Data
         ```json
         {
            "userId": "SYSTEM",
            "userName": "SYSTEM",
            "name": "test2",
            "displayName": "test2",
            "url": "job/test2/",
            "build": {
                "fullUrl": "http://3.101.116.80/job/test2/13/",
                "number": 13,
                "queueId": 25,
                "timestamp": 1626611053632,
                "duration": 0,
                "phase": "STARTED",
                "status": null,
                "url": "job/test2/13/",
                "displayName": null,
                "parameters": {
                    "IsSCM": "true"
                },
                "scmState": {
                    "url": "https://github.com/ShrutiC-git/scmConfig.git",
                    "branch": "origin/main",
                    "commit": "218c63f230732c6d561fefd00fd0d2ac4ded1a0c"
                }
            }
         }
         ```
   
2. **Job Completed**: Emitted as build of a job has completed.
    - Event Metada
        ```
        ce-specversion: 1.0
        ce-id: 1fe6f79d-19b8-4e47-ab20-675b6ed8c514
        ce-type: org.jenkinsci.job.completed
        ce-source: job/test2
        ```

    - Event Data
         ```json
         {
            "userId": "SYSTEM",
            "userName": "SYSTEM",
            "name": "test2",
            "displayName": "test2",
            "url": "job/test2/",
            "build": {
                "fullUrl": "http://3.101.116.80/job/test2/13/",
                "number": 13,
                "queueId": 25,
                "timestamp": 1626611059215,
                "duration": 5583,
                "phase": "COMPLETED",
                "status": "SUCCESS",
                "url": "job/test2/13/",
                "displayName": null,
                "parameters": {
                    "IsSCM": "true"
                },
                "scmState": {
                    "url": "https://github.com/ShrutiC-git/scmConfig.git",
                    "branch": "origin/main",
                    "commit": "218c63f230732c6d561fefd00fd0d2ac4ded1a0c"
                }
            }
         }
         ```   

3. **Job Finalized**: Emitted as build of a job has finalized.
    - Event Metada
        ```
        ce-specversion: 1.0
        ce-id: cb7fba47-46fa-4d4d-8e46-a239fc4ff801
        ce-type: org.jenkinsci.job.finalzed
        ce-source: job/test2
        ```

    - Event Data
         ```json
         {
            "userId": "SYSTEM",
            "userName": "SYSTEM",
            "name": "test2",
            "displayName": "test2",
            "url": "job/test2/",
            "build": {
                "fullUrl": "http://3.101.116.80/job/test2/13/",
                "number": 13,
                "queueId": 25,
                "timestamp": 1626611059241,
                "duration": 5583,
                "phase": "FINALIZED",
                "status": "SUCCESS",
                "url": "job/test2/13/",
                "displayName": null,
                "parameters": {
                    "IsSCM": "true"
                },
                "scmState": {
                    "url": "https://github.com/ShrutiC-git/scmConfig.git",
                    "branch": "origin/main",
                    "commit": "218c63f230732c6d561fefd00fd0d2ac4ded1a0c"
                }
            }
         }
         ```   
    
4. **Job Failed**: Emitted as build of a job has failed.
    - Event Metada
        ```
        ce-specversion: 1.0
        ce-id: cb7fba47-46fa-4d4d-8e46-a239fc4ff801
        ce-type: org.jenkinsci.job.failed
        ce-source: job/fail
        ```

    - Event Data
         ```json
         {
            "userId": "SYSTEM",
            "userName": "SYSTEM",
            "name": "fail",
            "displayName": "fail",
            "url": "job/fail/",
            "build": {
                "fullUrl": "http://3.101.116.80/job/fail/4/",
                "number": 4, 
                "queueId": 28,
                "timestamp": 1626619978412,
                "duration": 1456,
                "phase": "FINALIZED",
                "status": "FAILED",
                "url": "job/fail/4/",
                "displayName": null,
                "parameters": null,
                "scmState": {
                    "url": null,
                    "branch": null,
                    "commit": null
                }
            }
         }
         ```   

Job Events
-------

1. **Job Created**: Emitted as a new job is created.

    - Event Metada
        ```
        ce-specversion: 1.0
        ce-id: 1930d373-e2e8-4e4e-86ae-259a6e9a7923
        ce-type: org.jenkinsci.job.created
        ce-source: job/Item
        ```

    - Event Data
         ```json
      {
        "userId": "shruti",
        "userName": "shruti chaturvedi",
        "status": "CREATED",
        "name": "Item",
        "displayName": "Item",
        "url": "job/Item/",
        "createdDate": 1626621074004,
        "configFile": "<?xml version='1.1' encoding='UTF-8'?>\n<project>\n  <keepDependencies>false</keepDependencies>\n  <properties/>\n  <scm class=\"hudson.scm.NullSCM\"/>\n  <canRoam>false</canRoam>\n  <disabled>false</disabled>\n  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n  <triggers/>\n  <concurrentBuild>false</concurrentBuild>\n  <builders/>\n  <publishers/>\n  <buildWrappers/>\n</project>"
      }
         ```
   
2. **Job Updated**: Emitted as an existing job is updated.
    - Event Metada
        ```
        ce-specversion: 1.0
        ce-id: 1930d373-a1a1-4e4e-52er-259a6e9a1598
        ce-type: org.jenkinsci.job.updated
        ce-source: job/Item
        ```

    - Event Data
         ```json
      {
        "userId": "shruti",
        "userName": "shruti chaturvedi",
        "status": "UPDATED",
        "name": "Item",
        "displayName": "Item",
        "url": "job/Item/",
        "updatedDate": 1626621078878,
        "configFile": "<?xml version='1.1' encoding='UTF-8'?>\n<project>\n  <keepDependencies>false</keepDependencies>\n  <properties/>\n  <scm class=\"hudson.scm.NullSCM\"/>\n  <canRoam>false</canRoam>\n  <disabled>false</disabled>\n  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n  <triggers/>\n  <concurrentBuild>false</concurrentBuild>\n  <builders/>\n  <publishers/>\n  <buildWrappers/>\n</project>"
      }
         ```

Node Events
-------
1. **Node Online**: Emitted when a node comes online.
   - Event Metada
       ```
       ce-specversion: 1.0
       ce-id: 8c78daa6-011c-4687-b5a5-b269087f6830
       ce-type: org.jenkinsci.node.online
       ce-source: node/test_node
       ```

   - Event Data
        ```json
        {
         "numExecutors": 2,
         "offlineCause": null,
         "nodeName": "test_node",
         "cachedHostName": "DESKTOP-A47DM8N",
         "terminatedBy": null,
         "connectTime": 0
        }
        ```

   
2. **Node Offline**: Emitted when a node goes offline.
   - Event Metada
       ```
       ce-specversion: 1.0
       ce-id: 70160f83-885c-4574-8a55-7660593c9b76
       ce-type: org.jenkinsci.node.offline
       ce-source: node/test_node
       ```

   - Event Data
        ```json
        {
         "numExecutors": 2,
         "offlineCause": {
            "timestamp": 1626622018554,
            "cause": {
               "cause": null,
               "stackTrace": [
                  {
                     "methodName": "onRecvClosed",
                     "fileName": "NetworkLayer.java",
                     "lineNumber": 154,
                     "className": "org.jenkinsci.remoting.protocol.NetworkLayer",
                     "nativeMethod": false
                  },
                  {
                     "methodName": "ready",
                     "fileName": "NIONetworkLayer.java",
                     "lineNumber": 179,
                     "className": "org.jenkinsci.remoting.protocol.impl.NIONetworkLayer",
                     "nativeMethod": false
                  }
               ],
               "localizedMessage": null,
               "message": null,
               "suppressed": []
            },
            "shortDescription": "java.nio.channels.ClosedChannelException",
            "time": 1626622018554
         },
         "nodeName": "test_node",
         "cachedHostName": "DESKTOP-A47DM8N",
         "terminatedBy": [],
         "connectTime": 0
        }
        ```
     
---------------------

Issues
==========

TODO Decide where you're going to host your issues, the default is Jenkins JIRA, but you can also enable GitHub issues,
If you use GitHub issues there's no need for this section; else add the following line:

Report issues and enhancements in the [Jenkins issue tracker](https://issues.jenkins-ci.org/).

-------------

Contributing
==========

TODO review the default [CONTRIBUTING](https://github.com/jenkinsci/.github/blob/master/CONTRIBUTING.md) file and make sure it is appropriate for your plugin, if not then add your own one adapted from the base file

Refer to our [contribution guidelines](https://github.com/jenkinsci/.github/blob/master/CONTRIBUTING.md)
