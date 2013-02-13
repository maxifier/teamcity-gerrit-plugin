TeamCity Gerrit Plugin
======================

This plugin adds an additional build runner with possibility to Label Verify changes in Gerrit.

Compatibility
=============

Plugin is compatible with TeamCity 7.1 at least. Feel free to check other versions.
Due to 'ssh' command usage, it'll work only on Linux Agents.

Installation
============
Drop gerrit-integration.zip to ~/.BuildServer/plugins folder on TeamCity server. Restart TeamCity.
Agents will be updated to use new plugin.

gerrit-integration.zip can be found in out folder or download directly
https://github.com/maxifier/teamcity-gerrit-plugin/raw/master/out/gerrit-integration.zip


Usage
=====

1. Create build configuration to verify changes in Gerrit. Add VCS root for this configuration.
   It's possible to ise only one VCS root for this runner.

2. Add build steps are necessary to verify changes. Maven for example. Plugin would Label Verify '+1'
   only if all build steps succeed.

3. Add Gerrit Verification build step. This build step should be executed even if previous steps are failed.
   Set up gerrit hostname and port are necessary for ssh connection to gerrit.
   Include username in hostname like harry.potter@gerrit.hogwarts.com. Be sure private key of used user is placed
   on build agent.
4. Add Branch Remote Run Trigger with branches pattern refs/changes/*. In this case TeamCity will run personal build
   every time any change is added. Due to TeamCity limitations, personal build would be run only if commiter
   signature is recognized as one of TeamCity users. Be sure everyone set up 'Version Control Username Settings' for Git.
   Check teamcity-remote-run.log if personal build isn't started. (Administartion -> Diagnostic -> Server Logs)


Now, every new change on Gerrit will be caught by remote run trigger and build run. Gerrit Verification build step will
catch verification step result and call ssh gerrit command to Label Verify. +1 if build success, -1 otherwise.


License
=======

Apache License, Version 2.0

Develop
=======

It's IntelliJ Idea project, all api libs included. Check artifacts output path before build.
