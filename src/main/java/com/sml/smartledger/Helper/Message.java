package com.sml.smartledger.Helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Message {

    String Content;
    MessageType type = MessageType.blue;
}
