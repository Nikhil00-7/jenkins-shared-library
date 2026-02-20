def call (String k8s-cred= "k8s-master-ssh" , String k8s-ip= "192.168.2.20" ){
    
    echo "copying files in k8s-VM"
    
    sshagent(["${k8s-cred}"]){
        sh ''' 
        scp -o StrictHostKeyChecking=no jenkinsOne.yaml ubuntu@${k8sIp}:/home/ubuntu/
            scp -o StrictHostKeyChecking=no service.yaml ubuntu@${k8sIp}:/home/ubuntu/

            ssh -o StrictHostKeyChecking=no ubuntu@${k8sIp} "kubectl apply -f /home/ubuntu/jenkinsOne.yaml"
            ssh -o StrictHostKeyChecking=no ubuntu@${k8sIp} "kubectl apply -f /home/ubuntu/service.yaml"
        '''
    }
}