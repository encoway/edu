#!/bin/bash
set -ev
if [ "${TRAVIS_TAG:0:1}" == "v" -o "$TRAVIS_PULL_REQUEST" != "false" ]; then
    mvn clean verify
else
    mvn clean deploy --settings .travis/settings.xml
fi