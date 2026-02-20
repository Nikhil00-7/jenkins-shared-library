// vars/sendEmail.groovy
def call(String email, String buildStatus = 'SUCCESS') {
    def subject = "${buildStatus}: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
    def body = """
Build Status: ${buildStatus}
Job Name: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Build URL: ${env.BUILD_URL}
"""
    emailext(
        to: email,
        subject: subject,
        body: body
    )
}