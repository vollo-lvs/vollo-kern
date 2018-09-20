package nl.vollo.kern.rest;

public class ResponseCorsFilter  { //implements ContainerResponseFilter {

//    @Override
//    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
//        containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
//        containerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
//
//        String reqHead = containerRequestContext.getHeaderString("Access-Control-Request-Headers");
//
//        if (null != reqHead && !reqHead.equals("")){
//            containerResponseContext.getHeaders().add("Access-Control-Allow-Headers", reqHead);
//        }
//    }
}