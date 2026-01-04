resource "kubernetes_namespace" "challengeone" {
  metadata {
    name = "app-challengeone-${var.environment}"
  }
}
