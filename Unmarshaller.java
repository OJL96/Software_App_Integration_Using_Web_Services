
package cloudcw;


import java.io.File;

import java.util.ArrayList;
import java.util.List;





public class Unmarshaller {
     public static void main(String[] args) {
        Unmarshaller unmarshal = new Unmarshaller();
        unmarshal.getUnmarshalShares(); // calls function 
     
  
         
     }
    public List<shareBind.Share> getUnmarshalShares() {
        shareBind.Shares share = new shareBind.Shares();
        
         
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(share.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            share = (shareBind.Shares) unmarshaller.unmarshal(new File("shares.xml")); //NOI18N
        } catch (javax.xml.bind.JAXBException ex) {
             // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
       
        List<shareBind.Share> shareList = share.getShare();
        
       
        
          
          
        return shareList;
      }
      
    
    
    
    
    
}
