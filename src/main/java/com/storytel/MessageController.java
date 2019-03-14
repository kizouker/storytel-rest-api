package com.storytel;

import java.util.concurrent.atomic.AtomicLong;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

// The Rest API

@RestController
@Api(description = "Set of endpoints for creating, updating, deleting and listing messages.")
public class MessageController {

    private final AtomicLong counter = new AtomicLong(); // Good for incremental updates

    @Resource (name = "applicationScopedBean")
    Messages applicationScopedBean; // A shared resource, the scope is over the whole app


    @RequestMapping(value = "/messageservice/1.0/post",
            method = RequestMethod.POST,
            consumes = {"text/plain"},
            produces = {"application/json"})
    @ApiOperation(value = "Create a message resource.", notes = "Returns a Json with the message and a generated ID")
    public Message post(@ApiParam(name = "The message provided in the payload/requestbody", required = true) @RequestBody String message) {
        long id = counter.incrementAndGet();
        Message msg = new Message(id, String.format(message));
        applicationScopedBean.setMessage(id, msg);
        return msg;
    }

    @RequestMapping(value = "/messageservice/1.0/get",
            method = RequestMethod.GET,
            consumes = {"application/x-www-form-urlencoded"},
            produces = {"application/json"})
    @ApiOperation(value = "Get a single message. You have to provide a valid message ID")
    public Message get(@ApiParam(name = "The ID of the Message.", required = true)
                           @RequestParam(value = "id") long id) {
        return applicationScopedBean.getText(id); //todo: return a differnt json object
    }

    @RequestMapping(value = "/messageservice/1.0/delete",
                method = RequestMethod.DELETE,
                consumes = {"application/x-www-form-urlencoded"},
                produces = {"application/json"})
    @ApiOperation(value = "Delete a message.", notes = "You have to provide a valid message ID in the url (ex: ?id=\"1\"). " +
            "Returned is a Json with the remaining messages")
        public String delete(@RequestParam(value = "id") long id) {
        return applicationScopedBean.removeMessage(id);
    }

    @RequestMapping(value = "/messageservice/1.0/put",
            method = RequestMethod.PUT,
            consumes = {"application/json"},
            produces = {"application/json"})
    @ApiOperation(value = "Update a message.", notes = "You have to provide a valid message ID and the message in the payload body.")
    public Message put(@ApiParam(value = "The ID of message,", required = true) @RequestBody long id,
                       @ApiParam(name = "The changed message.", required = true) @RequestBody String message){
        // public Message put(@RequestBody long id, @RequestBody String message){
        // public Message put(@RequestParam(value = "id") long id, @RequestParam(value = "message") String message){
        Message msg = new Message(id, String.format(message));
        applicationScopedBean.setMessage(id, msg);
        return msg;
    }

    @RequestMapping(value = "/messageservice/1.0/list",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation("List all available messages.")
    public String list(){ //todo: where should the exception be caught in app? or where?
        return applicationScopedBean.listMessages();
    }


    /* TODO: add exception handling - what error msg does the client get? is it useful? */
/**
    @ExceptionHandler({Exception.class})
    public  handleException(){
        //
    }




    /**
     *
     * HTTP Verb	CRUD	Entire Collection (e.g. /customers)	Specific Item (e.g. /customers/{id})
     *
     * POST	Create	201 (Created), 'Location' header with link to /customers/{id} containing new ID.	404 (Not Found), 409 (Conflict) if resource already exists..
     * GET	Read	200 (OK), list of customers. Use pagination, sorting and filtering to navigate big lists.	200 (OK), single customer. 404 (Not Found), if ID not found or invalid.
     * PUT	Update/Replace	405 (Method Not Allowed), unless you want to update/replace every resource in the entire collection.	200 (OK) or 204 (No Content). 404 (Not Found), if ID not found or invalid.
     * PATCH	Update/Modify	405 (Method Not Allowed), unless you want to modify the collection itself.	200 (OK) or 204 (No Content). 404 (Not Found), if ID not found or invalid.
     * DELETE	Delete	405 (Method Not Allowed), unless you want to delete the whole collection—not often desirable.	200 (OK). 404 (Not Found), if ID not found or invalid.
     */



}