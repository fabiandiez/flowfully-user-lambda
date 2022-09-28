terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.26.0"
    }
    archive = {
      source  = "hashicorp/archive"
      version = "~> 2.2.0"
    }
  }
  cloud {
    organization = "fdiez-tech"

    workspaces {
      name = "flowfully-user-lambda"
    }
  }

  required_version = "~> 1.2.7"
}

provider "aws" {
  region = "eu-central-1"
}
