package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int message_id) {
        Message m = messageRepository.getMessageByMessageId(message_id);
        return m;
    }

    public void deleteMessageById(Message message) {
        messageRepository.delete(message);
    }


    public Message createMessage(Message message) {
        Optional<Account> optionalUser = accountRepository.findById(message.getPostedBy());
        if (optionalUser.isPresent() && !message.getMessageText().isBlank() && message.getMessageText().length() <= 255) {
            return messageRepository.save(message);
        }
        return null;
    }

    public void updateMessage(Message m, Message message){
        m.setMessageText(message.getMessageText());
        m.setPostedBy(message.getPostedBy());
        m.setTimePostedEpoch(message.getTimePostedEpoch());
        messageRepository.save(m);
    }

    public List<Message> getAllMessagesFromUserAccountId(int account_id){
        List<Message> l = messageRepository.findAllByPostedBy(account_id);
        return l;
    }


}