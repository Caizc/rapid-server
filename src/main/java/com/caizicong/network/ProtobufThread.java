package com.caizicong.network;

import com.caizicong.protobuf.AddressBookProtos;

import java.io.*;
import java.net.Socket;

public class ProtobufThread extends Thread {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ProtobufThread(Socket socket) {
        this.socket = socket;

        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (true) {
            try {

                if (socket.isClosed()) {
                    System.out.println("One of the connections has been closed!");
                    break;
                }

                if (dataInputStream.available() == 0) {
                    continue;
                }

                // 前 4 个字节定义了数据包的长度
                byte[] byteslen = new byte[4];
                dataInputStream.read(byteslen, 0, byteslen.length);
                int length = ByteUtils.byteArray2Int(byteslen);

                // 根据数据包的长度读取输入流
                byte[] inputBytes = new byte[length];
                int count = 0;
                while (count < length) {
                    int tempLength = dataInputStream.read(inputBytes);
                    count += tempLength;
                }

                ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

                // 解码 Protobuf 消息
                AddressBookProtos.Person personB = AddressBookProtos.Person.parseFrom(inputStream);

                System.out.println("====== Protocol Buffers Message send from client: ======");
                System.out.println(personB.getId());
                System.out.println(personB.getName());
                System.out.println(personB.getEmail());

                for (AddressBookProtos.Person.PhoneNumber num : personB.getPhonesList()) {
                    System.out.println(num.getType().toString() + " : " + num.getNumber());
                }

                // 向客户端发送回应的 Protobuf 消息
                sendResponse();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            // 关闭数据流和 Socket 连接
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向客户端发送回应的 Protobuf 消息
     */
    private void sendResponse() {

        AddressBookProtos.Person.Builder personBuilder = AddressBookProtos.Person.newBuilder();
        personBuilder.setId(1);
        personBuilder.setName("John");
        personBuilder.setEmail("john@gmail.com");
        AddressBookProtos.Person.PhoneNumber.Builder phoneNumber = AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("110").setType(AddressBookProtos.Person.PhoneType.HOME);
        personBuilder.addPhones(phoneNumber);

        AddressBookProtos.Person personA = personBuilder.build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            personA.writeTo(outputStream);

            byte[] bytes = outputStream.toByteArray();

            int byteLength = bytes.length;

            // 发送 Protobuf 消息前先发送内容的长度
            this.dataOutputStream.write(ByteUtils.int2ByteArray(byteLength),0,4);
            // 发送 Protobuf 消息内容
            this.dataOutputStream.write(bytes);

            System.out.println("Protobuf Message has sended to Client.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
