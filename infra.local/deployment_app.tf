resource "kubernetes_deployment" "challengeone_app" {

  depends_on = [
    kubernetes_stateful_set.challengeone_db
  ]

  metadata {
    name      = "challengeone"
    namespace = kubernetes_namespace.challengeone.metadata[0].name
  }

  spec {

    selector {
      match_labels = {
        app = "challengeone"
      }
    }

    template {
      metadata {
        labels = {
          app = "challengeone"
        }
      }

      spec {
        # init_container {
        #   name  = "wait-for-db"
        #   image = "busybox"
        #   command = [
        #     "sh", "-c",
        #     "echo 'Aguardando banco de dados...' && until nc -z challengeone-db 5432; do echo waiting; sleep 3; done; echo 'Banco disponível!'"
        #   ]
        # }
        container {
          name              = "challengeone"
          image             = "thiagotierre/challengeone:1"
          image_pull_policy = "Always"

          port {
            container_port = 8080
          }

          env_from {
            secret_ref {
              name = kubernetes_secret.challengeone_db.metadata[0].name
            }
          }

          liveness_probe {
            http_get {
              path = "/api/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 20
            period_seconds        = 10
            timeout_seconds       = 2
            failure_threshold     = 3
          }

          readiness_probe {
            http_get {
              path = "/api/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds        = 5
            timeout_seconds       = 2
            failure_threshold     = 3
          }
          
          resources {
            requests = {
              cpu    = "500m"
              memory = "512Mi"
            }
          #   limits = {
          #     cpu    = "500m"
          #     memory = "256Mi"
          #   }
          }
        }
      }
    }
  }
  wait_for_rollout = false
}
