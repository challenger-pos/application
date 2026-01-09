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

  wait_for_rollout = false

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
          image             = "thiagotierre/challengeone:latest"
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
            value = "jdbc:postgresql://${data.terraform_remote_state.rds.outputs.rds_endpoint_host}:${data.terraform_remote_state.rds.outputs.db_port}/${data.terraform_remote_state.rds.outputs.db_name}?currentSchema=${var.environment}"
          }

          env {
            name  = "SPRING_DATASOURCE_USERNAME"
            value = data.terraform_remote_state.rds.outputs.db_username
          }

          env {
            name  = "SPRING_DATASOURCE_PASSWORD"
            value = var.db_password
          }

          env {
            name  = "DB_SCHEMA"
            value = "public"
          }

          env {
            name  = "JAVA_TOOL_OPTIONS"
            value = "-XX:+UseSerialGC -XX:MaxRAMPercentage=75 -Xss512k"
          }

          startup_probe {
            http_get {
              path = "/api/actuator/health/liveness"
              port = 8080
            }
            initial_delay_seconds = 60
            failure_threshold = 30
            period_seconds    = 10
          }

          liveness_probe {
            http_get {
              path = "/api/actuator/health/liveness"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds  = 30
            timeout_seconds = 5
            failure_threshold = 3
          }

          readiness_probe {
            http_get {
              path = "/api/actuator/health/readiness"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds        = 30
          }
          # Recursos otimizados para free tier (t3.micro tem 1 CPU, 1GB RAM)
          resources {
            requests = {
              cpu    = "50m"
              memory = "320Mi"
            }
            limits = {
              cpu    = "500m"
              memory = "448Mi"
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
        max_unavailable = 1
        max_surge       = 0
      }
    }
  }
}
