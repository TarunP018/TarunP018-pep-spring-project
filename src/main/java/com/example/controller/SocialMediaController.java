package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        try {
            accountService.registerUser(account);
            return ResponseEntity.ok(account);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Account authenticatedUser = accountService.loginUser(account.getUsername(), account.getPassword());
        if (authenticatedUser != null) {
            return ResponseEntity.ok(authenticatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{message_id}")
    public Message getMessageById(@PathVariable int message_id){
        return messageService.getMessageById(message_id);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable int message_id) {
        Message message = messageService.getMessageById(message_id);
        if (message != null) {
            messageService.deleteMessageById(message);
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }



    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            return ResponseEntity.ok(createdMessage);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> updateMessage(@RequestBody Message message , @PathVariable int message_id){
        Message m = messageService.getMessageById(message_id);
        if(m!=null && !message.getMessageText().isBlank() && message.getMessageText().length()<255 ){
            messageService.updateMessage(m, message);
            return ResponseEntity.ok().body(1);
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/accounts/{account_id}/messages")
    public List<Message> getAllMessagesFromUserAccountId(@PathVariable int account_id){
        return messageService.getAllMessagesFromUserAccountId(account_id);
    }



}