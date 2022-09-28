data "aws_secretsmanager_secret" "flowfully_user_lambda-mongodb_encryption_key" {
  name = "flowfully-user-lambda/mongodb-encryption-key"
}

data "aws_secretsmanager_secret_version" "flowfully_user_lambda-mongodb_encryption_key" {
  secret_id = data.aws_secretsmanager_secret.flowfully_user_lambda-mongodb_encryption_key.id
}