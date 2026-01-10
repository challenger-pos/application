resource "kubernetes_secret" "challengeone_secret" {
  metadata {
    name      = "challengeone-db-secret"
    namespace = kubernetes_namespace.challengeone.metadata[0].name
  }

  data = {
    POSTGRES_USER                = var.db_username
    POSTGRES_PASSWORD            = var.db_password
    SPRING_DATASOURCE_USERNAME   = var.db_username
    SPRING_DATASOURCE_PASSWORD   = var.db_password
  }

  type = "Opaque"
}