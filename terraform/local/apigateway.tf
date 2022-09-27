resource "aws_apigatewayv2_api" "flowfully_backend" {
  name          = "flowfully_backend_apigateway_api"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_stage" "flowfully_backend" {
  api_id = aws_apigatewayv2_api.flowfully_backend.id

  name        = "flowfully_backend_apigateway_api_stage"
  auto_deploy = true

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.api_gw.arn

    format = jsonencode({
      requestId               = "$context.requestId"
      sourceIp                = "$context.identity.sourceIp"
      requestTime             = "$context.requestTime"
      protocol                = "$context.protocol"
      httpMethod              = "$context.httpMethod"
      resourcePath            = "$context.resourcePath"
      routeKey                = "$context.routeKey"
      status                  = "$context.status"
      responseLength          = "$context.responseLength"
      integrationErrorMessage = "$context.integrationErrorMessage"
      }
    )
  }
}

resource "aws_apigatewayv2_integration" "flowfully_backend" {
  api_id = aws_apigatewayv2_api.flowfully_backend.id

  integration_uri    = aws_lambda_function.flowfully_backend.invoke_arn
  integration_type   = "AWS_PROXY"
  integration_method = "POST"
}

resource "aws_apigatewayv2_route" "create_user" {
  api_id = aws_apigatewayv2_api.flowfully_backend.id

  route_key = "POST /createUser"
  target    = "integrations/${aws_apigatewayv2_integration.flowfully_backend.id}"
}

resource "aws_apigatewayv2_route" "update_user" {
  api_id = aws_apigatewayv2_api.flowfully_backend.id

  route_key = "POST /updateUser"
  target    = "integrations/${aws_apigatewayv2_integration.flowfully_backend.id}"
}

resource "aws_apigatewayv2_route" "get_user" {
  api_id = aws_apigatewayv2_api.flowfully_backend.id

  route_key = "GET /getUser"
  target    = "integrations/${aws_apigatewayv2_integration.flowfully_backend.id}"
}

resource "aws_lambda_permission" "flowfully_backend" {
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.flowfully_backend.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_apigatewayv2_api.flowfully_backend.execution_arn}/*/*"
}

resource "aws_cloudwatch_log_group" "api_gw" {
  name = "/aws/api_gw/${aws_apigatewayv2_api.flowfully_backend.name}"

  retention_in_days = 14
}