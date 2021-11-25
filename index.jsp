<%-- 
    Document   : index
    Created on : 17-Jan-2020, 19:54:53
    Author     : n0745509
--%>



<%@page import="java.text.DecimalFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
    <div class="w3-container w3-blue">
     <h4>Select Currency</h4>
    </div>    
        
        
        <form method="get">
            <select name="currencySelector" onchange="this.form.submit();">
                
                    
                    
               

    <%
        
    try {
	docwebservices.CurrencyConversionWSService service = new docwebservices.CurrencyConversionWSService();
	docwebservices.CurrencyConversionWS port = service.getCurrencyConversionWSPort();
	// TODO process result here
	java.util.List<java.lang.String> result = port.getCurrencyCodes();
        
        for (int x = 0; x < result.size(); x++){
            
            String getCurrencyIntial = result.get(x);      
            // REFERENCE: https://www.w3schools.com/html/tryit.asp?filename=tryhtml_elem_select
  
            out.println("<option id='" + getCurrencyIntial.substring(0, 3) + "'value='"
                    + getCurrencyIntial.substring(0, 3)
                    + "'>" + getCurrencyIntial + "</option>");
           
                                          
        }
        
        
	
    } catch (Exception ex) {
	// TODO handle custom exceptions here
    }
    

    %>
            
        
    
           
        </select>
    </form>

    <%
    double resultOfConversion= 0.0;
    String currencySelectionSplit = "";
    String defaultCurrency = "GBP";
    if (request.getParameter("currencySelector") != null) {
        String userCurrencySelection = request.getParameter("currencySelector");
        try {
            docwebservices.CurrencyConversionWSService service = new docwebservices.CurrencyConversionWSService();
            docwebservices.CurrencyConversionWS port = service.getCurrencyConversionWSPort();
            currencySelectionSplit = userCurrencySelection.substring(0, 3);
         
            
            // TODO process result here
            resultOfConversion = port.getConversionRate(defaultCurrency,
                    currencySelectionSplit);
           

        } catch (Exception ex) {

        }
    }
    else {
       currencySelectionSplit = "GBP";
       resultOfConversion = 1;
       
            
    } 
        
    
   %>
   
   <div class="w3-container w3-blue">
     <h4>List Available Shares</h4>
</div>
<form  method="post" class="w3-container">
    <input type="submit" name="showSharesButton" value ="Show Shares">
 </form>
   
  
       <table  class="sb-table"> 
   
   <%
    if (request.getParameter("showSharesButton") != null){    

        try {
            org.me.sharebroker.ShareBrokerWS_Service service = new org.me.sharebroker.ShareBrokerWS_Service();
            org.me.sharebroker.ShareBrokerWS port = service.getShareBrokerWSPort();

            java.util.List<org.me.sharebroker.Share> result = port.getShares();
            java.util.List<org.me.sharebroker.SharePrice> price;




            out.println("<th>Symbol</th>"); 
            out.println("<th>Name</th>");
            out.println("<th>Available Shares</th>");
            out.println("<th>Value</th>");
            out.println("<th>Currency</th>");
            out.println("<th>Last Update</th>");
            out.println("<th></th>");


                for (int x = 0; x < result.size(); x++) {
                    out.println("<tr class=\"sb-table-item\">");
                    out.print("<td>" + "<img src='//logo.clearbit.com/"+
                            result.get(x).getCompanyName().toLowerCase()+
                            ".com?size=40'>"  +"</td>");
                    out.print("<td>" +  result.get(x).getCompanyName()+ "</td>");
                    out.print("<td>" + result.get(x).getAvailableShares()+ "</td>");

                    price = result.get(x).getSharePrice();

                    for (int y = 0; y < price.size(); y++) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        String updatedCurrencyValue = df.format(price.get(y).getValue()*resultOfConversion);

                        out.print("<td>"+ updatedCurrencyValue +"</td>");
                        out.print("<td>" + currencySelectionSplit + "</td>");
                        out.print("<td>" + price.get(y).getLastUpdate()+ "</td>");
                        out.println("</tr>");
                    }
                }
        
       
        
        
       
        
    } catch (Exception ex) {
	// TODO handle custom exceptions here
    }
    
    }
  %>
    
   </table>
  
  <div class="w3-container w3-blue">
      <h4> Get Online Shares</h4>
