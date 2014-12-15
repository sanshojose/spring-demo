package com.rogers.sample.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.rogers.sample.profile.SelfRegistrationRequestMessage;
import play.Logger;
import play.libs.F;
import play.libs.ws.*;


/**
 * Creates a client to handle the WS request-response
 * @param <A> accepts type
 *
 */

public class AsyncRestClient<A> {

    private static final Logger.ALogger logger = Logger.of(com.rogers.sample.rest.AsyncRestClient.class);
    private final String endpoint;

    public AsyncRestClient(String endpoint) {
        this.endpoint = endpoint;
        }


    /**
     * Make the WS call and parse the response into Json
     * @return F.Promise<JsonNode>
     */

    public F.Promise<JsonNode> makeRequest(A parameterObject) {

        logger.info("Inside AsyncRestClient.makeRequest()");
        //TODO : implement helper classes to convert generic parameterObject to WS request
        //TODO : implement helper classes to convert the WS response
        SelfRegistrationRequestMessage reqObj = (SelfRegistrationRequestMessage)parameterObject;
        WSRequestHolder request = WS.url(endpoint+"/name/"+reqObj.getName()+"/email/"+reqObj.getEmail().trim());
        logger.info("AsyncRestClient.makeRequest()::Hitting the echo WS at : "+ endpoint);
        JsonNode req = play.libs.Json.newObject().put("key1", "value1");
        F.Promise<JsonNode> responsePromise = request.post(req).map(new F.Function<WSResponse, JsonNode>() {
            @Override
            public JsonNode apply(WSResponse wsResponse) throws Throwable {

                JsonNode json = wsResponse.asJson();
                logger.info("AsyncRestClient.makeRequest()::Got response : "+json);

                return json;
            }
        });

        return responsePromise;

    }


}
