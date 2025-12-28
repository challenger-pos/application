terraform {
  backend "s3" {
    bucket = "challengeone-g19"
    key    = "challengeOne/terraform.tfstate"
    region = "us-east-1"
  }
}