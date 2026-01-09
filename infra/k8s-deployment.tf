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
    replicas = 2

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
        annotations = {
          "tags.datadoghq.com/env"     = "dev"
          "tags.datadoghq.com/service" = "challengeone"
          "tags.datadoghq.com/version" = "1.0.0"
          "admission.datadoghq.com/enabled" = "true"
        }
      }

      spec {
        volume {
          name = "dd-java-agent"
          empty_dir {}
        }

        init_container {
          name  = "dd-java-agent-init"
          image = "curlimages/curl:8.10.1"
          command = ["sh","-c","curl -L -o /dd/dd-java-agent.jar https://dtdg.co/latest-java-tracer"]
          volume_mount {
            name       = "dd-java-agent"
            mount_path = "/dd"
          }
        }

        container {
          name              = "challengeone"
          image             = "thiagotierre/challengeone:latest"
          image_pull_policy = "Always"
          

          port {
            container_port = 8080
          }

          env_from {
            secret_ref {
              name = kubernetes_secret.challengeone_secret.metadata[0].name
            }
          }

          env {
            name  = "JAVA_TOOL_OPTIONS"
            value = "-javaagent:/dd/dd-java-agent.jar"
          }
          env {
            name  = "DD_SERVICE"
            value = "challengeone"
          }
          env {
            name  = "DD_ENV"
            value = "dev"
          }
          env {
            name  = "DD_VERSION"
            value = "1.0.0"
          }
          env {
            name  = "DD_LOGS_INJECTION"
            value = "true"
          }
          env {
            name  = "DD_APPSEC_ENABLED"
            value = "true"
          }
          env {
            name  = "DD_IAST_ENABLED"
            value = "true"
          }
          env {
            name  = "DD_AGENT_HOST"
            value = "datadog-agent.default.svc.cluster.local"
          }
          env {
            name  = "DD_DOGSTATSD_PORT"
            value = "8125"
          }
          env {
            name  = "DATADOG_STATSD_HOST"
            value = "datadog-agent.default.svc.cluster.local"
          }
          env {
            name  = "DATADOG_STATSD_PORT"
            value = "8125"
          }

          volume_mount {
            name       = "dd-java-agent"
            mount_path = "/dd"
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
              path = "/api/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 100
            period_seconds        = 5
            timeout_seconds       = 2
            failure_threshold     = 5
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
