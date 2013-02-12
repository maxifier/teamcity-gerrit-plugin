package com.maxifier.teamcity.gerrit;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.vcs.VcsRootEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author aleksey.didik@maxifier.com (Aleksey Didik)
 */
public class GerritBuildProcess extends BuildServiceAdapter implements AgentBuildRunnerInfo {


    private final Map<AgentRunningBuild, BuildFinishedStatus> statuses;
    private ProgramCommandLine programCommandLine;

    public GerritBuildProcess(Map<AgentRunningBuild, BuildFinishedStatus> statuses) {
        this.statuses = statuses;
    }



    @Override
    public void afterInitialized() throws RunBuildException {
        super.afterInitialized();
    }

    @Override
    public void beforeProcessStarted() throws RunBuildException {
        getLogger().progressStarted("Start Gerrit Verification");
        BuildFinishedStatus previousStepStatus = statuses.get(getBuild());
        if (previousStepStatus == null) {
            throw new RunBuildException("Unable to recognize previous build steps result." +
                    " It should be at least one build step before Gerrit Verification Step used.");
        }
        String buildId = String.valueOf(getBuild().getBuildId());
        String buildTypeId = getBuild().getBuildTypeId();
        //get first available vcsRootEntry :(
        List<VcsRootEntry> vcsRootEntries = getBuild().getVcsRootEntries();
        if (vcsRootEntries.isEmpty()) {
            throw new RunBuildException("VCS root is not defined for this build configuration.");
        }
        if (vcsRootEntries.size() > 1) {
            throw new RunBuildException("More than one VCS root defined for this build," +
                    " but only one is supported by runner.");
        }
        VcsRootEntry vcsRootEntry = vcsRootEntries.get(0);
        String patchSet = getBuild().getBuildCurrentVersion(vcsRootEntry.getVcsRoot());

        String verified;
        switch (previousStepStatus) {
            case FINISHED_SUCCESS:
                verified = "+1";
                break;
            case FINISHED_FAILED:
            case FINISHED_WITH_PROBLEMS:
            case INTERRUPTED:
                verified = "-1";
                break;
            default:
                verified = "-1";
        }
        String gerritHost = getRunnerParameters().get("gerritHost");
        String gerritPort = getRunnerParameters().get("gerritPort");
        String teamCityUrl = getAgentConfiguration().getServerUrl();

        getLogger().progressMessage(String.format("Verify changes value: %s%n" +
                "Patch set: %s%n" +
                "Gerrit host: %s%n" +
                "Gerrit port: %s%n" +
                "TeamCity URL: %s",
                verified, patchSet, gerritHost, gerritPort, teamCityUrl));



        String gerritCommand = String.format("\"gerrit verify --verified=%s" +
                " --message=%s/viewLog.html?buildId=%s&tab=buildResultsDiv&buildTypeId=%s %s\"",
                verified, teamCityUrl, buildId, buildTypeId, patchSet);
        getLogger().progressMessage("Gerrit command: " + gerritCommand);

        programCommandLine
                = new SimpleProgramCommandLine(getRunnerContext(), "ssh",
                Arrays.asList(gerritHost, "-p", gerritPort, "-t", gerritCommand));

    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        return programCommandLine;
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
}
