pipeline {
    agent any

    environment {
        IMAGE_NAME = 'eventapp'
        CONTAINER_NAME = 'eventapp-container'
    }

    stages {

        stage('Clean & Build JAR') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17'
                    args '-v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Run App & MongoDB using Docker Compose') {
            steps {
                // Remove any existing containers
                sh '''
                    docker-compose down || true
                    docker-compose up -d
                '''
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
