terraform {
  backend "s3" {
    bucket         = "tf-state-challenge-bucket"
    key            = "api_gateway/terraform.tfstate"
    region         = "us-east-2"
    encrypt        = true
  }
}