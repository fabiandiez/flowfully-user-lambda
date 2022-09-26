resource "aws_sqs_queue" "flowfully_backend-incoming_queue" {
  name                       = "local-flowfully_backend-incoming_queue"
  visibility_timeout_seconds = 30
}