</div>
   <form  method="post" class="w3-container">
    <input type="submit" name="apiButton" value ="Get Shares Online">
 </form>
     
     
       <table  class="sb-table"> 
     
   <%
    
    if (request.getParameter("apiButton") != null ) {
        
        try {
            org.me.sharebroker.ShareBrokerWS_Service service = new org.me.sharebroker.ShareBrokerWS_Service();
            org.me.sharebroker.ShareBrokerWS port = service.getShareBrokerWSPort();
            // TODO process result here
            java.util.List<org.me.sharebroker.Share> apiResult = port.getSharesOnlineAPI();
             java.util.List<org.me.sharebroker.SharePrice> newPrice;
            //out.println("Result = "+result);
            


            out.println("<th>Symbol</th>"); 
            out.println("<th>Name</th>");
            out.println("<th>Available Shares</th>");
            out.println("<th>Value</th>");
            out.println("<th>Currency</th>");
            out.println("<th>Last Update</th>");
            out.println("<th></th>");


                for (int x = 0; x < apiResult.size(); x++) {
                    out.println("<tr class=\"sb-table-item\">");
                    out.print("<td>" + "<img src='//logo.clearbit.com/"+
                            apiResult.get(x).getCompanyName().toLowerCase()+
                            ".com?size=40'>"  +"</td>");
                    out.print("<td>" +  apiResult.get(x).getCompanyName()+ "</td>");
                    out.print("<td>" + apiResult.get(x).getAvailableShares()+ "</td>");

                    newPrice = apiResult.get(x).getSharePrice();

                    for (int y = 0; y < newPrice.size(); y++) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        String updatedCurrencyValue = df.format(newPrice.get(y).getValue());

                        out.print("<td>"+ updatedCurrencyValue +"</td>");
                        out.print("<td>" + "USD" + "</td>");
                        out.print("<td>" + newPrice.get(y).getLastUpdate()+ "</td>");
                        out.println("</tr>");
                    }
                }
        
       
            
            
            
            
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
    
    }
    %>

       </table>
    
    
    
    
    
    
  
    

    
