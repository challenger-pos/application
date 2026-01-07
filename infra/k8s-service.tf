resource "kubernetes_service" "challengeone_app" {
  metadata {
    name      = "challenge-service"
    namespace = kubernetes_namespace.challengeone.metadata[0].name
    labels = {
      app = "challengeone"
    }

    annotations = {
      "service.beta.kubernetes.io/aws-load-balancer-type"            = "nlb"
      "service.beta.kubernetes.io/aws-load-balancer-nlb-target-type" = "instance"
      "service.beta.kubernetes.io/aws-load-balancer-scheme"          = "internal"
      "service.beta.kubernetes.io/aws-load-balancer-manage-backend-security-group-rules" = "true"
    }
  }

  spec {
    selector = {
      app = "challengeone"
    }

    port {
      protocol    = "TCP"
      port        = 80
      target_port = 8080
    }

    type = "LoadBalancer"
  }
}
