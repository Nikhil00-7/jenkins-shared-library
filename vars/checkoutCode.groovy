def call(Map config=[:]){
    String branch = confg.get('branch' , 'main') 
    Strign url  = config.get('url') 
      if(!url){
        error  "Git URL is required"
      }
        
    git branch: branch , url: url 
}