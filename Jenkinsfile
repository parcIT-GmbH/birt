@Library('okuPipelineLib@master') _

def podYaml = """apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    env:
    - name: JAVA_OPTS
      value: '-Xmx256m'
    - name: MAVEN_OPTS
      value: '-Xmx6000m'
    resources:
      limits:
        cpu: '4'
        memory: 7300Mi
      requests:
        cpu: '2.5'
        memory: 7300Mi
"""

pitBuildWithNotification(noNode: true, podYaml: podYaml) {

	stage('checkout') {
		checkout scm
	}
	
	stage('build') {
		def javaHome = sh(returnStdout: true, script: 'echo $JAVA17_HOME').trim()
		withEnv(["PATH+JAVA=$javaHome/bin", "JAVA_HOME=$javaHome"]) {
			sh "mvn install -DskipTests -B"
		}
		
		archiveArtifacts artifacts: 'core/org.eclipse.birt.core/target/*.jar, engine/org.eclipse.birt.report.engine/target/*.jar'
	}
}
