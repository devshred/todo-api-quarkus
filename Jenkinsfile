parameters {
    string(name: 'namespace', description: 'Ziel Project zum Deployen')
}

pipeline {

    agent {
        node {
            label 'maven'
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
                            openshift.apply(readFile("build/imagestream.yaml"))
                            openshift.apply(readFile("build/buildconfig.yaml"))
                        }
                    }
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build image') {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject(params.namespace) {
                            def bc = openshift.selector("buildconfig", "todo-api-quarkus")
                            def build = bc.startBuild("--from-dir=.", "--wait")

                            build.logs('-f')
                        }
                    }
                }
            }
        }
    }

}