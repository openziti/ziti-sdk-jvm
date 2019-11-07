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
                    repo: "ziti-maven",
                )
            }
        }

        stage('Build') {
            steps {
                rtGradleRun (
                    usesPlugin: false,
                    useWrapper: true,
                    switches: "--no-daemon",
                    rootDir: ".",
                    buildFile: 'build.gradle.kts',
                    tasks: 'clean build :ziti:artifactoryPublish',
                    deployerId: "GRADLE_DEPLOYER",
                    resolverId: "GRADLE_RESOLVER"
                )
            }
        }
        stage ('Publish build info') {
            steps {
                rtPublishBuildInfo (
                    serverId: "ziti-uploads"
                )
            }
        }
    }
}