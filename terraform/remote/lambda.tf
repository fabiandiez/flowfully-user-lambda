variable "flowfully_user_lambda-name" {
  default = "local-flowfully_user_lambda"
}

resource "aws_lambda_function" "flowfully_user_lambda-function" {
  function_name    = var.flowfully_user_lambda-name
  filename         = "${path.module}/../tmp/lambda.zip"
  source_code_hash = filebase64sha256("${path.module}/../tmp/lambda.zip")
  handler          = "org.springframework.cloud.function.adapter.aws.FunctionInvoker"
  runtime          = "java11"
  role             = aws_iam_role.flowfully_user_lambda-role.arn
  timeout          = 30

  environment {
    variables = {
      MONGODB_ENCRYPTION_KEY = data.aws_secretsmanager_secret_version.flowfully_user_lambda-mongodb_encryption_key.secret_string
      MONGODB_HOSTNAME       = aws_docdb_cluster.flowfully_user_lambda-db.cluster_identifier
    }
  }

  depends_on = [
    aws_cloudwatch_log_group.flowfully_user_lambda-log_group
  ]
}

resource "aws_iam_role" "flowfully_user_lambda-role" {
  name = "flowfully_user_lambda-exec_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "flowfully_user_lambda-policy" {
  role       = aws_iam_role.flowfully_user_lambda-role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_cloudwatch_log_group" "flowfully_user_lambda-log_group" {
  name              = "/aws/lambda/${var.flowfully_user_lambda-name}"
  retention_in_days = 14
}