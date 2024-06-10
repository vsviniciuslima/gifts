package dev.vsviniciuslima.whatsapp.messages.model;

public record SendMessage(String content, long from, String to) {

    public String to() {
//        if(to.startsWith("55") && to.length() == 13)
            return to;

    }
}
