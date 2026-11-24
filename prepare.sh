#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p android-project/src/main/java/org/arabicagent/app android-project/src/main/res/drawable
cp MainActivity.java AgentCore.java android-project/src/main/java/org/arabicagent/app/
cp AndroidManifest.xml android-project/src/main/
cp agent_icon.xml android-project/src/main/res/drawable/
cp build.gradle settings.gradle android-project/
