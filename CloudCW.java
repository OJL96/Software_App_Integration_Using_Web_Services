
package cloudcw;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;




 class CloudCW {

   
    public static void main(String[] args) {
        
        File xmlFile = new File("shares.xml"); //used later for marshal to XML file 
        
        //allocate object for storage of first element
        shareBind.Shares allShares = new shareBind.Shares();
        shareBind.Share Shares;
        shareBind.SharePrice Price;
        
        
        
        List<shareBind.Share> totalShares = allShares.getShare(); //list to store shares
        
        List<String> companyNameList = new ArrayList<>();
        companyNameList.add("MICROSOFT");
        companyNameList.add("INTEL");
        companyNameList.add("MCDONALDS");
        companyNameList.add("GOOGLE");
        companyNameList.add("AMAZON");
        companyNameList.add("APPLE");
        
        List<String> symbolList = new ArrayList<>();
        symbolList.add("MSFT");
        symbolList.add("INTC");
        symbolList.add("MCD");
        symbolList.add("GOOG");
        symbolList.add("AMZN");
        symbolList.add("AAPL");
        
        List<Integer> sharesList = new ArrayList<>();
        sharesList.add(5470);
        sharesList.add(7699);
        sharesList.add(9087);
        sharesList.add(12127);
        sharesList.add(300453);
        sharesList.add(222456);
    
        List<Double> valueList = new ArrayList<>();
        valueList.add(255.84);
        valueList.add(388.24);
        valueList.add(643.75);
        valueList.add(785.75);
        valueList.add(235.22);
        valueList.add(69.22);
        
        List<String> currenyList = new ArrayList<>();
        currenyList.add("GBP");
        currenyList.add("GBP");
        currenyList.add("GBP");
        currenyList.add("GBP");
        currenyList.add("GBP");
        currenyList.add("GBP");

       
        for (int x = 0; x < companyNameList.size(); x++) {
            Shares = new shareBind.Share();
            Price = new shareBind.SharePrice();
            
            Shares.setCompanyName(companyNameList.get(x));
            Shares.setCompanySymbol(symbolList.get(x));
            Shares.setAvailableShares(sharesList.get(x));
            Price.setCurrency(currenyList.get(x));
            Price.setValue(valueList.get(x));
            
            try {
                Date date = new Date();
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(date);
                XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                Price.setLastUpdate(xmlDate);
 
             } catch(Exception e){
                  e.printStackTrace();
             }
            
            Shares.getSharePrice().add(Price); //add share price to list
            totalShares.add(Shares); //add share elements to list
            
        }

        //generated marshal code for writing to XML file using "jaxbm"
        try {            
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(allShares.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(allShares, xmlFile);
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
        
        
        
      
       
  
    }
        
      
    
}
