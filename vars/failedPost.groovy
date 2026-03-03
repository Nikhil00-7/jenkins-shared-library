def call(Map config = [:]) {

    String email = config.email ?: "kujurnikhil0007@gmail.com"
    String buildStatus = config.buildStatus ?: currentBuild.currentResult ?: "FAILED"

    def subject = "${buildStatus}: ${env.JOB_NAME} #${env.BUILD_NUMBER}"

    def body = """
Build Status : ${buildStatus}
Job Name     : ${env.JOB_NAME}
Build Number : ${env.BUILD_NUMBER}
Build URL    : ${env.BUILD_URL}
"""

    emailext(
        to: email,
        subject: subject,
        body: body
    )
}