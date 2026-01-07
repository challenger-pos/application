resource "kubernetes_secret" "challengeone_db_secret" {
  metadata {
    name      = "challengeone-db-secret"
    namespace = var.challengeone_namespace_name
  }

  # data = {
  #   POSTGRES_USER     = "postgres"
  #   POSTGRES_PASSWORD = "123"
  #   POSTGRES_DB       = "challengeone"
  #   SPRING_DATASOURCE_URL = "jdbc:postgresql://challengeone-db:5432/challengeone"
  #   SPRING_DATASOURCE_USERNAME = "postgres"
  #   SPRING_DATASOURCE_PASSWORD = "123"
  # }
  data = {
    POSTGRES_USER     = "postgres"
    POSTGRES_PASSWORD = "postgres123"
    POSTGRES_DB       = "challengeone"
    SPRING_DATASOURCE_URL = "jdbc:postgresql://${var.db_endpoint}/challengeone"
    SPRING_DATASOURCE_USERNAME = "postgres"
    SPRING_DATASOURCE_PASSWORD = "postgres123"
  }

  type = "Opaque"
}
