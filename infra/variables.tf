variable "projectName" {
  default = "challengeone-g19"
}

variable "environment" {
  type = string
  default = "develop"
}

variable "region_default" {
  default = "us-east-2"
}

variable "db_schema" {
  type = string
  default = "public"
}

variable "db_password" {
  description = "Database password (set via CI)"
  type        = string
  sensitive   = true
}

variable "db_username" {
  description = "Database username"
  type        = string
  default     = "postgres"
}

variable "tags" {
  default = {
    Name = "g19-challengeone"
  }
  
}