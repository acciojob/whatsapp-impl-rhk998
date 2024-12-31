package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();

        this.customGroupCount = 0;
        this.messageId = 0;
    }



    public String createUser(String name, String mobile) throws Exception{
        if (userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }

        User user = new User(name,mobile);
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users){
        int size = users.size();
        if(size==2){
            String groupName = users.get(1).getName();
            Group personalchat = new Group(groupName, 2);
            addGroup(personalchat,users,users.get(0));
            return personalchat;
        }else{
            customGroupCount++;
            String groupName = "Group " + customGroupCount;
            Group group = new Group(groupName,size);
            addGroup(group, users, users.get(0));
            return group;
        }
    }
    public void addGroup(Group group, List<User> users, User admin) {
        groupUserMap.put(group, users);
        adminMap.put(group, admin);
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId,content,new Date());
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {

            if(!groupUserMap.containsKey(group)){
                throw new Exception("Group does not exist");
            }

            List<User> users = groupUserMap.get(group);
            if(!users.contains(sender)){
                throw new Exception("You are not allowed to send message");
            }
            List<Message> mssgs = groupMessageMap.getOrDefault(group,new ArrayList<>());
            mssgs.add(message);
            groupMessageMap.put(group,mssgs);

            senderMap.put(message,sender);

            return mssgs.size();
    }

    public String findMessage(Date start, Date end, int k) throws Exception {

        List<Message> filteredMessages = new ArrayList<>();

        for(List<Message>  mssgs : groupMessageMap.values()){
            for(Message message : mssgs){
                if (message.getTimestamp().after(start) && message.getTimestamp().before(end)) {
                    filteredMessages.add(message);
                }
            }
        }
        filteredMessages.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));
        if (filteredMessages.size() < k) {
            throw new Exception("K is greater than the number of messages");
        }
        return filteredMessages.get(k - 1).getContent();
    }
}
