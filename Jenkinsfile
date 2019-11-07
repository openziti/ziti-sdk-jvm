pipeline {
    agent any

    stages {
        stage('Setup') {
            steps {
                rtGradleResolver (
                    id: "GRADLE_RESOLVER",
                    serverId: "ziti-uploads",
                    repo: "jcenter"
                )

                rtGradleDeployer (
                    id: "GRADLE_DEPLOYER",
                    serverId: "ziti-uploads",
                    repo: "libs-release-local",
                )
            }
        }

        stage('Build') {
            steps {
                rtGradleRun (
                    useWrapper: true,
                    rootDir: ".",
                    buildFile: 'build.gradle.kts',
                    tasks: 'clean build',
                    deployerId: "GRADLE_DEPLOYER",
                    resolverId: "GRADLE_RESOLVER"
                )
            }
        }
    }
}