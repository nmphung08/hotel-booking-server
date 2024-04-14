package com.nmphung.hotelbooking.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Component
public class CommonUtils {
    public Blob multipartToBlob(MultipartFile file) throws IOException, SQLException {
        byte[] fileBytes = file != null && !file.isEmpty() ? file.getBytes() : null;
        Blob fileBlob = fileBytes != null && fileBytes.length > 0 ? new SerialBlob(fileBytes) : null;
        return fileBlob;
    }

    public String blobToString(Blob file) throws SQLException {
        byte[] fileBytes = file != null && file.length() > 0 ? file.getBytes(1, (int) file.length()) : null;
        String fileEncodedString = fileBytes != null && fileBytes.length > 0 ? Base64.encodeBase64String(fileBytes) : null;
        return fileEncodedString;
    }

    public Blob stringToBlob(String fileStr) throws SQLException {
        byte[] fileBytes = fileStr != null && fileStr.length() > 0 ? Base64.decodeBase64(fileStr) : null;
        Blob fileBlob = fileBytes != null && fileBytes.length > 0 ? new SerialBlob(fileBytes) : null;
        return fileBlob;
    }
}