<form  method="post" class="w3-container">
    <input class="w3-input" type="text" name="userSearch" placeholder ="Please Enter Company Name, Symbol, or Price Range">
    <input type="submit" name="searchButton">
 </form> 
   
    <table class="sb-table">
        
    <%
        
      
    if (request.getParameter("searchButton") != null) {
        String userInput = request.getParameter("userSearch");
        
      
        out.println("<th>Symbol</th>"); 
        out.println("<th>Name</th>");
        out.println("<th>Available Shares</th>");
        out.println("<th>Value</th>");
        out.println("<th>Currency</th>");
        out.println("<th>Last Updated</th>");
        out.println("<th></th>");
       
        
        try {
            org.me.sharebroker.ShareBrokerWS_Service service = new org.me.sharebroker.ShareBrokerWS_Service();
            org.me.sharebroker.ShareBrokerWS port = service.getShareBrokerWSPort();
           
            //java.lang.String arg0 = "";
            java.util.List<org.me.sharebroker.SharePrice> price;
            java.util.List<org.me.sharebroker.Share> result = port.searchNameOrPriceRange(userInput);
          
 
            for (int x = 0; x < result.size(); x++) {
                out.println("<tr class=\"sb-table-item\">");
                out.print("<td>" + "<img src='//logo.clearbit.com/"+
                        result.get(x).getCompanyName().toLowerCase()+
                        ".com?size=50'>"  +"</td>");
                out.print("<td>" + result.get(x).getCompanyName()+ "</td>");
                out.print("<td>" + result.get(x).getAvailableShares()+ "</td>");

                price = result.get(x).getSharePrice();

                for (int y = 0; y < price.size(); y++) 
                {
                    DecimalFormat df = new DecimalFormat("#.##");
                    String updatedCurrencyValue = df.format(price.get(y).getValue()*resultOfConversion);
                    
                    out.print("<td>"+ updatedCurrencyValue +"</td>");
                    out.print("<td>" + currencySelectionSplit + "</td>");;
                    
                    
                    out.print("<td>" + price.get(y).getLastUpdate()+ "</td>");
                    out.println("</tr>");
                }
            }
               




        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
    }
    %>
    </table>

    


    
<div class="w3-container w3-blue">

     <h4>Buy Shares</h4>
</div>
     <form  method="post"class="w3-container">
       
        <input class="w3-input" type="text" name="companySearch" placeholder = "Enter Company Name">
        <input class="w3-input" type="number" name="sharesAmount" placeholder = "Enter Shares">
        
        <input type="submit" name="goButton" value="Buy Share">
     </form>
    
    <table class="sb-table">

    <%
    String updatedCurrencyValue = "";
    //int amount = 0;
    if(request.getParameter("goButton") != null) { 
       
        out.println("<th>Name: </th>");
        out.println("<th>Shares Purchased: </th>");
        out.println("<th>GBP Total: </th>");
        out.println("<th>Selected Currency Total: </th>");
        out.println("<th></th>");
        
        String companyName = request.getParameter("companySearch");
        int amount = Integer.parseInt(request.getParameter("sharesAmount"));

        try {
            org.me.sharebroker.ShareBrokerWS_Service service = new org.me.sharebroker.ShareBrokerWS_Service();
            org.me.sharebroker.ShareBrokerWS port = service.getShareBrokerWSPort();
 
            java.util.List<org.me.sharebroker.Share> result = port.purchaseShares(amount, companyName);
            java.util.List<org.me.sharebroker.SharePrice> price;
        
           
           
            for (int x = 0; x < result.size();x++){
    
                if (result.get(x).getCompanyName().equals(companyName)) {
                    price = result.get(x).getSharePrice();
                    DecimalFormat df = new DecimalFormat("#.##");
                    
                    for (int y = 0; y < result.size();y++){
                        updatedCurrencyValue = df.format(price.get(y).getValue()*resultOfConversion);
                        
                        out.println("<tr class=\"sb-table-item\">");
                        out.print("<td>"+ companyName + "</td>");
                        out.print("<td>" + amount + "</td>");
                        out.print("<td>" + price.get(y).getValue() * amount + " GBP" + "</td>");
                        out.print("<td>" + Double.valueOf(updatedCurrencyValue)*amount + currencySelectionSplit +"</td>");
                        out.println("</tr>");
                     }
                }
            }
                
        } catch (Exception ex) {
           
        }
    
    }
    %>
     </table>

     <div class="w3-container w3-blue">

     <h4>Sell Shares</h4>
</div>
     

     <form>
        
        <input class="w3-input" type="text" name="companySearch" placeholder = "Enter Company Name">
        <input class="w3-input" type="text" name="companySymbol" placeholder = "Enter Symbol">
        <input class="w3-input" type="number" name="sharesAmount" placeholder = "Add Shares">
    
        <input class="w3-input" type="Number" name="shareValue" placeholder = "Enter share value">

        <input type="submit" name="sellShare" value="Sell Share">
     </form>
     <table class="sb-table">
     
      
    <%
  
    if(request.getParameter("sellShare") != null) {  
        
        
        out.println("<th>Symbol</th>"); 
        out.println("<th>Name</th>");
        out.println("<th>Available Shares</th>");
        out.println("<th>Value</th>");
        out.println("<th>Currency</th>");
        out.println("<th>Last Updated</th>");
        out.println("<th></th>");
        
        try {
            org.me.sharebroker.ShareBrokerWS_Service service = new org.me.sharebroker.ShareBrokerWS_Service();
            org.me.sharebroker.ShareBrokerWS port = service.getShareBrokerWSPort();
             // TODO initialize WS operation arguments here
            java.lang.String arg0 = request.getParameter("companySearch");
            java.lang.String arg1 = request.getParameter("companySymbol");
            int arg2 = Integer.parseInt(request.getParameter("sharesAmount"));
            double arg3 = Double.parseDouble(request.getParameter("shareValue"));
            
            // TODO process result here
            java.util.List<org.me.sharebroker.Share> result = port.addShares(arg0, arg1, arg2, arg3);
            java.util.List<org.me.sharebroker.SharePrice> price;
            
            
                for (int x = 0; x < result.size(); x++) {
                    out.println("<tr class=\"sb-table-item\">");
                    out.print("<td>" + "<img src='//logo.clearbit.com/"+
                            result.get(x).getCompanyName().toLowerCase()+
                            ".com?size=40'>"  +"</td>");
                    out.print("<td>" +  result.get(x).getCompanyName()+ "</td>");
                    out.print("<td>" + result.get(x).getAvailableShares()+ "</td>");

                    price = result.get(x).getSharePrice();

                    for (int y = 0; y < price.size(); y++) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        updatedCurrencyValue = df.format(price.get(y).getValue()*resultOfConversion);

                        out.print("<td>"+ updatedCurrencyValue +"</td>");
                        out.print("<td>" + currencySelectionSplit + "</td>");
                        out.print("<td>" + price.get(y).getLastUpdate()+ "</td>");
                        out.println("</tr>");
                    }
                }
            
            
            
            
            
            
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        
    }
    %>
   
     </table>
     
     
     
     
     
     
     
     
     
     
     
    </body>
</html>
