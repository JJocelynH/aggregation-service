pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './mvnw package -DskipTests -B'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t jocelyner/aggregation-service .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh 'docker login -u $USERNAME -p $PASSWORD'
                    sh 'docker push jocelyner/aggregation-service'
                }
            }
        }
    }
}
