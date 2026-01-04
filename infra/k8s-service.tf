resource "kubernetes_service" "challengeone_app" {
  metadata {
    name      = "challengeone-service"
    namespace = kubernetes_namespace.challengeone.metadata[0].name
    labels = {
      app = "challengeone"
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

    type = "ClusterIP"
  }
}
