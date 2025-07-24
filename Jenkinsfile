pipeline {
    agent any

    environment {
        IMAGE_NAME = 'eventapp'
        CONTAINER_NAME = 'eventapp-container'
        MONGO_CONTAINER = 'mongo-container'
        MONGO_PORT = '27017'
        APP_PORT = '8080'
    }

    stages {
        stage('Clean & Build JAR on Host') {
            steps {
				sh'''
				apk add --no-cache openjdk17 maven
                mvn clean package -DskipTest
				'''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Start MongoDB Container') {
            steps {
                sh '''
                    docker rm -f $MONGO_CONTAINER || true
                    docker run -d --name $MONGO_CONTAINER -p $MONGO_PORT:27017 mongo:6.0
                '''
            }
        }

        stage('Start EventApp Container') {
            steps {
                sh '''
                    docker rm -f $CONTAINER_NAME || true
                    docker run -d \
                        --name $CONTAINER_NAME \
                        --link $MONGO_CONTAINER:mongo \
                        -p $APP_PORT:8080 \
                        -e SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/eventdb \
                        $IMAGE_NAME
                '''
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution finished.'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
