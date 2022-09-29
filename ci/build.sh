#!/usr/bin/env bash
set -eo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" ; pwd -P)"
PROJECT_ROOT="${SCRIPT_DIR}/.."
ZIP_FOLDER="${PROJECT_ROOT}/terraform/tmp"
BUILD_DIR="${PROJECT_ROOT}/build"

# Cleanup temp folders
rm -rf "${ZIP_FOLDER}"
rm -rf "${BUILD_DIR}"
mkdir -p "${ZIP_FOLDER}"
mkdir -p "${BUILD_DIR}"

./gradlew clean test
./gradlew assemble

pushd "${PROJECT_ROOT}" > /dev/null

popd > /dev/null

pushd "${BUILD_DIR}" > /dev/null

mv ./libs/flowfullyUserLambdaApplication.jar "${ZIP_FOLDER}/lambda.zip"
echo "Created lambda.zip"

popd > /dev/null