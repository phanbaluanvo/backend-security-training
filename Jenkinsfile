pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "vophanbaluan/agent_phisher:0.0.1"
        DOCKER_CREDENTIALS = "docker-credential"
        EC2_CREDENTIALS = "ec2-ssh-key"
        EC2_HOST = "3.99.136.55"
    }

    stages {
        stage('Checkout Source') {
            steps {
                git credentialsId: 'github-credential', url: 'https://github.com/phanbaluanvo/backend-security-training.git', branch: 'main'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t $DOCKER_IMAGE ."
            }
        }

        stage('Login to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                sh "docker push $DOCKER_IMAGE"
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent([EC2_CREDENTIALS]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ec2-user@$EC2_HOST << EOF
                        docker stop agent_phisher || true
                        docker rm -f agent_phisher || true
                        docker pull $DOCKER_IMAGE
                        docker run -d --name agent_phisher --env-file /home/ec2-user/.env -p 8081:8081 $DOCKER_IMAGE
                        EOF
                    """
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline execution completed."
        }
    }
}
