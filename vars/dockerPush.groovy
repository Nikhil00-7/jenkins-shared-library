def call (String image , String creds=""){
    sh "docker build -t ${image} ."
    withCredentials([usernamePassword(credentialsId: creds , usernameVariable: "USER" , passwordVariable: "PASS")]){
        sh 'echo  $PASS | docker login -u $USER --password-stdin'
        sh "docker push ${image}"
    }
}