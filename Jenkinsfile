pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Build da Vinheria'
                echo 'Deploy da Vinheria'
            }
        }

        stage('Publicacao') {
            steps {
                sh '''
                    mkdir -p publicacao
                    echo "Aplicacao publicada com sucesso" > publicacao/deploy.txt
                '''
            }
        }
    }
}