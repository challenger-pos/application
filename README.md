# 📦 application — Deploy on Kubernetes (AWS)

Este repositório contém uma aplicação Spring Boot e a infraestrutura necessária para executar a aplicação em Kubernetes (manifests via Terraform). O objetivo deste README é orientar passo a passo como preparar, buildar, publicar a imagem Docker e provisionar a aplicação no cluster Kubernetes (por exemplo, EKS na AWS).

---

## 🚀 Visão geral

- Aplicação: Spring Boot (diretório `application/`)
- Dockerfile + `docker-compose.yml` para desenvolvimento local
- Infraestrutura Kubernetes (namespace, secrets, StatefulSet para Postgres, Deployment do app, Service NodePort) em `application/infra/` (usando o provedor Kubernetes do Terraform)
- Manifests Kubernetes também disponíveis em `k8s/`
- CI: `.github/workflows/ci.yml` — build Maven, push de imagem e `terraform apply` da pasta `infra/`

---

## 🧰 Pré-requisitos

- Docker
- Maven 3.6+
- kubectl (configurado com o cluster alvo)
- AWS CLI (se usar EKS/ECR)
- eksctl (opçional, para criar EKS rapidamente)
- Terraform (usado pelo pipeline para aplicar `application/infra`)
- Conta AWS com permissões apropriadas (ECR/EKS/EC2/VPC/IAM)

---

## 🔁 Fluxos suportados

1. Desenvolvimento local com `docker-compose`
2. Deploy local/cluster manual: build image -> push para um registry -> `terraform apply` em `application/infra/` (garante que o Kubernetes provider aplique os recursos)
3. CI/CD: workflow em `.github/workflows/ci.yml` automatiza build, push e `terraform apply`

---

## 🔧 1) Desenvolvimento local

- Rodar DB + app localmente com Docker Compose:

```bash
docker compose up --build
# app: http://localhost:8080
# db: localhost:5433 (POSTGRES_USER=postgres, POSTGRES_PASSWORD=123)
```

- Alternativa: rodar com Maven:

```bash
mvn -B spring-boot:run
```

---

## 🐳 2) Build e publicar imagem Docker

1. Buildar o jar (uber-jar já gerado pelo `maven-shade-plugin`):

```bash
mvn -B clean package -DskipTests
# artefato: application/target/<...>.jar
```

2. Build da imagem Docker (exemplo Docker Hub):

```bash
docker build -t <DOCKERHUB_USERNAME>/challengeone:1 .
docker push <DOCKERHUB_USERNAME>/challengeone:1
```

3. (Opcional) Usar Amazon ECR:

```bash
aws ecr create-repository --repository-name challengeone || true
aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin <aws_account_id>.dkr.ecr.us-east-2.amazonaws.com
docker tag <LOCAL_IMAGE> <aws_account_id>.dkr.ecr.us-east-2.amazonaws.com/challengeone:1
docker push <aws_account_id>.dkr.ecr.us-east-2.amazonaws.com/challengeone:1
```

> Dica: mantenha versões/tags imutáveis (1.0.0, semver) para facilidade de rollback.

---

## ☸️ 3) Preparar o cluster Kubernetes (EKS)

- Se ainda não tem um cluster, crie um com `eksctl` (exemplo mínimo):

```bash
eksctl create cluster --name challengeone-cluster --region us-east-2 --nodes 2 --node-type t3.medium
```

- Atualize `kubeconfig` (se necessário):

```bash
aws eks --region us-east-2 update-kubeconfig --name challengeone-cluster
kubectl get nodes
```

- Certifique-se de que `kubectl` aponta para o contexto correto antes de usar o Terraform Kubernetes provider.

---

## ⚙️ 4) Provisionar a aplicação no cluster (Terraform)

> Observação: os recursos Kubernetes do projeto estão em `application/infra/` e o provedor Kubernetes está configurado para ler `~/.kube/config`.

1. Ajuste a imagem usada pelo deployment:

   - Editar `application/infra/deployment_app.tf` e atualizar `image = "<registry>/challengeone:<tag>"` para o artefato que você pushou.
   - Alternativamente, use `kubectl set image` após o primeiro deploy, mas preferimos manter o código Terraform como fonte de verdade (edit + `terraform apply`).

2. Inicializar/Aplicar:

```bash
cd application/infra
terraform init
terraform apply -auto-approve
```

3. Verificar os recursos:

```bash
kubectl get ns
kubectl get all -n challengeone
kubectl describe pod -n challengeone <pod-name>
```

4. Acessar o serviço:
   - O Service está configurado como NodePort (porta 30080). Para clusters gerenciados, verifique o IP do Node ou use `kubectl port-forward`.

```bash
# obter ip de um node (ex.: EKS) e acessar http://<NODE_IP>:30080
kubectl get nodes -o wide

# ou port-forward localmente
kubectl port-forward svc/challengeone-service 8080:8080 -n challengeone
curl http://localhost:8080/actuator/health
```

---

## 🔁 5) Atualizando a imagem em produção

- Editar `application/infra/deployment_app.tf` com a nova tag e rodar `terraform apply` (mantém a infra declarativa):

```bash
# editar o campo `image = "...:2"` e depois
cd application/infra
terraform plan
terraform apply -auto-approve
```

- Alternativa rápida (não recomendada para infra declarativa):

```bash
kubectl -n challengeone set image deployment/challengeone challengeone=<registry>/challengeone:2
```

---

## 🔁 6) CI/CD

- O workflow `.github/workflows/ci.yml` já faz:

  - Build + Test (Maven)
  - Build e push de imagem (Docker Hub)
  - `terraform init` + `terraform apply` em `infra/`

- Para usar na sua org: configure os secrets `DOCKERHUB_USERNAME` e `DOCKERHUB_TOKEN` (ou configure ECR auth) e ajuste `IMAGE_TAG` conforme necessário.

---

## 🛠️ Monitoramento e troubleshooting

- Logs: `kubectl logs -n challengeone <pod>` ou CloudWatch se estiver usando EKS com integração de logs
- Verificar eventos do namespace:

```bash
kubectl get events -n challengeone --sort-by='.metadata.creationTimestamp'
```

- Problemas comuns:
  - ImagePullBackOff: confirme que a imagem foi corretamente publicada e as credenciais de pull (imagePullSecrets) se necessário.
  - PersistentVolume: `deployment_db.tf` usa `local-path` StorageClass; confirme se seu cluster tem `local-path` (Minikube/k3s) ou ajuste para `gp2`/EBS se EKS.
  - Service inacessível por NodePort: verifique regras do Security Group / firewall.

---

## 🔐 Segurança e boas práticas

- Não commit secrets; use `kubernetes_secret` (já presente) ou soluções como AWS Secrets Manager + External Secrets
- Use remote state com locking (Terraform) para ambientes compartilhados
- Tenha policies IAM mínimas para runners de CI/CD

---

## ✅ Checklist antes de merge para `main`

1. `mvn -B clean package` passou
2. Imagem construída, testada e publicada no registry
3. `terraform plan` revisado e aprovado
4. Secrets e permissões configurados corretamente

---

Se quiser, posso:

- adicionar um **script de bootstrap** (por exemplo `scripts/bootstrap-eks.sh`) que cria um cluster EKS mínimo, configura kubeconfig e prepara o ambiente, ou
- adicionar um **workflow** de deploy com Canary / Blue-Green usando GitHub Actions and `kubectl`/`argocd`.

Responda com **"bootstrap"**, **"workflow"**, ou **"ambos"** que eu já crio os arquivos. Boa revisão!
