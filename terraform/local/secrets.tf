data "aws_secretsmanager_secret" "mongodb_encryption_key" {
  name = "flowfully-backend/mongodb-encryption-key"
}

data "aws_secretsmanager_secret_version" "mongodb_encryption_key" {
  secret_id = data.aws_secretsmanager_secret.mongodb_encryption_key.id
}