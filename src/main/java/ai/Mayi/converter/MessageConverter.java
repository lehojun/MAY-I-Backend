package ai.Mayi.converter;

import ai.Mayi.domain.Chat;
import ai.Mayi.domain.Message;
import ai.Mayi.domain.enums.MessageType;
import ai.Mayi.web.dto.ChatDTO;
import ai.Mayi.web.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageConverter {
    public MessageDTO.BardContents toBardContents(Message message){
        List<MessageDTO.BardContents.Parts> partsList = new ArrayList<>();
        partsList.add(MessageDTO.BardContents.Parts.builder().text(message.getText()).build());

        String role = "model";
        if(message.getMessageType() == MessageType.USER){
            role = "user";
        }

        return MessageDTO.BardContents.builder().parts(partsList).role(role).build();
    }
}
