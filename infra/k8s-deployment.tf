resource "kubernetes_deployment" "challengeone_app" {

  # depends_on = [
  #   kubernetes_deployment.challengeone_db
  # ]
  # Comentado para testar deployment sem banco de dados

  depends_on = [
    kubernetes_namespace.challengeone 
  ]

  metadata {
    name      = "challengeone"
    namespace = kubernetes_namespace.challengeone.metadata[0].name
  }

  spec {
    replicas = 1

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
        container {
          name              = "challengeone"
          image             = "thiagotierre/challengeone:1"
          image_pull_policy = "Always"
          

          port {
            container_port = 8080
          }

          # env_from {
          #   secret_ref {
          #     name = kubernetes_secret.challengeone_db.metadata[0].name
          #   }
          # }
          # Comentado para testar sem banco de dados

          env {
            name  = "SPRING_DATASOURCE_URL"
            value = "jdbc:h2:mem:testdb"
          }
          env {
            name  = "SPRING_DATASOURCE_DRIVERCLASSNAME"
            value = "org.h2.Driver"
          }

          liveness_probe {
            http_get {
              path = "/api/actuator/health/liveness"
              port = 8080
            }
            initial_delay_seconds = 10
            period_seconds        = 10
            timeout_seconds       = 2
            failure_threshold     = 3
          }
          # Recursos otimizados para free tier (t3.micro tem 1 CPU, 1GB RAM)
          resources {
            requests = {
              cpu    = "100m"
              memory = "256Mi"
            }
            limits = {
              cpu    = "250m"
              memory = "512Mi"
            }
          }
          

          security_context {
            allow_privilege_escalation = false
            read_only_root_filesystem = false
          }
        }

        security_context {
          run_as_non_root = false
        }
      }
    }

    strategy {
      type = "RollingUpdate"
      rolling_update {
        max_unavailable = 0
        max_surge       = 1
      }
    }
  }
}
