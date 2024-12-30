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



    public String createUser(String name, String mobile) {
        if (userMobile.contains(mobile)){
            return "User already exists";
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
}
