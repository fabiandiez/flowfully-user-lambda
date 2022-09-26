#!/usr/bin/env bash
set -eo pipefail

{ # Boilerplate
  set -e

  # Ensure we have associative arrays, co-processes, map files and more
  if test "${BASH_VERSINFO[0]}" -lt 4; then exit 1; fi

  # Needed by the `usage` command
  readonly __functions_before="$(declare -F | cut -d' ' -f3)"

  usage() {
    echo "Available commands:"
    comm -13 <(echo "$__functions_before") <(echo "$__functions_after") | sed '/^__/d;s/^/    - /'
  }

}

export AWS_REGION="eu-central-1"
export LOCALSTACK_ENDPOINT="http://localhost:4566"
export INCOMING_QUEUE_URL="${LOCALSTACK_ENDPOINT}/local-flowfully_backend-incoming_queue"
export LAMBDA_FUNCTION_NAME="local-flowfully_backend_lambda"

# See https://github.com/hashicorp/terraform-provider-aws/issues/20274
export GODEBUG=asyncpreemptoff=1

function start-mocks() {
  docker rm -f localstack_main || true
  localstack start --detached
}

function build-app() {
  ./ci/build.sh
}

function deploy-app() {
  cd terraform/local
  tflocal apply -auto-approve
}

function build-and-deploy() {
  build-app
  deploy-app
}

function start() {
  start-mocks
  deploy-app
  start-app
}

function send-message() {
  __aws sqs send-message --queue-url $INCOMING_QUEUE_URL --message-body "$1"
}

function lambda-logs() {
  __aws logs tail /aws/lambda/$LAMBDA_FUNCTION_NAME --follow
}

function __aws() {
  aws --region $AWS_REGION --endpoint $LOCALSTACK_ENDPOINT "$@"
}

{ # Boilerplate
  # Needed by the `usage` command
  readonly __functions_after="$(declare -F | cut -d' ' -f3)"

  "${@:-usage}"
}
