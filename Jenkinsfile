pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        // Path to Docker Desktop on Windows
        PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
        // id for Docker Hub credentials in Jenkins (you need to create this credential in Jenkins)
        DOCKERHUB_CREDENTIALS_ID = 'ca3e514b-32b9-4ac2-ab59-c733071649c5'

        //account name and repo name on Docker Hub
        DOCKERHUB_REPO = 'gregtish/edujournal'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'grigorii_sprint_3_prep', url: 'https://github.com/GrigoriiTishchuk/SWE-Project.git'
            }
        }

        stage('Run Tests & Build') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Generate Coverage Report') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withEnv(['PATH+DOCKER=C:\\Program Files\\Docker\\Docker\\resources\\bin']) {
                                bat 'docker build -t edujournal-frontend:latest .'
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    // authorisation and sending container to Docker Hub
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }

    }
}