package com.caizicong.protobuf;

import java.io.*;

public class ProtobufTest {

    public static void main(String args[]) throws IOException {

        AddressBookProtos.Person.Builder personBuilder = AddressBookProtos.Person.newBuilder();
        personBuilder.setId(1);
        personBuilder.setName("John");
        personBuilder.setEmail("john@gmail.com");

        AddressBookProtos.Person.PhoneNumber.Builder phoneNumber = AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("110").setType(AddressBookProtos.Person.PhoneType.HOME);

        personBuilder.addPhones(phoneNumber);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        AddressBookProtos.Person personA = personBuilder.build();
        personA.writeTo(outputStream);

        // 写文件
//        personA.writeTo(new FileOutputStream("abc.bytes"));
        // 读文件
//        AddressBookProtos.Person personB = AddressBookProtos.Person.parseFrom(new FileInputStream("ab.bytes"));

        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        AddressBookProtos.Person personB = AddressBookProtos.Person.parseFrom(inputStream);

        System.out.println(personB.getId());
        System.out.println(personB.getName());
        System.out.println(personB.getEmail());

        for (AddressBookProtos.Person.PhoneNumber num : personB.getPhonesList()) {
            System.out.println(num.getType().toString() + " : " + num.getNumber());
        }

    }

}
