parameters {
    string(name: 'namespace', description: 'Ziel Project zum Deployen')
}

pipeline {

    agent {
        node {
            label 'php'
        }
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout scm
                }
            }
        }

        stage('Prepare Environment') {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject(params.namespace) {
                            openshift.apply(readFile("php/build/imagestream.yaml"))
                            openshift.apply(readFile("php/build/buildconfig.yaml"))
                        }
                    }
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests -Pnative'
                }
            }
        }

        stage('Build image') {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject(params.namespace) {
                            def bc = openshift.selector("buildconfig", "php-sample-app")
                            def build = bc.startBuild("--from-dir=php", "--wait")

                            build.logs('-f')
                        }
                    }
                }
            }
        }
    }

}