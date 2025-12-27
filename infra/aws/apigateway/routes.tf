resource "aws_apigatewayv2_route" "auth_document_login" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "POST /auth"
  target    = "integrations/${aws_apigatewayv2_integration.auth_lambda.id}"
}

resource "aws_apigatewayv2_route" "auth_login" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "POST /api/v1/auth/login"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "customer_create" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "POST /api/v1/customer/create"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "customer_update" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PUT /api/v1/customer/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "customer_activate" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PUT /api/v1/customer/active/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "customer_inactivate" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PUT /api/v1/customer/inactive/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "customer_get_by_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/customer/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "customer_get_by_document" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/customer/document/{documentNumber}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "customer_delete" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "DELETE /api/v1/customer/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "part_create" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "POST /api/v1/parts"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "part_get_by_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/parts/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "part_update" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PUT /api/v1/parts/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "part_delete" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "DELETE /api/v1/parts/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "service_create" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "POST /api/v1/services"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "service_get_by_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/services/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "service_get_by_list_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/services"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "service_update" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PUT /api/v1/services/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "service_delete" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "DELETE /api/v1/services/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "user_create" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "POST /api/v1/users/create"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "user_update" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PUT /api/v1/users/update"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "user_delete" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "DELETE /api/v1/users/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "user_get_by_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/users/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "vehicle_create" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "POST /api/v1/vehicles"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "vehicle_get_by_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/vehicles/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "vehicle_get_by_plate" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/vehicles/by-plate"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "vehicle_update" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PUT /api/v1/vehicles/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "vehicle_delete" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "DELETE /api/v1/vehicles/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_create" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "POST /api/v1/work-orders/create"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_get_by_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/work-orders/{id}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_get_by_list_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/work-orders/list"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_assign_mechanic" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PATCH /api/v1/work-orders/{id}/assign-mechanic"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_status_update" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PATCH /api/v1/work-orders/{id}/status"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_status_by_id" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/work-orders/{id}/status"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_approve" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PATCH /api/v1/work-orders/{id}/approve"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_refuse" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PATCH /api/v1/work-orders/{id}/refuse"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_update_items" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "PATCH /api/v1/work-orders/{id}/update-items"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_get_history_by_document" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/work-orders/history/by-cpf/{cpfCnpj}"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}

resource "aws_apigatewayv2_route" "work_order_average_time" {
  api_id    = aws_apigatewayv2_api.main_api.id
  route_key = "GET /api/v1/work-orders/calculate-average-time"
  target    = "integrations/${aws_apigatewayv2_integration.app_backend.id}"
}
