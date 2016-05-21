package com.example.andre.robotprojekt;

import java.util.UUID;


/**
 * Contains the UUIDs to read and write to the bluetooth hardware
 *
 * @author Andreas Gåhlin
 * @name Andreas Gåhlin
 * @email andreas.gåhlin@gmail.com
 * @version 1.0
 */
public class GattUUID {
    public final String DEVICE_NAME = "JavaKungen";

    public static UUID LOSS_LINK_SERVICE_UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb"); //00001803-0000-1000-8000-00805f9b34fb //LOss link
    public static UUID ALERT_LEVEL_CHARACTERISTIC = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb"); //00002a06-0000-1000-8000-00805f9b34fb
    public static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"); //do not touch
    //public static UUID DATA_CHARACTERISTIC_READ = UUID.fromString("dd89e7a9-b698-4a24-8e6d-7d8fb2ed77ba");

    public static UUID SERVICE = UUID.fromString("bc2f4cc6-aaef-4351-9034-d66268e328f0"); //do not touch
    public static UUID DATA_CHARACTERISTIC = UUID.fromString("06d1e5e7-79ad-4a71-8faa-373789f7d93c"); //do not touch
    public static UUID DATA_CHARACTERISTIC2 = UUID.fromString("dd89e7a9-b698-4a25-8e6d-7d8fb2ed77ba"); //do not touch
    public static UUID DATA_CHARACTERISTIC3 = UUID.fromString("6f0e9b56-e175-4243-a20a-71ebdb92fe74"); //do not touch
    public static UUID DATA_CHARACTERISTIC4 = UUID.fromString("eb718970-adca-11e3-aca6-425861b86ab6"); //do not touch
    public static UUID DATA_CHARACTERISTIC5 = UUID.fromString("f372624b-6e84-4851-9b5f-272f3350cbcd"); //do not touch
    public static UUID DATA_CHARACTERISTIC6 = UUID.fromString("818ae306-9c56-448d-b51a-7add6a5d314d"); //do not touch


    public static UUID SERVICE_1 = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    public static UUID SERVICE_1_CHARA_1 = UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb"); //success answer 5

    public static UUID SERVICE_2 = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    public static UUID SERVICE_2_CHARA_1 = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"); //device name
    public static UUID SERVICE_2_CHARA_2 = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb"); //?
    public static UUID SERVICE_2_CHARA_3 = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb"); //?

    public static UUID SERVICE_3 = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb"); //
    public static UUID SERVICE_3_CHARA_1 = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb"); //ans d

    public static UUID SERVICE_4 = UUID.fromString("00001803-0000-1000-8000-00805f9b34fb"); //nothing
    public static UUID SERVICE_4_CHARA_1 = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

    public static UUID SERVICE_5 = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb"); //nothing
    public static UUID SERVICE_5_CHARA_1 = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

    public static UUID SERVICE_6 = UUID.fromString("00001804-0000-1000-8000-00805f9b34fb"); //nothing
    public static UUID SERVICE_6_CHARA_1 = UUID.fromString("00002a07-0000-1000-8000-00805f9b34fb");

    public static UUID SERVICE_7 = UUID.fromString("9bc5d610-c57b-11e3-9c1a-0800200c9a66"); //no success
    public static UUID SERVICE_7_CHARA_1 = UUID.fromString("9bc5d612-c57b-11e3-9c1a-0800200c9a66"); //success ans 1
    public static UUID SERVICE_7_CHARA_2 = UUID.fromString("9bc5d611-c57b-11e3-9c1a-0800200c9a66"); //no success
    public static UUID SERVICE_7_CHARA_3 = UUID.fromString("9bc5d613-c57b-11e3-9c1a-0800200c9a66"); //no success

    public static UUID SERVICE_8 = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb"); //no success
    public static UUID SERVICE_8_CHARA_1 = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb"); //success ans 1
    public static UUID SERVICE_8_CHARA_2 = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb"); //no success
    public static UUID SERVICE_8_CHARA_3 = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb"); //no success
    public static UUID SERVICE_8_CHARA_4 = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb"); //success ans 1
    public static UUID SERVICE_8_CHARA_5 = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb"); //no success
    public static UUID SERVICE_8_CHARA_6 = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb"); //no success
    public static UUID SERVICE_8_CHARA_7 = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb"); //success ans 1
    public static UUID SERVICE_8_CHARA_8 = UUID.fromString("00002a50-0000-1000-8000-00805f9b34fb"); //no success

}

