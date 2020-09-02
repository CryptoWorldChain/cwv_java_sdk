//package org.cwv.client.sdk.util;
//
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.google.protobuf.MessageOrBuilder;
//import com.google.protobuf.util.JsonFormat;
//
//import java.lang.reflect.Type;
//
//public class PB2JsonSerializer implements JsonSerializer<MessageOrBuilder> {
//
//    @Override
//    public JsonElement serialize(MessageOrBuilder message, Type type, JsonSerializationContext jsonSerializationContext) {
//
//        try {
//            String str = JsonFormat.printer().print( message);
//            System.out.println("--json---"+str);
//            return new JsonParser().parse(str);
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }
//    
//    
//}
