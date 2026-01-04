variable "projectName" {
  default = "challengeone-g19"
}

variable "region_default" {
  default = "us-east-2"
}

variable "cidr_vpc" {
  default = "10.0.0.0/16"
}

variable "eks_cluster_name" {
  description = "Nome do cluster EKS já existente"
  type        = string
}

variable "tags" {
  default = {
    Name = "g19-challengeone"
  }
  
}