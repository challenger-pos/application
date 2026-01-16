resource "kubernetes_horizontal_pod_autoscaler" "challengeone_hpa" {
	metadata {
		name      = "challengeone-hpa"
		namespace = kubernetes_namespace.challengeone.metadata[0].name
		labels = {
			app = "challengeone"
		}
	}

	spec {
		min_replicas = 2
		max_replicas = 4

		scale_target_ref {
			api_version = "apps/v1"
			kind        = "Deployment"
			name        = kubernetes_deployment.challengeone_app.metadata[0].name
		}

		target_cpu_utilization_percentage = 70
	}
}

