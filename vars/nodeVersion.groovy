def call(String  nodeVersion= "node22"){
    echo "using node version is ${nodeVersion}"

    pipeline {

        agent any 

        tools {
            nodejs "${nodeVersion}"
        }

        stages{
            stage("Version"){
                steps{
             echo "This pipeline is running on Version ${nodeVersion}"
            }
            }
        }
    }
}