
package org.me.shareBroker;






import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import shareBind.Share;
import shareBind.Shares;
import shareBind.SharePrice;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;









/**
 *
 * @author n0745509
 */
@WebService(serviceName = "shareBrokerWS")
public class shareBrokerWS {

    @WebMethod(operationName = "getShares")
    public List<Share> getShares() {
        Shares getShare = unmarshelShares();
        List<Share> listOfShares = getShare.getShare();
        
        
        Share nextShare;
        Shares share = new Shares();
        List<Share> outputList = share.getShare();
     
        
        for (int x = 0; x < listOfShares.size(); x++) {
             nextShare = listOfShares.get(x);
             if (nextShare.getAvailableShares() > 0) {
               outputList.add(nextShare);
                
            }
        }
        
        
 
       
        return outputList;
    }
      /**
     * Web service operation
     * 
     * @return
     */

    @WebMethod(operationName = "unmarshelShares")
    public Shares unmarshelShares() {
        Shares singleShare = new Shares();
        File file = new File("C:\\Users\\n0745509\\Downloads\\cloudCW\\cloudCW\\shares.xml");
        
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(singleShare.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            singleShare = (Shares) unmarshaller.unmarshal(file); //NOI18N
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
        
        return singleShare;
    }

    /**
     * Web service operation
     * @param offer
     * @param companyName
     * @return 
     */
    @WebMethod(operationName = "purchaseShares")
    public List<Share> purchaseShares(int offer, String companyName) {
        File file = new File("C:\\Users\\n0745509\\Downloads\\cloudCW\\cloudCW\\shares.xml");
       
        Shares shares = unmarshelShares();
        List<Share> listOfShares = shares.getShare();
        // basic linear search algorithm  
        for (int x = 0; x < listOfShares.size(); x++){
            if (listOfShares.get(x).getCompanyName().equals(companyName.toUpperCase())) {
                if (offer <= listOfShares.get(x).getAvailableShares()){
                    listOfShares.get(x).setAvailableShares(listOfShares.get(x).getAvailableShares() - offer);
                    
                    List<SharePrice> price = listOfShares.get(x).getSharePrice();
                    for (int y = 0; y < price.size(); y++) {
                        try {
                            Date date = new Date();
                            GregorianCalendar c = new GregorianCalendar();
                            c.setTime(date);
                            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                            price.get(y).setLastUpdate(xmlDate);
                        } catch(Exception e){
                            e.printStackTrace(); 
                        }
                    } 
                }
                else {
                    //## NEED TO FIND WAY TO THROW ERROR MESSAGE ##
                    //throw new IllegalArgumentException("Error! You tried to buy " + offer + "but only" + listOfShares.get(x).getAvailableShares());
   
                }
            
           }
        }
        

        try {            
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(shares.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(shares, file);
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }

        return listOfShares;
    }

    /**
     * Web service operation
     * @param nameOrSymbol
     * @return 
     */
    @WebMethod(operationName = "searchNameOrPriceRange")
    public List<Share> searchNameOrPriceRange(String nameOrSymbol) {
        String searchParam = nameOrSymbol.toUpperCase();
        Shares shares = unmarshelShares();
        List<Share> listOfShares = shares.getShare();
       
        Shares newShare = new Shares();
        List<Share> validList = newShare.getShare();
  
        boolean doubleInputCheck, stringInputcheck;
        doubleInputCheck = searchParam.matches("^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,][0-9]+$"); //double regular expression
        stringInputcheck = searchParam.matches("[a-zA-Z_]+"); // string regular expression
        
     
        double temp = 0;
        double temp2 = listOfShares.get(0).getSharePrice().get(0).getValue();

        
        for (int x = 0; x < listOfShares.size(); x++) {
            
            if (doubleInputCheck) {
                
                List<SharePrice> price = listOfShares.get(x).getSharePrice();
                for (int i = 0; i < price.size(); i++) {
                   
                    if (Double.parseDouble(searchParam) > price.get(i).getValue()) {
                        validList.add(listOfShares.get(x)); 
                    }
                }
            }
            else if (searchParam.equals("MAX")) {
                List<SharePrice> price = listOfShares.get(x).getSharePrice();
             
                for (int z = 0; z < price.size(); z++) {
                    
                    if (price.get(z).getValue() > temp) {
                        temp = price.get(z).getValue();
                        validList.clear();
                        validList.add(listOfShares.get(x));
                       
                    }
                }
                
            }
            
            else if (searchParam.equals("MIN")) {
                    List<SharePrice> price = listOfShares.get(x).getSharePrice();
                    
                    for (int z = 0; z < price.size(); z++) {
                    
                    if (price.get(z).getValue() < temp2) {
                        temp2 = price.get(z).getValue();
                        validList.clear();
                        validList.add(listOfShares.get(x));
                       
                    }
                }
            }
           
                
            else if (stringInputcheck)  {
                
                 if (listOfShares.get(x).getCompanyName().equals(searchParam) || listOfShares.get(x).getCompanySymbol().equals(searchParam) ) {
                     
                     validList.add(listOfShares.get(x)); 
                 }
            }
         
        }
        
       

        return validList;
        
        
        
    }
     /**
     * Web service operation
   
     * @return 
     * @throws java.net.MalformedURLException 
     */
    @WebMethod(operationName = "getSharesOnlineAPI")
    public List<Share> getSharesOnlineAPI() {
        
        Shares shares = unmarshelShares();
        List<Share> listOfShares = shares.getShare();
        List<SharePrice> price;
        
        File xmlFile = null;
      
        try {

            for (int x = 0; x < listOfShares.size(); x++){
                
               URL url = new URL("https://financialmodelingprep.com/api/v3/stock/real-time-price/"+ listOfShares.get(x).getCompanySymbol());
               xmlFile = new File("C:\\Users\\n0745509\\Downloads\\cloudCW\\cloudCW\\shares.xml");
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               
                if (conn.getResponseCode() != 200) {
                    throw new IOException(conn.getResponseMessage());
                }

                BufferedReader ins = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inString;
                StringBuilder sb = new StringBuilder();
                
                while ((inString = ins.readLine()) != null) {
                    sb.append(inString);
                }

                String newString = sb.toString();
                String apiPrice = newString.replaceAll("[^0-9\\.]","");

             
                price = listOfShares.get(x).getSharePrice();
                for (int y = 0; y < price.size(); y++){
                    price.get(y).setValue(Double.parseDouble(apiPrice));

                    try {
                        Date date = new Date();
                        GregorianCalendar c = new GregorianCalendar();
                        c.setTime(date);
                        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                        price.get(y).setLastUpdate(xmlDate);
                    } catch(Exception e){
                        e.printStackTrace(); 
                    }
                }

            ins.close();
               
        }
                    
        } catch (MalformedURLException me) {
            System.out.println("MalformedURLException: " + me); 
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe); 
        }

//        try {            
//            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(shares.getClass().getPackage().getName());
//            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
//            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
//            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//            marshaller.marshal(shares, xmlFile);
//        } catch (javax.xml.bind.JAXBException ex) {
//            // XXXTODO Handle exception
//            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
//        }

     return listOfShares;
    }   

    /**
     * Web service operation
     * @param newCompanyName
     * @param newSymbol
     * @param newAvilableShares
     * @param newValue
     * @return 
     */
    @WebMethod(operationName = "addShares")
    public List<Share> addShares(String newCompanyName, String newSymbol, 
            int newAvilableShares, double newValue ) {
        
        File xmlFile = new File("C:\\Users\\n0745509\\Downloads\\cloudCW\\cloudCW\\sharesAPI.xml");
       
        Shares shares = unmarshelShares();
        List<Share> listOfShares = shares.getShare();
        SharePrice addNewPrice = new SharePrice();
        Share addNewShare = new Share();
        
        for (int x = 0; x < listOfShares.size(); x++){
            if (newCompanyName.equals(listOfShares.get(x).getCompanyName())){
                listOfShares.get(x).setAvailableShares(listOfShares.get(x).getAvailableShares() + newAvilableShares);
                     
            try {            
                javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(shares.getClass().getPackage().getName());
                javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
                marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
                marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(shares, xmlFile);
            } catch (javax.xml.bind.JAXBException ex) {
                // XXXTODO Handle exception
                java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
            }

            
                return listOfShares;
            }
        }
        
     
        
        addNewShare.setCompanyName(newCompanyName);
        addNewShare.setCompanySymbol(newSymbol);
        addNewShare.setAvailableShares(newAvilableShares);
        
        addNewPrice.setCurrency("GBP");
        addNewPrice.setValue(newValue);
        
        
        try{
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            addNewPrice.setLastUpdate(xmlDate);
        }catch(Exception e){
            System.out.println(e);
        }
        
        addNewShare.getSharePrice().add(addNewPrice);
        listOfShares.add(addNewShare);
        
        try {            
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(shares.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(shares, xmlFile);
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
        
        
        
        return listOfShares;
    }  
    
   
}  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
 