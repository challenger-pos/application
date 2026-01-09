resource "kubernetes_secret" "challengeone_db" {
  metadata {
    name      = "challengeone-db-secret"
    namespace = kubernetes_namespace.challengeone.metadata[0].name
  }

  data = {
    POSTGRES_USER                = var.db_username
    POSTGRES_PASSWORD            = var.db_password
    POSTGRES_DB                  = var.db_name
    SPRING_DATASOURCE_URL        = "jdbc:postgresql://${var.db_host}:${var.db_port}/${var.db_name}?currentSchema=${var.db_schema}"
    SPRING_DATASOURCE_USERNAME   = var.db_username
    SPRING_DATASOURCE_PASSWORD   = var.db_password
  }

  type = "Opaque"
}