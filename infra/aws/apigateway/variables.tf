variable "app_base_url" {
  description = "URL base da aplicação backend"
  type        = string
}

variable "alb_listener_arn" {
  description = "ARN do listener do ALB da aplicação"
  type        = string
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "vpc_link_sg_id" {
  type = string
}

variable "environment" {
  type    = string
}