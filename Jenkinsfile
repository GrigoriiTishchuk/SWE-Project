pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {

        PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
        DOCKERHUB_CREDENTIALS_ID = 'ca3e514b-32b9-4ac2-ab59-c733071649c5'
        DOCKERHUB_REPO = 'gregtish/edujournal-frontend'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/GrigoriiTishchuk/SWE-Project.git'
            }
        }

        stage('Create .env') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'edujournal-db',
                                                  usernameVariable: 'DB_USER',
                                                  passwordVariable: 'DB_PASS')]) {
                    bat '''
                        echo DB_HOST=localhost > .env
                        echo DB_PORT=3306 >> .env
                        echo DB_NAME=edujournal >> .env
                        echo DB_USERNAME=%DB_USER% >> .env
                        echo DB_PASSWORD=%DB_PASS% >> .env
                        echo DB_URL=jdbc:mariadb://localhost:3306/edujournal >> .env
                    '''
                }
            }
        }


        stage('Run Tests & Build') {
            steps {
                bat 'mvn clean test -Dtest="!com.edujournal.e2e.**"'
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
                jacoco(exclusionPattern: '**/view/**/*.class')
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withEnv(['PATH+DOCKER=C:\\Program Files\\Docker\\Docker\\resources\\bin']) {
                        bat "docker build -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} ."
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('', DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }

    }
}