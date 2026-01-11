terraform {
  backend "s3" {
    bucket = "tf-state-challenge-bucket"
    key   = "app/homologation/terraform.tfstate"
    region = "us-east-2"
  }
}