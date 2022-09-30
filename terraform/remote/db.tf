resource "aws_docdb_cluster" "flowfully_user_lambda-db" {
  cluster_identifier      = "flowfully_user_lambda-db"
  engine                  = "docdb"
  master_username         = "foo"              // todo: use secrets manager
  master_password         = "mustbeeightchars" // todo: use secrets manager
  backup_retention_period = 5
  preferred_backup_window = "02:00-04:00"
  skip_final_snapshot     = true
}