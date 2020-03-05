pipeline {
    agent {
        docker {
           image 'uber/android-build-environment:latest'
           args '-u root:root'
        }
    }

    stages {
        stage('Setup') {
            steps {
                sh 'git tag --delete $(git tag -l)'
                sh 'git fetch --verbose --tags'
                sh 'mkdir -p ${ANDROID_HOME}/licenses'
                sh 'cp -n etc/android-sdk*-license ${ANDROID_HOME}/licenses'
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

        stage('Tagging') {
          when { branch 'master'}
          steps {
            script {
               def zitiVer = readFile('version').trim()
               def (zitiMajor, zitiMinor, zitiPatch) = zitiVer.split(/\./).collect{ it.toInteger() }

               echo "${zitiMajor}.${zitiMinor}.${zitiPatch}"

               def tagVer = sh(returnStdout: true, script: 'git describe --long')
               def tagVerSplit = tagVer.split(/[\.-]/)
               def (tagMajor, tagMinor, tagPatch, ahead) = [
                   tagVerSplit[0].toInteger(),
                   tagVerSplit[1].toInteger(),
                   tagVerSplit[2].toInteger(),
                   tagVerSplit[3].toInteger()
               ]

               if ( zitiMajor > tagMajor ||
                    (zitiMajor == tagMajor && zitiMinor > tagMinor ) ||
                    (zitiMinor == tagMajor && zitiMinor == tagMinor && zitiPatch > tagPatch) )
               {
                    new_tag = zitiVer
                    echo "advancing tag($new_tag) based on 'version' file"
                    sh "git tag -a ${new_tag} -m \'CI tag ${new_tag} \'"
               } else {
                   if (ahead == "0") {
                        echo "already has tag = ${tagMajor}.${tagMinor}.${tagPatch}"
                        new_tag = "${tagMajor}.${tagMinor}.${tagPatch}"
                   }
                   else {
                        echo "bumping up new tag"
                        new_tag = "${tagMajor}.${tagMinor}.${tagPatch + 1}"
                        echo "setting new tag = $new_tag"
                        sh "git tag -a ${new_tag} -m \'CI tag ${new_tag} \'"
                   }
               }
            }
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

        stage('git push tag') {
          when { branch 'master' }
          steps {
            echo "pushing $new_tag to ${env.GIT_URL}"
            withCredentials(
              [usernamePassword(credentialsId: 'github',
                                usernameVariable: 'GIT_USER',
                                passwordVariable: 'GIT_PASS')
                                ]) {

                        script {
                            def u = URLEncoder.encode(GIT_USER, "UTF-8")
                            def p = URLEncoder.encode(GIT_PASS, "UTF-8")
                            def git_url = "${env.GIT_URL}".replace("https://", "")
                            sh "git push https://${u}:${p}@${git_url} ${new_tag}"
                        }
                    }
          }
        }
    }
}