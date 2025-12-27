resource "aws_apigatewayv2_api" "main_api" {
  name          = "main-http-api"
  protocol_type = "HTTP"
}