def call(Map config = [:]) {
    String dir = config.dir ?: '.'       
    String options = config.options ?: '' 

    echo "Installing dependencies in ${dir}"
    dir(dir) {
        sh "npm install ${options}"
    }
}