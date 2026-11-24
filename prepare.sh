#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p android-project/src/main/java/org/arabicagent/app android-project/src/main/res/drawable
mkdir -p android-project/src/main/res/xml android-project/src/main/res/values
cp screen_agent_config.xml android-project/src/main/res/xml/
cp screen_agent_strings.xml android-project/src/main/res/values/
cp MainActivity.java AgentCore.java ScreenAgentService.java android-project/src/main/java/org/arabicagent/app/
cp AndroidManifest.xml android-project/src/main/
cp agent_icon.xml android-project/src/main/res/drawable/
cp build.gradle settings.gradle android-project/
