# resource "kubernetes_ingress_v1" "challengeone_alb" {
# 	metadata {
# 		name      = "challengeone-ingress"
# 		namespace = var.challengeone_namespace_name
# 		annotations = {
# 			"kubernetes.io/ingress.class"                = "alb"
# 			"alb.ingress.kubernetes.io/scheme"           = "internal"
# 			"alb.ingress.kubernetes.io/target-type"      = "ip"
# 			"alb.ingress.kubernetes.io/security-groups"  = data.terraform_remote_state.infra.outputs.alb_security_group_id
# 			"alb.ingress.kubernetes.io/listen-ports"     = "[{\"HTTP\":80}]"
# 		}
# 	}

# 	spec {
# 		rule {
# 			http {
# 				path {
# 					path     = "/"
# 					path_type = "Prefix"
# 					backend {
# 						service {
# 							name = kubernetes_service.challengeone_app.metadata[0].name
# 							port {
# 								number = 8080
# 							}
# 						}
# 					}
# 				}
# 			}
# 		}
# 	}
# }
