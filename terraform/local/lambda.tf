variable "flowfully_backend-lambda_name" {
  default = "local-flowfully_backend_lambda"
}

resource "aws_lambda_function" "flowfully_backend" {
  function_name    = var.flowfully_backend-lambda_name
  filename         = "${path.module}/../tmp/lambda.zip"
  source_code_hash = filebase64sha256("${path.module}/../tmp/lambda.zip")
  handler          = "org.springframework.cloud.function.adapter.aws.FunctionInvoker"
  runtime          = "java11"
  role             = aws_iam_role.flowfully_backend.arn
  timeout          = 30

  environment {
    variables = {
      SPRING_PROFILES_ACTIVE = "local"
      MONGODB_ENCRYPTION_KEY = data.aws_secretsmanager_secret_version.mongodb_encryption_key.secret_string
      MONGODB_HOSTNAME       = "local-flowfully_db" // TODO: Use actual hostname in live
    }
  }

  depends_on = [
    aws_cloudwatch_log_group.flowfully_backend
  ]
}

resource "aws_iam_role" "flowfully_backend" {
  name = "flowfully_flowfully_backend_exec_role"

  assume_role_policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [
      {
        Action    = "sts:AssumeRole"
        Effect    = "Allow"
        Sid       = ""
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "flowfully_backend" {
  role       = aws_iam_role.flowfully_backend.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_cloudwatch_log_group" "flowfully_backend" {
  name              = "/aws/lambda/${var.flowfully_backend-lambda_name}"
  retention_in_days = 14
}