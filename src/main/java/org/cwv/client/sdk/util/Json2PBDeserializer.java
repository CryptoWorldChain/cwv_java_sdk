package org.cwv.client.sdk.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.protobuf.Message;

import java.lang.reflect.Type;

public class Json2PBDeserializer<E extends Message> implements JsonDeserializer<Message> {
    @Override
    public E deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        
        
        return null;
    }
}
