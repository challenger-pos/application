variable "projectName" {
  default = "challengeone-g19"
}

variable "environment" {
  type = string
  default = "public"
}

variable "region_default" {
  default = "us-east-2"
}

variable "db_password" {
  description = "postgres123"
  default = "postgres123"
  sensitive   = true
}

variable "tags" {
  default = {
    Name = "g19-challengeone"
  }
  
}