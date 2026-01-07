variable "projectName" {
  default = "challengeone-g19"
}

variable "region_default" {
  default = "us-east-2"
}

variable "tags" {
  default = {
    Name = "g19-challengeone"
  }
}

variable "eks_cluster_name" {
  description = "Nome do cluster EKS"
  type        = string
}

variable "challengeone_namespace_name" {
  description = "Nome do namespace para a aplicação ChallengeOne"
  type        = string
}

variable "db_endpoint" {
  description = "Endpoint do banco RDS"
  type        = string
}