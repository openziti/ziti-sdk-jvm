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
                    tool: GRADLE_TOOL, // Tool name from Jenkins configuration
                    rootDir: ".",
                    buildFile: 'build.gradle',
                    tasks: 'clean build',
                    deployerId: "GRADLE_DEPLOYER",
                    resolverId: "GRADLE_RESOLVER"
                )
            }
        }
    }
}