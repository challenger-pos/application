data "terraform_remote_state" "auth_lambda" {
  backend = "s3"

  config = {
    bucket = "tf-state-challenge-bucket"
    key    = "lambda/terraform.tfstate"
    region = "us-east-2"
  }
}