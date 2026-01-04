data "terraform_remote_state" "infra" {
  backend = "s3"

  config = {
    bucket = "tf-state-challenge-bucket"
    key    = "kubernetes/terraform.tfstate"
    region = "us-east-2"
  }
}
