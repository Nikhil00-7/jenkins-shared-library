def  call (Map  config= [:]){
    List images  = config.images ?: [
        [imageName: 'docdon0007/app-v1:04' , creds: 'docker-loginhub']
            ]

    images.each { img ->
    Sring imageName  = img.imageName 
    Sring creds = img.creds ?:"docker-loginhub"

    echo "Building image"
    sh "docker build -t '${imageName}'"
    
    withCredentials([usernamePassword(credentialsId: "${creds}", usernameVariable: "USER", passwordVariable: "PASS")]) {
            sh "echo \$PASS | docker login -u \$USER --password-stdin"
            sh "docker push ${imageName}"
        }
    }
}
