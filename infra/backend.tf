terraform {
  backend "s3" {
    bucket = "tf-state-challenge-bucket"
    key    = "app/develop/terraform.tfstate"
    region = "us-east-2"
  }
}