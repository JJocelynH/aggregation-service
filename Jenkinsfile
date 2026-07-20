pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './mvnw package -DskipTests -B'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test -B'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    sh './mvnw sonar:sonar -B -Dsonar.projectKey=aggregation-service -Dsonar.host.url=http://host.docker.internal:9000 -Dsonar.token=$SONAR_TOKEN'
                }
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
