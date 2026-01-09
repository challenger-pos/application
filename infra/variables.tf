variable "projectName" {
  default = "challengeone-g19"
}

variable "environment" {
  type = string
  default = "homologation"
}

variable "region_default" {
  default = "us-east-2"
}


variable "db_schema" {
  type = string
  default = "public"
}

variable "tags" {
  default = {
    Name = "g19-challengeone"
  }
  
}

variable "db_password" {
  description = "Database password (set via CI as TF_VAR_db_password)"
  type        = string
  sensitive   = true
}

variable "db_username" {
  description = "Database username"
  type        = string
  default     = "postgres"
}

variable "db_name" {
  description = "Database name"
  type        = string
  default     = "challengeone"
}

variable "db_host" {
  description = "Database host (service name or endpoint)"
  type        = string
  default     = "challengeone-db"
}

variable "db_port" {
  description = "Database port"
  type        = string
  default     = "5432"
}