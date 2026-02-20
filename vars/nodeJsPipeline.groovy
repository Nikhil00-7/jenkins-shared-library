def call(Map config = [:]) {

    pipeline {

        agent any

        tools {
            nodejs config.nodeVersion ?: 'node22'
        }

        environment {
            DOCKER_USER  = config.dockerUser ?: "docdon0007"
            IMAGE        = config.image ?: "jenkins"
            TAG          = config.tag ?: "01"
            DOCKER_IMAGE = "${DOCKER_USER}/${IMAGE}:${TAG}"
        }

        stages {

            stage('Checkout code') {
                steps {
                    git branch: config.branch ?: 'main', url: config.repoUrl
                }
            }

            stage("Install Dependency") {
                steps {
                    sh 'npm install'
                }
            }

            stage("Run tests") {
                steps {
                    sh 'npm test'
                }
            }

            stage("Build") {
                steps {
                    sh 'npm run build || echo "No build script"'
                }
            }

            stage("Docker login & push") {
                steps {
                    sh 'docker build -t $DOCKER_IMAGE .'
                    withCredentials([usernamePassword(
                        credentialsId: config.dockerCreds ?: 'dockerhub-login',
                        usernameVariable: 'USER',
                        passwordVariable: 'PASS'
                    )]) {
                        sh 'echo $PASS | docker login -u $USER --password-stdin'
                        sh 'docker push $DOCKER_IMAGE'
                    }
                }
            }

            stage("Copy files to k8s") {
                steps {
                    sshagent([config.k8sCreds ?: 'k8s-master-ssh']) {
                        sh """
                          scp -o StrictHostKeyChecking=no jenkinsOne.yaml ubuntu@${config.k8sIp}:/home/ubuntu/
                          scp -o StrictHostKeyChecking=no service.yaml ubuntu@${config.k8sIp}:/home/ubuntu/
                        """
                    }
                }
            }

            stage("Deploy to k8s") {
                steps {
                    sshagent([config.k8sCreds ?: 'k8s-master-ssh']) {
                        sh """
                          ssh -o StrictHostKeyChecking=no ubuntu@${config.k8sIp} "kubectl apply -f /home/ubuntu/jenkinsOne.yaml"
                          ssh -o StrictHostKeyChecking=no ubuntu@${config.k8sIp} "kubectl apply -f /home/ubuntu/service.yaml"
                        """
                    }
                }
            }
        }

        post {
            success {
                emailext(
                    to: config.email ?: "kujurnikhil0007@gmail.com",
                    subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
Build Successful!

Job Name: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Build URL: ${env.BUILD_URL}
"""
                )
            }
            failure {
                emailext(
                    to: config.email ?: "kujurnikhil0007@gmail.com",
                    subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
Build Failed!

Job Name: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Build URL: ${env.BUILD_URL}
"""
                )
            }
        }
    }
}