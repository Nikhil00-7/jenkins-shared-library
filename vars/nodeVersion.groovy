def call(String  nodeVersion= "node22"){
    echo "using node version is ${nodeVersion}"

    pipeline {

        angent any 

        tools {
            nodejs "${nodeVersion}"
        }

        stages{
            stage("Version"){
             echo "This pipeline is running on Version ${nodeVersion}"
            }
        }
    }
}