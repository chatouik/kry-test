package se.kry.codetest;

import java.util.Date;

public class Service {

    String name;
    String status;
    String creactionDate;

    public Service(String name, String status) {
        this(name,status, null);
    }

    public Service(String name, String status, String creationDate) {
        this.name = name;
        this.status = status;
        if (creationDate == null) {
            this.creactionDate = new Date().toString();
        } else {
            this.creactionDate = creationDate;
        }
    }
}
