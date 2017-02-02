/**
 * 
 */
package com.rayzr522.requestit.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Rayzr
 *
 */
public class Request {

    private String request;
    private UUID requester;
    private int status;

    private Request(String request, UUID requester, int status) {
        this.request = request;
        this.requester = requester;
        this.status = status;
    }

    public Request(String request, UUID requester) {
        this(request, requester, 0);
    }

    /**
     * @return the request
     */
    public String getRequest() {
        return request;
    }

    public String getShortDescription() {
        return request.length() > 25 ? request.substring(0, 22) + "..." : request;
    }

    /**
     * @return the requester
     */
    public UUID getRequester() {
        return requester;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    public boolean is(RequestState state) {
        return state.getStatusCode() == status;
    }

    public RequestState getState() {
        return RequestState.getState(status);
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @param viewed
     */
    public void setState(RequestState viewed) {
        this.setStatus(viewed.getStatusCode());
    }

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("request", request);
        data.put("requester", requester.toString());
        data.put("status", status);
        return data;
    }

    public static Request deserialize(Map<String, Object> data) {
        String request = (String) data.get("request");
        UUID requester = UUID.fromString(data.get("requester").toString());
        int status = (int) data.get("status");

        return new Request(request, requester, status);
    }

}
