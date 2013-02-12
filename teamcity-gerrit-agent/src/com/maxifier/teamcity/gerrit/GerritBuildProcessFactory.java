package com.maxifier.teamcity.gerrit;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.AgentLifeCycleAdapter;
import jetbrains.buildServer.agent.AgentLifeCycleListener;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author aleksey.didik@maxifier.com (Aleksey Didik)
 */
public class GerritBuildProcessFactory implements CommandLineBuildServiceFactory, AgentBuildRunnerInfo {

    private final Map<AgentRunningBuild, BuildFinishedStatus> statuses
            = new WeakHashMap<AgentRunningBuild, BuildFinishedStatus>();

    public GerritBuildProcessFactory(EventDispatcher<AgentLifeCycleListener> eventDispatcher) {
        eventDispatcher.addListener(new AgentLifeCycleAdapter() {
            @Override
            public void runnerFinished(@NotNull BuildRunnerContext runner, @NotNull BuildFinishedStatus status) {
                BuildFinishedStatus previousStepStatus = statuses.get(runner.getBuild());
                //save build step finished status if it's no step finished before or previous step finished with success.
                if (previousStepStatus == null || previousStepStatus.equals(BuildFinishedStatus.FINISHED_SUCCESS)) {
                    statuses.put(runner.getBuild(), status);
                } else {
                    //if previous step finished without success, whole build failed.
                    //keep failed status for build in the map.
                }

            }
        });
    }

    @NotNull
    @Override
    public String getType() {
        return "GerritVerification";
    }

    @Override
    public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
        return true;
    }

    @NotNull
    @Override
    public CommandLineBuildService createService() {
        return new GerritBuildProcess(statuses);
    }

    @NotNull
    @Override
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return this;
    }
}
