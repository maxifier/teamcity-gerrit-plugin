teamcity-gerrit-plugin
======================

This plugin adds the additional build runner with possibility to Label Verify changes in Gerrit.

Compatibility
=============

Plugin is compatible with TeamCity 7.1 at least. Feel free to check other verwions.

Installation
============
Drop gerrit-integration.zip to .BuildServer/plugins folder. Restart TeamCity server.
Due to transfer of new runner to agents, it can take some to time.

gerrit-integration.zip can be found in out folder.


Usage
=====

1. Create build configuration to verify changes in Gerrit. Add VCS root for this configuration.
   It's possible to ise only one VCS root for this runner.

2. Add build step necessary to verify change. Maven for example.

3. Add Gerrit Verification build step. This build step should be executed even if previous step is failed.
   Set up gerrit hostname and port are necessary for ssh connection to gerrit and execute approval command.
   Include username in hostname like harry.potter@gerrit.hogwarts.com. Be sure private key of used user is placed
   on build agent.
4. Add Branch Remote Run Trigger with branches pattern refs/changes/*. In this case TeamCity will run personal build
   every time any change will be added. Due to TeamCity limitations, personal build would be run only if commiter
   signature is recognized as one of TeamCity users. Be sure everyone set up 'Version Control Username Settings' for Git.
   Check teamcity-remote-run.log is personal build is not started. (Administartion -> Diagnostic -> Server Logs)


Now, every new change on Gerrit will be caught by remote run trigger and build run. Gerrit Verification build step will
catch verification step result and call ssh gerrit command to Label Verify. +1 if build success, -1 otherwise.


License
=======

Apache License, Version 2.0

Develop
=======

It's IntelliJ Idea project, all api libs included. Check artifacts output path before build.
