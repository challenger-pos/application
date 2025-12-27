resource "aws_apigatewayv2_integration" "auth_lambda" {
  api_id                 = aws_apigatewayv2_api.main_api.id
  integration_type       = "AWS_PROXY"
  integration_uri        = data.terraform_remote_state.auth_lambda.outputs.lambda_arn
  payload_format_version = "2.0"
}

resource "aws_apigatewayv2_integration" "app_backend" {
  api_id                 = aws_apigatewayv2_api.main_api.id
  integration_type       = "HTTP_PROXY"
  integration_method     = "ANY"
  integration_uri        = var.app_base_url
  payload_format_version = "1.0"
}

/*resource "aws_apigatewayv2_integration" "app_backend" {
  api_id                 = aws_apigatewayv2_api.main_api.id
  integration_type       = "HTTP_PROXY"
  connection_type        = "VPC_LINK"
  connection_id          = aws_apigatewayv2_vpc_link.app.id
  integration_uri        = var.alb_listener_arn
  integration_method     = "ANY"
  payload_format_version = "1.0"
}*/