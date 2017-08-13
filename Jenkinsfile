pipeline {
  agent any
  stages {
    stage('Build') {
      when {
        not {
          branch 'master'
        }
        
      }
      steps {
        echo 'Building..'
      }
    }
    stage('Test') {
      when {
        environment name: 'ENV', value: 'dev'
      }
      steps {
        echo 'Testing..'
      }
    }
    stage('Deploy') {
      when {
        environment name: 'ENV', value: 'prod'
      }
      steps {
        echo 'Deploying....'
      }
    }
  }
  environment {
    ENV = 'dev'
  }
}