data "terraform_remote_state" "infra" {
  backend = "s3"
  config = {
    bucket = "tf-state-challenge-bucket"
    key    = "challengeOne/terraform.tfstate"
    region = "us-east-2"
  }
}