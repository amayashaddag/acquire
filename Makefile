# Spécifiez l'image Docker à utiliser comme base pour les jobs :
# Dans notre cas on utilise la dernière version
image: openjdk:latest

# Définition des étapes du pipeline
stages:
  - build
  - test
  - deploy

# Job de compilation : 
compile:
  stage: build
  script:
    - mkdir -p build
    - javac -d build src/main/java/control/GameController.java

# Job de test
test:
  stage: test
  script:
    - echo "Pas encore de test pour le moment"

# Job de déploiement (exemple : déploiement sur un serveur après les tests réussis)
deploy:
  stage: deploy
  script:
    - echo "Pas encore de déploiement pour le moment"
