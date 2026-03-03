def call(Map config = [:]) {

    // Configurable options with defaults
    String tfDir = config.tfDir ?: './terraform'       // directory where your Terraform code lives
    String tfVars = config.tfVars ?: ''               // optional -var flags
    boolean autoApprove = config.autoApprove ?: true // whether to auto-approve Terraform apply

    // Initialize Terraform
    echo "Initializing Terraform in ${tfDir}"
    dir(tfDir) {
        sh 'terraform init'
    }

    // Plan Terraform
    echo "Planning Terraform in ${tfDir}"
    dir(tfDir) {
        sh "terraform plan ${tfVars}"
    }

    // Apply Terraform
    echo "Applying Terraform in ${tfDir}"
    dir(tfDir) {
        String applyCmd = "terraform apply"
        if (autoApprove) {
            applyCmd += " -auto-approve"
        }
        if (tfVars) {
            applyCmd += " ${tfVars}"
        }
        sh applyCmd
    }

    // Optional: capture kubeconfig if output exists
    if (config.kubeconfigOutput) {
        dir(tfDir) {
            sh "terraform output -raw ${config.kubeconfigOutput} > kubeconfig"
        }
        echo "Kubeconfig saved to ${tfDir}/kubeconfig"
    }
}