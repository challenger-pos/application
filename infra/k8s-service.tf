# svc do banco
# resource "kubernetes_service" "challengeone_db" {
#   metadata {
#     name      = "challengeone-db"
#     namespace = kubernetes_namespace.challengeone.metadata[0].name
#   }

#   spec {
#     selector = {
#       app = "challengeone-db"
#     }

#     port {
#       port        = 5432
#       target_port = 5432
#     }

#     type = "ClusterIP"
#   }
# }

# svc do app
resource "kubernetes_service" "challengeone_app" {
  depends_on = [ 
    kubernetes_deployment.challengeone_app
  ]
  metadata {
    name      = "challengeone-service"
    namespace = var.challengeone_namespace_name
    labels = {
      app = "challengeone"
    }
    annotations = {
      "service.beta.kubernetes.io/aws-load-balancer-type"        = "nlb"
      "service.beta.kubernetes.io/aws-load-balancer-target-type" = "ip"
      "service.beta.kubernetes.io/aws-load-balancer-healthcheck-protocol" = "HTTP"
      "service.beta.kubernetes.io/aws-load-balancer-healthcheck-port"     = "8080"
      "service.beta.kubernetes.io/aws-load-balancer-healthcheck-path"     = "/"
    }
  }

  spec {
    selector = {
      app = "challengeone"
    }

    port {
      protocol    = "TCP"
      port        = 8080
      target_port = 8080
    }

    type = "LoadBalancer"
  }
}
