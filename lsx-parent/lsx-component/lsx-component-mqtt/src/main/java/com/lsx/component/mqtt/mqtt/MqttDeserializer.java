package com.lsx.component.mqtt.mqtt;

import org.springframework.core.serializer.Deserializer;
import org.springframework.integration.ip.tcp.serializer.SoftEndOfStreamException;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MqttDeserializer implements Deserializer<byte[]> {


    private int initialBufferSize = 32;//默认大小

    @Override
    public byte[] deserialize(InputStream inputStream) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream(this.initialBufferSize);
        if (StreamUtils.copy(inputStream, out) == 0) {
            throw new SoftEndOfStreamException("Stream closed with no data");
        }
        out.close();
        return out.toByteArray();
    }



}
