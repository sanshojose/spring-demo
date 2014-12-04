package com.rogers.sample.rest;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.libs.F;
import play.libs.ws.*;


/**
 *
 * @param <A> accepts type

 */

public class AsyncRestClient<A> {

    private static final Logger.ALogger logger = Logger.of(com.rogers.sample.rest.AsyncRestClient.class);
    private final String endpoint;

    /**
     *
     */
    public AsyncRestClient(String endpoint) {

        this.endpoint = endpoint;

        }


    /**
     * Make the WS call and parse the response into Json
     * @return
     */

    public F.Promise<JsonNode> makeRequest(A parameterObject) {
        logger.info("Inside AsyncRestClient.makeRequest()");
        WSRequestHolder request = WS.url(endpoint);
        logger.info("AsyncRestClient.makeRequest()::Hitting the echo WS at : "+ endpoint);

        F.Promise<JsonNode> responsePromise = request.get().map(new F.Function<WSResponse, JsonNode>() {
            @Override
            public JsonNode apply(WSResponse wsResponse) throws Throwable {
                logger.info("AsyncRestClient.makeRequest()::Got response");
                return wsResponse.asJson();
            }
        });

        return responsePromise;

    }


}
